package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;
import com.yunnex.ops.erp.modules.message.constant.ServiceProgressTemplateConstants;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitCommentQuestion;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitCommentQuestionService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.sys.constant.RoleConstant;
import com.yunnex.ops.erp.modules.sys.entity.ServiceOperation;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.ServiceOperationService;
import com.yunnex.ops.erp.modules.sys.service.SysConstantsService;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceItem;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;
import com.yunnex.ops.erp.modules.workflow.acceptance.entity.ErpServiceAcceptance;
import com.yunnex.ops.erp.modules.workflow.acceptance.service.ErpServiceAcceptanceService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.enums.FlowServiceType;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant.QuestionType;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

@Service
public class DeliveryFlow3P3Service extends BaseService {
	@Autowired
	private ErpOrderFlowUserService erpOrderFlowUserService;
	@Autowired
	private ErpDeliveryServiceService erpDeliveryServiceService;
	@Autowired
	private DeliveryFlowService deliveryFlowService;
	@Autowired
	private ErpShopInfoService erpShopInfoService;
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private TaskService taskService;
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private UserService userService;
    @Autowired
    private ServiceOperationService serviceOperationService;
    @Autowired
    private SysConstantsService sysConstantsService;
    @Autowired
    private DeliveryFlowDetailsService deliveryFlowDetailsService;
    @Autowired
    private ErpVisitServiceInfoService erpVisitServiceInfoService;
    @Autowired
    private ErpServiceAcceptanceService erpServiceAcceptanceService;
    @Autowired
    private ErpOrderSplitCommentQuestionService erpOrderSplitCommentQuestionService;
    @Autowired
    private ErpServiceProgressService serviceProgressService;
    @Autowired
    private ErpOrderMaterialCreationService erpOrderMaterialCreationService;
    @Autowired
    private JykFlow3P5Service jykFlow3P5Service;
    
    /**
     * 查看订单信息，指派订单处理人员
     *
     * @param taskId
     * @param procInsId operationAdviser 运营顾问 openAccountConsultant 开户顾问 materialConsultant 物料顾问
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
	@Transactional(readOnly = false)
	public JSONObject assignOrderHandlers3V3(Map<String, String> map) {
		
	    String taskId=map.get("taskId");
        String procInsId=map.get("procInsId");
        String operationAdviser=map.get("operationAdviser");
        String openAccountConsultant=map.get("openAccountConsultant");
        String materialConsultant=map.get("materialConsultant");
        String orderId=map.get("orderId");
        String visitServiceFlag=map.get("visitServiceFlag");
        String boundFlag= map.get("boundFlag");
	    JSONObject resObject = new JSONObject();
		logger.info(
                        "查看订单信息，指派订单处理人员start=== taskid[{}],procInsId[{}],orderId[{}],operationAdviser[{}],openAccountConsultant[{}],materialConsultant[{}]",
				taskId, procInsId, orderId, operationAdviser, openAccountConsultant, materialConsultant);
        // 根据流程id查询交付流程信息
		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpShopInfo shopinfo = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
		if (StringUtils.isNotBlank(operationAdviser)) {
            // 记录运营顾问信息到表erp_order_flow_user
			this.erpOrderFlowUserService.insertOrderFlowUser(operationAdviser, orderId, "",
					JykFlowConstants.OPERATION_ADVISER, procInsId);
            // 查询物料制作表 有没运营顾问
            List<ErpOrderMaterialCreation> materialList= erpOrderMaterialCreationService.findMaterialCreation(procInsId);
            if(!CollectionUtils.isEmpty(materialList)){
                ErpOrderMaterialCreation erpOrderMaterialCreation= materialList.get(0);
                erpOrderMaterialCreation.setOperationAdviserId(operationAdviser);
                User user= userService.get(operationAdviser);
                erpOrderMaterialCreation.setOperationAdviserName(user!=null?user.getName():"");
                erpOrderMaterialCreation.setProcInsId(procInsId);
                erpOrderMaterialCreationService.save(erpOrderMaterialCreation);
                 
            }
            // 1.商户之前没有绑定运营顾问2.商户之前绑定了运营顾问，当前选择的绑定标识为Y,并且商户绑定的运营顾问跟当前选择的不是同一个。这两种情况需要更新商户下面所有订单关联的运营顾问信息。
            if (shopinfo != null && (
                                     StringUtils.isBlank(shopinfo.getOperationAdviserId())
			                         ||!operationAdviser.equals(shopinfo.getOperationAdviserId())
			                         && Constant.YES.equals(boundFlag)
			                        )){
                // 更改商户 所有流程中的运营顾问
			    erpShopInfoService.updateOpsAdviserOfShop(shopinfo.getZhangbeiId(), operationAdviser);
			}
		}
		if (StringUtils.isNotBlank(openAccountConsultant)) {
            // 记录开户顾问信息到表erp_order_flow_user
			this.erpOrderFlowUserService.insertOrderFlowUser(openAccountConsultant, orderId, "",
					JykFlowConstants.ACCOUNT_ADVISER, procInsId);
		}
		if (StringUtils.isNotBlank(materialConsultant)) {
            // 记录物料顾问信息到表erp_order_flow_user
			this.erpOrderFlowUserService.insertOrderFlowUser(materialConsultant, orderId, "",
					JykFlowConstants.MATERIAL_ADVISER, procInsId);
		}
        // 获取服务类型
		String serviceType = (String) taskService.getVariable(taskId, DeliveryFlowConstant.SERVICE_TYPE);
		
        // 获取智慧餐厅类型
		String zhctActType = (String) taskService.getVariable(taskId, DeliveryFlowConstant.ZHCT_ACT_TYPE);
		
		if(DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT_OLD.equals(zhctActType)) {
            // 结束流程
			if (StringUtils.isNotBlank(operationAdviser)) {
                // 保存启动时间 和 其他一些时间
	            deliveryFlowService.saveStartTimeOther(procInsId, serviceType,zhctActType);
                // 删除服务进度
	            this.deleteServiceProgress(procInsId, serviceType, visitServiceFlag);
				Map<String, Object> vars = Maps.newHashMap();
				vars.put("visitServiceFlag", visitServiceFlag);
				vars.put(JykFlowConstants.OPERATION_ADVISER, operationAdviser);
				this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId,
                                "完成指派订单处理人员", vars);
			}
		}else {
            // 结束流程
			if (StringUtils.isNotBlank(operationAdviser) || StringUtils.isNotBlank(openAccountConsultant)
					|| StringUtils.isNotBlank(materialConsultant)) {
                // 保存启动时间 和 其他一些时间
	            deliveryFlowService.saveStartTimeOther(procInsId, serviceType,zhctActType);
                // 删除服务进度
	            this.deleteServiceProgress(procInsId, serviceType, visitServiceFlag);
				Map<String, Object> vars = Maps.newHashMap();
				vars.put("visitServiceFlag", visitServiceFlag);
				vars.put(JykFlowConstants.OPERATION_ADVISER, operationAdviser);
				vars.put(JykFlowConstants.ACCOUNT_ADVISER, openAccountConsultant);
				this.workFlowService.completeFlow2(new String[] { JykFlowConstants.OPERATION_ADVISER,JykFlowConstants.ACCOUNT_ADVISER,JykFlowConstants.MATERIAL_ADVISER}, taskId, procInsId,
                                "完成指派订单处理人员", vars);
			}
		}
        jykFlow3P5Service.activateTask(procInsId, orderId, null);// 将制定的开户顾问+运营顾问同步至聚引客生产流程中
		resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
		return resObject;
	}
	
    private void deleteServiceProgress(String procInsId, String serviceType, String visitServiceFlag) {
        // 如果是聚引客流程 并且选择了不需要上门 删除上门服务进度
        if (FlowServiceType.DELIVERY_JYK.getType().equals(serviceType) && CommonConstants.Sign.NO.equals(visitServiceFlag)) {
            serviceProgressService.deleteByProcInsIdType(procInsId, serviceType, ServiceProgressTemplateConstants.Type.VISIT_SERVICE);
        }

    }

    /**
     * 查询订单详情信息
     */
    public JSONObject getOrderInfo(String taskId,String procInsId) {
        JSONObject resObject = new JSONObject();

        // 根据流程id查询交付流程信息
        ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        // 根据订单id查询原始订单信息
        ErpOrderOriginalInfo orderOriginalInfo =erpOrderOriginalInfoService.get(erpDeliveryService.getOrderId());
        // 设置订单信息到resObject
        deliveryFlowDetailsService.setOrderInfoDto(orderOriginalInfo, resObject,taskId,procInsId);
        // 根据订单的agent_id查询分公司对应的运营经理
        ServiceOperation serviceOperation =serviceOperationService.getByServiceNo(orderOriginalInfo.getAgentId()+"");
        List<User> operationAdviserUserList=null;
        List<User> openAccountConsultantUserList=null;
        List<User> materialConsultantUserList =null;
        String opsAdviserRole;
        if(serviceOperation==null){
            opsAdviserRole = RoleConstant.OPS_ADVISER_AGENT;
            // 开户顾问 服务商
            openAccountConsultantUserList = userService.getUserByRoleNameAndAgentId(RoleConstant.ACCOUNT_ADVISER_AGENT, orderOriginalInfo.getAgentId());
            // 物料顾问 服务商
            materialConsultantUserList = userService.getUserByRoleNameAndAgentId(RoleConstant.MATERIALADVISER_AGENT, orderOriginalInfo.getAgentId());
        }else{
            opsAdviserRole = RoleConstant.OPS_ADVISER;
            // 开户顾问 分公司、erp
            openAccountConsultantUserList= systemService.getUserByRoleName(RoleConstant.ACCOUNT_ADVISER);
            // 物料顾问 分公司、erp
            materialConsultantUserList= systemService.getUserByRoleName(RoleConstant.MATERIALADVISER);
        }
        // 获取指定服务商运营顾问列表
        if (Constant.NEGATIVE_ONE != orderOriginalInfo.getAgentId()) {
            operationAdviserUserList = userService.findRoleUsersByAgent(orderOriginalInfo.getAgentId(), opsAdviserRole);
        }
        String serviceType = (String) taskService.getVariable(taskId, FlowConstant.SERVICETYPE);
        
        // 根据服务项动态显示 角色人员
        String josnStr = sysConstantsService.getConstantValByKey("delivery_assign_roles");
        JSONObject jsonJ = JSONObject.parseObject(josnStr);
        JSONArray rolesLists = jsonJ.getJSONArray(serviceType);
        
        // 服务商的订单去掉“指派物料顾问”
        String agentType = (String) taskService.getVariable(taskId, FlowConstant.AGENTTYPE);
        if("Agent".equals(agentType)) {
        	rolesLists.remove("materialConsultant");
        }
        
        ErpShopInfo shopinfo = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        User user=null;
        if(shopinfo!=null&&StringUtils.isNotBlank(shopinfo.getOperationAdviserId())){
            user =userService.get(shopinfo.getOperationAdviserId());
        }
        resObject.put("IsNewShop", orderOriginalInfo.getIsNewShop());
        resObject.put("serviceType", serviceType);
        resObject.put("rolesLists", rolesLists);
        resObject.put("shopBoundOperationAdviser", user); // 商户绑定的运营顾问
        resObject.put("operationAdviser", operationAdviserUserList);// 指派运营顾问列表
        resObject.put("openAccountConsultant", openAccountConsultantUserList);// 指派开户顾问列表
        resObject.put("materialConsultant", materialConsultantUserList);// 指派物料顾问列表
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
    
	/**
     * 上门服务完成节点
     */
    public JSONObject getVisitServiceCompleteDetail(String serviceGoalCode, String procInsId) {
        JSONObject resObject = new JSONObject();
        ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpShopInfo shopInfo=  erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        List<String> list = erpVisitServiceInfoService.findVisitIdByProcInsId(procInsId,serviceGoalCode);
        if (CollectionUtils.isEmpty(list)|| list.size() > 1) {
            resObject.put("message", "上门服务信息出现脏数据{}");
            resObject.put("result", false);
            return resObject;
        }
        String visitId = list.get(0);
        // 完成评价信息
        ErpServiceAcceptance serviceAcceptance= erpServiceAcceptanceService.getAcceptanceInfoByVisitId(visitId);
        resObject.put("deliveryAcceptInfo ", serviceAcceptance);
        if(serviceAcceptance!=null){
            // 产品信息
            List<ErpOrderSplitCommentQuestion>productInfo =  erpOrderSplitCommentQuestionService.getCommentAnswerByComIdAndType(serviceAcceptance.getId(), QuestionType.SELECT_MULTIPLE_TABLE);  
            resObject.put("productInfo ", productInfo);
            // 交付培训
            List<ErpOrderSplitCommentQuestion> trainInfo =  erpOrderSplitCommentQuestionService.getCommentAnswerByComIdAndType(serviceAcceptance.getId(), QuestionType.SELECT_MULTIPLE);
            resObject.put("trainInfo ", trainInfo);
        }
        
        ErpShopInfo shop=null;
        ErpVisitServiceInfo ll = erpVisitServiceInfoService.get(visitId);
        if (!StringUtil.isBlank(visitId)) {
            shop =shopInfo;
        }
        List<ErpVisitServiceItem> ls = erpVisitServiceInfoService.queryServiceItemData(ll);
        resObject.put("message", "查询成功");
        resObject.put("result", true);
        resObject.put("shop", shop);
        resObject.put("result1", ll);
        resObject.put("result2", ls);
        return resObject;
        
	}
	
	
	
	
	

}
