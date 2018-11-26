package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;

/**
 * 聚引客流程<服务商服务类>
 * 
 * @author zjq
 * @date 2018年4月9日
 */
@Service
public class JykServiceProviderService extends AbstractFlowService {


    /**
     * 
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
     * @param channels 1：朋友圈 2：微博 3：陌陌
     * @return
     * @date 2018年4月10日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject contactShopServiceLatest(String taskId, String procInsId, boolean isFinished, String storeId, String storeName, String license,
                    String qualification, String extensionExpect, String nextLicenseTime, String nextQualificationTime,
                    String nextExtensionExpectTime, String promotionTime, String[] channels) {
        JSONObject resObject = new JSONObject();
        // 获取信息
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
            // 商户是否掌贝进件成功
            vars.put(ZHANGBEI_ACCOUNT_FLAG, Constant.NO);
            ErpShopInfo shopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
            if (shopInfo != null && StringUtils.isNotBlank(shopInfo.getPassword())) {
                vars.put(ZHANGBEI_ACCOUNT_FLAG, Constant.YES);
            }

            // 门店是否有微信资质
            vars.put(WECHAT_PAYM_QUAL_FLAG, Constant.NO);
            String state = erpStoreInfoService.getPayQualifyById(storeId);
            if (StringUtils.isNotBlank(state)) {
                vars.put(WECHAT_PAYM_QUAL_FLAG, Constant.YES);
            }

            if (isFinished) {
                if (distanceDays.intValue() <= 0) {
                    vars.put(IS_STARTED, STRCONSTANT_1);

                    split.setPendingProdFlag(Constant.NO);
                    taskExt.setPendingProdFlag(Constant.NO);

                    // 如果满足 小于20工作日，必须要选择门店
                    if (StringUtils.isBlank(storeId)) {
                        resObject.put(RESULT, false);
                        resObject.put(MESSAGE, "请选择需要推广的门店!");
                        return resObject;
                    }
                    // 更新门店信息
                    erpStoreInfoService.save(storeInfo);
                    // 流程结束前保存分单信息
                    this.erpOrderSplitInfoService.save(split);
                    // 完成服务商推广时候确认流程
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
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "推广时间确认", vars);

        // 确认推广门店成功
        erpHisSplitServiceApi.extensionStore(split.getId(), storeId, storeName);
        // 确认投放门店资质齐全
        erpHisSplitServiceApi.qualification(split.getId(), qualification, storeId, storeName);

        // 获取当前流程下的任务
        ProcessInstance processInstance = actTaskService.getProcIns(procInsId);
        List<Task> tasks = taskService.createTaskQuery().includeProcessVariables().processInstanceId(processInstance.getId()).list();
        for (Task task : tasks) {
            // 判断商户是否上传推广图片素材
            if (UPLOAD_PROMOTIONAL_PICTURES_SERVICE.equals(task.getTaskDefinitionKey())) {
                ErpStorePromotePhotoMaterial promotePhotoMaterial = erpStorePromotePhotoMaterialService.findlistWhereStoreId("0",
                                storeId);
                if (promotePhotoMaterial != null) {
                    jykFlowAccountSignalService.uploadPPromotionalPictures(storeId);
                }
                continue;
            }
        }
    }




}
