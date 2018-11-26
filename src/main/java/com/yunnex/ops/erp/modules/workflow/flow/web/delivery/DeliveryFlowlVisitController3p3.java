package com.yunnex.ops.erp.modules.workflow.flow.web.delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowVisitService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowBeiyiService;


/**
 *售后上门服务付费 Controller
 * 
 * @author yunnex
 * @date 2018年7月4日
 */
@Controller
@RequestMapping(value = "${adminPath}/delivery/flow/")

public class DeliveryFlowlVisitController3p3 extends BaseController {
    @Autowired
    private DeliveryFlowVisitService deliveryFlowVisitService;
    @Autowired
    private ErpOrderMaterialCreationService materialCreationService;
    @Autowired
    private JykFlowBeiyiService jykFlowBeiyiService;
    /**
     * 电话预约电话预约上门服务（聚引客上门交付服务）
     * @RequiresPermissions("visitService:dataEdit:create")
     * @param taskId
     * @param procInsId
     * @return
     */
    @RequestMapping(value = "visit_service_subscribe_jyk_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceSubscribeJyk3V3(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj ) {
        Map<String, String>map=new HashMap<String,String>();
        map.put("formAttrName", "affirmVisitTimeList");
        map.put("node", "visit_service_subscribe_jyk_3.3");
        JSONObject resObject = deliveryFlowVisitService.phoneRevationShop(Constant.NO,taskId, procInsId, channelType, paramObj,map);
        return resObject;
    }
    
    /**
     * 上门服务预约申请（聚引客上门交付服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_apply_jyk_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceApplyJyk3V3(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="visitServiceApplyRemark";
        String node="visit_service_apply_jyk_3.3";
        JSONObject resObject =deliveryFlowVisitService.visitServiceRevationApply("N",taskId, procInsId, channelType,
            paramObj, formAttrName, node);
        return resObject;
    }

    
    /**
     * 审核上门服务预约申请（聚引客上门交付服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_review_jyk_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceReviewJyk3V3(@RequestParam(required = true, value = "taskId") String taskId,
              @RequestParam(required = false, value = "procInsId") String procInsId,
              @RequestParam(required = false, value = "channelType") String channelType,
              @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="checkVisitServiceRevation";
        String node="visit_service_review_jyk_3.3";
        JSONObject resObject =deliveryFlowVisitService.checkVisitServiceRevation(taskId, procInsId, paramObj,
            channelType, paramObj.getId(), formAttrName, node);
        return resObject;
    }
    
     /**
     * 上门服务预约申请不通过&修改（聚引客上门交付服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_modify_jyk_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceModifyJyk3V3(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="checkNotPassVisitService";
        String node="visit_service_modify_jyk_3.3";
        JSONObject resObject =deliveryFlowVisitService.checkNotPassVisitService(Constant.NO,taskId, procInsId, paramObj,
            channelType, paramObj.getId(), formAttrName, node);
        return resObject;
    }
    /**
     * 上门服务提醒（聚引客上门交付服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_remind_jyk_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceRemindJyk3V3(@RequestParam(required = true, value = "taskId") String taskId,
              @RequestParam(required = false, value = "procInsId") String procInsId,
              @RequestParam(required = false, value = "channelType") String channelType,
              @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="remindVisitService";
        String node="visit_service_remind_jyk_3.3";
        JSONObject resObject = deliveryFlowVisitService.remindDoorService(taskId, procInsId, channelType, 
            formAttrName, node,paramObj);
        return resObject;
    }

    /**
     * 上门服务完成（聚引客上门交付服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_complete_jyk_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject completeDoorService(@RequestParam(required = false, value = "taskId") String taskId,
            @RequestParam(required = false, value = "procInsId") String procInsId,
            @RequestParam(required = false, value = "receivingReport") String receivingReport,
            @RequestBody(required = false) ErpVisitServiceInfo paramObj,
            @RequestBody(required = false) boolean isFinished) {
        String formAttrName = "completeDoorService";
        String node = "visit_service_complete_jyk_3.3";
        JSONObject resObject = this.deliveryFlowVisitService.completeVisitService(taskId, procInsId, paramObj, 
                formAttrName, node,isFinished);
        return resObject;
    }
    
    
    
    
    /**
     * 电话预约电话预约上门服务（物料实施服务）
     * @RequiresPermissions("visitService:dataEdit:create")
     * @param taskId
     * @param procInsId
     * @return
     */
    @RequestMapping(value = "visit_service_subscribe_material_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceSubscribeMaterial3V3(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        Map<String, String>map=new HashMap<String,String>();
        map.put("formAttrName", "affirmVisitTimeList");
        map.put("node", "visit_service_subscribe_material_3.3");
        JSONObject resObject = deliveryFlowVisitService.phoneRevationShop(Constant.NO,taskId, procInsId, channelType, paramObj,map);
        return resObject;
    }
    
    /**
     * 上门服务预约申请（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_apply_material_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceApplyMaterial3V3(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="visitServiceApplyRemark";
        String node="visit_service_apply_material_3.3";
        JSONObject resObject =deliveryFlowVisitService.visitServiceRevationApply("N",taskId, procInsId, channelType,
            paramObj, formAttrName, node);
        return resObject;
    }

    
    /**
     * 审核上门服务预约申请（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_review_material_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceReviewMaterial3V3(@RequestParam(required = true, value = "taskId") String taskId,
              @RequestParam(required = false, value = "procInsId") String procInsId,
              @RequestParam(required = false, value = "channelType") String channelType,
              @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="checkVisitServiceRevation";
        String node="visit_service_review_material_3.3";
        JSONObject resObject =deliveryFlowVisitService.checkVisitServiceRevation(taskId, procInsId, paramObj,
            channelType, paramObj.getId(), formAttrName, node);
        return resObject;
    }
    
     /**
     * 上门服务预约申请不通过&修改（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_modify_material_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceModifyMaterial3V3(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="checkNotPassVisitService";
        String node="visit_service_modify_material_3.3";
        JSONObject resObject =deliveryFlowVisitService.checkNotPassVisitService(Constant.NO,taskId, procInsId, paramObj,
            channelType, paramObj.getId(), formAttrName, node);
        return resObject;
    }
    /**
     * 上门服务提醒（物料实施服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_remind_material_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceRemindMaterial3V3(@RequestParam(required = true, value = "taskId") String taskId,
              @RequestParam(required = false, value = "procInsId") String procInsId,
              @RequestParam(required = false, value = "channelType") String channelType,
              @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="remindVisitService";
        String node="visit_service_remind_material_3.3";
        JSONObject resObject = deliveryFlowVisitService.remindDoorService(taskId, procInsId, channelType, 
            formAttrName, node,paramObj);
        return resObject;
    }
    
    /**
     * 电话预约电话预约上门服务（首次营销策划服务）
     * @RequiresPermissions("visitService:dataEdit:create")
     * @param taskId
     * @param procInsId
     * @return
     */
    @RequestMapping(value = "visit_service_subscribe_first_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceSubscribeFirst3V3(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        Map<String, String>map=new HashMap<String,String>();
        map.put("formAttrName", "affirmVisitTimeList");
        map.put("node", "visit_service_subscribe_first_3.3");
        JSONObject resObject = deliveryFlowVisitService.phoneRevationShop(Constant.NO,taskId, procInsId, channelType, paramObj,map);
        return resObject;
    }
    
    /**
     * 上门服务预约申请（首次营销策划服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_apply_first_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceApplyFirst3V3(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="visitServiceApplyRemark";
        String node="visit_service_apply_first_3.3";
        JSONObject resObject =deliveryFlowVisitService.visitServiceRevationApply("N",taskId, procInsId, channelType,
            paramObj, formAttrName, node);
        return resObject;
    }

    
    /**
     * 审核上门服务预约申请（首次营销策划服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_review_first_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceReviewFirst3V3(@RequestParam(required = true, value = "taskId") String taskId,
              @RequestParam(required = false, value = "procInsId") String procInsId,
              @RequestParam(required = false, value = "channelType") String channelType,
              @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="checkVisitServiceRevation";
        String node="visit_service_review_first_3.3";
        JSONObject resObject =deliveryFlowVisitService.checkVisitServiceRevation(taskId, procInsId, paramObj,
            channelType, paramObj.getId(), formAttrName, node);
        return resObject;
    }
    
     /**
     * 上门服务预约申请不通过&修改（首次营销策划服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_modify_first_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceModifyFirst3V3(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="checkNotPassVisitService";
        String node="visit_service_modify_first_3.3";
        JSONObject resObject =deliveryFlowVisitService.checkNotPassVisitService(Constant.NO,taskId, procInsId, paramObj,
            channelType, paramObj.getId(), formAttrName, node);
        return resObject;
    }
    /**
     * 上门服务提醒（首次营销策划服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_remind_first_3.3", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceRemindFirst3V3(@RequestParam(required = true, value = "taskId") String taskId,
              @RequestParam(required = false, value = "procInsId") String procInsId,
              @RequestParam(required = false, value = "channelType") String channelType,
              @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="remindVisitService";
        String node="visit_service_remind_first_3.3";
        JSONObject resObject = deliveryFlowVisitService.remindDoorService(taskId, procInsId, channelType, 
            formAttrName, node,paramObj);
        return resObject;
    }
    
   
    /**
     * 电话预约上门服务（首次上门服务基础版）
     * @RequiresPermissions
     * @param taskId
     * @param procInsId
     * @return
     */
    @RequestMapping(value = "visit_service_subscribe_basic", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceSubscribeBasic(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj ) {
        Map<String, String>map=new HashMap<String,String>();
        map.put("formAttrName", "affirmVisitTimeList");
        map.put("node", "visit_service_subscribe_basic");
        JSONObject resObject = deliveryFlowVisitService.phoneRevationShop(Constant.NO,taskId, procInsId, channelType, paramObj,map);
        return resObject;
    }
    
    /**
     * 上门服务预约申请（首次上门服务基础版）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_apply_basic", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceApplyBasic(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="visitServiceApplyRemark";
        String node="visit_service_subscribe_basic";
        JSONObject resObject =deliveryFlowVisitService.visitServiceRevationApply("N",taskId, procInsId, channelType,
            paramObj, formAttrName, node);
        return resObject;
    }

    
    /**
     * 审核上门服务预约申请（首次上门服务基础版）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_review_basic", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceReviewBasic(@RequestParam(required = true, value = "taskId") String taskId,
              @RequestParam(required = false, value = "procInsId") String procInsId,
              @RequestParam(required = false, value = "channelType") String channelType,
              @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="checkVisitServiceRevation";
        String node="visit_service_subscribe_basic";
        JSONObject resObject =deliveryFlowVisitService.checkVisitServiceRevation(taskId, procInsId, paramObj,
            channelType, paramObj.getId(), formAttrName, node);
        return resObject;
    }
    
     /**
     * 上门服务预约申请不通过&修改（首次上门服务基础版）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_modify_basic", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceModifyBasic(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="checkNotPassVisitService";
        String node="visit_service_subscribe_basic";
        JSONObject resObject =deliveryFlowVisitService.checkNotPassVisitService(Constant.NO,taskId, procInsId, paramObj,
            channelType, paramObj.getId(), formAttrName, node);
        return resObject;
    }
    /**
     * 上门服务提醒（首次上门服务基础版）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_remind_basic", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceRemindBasic(@RequestParam(required = true, value = "taskId") String taskId,
              @RequestParam(required = false, value = "procInsId") String procInsId,
              @RequestParam(required = false, value = "channelType") String channelType,
              @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="remindVisitService";
        String node="visit_service_subscribe_basic";
        JSONObject resObject = deliveryFlowVisitService.remindDoorService(taskId, procInsId, channelType, 
            formAttrName, node,paramObj);
        return resObject;
    }
    
    
    /**
     *  物料部署（首次基础版）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "material_deploy_service_first")
    @ResponseBody
    public JSONObject materialDeployServiceUpdate(String taskId, String procInsId,String completeMaterialDeployVideoShop) {
        JSONObject resObject = new JSONObject();
        List<ErpOrderMaterialCreation> list = this.materialCreationService.findMaterialCreation(procInsId);
        if (list.size() > 1) {
            resObject.put("message", procInsId + "下的物料流程有多个{}");
            resObject.put("result", false);
        } else {
            if (list.size() == 1) {
                String node="material_deploy_service_first";
                resObject = this.jykFlowBeiyiService.materialDeployServiceUpdate(taskId, procInsId, list.get(0),
                    completeMaterialDeployVideoShop,node);
            } else {
                resObject.put("message", procInsId + "下的物料流程不存在{}");
                resObject.put("result", false);
            }
        }
        return resObject;
    }
    
    @RequestMapping(value = "material_make_follow_first")
    @ResponseBody
    public JSONObject materialMakingTrackingFirst(String taskId, String procInsId, String channelType) {
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
    

    
}