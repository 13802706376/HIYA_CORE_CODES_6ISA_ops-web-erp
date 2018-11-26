package com.yunnex.ops.erp.modules.workflow.flow.web.delivery;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowVisitService;

/**
 * 售后上门服务付费 Controller
 * 
 * @author yunnex
 * @date 2018年7月4日
 */
@Controller
@RequestMapping(value = "${adminPath}/delivery/flow/2P0")
public class DeliveryFlowlVisitController2P0 {
	@Autowired
	private DeliveryFlowVisitService deliveryFlowVisitService;

	/**
	 * 电话预约电话预约上门服务 @RequiresPermissions("visitService:dataEdit:create")
	 * 
	 * @param taskId
	 * @param procInsId 
	 * @return
	 */
	@RequestMapping(value = "visit_service_subscribe_public", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject visitServiceSubscribePublic(@RequestParam(required = true, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "serviceType") String serviceType,
			@RequestParam(required = false, value = "visitType") String visitType,
			@RequestParam(required = false, value = "channelType") String channelType,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		if (StringUtil.isBlank(serviceType)) {
			JSONObject resObject = new JSONObject();
			resObject.put("message", "请提供服务类型");
			resObject.put("result", false);
			return resObject;
		}
		Map<String, String> map = new HashMap<String, String>();
        map.put("formAttrName", "visitServiceSubscribePublic_" + visitType);
		map.put("node", "visit_service_subscribe_public_" + serviceType + "_" + visitType);
		JSONObject resObject = deliveryFlowVisitService.phoneRevationShop(Constant.NO,taskId, procInsId, channelType, paramObj,
				map);
		return resObject;
	}

	/**
	 * 上门服务预约申请
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "visit_service_apply_public", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject visitServiceApplyPublic(@RequestParam(required = true, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "visitType") String visitType,
			@RequestParam(required = false, value = "channelType") String channelType,
			@RequestParam(required = false, value = "serviceType") String serviceType,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		if (StringUtil.isBlank(serviceType)) {
			JSONObject resObject = new JSONObject();
			resObject.put("message", "请提供服务类型");
			resObject.put("result", false);
			return resObject;
		}
		String formAttrName = "visitServiceApplyPublic";
		String node = "visit_service_apply_public_" + serviceType + "_" + visitType;
		JSONObject resObject = deliveryFlowVisitService.visitServiceRevationApply(Constant.NO,taskId, procInsId, channelType,
				paramObj, formAttrName, node);
		return resObject;
	}

	/**
	 * 审核上门服务预约申请
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "visit_service_review_public", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject visitServiceReviewPublic(@RequestParam(required = true, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "visitType") String visitType,
			@RequestParam(required = false, value = "serviceType") String serviceType,
			@RequestParam(required = false, value = "channelType") String channelType,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		if (StringUtil.isBlank(serviceType)) {
			JSONObject resObject = new JSONObject();
			resObject.put("message", "请提供服务类型");
			resObject.put("result", false);
			return resObject;
		}
		String formAttrName = "visitServiceReviewPublic";
		String node = "visit_service_review_public_" + serviceType + "_" + visitType;
		JSONObject resObject = deliveryFlowVisitService.checkVisitServiceRevation(taskId, procInsId, paramObj,
				channelType, paramObj.getId(), formAttrName, node);
		return resObject;
	}

	/**
	 * 上门服务预约申请不通过&修改
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "visit_service_modify_public", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject visitServiceModifyPublic(@RequestParam(required = true, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "visitType") String visitType,
			@RequestParam(required = false, value = "serviceType") String serviceType,
			@RequestParam(required = false, value = "channelType") String channelType,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		if (StringUtil.isBlank(serviceType)) {
			JSONObject resObject = new JSONObject();
			resObject.put("message", "请提供服务类型");
			resObject.put("result", false);
			return resObject;
		}
		String formAttrName = "visitServiceModifyPublic";
		String node = "visit_service_modify_public_" + serviceType + "_" + visitType;
		JSONObject resObject = deliveryFlowVisitService.checkNotPassVisitService(Constant.NO,taskId, procInsId, paramObj,
				channelType, paramObj.getId(), formAttrName, node);
		return resObject;
	}

	/**
	 * 上门服务提醒
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "visit_service_remind_public", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject visitServiceRemindPublic(@RequestParam(required = true, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "visitType") String visitType,
			@RequestParam(required = false, value = "serviceType") String serviceType,
			@RequestParam(required = false, value = "channelType") String channelType,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		if (StringUtil.isBlank(serviceType)) {
			JSONObject resObject = new JSONObject();
			resObject.put("message", "请提供服务类型");
			resObject.put("result", false);
			return resObject;
		}
		String formAttrName = "visitServiceRemindPublic";
		String node = "visit_service_remind_public_" + serviceType + "_" + visitType;
		JSONObject resObject = deliveryFlowVisitService.remindDoorService(taskId, procInsId, channelType, formAttrName,
				node, paramObj);
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
	@RequestMapping(value = "visit_service_complete_public", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject visitServiceCompletePublic(@RequestParam(required = false, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "visitType") String visitType,
			@RequestParam(required = false, value = "serviceType") String serviceType,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj,
			@RequestParam(required = false) boolean isFinished) {
		if (StringUtil.isBlank(serviceType)) {
			JSONObject resObject = new JSONObject();
			resObject.put("message", "请提供服务类型");
			resObject.put("result", false);
			return resObject;
		}
		String formAttrName = "visitServiceCompletePublic";
		String node = "visit_service_complete_public_" + serviceType + "_" + visitType;
		JSONObject resObject = this.deliveryFlowVisitService.completeVisitService(taskId, procInsId, paramObj,
formAttrName, node, isFinished);
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
	@RequestMapping(value = "getVisitServiceCompleteDetail")
	@ResponseBody
    public JSONObject getVisitServiceCompleteDetail(String serviceGoalCode, String procInsId, String visitType,String taskDefKey,String serviceType) {
        return this.deliveryFlowVisitService.getVisitServiceCompleteDetail(serviceGoalCode, procInsId, visitType,taskDefKey,serviceType);
	}
}
