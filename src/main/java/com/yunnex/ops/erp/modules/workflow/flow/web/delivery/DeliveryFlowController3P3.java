package com.yunnex.ops.erp.modules.workflow.flow.web.delivery;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlow3P3Service;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpFlowFormService;


/**
 * 聚引客  直销开户相关  Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/delivery/flow/")

public class DeliveryFlowController3P3 extends BaseController {
    @Autowired
    private DeliveryFlow3P3Service deliveryFlow3P3Service;
    @Autowired
    private ErpFlowFormService erpFlowFormService;
    
    /**
     *查看订单信息，指派订单处理人员
     *
     * @param taskId
     * @param procInsId
     * operationAdviser 运营顾问
     * openAccountConsultant 开户顾问
     * materialConsultant 物料顾问
     * @return
     * @date 2018年5月26日
     * @author hanhan
     */
    @RequestMapping(value = "assign_order_handlers_3.3")
    @ResponseBody
    public JSONObject assignOrderHandlers3V3(@RequestBody Map<String, String> map) {
      
        return  deliveryFlow3P3Service.assignOrderHandlers3V3(map);
    }
    
    /**
     *查看订单信息
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    @RequestMapping(value = "getFlowOrderInfo_3.3")
    @ResponseBody
    public JSONObject getOrderInfo(String taskId, String procInsId) {

        return  deliveryFlow3P3Service.getOrderInfo(taskId,procInsId);
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
    public JSONObject getVisitServiceCompleteDetail(String serviceGoalCode, String procInsId) {
        return deliveryFlow3P3Service.getVisitServiceCompleteDetail(serviceGoalCode, procInsId);
        }
    
    @RequestMapping(value = "getFlowFormData")
    @ResponseBody
    public JSONObject getFlowFormData(String taskDef, String procInsId,String formAttrName) {
    	JSONObject obj = new JSONObject();
        String attrValue = erpFlowFormService.findByCondition(taskDef, procInsId, formAttrName);
        obj.put("attrValue", attrValue);
        return obj;
    }
    
}