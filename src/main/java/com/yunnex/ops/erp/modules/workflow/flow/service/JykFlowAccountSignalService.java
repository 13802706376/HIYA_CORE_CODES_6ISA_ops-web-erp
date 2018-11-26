package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStorePromotePhotoMaterialService;
import com.yunnex.ops.erp.modules.store.pay.dao.ErpStorePayWeixinDao;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 聚引客开户自动流转Service
 * 
 * @author SunQ
 * @date 2018年1月9日
 */
@Service
public class JykFlowAccountSignalService {
    
    private static final Logger LOGGER  = LoggerFactory.getLogger(JykFlowAccountSignalService.class);
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private JykOrderChoiceStoreService jykOrderChoiceStoreService;
    @Autowired
    private ErpStorePayWeixinDao erpStorePayWeixinDao;
    @Autowired
    private ErpStorePromotePhotoMaterialService erpStorePromotePhotoMaterialService;
    
    /**
     * 商户掌贝进件成功
     *
     * @param shopId
     * @date 2018年1月10日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void zhangbeiIntopiece(String shopId){
        
        try {
            // 1.找到该商户当前所有的正在进行的任务
            List<String> taskIds = erpShopInfoService.findShopTaskId(shopId);
            if(!CollectionUtils.isEmpty(taskIds)){
                for(String taskId : taskIds){
                    Task task = actTaskService.getTask(taskId);
                    // 2.判断当前任务是否为‘zhangbei_in_sucess_zhixiao’ 或者 ‘urge_zhangbei_enter_service’
                    if("zhangbei_in_sucess_zhixiao".equals(task.getTaskDefinitionKey()) ||
                                    "urge_zhangbei_enter_service".equals(task.getTaskDefinitionKey())){
                        // 3.设置流程变量，使流程继续执行
                        Map<String, Object> vars = Maps.newHashMap();
                        // 4.完成任务
                        this.workFlowService.completeFlow2(new String[]{JykFlowConstants.Planning_Expert}, taskId, task.getProcessInstanceId(), "掌贝进件成功", vars);
                        LOGGER.info("商户掌贝进件状态任务{}成功向下流转", taskId);
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("商户掌贝进件成功自动流程执行异常: {}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 微信支付开通成功(OEM审核通过调用)
     *
     * @param shopId
     * @param registerNo
     * @param bankNo
     * @date 2018年1月10日
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
                // 获取相关的流程id
                List<String> ids = erpOrderSplitInfoService.findProcIdListByShopId(shopId);
                if(!CollectionUtils.isEmpty(ids)){
                    for(String id : ids){
                        String storeId = jykOrderChoiceStoreService.getStoreIdByProcInsId(id);
                        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
                        if(storeInfo!=null){
                            for(ErpStorePayWeixin weixin : wxpaylist){
                                // 是否为同一个门店
                                if(storeInfo.getWeixinPayId().equals(weixin.getId())){
                                    ProcessEngine processEngine = actTaskService.getProcessEngine();
                                    // 查询是否有一个执行对象在描述‘weixin_in_sucess_zhixiao’
                                    Execution execution = processEngine
                                                    .getRuntimeService()
                                                    .createExecutionQuery()
                                                    .processInstanceId(id)
                                                    .activityId("weixin_in_sucess_zhixiao")
                                                    .singleResult();
                                    
                                    if(execution!=null){
                                        // 执行逻辑，并设置流程变量
                                        Map<String,Object> vars = new HashMap<String, Object>();
                                        TaskQuery taskQuery = taskService.createTaskQuery().executionId(execution.getId());
                                        Task task = taskQuery.singleResult();  
                                        if(task!=null){
                                            vars.put("wechatPaySuccess", "1");
                                            this.workFlowService.setVariable(task.getId(), "wechatPaySuccess", "1");
                                            this.workFlowService.completeFlow2(new String[]{JykFlowConstants.Planning_Expert}, task.getId(), task.getProcessInstanceId(), "微信支付开通成功", vars);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("商户微信支付开通成功自动流程执行异常: {}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 微信支付开通成功(流程判断已经成功调用)
     *
     * @param shopId
     * @param registerNo
     * @param bankNo
     * @date 2018年1月10日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void wechatPayIntopiece2(String storeId){
        
        try {
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            ErpShopInfo shopInfo = erpShopInfoService.get(storeInfo.getShopInfoId());
            // 1.找到该商户当前所有的正在进行的任务
            List<String> taskIds = erpShopInfoService.findShopTaskId(shopInfo.getZhangbeiId());
            for(String taskId : taskIds){
                Task task = actTaskService.getTask(taskId);
                if(task!=null){
                    // 获取流程选中的门店
                    String choiceStore = jykOrderChoiceStoreService.getStoreIdByProcInsId(task.getProcessInstanceId());
                    // 判断选择的门店是否为当前门店
                    // 2.判断当前任务是否为‘weixin_in_sucess_zhixiao’
                    boolean isRight = 
                                    "weixin_in_sucess_zhixiao".equals(task.getTaskDefinitionKey()) || 
                                        "urge_wechat_enter_service".equals(task.getTaskDefinitionKey());
                    if(StringUtils.isNotBlank(choiceStore) && choiceStore.equals(storeId) && isRight){
                        // 3.设置流程变量，使流程继续执行
                        Map<String, Object> vars = Maps.newHashMap();
                        vars.put("wechatPaySuccess", "1");
                        this.workFlowService.setVariable(taskId, "wechatPaySuccess", "1");
                        // 4.完成任务
                        this.workFlowService.completeFlow2(new String[]{JykFlowConstants.Planning_Expert}, taskId, task.getProcessInstanceId(), "微信支付开通成功", vars);
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("商户微信支付开通成功自动流程执行异常: {}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 运营经理指派订单给运营顾问(资料录入流程指派调用)
     *
     * @param shopId
     * @param operationAdviser
     * @date 2018年1月9日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void assignConsultant(String shopId, String operationAdviser) {
        
        try {
            // 1.找到该商户当前所有的正在进行的任务
            List<String> taskIds = erpShopInfoService.findShopTaskId(shopId);
            if(!CollectionUtils.isEmpty(taskIds)){
                for(String taskId : taskIds){
                    Task task = actTaskService.getTask(taskId);
                    // 2.判断当前任务是否为‘联系运营经理指派订单给运营顾问’
                    if("contact_manager_assign_consultant".equals(task.getTaskDefinitionKey())){
                        // 3.设置流程变量，使流程继续执行
                        Map<String, Object> vars = Maps.newHashMap();
                        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(task.getProcessInstanceId());
                        vars.put("OperationAdviser", operationAdviser);
                        erpOrderFlowUserService.insertOrderFlowUser(operationAdviser, split.getOrderId(), split.getId(), JykFlowConstants.OPERATION_ADVISER, task.getProcessInstanceId());
                        // 4.完成任务
                        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, task.getProcessInstanceId(), "指派运营顾问", vars);
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("运营经理指派订单给运营顾问自动流程执行异常: {}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 商户完成推广图片上传
     *
     * @param shopId
     * @date 2018年1月9日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void uploadPPromotionalPictures(String storeId) {
        
        try {
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            ErpShopInfo shopInfo = erpShopInfoService.get(storeInfo.getShopInfoId());
            // 1.找到该商户当前所有的正在进行的任务
            List<String> taskIds = erpShopInfoService.findShopTaskId(shopInfo.getZhangbeiId());
            for(String taskId : taskIds){
                Task task = actTaskService.getTask(taskId);
                if(task!=null){
                    // 获取流程选中的门店
                    String choiceStore = jykOrderChoiceStoreService.getStoreIdByProcInsId(task.getProcessInstanceId());
                    // 判断选择的门店是否为当前门店
                    if(StringUtils.isNotBlank(choiceStore) && choiceStore.equals(storeId)){
                        // 判断是否上传过推广素材
                        ErpStorePromotePhotoMaterial promotePhotoMaterial = erpStorePromotePhotoMaterialService.findlistWhereStoreId("0", storeId);
                        if(promotePhotoMaterial!=null){
                            this.workFlowService.setVariable(taskId, "UploadPictureMaterial", promotePhotoMaterial.getStoreInfoId());
                        }
                        
                        // 2.判断当前任务是否为‘upload_promotional_pictures_zhixiao’或者 ‘upload_promotional_pictures_service’
                        if("upload_promotional_pictures_zhixiao".equals(task.getTaskDefinitionKey()) || 
                                        "upload_promotional_pictures_service".equals(task.getTaskDefinitionKey())){
                            // 3.设置流程变量，使流程继续执行
                            Map<String, Object> vars = Maps.newHashMap();
                            // 4.完成任务
                            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, taskId, task.getProcessInstanceId(), "商户完成推广图片上传", vars);
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("商户完成推广图片上传自动流程执行异常: {}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 完善朋友圈推广开户资料
     *
     * @param shopId
     * @date 2018年1月9日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void perfectFriendsPromote(String storeId) {
        
        try {
            //ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            // 1.找到该门店当前所有的正在进行的任务
            List<String> taskIds = erpStoreInfoService.findTaskIdByStoreId(storeId);
            for(String taskId : taskIds){
                Task task = actTaskService.getTask(taskId);
                if(task!=null){
                    // 获取流程选中的门店
                    //String choiceStore = jykOrderChoiceStoreService.getStoreIdByProcInsId(task.getProcessInstanceId());
                    // 
                    /*
                     * 1.判断选择的门店是否为当前门店
                     * 2.判断当前任务是否为
                     *  ‘perfect_friends_promote_info_zhixiao’(完善朋友圈推广开户资料(提示)) 
                     * 'modify_friends_promote_info' (修改朋友圈推广开户资料) 聚引客3.0大流程里面的任务节点
                     * 'modify_friends_promote_info_zhixiao'(修改朋友圈推广开户资料) 朋友圈推广提审流程里面的节点
                     */
                    if (task.getTaskDefinitionKey().startsWith("perfect_friends_promote_info_zhixiao")
                    		|| task.getTaskDefinitionKey().startsWith("modify_friends_promote_info")
                    		|| task.getTaskDefinitionKey().startsWith("modify_friends_promote_info_zhixiao")) {
                        // 3.设置流程变量，使流程继续执行
                        Map<String, Object> vars = Maps.newHashMap();
                        // 4.完成任务
                        this.workFlowService.completeFlow2(
                            new String[] {JykFlowConstants.pipeIndustryFriends, JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, 
                                taskId, task.getProcessInstanceId(), "完善朋友圈推广开户资料", vars);
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("商户完善朋友圈推广开户资料自动流程执行异常: {}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 完善微博推广开户资料
     *
     * @param shopId
     * @date 2018年1月9日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void perfectMicroblogPromote(String storeId) {
        
        try {
            //ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            //ErpShopInfo shopInfo = erpShopInfoService.get(storeInfo.getShopInfoId());
            // 1.找到该门店当前所有的正在进行的任务
            List<String> taskIds = erpStoreInfoService.findTaskIdByStoreId(storeId);
            for(String taskId : taskIds){
                Task task = actTaskService.getTask(taskId);
                if(task!=null){
                    // 获取流程选中的门店
                    //String choiceStore = jykOrderChoiceStoreService.getStoreIdByProcInsId(task.getProcessInstanceId());
                    /* 1.判断选择的门店是否为当前门店  
                     * 2.判断当前任务是否为‘perfect_microblog_promote_info_zhixiao’ 
                     * modify_microblog_promote_info_zhixiao 大流程节点
                     * modify_microblog_promote_info 单独流程节点
                     */
                    if ( task.getTaskDefinitionKey().startsWith("perfect_microblog_promote_info_zhixiao")
                    			||task.getTaskDefinitionKey().startsWith("modify_microblog_promote_info_zhixiao")
                    			||task.getTaskDefinitionKey().startsWith("modify_microblog_promote_info")) {
                        // 3.设置流程变量，使流程继续执行
                        Map<String, Object> vars = Maps.newHashMap();
                        // 4.完成任务
                        this.workFlowService.completeFlow2(
                            new String[] {JykFlowConstants.pipeIndustryWeibo, JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, 
                                taskId, task.getProcessInstanceId(), "完善微博推广开户资料", vars);
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("商户完善微博推广开户资料自动流程执行异常: {}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 完善陌陌推广开户资料
     *
     * @param shopId
     * @date 2018年1月9日
     * @author SunQ
     */
    @Transactional(readOnly=false)
    public void perfectMomoPromote(String storeId) {
        
        try {
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            ErpShopInfo shopInfo = erpShopInfoService.get(storeInfo.getShopInfoId());
            // 1.找到该商户当前所有的正在进行的任务
            List<String> taskIds = erpShopInfoService.findShopTaskId(shopInfo.getZhangbeiId());
            for(String taskId : taskIds){
                Task task = actTaskService.getTask(taskId);
                if(task!=null){
                    // 获取流程选中的门店
                    String choiceStore = jykOrderChoiceStoreService.getStoreIdByProcInsId(task.getProcessInstanceId());
                    // 判断选择的门店是否为当前门店
                    // 2.判断当前任务是否为‘perfect_momo_promote_info_zhixiao’
                    if (StringUtils.isNotBlank(choiceStore) && choiceStore.equals(storeId) && task.getTaskDefinitionKey()
                                    .startsWith("perfect_momo_promote_info_zhixiao")) {
                        // 3.设置流程变量，使流程继续执行
                        Map<String, Object> vars = Maps.newHashMap();
                        // 4.完成任务
                        this.workFlowService.completeFlow2(
                            new String[] {JykFlowConstants.pipeIndustryMomo, JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, 
                                taskId, task.getProcessInstanceId(), "完善陌陌推广开户资料", vars);
                    }
                }
            }
        } catch (RuntimeException e) {
            LOGGER.info("商户完善陌陌推广开户资料自动流程执行异常: {}", e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
    }



    /**
     * 验证 完善陌陌推广开户资料(提示)是否存在
     * 
     * @param storeId
     * @return
     */
    public boolean validateMomoPromoteTaskExists(String storeId) {
        return validatePromoteTaskExists(storeId, "perfect_momo_promote_info_zhixiao");
    }

    /**
     * 验证 完善朋友圈推广开户资料并提交(提示) 是否存在
     * 
     * @param storeId
     * @return
     */
    public boolean validateFriendsPromoteTaskExists(String storeId) {
        return validatePromoteTaskExists(storeId, "perfect_friends_promote_info_zhixiao");
    }

    /**
     * 验证 完善微博推广开户资料并提交(提示)/录入微博账号信息 是否存在
     * 
     * @param storeId
     * @return
     */
    public boolean validateMicroblogPromoteTaskExists(String storeId) {
        return validatePromoteTaskExists(storeId, "perfect_microblog_promote_info_zhixiao");
    }

    private boolean validatePromoteTaskExists(String storeId, String actId) {

        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        ErpShopInfo shopInfo = erpShopInfoService.get(storeInfo.getShopInfoId());
        // 1.找到该商户当前所有的正在进行的任务
        List<String> taskIds = erpShopInfoService.findShopTaskId(shopInfo.getZhangbeiId());
        for (String taskId : taskIds) {
            Task task = actTaskService.getTask(taskId);
            if (task != null) {
                // 获取流程选中的门店
                String choiceStore = jykOrderChoiceStoreService.getStoreIdByProcInsId(task.getProcessInstanceId());
                // 判断选择的门店是否为当前门店
                if (StringUtils.isNotBlank(choiceStore) && choiceStore.equals(storeId) && task.getTaskDefinitionKey().startsWith(actId)) {
                    return true;
                }
            }
        }

        return false;

    }


}