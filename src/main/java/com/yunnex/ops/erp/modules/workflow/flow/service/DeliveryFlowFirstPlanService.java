package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopMallForm;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopMallFormService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;

@Service
public class DeliveryFlowFirstPlanService extends BaseService{
    @Autowired
    ErpDeliveryServiceService erpDeliveryServiceService;
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private ErpShopMallFormService erpShopMallFormService;
    
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
    @Transactional(readOnly = false)
    public JSONObject shopInfoCollection(String taskId, String procInsId,String orderId, String jsonStr,boolean isFinished ) {
        logger.info("商户信息收集（首次营销策划服务）start=== taskid[{}],procInsId[{}],orderId[{}], jsonStr[{}]", taskId, procInsId,orderId,jsonStr);
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        ErpDeliveryService erpDeliveryService =erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpShopInfo shopinfo=  erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        if (jsonStr != null) {
            JSONObject jsonJ = JSONObject.parseObject(jsonStr);
            if (jsonJ != null) {
                JSONArray mallFormlist = jsonJ.getJSONArray("mallFormlist");
                if (!CollectionUtils.isEmpty(mallFormlist)) {
                    for (int i = 0; i < mallFormlist.size(); i++) {
                        JSONObject obj = mallFormlist.getJSONObject(i);
                        ErpShopMallForm erpShopMallForm = new ErpShopMallForm();
                        erpShopMallForm.setShopInfoId(shopinfo.getId());
                        String id = obj.getString("id");
                        if (StringUtils.isNotBlank(id)) {
                            erpShopMallForm.setId(id);
                        }
                        erpShopMallForm.setFormAttrName(obj.getString("formAttrName"));
                        erpShopMallForm.setFormAttrValue(obj.getString("formAttrValue"));
                        erpShopMallFormService.save(erpShopMallForm);
                    }
                }
            }
        }
        
        if(isFinished){
            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId,
                "商户信息收集（首次营销策划服务）", vars);    
        }
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
}
