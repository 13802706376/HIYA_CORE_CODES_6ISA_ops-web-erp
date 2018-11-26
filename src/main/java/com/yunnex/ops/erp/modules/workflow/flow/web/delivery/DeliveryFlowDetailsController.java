package com.yunnex.ops.erp.modules.workflow.flow.web.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.workflow.flow.service.DeliveryFlowDetailsService;


/**
 *交付流程查看详情信息  Controller
 * 
 * @author yunnex
 * @date 2018年5月30日
 */
@Controller
@RequestMapping(value = "${adminPath}/delivery/flow")
public class DeliveryFlowDetailsController extends BaseController {
    @Autowired
    private DeliveryFlowDetailsService deliveryFlowDetailsService;
   
    /**
     *查看订单信息
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    @RequestMapping(value = "getFlowOrderInfo")
    @ResponseBody
    public JSONObject getOrderInfo(String taskId, String procInsId) {
        return  deliveryFlowDetailsService.getOrderInfo(taskId,procInsId);
    }
    
    /**
     *电话联系商户,确认服务内容 节点  进件资料收集节点
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    @RequestMapping(value = "getFlowOrderAndShopInfo")
    @ResponseBody
    public JSONObject getFlowOrderAndShopInfo(String taskId, String procInsId) {
        return  deliveryFlowDetailsService.getFlowOrderAndShopInfo(taskId,procInsId);
    }
    
    
    /**
     *掌贝信息
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    @RequestMapping(value = "getFlowzhangBeiInfo")
    @ResponseBody
    public JSONObject getFlowzhangBeiInfo(String taskId, String procInsId) {
        return  deliveryFlowDetailsService.getFlowzhangBeiInfo(taskId,procInsId);
    }
    
    /**
     *微信相关的信息
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    @RequestMapping(value = "getFlowWeiXinInfo")
    @ResponseBody
    public JSONObject getFlowWeiXinInfo(String taskId, String procInsId) {
        return  deliveryFlowDetailsService.getFlowWeiXinInfo(taskId,procInsId);
    }
    
    /**
     *支付宝口碑
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    
    @RequestMapping(value = "getFlowAliPaInfo")
    @ResponseBody
    public JSONObject getFlowAliPaInfo(String taskId, String procInsId) {
        return  deliveryFlowDetailsService.getFlowAliPaInfo(taskId,procInsId);
    }
    
    /**
     *银联支付信息
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    
    @RequestMapping(value = "getFlowUnionpayInfo")
    @ResponseBody
    public JSONObject getFlowUnionpayInfo(String taskId, String procInsId) {
        return  deliveryFlowDetailsService.getFlowUnionpayInfo(taskId,procInsId);
    }
    
    /**
     *商户信息收集（首次营销策划服务）
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月26日
     */
    @RequestMapping(value = "getshopInfoCollection")
    @ResponseBody
    public JSONObject getshopInfoCollection(String taskId, String procInsId) {
        return  deliveryFlowDetailsService.getshopInfoCollection(taskId,procInsId);
    }
}