package com.yunnex.ops.erp.modules.workflow.flow.asyevent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.IdGen;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.act.dao.ActDao;
import com.yunnex.ops.erp.modules.act.service.ActProInstService;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageService;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderMaterialApiService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.sys.service.SysConstantsService;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceProductRecordService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.enums.FlowServiceType;
import com.yunnex.ops.erp.modules.workflow.flow.common.WorkFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowVariableConstants;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowMonitorService;
import com.yunnex.ops.erp.modules.workflow.flow.strategy.DeliveryProcessOperate;

/**
 * @author hanhan
 * @date 2018年8月27日
 */
@Component
public class AsyupdateVisitNodeName {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ActProInstService actProInstService;
    
    @Transactional(readOnly = false)
    @Async // 必须有次注解
    public void updateVisitNodeName(String serviceType, String visitType, String taskName,String taskDefKey,String taskId,String procInsId) {
        try {
            Thread.sleep(2* 1000);
            Map<String,String>map=DeliveryProcessOperate.nodeExtensionNameMap;
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
            actProInstService.updateHiActInstName(taskName, taskDefKey, taskId);
            //
            actProInstService.updateHiTaskInstName(taskName, taskId);
            
          System.out.println("维护名字!=========");
        }catch (Exception e) {
        }
        
        
        }   


}
