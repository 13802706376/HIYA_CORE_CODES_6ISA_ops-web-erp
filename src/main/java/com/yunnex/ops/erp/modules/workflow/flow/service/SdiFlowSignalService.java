package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;
import java.util.Map;

import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalGood;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalGoodService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpPayIntopieces;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpShopDataInput;
import com.yunnex.ops.erp.modules.shopdata.service.ErpPayIntopiecesService;
import com.yunnex.ops.erp.modules.shopdata.service.ErpShopDataInputService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.pay.dao.ErpStorePayUnionpayDao;
import com.yunnex.ops.erp.modules.store.pay.dao.ErpStorePayWeixinDao;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SdiFlow;

/**
 * 商户资料录入自动流转Service
 * 
 * @author SunQ
 * @date 2018年1月18日
 */
@Service
public class SdiFlowSignalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SdiFlowSignalService.class);
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private ErpShopDataInputService erpShopDataInputService;
    @Autowired
    private SdiFlowService sdiFlowService;
    @Autowired
    private ErpOrderOriginalGoodService erpOrderOriginalGoodService;
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpStorePayWeixinDao erpStorePayWeixinDao;
    @Autowired
    private ErpStorePayUnionpayDao erpStorePayUnionpayDao;
    @Autowired
    private ErpPayIntopiecesService erpPayIntopiecesService;
    @Autowired
    private ErpDeliveryServiceService erpDeliveryServiceService;
    /**
     * 是否提交掌贝进件
     *
     * @param shopId
     * @date 2018年1月19日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void applyZhangbeiIntopiece(String shopId) {
        
        try {
            // 1.找到该商户当前所有的正在进行的任务
            List<String> taskIds = erpShopDataInputService.findTaskId(shopId);
            if(!CollectionUtils.isEmpty(taskIds)){
                for(String taskId : taskIds){
                    Task task = actTaskService.getTask(taskId);
                    // 2.判断当前任务是否为‘data_apply_shop’
                    if("data_apply_shop".equals(task.getTaskDefinitionKey())){
                        ErpShopDataInput info = erpShopDataInputService.getByShopId(shopId);
                        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(info.getProcInsId());
                        Map<String, Object> vars = Maps.newHashMap();
                        // 修改子任务完成状态
                        workFlowService.submitShopDataInputSubTask(task.getProcessInstanceId(), "1,2,3,4,", task.getId());
                        // 完成任务
                        workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), task.getId(), task.getProcessInstanceId(), "是否提交掌贝进件", vars);
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("商户是否提交掌贝进件自动流程执行异常:" + e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 商户掌贝进件成功
     *
     * @param shopId
     * @date 2018年1月18日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void zhangbeiIntopiece(String shopId){
        
        try {
            // 1.找到该商户当前所有的正在进行的任务
            List<String> taskIds = erpShopDataInputService.findTaskId(shopId);
            if(!CollectionUtils.isEmpty(taskIds)){
                for(String taskId : taskIds){
                    Task task = actTaskService.getTask(taskId);
                    // 2.判断当前任务是否为‘zhangbei_state_shop’
                    if("zhangbei_state_shop".equals(task.getTaskDefinitionKey())){
                        ErpShopDataInput info = erpShopDataInputService.getByShopId(shopId);
                        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(info.getProcInsId());
                        ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiID(shopId);
                        Map<String, Object> vars = Maps.newHashMap();
                        // 是否包含聚引客订单
                        boolean hasJyk = false;
                        List<ErpOrderOriginalGood> orderGoods = erpOrderOriginalGoodService.findListByOrderId(info.getOrderId());
                        if(orderGoods != null){
                            for(ErpOrderOriginalGood orderGood : orderGoods){
                                //聚引客订单
                                if(orderGood.getGoodTypeId().intValue() == 5){
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
                        if(null!=shopInfo && erpShopInfoService.countApplyWechatpayByShopId(shopInfo.getZhangbeiId())>0){
                            vars.put("isApplyWechatpay", 1);
                        }
                        vars.put("isApplyUnionpay", 2);
                        // 判断是否提交银联支付进件审核
                        if(null!=shopInfo && erpShopInfoService.countApplyUnionpayByShopId(shopInfo.getZhangbeiId())>0){
                            vars.put("isApplyUnionpay", 1);
                        }
                        // 修改子任务完成状态
                        workFlowService.submitShopDataInputSubTask(task.getProcessInstanceId(), "1", task.getId());
                        // 完成任务
                        workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), task.getId(), task.getProcessInstanceId(), "掌贝进件状态成功", vars);
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("商户掌贝进件成功自动流程执行异常:" + e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    
    /**
     * 商户掌贝进件成功交付服务流程
     *
     * @param shopId
     * @date 2018年1月18日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void deliveryServiceFlowZhangbeiIntopiece(String shopId){
        try {
            // 1.找到该商户当前所有的正在进行的任务
            List<String> taskIds = erpDeliveryServiceService.findTaskIdByShopId(shopId);
            if(!CollectionUtils.isEmpty(taskIds)){
                for(String taskId : taskIds){
                    Task task = actTaskService.getTask(taskId);
                    // 2.判断当前任务是否为‘zhangbei_account_open’
                    if("zhangbei_account_open".equals(task.getTaskDefinitionKey())){
                        Map<String, Object> vars = Maps.newHashMap();
                        // 完成任务
                        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER}, taskId, task.getProcessInstanceId(),
                            "电话联系商户，确认服务内容", vars); 
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("商户掌贝进件成功自动流程执行异常:" + e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    
    /**
     * 微信支付进件成功
     *
     * @param shopId
     * @param registerNo
     * @param bankNo
     * @date 2018年1月18日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void wechatPayIntopiece(String shopId, String registerNo, String bankNo){
        
        try {
            // 获取商户信息
            ErpShopInfo shopInfo = erpShopInfoService.findListByZhangbeiId(shopId);
            // 获取所有匹配的门店微信支付信息
            List<ErpStorePayWeixin> wxpaylist = erpStorePayWeixinDao.findwxpayaudit(shopInfo.getId(), registerNo, bankNo);
            
            if(!CollectionUtils.isEmpty(wxpaylist)){
                // 1.找到该商户当前所有的正在进行的任务
                List<String> taskIds = erpPayIntopiecesService.findTaskId(shopId, "0");
                if(!CollectionUtils.isEmpty(taskIds)){
                    for(String taskId : taskIds){
                        Task task = actTaskService.getTask(taskId);
                        // 2.判断当前任务是否为‘wechat_pay_state_pay’
                        if("wechat_pay_state_pay".equals(task.getTaskDefinitionKey())){
                            ErpPayIntopieces erpPayIntopieces = erpPayIntopiecesService.getByProsIncId(task.getProcessInstanceId());
                            ErpStoreInfo erpStoreInfo = erpStoreInfoService.get(erpPayIntopieces.getStoreId());
                            
                            for(ErpStorePayWeixin wxpay : wxpaylist){
                                if(wxpay.getId().equals(erpStoreInfo.getWeixinPayId())){
                                    Map<String, Object> vars = Maps.newHashMap();
                                    // 修改子任务完成状态
                                    this.workFlowService.submitPayIntopiecesSubTask(task.getProcessInstanceId(), "1", taskId);
                                    //完成任务
                                    this.workFlowService.completePayIntopiecesFlow(erpPayIntopieces.getChargePerson(), taskId, task.getProcessInstanceId(), "完成微信支付进件成功", vars);
                                }
                            }
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("微信支付进件成功自动流程执行异常:" + e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 银联支付进件成功
     *
     * @param shopId
     * @param registerNo
     * @param bankNo
     * @date 2018年1月18日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void unionPayIntopiece(String shopId, String registerNo, String bankNo){
        
        try {
            // 获取商户信息
            ErpShopInfo shopInfo = erpShopInfoService.findListByZhangbeiId(shopId);
            // 获取所有匹配的门店银联支付信息
            List<ErpStorePayUnionpay> unionpaylist = erpStorePayUnionpayDao.findunionaudit(shopInfo.getId(), registerNo, bankNo);
            
            if(!CollectionUtils.isEmpty(unionpaylist)){
                // 1.找到该商户当前所有的正在进行的任务
                List<String> taskIds = erpPayIntopiecesService.findTaskId(shopId, "1");
                if(!CollectionUtils.isEmpty(taskIds)){
                    for(String taskId : taskIds){
                        Task task = actTaskService.getTask(taskId);
                        // 2.判断当前任务是否为‘union_pay_state_pay’
                        if("union_pay_state_pay".equals(task.getTaskDefinitionKey())){
                            ErpPayIntopieces erpPayIntopieces = erpPayIntopiecesService.getByProsIncId(task.getProcessInstanceId());
                            ErpStoreInfo erpStoreInfo = erpStoreInfoService.get(erpPayIntopieces.getStoreId());
                            
                            for(ErpStorePayUnionpay unionpay : unionpaylist){
                                if(unionpay.getId().equals(erpStoreInfo.getUnionpayId())){
                                    Map<String, Object> vars = Maps.newHashMap();
                                    // 修改子任务完成状态
                                    workFlowService.submitPayIntopiecesSubTask(task.getProcessInstanceId(), "1", taskId);
                                    //完成任务
                                    this.workFlowService.completePayIntopiecesFlow(erpPayIntopieces.getChargePerson(), taskId, task.getProcessInstanceId(), "完成银联支付进件成功", vars);
                                }
                            }
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("银联支付进件成功自动流程执行异常:" + e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 是否提交微信支付进件
     *
     * @param shopId
     * @date 2018年1月19日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void applyWechatPayIntopiece(String shopId) {
        
        try {
            // 1.找到该商户当前所有的正在进行的任务
            List<String> taskIds = erpShopDataInputService.findTaskId(shopId);
            if(!CollectionUtils.isEmpty(taskIds)){
                for(String taskId : taskIds){
                    Task task = actTaskService.getTask(taskId);
                    // 2.判断当前任务是否为‘wechat_pay_state_shop’
                    if("wechat_pay_state_shop".equals(task.getTaskDefinitionKey())){
                        ErpShopDataInput info = erpShopDataInputService.getByShopId(shopId);
                        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(info.getProcInsId());
                        Map<String, Object> vars = Maps.newHashMap();
                        // 修改子任务完成状态
                        workFlowService.submitShopDataInputSubTask(task.getProcessInstanceId(), "1,2,3,", task.getId());
                        // 完成任务
                        workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), task.getId(), task.getProcessInstanceId(), "是否提交微信支付进件", vars);
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("商户是否提交微信支付进件自动流程执行异常:" + e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 是否提交银联支付进件
     *
     * @param shopId
     * @date 2018年1月19日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void applyUnionPayIntopiece(String shopId) {
        
        try {
            // 1.找到该商户当前所有的正在进行的任务
            List<String> taskIds = erpShopDataInputService.findTaskId(shopId);
            if(!CollectionUtils.isEmpty(taskIds)){
                for(String taskId : taskIds){
                    Task task = actTaskService.getTask(taskId);
                    // 2.判断当前任务是否为‘union_pay_state_shop’
                    if("union_pay_state_shop".equals(task.getTaskDefinitionKey())){
                        ErpShopDataInput info = erpShopDataInputService.getByShopId(shopId);
                        SdiFlow sdiFlow = sdiFlowService.getByProcInstId(info.getProcInsId());
                        Map<String, Object> vars = Maps.newHashMap();
                        // 修改子任务完成状态
                        workFlowService.submitShopDataInputSubTask(task.getProcessInstanceId(), "1,2,3,", task.getId());
                        // 完成任务
                        workFlowService.completeShopDataInputFlow(sdiFlow.getOperationAdviser(), task.getId(), task.getProcessInstanceId(), "是否提交银联支付进件", vars);
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("商户是否提交银联支付进件自动流程执行异常:" + e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
}