package com.yunnex.ops.erp.modules.workflow.flow.web.jyk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.act.service.TaskExtService;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisFormValidateService;
import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.message.constant.ServiceProgressTemplateConstants;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.qualify.entity.ErpShopExtensionQualify;
import com.yunnex.ops.erp.modules.qualify.service.ErpShopExtensionQualifyService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserFriendsService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserMomoService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStorePromotePhotoMaterialService;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayWeixinService;
import com.yunnex.ops.erp.modules.workflow.channel.service.JykOrderPromotionChannelService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderOperateValue;
import com.yunnex.ops.erp.modules.workflow.flow.entity.JykFlow;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpHisSplitServiceApi;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderFileService;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpOrderOperateValueService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowAccountSignalService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowContactShopService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowJudgeService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykMerchantService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;
import com.yunnex.ops.erp.modules.workflow.store.entity.JykOrderChoiceStore;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;

/**
 * 聚引客 直销开户相关 Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/jyk/flow/accountZhixiao")
public class JykFlowAccountZhixiaoController extends BaseController {

    private static final int AUDIT_STATUS_PASSED = 4;// 通过
    private static final int AUDIT_STATUS_REJECT = 3;// 驳回

    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private JykFlowContactShopService jykFlowContactShopService;
    @Autowired
    private JykFlowService flowService;
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    private ErpShopInfoService erpShopInfoService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private JykOrderChoiceStoreService jykOrderChoiceStoreService;
    @Autowired
    private JykFlowAccountSignalService jykFlowAccountSignalService;
    @Autowired
    private ErpStorePayWeixinService erpStorePayWeixinService;
    @Autowired
    private TaskExtService taskExtService;
    @Autowired
    private ErpStoreAdvertiserFriendsService erpStoreAdvertiserFriendsService;
    @Autowired
    private ErpStoreAdvertiserMomoService erpStoreAdvertiserMomoService;
    @Autowired
    private ErpStoreAdvertiserWeiboService erpStoreAdvertiserWeiboService;
    @Autowired
    private ErpStorePromotePhotoMaterialService erpStorePromotePhotoMaterialService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;

    @Autowired
    private ErpOrderOperateValueService erpOrderOperateValueService;

    @Autowired
    private ErpHisSplitServiceApi erpHisSplitServiceApi;
    
    @Autowired
    private JykFlowJudgeService jykFlowJudgeService;
    
    @Autowired
    private ErpShopExtensionQualifyService erpShopExtensionQualifyService;
    @Autowired
    private ErpHolidaysService erpHolidaysService;
    @Autowired
    private DiagnosisFormValidateService formValidateService;
    @Autowired
    private JykOrderPromotionChannelService jykOrderPromotionChannelService;
    
    @Autowired
    private JykMerchantService jykMerchantService;
    @Autowired
    private ErpOrderFileService erpOrderFileService;
    @Autowired
    private ErpServiceMessageService erpServiceMessageService;
    @Autowired
    private ErpStoreAdvertiserFriendsService advertiserFriendsService;
    /**
     * 联系运营经理指派订单给运营顾问
     *
     * @return
     * @date 2018年1月5日
     * @author SunQ
     */
    @RequestMapping(value = "contact_manager_assign_consultant")
    @ResponseBody
    public JSONObject contactManagerAssignConsultant(String taskId, String procInsId) {
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "推广时间确认", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 直销-商户对接/确认推广门店/资质/推广时间
     *
     * @param taskId
     * @param procInsId
     * @param storeId
     * @param storeName
     * @param nextLicenseTime 下次确定营业执照时间
     * @param nextQualificationTime 下次确定资质齐全时间
     * @param nextExtensionExpectTime 下次确定投放上线预期时间
     * @param promotionTime 预期投放时间
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "contact_shop_zhixiao")
    @ResponseBody
    public JSONObject contactShopZhixiao(String taskId, String procInsId, 
                                         boolean isFinished, String storeId, String storeName,
                                         String license, String qualification, String extensionExpect,
                                         String nextLicenseTime, String nextQualificationTime, String nextExtensionExpectTime, String promotionTime) {
        JSONObject resObject = new JSONObject();
        // 获取信息
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        String pendingProdFlag = split.getPendingProdFlag();
        TaskExt taskExt = new TaskExt();
        taskExt.setTaskId(taskId);
        taskExt.setPendingProdFlag(Constant.NO);
        split.setPendingProdFlag(Constant.NO);
        
        if(!StringUtils.isBlank(storeId))
        {
            // 先修改之前已经选择门店的状态
            jykOrderChoiceStoreService.deleteByByProcInsId(procInsId);
            // 插入新选择的门店
            JykOrderChoiceStore choiceStore = new JykOrderChoiceStore();
            JykFlow jykFlow = flowService.getByProcInstId(procInsId);
            choiceStore.setOrderId(jykFlow.getOrderId());
            choiceStore.setSplitId(jykFlow.getSplitId());
            choiceStore.setProcInsId(procInsId);
            choiceStore.setChoiceStore(storeId);
            jykOrderChoiceStoreService.save(choiceStore);
            
            // 修改门店的状态为已推广
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            storeInfo.setStoreExtension("1");
            erpStoreInfoService.save(storeInfo);
        }
        
        // 判断选择的值
        if(StringUtils.isNotBlank(license) && "2".equals(license)){
            qualification = "";
            extensionExpect = "";
        }
        if(StringUtils.isNotBlank(qualification) && "2".equals(qualification)){
            extensionExpect = "";
        }
        
        // 默认自动移出待生产库
        this.workFlowService.setVariable(taskId, "isAutoOutPending", "true");
        // 推广时间
        Double distanceDays = 0D;
        if (StringUtils.isNotBlank(extensionExpect) && "1".equals(extensionExpect) && StringUtils.isNotBlank(promotionTime)) {
            split.setPromotionTime(DateUtils.parseDate(promotionTime));
            // 超过20工作日要进入待生产库
            try {
                distanceDays = DateUtils.getDistanceOfTwoDate(
                                this.erpHolidaysService.enddate(new Date(), JykFlowConstants.PLANNING_DATE_DISTINCT * 8), split.getPromotionTime());
            } catch (ParseException e) {
                logger.info("日期解析出错！", e);
            }
            if (distanceDays.intValue() > 0) {
                split.setPendingReason("D");
                split.setTimeoutFlag(Constant.NO);
                split.setPendingProduced(Constant.YES);
                split.setPendingProdFlag(Constant.YES);
                taskExt.setPendingProdFlag(Constant.YES);
                this.workFlowService.setVariable(taskId, "inPendingReason", "PT");
            }
            this.workFlowService.setVariable(taskId, "promotionTime", DateUtils.formatDateTime(DateUtils.parseDate(promotionTime)));
            this.workFlowService.setVariable(taskId, "surePromotionButton", "1");
        }
        
        // 下次确定营业执照时间
        if (StringUtils.isNotBlank(license) && "2".equals(license) && StringUtils.isNotBlank(nextLicenseTime)) {
            split.setNextLicenseTime(DateUtils.parseDate(nextLicenseTime));
            split.setPendingReason("Q");
            split.setTimeoutFlag(Constant.NO);
            split.setPendingProduced(Constant.YES);
            split.setPendingProdFlag(Constant.YES);
            taskExt.setPendingProdFlag(Constant.YES);
            this.workFlowService.setVariable(taskId, "inPendingReason", "NLT");
            this.workFlowService.setVariable(taskId, "licenseNextTime", DateUtils.formatDateTime(DateUtils.parseDate(nextLicenseTime)));
            this.workFlowService.setVariable(taskId, "sureLicenseButton", "2");
        }else if("1".equals(license)){
            this.workFlowService.setVariable(taskId, "sureLicenseButton", "1");
        }
        
        // 下次确定资质齐全时间
        if (StringUtils.isNotBlank(qualification) && "2".equals(qualification) && StringUtils.isNotBlank(nextQualificationTime)) {
            Date date = split.getNextQualificationTime();
            if(null==date){
                // 确认投放门店资质齐全
                erpHisSplitServiceApi.qualification(split.getId(), qualification, storeId, "");
            }
            split.setNextQualificationTime(DateUtils.parseDate(nextQualificationTime));
            split.setPendingReason("Q");
            split.setTimeoutFlag(Constant.NO);
            split.setPendingProduced(Constant.YES);
            split.setPendingProdFlag(Constant.YES);
            taskExt.setPendingProdFlag(Constant.YES);
            this.workFlowService.setVariable(taskId, "inPendingReason", "NQT");
            this.workFlowService.setVariable(taskId, "qualificationNextTime", DateUtils.formatDateTime(DateUtils.parseDate(nextQualificationTime)));
            this.workFlowService.setVariable(taskId, "sureQualificationButton", "2");
        }else if("1".equals(qualification)){
            this.workFlowService.setVariable(taskId, "sureQualificationButton", "1");
        }
        
        // 下次确定投放上线预期时间
        if (StringUtils.isNotBlank(extensionExpect) && "2".equals(extensionExpect) && StringUtils.isNotBlank(nextExtensionExpectTime)) {
            split.setNextContactTime(DateUtils.parseDate(nextExtensionExpectTime));
            split.setPendingReason("D");
            split.setTimeoutFlag(Constant.NO);
            split.setPendingProduced(Constant.YES);
            split.setPendingProdFlag(Constant.YES);
            taskExt.setPendingProdFlag(Constant.YES);
            this.workFlowService.setVariable(taskId, "inPendingReason", "NCT");
            this.workFlowService.setVariable(taskId, "promotionNextTime", DateUtils.formatDateTime(DateUtils.parseDate(nextExtensionExpectTime)));
            this.workFlowService.setVariable(taskId, "surePromotionButton", "2");
        }
        
        // 确认推广
        if (StringUtils.isNotBlank(extensionExpect) && "1".equals(extensionExpect)) {
            Map<String, Object> vars = Maps.newHashMap();
            if(isFinished){
                // 推广时间
                if (distanceDays.intValue() <= 0) {
                    vars.put("isStarted", "1");
                    split.setPendingProdFlag(Constant.NO);
                    taskExt.setPendingProdFlag(Constant.NO);
                    
                    // 如果满足 小于20工作日，必须要选择门店
                    if(StringUtils.isBlank(storeId))
                    {
                       resObject.put("result", false);
                        resObject.put("message", "请选择需要推广的门店!");
                       return resObject;
                    }
                    
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
                    for(Task task : tasks){
                        // 判断商户是否上传推广图片素材
                        if (task.getTaskDefinitionKey().startsWith("upload_promotional_pictures_zhixiao")) {
                            ErpStorePromotePhotoMaterial promotePhotoMaterial = erpStorePromotePhotoMaterialService.findlistWhereStoreId("0", storeId);
                            if(promotePhotoMaterial!=null){
                                jykFlowAccountSignalService.uploadPPromotionalPictures(storeId);
                            }
                            continue;
                        }
                        
                        // 判断当前商户是否完成掌贝进件
                        if (task.getTaskDefinitionKey().startsWith("zhangbei_in_sucess_zhixiao")) {
                            ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiID(split.getShopId());
                            if(shopInfo!=null && 2 == shopInfo.getZhangbeiState().intValue()){
                                jykFlowAccountSignalService.zhangbeiIntopiece(split.getShopId());
                            }
                            continue;
                        }
                    }
                }
            }
        }
        // 手动激活待生产库订单，但任务未结束（正处于等生产库但手动移出了）
        if (Constant.YES.equals(pendingProdFlag) && Constant.NO.equals(taskExt.getPendingProdFlag())) {
            split.setActivationTime(new Date());
            // 手动移出待生产库
            this.workFlowService.setVariable(taskId, "isAutoOutPending", "false");
        }
        this.erpOrderSplitInfoService.save(split);
        this.taskExtService.updateTaskState(taskExt);
        resObject.put("result", true);
        return resObject;
    }
    
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
     * @param nextLicenseTime 下次确定营业执照时间
     * @param nextQualificationTime 下次确定资质齐全时间
     * @param nextExtensionExpectTime 下次确定投放上线预期时间
     * @param promotionTime 预期投放时间
     * @param channels 推广通道
     * @return
     * @date 2018年4月9日
     * @author zjq
     */
    @RequestMapping(value = "contact_shop_zhixiao_latest")
    @ResponseBody
    public JSONObject contactShopZhixiaoLatest(String taskId, String procInsId, 
                                         boolean isFinished, String storeId, String storeName,
                                         String license, String qualification, String extensionExpect,
                    String nextLicenseTime, String nextQualificationTime, String nextExtensionExpectTime, String promotionTime,
                    String[] channels) {
        return  jykMerchantService.contactShopZhixiaoLatest(taskId, procInsId, isFinished, storeId, storeName, license, qualification,
                        extensionExpect, nextLicenseTime, nextQualificationTime, nextExtensionExpectTime, promotionTime, channels);
    }

    @RequestMapping(value = "management_diagnosis_marketing_planning_3.1")
    public @ResponseBody BaseResult diagnosisMarketingV31(String splitId, String taskId, String procInsId) {
        logger.info("确定完成任务 =》 经营诊断&营销策划，参数：splitId = {}, taskId = {}, procInsId = {}", splitId, taskId, procInsId);
        String resultMsg = "确定完成任务 =》 经营诊断&营销策划，结果：{}";

        // 校验表单
        BaseResult baseResult = formValidateService.validateForm(splitId);
        if (!BaseResult.isSuccess(baseResult)) {
            logger.info(resultMsg, baseResult);
            return baseResult;
        }

        // 获取推广通道
        String channelList = "";
        List<Integer> channels = jykOrderPromotionChannelService.getChannels(splitId);
        if (CollectionUtils.isEmpty(channels)) {
            return baseResult.error("-1", "推广通道未选择！");
        }
        for (Integer integer : channels) {
            channelList += (String.valueOf(integer) + Constant.COMMA);
        }
        if (channelList.endsWith(Constant.COMMA)) {
            channelList = channelList.substring(0, channelList.lastIndexOf(Constant.COMMA));
        }
        
        // 完成任务
        JSONObject jsonObject = managementDiagnosisMarketingPlanning(taskId, procInsId, true, channelList, splitId);
        if (!jsonObject.getBoolean("result")) {
            return baseResult.error("-3", jsonObject.getString("message"));
        }
        // 判断是否开户完成-修改对应通知状态
        if (jykFlowJudgeService.isFinishOpenAccountService(procInsId) && jykFlowJudgeService.isFinishOpenAccountZhixiao(procInsId)) {
            erpServiceMessageService.managerMessageByManual(procInsId, "SplitJuYinKe", "PromotionProposalConfirmFinish",
                            ServiceProgressTemplateConstants.STATUS_BEGIN);
        } else {
            erpServiceMessageService.managerMessageByManual(procInsId, "SplitJuYinKe", "PromotionProposalConfirmNoFinish",
                            ServiceProgressTemplateConstants.STATUS_BEGIN);
        }
        logger.info(resultMsg, baseResult);
        return baseResult;
    }
    

    /**
     * 经营诊断&营销策划
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "management_diagnosis_marketing_planning")
    @ResponseBody
    public JSONObject managementDiagnosisMarketingPlanning(String taskId, String procInsId, boolean isFinished, String channelList,
                    String orderFileId, String orderFileName) {
        JSONObject resObject = new JSONObject();
        // 获取信息
        JykFlow jykFlow = flowService.getByProcInstId(procInsId);
        ErpOrderOriginalInfo order = erpOrderOriginalInfoService.get(jykFlow.getOrderId());
        ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiID(order.getShopId());
        List<ErpStoreInfo> stores = erpStoreInfoService.findAllListWhereShopId("0", shopInfo.getId());
        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(jykFlow.getSplitId());
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);

        if (jykFlow == null || order == null) {
            resObject.put("result", false);
            resObject.put("message", "非法的订单编号，请确认订单编号是否正确!");
            return resObject;
        }

        if (StringUtils.isNotBlank(orderFileId)) {
            String[] fileIds = orderFileId.split(",");
            for (String fileId : fileIds) {
                // 更新当前的文件为正式文件
                ErpOrderFile file = this.erpOrderFileService.get(fileId);
                file.setDelFlag("0");
                this.erpOrderFileService.save(file);
            }
        }

        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("chooseFriendFlag", "N");
        vars.put("chooseMicroblogFlag", "N");
        vars.put("chooseMomoFlag", "N");
        String channel[] = channelList.split(",");
        for (int i = 0; i < channel.length; i++) {
            if (channel[i].equals("1")) {
                vars.put("chooseFriendFlag", "Y");
                vars.put("friends", "1");
                // 获取门店信息,判断是否进行过朋友圈推广
                if (storeInfo != null && StringUtils.isNotBlank(storeInfo.getFriendExtension()) && "1".equals(storeInfo.getFriendExtension())) {
                    vars.put("storeFriendsHisFlag", "Y");
                    vars.put("shopFriendsHisFlag", "Y");
                } else {
                    vars.put("storeFriendsHisFlag", "N");
                    vars.put("shopFriendsHisFlag", "N");
                    // 判断商户是否进行过朋友圈推广
                    if (stores != null && stores.size() > 0) {
                        for (ErpStoreInfo store : stores) {
                            if (store != null && StringUtils.isNotBlank(storeInfo.getFriendExtension()) && "1"
                                            .equals(storeInfo.getFriendExtension())) {
                                vars.put("shopFriendsHisFlag", "Y");
                                break;
                            }
                        }
                    }
                }
                storeInfo.setFriendExtension("1");
            }
            if (channel[i].equals("2")) {
                vars.put("chooseMicroblogFlag", "Y");
                vars.put("weibo", "2");
                // 获取门店信息,判断是否进行过微博推广
                if (storeInfo != null && StringUtils.isNotBlank(storeInfo.getWeiboExtension()) && "1".equals(storeInfo.getWeiboExtension())) {
                    vars.put("storeMicroblogHisFlag", "Y");
                    vars.put("shopMicroblogHisFlag", "Y");
                } else {
                    vars.put("storeMicroblogHisFlag", "N");
                    vars.put("shopMicroblogHisFlag", "N");
                    if (stores != null && stores.size() > 0) {
                        // 判断商户是否进行过微博推广
                        for (ErpStoreInfo store : stores) {
                            if (store != null && StringUtils.isNotBlank(storeInfo.getWeiboExtension()) && "1".equals(storeInfo.getWeiboExtension())) {
                                vars.put("shopMicroblogHisFlag", "Y");
                                break;
                            }
                        }
                    }
                }
                storeInfo.setWeiboExtension("1");
            }
            if (channel[i].equals("3")) {
                vars.put("chooseMomoFlag", "Y");
                vars.put("momo", "3");
                // 获取门店信息,判断是否进行过陌陌推广
                if (storeInfo != null && StringUtils.isNotBlank(storeInfo.getMomoExtension()) && "1".equals(storeInfo.getMomoExtension())) {
                    vars.put("storeMomoHisFlag", "Y");
                    vars.put("shopMomoHisFlag", "Y");
                } else {
                    vars.put("storeMomoHisFlag", "N");
                    vars.put("shopMomoHisFlag", "N");
                    if (stores != null && stores.size() > 0) {
                        // 判断商户是否进行过陌陌推广
                        for (ErpStoreInfo store : stores) {
                            if (store != null && StringUtils.isNotBlank(storeInfo.getMomoExtension()) && "1".equals(storeInfo.getMomoExtension())) {
                                vars.put("shopMomoHisFlag", "Y");
                                break;
                            }
                        }
                    }
                }
                storeInfo.setMomoExtension("1");
            }
        }

        // 保存门店信息
        erpStoreInfoService.save(storeInfo);
        // 保存推广渠道
        this.jykFlowContactShopService.choosePromotionChannel(procInsId, channelList, jykFlow, taskId);

        // 判断是否上传过推广素材
        ErpStorePromotePhotoMaterial promotePhotoMaterial = erpStorePromotePhotoMaterialService.findlistWhereStoreId("0", storeId);
        if (promotePhotoMaterial != null) {
            this.workFlowService.setVariable(taskId, "UploadPictureMaterial", promotePhotoMaterial.getStoreInfoId());
        }

        vars.put("orderType", order.getOrderType());
        if (isFinished) {
            String textDesignInterfacePerson = this.erpOrderFlowUserService
                            .findListByFlowId(procInsId, JykFlowConstants.assignTextDesignInterfacePerson).getUser().getId();
            String consultantInterface = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultantInterface)
                            .getUser().getId();
            vars.put("textDesignInterfacePerson", textDesignInterfacePerson);
            vars.put("consultantInterface", consultantInterface);
            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                            "经营诊断&营销策划", vars);

            // 判断接下来的流程是否往下流转
            // 获取当前流程下的任务
            ProcessInstance processInstance = actTaskService.getProcIns(procInsId);
            List<Task> tasks = taskService.createTaskQuery().includeProcessVariables().processInstanceId(processInstance.getId()).list();
            for (Task task : tasks) {
                // 判断是否完善朋友圈开户资料
                if (validateFriendAccount(task)) {
                    ErpStoreAdvertiserFriends advertiserFriends = erpStoreAdvertiserFriendsService.getByStoreId(storeId);
                    if (advertiserFriends != null && 1 == advertiserFriends.getAuditStatus().intValue()) {
                        jykFlowAccountSignalService.perfectFriendsPromote(storeId);
                    }
                    continue;
                }

                // 判断是否完善微博开户资料
                if (validateWeiboAccount(task)) {
                    ErpStoreAdvertiserWeibo advertiserWeibo = erpStoreAdvertiserWeiboService.getByStoreId(storeId);
                    if (advertiserWeibo != null && 1 == advertiserWeibo.getAuditStatus().intValue()) {
                        jykFlowAccountSignalService.perfectMicroblogPromote(storeId);
                    }
                    continue;
                }

                // 判断是否完善陌陌开户资料
                if (validateMomoAccount(task)) {
                    ErpStoreAdvertiserMomo advertiserMomo = erpStoreAdvertiserMomoService.getByStoreId(storeId);
                    if (advertiserMomo != null && 1 == advertiserMomo.getAuditStatus().intValue()) {
                        jykFlowAccountSignalService.perfectMomoPromote(storeId);
                    }
                    continue;
                }
            }
        }
        resObject.put("result", true);
        return resObject;
    }

    private boolean validateFriendAccount(Task task) {
        return task.getTaskDefinitionKey().startsWith("perfect_friends_promote_info_zhixiao") || task.getTaskDefinitionKey()
                        .startsWith("perfect_friends_promote_info_service");
    }


    /**
     * 经营诊断&营销策划
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    public JSONObject managementDiagnosisMarketingPlanning(String taskId, String procInsId, 
                    boolean isFinished, String channelList, String splitId) {
        JSONObject resObject = new JSONObject();
        // 获取信息
        JykFlow jykFlow = flowService.getByProcInstId(procInsId);
        ErpOrderOriginalInfo order = erpOrderOriginalInfoService.get(jykFlow.getOrderId());

        if (jykFlow == null || order == null) {
            resObject.put("result", false);
            resObject.put("message", "非法的订单编号，请确认订单编号是否正确!");
            return resObject;
        }


        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoService.get(splitId);

        List<ErpOrderSplitGood> erpOrderSplitGoods = erpOrderSplitInfo.getErpOrderSplitGoods();

        String chooseGoodFlag = Constant.NO;

        for (ErpOrderSplitGood erpOrderSplitGood : erpOrderSplitGoods) {
            if (Constant.YES.equalsIgnoreCase(erpOrderSplitGood.getIsPromote())) {
                chooseGoodFlag = Constant.YES;
                break;
            }
        }

        if (Constant.NO.equalsIgnoreCase(chooseGoodFlag)) {
            resObject.put("result", false);
            resObject.put("message", "请选择套餐!");
            return resObject;
        }

        ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiID(order.getShopId());
        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(jykFlow.getSplitId());
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("chooseFriendFlag", "N");
        vars.put("chooseMicroblogFlag", "N");
        vars.put("chooseMomoFlag", "N");
        String[] channel = channelList.split(",");
        List<ErpStoreInfo> stores = erpStoreInfoService.findAllListWhereShopId("0", shopInfo.getId());
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        for (int i = 0; i < channel.length; i++) {
            if ("1".equals(channel[i])) {
                vars.put("chooseFriendFlag", "Y");
                vars.put("friends", "1");
                // 获取门店信息,判断是否进行过朋友圈推广
                if (storeInfo != null && StringUtils.isNotBlank(storeInfo.getFriendExtension()) && "1".equals(storeInfo.getFriendExtension())) {
                    vars.put("storeFriendsHisFlag", "Y");
                    vars.put("shopFriendsHisFlag", "Y");
                } else {
                    vars.put("storeFriendsHisFlag", "N");
                    vars.put("shopFriendsHisFlag", "N");
                    // 判断商户是否进行过朋友圈推广
                    if (!CollectionUtils.isEmpty(stores)) {
                        for (ErpStoreInfo store : stores) {
                            if (store != null && StringUtils.isNotBlank(storeInfo.getFriendExtension()) && "1"
                                            .equals(storeInfo.getFriendExtension())) {
                                vars.put("shopFriendsHisFlag", "Y");
                                break;
                            }
                        }
                    }
                }
                storeInfo.setFriendExtension("1");

                if (erpShopExtensionQualifyService.countByShopAndChannel(shopInfo.getId(), "1") == 0) {
                    ErpShopExtensionQualify extensionQualify = new ErpShopExtensionQualify();
                    extensionQualify.setShopId(shopInfo.getId());
                    extensionQualify.setExtensionValue("1");
                    this.erpShopExtensionQualifyService.save(extensionQualify);
                }
            }
            if ("2".equals(channel[i])) {
                vars.put("chooseMicroblogFlag", "Y");
                vars.put("weibo", "2");
                // 获取门店信息,判断是否进行过微博推广
                if (storeInfo != null && StringUtils.isNotBlank(storeInfo.getWeiboExtension()) && "1".equals(storeInfo.getWeiboExtension())) {
                    vars.put("storeMicroblogHisFlag", "Y");
                    vars.put("shopMicroblogHisFlag", "Y");
                } else {
                    vars.put("storeMicroblogHisFlag", "N");
                    vars.put("shopMicroblogHisFlag", "N");
                    if (!CollectionUtils.isEmpty(stores)) {
                        // 判断商户是否进行过微博推广
                        for (ErpStoreInfo store : stores) {
                            if (store != null && StringUtils.isNotBlank(storeInfo.getWeiboExtension()) && "1".equals(storeInfo.getWeiboExtension())) {
                                vars.put("shopMicroblogHisFlag", "Y");
                                break;
                            }
                        }
                    }
                }
                storeInfo.setWeiboExtension("1");

                if (erpShopExtensionQualifyService.countByShopAndChannel(shopInfo.getId(), "2") == 0) {
                    ErpShopExtensionQualify extensionQualify = new ErpShopExtensionQualify();
                    extensionQualify.setShopId(shopInfo.getId());
                    extensionQualify.setExtensionValue("2");
                    this.erpShopExtensionQualifyService.save(extensionQualify);
                }
            }
            if ("3".equals(channel[i])) {
                vars.put("chooseMomoFlag", "Y");
                vars.put("momo", "3");
                // 获取门店信息,判断是否进行过陌陌推广
                if (storeInfo != null && StringUtils.isNotBlank(storeInfo.getMomoExtension()) && "1".equals(storeInfo.getMomoExtension())) {
                    vars.put("storeMomoHisFlag", "Y");
                    vars.put("shopMomoHisFlag", "Y");
                } else {
                    vars.put("storeMomoHisFlag", "N");
                    vars.put("shopMomoHisFlag", "N");
                    if (!CollectionUtils.isEmpty(stores)) {
                        // 判断商户是否进行过陌陌推广
                        for (ErpStoreInfo store : stores) {
                            if (store != null && StringUtils.isNotBlank(storeInfo.getMomoExtension()) && "1".equals(storeInfo.getMomoExtension())) {
                                vars.put("shopMomoHisFlag", "Y");
                                break;
                            }
                        }
                    }
                }
                storeInfo.setMomoExtension("1");

                if (erpShopExtensionQualifyService.countByShopAndChannel(shopInfo.getId(), "3") == 0) {
                    ErpShopExtensionQualify extensionQualify = new ErpShopExtensionQualify();
                    extensionQualify.setShopId(shopInfo.getId());
                    extensionQualify.setExtensionValue("3");
                    this.erpShopExtensionQualifyService.save(extensionQualify);
                }
            }
        }

        // 保存门店信息
        erpStoreInfoService.save(storeInfo);
        // 保存推广渠道
        this.jykFlowContactShopService.choosePromotionChannel(procInsId, channelList, jykFlow, taskId);

        // 判断是否上传过推广素材
        ErpStorePromotePhotoMaterial promotePhotoMaterial = erpStorePromotePhotoMaterialService.findlistWhereStoreId("0", storeId);
        if (promotePhotoMaterial != null) {
            this.workFlowService.setVariable(taskId, "UploadPictureMaterial", promotePhotoMaterial.getStoreInfoId());
        }

        vars.put("orderType", order.getOrderType());
        if (isFinished) {
            String textDesignInterfacePerson = this.erpOrderFlowUserService
                            .findListByFlowId(procInsId, JykFlowConstants.assignTextDesignInterfacePerson).getUser().getId();
            String consultantInterface = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultantInterface)
                            .getUser().getId();
            vars.put("textDesignInterfacePerson", textDesignInterfacePerson);
            vars.put("consultantInterface", consultantInterface);
            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                            "经营诊断&营销策划", vars);

            // 根据经营诊断选择套餐信息，动态更新分单商品及数量
            erpOrderSplitInfoService.updateSplitGoodInfoByDiagnosis(splitId);

            // 经营诊断完成
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            erpHisSplitServiceApi.diagnosis(jykFlow.getSplitId(), sdf.format(new Date(System.currentTimeMillis())), storeInfo.getId(),
                            storeInfo.getShortName());

            // 确认投放通道后
            erpHisSplitServiceApi.deliveryChannel(jykFlow.getSplitId(), channelList, storeInfo.getId(), storeInfo.getShortName());

            // 判断接下来的流程是否往下流转
            // 获取当前流程下的任务
            ProcessInstance processInstance = actTaskService.getProcIns(procInsId);
            List<Task> tasks = taskService.createTaskQuery().includeProcessVariables().processInstanceId(processInstance.getId()).list();
            for (Task task : tasks) {
                // 判断是否完善朋友圈开户资料
                if (validateFriendAccount(task)) {
                    ErpStoreAdvertiserFriends advertiserFriends = erpStoreAdvertiserFriendsService.getByStoreId(storeId);
                    if (advertiserFriends != null && 1 == advertiserFriends.getAuditStatus().intValue()) {
                        jykFlowAccountSignalService.perfectFriendsPromote(storeId);
                    }
                    continue;
                }

                // 判断是否完善微博开户资料
                if (validateWeiboAccount(task)) {
                    ErpStoreAdvertiserWeibo advertiserWeibo = erpStoreAdvertiserWeiboService.getByStoreId(storeId);
                    if (advertiserWeibo != null && 1 == advertiserWeibo.getAuditStatus().intValue()) {
                        jykFlowAccountSignalService.perfectMicroblogPromote(storeId);
                    }
                    continue;
                }

                // 判断是否完善陌陌开户资料
                if (validateMomoAccount(task)) {
                    ErpStoreAdvertiserMomo advertiserMomo = erpStoreAdvertiserMomoService.getByStoreId(storeId);
                    if (advertiserMomo != null && 1 == advertiserMomo.getAuditStatus().intValue()) {
                        jykFlowAccountSignalService.perfectMomoPromote(storeId);
                    }
                    continue;
                }
            
            }
        resObject.put("result", true);
            }
        

        return resObject ;
    }

    private boolean validateMomoAccount(Task task) {
        return task.getTaskDefinitionKey().startsWith("perfect_momo_promote_info_zhixiao") || task.getTaskDefinitionKey()
                        .startsWith("perfect_momo_promote_info_service");
    }

    private boolean validateWeiboAccount(Task task) {
        return task.getTaskDefinitionKey().startsWith("perfect_microblog_promote_info_zhixiao") || task.getTaskDefinitionKey()
                        .startsWith("perfect_microblog_promote_info_service");
    }
    
    /**
     * 确认完成上传推广图片素材
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "upload_promotional_pictures_zhixiao")
    @ResponseBody
    public JSONObject uploadPromotionalPicturesZhixiao(String taskId, String procInsId, String channelList){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "确认完成上传推广图片素材", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 推广图片素材查阅
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "popularize_picture_material_consult_zhixiao")
    @ResponseBody
    public JSONObject popularizePictureMaterialConsultZhixiao(String taskId, String procInsId){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "确认完成上传推广图片素材", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 是否进行微信支付进件
     *
     * @param taskId
     * @param procInsId
     * @param isPass 是否通过 1：通过2：不通过
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "decide_wechat_in_zhixiao")
    @ResponseBody
    public JSONObject decideWechatInZhixiao(String taskId, String procInsId, String isPass){
        JSONObject resObject = new JSONObject();
        
        ErpStoreInfo storeInfo = erpStoreInfoService.get(jykOrderChoiceStoreService.getStoreIdByProcInsId(procInsId));
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("wechatIntoFlag", "N");
        if("1".equals(isPass)){
            vars.put("wechatIntoFlag", "Y");
        }
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId, "是否进行微信支付进件", vars);
        
        // 获取当前流程下的任务
        ProcessInstance processInstance = actTaskService.getProcIns(procInsId);
        List<Task> tasks = taskService.createTaskQuery().includeProcessVariables().processInstanceId(processInstance.getId()).list();
        for(Task task : tasks){
            // 判断所选门店是否完成微信支付进件
            if (task.getTaskDefinitionKey().startsWith("weixin_in_sucess_zhixiao")) {
                if(storeInfo!=null && StringUtils.isNotBlank(storeInfo.getWeixinPayId())){
                    ErpStorePayWeixin payWeixin = erpStorePayWeixinService.get(storeInfo.getWeixinPayId());
                    if(payWeixin!=null && payWeixin.getAuditStatus().intValue() == 2){
                        jykFlowAccountSignalService.wechatPayIntopiece2(storeInfo.getId());
                    }
                }
                continue;
            }
        }
        resObject.put("result", true);
        return resObject;
    }

    /* ===============================朋友圈 START============================== */
    /**
     * 在公众号平台创建新门店
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "create_stores_on_official_zhixiao")
    @ResponseBody
    public JSONObject createStoresOnOfficialZhixiao(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                        "在公众号平台创建新门店", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 确认完成朋友圈推广开户资料
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "perfect_friends_promote_info_zhixiao")
    @ResponseBody
    public JSONObject perfectFriendsPromoteInfoZhixiao(String taskId, String procInsId, String channelList){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryFriends, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                        "确认完成朋友圈推广开户资料", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 朋友圈推广开户资料复审
     *
     * @param taskId
     * @param procInsId
     * @param isPass 是否通过 1：通过2：不通过
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "friends_promote_info_review_zhixiao")
    @ResponseBody
    public JSONObject friendsPromoteInfoReviewZhixiao(String taskId, String procInsId, String isPass, String reason){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        JykFlow jykFlow = flowService.getByProcInstId(procInsId);
        vars.put("reviewResultFriends", "1".equals(isPass) ? "Y" : "N");
        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(jykFlow.getSplitId());
        ErpStoreAdvertiserFriends erpStoreAdvertiserFriends = erpStoreAdvertiserFriendsService.getByStoreId(storeId);
        if(erpStoreAdvertiserFriends!=null){
            erpStoreAdvertiserFriends.setAuditStatus("1".equals(isPass) ? AUDIT_STATUS_PASSED : AUDIT_STATUS_REJECT);
            erpStoreAdvertiserFriendsService.save(erpStoreAdvertiserFriends);
        }
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 保存操作内容
        ErpOrderOperateValue erpOrderOperateValue = this.erpOrderOperateValueService.getOnlyOne(procInsId, "friends_promote_info_review_zhixiao", "1");
        if(erpOrderOperateValue!=null){
            erpOrderOperateValue.setValue(reason);
        }else{
            erpOrderOperateValue = new ErpOrderOperateValue();
            erpOrderOperateValue.setOrderId(jykFlow.getOrderId());
            erpOrderOperateValue.setSplitId(jykFlow.getSplitId());
            erpOrderOperateValue.setProcInsId(procInsId);
            erpOrderOperateValue.setSubTaskId("1");
            erpOrderOperateValue.setKeyName("friends_promote_info_review_zhixiao");
            erpOrderOperateValue.setValue(reason);
        }
        this.erpOrderOperateValueService.save(erpOrderOperateValue);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryFriends, JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId,
                        "完成朋友圈推广开户资料复审", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 修改朋友圈推广开户资料
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "modify_friends_promote_info_zhixiao")
    @ResponseBody
    public JSONObject modifyFriendsPromoteInfoService(String taskId, String procInsId){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryFriends}, taskId, procInsId, "完成修改朋友圈推广开户资料", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 发起朋友圈授权
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "start_friends_authorization_zhixiao")
    @ResponseBody
    public JSONObject startFriendsAuthorizationZhixiao(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId, "完成发起朋友圈授权", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 确认朋友圈授权成功
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "confirmed_friends_authorization_sucess_zhixiao")
    @ResponseBody
    public JSONObject confirmedFriendsAuthorizationSucessZhixiao(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                        "完成确认朋友圈授权成功", vars);
        // 是否完成直销开户
        if(jykFlowJudgeService.isFinishOpenAccountZhixiao(procInsId)){
            JykFlow jykFlow = flowService.getByProcInstId(procInsId);
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(jykFlow.getSplitId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            erpHisSplitServiceApi.openAccount(jykFlow.getSplitId(), "", sdf.format(new Date(System.currentTimeMillis())), storeInfo.getId(), storeInfo.getShortName());
            jykMerchantService.isActivateNotifyShopFlow(jykFlow.getSplitId(), procInsId);
        }
        resObject.put("result", true);
        return resObject;
    }
    /* ===============================朋友圈 END============================== */
    
    /* ===============================微博 START============================== */
    /**
     * 判断是否进行微博推广开户
     *
     * @param taskId
     * @param procInsId
     * @param isPass
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "decide_microblog_extension_zhixiao")
    @ResponseBody
    public JSONObject decideMicroblogExtensionZhixiao(String taskId, String procInsId, String isPass){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        vars.put("isExtensionWeibo", "N");
        if("1".equals(isPass)){
            vars.put("isExtensionWeibo", "Y");
        }
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                        "判断是否进行微博推广开户", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 确认完成微博推广开户资料
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "perfect_microblog_promote_info_zhixiao")
    @ResponseBody
    public JSONObject perfectMicroblogPromoteInfoZhixiao(String taskId, String procInsId, String channelList){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryWeibo}, taskId, procInsId, "确认完成微博推广开户资料", vars);
        resObject.put("result", true);
        return resObject;
    }
    

    /**
     * 确认完成录入微博账号信息
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年4月17日
     * @author zjq
     */
    @RequestMapping(value = "perfect_microblog_promote_info_zhixiao_latest")
    @ResponseBody
    public JSONObject perfectMicroblogPromoteInfoZhixiaoLatest(String taskId, String procInsId, String channelList, String openOrTrans) {
        return jykMerchantService.perfectMicroblogPromoteInfoZhixiaoLatest(taskId, procInsId, openOrTrans);

    }

    /**
     * 微博推广开户资料复审
     *
     * @param taskId
     * @param procInsId
     * @param isPass 是否通过 1：通过2：不通过
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "microblog_promote_info_review_zhixiao")
    @ResponseBody
    public JSONObject microblogPromoteInfoReviewZhixiao(String taskId, String procInsId, String isPass, String reason){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        JykFlow jykFlow = flowService.getByProcInstId(procInsId);
        vars.put("reviewResultWeibo", "N");
        if("1".equals(isPass)){
            vars.put("reviewResultWeibo", "Y");
        }
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 保存操作内容
        ErpOrderOperateValue erpOrderOperateValue = this.erpOrderOperateValueService.getOnlyOne(procInsId, "microblog_promote_info_review_zhixiao", "1");
        if(erpOrderOperateValue!=null){
            erpOrderOperateValue.setValue(reason);
        }else{
            erpOrderOperateValue = new ErpOrderOperateValue();
            erpOrderOperateValue.setOrderId(jykFlow.getOrderId());
            erpOrderOperateValue.setSplitId(jykFlow.getSplitId());
            erpOrderOperateValue.setProcInsId(procInsId);
            erpOrderOperateValue.setSubTaskId("1");
            erpOrderOperateValue.setKeyName("microblog_promote_info_review_zhixiao");
            erpOrderOperateValue.setValue(reason);
        }
        this.erpOrderOperateValueService.save(erpOrderOperateValue);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryWeibo, JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId,
                        "完成微博推广开户资料复审", vars);
        resObject.put("result", true);
        return resObject;
    }
    

    /**
     * 微博推广开户资料复审
     *
     * @param taskId
     * @param procInsId
     * @param isPass 是否通过 1：通过 2：不通过
     * @param openOrTrans 开户或转户 O:开户T:转户
     * @return
     * @date 2018年4月10日
     * @author
     */
    @RequestMapping(value = "microblog_promote_info_review_zhixiao_latest")
    @ResponseBody
    public JSONObject microblogPromoteInfoReviewZhixiaoLatest(String taskId, String procInsId, String isPass, String reason, String openOrTrans) {
        return jykMerchantService.microblogPromoteInfoReviewZhixiaoLatest(taskId, procInsId, isPass, reason, openOrTrans);
    }

    /**
     * 修改微博推广开户资料
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "modify_microblog_promote_info_zhixiao")
    @ResponseBody
    public JSONObject modifyMicroblogPromoteInfoZhixiao(String taskId, String procInsId){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryWeibo}, taskId, procInsId, "完成修改微博推广开户资料", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 微博推广开户资料提审
     *
     * @param taskId
     * @param procInsId
     * @param isPass 是否通过 1：通过2：不通过
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "microblog_promote_info_review_result_zhixiao")
    @ResponseBody
    public JSONObject microblogPromoteInfoReviewResultZhixiao(String taskId, String procInsId, String isPass, String reason){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        JykFlow jykFlow = flowService.getByProcInstId(procInsId);
        vars.put("reviewResultWeibo2", "N");
        if("1".equals(isPass)){
            vars.put("reviewResultWeibo2", "Y");
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(jykFlow.getSplitId());
            ErpStoreAdvertiserWeibo erpStoreAdvertiserWeibo = erpStoreAdvertiserWeiboService.getByStoreId(storeId);
            if(erpStoreAdvertiserWeibo!=null){
                erpStoreAdvertiserWeibo.setAuditStatus(AUDIT_STATUS_PASSED);
                erpStoreAdvertiserWeiboService.save(erpStoreAdvertiserWeibo);
            }
        }
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 保存操作内容
        ErpOrderOperateValue erpOrderOperateValue = this.erpOrderOperateValueService.getOnlyOne(procInsId, "microblog_promote_info_review_result_zhixiao", "1");
        if(erpOrderOperateValue!=null){
            erpOrderOperateValue.setValue(reason);
        }else{
            erpOrderOperateValue = new ErpOrderOperateValue();
            erpOrderOperateValue.setOrderId(jykFlow.getOrderId());
            erpOrderOperateValue.setSplitId(jykFlow.getSplitId());
            erpOrderOperateValue.setProcInsId(procInsId);
            erpOrderOperateValue.setSubTaskId("1");
            erpOrderOperateValue.setKeyName("microblog_promote_info_review_result_zhixiao");
            erpOrderOperateValue.setValue(reason);
        }
        this.erpOrderOperateValueService.save(erpOrderOperateValue);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                        "完成微博推广开户资料提审", vars);
        // 审核通过并且完成开户
        if("1".equals(isPass) && jykFlowJudgeService.isFinishOpenAccountZhixiao(procInsId)){
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(jykFlow.getSplitId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            erpHisSplitServiceApi.openAccount(jykFlow.getSplitId(), "", sdf.format(new Date(System.currentTimeMillis())), storeInfo.getId(), storeInfo.getShortName());
        }
        resObject.put("result", true);
        return resObject;
    }
    /* ===============================微博 END============================== */
    
    /* ===============================陌陌 START============================== */
    /**
     * 判断是否进行陌陌推广开户
     *
     * @param taskId
     * @param procInsId
     * @param isPass
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "decide_momo_extension_zhixiao")
    @ResponseBody
    public JSONObject decideMomoExtensionZhixiao(String taskId, String procInsId, String isPass){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        vars.put("isExtensionMomo", "N");
        if("1".equals(isPass)){
            vars.put("isExtensionMomo", "Y");
        }
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                        "判断是否进行陌陌推广开户", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 确认完成陌陌推广开户资料
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "perfect_momo_promote_info_zhixiao")
    @ResponseBody
    public JSONObject perfectMomoPromoteInfoZhixiao(String taskId, String procInsId, String channelList){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryMomo, JykFlowConstants.OPERATION_ADVISER}, taskId, procInsId,
                        "确认完成陌陌推广开户资料", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 陌陌推广开户资料复审
     *
     * @param taskId
     * @param procInsId
     * @param isPass 是否通过 1：通过2：不通过
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "momo_promote_info_review_zhixiao")
    @ResponseBody
    public JSONObject momoPromoteInfoReviewZhixiao(String taskId, String procInsId, String isPass, String reason){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        JykFlow jykFlow = flowService.getByProcInstId(procInsId);
        vars.put("reviewResultMomo", "1".equals(isPass) ? "Y" : "N");
        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(jykFlow.getSplitId());
        ErpStoreAdvertiserMomo erpStoreAdvertiserMomo = erpStoreAdvertiserMomoService.getByStoreId(storeId);
        if (erpStoreAdvertiserMomo != null) {
            erpStoreAdvertiserMomo.setAuditStatus("1".equals(isPass) ? AUDIT_STATUS_PASSED : AUDIT_STATUS_REJECT);
            erpStoreAdvertiserMomoService.save(erpStoreAdvertiserMomo);
        }
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 保存操作内容
        ErpOrderOperateValue erpOrderOperateValue = this.erpOrderOperateValueService.getOnlyOne(procInsId, "momo_promote_info_review_zhixiao", "1");
        if(erpOrderOperateValue!=null){
            erpOrderOperateValue.setValue(reason);
        }else{
            erpOrderOperateValue = new ErpOrderOperateValue();
            erpOrderOperateValue.setOrderId(jykFlow.getOrderId());
            erpOrderOperateValue.setSplitId(jykFlow.getSplitId());
            erpOrderOperateValue.setProcInsId(procInsId);
            erpOrderOperateValue.setSubTaskId("1");
            erpOrderOperateValue.setKeyName("momo_promote_info_review_zhixiao");
            erpOrderOperateValue.setValue(reason);
        }
        this.erpOrderOperateValueService.save(erpOrderOperateValue);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                        "完成陌陌推广开户资料复审", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 修改陌陌推广开户资料
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "modify_momo_promote_info_zhixiao")
    @ResponseBody
    public JSONObject modifyMomoPromoteInfoService(String taskId, String procInsId){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryMomo}, taskId, procInsId, "完成修改陌陌推广开户资料", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 陌陌推广素材内审
     *
     * @param taskId
     * @param procInsId
     * @param isPass
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "promote_material_internal_review_zhixiao")
    @ResponseBody
    public JSONObject promoteMaterialInternalReviewZhixiao(String taskId, String procInsId, String isPass){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        vars.put("reviewResultMomo2", "N");
        if("1".equals(isPass)){
            vars.put("reviewResultMomo2", "Y");
        }
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "确认完成陌陌推广素材内审", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 补充陌陌开户资料
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "perfect_momo_account_info_zhixiao")
    @ResponseBody
    public JSONObject perfectMomoAccountInfoZhixiao(String taskId, String procInsId){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryMomo, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                        "完成补充陌陌开户资料", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 确认陌陌推广开户完成
     *
     * @param taskId
     * @param procInsId
     * @param url
     * @param isPass
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "confirm_momo_account_sucess_zhixiao")
    @ResponseBody
    public JSONObject confirmMomoAccountSucessZhixiao(String taskId, String procInsId, String url, String isPass){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        /**
         * if(StringUtils.isNotBlank(url)){ // 修改子任务完成状态
         * this.workFlowService.submitSubTask(procInsId, "1", taskId); }
         */
        if(StringUtils.isNotBlank(isPass)){
            vars.put("reviewResult", "modifyLandingPage");
            if("1".equals(isPass)){
                vars.put("reviewResult", "completed");
            }
            // 修改子任务完成状态
            this.workFlowService.submitSubTask(procInsId, "1", taskId);
        }
        
        if(StringUtils.isNotBlank(isPass)){
            // 任务完成
            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.OPERATION_ADVISER, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                            "确认陌陌推广开户完成", vars);
            // 是否完成直销开户
            if("1".equals(isPass) && jykFlowJudgeService.isFinishOpenAccountZhixiao(procInsId)){
                JykFlow jykFlow = flowService.getByProcInstId(procInsId);
                String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(jykFlow.getSplitId());
                ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                erpHisSplitServiceApi.openAccount(jykFlow.getSplitId(), "", sdf.format(new Date(System.currentTimeMillis())), storeInfo.getId(), storeInfo.getShortName());
                // 判断激活等待任务
                jykMerchantService.isActivateNotifyShopFlow(jykFlow.getSplitId(), procInsId);
            }
        }
        resObject.put("result", true);
        return resObject;
    }
    /* ===============================陌陌 END============================== */
}