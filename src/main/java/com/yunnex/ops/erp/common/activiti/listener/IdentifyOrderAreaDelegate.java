package com.yunnex.ops.erp.common.activiti.listener;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Task;
import org.springframework.util.CollectionUtils;

import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.act.dao.ActDao;
import com.yunnex.ops.erp.modules.act.entity.Act;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.sys.constant.RoleConstant;
import com.yunnex.ops.erp.modules.sys.entity.ServiceOperation;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.ServiceOperationService;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.enums.FlowServiceType;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowVariableConstants;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowMonitorService;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 识别订单区域服务
 * 
 * @author caozhijun
 * @see 服务节点对应服务实体 delivery_service_flow-identify_order_area
 */
public class IdentifyOrderAreaDelegate implements JavaDelegate
{
	private ErpOrderOriginalInfoService erpOrderOriginalInfoService = SpringContextHolder.getBean(ErpOrderOriginalInfoService.class);
	private ErpDeliveryServiceService erpDeliveryServiceService = SpringContextHolder.getBean(ErpDeliveryServiceService.class);
	private ServiceOperationService serviceOperationService = SpringContextHolder.getBean(ServiceOperationService.class);
	private UserService userService = SpringContextHolder.getBean(UserService.class);
	private ErpOrderFlowUserService erpOrderFlowUserService = SpringContextHolder.getBean(ErpOrderFlowUserService.class);
	private  ActDao actDao = SpringContextHolder.getBean(ActDao.class);
    private ErpServiceProgressService scheduleService = SpringContextHolder.getBean(ErpServiceProgressService.class);
    private WorkFlowMonitorService workFlowMonitorService = SpringContextHolder.getBean(WorkFlowMonitorService.class);;
    @Override
    public void execute(DelegateExecution execution) throws Exception
    {
       String businessId= execution.getBusinessKey();
        String  procInsId= execution.getProcessInstanceId();

        Act act = new Act();
        act.setBusinessTable("erp_delivery_service");// 业务表名
        act.setBusinessId(businessId); // 业务表ID
        act.setProcInsId(procInsId);
        actDao.updateProcInsIdByBusinessId(act);
        
        ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        String orderId = erpDeliveryService.getOrderId();
        ErpOrderOriginalInfo orderInfo = this.erpOrderOriginalInfoService.get(orderId);
        // 指派运营经理
        ServiceOperation serviceOperation =serviceOperationService.getByServiceNo(orderInfo.getAgentId()+"");
        // 分公司or服务商
        execution.setVariable("agentType", "Agent");
        execution.setVariable("service_startup", "running");
        // 获取运营经理
        if(serviceOperation!=null){
             execution.setVariable("agentType", "Branch");
            String opsManagerId = StringUtils.isNotBlank(serviceOperation.getDefaultManagerId()) ? serviceOperation
                            .getDefaultManagerId() : serviceOperation.getAlternativeManagerId();// 负责人
             execution.setVariable(JykFlowConstants.OPERATION_MANAGER, opsManagerId);
             this.erpOrderFlowUserService.insertOrderFlowUser(opsManagerId, erpDeliveryService.getOrderId(), "", JykFlowConstants.OPERATION_MANAGER, procInsId);
        }else{
		   List<User> operationAdviserUserList = userService.getUserByRoleNameAndAgentId(RoleConstant.AGENT_OPERATION_MANAGER, orderInfo.getAgentId());
		   if (!CollectionUtils.isEmpty(operationAdviserUserList)) {
		       String userId = operationAdviserUserList.get(0).getId();
		       execution.setVariable(JykFlowConstants.OPERATION_MANAGER, userId);
		       this.erpOrderFlowUserService.insertOrderFlowUser(userId, erpDeliveryService.getOrderId(), "", JykFlowConstants.OPERATION_MANAGER, procInsId);
		   }  
        }
        
        String serviceType = erpDeliveryService.getServiceType();
        String zhctType = erpDeliveryService.getZhctType();
        String zhangbeiId = erpDeliveryService.getShopId();
        // 流程中包含的服务集合
        List<String> goodServieTypeList = getGoodServieTypeList(procInsId, zhangbeiId, serviceType, zhctType);
        execution.setVariable(FlowVariableConstants.GOOD_SERVIE_TYPE_LIST, goodServieTypeList);
        // 创建服务进度数据
        scheduleService.createNoBeginSchedule(execution.getVariable("agentType").toString(),procInsId, goodServieTypeList, orderId);
    }

    /**
     * 获取 流程中包含的服务集合
     *
     * @param serviceType
     * @param zhctType
     * @param procInsId
     * @param zhangbeiId
     * @return
     * @date 2018年8月30日
     * @author linqunzhi
     * 
     */
    private List<String> getGoodServieTypeList(String procInsId, String zhangbeiId, String serviceType, String zhctType) {
        List<String> result = new ArrayList<>();
        if (StringUtils.isNotEmpty(serviceType)) {
            result.add(serviceType);
        }
        if (StringUtils.isNotEmpty(zhctType)) {
            result.add(FlowServiceType.ZHI_HUI_CAN_TING.getType());
        } else {
            // 如果是升级 基础版，并且基础版有智慧餐厅
            ErpDeliveryService old = isEasyPlus(zhangbeiId, procInsId);
            if (old != null) {
                if (StringUtils.isNotBlank(old.getZhctType())) {
                    result.add(FlowServiceType.ZHI_HUI_CAN_TING.getType());
                }
            }
        }
        return result;
    }

    /**
     * 套餐否升级 升级返回老的流程
     *
     * @param zhangbeiId
     * @param procInsId
     * @return
     * @date 2018年9月18日
     * @author linqunzhi
     */
    public ErpDeliveryService isEasyPlus(String zhangbeiId, String procInsId) {
        ErpDeliveryService deliveryService = erpDeliveryServiceService.getDeliveryInfoByShopIdAndServiceTypeDesc(zhangbeiId,
                        DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI_BASIC, procInsId);
        List<Task> tasks = null;
        if (null != deliveryService) {
            tasks = workFlowMonitorService.getCurrentTasks(deliveryService.getProcInsId());

        }
        if (deliveryService != null && !CollectionUtils.isEmpty(tasks) && tasks.size() > 0) {
            return deliveryService;
        }
        return null;
    }
}