package com.yunnex.ops.erp.modules.workflow.zhct.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.yunnex.ops.erp.modules.sys.service.SysConstantsService;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlow3P3Service;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowService;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowVisitService;
import com.yunnex.ops.erp.modules.workflow.zhct.constant.ZhctOldShopFlowConstants;


/**
 * 智慧餐厅安装交付（老商户）流程Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/zhctOldShop/")

public class ZhctOldShopController extends BaseController {
	
	@Autowired
    private DeliveryFlow3P3Service deliveryFlow3P3Service;
	 @Autowired
	 private DeliveryFlowService deliveryFlowService;
	 @Autowired
	 private DeliveryFlowVisitService deliveryFlowVisitService;
	 @Autowired
	 private SysConstantsService sysConstantsService;
    
    /**
     *查看订单信息，指派订单处理人员
     */
    @RequestMapping(value = "assign_order_handlers_zhct_old")
    @ResponseBody
    public JSONObject assign_order_handlers_zhct_old(@RequestBody Map<String, String> map) {
        return  deliveryFlow3P3Service.assignOrderHandlers3V3(map);
    }
    
	/**
     *智慧餐厅菜单配置（老商户)
     */
    @RequestMapping(value = "zhct_menu_configuration_old")
    @ResponseBody
    public JSONObject zhct_menu_configuration_old(String taskId,String procInsId, String orderId,HttpServletRequest request) {
    	String jsonStr=  request.getParameter("jsonStr");
    	return  deliveryFlowService.zhctMenuConfiguration(taskId, procInsId,orderId,jsonStr,ZhctOldShopFlowConstants.ZHCT_MENU_CONFIGURATION_OLD);
    }
    
    
    /**
	 * 电话预约电话预约上门服务 （智慧餐厅）
	 * 
	 * @param taskId
	 * @param procInsId 
	 * @return
	 */
	@RequestMapping(value = "visit_service_subscribe_zhct_old", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject visit_service_subscribe_zhct_old(@RequestParam(required = false, value = "orderId") String orderId,@RequestParam(required = true, value = "taskId") String taskId,
			@RequestParam(required = false, value = "procInsId") String procInsId,
			@RequestParam(required = false, value = "channelType") String channelType,
			@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("formAttrName", "visitServiceSubscribePublic");
		map.put("node", "visit_service_subscribe_zhct_old");
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
		@RequestMapping(value = "visit_service_apply_zhct_old", method = RequestMethod.POST)
		@ResponseBody
		public JSONObject visitServiceApplyPublic(@RequestParam(required = true, value = "taskId") String taskId,
				@RequestParam(required = false, value = "procInsId") String procInsId,
				@RequestParam(required = false, value = "visitType") String visitType,
				@RequestParam(required = false, value = "channelType") String channelType,
				@RequestParam(required = false, value = "serviceType") String serviceType,
				@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
			String formAttrName = "visitServiceApplyPublic";
			String node = "visit_service_apply_zhct_old";
			JSONObject resObject = deliveryFlowVisitService.visitServiceRevationApply(Constant.YES,taskId, procInsId, channelType,
					paramObj, formAttrName, node);
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
		@RequestMapping(value = "visit_service_review_zhct_old", method = RequestMethod.POST)
		@ResponseBody
		public JSONObject visit_service_review_zhct_old(@RequestParam(required = true, value = "taskId") String taskId,
				@RequestParam(required = false, value = "procInsId") String procInsId,
				@RequestParam(required = false, value = "channelType") String channelType,
				@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
			String formAttrName = "visitServiceReviewPublic";
			String node = "visit_service_review_zhct_old";
			JSONObject resObject = deliveryFlowVisitService.checkVisitServiceRevation(taskId, procInsId, paramObj,
					channelType, paramObj.getId(), formAttrName, node);
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
		@RequestMapping(value = "visit_service_modify_zhct_old", method = RequestMethod.POST)
		@ResponseBody
		public JSONObject visit_service_modify_zhct_old(@RequestParam(required = true, value = "taskId") String taskId,
				@RequestParam(required = false, value = "procInsId") String procInsId,
				@RequestParam(required = false, value = "channelType") String channelType,
				@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
			String formAttrName = "visitServiceModifyPublic";
			String node = "visit_service_modify_zhct_old";
			JSONObject resObject = deliveryFlowVisitService.checkNotPassVisitService(Constant.YES,taskId, procInsId, paramObj,
					channelType, paramObj.getId(), formAttrName, node);
			return resObject;
		}
    
		 /**
		 * 上门服务提醒（智慧餐厅）
		 * 
		 * @param taskId
		 * @param procInsId
		 * @param materialId
		 * @return
		 */
		@RequestMapping(value = "visit_service_remind_zhct_old", method = RequestMethod.POST)
		@ResponseBody
		public JSONObject visit_service_remind_zhct_old(@RequestParam(required = true, value = "taskId") String taskId,
				@RequestParam(required = false, value = "procInsId") String procInsId,
				@RequestParam(required = false, value = "channelType") String channelType,
				@RequestBody(required = false) ErpVisitServiceInfo paramObj) {
			String formAttrName = "visitServiceRemindPublic";
			String node = "visit_service_remind_zhct_old";
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
		@RequestMapping(value = "visit_service_complete_zhct_old", method = RequestMethod.POST)
		@ResponseBody
		public JSONObject visit_service_complete_zhct_old(@RequestParam(required = false, value = "taskId") String taskId,
				@RequestParam(required = false, value = "procInsId") String procInsId,
				@RequestBody(required = false) ErpVisitServiceInfo paramObj,
				@RequestParam(required = false) boolean isFinished) {
			String formAttrName = "visitServiceCompletePublic";
			String node = "visit_service_complete_zhct_old";
			JSONObject resObject = this.deliveryFlowVisitService.completeVisitService(taskId, procInsId, paramObj,
	         formAttrName, node, isFinished);
			return resObject;
		}
		
		/**
		 * 获取产量表中界面上所有的链接url
		 * @return
		 */
		@RequestMapping(value = "getLinkUrls", method = RequestMethod.GET)
		@ResponseBody
		public JSONObject getConstantLinkUrls() {
			JSONObject obj = new JSONObject();
			String value = sysConstantsService.getConstantValByKey(ZhctOldShopFlowConstants.LINK_URLS);
			if(StringUtils.isNotBlank(value)) {
				obj = JSONObject.parseObject(value);
			}
			return obj;
		}
		
		
}