package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 3.5.0聚迎客流程
 * 
 * @author R/Q
 * @date 2018年10月18日
 */
@Service
public class JykFlow3P5Service extends AbstractFlowService {
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;

    private static final Logger logger = LoggerFactory.getLogger(JykFlow3P5Service.class);


    /**
     * 业务定义：商户对接/确认推广门店/资质/推广时间
     * 
     * @date 2018年10月18日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public JSONObject contactShop3P5(String taskId, String procInsId, boolean isFinished, String storeId, String storeName, String license,
                    String qualification, String extensionExpect, String nextLicenseTime, String nextQualificationTime,
                    String nextExtensionExpectTime, String promotionTime, String[] channels) {
        logger.info("【商户对接/确认推广门店/资质/推广时间】任务  processId[{}],storeId[{}],channels[{}] start ", procInsId, storeId, channels);
        JSONObject resObject = new JSONObject();
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
                    // 更新门店信息
                    erpStoreInfoService.save(storeInfo);
                    // 流程结束前保存分单信息
                    this.erpOrderSplitInfoService.save(split);
                    // 完成推广时间确认流程
                    this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "推广时间确认", vars);
                    // 确认推广门店成功
                    erpHisSplitServiceApi.extensionStore(split.getId(), storeId, storeName);
                    // 确认投放门店资质齐全
                    erpHisSplitServiceApi.qualification(split.getId(), qualification, storeId, storeName);
                    // 手动激活任务
                    this.activateTask(procInsId, split.getOrderId(), split.getId());
                }
            }
        }

        this.erpOrderSplitInfoService.save(split);
        this.taskExtService.updateTaskState(taskExt);
        resObject.put(RESULT, true);
        return resObject;
    }

    /**
     * 业务定义：激活指定开户顾问后的后续聚引客生产流程节点任务
     * 
     * @date 2018年10月18日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public void activateTask(String procInsId, String orderId, String splitOrderId) {
        List<ErpOrderFlowUser> deliveryFlowUsers = erpOrderFlowUserService.getDeliveryFlowUserByOrderId(orderId);
        logger.info("聚引客生产流程设置开户顾问+运营顾问  processId[{}],orderId[{}],splitOrderId[{}] start ", procInsId, orderId, splitOrderId);
        if (CollectionUtils.isNotEmpty(deliveryFlowUsers)) {
            List<ErpOrderFlowUser> adviserUsers = deliveryFlowUsers.stream().filter(deliveryUser -> deliveryUser.getFlowUserId()
                            .equals(JykFlowConstants.ACCOUNT_ADVISER) || deliveryUser.getFlowUserId().equals(JykFlowConstants.OPERATION_ADVISER))
                            .collect(Collectors.toList());// 过滤获取开户顾问+运营顾问
            if (CollectionUtils.isNotEmpty(adviserUsers)) {
                // 设置聚引客开户顾问+运营顾问
                List<ErpOrderSplitInfo> jykSplitOrders = Lists.newArrayList();
                if (StringUtils.isNotBlank(splitOrderId)) {// 依据分号进行判断是只同步一个还是同步订单下所属分单
                    jykSplitOrders.add(erpOrderSplitInfoService.get(splitOrderId));
                } else {
                    jykSplitOrders = erpOrderSplitInfoService.findByOrderId(orderId);
                }
                if (CollectionUtils.isNotEmpty(jykSplitOrders)) {
                    for (ErpOrderSplitInfo erpOrderSplitInfo : jykSplitOrders) {
                        Execution execution = runtimeService.createExecutionQuery().processInstanceId(erpOrderSplitInfo.getProcInsId())
                                        .activityId("to_account_adviser").singleResult();
                        if (execution != null) {// 判断是否有【待开户顾问指派】节点停留
                            ErpOrderFlowUser erpOrderFlowUser = erpOrderFlowUserService.findListByFlowId(erpOrderSplitInfo.getProcInsId(),
                                            JykFlowConstants.ACCOUNT_ADVISER);// 判断是否存在开户顾问
                            if (erpOrderFlowUser == null) {
                                for (ErpOrderFlowUser adviserUser : adviserUsers) {// 新增流程处理人员表数据
                                    erpOrderFlowUserService.insertOrderFlowUser(adviserUser.getUser().getId(), erpOrderSplitInfo.getOrderId(),
                                                    erpOrderSplitInfo.getId(), adviserUser.getFlowUserId(), erpOrderSplitInfo.getProcInsId());
                                    // 设置流程参数-开户顾问+运营顾问
                                    runtimeService.setVariable(execution.getId(), adviserUser.getFlowUserId(), adviserUser.getUser().getId());
                                }
                                // 完成等待任务节点，激活后续任务
                                runtimeService.signal(execution.getId());
                                // 判断是否上传推广素材，完成对应提示节点
                                this.changePromotionalPictures(erpOrderSplitInfo.getProcInsId());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 业务定义：推广图片上传节点完成
     * 
     * @date 2018年10月19日
     * @author R/Q
     */
    private void changePromotionalPictures(String procInsId) {
        List<Task> tasks = taskService.createTaskQuery().includeProcessVariables().processInstanceId(procInsId).list();// 获取待办
        for(Task task : tasks){
            if (task.getTaskDefinitionKey().startsWith("upload_promotional_pictures_zhixiao")) {
                String choiceStore = jykOrderChoiceStoreService.getStoreIdByProcInsId(task.getProcessInstanceId());// 获取流程对应的推广门店
                ErpStorePromotePhotoMaterial promotePhotoMaterial = erpStorePromotePhotoMaterialService.findlistWhereStoreId("0", choiceStore);// 获取门店的推广素材
                if(promotePhotoMaterial!=null){
                    this.workFlowService.completeFlow2(new String[] {JykFlowConstants.ACCOUNT_ADVISER, JykFlowConstants.Planning_Expert},
                                    task.getId(), task.getProcessInstanceId(), "商户完成推广图片上传", Maps.newHashMap());// 完成【尽快上传推广图片素材(提示)】节点
                }
                continue;
            }
        }
    }
}
