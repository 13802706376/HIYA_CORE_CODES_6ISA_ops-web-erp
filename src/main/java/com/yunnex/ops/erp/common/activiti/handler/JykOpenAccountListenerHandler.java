package com.yunnex.ops.erp.common.activiti.handler;

import java.util.Map;
import java.util.Optional;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.message.constant.ServiceProgressTemplateConstants;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceProgress;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;
import com.yunnex.ops.erp.modules.workflow.enums.FlowServiceType;

/**
 * 聚引客生产流程开户进度监听实现类
 * 
 * @author zjq
 *
 */
@Service("jykOpenAccountListenerHandler")
public class JykOpenAccountListenerHandler implements IListenerHandler {

    private ErpServiceProgressService erpServiceProgressService = SpringContextHolder.getBean(ErpServiceProgressService.class);
    private ErpServiceMessageService erpServiceMessageService = SpringContextHolder.getBean(ErpServiceMessageService.class);
    private ActTaskService actTaskService = SpringContextHolder.getBean(ActTaskService.class);

    @Override
    @Transactional(readOnly = false)
    public String doListenerBusiness(DelegateTask task, DelegateExecution execution, Map<String, Object> params) {
        String openType = String.valueOf(Optional.ofNullable(params.get("openType")).orElse(StringUtils.EMPTY));
        if (StringUtils.isNotBlank(openType)) {
            ErpServiceProgress erpServiceProgress = erpServiceProgressService.getByProcInsIdAndType(execution.getProcessInstanceId(),
                            FlowServiceType.SPLIT_JU_YIN_KE.getType(),
                            "PromotionAccount");
            if (erpServiceProgress != null) {
                String remarks = String.valueOf(Optional.ofNullable(erpServiceProgress.getRemarks()).orElse(StringUtils.EMPTY));
                if (!StringUtils.contains(remarks, openType)) {
                    remarks += openType;
                }
                erpServiceProgress.setRemarks(remarks);
                erpServiceProgressService.save(erpServiceProgress);
                if (StringUtils.contains(remarks, Constant.CHANNEL_1) && StringUtils.contains(remarks, Constant.CHANNEL_2) && StringUtils
                                .contains(remarks, Constant.CHANNEL_3)) {
                    erpServiceProgressService.updateTemplateIdByManual(execution.getProcessInstanceId(), FlowServiceType.SPLIT_JU_YIN_KE.getType(),
                                    "PromotionAccount",
                                    ServiceProgressTemplateConstants.STATUS_END);
                    erpServiceMessageService.managerMessageByManual(execution.getProcessInstanceId(), FlowServiceType.SPLIT_JU_YIN_KE.getType(),
                                    "PromotionProposalConfirmNoFinish", ServiceProgressTemplateConstants.STATUS_END);
                    if (actTaskService.taskIsFinish(execution.getProcessInstanceId(), "management_diagnosis_marketing_planning_3.1")) {
                        erpServiceMessageService.managerMessageByManual(execution.getProcessInstanceId(), FlowServiceType.SPLIT_JU_YIN_KE.getType(),
                                        "PromotionProposalConfirmFinish", ServiceProgressTemplateConstants.STATUS_BEGIN);
                    }
                }
            }
        }
        return "";
    }
}
