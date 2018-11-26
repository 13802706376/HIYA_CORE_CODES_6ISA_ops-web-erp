package com.yunnex.ops.erp.modules.workflow.flow.web.delivery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceProductRecord;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowVisitService;


/**
 *售后上门服务付费 Controller
 * 
 * @author yunnex
 * @date 2018年7月4日
 */
@Controller
@RequestMapping(value = "${adminPath}/delivery/flow/")

public class DeliveryVistitChargeController extends BaseController {
    @Autowired
    private DeliveryFlowVisitService deliveryFlowVisitService;

    /**
     * 电话预约电话预约上门服务（售后上门服务收费）
     * @RequiresPermissions("visitService:dataEdit:create")
     * @param taskId
     * @param procInsId
     * @return
     */
    @RequestMapping(value = "visit_service_subscribe_charge", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceSubscribeCharge(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj
                    ) {
        Map<String, String>map=new HashMap<String,String>();
        map.put("formAttrName", "affirmVisitTimeList");
        map.put("node", "visit_service_subscribe_first");
        JSONObject resObject = deliveryFlowVisitService.phoneRevationShop(Constant.NO,taskId, procInsId, channelType, paramObj,map);
        return resObject;
    }
    
    /**
     * 上门服务预约申请（售后上门服务收费）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_apply_charge", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceApplyCharge(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="visitServiceApplyRemark";
        String node="visit_service_apply_charge";
        JSONObject resObject =deliveryFlowVisitService.visitServiceRevationApply("N",taskId, procInsId, channelType,
            paramObj, formAttrName, node);
        return resObject;
    }

    
    /**
     * 审核上门服务预约申请（售后上门服务收费）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_review_charge", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceReviewCharge(@RequestParam(required = true, value = "taskId") String taskId,
              @RequestParam(required = false, value = "procInsId") String procInsId,
              @RequestParam(required = false, value = "channelType") String channelType,
              @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="checkVisitServiceRevation";
        String node="visit_service_review_charge";
        JSONObject resObject =deliveryFlowVisitService.checkVisitServiceRevation(taskId, procInsId, paramObj,
            channelType, paramObj.getId(), formAttrName, node);
        return resObject;
    }
    
     /**
     * 上门服务预约申请不通过&修改（售后上门服务收费）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_modify_charge", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceModifyCharge(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="checkNotPassVisitService";
        String node="visit_service_modify_charge";
        JSONObject resObject =deliveryFlowVisitService.checkNotPassVisitService(Constant.NO,taskId, procInsId, paramObj,
            channelType, paramObj.getId(), formAttrName, node);
        return resObject;
    }
    /**
     * 上门服务提醒（售后上门服务收费）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_remind_charge", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceRemindCharge(@RequestParam(required = true, value = "taskId") String taskId,
              @RequestParam(required = false, value = "procInsId") String procInsId,
              @RequestParam(required = false, value = "channelType") String channelType,
              @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="remindVisitService";
        String node="visit_service_remind_charge";
        JSONObject resObject = deliveryFlowVisitService.remindDoorService(taskId, procInsId, channelType, 
            formAttrName, node,paramObj);
        return resObject;
    }
    
    
}