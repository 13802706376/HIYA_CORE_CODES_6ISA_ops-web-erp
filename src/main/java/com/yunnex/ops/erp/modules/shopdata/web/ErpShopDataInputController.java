package com.yunnex.ops.erp.modules.shopdata.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpShopDataInput;
import com.yunnex.ops.erp.modules.shopdata.service.ErpShopDataInputService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;

/**
 * 商户资料录入
 * 
 * @author SunQ
 * @date 2017年12月8日
 */
@Controller
@RequestMapping(value = "${adminPath}/data/erpShopDataInput")
public class ErpShopDataInputController extends BaseController {

    @Autowired
    private ErpShopDataInputService erpShopDataInputService;
    
    //订单Service
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;

    //任务流Service
    @Autowired
    private WorkFlowService workFlowService;
    
    /**
     * 根据订单指派运营经理, 启动流程
     *
     * @date 2017年12月8日
     * @author SunQ
     */
    @RequestMapping(value = "assignDeal")
    public void assignDeal(@RequestParam(value="orderId", required=true) String orderId) {
     
        /* 获取订单信息 */
        ErpOrderOriginalInfo orderInfo = erpOrderOriginalInfoService.get(orderId);
        
        /* 商户资料录入对象 */
        ErpShopDataInput shopDataInput = new ErpShopDataInput();
        shopDataInput.setOrderId(orderId);
        shopDataInput.setOrderNumber(orderInfo.getOrderNumber());
        shopDataInput.setOrderType(orderInfo.getOrderType());
        shopDataInput.setSource(orderInfo.getSource());
        shopDataInput.setShopId(orderInfo.getShopId());
        shopDataInput.setShopName(orderInfo.getShopName());
        shopDataInput.setAddress("");
        //负责人  开发使用,系统管理员
        shopDataInput.setPlanningExpert("1");
        
        /* 保存对象 */
        erpShopDataInputService.save(shopDataInput);
        
        /* 启动商户资料录入流程 */
        workFlowService.startShopDataInputWorkFlow(shopDataInput.getPlanningExpert(), orderInfo.getId(), shopDataInput.getId());
    }
}