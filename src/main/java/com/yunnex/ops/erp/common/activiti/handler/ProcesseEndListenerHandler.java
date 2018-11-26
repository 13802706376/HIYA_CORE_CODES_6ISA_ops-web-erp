package com.yunnex.ops.erp.common.activiti.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.act.dao.TaskExtDao;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.message.constant.ServiceProgressTemplateConstants;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;
import com.yunnex.ops.erp.modules.order.constant.OrderSplitConstants;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalGoodService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.dao.ErpFlowFormDao;
import com.yunnex.ops.erp.modules.workflow.flow.dao.ErpFlowFormHisDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpFlowForm;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlow3p25Service;

/**
 * 流程结束事件触发执行器
 * 
 * @date 2018年7月25日
 */
@Service("processeEndListenerHandler")
public class ProcesseEndListenerHandler implements IListenerHandler {

    private ErpDeliveryServiceService erpDeliveryServiceService = SpringContextHolder.getBean(ErpDeliveryServiceService.class);
    private ErpOrderSplitInfoService erpOrderSplitInfoService = SpringContextHolder.getBean(ErpOrderSplitInfoService.class);
    private ErpOrderOriginalGoodService goodService = SpringContextHolder.getBean(ErpOrderOriginalGoodService.class);
    private TaskExtDao taskExtDao = SpringContextHolder.getBean(TaskExtDao.class);
    private ErpFlowFormDao erpFlowFormDao = SpringContextHolder.getBean(ErpFlowFormDao.class);
    private ErpFlowFormHisDao erpFlowFormHisDao = SpringContextHolder.getBean(ErpFlowFormHisDao.class);
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    private WorkFlow3p25Service workFlowService = SpringContextHolder.getBean(WorkFlow3p25Service.class);

    @Override
    @Transactional(readOnly = false)
    public String doListenerBusiness(DelegateTask task, DelegateExecution execution, Map<String, Object> params) {
        // 流程id
        String procInsId = execution.getProcessInstanceId();
        // 流程结束了 删除 ACT_RU_TASK_EXT表 数据
        taskExtDao.deleteTaskExtsByProcInsId(procInsId);
        // 删除 erp_flow_form
        List<ErpFlowForm> erpFlowFormList = erpFlowFormDao.findByProcInsId(procInsId);
        if (!CollectionUtils.isEmpty(erpFlowFormList)) {
            erpFlowFormHisDao.batchInsertFlowFormData(erpFlowFormList);
        }
        erpFlowFormDao.deleteByProcInsId(procInsId);
        ProcessDefinition pd = processEngine.getRepositoryService() // 获取service类
                        .createProcessDefinitionQuery() // 创建流程定义查询
                        .processDefinitionId(execution.getProcessDefinitionId()) // 通过id查询
                        .singleResult(); // 查询返回当个结果
        // 交付服务流程 结束保存结束时间
        if (pd.getKey().startsWith(ActUtils.DELIVERY_SERVICE_FLOW[0]) || pd.getKey().startsWith(ActUtils.VISIT_SERVICE_ZHCT_FLOW)) {
            erpDeliveryServiceService.saveFlowEndTime(procInsId);
        }
        // 流程结束，如果为正常结束的流程则更新服务项次数
        if (!Optional.ofNullable(execution.getVariable(DeliveryFlowConstant.RESET_FLAG)).isPresent()) {
            workFlowService.updateServiceItemNum(procInsId);
        }

        // 修改聚引客商品处理数量
        if (pd.getKey().startsWith(ActUtils.FLOW_ALIAS[0])) {
            ErpOrderSplitInfo erpOrderSplit = erpOrderSplitInfoService.getByProsIncId(procInsId);
            // 非正常结束流程时进行修改，如取消订单
            if (erpOrderSplit != null && erpOrderSplit.getStatus() == OrderSplitConstants.STATUS_CANCEL) {
                erpOrderSplit.setEndTime(new Date());
                erpOrderSplitInfoService.save(erpOrderSplit);
                List<ErpOrderSplitGood> erpOrderSplitGoods = erpOrderSplit.getErpOrderSplitGoods();
                Optional.ofNullable(erpOrderSplitGoods).ifPresent(splitGoods -> splitGoods
                                .forEach(splitGood -> goodService.decreaseProcessNum(splitGood.getOriginalGoodId(), splitGood.getNum())));
            }
        }

        // 流程进度监控数据修改
        String serviceType = StringUtils.EMPTY;
        String procDefKey = execution.getEngineServices().getRepositoryService().getProcessDefinition(execution.getProcessDefinitionId()).getKey();// 获取流程KEY
        if (StringUtils.equals(procDefKey, "jyk_flow_new")) {// 聚引客流程
            serviceType = "SplitJuYinKe";
        } else {
            serviceType = String.valueOf(Optional.ofNullable(execution.getVariable(DeliveryFlowConstant.SERVICE_TYPE)).orElse(StringUtils.EMPTY));
        }
        if (StringUtils.isNotBlank(serviceType)) {
            ErpServiceProgressService erpServiceProgressService = SpringContextHolder.getBean(ErpServiceProgressService.class);
            erpServiceProgressService.updateTemplateIdByManual(execution.getProcessInstanceId(), serviceType, "ServiceEnd",
                            ServiceProgressTemplateConstants.STATUS_END);// 服务结束处理一致，不同节点进行监控
        }
        return null;
    }

}
