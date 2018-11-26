package com.yunnex.ops.erp.common.activiti.handler;

import java.util.Date;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.act.service.TaskExtService;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 人工任务节点创建事件触发执行器
 * 
 * @date 2018年7月25日
 */
@Service("userTaskCreateListenerHandler")
public class UserTaskCreateListenerHandler implements IListenerHandler {

    private ErpOrderMaterialCreationService materialCreationService = SpringContextHolder.getBean(ErpOrderMaterialCreationService.class);
    private ErpOrderFlowUserService orderFlowUserService = SpringContextHolder.getBean(ErpOrderFlowUserService.class);
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService = SpringContextHolder.getBean(ErpOrderOriginalInfoService.class);
    private ErpDeliveryServiceService erpDeliveryServiceService = SpringContextHolder.getBean(ErpDeliveryServiceService.class);
    
    @Override
    @Transactional(readOnly = false)
    public String doListenerBusiness(DelegateTask task, DelegateExecution execution, Map<String, Object> params) {
        TaskExtService taskExtService = SpringContextHolder.getBean(TaskExtService.class);
        TaskExt ext = new TaskExt();
        ext.setTaskId(task.getId());
        ext.setPendingProdFlag("N");
        ext.setStatus("1");
        ext.setRemark(params.get("actType")+"");
        taskExtService.save(ext);
        TaskService taskService = SpringContextHolder.getBean(TaskService.class);

        updateProcessProgress(task, taskService);
        // 创建物料信息
        createMaterialInfo(task);
        return null;
    }

  private void createMaterialInfo(DelegateTask task) {
        if (task.getTaskDefinitionKey().startsWith(DeliveryFlowConstant.MATERIAL_MAKE_SUBMIT)||task.getTaskDefinitionKey().startsWith(DeliveryFlowConstant.MATERIAL_MAKE_FOLLOW_FIRST)
                        ||task.getTaskDefinitionKey().startsWith (DeliveryFlowConstant.MATERIAL_MAKE_FOLLOW_UPDATE))
        {
            /*
             * List<ErpOrderMaterialCreation> list =
             * materialCreationService.findMaterialCreation(task.getProcessInstanceId());
             * ErpDeliveryService erpDeliveryService =
             * erpDeliveryServiceService.getDeliveryInfoByProsIncId(task.getProcessInstanceId());
             * ErpOrderOriginalInfo orderInfo =
             * this.erpOrderOriginalInfoService.get(erpDeliveryService.getOrderId());
             * ErpOrderFlowUser flowUser =
             * orderFlowUserService.findListByFlowId(task.getProcessInstanceId(),
             * JykFlowConstants.OPERATION_ADVISER); if (list.isEmpty()) { ErpOrderMaterialCreation
             * em = new ErpOrderMaterialCreation(); em.setProcInsId(task.getProcessInstanceId());
             * em.setZhangbeiId(orderInfo.getShopId()); em.setStatus("waiting_layout");
             * em.setStatusName("待设计稿"); em.setOrderNumber(erpDeliveryService.getOrderNumber());
             * em.setOperationAdviserId(flowUser.getUser().getId());
             * em.setOperationAdviserName(flowUser.getUser().getName()); em.setPlaceOrderTime(new
             * Date()); em.setDeliverTime(new Date()); em.setShopName(orderInfo.getShopName());
             * this.materialCreationService.save(em); }
             */
            ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(task.getProcessInstanceId());
            ErpOrderOriginalInfo orderInfo = this.erpOrderOriginalInfoService.get(erpDeliveryService.getOrderId());
            ErpOrderFlowUser flowUser = orderFlowUserService.findListByFlowId(task.getProcessInstanceId(), JykFlowConstants.OPERATION_ADVISER);
            ErpOrderMaterialCreation erpOrderMaterialCreation= materialCreationService.findByOrderNumber(erpDeliveryService.getOrderNumber());
            if (erpOrderMaterialCreation==null) {
                ErpOrderMaterialCreation em = new ErpOrderMaterialCreation();
                em.setProcInsId(task.getProcessInstanceId());
                em.setZhangbeiId(orderInfo.getShopId());
                em.setStatus("waiting_layout");
                em.setStatusName("待设计稿");
                em.setOrderNumber(erpDeliveryService.getOrderNumber());
                em.setOperationAdviserId(flowUser.getUser().getId());
                em.setOperationAdviserName(flowUser.getUser().getName());
                em.setPlaceOrderTime(new Date());
                em.setDeliverTime(new Date());
                em.setShopName(orderInfo.getShopName());
                this.materialCreationService.save(em);
          }else{
              
              erpOrderMaterialCreation.setOperationAdviserId(flowUser.getUser().getId());
              erpOrderMaterialCreation.setOperationAdviserName(flowUser.getUser().getName());
              erpOrderMaterialCreation.setProcInsId(task.getProcessInstanceId());
              this.materialCreationService.save(erpOrderMaterialCreation);
          }
            
        }
    }


    /**
     * 更新任务进度
     *
     * @param task
     * @param taskService
     * @date 2018年6月5日
     * @author zjq
     */
    private static void updateProcessProgress(DelegateTask task, TaskService taskService) {

        // 电话预约商户(聚引客上门交付服务)
        if (task.getTaskDefinitionKey().startsWith(DeliveryFlowConstant.VISIT_SERVICE_SUBSCRIBE_JYK)) {
            taskService.setVariable(task.getId(), FlowConstant.MATERIAL_SERVICE, FlowConstant.RUNNING);
        }
        // 商户信息收集(首次营销策划服务)
        if (task.getTaskDefinitionKey().startsWith(DeliveryFlowConstant.SHOP_INFO_COLLECTION)) {
            taskService.setVariable(task.getId(), FlowConstant.MARKETING_PLANNING, FlowConstant.RUNNING);
        }

        // 物料内容制作 物料制作跟踪
        if (task.getTaskDefinitionKey().startsWith(DeliveryFlowConstant.MATERIAL_MAKE_SUBMIT) || task.getTaskDefinitionKey()
                        .startsWith(DeliveryFlowConstant.MATERIAL_MAKE_FOLLOW)) {
            taskService.setVariable(task.getId(), FlowConstant.MARKETING_PLANNING, FlowConstant.FINISH);
            taskService.setVariable(task.getId(), FlowConstant.MATERIAL_SERVICE, FlowConstant.RUNNING);
            taskService.setVariable(task.getId(), FlowConstant.ACCOUNT_PAY_OPEN, FlowConstant.FINISH);
        }
        // 上门服务完成（JYK）任务 完成节点
        if (task.getTaskDefinitionKey().startsWith(DeliveryFlowConstant.VISIT_SERVICE_COMPLETE_JYK)) {
            taskService.setVariable(task.getId(), FlowConstant.MATERIAL_SERVICE, FlowConstant.FINISH);
        }
        // 上门服务完成（首次营销策划服务）任务 完成节点
        if (task.getTaskDefinitionKey().startsWith(DeliveryFlowConstant.VISIT_SERVICE_COMPLETE_FIRST)) {
            taskService.setVariable(task.getId(), FlowConstant.MARKETING_PLANNING, FlowConstant.FINISH);
        }
        // 上门服务完成（物料服务）任务 完成节点
        if (task.getTaskDefinitionKey().startsWith(DeliveryFlowConstant.VISIT_SERVICE_COMPLETE_MATERIAL)) {
            taskService.setVariable(task.getId(), FlowConstant.MATERIAL_SERVICE, FlowConstant.FINISH);
        }


    }

}
