package com.yunnex.ops.erp.common.activiti.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceMessageTemplate;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceProgressTemplate;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageTemplateService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressTemplateService;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowVariableConstants;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowService;

/**
 * 交付流程进度监听实现类
 * 
 * @author zjq
 *
 */
@Service("progressListenerHandler")
public class ProgressListenerHandler implements IListenerHandler {

    private ErpServiceProgressTemplateService progressTemplateService = SpringContextHolder.getBean(ErpServiceProgressTemplateService.class);
    private ErpServiceMessageTemplateService messageTemplateService = SpringContextHolder.getBean(ErpServiceMessageTemplateService.class);
    private ErpServiceProgressService erpServiceProgressService = SpringContextHolder.getBean(ErpServiceProgressService.class);
    private ErpServiceMessageService erpServiceMessageService = SpringContextHolder.getBean(ErpServiceMessageService.class);

    /**
     * 进度监控触发
     * 
     * @see 1、在ACT_RE_ACTDEF_EXT表中配置对应监控节点所需参数
     * @see 2、通过参数中的templateIds参数来获取erp_service_progress_template表中数据
     * @see 3、通过流程实例ID获取对应的流程类型（DeliveryFlowConstant->流程变量值）
     * @see 4、将流程类型和进度模板表中数据做对比，如果该流程实例对应的该节点信息与模板信息一致，则更新对应的进度数据
     */
    @Override
    @Transactional(readOnly = false)
    public String doListenerBusiness(DelegateTask task, DelegateExecution execution, Map<String, Object> params) {
        // 流程进度监控-小程序用数据修改
        List<String> goodServieTypeList = (List<String>) execution.getVariable(FlowVariableConstants.GOOD_SERVIE_TYPE_LIST);
        // 兼容 贝蚁 2.1 上线前的服务类型
        if (CollectionUtils.isEmpty(goodServieTypeList)) {
            Object serviceType = execution.getVariable(DeliveryFlowConstant.SERVICE_TYPE);
            String serviceTypeStr = serviceType == null ? null : serviceType.toString();
            if (StringUtils.isNotBlank(serviceTypeStr)) {
                goodServieTypeList = new ArrayList<>();
                goodServieTypeList.add(serviceTypeStr);
            }
        }
        String visitType = String.valueOf(Optional.ofNullable(execution.getVariable(DeliveryFlowConstant.VISIT_TYPE)).orElse(StringUtils.EMPTY));
        List<String> progressIds = null;
        // 将多个服务需要触发的 通知与进度 id 合并成一个集合id
        if (CollectionUtils.isNotEmpty(goodServieTypeList)) {
            progressIds = new ArrayList<>();
            for (String goodServieType : goodServieTypeList) {
                String progressIdStr = String.valueOf(Optional.ofNullable(params.get(goodServieType + visitType)).orElse(StringUtils.EMPTY));
                List<String> ids = JSON.parseArray(progressIdStr, String.class);
                if (CollectionUtils.isNotEmpty(ids)) {
                    progressIds.addAll(ids);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(progressIds)) {
            List<ErpServiceProgressTemplate> progressTemplateList = progressTemplateService.queryTemplateByIds(progressIds);// 获取进度模板ID对应模板参数
            if (CollectionUtils.isNotEmpty(progressTemplateList)) {
                for (ErpServiceProgressTemplate template : progressTemplateList) {
                    erpServiceProgressService.updateTemplateIdByManual(execution.getProcessInstanceId(), template.getServiceType(),
                                    template.getType(), template.getStatus());
                }
            }
            List<ErpServiceMessageTemplate> messageTemplateList = messageTemplateService.queryTemplateByIds(progressIds);// 获取通知模板ID对应模板参数
            if (CollectionUtils.isNotEmpty(messageTemplateList)) {
                for (ErpServiceMessageTemplate template : messageTemplateList) {
                    erpServiceMessageService.managerMessageByManual(execution.getProcessInstanceId(), template.getServiceType(),
                                    template.getNodeType(), template.getStatus());
                }
            }
        }
        // 保存 银联支付培训&测试（远程） 完成时间
        String saveTrainTestTimeFlag = String.valueOf(Optional.ofNullable(params.get("saveTrainTestTime")).orElse(StringUtils.EMPTY));
        if (StringUtils.isNotBlank(saveTrainTestTimeFlag)) {
            SpringContextHolder.getBean(DeliveryFlowService.class).saveTrainTestTime(execution.getProcessInstanceId());
        }
        return "";
    }
}
