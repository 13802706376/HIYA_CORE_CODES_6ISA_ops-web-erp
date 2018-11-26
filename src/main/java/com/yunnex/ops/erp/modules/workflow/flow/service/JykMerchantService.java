package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserFriendsService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserMomoService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderOperateValue;
import com.yunnex.ops.erp.modules.workflow.flow.entity.JykFlow;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 聚引客流程<直销商户服务类>
 * 
 * @author zjq
 * @date 2018年4月9日
 */
@Service
public class JykMerchantService extends AbstractFlowService {

    private static final Logger logger = LoggerFactory.getLogger(JykMerchantService.class);

    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private ErpStoreAdvertiserFriendsService erpStoreAdvertiserFriendsService;
    @Autowired
    private ErpStoreAdvertiserMomoService erpStoreAdvertiserMomoService;
    
    /**
     * 直销-商户对接/确认推广门店/资质/推广时间
     *
     * @param taskId
     * @param procInsId
     * @param isFinished
     * @param storeId
     * @param storeName
     * @param license
     * @param qualification
     * @param extensionExpect
     * @param nextLicenseTime
     * @param nextQualificationTime
     * @param nextExtensionExpectTime
     * @param promotionTime
     * @param channels
     * @param resObject
     * @return
     * @date 2018年4月9日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject contactShopZhixiaoLatest(String taskId, String procInsId, boolean isFinished, String storeId, String storeName, String license,
                    String qualification, String extensionExpect, String nextLicenseTime, String nextQualificationTime,
                    String nextExtensionExpectTime, String promotionTime, String[] channels) {
        JSONObject resObject =new JSONObject();
        // 获取分单信息
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        String pendingProdFlag = split.getPendingProdFlag();
        TaskExt taskExt = new TaskExt();
        taskExt.setTaskId(taskId);
        taskExt.setPendingProdFlag(Constant.NO);
        split.setPendingProdFlag(Constant.NO);

        // 保存门店 和推广渠道
        ErpStoreInfo storeInfo = saveStroeAndChannel(taskId, procInsId, storeId, channels, split);

        // 判断选择的值
        if (StringUtils.isNotBlank(license) && STRCONSTANT_2.equals(license)) {
            qualification = StringUtils.EMPTY;
            extensionExpect = StringUtils.EMPTY;
        }
        if (StringUtils.isNotBlank(qualification) && STRCONSTANT_2.equals(qualification)) {
            extensionExpect = StringUtils.EMPTY;
        }

        // 默认自动移出待生产库
        this.workFlowService.setVariable(taskId, IS_AUTO_OUT_PENDING, BOOLEAN_TRUE);
        // 推广时间
        Double distanceDays = confirmPromotionTime(taskId, extensionExpect, promotionTime, split, taskExt);

        // 下次确定营业执照时间
        validateNextLicenseTime(taskId, license, nextLicenseTime, split, taskExt);

        // 下次确定资质齐全时间
        validateNextQualificationTime(taskId, storeId, qualification, nextQualificationTime, split, taskExt);

        // 下次确定投放上线预期时间
        validateNextExtensionExpectTime(taskId, extensionExpect, nextExtensionExpectTime, split, taskExt);

        // 手动激活待生产库订单，但任务未结束（正处于等生产库但手动移出了）
        setPendingProdFlagNo(taskId, split, pendingProdFlag, taskExt);

        // 确认推广
        if (StringUtils.isNotBlank(extensionExpect) && STRCONSTANT_1.equals(extensionExpect)) {
            Map<String, Object> vars = Maps.newHashMap();

            if (isFinished) {
                // 推广时间
                if (distanceDays.intValue() <= INTEGER_0) {
                    vars.put(IS_STARTED, STRCONSTANT_1);
                    split.setPendingProdFlag(Constant.NO);
                    taskExt.setPendingProdFlag(Constant.NO);

                    // 如果满足 小于20工作日，必须要选择门店
                    if (StringUtils.isBlank(storeId)) {
                        resObject.put(RESULT, false);
                        resObject.put(MESSAGE, "请选择需要推广的门店!");
                        return resObject;
                    }

                    // 获取运营顾问
                    Map<String, Object> map = taskService.getVariables(taskId);
                    if (Constant.NO.equalsIgnoreCase(String.valueOf(map.get(DISTR_CONSULTANT_FLAG))))
                    {
                        // 指定运营顾问
                        ErpOrderOriginalInfo order = erpOrderOriginalInfoService.get(split.getOrderId());

                        ErpShopInfo erpShopInfo = erpShopInfoService.getByZhangbeiID(order.getShopId());

                        if (null != erpShopInfo && StringUtils.isNotBlank(erpShopInfo.getOperationAdviserId())) {
                            erpOrderFlowUserService.insertOrderFlowUser(erpShopInfo.getOperationAdviserId(), order.getId(), split.getId(),
                                            JykFlowConstants.OPERATION_ADVISER,
                                            procInsId);
                            this.workFlowService.setVariable(taskId, DISTR_CONSULTANT_FLAG, Constant.YES);
                            this.workFlowService.setVariable(taskId, JykFlowConstants.OPERATION_ADVISER, erpShopInfo.getOperationAdviserId());
                        } else {
                            resObject.put(RESULT, false);
                            resObject.put(MESSAGE, "请指定运营顾问!");
                            return resObject;
                        }
                    }
                    
                    // 更新门店信息
                    erpStoreInfoService.save(storeInfo);
                    // 确认门店和推广通道, 激活等待任务
                    activateTask(taskId, procInsId, storeId, channels);
                    // 流程结束前保存分单信息
                    this.erpOrderSplitInfoService.save(split);
                    // 完成推广时间确认流程
                    comPromotionTimeAffirmProcess(taskId, procInsId, storeId, storeName, qualification, split, vars);
                }
            }
        }

        this.erpOrderSplitInfoService.save(split);
        this.taskExtService.updateTaskState(taskExt);
        resObject.put(RESULT, true);
        return resObject;
    }

    private void comPromotionTimeAffirmProcess(String taskId, String procInsId, String storeId, String storeName, String qualification,
                    ErpOrderSplitInfo split, Map<String, Object> vars) {
        // 完成当前任务
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, taskId,
                        procInsId, "推广时间确认", vars);

        // 确认推广门店成功
        erpHisSplitServiceApi.extensionStore(split.getId(), storeId, storeName);
        // 确认投放门店资质齐全
        erpHisSplitServiceApi.qualification(split.getId(), qualification, storeId, storeName);

        // 获取当前流程下的任务
        ProcessInstance processInstance = actTaskService.getProcIns(procInsId);
        List<Task> tasks = taskService.createTaskQuery().includeProcessVariables().processInstanceId(processInstance.getId()).list();
        for(Task task : tasks) {
            // 判断商户是否上传推广图片素材
            if (UPLOAD_PROMOTIONAL_PICTURES_ZHIXIAO.equals(task.getTaskDefinitionKey())) {
                ErpStorePromotePhotoMaterial promotePhotoMaterial = erpStorePromotePhotoMaterialService.findlistWhereStoreId("0",
                                storeId);
                if (promotePhotoMaterial != null) {
                    jykFlowAccountSignalService.uploadPPromotionalPictures(storeId);
                }
                continue;
            }

            // 判断当前商户是否完成掌贝进件
            if (ZHANGBEI_IN_SUCESS_ZHIXIAO.equals(task.getTaskDefinitionKey())) {
                ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiID(split.getShopId());
                if (shopInfo != null && 2 == shopInfo.getZhangbeiState().intValue()) {
                    jykFlowAccountSignalService.zhangbeiIntopiece(split.getShopId());
                }
                continue;
            }
        }
    }

    /**
     * 确认门店和推广通道，手动激活任务
     *
     * @param procInsId
     * @param storeId
     * @param channels
     * @date 2018年4月11日
     * @author zjq
     */
    private void activateTask(String taskId, String procInsId, String storeId, String[] channels) {

        if (StringUtils.isNotEmpty(storeId) && isNotEmptyArray(channels)) {

            logger.info("流程等待任务  processId[{}],storeId[{}],channels[{}] start ", procInsId, storeId, channels);

            Execution execution = runtimeService.createExecutionQuery().processInstanceId(procInsId).activityId(TO_ACTIVATE_ACCOUNT_ZHIXIAO)
                            .singleResult();
            runtimeService.signal(execution.getId());

            logger.info("流程等待任务  processId[{}],storeId[{}],channels[{}],execution[{}] end ", procInsId, storeId, channels, execution);
        }

    }

    /**
     * 
     * 直销商户微博推广开户资料复审
     * 
     * @param taskId
     * @param procInsId
     * @param isPass
     * @param reason
     * @param openOrTrans
     * @return
     * @date 2018年4月10日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject microblogPromoteInfoReviewZhixiaoLatest(String taskId, String procInsId, String isPass, String reason, String openOrTrans) {
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();

        logger.info("微博推广开户资料复审==start,taskId{},procInsId{},openOrTrans{},isPass{},reason{}", taskId, procInsId, openOrTrans, isPass, reason);


        JykFlow jykFlow = flowService.getByProcInstId(procInsId);
        vars.put(REVIEW_RESULT_WEIBO, Constant.NO);
        if (STRCONSTANT_1.equals(isPass)) {
            vars.put(REVIEW_RESULT_WEIBO, Constant.YES);
        }
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, STRCONSTANT_1, taskId);
        // 保存操作内容
        ErpOrderOperateValue erpOrderOperateValue = this.erpOrderOperateValueService.getOnlyOne(procInsId,
                        JykFlowConstants.MICROBLOG_PROMOTE_INFO_REVIEW_ZHIXIAO_LATEST,
                        STRCONSTANT_1);
        if (erpOrderOperateValue != null) {
            erpOrderOperateValue.setValue(reason);
        } else {
            erpOrderOperateValue = new ErpOrderOperateValue();
            erpOrderOperateValue.setOrderId(jykFlow.getOrderId());
            erpOrderOperateValue.setSplitId(jykFlow.getSplitId());
            erpOrderOperateValue.setProcInsId(procInsId);
            erpOrderOperateValue.setSubTaskId(STRCONSTANT_1);
            erpOrderOperateValue.setKeyName(JykFlowConstants.MICROBLOG_PROMOTE_INFO_REVIEW_ZHIXIAO_LATEST);
            erpOrderOperateValue.setValue(reason);
        }

        // 保存新开户/转换信息
        if (StringUtils.isNotEmpty(openOrTrans)) {
            String storeId = jykOrderChoiceStoreService.getStoreIdByProcInsId(procInsId);
            if (StringUtils.isNotEmpty(storeId)) {
                String weiboId = erpStoreInfoService.get(storeId).getAdvertiserWeiboId();
                erpStoreAdvertiserWeiboService.updateOpenOrTrans(weiboId, openOrTrans);
            }
        }

        this.erpOrderOperateValueService.save(erpOrderOperateValue);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryWeibo, JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId,
                        "完成微博推广开户资料复审", vars);

        // 保存微博审核状态
        vars.put("reviewResultWeibo2", STRCONSTANT_1.equals(isPass) ? "Y" : "N");
        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(jykFlow.getSplitId());
        ErpStoreAdvertiserWeibo erpStoreAdvertiserWeibo = erpStoreAdvertiserWeiboService.getByStoreId(storeId);
        if (erpStoreAdvertiserWeibo != null) {
            erpStoreAdvertiserWeibo.setAuditStatus(STRCONSTANT_1.equals(isPass) ? AUDIT_STATUS_PASSED : AUDIT_STATUS_REJECT);
            erpStoreAdvertiserWeiboService.save(erpStoreAdvertiserWeibo);
        }
        if (STRCONSTANT_1.equals(isPass)) {
            // 激活等待任务
            isActivateNotifyShopFlow(jykFlow.getSplitId(), procInsId);
        }
        // 审核通过并且完成开户
        if (STRCONSTANT_1.equals(isPass) && jykFlowJudgeService.isFinishOpenAccountZhixiao(procInsId)) {
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            erpHisSplitServiceApi.openAccount(jykFlow.getSplitId(), "", sdf.format(new Date(System.currentTimeMillis())), storeInfo.getId(),
                            storeInfo.getShortName());
        }

        resObject.put(RESULT, true);

        logger.info("微博推广开户资料复审==end,taskId{},procInsId{},openOrTrans{},isPass{},reason{}", taskId, procInsId, openOrTrans, isPass, reason);

        return resObject;
    }

    @Transactional(readOnly = false)
    public JSONObject perfectMicroblogPromoteInfoZhixiaoLatest(String taskId, String procInsId, String openOrTrans) {

        logger.info("录入微博账号信息==start,taskId{},procInsId{},openOrTrans{}", taskId, procInsId, openOrTrans);

        JSONObject resObject = new JSONObject();


        String storeId = jykOrderChoiceStoreService.getStoreIdByProcInsId(procInsId);

        // 保存新开户/转换信息
        if (StringUtils.isNotEmpty(openOrTrans) && StringUtils.isNotEmpty(storeId)) {

            String weiboId = erpStoreInfoService.get(storeId).getAdvertiserWeiboId();
            ErpStoreAdvertiserWeibo advertiserWeibo = erpStoreAdvertiserWeiboService.get(weiboId);
            if (null == advertiserWeibo || StringUtils.isEmpty(advertiserWeibo.getAccountNo()) || StringUtils
                            .isEmpty(advertiserWeibo.getAccountPassword())) {
                resObject.put(MESSAGE, "请输入微博账号信息!");
                resObject.put(RESULT, false);
                return resObject;
            }
            erpStoreAdvertiserWeiboService.updateOpenOrTrans(weiboId, openOrTrans);

        }

        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryWeibo}, taskId, procInsId, "确认完成录入微博账号信息", vars);


        logger.info("录入微博账号信息==end,taskId{},procInsId{},openOrTrans{}", taskId, procInsId, openOrTrans);

        resObject.put(RESULT, true);
        return resObject;
    }
 
    public void isActivateNotifyShopFlow(String splitId,String procInsId){
        List<Integer> channels =jykOrderPromotionChannelService.getChannels(splitId);
        if(!CollectionUtils.isEmpty(channels)){
        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(splitId);  
        boolean auditStaus=true;   
        for(Integer channel:channels){
            if (Constant.CHANNEL_1.equals(channel.toString())) {
                  ErpStoreAdvertiserFriends erpStoreAdvertiserFriends=  erpStoreAdvertiserFriendsService.getByStoreId(storeId);
                  if(null==erpStoreAdvertiserFriends||erpStoreAdvertiserFriends.getAuditStatus()!=AUDIT_STATUS_PASSED){
                      auditStaus=false;
                      break;
                 }
            }
             if (Constant.CHANNEL_2.equals(channel.toString())) {
                    ErpStoreAdvertiserWeibo erpStoreAdvertiserWeibo = erpStoreAdvertiserWeiboService.getByStoreId(storeId);
                   if(erpStoreAdvertiserWeibo==null||erpStoreAdvertiserWeibo.getAuditStatus()!=AUDIT_STATUS_PASSED){
                       auditStaus=false;
                       break;
                   }
                }
             if (Constant.CHANNEL_3.equals(channel.toString())) {
                    ErpStoreAdvertiserMomo ErpStoreAdvertiserMomo=    erpStoreAdvertiserMomoService.getByStoreId(storeId); 
                  if(ErpStoreAdvertiserMomo==null||ErpStoreAdvertiserMomo.getAuditStatus()!=AUDIT_STATUS_PASSED){
                      auditStaus=false;
                      break;
                  }
                }  
            }
        if(auditStaus){
                // 激活等待任务
            Execution execution = runtimeService.createExecutionQuery().processInstanceId(procInsId).activityId(WAITING_ADVERTISER_NOTIFY_SHOP)
                            .singleResult();
                if (null != execution) {
                    runtimeService.signal(execution.getId());
                } else {
                    logger.info("runtimeService.createExecutionQuery().processInstanceId(procInsId).activityId(WAITING_ADVERTISER_NOTIFY_SHOP).singleResult(), is null");
                }
        }
      }
    }  
    
}
