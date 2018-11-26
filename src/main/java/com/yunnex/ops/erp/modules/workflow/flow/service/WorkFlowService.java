package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.freemarker.TemplateServiceFreeMarkerImpl;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.modules.act.dao.ActDao;
import com.yunnex.ops.erp.modules.act.entity.Act;
import com.yunnex.ops.erp.modules.act.entity.ActDefExt;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.act.service.ActDefExtService;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.act.utils.ProcessDefCache;
import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalGoodService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitGoodService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpPayIntopieces;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpShopDataInput;
import com.yunnex.ops.erp.modules.shopdata.service.ErpPayIntopiecesService;
import com.yunnex.ops.erp.modules.shopdata.service.ErpShopDataInputService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStorePromoteAccountService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStorePromotePhotoMaterialService;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayWeixinService;
import com.yunnex.ops.erp.modules.sys.dao.UserDao;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.sys.utils.DictUtils;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.service.ErpTeamUserService;
import com.yunnex.ops.erp.modules.workflow.channel.service.JykOrderPromotionChannelService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.common.WorkFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowVariableConstants;
import com.yunnex.ops.erp.modules.workflow.flow.dao.JykFlowDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderOperateValue;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderSubTask;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpPayIntopiecesSubTask;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpShopDataInputSubTask;
import com.yunnex.ops.erp.modules.workflow.flow.entity.FlowHistory;
import com.yunnex.ops.erp.modules.workflow.flow.entity.JykFlow;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SdiFlow;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SubTask;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SubTaskHistory;
import com.yunnex.ops.erp.modules.workflow.flow.entity.TaskHistory;
import com.yunnex.ops.erp.modules.workflow.flow.from.FlowForm;
import com.yunnex.ops.erp.modules.workflow.flow.from.FlowStatForm;
import com.yunnex.ops.erp.modules.workflow.flow.from.WorkFlowQueryForm;
import com.yunnex.ops.erp.modules.workflow.flow.strategy.DeliveryProcessOperate;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 任务流处理信息
 * 
 * @author yunnex
 * @date 2017年10月31日
 */
@Service
public class WorkFlowService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowService.class);
    // 是否是自动移出待生产库，false为定时任务移出
    private static final String IS_AUTO_OUT_PENDING = "isAutoOutPending";
    // 进入待生产库原因 key
    private static final String IN_PENDING_REASON = "inPendingReason";
    // 进入待生产库原因：超过20工作日
    private static final String IN_PENDING_REASON_PT = "PT";
    // 进入待生产库原因：下次确定营业执照时间
    private static final String IN_PENDING_REASON_NLT = "NLT";
    // 进入待生产库原因：下次确定资质齐全时间
    private static final String IN_PENDING_REASON_NQT = "NQT";
    // 进入待生产库原因：下次确定投放上线预期时间
    private static final String IN_PENDING_REASON_NCT = "NCT";

    private static final String SUSPENDED_TASK_NAME = "已暂停订单任务，与商户沟通启动服务";

    /** Activiti工作流处理服务 */
    @Autowired
    private ActTaskService actTaskService;
    /*** 聚引客业务处理服务 */
    @Autowired
    private JykFlowService jykFlowService;
    /** 订单信息表 */
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    /** 订单分单信息表 */
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    private ErpOrderSplitGoodService erpOrderSplitGoodService;
    /** 表单服务 */
    @Autowired
    private FormService formService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private TemplateServiceFreeMarkerImpl serviceFreeMarker;
    @Autowired
    private ErpOrderSubTaskService erpOrderSubTaskService;
    @Autowired
    private ErpHolidaysService erpHolidaysService;
    @Autowired
    private WorkFlowMonitorService workFlowMonitorService;
    @Autowired
    private JykFlowDao jykFlowDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ActDefExtService actDefExtService;
    @Autowired
    private ErpShopDataInputService erpShopDataInputService;
    /*** 商户资料录入业务处理服务 */
    @Autowired
    private SdiFlowService sdiFlowService;
    /*** 商户资料录入子任务信息处理服务 */
    @Autowired
    private ErpShopDataInputSubTaskService erpShopDataInputSubTaskService;
    /*** 支付进件流程子任务信息处理服务 */
    @Autowired
    private ErpPayIntopiecesSubTaskService erpPayIntopiecesSubTaskService;
    @Autowired
    private ErpPayIntopiecesService erpPayIntopiecesService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ActDao actDao;
    @Autowired
    private ErpOrderFileService erpOrderFileService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private JykOrderPromotionChannelService jykOrderPromotionChannelService;
    @Autowired
    private ErpStorePayWeixinService erpStorePayWeixinService;
    @Autowired
    private UserService userService;
    @Autowired
    private JykOrderChoiceStoreService jykOrderChoiceStoreService;
    @Autowired
    private ErpTeamUserService erpTeamUserService;
    @Autowired
    private ErpOrderOriginalGoodService erpOrderOriginalGoodService;
    @Autowired
    private ErpStorePromotePhotoMaterialService erpStorePromotePhotoMaterialService;
    @Autowired
    private ErpStoreAdvertiserWeiboService erpStoreAdvertiserWeiboService;
    @Autowired 
    private ErpStorePromoteAccountService  erpStorePromoteAccountService;
    
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    
     @Autowired
    private ErpOrderOperateValueService erpOrderOperateValueService;

    @Autowired
    private ErpHisSplitServiceApi erpHisSplitServiceApi;    
    @Autowired
    private ErpServiceProgressService serviceScheduleService;
    
    /**
     * 启动聚引客工作流
     *
     * @param paranningExpertInterface 策划专家接口
     * @param orderId 订单编号
     * @param splitId 分单编号
     * @param procInsId 流程编号
     * @date 2017年10月31日
     * @author yunnex
     */
    public boolean startJykWorkFlow(String paranningExpertInterface, String orderId, String splitId) {
        Map<String, Object> vars = Maps.newHashMap();
        // 设置任务处理人
        vars.put(JykFlowConstants.PLANNING_EXPERT_INTERFACE_MAN, paranningExpertInterface);
        // 启动流程
        String procInsId = actTaskService.startProcess(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0], ActUtils.JYK_OPEN_ACCOUNT_FLOW[1], splitId,
                        "分单并指定策划专家接口人成功.", vars);
        LOGGER.info("聚引客工作流程启动成功|流程编号:{}", procInsId);
        JykFlow flow = new JykFlow();
        flow.setOrderId(orderId);
        flow.setSplitId(splitId);
        flow.setPlanningExpertInterface(paranningExpertInterface);
        flow.setProcInsId(procInsId);
        jykFlowService.save(flow);
        LOGGER.info("聚引客流程信息表保存成功|对象:{}", flow);

        Task task = actTaskService.getCurrentTaskInfo(this.actTaskService.getProcIns(procInsId));
        // 插入下一步所需要的
        insertSubTask(task.getId(), procInsId, paranningExpertInterface);

        return true;
    }
    
    /**
     * 启动聚引客工作流(3.0)
     *
     * @param paranningExpertInterface 策划专家接口
     * @param orderId 订单编号
     * @param splitId 分单编号
     * @param procInsId 流程编号
     * @date 2018年01月05日
     * @author SunQ
     */
    public boolean startJykWorkFlowNew(String paranningExpert, String orderId, String splitId) {
        ErpOrderOriginalInfo orderInfo = erpOrderOriginalInfoService.get(orderId);
        Map<String, Object> vars = Maps.newHashMap();
        // 设置商户订单类型 3.25 所有订单都走直销
        vars.put("orderType", 1);

        // 设置订单管理专员 用于分配策划专家
        List<User> userListCommissioner = systemService.getUserByRoleName(Constant.ORDER_MANAGER_COMMISSIONER);
        if (!CollectionUtils.isEmpty(userListCommissioner)) {
            vars.put(JykFlowConstants.orderCommissioner, userListCommissioner.get(0).getId());
        }else {
        	LOGGER.error("userListCommissioner={}订单管理专员没找到！",userListCommissioner);
            throw new ServiceException("订单管理专员没找到！");
        }

        // 设置是否指定运营顾问
        SdiFlow sdiInfo = sdiFlowService.getSdiinfoByShopId(orderInfo.getShopId());
        vars.put("distrConsultantFlag", "N");
        if(sdiInfo!=null && StringUtils.isNotBlank(sdiInfo.getOperationAdviser())){
            vars.put("distrConsultantFlag", "Y");
            vars.put("OperationAdviser", sdiInfo.getOperationAdviser());
        }
        //服务类型
        vars.put(DeliveryFlowConstant.SERVICE_TYPE, FlowConstant.ServiceType.SPLIT_JU_YIN_KE);
        // 流程中包含的服务集合
        List<String> goodServieTypeList = new ArrayList<>();
        goodServieTypeList.add(FlowConstant.ServiceType.SPLIT_JU_YIN_KE);
        vars.put(FlowVariableConstants.GOOD_SERVIE_TYPE_LIST, goodServieTypeList);
        // 启动流程
        String procInsId = actTaskService.startProcess(ActUtils.JYK_FLOW_LAST[0], ActUtils.JYK_FLOW_LAST[1], splitId, "分单并指定策划专家成功.", vars);
        LOGGER.info("聚引客工作流程3.0启动成功|流程编号:{}", procInsId);
        // 生成服务进度
        serviceScheduleService.createNoBeginSchedule("",procInsId, FlowConstant.ServiceType.SPLIT_JU_YIN_KE, orderId);
        JykFlow flow = new JykFlow();
        flow.setOrderId(orderId);
        flow.setSplitId(splitId);
        flow.setPlanningExpertInterface(userListCommissioner.get(0).getId());
        flow.setPlanningExpert(userListCommissioner.get(0).getId());
        if (sdiInfo != null && StringUtils.isNotBlank(sdiInfo.getOperationAdviser())) {
            flow.setOperationAdviser(sdiInfo.getOperationAdviser());
        }
        flow.setProcInsId(procInsId);
        jykFlowService.save(flow);

        LOGGER.info("聚引客流程信息表保存成功|对象:{}");
        
        // 保存流程角色信息(策划专家)
        erpOrderFlowUserService.insertOrderFlowUser(paranningExpert, orderId, splitId, JykFlowConstants.Planning_Expert, procInsId);


        // 保存流程角色信息(运营顾问)
        if(sdiInfo!=null && StringUtils.isNotBlank(sdiInfo.getOperationAdviser())){
            erpOrderFlowUserService.insertOrderFlowUser(sdiInfo.getOperationAdviser(), orderId, splitId, JykFlowConstants.OPERATION_ADVISER, procInsId);
        }
        
        if (!CollectionUtils.isEmpty(userListCommissioner)) {
            // 保存流程人员信息
            erpOrderFlowUserService.insertOrderFlowUser(userListCommissioner.get(0).getId(), orderId, splitId, JykFlowConstants.orderCommissioner,
                            procInsId);
        }

        // 获取业管-朋友圈开户
        List<User> userListFriends = systemService.getUserByRoleName("pi_friends");
        if(!CollectionUtils.isEmpty(userListFriends)){
            // 保存流程角色信息(业管-朋友圈开户)
            erpOrderFlowUserService.insertOrderFlowUser(userListFriends.get(0).getId(), orderId, splitId, JykFlowConstants.pipeIndustryFriends, procInsId);
            vars.put(JykFlowConstants.pipeIndustryFriends, userListFriends.get(0).getId());
        }
        
        // 获取业管-微博开户
        List<User> userListWeibo = systemService.getUserByRoleName("pi_weibo");
        if(!CollectionUtils.isEmpty(userListWeibo)){
            // 保存流程角色信息(业管-微博开户)
            erpOrderFlowUserService.insertOrderFlowUser(userListWeibo.get(0).getId(), orderId, splitId, JykFlowConstants.pipeIndustryWeibo, procInsId);
            vars.put(JykFlowConstants.pipeIndustryWeibo, userListWeibo.get(0).getId());
        }
        
        // 获取业管-陌陌开户

        List<User> userListMomo = systemService.getUserByRoleName("pi_momo");
        if(!CollectionUtils.isEmpty(userListMomo)){
            // 保存流程角色信息(业管-陌陌开户)
            erpOrderFlowUserService.insertOrderFlowUser(userListMomo.get(0).getId(), orderId, splitId, JykFlowConstants.pipeIndustryMomo, procInsId);
            vars.put(JykFlowConstants.pipeIndustryMomo, userListMomo.get(0).getId());
        }
        
        // 文案设计接口人
        List<User> userListDesign = systemService.getUserByRoleName("text_design_manager");
        if(!CollectionUtils.isEmpty(userListDesign)){
            // 保存流程角色信息(文案设计接口人)
            erpOrderFlowUserService.insertOrderFlowUser(userListDesign.get(0).getId(), orderId, splitId, JykFlowConstants.assignTextDesignInterfacePerson, procInsId);
            vars.put("textDesignInterfacePerson", userListDesign.get(0).getId());
        }
        
        // 投放顾问接口人

        List<User> userListPut = systemService.getUserByRoleName("put_advisor_manager");
        if(!CollectionUtils.isEmpty(userListPut)){
            // 保存流程角色信息(投放顾问接口人)
            erpOrderFlowUserService.insertOrderFlowUser(userListPut.get(0).getId(), orderId, splitId, JykFlowConstants.assignConsultantInterface, procInsId);
            vars.put("consultantInterface", userListPut.get(0).getId());
        }

        // 订单生效保存, 调用订单进度接口
        erpHisSplitServiceApi.orderEffective(splitId);
        // 订单指派策划专家, 调用指派策划专家完成接口
        erpHisSplitServiceApi.planning(splitId);
        
        Task task = actTaskService.getCurrentTaskInfo(this.actTaskService.getProcIns(procInsId));
        // 插入下一步所需要的
        if(null != task)
        {
            insertSubTask(task.getId(), procInsId, null);
        }

        return true;
    }

    /**
     * 
     * 根据条件获取我的待办列表
     * 
     * @param act
     * @param orderId 订单编号（支持模糊 查询）
     * @param taskStateList 任务状态列表
     * @param hurryFlag 订单加急标记
     * @param shopName 商户名称（支持模糊查询）
     * @return
     * @date 2017年10月31日
     * @author yunnex
     * @throws ParseException 
     */
    public List<Act> todoList(WorkFlowQueryForm form) throws ParseException {//NOSONAR
        List<String> taskStateList = StringUtils.isNotBlank(form.getTaskStateList()) ? Arrays.asList(form.getTaskStateList().split(",")) : null;
        List<String> goodsType = StringUtils.isNotBlank(form.getGoodsType()) ? Arrays.asList(form.getGoodsType().split(",")) : null;
        Integer hurryFlag = StringUtils.isNotBlank(form.getHurryFlag()) ? Integer.parseInt(form.getHurryFlag()) : null;
        
        // 过滤分单信息表编号
        List<ErpOrderSplitInfo> orderSplitList = erpOrderSplitInfoService.findListByParams(form.getShopName(), form.getOrderNumber(), hurryFlag, goodsType);
        
        // 获取提交商户资料录入对象集合
        ErpShopDataInput dataInput = new ErpShopDataInput();
        dataInput.setShopName(form.getShopName());
        dataInput.setOrderNumber(form.getOrderNumber());
        List<ErpShopDataInput> shopDataInputList = erpShopDataInputService.findListByParams(dataInput);
        
        // 获取支付进件流程对象集合
        List<ErpPayIntopieces> payIntopiecesList = erpPayIntopiecesService.findListByParams(form.getOrderNumber(), form.getShopName());
        
        // 获取流程待办信息列表
        List<Act> taskTodoList = actTaskService.todoList(new Act());
        //设置节点信息
        Map<String,String> a=new HashMap<String, String>();
        a.put("promotion_time_determination","promotion_time_determination");
        a.put("channel_confirm_business_license", "channel_confirm_business_license");
        a.put("channel_confirm_business_qualification", "channel_confirm_business_qualification");
        List<Act> taskTodoListFilter = new ArrayList<Act>();
        for (Act act : taskTodoList) {
            /*判断任务的类型(聚引客任务或者商户资料录入任务)*/
            if (act.getProcDef().getKey().startsWith(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0])) {
                ErpOrderSplitInfo splitRow = null;
                // 查看列表中是否存在任务
                for (ErpOrderSplitInfo split : orderSplitList) {
                    if (split.getAct().getProcInsId() == null) {
                        continue;
                    }
                    if (split.getAct().getProcInsId().equals(act.getBusinessId())) {
                        splitRow = split;
                    }
                }
                // 任务列表中，不包含的分单编号
                if (splitRow == null) {
                    LOGGER.debug("不符合条件过滤的任务Id为|ID:{}", act.getBusinessId());
                    continue;
                }
                //与商户沟通推广时间
                if("promotion_time_determination".equals(act.getTaskDefKey())){
                    // 过滤设置了沟通时间，但未确定投放时间的订单
                    if (splitRow.getNextContactTime() != null && splitRow.getPromotionTime() == null) {
                        Date now=new Date();
                        if(now.getTime()<splitRow.getNextContactTime().getTime()){
                            LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                            continue;
                        }
                        act.setTaskStarterDate(splitRow.getNextContactTime());
                    }
                    // 过滤投放日期距离现在时间超于20个工作日的订单任务
                    if (splitRow.getPromotionTime() != null) {
                        Double distanceDays = DateUtils.getDistanceOfTwoDate(this.erpHolidaysService.enddate(new Date(),JykFlowConstants.PLANNING_DATE_DISTINCT*8), splitRow.getPromotionTime());
                        if (distanceDays.intValue() > 0) {
                            LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                            continue;
                        }
                    }
                }
                //商户没有营业执照下次沟通时间
                if("channel_confirm_business_license".equals(act.getTaskDefKey())){
                    // 过滤设置了沟通时间，但未确定投放时间的订单
                    if (splitRow.getNextLicenseTime() != null) {
                        Date now=new Date();
                        if(now.getTime()<splitRow.getNextLicenseTime().getTime()){
                            LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                            continue;
                        }
                        act.setTaskStarterDate(splitRow.getNextLicenseTime());
                    }
                }
                 //商户资质是否齐全下次沟通时间
                if("channel_confirm_business_qualification".equals(act.getTaskDefKey())){
                    // 过滤设置了沟通时间，但未确定投放时间的订单
                    if (splitRow.getNextQualificationTime() != null) {
                        Date now=new Date();
                        if(now.getTime()<splitRow.getNextQualificationTime().getTime()){
                            LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                            continue;
                        }
                        act.setTaskStarterDate(splitRow.getNextQualificationTime());
                    }
                }
                setFormProperty(act);
                int taskHours = splitRow.getHurryFlag() == ErpOrderSplitInfo.HURRY_FLAG_YES ? act.getUrgentTaskDateHours() : act.getTaskDateHours();
                act.setTaskConsumTime(computingTaskSchedule(act.getTaskStarterDate(), taskHours));
                boolean flag = filterTaskState(taskStateList, act);
                try {
                    act.setTaskBusinessEndDate(erpHolidaysService.enddate(act.getTaskStarterDate(), taskHours));
                } catch (ParseException e) {
                    act.setTaskBusinessEndDate(null);
                }
                if (flag) {
                    taskTodoListFilter.add(act);
                }
            } else if (ActUtils.SHOP_DATA_INPUT_FLOW[0].equals(act.getProcDef().getKey())) {
                //商户资料录入流程
                ErpShopDataInput splitRow = null;
                // 查看列表中是否存在任务
                for (ErpShopDataInput split : shopDataInputList) {
                    if (split.getAct().getProcInsId() == null) {
                        continue;
                    }
                    if (split.getAct().getProcInsId().equals(act.getBusinessId())) {
                        splitRow = split;
                    }
                }
                // 任务列表中，不包含的分单编号
                if (splitRow == null) {
                    LOGGER.debug("不符合条件过滤的任务Id为|ID:{}", act.getBusinessId());
                    continue;
                }
                
                //获取表单数据
                setFormProperty(act);
                
                act.setTaskState(WorkFlowConstants.NORMAL);
                
                //将任务添加至列表
                taskTodoListFilter.add(act);
            } else if (ActUtils.WECHAT_PAY_INTOPIECES_FLOW[0].equals(act.getProcDef().getKey()) || ActUtils.UNION_PAY_INTOPIECES_FLOW[0]
                            .equals(act.getProcDef().getKey())) {
                //微信支付进件流程or银联支付进件流程
                ErpPayIntopieces splitRow = null;
                // 查看列表中是否存在任务
                for (ErpPayIntopieces split : payIntopiecesList) {
                    if (split.getAct().getProcInsId() == null) {
                        continue;
                    }
                    if (split.getAct().getProcInsId().equals(act.getBusinessId())) {
                        splitRow = split;
                    }
                }
                // 任务列表中，不包含的分单编号
                if (splitRow == null) {
                    LOGGER.debug("不符合条件过滤的任务Id为|ID:{}", act.getBusinessId());
                    continue;
                }
                
                //获取表单数据
                setFormProperty(act);
                
                act.setTaskState(WorkFlowConstants.NORMAL);
                
                //将任务添加至列表
                taskTodoListFilter.add(act);
            }
        }
        LOGGER.info("条件过滤后剩余的任务列表为|actList:{}", taskTodoList);
        return taskTodoListFilter;
    }


    // 还原订单号以及序号，以进行查询表单的回显
    public void restoreOrderNum(WorkFlowQueryForm workFlowQueryForm, String[] orderNo) {
        if (orderNo != null && orderNo.length == 2)
            workFlowQueryForm.setOrderNumber(orderNo[0] + "-" + orderNo[1]);
    }

    // 拆分订单号以及序号
    public String[] splitOrderNum(WorkFlowQueryForm workFlowQueryForm) {
        String[] orderNo = null;
        if(workFlowQueryForm.getOrderNumber()!=null){
            orderNo=workFlowQueryForm.getOrderNumber().split("-");
            workFlowQueryForm.setOrderNumber(orderNo[0]);
        }
        return orderNo;
    }


    public List<FlowForm> todoListNew(WorkFlowQueryForm queryForm, String detailType, String followTaskDetailWrap) throws ParseException {
        String[] orderNo = splitOrderNum(queryForm);
        queryForm.setAssignee(UserUtils.getUser().getId());
        LOGGER.info("我的任务请求参数：{}", queryForm);
        List<FlowForm> todoTasks = actDao.findTodoTasks(queryForm);
        int total = actDao.findTodoTaskCount(queryForm);
        queryForm.setTotal(total);
        restoreOrderNum(queryForm, orderNo);

        // 获取策划专家集合
        Role commissioner = systemService.getRoleByEnname(Constant.PLANNING_PERSON);
        List<User> userListCommissioner = systemService.findUser(new User(new Role(commissioner.getId())));
        // 封装表单数据
        List<FlowForm> result = new LinkedList<FlowForm>();
        
        for (FlowForm form : todoTasks) {
            Act act = form.getAct();
            act.setStatus("todo");
            
            if (act.getProcDefKey().startsWith(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0])) {
                wrapJykFlowForm(result, form, detailType, followTaskDetailWrap, userListCommissioner);
            } else if (ActUtils.SHOP_DATA_INPUT_FLOW[0].equals(act.getProcDefKey())) {
                wrapSdiForm(result, form, detailType, followTaskDetailWrap);
            } else if (ActUtils.WECHAT_PAY_INTOPIECES_FLOW[0].equals(act.getProcDefKey()) || ActUtils.UNION_PAY_INTOPIECES_FLOW[0]
                            .equals(act.getProcDefKey())) {
                wrapPayIntopieces(result, form, detailType, followTaskDetailWrap);
            }else if (ActUtils.MICROBLOG_PROMOTION_FLOW[0].equals(act.getProcDefKey())|| ActUtils.FRIENDS_PROMOTION_FLOW[0]
                            .equals(act.getProcDefKey())) {
                promoteInfoflow(result, form, detailType, followTaskDetailWrap);
            }
        }
        
        LOGGER.info("我的任务返回结果：{}", result);
        return result;
    }
    
    private void wrapPayIntopieces(List<FlowForm> result, FlowForm form, String detailType, String followTaskDetailWrap) {
        form.setFlowMark("payInto_flow");
        Act act = form.getAct();
        
        ErpPayIntopieces payInto = form.getPayInto();
        ErpShopInfo shop = form.getShop();
        ErpStoreInfo store = erpStoreInfoService.getStorePayInfo(payInto.getStoreId());
        ErpStorePayWeixin weixin=null;
        ErpStorePayUnionpay union=null;
        if(store!=null){
        	weixin = store.getWxPay();
        	union = store.getUnionPay();
        }
        
        form.setOrderNo(payInto.getIntopiecesName());
        form.setOrderTime(payInto.getCreateDate());
        String name = ActUtils.WECHAT_PAY_INTOPIECES_FLOW[0].equals(act.getProcDefKey()) ? "微信支付进件" : "银联支付进件";
        form.setGoodName(name);
        form.setShopName(shop.getName());
        form.setAgentName(shop.getServiceProvider());
        form.setTaskConsumTime(0);
        
        Map<String, Object> variables = runtimeService.getVariables(act.getExecutionId());
        act.setVars(variables);
        form.setVars(act.getVars());
        
        form.setDeliveryTime(null);
        
        // 获取子任务显示
        Map<String, Object> freemarkerMap = new HashMap<String, Object>();
        // 获取子任务完成状态
        List<ErpPayIntopiecesSubTask> subTaskList = erpPayIntopiecesSubTaskService.getSubTaskList(act.getTaskId());
        
        freemarkerMap.put("subTaskList", subTaskList);

        if (act.getTaskStarterDate() != null) {
            freemarkerMap.put("startDate", DateUtils.formatDate(act.getTaskStarterDate(), "MM-dd HH:mm"));
        }
        if (act.getTaskBusinessEndDate() != null) {
            freemarkerMap.put("endDate", DateUtils.formatDate(act.getTaskBusinessEndDate(), "MM-dd HH:mm"));
        } else {
            freemarkerMap.put("endDate", "未指定");
        }
        if (act.getTaskConsumTime() != null) {
            freemarkerMap.put("taskConsumTime", act.getTaskConsumTime());
        }
        form.setTaskConsumTime(0);
        freemarkerMap.put("followTaskDetailWrap", followTaskDetailWrap);
        
        freemarkerMap.put("vars", act.getVars().getMap());
        
        freemarkerMap.put("taskId", act.getTaskId());
        freemarkerMap.put("procInsId", act.getBusinessId());
        freemarkerMap.put("taskName", act.getTaskName());
        freemarkerMap.put("detailType", detailType);
        freemarkerMap.put("taskConsumTimeMin", 80);
        freemarkerMap.put("taskConsumTimeMax", 100);
        User user = UserUtils.get(act.getAssignee());
        freemarkerMap.put("taskUser", user.getName());

        /* 获取任务的key,并存入模板 */
        freemarkerMap.put("taskDefKey", act.getTaskDefKey());
        freemarkerMap.put("splitId", payInto.getId());
        /* 获取商户审核信息的状态 */
        freemarkerMap.put("wechatpayState", weixin!=null ? weixin.getAuditStatus() : 0);
        freemarkerMap.put("wechatpayRemark", weixin!=null ? weixin.getAuditContent() : "");
        freemarkerMap.put("unionpayState", union!=null ? union.getAuditStatus() : 0);
        freemarkerMap.put("unionpayRemark", union!=null ? union.getAuditContent() : "");
        freemarkerMap.put("shopMainId", shop.getId());
        freemarkerMap.put("storeId", store.getId());
        freemarkerMap.put("isMain", store.getIsMain());
        
        String taskStr = serviceFreeMarker.getContent(act.getTaskDefKey() + ".ftl", freemarkerMap);
        boolean isExists = true;
        for (FlowForm flowform : result) {
            // 如果已经存在，则添回到进旧的flowFrom中
            if (StringUtils.isNotBlank(form.getPayIntopiecesId()) && flowform.getPayIntopiecesId().equals(form.getPayIntopiecesId())) {
                // 比较大小
                if (flowform.getTaskConsumTime() < form.getTaskConsumTime()) {
                    flowform.setTaskConsumTime(form.getTaskConsumTime());
                }
                flowform.getSubTaskStrList().add(new SubTask(form.getTaskConsumTime(), taskStr));
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
            form.setSubTaskStr(taskStr);
            form.getSubTaskStrList().add(new SubTask(form.getTaskConsumTime(), taskStr));
            result.add(form);
        }
    }

    
    private void promoteInfoflow(List<FlowForm> result, FlowForm form, String detailType, String followTaskDetailWrap) {
        Act act = form.getAct();
        form.setFlowMark("promote_info_flow");
        ErpShopInfo shop = form.getShop();
        String name = ActUtils.MICROBLOG_PROMOTION_FLOW[0].equals(act.getProcDefKey()) ? "微博开户" : "朋友圈开户";
        form.setGoodName(name);
        form.setShopName(shop.getName());
        ErpOrderOriginalInfo order = form.getOrderOriginalInfo();
        order.setOrderNumber(name+"_"+order.getOrderNumber());
        form.setOrderNo(order.getOrderNumber());
        form.setOrderTime(form.getOrderDate());
        form.setTaskConsumTime(0);  
        
        int taskHours =  act.getTaskDateHours();
        Date enddate = null;
        
        try {
            enddate = erpHolidaysService.enddate(act.getTaskStarterDate(), taskHours);
        } catch (ParseException e1) {
            LOGGER.error(e1.getMessage(), e1);
        }
        act.setTaskConsumTime(computingTaskSchedule(act.getTaskStarterDate(), taskHours));
        try {
            act.setTaskBusinessEndDate(enddate);
        } catch (Exception e) {
            act.setTaskBusinessEndDate(null);
        }
        Map<String, Object> freemarkerMap = new HashMap<String, Object>();
        if (act.getTaskStarterDate() != null) {
            freemarkerMap.put("startDate", DateUtils.formatDate(act.getTaskStarterDate(), "MM-dd HH:mm"));
        }
        if (act.getTaskBusinessEndDate() != null) {
            freemarkerMap.put("endDate", DateUtils.formatDate(act.getTaskBusinessEndDate(), "MM-dd HH:mm"));
        } else {
            freemarkerMap.put("endDate", "未指定");
        }
        if (act.getTaskConsumTime() != null) {
            freemarkerMap.put("taskConsumTime", act.getTaskConsumTime());
        }
        form.setTaskConsumTime(act.getTaskConsumTime());
        freemarkerMap.put("followTaskDetailWrap", followTaskDetailWrap);
        freemarkerMap.put("taskId", act.getTaskId());
        freemarkerMap.put("procInsId", act.getBusinessId());
        freemarkerMap.put("taskName", act.getTaskName());
        freemarkerMap.put("detailType", detailType);
        freemarkerMap.put("taskConsumTimeMin", 80);
        freemarkerMap.put("taskConsumTimeMax", 100);
        User user = UserUtils.get(act.getAssignee());
        freemarkerMap.put("taskUser", user.getName());

        /* 获取任务的key,并存入模板 */
        freemarkerMap.put("taskDefKey", act.getTaskDefKey());
        String taskStr = serviceFreeMarker.getContent(act.getTaskDefKey() + ".ftl", freemarkerMap);
        boolean isExists = true;
        for (FlowForm flowform : result) {
            // 如果已经存在，则添回到进旧的flowFrom中
            if (StringUtils.isNotBlank(form.getPayIntopiecesId()) && flowform.getPayIntopiecesId().equals(form.getPayIntopiecesId())) {
                // 比较大小
                if (flowform.getTaskConsumTime() < form.getTaskConsumTime()) {
                    flowform.setTaskConsumTime(form.getTaskConsumTime());
                }
                flowform.getSubTaskStrList().add(new SubTask(form.getTaskConsumTime(), taskStr));
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
            form.setSubTaskStr(taskStr);
            form.getSubTaskStrList().add(new SubTask(form.getTaskConsumTime(), taskStr));
            result.add(form);
        }
    }
    
    
    
    private void wrapSdiForm(List<FlowForm> result, FlowForm form, String detailType, String followTaskDetailWrap) throws ParseException {
        form.setFlowMark("sdi_flow");
        Act act = form.getAct();
        act.setTaskState(WorkFlowConstants.NORMAL);
        
        SdiFlow flow = form.getSdiFlow();
        ErpOrderOriginalInfo order = form.getOrderOriginalInfo();
        ErpShopInfo shop = form.getShop();
        
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
        if ("1".equals(form.getOrderType())) {
            form.setContactWay(order.getContactName() + " / " + order.getContactNumber());
        } else {
            String promoteContact = order.getPromoteContact() != null && !"".equals(order.getPromoteContact()) ? order.getPromoteContact() : "--";
            String promotePhone = order.getPromotePhone() != null && !"".equals(order.getPromotePhone()) ? order.getPromotePhone() : "--";
            form.setContactWay(promoteContact + " / " + promotePhone);
        }
        
        Map<String, Object> variables = runtimeService.getVariables(act.getExecutionId());
        act.setVars(variables);
        form.setVars(act.getVars());
        form.setDeliveryTime(null);
        
        // 获取子任务显示
        Map<String, Object> freemarkerMap = new HashMap<String, Object>();
        // 获取子任务完成状态
        List<ErpShopDataInputSubTask> subTaskList = this.erpShopDataInputSubTaskService.getSubTaskList(act.getTaskId());
        
        freemarkerMap.put("subTaskList", subTaskList);

        
        int taskHours =  act.getTaskDateHours();
        Date enddate = erpHolidaysService.enddate(act.getTaskStarterDate(), taskHours);
        act.setTaskConsumTime(computingTaskSchedule(act.getTaskStarterDate(), taskHours));
        try {
            act.setTaskBusinessEndDate(enddate);
        } catch (Exception e) {
            act.setTaskBusinessEndDate(null);
        }
        
        if (act.getTaskStarterDate() != null) {
            freemarkerMap.put("startDate", DateUtils.formatDate(act.getTaskStarterDate(), "MM-dd HH:mm"));
        }
        if (act.getTaskBusinessEndDate() != null) {
            freemarkerMap.put("endDate", DateUtils.formatDate(act.getTaskBusinessEndDate(), "MM-dd HH:mm"));
        } else {
            freemarkerMap.put("endDate", "未指定");
        }
        if (act.getTaskConsumTime() != null) {
            freemarkerMap.put("taskConsumTime", act.getTaskConsumTime());
        }
        form.setTaskConsumTime(act.getTaskConsumTime());
        
        if (StringUtils.isNotBlank(act.getTaskUserRole())) {
            List<User> userList = userService.getUserByRoleName(act.getTaskUserRole());
            freemarkerMap.put("taskUserList", userList);
        }
        if (StringUtils.isNotBlank(act.getTaskUserRole2())) {
            List<User> userList = userService.getUserByRoleName(act.getTaskUserRole2());
            freemarkerMap.put("taskUserList2", userList);
        }
        freemarkerMap.put("followTaskDetailWrap", followTaskDetailWrap);
        
        freemarkerMap.put("vars", act.getVars().getMap());
        
        freemarkerMap.put("taskId", act.getTaskId());
        freemarkerMap.put("procInsId", act.getBusinessId());
        freemarkerMap.put("taskName", act.getTaskName());
        freemarkerMap.put("detailType", detailType);
        freemarkerMap.put("taskConsumTimeMin", 80);
        freemarkerMap.put("taskConsumTimeMax", 100);
        User user = UserUtils.get(act.getAssignee());
        freemarkerMap.put("taskUser", user.getName());
        StringBuffer sb = new StringBuffer();
        for(Role role:user.getRoleList()){
            sb.append(role.getName()+" ");
        }
        freemarkerMap.put("taskUserRole", sb.toString());
        freemarkerMap.put("taskUserId", user.getId());

        /* 获取任务的key,并存入模板 */
        freemarkerMap.put("taskDefKey", act.getTaskDefKey());
        freemarkerMap.put("splitId", flow.getSdiId());
        /* 获取商户审核信息的状态 */
        freemarkerMap.put("zhangbeiState", shop!=null&&null!=shop.getZhangbeiState() ? shop.getZhangbeiState() : 0);
        freemarkerMap.put("zhangbeiRemark", shop!=null&&StringUtils.isNotBlank(shop.getZhangbeiRemark()) ? shop.getZhangbeiRemark() : "");
        freemarkerMap.put("wechatpayState", shop!=null&&null!=shop.getWechatpayState() ? shop.getWechatpayState() : 0);
        freemarkerMap.put("wechatpayRemark", shop!=null&&StringUtils.isNotBlank(shop.getWechatpayRemark()) ? shop.getWechatpayRemark() : "");
        freemarkerMap.put("unionpayState", shop!=null&&null!=shop.getUnionpayState() ? shop.getUnionpayState() : 0);
        freemarkerMap.put("unionpayRemark", shop!=null&&StringUtils.isNotBlank(shop.getUnionpayRemark()) ? shop.getUnionpayRemark() : "");
        freemarkerMap.put("shopId", order.getShopId());
        freemarkerMap.put("shopMainId", shop!=null ? shop.getId() : "");
        
        if("select_extension_store_shop".equals(act.getTaskDefKey())
                        || "select_extension_store2_shop".equals(act.getTaskDefKey())){
            // 获得商户下的所有门店
            if(null!=shop)
            {
                   List<ErpStoreInfo> storeList = erpStoreInfoService.findAllListWhereShopId("0", shop.getId());
                   freemarkerMap.put("storeList", storeList);
            }
        }
        
        freemarkerMap.put("zhangbeiId", "--");
        freemarkerMap.put("passWord", "--");
        if("conact_new_shop_shop".equals(act.getTaskDefKey()) && shop!=null && StringUtils.isNotBlank(shop.getZhangbeiId())){
            freemarkerMap.put("zhangbeiId", shop.getZhangbeiId());
            String passwordStr = StringUtils.rightPad(shop.getZhangbeiId(), 6, '0');
            freemarkerMap.put("passWord", passwordStr.substring(passwordStr.length()-6, passwordStr.length()));
        }
        
        
        if(null!=shop)
        {
            int storeCount = erpStoreInfoService.findCountWhereShopId("0", shop.getId());
            freemarkerMap.put("storeCount", storeCount);
            if(storeCount > 0){
                ErpStoreInfo mainStore = erpStoreInfoService.getIsmainStore("0", shop.getId(), "1");
                freemarkerMap.put("storeId", mainStore!=null ? mainStore.getId() : "");
            }
        }
        
        String taskStr = serviceFreeMarker.getContent(act.getTaskDefKey() + ".ftl", freemarkerMap);
        boolean isExists = true;
        for (FlowForm flowform : result) {
            // 如果已经存在，则添回到进旧的flowFrom中 
            if (flowform.getOrderNo().equals(form.getOrderNo()) && flowform.getAct().getProcDefKey().equals(form.getAct().getProcDefKey())) {
                // 比较大小
                if (flowform.getTaskConsumTime() < form.getTaskConsumTime()) {
                    flowform.setTaskConsumTime(form.getTaskConsumTime());
                }
                flowform.getSubTaskStrList().add(new SubTask(form.getTaskConsumTime(), taskStr));
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
            form.setSubTaskStr(taskStr);
            form.getSubTaskStrList().add(new SubTask(form.getTaskConsumTime(), taskStr));
            result.add(form);
        }
    }
    
    private void wrapJykFlowForm(List<FlowForm> result, FlowForm form, String detailType, String followTaskDetailWrap,
                    List<User> userListCommissioner) throws ParseException {
        form.setFlowMark(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0]);
        // 处理暂停任务
        processSuspendedTask(form);

        Act act = form.getAct();
        
        JykFlow flow = form.getJykFlow();
        ErpOrderOriginalInfo order = form.getOrderOriginalInfo();
        ErpOrderSplitInfo orderSplit = form.getOrderSplitInfo();
        ErpShopInfo shop = form.getShop();
        TaskExt taskExt = form.getTaskExt();
        // 填充订单信息
        fillOrderInfo(form, act, order, orderSplit, taskExt);
        //新流程 才要设置限制 
        setJykVariable(act, orderSplit, shop);
        
        Map<String, Object> variables = runtimeService.getVariables(act.getExecutionId());
        act.setVars(variables);
        form.setVars(act.getVars());
        
        // 获取子任务显示
        Map<String, Object> freemarkerMap = new HashMap<String, Object>();
        // 填充子任务信息
        fillSubTaskInfo(act, freemarkerMap);
        // 填充推广时间表单信息
        String inPendingReason = fillPromoteDateInfo(act, orderSplit, variables);
        // 直销和服务商 -> 商户对接/确认推广门店/资质/推广时间 <填充资质信息>
        fillQualificationInfo(act, orderSplit, variables, inPendingReason);
        // 填充任务信息
        fillTaskInfo(form, detailType, followTaskDetailWrap, act, orderSplit, taskExt, freemarkerMap);
        // 填充订单商品信息
        fillOrderGoodInfo(act, order, freemarkerMap);

        // 获得商户下的所有门店 填充商户信息
        fillMerchantInfo(act, shop, freemarkerMap);

        // 填充支付进件状态，需要获取掌贝进件状态的模板
        fillPayState(act, flow, shop, freemarkerMap);

        // 填充驳回原因
        fillRejectedReasion(act, freemarkerMap);

        // 填充商户信息
        fillShopInfo(orderSplit, shop, freemarkerMap); // NOSONAR
        
        freemarkerMap.put("vars", act.getVars().getMap());
        
        /* 获取任务的key,并存入模板 */
        freemarkerMap.put("taskDefKey", act.getTaskDefKey());
        freemarkerMap.put("splitId", orderSplit.getId());
        freemarkerMap.put("channelService", jykOrderPromotionChannelService);
        freemarkerMap.put("orderInfo", order);
        if (null != userListCommissioner)
            freemarkerMap.put("planningPerson", userListCommissioner);
        String taskStr = serviceFreeMarker.getContent(act.getTaskDefKey() + ".ftl", freemarkerMap);
        boolean isExists = true;
        // 任务分组,合并
        taskGroup(result, form, taskStr, isExists);
    }

    private void taskGroup(List<FlowForm> result, FlowForm form, String taskStr, boolean isExists) {
        for (FlowForm flowform : result) {
            // 如果已经存在，则添回到进旧的flowFrom中
            if (StringUtils.isNotBlank(flowform.getOrderNo()) && flowform.getOrderNo().equals(form.getOrderNo()) && flowform.getAct().getProcDefKey()
                            .equals(form.getAct().getProcDefKey())) {
                // 比较大小
                if (flowform.getTaskConsumTime() < form.getTaskConsumTime()) {
                    flowform.setTaskConsumTime(form.getTaskConsumTime());
                }

                // 暂停标识订单只展示一个任务名称
                String suspendFlag = form.getOrderSplitInfo().getSuspendFlag();
                if (!Constant.YES.equals(suspendFlag)) {
                    flowform.getSubTaskStrList().add(new SubTask(form.getTaskConsumTime(), taskStr));
                }

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
            form.setSubTaskStr(taskStr);
            form.getSubTaskStrList().add(new SubTask(form.getTaskConsumTime(), taskStr));
            result.add(form);
        }
    }

    private void fillPayState(Act act, JykFlow flow, ErpShopInfo shop, Map<String, Object> freemarkerMap) {
        if("zhangbei_in_sucess_zhixiao".equals(act.getTaskDefKey()) && shop!=null){
            freemarkerMap.put("zhangbeiState", null!=shop&&null!=shop.getZhangbeiState() ? shop.getZhangbeiState() : 0);
        }
   
        // 需要获取微信支付状态的模板
        if("weixin_in_sucess_zhixiao".equals(act.getTaskDefKey())){
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(flow.getSplitId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            if(storeInfo!=null){
                // 获取微信进件信息
                ErpStorePayWeixin weixin = StringUtils.isNotBlank(storeInfo.getWeixinPayId()) ? erpStorePayWeixinService.get(storeInfo.getWeixinPayId()) : null;
                freemarkerMap.put("wechatpayState", weixin!=null ? weixin.getAuditStatus() : 0);
            }
        }
    }

    private void fillRejectedReasion(Act act, Map<String, Object> freemarkerMap) {
        // 需要驳回原因的模板
        if("modify_friends_promote_info_zhixiao".equals(act.getTaskDefKey()) || 
                    "modify_microblog_promote_info_zhixiao".equals(act.getTaskDefKey()) ||
                        "modify_momo_promote_info_zhixiao".equals(act.getTaskDefKey())){
            ErpOrderOperateValue erpOrderOperateValue = null;
            if("modify_friends_promote_info_zhixiao".equals(act.getTaskDefKey())){
                erpOrderOperateValue = erpOrderOperateValueService.getOnlyOne(act.getProcInsId(), "friends_promote_info_review_zhixiao", "1");
            }
            if("modify_microblog_promote_info_zhixiao".equals(act.getTaskDefKey())){
                erpOrderOperateValue = erpOrderOperateValueService.getOnlyOne(act.getProcInsId(), "microblog_promote_info_review_result_zhixiao", "1");
                if(erpOrderOperateValue==null){
                    erpOrderOperateValue = erpOrderOperateValueService.getOnlyOne(act.getProcInsId(), "microblog_promote_info_review_zhixiao", "1");
                }
                if (erpOrderOperateValue == null) {
                    erpOrderOperateValue = erpOrderOperateValueService.getOnlyOne(act.getProcInsId(), "microblog_promote_info_review_zhixiao_latest",
                                    "1");
                }
            }
            if("modify_momo_promote_info_zhixiao".equals(act.getTaskDefKey())){
                erpOrderOperateValue = erpOrderOperateValueService.getOnlyOne(act.getProcInsId(), "momo_promote_info_review_zhixiao", "1");
            }
            if(erpOrderOperateValue!=null){
                freemarkerMap.put("reason", erpOrderOperateValue.getValue());
            }
        }
    }

    private void fillShopInfo(ErpOrderSplitInfo orderSplit, ErpShopInfo shop, Map<String, Object> freemarkerMap) {
        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(orderSplit.getId());
        if(StringUtils.isNotBlank(storeId)){
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            if(storeInfo!=null){
                freemarkerMap.put("storeId", storeInfo.getId());
                freemarkerMap.put("storeName", storeInfo.getShortName());
                freemarkerMap.put("isMain", storeInfo.getIsMain());
                String weiboId = erpStoreInfoService.get(storeId).getAdvertiserWeiboId();
                if (StringUtils.isNotBlank(weiboId)) {
                    String OpenOrTrans = erpStoreAdvertiserWeiboService.get(weiboId).getOpenOrTrans();
                    freemarkerMap.put("openOrTrans", OpenOrTrans);
                }
            }
        }
        
        if (shop != null) {
            freemarkerMap.put("shopMainId", shop.getId());
            freemarkerMap.put("shopName", shop.getName());
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

    private void setJykVariable(Act act, ErpOrderSplitInfo orderSplit, ErpShopInfo shop) {
        if (ActUtils.JYK_FLOW_NEW[0].equals(act.getProcDefKey()))
        {
            // 输出卡券之前必须先要 上传推广图片 
            if ("work_output_card_coupon".equals(act.getTaskDefKey())
                    || "work_output_design_draft_update".equals(act.getTaskDefKey())
                    || "work_output_design_draft".equals(act.getTaskDefKey())
                    || "work_output_official_documents_update".equals(act.getTaskDefKey())
                    || "work_output_official_documents".equals(act.getTaskDefKey())
                    || "work_promotion_recharge".equals(act.getTaskDefKey())
                    || "work_promotion_online".equals(act.getTaskDefKey())) {
                Object obj = runtimeService.getVariable(act.getExecutionId(), "UploadPictureMaterial");
                if (null == obj) {
                    String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(orderSplit.getId());
                    ErpStorePromotePhotoMaterial promotePhotoMaterial = erpStorePromotePhotoMaterialService
                            .findlistWhereStoreId("0", storeId);
                    if (promotePhotoMaterial != null) {
                        runtimeService.setVariable(act.getExecutionId(), "UploadPictureMaterial", storeId);
                    }
                }

                // 输出卡券之前必须先要 掌贝进件 
                if ("work_output_card_coupon".equals(act.getTaskDefKey())) {
                    Object objZif = runtimeService.getVariable(act.getExecutionId(), "ZhangbeiInFlag");
                    if (null == objZif) {
                        ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiID(shop.getZhangbeiId());
                        if (shopInfo != null && shopInfo.getZhangbeiState() != null && shopInfo.getZhangbeiState().intValue() == 2) {
                            runtimeService.setVariable(act.getExecutionId(), "ZhangbeiInFlag", shopInfo.getId());
                        }
                    }
                }

                // 推广充值之前 先要把推广开户完成
                if ("work_promotion_recharge".equals(act.getTaskDefKey())) {
                    Object objPaf = runtimeService.getVariable(act.getExecutionId(), "promoteAccountFinish");
                    if (null == objPaf) {
                        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(orderSplit.getId());
                        if (erpStorePromoteAccountService.isPromoteAccountFinish(storeId, orderSplit.getId())) {
                            runtimeService.setVariable(act.getExecutionId(), "promoteAccountFinish", storeId);
                        }
                    }
                }

                // 推广上线之前 先要完成微信支付进件
                if ("work_promotion_online".equals(act.getTaskDefKey())) {
                    Object objWps = runtimeService.getVariable(act.getExecutionId(), "wechatPaySuccess");
                    if (null == objWps) {
                        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(orderSplit.getId());
                        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
                        ErpStorePayWeixin weixin = erpStorePayWeixinService.get(storeInfo.getWeixinPayId());
                        if (weixin != null && 2 == weixin.getAuditStatus().intValue()) {
                            runtimeService.setVariable(act.getExecutionId(), "wechatPaySuccess", "1");
                        }
                    }
                }
            }
        }
        else if (ActUtils.JYK_OPEN_ACCOUNT_FLOW[0].equals(act.getProcDefKey()))
        {
            runtimeService.setVariable(act.getExecutionId(), "ZhangbeiInFlag", ActUtils.JYK_OPEN_ACCOUNT_FLOW[0]);
            runtimeService.setVariable(act.getExecutionId(), "wechatPaySuccess", "1");
            runtimeService.setVariable(act.getExecutionId(), "promoteAccountFinish", ActUtils.JYK_OPEN_ACCOUNT_FLOW[0]);
        }
    }

    protected void fillOrderGoodInfo(Act act, ErpOrderOriginalInfo order, Map<String, Object> freemarkerMap) {
        if (JykFlowConstants.ASSIGNE_PLANNING_EXPERT.equalsIgnoreCase(act.getTaskDefKey())) {
            List<ErpOrderOriginalGood> erpOrderOriginalGoods =  erpOrderOriginalGoodService.findListByOrderId(order.getId());
            StringBuilder builder = new StringBuilder();
            for (ErpOrderOriginalGood erpOrderOriginalGood : erpOrderOriginalGoods) {
                builder.append(Constant.SEMICOLON).append(erpOrderOriginalGood.getGoodName()).append(Constant.ASTERISK)
                                .append(erpOrderOriginalGood.getNum());
            }
            if(builder.length()>0)
            {
                builder.deleteCharAt(0);
            }
            freemarkerMap.put("orderGoodName", builder.toString());
       
        }
    }

    protected void fillTaskInfo(FlowForm form, String detailType, String followTaskDetailWrap, Act act, ErpOrderSplitInfo orderSplit, TaskExt taskExt,
                    Map<String, Object> freemarkerMap) throws ParseException {
        int taskHours = orderSplit.getHurryFlag() == ErpOrderSplitInfo.HURRY_FLAG_YES ? act.getUrgentTaskDateHours() : act.getTaskDateHours();
        Date enddate = erpHolidaysService.enddate(act.getTaskStarterDate(), taskHours);
        act.setTaskConsumTime(computingTaskSchedule(act.getTaskStarterDate(), taskHours));
        try {
            act.setTaskBusinessEndDate(enddate);
        } catch (Exception e) {
            act.setTaskBusinessEndDate(null);
        }
        
        if (act.getTaskStarterDate() != null) {
            freemarkerMap.put("startDate", DateUtils.formatDate(act.getTaskStarterDate(), "MM-dd HH:mm"));
        }
        if (act.getTaskBusinessEndDate() != null && Constant.NO.equals(taskExt.getPendingProdFlag())) {
            freemarkerMap.put("endDate", DateUtils.formatDate(act.getTaskBusinessEndDate(), "MM-dd HH:mm"));
        } else {
            freemarkerMap.put("endDate", "未指定");
        }
        Integer taskConsumTime = act.getTaskConsumTime();
        if (Constant.YES.equals(taskExt.getPendingProdFlag())) {
            taskConsumTime = 0;
            freemarkerMap.put("endDate", "/");
        }
        if (act.getTaskConsumTime() != null) {
            freemarkerMap.put("taskConsumTime", taskConsumTime);
        }
        form.setTaskConsumTime(taskConsumTime);
        
        if (StringUtils.isNotBlank(act.getTaskUserRole())) {
            List<User> userList = userService.getUserByRoleName(act.getTaskUserRole());
            freemarkerMap.put("taskUserList", userList);
        }
        if (StringUtils.isNotBlank(act.getTaskUserRole2())) {
            List<User> userList = userService.getUserByRoleName(act.getTaskUserRole2());
            freemarkerMap.put("taskUserList2", userList);
        }
        freemarkerMap.put("followTaskDetailWrap", followTaskDetailWrap);
        freemarkerMap.put("taskId", act.getTaskId());
        freemarkerMap.put("procInsId", act.getBusinessId());
        freemarkerMap.put("taskName", act.getTaskName());
        freemarkerMap.put("detailType", detailType);
        freemarkerMap.put("taskConsumTimeMin", 80);
        freemarkerMap.put("taskConsumTimeMax", 100);
        User user = UserUtils.get(act.getAssignee());
        freemarkerMap.put("taskUser", user.getName());
        StringBuffer sb = new StringBuffer();
        for(Role role:user.getRoleList()){
            sb.append(role.getName()+" ");
        }
        freemarkerMap.put("taskUserRole", sb.toString());
        freemarkerMap.put("taskUserId", user.getId());
        String roleName = DictUtils.getDictValue(act.getTaskUserRole(), "role_jyk_constant", null);
        if (roleName != null) {
            ErpOrderFlowUser assignee = erpOrderFlowUserService.findByProcInsIdAndRoleName(act.getProcInsId(), roleName);
            freemarkerMap.put("assignee", assignee);
        }
    }

    protected String fillPromoteDateInfo(Act act, ErpOrderSplitInfo orderSplit, Map<String, Object> variables) {
        Object iprObj = variables.get(IN_PENDING_REASON);
        String inPendingReason = String.valueOf(iprObj != null ? iprObj : "");
        // 与商户沟通推广时间
        if ("promotion_time_determination".equals(act.getTaskDefKey())) {
            if (IN_PENDING_REASON_PT.equals(inPendingReason)) {
                act.setTaskStarterDate(orderSplit.getPromotionTime());
            }
            if (IN_PENDING_REASON_NCT.equals(inPendingReason)) {
                act.setTaskStarterDate(orderSplit.getNextContactTime());
            }
        }
        // 商户没有营业执照下次沟通时间
        if ("channel_confirm_business_license".equals(act.getTaskDefKey())) {
            if (orderSplit.getNextLicenseTime() != null) {
                act.setTaskStarterDate(orderSplit.getNextLicenseTime());
            }
        }
        // 商户资质是否齐全下次沟通时间
        if ("channel_confirm_business_qualification".equals(act.getTaskDefKey())) {
            if (orderSplit.getNextQualificationTime() != null) {
                act.setTaskStarterDate(orderSplit.getNextQualificationTime());
            }
        }
        return inPendingReason;
    }

    protected void fillSubTaskInfo(Act act, Map<String, Object> freemarkerMap) {
        // 获取子任务完成状态
        List<ErpOrderSubTask> subTaskList = this.erpOrderSubTaskService.getSubTaskList(act.getTaskId());
        
        /*判断子任务列表是否为空，并找出对应的文件列表*/
        if (!CollectionUtils.isEmpty(subTaskList)) {
            for (ErpOrderSubTask subTask : subTaskList) {
                // 获取子任务的上传文件
                ErpOrderFile erpOrderFile = new ErpOrderFile();
                erpOrderFile.setProcInsId(act.getBusinessId());
                erpOrderFile.setTaskDefKey(act.getTaskDefKey());
                erpOrderFile.setSubTaskId(subTask.getSubTaskId());
                List<ErpOrderFile> fileList = erpOrderFileService.findListSubTask(erpOrderFile);
                /* 将对应子任务的文件存入对象 */
                if (!CollectionUtils.isEmpty(fileList)) {
                    subTask.setOrderFiles(fileList);
                }
            }
        }
        freemarkerMap.put("subTaskList", subTaskList);
    }

    protected void fillQualificationInfo(Act act, ErpOrderSplitInfo orderSplit, Map<String, Object> variables, String inPendingReason) {
        if (isMerchantDocking(act)) {
            boolean isAutoOutPending = true;
            Object object = variables.get(IS_AUTO_OUT_PENDING);
            if (object != null && !Boolean.valueOf(object.toString())) {
                isAutoOutPending = false;
            }
            Date activationTime = orderSplit.getActivationTime();

            if (IN_PENDING_REASON_PT.equals(inPendingReason)) {
                act.setTaskStarterDate(!isAutoOutPending ? activationTime : orderSplit.getPromotionTime());
            } else if (IN_PENDING_REASON_NLT.equals(inPendingReason)) {
                act.setTaskStarterDate(!isAutoOutPending ? activationTime : orderSplit.getNextLicenseTime());
            } else if (IN_PENDING_REASON_NQT.equals(inPendingReason)) {
                act.setTaskStarterDate(!isAutoOutPending ? activationTime : orderSplit.getNextQualificationTime());
            } else if (IN_PENDING_REASON_NCT.equals(inPendingReason)) {
                act.setTaskStarterDate(!isAutoOutPending ? activationTime : orderSplit.getNextContactTime());
            }
            // 特殊处理
            if ("Y".equals(orderSplit.getPendingProduced()) && orderSplit.getActivationTime() != null) {
                if (orderSplit.getHurryFlag().intValue() == 1) {
                    act.setUrgentTaskDateHours(2);
                } else {
                    act.setTaskDateHours(4);
                }
            }
        }
    }

    protected void fillMerchantInfo(Act act, ErpShopInfo shop, Map<String, Object> freemarkerMap) {
        if (isMerchantDocking(act)) {
            if(shop!=null){
                List<ErpStoreInfo> storeList = erpStoreInfoService.findAllListWhereShopId("0", shop.getId());
                freemarkerMap.put("storeList", storeList);
                // 获取商户运营顾问
                User sdiuser = sdiFlowService.findOperationAdviserByShopId(shop.getZhangbeiId());
                freemarkerMap.put("sdiFlowUser", "未分配");
                if (sdiuser != null) {
                    freemarkerMap.put("sdiFlowUser", sdiuser.getName());
                }
            }

        }
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

    /**
     * <直销/服务商>商户对接
     *
     * @param act
     * @return
     * @date 2018年4月12日
     * @author zjq
     */
    private boolean isMerchantDocking(Act act) {
        return JykFlowConstants.CONTACT_SHOP_ZHIXIAO.equals(act.getTaskDefKey()) || JykFlowConstants.CONTACT_SHOP_SERVICE
                        .equals(act.getTaskDefKey()) || JykFlowConstants.CONTACT_SHOP_ZHIXIAO_LATEST
                                        .equals(act.getTaskDefKey()) || JykFlowConstants.CONTACT_SHOP_SERVICE_LATEST.equals(act.getTaskDefKey());
    }
    

    /**
     * 获取我的任务统计数据
     *
     * @param workFlowQueryForm
     * @return
     * @throws ParseException
     * @date 2018年1月11日
     */
    public Map<String, Object> getTodoTaskStat(WorkFlowQueryForm workFlowQueryForm) throws ParseException {
        splitOrderNum(workFlowQueryForm);
        workFlowQueryForm.setAssignee(UserUtils.getUser().getId());
        workFlowQueryForm.setIsPage(0);
        List<FlowForm> tasks = actDao.findTodoTasks(workFlowQueryForm);
        Map<String, Object> map = calcStat(tasks);
        return map;
    }
    
    /**
     * 获取我的关注统计数据
     *
     * @param workFlowQueryForm
     * @return
     * @throws ParseException
     * @date 2018年1月13日
     */
    public Map<String, Object> getFollowTaskStat(WorkFlowQueryForm workFlowQueryForm) throws ParseException {
        splitOrderNum(workFlowQueryForm);
        workFlowQueryForm.setAssignee(UserUtils.getUser().getId());
        workFlowQueryForm.setIsPage(0);
        List<FlowForm> tasks = actDao.findFollowTasks(workFlowQueryForm);
        Map<String, Object> map = calcStat(tasks);
        return map;
    }

    /**
     * 获取我的待生产库统计数据
     *
     * @param workFlowQueryForm
     * @return
     * @throws ParseException
     * @date 2018年1月13日
     */
    public Map<String, Object> getPendingProductionTaskStat(WorkFlowQueryForm workFlowQueryForm) throws ParseException {
        splitOrderNum(workFlowQueryForm);
        workFlowQueryForm.setAssignee(UserUtils.getUser().getId());
        workFlowQueryForm.setIsPage(0);
        List<FlowForm> tasks = actDao.findPendingProductionTasks(workFlowQueryForm);
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
                if (null == form.getOrderOriginalInfo() || null == form.getOrderSplitInfo()) {
                    System.out.println("=====================" + act.getTaskDefKey() + "=====" + act.getTaskName() + "===" + act.getProcInsId());
                }
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
        if (isExists) result.add(form);
    }

    
    private boolean filterTaskState(List<String> taskStateList, Act act) {
        // 正常
        if (act.getTaskConsumTime() < 80) {
            act.setTaskState(WorkFlowConstants.NORMAL);
            if (taskStateList != null && !taskStateList.isEmpty() && !taskStateList.contains(WorkFlowConstants.NORMAL)) {
                return false;
            }
            // 既将到期
        } else if (act.getTaskConsumTime() >= 80 && act.getTaskConsumTime() < 100) {
            act.setTaskState(WorkFlowConstants.ONLY_WILL_OVER_TIME);
            if (taskStateList != null && !taskStateList.isEmpty() && !taskStateList.contains(WorkFlowConstants.ONLY_WILL_OVER_TIME)) {
                return false;
            }
            // 超时
        } else if (act.getTaskConsumTime() >= 100) {
            act.setTaskState(WorkFlowConstants.OVER_TIME);
            if (taskStateList != null && !taskStateList.isEmpty() && !taskStateList.contains(WorkFlowConstants.OVER_TIME)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * 根据条件获取我的关注列表
     * 
     * @param act
     * @param orderId 订单编号（支持模糊 查询）
     * @param taskStateList 任务状态列表
     * @param hurryFlag 订单加急标记
     * @param shopName 商户名称（支持模糊查询）
     * @return
     * @date 2017年10月31日
     * @author yunnex
     * @throws ParseException 
     */
    public List<Act> getFollowTaskList(WorkFlowQueryForm form) throws ParseException {
        List<String> taskStateList = StringUtils.isNotBlank(form.getTaskStateList()) ? Arrays.asList(form.getTaskStateList().split(",")) : null;
        List<String> goodsType = StringUtils.isNotBlank(form.getGoodsType()) ? Arrays.asList(form.getGoodsType().split(",")) : null;
        Integer hurryFlag = StringUtils.isNotBlank(form.getHurryFlag()) ? Integer.parseInt(form.getHurryFlag()) : null;
        // 过滤分单信息表编号
        List<String> orderSplitList = erpOrderSplitInfoService.findFollowOrderByParams(form.getShopName(), form.getOrderNumber(), hurryFlag,
                        goodsType);
        
        // 过滤提交商户资料录入对象编号
        List<String> shopDataInputList = erpShopDataInputService.findFollowByParams(form.getShopName(), form.getOrderNumber());
        
        List<Act> actList = new ArrayList<Act>();
        int taskCount = 0, followCount = 0;
        // 流程编号
        for (String procInsId : orderSplitList) {
            Act act;
            ProcessInstance processInstance = this.actTaskService.getProcIns(procInsId);
            if (processInstance == null) continue;
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
            for (Task task : tasks) {
                taskCount++;
                act = new Act();
                act.setTask(task);
                act.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
                // 如果当前任务的处理人为登录用户，则不显示在我的关注列表中
                if (task.getAssignee().equals(UserUtils.getUser().getId())) {
                    continue;
                }
                followCount++;
                act.setBusinessId(procInsId);
                act.setTaskId(task.getId());
                act.setVars(task.getProcessVariables());
                // 设置表单属性
                setFormProperty(act);
                ErpOrderSplitInfo splitRow = erpOrderSplitInfoService.getByProsIncId(procInsId);
                int taskHours = splitRow.getHurryFlag() == ErpOrderSplitInfo.HURRY_FLAG_YES ? act.getUrgentTaskDateHours() : act.getTaskDateHours();
                HistoricTaskInstanceEntity historicTask = (HistoricTaskInstanceEntity) historyService.createHistoricTaskInstanceQuery()
                                .taskId(task.getId()).singleResult();
                act.setTaskStarterDate(historicTask.getStartTime());
                act.setTaskConsumTime(computingTaskSchedule(act.getTaskStarterDate(), taskHours));
                boolean flag = filterTaskState(taskStateList, act);
                try {
                    act.setTaskBusinessEndDate(erpHolidaysService.enddate(act.getTaskStarterDate(), taskHours));
                } catch (ParseException e) {
                    act.setTaskBusinessEndDate(null);
                }
                if (flag) {
                    actList.add(act);
                }
            }
        }
        for(String procInsId : shopDataInputList){
            Act act;
            ProcessInstance processInstance = this.actTaskService.getProcIns(procInsId);
            if (processInstance == null) continue;
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
            for (Task task : tasks) {
                act = new Act();
                act.setTask(task);
                act.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
                // 如果当前任务的处理人为登录用户，则不显示在我的关注列表中
                if (task.getAssignee().equals(UserUtils.getUser().getId())) {
                    continue;
                }
                act.setBusinessId(procInsId);
                act.setTaskId(task.getId());
                act.setVars(task.getProcessVariables());
                // 设置表单属性
                setFormProperty(act);
                
                HistoricTaskInstanceEntity historicTask = 
                    (HistoricTaskInstanceEntity) historyService.createHistoricTaskInstanceQuery().taskId(task.getId()).singleResult();
                act.setTaskStarterDate(historicTask.getStartTime());
                act.setTaskConsumTime(null);
                act.setTaskState(WorkFlowConstants.NORMAL);
                actList.add(act);
            }
        }
        return actList;
    }

    /**
     * 
     * 根据条件获取我的关注列表
     * 
     * @param act
     * @param orderId 订单编号（支持模糊 查询）
     * @param taskStateList 任务状态列表
     * @param hurryFlag 订单加急标记
     * @param shopName 商户名称（支持模糊查询）
     * @return
     * @throws ParseException 
     */
    public List<FlowForm> getFollowTaskListNew(WorkFlowQueryForm queryForm, String detailType, String followTaskDetailWrap) throws ParseException {
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
                wrapJykFlowForm(result, form, detailType, followTaskDetailWrap, null);
            } else if (ActUtils.SHOP_DATA_INPUT_FLOW[0].equals(act.getProcDefKey())) {
                wrapSdiForm(result, form, detailType, followTaskDetailWrap);
            }
        }
        LOGGER.info("我的关注返回结果：{}", result);
        return result;
    }

    /**
     * 
     * 根据条件获取待生产库订单
     * 
     * @param orderId 订单编号（支持模糊 查询）
     * @param taskStateList 任务状态列表
     * @param hurryFlag 订单加急标记
     * @param shopName 商户名称（支持模糊查询）
     * @return
     * @date 2017年10月31日
     * @author yunnex
     * @throws ParseException 
     */
    public List<Act> getPendingProductionTaskList(WorkFlowQueryForm form) throws ParseException {//NOSONAR
        List<String> taskStateList = StringUtils.isNotBlank(form.getTaskStateList()) ? Arrays.asList(form.getTaskStateList().split(",")) : null;
        List<String> goodsType = StringUtils.isNotBlank(form.getGoodsType()) ? Arrays.asList(form.getGoodsType().split(",")) : null;
        Integer hurryFlag = StringUtils.isNotBlank(form.getHurryFlag()) ? Integer.parseInt(form.getHurryFlag()) : null;
        // 过滤分单信息表编号
        List<ErpOrderSplitInfo> orderSplitList = erpOrderSplitInfoService.findListByParams(form.getShopName(), form.getOrderNumber(), hurryFlag,
                        goodsType);
        // 获取流程待办信息列表
        List<Act> taskTodoList = actTaskService.todoList(new Act());
        //设置节点信息
        Map<String,String> a=new HashMap<String, String>();
        a.put("promotion_time_determination","promotion_time_determination");
        a.put("channel_confirm_business_license", "channel_confirm_business_license");
        a.put("channel_confirm_business_qualification", "channel_confirm_business_qualification");
        List<Act> taskTodoListFilter = new ArrayList<Act>();
        for (Act act : taskTodoList) {
            ErpOrderSplitInfo splitRow = null;
            // 查看列表中是否存在任务
            for (ErpOrderSplitInfo split : orderSplitList) {
                if (split.getAct().getProcInsId() == null) {
                    continue;
                }
                if (split.getAct().getProcInsId().equals(act.getBusinessId())) {
                    splitRow = split;
                }
            }
            for(Map.Entry<String, String> entry : a.entrySet()){
                if (!entry.getValue().toString().equals(act.getTaskDefKey())) {
                    continue;
                }
                // 任务列表中，不包含的分单编号
                if (splitRow == null) {
                    LOGGER.debug("不符合条件过滤的任务Id为|ID:{}", act.getBusinessId());
                    continue;
                }
                boolean isAddFlag = false;
                if ("promotion_time_determination".equals(entry.getValue())) {
                    // 过滤设置了沟通时间，但未确定投放时间的订单
                    if (splitRow.getNextContactTime() != null && splitRow.getPromotionTime() == null) {
                        Date now=new Date();
                        if(now.getTime()<splitRow.getNextContactTime().getTime()){
                            act.setTaskStarterDate(splitRow.getNextContactTime());
                            isAddFlag = true;
                        }
                        
                    }
                    // 过滤投放日期距离现在时间超于20个工作日的订单任务
                    if (splitRow.getPromotionTime() != null) {
                        Double distanceDays = DateUtils.getDistanceOfTwoDate(this.erpHolidaysService.enddate(new Date(),JykFlowConstants.PLANNING_DATE_DISTINCT*8), splitRow.getPromotionTime());
                        if (distanceDays.intValue() > 0) {
                            act.setTaskStarterDate(splitRow.getNextContactTime());
                            isAddFlag = true;
                        }
                    }
                }
                
              //商户没有营业执照下次沟通时间
                if ("channel_confirm_business_license".equals(entry.getValue())) {
                    if (splitRow.getNextLicenseTime()!= null) {
                        Date now=new Date();
                        if(now.getTime()<splitRow.getNextLicenseTime().getTime()){
                            act.setTaskStarterDate(splitRow.getNextLicenseTime());
                            isAddFlag = true;
                        }
                        
                    }
                }
                //商户资质是否齐全下次沟通时间
                if ("channel_confirm_business_qualification".equals(entry.getValue())) {
                    if (splitRow.getNextQualificationTime()!= null) {
                        Date now=new Date();
                        if(now.getTime()<splitRow.getNextQualificationTime().getTime()){
                            act.setTaskStarterDate(splitRow.getNextQualificationTime());
                            isAddFlag = true;
                        }
                        
                    }
                }
                if (isAddFlag) {
                    setFormProperty(act);
                    int taskHours = splitRow.getHurryFlag() == ErpOrderSplitInfo.HURRY_FLAG_YES ? act.getUrgentTaskDateHours() : act.getTaskDateHours();
                    act.setTaskConsumTime(0);
                    boolean flag = filterTaskState(taskStateList, act);
                    try {
                        act.setTaskBusinessEndDate(erpHolidaysService.enddate(act.getTaskStarterDate(), taskHours));
                    } catch (ParseException e) {
                        act.setTaskBusinessEndDate(null);
                    }
                    if (flag) {
                        taskTodoListFilter.add(act);
                    }
                }
            }
        }
        
        LOGGER.info("待生产库的订单列表为|actList:{}", taskTodoList);
        return taskTodoListFilter;
    }


    /**
     * 
     * 根据条件获取待生产库订单
     * 
     * @param orderId 订单编号（支持模糊 查询）
     * @param taskStateList 任务状态列表
     * @param hurryFlag 订单加急标记
     * @param shopName 商户名称（支持模糊查询）
     * @return
     * @throws ParseException 
     */
    public List<FlowForm> getPendingProductionTaskListNew(WorkFlowQueryForm queryForm, String detailType, String followTaskDetailWrap) throws ParseException {
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
                wrapJykFlowForm(result, form, detailType, followTaskDetailWrap, null);
            }
        }
        
        LOGGER.info("我的待生产库返回结果：{}", result);
        return result;
    }

    /**
     * 
     * 根据条件获取我的关注列表
     * 
     * @param act
     * @param orderId 订单编号（支持模糊 查询）
     * @param taskStateList 任务状态列表
     * @param hurryFlag 订单加急标记
     * @param shopName 商户名称（支持模糊查询）
     * @return
     * @date 2017年10月31日
     * @author yunnex
     * @throws ParseException 
     */
    public Act getTaskDetailList(String taskId) throws ParseException {
        Act act = new Act();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        act.setTask(task);
        act.setBusinessId(task.getProcessInstanceId());
        act.setTaskId(task.getId());
        act.setVars(this.actTaskService.getProcessEngine().getRuntimeService().getVariables(task.getExecutionId()));
        act.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
        // 设置表单属性
        setFormProperty(act);
        
        if (act.getProcDef().getKey().startsWith(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0])) {
            ErpOrderSplitInfo splitRow = erpOrderSplitInfoService.getByProsIncId(task.getProcessInstanceId());
            int taskHours = splitRow.getHurryFlag() == ErpOrderSplitInfo.HURRY_FLAG_YES ? act.getUrgentTaskDateHours() : act.getTaskDateHours();
            HistoricTaskInstanceEntity historicTask = (HistoricTaskInstanceEntity) historyService.createHistoricTaskInstanceQuery().taskId(task.getId())
                            .singleResult();
            act.setTaskStarterDate(historicTask.getStartTime());
            act.setTaskConsumTime(computingTaskSchedule(act.getTaskStarterDate(), taskHours));
            try {
                act.setTaskBusinessEndDate(erpHolidaysService.enddate(act.getTaskStarterDate(), taskHours));
            } catch (ParseException e) {
                act.setTaskBusinessEndDate(null);
            }
        } else if (ActUtils.SHOP_DATA_INPUT_FLOW[0].equals(act.getProcDef().getKey())) {
            HistoricTaskInstanceEntity historicTask = 
                           (HistoricTaskInstanceEntity) historyService.createHistoricTaskInstanceQuery().taskId(task.getId()).singleResult();
            act.setTaskStarterDate(historicTask.getStartTime());
        }else if (ActUtils.MICROBLOG_PROMOTION_FLOW[0].equals(act.getProcDef().getKey())||ActUtils.FRIENDS_PROMOTION_FLOW[0].equals(act.getProcDef().getKey())) {
            HistoricTaskInstanceEntity historicTask = 
                            (HistoricTaskInstanceEntity) historyService.createHistoricTaskInstanceQuery().taskId(task.getId()).singleResult();
             act.setTaskStarterDate(historicTask.getStartTime());
         }
        return act;
    }

    public Task getTaskById(String taskId) {
        return taskService.createTaskQuery().includeProcessVariables().taskId(taskId).singleResult();
    }

    /**
     * 完成服务，自动进入下一节点
     *
     * @param taskId
     * @param procInsId
     * @param comment
     * @param vars
     * @date 2017年11月2日
     * @author yunnex
     */
    public void completeFlow(String folwUserType, String taskId, String procInsId, String comment, Map<String, Object> vars) {
        if (vars == null) {
            vars = Maps.newHashMap();
        }
        String userId = this.erpOrderFlowUserService.findListByFlowId(procInsId, folwUserType).getUser().getId();
        vars.put("TaskUser", userId);
        vars.put(folwUserType, userId);
        this.actTaskService.complete(taskId, procInsId, comment, vars);
        updateSubTask(procInsId);
        // 插入下一步所需要的
        insertSubTask(taskId, procInsId, userId);
    }
    
    /**
     * 完成服务，自动进入下一节点(针对下一步存在多任务多角色时使用)
     *
     * @param taskId
     * @param procInsId
     * @param comment
     * @param vars
     * @date 2017年11月2日
     * @author yunnex
     */
    public void completeFlow2(String[] folwUserTypes, String taskId, String procInsId, String comment, Map<String, Object> vars) {
        if (vars == null) {
            vars = Maps.newHashMap();
        }
        for(String folwUserType : folwUserTypes){
            ErpOrderFlowUser flowUser = this.erpOrderFlowUserService.findListByFlowId(procInsId, folwUserType);
            if(flowUser!=null){
                User user = flowUser.getUser();
                vars.put(folwUserType, user.getId());
                vars.put("TaskUser", user.getId());
            }
        }
        
    	ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult();
    	String processDefinitionKey = pi.getProcessDefinitionKey();
    	
        this.actTaskService.complete(taskId, procInsId, comment, vars);
        
        if(processDefinitionKey!=null&&processDefinitionKey.startsWith(JykFlowConstants.JYK_FLOW)) {
            updateSubTask(procInsId);
            // 插入下一步所需要的
            insertSubTask(taskId, procInsId, null);
        }
    }
    
    
    /**
     * 兼容老流程 
     *
     * @param taskId
     * @param procInsId
     * @param comment
     * @param vars
     * @date 2017年11月2日
     * @author yunnex
     */
    public void completeFlowCompatibility(Map<String,String> userMap, String taskId, String procInsId, String comment, Map<String, Object> vars) 
    {
        if (vars == null) 
        {
            vars = Maps.newHashMap();
        }
        for(Map.Entry<String, String> entry: userMap.entrySet())
        {
               ErpOrderFlowUser flowUser = this.erpOrderFlowUserService.findListByFlowId(procInsId, entry.getValue());
               if(flowUser!=null)
               {
                   User user = flowUser.getUser();
                   vars.put(entry.getKey(), user.getId());
               }
         }
        this.actTaskService.complete(taskId, procInsId, comment, vars);
        updateSubTask(procInsId);
        // 插入下一步所需要的
        insertSubTask(taskId, procInsId, null);
    }
    
    
    // 更新子任务的taskId为新（当前）的taskId
    private void updateSubTask(String processInstanceId) {
        List<Task> currentTasks = workFlowMonitorService.getCurrentTasks(processInstanceId);
        if (CollectionUtils.isEmpty(currentTasks))  return;
        for (Task task : currentTasks) {
            updateOldTaskId(processInstanceId, task.getTaskDefinitionKey());
        }
    }
   
    private void insertSubTask(String taskId, String procInsId, String userId) {
        ProcessInstance processInstance = this.actTaskService.getProcIns(procInsId);
        if (processInstance == null) {
            return;
        }
        List<Task> tasks = taskService.createTaskQuery().includeProcessVariables().processInstanceId(processInstance.getId()).list();

        for (Task task : tasks) {
            // 获取子任务显示
            Map<String, Object> freemarkerMap = new HashMap<String, Object>();
            freemarkerMap.put("vars", task.getProcessVariables());
            String taskStr = serviceFreeMarker.getContent(task.getTaskDefinitionKey() + ".ftl", freemarkerMap);
            if (StringUtils.isEmpty(taskStr))
                continue;
            Document doc = Jsoup.parseBodyFragment(taskStr);
            // 获取当前任务的子任务
            Elements elements = doc.getElementsByClass("subTask");
            for (Iterator<Element> it = elements.iterator(); it.hasNext();) {
                Element e = (Element) it.next();
                if ((e.text() != null && !"".equals(e.text())) && (e.attr("value") != null && !"".equals(e.attr("value")))) {
                    ErpOrderSubTask subTask = new ErpOrderSubTask();
                    subTask.setTaskId(task.getId());
                    subTask.setSplitId(procInsId);
                    subTask.setSubTaskPerson(task.getAssignee());
                    subTask.setState("0");
                    subTask.setSubTaskDetail(e.text());
                    subTask.setSubTaskId(e.attr("value"));
                    // 如果当前子任务已经存在，则不插入子任务信息表
                    if (this.erpOrderSubTaskService.getSubTask(task.getId(), e.attr("value")) == null) {
                        erpOrderSubTaskService.save(subTask);
                    }
                }
            }
        }
    }

    /**
     * 设置表单属性
     *
     * @param task
     * @return
     * @date 2017年10月31日
     * @author yunnex
     */
    private Act setFormProperty(Act task) {
        // 获取工作流表单属性
        TaskFormData st = formService.getTaskFormData(task.getTaskId());
        List<FormProperty> fromProperty = st.getFormProperties();
        for (FormProperty formProperty : fromProperty) {
            if (WorkFlowConstants.TASK_DATE_HOURS.equals(formProperty.getName())) {
                task.setTaskDateHours(Integer.valueOf(formProperty.getId()));
            }
            if (WorkFlowConstants.URGENT_TASK_DATEHOURS.equals(formProperty.getName())) {
                task.setUrgentTaskDateHours(Integer.valueOf(formProperty.getId()));
            }
            // 如果表单属性中存在指派用户分组
            if (WorkFlowConstants.TASK_USER_ROLE.equals(formProperty.getName())) {
                task.setTaskUserRole(formProperty.getId());
            }
            if (WorkFlowConstants.TASK_USER_ROLE2.equals(formProperty.getName())) {
                task.setTaskUserRole2(formProperty.getId());
            }
        }
        if (task.getTaskDateHours() == null) {
            task.setTaskDateHours(0);
        }
        if (task.getUrgentTaskDateHours() == null) {
            task.setUrgentTaskDateHours(0);
        }
        return task;
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
        float startDateLong = (erpHolidaysService.calculateHours(staterDate, new Date()))*60*60*1000;
        float taskHoursLong = (taskHours * 60 * 60 * 1000);
        return (int) ((startDateLong / taskHoursLong) * 100);
    }

    /**
     * 确认子任务完成
     *
     * @param procInsId
     * @param channelList
     * @param taskId
     * @date 2017年11月5日
     * @author yunnex
     */
    @Transactional(readOnly = false)
    public void submitSubTask(String procInsId, String channelList, String taskId) {
        // 先将任务所有状态复原，再更新，如果channelList为空，表示全部取消勾选
        this.erpOrderSubTaskService.updateTaskState(taskId, "0");
        if (StringUtils.isBlank(channelList)) {
            return;
        }
        String channel[] = channelList.split(",");
        for (int i = 0; i < channel.length; i++) {
            // 修改子任务完成状态
            this.erpOrderSubTaskService.updateState(taskId, channel[i]);
        }
    }

    /**
     * 任务进度跟踪
     *
     * @param taskId
     * @return
     * @date 2017年11月5日
     * @author yunnex
     */
    
    public FlowHistory getTaskHistoicFlow(String procInsId) {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(procInsId)
                        .orderByHistoricActivityInstanceEndTime().desc().list();
        /*List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
        		.processInstanceId(procInsId)
        		.orderByHistoricTaskInstanceEndTime()
        		.desc()
        		.list();*/
       /* List<HistoricTaskInstance> list1 = historyService.createNativeHistoricTaskInstanceQuery()
                        .sql("SELECT * FROM  ACT_HI_TASKINST WHERE PROC_INST_ID_=#{procInsId} order by END_TIME_ desc")
                        .parameter("procInsId",procInsId).list();*/
        FlowHistory history = new FlowHistory();
        List<TaskHistory> taskHistoryList = new ArrayList<TaskHistory>();
        List<SubTaskHistory> subTaskHistoryList;
        TaskHistory taskHistory;
        SubTaskHistory subTaskHistory;
        for (int i = 0; i < list.size(); i++) {
        	HistoricActivityInstance histIns = list.get(i);
            if (StringUtils.isBlank(histIns.getAssignee())) {
                continue;
            }
            subTaskHistoryList = new ArrayList<SubTaskHistory>();
            taskHistory = new TaskHistory();
            taskHistory.setSort(histIns.getEndTime() != null ? (Integer.MAX_VALUE - i) : Integer.MAX_VALUE);
            taskHistory.setIsFinished(histIns.getEndTime() != null ? 1 : 0);
            taskHistory.setTaskTime(histIns.getEndTime());
            taskHistory.setStartTaskTime(histIns.getStartTime());
            taskHistory.setTaskId(histIns.getId());
            taskHistory.setTaskPerson(userDao.getUserByTaskId(histIns.getTaskId())==null?UserUtils.get(histIns.getAssignee())==null? "未知离职人员":UserUtils.get(histIns.getAssignee()).getName():userDao.getUserByTaskId(histIns.getTaskId()).getName());
            //taskHistory.setTaskPerson(UserUtils.get(histIns.getAssignee()).getName());
            taskHistory.setTaskName(histIns.getActivityName());
            /*String taskDefef= histIns.getActivityId();
            if(taskDefef.startsWith(DeliveryFlowConstant.VISIT_SERVICE_SUBSCRIBE_PUBLIC)||taskDefef.startsWith(DeliveryFlowConstant.VISIT_SERVICE_APPLY_PUBLIC)
                            ||taskDefef.startsWith(DeliveryFlowConstant.VISIT_SERVICE_REVIEW_PUBLIC)||taskDefef.startsWith(DeliveryFlowConstant.VISIT_SERVICE_MODIFY_PUBLIC)
                            ||taskDefef.startsWith(DeliveryFlowConstant.VISIT_SERVICE_REMIND_PUBLIC)||taskDefef.startsWith(DeliveryFlowConstant.VISIT_SERVICE_COMPLETE_PUBLIC)
                            ){
                HistoricVariableInstance hiVariableServiceType = historyService.createHistoricVariableInstanceQuery()
                                .processInstanceId(procInsId).variableName(DeliveryFlowConstant.SERVICE_TYPE).singleResult();
                String serviceType = "";
                if(null!=hiVariableServiceType){
                    serviceType = hiVariableServiceType.getValue()+StringUtils.EMPTY;
                }
                HistoricVariableInstance hiVariableVisitType = historyService.createHistoricVariableInstanceQuery()
                                .processInstanceId(procInsId).variableName(DeliveryFlowConstant.VISIT_TYPE).singleResult();
                String visitType = "";
                if(null!=hiVariableVisitType){
                    visitType = hiVariableVisitType.getValue()+StringUtils.EMPTY;
                }
                HistoricVariableInstance firstServicemsign = historyService.createHistoricVariableInstanceQuery()
                                .processInstanceId(procInsId).variableName(DeliveryFlowConstant.FIRST_SERVICE_M_SIGN).singleResult();
                String msign ="";
                if(null!=firstServicemsign){
                    msign = firstServicemsign.getValue()+StringUtils.EMPTY;
               }
                Map<String,String>map=DeliveryProcessOperate.nodeExtensionNameMap;
                String name="";
                if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(serviceType)&&DeliveryFlowConstant.VISIT_TYPE_FMPS_M.equals(visitType)
                                &&DeliveryFlowConstant.SERVICE_M_SIGN.equals(msign)){
                    name=map.get(DeliveryFlowConstant.VISIT_TYPE_FMPS_M);
                }else if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(serviceType)&&DeliveryFlowConstant.VISIT_TYPE_FMPS_M.equals(visitType)
                                &&!DeliveryFlowConstant.SERVICE_M_SIGN.equals(msign)){
                    name=map.get(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI);
                }else{
                    name=map.get(serviceType); 
                }
                taskHistory.setTaskName(histIns.getActivityName().concat("(").concat(name).concat(")"));
            }*/
            List<ErpOrderSubTask> subTaskList = this.erpOrderSubTaskService.getSubTaskList(histIns.getTaskId());
            for (ErpOrderSubTask erpOrderSubTask : subTaskList) {
                subTaskHistory = new SubTaskHistory();
                subTaskHistory.setIsFinished(erpOrderSubTask.getState());
                subTaskHistory.setStartTaskTime(histIns.getStartTime());
                //去掉子任务名称中最后一个冒号
                String subTaskDetail = erpOrderSubTask.getSubTaskDetail();
                if(subTaskDetail.endsWith(":")||subTaskDetail.endsWith("：")) {
                	subTaskDetail = subTaskDetail.substring(0, subTaskDetail.length()-1);
                }
                subTaskHistory.setTaskName(subTaskDetail);
                subTaskHistory.setTaskPerson(UserUtils.get(histIns.getAssignee())==null?"未知人员":UserUtils.get(histIns.getAssignee()).getName());
                subTaskHistory.setTaskTime(erpOrderSubTask.getUpdateDate());
                subTaskHistoryList.add(subTaskHistory);
            }
            taskHistory.setSubTaskHistroy(subTaskHistoryList);
            taskHistoryList.add(taskHistory);
        }

        Collections.sort(taskHistoryList, new Comparator<TaskHistory>() {
            
            // 重写排序规则
            public int compare(TaskHistory o1, TaskHistory o2) {
                if(o1.getIsFinished()==0){
                    return -1;
                }
                return o2.getSort()-o1.getSort();
            }
        });

        history.setTaskHistory(taskHistoryList);

        return history; 
    }

    /**
     * 
     * 根据条件获取我的待办列表
     * 
     * @param act
     * @param orderId 订单编号（支持模糊 查询）
     * @param taskStateList 任务状态列表
     * @param hurryFlag 订单加急标记
     * @param shopName 商户名称（支持模糊查询）
     * @return
     * @date 2017年10月31日
     * @author yunnex
     * @throws ParseException 
     */
    public List<Act> todoTeamList(WorkFlowQueryForm form, List<String> userIds) throws ParseException {//NOSONAR
        List<String> taskStateList = StringUtils.isNotBlank(form.getTaskStateList()) ? Arrays.asList(form.getTaskStateList().split(",")) : null;
        List<String> goodsType = StringUtils.isNotBlank(form.getGoodsType()) ? Arrays.asList(form.getGoodsType().split(",")) : null;
        Integer hurryFlag = StringUtils.isNotBlank(form.getHurryFlag()) ? Integer.parseInt(form.getHurryFlag()) : null;
        // 过滤分单信息表编号
        List<ErpOrderSplitInfo> orderSplitList = 
                erpOrderSplitInfoService.findListByParams(form.getShopName(), form.getOrderNumber(), hurryFlag, goodsType);
        // 获取流程待办信息列表
        List<Act> taskTodoList = actTaskService.todoTeamList(userIds);
        List<Act> taskTodoListFilter = new ArrayList<Act>();
        for (Act act : taskTodoList) {
            ErpOrderSplitInfo splitRow = null;
            // 查看列表中是否存在任务
            for (ErpOrderSplitInfo split : orderSplitList) {
                if (split.getAct().getProcInsId() == null) {
                    continue;
                }
                if (split.getAct().getProcInsId().equals(act.getBusinessId())) {
                    splitRow = split;
                }
            }
            // 任务列表中，不包含的分单编号
            if (splitRow == null) {
                LOGGER.debug("不符合条件过滤的任务Id为|ID:{}", act.getBusinessId());
                continue;
            }
            
            //与商户沟通推广时间
            if("promotion_time_determination".equals(act.getTaskDefKey())){
                // 过滤设置了沟通时间，但未确定投放时间的订单
                if (splitRow.getNextContactTime() != null && splitRow.getPromotionTime() == null) {
                    Date now=new Date();
                    if(now.getTime()<splitRow.getNextContactTime().getTime()){
                        LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                        continue;
                    }
                    act.setTaskStarterDate(splitRow.getNextContactTime());
                }
                // 过滤投放日期距离现在时间超于20个工作日的订单任务
                if (splitRow.getPromotionTime() != null) {
                    Double distanceDays = DateUtils.getDistanceOfTwoDate(this.erpHolidaysService.enddate(new Date(),JykFlowConstants.PLANNING_DATE_DISTINCT*8), splitRow.getPromotionTime());
                    if (distanceDays.intValue() > 0) {
                        LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                        continue;
                    }
                }
            }
            //商户没有营业执照下次沟通时间
            if("channel_confirm_business_license".equals(act.getTaskDefKey())){
                // 过滤设置了沟通时间，但未确定投放时间的订单
                if (splitRow.getNextLicenseTime() != null) {
                    Date now=new Date();
                    if(now.getTime()<splitRow.getNextLicenseTime().getTime()){
                        LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                        continue;
                    }
                    act.setTaskStarterDate(splitRow.getNextLicenseTime());
                }
            }
             //商户资质是否齐全下次沟通时间
            if("channel_confirm_business_qualification".equals(act.getTaskDefKey())){
                // 过滤设置了沟通时间，但未确定投放时间的订单
                if (splitRow.getNextQualificationTime() != null) {
                    Date now=new Date();
                    if(now.getTime()<splitRow.getNextQualificationTime().getTime()){
                        LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                        continue;
                    }
                    act.setTaskStarterDate(splitRow.getNextQualificationTime());
                }
            }
            
            setFormProperty(act);
            int taskHours = splitRow.getHurryFlag() == ErpOrderSplitInfo.HURRY_FLAG_YES ? act.getUrgentTaskDateHours() : act.getTaskDateHours();
            act.setTaskConsumTime(computingTaskSchedule(act.getTaskStarterDate(), taskHours));
            boolean flag = filterTaskState(taskStateList, act);
            try {
                act.setTaskBusinessEndDate(erpHolidaysService.enddate(act.getTaskStarterDate(), taskHours));
            } catch (ParseException e) {
                act.setTaskBusinessEndDate(null);
            }
            if (flag) {
                taskTodoListFilter.add(act);
            }
        }
        LOGGER.info("条件过滤后剩余的任务列表为|actList:{}", taskTodoList);
        return taskTodoListFilter;
    }
    
    /**
     * 
     * 根据条件获取我的待办列表
     * 
     * @param orderId 订单编号（支持模糊 查询）
     * @param taskStateList 任务状态列表
     * @param hurryFlag 订单加急标记
     * @param shopName 商户名称（支持模糊查询）
     * @return
     * @throws ParseException 
     */
    public List<FlowForm> getTeamTasksNew(WorkFlowQueryForm queryForm, String detailType, String followTaskDetailWrap) throws ParseException {
        if(null!=queryForm.getTeamUserId()&&!"".equals(queryForm.getTeamUserId())){
        	wrapTeamUserIds(queryForm);
        	queryForm.getUserIds().clear();
            List<String> list=new ArrayList<String>();
            list.add(queryForm.getTeamUserId());
            queryForm.setUserIds(list);
        }else{
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
                wrapJykFlowForm(result, form, detailType, followTaskDetailWrap, null);
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
     * 获取我的团队任务统计数据
     *
     * @param workFlowQueryForm
     * @return
     * @throws ParseException
     * @date 2018年1月11日
     */
    public Map<String, Object> getTeamTaskStat(WorkFlowQueryForm workFlowQueryForm) throws ParseException {
        wrapTeamUserIds(workFlowQueryForm);
        splitOrderNum(workFlowQueryForm);
        workFlowQueryForm.setAssignee(UserUtils.getUser().getId());
        workFlowQueryForm.setIsPage(0);
        List<FlowForm> tasks = actDao.findTeamTasks(workFlowQueryForm);
        Map<String, Object> map = calcStat(tasks);
        return map;
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
        if (StringUtils.isNotBlank(teamId) && !"1".equals(teamId)) {    // 指定团队成员
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
     * 根据条件获取我的待办列表(针对之前的查询逻辑进行优化,提高查询的响应速度)
     * 
     * @param form 封装的查询参数
     * @param userIds 团队人员id集合
     * @return
     * @date 2017年12月27日
     * @author SunQ
     * @throws ParseException 
     */
    public List<Act> todoTeamList2(WorkFlowQueryForm form, List<String> userIds) throws ParseException {
        List<String> taskStateList = StringUtils.isNotBlank(form.getTaskStateList()) ? Arrays.asList(form.getTaskStateList().split(",")) : null;
        List<String> goodsType = StringUtils.isNotBlank(form.getGoodsType()) ? Arrays.asList(form.getGoodsType().split(",")) : null;
        Integer hurryFlag = StringUtils.isNotBlank(form.getHurryFlag()) ? Integer.parseInt(form.getHurryFlag()) : null;
        
        // 获得订单与任务的对应关系列表
        List<ErpOrderSplitInfo> orderSplitList = 
                erpOrderSplitInfoService.findListOrderInfoAndTask(form.getShopName(), form.getOrderNumber(), hurryFlag, goodsType, userIds);
        
        // 获取流程待办的任务列表
        Map<String, Act> map = actTaskService.todoTeamList2(userIds);
        
        List<Act> taskTodoListFilter = new ArrayList<Act>();
        for(ErpOrderSplitInfo orderSplit : orderSplitList){
            Act act = map.get(orderSplit.getTaskId());
            if ("promotion_time_determination".equals(act.getTaskDefKey())) {
                // 过滤设置了沟通时间，但未确定投放时间的订单
                if (orderSplit.getNextContactTime() != null && orderSplit.getPromotionTime() == null) {
                    LOGGER.debug("待生产库订单|splitOrderInfo:{}", orderSplit);
                    continue;
                }
                // 过滤投放日期距离现在时间超于20个工作日的订单任务
                if (orderSplit.getPromotionTime() != null) {
                    Double distanceDays = DateUtils.getDistanceOfTwoDate(this.erpHolidaysService.enddate(new Date(),JykFlowConstants.PLANNING_DATE_DISTINCT*8), orderSplit.getPromotionTime());
                    if (distanceDays.intValue() > 0) {
                        LOGGER.debug("待生产库订单|splitOrderInfo:{}", orderSplit);
                        continue;
                    }
                }
            }
            
            //设置表单属性
            setFormProperty(act);
            int taskHours = orderSplit.getHurryFlag() == ErpOrderSplitInfo.HURRY_FLAG_YES ? act.getUrgentTaskDateHours() : act.getTaskDateHours();
            act.setTaskConsumTime(computingTaskSchedule(act.getTaskStarterDate(), taskHours));
            boolean flag = filterTaskState(taskStateList, act);
            try {
                act.setTaskBusinessEndDate(erpHolidaysService.enddate(act.getTaskStarterDate(), taskHours));
            } catch (ParseException e) {
                act.setTaskBusinessEndDate(null);
            }
            if (flag) {
                taskTodoListFilter.add(act);
            }
        }
        
        return taskTodoListFilter;
    }

    /**
     * 
     * 根据条件获取待生产库订单
     * 
     * @param orderId 订单编号（支持模糊 查询）
     * @param taskStateList 任务状态列表
     * @param hurryFlag 订单加急标记
     * @param shopName 商户名称（支持模糊查询）
     * @return
     * @date 2017年10月31日
     * @author yunnex
     * @throws ParseException 
     */
    public List<Act> getPendingProductionTeamTaskList(WorkFlowQueryForm form, List<String> userIds) throws ParseException {//NOSONAR
        List<String> taskStateList = StringUtils.isNotBlank(form.getTaskStateList()) ? Arrays.asList(form.getTaskStateList().split(",")) : null;
        List<String> goodsType = StringUtils.isNotBlank(form.getGoodsType()) ? Arrays.asList(form.getGoodsType().split(",")) : null;
        Integer hurryFlag = StringUtils.isNotBlank(form.getHurryFlag()) ? Integer.parseInt(form.getHurryFlag()) : null;
        // 过滤分单信息表编号
        List<ErpOrderSplitInfo> orderSplitList = erpOrderSplitInfoService.findListByParams(form.getShopName(), form.getOrderNumber(), hurryFlag,
                        goodsType);
        // 获取流程待办信息列表
        List<Act> taskTodoList = actTaskService.todoTeamList(userIds);
        List<Act> taskTodoListFilter = new ArrayList<Act>();
        for (Act act : taskTodoList) {
            ErpOrderSplitInfo splitRow = null;
            // 查看列表中是否存在任务
            for (ErpOrderSplitInfo split : orderSplitList) {
                if (split.getAct().getProcInsId() == null) {
                    continue;
                }
                if (split.getAct().getProcInsId().equals(act.getBusinessId())) {
                    splitRow = split;
                }
            }
            // 任务列表中，不包含的分单编号
            if (splitRow == null) {
                LOGGER.debug("不符合条件过滤的任务Id为|ID:{}", act.getBusinessId());
                continue;
            }
            
            boolean isAddFlag = false;
            //与商户沟通推广时间
            if("promotion_time_determination".equals(act.getTaskDefKey())){
                // 过滤设置了沟通时间，但未确定投放时间的订单
                if (splitRow.getNextContactTime() != null && splitRow.getPromotionTime() == null) {
                    Date now=new Date();
                    if(now.getTime()<splitRow.getNextContactTime().getTime()){
                        LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                        isAddFlag = true;
                    }
                    act.setTaskStarterDate(splitRow.getNextContactTime());
                }
                // 过滤投放日期距离现在时间超于20个工作日的订单任务
                if (splitRow.getPromotionTime() != null) {
                    Double distanceDays = DateUtils.getDistanceOfTwoDate(this.erpHolidaysService.enddate(new Date(),JykFlowConstants.PLANNING_DATE_DISTINCT*8), splitRow.getPromotionTime());
                    if (distanceDays.intValue() > 0) {
                        LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                        isAddFlag = true;
                    }
                }
            }
            //商户没有营业执照下次沟通时间
            if("channel_confirm_business_license".equals(act.getTaskDefKey())){
                // 过滤设置了沟通时间，但未确定投放时间的订单
                if (splitRow.getNextLicenseTime() != null) {
                    Date now=new Date();
                    if(now.getTime()<splitRow.getNextLicenseTime().getTime()){
                        LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                        isAddFlag = true;
                    }
                    act.setTaskStarterDate(splitRow.getNextLicenseTime());
                }
            }
             //商户资质是否齐全下次沟通时间
            if("channel_confirm_business_qualification".equals(act.getTaskDefKey())){
                // 过滤设置了沟通时间，但未确定投放时间的订单
                if (splitRow.getNextQualificationTime() != null) {
                    Date now=new Date();
                    if(now.getTime()<splitRow.getNextQualificationTime().getTime()){
                        LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                        isAddFlag = true;
                    }
                    act.setTaskStarterDate(splitRow.getNextQualificationTime());
                }
            }
            
            if (isAddFlag) {
                setFormProperty(act);
                int taskHours = splitRow.getHurryFlag() == ErpOrderSplitInfo.HURRY_FLAG_YES ? act.getUrgentTaskDateHours() : act.getTaskDateHours();
                act.setTaskConsumTime(computingTaskSchedule(act.getTaskStarterDate(), taskHours));
                boolean flag = filterTaskState(taskStateList, act);
                try {
                    act.setTaskBusinessEndDate(erpHolidaysService.enddate(act.getTaskStarterDate(), taskHours));
                } catch (ParseException e) {
                    act.setTaskBusinessEndDate(null);
                }


                if (flag) {
                    taskTodoListFilter.add(act);
                }
            }
        }
        LOGGER.info("待生产库的订单列表为|actList:{}", taskTodoList);
        return taskTodoListFilter;
    }
    
    public void setVariable(String taskId, String variableName, String value) {
        this.actTaskService.getProcessEngine().getTaskService().setVariable(taskId, variableName, value);

    }

    public ProcessEngine getProcessEngine() {
        return this.actTaskService.getProcessEngine();
    }

    /**
     * 跳转（包括回退和向前）至指定活动节点
     */
    public void jumpTask(String procInsId, String currentTaskId, String targetTaskDefinitionKey, Map<String, Object> variables) {
        this.actTaskService.jumpTask(procInsId, currentTaskId, targetTaskDefinitionKey, variables);
        // 插入下一步所需要的
        insertSubTask(currentTaskId, procInsId, UserUtils.getUser().getId());
    }
    
    /**
     * 获取某个流程下的所有用户任务，不包括子流程的任务
     *
     * @param procInsId
     * @return
     * @date 2017年11月28日
     * @author hsr
     */
    public List<Map<String, Object>> getProcInsTasks(String procInsId) {
        // 所有任务
        List<ActivityImpl> activities = workFlowMonitorService.getProcessNodes(procInsId);
        // 当前任务
        List<Task> currTasks = workFlowMonitorService.getCurrentTasks(procInsId);
        // 任务的处理人
        List<Map<String, Object>> assignees = jykFlowDao.findTaskAssignee(procInsId);
        // 封装跳转任务列表数据
        List<Map<String, Object>> jumpTasks = new ArrayList<Map<String,Object>>();
        if (!CollectionUtils.isEmpty(assignees)) {
            for (ActivityImpl activity : activities) {
                // 排除分支为“不是新门店”的终点
                if ("sid-E3A66D76-38E1-42F0-85CC-8A95DF374C3A".equals(activity.getId())) {
                    continue;
                }
                Map<String, Object> jumpTask = Maps.newHashMap();
                jumpTask.put("id", activity.getId());   // 任务ID
                jumpTask.put("taskName", activity.getProperties().get("name")); // 任务名称
                for (Map<String, Object> map : assignees) {
                    if (map.get("act_id_").equals(activity.getId())) {
                        // 任务负责人
                        jumpTask.put("userId", map.get("userId"));
                        jumpTask.put("assignee", map.get("username"));
                    }
                }
                // 给当前任务添加标识
                for (Task task : currTasks) {
                    if (activity.getId().equals(task.getTaskDefinitionKey()))
                        jumpTask.put("current", 1);
                }
                jumpTasks.add(jumpTask);
            }
        }
        return jumpTasks;
    }
    
    /**
     * 跳转任务，并将原来旧的任务ID改为跳转后的新任务ID，以获取旧任务数据进行回显和修改
     *
     * @param procInsId
     * @param taskId
     * @throws Exception
     * @date 2017年12月1日
     * @author hsr
     */
    @Transactional(readOnly = false)
    public void jumpTask(String procInsId, String taskId, String userId) throws Exception {
        // 设置处理/负责人
        Map<String, Object> vars = Maps.newHashMap();
        ActDefExt actDefExt = actDefExtService.getByActId(taskId);  // 获取任务负责人变量名
        vars.put(actDefExt.getAssignee(), userId);
        
        // 将指定任务变为当前任务
        workFlowMonitorService.jump(procInsId, vars, taskId);
     
        updateOldTaskId(procInsId, taskId);
    }

    // 修改子任务的旧TaskId为新（当前任务，即跳转后的任务）的TaskId
    private void updateOldTaskId(String procInsId, String taskDefKey) {
        // 获取旧任务
        HistoricTaskInstance lastHistoryTask = actTaskService.getLastHistoryTask(procInsId, taskDefKey);
        // 以前没有处理过该任务,不需要修改旧任务ID
        if (lastHistoryTask == null)
            return;

        // 获取新（当前）任务
        List<Task> currentTasks = workFlowMonitorService.getCurrentTasks(procInsId);
        String newTaskId = null;
        for (Task task : currentTasks) {
            if (task.getTaskDefinitionKey().equals(taskDefKey)) {
                newTaskId = task.getId();
            }
        }
        if (newTaskId != null) {
            // 修改旧任务ID为新任务ID(子任务表)
            erpOrderSubTaskService.updateTaskId(newTaskId, lastHistoryTask.getId());
        }
    }
    
    /*========================================add by SunQ 商户资料录入流程操作 start=========================================*/
    /**
     * 启动商户资料录入工作流
     *
     * @param operateManagerInterface
     * @param shopDataInputId
     * @return
     * @date 2017年12月8日
     * @author SunQ
     */
    public boolean startShopDataInputWorkFlow(String operateManagerInterface, String orderId, String shopDataInputId) {
        
        Map<String, Object> vars = Maps.newHashMap();
        // 设置任务处理人
        vars.put(JykFlowConstants.OPERATION_MANAGER, operateManagerInterface);
        // 启动流程
        String procInsId = 
                        actTaskService.startProcess(ActUtils.SHOP_DATA_INPUT_FLOW[0], ActUtils.SHOP_DATA_INPUT_FLOW[1], shopDataInputId, "指派运营经理成功.", vars);
        LOGGER.info("商户资料录入工作流程启动成功|流程编号:{}", procInsId);
        
        /*保存商户资料录入流程信息表*/
        SdiFlow flow = new SdiFlow();
        flow.setOrderId(orderId);
        flow.setSdiId(shopDataInputId);
        flow.setOperationManager(operateManagerInterface);
        flow.setProcInsId(procInsId);
        sdiFlowService.save(flow);
        LOGGER.info("商户资料录入流程信息表保存成功|对象:{}", flow);
        // 保存指派人员信息
        this.erpOrderFlowUserService.insertOrderFlowUser(operateManagerInterface, orderId, "", JykFlowConstants.OPERATION_MANAGER, procInsId);

        Task task = actTaskService.getCurrentTaskInfo(this.actTaskService.getProcIns(procInsId));
        // 插入下一步所需要的
        insertNextShopDataInputTask(task.getId(), procInsId, operateManagerInterface);
        return true;
    }

    /**
     * 插入商户资料录入工作流接下来需要执行的任务
     *
     * @param taskId
     * @param procInsId
     * @param userId
     * @date 2017年12月8日
     * @author SunQ
     */
    private void insertNextShopDataInputTask(String taskId, String procInsId, String userId) {
        
        ProcessInstance processInstance = this.actTaskService.getProcIns(procInsId);
        if (processInstance == null) {
            return;
        }
        List<Task> tasks = taskService.createTaskQuery().includeProcessVariables().processInstanceId(processInstance.getId()).list();

        for (Task task : tasks) {
            // 获取任务显示
            Map<String, Object> freemarkerMap = new HashMap<String, Object>();
            freemarkerMap.put("vars", task.getProcessVariables());
            String taskStr = serviceFreeMarker.getContent(task.getTaskDefinitionKey() + ".ftl", freemarkerMap);
            Document doc = Jsoup.parseBodyFragment(taskStr);

            //获取当前任务的子任务 
            Elements elements = doc.getElementsByClass("subTask");
            for(Iterator<Element> it = elements.iterator(); it.hasNext();) {
                Element e = it.next(); 
                if ((e.text() != null && !"".equals(e.text())) && (e.attr("value") != null && !"".equals(e.attr("value")))) { 
                    ErpShopDataInputSubTask erpShopDataInputSubTask = new ErpShopDataInputSubTask();
                    erpShopDataInputSubTask.setTaskId(task.getId()); 
                    erpShopDataInputSubTask.setSdiId(procInsId);
                    erpShopDataInputSubTask.setSubTaskPerson(userId); 
                    erpShopDataInputSubTask.setState("0");
                    erpShopDataInputSubTask.setSubTaskDetail(e.text()); 
                    erpShopDataInputSubTask.setSubTaskId(e.attr("value"));
                    
                    // 如果当前子任务已经存在，则不插入子任务信息表 
                    if (this.erpShopDataInputSubTaskService.getSubTask(task.getId(), e.attr("value")) == null) { 
                        erpShopDataInputSubTaskService.save(erpShopDataInputSubTask);
                    } 
                } 
            }
        }
    }
    
    /**
     * 商户资料录入完成服务，自动进入下一节点
     *
     * @param folwUserType
     * @param taskId
     * @param procInsId
     * @param comment
     * @param vars
     * @date 2017年12月9日
     * @author SunQ
     */
    public void completeShopDataInputFlow(String userId, String taskId, String procInsId, String comment, Map<String, Object> vars) {
        if (vars == null) {
            vars = Maps.newHashMap();
        }
        vars.put("taskUser", userId);
        this.actTaskService.complete(taskId, procInsId, comment, vars);
        updateSubTask(procInsId);
        // 插入下一步所需要的
        insertNextShopDataInputTask(taskId, procInsId, userId);
    }
    
    /**
     * 确认子任务完成
     *
     * @param procInsId
     * @param channelList
     * @param taskId
     * @date 2017年11月5日
     * @author yunnex
     */
    @Transactional(readOnly = false)
    public void submitShopDataInputSubTask(String procInsId, String channelList, String taskId) {
        // 先将任务所有状态复原，再更新，如果channelList为空，表示全部取消勾选
        this.erpShopDataInputSubTaskService.updateTaskState(taskId, "0");
        if (StringUtils.isBlank(channelList)) {
            return;
        }
        String[] channel = channelList.split(",");
        for (int i = 0; i < channel.length; i++) {
            // 修改子任务完成状态
            this.erpShopDataInputSubTaskService.updateState(taskId, channel[i]);
        }
    }
    /*========================================add by SunQ 商户资料录入流程操作 end=========================================*/
    
    /*========================================add by SunQ 商户支付进件流程操作 start=========================================*/
    /**
     * 启动支付进件工作流
     *
     * @param chargePerson
     * @param storeId
     * @return
     * @date 2017年12月8日
     * @author SunQ
     */
    public boolean startPayIntopiecesWorkFlow(String chargePerson, String storeId, String intopiecesType) {
        
        Map<String, Object> vars = Maps.newHashMap();
        // 设置任务处理人
        vars.put("TaskUser", chargePerson);
        
        String procInsId = "";
        if("0".equals(intopiecesType)){
            // 启动流程
            procInsId = 
                            actTaskService.startProcess(ActUtils.WECHAT_PAY_INTOPIECES_FLOW[0], ActUtils.WECHAT_PAY_INTOPIECES_FLOW[1], storeId, "启动微信支付进件工作流程成功.", vars);
            LOGGER.info("微信支付进件工作流程启动成功|流程编号:{}", procInsId);
        }else{
            // 启动流程
            procInsId = 
                            actTaskService.startProcess(ActUtils.UNION_PAY_INTOPIECES_FLOW[0], ActUtils.UNION_PAY_INTOPIECES_FLOW[1], storeId, "启动银联支付进件工作流程成功.", vars);
            LOGGER.info("银联支付工作流程启动成功|流程编号:{}", procInsId);
        }
        
        Task task = actTaskService.getCurrentTaskInfo(this.actTaskService.getProcIns(procInsId));
        // 插入下一步所需要的
        insertNextPayIntopiecesTask(task.getId(), procInsId, chargePerson);
        return true;
    }
    
    /**
     * 插入支付进件工作流接下来需要执行的任务
     *
     * @param taskId
     * @param procInsId
     * @param userId
     * @date 2017年12月8日
     * @author SunQ
     */
    private void insertNextPayIntopiecesTask(String taskId, String procInsId, String userId) {
        
        ProcessInstance processInstance = this.actTaskService.getProcIns(procInsId);
        if (processInstance == null) {
            return;
        }
        List<Task> tasks = taskService.createTaskQuery().includeProcessVariables().processInstanceId(processInstance.getId()).list();

        for (Task task : tasks) {
            // 获取任务显示
            Map<String, Object> freemarkerMap = new HashMap<String, Object>();
            freemarkerMap.put("vars", task.getProcessVariables());
            String taskStr = serviceFreeMarker.getContent(task.getTaskDefinitionKey() + ".ftl", freemarkerMap);
            Document doc = Jsoup.parseBodyFragment(taskStr);

            //获取当前任务的子任务 
            Elements elements = doc.getElementsByClass("subTask");
            for(Iterator<Element> it = elements.iterator(); it.hasNext();) {
                Element e = it.next();
                if ((e.text() != null && !"".equals(e.text())) && (e.attr("value") != null && !"".equals(e.attr("value")))) { 
                    ErpPayIntopiecesSubTask erpPayIntopiecesSubTask = new ErpPayIntopiecesSubTask();
                    erpPayIntopiecesSubTask.setTaskId(task.getId());
                    erpPayIntopiecesSubTask.setPiId(procInsId);
                    erpPayIntopiecesSubTask.setSubTaskPerson(userId);
                    erpPayIntopiecesSubTask.setState("0");
                    erpPayIntopiecesSubTask.setSubTaskDetail(e.text());
                    erpPayIntopiecesSubTask.setSubTaskId(e.attr("value"));
                    
                    // 如果当前子任务已经存在，则不插入子任务信息表 
                    if (this.erpPayIntopiecesSubTaskService.getSubTask(task.getId(), e.attr("value")) == null) { 
                        erpPayIntopiecesSubTaskService.save(erpPayIntopiecesSubTask);
                    }
                } 
            }
        }
    }
    
    /**
     * 支付进件流程完成服务，自动进入下一节点
     *
     * @param folwUserType
     * @param taskId
     * @param procInsId
     * @param comment
     * @param vars
     * @date 2017年12月9日
     * @author SunQ
     */
    public void completePayIntopiecesFlow(String userId, String taskId, String procInsId, String comment, Map<String, Object> vars) {
        if (vars == null) {
            vars = Maps.newHashMap();
        }
        vars.put("taskUser", userId);
        this.actTaskService.complete(taskId, procInsId, comment, vars);
        updateSubTask(procInsId);
        // 插入下一步所需要的
        insertNextPayIntopiecesTask(taskId, procInsId, userId);
    }
    
    /**
     * 确认子任务完成
     *
     * @param procInsId
     * @param channelList
     * @param taskId
     * @date 2017年11月5日
     * @author yunnex
     */
    @Transactional(readOnly = false)
    public void submitPayIntopiecesSubTask(String procInsId, String channelList, String taskId) {
        // 先将任务所有状态复原，再更新，如果channelList为空，表示全部取消勾选
        this.erpPayIntopiecesSubTaskService.updateTaskState(taskId, "0");
        if (StringUtils.isBlank(channelList)) {
            return;
        }
        String[] channel = channelList.split(",");
        for (int i = 0; i < channel.length; i++) {
            // 修改子任务完成状态
            this.erpPayIntopiecesSubTaskService.updateState(taskId, channel[i]);
        }
    }
    
    
    /**
     * 
     * 定时任务触发待生产Y/N标记转换
     * 
     */
    public List<Act> transformation(WorkFlowQueryForm form) throws ParseException {
        List<String> goodsType = StringUtils.isNotBlank(form.getGoodsType()) ? Arrays.asList(form.getGoodsType().split(",")) : null;
        Integer hurryFlag = StringUtils.isNotBlank(form.getHurryFlag()) ? Integer.parseInt(form.getHurryFlag()) : null;
        
        // 过滤分单信息表编号
        List<ErpOrderSplitInfo> orderSplitList = erpOrderSplitInfoService.findListByParams(form.getShopName(), form.getOrderNumber(), hurryFlag, goodsType);
        
        // 获取提交商户资料录入对象集合
        ErpShopDataInput dataInput = new ErpShopDataInput();
        dataInput.setShopName(form.getShopName());
        dataInput.setOrderNumber(form.getOrderNumber());    
        // 获取流程待办信息列表
        List<Act> taskTodoList = actTaskService.todoList(new Act());
        //设置节点信息
        Map<String,String> a=new HashMap<String, String>();
        a.put("promotion_time_determination","promotion_time_determination");
        a.put("channel_confirm_business_license", "channel_confirm_business_license");
        a.put("channel_confirm_business_qualification", "channel_confirm_business_qualification");
        List<Act> taskTodoListFilter = new ArrayList<Act>();
                // 查看列表中是否存在任务
                for (ErpOrderSplitInfo splitRow : orderSplitList) {
                //与商户沟通推广时间
                    // 过滤设置了沟通时间，但未确定投放时间的订单
                    if (splitRow.getNextContactTime() != null && splitRow.getPromotionTime() == null) {
                        Date now=new Date();
                        if(now.getTime()<splitRow.getNextContactTime().getTime()){
                    LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                            continue;
                        }
                        splitRow.setPendingProdFlag("N");
                        erpOrderSplitInfoService.save(splitRow);
                    }
                    // 过滤投放日期距离现在时间超于20个工作日的订单任务
                    if (splitRow.getPromotionTime() != null) {
                        Double distanceDays = DateUtils.getDistanceOfTwoDate(this.erpHolidaysService.enddate(new Date(),JykFlowConstants.PLANNING_DATE_DISTINCT*8), splitRow.getPromotionTime());
                        if (distanceDays.intValue() > 0) {
                    LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                            continue;
                        }
                        splitRow.setPendingProdFlag("N");
                        erpOrderSplitInfoService.save(splitRow);
                    }
                
                    // 过滤设置了沟通时间，但未确定投放时间的订单
                    if (splitRow.getNextLicenseTime() != null) {
                        Date now=new Date();
                        if(now.getTime()<splitRow.getNextLicenseTime().getTime()){
                    LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                            continue;
                        }
                        splitRow.setPendingProdFlag("N");
                        erpOrderSplitInfoService.save(splitRow);

                    }
                    // 过滤设置了沟通时间，但未确定投放时间的订单
                    if (splitRow.getNextQualificationTime() != null) {
                        Date now=new Date();
                        if(now.getTime()<splitRow.getNextQualificationTime().getTime()){
                    LOGGER.debug("待生产库订单|splitOrderInfo:{}", splitRow);
                            continue;
                        }
                        splitRow.setPendingProdFlag("N");
                        erpOrderSplitInfoService.save(splitRow);
                    }
                
                }

        LOGGER.info("条件过滤后剩余的任务列表为|actList:{}", taskTodoList);
        return taskTodoListFilter;
    }
    /*========================================add by SunQ 商户支付进件流程操作 end=========================================*/
    
}
