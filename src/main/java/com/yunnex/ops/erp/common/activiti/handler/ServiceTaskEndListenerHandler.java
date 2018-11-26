package com.yunnex.ops.erp.common.activiti.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowVariableConstants;

/**
 * 服务任务节点结束事件触发执行器
 * 
 * @date 2018年7月25日
 */
@Service("serviceTaskEndListenerHandler")
public class ServiceTaskEndListenerHandler implements IListenerHandler {

    private ErpServiceProgressService serviceScheduleService = SpringContextHolder.getBean(ErpServiceProgressService.class);
    private ErpServiceMessageService serviceMessageService = SpringContextHolder.getBean(ErpServiceMessageService.class);
    private ErpDeliveryServiceService erpDeliveryServiceService = SpringContextHolder.getBean(ErpDeliveryServiceService.class);
    private TaskService taskService = SpringContextHolder.getBean(TaskService.class);

    @Override
    @Transactional(readOnly = false)
    public String doListenerBusiness(DelegateTask task, DelegateExecution execution, Map<String, Object> params) {
        List<String> goodServiceTypeList = (List<String>) execution.getVariable(FlowVariableConstants.GOOD_SERVIE_TYPE_LIST);
        // 兼容 贝蚁 2.1 上线前的服务类型
        if (CollectionUtils.isEmpty(goodServiceTypeList)) {
            Object serviceType = execution.getVariable(DeliveryFlowConstant.SERVICE_TYPE);
            String serviceTypeStr = serviceType == null ? null : serviceType.toString();
            if (StringUtils.isNotBlank(serviceTypeStr)) {
                goodServiceTypeList = new ArrayList<>();
                goodServiceTypeList.add(serviceTypeStr);
            }
        }
        // 服务进度管理
        serviceScheduleService.updateTemplateIdByProcInsId(execution.getProcessInstanceId(), goodServiceTypeList, execution.getCurrentActivityId());
        // 服务通知管理
        serviceMessageService.managerMessage(execution.getProcessInstanceId(), goodServiceTypeList, execution.getCurrentActivityId());
        return null;
    }

}
