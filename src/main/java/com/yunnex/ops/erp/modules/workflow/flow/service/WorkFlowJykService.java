package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.modules.act.dao.ActDao;
import com.yunnex.ops.erp.modules.act.entity.Act;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitGoodService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpPayIntopieces;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.service.ErpTeamUserService;
import com.yunnex.ops.erp.modules.workflow.flow.common.WorkFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SubTask;
import com.yunnex.ops.erp.modules.workflow.flow.from.FlowForm;
import com.yunnex.ops.erp.modules.workflow.flow.from.FlowStatForm;
import com.yunnex.ops.erp.modules.workflow.flow.from.WorkFlowQueryForm;
import com.yunnex.ops.erp.modules.workflow.flow.strategy.IFunction;

/**
 * 任务流处理信息
 * 
 * @author yunnex
 * @date 2017年10月31日
 */
@Service
public class WorkFlowJykService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowJykService.class);

    private static final String SUSPENDED_TASK_NAME = "已暂停订单任务，与商户沟通启动服务";
    @Autowired
    private ErpOrderSplitGoodService erpOrderSplitGoodService;
    @Autowired
    private ErpHolidaysService erpHolidaysService;
    @Autowired
    private ActDao actDao;
    @Autowired
    private ErpTeamUserService erpTeamUserService;
    /**
     * 我的任务列表
     *
     * @param queryForm
     * @param detailType
     * @param followTaskDetailWrap
     * @return
     * @throws ParseException
     * @date 2018年6月4日
     * @author zjq
     */
    public List<FlowForm> getTaskList(WorkFlowQueryForm queryForm, String detailType, String followTaskDetailWrap) throws ParseException {
        String[] orderNo = splitOrderNum(queryForm);
        queryForm.setAssignee(UserUtils.getUser().getId());
        LOGGER.info("我的任务请求参数：{}", queryForm);
        List<FlowForm> todoTasks = actDao.findTodoTasks(queryForm);
        int total = actDao.findTodoTaskCount(queryForm);
        queryForm.setTotal(total);
        restoreOrderNum(queryForm, orderNo);
        // 封装表单数据
        List<FlowForm> result = new LinkedList<FlowForm>();
        
        for (FlowForm form : todoTasks) {
            wrapJykFlowForm(result, form, detailType, followTaskDetailWrap);
        }
        
        LOGGER.info("我的任务返回结果：{}", result);
        return result;
    }
    

    /**
     * 我的团队任务列表
     *
     * @param workFlowQueryForm
     * @param string
     * @param string2
     * @return
     * @date 2018年6月6日
     * @author zjq
     * @throws ParseException
     */
    public List<FlowForm> getTeamTasks(WorkFlowQueryForm queryForm, String detailType, String followTaskDetailWrap) throws ParseException {

        if (null != queryForm.getTeamUserId() && !"".equals(queryForm.getTeamUserId())) {
            wrapTeamUserIds(queryForm);
            queryForm.getUserIds().clear();
            List<String> list = new ArrayList<String>();
            list.add(queryForm.getTeamUserId());
            queryForm.setUserIds(list);
        } else {
            wrapTeamUserIds(queryForm);
        }

        String[] orderNo = splitOrderNum(queryForm);
        queryForm.setAssignee(UserUtils.getUser().getId());
        LOGGER.info("我的团队任务请求参数：{}", queryForm);
        List<FlowForm> tasks = actDao.findTeamTasks(queryForm);
        int total = actDao.findTeamTaskCount(queryForm);
        queryForm.setTotal(total);
        restoreOrderNum(queryForm, orderNo);

        List<FlowForm> result = new LinkedList<FlowForm>();

        for (FlowForm form : tasks) {
            Act act = form.getAct();
            act.setStatus("todo");

            if (act.getProcDefKey().startsWith(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0])) {
                wrapJykFlowForm(result, form, detailType, followTaskDetailWrap);
            } else if (act.getProcDefKey().startsWith(ActUtils.SHOP_DATA_INPUT_FLOW[0])) {
                wrapSdiForm(result, form, detailType, followTaskDetailWrap);
            } else if (act.getProcDefKey().startsWith(ActUtils.WECHAT_PAY_INTOPIECES_FLOW[0]) || act.getProcDefKey()
                            .startsWith(ActUtils.UNION_PAY_INTOPIECES_FLOW[0])) {
                wrapPayIntopieces(result, form, detailType, followTaskDetailWrap);
            }
        }

        LOGGER.info("我的团队任务返回结果：{}", result);
        return result;
    }


    /**
     * 获取团队成员
     *
     * @param queryForm
     * @date 2018年1月15日
     */
    private void wrapTeamUserIds(WorkFlowQueryForm queryForm) {
        // 获取当前用户管理的所有团队
        Principal principal = UserUtils.getPrincipal();
        List<ErpTeam> adminTeams = erpTeamUserService.findAdminTeams(principal.getId(), null);
        if (CollectionUtils.isEmpty(adminTeams)) {
            throw new AuthenticationException("你不是团队管理员，无法查看团队任务！");
        }
        queryForm.setTeams(adminTeams);

        // 获得团队成员
        List<String> teamIds = new ArrayList<String>();
        String teamId = queryForm.getTeamId();
        if (StringUtils.isNotBlank(teamId) && !"1".equals(teamId)) { // 指定团队成员
            for (ErpTeam team : adminTeams) {
                if (teamId.equals(team.getId())) {
                    teamIds.add(team.getId());
                    break;
                }
            }
        } else {
            for (ErpTeam team : adminTeams) {
                teamIds.add(team.getId());
            }
        }
        List<String> teamUserIds = erpTeamUserService.findTeamUserIds(teamIds);
        queryForm.setUserIds(teamUserIds);
    }


    /**
     * 
     * 我的关注列表
     * 
     * @param act
     * @param orderId 订单编号（支持模糊 查询）
     * @param taskStateList 任务状态列表
     * @param hurryFlag 订单加急标记
     * @param shopName 商户名称（支持模糊查询）
     * @return
     * @throws ParseException
     */
    public List<FlowForm> getFollowTaskList(WorkFlowQueryForm queryForm, String detailType, String followTaskDetailWrap) throws ParseException {
        String[] orderNo = splitOrderNum(queryForm);
        queryForm.setAssignee(UserUtils.getUser().getId());
        LOGGER.info("我的关注请求参数：{}", queryForm);
        List<FlowForm> followTasks = actDao.findFollowTasks(queryForm);
        int total = actDao.findFollowTaskCount(queryForm);
        queryForm.setTotal(total);
        restoreOrderNum(queryForm, orderNo);

        List<FlowForm> result = new LinkedList<FlowForm>();

        for (FlowForm form : followTasks) {
            Act act = form.getAct();
            act.setStatus("todo");

            if (act.getProcDefKey().startsWith(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0])) {
                wrapJykFlowForm(result, form, detailType, followTaskDetailWrap);
            }
        }
        LOGGER.info("我的关注返回结果：{}", result);
        return result;
    }


    public Map<String, Object> getTeamTaskStat(WorkFlowQueryForm workFlowQueryForm) throws ParseException {
        wrapTeamUserIds(workFlowQueryForm);
        splitOrderNum(workFlowQueryForm);
        workFlowQueryForm.setAssignee(UserUtils.getUser().getId());
        workFlowQueryForm.setIsPage(0);
        List<FlowForm> tasks = actDao.findTeamTasks(workFlowQueryForm);
        Map<String, Object> map = calcStat(tasks);
        return map;
    }

    private Map<String, Object> calcStat(List<FlowForm> tasks) {
        if (CollectionUtils.isEmpty(tasks)) {
            return null;
        }
        Map<String, Object> map = Maps.newHashMap();
        FlowStatForm flowStatForm = new FlowStatForm();
        List<FlowForm> result = new ArrayList<FlowForm>();
        for (FlowForm form : tasks) {
            Act act = form.getAct();
            if (act.getProcDefKey().startsWith(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0])) {
                form.setOrderNo(form.getOrderOriginalInfo().getOrderNumber() + "-" + form.getOrderSplitInfo().getSplitId());
                isOrderExists(result, form);
                act.setTaskState(form.getTaskExt().getStatus());
            } else if (ActUtils.SHOP_DATA_INPUT_FLOW[0].equals(act.getProcDefKey())) {
                form.setOrderNo(form.getOrderOriginalInfo().getOrderNumber());
                isOrderExists(result, form);
                act.setTaskState(form.getTaskExt().getStatus());
            } else if (ActUtils.WECHAT_PAY_INTOPIECES_FLOW[0].equals(act.getProcDefKey()) || ActUtils.UNION_PAY_INTOPIECES_FLOW[0]
                            .equals(act.getProcDefKey())) {
                form.setOrderNo(form.getPayInto().getIntopiecesName());
                isOrderExists(result, form);
                act.setTaskState(WorkFlowConstants.NORMAL);
            }

            if (WorkFlowConstants.NORMAL.equals(act.getTaskState())) {
                // 正常
                flowStatForm.setTaskNormal(flowStatForm.getTaskNormal() + 1);
            } else if (WorkFlowConstants.ONLY_WILL_OVER_TIME.equals(act.getTaskState())) {
                // 既将到期
                flowStatForm.setTaskOnlyWillOvertime(flowStatForm.getTaskOnlyWillOvertime() + 1);
            } else if (WorkFlowConstants.OVER_TIME.equals(act.getTaskState())) {
                // 超时
                flowStatForm.setTaskOverTime(flowStatForm.getTaskOverTime() + 1);
            }
        }

        map.put("orderCount", result.size());
        map.put("flowStatForm", flowStatForm);
        return map;
    }

    private static void isOrderExists(List<FlowForm> result, FlowForm form) {
        boolean isExists = true;
        for (FlowForm flowform : result) {
            if (StringUtils.isNotBlank(flowform.getOrderNo()) && flowform.getOrderNo().equals(form.getOrderNo())) {
                isExists = false;
                break;
            }
        }
        if (isExists)
            result.add(form);
    }

    public List<FlowForm> getPendingProductionTaskListNew(WorkFlowQueryForm queryForm, String detailType, String followTaskDetailWrap)
                    throws ParseException {
        String[] orderNo = splitOrderNum(queryForm);
        queryForm.setAssignee(UserUtils.getUser().getId());
        LOGGER.info("我的待生产库请求参数：{}", queryForm);
        List<FlowForm> followTasks = actDao.findPendingProductionTasks(queryForm);
        int total = actDao.findPendingProductionTaskCount(queryForm);
        queryForm.setTotal(total);
        restoreOrderNum(queryForm, orderNo);

        List<FlowForm> result = new LinkedList<FlowForm>();

        for (FlowForm form : followTasks) {
            Act act = form.getAct();
            act.setStatus("todo");

            if (act.getProcDefKey().startsWith(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0])) {
                wrapJykFlowForm(result, form, detailType, followTaskDetailWrap);
            }
        }

        LOGGER.info("我的待生产库返回结果：{}", result);
        return result;
    }

    private void wrapJykFlowForm(List<FlowForm> result, FlowForm form, String detailType, String followTaskDetailWrap) throws ParseException {

        form.setFlowMark(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0]);
        // 处理暂停任务
        processSuspendedTask(form);
        Act act = form.getAct();
        ErpOrderOriginalInfo order = form.getOrderOriginalInfo();
        ErpOrderSplitInfo orderSplit = form.getOrderSplitInfo();
        TaskExt taskExt = form.getTaskExt();
        // 填充订单信息
        fillOrderInfo(form, act, order, orderSplit, taskExt);
        // 填充任务进度
        fillTaskInfo(form, detailType, followTaskDetailWrap, act, orderSplit, taskExt);
        // 任务分组,合并
        IFunction<FlowForm, FlowForm> function = (original, temp) -> StringUtils.isNotBlank(original.getOrderNo()) && original.getOrderNo()
                        .equals(temp.getOrderNo()) && original.getAct().getProcDefKey().equals(temp.getAct().getProcDefKey());
        if (null != orderSplit)
        {
            taskGroup(result, form, orderSplit.getId(), true, act, function);
        }
    }

    private void wrapPayIntopieces(List<FlowForm> result, FlowForm form, String detailType, String followTaskDetailWrap) {
        form.setFlowMark("payInto_flow");
        Act act = form.getAct();

        ErpPayIntopieces payInto = form.getPayInto();
        ErpShopInfo shop = form.getShop();
        form.setOrderNo(payInto.getIntopiecesName());
        form.setOrderTime(payInto.getCreateDate());
        String name = ActUtils.WECHAT_PAY_INTOPIECES_FLOW[0].equals(act.getProcDefKey()) ? "微信支付进件" : "银联支付进件";
        form.setGoodName(name);
        form.setShopName(shop.getName());
        form.setAgentName(shop.getServiceProvider());
        form.setTaskConsumTime(0);

        if (act.getTaskStarterDate() != null) {
            form.setTaskStartDate(DateUtils.formatDate(act.getTaskStarterDate(), "MM-dd HH:mm"));
        }
        if (act.getTaskBusinessEndDate() != null) {
            form.setTaskEndDate(DateUtils.formatDate(act.getTaskBusinessEndDate(), "MM-dd HH:mm"));
        } else {
            form.setTaskEndDate("未指定");
        }
        if (act.getTaskConsumTime() != null) {
            form.setTaskConsumTime(act.getTaskConsumTime());
        }
        form.setTaskConsumTime(0);
        
        User user = UserUtils.get(act.getAssignee());
        form.setAssignee(user.getName());
        
        IFunction<FlowForm, FlowForm> function = (original,
                        temp) -> StringUtils.isNotBlank(temp.getPayIntopiecesId()) && original.getPayIntopiecesId().equals(temp.getPayIntopiecesId());
        taskGroup(result, form, StringUtils.EMPTY, true, act, function);
    }


    private void wrapSdiForm(List<FlowForm> result, FlowForm form, String detailType, String followTaskDetailWrap) throws ParseException {
        form.setFlowMark("sdi_flow");
        Act act = form.getAct();
        act.setTaskState(WorkFlowConstants.NORMAL);
        ErpOrderOriginalInfo order = form.getOrderOriginalInfo();
        form.setOrderNo(order.getOrderNumber());
        form.setOrderTime(order.getBuyDate());
        form.setGoodName("商户资料录入与进件");
        form.setNum(null);
        form.setGoodType("");
        form.setGoodCount(null);
        form.setShopName(order.getShopName());
        form.setDeliveryTime(null);
        form.setHurryFlag(null);
        form.setAgentName(order.getAgentName());
        form.setOrderType(String.valueOf(order.getOrderType()));
        form.setTaskConsumTime(0);
        TaskExt taskExt = form.getTaskExt();
        fillTaskInfo(form, detailType, followTaskDetailWrap, act, null, taskExt);
        // 任务分组,合并
        IFunction<FlowForm, FlowForm> function = (original, temp) -> StringUtils.isNotBlank(original.getOrderNo()) && original.getOrderNo()
                        .equals(temp.getOrderNo()) && original.getAct().getProcDefKey().equals(temp.getAct().getProcDefKey());
        taskGroup(result, form, StringUtils.EMPTY, true, act, function);
    }

    // 还原订单号以及序号，以进行查询表单的回显
    public void restoreOrderNum(WorkFlowQueryForm workFlowQueryForm, String[] orderNo) {
        if (orderNo != null && orderNo.length == 2)
            workFlowQueryForm.setOrderNumber(orderNo[0] + "-" + orderNo[1]);
    }

    private void processSuspendedTask(FlowForm form) {
        ErpOrderSplitInfo orderSplitInfo = form.getOrderSplitInfo();
        if (orderSplitInfo != null) {
            String suspendFlag = orderSplitInfo.getSuspendFlag();
            if (Constant.YES.equals(suspendFlag)) {
                // 修改任务名称
                Act act = form.getAct();
                act.setTaskName(SUSPENDED_TASK_NAME);
            }
        }
    }

    private void taskGroup(List<FlowForm> result, FlowForm form, String taskStr, boolean isExists, Act act, IFunction<FlowForm, FlowForm> fun) {
        for (FlowForm flowform : result) {
            // 如果已经存在，则添回到进旧的flowFrom中
            if (fun.group(flowform, form)) {
                // 比较大小
                if (flowform.getTaskConsumTime() < form.getTaskConsumTime()) {
                    flowform.setTaskConsumTime(form.getTaskConsumTime());
                }

                flowform.getSubTaskStrList().add(new SubTask(act.getTaskId(), act.getTaskName(), form.getTaskStartDate(), form.getTaskEndDate(),
                                form.getTaskConsumTime(), form.getAssignee(), act.getAssignee(), act.getTaskDefKey(), taskStr, act.getProcInsId()));
                //排序
                Collections.sort(flowform.getSubTaskStrList(), new Comparator<SubTask>() {  
                    public int compare(SubTask o1,   SubTask o2) {  
                        return (o2.getSubTaskConsumTime() - o1.getSubTaskConsumTime() );  
                    }  
                }); 
              
                isExists = false;
            }
        }

        // 不存在，新建
        if (isExists) {
            form.getSubTaskStrList().add(new SubTask(act.getTaskId(), act.getTaskName(), form.getTaskStartDate(), form.getTaskEndDate(),
                            form.getTaskConsumTime(), form.getAssignee(), act.getAssignee(), act.getTaskDefKey(), taskStr, act.getProcInsId()));
            result.add(form);
        }
    }

    private void fillOrderInfo(FlowForm form, Act act, ErpOrderOriginalInfo order, ErpOrderSplitInfo orderSplit, TaskExt taskExt) {

        if (null != orderSplit) {
            act.setTaskState(taskExt.getStatus());
            if (null != orderSplit && orderSplit.getSplitId() == 0)
                form.setOrderNo(order.getOrderNumber());
            else
                form.setOrderNo(order.getOrderNumber() + "-" + orderSplit.getSplitId());
            form.setOrderTime(order.getBuyDate());
            form.setTaskDisplay(erpOrderSplitGoodService.getServiceAndNumAndType(orderSplit.getId()));
            form.setGoodName(orderSplit.getGoodName());
            form.setNum(orderSplit.getNum());
            form.setGoodType(orderSplit.getGoodTypeName());
            form.setGoodCount(String.valueOf(orderSplit.getNum()));
            form.setShopName(order.getShopName());
            form.setHurryFlag(orderSplit.getHurryFlag());
            form.setTimeoutFlag(orderSplit.getTimeoutFlag());
            form.setAgentName(order.getAgentName());
            form.setOrderType(String.valueOf(order.getOrderType()));
            form.setDeliveryTime(orderSplit.getPromotionTime());
            form.setTaskConsumTime(0);
        }

        if ("1".equals(form.getOrderType())) {
            form.setContactWay(order.getContactName() + " / " + order.getContactNumber());
        } else {
            String promoteContact = order.getPromoteContact() != null && !"".equals(order.getPromoteContact()) ? order.getPromoteContact() : "--";
            String promotePhone = order.getPromotePhone() != null && !"".equals(order.getPromotePhone()) ? order.getPromotePhone() : "--";
            form.setContactWay(promoteContact + " / " + promotePhone);
        }
    }

    // 拆分订单号以及序号
    public String[] splitOrderNum(WorkFlowQueryForm workFlowQueryForm) {
        String[] orderNo = null;
        if (workFlowQueryForm.getOrderNumber() != null) {
            orderNo = workFlowQueryForm.getOrderNumber().split("-");
            workFlowQueryForm.setOrderNumber(orderNo[0]);
        }
        return orderNo;
    }

    protected void fillTaskInfo(FlowForm form, String detailType, String followTaskDetailWrap, Act act, ErpOrderSplitInfo orderSplit, TaskExt taskExt)
                    throws ParseException {
        int taskHours = 0;
        if (null != orderSplit) {
            taskHours = orderSplit.getHurryFlag() == ErpOrderSplitInfo.HURRY_FLAG_YES ? act.getUrgentTaskDateHours() : act.getTaskDateHours();
        } else {
            taskHours = act.getTaskDateHours();
        }
        Date enddate = erpHolidaysService.enddate(act.getTaskStarterDate(), taskHours);
        act.setTaskConsumTime(computingTaskSchedule(act.getTaskStarterDate(), taskHours));
        try {
            act.setTaskBusinessEndDate(enddate);
        } catch (Exception e) {
            act.setTaskBusinessEndDate(null);
        }
        
        if (act.getTaskStarterDate() != null) {
            form.setTaskStartDate(DateUtils.formatDate(act.getTaskStarterDate(), "MM-dd HH:mm"));
        }
        if (act.getTaskBusinessEndDate() != null && Constant.NO.equals(taskExt.getPendingProdFlag())) {
            form.setTaskEndDate(DateUtils.formatDate(act.getTaskBusinessEndDate(), "MM-dd HH:mm"));
        } else {
            form.setTaskEndDate("未指定");
        }
        Integer taskConsumTime = act.getTaskConsumTime();
        if (Constant.YES.equals(taskExt.getPendingProdFlag())) {
            taskConsumTime = 0;
            form.setTaskEndDate("/");
        }
        if (act.getTaskConsumTime() != null) {
            form.setTaskConsumTime(taskConsumTime);
        }
        form.setTaskConsumTime(taskConsumTime);

        User user = UserUtils.get(act.getAssignee());
        if (null != user)
        {
            form.setAssignee(user.getName());
        }

    }

    /***
     * 计算任务进度
     *
     * @param staterDate
     * @param taskHours
     * @return
     * @date 2017年10月31日
     * @author yunnex
     */
    private Integer computingTaskSchedule(Date staterDate, Integer taskHours) {

        BigDecimal startDateLong = BigDecimal.valueOf(erpHolidaysService.calculateHours(staterDate, new Date()))
                        .multiply(BigDecimal.valueOf(60 * 60 * 1000));
        taskHours = taskHours == 0 ? 1 : taskHours;
        double taskHoursLong = (taskHours * 60 * 60 * 1000);
        return startDateLong.divide(BigDecimal.valueOf(taskHoursLong), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).intValue();
    }



}
