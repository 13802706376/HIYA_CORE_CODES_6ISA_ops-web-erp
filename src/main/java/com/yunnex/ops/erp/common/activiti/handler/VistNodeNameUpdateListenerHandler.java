package com.yunnex.ops.erp.common.activiti.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.act.service.ActProInstService;
import com.yunnex.ops.erp.modules.workflow.flow.asyevent.AsyupdateVisitNodeName;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.strategy.DeliveryProcessOperate;

/**
 * 交付流程进度监听实现类
 * 
 * @author zjq
 *
 */
@Service("vistNodeNameUpdateListenerHandler")
public class VistNodeNameUpdateListenerHandler implements IListenerHandler {
    private AsyupdateVisitNodeName asyupdateVisitNodeName = SpringContextHolder.getBean(AsyupdateVisitNodeName.class);
    
    @Override
    @Transactional(readOnly = false)
    public String doListenerBusiness(DelegateTask task, DelegateExecution execution, Map<String, Object> params) {
        
        String visitType = String.valueOf(Optional.ofNullable(execution.getVariable(DeliveryFlowConstant.VISIT_TYPE)).orElse(StringUtils.EMPTY));
        String serviceType = String.valueOf(Optional.ofNullable(execution.getVariable(DeliveryFlowConstant.SERVICE_TYPE)).orElse(StringUtils.EMPTY));
        String procInsId=task.getProcessInstanceId();
        String taskId=task.getId();
        String  taskDefKey=  task.getTaskDefinitionKey();
        String  taskName=  task.getName();
        /* Map<String,String>map=DeliveryProcessOperate.nodeExtensionNameMap;
        String name="";
        if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(serviceType)&&DeliveryFlowConstant.VISIT_TYPE_FMPS_I.equals(visitType)
                       ){
            name=map.get(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI);
        }else if(DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI.equals(serviceType)&&DeliveryFlowConstant.VISIT_TYPE_FMPS_M.equals(visitType)
                        ){
            name=map.get(DeliveryFlowConstant.VISIT_TYPE_FMPS_M);
        }else{
            name=map.get(serviceType); 
        }
        taskName =taskName+"("+name+")";
        asyupdateVisitNodeName.updateHiActInstName(taskName, taskDefKey, taskId);*/
        
        asyupdateVisitNodeName.updateVisitNodeName(serviceType, visitType, taskName, taskDefKey, taskId,procInsId);
        return "";
    }
    
    
    
    
    
    
    
    
}
