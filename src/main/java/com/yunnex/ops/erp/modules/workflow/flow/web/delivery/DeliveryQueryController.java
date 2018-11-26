package com.yunnex.ops.erp.modules.workflow.flow.web.delivery;

import java.util.List;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceInfo;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceItem;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;

/**
 * 交付服务公共查询 Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/delivery/common/flow")
public class DeliveryQueryController extends BaseController {

    @Autowired
    private ErpDeliveryServiceService erpDeliveryServiceService;
    @Autowired
	private ErpShopInfoService erpShopInfoService;
    @Autowired
	private ErpVisitServiceInfoService erpVisitServiceInfoService;

    /**
     * 
     * 查询上门服务信息
     * 
     * @param serviceGoalCode 上门目的code
     * @param procInsId 流程ID
     * @return
     * @date 2018年7月3日
     * @author zjq
     */
    @RequestMapping(value = "getDoorDetail")
    @ResponseBody
    public JSONObject getDoorDetail(String serviceGoalCode, String procInsId) {

        JSONObject resObject = new JSONObject();
        ErpDeliveryService erpDeliveryService = erpDeliveryServiceService.getDeliveryInfoByProsIncId(procInsId);
        ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiID(erpDeliveryService.getShopId());
        List<String> list = erpVisitServiceInfoService.findVisitIdByProcInsId(procInsId,serviceGoalCode);
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

    
}
