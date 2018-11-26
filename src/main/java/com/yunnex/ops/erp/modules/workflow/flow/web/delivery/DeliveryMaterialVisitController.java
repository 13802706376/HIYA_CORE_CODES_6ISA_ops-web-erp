package com.yunnex.ops.erp.modules.workflow.flow.web.delivery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.sys.constant.RoleConstant;
import com.yunnex.ops.erp.modules.sys.entity.ServiceOperation;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.ServiceOperationService;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.visit.constants.ErpVisitServiceConstants;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceItem;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceProductRecord;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceProductRecordService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpVisitUser;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpFlowFormService;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderFileService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowBeiyiService;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;


/**
 * 聚引客联系商户前段控制Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/visit/flow")
public class DeliveryMaterialVisitController extends BaseController {
	@Autowired
	private JykFlowBeiyiService jykFlowBeiyiService;
	@Autowired
    private ErpFlowFormService erpFlowFormService;
    @Autowired
	private ErpOrderFileService erpOrderFileService;
	@Autowired
	private ErpOrderMaterialCreationService materialCreationService;
	@Autowired
	private ErpVisitServiceInfoService erpVisitServiceInfoService;
	@Autowired
	private ErpDeliveryServiceService erpDeliveryServiceService;
	@Autowired
	private ErpShopInfoService erpShopInfoService;
	@Autowired
	private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
	@Autowired
	private ServiceOperationService serviceOperationService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private UserService userService;
	@Autowired
	private ErpOrderFlowUserService erpOrderFlowUserService;
	@Autowired
    private TaskService taskService;
	@Autowired
	private ErpVisitServiceProductRecordService visitServiceProductRecordService;
	@Autowired
    private ActTaskService actTaskService;
	/**
	 * 根据流程id查询物料制作信息
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param orderFileId
	 * @return,String orderFileId
	 */
	@RequestMapping(value = "getMaterialCreation")
	@ResponseBody
	public JSONObject getMaterialCreation(String procInsId) {
		JSONObject resObject = new JSONObject();
		List<ErpOrderMaterialCreation> list = materialCreationService.findMaterialCreation(procInsId);
		if (list.size() > 1) {
			resObject.put("message", "物料制作单过多有脏数据，请处理{}");
			resObject.put("result", false);
			return resObject;
		}
		if (CollectionUtils.isEmpty(list)) {
			resObject.put("message", "物料未制作{}");
			resObject.put("result", false);
			return resObject;
		}
		resObject.put("message", list.get(0));
		resObject.put("result", true);
		return resObject;
	}

	/**
	 * 上门查询
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param orderFileId
	 * @return,String orderFileId
	 */
	@RequestMapping(value = "getContentList")
	@ResponseBody
	public List<Map<String, Object>> getContentList(String taskId, String procInsId) {
		List<Map<String, Object>> resObject = this.jykFlowBeiyiService.getContentList(taskId, procInsId);
		return resObject;
	}

	/**
	 * 物料制作内容提交
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param orderFileId
	 * @return,String orderFileId
	 */
	@RequestMapping(value = "material_make_submit")
	@ResponseBody
	public JSONObject materialContentSubmit(String taskId, String procInsId, String fileId) {
		JSONObject resObject = new JSONObject();
		ErpOrderFile file = this.erpOrderFileService.get(fileId);
		if (file != null) {
			// 确认物料制作内容提交
			resObject = this.jykFlowBeiyiService.materialContentSubmit(taskId, procInsId, file);
		} else {
			resObject.put("message", "请上传物料制作{}");
			resObject.put("result", false);
		}
		return resObject;
	}

	/**
	 * 物料制作下单并进度同步
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "material_progress_sync")
	@ResponseBody
	public JSONObject materialContentSync(String taskId, String procInsId) {
		JSONObject resObject = new JSONObject();
		if (StringUtil.isBlank(procInsId)) {
			resObject.put("message", "该物料流程不存在{}");
			resObject.put("result", false);
		} else {
			List<ErpOrderMaterialCreation> list = this.materialCreationService.findMaterialCreation(procInsId);
			if (list.size() > 1) {
				resObject.put("message", procInsId + "下的物料流程有多个{}");
				resObject.put("result", false);
			} else {
				if (list.size() == 1) {
					resObject = this.jykFlowBeiyiService.materialContentSync(taskId, procInsId, list.get(0));
				} else {
					resObject.put("message", procInsId + "下的物料流程不存在{}");
					resObject.put("result", false);
				}
			}
		}
		return resObject;
	}

	/**
	 * material_making_tracking 物料制作跟踪
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "material_make_follow")
	@ResponseBody
	public JSONObject materialMakingTracking(String taskId, String procInsId, String channelType) {
		JSONObject resObject = new JSONObject();
		List<ErpOrderMaterialCreation> list = this.materialCreationService.findMaterialCreation(procInsId);
		if (list.size() > 1) {
			resObject.put("message", procInsId + "下的物料流程有多个{}");
			resObject.put("result", false);
		} else {
			if (list.size() == 1) {
				resObject = this.jykFlowBeiyiService.materialMakingTracking(taskId, procInsId, list.get(0),
						channelType);
			} else {
				resObject.put("message", procInsId + "下的物料流程不存在{}");
				resObject.put("result", false);
			}
		}
		return resObject;
	}

	/**
	 * 电话预约商户（物料实施服务）
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "savePhoneRevation", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject savePhoneRevation(@RequestParam(required = false, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		JSONObject resObject = this.jykFlowBeiyiService.savePhoneRevation(taskId, procInsId, paramObj);
		return resObject;
	}

	/**
	 * 电话预约商户（物料实施服务） @RequiresPermissions("visitService:dataEdit:create")
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */

	@RequestMapping(value = "visit_service_subscribe_material", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject phoneRevation(@RequestParam(required = false, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "channelType") String channelType,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		String formAttrName = "phoneRevation";
		String node = "visit_service_subscribe_material";
		JSONObject resObject = this.jykFlowBeiyiService.phoneRevation(taskId, procInsId, paramObj.getId(), channelType, paramObj,
				formAttrName, node);
		return resObject;
	}

	/**
	 * 上门服务预约申请 @RequiresPermissions("visitService:dataEdit:update")
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */

	@RequestMapping(value = "visit_service_apply_material", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject homeServiceRevation(@RequestParam(required = false, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "channelType") String channelType,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		String formAttrName = "homeServiceRevation";
		String node = "visit_service_apply_material";
		JSONObject resObject  = this.jykFlowBeiyiService.homeServiceRevation(taskId, procInsId, channelType, paramObj.getId(), paramObj,
				formAttrName, node);
		return resObject;
	}

	/**
	 * 审核上门服务预约申请
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * 			@RequiresRoles("")
	 * @return
	 */
	@RequestMapping(value = "visit_service_review_material", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject checkHomeServiceRevation(@RequestParam(required = false, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "channelType") String channelType,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		String formAttrName = "checkHomeServiceRevation";
		String node = "visit_service_review_material";
		JSONObject resObject  = this.jykFlowBeiyiService.checkHomeServiceRevation(taskId, procInsId, paramObj, channelType, paramObj.getId(),
				formAttrName, node);
		return resObject;
	}

	/**
	 * 上门服务预约申请不通过 @RequiresPermissions("visitService:dataEdit:create")
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */

	@RequestMapping(value = "visit_service_modify_material", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject checkNotPassHomeService(@RequestParam(required = false, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "channelType") String channelType,
			@RequestParam(required = false, value = "visitId") String visitId,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		String formAttrName = "checkNotPassHomeService";
		String node = "visit_service_modify_material";
		JSONObject resObject  = this.jykFlowBeiyiService.checkNotPassHomeService(taskId, procInsId, paramObj, channelType, paramObj.getId(),
				formAttrName, node);
		return resObject;
	}

	/**
	 * 上门服务提醒 @RequiresPermissions("visitService:dataEdit:create")
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */

	@RequestMapping(value = "visit_service_remind_material", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject remindDoorService(@RequestParam(required = false, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "channelType") String channelType,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		String formAttrName = "remindDoorService";
		String node = "visit_service_remind_material";
		JSONObject resObject  = this.jykFlowBeiyiService.remindDoorService(taskId, procInsId, channelType, paramObj.getId(), formAttrName,
				node, paramObj);
		return resObject;
	}
	
	/**
	 * 上门服务提醒 @RequiresPermissions("visitService:dataEdit:create")
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */

	@RequestMapping(value = "after_visit_service_remind", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject afterRemindDoorService(@RequestParam(required = false, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "channelType") String channelType,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		String formAttrName = "afterRemindDoorService";
		String node = "after_visit_service_remind";
		JSONObject resObject  = this.jykFlowBeiyiService.afterRemindDoorService(taskId, procInsId, channelType, paramObj.getId(), formAttrName,
				node, paramObj);
		return resObject;
	}
	
	/**
	 * 获取当前任务节点taskId
	 * 
	 * @param procInsId
	 * @return
	 */

	@RequestMapping(value = "getCurTaskId")
	@ResponseBody
	public String getCurTaskId(String procInsId,String channel) {
		Task task= this.taskService.createTaskQuery().processInstanceId(procInsId).taskDefinitionKeyLike(channel+"%").singleResult();
		return task==null?null:task.getId();
	}

	/**
	 * 上门服务完成
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "visit_service_complete_material", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject completeDoorService(@RequestParam(required = false, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "receivingReport") String receivingReport,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		String formAttrName = "completeDoorService";
		String node = "visit_service_complete_material";
		String[] fileIds=receivingReport.split(";");
        List<ErpOrderFile> fileList=new ArrayList<ErpOrderFile>();
        for(String fid:fileIds){
        	ErpOrderFile file = this.erpOrderFileService.get(fid);
        	fileList.add(file);
        }
		JSONObject resObject  = this.jykFlowBeiyiService.completeDoorService(taskId, procInsId, paramObj, paramObj.getId(), formAttrName,
				node,fileList);
		return resObject;
	}

	/**
	 * 培训
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "getTrainServiceRecord")
	@ResponseBody
	public JSONObject getTrainServiceRecord(String taskId,String procInsId) {
		JSONObject resObject =this.jykFlowBeiyiService.queryTrainItemRecordOther(taskId,procInsId);
		resObject.put("message", "查询成功");
		return resObject;
	}


	/**
	 * 上门服务流程id查询记录
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "find_process")
	@ResponseBody
	public JSONObject findProcessByProcId(String procInsId) {
		JSONObject resObject = this.jykFlowBeiyiService.findProcessByProcId(procInsId);
		return resObject;
	}

	/**
	 * 保存上门服务接口
	 * 
	 */
	@RequestMapping(value = "saveAftersale", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject saveAftersale(@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		User user = UserUtils.getUser();
		paramObj.setAuditStatus(ErpVisitServiceConstants.AUDIT_STATUS_SUBMITTED);
		JSONObject resObject=this.jykFlowBeiyiService.saveVisitService(paramObj);
		return resObject;
	}

	/**
	 * 电话预约商户（物料实施服务）
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "cancelRevation", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject cancelRevation(@RequestBody(required = false) ErpVisitServiceInfo erpVisitServiceInfo) {
		JSONObject resObject = new JSONObject();
		erpVisitServiceInfoService.cancelVisitService(erpVisitServiceInfo);
		resObject.put("result", true);
		resObject.put("message", "取消成功");
		return resObject;
	}
	
	/**
	 * 电话预约商户（物料实施服务）
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
    @RequestMapping(value = "changeRoleUser", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changeRoleUser(String procInsId,String userOrTeamObj) {
		JSONObject resObject = new JSONObject();
		List<ErpVisitUser> taskUserList = JSON.parseArray(StringEscapeUtils.unescapeHtml4(userOrTeamObj), ErpVisitUser.class);
		for(ErpVisitUser vuser:taskUserList){
			String roleName=vuser.getRoleName();
			String userId=vuser.getUserId();
			this.jykFlowBeiyiService.changeRoleUser(procInsId,userId,roleName);
			if("OperationAdviser".equals(roleName)){
				materialCreationService.updateAdviser(procInsId, userId);
				materialCreationService.changeRoleUser(procInsId, userId, roleName);
			}
			if("operationManager".equals(roleName)){
				materialCreationService.changeRoleUser(procInsId, userId, roleName);
			}
		}
		resObject.put("result", true);
		resObject.put("message", "转派成功");
		return resObject;
	}

	@RequestMapping(value = "changeRoleList")
	@ResponseBody
	public JSONObject changeRoleList(String procInsId) {
		JSONObject resObject = new JSONObject();
		ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        if (erpDeliveryService == null) {
            return resObject;
        }
		ErpOrderOriginalInfo orderOriginalInfo =erpOrderOriginalInfoService.get(erpDeliveryService.getOrderId());
		ServiceOperation serviceOperation = serviceOperationService.getByServiceNo(orderOriginalInfo.getAgentId() + "");
		List<User> userOperationManagerList = null;
		if (serviceOperation != null) {
			userOperationManagerList = systemService.getUserByRoleName(RoleConstant.OPERATION_MANAGER);
			for(User u:userOperationManagerList){
				u.setRemarks(JykFlowConstants.OPERATION_MANAGER);
			}
		} else {
			userOperationManagerList = userService.getUserByRoleNameAndAgentId(RoleConstant.AGENT_OPERATION_MANAGER,
					orderOriginalInfo.getAgentId());
			for(User u:userOperationManagerList){
				u.setRemarks(JykFlowConstants.OPERATION_MANAGER);
			}
		}
		List<User> operationAdviserUserList = null;
		List<User> openAccountConsultantUserList = null;
		List<User> materialConsultantUserList = null;
        String opsAdviserRole;
		if(serviceOperation==null){
            opsAdviserRole = RoleConstant.OPS_ADVISER_AGENT;
			openAccountConsultantUserList = userService.getUserByRoleNameAndAgentId(RoleConstant.ACCOUNT_ADVISER_AGENT, orderOriginalInfo.getAgentId());
            for(User u:openAccountConsultantUserList){
				u.setRemarks(JykFlowConstants.ACCOUNT_ADVISER);
			}
            materialConsultantUserList = userService.getUserByRoleNameAndAgentId(RoleConstant.MATERIALADVISER_AGENT, orderOriginalInfo.getAgentId());
            for(User u:materialConsultantUserList){
				u.setRemarks(JykFlowConstants.MATERIAL_ADVISER);
			}
		}else{
            opsAdviserRole = RoleConstant.OPS_ADVISER;
			openAccountConsultantUserList= systemService.getUserByRoleName(RoleConstant.ACCOUNT_ADVISER);
            for(User u:openAccountConsultantUserList){
            	u.setRemarks(JykFlowConstants.ACCOUNT_ADVISER);
			}
            materialConsultantUserList= systemService.getUserByRoleName(RoleConstant.MATERIALADVISER);
            for(User u:materialConsultantUserList){
            	u.setRemarks(JykFlowConstants.MATERIAL_ADVISER);
			}
        }
        // 订单所属服务商（或分公司）对应的团队下所有运营顾问
        if (Constant.NEGATIVE_ONE != orderOriginalInfo.getAgentId()) {
            operationAdviserUserList = userService.findRoleUsersByAgent(orderOriginalInfo.getAgentId(), opsAdviserRole);
            if (!CollectionUtils.isEmpty(operationAdviserUserList)) {
                for (User u : operationAdviserUserList) {
                    u.setRemarks(JykFlowConstants.OPERATION_ADVISER);
                }
            }
		}
		List<Map<String, String>> list = erpOrderFlowUserService.findByProcInsId(procInsId);
		resObject.put("result", true);
		resObject.put("userOperationManagerList", userOperationManagerList);
		resObject.put("operationAdviserUserList", operationAdviserUserList);
		resObject.put("openAccountConsultantUserList", openAccountConsultantUserList);
		resObject.put("materialConsultantUserList", materialConsultantUserList);
		resObject.put("list", list);
		return resObject;
	}
	
	/**
	 * 上门服务完成
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "getDoorDetail")
	@ResponseBody
	public JSONObject getDoorDetail(String serviceGoalCode, String procInsId) {
		JSONObject resObject = new JSONObject();

        if (StringUtil.isBlank(procInsId)) {
            resObject.put("message", "没有传流程ID！");
            resObject.put("result", false);
            return resObject;
        }

        // 根据流程id查询指定的流程变量值 用于回显用户之前的选择
        //String value = erpFlowFormService.findByProcessIdAndAttrName(procInsId, "affirmVisitTimeList");
        //resObject.put("affirmVisitTimeList", value);

		ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpShopInfo shopInfo=  erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        List<String> list = erpVisitServiceInfoService.findVisitIdByProcInsId(procInsId,serviceGoalCode);
        //获取硬件信息
        List<ErpVisitServiceProductRecord> productlists=null;
        if(!CollectionUtils.isEmpty(list)){
            productlists= visitServiceProductRecordService.getProductInfoByVisitServiceId(list.get(0));
        }
        resObject.put("listProductObj", productlists);
        if (CollectionUtils.isEmpty(list)) {
			ErpVisitServiceInfo esi=new ErpVisitServiceInfo();
			esi.setServiceGoalCode(serviceGoalCode);
			esi.setShopInfoId(shopInfo.getId());
			esi.setProcInsId(procInsId);
			List<ErpVisitServiceItem> ls = erpVisitServiceInfoService.queryServiceItemData(esi);
			resObject.put("message", "查询成功");
			resObject.put("result", true);
			resObject.put("shop", shopInfo);
			resObject.put("result1", null);
			resObject.put("result2", ls);
			return resObject;
		}else{
			if (list.size() > 1) {
				resObject.put("message", "有多条上门服务{}");
				resObject.put("result", false);
				return resObject;
			}
			String visitId = list.get(0);
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
}
