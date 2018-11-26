package com.yunnex.ops.erp.common.activiti.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.asyevent.AsyUpFlowActInst;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowVariableConstants;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowService;

/**
 * 人工任务节点结束事件触发执行器
 * 
 * @date 2018年7月25日
 */
@Service("userTaskCompletetListenerHandler")
public class UserTaskCompletetListenerHandler implements IListenerHandler {

    private ErpDeliveryServiceService erpDeliveryServiceService = SpringContextHolder.getBean(ErpDeliveryServiceService.class);
    private ErpServiceProgressService serviceScheduleService = SpringContextHolder.getBean(ErpServiceProgressService.class);
    private DeliveryFlowService deliveryFlowService = SpringContextHolder.getBean(DeliveryFlowService.class);
    private ErpServiceMessageService serviceMessageService = SpringContextHolder.getBean(ErpServiceMessageService.class);
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService = SpringContextHolder.getBean(ErpOrderOriginalInfoService.class);
    private AsyUpFlowActInst asyUpFlowActInst = SpringContextHolder.getBean(AsyUpFlowActInst.class);

    @Override
    @Transactional(readOnly = false)
    public String doListenerBusiness(DelegateTask task, DelegateExecution execution, Map<String, Object> params) {
        // 更新流程进度
        TaskService taskService = SpringContextHolder.getBean(TaskService.class);
        String taskId = task.getId();// 任务id
        String procInsId = task.getProcessInstanceId(); // 流程id
        String taskDefKey = task.getTaskDefinitionKey();// 任务节点Key
        // 流程中 商品服务类型集合
        List<String> goodServiceTypeList = (List<String>) taskService.getVariable(taskId, FlowVariableConstants.GOOD_SERVIE_TYPE_LIST);
        // 兼容 贝蚁 2.1 上线前的服务类型
        if (CollectionUtils.isEmpty(goodServiceTypeList)) {
            Object serviceType = taskService.getVariable(taskId, DeliveryFlowConstant.SERVICE_TYPE);
            String serviceTypeStr = serviceType == null ? null : serviceType.toString();
            if (serviceTypeStr != null) {
                goodServiceTypeList = new ArrayList<>();
                goodServiceTypeList.add(serviceTypeStr);
            }
        }
        // 服务进度管理
        serviceScheduleService.updateTemplateIdByProcInsId(procInsId, goodServiceTypeList, taskDefKey);
        // 服务通知管理
        serviceMessageService.managerMessage(procInsId, goodServiceTypeList, taskDefKey);

        updateProcessProgress(task, taskService);

        // 售后上门收费结束
        if ("visit_service_remind_charge".equals(task.getTaskDefinitionKey())) {
            saveVisitServiceTime(procInsId);
        }
        
        //判断是否是升级流程
        //根据流程id 查询看是不是升级订单
       
        String  upFlowFlag = execution.getVariable(DeliveryFlowConstant.UP_FLOW_FLAG)==null?"":execution.getVariable(DeliveryFlowConstant.UP_FLOW_FLAG)+"";
        if(StringUtils.isEmpty(upFlowFlag)){
            execution.setVariable(DeliveryFlowConstant.UP_FLOW_FLAG, "N");
            //
           ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
            if(erpDeliveryService!=null&&!StringUtils.isEmpty(erpDeliveryService.getOrderNumber())){
                ErpOrderOriginalInfo orderOriginalInfo= erpOrderOriginalInfoService.findOrderInfoByUpOrderNumber(erpDeliveryService.getOrderNumber());
               if(orderOriginalInfo!=null){
                   execution.setVariable(DeliveryFlowConstant.UP_FLOW_FLAG, Constant.YES); 
                   asyUpFlowActInst.upActInst(procInsId, taskDefKey);
               }
            }
        }else{
            if((Constant.YES).equals(upFlowFlag)){
                //维护升级后流程节点历史时间
                asyUpFlowActInst.upActInst(procInsId, taskDefKey);
            }
        }
        return null;
        
    }

    /**
     *
     * @param procInsId
     * @date 2018年6月8日
     * @author linqunzhi
     */
    private void saveVisitServiceTime(String procInsId) {
        ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (erpDeliveryService == null) {
            throw new ServiceException(CommonConstants.FailMsg.DATA);
        }
        Date now = new Date();
        erpDeliveryService.setFlowEndTime(now);
//        if(StringUtils.isEmpty(erpDeliveryService.getVisitServiceTime())){
//        	erpDeliveryService.setVisitServiceTime(now);
//        }
//        if(StringUtils.isEmpty(erpDeliveryService.getMaterielTime())){
//        	erpDeliveryService.setMaterielTime(now);
//        }
//        if(StringUtils.isEmpty(erpDeliveryService.getTrainTestTime())){
//        	erpDeliveryService.setTrainTestTime(now);
//        }
        if(StringUtils.isEmpty(erpDeliveryService.getShouldTrainTestTime())
        		||StringUtils.isEmpty(erpDeliveryService.getShouldVisitServiceTime())
        		||StringUtils.isEmpty(erpDeliveryService.getShouldMaterielTime())){
        	erpDeliveryService=deliveryFlowService.saveStartTimeOther1(procInsId);
        }
//        if(StringUtils.isEmpty(erpDeliveryService.getShouldTrainTestTime())){
//        	erpDeliveryService.setShouldTrainTestTime(erpDeliveryService.getShouldFlowEndTime());
//        }
//        if(StringUtils.isEmpty(erpDeliveryService.getShouldMaterielTime())){
//        	erpDeliveryService.setShouldMaterielTime(erpDeliveryService.getShouldFlowEndTime());
//        }
//        if(StringUtils.isEmpty(erpDeliveryService.getShouldVisitServiceTime())){
//        	erpDeliveryService.setShouldVisitServiceTime(erpDeliveryService.getShouldFlowEndTime());
//        }
        erpDeliveryServiceService.save(erpDeliveryService);
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

        // 上门服务提醒（JYK）任务 完成节点
        if (task.getTaskDefinitionKey().startsWith(DeliveryFlowConstant.VISIT_SERVICE_REMIND_JYK)) {
            taskService.setVariable(task.getId(), FlowConstant.MATERIAL_SERVICE, FlowConstant.FINISH);
        }
        // 上门服务提醒（首次营销策划服务）任务 完成节点
        if (task.getTaskDefinitionKey().startsWith(DeliveryFlowConstant.VISIT_SERVICE_REMIND_FIRST)) {
            taskService.setVariable(task.getId(), FlowConstant.MARKETING_PLANNING, FlowConstant.FINISH);
        }
        // 上门服务提醒（物料服务）任务 完成节点
        if (task.getTaskDefinitionKey().startsWith(DeliveryFlowConstant.VISIT_SERVICE_REMIND_MATERIAL)) {
            taskService.setVariable(task.getId(), FlowConstant.MATERIAL_SERVICE, FlowConstant.FINISH);
        }


    }

}
