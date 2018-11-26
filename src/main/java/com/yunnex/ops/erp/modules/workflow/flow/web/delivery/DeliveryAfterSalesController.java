package com.yunnex.ops.erp.modules.workflow.flow.web.delivery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.act.dao.ActDao;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceItem;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderFileService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowBeiyiService;

/**
 * 售后上门Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/afterSales/flow")
public class DeliveryAfterSalesController extends BaseController {

    @Autowired
    private JykFlowBeiyiService jykFlowBeiyiService;
    @Autowired
    private ErpOrderFileService erpOrderFileService;
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private ErpVisitServiceInfoService erpVisitServiceInfoService;
    @Autowired
    private ActDao actDao;

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
    public JSONObject getDoorDetail(String procInsId) {
        JSONObject resObject = new JSONObject();
        List<Map<String, String>> code = erpVisitServiceInfoService.findDoorVisitIdByProcInsId(procInsId);
        if (code.size() > 1) {
            resObject.put("serviceGoalCode", code);
            resObject.put("result", false);
            return resObject;
        }
        if (!CollectionUtils.isEmpty(code)) {
            String serviceGoalCode = code.get(0).get("serviceGoalCode");
            String shopInfoId = code.get(0).get("shopInfoId");
            resObject.put("serviceGoalCode", serviceGoalCode);
            resObject.put("shopInfoId", shopInfoId);
            resObject.put("result", true);
        }
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
    @RequestMapping(value = "getDoorDetails")
    @ResponseBody
    public JSONObject getDoorDetail(String serviceGoalCode, String procInsId, String shopInfoId) {
        JSONObject resObject = new JSONObject();
        ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiID(shopInfoId);
        List<String> list = erpVisitServiceInfoService.findVisitIdByProcInsId(procInsId, serviceGoalCode);
        if (CollectionUtils.isEmpty(list)) {
            ErpVisitServiceInfo esi = new ErpVisitServiceInfo();
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
        } else {
            if (list.size() > 1) {
                resObject.put("message", "有多条上门服务{}");
                resObject.put("result", false);
                return resObject;
            }
            String visitId = list.get(0);
            ErpShopInfo shop = null;
            ErpVisitServiceInfo ll = erpVisitServiceInfoService.get(visitId);
            if (!StringUtil.isBlank(visitId)) {
                shop = shopInfo;
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

    @RequestMapping(value = "visit_service_apply", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject homeServiceRevation(@RequestParam(required = false, value = "taskId") String taskId,
                    @RequestParam(required = false, value = "procInsId") String procInsId,
                    @RequestParam(required = false, value = "channelType") String channelType,
                    @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName = "homeServiceRevation";
        String node = "visit_service_apply";
        if(StringUtil.isBlank(taskId)){
        	taskId=actDao.findTaskIdByProcInsId(procInsId).get(0);
        }
        JSONObject resObject = this.jykFlowBeiyiService.serviceRevation(taskId, procInsId, channelType, paramObj.getId(), paramObj, formAttrName,
                        node);
        return resObject;
    }

    /**
     * 审核上门服务预约申请
     * 
     * @param taskId
     * @param procInsId
     * @param materialId @RequiresRoles("")
     * @return
     */
    @RequestMapping(value = "visit_service_review", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject checkHomeServiceRevation(@RequestParam(required = false, value = "taskId") String taskId,
                    @RequestParam(required = false, value = "procInsId") String procInsId,
                    @RequestParam(required = false, value = "channelType") String channelType,
                    @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName = "checkHomeServiceRevation";
        String node = "visit_service_review";
        JSONObject resObject = this.jykFlowBeiyiService.checkHomeServiceRevation(taskId, procInsId, paramObj, channelType, paramObj.getId(),
                        formAttrName, node);
        return resObject;
    }

    /**
     * 修改售后上门服务
     * 
     * @param taskId
     * @param procInsId
     * @param materialId @RequiresRoles("")
     * @return
     */
    @RequestMapping(value = "visit_service_modify", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject checkNotPassHomeService(@RequestParam(required = false, value = "taskId") String taskId,
                    @RequestParam(required = false, value = "procInsId") String procInsId,
                    @RequestParam(required = false, value = "channelType") String channelType,
                    @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName = "checkHomeServiceRevation";
        String node = "visit_service_modify";
        JSONObject resObject = this.jykFlowBeiyiService.serviceRevationOther(taskId, procInsId, channelType, paramObj.getId(), paramObj, formAttrName,
                        node);
        return resObject;
    }

    /**
     * 上门服务提醒
     * 
     * @param taskId
     * @param procInsId
     * @param materialId @RequiresRoles("")
     * @return
     */
    @RequestMapping(value = "visit_service_remind", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject remindDoorService(@RequestParam(required = false, value = "taskId") String taskId,
                    @RequestParam(required = false, value = "procInsId") String procInsId,
                    @RequestParam(required = false, value = "channelType") String channelType,
                    @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName = "remindDoorService";
        String node = "visit_service_remind";
        JSONObject resObject = this.jykFlowBeiyiService.remindDoorService(taskId, procInsId, channelType, paramObj.getId(), formAttrName, node,
                        paramObj);
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
    @RequestMapping(value = "visit_service_complete", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject completeDoorService(@RequestParam(required = false, value = "taskId") String taskId,
                    @RequestParam(required = false, value = "procInsId") String procInsId,
                    @RequestParam(required = false, value = "receivingReport") String receivingReport,
                    @RequestBody(required = false) ErpVisitServiceInfo paramObj) {
        String formAttrName = "completeDoorService";
        String node = "visit_service_complete";
        String[] fileIds = receivingReport.split(";");
        List<ErpOrderFile> fileList = new ArrayList<ErpOrderFile>();
        for (String fid : fileIds) {
            if (!StringUtil.isBlank(fid)) {
                ErpOrderFile file = this.erpOrderFileService.get(fid);
                fileList.add(file);
            }
        }
        JSONObject resObject = this.jykFlowBeiyiService.completeDoorService(taskId, procInsId, paramObj, paramObj.getId(), formAttrName, node,
                        fileList);
        return resObject;
    }
}