package com.yunnex.ops.erp.modules.message.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.message.constant.ServiceMessageTemplateConstants;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitGoodService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.sys.entity.JobNumberInfo;
import com.yunnex.ops.erp.modules.sys.service.JobNumberInfoService;
import com.yunnex.ops.erp.modules.visit.constants.VisitServiceItemConstants;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;
import com.yunnex.ops.erp.modules.workflow.acceptance.entity.ErpServiceAcceptance;
import com.yunnex.ops.erp.modules.workflow.acceptance.service.ErpServiceAcceptanceService;
import com.yunnex.ops.erp.modules.workflow.channel.entity.JykOrderPromotionChannel;
import com.yunnex.ops.erp.modules.workflow.channel.enums.PromotionChannelType;
import com.yunnex.ops.erp.modules.workflow.channel.service.JykOrderPromotionChannelService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.enums.FlowServiceType;
import com.yunnex.ops.erp.modules.workflow.store.entity.JykOrderChoiceStore;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;
import com.yunnex.ops.erp.modules.workflow.user.constant.OrderFlowUserConstants;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 服务通知变量
 * 
 * @author linqunzhi
 * @date 2018年7月18日
 */
@Service
public class ServiceMessageVariableService extends BaseService {

    @Autowired
    private ErpOrderFlowUserService orderFlowUserService;
    @Autowired
    private JobNumberInfoService jobNumberInfoService;
    @Autowired
    private JykOrderChoiceStoreService orderChoiceStoreService;
    @Autowired
    private ErpOrderSplitInfoService splitInfoService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private JykOrderPromotionChannelService orderPromotionChannelService;
    @Autowired
    private ErpVisitServiceInfoService visitServiceInfoService;
    @Autowired
    private ErpServiceAcceptanceService acceptanceService;
    @Autowired
    private ErpDeliveryServiceService deliveryServiceService;
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private ErpOrderSplitGoodService erpOrderSplitGoodService;

    /**
     * 根据 服务类型 获取变量map
     *
     * @param procInsId
     * @param serviceType
     * @return
     * @date 2018年7月12日
     * @author linqunzhi
     */
    public Map<String, String> getVariableMap(String procInsId, String serviceType, String nodeType) {
        Map<String, String> result = new HashMap<>();
        if (FlowServiceType.SPLIT_JU_YIN_KE.getType().equals(serviceType)) {
            result = getSplitJuYinKeVariableMap(procInsId);
        } else {
            result = getDeliveryVariableMap(procInsId, nodeType, serviceType);
        }
        return result;
    }

    /**
     * 获取Delivery相关流程 变量
     *
     * @param procInsId
     * @param nodeType
     * @return
     * @date 2018年7月18日
     * @author linqunzhi
     * @param serviceType
     */
    private Map<String, String> getDeliveryVariableMap(String procInsId, String nodeType, String serviceType) {
        Map<String, String> result = new HashMap<>();
        ErpDeliveryService deliveryService = deliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        String zhangbeiId = null;
        if (deliveryService != null) {
            zhangbeiId = deliveryService.getShopId();
        }
        // 获取商户信息
        ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiId(zhangbeiId);
        int storeCount = 0; // 门店总数

        if (shopInfo != null) {
            storeCount = shopInfo.getStoreCount();
        }
        // 获取上门目的
        String serviceGoalCode = calculateServiceGoalCode(serviceType, nodeType);
        ErpVisitServiceInfo visitInfo = visitServiceInfoService.getByGoalCode(procInsId, serviceGoalCode);
        String visitServiceInfoId = null;
        // 上门提醒标识
        String visitFlag = null;
        Date visitTime = null;
        String shopPreparationInfo = null;
        if (visitInfo != null) {
            visitServiceInfoId = visitInfo.getId();
            visitTime = visitInfo.getAppointedStartTime();
            shopPreparationInfo = visitInfo.getShopPreparationInfo();
            visitFlag = visitInfo.getRemindFlag();
        }
        String acceptanceId = null;
        if (StringUtils.isNotBlank(visitServiceInfoId)) {
            ErpServiceAcceptance acceptance = acceptanceService.getByVisitId(visitServiceInfoId);
            if (acceptance != null) {
                acceptanceId = acceptance.getId();
            }
        }

        // ### 生成变量值 ###
        result.put("@ZhangbeiId@", StringUtils.toString(zhangbeiId, ""));
        result.put("@ServiceNums@", "服务*1");
        result.put("@StoreCount@", StringUtils.toString(storeCount, "0"));
        result.put("@VisitTime@", StringUtils.toString(DateUtils.formatDate(visitTime, DateUtils.YYYY_MM_DD_HH_MM), ""));
        result.put("@ShopPreparationInfo@", StringUtils.toString(replaceRN(shopPreparationInfo), ""));
        result.put("@AcceptanceId@", StringUtils.toString(acceptanceId, ""));
        result.put("@VisitServiceInfoId@", StringUtils.toString(visitServiceInfoId, ""));
        result.put("@VisitFlag@", StringUtils.toString(visitFlag, CommonConstants.Sign.NO));
        return result;
    }

    /**
     * 获取上门目的
     *
     * @param serviceType
     * @param nodeType
     * @return
     * @date 2018年7月24日
     * @author linqunzhi
     */
    private String calculateServiceGoalCode(String serviceType, String nodeType) {
        String result = "";
        if (FlowServiceType.DELIVERY_FMPS.getType().equals(serviceType)) {
            if (ServiceMessageTemplateConstants.NodeType.FLOOR_SERVICE
                            .equals(nodeType) || ServiceMessageTemplateConstants.NodeType.FLOOR_SERVICE_VISIT
                                            .equals(nodeType) || ServiceMessageTemplateConstants.NodeType.FLOOR_SERVICE_COMMENT.equals(nodeType)) {
                result = VisitServiceItemConstants.Goal.FMPS_CODE;
            } else {
                result = VisitServiceItemConstants.Goal.FMPS_VISIT_CODE;
            }
        } else if (FlowServiceType.DELIVERY_FMPS_BASIC.getType().equals(serviceType)) {
            result = VisitServiceItemConstants.Goal.FMPS_BASIC_CODE;
        } else if (FlowServiceType.DELIVERY_JYK.getType().equals(serviceType)) {
            result = VisitServiceItemConstants.Goal.JYK_CODE;
        } else if (FlowServiceType.ZHI_HUI_CAN_TING.getType().equals(serviceType)) {
            result = VisitServiceItemConstants.Goal.ZHCT_CODE;
        }
        return result;
    }

    /**
     * 获取引流服务 变量集合
     *
     * @return
     * @date 2018年7月18日
     * @author linqunzhi
     * @param procInsId
     */
    private Map<String, String> getSplitJuYinKeVariableMap(String procInsId) {
        Map<String, String> result = new HashMap<>();
        // 获取分单信息
        ErpOrderSplitInfo splitInfo = splitInfoService.getByProsIncId(procInsId);
        String splitId = null; // 分单id
        Date promotionTime = null;// 推广上线时间
        String lookEffectFlag = null;// 是否查看过效果报告
        String commentFlag = CommonConstants.Sign.NO;// 是否评价过
        String zhangbeiId = null;
        if (splitInfo != null) {
            splitId = splitInfo.getId();
            promotionTime = splitInfo.getPromotionTime();
            lookEffectFlag = splitInfo.getLookEffectFlag();
            commentFlag = splitInfo.getCommentCount() > 0 ? CommonConstants.Sign.YES : CommonConstants.Sign.NO;
            zhangbeiId = splitInfo.getShopId();
        }
        // 商户信息
        ErpShopInfo shop = erpShopInfoService.findByZhangbeiId(zhangbeiId);
        String operationAdviserId = null;
        if (shop != null) {
            operationAdviserId = shop.getOperationAdviserId();
        }
        // 获取运营顾问
        ErpOrderFlowUser operationFlowUser = orderFlowUserService.findByProcInsIdAndRoleName(procInsId,
                        OrderFlowUserConstants.FLOW_USER_OPERATION_ADVISER);
        String operationUserId = null;// 运营顾问id
        if (operationFlowUser != null) {
            if (operationFlowUser.getUser() != null) {
                operationUserId = operationFlowUser.getUser().getId();
            }
        }
        if (StringUtils.isBlank(operationUserId)) {
            // 如果正在跑的流程中没指派运营顾问，取商户绑定运营顾问
            operationUserId = operationAdviserId;
        }
        String operationName = null;
        JobNumberInfo operationJob = jobNumberInfoService.getByUserId(operationUserId);
        if (operationJob != null) {
            operationName = operationJob.getJobNumber();
        }
        // 获取决策专家
        ErpOrderFlowUser planningFlowUser = orderFlowUserService.findByProcInsIdAndRoleName(procInsId,
                        OrderFlowUserConstants.FLOW_USER_PLANNING_EXPERT);
        JobNumberInfo planningJob = null;
        String planningName = null;
        if (planningFlowUser != null) {
            if (planningFlowUser.getUser() != null) {
                planningJob = jobNumberInfoService.getByUserId(planningFlowUser.getUser().getId());
                if (planningJob != null) {
                    planningName = planningJob.getJobNumber();
                }
            }
        }
        // 商品数量
        String serviceNumbers = erpOrderSplitGoodService.getServiceAndNum(splitId);
        //获取门店推广信息
        JykOrderChoiceStore choiceStore = orderChoiceStoreService.getBySplitId(splitId);
        String storeId = null;
        if (choiceStore != null) {
            storeId = choiceStore.getChoiceStore();
        }
        //获取门店详情
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        String storeName = null;
        if (storeInfo != null) {
            storeName = storeInfo.getShortName();
        }
        //获取推广通道
        List<JykOrderPromotionChannel> channelList = orderPromotionChannelService.findListBySplitId(splitId);
        String promotionChannels = null;// 推广渠道
        if (channelList != null) {
            StringBuilder channels = new StringBuilder();
            for (JykOrderPromotionChannel channel : channelList) {
                String channelName = PromotionChannelType.getNameByCode(channel.getPromotionChannel());
                channels.append(CommonConstants.Sign.DUN_HAO).append(channelName);
            }
            if (channels.length() > 0) {
                promotionChannels = channels.substring(1);
            }
        }
        result.put("@ServiceNums@", StringUtils.toString(serviceNumbers, ""));
        result.put("@ZhangbeiId@", StringUtils.toString(zhangbeiId, ""));
        result.put("@SplitId@", StringUtils.toString(splitId, ""));
        result.put("@OperationAdviserName@", StringUtils.toString(operationName, ""));
        result.put("@PlanningExpertName@", StringUtils.toString(planningName, ""));
        result.put("@PromotionStoreNames@", StringUtils.toString(storeName, ""));
        result.put("@PromotionOnlineDate@", StringUtils.toString(DateUtils.formatDateTime(promotionTime), ""));
        result.put("@PromotionChannels@", StringUtils.toString(promotionChannels, ""));
        result.put("@LookEffectFlag@", StringUtils.toString(lookEffectFlag, CommonConstants.Sign.NO));
        result.put("@CommentFlag@", StringUtils.toString(commentFlag, CommonConstants.Sign.NO));
        return result;
    }

    /**
     * 去除换行
     *
     * @param str
     * @return
     * @date 2018年8月1日
     * @author linqunzhi
     */
    private static String replaceRN(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return str.replaceAll("\\r\\n|\\r|\\n|\\n\\r", "");
    }

}
