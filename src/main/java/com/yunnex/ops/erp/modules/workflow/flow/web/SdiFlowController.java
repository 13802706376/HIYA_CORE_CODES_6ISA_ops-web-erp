package com.yunnex.ops.erp.modules.workflow.flow.web;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.utils.MD5Util;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalGoodService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoApiService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpShopDataInput;
import com.yunnex.ops.erp.modules.shopdata.service.ErpShopDataInputService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.sys.utils.DwrUtils;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SdiFlow;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpShopDataInputSubTaskService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowAccountSignalService;
import com.yunnex.ops.erp.modules.workflow.flow.service.SdiFlowService;
import com.yunnex.ops.erp.modules.workflow.flow.service.SdiFlowSignalService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 商户资料录入处理
 * 
 * @author SunQ
 * @date 2017年12月9日
 */
@Controller
@RequestMapping(value = "${adminPath}/sdi/flow")
public class SdiFlowController extends BaseController {

    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private SdiFlowService sdiFlowService;
    @Autowired
    private ErpShopDataInputService erpShopDataInputService;
    @Autowired
    private ErpShopDataInputSubTaskService erpShopDataInputSubTaskService;
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    private ErpOrderOriginalGoodService erpOrderOriginalGoodService;
    @Autowired
    private ErpShopInfoApiService erpShopInfoApiService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private JykFlowAccountSignalService jykFlowAccountSignalService;
    @Autowired
    private SdiFlowSignalService sdiFlowSignalService;
    @Autowired
    private DwrUtils dwrUtils;
    
    /**
     * 指派运营顾问
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @param operationAdviser
     * @param isFinished
     * @return
     * @date 2017年12月9日
     * @author Administrator
     */
    @RequestMapping(value = "assign_operation_adviser_shop")
    @ResponseBody
    public JSONObject assignOperationAdviser(String taskId, String procInsId, String operationAdviser) {
        
        JSONObject resObject = new JSONObject();
        
        ErpShopDataInput dataInput = this.erpShopDataInputService.getByProsIncId(procInsId);
        ErpOrderOriginalInfo orderInfo = this.erpOrderOriginalInfoService.get(dataInput.getOrderId());
        ErpShopInfo erpShopInfo = this.erpShopInfoService.findListByZhangbeiId(dataInput.getShopId());
        
        Map<String, Object> vars = Maps.newHashMap();
        // 订单中是否存在商户编码
        if(StringUtils.isNotBlank(orderInfo.getShopNumber())){
            // 判断本地数据库是否存在该商户
            if(erpShopInfo==null){
                resObject.put("result", false);
                resObject.put("message", "商户数据没有同步，待商户数据同步后再处理该流程。");
                return resObject;
            }
            
            // 是否掌贝进件
            vars.put("isZhangbeiJJ", 2);
            if (StringUtils.isNotBlank(erpShopInfo.getPassword())) {
                // 掌贝已进件
                vars.put("isZhangbeiJJ", 1);
            }else{
                // 未掌贝进件
                String zhangbeiID = erpShopInfo.getZhangbeiId();
                // 使用掌贝账号的后6位作为密码,并进行MD5加密
                if(zhangbeiID.length() < 6){
                    // 不足6位的情况，在末尾补'0'
                    zhangbeiID = StringUtils.rightPad(zhangbeiID, 6, '0');
                }
                String passWord = MD5Util.md5(zhangbeiID.substring(zhangbeiID.length()-6, zhangbeiID.length()));
                if(erpShopInfoService.updateShopPassword(erpShopInfo.getZhangbeiId(), passWord)){
                    // 同步到商户后台
                    erpShopInfoApiService.syncShopPasswordApi(erpShopInfo.getZhangbeiId(), passWord);
                }
            }
        }else{
            String zhangbeiID = orderInfo.getShopId();
            if(erpShopInfoService.countShopByZhangbeiId(zhangbeiID) == 0){
                // 使用掌贝账号的后6位作为密码,并进行MD5加密
                if(zhangbeiID.length() < 6){
                    // 不足6位的情况，在末尾补'0'
                    zhangbeiID = StringUtils.rightPad(zhangbeiID, 6, '0');
                }
                String passWord = MD5Util.md5(zhangbeiID.substring(zhangbeiID.length()-6, zhangbeiID.length()));
                
                ErpShopInfo addshopinfo = new ErpShopInfo();
                addshopinfo.setName(orderInfo.getShopName());
                // 商户来源为ERP添加
                addshopinfo.setSource("1");
                addshopinfo.setZhangbeiId(zhangbeiID);
                addshopinfo.setPassword(passWord);
                addshopinfo.setOrderId(orderInfo.getId());
                addshopinfo.setAgentId(orderInfo.getAgentId());
                erpShopInfoService.save(addshopinfo);
            }
            
            // 新商户，需要新增商户信息
            vars.put("isZhangbeiJJ", 2);
        }
        
        // 保存业务流转信息
        this.sdiFlowService.assignOperationAdviser(procInsId, operationAdviser, taskId);
        // 保存指派人员信息
        this.erpOrderFlowUserService.insertOrderFlowUser(operationAdviser, dataInput.getOrderId(), "", JykFlowConstants.OPERATION_ADVISER, procInsId);
        
        // 完成任务
        this.workFlowService.completeShopDataInputFlow(operationAdviser, taskId, procInsId,
                        UserUtils.getUser().getId() + "指派运营顾问为:" + operationAdviser, vars);
        
        // 通知聚引客流程已经指派运营顾问
        jykFlowAccountSignalService.assignConsultant(dataInput.getShopId(), operationAdviser);
        
        dwrUtils.dwr(operationAdviser);
            
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 联系老商户
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2017年12月11日
     * @author Administrator
     */
    @RequestMapping(value = "conact_old_shop_shop")
    @ResponseBody
    public JSONObject conactOldShop(String taskId, String procInsId, String channelList, String hasZzqq, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        
        // 获取商户资料录入流程对象
        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(procInsId);
        Map<String, Object> vars = Maps.newHashMap();
        
        // 是否包含聚引客订单
        boolean hasJyk = false;
        List<ErpOrderOriginalGood> orderGoods = erpOrderOriginalGoodService.findListByOrderId(sdiFlow.getOrderId());
        if(orderGoods != null){
            for(ErpOrderOriginalGood orderGood : orderGoods){
                // 聚引客订单
                if (isJykGood(orderGood)) {
                    hasJyk = true;
                    break;
                }
            }
        }
        if(hasJyk){
            vars.put("hasJykOrder", 1);
        }else{
            vars.put("hasJykOrder", 2);
        }
        
        // 修改子任务完成状态
        workFlowService.submitShopDataInputSubTask(procInsId, channelList, taskId);
        if(StringUtils.isNotBlank(hasZzqq)){
            erpShopDataInputSubTaskService.updateState(taskId, "2");
            erpShopDataInputSubTaskService.updateTaskRemark(taskId, "2", hasZzqq);
        }
        
        if(isFinished){
            // 完成任务
            this.workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), taskId, procInsId, "联系老商户成功", vars);
            dwrUtils.dwr(sdiFlow.getOperationAdviser());
        }
        resObject.put("result", true);
        return resObject;
    }

    private boolean isJykGood(ErpOrderOriginalGood orderGood) {
        return null != orderGood.getGoodTypeId() && orderGood.getGoodTypeId().intValue() == 5;
    }
    
    /**
     * 选择推广门店
     *
     * @return
     * @date 2017年12月11日
     * @author SunQ
     */
    @RequestMapping(value = "select_extension_store_shop")
    @ResponseBody
    public JSONObject selectExtensionStore(String taskId, String procInsId) {
        JSONObject resObject = new JSONObject();
        
        // 获取商户资料录入流程对象
        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(procInsId);
        ErpOrderOriginalInfo orderInfo = erpOrderOriginalInfoService.get(sdiFlow.getOrderId());
        List<ErpStoreInfo> stores = erpStoreInfoService.findwhereshopidList(orderInfo.getId(), "0");
        Map<String, Object> vars = Maps.newHashMap();
        
        // 门店是否有微信资质
        vars.put("hasWXQuali", "2");
        if(!CollectionUtils.isEmpty(stores)){
            for(ErpStoreInfo store : stores){
                String state = erpStoreInfoService.getPayQualifyById(store.getId());
                if(StringUtils.isNotBlank(state)){
                    vars.put("hasWXQuali", "1");
                    break;
                }
            }
        }
        
        // 修改子任务完成状态
        workFlowService.submitShopDataInputSubTask(procInsId, "1", taskId);
        // 完成任务
        this.workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), taskId, procInsId, "选择推广门店成功", vars);
        dwrUtils.dwr(sdiFlow.getOperationAdviser());
        
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 新增商户
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2017年12月11日
     * @author Administrator
     */
    @RequestMapping(value = "add_shop_shop")
    @ResponseBody
    public JSONObject addShopShop(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        
        // 获取商户资料录入流程对象
        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(procInsId);
        ErpOrderOriginalInfo orderInfo = erpOrderOriginalInfoService.get(sdiFlow.getOrderId());
        Map<String, Object> vars = Maps.newHashMap();
        
        ErpShopInfo erpShopInfo = erpShopInfoService.getByOrderID(orderInfo.getId());
        // 判断商户是否新增成功
        if(erpShopInfo==null){
            resObject.put("result", false);
            resObject.put("message", "请先新增商户信息再继续处理该订单。");
            return resObject;
        }
        
        // 修改子任务完成状态
        workFlowService.submitShopDataInputSubTask(procInsId, channelList, taskId);
        // 完成任务
        this.workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), taskId, procInsId, "新增商户成功", vars);
        dwrUtils.dwr(sdiFlow.getOperationAdviser());
            
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 联系新商户
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2017年12月11日
     * @author Administrator
     */
    @RequestMapping(value = "conact_new_shop_shop")
    @ResponseBody
    public JSONObject conactNewShop(String taskId, String procInsId, String channelList, String hasYyzz, String hasZzqq, boolean isFinished) {
        JSONObject resObject = new JSONObject();
        
        // 获取商户资料录入流程对象
        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(procInsId);
        ErpOrderOriginalInfo orderInfo = erpOrderOriginalInfoService.get(sdiFlow.getOrderId());
        ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(orderInfo.getShopId());
        Map<String, Object> vars = Maps.newHashMap();
        
        // 是否包含聚引客订单
        boolean hasJyk = false;
        List<ErpOrderOriginalGood> orderGoods = erpOrderOriginalGoodService.findListByOrderId(sdiFlow.getOrderId());
        if(orderGoods != null){
            for(ErpOrderOriginalGood orderGood : orderGoods){
                // 聚引客订单
                if (isJykGood(orderGood)) {
                    hasJyk = true;
                    break;
                }
            }
        }
        if(hasJyk){
            vars.put("hasJykOrder", 1);
        }else{
            vars.put("hasJykOrder", 2);
        }
        
        // 是否提交掌贝进件
        vars.put("isApplyZhangbeiJJ", 2);
        if (erpShopInfo.getZhangbeiState() > 0) {
            vars.put("isApplyZhangbeiJJ", 1);
        }
        
        // 修改子任务完成状态
        workFlowService.submitShopDataInputSubTask(procInsId, channelList, taskId);
        if(StringUtils.isNotBlank(hasYyzz)){
            erpShopDataInputSubTaskService.updateState(taskId, "2");
            erpShopDataInputSubTaskService.updateTaskRemark(taskId, "2", hasYyzz);
        }
        if(StringUtils.isNotBlank(hasZzqq)){
            erpShopDataInputSubTaskService.updateState(taskId, "3");
            erpShopDataInputSubTaskService.updateTaskRemark(taskId, "3", hasZzqq);
        }
        
        if(isFinished){
            // 完成任务
            this.workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), taskId, procInsId, "联系新商户成功", vars);
            dwrUtils.dwr(sdiFlow.getOperationAdviser());
            
            // 已提交掌贝进件，触发流程向下执行
            if(erpShopInfo.getZhangbeiState() > 0){
                sdiFlowSignalService.applyZhangbeiIntopiece(erpShopInfo.getZhangbeiId());
            }
        }
            
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 确认用户提交掌贝进件
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2017年12月13日
     * @author SunQ
     */
    @RequestMapping(value = "data_apply_shop")
    @ResponseBody
    public JSONObject dataApplyShop(String taskId, String procInsId) {
        
        JSONObject resObject = new JSONObject();
        
        // 获取商户资料录入流程对象
        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(procInsId);
        ErpOrderOriginalInfo orderInfo = erpOrderOriginalInfoService.get(sdiFlow.getOrderId());
        ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(orderInfo.getShopId());
        Map<String, Object> vars = Maps.newHashMap();

        // 商户是否提交掌贝进件
        if(erpShopInfo!=null && erpShopInfo.getZhangbeiState().intValue()==0){
            resObject.put("result", false);
            resObject.put("message", "商户未提交掌贝进件，待商户掌贝进件后再执行该操作。");
            return resObject;
        }
        
        // 修改子任务完成状态
        workFlowService.submitShopDataInputSubTask(procInsId, "1", taskId);
        // 完成任务
        this.workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), taskId, procInsId, "用户提交成功", vars);
        dwrUtils.dwr(sdiFlow.getOperationAdviser());
        
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 确认掌贝进件状态
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2017年12月13日
     * @author SunQ
     */
    @RequestMapping(value = "zhangbei_state_shop")
    @ResponseBody
    public JSONObject zhangbeiStateShop(String taskId, String procInsId) {
        
        JSONObject resObject = new JSONObject();
        // 获取商户资料录入流程对象
        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(procInsId);
        ErpOrderOriginalInfo orderInfo = erpOrderOriginalInfoService.get(sdiFlow.getOrderId());
        ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(orderInfo.getShopId());
        Map<String, Object> vars = Maps.newHashMap();

        // 是否包含聚引客订单
        boolean hasJyk = false;
        List<ErpOrderOriginalGood> orderGoods = erpOrderOriginalGoodService.findListByOrderId(sdiFlow.getOrderId());
        if(orderGoods != null){
            for(ErpOrderOriginalGood orderGood : orderGoods){
                // 聚引客订单
                if (isJykGood(orderGood)) {
                    hasJyk = true;
                    break;
                }
            }
        }
        if(hasJyk){
            vars.put("hasJykOrder", 1);
        }else{
            vars.put("hasJykOrder", 2);
        }
        vars.put("isApplyWechatpay", 2);
        // 判断是否提交微信支付进件审核
        if(null!=erpShopInfo && erpShopInfoService.countApplyWechatpayByShopId(erpShopInfo.getZhangbeiId())>0){
            vars.put("isApplyWechatpay", 1);
        }
        vars.put("isApplyUnionpay", 2);
        // 判断是否提交银联支付进件审核
        if(null!=erpShopInfo && erpShopInfoService.countApplyUnionpayByShopId(erpShopInfo.getZhangbeiId())>0){
            vars.put("isApplyUnionpay", 1);
        }
        // 修改子任务完成状态
        workFlowService.submitShopDataInputSubTask(procInsId, "1", taskId);
        // 完成任务
        this.workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), taskId, procInsId, "掌贝进件状态成功", vars);
        dwrUtils.dwr(sdiFlow.getOperationAdviser());
        
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 选择推广门店(开户流程中)
     *
     * @return
     * @date 2017年12月11日
     * @author SunQ
     */
    @RequestMapping(value = "select_extension_store2_shop")
    @ResponseBody
    public JSONObject selectExtensionStore2(String taskId, String procInsId, String storeId) {
        JSONObject resObject = new JSONObject();
        
        // 获取商户资料录入流程对象
        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(procInsId);
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        workFlowService.submitShopDataInputSubTask(procInsId, "1", taskId);
        // 完成任务
        this.workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), taskId, procInsId, "选择推广门店成功", vars);
        dwrUtils.dwr(sdiFlow.getOperationAdviser());
        
        resObject.put("result", true);
        return resObject;
    }
    
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
    @RequestMapping(value = "need_wechat_pay_shop")
    @ResponseBody
    public JSONObject needWechatPayShop(String taskId, String procInsId, String channelVal) {
        
        JSONObject resObject = new JSONObject();
        // 获取商户资料录入流程对象
        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(procInsId);
        ErpOrderOriginalInfo orderInfo = erpOrderOriginalInfoService.get(sdiFlow.getOrderId());
        ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(orderInfo.getShopId());
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("isNeedWechat", 2);
        if(StringUtils.isNotBlank(channelVal) && "1".equals(channelVal)){
            vars.put("isNeedWechat", 1);
        }
        
        // 修改子任务完成状态
        workFlowService.submitShopDataInputSubTask(procInsId, "1", taskId);
        // 完成任务
        this.workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), taskId, procInsId, "商户是否需要开通微信支付成功", vars);
        // 判断是否提交微信支付进件审核
        if(null!=erpShopInfo && erpShopInfo.getWechatpayState().intValue()>0){
            sdiFlowSignalService.applyWechatPayIntopiece(erpShopInfo.getZhangbeiId());
        }
        if(StringUtils.isNotBlank(channelVal) && "1".equals(channelVal)){
            dwrUtils.dwr(sdiFlow.getOperationAdviser());
        }
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 完成微信支付进件
     *
     * @param taskId
     * @param procInsId
     * @param channelVal
     * @date 2017年12月13日
     * @author SunQ
     */
    @RequestMapping(value = "wechat_pay_state_shop")
    @ResponseBody
    public JSONObject wechatPayStateShop(String taskId, String procInsId, String channelList) {
        
        JSONObject resObject = new JSONObject();
        // 获取商户资料录入流程对象
        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(procInsId);
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        workFlowService.submitShopDataInputSubTask(procInsId, channelList, taskId);
        // 完成任务
        this.workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), taskId, procInsId, "完成微信支付进件成功", vars);
        
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
    @RequestMapping(value = "need_union_pay_shop")
    @ResponseBody
    public JSONObject needUnionPayShop(String taskId, String procInsId, String channelVal) {
        
        JSONObject resObject = new JSONObject();
        // 获取商户资料录入流程对象
        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(procInsId);
        ErpOrderOriginalInfo orderInfo = erpOrderOriginalInfoService.get(sdiFlow.getOrderId());
        ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(orderInfo.getShopId());
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("isNeedUnion", 2);
        if(StringUtils.isNotBlank(channelVal) && "1".equals(channelVal)){
            vars.put("isNeedUnion", 1);
        }
        // 修改子任务完成状态
        workFlowService.submitShopDataInputSubTask(procInsId, "1", taskId);
        // 完成任务
        this.workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), taskId, procInsId, "商户是否需要开通银联支付成功", vars);
        // 判断是否提交银联支付进件审核
        if (null != erpShopInfo && erpShopInfo.getUnionpayState().intValue() > 0) {
            sdiFlowSignalService.applyUnionPayIntopiece(erpShopInfo.getZhangbeiId());
        }
        if(StringUtils.isNotBlank(channelVal) && "1".equals(channelVal)){
            dwrUtils.dwr(sdiFlow.getOperationAdviser());
        }
        
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 完成银联支付进件
     *
     * @param taskId
     * @param procInsId
     * @param channelVal
     * @date 2017年12月13日
     * @author SunQ
     */
    @RequestMapping(value = "union_pay_state_shop")
    @ResponseBody
    public JSONObject unionPayStateShop(String taskId, String procInsId, String channelList) {
        
        JSONObject resObject = new JSONObject();
        // 获取商户资料录入流程对象
        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(procInsId);
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        workFlowService.submitShopDataInputSubTask(procInsId, channelList, taskId);
        // 完成任务
        this.workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), taskId, procInsId, "完成银联支付进件成功", vars);
        
        resObject.put("result", true);
        return resObject;
    }
}