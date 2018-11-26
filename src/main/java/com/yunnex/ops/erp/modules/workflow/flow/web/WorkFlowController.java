package com.yunnex.ops.erp.modules.workflow.flow.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.freemarker.TemplateServiceFreeMarkerImpl;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.act.dao.ActDao;
import com.yunnex.ops.erp.modules.act.entity.Act;
import com.yunnex.ops.erp.modules.act.entity.ActDefExt;
import com.yunnex.ops.erp.modules.act.service.ActDefExtService;
import com.yunnex.ops.erp.modules.act.service.ActProcessService;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.good.category.entity.ErpGoodCategory;
import com.yunnex.ops.erp.modules.good.category.service.ErpGoodCategoryService;
import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.order.entity.ApiCompleteOrder;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderCouponOutput;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderCouponOutputService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalGoodService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpPayIntopieces;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpShopDataInput;
import com.yunnex.ops.erp.modules.shopdata.service.ErpPayIntopiecesService;
import com.yunnex.ops.erp.modules.shopdata.service.ErpShopDataInputService;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserFriendsService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserMomoService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStorePromoteAccountService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStorePromotePhotoMaterialService;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayUnionpayService;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayWeixinService;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.sys.utils.DictUtils;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.entity.ApiTeamAndUser;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamUser;
import com.yunnex.ops.erp.modules.team.service.ErpTeamService;
import com.yunnex.ops.erp.modules.team.service.ErpTeamUserService;
import com.yunnex.ops.erp.modules.workflow.channel.constant.Constants;
import com.yunnex.ops.erp.modules.workflow.channel.dto.WeiboRechargeResponseDto;
import com.yunnex.ops.erp.modules.workflow.channel.entity.JykOrderPromotionChannel;
import com.yunnex.ops.erp.modules.workflow.channel.service.ErpChannelWeiboRechargeService;
import com.yunnex.ops.erp.modules.workflow.channel.service.JykOrderPromotionChannelService;
import com.yunnex.ops.erp.modules.workflow.data.entity.JykDataPresentation;
import com.yunnex.ops.erp.modules.workflow.data.service.JykDataPresentationService;
import com.yunnex.ops.erp.modules.workflow.effect.entity.JykDeliveryEffectInfo;
import com.yunnex.ops.erp.modules.workflow.effect.service.JykDeliveryEffectInfoService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.common.WorkFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowInfoDto;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpFlowForm;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderOperateValue;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderSubTask;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpPayIntopiecesSubTask;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpShopDataInputSubTask;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpStoreWeiboReqdto;
import com.yunnex.ops.erp.modules.workflow.flow.entity.FlowHistory;
import com.yunnex.ops.erp.modules.workflow.flow.entity.JykFlow;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SdiFlow;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SubTask;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SubTaskByUser;
import com.yunnex.ops.erp.modules.workflow.flow.from.FlowForm;
import com.yunnex.ops.erp.modules.workflow.flow.from.FlowStatForm;
import com.yunnex.ops.erp.modules.workflow.flow.from.TaskUserJson;
import com.yunnex.ops.erp.modules.workflow.flow.from.WorkFlowQueryForm;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpFlowFormService;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderFileService;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderOperateValueService;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderSubTaskService;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpPayIntopiecesSubTaskService;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpShopDataInputSubTaskService;
import com.yunnex.ops.erp.modules.workflow.flow.service.FlowInfoService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowService;
import com.yunnex.ops.erp.modules.workflow.flow.service.SdiFlowService;
import com.yunnex.ops.erp.modules.workflow.flow.service.SubTaskByUserService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlow3p25Service;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowJykService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowMonitorService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 任务管理
 * 
 * @author yunnex
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/workflow")
public class WorkFlowController extends BaseController {


    @Autowired
    private ErpFlowFormService erpFlowFormService;
    @Autowired
    private ErpStorePromotePhotoMaterialService erpStorePromotePhotoMaterialService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private JykFlowService flowService;
    @Autowired
    private ErpOrderOriginalInfoService orderService;
    @Autowired
    private ErpOrderSplitInfoService orderSplitService;
    @Autowired
    private TemplateServiceFreeMarkerImpl serviceFreeMarker;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ErpGoodCategoryService goodCategoryService;
    @Autowired
    private ErpOrderSubTaskService erpOrderSubTaskService;
    @Autowired
    private ErpOrderOriginalGoodService erpOrderOriginalGoodService;
    @Autowired
    private ErpTeamUserService erpTeamUserService;
    @Autowired
    private ErpTeamService erpTeamService;
    @Autowired
    private ErpOrderOriginalGoodService goodService;
    @Autowired
    private JykDeliveryEffectInfoService jykDeliveryEffectInfoService;
    @Autowired
    private ErpOrderFileService erpOrderFileService;
    @Autowired
    private ErpShopInfoService shopService;
    @Autowired
    private ActProcessService actProcessService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private WorkFlowMonitorService workFlowMonitorService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActDefExtService actDefExtService;
    @Autowired
    private JykOrderPromotionChannelService jykOrderPromotionChannelService;
    @Autowired
    private ErpStorePromoteAccountService erpStorePromoteAccountService;
    @Autowired
    private SdiFlowService sdiFlowService;

    @Autowired
    private SubTaskByUserService subTaskByUserService;

    @Autowired
    private ErpShopDataInputService erpShopDataInputService;
    @Autowired
    private ErpShopDataInputSubTaskService erpShopDataInputSubTaskService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpPayIntopiecesService erpPayIntopiecesService;
    @Autowired
    private ErpPayIntopiecesSubTaskService erpPayIntopiecesSubTaskService;
    @Autowired
    private ErpStorePayWeixinService erpStorePayWeixinService;
    @Autowired
    private ErpStorePayUnionpayService erpStorePayUnionpayService;
    @Autowired
    private JykOrderChoiceStoreService jykOrderChoiceStoreService;
    @Autowired
    private ErpHolidaysService erpHolidaysService;
    @Autowired
    private ErpOrderOperateValueService erpOrderOperateValueService;
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private ErpStoreAdvertiserWeiboService erpStoreAdvertiserWeiboService;
    @Autowired
    private ActDao actDao;
    @Autowired
    private ErpStoreAdvertiserFriendsService erpStoreAdvertiserFriendsService;
    @Autowired
    private ErpStoreAdvertiserMomoService erpStoreAdvertiserMomoService;
    @Autowired
    private ErpOrderCouponOutputService erpOrderCouponOutputService;
    @Autowired
    private ErpChannelWeiboRechargeService erpChannelWeiboRechargeService;
    @Autowired
    private JykDataPresentationService jykDataPresentationService;
    @Autowired
    private FlowInfoService flowInfoService;

    @Autowired
    private WorkFlowJykService workFlowJykService;

    @Autowired
    private WorkFlow3p25Service workFlow3p25Service;
    @Autowired
    private TaskService taskService;
    
    /**
     * 获取我的任务
     * 
     * @param procDefKey 流程定义标识
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"tasklistOld"})
    public String taskList(WorkFlowQueryForm workFlowQueryForm, HttpServletResponse response, Model model) throws ParseException {
        String[] orderNo = splitOrderNum(workFlowQueryForm);
        List<Act> list = workFlowService.todoList(workFlowQueryForm);
        // 封装任务信息
        List<FlowForm> flowFromList = transFlowFrom(list, "tasklist", "");
        // 封装工作流统计信息
        FlowStatForm flowStatForm = setFlowStat(list, new ArrayList<Act>());
        List<ErpGoodCategory> categoryList = goodCategoryService.findList(new ErpGoodCategory());
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("list", flowFromList);
        model.addAttribute("flowStatForm", flowStatForm);
        restoreOrderNum(workFlowQueryForm, orderNo);
        return "modules/workflow/tasklist";
    }

    @RequestMapping(value = {"tasklist", ""})
    public String taskListNew(WorkFlowQueryForm workFlowQueryForm, HttpServletResponse response, Model model) throws ParseException {
        long start = System.currentTimeMillis();
        List<FlowForm> flowFromList = workFlowJykService.getTaskList(workFlowQueryForm, "tasklist", "");
        logger.info("workFlowService.todoListNew method cost [{}] ms", (System.currentTimeMillis() - start));
        model.addAttribute("list", flowFromList);
        return "modules/workflow/tasklist3p25Jyk";
    }

    /**
     * 暂停流程
     *
     * @param orderSplitInfo
     * @return
     * @date 2018年4月17日
     */
    @RequestMapping(value = "suspend")
    @ResponseBody
    public BaseResult suspend(ErpOrderSplitInfo orderSplitInfo) {
        return orderSplitService.suspend(orderSplitInfo);
    }

    @RequestMapping(value = "getTodoTaskStat")
    public @ResponseBody Map<String, Object> getTodoTaskStat(WorkFlowQueryForm workFlowQueryForm) throws ParseException {
        return workFlowService.getTodoTaskStat(workFlowQueryForm);
    }

    @RequestMapping(value = "getFollowTaskStat")
    public @ResponseBody Map<String, Object> getFollowTaskStat(WorkFlowQueryForm workFlowQueryForm) throws ParseException {
        return workFlowService.getFollowTaskStat(workFlowQueryForm);
    }

    @RequestMapping(value = "getPendingProductionTaskStat")
    public @ResponseBody Map<String, Object> getPendingProductionTaskStat(WorkFlowQueryForm workFlowQueryForm) throws ParseException {
        return workFlowService.getPendingProductionTaskStat(workFlowQueryForm);
    }


    // 还原订单号以及序号，以进行查询表单的回显
    private static void restoreOrderNum(WorkFlowQueryForm workFlowQueryForm, String[] orderNo) {
        if (orderNo != null && orderNo.length == 2)
            workFlowQueryForm.setOrderNumber(orderNo[0] + "-" + orderNo[1]);
    }

    // 拆分订单号以及序号
    private static String[] splitOrderNum(WorkFlowQueryForm workFlowQueryForm) {
        String[] orderNo = null;
        if (workFlowQueryForm.getOrderNumber() != null) {
            orderNo = workFlowQueryForm.getOrderNumber().split("-");
            workFlowQueryForm.setOrderNumber(orderNo[0]);
        }
        return orderNo;
    }

    /**
     * 获取我的关注
     * 
     * @param procDefKey 流程定义标识
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"followTaskListOld"})
    public String followTaskList(WorkFlowQueryForm workFlowQuery, HttpServletResponse response, Model model) throws ParseException {
        String[] orderNo = splitOrderNum(workFlowQuery);
        List<Act> followActList = workFlowService.getFollowTaskList(workFlowQuery);
        // 封装任务信息
        List<FlowForm> flowFromList = transFlowFrom(followActList, "followTaskList", "followTaskDetailWrap");
        // 封装工作流统计信息
        FlowStatForm flowStatForm = setFlowStat(new ArrayList<Act>(), followActList);
        List<ErpGoodCategory> categoryList = goodCategoryService.findList(new ErpGoodCategory());
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("list", flowFromList);
        model.addAttribute("flowStatForm", flowStatForm);
        restoreOrderNum(workFlowQuery, orderNo);
        return "modules/workflow/followTaskList";
    }

    /**
     * 获取我的关注
     *
     * @param workFlowQuery
     * @param response
     * @param model
     * @return
     * @throws ParseException
     * @throws Exception
     * @date 2018年1月12日
     */
    @RequestMapping(value = {"followTaskList"})
    public String followTaskListNew(WorkFlowQueryForm workFlowQuery, HttpServletResponse response, Model model) throws ParseException {
        List<FlowForm> flowFromList = workFlowJykService.getFollowTaskList(workFlowQuery, "followTaskList", "followTaskDetailWrap");
        model.addAttribute("list", flowFromList);
        return "modules/workflow/followTaskList3p25Jyk";
    }

    /**
     * 待生产库
     * 
     * @param procDefKey 流程定义标识
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"pendingProductionTaskListOld"})
    public String pendingProductionTaskList(WorkFlowQueryForm workFlowQuery, HttpServletResponse response, Model model) throws ParseException {
        String[] orderNo = splitOrderNum(workFlowQuery);
        List<Act> pendingActList = workFlowService.getPendingProductionTaskList(workFlowQuery);
        // 封装任务信息
        List<FlowForm> flowFromList = transFlowFrom(pendingActList, "pendingProductionTaskList", "");
        // 封装工作流统计信息
        ArrayList<Act> acts = new ArrayList<Act>();
        FlowStatForm flowStatForm = setFlowStat(acts, acts);
        List<ErpGoodCategory> categoryList = goodCategoryService.findList(new ErpGoodCategory());
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("list", flowFromList);
        model.addAttribute("flowStatForm", flowStatForm);
        restoreOrderNum(workFlowQuery, orderNo);
        return "modules/workflow/pendingProductionTaskList";
    }

    /**
     * 待生产库
     * 
     * @param procDefKey 流程定义标识
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"pendingProductionTaskList"})
    public String pendingProductionTaskListNew(WorkFlowQueryForm workFlowQuery, HttpServletResponse response, Model model) throws ParseException {
        List<FlowForm> list = workFlowJykService.getPendingProductionTaskListNew(workFlowQuery, "pendingProductionTaskList", "");
        model.addAttribute("list", list);
        return "modules/workflow/pendingProductionTaskList3p25Jyk";
    }


    // 我的任务数量
    @RequestMapping("todoTaskCount")
    public @ResponseBody FlowStatForm todoTaskCount(WorkFlowQueryForm workFlowQuery) throws ParseException {
        splitOrderNum(workFlowQuery);
        List<Act> followActList = workFlowService.getFollowTaskList(workFlowQuery);
        return setFlowStat(new ArrayList<Act>(), followActList);
    }

    // 我的关注数量
    @RequestMapping("followTaskCount")
    public @ResponseBody FlowStatForm followTaskCount(WorkFlowQueryForm workFlowQuery) throws ParseException {
        splitOrderNum(workFlowQuery);
        List<Act> list = workFlowService.todoList(workFlowQuery);
        return setFlowStat(list, new ArrayList<Act>());
    }

    // 我的任务和关注数量
    @RequestMapping("pendingProductionTaskCount")
    public @ResponseBody FlowStatForm pendingProductionTaskCount(WorkFlowQueryForm workFlowQuery) throws ParseException {
        splitOrderNum(workFlowQuery);
        List<Act> list = workFlowService.todoList(workFlowQuery);
        List<Act> followActList = workFlowService.getFollowTaskList(workFlowQuery);
        return setFlowStat(list, followActList);
    }

    /**
     * 完成订单展示
     * 
     * @return
     */
    @RequestMapping(value = "status")
    public String status(HttpServletRequest request, HttpServletResponse response, Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ApiCompleteOrder> orderlist = new ArrayList<ApiCompleteOrder>();
        ApiCompleteOrder order = null;
        List<ErpOrderSplitInfo> list = orderSplitService.getBystate(1);
        ErpShopInfo shop = new ErpShopInfo();
        List<ErpShopInfo> shoplist = shopService.findList(shop);
        ErpOrderOriginalInfo original = null;
        ErpOrderOriginalGood good = null;

        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                order = new ApiCompleteOrder();
                order.setOrderId(list.get(i).getOrderId());
                order.setOrderNumber(list.get(i).getOrderNumber());
                order.setSplitId(list.get(i).getSplitId());
                order.setNum(list.get(i).getNum());
                order.setShopId(list.get(i).getShopId());
                order.setGoodName(list.get(i).getGoodName());
                order.setPromotionTime(sdf.format(list.get(i).getUpdateDate()));

                // 获取分单的主订单
                original = orderService.get(list.get(i).getOrderId());
                if (original != null) {
                    order.setBuyTime(sdf.format(original.getBuyDate()));
                    order.setPhone(original.getContactNumber());
                    order.setShopName(original.getShopName());
                    order.setCname(original.getContactName());
                    order.setOrderType(original.getOrderType().toString());
                }
                good = goodService.get(list.get(i).getOriginalGoodId());
                if (good != null) {
                    order.setServerType(good.getGoodTypeName());
                }
                order.setProcInsId(list.get(i).getProcInsId());
                orderlist.add(order);
            }

        }
        model.addAttribute("list", list);
        model.addAttribute("shoplist", shoplist);
        model.addAttribute("order", orderlist);
        return "/modules/workflow/taskpPocessList";

    }

    /**
     * 完成订单条件查询展示
     * 
     * @return
     */
    @RequestMapping(value = "complete")
    public String complete(HttpServletRequest request, HttpServletResponse response, Model model, String oidAndSid, String shopId, String hurryflag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ErpOrderSplitInfo> oldlist = orderSplitService.getBystate(1);
        List<ApiCompleteOrder> orderlist = new ArrayList<ApiCompleteOrder>();
        ApiCompleteOrder order = null;
        ErpShopInfo shop = new ErpShopInfo();
        List<ErpShopInfo> shoplist = shopService.findList(shop);
        String[] StrArray = new String[10];
        String orderNumber = "";
        String splitId = "";
        if (oidAndSid != null && !"".equals(oidAndSid)) {
            StrArray = oidAndSid.split("-");
            orderNumber = StrArray[0];
            splitId = StrArray[1];
        }

        Integer hurryFlag = 0;
        if (hurryflag != null && !"".equals(hurryflag)) {
            hurryFlag = Integer.parseInt(hurryflag);
        }

        List<ErpOrderSplitInfo> list = orderSplitService.findcomplete(1, Global.NO, orderNumber, splitId, shopId, hurryFlag);
        ErpOrderOriginalInfo original = null;
        ErpOrderOriginalGood good = null;

        if (!CollectionUtils.isEmpty(list)) {

            for (int i = 0; i < list.size(); i++) {
                order = new ApiCompleteOrder();
                order.setOrderId(list.get(i).getOrderId());
                order.setOrderNumber(list.get(i).getOrderNumber());
                order.setSplitId(list.get(i).getSplitId());
                order.setNum(list.get(i).getNum());
                order.setShopId(list.get(i).getShopId());
                order.setGoodName(list.get(i).getGoodName());
                order.setPromotionTime(sdf.format(list.get(i).getUpdateDate()));
                // 获取分单的主订单
                original = orderService.get(list.get(i).getOrderId());
                if (original != null) {
                    order.setBuyTime(sdf.format(original.getBuyDate()));
                    order.setPhone(original.getContactNumber());
                    order.setShopName(original.getShopName());
                    order.setCname(original.getContactName());
                }
                good = goodService.get(list.get(i).getOriginalGoodId());
                if (good != null) {
                    order.setServerType(good.getGoodTypeName());
                }
                order.setProcInsId(list.get(0).getProcInsId());
                orderlist.add(order);
            }

        }
        if (oidAndSid != null && !"".equals(oidAndSid)) {
            model.addAttribute("orderNumber", orderNumber);
            model.addAttribute("splitId", splitId);
        }
        if (shopId != null && !"".equals(shopId)) {
            model.addAttribute("shopid", shopId);
        }
        model.addAttribute("list", oldlist);
        model.addAttribute("shoplist", shoplist);
        model.addAttribute("order", orderlist);
        return "/modules/workflow/taskpPocessList";

    }



    /**
     * 任务详情页面展示
     * 
     * @param procDefKey 流程定义标识
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"taskDetail", ""})
    public String taskDetail(@RequestParam(required = true) String taskId, @RequestParam(required = false) String followTaskDetailWrap,
                    @RequestParam(required = false) String readOnly,
                    HttpServletResponse response, Model model) throws ParseException {

        Act actDetail = workFlowService.getTaskDetailList(taskId);
        List<Act> actList = new ArrayList<Act>();
        actList.add(actDetail);
        // 封装任务信息
        List<FlowForm> flowFromList = transFlowFrom(actList, "taskDetail", StringUtils.isNotBlank(followTaskDetailWrap) ? followTaskDetailWrap : "");
        Task task = this.workFlowService.getTaskById(taskId);

        // 当前登录用户
        Principal principal = UserUtils.getPrincipal();
        model.addAttribute("userId", principal.getId());

        FlowForm flowForm = flowFromList.get(0);
        model.addAttribute("procInstId", flowForm.getAct().getProcInsId());

        // 聚引客流程
        if (actDetail.getProcDef().getKey().startsWith(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0])) {
            ErpOrderSplitInfo orderSplit = orderSplitService.getByProsIncId(task.getProcessInstanceId());
            model.addAttribute("flowFrom", flowForm);
            model.addAttribute("taskId", taskId);
            model.addAttribute("taskDefKey", task.getTaskDefinitionKey());
            model.addAttribute("splitId", orderSplit.getId());
            model.addAttribute("followTaskDetailWrap", followTaskDetailWrap);
            // 暂停原因字典
            model.addAttribute("suspendReasons", DictUtils.getDictList(FlowConstant.SUSPEND_REASON));
        }

        // 商户资料录入流程
        if ("shop_data_input_flow".equals(actDetail.getProcDef().getKey())) {
            model.addAttribute("flowFrom", flowForm);
            model.addAttribute("taskId", taskId);
            model.addAttribute("followTaskDetailWrap", followTaskDetailWrap);
        }

        // 支付进件流程
        if ("wechatpay_intopieces_flow".equals(actDetail.getProcDef().getKey()) || "unionpay_intopieces_flow"
                        .equals(actDetail.getProcDef().getKey())) {
            model.addAttribute("flowFrom", flowForm);
            model.addAttribute("taskId", taskId);
            model.addAttribute("followTaskDetailWrap", followTaskDetailWrap);
        }
        // 微博开户流程
        if ("microblog_promotion_flow".equals(actDetail.getProcDef().getKey())) {
            model.addAttribute("flowFrom", flowForm);
            model.addAttribute("taskId", taskId);
            model.addAttribute("followTaskDetailWrap", followTaskDetailWrap);
        }
        // 朋友圈开户流程
        if ("friends_promotion_flow".equals(actDetail.getProcDef().getKey())) {
            model.addAttribute("flowFrom", flowForm);
            model.addAttribute("taskId", taskId);
            model.addAttribute("followTaskDetailWrap", followTaskDetailWrap);
        }
        if (actDetail.getProcDef().getKey().startsWith(ActUtils.JYK_FLOW_NEW[0])) {
            model.addAttribute("flowUsers", erpOrderFlowUserService.getFlowUser(task.getProcessInstanceId())); // 聚引客生产流程处理人
        }
        FlowInfoDto flowInfo = flowInfoService.getFlowInfo(flowForm.getAct().getProcInsId());
        model.addAttribute("flowInfo", flowInfo);
        model.addAttribute("readOnly", readOnly);
        model.addAttribute("userMatch", task.getAssignee().equalsIgnoreCase(principal.getId()));
        return "modules/workflow/taskDetail";
    }

    /**
     * 流程进度页面
     * 
     * @param from 统计中用于标示来源
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"toTaskHistoicFlow"})
    public String toTaskHistoicFlow(String procInstId, String taskId, String flowMark, @RequestParam(required = false) String from, Model model)
                    throws ParseException {
        FlowInfoDto flowInfo = workFlow3p25Service.getFlowInfo(taskId, procInstId, flowMark);
        model.addAttribute("flowInfo", flowInfo);
        model.addAttribute("taskId", taskId);
        model.addAttribute("flowMark", flowInfo.getFlowMark());
        model.addAttribute("procInstId", procInstId);
        model.addAttribute("from", from);
        if (StringUtils.isNotBlank(taskId)){
        model.addAttribute(DeliveryFlowConstant.SERVICE_TYPE, taskService.getVariable(taskId, DeliveryFlowConstant.SERVICE_TYPE)+"");
        model.addAttribute(DeliveryFlowConstant.VISIT_TYPE, taskService.getVariable(taskId, DeliveryFlowConstant.VISIT_TYPE)+"");
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        if (processInstance != null && processInstance.getProcessDefinitionKey().startsWith(ActUtils.JYK_FLOW_NEW[0])) {
            model.addAttribute("flowUsers", erpOrderFlowUserService.getFlowUser(procInstId)); // 聚引客生产流程处理人
        }
        return "modules/workflow/taskHistoricFlow";
    }

    /**
     * 任务详情页面展示
     * 
     * @param procDefKey 流程定义标识
     * @return
     */
   @RequestMapping(value = {"toTaskHistoicFlowByProcInsId"})
   public String toTaskHistoicFlowByProcInsId(String procInsId, Model model) {
       FlowInfoDto flowInfo = flowInfoService.getFlowInfo(procInsId);
       model.addAttribute("flowInfo", flowInfo);
       model.addAttribute("procInsId", procInsId);
       return "modules/workflow/taskHistoricFlowByProcInsId";
   }
    
    /**
     * 流程进度数据
     */
    @RequestMapping(value = {"taskHistoicFlowByProcInsId"})
    @ResponseBody
    public JSONObject taskHistoicFlowByProcInsId(String procInsId) {
        FlowHistory flowHistory = this.workFlowService.getTaskHistoicFlow(procInsId);
        JSONObject resObject = new JSONObject();
        resObject.put("result", JSON.toJSONString(flowHistory));
        return resObject;
    }

    /**
     * 我的团队任务
     * 
     * 
     * @param procDefKey 流程定义标识
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"teamtasklistOld"})
    public String teamtasklist(WorkFlowQueryForm workFlowQueryForm, HttpServletRequest request, HttpServletResponse response, Model model)
                    throws ParseException {
        String selectteam = request.getParameter("selectteam");
        List<String> userIds = new ArrayList<String>();
        if (selectteam == null || "1".equals(selectteam)) {
            Principal principal = UserUtils.getPrincipal();
            // 根据团队管理员id查询到其管理员所有的团队
            List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
            List<ErpTeamUser> user = null;
            if (null != teamuser && !teamuser.isEmpty()) {
                for (int i = 0; i < teamuser.size(); i++) {
                    // 判断该团队是否已经删除
                    Integer count = erpTeamService.findteam(Global.NO, teamuser.get(i).getTeamId());
                    if (count > 0) {
                        // 循环查询该团队下所有成员
                        user = erpTeamUserService.findwhereteam(Global.NO, teamuser.get(i).getTeamId());
                        for (int j = 0; j < user.size(); j++) {
                            userIds.add(user.get(j).getUserId());
                        }
                    }
                }
            }
        } else {
            Principal principal = UserUtils.getPrincipal();
            List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
            List<ErpTeamUser> user = null;
            if (null != teamuser && !teamuser.isEmpty()) {
                for (int i = 0; i < teamuser.size(); i++) {
                    if (selectteam.equals(teamuser.get(i).getTeamId())) {
                        Integer count = erpTeamService.findteam(Global.NO, teamuser.get(i).getTeamId());
                        if (count > 0) {
                            user = erpTeamUserService.findwhereteam(Global.NO, teamuser.get(i).getTeamId());
                            for (int j = 0; j < user.size(); j++) {
                                userIds.add(user.get(j).getUserId());
                            }
                        }
                    }
                }
            }
        }

        Principal principal = UserUtils.getPrincipal();
        List<ErpTeam> team = new ArrayList<ErpTeam>();
        ErpTeam eteam = null;
        // 根据团队管理员id查询到其管理员所有的团队
        List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
        if (null != teamuser && !teamuser.isEmpty()) {
            for (int i = 0; i < teamuser.size(); i++) {
                eteam = erpTeamService.get(teamuser.get(i).getTeamId());
                if (eteam != null && "0".equals(eteam.getDelFlag())) {
                    team.add(eteam);
                }
            }
        }
        // 如果不是团队管理员,直接 抛出权限不足异常
        if (null == team || team.isEmpty()) {
            throw new AuthenticationException("你不是团队管理员，无法查看团队任务！");
        }
        model.addAttribute("team", team);


        Set set = new HashSet();
        List newuserIdsList = new ArrayList();
        set.addAll(userIds);
        newuserIdsList.addAll(set);
        List<Act> list = workFlowService.todoTeamList(workFlowQueryForm, newuserIdsList);
        // 封装任务信息
        List<FlowForm> flowFromList = transFlowFrom(list, "teamtasklist", "followTaskDetailWrap");
        List<ErpGoodCategory> categoryList = goodCategoryService.findList(new ErpGoodCategory());
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("list", flowFromList);
        model.addAttribute("selectteam", selectteam);
        return "modules/workflow/teamtasklist";
    }

    /**
     * 我的团队任务 - 处理中
     * 
     * 
     * @param procDefKey 流程定义标识
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"teamtasklist"})
    public String teamtasklistNew(WorkFlowQueryForm workFlowQueryForm, HttpServletRequest request, HttpServletResponse response, Model model)
                    throws ParseException {
        workFlowQueryForm.setPendingProdFlag("N");
        List<FlowForm> list = workFlowJykService.getTeamTasks(workFlowQueryForm, "teamtasklist", "followTaskDetailWrap");
        model.addAttribute("list", list);
        return "modules/workflow/teamtasklist3p25Jyk";
    }

    /**
     * 我的团队任务 - 处理中统计
     *
     * @param workFlowQueryForm
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "getTodoTeamTaskStat")
    public @ResponseBody Map<String, Object> getTodoTeamTaskStat(WorkFlowQueryForm workFlowQueryForm) throws ParseException {
        workFlowQueryForm.setPendingProdFlag("N");
        return workFlowService.getTeamTaskStat(workFlowQueryForm);
    }

    /**
     * 我的团队任务 - 待生产
     * 
     * 
     * @param procDefKey 流程定义标识
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"productionTeamTaskListOld"})
    public String productionTeamTaskList(WorkFlowQueryForm workFlowQueryForm, HttpServletRequest request, HttpServletResponse response, Model model)
                    throws ParseException {
        String selectteam = request.getParameter("selectteam");
        List<String> userIds = new ArrayList<String>();
        if (selectteam == null || "1".equals(selectteam)) {
            Principal principal = UserUtils.getPrincipal();
            // 根据团队管理员id查询到其管理员所有的团队
            List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
            List<ErpTeamUser> user = null;
            if (null != teamuser && !teamuser.isEmpty()) {
                for (int i = 0; i < teamuser.size(); i++) {
                    // 判断该团队是否已经删除
                    Integer count = erpTeamService.findteam(Global.NO, teamuser.get(i).getTeamId());
                    if (count > 0) {
                        // 循环查询该团队下所有成员
                        user = erpTeamUserService.findwhereteam(Global.NO, teamuser.get(i).getTeamId());
                        for (int j = 0; j < user.size(); j++) {
                            userIds.add(user.get(j).getUserId());
                        }
                    }
                }
            }
        } else {
            Principal principal = UserUtils.getPrincipal();
            List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
            List<ErpTeamUser> user = null;
            if (null != teamuser && !teamuser.isEmpty()) {
                for (int i = 0; i < teamuser.size(); i++) {
                    if (selectteam.equals(teamuser.get(i).getTeamId())) {
                        Integer count = erpTeamService.findteam(Global.NO, teamuser.get(i).getTeamId());
                        if (count > 0) {
                            user = erpTeamUserService.findwhereteam(Global.NO, teamuser.get(i).getTeamId());
                            for (int j = 0; j < user.size(); j++) {
                                userIds.add(user.get(j).getUserId());
                            }
                        }
                    }
                }
            }
        }

        Principal principal = UserUtils.getPrincipal();
        List<ErpTeam> team = new ArrayList<ErpTeam>();
        ErpTeam eteam = null;
        // 根据团队管理员id查询到其管理员所有的团队
        List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
        if (null != teamuser && !teamuser.isEmpty()) {
            for (int i = 0; i < teamuser.size(); i++) {
                eteam = erpTeamService.get(teamuser.get(i).getTeamId());
                if (eteam != null) {
                    team.add(eteam);
                }
            }
        }
        model.addAttribute("team", team);

        Set set = new HashSet();
        List newuserIdsList = new ArrayList();
        set.addAll(userIds);
        newuserIdsList.addAll(set);
        List<Act> list = workFlowService.getPendingProductionTeamTaskList(workFlowQueryForm, newuserIdsList);
        // 封装任务信息
        List<FlowForm> flowFromList = transFlowFrom(list, "productionTeamTaskList", "");
        List<ErpGoodCategory> categoryList = goodCategoryService.findList(new ErpGoodCategory());
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("list", flowFromList);
        return "modules/workflow/productionTeamTaskList";
    }


    /**
     * 我的团队任务 - 待生产
     * 
     * 
     * @param procDefKey 流程定义标识
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"productionTeamTaskList"})
    public String productionTeamTaskListNew(WorkFlowQueryForm workFlowQueryForm, HttpServletRequest request, HttpServletResponse response,
                    Model model) throws ParseException {
        workFlowQueryForm.setPendingProdFlag("Y");
        List<FlowForm> list = workFlowJykService.getTeamTasks(workFlowQueryForm, "productionTeamTaskList", "");
        model.addAttribute("list", list);
        return "modules/workflow/productionTeamTaskList3p25Jyk";
    }

    /**
     * 我的团队任务 - 待生产统计
     *
     * @param workFlowQueryForm
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "getProductionTeamTaskStat")
    public @ResponseBody Map<String, Object> getProductionTeamTaskStat(WorkFlowQueryForm workFlowQueryForm) throws ParseException {
        workFlowQueryForm.setPendingProdFlag("Y");
        return workFlowService.getTeamTaskStat(workFlowQueryForm);
    }

    /**
     * 将订单对象转换成任务列表展示的字段
     * 
     * @param orderSplitId
     * @return
     * @throws ParseException
     */
    private List<FlowForm> transFlowFrom(List<Act> taskList, String detailType, String followTaskDetailWrap) throws ParseException {
        List<FlowForm> flowFromList = new ArrayList<FlowForm>();
        for (Act act : taskList) {
            FlowForm from = new FlowForm();
            from.setAct(act);

            /* 判断任务的类型(聚引客任务或者商户资料录入任务) */
            if (act.getProcDef().getKey().startsWith(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0])) {
                from.setFlowMark(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0]);
                // 获取基本订单信息
                JykFlow flow = flowService.getByProcInstId(act.getBusinessId());
                ErpOrderSplitInfo orderSplit = orderSplitService.get(flow.getSplitId());
                ErpOrderOriginalInfo order = orderService.get(orderSplit.getOrderId());
                ErpShopInfo shop = shopService.findListByZhangbeiId(order.getShopId());
                ActDefExt actDefExt = actDefExtService.findByProcDefKeyAndActId(act.getProcDef().getKey(), act.getTaskDefKey());
                setJykVariable(act, orderSplit, shop);
                from.setShop(shop);
                from.setOrderNo(order.getOrderNumber() + "-" + orderSplit.getSplitId());
                from.setOrderTime(order.getBuyDate());
                from.setGoodName(orderSplit.getGoodName());
                from.setNum(orderSplit.getNum());
                from.setGoodType(orderSplit.getGoodTypeName());
                from.setGoodCount(String.valueOf(orderSplit.getNum()));
                from.setShopName(order.getShopName());
                from.setDeliveryTime(flow.getPromotionTime());
                from.setHurryFlag(orderSplit.getHurryFlag());
                // 回显下次联系时间
                from.setNextContactTimeStr(DateUtils.formatDateTime(orderSplit.getNextContactTime()));
                from.setAgentName(order.getAgentName());
                from.setOrderType(String.valueOf(order.getOrderType()));
                from.setTaskConsumTime(0);
                from.setOrderOriginalInfo(order);
                // 任务详细里面增加拆单信息
                from.setOrderSplitInfo(orderSplit);
                // 联系方式
                fillContactWay(from, order);
                from.setVars(act.getVars());
                from.setDeliveryTime(orderSplit.getPromotionTime());
                // 获取子任务显示
                Map<String, Object> freemarkerMap = new HashMap<String, Object>();
                // 获取子任务完成状态
                List<ErpOrderSubTask> subTaskList = this.erpOrderSubTaskService.getSubTaskList(act.getTaskId());
                /* 判断子任务列表是否为空，并找出对应的文件列表 */
                fillSubTaskList(act, freemarkerMap, subTaskList);
                // 拼装分单的任务时间
                fillSplitInfoTaskTime(act, orderSplit);
                // 任务开始时间和结束时间 放入表单
                fillTaskStartAndEndTime(act, freemarkerMap);
                // 分单任务时长
                int taskHours = setSplitTaskHours(orderSplit, actDefExt);
                fillTaskConsumTime(act, from, freemarkerMap, taskHours);
                // 根据流程图节点 表单属性设置人员信息
                setTaskUserByFlowFormArrt(act, freemarkerMap);

                setCommonTemplateInfo(detailType, followTaskDetailWrap, act, freemarkerMap);

                queryTaskUser(act, freemarkerMap);

                // 获得商户下的所有门店 并设置运营顾问
                fillStoreAndConsultant(act, shop, freemarkerMap, order);

                fillTemplateInfoByTaskKey(act, flow, shop, freemarkerMap);

                fillShopOpenOrTrans(orderSplit, freemarkerMap, shop);

                fillOrderGoodInfo(act, order, freemarkerMap);

                fillErpFlowForm(act, order, freemarkerMap, flow.getSplitId());

                fillPlanningExpert(act, freemarkerMap);
                // 广告主开通通知商户
                fillAdvertiserSuccessNotifyShop(act, flow.getSplitId(), freemarkerMap);
                // 推广状态同步
                fillPromotionStateSync(act, flow.getSplitId(), freemarkerMap);
                // 输出卡券
                fillCouponOutput(act, flow.getSplitId(), freemarkerMap);
                // 微博推广资料
                weiboRechargeData(act, flow.getSplitId(), freemarkerMap);
                // 微博推广任务详情
                weiboReachargeFlow(act, flow.getSplitId(), freemarkerMap);

                // 上传收据报告
                fillDataPresentation(act, flow.getSplitId(), freemarkerMap);

                /* 获取任务的key,并存入模板 */
                freemarkerMap.put("taskDefKey", act.getTaskDefKey());
                freemarkerMap.put("splitId", flow.getSplitId());
                freemarkerMap.put("channelService", jykOrderPromotionChannelService);
                freemarkerMap.put("isTaskDetail", true); // 是详情页
                freemarkerMap.put("taskStarterDate", DateUtils.formatDateTime(act.getTaskStarterDate()));
                freemarkerMap.put("orderInfo", order);
                freemarkerMap.put("publishToWxapp", orderSplit.getPublishToWxapp());

                //String taskStr = serviceFreeMarker.getContent("friends_promote_info_review_1.0" + ".ftl", freemarkerMap);
                String taskStr = serviceFreeMarker.getContent(act.getTaskDefKey() + ".ftl", freemarkerMap);
                boolean isExists = true;
                // 任务分组
                jykTaskGroup(flowFromList, from, taskStr, isExists);
            } else if ("shop_data_input_flow".equals(act.getProcDef().getKey())) {
                from.setFlowMark("sdi_flow");
                // 获取任务流信息
                SdiFlow flow = sdiFlowService.getByProcInstId(act.getBusinessId());
                // 获取商户资料录入信息
                ErpShopDataInput shopData = erpShopDataInputService.get(flow.getSdiId());
                // 获取订单信息
                ErpOrderOriginalInfo order = orderService.get(flow.getOrderId());
                // 获取商户信息
                ErpShopInfo shop = shopService.findListByZhangbeiId(shopData.getShopId());
                ActDefExt actDefExt = actDefExtService.findByProcDefKeyAndActId(act.getProcDef().getKey(), act.getTaskDefKey());

                from.setShop(shop);
                from.setOrderNo(order.getOrderNumber());
                from.setOrderTime(order.getBuyDate());
                from.setGoodName("商户资料录入与进件");
                from.setNum(null);
                from.setGoodType("");
                from.setGoodCount(null);
                from.setShopName(order.getShopName());
                from.setDeliveryTime(null);
                from.setHurryFlag(null);
                from.setAgentName(order.getAgentName());
                from.setOrderType(String.valueOf(order.getOrderType()));
                from.setTaskConsumTime(0);
                from.setOrderOriginalInfo(order);
                fillContactWay(from, order);
                from.setVars(act.getVars());
                from.setDeliveryTime(null);

                // 获取子任务显示
                Map<String, Object> freemarkerMap = new HashMap<String, Object>();
                // 获取子任务完成状态
                List<ErpShopDataInputSubTask> subTaskList = this.erpShopDataInputSubTaskService.getSubTaskList(act.getTaskId());

                freemarkerMap.put("subTaskList", subTaskList);

                int taskHours = 0;
                if (actDefExt != null && StringUtils.isNotBlank(actDefExt.getTaskDateHours())) {
                    taskHours = Integer.parseInt(actDefExt.getTaskDateHours());
                }

                fillTaskStartAndEndTime(act, freemarkerMap);

                fillTaskConsumTime(act, from, freemarkerMap, taskHours);


                setTaskUserByFlowFormArrt(act, freemarkerMap);
                setCommonTemplateInfo(detailType, followTaskDetailWrap, act, freemarkerMap);
                User user = UserUtils.get(act.getTask().getAssignee());
                freemarkerMap.put("taskUser", user.getName());
                StringBuffer sb = new StringBuffer();
                for (Role role : user.getRoleList()) {
                    sb.append(role.getName() + " ");
                }
                freemarkerMap.put("taskUserRole", sb.toString());
                freemarkerMap.put("taskUserId", user.getId());

                /* 获取任务的key,并存入模板 */
                freemarkerMap.put("taskDefKey", act.getTaskDefKey());
                freemarkerMap.put("splitId", flow.getSdiId());
                /* 获取商户审核信息的状态 */
                freemarkerMap.put("zhangbeiState", null != shop && null != shop.getZhangbeiState() ? shop.getZhangbeiState() : 0);
                freemarkerMap.put("zhangbeiRemark", null != shop && StringUtils.isNotBlank(shop.getZhangbeiRemark()) ? shop.getZhangbeiRemark() : "");
                freemarkerMap.put("wechatpayState", null != shop && null != shop.getWechatpayState() ? shop.getWechatpayState() : 0);
                freemarkerMap.put("wechatpayRemark",
                                null != shop && StringUtils.isNotBlank(shop.getWechatpayRemark()) ? shop.getWechatpayRemark() : "");
                freemarkerMap.put("unionpayState", null != shop && null != shop.getUnionpayState() ? shop.getUnionpayState() : 0);
                freemarkerMap.put("unionpayRemark", null != shop && StringUtils.isNotBlank(shop.getUnionpayRemark()) ? shop.getUnionpayRemark() : "");
                freemarkerMap.put("shopId", null != shop ? shop.getId() : "");
                freemarkerMap.put("shopMainId", null != shop ? shop.getId() : "");

                if ("select_extension_store_shop".equals(act.getTask().getTaskDefinitionKey()) || "select_extension_store2_shop"
                                .equals(act.getTask().getTaskDefinitionKey())) {
                    // 获得商户下的所有门店
                    List<ErpStoreInfo> storeList = erpStoreInfoService.findAllListWhereShopId("0", shop.getId());
                    freemarkerMap.put("storeList", storeList);
                }

                freemarkerMap.put("zhangbeiId", "--");
                freemarkerMap.put("passWord", "--");
                if ("conact_new_shop_shop".equals(act.getTaskDefKey()) && shop != null && StringUtils.isNotBlank(shop.getZhangbeiId())) {
                    freemarkerMap.put("zhangbeiId", shop.getZhangbeiId());
                    String passwordStr = StringUtils.rightPad(shop.getZhangbeiId(), 6, '0');
                    freemarkerMap.put("passWord", passwordStr.substring(passwordStr.length() - 6, passwordStr.length()));
                }


                if (null != shop) {
                    int storeCount = erpStoreInfoService.findCountWhereShopId("0", shop.getId());
                    freemarkerMap.put("storeCount", storeCount);
                    if (storeCount > 0) {
                        ErpStoreInfo mainStore = erpStoreInfoService.getIsmainStore("0", shop.getId(), "1");
                        freemarkerMap.put("storeId", mainStore != null ? mainStore.getId() : "");
                    }
                }

                freemarkerMap.put("isTaskDetail", true); // 是详情页
                String taskStr = serviceFreeMarker.getContent(act.getTaskDefKey() + ".ftl", freemarkerMap);
                // taskStr = serviceFreeMarker.getContent("put_info_review_3.2.ftl", freemarkerMap);

                shopTaskGroup(flowFromList, from, taskStr);

            } else if ("wechatpay_intopieces_flow".equals(act.getProcDef().getKey()) || "unionpay_intopieces_flow"
                            .equals(act.getProcDef().getKey())) {
                from.setFlowMark("payInto_flow");

                ErpPayIntopieces payInto = erpPayIntopiecesService.getByProsIncId(act.getBusinessId());
                // 获取商户信息
                ErpShopInfo shop = shopService.get(payInto.getShopId());
                // 获取门店信息
                ErpStoreInfo store = erpStoreInfoService.get(payInto.getStoreId());
                // 获取微信进件信息
                ErpStorePayWeixin weixin = StringUtils.isNotBlank(store.getWeixinPayId()) ? erpStorePayWeixinService
                                .get(store.getWeixinPayId()) : null;
                // 获取银联进件信息
                ErpStorePayUnionpay union = StringUtils.isNotBlank(store.getUnionpayId()) ? erpStorePayUnionpayService
                                .get(store.getUnionpayId()) : null;

                from.setShop(shop);
                from.setOrderNo(payInto.getIntopiecesName());
                from.setOrderTime(payInto.getCreateDate());
                String name = "wechatpay_intopieces_flow".equals(act.getProcDef().getKey()) ? "微信支付进件" : "银联支付进件";
                from.setGoodName(name);
                from.setShopName(shop.getName());
                from.setAgentName(shop.getServiceProvider());
                from.setTaskConsumTime(0);
                from.setVars(act.getVars());
                from.setDeliveryTime(null);

                // 获取子任务显示
                Map<String, Object> freemarkerMap = new HashMap<String, Object>();
                // 获取子任务完成状态
                List<ErpPayIntopiecesSubTask> subTaskList = this.erpPayIntopiecesSubTaskService.getSubTaskList(act.getTaskId());

                freemarkerMap.put("subTaskList", subTaskList);

                fillTaskStartAndEndTime(act, freemarkerMap);
                if (act.getTaskConsumTime() != null) {
                    freemarkerMap.put("taskConsumTime", act.getTaskConsumTime());
                }
                from.setTaskConsumTime(0);
                setCommonTemplateInfo(detailType, followTaskDetailWrap, act, freemarkerMap);
                User user = UserUtils.get(act.getTask().getAssignee());
                freemarkerMap.put("taskUser", user.getName());

                /* 获取任务的key,并存入模板 */
                freemarkerMap.put("taskDefKey", act.getTaskDefKey());
                freemarkerMap.put("splitId", payInto.getId());
                /* 获取商户审核信息的状态 */
                freemarkerMap.put("wechatpayState", weixin != null ? weixin.getAuditStatus() : 0);
                freemarkerMap.put("wechatpayRemark", weixin != null ? weixin.getAuditContent() : "");
                freemarkerMap.put("unionpayState", union != null ? union.getAuditStatus() : 0);
                freemarkerMap.put("unionpayRemark", union != null ? union.getAuditContent() : "");
                freemarkerMap.put("shopId", shop.getId());
                freemarkerMap.put("shopMainId", shop.getId());
                freemarkerMap.put("storeId", store.getId());
                freemarkerMap.put("isMain", store.getIsMain());

                freemarkerMap.put("isTaskDetail", true); // 是详情页
                String taskStr = serviceFreeMarker.getContent(act.getTaskDefKey() + ".ftl", freemarkerMap);
                // taskStr = serviceFreeMarker.getContent("modify_promotion_proposal_3.2.ftl", freemarkerMap);
                boolean isExists = true;
                payTaskGroup(flowFromList, from, taskStr, isExists);
            }else if ("microblog_promotion_flow".equals(act.getProcDef().getKey())) {
                from.setFlowMark("promote_info_flow");
                // 获取基本订单信息
                ErpStoreInfo erpStoreInfo=  erpStoreInfoService.getWeiboPromotionInfobyProcInsId(act.getProcInsId());
                ErpShopInfo shop = shopService.get(erpStoreInfo.getShopInfoId());
                from.setShop(shop);
                // 回显下次联系时间
                from.setTaskConsumTime(0);
                // 获取子任务显示
                Map<String, Object> freemarkerMap = new HashMap<String, Object>();
                setCommonTemplateInfo(detailType, followTaskDetailWrap, act, freemarkerMap);
                ActDefExt actDefExt = actDefExtService.findByProcDefKeyAndActId(act.getProcDef().getKey(), act.getTaskDefKey());
                 int taskHours = 0;
                if (actDefExt != null && StringUtils.isNotBlank(actDefExt.getTaskDateHours())) {
                    taskHours = Integer.parseInt(actDefExt.getTaskDateHours());
                }
                fillTaskStartAndEndTime(act, freemarkerMap);
                fillTaskConsumTime(act, from, freemarkerMap, taskHours);
                queryTaskUser(act, freemarkerMap);
                fillErpFlowForm(act,  new ErpOrderOriginalInfo(), freemarkerMap, null);
                // 获得商户下的所有门店 并设置运营顾问
                fillStoreAndConsultant(act, shop, freemarkerMap, null);
                freemarkerMap.put("channelService", jykOrderPromotionChannelService);
                freemarkerMap.put("isTaskDetail", true); // 是详情页
                freemarkerMap.put("taskStarterDate", DateUtils.formatDateTime(act.getTaskStarterDate()));
                freemarkerMap.put("storeName", erpStoreInfo.getShortName());
                freemarkerMap.put("storeId", erpStoreInfo.getId());
                String OpenOrTrans = erpStoreAdvertiserWeiboService.get(erpStoreInfo.getWeibo().getId()).getOpenOrTrans();
                freemarkerMap.put("openOrTrans", OpenOrTrans);
                // String taskStr = serviceFreeMarker.getContent("microblog_recharge_modify_3.2" + ".ftl",
                // freemarkerMap);
                String taskStr = serviceFreeMarker.getContent(act.getTaskDefKey() + ".ftl", freemarkerMap);
                boolean isExists = true;
                // 任务分组
                jykTaskGroup(flowFromList, from, taskStr, isExists);
            }else if ("friends_promotion_flow".equals(act.getProcDef().getKey())) {
                from.setFlowMark("promote_info_flow");
                // 获取基本订单信息
                ErpStoreInfo erpStoreInfo=  erpStoreInfoService.getFriendsPromotionInfobyProcInsId(act.getProcInsId());
                ErpShopInfo shop = shopService.get(erpStoreInfo.getShopInfoId());
                from.setShop(shop);
                // 回显下次联系时间
                from.setTaskConsumTime(0);
                // 任务详细里面增加拆单信息
                from.setVars(act.getVars());
                // 获取子任务显示
                Map<String, Object> freemarkerMap = new HashMap<String, Object>();

                setCommonTemplateInfo(detailType, followTaskDetailWrap, act, freemarkerMap);
                ActDefExt actDefExt = actDefExtService.findByProcDefKeyAndActId(act.getProcDef().getKey(), act.getTaskDefKey());
                 int taskHours = 0;
                if (actDefExt != null && StringUtils.isNotBlank(actDefExt.getTaskDateHours())) {
                    taskHours = Integer.parseInt(actDefExt.getTaskDateHours());
                }
                fillTaskConsumTime(act, from, freemarkerMap, taskHours);
                queryTaskUser(act, freemarkerMap);
                fillErpFlowForm(act,  new ErpOrderOriginalInfo(), freemarkerMap, null);
                // 获得商户下的所有门店 并设置运营顾问
                fillStoreAndConsultant(act, shop, freemarkerMap, null);
                freemarkerMap.put("channelService", jykOrderPromotionChannelService);
                freemarkerMap.put("isTaskDetail", true); // 是详情页
                freemarkerMap.put("taskStarterDate", DateUtils.formatDateTime(act.getTaskStarterDate()));
                freemarkerMap.put("storeName", erpStoreInfo.getShortName());
                freemarkerMap.put("storeId", erpStoreInfo.getId());
                // String taskStr = serviceFreeMarker.getContent("microblog_recharge_modify_3.2" + ".ftl",
                // freemarkerMap);
                String taskStr = serviceFreeMarker.getContent(act.getTaskDefKey() + ".ftl", freemarkerMap);
                boolean isExists = true;
                // 任务分组
                jykTaskGroup(flowFromList, from, taskStr, isExists);
            }
        }
        Collections.sort(flowFromList);
        return flowFromList;
    }


    private void setJykVariable(Act act, ErpOrderSplitInfo orderSplit, ErpShopInfo shop) {
        if (act.getProcDef().getKey().startsWith(ActUtils.JYK_FLOW_NEW[0])) {
            // 输出卡券之前必须先要 上传推广图片
            if ("work_output_card_coupon".equals(act.getTaskDefKey()) || "work_output_design_draft_update".equals(
                            act.getTaskDefKey()) || "work_output_design_draft".equals(act.getTaskDefKey()) || "work_output_official_documents_update"
                                            .equals(act.getTaskDefKey()) || "work_output_official_documents"
                                                            .equals(act.getTaskDefKey()) || "work_promotion_recharge"
                                                                            .equals(act.getTaskDefKey()) || "work_promotion_online"
                                                                                            .equals(act.getTaskDefKey())) {
                Object obj = runtimeService.getVariable(act.getBusinessId(), "UploadPictureMaterial");
                if (null == obj) {
                    String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(orderSplit.getId());
                    ErpStorePromotePhotoMaterial promotePhotoMaterial = erpStorePromotePhotoMaterialService.findlistWhereStoreId("0", storeId);
                    if (promotePhotoMaterial != null) {
                        runtimeService.setVariable(act.getBusinessId(), "UploadPictureMaterial", storeId);
                        act.setVars(runtimeService.getVariables(act.getBusinessId()));
                    }
                }

                // 输出卡券之前必须先要 掌贝进件
                if ("work_output_card_coupon".equals(act.getTaskDefKey())) {
                    Object objZif = runtimeService.getVariable(act.getBusinessId(), "ZhangbeiInFlag");
                    if (null == objZif) {
                        ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiID(shop.getZhangbeiId());
                        if (shopInfo != null && shopInfo.getZhangbeiState() != null && shopInfo.getZhangbeiState().intValue() == 2) {
                            runtimeService.setVariable(act.getBusinessId(), "ZhangbeiInFlag", shopInfo.getId());
                            act.setVars(runtimeService.getVariables(act.getBusinessId()));
                        }
                    }
                }

                // 推广充值之前 先要把推广开户完成
                if ("work_promotion_recharge".equals(act.getTaskDefKey())) {
                    Object objPaf = runtimeService.getVariable(act.getBusinessId(), "promoteAccountFinish");
                    if (null == objPaf) {
                        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(orderSplit.getId());
                        if (erpStorePromoteAccountService.isPromoteAccountFinish(storeId, orderSplit.getId())) {
                            runtimeService.setVariable(act.getBusinessId(), "promoteAccountFinish", storeId);
                            act.setVars(runtimeService.getVariables(act.getBusinessId()));
                        }
                    }
                }

                // 推广上线之前 先要完成微信支付进件
                if ("work_promotion_online".equals(act.getTaskDefKey())) {
                    Object objWps = runtimeService.getVariable(act.getBusinessId(), "wechatPaySuccess");
                    if (null == objWps) {
                        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(orderSplit.getId());
                        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
                        ErpStorePayWeixin weixin = erpStorePayWeixinService.get(storeInfo.getWeixinPayId());
                        if (weixin != null && 2 == weixin.getAuditStatus().intValue()) {
                            runtimeService.setVariable(act.getBusinessId(), "wechatPaySuccess", "1");
                            act.setVars(runtimeService.getVariables(act.getBusinessId()));
                        }
                    }
                }
            }
        } else if (act.getProcDef().getKey().startsWith(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0])) {
            runtimeService.setVariable(act.getBusinessId(), "ZhangbeiInFlag", ActUtils.JYK_OPEN_ACCOUNT_FLOW[0]);
            runtimeService.setVariable(act.getBusinessId(), "wechatPaySuccess", "1");
            runtimeService.setVariable(act.getBusinessId(), "promoteAccountFinish", ActUtils.JYK_OPEN_ACCOUNT_FLOW[0]);
            act.setVars(runtimeService.getVariables(act.getBusinessId()));
        }
    }

    /**
     * 推广上线与监测 数据准备
     *
     * @param act
     * @param order
     * @param freemarkerMap
     * @date 2018年5月11日
     * @author zjq
     */
    private void promoteMonitorDatePrepare(Act act, ErpOrderOriginalInfo order, Map<String, Object> map, String splitId) {

        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.PROMOTE_ONLINE_FRIENDS)) {

            JykDeliveryEffectInfo deliveryEffectInfo = jykDeliveryEffectInfoService.getByProcInsId(act.getProcInsId());
            List<ErpOrderFile> outerImgFriends = transformFileInfo(deliveryEffectInfo.getOuterImgNameFriends());
            // 朋友圈外层入口截图
            if (!outerImgFriends.isEmpty()) {
                map.put(FlowConstant.CHANNEL_OUTERIMGFRIENDS, outerImgFriends);
            }
            // 朋友圈落地页截图
            List<ErpOrderFile> innerImgFriends = transformFileInfo(deliveryEffectInfo.getInnerImgNameFriends());
            if (!innerImgFriends.isEmpty()) {
                map.put(FlowConstant.CHANNEL_INNERIMGFRIENDS, innerImgFriends);
            }

        }
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.PROMOTE_ONLINE_MICROBLOG)) {
            JykDeliveryEffectInfo deliveryEffectInfo = jykDeliveryEffectInfoService.getByProcInsId(act.getProcInsId());
            List<ErpOrderFile> outerImgWeibo = transformFileInfo(deliveryEffectInfo.getOuterImgNameWeibo());
            // 微博外层入口截图
            if (!outerImgWeibo.isEmpty()) {
                map.put(FlowConstant.CHANNEL_OUTERIMGWEIBO, outerImgWeibo);
            }
            // 微博落地页截图
            List<ErpOrderFile> innerImgWeibo = transformFileInfo(deliveryEffectInfo.getInnerImgNameWeibo());
            if (!innerImgWeibo.isEmpty()) {
                map.put(FlowConstant.CHANNEL_INNERIMGWEIBO, innerImgWeibo);
            }
        }
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.PROMOTE_ONLINE_MOMO)) {
            JykDeliveryEffectInfo deliveryEffectInfo = jykDeliveryEffectInfoService.getByProcInsId(act.getProcInsId());
            List<ErpOrderFile> outerImgMomo = transformFileInfo(deliveryEffectInfo.getOuterImgNameMomo());
            // 陌陌外层入口截图
            if (!outerImgMomo.isEmpty()) {
                map.put(FlowConstant.CHANNEL_OUTERIMGMOMO, outerImgMomo);
            }
            // 陌陌落地页截图
            List<ErpOrderFile> innerImgMomo = transformFileInfo(deliveryEffectInfo.getInnerImgNameMomo());
            if (!innerImgMomo.isEmpty()) {
                map.put(FlowConstant.CHANNEL_INNERIMGMOMO, innerImgMomo);
            }
        }
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.PROMOTE_ONLINE_MONITOR_FRIENDS)) {
            map.put("friendChannelSelected", jykOrderPromotionChannelService.getChannelSelected(splitId, Constant.CHANNEL_1));
        }
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.PROMOTE_ONLINE_MONITOR_MICROBLOG)) {
            map.put("weiboChannelSelected", jykOrderPromotionChannelService.getChannelSelected(splitId, Constant.CHANNEL_2));
        }
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.PROMOTE_ONLINE_MONITOR_MOMO)) {
            map.put("momoChannelSelected", jykOrderPromotionChannelService.getChannelSelected(splitId, Constant.CHANNEL_3));
        }
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.MODIFY_PROMOTION_PROPOSAL)) {
            map.put("failReason", erpFlowFormService.findByProcessIdAndAttrName(act.getProcInsId(), "promotionFailReason"));
        }
    }

    private List<ErpOrderFile> transformFileInfo(String fileNames) {
        List<ErpOrderFile> erpOrderFiles = new ArrayList<ErpOrderFile>();
        String[] files = fileNames.split(Constant.SEMICOLON);
        for (String tmp : files) {
            if (StringUtils.isNoneBlank(tmp)) {
                ErpOrderFile erpOrderFile = new ErpOrderFile();
                erpOrderFile.setFileName(tmp);
                erpOrderFiles.add(erpOrderFile);
            }
        }
        return erpOrderFiles;
    }

    private void fillContactWay(FlowForm from, ErpOrderOriginalInfo order) {
        if ("1".equals(from.getOrderType())) {
            from.setContactWay(order.getContactName() + " / " + order.getContactNumber());
        } else {
            String promoteContact = order.getPromoteContact() != null && !"".equals(order.getPromoteContact()) ? order.getPromoteContact() : "--";
            String promotePhone = order.getPromotePhone() != null && !"".equals(order.getPromotePhone()) ? order.getPromotePhone() : "--";
            from.setContactWay(promoteContact + " / " + promotePhone);
        }
    }

    private void setCommonTemplateInfo(String detailType, String followTaskDetailWrap, Act act, Map<String, Object> freemarkerMap) {
        freemarkerMap.put("followTaskDetailWrap", followTaskDetailWrap);
        freemarkerMap.put("vars", act.getVars().getMap());
        freemarkerMap.put("taskId", act.getTaskId());
        freemarkerMap.put("procInsId", act.getBusinessId());
        freemarkerMap.put("taskName", act.getTaskName());
        freemarkerMap.put("detailType", detailType);
        freemarkerMap.put("taskConsumTimeMin", 80);
        freemarkerMap.put("taskConsumTimeMax", 100);
    }

    /**
     * 查询当前用户信息
     *
     * @param act
     * @param freemarkerMap
     * @date 2018年5月10日
     * @author zjq
     */
    private void queryTaskUser(Act act, Map<String, Object> freemarkerMap) {
        User user = UserUtils.get(act.getTask().getAssignee());
        freemarkerMap.put("taskUser", user.getName());
        StringBuffer sb = new StringBuffer();
        for (Role role : user.getRoleList()) {
            sb.append(role.getName() + " ");
        }
        freemarkerMap.put("taskUserRole", sb.toString());
        freemarkerMap.put("taskUserId", user.getId());
        String roleName = DictUtils.getDictValue(act.getTaskUserRole(), "role_jyk_constant", null);
        if (roleName != null) {
            ErpOrderFlowUser assignee = erpOrderFlowUserService.findByProcInsIdAndRoleName(act.getProcInsId(), roleName);
            freemarkerMap.put("assignee", assignee);
        }
    }

    /**
     * 填充广告主开通通知商户
     */
    private void fillAdvertiserSuccessNotifyShop(Act act, String splitId, Map<String, Object> freemarkerMap) {
        // 选择推广的门店
        if (act.getTask().getTaskDefinitionKey().startsWith("advertiser_success_notify_shop")) {
            List<Integer> channels = jykOrderPromotionChannelService.getChannels(splitId);
            if (!CollectionUtils.isEmpty(channels)) {
                String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(splitId);
                ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
                freemarkerMap.put("storeName", storeInfo.getShortName());
                freemarkerMap.put("channels", channels);
                for (Integer channel : channels) {
                    if (Constant.CHANNEL_1.equals(channel.toString())) {
                        ErpStoreAdvertiserFriends erpStoreAdvertiserFriends = erpStoreAdvertiserFriendsService.getByStoreId(storeId);
                        freemarkerMap.put("friendsAuditStatus", erpStoreAdvertiserFriends == null ? "" : erpStoreAdvertiserFriends.getAuditStatus());
                    }
                    if (Constant.CHANNEL_2.equals(channel.toString())) {
                        ErpStoreAdvertiserWeibo erpStoreAdvertiserWeibo = erpStoreAdvertiserWeiboService.getByStoreId(storeId);
                        freemarkerMap.put("weiboAuditStatus", erpStoreAdvertiserWeibo == null ? "" : erpStoreAdvertiserWeibo.getAuditStatus());
                    }
                    if (Constant.CHANNEL_3.equals(channel.toString())) {
                        ErpStoreAdvertiserMomo erpStoreAdvertiserMomo = erpStoreAdvertiserMomoService.getByStoreId(storeId);
                        freemarkerMap.put("momoAuditStatus", erpStoreAdvertiserMomo == null ? "" : erpStoreAdvertiserMomo.getAuditStatus());
                    }

                }
            }
        }
    }

    // 推广状态同步
    private void fillPromotionStateSync(Act act, String splitId, Map<String, Object> freemarkerMap) {
        if (act.getTask().getTaskDefinitionKey().startsWith("promotion_state_sync")) {
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(splitId);
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            freemarkerMap.put("storeName", storeInfo.getShortName());
            List<JykOrderPromotionChannel> promotionchannelsLists = jykOrderPromotionChannelService.findListBySplitId(splitId);
            freemarkerMap.put("promotionchannelsLists", promotionchannelsLists);
            for (JykOrderPromotionChannel list : promotionchannelsLists) {
                if (Constant.CHANNEL_1.equals(list.getPromotionChannel())) {
                    List<ErpOrderFile> friendPicLists = new ArrayList<>();
                    List<ErpOrderFile> friendPicOutLists = erpOrderFileService.findByProcInsIdAndTileName("朋友圈外层入口截图", act.getProcInsId());
                    List<ErpOrderFile> friendPicFallLists = erpOrderFileService.findByProcInsIdAndTileName("朋友圈落地页截图", act.getProcInsId());
                    friendPicLists.addAll(friendPicOutLists);
                    friendPicLists.addAll(friendPicFallLists);
                    freemarkerMap.put("friendPicLists", friendPicLists);
                }
                if (Constant.CHANNEL_2.equals(list.getPromotionChannel())) {
                    List<ErpOrderFile> weiboPicLists = new ArrayList<>();
                    List<ErpOrderFile> weiboPicOutLists = erpOrderFileService.findByProcInsIdAndTileName("微博外层入口截图", act.getProcInsId());
                    List<ErpOrderFile> weiboPicFallLists = erpOrderFileService.findByProcInsIdAndTileName("微博落地页截图", act.getProcInsId());
                    weiboPicLists.addAll(weiboPicOutLists);
                    weiboPicLists.addAll(weiboPicFallLists);
                    freemarkerMap.put("weiboPicLists", weiboPicLists);
                }
                if (Constant.CHANNEL_3.equals(list.getPromotionChannel())) {
                    List<ErpOrderFile> momoPicLists = new ArrayList<>();
                    List<ErpOrderFile> momoPicOutLists = erpOrderFileService.findByProcInsIdAndTileName("陌陌外层入口截图", act.getProcInsId());
                    List<ErpOrderFile> momoPicFallLists = erpOrderFileService.findByProcInsIdAndTileName("陌陌落地页截图", act.getProcInsId());
                    momoPicLists.addAll(momoPicOutLists);
                    momoPicLists.addAll(momoPicFallLists);
                    freemarkerMap.put("momoPicLists", momoPicLists);
                }
            }

        }
    }

    // 输出卡券list
    private void fillCouponOutput(Act act, String splitId, Map<String, Object> freemarkerMap) {
        if (act.getTask().getTaskDefinitionKey().startsWith("output_card_coupon")) {
            List<ErpOrderCouponOutput> couponOutputList = erpOrderCouponOutputService.findListBySplitId(splitId);
            freemarkerMap.put("couponOutputList", couponOutputList);
        }
    }

    // 微博充值 门店资料
    public void weiboRechargeData(Act act, String splitId, Map<String, Object> freemarkerMap) {
        if (act.getTask().getTaskDefinitionKey().startsWith("sure_recharge_microblog") || act.getTask().getTaskDefinitionKey().startsWith(
                        "microblog_recharge_supplement") || act.getTask().getTaskDefinitionKey().startsWith("microblog_recharge_modify") || act
                                        .getTask().getTaskDefinitionKey().startsWith("microblog_recharge_review")) {
            ErpOrderSplitInfo erpOrderSplitInfo = orderSplitService.get(splitId);
            erpOrderSplitInfo.setTaskDisplay("");
            // 本次推广的门店数
            List<String> StoreIdLists = jykOrderChoiceStoreService.getAllStoreIdBySplitId(splitId);
            List<ErpStoreWeiboReqdto> storeWeiboDataLists = new ArrayList<ErpStoreWeiboReqdto>();
            freemarkerMap.put("taskDisplay", erpOrderSplitInfo.getTaskDisplay());
            for (String storeId : StoreIdLists) {
                ErpStoreWeiboReqdto erpStoreWeiboReqdto = new ErpStoreWeiboReqdto();
                ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
                ErpStoreAdvertiserWeibo erpStoreAdvertiserWeibo = erpStoreAdvertiserWeiboService.getByStoreId(storeId);
                erpStoreWeiboReqdto.setStoreName(storeInfo.getShortName());
                if (null != erpStoreAdvertiserWeibo) {
                    erpStoreWeiboReqdto.setAccountNo(erpStoreAdvertiserWeibo.getAccountNo());
                    erpStoreWeiboReqdto.setUid(erpStoreAdvertiserWeibo.getUid());
                    erpStoreWeiboReqdto.setAuditStatus(erpStoreAdvertiserWeibo.getAuditStatus());
                    storeWeiboDataLists.add(erpStoreWeiboReqdto);
                }
            }
            freemarkerMap.put("storeWeiboData", storeWeiboDataLists);
        }
    }

    // 充值账号，申请金额
    public void weiboReachargeFlow(Act act, String splitId, Map<String, Object> freemarkerMap) {
        if (act.getTask().getTaskDefinitionKey().startsWith("microblog_recharge_review") || act.getTask().getTaskDefinitionKey()
                        .startsWith("microblog_recharge_modify") || act.getTask().getTaskDefinitionKey().startsWith("microblog_recharge_finish")) {
            List<WeiboRechargeResponseDto> weiboReachargeLists = erpChannelWeiboRechargeService.findWeiboRechargeBysplitId(splitId,
                            Constants.SOURCE_FLOW);
            freemarkerMap.put("weiboReachargeLists", weiboReachargeLists);
        }
    }

    public void fillDataPresentation(Act act, String splitId, Map<String, Object> freemarkerMap) {
        if (act.getTask().getTaskDefinitionKey().startsWith("work_data_synchronization_first_day")) {
            JykDataPresentation dataPresentation = jykDataPresentationService.getByProcInsId(act.getBusinessId(), "1");
            freemarkerMap.put("firstDayDataReport", dataPresentation);
        }
        if (act.getTask().getTaskDefinitionKey().startsWith("advertiser_success_notify_shop")) {
            JykDataPresentation dataPresentation = jykDataPresentationService.getByProcInsId(act.getBusinessId(), "3");
            freemarkerMap.put("notifyShopDataReport", dataPresentation);
        }

    }

    /**
     * 填充策划专家
     *
     * @param freemarkerMap
     * @date 2018年5月10日
     * @author zjq
     */
    private void fillPlanningExpert(Act act, Map<String, Object> freemarkerMap) {
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.ASSIGNE_PLANNING_EXPERT)) {
            Role commissioner = systemService.getRoleByEnname(Constant.PLANNING_PERSON);
            List<User> userListCommissioner = systemService.findUser(new User(new Role(commissioner.getId())));
            if (null != userListCommissioner)
                freemarkerMap.put("planningPerson", userListCommissioner);
        }

    }

    /**
     * 根据任务key设置任务节点特有信息
     *
     * @param act
     * @param flow
     * @param shop
     * @param freemarkerMap
     * @date 2018年5月10日
     * @author zjq
     */
    private void fillTemplateInfoByTaskKey(Act act, JykFlow flow, ErpShopInfo shop, Map<String, Object> freemarkerMap) {
        // 需要获取掌贝进件状态的模板
        if ("zhangbei_in_sucess_zhixiao".equals(act.getTask().getTaskDefinitionKey()) && shop != null) {
            freemarkerMap.put("zhangbeiState", shop.getZhangbeiState());
        }

        // 需要获取微信支付状态的模板
        if ("weixin_in_sucess_zhixiao".equals(act.getTask().getTaskDefinitionKey())) {
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(flow.getSplitId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            if (storeInfo != null) {
                // 获取微信进件信息
                ErpStorePayWeixin weixin = StringUtils.isNotBlank(storeInfo.getWeixinPayId()) ? erpStorePayWeixinService
                                .get(storeInfo.getWeixinPayId()) : null;
                freemarkerMap.put("wechatpayState", weixin != null ? weixin.getAuditStatus() : 0);
            }
        }
        // 需要驳回原因的模板
        if ("modify_friends_promote_info_zhixiao".equals(act.getTaskDefKey()) || "modify_microblog_promote_info_zhixiao"
                        .equals(act.getTaskDefKey()) || "modify_momo_promote_info_zhixiao".equals(act.getTaskDefKey())) {
            ErpOrderOperateValue erpOrderOperateValue = null;
            if ("modify_friends_promote_info_zhixiao".equals(act.getTaskDefKey())) {
                erpOrderOperateValue = erpOrderOperateValueService.getOnlyOne(act.getProcInsId(), "friends_promote_info_review_zhixiao", "1");
            }
            if ("modify_microblog_promote_info_zhixiao".equals(act.getTaskDefKey())) {
                erpOrderOperateValue = erpOrderOperateValueService.getOnlyOne(act.getProcInsId(), "microblog_promote_info_review_result_zhixiao",
                                "1");
                if (erpOrderOperateValue == null) {
                    erpOrderOperateValue = erpOrderOperateValueService.getOnlyOne(act.getProcInsId(), "microblog_promote_info_review_zhixiao", "1");
                }
                if (erpOrderOperateValue == null) {
                    erpOrderOperateValue = erpOrderOperateValueService.getOnlyOne(act.getProcInsId(), "microblog_promote_info_review_zhixiao_latest", "1");
                }
                
            }
            if ("modify_momo_promote_info_zhixiao".equals(act.getTaskDefKey())) {
                erpOrderOperateValue = erpOrderOperateValueService.getOnlyOne(act.getProcInsId(), "momo_promote_info_review_zhixiao", "1");
            }
            if (erpOrderOperateValue != null) {
                freemarkerMap.put("reason", erpOrderOperateValue.getValue());
            }
        }
    }

    /**
     * 设置门店信息及开户转户信息
     *
     * @param orderSplit
     * @param freemarkerMap
     * @date 2018年5月10日
     * @author zjq
     */
    private void fillShopOpenOrTrans(ErpOrderSplitInfo orderSplit, Map<String, Object> freemarkerMap, ErpShopInfo shop) {
        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(orderSplit.getId());
        if (StringUtils.isNotBlank(storeId)) {
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            if (storeInfo != null) {
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

    /**
     * 支付流程分组
     *
     * @param flowFromList
     * @param from
     * @param taskStr
     * @param isExists
     * @date 2018年5月10日
     * @author zjq
     */
    private void payTaskGroup(List<FlowForm> flowFromList, FlowForm from, String taskStr, boolean isExists) {
        for (FlowForm flowFrom : flowFromList) {
            // 如果已经存在，则添回到进旧的flowFrom中
            if (StringUtils.isNotBlank(from.getOrderNo()) && flowFrom.getOrderNo().equals(from.getOrderNo())) {
                // 比较大小
                if (flowFrom.getTaskConsumTime() < from.getTaskConsumTime()) {
                    flowFrom.setTaskConsumTime(from.getTaskConsumTime());
                }
                flowFrom.getSubTaskStrList().add(new SubTask(from.getTaskConsumTime(), taskStr));
                // 排序
                Collections.sort(flowFrom.getSubTaskStrList(), new Comparator<SubTask>() {
                    public int compare(SubTask o1, SubTask o2) {
                        return (o2.getSubTaskConsumTime() - o1.getSubTaskConsumTime());
                    }
                });

                isExists = false;
            }
        }

        // 不存在，新建
        if (isExists) {
            from.setSubTaskStr(taskStr);
            from.getSubTaskStrList().add(new SubTask(from.getTaskConsumTime(), taskStr));
            flowFromList.add(from);
        }
    }

    /**
     * 商户流程任务分组
     *
     * @param flowFromList
     * @param from
     * @param taskStr
     * @date 2018年5月10日
     * @author zjq
     */
    private void shopTaskGroup(List<FlowForm> flowFromList, FlowForm from, String taskStr) {
        boolean isExists = true;
        for (FlowForm flowFrom : flowFromList) {
            // 如果已经存在，则添回到进旧的flowFrom中
            if (flowFrom.getOrderNo().equals(from.getOrderNo())) {
                // 比较大小
                if (flowFrom.getTaskConsumTime() < from.getTaskConsumTime()) {
                    flowFrom.setTaskConsumTime(from.getTaskConsumTime());
                }
                flowFrom.getSubTaskStrList().add(new SubTask(from.getTaskConsumTime(), taskStr));
                // 排序
                Collections.sort(flowFrom.getSubTaskStrList(), new Comparator<SubTask>() {
                    public int compare(SubTask o1, SubTask o2) {
                        return (o2.getSubTaskConsumTime() - o1.getSubTaskConsumTime());
                    }
                });

                isExists = false;
            }
        }

        // 不存在，新建
        if (isExists) {
            from.setSubTaskStr(taskStr);
            from.getSubTaskStrList().add(new SubTask(from.getTaskConsumTime(), taskStr));
            flowFromList.add(from);
        }
    }

    /**
     * jyk流程任务分组
     *
     * @param flowFromList
     * @param from
     * @param taskStr
     * @param isExists
     * @date 2018年5月10日
     * @author zjq
     */
    private void jykTaskGroup(List<FlowForm> flowFromList, FlowForm from, String taskStr, boolean isExists) {
        for (FlowForm flowFrom : flowFromList) {
            // 如果已经存在，则添回到进旧的flowFrom中
            if (flowFrom.getOrderNo().equals(from.getOrderNo())) {
                // 比较大小
                if (flowFrom.getTaskConsumTime() < from.getTaskConsumTime()) {
                    flowFrom.setTaskConsumTime(from.getTaskConsumTime());
                }
                flowFrom.getSubTaskStrList().add(new SubTask(from.getTaskConsumTime(), taskStr));
                // 排序
                Collections.sort(flowFrom.getSubTaskStrList(), new Comparator<SubTask>() {
                    public int compare(SubTask o1, SubTask o2) {
                        return (o2.getSubTaskConsumTime() - o1.getSubTaskConsumTime());
                    }
                });

                isExists = false;
            }
        }

        // 不存在，新建
        if (isExists) {
            from.setSubTaskStr(taskStr);
            from.getSubTaskStrList().add(new SubTask(from.getTaskConsumTime(), taskStr));
            flowFromList.add(from);
        }
    }

    private void setTaskUserByFlowFormArrt(Act act, Map<String, Object> freemarkerMap) {
        if (StringUtils.isNotBlank(act.getTaskUserRole())) {
            Role role = systemService.getRoleByEnname(act.getTaskUserRole());
            List<User> userList = systemService.findUser(new User(new Role(role.getId())));
            freemarkerMap.put("taskUserList", userList);
        }
        if (StringUtils.isNotBlank(act.getTaskUserRole2())) {
            Role role = systemService.getRoleByEnname(act.getTaskUserRole2());
            List<User> userList = systemService.findUser(new User(new Role(role.getId())));
            freemarkerMap.put("taskUserList2", userList);
        }
    }

    private int setSplitTaskHours(ErpOrderSplitInfo orderSplit, ActDefExt actDefExt) {
        int taskHours = 0;
        if (actDefExt != null && StringUtils.isNotBlank(actDefExt.getUrgentTaskDateHours()) && StringUtils.isNotBlank(actDefExt.getTaskDateHours())) {
            taskHours = orderSplit.getHurryFlag() == ErpOrderSplitInfo.HURRY_FLAG_YES ? Integer.parseInt(actDefExt.getUrgentTaskDateHours()) : Integer
                            .parseInt(actDefExt.getTaskDateHours());
        }
        return taskHours;
    }

    private void fillStoreAndConsultant(Act act, ErpShopInfo shop, Map<String, Object> freemarkerMap, ErpOrderOriginalInfo order) {
        if (isMerchantDocking(act)) {


            if (shop != null) {
                List<ErpStoreInfo> storeList = erpStoreInfoService.findAllListWhereShopId("0", shop.getId());
                freemarkerMap.put("storeList", storeList);
            }

            freemarkerMap.put("sdiFlowUser", "未分配");
            if (shop != null && StringUtils.isNotBlank(shop.getOperationAdviserId())) {
                User sdiuser = systemService.getUser(shop.getOperationAdviserId());
                if (sdiuser != null) {
                    freemarkerMap.put("sdiFlowUser", sdiuser.getName());
                }
            }
        }
    }

    private void fillTaskConsumTime(Act act, FlowForm from, Map<String, Object> freemarkerMap, int taskHours) throws ParseException {
        Date enddate = erpHolidaysService.enddate(act.getTaskStarterDate(), taskHours);
        act.setTaskConsumTime(computingTaskSchedule(act.getTaskStarterDate(), taskHours));
        try {
            act.setTaskBusinessEndDate(enddate);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            act.setTaskBusinessEndDate(null);
        }
        if (act.getTaskConsumTime() != null) {
            freemarkerMap.put("taskConsumTime", act.getTaskConsumTime());
        }
        from.setTaskConsumTime(act.getTaskConsumTime());
    }

    private void fillSplitInfoTaskTime(Act act, ErpOrderSplitInfo orderSplit) {
        long now = System.currentTimeMillis();
        if (orderSplit.getNextLicenseTime() != null && now >= orderSplit.getNextLicenseTime().getTime()) {
            act.setTaskStarterDate(orderSplit.getNextLicenseTime());
        }
        if (orderSplit.getNextQualificationTime() != null && now >= orderSplit.getNextQualificationTime().getTime()) {
            act.setTaskStarterDate(orderSplit.getNextQualificationTime());
        }
        if (orderSplit.getNextContactTime() != null && orderSplit.getPromotionTime() == null && now >= orderSplit.getNextContactTime().getTime()) {
            act.setTaskStarterDate(orderSplit.getNextContactTime());
        }
    }

    private void fillTaskStartAndEndTime(Act act, Map<String, Object> freemarkerMap) {
        if (act.getTaskStarterDate() != null) {
            freemarkerMap.put("startDate", DateUtils.formatDate(act.getTaskStarterDate(), "MM-dd HH:mm"));
        }
        if (act.getTaskBusinessEndDate() != null) {
            freemarkerMap.put("endDate", DateUtils.formatDate(act.getTaskBusinessEndDate(), "MM-dd HH:mm"));
        } else {
            freemarkerMap.put("endDate", "未指定");
        }
    }

    private void fillSubTaskList(Act act, Map<String, Object> freemarkerMap, List<ErpOrderSubTask> subTaskList) {
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

    /**
     * 填充流程表单数据
     *
     * @param act
     * @param order
     * @param freemarkerMap
     * @date 2018年5月11日
     * @author zjq
     */
    private void fillErpFlowForm(Act act, ErpOrderOriginalInfo order, Map<String, Object> freemarkerMap, String splitId) {

        Map<String, Object> map = Maps.newHashMap();

        List<ErpFlowForm> erpFlowForms = erpFlowFormService.findByTaskId(act.getTaskId());

        fillFrameMarkerMap(map, erpFlowForms);

        // 输出推广页面预览给策划专家
        promotionPreviewDatePrepare(act, order, map, splitId);
        // 推广页面预览确认
        promotionConfirmDatePrepare(act, map);
        // 设计稿 文案数据准备
        officalDatePrepare(act, map, splitId);
        // 首次进入任务节点，初始化节点数据
        if (map.isEmpty()) {
            // 推广上线与监测 数据准备
            promoteMonitorDatePrepare(act, order, map, splitId);
            // 修改推提案
            promotionModifyDatePrepare(act, map);
        }
        freemarkerMap.put("flowdata", map);
    }

    private void promotionModifyDatePrepare(Act act, Map<String, Object> map) {
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.MODIFY_PROMOTION_PROPOSAL)) {
            fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.PROMOTION_PROPOSAL_AUDIT));
        }
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.MODIFY_OFFICIAL_DOCUMENTS)) {
            fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.REVIEW_OFFICIAL_DOCUMENTS));
        }
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.MODIFY_DESIGN_DRAFT)) {
            fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.REVIEW_DESIGN_DRAFT));
        }
    }

    private void promotionConfirmDatePrepare(Act act, Map<String, Object> map) {
        // “商户在掌贝智慧服务中心（小程序）里确定推广预览”是商户在推广预览页面上是否点击的“确认”按钮；若点击了，既回显已确定； 若没有点击，即回显未确定；
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.PROMOTION_PLAN_PREVIEW_CONFIRMATION)) {
            map.put("shopPromoteConfirm", jykDeliveryEffectInfoService.getByProcInsId(act.getProcInsId()).getState());
            map.put("momoLink", erpFlowFormService.findByProcessIdAndAttrName(act.getProcInsId(), "momoLink"));
            fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.WORK_PROMOTION_SCHEME_PREVIEW));
        }
    }

    private void promotionPreviewDatePrepare(Act act, ErpOrderOriginalInfo order, Map<String, Object> map, String splitId) {
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.WORK_PROMOTION_SCHEME_PREVIEW)) {

            if (fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.MODIFY_OFFICIAL_DOCUMENTS)) == 0) {
                fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.OUTPUT_OFFICIAL_DOCUMENTS));
            }
            if (fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.MODIFY_DESIGN_DRAFT)) == 0) {
                fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.OUTPUT_DESIGN_DRAFT));
            }
        }
    }

    private void officalDatePrepare(Act act, Map<String, Object> map, String splitId) {

        // 文案审核
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.REVIEW_OFFICIAL_DOCUMENTS)) {
            if (fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.MODIFY_OFFICIAL_DOCUMENTS)) <= 1) {
                fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.OUTPUT_OFFICIAL_DOCUMENTS));
            }
        }
        // 设计稿审核
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.REVIEW_DESIGN_DRAFT)) {
            if (fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.MODIFY_DESIGN_DRAFT)) <= 1) {
                fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.OUTPUT_DESIGN_DRAFT));
            }
        }
        // 修改文案
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.MODIFY_OFFICIAL_DOCUMENTS)) {
            fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.OUTPUT_OFFICIAL_DOCUMENTS));
            fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.REVIEW_OFFICIAL_DOCUMENTS));
        }
        // 修改设计稿
        if (act.getTask().getTaskDefinitionKey().startsWith(FlowConstant.MODIFY_DESIGN_DRAFT)) {
            fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.OUTPUT_DESIGN_DRAFT));
            fillFrameMarkerMap(map, erpFlowFormService.findByProcessIdAndTask(act.getProcInsId(), FlowConstant.REVIEW_DESIGN_DRAFT));
        }
    }

    private int fillFrameMarkerMap(Map<String, Object> map, List<ErpFlowForm> erpFlowForms) {
        for (ErpFlowForm erpFlowForm : erpFlowForms) {
            if (ErpFlowForm.NORMAL.equalsIgnoreCase(erpFlowForm.getFormAttrType())) {
                map.put(erpFlowForm.getFormAttrName(), erpFlowForm.getFormAttrValue());
            } else if (ErpFlowForm.FILE.equalsIgnoreCase(erpFlowForm.getFormAttrType())) {
                fillErpOrderFile(map, erpFlowForm.getFormAttrName(), erpFlowForm.getFormAttrValue());
            } else {
                map.put(erpFlowForm.getFormAttrName(), erpFlowForm.getFormTextValue());
            }
        }
        return erpFlowForms.size();
    }

    private void fillErpOrderFile(Map<String, Object> map, String formAttrName, String formAttrValue) {
        List<ErpOrderFile> files = new ArrayList<ErpOrderFile>();
        if (StringUtils.isNoneBlank(formAttrValue)) {
            for (String id : formAttrValue.split(Constant.SEMICOLON)) {
                if (StringUtils.isNoneBlank(id))
                    files.add(erpOrderFileService.get(id));
            }
        }
        map.put(formAttrName, files);
    }

    protected void fillOrderGoodInfo(Act act, ErpOrderOriginalInfo order, Map<String, Object> freemarkerMap) {
        if (JykFlowConstants.ASSIGNE_PLANNING_EXPERT.equalsIgnoreCase(act.getTaskDefKey())) {
            List<ErpOrderOriginalGood> erpOrderOriginalGoods = erpOrderOriginalGoodService.findListByOrderId(order.getId());
            StringBuilder builder = new StringBuilder();
            for (ErpOrderOriginalGood erpOrderOriginalGood : erpOrderOriginalGoods) {
                builder.append(Constant.SEMICOLON).append(erpOrderOriginalGood.getGoodName()).append(Constant.ASTERISK)
                                .append(erpOrderOriginalGood.getNum());
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(0);
            }
            freemarkerMap.put("orderGoodName", builder.toString());

        }
    }

    private boolean isMerchantDocking(Act act) {
        return JykFlowConstants.CONTACT_SHOP_ZHIXIAO.equals(act.getTask().getTaskDefinitionKey()) || JykFlowConstants.CONTACT_SHOP_SERVICE
                        .equals(act.getTaskDefKey()) || JykFlowConstants.CONTACT_SHOP_ZHIXIAO_LATEST
                                        .equals(act.getTaskDefKey()) || JykFlowConstants.CONTACT_SHOP_SERVICE_LATEST
                                                        .equals(act.getTaskDefKey()) || JykFlowConstants.CONTACT_SHOP_3P5.equals(act.getTaskDefKey());
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
        float startDateLong = (erpHolidaysService.calculateHours(staterDate, new Date())) * 60 * 60 * 1000;
        float taskHoursLong = (taskHours * 60 * 60 * 1000);
        return (int) ((startDateLong / taskHoursLong) * 100);
    }

    private static FlowStatForm setFlowStat(List<Act> list, List<Act> followActList) {
        FlowStatForm flowStatForm = new FlowStatForm();
        for (Act act2 : list) {
            if (act2.getTaskState().equals(WorkFlowConstants.NORMAL)) {
                flowStatForm.setTaskNormal(flowStatForm.getTaskNormal() + 1);
            }
            if (act2.getTaskState().equals(WorkFlowConstants.ONLY_WILL_OVER_TIME)) {
                flowStatForm.setTaskOnlyWillOvertime(flowStatForm.getTaskOnlyWillOvertime() + 1);
            }
            if (act2.getTaskState().equals(WorkFlowConstants.OVER_TIME)) {
                flowStatForm.setTaskOverTime(flowStatForm.getTaskOverTime() + 1);
            }
        }
        for (Act act2 : followActList) {
            if (act2.getTaskState().equals(WorkFlowConstants.NORMAL)) {
                flowStatForm.setFollowNormal(flowStatForm.getFollowNormal() + 1);
            }
            if (act2.getTaskState().equals(WorkFlowConstants.ONLY_WILL_OVER_TIME)) {
                flowStatForm.setFollowOnlyWillOvertime(flowStatForm.getFollowOnlyWillOvertime() + 1);
            }
            if (act2.getTaskState().equals(WorkFlowConstants.OVER_TIME)) {
                flowStatForm.setFollowOverTime(flowStatForm.getFollowOverTime() + 1);
            }
        }
        return flowStatForm;
    }

    // 根据登录团队管理员，获取其所有的团队
    @RequestMapping(value = "pushTeamAll")
    @ResponseBody
    public List<ErpTeam> pushTeamAll() {
        Principal principal = UserUtils.getPrincipal();
        List<ErpTeam> team = new ArrayList<ErpTeam>();
        ErpTeam eteam = null;
        // 根据团队管理员id查询到其管理员所有的团队
        List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
        if (null != teamuser && !teamuser.isEmpty()) {
            for (int i = 0; i < teamuser.size(); i++) {
                eteam = erpTeamService.get(teamuser.get(i).getTeamId());
                if (eteam != null) {
                    team.add(eteam);
                }
            }
            return team;
        }
        return new ArrayList<>();
    }

    // 获取团队成员方法
    @RequestMapping(value = "pushTeamUser")
    @ResponseBody
    public List<ErpTeamUser> pushTeamUser(String selectteamid) {
        List<ErpTeamUser> listuser = new ArrayList<ErpTeamUser>();
        User u = null;
        if (selectteamid == null || "".equals(selectteamid)) {
            Principal principal = UserUtils.getPrincipal();
            // 根据团队管理员id查询到其管理员所有的团队
            List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
            List<ErpTeamUser> user = null;
            if (null != teamuser && !teamuser.isEmpty()) {
                for (int i = 0; i < teamuser.size(); i++) {
                    // 判断该团队是否已经删除
                    Integer count = erpTeamService.findteam(Global.NO, teamuser.get(i).getTeamId());
                    if (count > 0) {
                        // 循环查询该团队下所有成员
                        user = erpTeamUserService.findwhereteam(Global.NO, teamuser.get(i).getTeamId());
                        if (null != user && !user.isEmpty()) {
                            for (int k = 0; k < user.size(); k++) {
                                u = systemService.getUser(user.get(k).getUserId());
                                if (u != null) {
                                    user.get(k).setUserName(u.getName());
                                }
                            }
                            listuser.addAll(user);
                        }
                    }
                }
            }

        } else {
            Principal principal = UserUtils.getPrincipal();
            List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
            List<ErpTeamUser> user = null;
            if (null != teamuser && !teamuser.isEmpty()) {
                for (int i = 0; i < teamuser.size(); i++) {
                    if (selectteamid.equals(teamuser.get(i).getTeamId())) {
                        Integer count = erpTeamService.findteam(Global.NO, teamuser.get(i).getTeamId());
                        if (count > 0) {
                            user = erpTeamUserService.findwhereteam(Global.NO, teamuser.get(i).getTeamId());
                            if (null != user && !user.isEmpty()) {
                                for (int k = 0; k < user.size(); k++) {
                                    u = systemService.getUser(user.get(k).getUserId());
                                    if (u != null) {
                                        user.get(k).setUserName(u.getName());

                                    }
                                }
                                listuser.addAll(user);
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < listuser.size(); i++) {// 循环list
            for (int j = i + 1; j < listuser.size(); j++) {
                if (null == listuser.get(i).getUserName() || listuser.get(i).getUserName().equals(listuser.get(j).getUserName())) {
                    listuser.remove(i);// 删除一样的元素
                }
            }
        }


        return listuser;

    }

    // 获取团队下包含成员方法
    @RequestMapping(value = "pushTeamAndUser")
    @ResponseBody
    public List<ApiTeamAndUser> pushTeamAndUser(String selectteamid) {
        List<ApiTeamAndUser> teamAndUser = new ArrayList<ApiTeamAndUser>();
        ErpTeam eteam = null;
        ApiTeamAndUser ateamuser = null;
        User u = null;
        if (selectteamid == null || "".equals(selectteamid)) {
            Principal principal = UserUtils.getPrincipal();
            // 根据团队管理员id查询到其管理员所有的团队
            List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
            List<ErpTeamUser> user = null;
            if (null != teamuser && !teamuser.isEmpty()) {
                for (int i = 0; i < teamuser.size(); i++) {
                    // 添加团队名称到teamanduser中
                    eteam = erpTeamService.get(teamuser.get(i).getTeamId());
                    ateamuser = new ApiTeamAndUser();
                    ateamuser.setTeamId(eteam.getId());
                    ateamuser.setTeamName(eteam.getTeamName());
                    // 判断该团队是否已经删除
                    Integer count = erpTeamService.findteam(Global.NO, teamuser.get(i).getTeamId());
                    if (count > 0) {
                        // 循环查询该团队下所有成员
                        user = erpTeamUserService.findwhereteam(Global.NO, teamuser.get(i).getTeamId());
                        if (null != user && !user.isEmpty()) {
                            for (int k = 0; k < user.size(); k++) {
                                u = systemService.getUser(user.get(k).getUserId());
                                if (u != null) {
                                    user.get(k).setUserName(u.getName());
                                }
                            }
                            ateamuser.setTeamUser(user);
                        }
                        teamAndUser.add(ateamuser);
                    }
                }
            }

        } else {
            Principal principal = UserUtils.getPrincipal();
            List<ErpTeamUser> teamuser = erpTeamUserService.findwhereuser(Global.NO, Global.YES, principal.getId());
            List<ErpTeamUser> user = null;
            if (null != teamuser && !teamuser.isEmpty()) {
                for (int i = 0; i < teamuser.size(); i++) {
                    if (selectteamid.equals(teamuser.get(i).getTeamId())) {
                        // 添加团队名称到teamanduser中
                        eteam = erpTeamService.get(teamuser.get(i).getTeamId());
                        ateamuser = new ApiTeamAndUser();
                        ateamuser.setTeamId(eteam.getId());
                        ateamuser.setTeamName(eteam.getTeamName());
                        Integer count = erpTeamService.findteam(Global.NO, teamuser.get(i).getTeamId());
                        if (count > 0) {
                            user = erpTeamUserService.findwhereteam(Global.NO, teamuser.get(i).getTeamId());
                            if (null != user && !user.isEmpty()) {
                                for (int k = 0; k < user.size(); k++) {
                                    u = systemService.getUser(user.get(k).getUserId());
                                    if (u != null) {
                                        user.get(k).setUserName(u.getName());
                                    }
                                }
                                ateamuser.setTeamUser(user);
                            }
                            teamAndUser.add(ateamuser);
                        }
                    }
                }
            }
        }
        return teamAndUser;

    }

    // 团队提交接口
    @RequestMapping(value = "acceptTeamUser")
    @ResponseBody
    public String acceptTeamUser(String taskId, String userOrTeamObj, String userId) {
        List<TaskUserJson> taskUserList = JSON.parseArray(StringEscapeUtils.unescapeHtml4(userOrTeamObj), TaskUserJson.class);
        if (taskUserList.isEmpty()) {
            return "重新指派人员不能为空!";
        }
        // 此处需求修改为单选，待前段更新代码
        TaskUserJson user = taskUserList.get(0);
        // this.workFlowService.getProcessEngine().getTaskService().setAssignee(taskId, user.getUserId());

        List<SubTaskByUser> tasklist = subTaskByUserService.getSubTaskByUser(taskId);
        String delFlag = "0";
        if (!CollectionUtils.isEmpty(tasklist)) {
            delFlag = tasklist.get(0).getDelFlag();
        }
        if (Constant.DIMISSION.equals(delFlag)) {
            // 离职
            for (SubTaskByUser task : tasklist) {
                String tId = task.getTaskId();
                if (!StringUtils.isEmpty(tId)) {
                    doTransferTask(tId, user, userId);
                }
            }
        } else {
            doTransferTask(taskId, user, userId);
        }

        return "重新指派此任务的处理人为:" + UserUtils.get(user.getUserId()).getName();

    }

    public void doTransferTask(String taskId, TaskUserJson user, String userId) {
        // 获得任务对象
        Task task = this.workFlowService.getTaskById(taskId);
        if (null != task) {
            String procKey = task.getProcessDefinitionId();
            String taskKey = task.getTaskDefinitionKey();
            String procName = procKey.substring(0, procKey.indexOf(":", 0));

            // 获得当前任务的角色信息
            String role = actDao.getTaskRole(taskId, procName);
            // 过滤掉角色信息不明确的任务
            if (StringUtils.isNotBlank(role)) {
                // 获得当前任务角色相关的任务集合
                List<String> taskIds = actDao.findRoleTasks(task.getProcessInstanceId(), procName, role);
                if (!CollectionUtils.isEmpty(taskIds)) {
                    for (String taskid : taskIds) {
                        // 将对应任务的处理人都修改
                        this.workFlowService.getProcessEngine().getTaskService().setAssignee(taskid, user.getUserId());
                    }
                }
            }

            // 更新资料录入流程表中的处理人信息
            if ("shop_data_input_flow".equals(procName)) {
                SdiFlow sdiFlow = sdiFlowService.getByProcInstId(task.getProcessInstanceId());
                ErpShopDataInput erpShopDataInput = erpShopDataInputService.getByProsIncId(task.getProcessInstanceId());
                if (null != sdiFlow && null != erpShopDataInput && ("assign_operation_adviser_shop".equals(taskKey))) {
                    sdiFlow.setOperationManager(user.getUserId());
                    erpShopDataInput.setPlanningExpert(user.getUserId());
                    sdiFlowService.update(sdiFlow);
                    erpShopDataInputService.save(erpShopDataInput);
                } else if (null != sdiFlow && null != erpShopDataInput && (!"assign_operation_adviser_shop".equals(taskKey))) {
                    sdiFlow.setOperationAdviser(user.getUserId());
                    sdiFlowService.update(sdiFlow);
                }
            }

            // 更新支付进件流程表中的处理人信息
            if ("wechatpay_intopieces_flow".equals(procName) || "unionpay_intopieces_flow".equals(procName)) {
                ErpPayIntopieces erpPayIntopieces = erpPayIntopiecesService.getByProsIncId(task.getProcessInstanceId());
                if (null != erpPayIntopieces && ("wechat_pay_state_pay".equals(taskKey) || "union_pay_state_pay".equals(taskKey))) {
                    erpPayIntopieces.setChargePerson(user.getUserId());
                    erpPayIntopiecesService.save(erpPayIntopieces);
                }
            }

            String procInsId = this.workFlowService.getTaskById(taskId).getProcessInstanceId();
            // 更新订单流程人员表中的角色信息
            List<ErpOrderFlowUser> erpOrderFlowUserList = this.erpOrderFlowUserService.findListByFlowIdAndUserId(procInsId, userId);
            if (!CollectionUtils.isEmpty(erpOrderFlowUserList)) {
                for (ErpOrderFlowUser erpOrderFlowUser : erpOrderFlowUserList) {
                    erpOrderFlowUser.setUser(new User(user.getUserId()));
                    this.erpOrderFlowUserService.save(erpOrderFlowUser);
                    logger.info("修改流程人员信息{}", erpOrderFlowUser);

                }
            }
        }
    }

    // 团队提交接口
    @RequestMapping(value = "skipUrl")
    public String skipUrl() {
        return "modules/workflow/selectUsersOrTeam";

    }

    @RequiresPermissions("workflow:finishProcInsId")
    @RequestMapping(value = "finishProcInsId")
    @ResponseBody
    public JSONObject finishProcInsId(String procInsId, HttpServletRequest request) {
        JSONObject resObject = new JSONObject();
        actProcessService.deleteProcIns(procInsId, UserUtils.getUser().getName() + "强行删除流程编号" + procInsId);
        // 更新订单为已完成状态
        ErpOrderSplitInfo erpOrderSplit = this.orderSplitService.getByProsIncId(procInsId);
        erpOrderSplit.setStatus(1);
        erpOrderSplit.setEndTime(new Date());
        // 记录订单最终推广上线的用时
        ErpOrderOriginalInfo orderInfo = orderService.get(erpOrderSplit.getOrderId());
        float between = erpHolidaysService.calculateHours(orderInfo.getBuyDate(), new Date(System.currentTimeMillis()));
        erpOrderSplit.setOnlineUseTime(Double.parseDouble(String.valueOf(between)));
        erpOrderSplit.setManualDate(new Date(System.currentTimeMillis()));
        erpOrderSplit.setTimeoutFlag(Constant.NO); // 标识为没有超时风险
        this.orderSplitService.save(erpOrderSplit);

        // 标记完成更新商品完成数量
        for (ErpOrderSplitGood erpOrderSplitGood : erpOrderSplit.getErpOrderSplitGoods()) {
            this.goodService.decreaseProcessNum(erpOrderSplitGood.getOriginalGoodId(), erpOrderSplitGood.getNum());
        }

        resObject.put("result", true);
        return resObject;
    }

    // 查找流程下的所有任务
    @RequestMapping("procins/tasks")
    public String procInsTasks(@RequestParam("procInsId") String procInsId, Model model) {
        List<Map<String, Object>> jumpTasks = workFlowService.getProcInsTasks(procInsId);
        model.addAttribute("jumpTasks", jumpTasks);
        model.addAttribute("procInsId", procInsId);
        return "modules/order/erpOrderOriginalJumpList";
    }

    // 判断流程下是否有未结束的子流程，如果有，不能跳转任务
    @RequestMapping("procins/hasSubProcess")
    public @ResponseBody JSONObject hasSubProcess(@RequestParam("procInsId") String procInsId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", true);
        List<Task> subProcessTasks = workFlowMonitorService.getCurrentSubProcessTasks(procInsId);
        if (!CollectionUtils.isEmpty(subProcessTasks)) {
            jsonObject.put("result", false);
        }
        return jsonObject;
    }

    @RequestMapping("procins/getUserByRoleName")
    public @ResponseBody List<User> getUserByRoleName(@RequestParam("taskId") String taskId) {
        ActDefExt actDefExt = actDefExtService.getByActId(taskId);
        if (actDefExt == null)
            return new ArrayList<>();
        return userService.getUserByRoleName(actDefExt.getRoleName());
    }

    // 跳转任务，把指定任务变为当前任务
    @RequiresPermissions(value = {"workflow:jumpTask"})
    @RequestMapping("procins/tasks/jump")
    public @ResponseBody JSONObject jumpTask(@RequestParam("procInsId") String procInsId, @RequestParam("taskId") String taskId,
                    @RequestParam("userId") String userId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", true);
        try {
            workFlowService.jumpTask(procInsId, taskId, userId);
        } catch (Exception e) {
            jsonObject.put("result", false);
            logger.error(e.getMessage(), e);
        }
        return jsonObject;
    }

    /**
     * 删除任务的文件
     *
     * @return
     * @date 2017年12月1日
     * @author Administrator
     */
    @RequestMapping("procins/orderFile/deleteOrderSplitFile")
    @ResponseBody
    public JSONObject deleteOrderSplitFile(@RequestParam("id") String id) {

        JSONObject jsonObject = new JSONObject();
        try {
            ErpOrderFile orderFile = new ErpOrderFile();
            orderFile.setId(id);
            erpOrderFileService.delete(orderFile);
            jsonObject.put("result", "200");
        } catch (RuntimeException e) {
            jsonObject.put("result", "500");
            logger.error(e.getMessage(), e);
        }
        return jsonObject;
    }


    /**
     * 修改门店及推广通道的推广状态
     *
     * @return
     */
    @RequestMapping(value = "updateExtension/{splitId}")
    @ResponseBody
    public String updateExtension(@PathVariable String splitId) {
        return flowService.updateExtension(splitId);
    }

    // 查找流程下的所有任务
    @RequestMapping("procins/openPayDialog")
    public String procInsTasks(@RequestParam("procInsId") String procInsId) {
        return "modules/workflow/openPayDialog";
    }

}
