package com.yunnex.ops.erp.modules.workflow.flow.web.jyk;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.StringUtil;
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
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderFileService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowBeiyiService;

/**
 * 聚引客联系商户前段控制Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/jykvisit/flow")
public class JykFlowVisitController extends BaseController {

    @Autowired
    private JykFlowBeiyiService jykFlowBeiyiService;
    @Autowired
	private ErpOrderFileService erpOrderFileService;
    
    /**
     * 电话预约商户（物料实施服务）
     * @RequiresPermissions("visitService:dataEdit:create")
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
         
    @RequestMapping(value = "visit_service_subscribe_jyk", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject phoneRevation(@RequestParam(required = false, value = "taskId") String taskId,
            @RequestParam(required = false, value = "procInsId") String procInsId,
            @RequestParam(required = false, value = "channelType") String channelType,
            @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName = "phoneRevation";
        String node = "visit_service_subscribe_jyk";
        JSONObject resObject = this.jykFlowBeiyiService.phoneRevation(taskId, procInsId, paramObj.getId(), channelType, paramObj,
                formAttrName, node);
        return resObject;
    }

    /**
     * 上门服务预约申请
     * @RequiresPermissions("visitService:dataEdit:update")
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    
    @RequestMapping(value = "visit_service_apply_jyk", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject homeServiceRevation(@RequestParam(required = false, value = "taskId") String taskId,
            @RequestParam(required = false, value = "procInsId") String procInsId,
            @RequestParam(required = false, value = "channelType") String channelType,
            @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName = "homeServiceRevation";
        String node = "visit_service_apply_jyk";
        JSONObject resObject = this.jykFlowBeiyiService.homeServiceRevation(taskId, procInsId, channelType, paramObj.getId(),
                paramObj, formAttrName, node);
        return resObject;
    }

    /**
     * 审核上门服务预约申请
     * 
     * @param taskId
     * @param procInsId
     * @param materialId
     * @RequiresRoles("")
     * @return
     */
    @RequestMapping(value = "visit_service_review_jyk", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject checkHomeServiceRevation(@RequestParam(required = false, value = "taskId") String taskId,
            @RequestParam(required = false, value = "procInsId") String procInsId,
            @RequestParam(required = false, value = "channelType") String channelType,
            @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName = "checkHomeServiceRevation";
        String node = "visit_service_review_jyk";
        JSONObject resObject = this.jykFlowBeiyiService.checkHomeServiceRevation(taskId, procInsId, paramObj,
                channelType, paramObj.getId(), formAttrName, node);
        return resObject;
    }

    /**
     * 上门服务预约申请不通过
     * @RequiresPermissions("visitService:dataEdit:create")
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    
    @RequestMapping(value = "visit_service_modify_jyk", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject checkNotPassHomeService(@RequestParam(required = false, value = "taskId") String taskId,
            @RequestParam(required = false, value = "procInsId") String procInsId,
            @RequestParam(required = false, value = "channelType") String channelType,
            @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName = "checkNotPassHomeService";
        String node = "visit_service_modify_jyk";
        JSONObject resObject = this.jykFlowBeiyiService.checkNotPassHomeService(taskId, procInsId, paramObj,
                channelType, paramObj.getId(), formAttrName, node);
        return resObject;
    }

    /**
     * 上门服务提醒
     * @RequiresPermissions("visitService:dataEdit:create")
     * @param taskId
     * @param procInsId
     * @param materialId
     * @return
     */
    
    @RequestMapping(value = "visit_service_remind_jyk", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject remindDoorService(@RequestParam(required = false, value = "taskId") String taskId,
            @RequestParam(required = false, value = "procInsId") String procInsId,
            @RequestParam(required = false, value = "channelType") String channelType,
            @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName = "remindDoorService";
        String node = "visit_service_remind_jyk";
        JSONObject resObject = this.jykFlowBeiyiService.remindDoorService(taskId, procInsId, channelType, paramObj.getId(),
                formAttrName, node,paramObj);
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
    @RequestMapping(value = "visit_service_complete_jyk", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject completeDoorService(@RequestParam(required = false, value = "taskId") String taskId,
            @RequestParam(required = false, value = "procInsId") String procInsId,
            @RequestParam(required = false, value = "receivingReport") String receivingReport,
            @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName = "completeDoorService";
        String node = "visit_service_complete_jyk";
        String[] fileIds=receivingReport.split(";");
        List<ErpOrderFile> fileList=new ArrayList<ErpOrderFile>();
        for(String fid:fileIds){
        	if(!StringUtil.isBlank(fid)){
        		ErpOrderFile file = this.erpOrderFileService.get(fid);
            	fileList.add(file);
        	}
        }
        JSONObject resObject = this.jykFlowBeiyiService.completeDoorService(taskId, procInsId, paramObj, paramObj.getId(),
                formAttrName, node,fileList);
        return resObject;
    }
}
