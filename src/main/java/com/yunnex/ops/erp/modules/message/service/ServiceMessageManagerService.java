package com.yunnex.ops.erp.modules.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.message.constant.ServiceMessageTemplateConstants;
import com.yunnex.ops.erp.modules.visit.constants.VisitServiceItemConstants;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.enums.FlowServiceType;

/**
 * 服务通知管理
 * 
 * @author linqunzhi
 * @date 2018年7月27日
 */
@Service
public class ServiceMessageManagerService extends BaseService {

    @Autowired
    private ErpVisitServiceInfoService visitServiceInfoService;
    @Autowired
    private ErpDeliveryServiceService deliveryServiceService;
    @Autowired
    private ErpServiceMessageService serviceMessageService;



    /**
     * 服务验收及评价 提交触发通知
     *
     * @param acceptance
     * @date 2018年7月27日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void acceptanceCommit(String visitInfoId) {
        logger.info("acceptanceCommit start | visitInfoId={}", visitInfoId);
        if (StringUtils.isBlank(visitInfoId)) {
            return;
        }
        ErpVisitServiceInfo erpVisitServiceInfo = visitServiceInfoService.get(visitInfoId);
        if (erpVisitServiceInfo == null) {
            return;
        }
        String procInsId = erpVisitServiceInfo.getProcInsId();
        ErpDeliveryService delivery = deliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (delivery == null) {
            return;
        }
        // 服务目的code
        String serviceGoalCode = erpVisitServiceInfo.getServiceGoalCode();
        // 获取服务类型
        String serviceType = getServiceType(delivery.getServiceType(), serviceGoalCode);
        if (FlowServiceType.DELIVERY_FMPS.getType().equals(serviceType)) {
            if ("1".equals(serviceGoalCode)) {
                // end 请核实本次服务内容并对本次服务进行评分
                serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.FLOOR_SERVICE_COMMENT,
                                ServiceMessageTemplateConstants.STATUS_END);
            } else {
                // end 请核实本次服务内容并对本次服务进行评分
                serviceMessageService.managerMessageByManual(procInsId, serviceType,
                                ServiceMessageTemplateConstants.NodeType.MATERIAL_DEPLOYMENT_COMMENT, ServiceMessageTemplateConstants.STATUS_END);
            }
        } else if (sendVisitMessage(serviceType)) {
            // end 请核实本次服务内容并对本次服务进行评分
            serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.VISIT_SERVICE_COMMENT,
                            ServiceMessageTemplateConstants.STATUS_END);
        }
        logger.info("acceptanceCommit end");
    }

    /**
     * 获取服务类型
     *
     * @param serviceType
     * @param serviceGoalCode
     * @return
     * @date 2018年9月4日
     * @author linqunzhi
     */
    private String getServiceType(String serviceType, String serviceGoalCode) {
        String result = serviceType;
        // 智慧餐厅
        if (VisitServiceItemConstants.Goal.ZHCT_CODE.equals(serviceGoalCode)) {
            result = FlowServiceType.ZHI_HUI_CAN_TING.getType();
        }
        return result;
    }

    /**
     * 上门提醒 触发通知
     *
     * @param visitInfoId
     * @date 2018年8月1日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void remindDoor(String visitInfoId) {
        logger.info("remindDoor start | visitInfoId={}", visitInfoId);
        if (StringUtils.isBlank(visitInfoId)) {
            return;
        }
        ErpVisitServiceInfo erpVisitServiceInfo = visitServiceInfoService.get(visitInfoId);
        if (erpVisitServiceInfo == null) {
            return;
        }
        String procInsId = erpVisitServiceInfo.getProcInsId();
        ErpDeliveryService delivery = deliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (delivery == null) {
            return;
        }
        // 服务目的code
        String serviceGoalCode = erpVisitServiceInfo.getServiceGoalCode();
        // 获取服务类型
        String serviceType = getServiceType(delivery.getServiceType(), serviceGoalCode);
        if (FlowServiceType.DELIVERY_FMPS.getType().equals(serviceType)) {
            if ("1".equals(serviceGoalCode)) {
                // end 您已经预约【XXX】日上门服务，请您提前准备【商户准备材料】等资料，并保持电话通畅
                serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.FLOOR_SERVICE_VISIT,
                                ServiceMessageTemplateConstants.STATUS_END);
                // start 请核实本次服务内容并对本次服务进行评分
                serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.FLOOR_SERVICE_COMMENT,
                                ServiceMessageTemplateConstants.STATUS_BEGIN);
            } else {
                // end 您已经预约【XXX】日上门服务，请您提前准备【商户准备材料】等资料，并保持电话通畅
                serviceMessageService.managerMessageByManual(procInsId, serviceType,
                                ServiceMessageTemplateConstants.NodeType.MATERIAL_DEPLOYMENT_VISIT, ServiceMessageTemplateConstants.STATUS_END);
                // start 请核实本次服务内容并对本次服务进行评分
                serviceMessageService.managerMessageByManual(procInsId, serviceType,
                                ServiceMessageTemplateConstants.NodeType.MATERIAL_DEPLOYMENT_COMMENT, ServiceMessageTemplateConstants.STATUS_BEGIN);
            }

        } else if (sendVisitMessage(serviceType)) {
            // end 您已经预约【XXX】日上门服务，请您提前准备【商户准备材料】等资料，并保持电话通畅
            serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.VISIT_SERVICE_VISIT,
                            ServiceMessageTemplateConstants.STATUS_END);
            // start 请核实本次服务内容并对本次服务进行评分
            serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.VISIT_SERVICE_COMMENT,
                            ServiceMessageTemplateConstants.STATUS_BEGIN);
        }
        logger.info("remindDoor end | visitInfoId={}", visitInfoId);
    }


    /**
     * 是否满足发送上门服务相关通知
     *
     * @param serviceType
     * @return
     * @date 2018年9月4日
     * @author linqunzhi
     */
    private boolean sendVisitMessage(String serviceType) {
        return FlowServiceType.DELIVERY_FMPS_BASIC.getType().equals(serviceType) || FlowServiceType.DELIVERY_JYK.getType()
                        .equals(serviceType) || FlowServiceType.ZHI_HUI_CAN_TING.getType().equals(serviceType);
    }

    /**
     * 已预约上门 触发通知
     *
     * @param procInsId
     * @param visitInfoId
     * @date 2018年8月6日
     * @author linqunzhi
     */
    @Transactional(readOnly = false)
    public void reserved(String procInsId, String visitInfoId) {
        ErpDeliveryService delivery = deliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (delivery == null) {
            return;
        }
        ErpVisitServiceInfo visitInfo = visitServiceInfoService.get(visitInfoId);
        if (visitInfo == null) {
            return;
        }
        String serviceGoalCode = visitInfo.getServiceGoalCode();
        // 获取服务类型
        String serviceType = getServiceType(delivery.getServiceType(), serviceGoalCode);
        if (FlowServiceType.DELIVERY_FMPS.getType().equals(serviceType)) {
            if ("1".equals(serviceGoalCode)) {
                // end 运营顾问将和您电话预约上门服务时间，请保持电话畅通
                serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.FLOOR_SERVICE,
                                ServiceMessageTemplateConstants.STATUS_END);
                // start 您已经预约【XXX】日上门服务，请您提前准备【商户准备材料】等资料，并保持电话通畅
                serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.FLOOR_SERVICE_VISIT,
                                ServiceMessageTemplateConstants.STATUS_BEGIN);
            } else {
                // end 运营顾问将和您电话预约上门服务时间，请保持电话畅通
                serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.MATERIAL_DEPLOYMENT,
                                ServiceMessageTemplateConstants.STATUS_END);
                // start 您已经预约【XXX】日上门服务，请您提前准备【商户准备材料】等资料，并保持电话通畅
                serviceMessageService.managerMessageByManual(procInsId, serviceType,
                                ServiceMessageTemplateConstants.NodeType.MATERIAL_DEPLOYMENT_VISIT, ServiceMessageTemplateConstants.STATUS_BEGIN);
            }
        } else if (sendVisitMessage(serviceType)) {
            // end 运营顾问将和您电话预约上门服务时间，请保持电话畅通
            serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.VISIT_SERVICE,
                            ServiceMessageTemplateConstants.STATUS_END);
            // start 您已经预约【XXX】日上门服务，请您提前准备【商户准备材料】等资料，并保持电话通畅
            serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.VISIT_SERVICE_VISIT,
                            ServiceMessageTemplateConstants.STATUS_BEGIN);
        }

    }

}
