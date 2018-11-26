package com.yunnex.ops.erp.modules.workflow.flow.web.delivery;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowFirstPlanService;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderFileService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowBeiyiService;


/**
 * 聚引客  直销开户相关  Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/delivery/flow/")

public class DeliveryFlowfirstPlanController extends BaseController {
    @Autowired
    private DeliveryFlowFirstPlanService deliveryFlowFirstPlanService;
    @Autowired
    private JykFlowBeiyiService jykFlowBeiyiService;
    @Autowired
    private ErpOrderFileService erpOrderFileService;
    
    /**
     * 商户信息收集（首次营销策划服务）
         * @param taskId
     * @param procInsId
     * @param orderId
     *  
     * @return
     * @date 2018年5月30日
     * @author hanhan
     */
    @RequestMapping(value = "shop_info_collection")
    @ResponseBody
    public JSONObject shopInfoCollection(String taskId, String procInsId,  String orderId,HttpServletRequest request,boolean isFinished ) {
        String jsonStr=  request.getParameter("jsonStr");
        return  deliveryFlowFirstPlanService.shopInfoCollection(taskId, procInsId,orderId,jsonStr, isFinished );
    }
    
    /**
     * 电话预约电话预约上门服务（首次营销策划服务）
     *  @RequiresPermissions("visitService:dataEdit:create")
     * @param taskId
     * @param procInsId
     * @return
     */
    //@RequiresPermissions("visitService:dataEdit:create")
    @RequestMapping(value = "visit_service_subscribe_first", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceSubscribeFirst(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="affirmVisitTimeList";
        String node="visit_service_subscribe_first";
        JSONObject resObject = jykFlowBeiyiService.phoneRevation(taskId, procInsId, paramObj.getId(), channelType, paramObj,
            formAttrName, node);
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
   // @RequiresPermissions("visitService:dataEdit:update")
    @RequestMapping(value = "visit_service_apply_first", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceApplyFirst(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="firstHomeServiceRemark";
        String node="visit_service_apply_first";
        JSONObject resObject =jykFlowBeiyiService.homeServiceRevation(taskId, procInsId, channelType, paramObj.getId(),
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
  //  @RequiresRoles("")
    @RequestMapping(value = "visit_service_review_first", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceReviewFirst(@RequestParam(required = true, value = "taskId") String taskId,
              @RequestParam(required = false, value = "procInsId") String procInsId,
              @RequestParam(required = false, value = "channelType") String channelType,
              @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="checkFirstHomeServiceRevation";
        String node="visit_service_review_first";
        JSONObject resObject =jykFlowBeiyiService.checkHomeServiceRevation(taskId, procInsId, paramObj,
            channelType, paramObj.getId(), formAttrName, node);
        return resObject;
    }
    
     /**
     * 上门服务预约申请不通过
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    //@RequiresPermissions("visitService:dataEdit:create")
    @RequestMapping(value = "visit_service_modify_first", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceModifyFirst(@RequestParam(required = true, value = "taskId") String taskId,
             @RequestParam(required = false, value = "procInsId") String procInsId,
             @RequestParam(required = false, value = "channelType") String channelType,
             @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="checkNotPassFirstHomeService";
        String node="visit_service_modify_first";
        JSONObject resObject =jykFlowBeiyiService.checkNotPassHomeService(taskId, procInsId, paramObj,
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
   // @RequiresPermissions("visitService:dataEdit:create")
    @RequestMapping(value = "visit_service_remind_first", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceRemindFirst(@RequestParam(required = true, value = "taskId") String taskId,
              @RequestParam(required = false, value = "procInsId") String procInsId,
              @RequestParam(required = false, value = "channelType") String channelType,
              @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="remindFirstDoorService";
        String node="visit_service_remind_first";
        JSONObject resObject = jykFlowBeiyiService.remindDoorService(taskId, procInsId, channelType, paramObj.getId(),
            formAttrName, node,paramObj);
        return resObject;
    }
    
    /**
     * 上门服务完成（首次营销策划服务）
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    @RequestMapping(value = "visit_service_complete_first", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject visitServiceCompleteFirst(@RequestParam(required = true, value = "taskId") String taskId,
            @RequestParam(required = false, value = "procInsId") String procInsId,
            @RequestParam(required = false, value = "receivingReport") String receivingReport,
            @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName="completeFirstDoorService";
        String node = DeliveryFlowConstant.VISIT_SERVICE_COMPLETE_FIRST;
        String[] fileIds=receivingReport.split(";");
        List<ErpOrderFile> fileList=new ArrayList<ErpOrderFile>();
        for(String fid:fileIds){
            ErpOrderFile file = this.erpOrderFileService.get(fid);
            fileList.add(file);
        }
        JSONObject resObject =jykFlowBeiyiService.completeDoorService(taskId, procInsId, paramObj, paramObj.getId(),
            formAttrName, node,fileList);
        return resObject;
    }
    
    
}