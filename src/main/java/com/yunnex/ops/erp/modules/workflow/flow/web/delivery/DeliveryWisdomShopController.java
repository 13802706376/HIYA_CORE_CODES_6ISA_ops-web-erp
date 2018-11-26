package com.yunnex.ops.erp.modules.workflow.flow.web.delivery;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowService;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowVisitService;

/**
 * 交付流程智慧餐厅Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/wisdomShop/flow")
public class DeliveryWisdomShopController extends BaseController {
	 @Autowired
	 private DeliveryFlowService deliveryFlowService;
	 @Autowired
	private DeliveryFlowVisitService deliveryFlowVisitService;
	 
	 /**
	     * 电话联系商户，确认服务内容（智慧餐厅）
	     *
	     * @param taskId
	     * @param procInsId
	     * request 
	     * @return
	     * @date 2018年5月26日
	     * @author hanhan
	     */
	    @RequestMapping(value = "telephone_confirm_service_zhct")
	    @ResponseBody
	    public JSONObject telephone_confirm_service_zhct(String taskId,String procInsId, String orderId,HttpServletRequest request,boolean isFinished) {
	        String jsonStr=  request.getParameter("jsonStr");
	        return  deliveryFlowService.telephoneConfirmService(taskId, procInsId,orderId,jsonStr,isFinished);
	    }
	
	/**
     *智慧餐厅菜单配置(交付流程)
     */
    @RequestMapping(value = "zhct_menu_configuration")
    @ResponseBody
    public JSONObject zhct_menu_configuration(String taskId,String procInsId, String orderId,HttpServletRequest request) {
    	String jsonStr=  request.getParameter("jsonStr");
    	return  deliveryFlowService.zhctMenuConfiguration(taskId, procInsId,orderId,jsonStr,DeliveryFlowConstant.ZHCT_MENU_CONFIGURATION);
    }

    
    /**
	 * 电话预约电话预约上门服务 （智慧餐厅）
	 * 
	 * @param taskId
	 * @param procInsId 
	 * @return
	 */
	@RequestMapping(value = "visit_service_subscribe_zhct", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject visit_service_subscribe_zhct(@RequestParam(required = false, value = "orderId") String orderId,@RequestParam(required = true, value = "taskId") String taskId,
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
		map.put("node", "visit_service_subscribe_zhct");
		JSONObject resObject = deliveryFlowVisitService.phoneRevationShop(Constant.YES,taskId, procInsId, channelType, paramObj,
				map);
		return resObject;
	}
 
    /**
	 * 上门服务预约申请(智慧餐厅)
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "visit_service_apply_zhct", method = RequestMethod.POST)
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
		String node = "visit_service_apply_zhct";
		JSONObject resObject = deliveryFlowVisitService.visitServiceRevationApply(Constant.YES,taskId, procInsId, channelType,
				paramObj, formAttrName, node);
		return resObject;
	}
    
    /**
	 * 上门服务预约申请不通过&修改（智慧餐厅）
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "visit_service_modify_zhct", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject visit_service_modify_zhct(@RequestParam(required = true, value = "taskId") String taskId,
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
		String node = "visit_service_modify_zhct";
		JSONObject resObject = deliveryFlowVisitService.checkNotPassVisitService(Constant.YES,taskId, procInsId, paramObj,
				channelType, paramObj.getId(), formAttrName, node);
		return resObject;
	}
    
    /**
	 * 审核上门服务预约申请(智慧餐厅)
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "visit_service_review_zhct", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject visit_service_review_zhct(@RequestParam(required = true, value = "taskId") String taskId,
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
		String node = "visit_service_review_zhct";
		JSONObject resObject = deliveryFlowVisitService.checkVisitServiceRevation(taskId, procInsId, paramObj,
				channelType, paramObj.getId(), formAttrName, node);
		return resObject;
	}
    
    /**
   /**
	 * 上门服务提醒（智慧餐厅）
	 * 
	 * @param taskId
	 * @param procInsId
	 * @param materialId
	 * @return
	 */
	@RequestMapping(value = "visit_service_remind_zhct", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject visit_service_remind_zhct(@RequestParam(required = true, value = "taskId") String taskId,
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
		String node = "visit_service_remind_zhct";
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
	@RequestMapping(value = "visit_service_complete_zhct", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject visit_service_complete_zhct(@RequestParam(required = false, value = "taskId") String taskId,
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
		String node = "visit_service_complete_zhct";
		JSONObject resObject = this.deliveryFlowVisitService.completeVisitService(taskId, procInsId, paramObj,
formAttrName, node, isFinished);
		return resObject;
	}
}
