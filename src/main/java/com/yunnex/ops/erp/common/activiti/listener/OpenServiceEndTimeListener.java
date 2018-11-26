package com.yunnex.ops.erp.common.activiti.listener;

import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.message.constant.ServiceMessageTemplateConstants;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.enums.FlowServiceType;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowVariableConstants;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowService;

/**
 * 账号支付开通监听
 * 
 * @author Ejon
 * @date 2018年6月22日
 * @see 服务节点对应服务实体 delivery_service_flow-open_service_end_listener
 */
public class OpenServiceEndTimeListener implements JavaDelegate
{
    private RuntimeService runtimeService = SpringContextHolder.getBean(RuntimeService.class);
    ErpDeliveryServiceService deliveryServiceService = SpringContextHolder.getBean(ErpDeliveryServiceService.class);
    ErpServiceMessageService serviceMessageService = SpringContextHolder.getBean(ErpServiceMessageService.class);
    private DeliveryFlowService deliveryFlowService = SpringContextHolder.getBean(DeliveryFlowService.class);

    /**
     * ErpOrderFlowUserService orderFlowUserService =
     * SpringContextHolder.getBean(ErpOrderFlowUserService .class);
     */

    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
        // 流程ID
        String  procInsId= execution.getProcessInstanceId();
        // 获取流程变量值
        Map<String, Object> variables = execution.getVariables();
        // 是否需要上门标识
        String visitServiceFlag = null;
        if (variables != null) {
            Object visitFlagObject = variables.get(FlowVariableConstants.Delivery.VISIT_SERVICE_FLAG);
            visitServiceFlag = visitFlagObject == null ? null : visitFlagObject.toString();
        }
        // 设置流程进度
        runtimeService.setVariable(procInsId, FlowConstant.ACCOUNT_PAY_OPEN, FlowConstant.FINISH);
        // 保存完成测试时间
        deliveryFlowService.saveTrainTestTime(procInsId);
        // 消息通知
        managerServiceMessage(procInsId, visitServiceFlag);
    }

    /**
     * 消息通知管理
     *
     * @param procInsId
     * @param visitServiceFlag
     * @date 2018年7月26日
     * @author linqunzhi
     */
    private void managerServiceMessage(String procInsId, String visitServiceFlag) {
        ErpDeliveryService delivery = deliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (delivery == null) {
            return;
        }
        // 服务类型
        String serviceType = delivery.getServiceType();
        managerServiceMessage(procInsId, serviceType,visitServiceFlag);
    }

    private void managerServiceMessage(String procInsId, String serviceType, String visitServiceFlag) {
        if (FlowServiceType.DELIVERY_FMPS.getType().equals(serviceType)) {
            // start 运营顾问将和您电话预约上门服务时间，请保持电话畅通
            serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.FLOOR_SERVICE,
                            ServiceMessageTemplateConstants.STATUS_BEGIN);
        } else if (FlowServiceType.DELIVERY_FMPS_BASIC.getType().equals(serviceType)) {
            // start 运营顾问将和您电话预约上门服务时间，请保持电话畅通
            serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.VISIT_SERVICE,
                            ServiceMessageTemplateConstants.STATUS_BEGIN);
        } else if (FlowServiceType.DELIVERY_JYK.getType().equals(serviceType) && CommonConstants.Sign.YES.equals(visitServiceFlag)) {
            // start 运营顾问将和您电话预约上门服务时间，请保持电话畅通
            serviceMessageService.managerMessageByManual(procInsId, serviceType, ServiceMessageTemplateConstants.NodeType.VISIT_SERVICE,
                            ServiceMessageTemplateConstants.STATUS_BEGIN);
        }
    }
}