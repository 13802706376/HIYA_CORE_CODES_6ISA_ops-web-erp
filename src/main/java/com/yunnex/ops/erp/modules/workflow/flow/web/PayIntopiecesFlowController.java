package com.yunnex.ops.erp.modules.workflow.flow.web;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpPayIntopieces;
import com.yunnex.ops.erp.modules.shopdata.service.ErpPayIntopiecesService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayUnionpayService;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayWeixinService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;

/**
 * 支付进件流程处理
 * 
 * @author SunQ
 * @date 2017年12月9日
 */
@Controller
@RequestMapping(value = "${adminPath}/payInto/flow")
public class PayIntopiecesFlowController extends BaseController {

    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private ErpPayIntopiecesService erpPayIntopiecesService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpStorePayWeixinService erpStorePayWeixinService;
    @Autowired
    private ErpStorePayUnionpayService erpStorePayUnionpayService;
    
    /**
     * 确认商户是否需要开通微信支付
     *
     * @param taskId
     * @param procInsId
     * @param channelVal
     * @return
     * @date 2017年12月13日
     * @author SunQ
     */
    @RequestMapping(value = "need_wechat_pay_pay")
    @ResponseBody
    public JSONObject needWechatPayPay(String taskId, String procInsId, String channelVal) {
        
        JSONObject resObject = new JSONObject();
        // 获取支付进件流程对象
        ErpPayIntopieces payInto = erpPayIntopiecesService.getByProsIncId(procInsId);
        ErpStoreInfo store = erpStoreInfoService.get(payInto.getStoreId());
        ErpStorePayWeixin weixin = StringUtils.isNotBlank(store.getWeixinPayId()) ? erpStorePayWeixinService.get(store.getWeixinPayId()) : null;
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("isNeedWechat", channelVal);
        vars.put("isApplyWechat", 2);
        if (weixin!=null && weixin.getAuditStatus().intValue()==2) {
            vars.put("isApplyWechat", 1);
        }
        
        // 修改子任务完成状态
        workFlowService.submitPayIntopiecesSubTask(procInsId, "1", taskId);
        //完成任务
        this.workFlowService.completePayIntopiecesFlow(payInto.getChargePerson(), taskId, procInsId, "商户是否需要开通微信支付成功", vars);
        
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 完成微信支付进件
     *
     * @param taskId
     * @param procInsId
     * @date 2017年12月13日
     * @author SunQ
     */
    @RequestMapping(value = "wechat_pay_state_pay")
    @ResponseBody
    public JSONObject wechatPayStatePay(String taskId, String procInsId) {
        
        JSONObject resObject = new JSONObject();
        // 获取支付进件流程对象
        ErpPayIntopieces payInto = erpPayIntopiecesService.getByProsIncId(procInsId);
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        workFlowService.submitPayIntopiecesSubTask(procInsId, "1", taskId);
        //完成任务
        this.workFlowService.completePayIntopiecesFlow(payInto.getChargePerson(), taskId, procInsId, "完成微信支付进件成功", vars);
        
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 确认商户是否需要开通银联支付
     *
     * @param taskId
     * @param procInsId
     * @param channelVal
     * @return
     * @date 2017年12月13日
     * @author SunQ
     */
    @RequestMapping(value = "need_union_pay_pay")
    @ResponseBody
    public JSONObject needUnionPayPay(String taskId, String procInsId, String channelVal) {
        
        JSONObject resObject = new JSONObject();
        // 获取支付进件流程对象
        ErpPayIntopieces payInto = erpPayIntopiecesService.getByProsIncId(procInsId);
        ErpStoreInfo store = erpStoreInfoService.get(payInto.getStoreId());
        ErpStorePayUnionpay union = StringUtils.isNotBlank(store.getUnionpayId()) ? erpStorePayUnionpayService.get(store.getUnionpayId()) : null;
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("isNeedUnion", channelVal);
        vars.put("isApplyUnion", 2);
        if (union!=null && union.getAuditStatus().intValue()==2) {
            vars.put("isApplyUnion", 1);
        }
        
        // 修改子任务完成状态
        workFlowService.submitPayIntopiecesSubTask(procInsId, "1", taskId);
        //完成任务
        this.workFlowService.completePayIntopiecesFlow(payInto.getChargePerson(), taskId, procInsId, "商户是否需要开通银联支付成功", vars);
        
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 完成银联支付进件
     *
     * @param taskId
     * @param procInsId
     * @date 2017年12月13日
     * @author SunQ
     */
    @RequestMapping(value = "union_pay_state_pay")
    @ResponseBody
    public JSONObject unionPayStatePay(String taskId, String procInsId) {
        
        JSONObject resObject = new JSONObject();
        // 获取支付进件流程对象
        ErpPayIntopieces payInto = erpPayIntopiecesService.getByProsIncId(procInsId);
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        workFlowService.submitPayIntopiecesSubTask(procInsId, "1", taskId);
        //完成任务
        this.workFlowService.completePayIntopiecesFlow(payInto.getChargePerson(), taskId, procInsId, "完成银联支付进件成功", vars);
        
        resObject.put("result", true);
        return resObject;
    }
}