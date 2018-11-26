package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.Date;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;
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
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.utils.DictUtils;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.channel.service.JykOrderPromotionChannelService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.dto.AdvAuditStatusDto;
import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowInfoDto;
import com.yunnex.ops.erp.modules.workflow.flow.dto.JykInfoDto;
import com.yunnex.ops.erp.modules.workflow.flow.dto.OrderInfoDto;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderInputDetail;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SdiFlow;
import com.yunnex.ops.erp.modules.workflow.remarks.entity.WorkflowRemarksInfo;
import com.yunnex.ops.erp.modules.workflow.remarks.service.WorkflowRemarksInfoService;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 流程信息
 */
@Service
public class FlowInfoService implements BaseFlowInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowInfoService.class);

    private final String ORDER_TYPE = "orderType";
    private final String UNKNOW = "未知";
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ErpOrderFileService erpOrderFileService;
    @Autowired
    @Lazy(true)
    private ErpOrderSplitInfoService orderSplitInfoService;
    @Autowired
    private ErpOrderInputDetailService inputDetailService;
    @Autowired
    @Lazy(true)
    private ErpOrderOriginalInfoService orderOriginalInfoService;
    @Autowired
    private ErpOrderOriginalGoodService orderOriginalGoodService;
    @Autowired
    private ErpOrderFlowUserService orderFlowUserService;
    @Autowired
    private WorkflowRemarksInfoService workflowRemarksInfoService;
    @Autowired
    private ErpOrderSplitGoodService orderSplitGoodService;
    @Autowired
    private JykOrderPromotionChannelService jykOrderPromotionChannelService;
    @Autowired
    private JykOrderChoiceStoreService jykOrderChoiceStoreService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private SdiFlowService sdiFlowService;
    @Autowired
    private ErpShopDataInputService erpShopDataInputService;
    @Autowired
    private ErpShopInfoService shopInfoService;
    @Autowired
    private ErpPayIntopiecesService payIntopiecesService;

    /**
     * 获取流程业务信息
     * 
     * @param procInstId
     * @return
     */
    public FlowInfoDto getFlowInfo(String procInstId) {
        LOGGER.info("任务详情通用模块入参：procInstId = {}", procInstId);
        ErpShopInfo shopInfo = null; // 商户信息
        FlowInfoDto flowInfoDto = new FlowInfoDto();; // 封装结果
        if (StringUtils.isBlank(procInstId)) {
            return flowInfoDto;
        }
        
        // 流程实例
        HistoricProcessInstance procInst = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        if (procInst == null) {
            return flowInfoDto;
        }
        
        String procDefKey = procInst.getProcessDefinitionKey();
        if (StringUtils.isBlank(procDefKey)) {
            return flowInfoDto;
        }

        // 聚引客流程
        if (procDefKey.startsWith(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0])) {
            flowInfoDto.setFlowName(ActUtils.FLOW_ALIAS[0]);
            ErpOrderOriginalInfo orderOriginalInfo = null;
            ErpOrderSplitInfo orderSplitInfo = orderSplitInfoService.getByProsIncId(procInstId);
            if (orderSplitInfo != null) {
                flowInfoDto.setSplitId(orderSplitInfo.getId());
                orderOriginalInfo = orderOriginalInfoService.get(orderSplitInfo.getOrderId());
            }
            if (orderOriginalInfo != null && StringUtils.isNotBlank(orderOriginalInfo.getShopId())) {
                shopInfo = shopInfoService.findListByZhangbeiId(orderOriginalInfo.getShopId());
            }

            // 封装订单模块信息
            wrapOrderInfo(flowInfoDto, orderOriginalInfo);
            wrapOrderInfoJyk(flowInfoDto, orderSplitInfo, procInstId);

            // 封装聚引客信息
            wrapJykInfo(flowInfoDto, orderSplitInfo);

            // 任务相关文件
            List<ErpOrderFile> orderFiles = this.erpOrderFileService.findListByProcInsId(procInstId);
            flowInfoDto.setOrderFiles(orderFiles);

            // 任务相关资料
            if (orderSplitInfo != null && StringUtils.isNotBlank(orderSplitInfo.getId())) {
                List<ErpOrderInputDetail> orderInputDetails = this.inputDetailService.findListBySplitId(orderSplitInfo.getId());
                flowInfoDto.setOrderInputDetails(orderInputDetails);
            }
        }
        // 商户资料录入流程
        else if (procDefKey.startsWith(ActUtils.SHOP_DATA_INPUT_FLOW[0])) {
            flowInfoDto.setFlowName(ActUtils.FLOW_ALIAS[1]);
            ErpShopDataInput shopData = null;
            ErpOrderOriginalInfo orderOriginalInfo = null;
            // 获取任务流信息
            SdiFlow flow = sdiFlowService.getByProcInstId(procInstId);
            if (flow != null) {
                // 获取商户资料录入信息
                shopData = erpShopDataInputService.get(flow.getSdiId());
                // 获取订单信息
                orderOriginalInfo = orderOriginalInfoService.get(flow.getOrderId());
            }
            // 获取商户信息
            if (shopData != null) {
                shopInfo = shopInfoService.findListByZhangbeiId(shopData.getShopId());
            }

            // 封装订单信息
            wrapOrderInfo(flowInfoDto, orderOriginalInfo);
            wrapOrderInfoSdi(flowInfoDto, orderOriginalInfo, procInstId);
        }
        // 微信支付进件流程或者银联支付进件流程
        else if (procDefKey.startsWith(ActUtils.WECHAT_PAY_INTOPIECES_FLOW[0]) || procDefKey.startsWith(ActUtils.UNION_PAY_INTOPIECES_FLOW[0])) {
            flowInfoDto.setFlowName(ActUtils.FLOW_ALIAS[2]);
            ErpPayIntopieces payInto = payIntopiecesService.getByProsIncId(procInstId);
            // 获取商户信息
            if (payInto != null && StringUtils.isNotBlank(payInto.getShopId())) {
                shopInfo = shopInfoService.get(payInto.getShopId());
            }

            // 支付进件的订单模块信息
            wrapOrderInfoPay(flowInfoDto, shopInfo, payInto, procDefKey);
        }
        // 朋友圈或微博开户流程
        else if (procDefKey.startsWith(ActUtils.MICROBLOG_PROMOTION_FLOW[0]) || procDefKey.startsWith(ActUtils.FRIENDS_PROMOTION_FLOW[0])) {
            flowInfoDto.setFlowName(ActUtils.FLOW_ALIAS[3]);
            ErpStoreInfo erpStoreInfo = new ErpStoreInfo();
            OrderInfoDto orderInfoDto = createOrderInfoDto(flowInfoDto);
            if (ActUtils.FRIENDS_PROMOTION_FLOW[0].equals(procDefKey)) {
                erpStoreInfo = erpStoreInfoService.getFriendsPromotionInfobyProcInsId(procInstId);
                orderInfoDto.setOrderNo("朋友圈开户" + erpStoreInfo.getFriend().getAccountNo());
            } else {
                erpStoreInfo = erpStoreInfoService.getWeiboPromotionInfobyProcInsId(procInstId);
                orderInfoDto.setOrderNo("微博开户" + erpStoreInfo.getWeibo().getAccountNo());
            }
            shopInfo = shopInfoService.get(erpStoreInfo.getShopInfoId());
            if (shopInfo != null) {
                orderInfoDto.setShopName(shopInfo.getName());
                orderInfoDto.setAgentName(shopInfo.getServiceProvider());
            }
            String name = ActUtils.FRIENDS_PROMOTION_FLOW[0].equals(procDefKey) ? ActUtils.FLOW_CN_NAME[4] : ActUtils.FLOW_CN_NAME[5];
            orderInfoDto.setServiceTypeInfo(name);
        }
        // 流程备注
        List<WorkflowRemarksInfo> remarksInfo = workflowRemarksInfoService.findListByProcInsId(procInstId);
        flowInfoDto.setRemarksInfo(remarksInfo);

        // 商户的进件信息
        wrapPayInfo(erpStoreInfoService, flowInfoDto, shopInfo);

        return flowInfoDto;
    }

    /**
     * 创建订单模块对象
     * 
     * @param flowInfo
     * @return
     */
    private OrderInfoDto createOrderInfoDto(FlowInfoDto flowInfo) {
        FlowInfoDto flowInfoDto = createFlowInfoDto(flowInfo);

        OrderInfoDto orderInfoDto = flowInfoDto.getOrderInfoDto();
        if (orderInfoDto == null) {
            orderInfoDto = new OrderInfoDto();
            flowInfoDto.setOrderInfoDto(orderInfoDto);
        }

        return orderInfoDto;
    }

    /**
     * 支付进件的订单模块信息
     * 
     * @param flowInfoDto
     * @param payInto
     */
    private void wrapOrderInfoPay(FlowInfoDto flowInfoDto, ErpShopInfo shopInfo, ErpPayIntopieces payInto, String procDefKey) {
        OrderInfoDto orderInfoDto = createOrderInfoDto(flowInfoDto);

        if (payInto != null) {
            orderInfoDto.setOrderNo(payInto.getIntopiecesName());
        }

        if (shopInfo != null) {
            orderInfoDto.setShopName(shopInfo.getName());
            orderInfoDto.setAgentName(shopInfo.getServiceProvider());
        }

        String name = ActUtils.WECHAT_PAY_INTOPIECES_FLOW[0].equals(procDefKey) ? ActUtils.FLOW_CN_NAME[0] : ActUtils.FLOW_CN_NAME[1];
        orderInfoDto.setServiceTypeInfo(name);
    }

    /**
     * 商户资料录入的订单模块信息
     * 
     * @param flowInfoDto
     * @param procInstId
     */
    private void wrapOrderInfoSdi(FlowInfoDto flowInfoDto, ErpOrderOriginalInfo orderOriginalInfo, String procInstId) {
        OrderInfoDto orderInfoDto = createOrderInfoDto(flowInfoDto);

        // 运营顾问
        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(procInstId);
        if (sdiFlow != null && sdiFlow.getOperationAdviser() != null) {
            User user = UserUtils.get(sdiFlow.getOperationAdviser());
            String operationAdviser = user != null ? user.getName() : Constant.BLANK;
            orderInfoDto.setOperationAdviser(operationAdviser);
        }

        if (orderOriginalInfo != null) {
            // 订单号
            orderInfoDto.setOrderNo(orderOriginalInfo.getOrderNumber());
        }
    }

    /**
     * 聚引客流程订单模块信息
     * 
     * @param flowInfoDto
     * @param orderSplitInfo
     * @param procInstId
     */
    public void wrapOrderInfoJyk(FlowInfoDto flowInfoDto, ErpOrderSplitInfo orderSplitInfo, String procInstId) {
        OrderInfoDto orderInfoDto = createOrderInfoDto(flowInfoDto);

        if (orderSplitInfo != null) {
            // 紧急标识
            orderInfoDto.setHurryFlag(orderSplitInfo.getHurryFlag());
            // 分单号
            orderInfoDto.setOrderNo(orderSplitInfo.getOrderNumber() + Constant.DASH + orderSplitInfo.getSplitId());
        }

        // 运营顾问
        ErpOrderFlowUser flowUser = orderFlowUserService.findListByFlowId(procInstId, JykFlowConstants.OPERATION_ADVISER);
        if (flowUser != null && flowUser.getUser() != null) {
            User user = UserUtils.get(flowUser.getUser().getId());
            String operationAdviser = user != null ? user.getName() : Constant.BLANK;
            orderInfoDto.setOperationAdviser(operationAdviser);
        }
    }

    /**
     * 封装订单信息
     * 
     * @param flowInfoDto
     * @param orderOriginalInfo
     */
    public void wrapOrderInfo(FlowInfoDto flowInfoDto, ErpOrderOriginalInfo orderOriginalInfo) {
        OrderInfoDto orderInfoDto = createOrderInfoDto(flowInfoDto);

        if (orderOriginalInfo != null) {
            orderInfoDto.setShopName(orderOriginalInfo.getShopName());
            orderInfoDto.setOrderRemarks(orderOriginalInfo.getRemark()); // 备注
            orderInfoDto.setShopContactPerson(orderOriginalInfo.getContactName());
            orderInfoDto.setShopContactPhone(orderOriginalInfo.getContactNumber());
            orderInfoDto.setAgentName(orderOriginalInfo.getAgentName());
            orderInfoDto.setAgentContactPerson(orderOriginalInfo.getPromoteContact());
            orderInfoDto.setAgentContactPhone(orderOriginalInfo.getPromotePhone());
            orderInfoDto.setBuyDate(orderOriginalInfo.getBuyDate());
            // 订单类型
            Integer orderType = orderOriginalInfo.getOrderType();
            String type = orderType != null ? String.valueOf(orderType) : null;
            orderInfoDto.setOrderTypeName(DictUtils.getDictLabel(type, ORDER_TYPE, UNKNOW));
            // 购买的服务类型+版本号
            String jykAndKclServiceType = orderOriginalGoodService.getJykAndKclServiceType(orderOriginalInfo);
            orderInfoDto.setServiceTypeInfo(jykAndKclServiceType);
        }
    }

    /**
     * 聚引客信息
     */
    private void wrapJykInfo(FlowInfoDto flowInfoDto, ErpOrderSplitInfo orderSplitInfo) {
        if (orderSplitInfo == null || StringUtils.isBlank(orderSplitInfo.getId())) {
            return;
        }
        flowInfoDto = createFlowInfoDto(flowInfoDto);
        JykInfoDto jykInfoDto = flowInfoDto.getJykInfoDto();
        if (jykInfoDto == null) {
            jykInfoDto = new JykInfoDto();
            flowInfoDto.setJykInfoDto(jykInfoDto);
        }
        String splitId = orderSplitInfo.getId();

        // 推广套餐
        List<ErpOrderSplitGood> splitGoods = orderSplitGoodService.getPromoteGoods(splitId);
        jykInfoDto.setSplitGoods(splitGoods);
        // 推广时间
        Date promotionTime = orderSplitInfoService.getPromotionTime(orderSplitInfo);
        jykInfoDto.setPromotionTime(promotionTime);
        // 推广通道，格式：微博 | 陌陌
        List<String> channelNames = jykOrderPromotionChannelService.getChannelNames(splitId);
        if (!CollectionUtils.isEmpty(channelNames)) {
            StringBuilder channels = new StringBuilder();
            for (String channelName : channelNames) {
                channels.append(channelName).append(Constant.SPACE).append(Constant.VERTICAL_LINE).append(Constant.SPACE);
            }
            channels.deleteCharAt(channels.length() - 2);
            jykInfoDto.setPromotionChannels(channels.toString());
        }
        // 策划专家
        ErpOrderFlowUser flowUser = orderFlowUserService.findBySplitIdAndFlowUser(splitId, JykFlowConstants.Planning_Expert);
        if (flowUser != null && flowUser.getUser() != null) {
            User user = UserUtils.get(flowUser.getUser().getId());
            String planningExpert = user != null ? user.getName() : Constant.BLANK;
            jykInfoDto.setPlanningExpert(planningExpert);
        }
        // 推广门店的广告主审核状态
        List<AdvAuditStatusDto> advAuditStatus = jykOrderChoiceStoreService.getAdvAuditStatus(splitId);
        jykInfoDto.setAdvAuditStatus(advAuditStatus);
    }

}
