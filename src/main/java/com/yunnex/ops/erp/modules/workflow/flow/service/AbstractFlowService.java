package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.act.service.TaskExtService;
import com.yunnex.ops.erp.modules.holiday.service.ErpHolidaysService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStorePromotePhotoMaterialService;
import com.yunnex.ops.erp.modules.workflow.channel.dao.JykOrderPromotionChannelDao;
import com.yunnex.ops.erp.modules.workflow.channel.entity.JykOrderPromotionChannel;
import com.yunnex.ops.erp.modules.workflow.channel.service.JykOrderPromotionChannelService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.entity.JykFlow;
import com.yunnex.ops.erp.modules.workflow.store.entity.JykOrderChoiceStore;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;

public abstract class AbstractFlowService {

    protected static final String MOMO = "momo";
    protected static final String WEIBO = "weibo";
    protected static final String FRIENDS = "friends";
    protected static final String SURE_PROMOTION_BUTTON = "surePromotionButton";
    protected static final String PROMOTION_TIME = "promotionTime";
    protected static final String STRCONSTANT_NLT = "NLT";
    protected static final String STRCONSTANT_NCT = "NCT";
    protected static final String SURE_QUALIFICATION_BUTTON = "sureQualificationButton";
    protected static final String QUALIFICATION_NEXT_TIME = "qualificationNextTime";
    protected static final String SURE_LICENSE_BUTTON = "sureLicenseButton";
    protected static final String LICENSE_NEXT_TIME = "licenseNextTime";
    protected static final String IN_PENDING_REASON = "inPendingReason";
    protected static final String STRCONSTANT_PT = "PT";
    protected static final String STRCONSTANT_D = "D";
    protected static final String STRCONSTANT_NQT = "NQT";
    protected static final String STRCONSTANT_Q = "Q";
    protected static final String STRCONSTANT_2 = "2";
    protected static final String STRCONSTANT_1 = "1";
    protected static final String STRCONSTANT_3 = "3";
    protected static final String REVIEW_RESULT_WEIBO = "reviewResultWeibo";
    protected static final String BOOLEAN_TRUE = "true";
    protected static final String IS_AUTO_OUT_PENDING = "isAutoOutPending";
    protected static final int INTEGER_0 = 0;
    protected static final String MESSAGE = "message";
    protected static final String RESULT = "result";
    protected static final String BOOLEAN_FALSE = "false";
    protected static final int AUDIT_STATUS_PASSED = 4;// 通过
    protected static final int AUDIT_STATUS_REJECT = 3;// 驳回

    protected static final String ZHANGBEI_IN_SUCESS_ZHIXIAO = "zhangbei_in_sucess_zhixiao";
    protected static final String UPLOAD_PROMOTIONAL_PICTURES_ZHIXIAO = "upload_promotional_pictures_zhixiao";
    protected static final String UPLOAD_PROMOTIONAL_PICTURES_SERVICE = "upload_promotional_pictures_service";
    protected static final String IS_STARTED = "isStarted";
    protected static final String WECHAT_PAYM_QUAL_FLAG = "wechatPaymQualFlag";
    protected static final String ZHANGBEI_ACCOUNT_FLAG = "zhangbeiAccountFlag";

    // 推广通道<陌陌> 是否选中 Y/N
    protected static final String CHECK_MOMO_FLAG = "checkMomoFlag";
    // 推广通道<微博> 是否选中 Y/N
    protected static final String CHECK_MICROBLOG_FLAG = "checkMicroblogFlag";
    // 推广通道<朋友圈> 是否选中 Y/N
    protected static final String CHECK_FRIEND_FLAG = "checkFriendFlag";
    // 是否指定运营顾问
    protected static final String DISTR_CONSULTANT_FLAG = "distrConsultantFlag";

    private static final Logger logger = LoggerFactory.getLogger(AbstractFlowService.class);

    @Autowired
    protected WorkFlowService workFlowService;
    @Autowired
    protected ActTaskService actTaskService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected JykFlowService flowService;
    @Autowired
    protected ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    protected ErpShopInfoService erpShopInfoService;
    @Autowired
    protected ErpStoreInfoService erpStoreInfoService;
    @Autowired
    protected JykOrderChoiceStoreService jykOrderChoiceStoreService;
    @Autowired
    protected JykFlowAccountSignalService jykFlowAccountSignalService;
    @Autowired
    protected TaskExtService taskExtService;
    @Autowired
    protected ErpStorePromotePhotoMaterialService erpStorePromotePhotoMaterialService;
    @Autowired
    protected ErpHisSplitServiceApi erpHisSplitServiceApi;
    @Autowired
    protected ErpHolidaysService erpHolidaysService;
    @Autowired
    protected ErpStoreAdvertiserWeiboService erpStoreAdvertiserWeiboService;
    @Autowired
    protected ErpOrderOperateValueService erpOrderOperateValueService;
    @Autowired
    protected JykOrderPromotionChannelService jykOrderPromotionChannelService;
    @Autowired
    protected JykOrderPromotionChannelDao jykOrderPromotionChannelDao;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected SdiFlowService sdiFlowService;
    @Autowired
    protected ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    protected JykFlowJudgeService jykFlowJudgeService;

    public JykFlowJudgeService getJykFlowJudgeService() {
        return jykFlowJudgeService;
    }

    public void setJykFlowJudgeService(JykFlowJudgeService jykFlowJudgeService) {
        this.jykFlowJudgeService = jykFlowJudgeService;
    }

    public WorkFlowService getWorkFlowService() {
        return workFlowService;
    }

    public void setWorkFlowService(WorkFlowService workFlowService) {
        this.workFlowService = workFlowService;
    }

    public ActTaskService getActTaskService() {
        return actTaskService;
    }

    public void setActTaskService(ActTaskService actTaskService) {
        this.actTaskService = actTaskService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public JykFlowService getFlowService() {
        return flowService;
    }

    public void setFlowService(JykFlowService flowService) {
        this.flowService = flowService;
    }

    public ErpOrderSplitInfoService getErpOrderSplitInfoService() {
        return erpOrderSplitInfoService;
    }

    public void setErpOrderSplitInfoService(ErpOrderSplitInfoService erpOrderSplitInfoService) {
        this.erpOrderSplitInfoService = erpOrderSplitInfoService;
    }

    public ErpShopInfoService getErpShopInfoService() {
        return erpShopInfoService;
    }

    public void setErpShopInfoService(ErpShopInfoService erpShopInfoService) {
        this.erpShopInfoService = erpShopInfoService;
    }

    public ErpStoreInfoService getErpStoreInfoService() {
        return erpStoreInfoService;
    }

    public void setErpStoreInfoService(ErpStoreInfoService erpStoreInfoService) {
        this.erpStoreInfoService = erpStoreInfoService;
    }

    public JykOrderChoiceStoreService getJykOrderChoiceStoreService() {
        return jykOrderChoiceStoreService;
    }

    public void setJykOrderChoiceStoreService(JykOrderChoiceStoreService jykOrderChoiceStoreService) {
        this.jykOrderChoiceStoreService = jykOrderChoiceStoreService;
    }

    public JykFlowAccountSignalService getJykFlowAccountSignalService() {
        return jykFlowAccountSignalService;
    }

    public void setJykFlowAccountSignalService(JykFlowAccountSignalService jykFlowAccountSignalService) {
        this.jykFlowAccountSignalService = jykFlowAccountSignalService;
    }

    public TaskExtService getTaskExtService() {
        return taskExtService;
    }

    public void setTaskExtService(TaskExtService taskExtService) {
        this.taskExtService = taskExtService;
    }

    public ErpStorePromotePhotoMaterialService getErpStorePromotePhotoMaterialService() {
        return erpStorePromotePhotoMaterialService;
    }

    public void setErpStorePromotePhotoMaterialService(ErpStorePromotePhotoMaterialService erpStorePromotePhotoMaterialService) {
        this.erpStorePromotePhotoMaterialService = erpStorePromotePhotoMaterialService;
    }

    public ErpHisSplitServiceApi getErpHisSplitServiceApi() {
        return erpHisSplitServiceApi;
    }

    public void setErpHisSplitServiceApi(ErpHisSplitServiceApi erpHisSplitServiceApi) {
        this.erpHisSplitServiceApi = erpHisSplitServiceApi;
    }

    public ErpHolidaysService getErpHolidaysService() {
        return erpHolidaysService;
    }

    public void setErpHolidaysService(ErpHolidaysService erpHolidaysService) {
        this.erpHolidaysService = erpHolidaysService;
    }



    public ErpStoreAdvertiserWeiboService getErpStoreAdvertiserWeiboService() {
        return erpStoreAdvertiserWeiboService;
    }

    public void setErpStoreAdvertiserWeiboService(ErpStoreAdvertiserWeiboService erpStoreAdvertiserWeiboService) {
        this.erpStoreAdvertiserWeiboService = erpStoreAdvertiserWeiboService;
    }

    public ErpOrderOperateValueService getErpOrderOperateValueService() {
        return erpOrderOperateValueService;
    }

    public void setErpOrderOperateValueService(ErpOrderOperateValueService erpOrderOperateValueService) {
        this.erpOrderOperateValueService = erpOrderOperateValueService;
    }

    public JykOrderPromotionChannelService getJykOrderPromotionChannelService() {
        return jykOrderPromotionChannelService;
    }

    public void setJykOrderPromotionChannelService(JykOrderPromotionChannelService jykOrderPromotionChannelService) {
        this.jykOrderPromotionChannelService = jykOrderPromotionChannelService;
    }

    public JykOrderPromotionChannelDao getJykOrderPromotionChannelDao() {
        return jykOrderPromotionChannelDao;
    }

    public void setJykOrderPromotionChannelDao(JykOrderPromotionChannelDao jykOrderPromotionChannelDao) {
        this.jykOrderPromotionChannelDao = jykOrderPromotionChannelDao;
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public ErpOrderOriginalInfoService getErpOrderOriginalInfoService() {
        return erpOrderOriginalInfoService;
    }

    public void setErpOrderOriginalInfoService(ErpOrderOriginalInfoService erpOrderOriginalInfoService) {
        this.erpOrderOriginalInfoService = erpOrderOriginalInfoService;
    }

    public SdiFlowService getSdiFlowService() {
        return sdiFlowService;
    }

    public void setSdiFlowService(SdiFlowService sdiFlowService) {
        this.sdiFlowService = sdiFlowService;
    }




    protected final String TO_ACTIVATE_ACCOUNT_ZHIXIAO = "to_activate_account_zhixiao";
    
    protected final String WAITING_ADVERTISER_NOTIFY_SHOP = "waiting_advertiser_notify_shop_3.2";
    
    /**
     * 手动激活待生产库订单
     *
     * @param taskId
     * @param split
     * @param pendingProdFlag
     * @param taskExt
     * @date 2018年4月16日
     * @author zjq
     */
    protected void setPendingProdFlagNo(String taskId, ErpOrderSplitInfo split, String pendingProdFlag, TaskExt taskExt) {
        if (Constant.YES.equals(pendingProdFlag) && Constant.NO.equals(taskExt.getPendingProdFlag())) {
            split.setActivationTime(new Date());
            // 手动移出待生产库
            this.workFlowService.setVariable(taskId, IS_AUTO_OUT_PENDING, BOOLEAN_FALSE);
        }
    }

    /**
     * 保存推广通道信息 ,并设置流程变量
     *
     * @param procInsId
     * @param channels
     * @param channelVars
     * @date 2018年4月10日
     * @author zjq
     */
    protected void saveChannelAndSetProcessVars(String taskId, String procInsId, String[] channels, ErpOrderSplitInfo split, ErpStoreInfo storeInfo) {

        if (null != channels) {

            logger.info("保存推广通道信息==start==,分单{},任务id{},流程id{},选择渠道信息{}", split, taskId, procInsId, channels);

            ErpOrderSplitInfo splitInfo = erpOrderSplitInfoService.getByProsIncId(procInsId);
            JykOrderPromotionChannel jykOrderPromotionChannel = null;
            // 删除之前保存的数据
            jykOrderPromotionChannelDao.deleteChannels(splitInfo.getId());
            // 初始化流程变量
            this.workFlowService.setVariable(taskId, CHECK_FRIEND_FLAG, Constant.NO);
            this.workFlowService.setVariable(taskId, CHECK_MICROBLOG_FLAG, Constant.NO);
            this.workFlowService.setVariable(taskId, CHECK_MOMO_FLAG, Constant.NO);

            // 选择推广渠道 默认没有推广
            this.workFlowService.setVariable(taskId, JykFlowConstants.CHOOSE_FRIEND_FLAG, Constant.NO);
            this.workFlowService.setVariable(taskId, JykFlowConstants.CHOOSE_MICROBLOG_FLAG, Constant.NO);
            this.workFlowService.setVariable(taskId, JykFlowConstants.CHOOSE_MOMO_FLAG, Constant.NO);

            // 门店是否有推广历 史，默认没有推广
            this.workFlowService.setVariable(taskId, JykFlowConstants.STORE_FRIENDS_HISFLAG, Constant.NO);
            this.workFlowService.setVariable(taskId, JykFlowConstants.STORE_MICROBLOG_HISFLAG, Constant.NO);
            this.workFlowService.setVariable(taskId, JykFlowConstants.STORE_MOMO_HISFLAG, Constant.NO);
            // 商户是否有推广历史 ，默认没有推广
            this.workFlowService.setVariable(taskId, JykFlowConstants.SHOP_FRIENDS_HISFLAG, Constant.NO);
            this.workFlowService.setVariable(taskId, JykFlowConstants.SHOP_MICROBLOG_HISFLAG, Constant.NO);
            this.workFlowService.setVariable(taskId, JykFlowConstants.SHOP_MOMO_HISFLAG, Constant.NO);

            // 确认门店是否有推广历史
            if (STRCONSTANT_1.equalsIgnoreCase(storeInfo.getFriendExtension())) {
                this.workFlowService.setVariable(taskId, JykFlowConstants.STORE_FRIENDS_HISFLAG, Constant.YES);
            }
            if (STRCONSTANT_1.equalsIgnoreCase(storeInfo.getWeiboExtension())) {
                this.workFlowService.setVariable(taskId, JykFlowConstants.STORE_MICROBLOG_HISFLAG, Constant.YES);
            }
            if (STRCONSTANT_1.equalsIgnoreCase(storeInfo.getMomoExtension())) {
                this.workFlowService.setVariable(taskId, JykFlowConstants.STORE_MOMO_HISFLAG, Constant.YES);
            }

            ErpOrderOriginalInfo order = erpOrderOriginalInfoService.get(splitInfo.getOrderId());
            ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiID(order.getShopId());
            List<ErpStoreInfo> stores = erpStoreInfoService.findAllListWhereShopId("0", shopInfo.getId());


            // 确认商户是否有推广历史
            for (ErpStoreInfo store : stores) {
                if (STRCONSTANT_1.equals(store.getFriendExtension())) {
                    this.workFlowService.setVariable(taskId, JykFlowConstants.SHOP_FRIENDS_HISFLAG, Constant.YES);
                }
                if (STRCONSTANT_1.equals(store.getWeiboExtension())) {
                    this.workFlowService.setVariable(taskId, JykFlowConstants.SHOP_MICROBLOG_HISFLAG, Constant.YES);
                }
                if (STRCONSTANT_1.equals(store.getMomoExtension())) {
                    this.workFlowService.setVariable(taskId, JykFlowConstants.SHOP_MOMO_HISFLAG, Constant.YES);
                }
            }


            // 确认选择的推广渠道信息
            for (String channel : channels) {

                if (Constant.CHANNEL_1.equalsIgnoreCase(channel)) {
                    this.workFlowService.setVariable(taskId, JykFlowConstants.CHOOSE_FRIEND_FLAG, Constant.YES);
                    this.workFlowService.setVariable(taskId, CHECK_FRIEND_FLAG, Constant.YES);
                    // 输出卡券使用
                    this.workFlowService.setVariable(taskId, FRIENDS, STRCONSTANT_1);
                    storeInfo.setFriendExtension(STRCONSTANT_1);
                }

                if (Constant.CHANNEL_2.equalsIgnoreCase(channel)) {
                    this.workFlowService.setVariable(taskId, JykFlowConstants.CHOOSE_MICROBLOG_FLAG, Constant.YES);
                    this.workFlowService.setVariable(taskId, CHECK_MICROBLOG_FLAG, Constant.YES);
                    // 输出卡券使用
                    this.workFlowService.setVariable(taskId, WEIBO, STRCONSTANT_2);
                    storeInfo.setWeiboExtension(STRCONSTANT_1);
                }

                if (Constant.CHANNEL_3.equalsIgnoreCase(channel)) {
                    this.workFlowService.setVariable(taskId, JykFlowConstants.CHOOSE_MOMO_FLAG, Constant.YES);
                    this.workFlowService.setVariable(taskId, CHECK_MOMO_FLAG, Constant.YES);
                    // 输出卡券使用
                    this.workFlowService.setVariable(taskId, MOMO, STRCONSTANT_3);
                    storeInfo.setMomoExtension(STRCONSTANT_1);
                }

                jykOrderPromotionChannel = new JykOrderPromotionChannel();
                jykOrderPromotionChannel.setOrderId(splitInfo.getOrderId());
                jykOrderPromotionChannel.setSplitId(splitInfo.getId());
                jykOrderPromotionChannel.setPromotionChannel(channel);
                jykOrderPromotionChannelService.save(jykOrderPromotionChannel);

            }

            logger.info("保存推广通道信息==end==,分单{},任务id{},流程id{},选择渠道信息{}", split, taskId, procInsId, channels);
        }
    }

    /**
     * 保存门店和推广渠道
     *
     * @param taskId
     * @param procInsId
     * @param storeId
     * @param channels
     * @param split
     * @date 2018年4月13日
     * @author zjq
     */
    protected ErpStoreInfo saveStroeAndChannel(String taskId, String procInsId, String storeId, String[] channels, ErpOrderSplitInfo split) {

        logger.info("保存门店和推广渠道[saveStroeAndChannel]=start=,current{},taskId{},procInsId{},storeId{},channels{}", System.currentTimeMillis(), taskId,
                        procInsId, storeId, channels);

        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);

        if (null == storeInfo) {
            logger.info("保存门店和推广渠道[saveStroeAndChannel]资质不全=start=,current{},taskId{},procInsId{},storeId{},channels{}", System.currentTimeMillis(),
                            taskId, procInsId, storeId, channels);
            return null;
        }
        // 保存门店推广标识
        saveStoreInfo(procInsId, storeId, storeInfo);
        // 保存推广通道信息，设置流程变量 ,更新门店推广渠道标识
        saveChannelAndSetProcessVars(taskId, procInsId, channels, split, storeInfo);

        logger.info("保存门店和推广渠道[saveStroeAndChannel]=end=,storeInfo{},current{}", storeInfo, System.currentTimeMillis());

        return storeInfo;
    }


    /**
     * 保存门店信息
     *
     * @param procInsId
     * @param storeId
     * @date 2018年4月11日
     * @author zjq
     */
    protected void saveStoreInfo(String procInsId, String storeId, ErpStoreInfo storeInfo) {

        if (!StringUtils.isBlank(storeId)) {
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

            storeInfo.setStoreExtension(STRCONSTANT_1);
        }
    }



    protected Double confirmPromotionTime(String taskId, String extensionExpect, String promotionTime, ErpOrderSplitInfo split, TaskExt taskExt) {
        Double distanceDays = 0D;
        if (StringUtils.isNotBlank(extensionExpect) && STRCONSTANT_1.equals(extensionExpect) && StringUtils.isNotBlank(promotionTime)) {
            split.setPromotionTime(DateUtils.parseDate(promotionTime));
            // 超过20工作日要进入待生产库
            try {
                distanceDays = DateUtils.getDistanceOfTwoDate(
                                this.erpHolidaysService.enddate(new Date(), JykFlowConstants.PLANNING_DATE_DISTINCT * 8), split.getPromotionTime());
            } catch (ParseException e) {
                logger.info("日期解析出错！", e);
            }
            if (distanceDays.intValue() > 0) {
                split.setPendingReason(STRCONSTANT_D);
                split.setTimeoutFlag(Constant.NO);
                split.setPendingProduced(Constant.YES);
                split.setPendingProdFlag(Constant.YES);
                taskExt.setPendingProdFlag(Constant.YES);
                this.workFlowService.setVariable(taskId, IN_PENDING_REASON, STRCONSTANT_PT);
            }
            this.workFlowService.setVariable(taskId, PROMOTION_TIME, DateUtils.formatDateTime(DateUtils.parseDate(promotionTime)));
            this.workFlowService.setVariable(taskId, SURE_PROMOTION_BUTTON, STRCONSTANT_1);
        }
        return distanceDays;
    }

    protected void validateNextLicenseTime(String taskId, String license, String nextLicenseTime, ErpOrderSplitInfo split, TaskExt taskExt) {
        if (StringUtils.isNotBlank(license) && STRCONSTANT_2.equals(license) && StringUtils.isNotBlank(nextLicenseTime)) {
            split.setNextLicenseTime(DateUtils.parseDate(nextLicenseTime));
            split.setPendingReason(STRCONSTANT_Q);
            split.setTimeoutFlag(Constant.NO);
            split.setPendingProduced(Constant.YES);
            split.setPendingProdFlag(Constant.YES);
            taskExt.setPendingProdFlag(Constant.YES);
            this.workFlowService.setVariable(taskId, IN_PENDING_REASON, STRCONSTANT_NLT);
            this.workFlowService.setVariable(taskId, LICENSE_NEXT_TIME, DateUtils.formatDateTime(DateUtils.parseDate(nextLicenseTime)));
            this.workFlowService.setVariable(taskId, SURE_LICENSE_BUTTON, STRCONSTANT_2);
        } else if ("1".equals(license)) {
            this.workFlowService.setVariable(taskId, SURE_LICENSE_BUTTON, STRCONSTANT_1);
        }
    }

    protected void validateNextQualificationTime(String taskId, String storeId, String qualification, String nextQualificationTime,
                    ErpOrderSplitInfo split, TaskExt taskExt) {
        if (StringUtils.isNotBlank(qualification) && STRCONSTANT_2.equals(qualification) && StringUtils.isNotBlank(nextQualificationTime)) {
            Date date = split.getNextQualificationTime();
            if (null == date) {
                // 确认投放门店资质齐全
                erpHisSplitServiceApi.qualification(split.getId(), qualification, storeId, StringUtils.EMPTY);
            }
            split.setNextQualificationTime(DateUtils.parseDate(nextQualificationTime));
            split.setPendingReason(STRCONSTANT_Q);
            split.setTimeoutFlag(Constant.NO);
            split.setPendingProduced(Constant.YES);
            split.setPendingProdFlag(Constant.YES);
            taskExt.setPendingProdFlag(Constant.YES);
            this.workFlowService.setVariable(taskId, IN_PENDING_REASON, STRCONSTANT_NQT);
            this.workFlowService.setVariable(taskId, QUALIFICATION_NEXT_TIME, DateUtils.formatDateTime(DateUtils.parseDate(nextQualificationTime)));
            this.workFlowService.setVariable(taskId, SURE_QUALIFICATION_BUTTON, STRCONSTANT_2);
        } else if ("1".equals(qualification)) {
            this.workFlowService.setVariable(taskId, SURE_QUALIFICATION_BUTTON, STRCONSTANT_1);
        }
    }

    protected void validateNextExtensionExpectTime(String taskId, String extensionExpect, String nextExtensionExpectTime, ErpOrderSplitInfo split,
                    TaskExt taskExt) {
        if (StringUtils.isNotBlank(extensionExpect) && STRCONSTANT_2.equals(extensionExpect) && StringUtils.isNotBlank(nextExtensionExpectTime)) {
            split.setNextContactTime(DateUtils.parseDate(nextExtensionExpectTime));
            split.setPendingReason(STRCONSTANT_D);
            split.setTimeoutFlag(Constant.NO);
            split.setPendingProduced(Constant.YES);
            split.setPendingProdFlag(Constant.YES);
            taskExt.setPendingProdFlag(Constant.YES);
            this.workFlowService.setVariable(taskId, IN_PENDING_REASON, STRCONSTANT_NCT);
            this.workFlowService.setVariable(taskId, "promotionNextTime", DateUtils.formatDateTime(DateUtils.parseDate(nextExtensionExpectTime)));
            this.workFlowService.setVariable(taskId, SURE_PROMOTION_BUTTON, STRCONSTANT_2);
        }
    }

    protected boolean isNotEmptyArray(String[] array) {
        if (null == array)
            return false;
        if (array.length == 0)
            return false;
        return true;
    }

}
