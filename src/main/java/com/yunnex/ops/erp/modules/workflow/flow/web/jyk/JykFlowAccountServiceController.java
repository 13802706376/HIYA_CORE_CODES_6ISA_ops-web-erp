package com.yunnex.ops.erp.modules.workflow.flow.web.jyk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.act.service.TaskExtService;
import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserFriendsService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserMomoService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreCredentials;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreCredentialsService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStorePromotePhotoMaterialService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.entity.JykFlow;
import com.yunnex.ops.erp.modules.workflow.flow.service.ErpHisSplitServiceApi;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowAccountSignalService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowJudgeService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowService;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykServiceProviderService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;
import com.yunnex.ops.erp.modules.workflow.store.entity.JykOrderChoiceStore;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;

/**
 * 聚引客 服务商开户相关 Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/jyk/flow/accountService")
public class JykFlowAccountServiceController extends BaseController {
    
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private JykFlowService flowService;
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
    private TaskExtService taskExtService;
    @Autowired
    private ErpStorePromotePhotoMaterialService erpStorePromotePhotoMaterialService;
    @Autowired
    private ErpHisSplitServiceApi erpHisSplitServiceApi;
    @Autowired
    private JykFlowJudgeService jykFlowJudgeService;
    @Autowired
    private ErpHolidaysService erpHolidaysService;
    @Autowired
    private JykServiceProviderService jykServiceProviderService;
    @Autowired
    private ErpStoreAdvertiserFriendsService erpStoreAdvertiserFriendsService;
    @Autowired
    private ErpStoreAdvertiserWeiboService erpStoreAdvertiserWeiboService;
    @Autowired
    private ErpStoreAdvertiserMomoService erpStoreAdvertiserMomoService;
    @Autowired
    private ErpStoreCredentialsService erpStoreCredentialsService;
    
    /**
     * 服务商-商户对接/确认推广门店/资质/推广时间
     *
     * @param taskId
     * @param procInsId
     * @param storeId
     * @param storeName
     * @param nextLicenseTime 下次确定营业执照时间
     * @param nextQualificationTime 下次确定资质齐全时间
     * @param nextExtensionExpectTime 下次确定投放上线预期时间 * @param promotionTime 预期投放时间
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "contact_shop_service")
    @ResponseBody
    public JSONObject contactShopService(String taskId, String procInsId, 
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
            // 商户是否掌贝进件成功
            vars.put("zhangbeiAccountFlag", "N");
            ErpShopInfo shopInfo = erpShopInfoService.findListByZhangbeiId(split.getShopId());
            if(shopInfo!=null && StringUtils.isNotBlank(shopInfo.getPassword())){
                vars.put("zhangbeiAccountFlag", "Y");
            }
            
            // 门店是否有微信资质
            vars.put("wechatPaymQualFlag", "N");
            String state = erpStoreInfoService.getPayQualifyById(storeId);
            if(StringUtils.isNotBlank(state)){
                vars.put("wechatPaymQualFlag", "Y");
            }
            if (isFinished && distanceDays.intValue() <= 0) {
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
                    this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "推广时间确认", vars);
                    
                    // 确认推广门店成功
                    erpHisSplitServiceApi.extensionStore(split.getId(), storeId, storeName);
                    // 确认投放门店资质齐全
                    erpHisSplitServiceApi.qualification(split.getId(), qualification, storeId, storeName);
                    // 保存分单信息 （在流程结束前）
                    this.erpOrderSplitInfoService.save(split);
                    // 获取当前流程下的任务
                    ProcessInstance processInstance = actTaskService.getProcIns(procInsId);
                    List<Task> tasks = taskService.createTaskQuery().includeProcessVariables().processInstanceId(processInstance.getId()).list();
                    for(Task task : tasks){
                        // 判断商户是否上传推广图片素材
                        if (task.getTaskDefinitionKey().startsWith("upload_promotional_pictures_service")) {
                            ErpStorePromotePhotoMaterial promotePhotoMaterial = erpStorePromotePhotoMaterialService.findlistWhereStoreId("0", storeId);
                            if(promotePhotoMaterial!=null){
                                jykFlowAccountSignalService.uploadPPromotionalPictures(storeId);
                            }
                            continue;
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
     * 服务商-商户对接/确认推广门店/资质/推广时间
     *
     * @param taskId
     * @param procInsId
     * @param storeId
     * @param storeName
     * @param nextLicenseTime 下次确定营业执照时间
     * @param nextQualificationTime 下次确定资质齐全时间
     * @param nextExtensionExpectTime 下次确定投放上线预期时间 * @param promotionTime 预期投放时间
     * @param channels 推广通道 1：朋友圈 2：微博 3：陌陌
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "contact_shop_service_latest")
    @ResponseBody
    public JSONObject contactShopServiceLatest(String taskId, String procInsId, boolean isFinished, String storeId, String storeName, String license,
                    String qualification, String extensionExpect, String nextLicenseTime, String nextQualificationTime,
                    String nextExtensionExpectTime, String promotionTime, String[] channels) {
        return jykServiceProviderService.contactShopServiceLatest(taskId, procInsId, isFinished, storeId, storeName, license,
                        qualification, extensionExpect, nextLicenseTime, nextQualificationTime, nextExtensionExpectTime, promotionTime, channels);
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
    @RequestMapping(value = "upload_promotional_pictures_service")
    @ResponseBody
    public JSONObject uploadPromotionalPicturesService(String taskId, String procInsId, String channelList){
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
    @RequestMapping(value = "popularize_picture_material_consult_service")
    @ResponseBody
    public JSONObject popularizePictureMaterialConsultService(String taskId, String procInsId){
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
     * 判断商户是否进行微信支付
     *
     * @param taskId
     * @param procInsId
     * @param isDecide
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "decide_wechat_payment_service")
    @ResponseBody
    public JSONObject decideWechatPaymentService(String taskId, String procInsId, String isDecide){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("wechatPaymFlag", "N");
        if("1".equals(isDecide)){
            vars.put("wechatPaymFlag", "Y");
        }
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "判断商户是否进行微信支付", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 督促微信进件
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @param isFinished
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "urge_wechat_enter_service")
    @ResponseBody
    public JSONObject urgeWechatEnterService(String taskId, String procInsId, String channelList, boolean isFinished){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        // 保存业务流转信息
        this.workFlowService.submitSubTask(procInsId, channelList, taskId);
        if (isFinished) {
            vars.put("wechatPaySuccess", "1");
            this.workFlowService.setVariable(taskId, "wechatPaySuccess", "1");
            // 任务完成
            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "督促微信进件", vars);
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
    @RequestMapping(value = "create_stores_on_official_service")
    @ResponseBody
    public JSONObject createStoresOnOfficialService(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "在公众号平台创建新门店", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 确保服务商提交朋友圈推广开户资料
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "perfect_friends_promote_info_service")
    @ResponseBody
    public JSONObject perfectFriendsPromoteInfoService(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryFriends, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                        "确保服务商提交朋友圈推广开户资料", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 收到服务商提交的朋友圈开户资料并存档, 发起朋友圈授权
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "friends_promote_info_review_service")
    @ResponseBody
    public JSONObject friendsPromoteInfoReviewService(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        String storeId = jykOrderChoiceStoreService.getStoreIdByProcInsId(procInsId);
        ErpStoreAdvertiserFriends friends = erpStoreAdvertiserFriendsService.getByStoreId(storeId);
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        ErpStoreCredentials credentials = null;
        if(StringUtils.isNotBlank(storeInfo.getCredentialsId())){
            credentials = erpStoreCredentialsService.get(storeInfo.getCredentialsId());
        }
        // 判断存档资料有没填完整
        if(null==friends||null==credentials||StringUtils.isBlank(friends.getAccountOriginalId())||StringUtils.isBlank(friends.getStoreScreenshot())
                        ||StringUtils.isBlank(friends.getAdvertiserScreenshot())||StringUtils.isBlank(credentials.getSpecialCertificate()))
        {
            resObject.put("result", false);
            resObject.put("message", "存档资料请补充完善后才能完成当前任务！");
            return resObject;
        }
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryFriends, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                        "收到服务商提交的朋友圈开户资料并存档, 发起朋友圈授权", vars);
        
        /* add by SunQ 2018-4-2 17:56:55 完成后将提审对象的状态更新成通过 */
        /*String storeId = jykOrderChoiceStoreService.getStoreIdByProcInsId(procInsId);
        ErpStoreAdvertiserFriends friends = erpStoreAdvertiserFriendsService.getByStoreId(storeId);*/
        friends.setAuditStatus(4);
        erpStoreAdvertiserFriendsService.save(friends);
        
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
    @RequestMapping(value = "confirmed_friends_authorization_sucess_service")
    @ResponseBody
    public JSONObject confirmedFriendsAuthorizationSucessService(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryFriends, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                        "确认朋友圈授权成功", vars);
        // 是否完成服务商开户
        if(jykFlowJudgeService.isFinishOpenAccountService(procInsId)){
            JykFlow jykFlow = flowService.getByProcInstId(procInsId);
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(jykFlow.getSplitId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            erpHisSplitServiceApi.openAccount(jykFlow.getSplitId(), "", sdf.format(new Date(System.currentTimeMillis())), storeInfo.getId(), storeInfo.getShortName());
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
     * @param isDecide
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "decide_microblog_extension_service")
    @ResponseBody
    public JSONObject decideMicroblogExtensionService(String taskId, String procInsId, String isDecide){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("reviewResultWeibo", "N");
        if("1".equals(isDecide)){
            vars.put("reviewResultWeibo", "Y");
        }
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "判断是否进行微博推广开户", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 确保服务商提交微博推广开户资料
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "perfect_microblog_promote_info_service")
    @ResponseBody
    public JSONObject perfectMicroblogPromoteInfoService(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryWeibo, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                        "确保服务商提交微博推广开户资料", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 微博推广开户资料提审完成并存档
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "perfect_microblog_review_info_service")
    @ResponseBody
    public JSONObject perfectMicroblogReviewInfoService(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        /* 判断存档资料有没填写 */
        String storeid = jykOrderChoiceStoreService.getStoreIdByProcInsId(procInsId);
        ErpStoreAdvertiserWeibo weibo = erpStoreAdvertiserWeiboService.getByStoreId(storeid);
        if(null==weibo){
            resObject.put("result", false);
            resObject.put("message", "存档资料请补充完善后才能完成当前任务！");
            return resObject;
        }
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "微博推广开户资料提审完成并存档", vars);
        // 是否完成服务商开户
        if(jykFlowJudgeService.isFinishOpenAccountService(procInsId)){
            JykFlow jykFlow = flowService.getByProcInstId(procInsId);
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(jykFlow.getSplitId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            erpHisSplitServiceApi.openAccount(jykFlow.getSplitId(), "", sdf.format(new Date(System.currentTimeMillis())), storeInfo.getId(), storeInfo.getShortName());
        }
        
        /* add by SunQ 2018-4-2 17:56:55 完成后将提审对象的状态更新成通过 */
       /* String storeId = jykOrderChoiceStoreService.getStoreIdByProcInsId(procInsId);
        ErpStoreAdvertiserWeibo weibo = erpStoreAdvertiserWeiboService.getByStoreId(storeId);*/
        weibo.setAuditStatus(4);
        erpStoreAdvertiserWeiboService.save(weibo);
        
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
     * @param isDecide
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "decide_momo_extension_service")
    @ResponseBody
    public JSONObject decideMomoExtensionService(String taskId, String procInsId, String isDecide){
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("reviewResultMomo", "N");
        if("1".equals(isDecide)){
            vars.put("reviewResultMomo", "Y");
        }
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "判断是否进行陌陌推广开户", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 确保服务商提交陌陌推广开户资料
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "perfect_momo_promote_info_service")
    @ResponseBody
    public JSONObject perfectMomoPromoteInfoService(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.pipeIndustryMomo, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                        "确保服务商提交陌陌推广开户资料", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 陌陌推广开户资料录入
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "momo_promote_info_in_service")
    @ResponseBody
    public JSONObject momoPromoteInfoInService(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "陌陌推广开户资料录入", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 整合推广素材
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "promote_material_internal_review_service")
    @ResponseBody
    public JSONObject promoteMaterialInternalReviewService(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "整合推广素材", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 陌陌开户资料补充
     *
     * @param taskId
     * @param procInsId
     * @param channelList
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "perfect_momo_account_info_service")
    @ResponseBody
    public JSONObject perfectMomoAccountInfoService(String taskId, String procInsId, String channelList) {
        JSONObject resObject = new JSONObject();
        
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        // 修改子任务完成状态
        this.workFlowService.submitSubTask(procInsId, "1", taskId);
        // 任务完成
        this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "陌陌开户资料补充", vars);
        resObject.put("result", true);
        return resObject;
    }
    
    /**
     * 陌陌推广开户提审
     *
     * @param taskId
     * @param procInsId
     * @param url
     * @param isPass
     * @return
     * @date 2018年1月8日
     * @author SunQ
     */
    @RequestMapping(value = "momo_promote_info_review_service")
    @ResponseBody
    public JSONObject momoPromoteInfoReviewService(String taskId, String procInsId, String url, String isPass) {
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        
        if(StringUtils.isNotBlank(url)){
            // 修改子任务完成状态
            this.workFlowService.submitSubTask(procInsId, "1", taskId);
        }
        if(StringUtils.isNotBlank(isPass)){
            vars.put("reviewResult", "modifyLandingPage");
            if("1".equals(isPass)){
                vars.put("reviewResult", "completed");
            }
            // 修改子任务完成状态
            this.workFlowService.submitSubTask(procInsId, "2", taskId);
        }
        
        if(StringUtils.isNotBlank(url) && StringUtils.isNotBlank(isPass)){
            // 任务完成
            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "陌陌推广开户提审", vars);
            
            /* add by SunQ 2018-4-2 17:56:55 完成后将提审对象的状态更新成通过 */
            String storeId = jykOrderChoiceStoreService.getStoreIdByProcInsId(procInsId);
            ErpStoreAdvertiserMomo momo = erpStoreAdvertiserMomoService.getByStoreId(storeId);
            momo.setAuditStatus(4);
            erpStoreAdvertiserMomoService.save(momo);
            
            // 是否完成服务商开户
            if(jykFlowJudgeService.isFinishOpenAccountService(procInsId)){
                JykFlow jykFlow = flowService.getByProcInstId(procInsId);
                ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                erpHisSplitServiceApi.openAccount(jykFlow.getSplitId(), "", sdf.format(new Date(System.currentTimeMillis())), storeInfo.getId(), storeInfo.getShortName());
            }
        }
        resObject.put("result", true);
        return resObject;
    }
    /* ===============================陌陌 END============================== */
}
