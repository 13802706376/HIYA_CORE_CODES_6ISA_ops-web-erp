package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.modules.act.service.ActTaskService;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisFormValidateService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
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
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.workflow.channel.constant.Constants;
import com.yunnex.ops.erp.modules.workflow.channel.entity.JykOrderPromotionChannel;
import com.yunnex.ops.erp.modules.workflow.channel.service.JykOrderPromotionChannelService;
import com.yunnex.ops.erp.modules.workflow.effect.entity.JykDeliveryEffectInfo;
import com.yunnex.ops.erp.modules.workflow.effect.service.JykDeliveryEffectInfoService;
import com.yunnex.ops.erp.modules.workflow.flow.common.JykFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpFlowForm;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderInputDetail;
import com.yunnex.ops.erp.modules.workflow.flow.entity.JykFlow;
import com.yunnex.ops.erp.modules.workflow.store.service.JykOrderChoiceStoreService;
import com.yunnex.ops.erp.modules.workflow.user.service.ErpOrderFlowUserService;


@Service
public class JykFlowDiagnosis3p2Service {


    public static final String WORK_PROMOTION_SCHEME_PREVIEW_3_2 = "work_promotion_scheme_preview_3.2";
    public static final String QRCODE = "qrcode";
    private static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String PROMOTION_PLAN_REVIEW_3_2 = "promotion_plan_review_3.2";
    public static final String MOMOCONFIRM = "momoConfirm";
    public static final String WEIBOCONFIRM = "weiboConfirm";
    public static final String FRIENDCONFIRM = "friendConfirm";
    public static final String PROMOTION_PLAN_PREVIEW_CONFIRMATION_3_2 = "promotion_plan_preview_confirmation_3.2";
    public static final String INNER_IMG_MOMO = "innerImgMomo";
    public static final String OUTER_IMG_MOMO = "outerImgMomo";
    public static final String INNER_IMG_WEIBO = "innerImgWeibo";
    public static final String OUTER_IMG_WEIBO = "outerImgWeibo";
    public static final String INNER_IMG_FRIENDS = "innerImgFriends";
    public static final String OUTER_IMG_FRIENDS = "outerImgFriends";
    public static final String REVIEW_DESIGN_DRAFT_3_2 = "review_design_draft_3.2";
    public static final String DRAFT_FAIL_REASON = "draftFailReason";
    public static final String FRIENDS_OFFICAL = "朋友圈设计稿";
    private static final String PICK_ID_FRIENDS = "pickIdFriends";
    private static final String MOMO_OFFICAL = "陌陌设计稿";
    public static final String PICK_ID_MOMO = "pickIdMomo";
    private static final String WEIBO_OFFICAL = "微博设计稿";
    public static final String PICK_ID_WEIBO = "pickIdWeibo";
    public static final String MODIFY_OFFICIAL_DOCUMENTS_3_2 = "modify_official_documents_3.2";
    public static final String OFFICALMODIFYCHECKVALUE = "officalmodifycheckvalue";
    public static final String OUTPUT_OFFICIAL_DOCUMENTS_3_2 = "output_official_documents_3.2";
    public static final String OFFICALCHECKVALUE = "officalcheckvalue";
    private static final String innerImgMomo_str = "陌陌落地页截图";
    private static final String outerImgMomo_str = "陌陌外层入口截图";
    private static final String innerImgWeibo_str = "微博落地页截图";
    private static final String outerImgWeibo_str = "微博外层入口截图";
    private static final String innerImgFriends_str = "朋友圈落地页截图";
    private static final String outerImgFriends_str = "朋友圈外层入口截图";
    private static final String OUTPUT_DESIGN_DRAFT_3_2 = "output_design_draft_3.2";
    public static final String PROMOTION_PROPOSAL_CONFIRMATION_3_2 = "promotion_proposal_confirmation_3.2";
    private static final Logger logger = LoggerFactory.getLogger(JykFlowDiagnosis3p2Service.class);
    @Autowired
    private ErpHisSplitServiceApi erpHisSplitServiceApi;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private JykOrderChoiceStoreService jykOrderChoiceStoreService;
    @Autowired
    private ErpOrderFlowUserService erpOrderFlowUserService;
    @Autowired
    private JykOrderPromotionChannelService jykOrderPromotionChannelService;
    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    private DiagnosisFormValidateService formValidateService;
    @Autowired
    private ErpOrderInputDetailService erpOrderInputDetailService;
    @Autowired
    private ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    private ErpOrderFileService erpOrderFileService;
    @Autowired
    private ErpStorePromotePhotoMaterialService erpStorePromotePhotoMaterialService;

    @Autowired
    private ErpFlowFormService erpFlowFormService;
    @Autowired
    private JykFlowService flowService;
    @Autowired
    private JykDeliveryEffectInfoService jykDeliveryEffectInfoService;
    @Autowired
    private ErpStoreAdvertiserFriendsService erpStoreAdvertiserFriendsService;
    @Autowired
    private ErpStoreAdvertiserMomoService erpStoreAdvertiserMomoService;
    @Autowired
    private ErpStoreAdvertiserWeiboService erpStoreAdvertiserWeiboService;
    @Autowired
    @Lazy(true)
    private WorkFlowService workFlowService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private JykFlowAccountSignalService jykFlowAccountSignalService;
    public void modifyPromotionProposalV3p2(String taskId, String procInsId, String splitId) {


    }

    /**
     * 推广提案发给商户确认
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @return
     * @date 2018年5月7日
     * @author zjq
     */
    public JSONObject promotionProposalConfirmationV3p2(String taskId, String procInsId, String splitId, String proposalConfirm) {

        logger.info("推广提案发给商户确认start=== taskid[{}],procInsId[{}],splitId[{}]", taskId, procInsId, splitId);

        JSONObject resObject = new JSONObject();

        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoService.get(splitId);

        if (null != erpOrderSplitInfo && erpOrderSplitInfo.getPublishToWxapp().intValue() > 0) {

            Map<String, Object> vars = Maps.newHashMap();
            String textDesignInterfacePerson = this.erpOrderFlowUserService
                            .findListByFlowId(procInsId, JykFlowConstants.assignTextDesignInterfacePerson).getUser().getId();
            vars.put("textDesignInterfacePerson", textDesignInterfacePerson);
            workFlowService.completeFlow(JykFlowConstants.assignTextDesignInterfacePerson, taskId, procInsId, "完成推广提案发给商户确认", vars);

            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);

        } else {
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        }

        logger.info("推广提案发给商户确认end=== taskid[{}],procInsId[{}],splitId[{}],JSONObject[{}]", taskId, procInsId, splitId, resObject);

        return resObject;
    }
    
    /**
     * 输出文案
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param editorWeibo
     * @param editorMomo
     * @param editorFriend
     * @return
     * @date 2018年5月8日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject outputOfficialDocumentsV3p2(String taskId, String procInsId, String splitId, String editorWeibo, String editorMomo,
                    String editorFriend, String checkvalue) {
        JSONObject resObject = new JSONObject();
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        // 保存微博文案信息
        saveOfficialWeibo(taskId, procInsId, splitId, editorWeibo);

        // 保存陌陌文案信息
        saveOfficialMomo(taskId, procInsId, splitId, editorMomo);

        // 保存朋友圈文案信息
        saveOfficialFriend(taskId, procInsId, splitId, editorFriend);
        
        saveFlowForm(taskId, splitId, procInsId, OFFICALCHECKVALUE, checkvalue, OUTPUT_OFFICIAL_DOCUMENTS_3_2, ErpFlowForm.NORMAL);

        boolean isFinish = validateFlowFinish(splitId, editorWeibo, editorMomo, editorFriend);

        if (isFinish && Constant.YES.equalsIgnoreCase(checkvalue)) {
            this.workFlowService.completeFlow(JykFlowConstants.assignTextDesignInterfacePerson, taskId, procInsId, "完成输出文案", null);
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        }

        return resObject;
    }


    public Set<String> getImgStr(String htmlStr) {
        Set<String> pics = new HashSet<String>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*title\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("title\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

    private boolean validateFlowFinish(String splitId, String editorWeibo, String editorMomo, String editorFriend) {
        boolean isFinish = true;

        List<Integer> channels = jykOrderPromotionChannelService.getChannels(splitId);

        for (Integer channel : channels) {
            // 朋友圈
            if (Constant.CHANNEL_1.equalsIgnoreCase(channel.toString()) && StringUtils.isEmpty(editorFriend)) {
                isFinish = false;
                break;
            }
            // 微博
            if (Constant.CHANNEL_2.equalsIgnoreCase(channel.toString()) && StringUtils.isEmpty(editorWeibo)) {
                isFinish = false;
                break;
            }
            // 陌陌
            if (Constant.CHANNEL_3.equalsIgnoreCase(channel.toString()) && StringUtils.isEmpty(editorMomo)) {
                isFinish = false;
                break;
            }
        }
        return isFinish;
    }

    /**
     * 修改文案
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param editorWeibo
     * @param editorMomo
     * @param editorFriend
     * @return
     * @date 2018年5月8日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject modifyOfficialDocumentsV3p2(String taskId, String procInsId, String splitId, String editorWeibo, String editorMomo,
                    String editorFriend, String checkvalue) {
        JSONObject resObject = new JSONObject();
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
        // 保存微博文案信息
        if (StringUtils.isNoneBlank(editorWeibo)) {
            saveOfficialWeibo(taskId, procInsId, splitId, editorWeibo);
        }

        // 保存陌陌文案信息
        if (StringUtils.isNoneBlank(editorMomo)) {
            saveOfficialMomo(taskId, procInsId, splitId, editorMomo);
        }

        // 保存朋友圈文案信息
        if (StringUtils.isNoneBlank(editorFriend)) {
            saveOfficialFriend(taskId, procInsId, splitId, editorFriend);
        }

        saveFlowForm(taskId, splitId, procInsId, OFFICALMODIFYCHECKVALUE, checkvalue, MODIFY_OFFICIAL_DOCUMENTS_3_2, ErpFlowForm.NORMAL);

        boolean isFinish = validateFlowFinish(splitId, editorWeibo, editorMomo, editorFriend);

        if (isFinish && Constant.YES.equalsIgnoreCase(checkvalue)) {
            this.workFlowService.completeFlow(JykFlowConstants.assignTextDesignInterfacePerson, taskId, procInsId, "完成修改文案", null);

        }
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }


    private void saveOfficialFriend(String taskId, String procInsId, String splitId, String editorFriend) {
        // 保存到流程表单表
        ErpFlowForm erpFlowForm = new ErpFlowForm();

        String htmlStr = base64Decode(editorFriend);

        erpFlowForm.setBusId(splitId);
        erpFlowForm.setTaskId(taskId);
        erpFlowForm.setProcInsId(procInsId);
        erpFlowForm.setTaskDef(OUTPUT_OFFICIAL_DOCUMENTS_3_2);
        erpFlowForm.setFormAttrName("editorFriend");
        erpFlowForm.setFormAttrType(ErpFlowForm.TEXT);
        erpFlowForm.setFormTextValue(htmlStr);
        erpFlowFormService.saveErpFlowForm(erpFlowForm);

        // 保存业务表
        ErpOrderInputDetail erpOrderInputDetail = erpOrderInputDetailService.findListByInputKey(splitId, "editorFriend");
        if (null == erpOrderInputDetail) {
            erpOrderInputDetail = new ErpOrderInputDetail();
        }

        erpOrderInputDetail.setSplitId(splitId);
        erpOrderInputDetail.setInputTaskName("朋友圈推广文案");
        erpOrderInputDetail.setInputDetail(htmlStr);
        erpOrderInputDetail.setInputTaskKey("editorFriend");

        erpOrderInputDetailService.save(erpOrderInputDetail);
        // 保存文案图片
        Set<String> ids = getImgStr(htmlStr);

        erpOrderFileService.deleteByProcInsId("朋友圈推广文案附件", procInsId);

        updateFilesDelFlag(ids);

    }

    private String base64Decode(String editorFriend) {

        if (StringUtils.isEmpty(editorFriend))
            return Constant.BLANK;;

        try {
            return URLDecoder.decode(editorFriend, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage(), e);
            return Constant.BLANK;
        }
    }

    private void saveOfficialMomo(String taskId, String procInsId, String splitId, String editorMomo) {

        ErpFlowForm erpFlowForm = new ErpFlowForm();

        String htmlStr = base64Decode(editorMomo);

        erpFlowForm.setBusId(splitId);
        erpFlowForm.setTaskId(taskId);
        erpFlowForm.setProcInsId(procInsId);
        erpFlowForm.setTaskDef(OUTPUT_OFFICIAL_DOCUMENTS_3_2);
        erpFlowForm.setFormAttrName("editorMomo");
        erpFlowForm.setFormAttrType(ErpFlowForm.TEXT);
        erpFlowForm.setFormTextValue(htmlStr);

        erpFlowFormService.saveErpFlowForm(erpFlowForm);

        ErpOrderInputDetail erpOrderInputDetail = erpOrderInputDetailService.findListByInputKey(splitId, "editorMomo");

        if (null == erpOrderInputDetail) {
            erpOrderInputDetail = new ErpOrderInputDetail();
        }
        erpOrderInputDetail.setSplitId(splitId);
        erpOrderInputDetail.setInputTaskName("陌陌推广文案");
        erpOrderInputDetail.setInputDetail(htmlStr);
        erpOrderInputDetail.setInputTaskKey("editorMomo");

        erpOrderInputDetailService.save(erpOrderInputDetail);

        // 保存文案图片
        Set<String> ids = getImgStr(htmlStr);

        erpOrderFileService.deleteByProcInsId("陌陌推广文案附件", procInsId);

        updateFilesDelFlag(ids);

    }

    private void saveOfficialWeibo(String taskId, String procInsId, String splitId, String editorWeibo) {

        ErpFlowForm erpFlowForm = new ErpFlowForm();

        String htmlStr = base64Decode(editorWeibo);

        erpFlowForm.setBusId(splitId);
        erpFlowForm.setTaskId(taskId);
        erpFlowForm.setProcInsId(procInsId);
        erpFlowForm.setTaskDef(OUTPUT_OFFICIAL_DOCUMENTS_3_2);
        erpFlowForm.setFormAttrName("editorWeibo");
        erpFlowForm.setFormAttrType(ErpFlowForm.TEXT);
        erpFlowForm.setFormTextValue(htmlStr);

        erpFlowFormService.saveErpFlowForm(erpFlowForm);

        ErpOrderInputDetail erpOrderInputDetail = erpOrderInputDetailService.findListByInputKey(splitId, "editorWeibo");

        if (null == erpOrderInputDetail) {
            erpOrderInputDetail = new ErpOrderInputDetail();
        }
        erpOrderInputDetail.setSplitId(splitId);
        erpOrderInputDetail.setInputTaskName("微博推广文案");
        erpOrderInputDetail.setInputDetail(htmlStr);
        erpOrderInputDetail.setInputTaskKey("editorWeibo");

        erpOrderInputDetailService.save(erpOrderInputDetail);

        // 保存文案图片
        Set<String> ids = getImgStr(htmlStr);

        erpOrderFileService.deleteByProcInsId("微博推广文案附件", procInsId);

        updateFilesDelFlag(ids);

    }

    /**
     * 更新文件为正常
     * 
     *
     * @param ids
     * @date 2018年5月14日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public void updateFilesDelFlag(Set<String> ids) {
        for (String id : ids) {
            ErpOrderFile erpOrderFile = erpOrderFileService.get(id);
            if (null != erpOrderFile) {
                erpOrderFile.setDelFlag(FlowConstant.STRING_0);
                erpOrderFileService.save(erpOrderFile);
            }
        }
    }

    /**
     * 输出设计稿
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param pickIdFriends
     * @param pickIdWeibo
     * @param pickIdMomo
     * @return
     * @date 2018年5月8日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject outputDesignDraftV3p2(String taskId, String procInsId, String splitId, String[] pickIdFriends, String[] pickIdWeibo,
                    String[] pickIdMomo, String momoLink) {
        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        // 陌陌落地页链接
        saveMomoLink(taskId, splitId, momoLink, procInsId);

        // 保存微博设计稿
        if (!isEmptyArrays(pickIdWeibo)) {
            saveDesignWeibo(taskId, procInsId, splitId, pickIdWeibo);
        } else {
            deleteFLowFormByAttrName(taskId, PICK_ID_WEIBO);
            erpOrderFileService.deleteByProcInsId(WEIBO_OFFICAL, procInsId);
        }

        // 保存陌陌设计稿
        if (!isEmptyArrays(pickIdMomo)) {
            saveDesignMomo(taskId, procInsId, splitId, pickIdMomo);
        } else {
            deleteFLowFormByAttrName(taskId, PICK_ID_MOMO);
            erpOrderFileService.deleteByProcInsId(MOMO_OFFICAL, procInsId);
        }
        // 保存朋友圈设计稿
        if (!isEmptyArrays(pickIdFriends)) {
            saveDesignFriend(taskId, procInsId, splitId, pickIdFriends);
        } else {
            deleteFLowFormByAttrName(taskId, PICK_ID_FRIENDS);
            erpOrderFileService.deleteByProcInsId(FRIENDS_OFFICAL, procInsId);
        }

        boolean isFinish = validateFlowFinish(splitId, pickIdWeibo, pickIdMomo, pickIdFriends);

        if (isFinish) {

            Task task = workFlowService.getTaskById(taskId);
            if (!task.getProcessVariables().containsKey("UploadPictureMaterial")) {
                resObject.put(FlowConstant.MESSAGE, "未上传图片推广素材，无法提交 当前任务");
                resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
                return resObject;
            }
            ErpOrderSplitInfo orderSplitInfo = erpOrderSplitInfoService.get(splitId);

            List<User> userListMomo = systemService.getUserByRoleName(Constant.DESIGNER_INTERFACE_PERSON);
            if (!CollectionUtils.isEmpty(userListMomo)) {
                // 保存流程角色信息(业管-陌陌开户)
                erpOrderFlowUserService.insertOrderFlowUser(userListMomo.get(0).getId(), orderSplitInfo.getOrderId(), splitId,
                                JykFlowConstants.designerInterfacePerson, procInsId);
                vars.put(JykFlowConstants.designerInterfacePerson, userListMomo.get(0).getId());
            }
            this.workFlowService.completeFlow(JykFlowConstants.designerInterfacePerson, taskId, procInsId, "完成输出文案", vars);
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        }

        return resObject;
    }



    /**
     * 修改设计稿
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param pickIdFriends
     * @param pickIdWeibo
     * @param pickIdMomo
     * @return
     * @date 2018年5月8日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject modifyDesignDraftV3p2(String taskId, String procInsId, String splitId, String[] pickIdFriends, String[] pickIdWeibo,
                    String[] pickIdMomo, String momoLink) {
        JSONObject resObject = new JSONObject();
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
        // 陌陌落地页链接
        saveMomoLink(taskId, splitId, momoLink, procInsId);

        // 保存微博设计稿
        if (!isEmptyArrays(pickIdWeibo)) {
            saveDesignWeibo(taskId, procInsId, splitId, pickIdWeibo);
        } else {
            deleteFLowFormByAttrName(taskId, PICK_ID_WEIBO);
            erpOrderFileService.deleteByProcInsId(WEIBO_OFFICAL, procInsId);
        }

        // 保存陌陌设计稿
        if (!isEmptyArrays(pickIdMomo)) {
            saveDesignMomo(taskId, procInsId, splitId, pickIdMomo);
        } else {
            deleteFLowFormByAttrName(taskId, PICK_ID_MOMO);
            erpOrderFileService.deleteByProcInsId(MOMO_OFFICAL, procInsId);
        }
        // 保存朋友圈设计稿
        if (!isEmptyArrays(pickIdFriends)) {
            saveDesignFriend(taskId, procInsId, splitId, pickIdFriends);
        } else {
            deleteFLowFormByAttrName(taskId, PICK_ID_FRIENDS);
            erpOrderFileService.deleteByProcInsId(FRIENDS_OFFICAL, procInsId);
        }
        boolean isFinish = validateFlowFinish(splitId, pickIdFriends, pickIdWeibo, pickIdMomo);

        if (isFinish) {
            this.workFlowService.completeFlow(JykFlowConstants.designerInterfacePerson, taskId, procInsId, "完成修改文案", null);
            resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        }
        return resObject;
    }

    private boolean validateFlowFinish(String splitId, String[] pickIdFriends, String[] pickIdWeibo, String[] pickIdMomo) {

        boolean isFinish = true;
        List<Integer> channels = jykOrderPromotionChannelService.getChannels(splitId);

        for (Integer channel : channels) {
            // 朋友圈
            if (Constant.CHANNEL_1.equalsIgnoreCase(channel.toString()) && isEmptyArrays(pickIdFriends)) {
                isFinish = false;
                break;
            }
            // 微博
            if (Constant.CHANNEL_2.equalsIgnoreCase(channel.toString()) && isEmptyArrays(pickIdWeibo)) {
                isFinish = false;
                break;
            }
            // 陌陌
            if (Constant.CHANNEL_3.equalsIgnoreCase(channel.toString()) && isEmptyArrays(pickIdMomo)) {
                isFinish = false;
                break;
            }
        }
        return isFinish;
    }

    /**
     * 审核设计稿
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param pickIdFriends
     * @param pickIdWeibo
     * @param pickIdMomo
     * @return
     * @date 2018年5月8日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject reviewDesignDrafV3p2(String taskId, String procInsId, String splitId, String checkvalue, String reason) {
        JSONObject resObject = new JSONObject();
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
        if (Constant.NO.equalsIgnoreCase(checkvalue)) {
            saveFlowForm(taskId, splitId, procInsId, DRAFT_FAIL_REASON, reason, REVIEW_DESIGN_DRAFT_3_2, ErpFlowForm.TEXT);
        }
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("reviewResult", checkvalue);
        this.workFlowService.completeFlow(JykFlowConstants.designer, taskId, procInsId, "完成审核设计稿", vars);
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }



    @Transactional(readOnly = false)
    public void deleteFLowFormByAttrName(String taskId, String formAttrName) {

        erpFlowFormService.deleteByFormAttrName(taskId, formAttrName);
    }



    @Transactional(readOnly = false)
    public void saveDesignWeibo(String taskId, String procInsId, String splitId, String[] pickIdWeibo) {

        ErpFlowForm erpFlowForm = new ErpFlowForm();

        erpFlowForm.setBusId(splitId);
        erpFlowForm.setTaskId(taskId);
        erpFlowForm.setProcInsId(procInsId);
        erpFlowForm.setTaskDef(OUTPUT_DESIGN_DRAFT_3_2);
        erpFlowForm.setFormAttrType(ErpFlowForm.FILE);
        erpFlowForm.setFormAttrName(PICK_ID_WEIBO);



        this.erpOrderFileService.deleteByProcInsId(WEIBO_OFFICAL, procInsId);

        for (String id : pickIdWeibo) {
            // 更新当前的文件为正式文件
            updateFileDeleteFlag(id);
            erpFlowForm.setFormAttrValue(erpFlowForm.getFormAttrValue() + id + Constant.SEMICOLON);
        }


        erpFlowFormService.saveErpFlowForm(erpFlowForm);
    }

    private void updateFileDeleteFlag(String id) {
        ErpOrderFile file = erpOrderFileService.get(id);
        if (null != file) {
            file.setDelFlag("0");
            erpOrderFileService.save(file);
        }
    }

    @Transactional(readOnly = false)
    public void saveDesignFriend(String taskId, String procInsId, String splitId, String[] pickIdFriends) {
        ErpFlowForm erpFlowForm = new ErpFlowForm();

        erpFlowForm.setBusId(splitId);
        erpFlowForm.setTaskId(taskId);
        erpFlowForm.setProcInsId(procInsId);
        erpFlowForm.setTaskDef(OUTPUT_DESIGN_DRAFT_3_2);
        erpFlowForm.setFormAttrName(PICK_ID_FRIENDS);
        erpFlowForm.setFormAttrType(ErpFlowForm.FILE);


        this.erpOrderFileService.deleteByProcInsId(FRIENDS_OFFICAL, procInsId);

        for (String id : pickIdFriends) {
            updateFileDeleteFlag(id);
            erpFlowForm.setFormAttrValue(erpFlowForm.getFormAttrValue() + id + Constant.SEMICOLON);
        }

        erpFlowFormService.saveErpFlowForm(erpFlowForm);

    }

    @Transactional(readOnly = false)
    public void saveDesignMomo(String taskId, String procInsId, String splitId, String[] pickIdMomo) {
        ErpFlowForm erpFlowForm = new ErpFlowForm();

        erpFlowForm.setBusId(splitId);
        erpFlowForm.setTaskId(taskId);
        erpFlowForm.setProcInsId(procInsId);
        erpFlowForm.setTaskDef(OUTPUT_DESIGN_DRAFT_3_2);
        erpFlowForm.setFormAttrName(PICK_ID_MOMO);
        erpFlowForm.setFormAttrType(ErpFlowForm.FILE);


        this.erpOrderFileService.deleteByProcInsId(MOMO_OFFICAL, procInsId);

        for (String id : pickIdMomo) {
            updateFileDeleteFlag(id);
            erpFlowForm.setFormAttrValue(erpFlowForm.getFormAttrValue() + id + Constant.SEMICOLON);
        }

        erpFlowFormService.saveErpFlowForm(erpFlowForm);
    }



    /**
     * 指派投放顾问
     *
     * @param taskId
     * @param procInsId
     * @param assignConsultant
     * @return
     * @date 2018年5月8日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject assignedOperationAdviser3p2(String taskId, String procInsId, String assignConsultant) {

        logger.info("指派投放顾问 ->start[assigned_operation_adviser_3.2],taskId[{}],procInsId[{}],assignConsultant[{}]", taskId, procInsId,
                        assignConsultant);

        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();

        ErpOrderSplitInfo split = erpOrderSplitInfoService.getByProsIncId(procInsId);
        if (StringUtils.isNotBlank(assignConsultant)) {
            // 插入订单流程信息表
            erpOrderFlowUserService.insertOrderFlowUser(assignConsultant, split.getOrderId(), split.getId(), JykFlowConstants.assignConsultant,
                            procInsId);
            vars.put(JykFlowConstants.assignConsultant, assignConsultant);

            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.assignConsultant, JykFlowConstants.Planning_Expert}, taskId, procInsId,
                            "指派投放顾问", null);

            resObject.put(FlowConstant.RESULT, true);

            logger.info("指派投放顾问 ->end[assigned_operation_adviser_3.2],taskId[{}],procInsId[{}],assignConsultant[{}]", taskId, procInsId,
                            assignConsultant);


            return resObject;

        }

        resObject.put(FlowConstant.RESULT, false);
        resObject.put(FlowConstant.MESSAGE, "请指派投放顾问!");

        return resObject;
    }

    /**
     * 输出推广页面预览给策划专家
     *
     * @param taskId
     * @param procInsId
     * @param outerImgFriends
     * @param outerImgWeibo
     * @param outerImgMomo
     * @return
     * @date 2018年5月9日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject workPromotionSchemePreview3p2(String taskId, String procInsId, String splitId, String[] outerImgFriends,
                    String[] innerImgFriends,
                    String[] outerImgWeibo, String[] innerImgWeibo, String[] outerImgMomo, String[] innerImgMomo, String qrcode, String checkvalue) {

        JSONObject resObject = new JSONObject();
        saveFlowForm(taskId, splitId, procInsId, QRCODE, qrcode, WORK_PROMOTION_SCHEME_PREVIEW_3_2, ErpFlowForm.NORMAL);

        // 查询预览信息
        JykDeliveryEffectInfo deliveryEffectInfo = queryDeliveryEffectInfoByProcessId(procInsId, splitId);

        // 朋友圈外层入口截图
        saveFlowPreviewImg(taskId, procInsId, splitId, outerImgFriends, OUTER_IMG_FRIENDS, deliveryEffectInfo, FlowConstant.CHANNEL_OUTERIMGFRIENDS,
                        outerImgFriends_str);
        // 朋友圈落地页截图
        saveFlowPreviewImg(taskId, procInsId, splitId, innerImgFriends, INNER_IMG_FRIENDS, deliveryEffectInfo, FlowConstant.CHANNEL_INNERIMGFRIENDS,
                        innerImgFriends_str);
        // 微博外层入口截图
        saveFlowPreviewImg(taskId, procInsId, splitId, outerImgWeibo, OUTER_IMG_WEIBO, deliveryEffectInfo, FlowConstant.CHANNEL_OUTERIMGWEIBO,
                        outerImgWeibo_str);
        // 微博落地页截图
        saveFlowPreviewImg(taskId, procInsId, splitId, innerImgWeibo, INNER_IMG_WEIBO, deliveryEffectInfo, FlowConstant.CHANNEL_INNERIMGWEIBO,
                        innerImgWeibo_str);
        // 陌陌外层入口截图
        saveFlowPreviewImg(taskId, procInsId, splitId, outerImgMomo, OUTER_IMG_MOMO, deliveryEffectInfo, FlowConstant.CHANNEL_OUTERIMGMOMO,
                        outerImgMomo_str);
        // 陌陌落地页截图
        saveFlowPreviewImg(taskId, procInsId, splitId, innerImgMomo, INNER_IMG_MOMO, deliveryEffectInfo, FlowConstant.CHANNEL_INNERIMGMOMO,
                        innerImgMomo_str);

        if (validateFlowFinish(splitId, outerImgFriends, outerImgWeibo, outerImgMomo) && validateFlowFinish(splitId, innerImgFriends, innerImgWeibo,
                        innerImgMomo)) {
            if (FlowConstant.STRING_1.equalsIgnoreCase(checkvalue) && !Boolean.valueOf(qrcode)) {
                jykDeliveryEffectInfoService.save(deliveryEffectInfo);
                resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
                return resObject;
            }

            deliveryEffectInfo.setState(FlowConstant.STRING_2);

            this.workFlowService.completeFlow2(new String[] {JykFlowConstants.Planning_Expert}, taskId, procInsId, "完成输出推广页面预览给策划专家", null);
        }

        jykDeliveryEffectInfoService.save(deliveryEffectInfo);


        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }

    @Transactional(readOnly = false)
    public void saveFlowPreviewImg(String taskId, String procInsId, String splitId, String[] imgs,
                    String attrname, JykDeliveryEffectInfo deliveryEffectInfo, String flag, String cnname) {
        if (!isEmptyArrays(imgs)) {
            savePreviewImg(taskId, procInsId, splitId, imgs, deliveryEffectInfo, WORK_PROMOTION_SCHEME_PREVIEW_3_2, attrname, flag, cnname);
        } else {
            deletePreviewImg(procInsId, taskId, attrname, cnname);
        }
    }


    @Transactional(readOnly = false)
    public void deletePreviewImg(String procInsId, String taskId, String attrname, String cnname) {
        erpOrderFileService.deleteByProcInsId(cnname, procInsId);
        deleteFLowFormByAttrName(taskId, attrname);
    }


    @Transactional(readOnly = false)
    public void savePreviewImg(String taskId, String procInsId, String splitId, String[] outerImgFriends,
                    JykDeliveryEffectInfo deliveryEffectInfo, String taskKey, String attrname, String channel, String cnname) {

        ErpFlowForm erpFlowForm = new ErpFlowForm();

        erpFlowForm.setBusId(splitId);
        erpFlowForm.setTaskId(taskId);
        erpFlowForm.setProcInsId(procInsId);
        erpFlowForm.setTaskDef(taskKey);
        erpFlowForm.setFormAttrName(attrname);
        erpFlowForm.setFormAttrType(ErpFlowForm.FILE);

        this.erpOrderFileService.deleteByProcInsId(cnname, procInsId);

        initImgUrl(channel, deliveryEffectInfo);

        for (String id : outerImgFriends) {
            // 更新当前的文件为正式文件
            ErpOrderFile file = erpOrderFileService.get(id);
            if (null != file) {
                file.setDelFlag("0");
                erpOrderFileService.save(file);
                setImgUrl(channel, deliveryEffectInfo, file);
            }
            erpFlowForm.setFormAttrValue(erpFlowForm.getFormAttrValue() + id + Constant.SEMICOLON);
        }

        erpFlowFormService.saveErpFlowForm(erpFlowForm);

    }


    private void initImgUrl(String channel, JykDeliveryEffectInfo deliveryEffectInfo) {
        if (FlowConstant.CHANNEL_OUTERIMGFRIENDS.equalsIgnoreCase(channel)) {
            deliveryEffectInfo.setOuterImgUrlFriends(Constant.BLANK);
            deliveryEffectInfo.setOuterImgNameFriends(Constant.BLANK);
        }
        if (FlowConstant.CHANNEL_OUTERIMGWEIBO.equalsIgnoreCase(channel)) {
            deliveryEffectInfo.setOuterImgUrlWeibo(Constant.BLANK);
            deliveryEffectInfo.setOuterImgNameWeibo(Constant.BLANK);
        }
        if (FlowConstant.CHANNEL_OUTERIMGMOMO.equalsIgnoreCase(channel)) {
            deliveryEffectInfo.setOuterImgUrlMomo(Constant.BLANK);
            deliveryEffectInfo.setOuterImgNameMomo(Constant.BLANK);
        }
        if (FlowConstant.CHANNEL_INNERIMGFRIENDS.equalsIgnoreCase(channel)) {
            deliveryEffectInfo.setInnerImgUrlFriends(Constant.BLANK);
            deliveryEffectInfo.setInnerImgNameFriends(Constant.BLANK);
        }
        if (FlowConstant.CHANNEL_INNERIMGWEIBO.equalsIgnoreCase(channel)) {
            deliveryEffectInfo.setInnerImgUrlWeibo(Constant.BLANK);
            deliveryEffectInfo.setInnerImgNameWeibo(Constant.BLANK);
        }
        if (FlowConstant.CHANNEL_INNERIMGMOMO.equalsIgnoreCase(channel)) {
            deliveryEffectInfo.setInnerImgUrlMomo(Constant.BLANK);
            deliveryEffectInfo.setInnerImgNameMomo(Constant.BLANK);
        }
    }

    private void setImgUrl(String channel, JykDeliveryEffectInfo deliveryEffectInfo, ErpOrderFile file) {

        if (FlowConstant.CHANNEL_OUTERIMGFRIENDS.equalsIgnoreCase(channel)) {
            deliveryEffectInfo.setOuterImgUrlFriends(deliveryEffectInfo.getOuterImgUrlFriends() + file.getFilePath() + Constant.SEMICOLON);
            deliveryEffectInfo.setOuterImgNameFriends(deliveryEffectInfo.getOuterImgNameFriends() + file.getFileName() + Constant.SEMICOLON);
        }
        if (FlowConstant.CHANNEL_OUTERIMGWEIBO.equalsIgnoreCase(channel)) {
            deliveryEffectInfo.setOuterImgUrlWeibo(deliveryEffectInfo.getOuterImgUrlWeibo() + file.getFilePath() + Constant.SEMICOLON);
            deliveryEffectInfo.setOuterImgNameWeibo(deliveryEffectInfo.getOuterImgNameWeibo() + file.getFileName() + Constant.SEMICOLON);
        }
        if (FlowConstant.CHANNEL_OUTERIMGMOMO.equalsIgnoreCase(channel)) {
            deliveryEffectInfo.setOuterImgUrlMomo(deliveryEffectInfo.getOuterImgUrlMomo() + file.getFilePath() + Constant.SEMICOLON);
            deliveryEffectInfo.setOuterImgNameMomo(deliveryEffectInfo.getOuterImgNameMomo() + file.getFileName() + Constant.SEMICOLON);
        }
        if (FlowConstant.CHANNEL_INNERIMGFRIENDS.equalsIgnoreCase(channel)) {
            deliveryEffectInfo.setInnerImgUrlFriends(deliveryEffectInfo.getInnerImgUrlFriends() + file.getFilePath() + Constant.SEMICOLON);
            deliveryEffectInfo.setInnerImgNameFriends(deliveryEffectInfo.getInnerImgNameFriends() + file.getFileName() + Constant.SEMICOLON);
        }
        if (FlowConstant.CHANNEL_INNERIMGWEIBO.equalsIgnoreCase(channel)) {
            deliveryEffectInfo.setInnerImgUrlWeibo(deliveryEffectInfo.getInnerImgUrlWeibo() + file.getFilePath() + Constant.SEMICOLON);
            deliveryEffectInfo.setInnerImgNameWeibo(deliveryEffectInfo.getInnerImgNameWeibo() + file.getFileName() + Constant.SEMICOLON);
        }
        if (FlowConstant.CHANNEL_INNERIMGMOMO.equalsIgnoreCase(channel)) {
            deliveryEffectInfo.setInnerImgUrlMomo(deliveryEffectInfo.getInnerImgUrlMomo() + file.getFilePath() + Constant.SEMICOLON);
            deliveryEffectInfo.setInnerImgNameMomo(deliveryEffectInfo.getInnerImgNameMomo() + file.getFileName() + Constant.SEMICOLON);
        }

    }


    /**
     * 保存流程表单数据 <erp_flow_form> 普通文本 form_attr_type = NORMAL
     * 
     * @param taskId
     * @param splitId
     * @param procInsId
     * @param attrname
     * @param attrval
     * @param taskRef
     * @date 2018年5月10日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public void saveFlowForm(String taskId, String splitId, String procInsId, String attrname, String attrval, String taskRef, String filetype) {

        if (StringUtils.isNotBlank(attrval)) {

            // 保存数据到流程表单
            ErpFlowForm erpFlowForm = new ErpFlowForm();
            erpFlowForm.setBusId(splitId);
            erpFlowForm.setTaskId(taskId);
            erpFlowForm.setProcInsId(procInsId);
            erpFlowForm.setTaskDef(taskRef);
            erpFlowForm.setFormAttrName(attrname);

            erpFlowForm.setFormAttrType(filetype);
            if (ErpFlowForm.TEXT.equalsIgnoreCase(filetype)) {
                erpFlowForm.setFormTextValue(attrval);
            } else {
                erpFlowForm.setFormAttrValue(attrval);
            }

            erpFlowFormService.saveErpFlowForm(erpFlowForm);
        }
    }

    @Transactional(readOnly = false)
    public JSONObject workAssignPlanningDesigner3p2(String taskId, String procInsId, String textDesignPerson, String designer, boolean isFinished) {
        logger.info("指派文案策划、设计师 ->start [work_assign_planning_designer_3.2],taskId[{}],procInsId[{}],textDesignPerson[{}],designer[{}],isFinished[{}]",
                        taskId, procInsId, textDesignPerson, designer, isFinished);

        JSONObject resObject = new JSONObject();
        Map<String, Object> vars = Maps.newHashMap();
        ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
        if (StringUtils.isNotBlank(textDesignPerson)) {
            // 插入订单流程信息表
            erpOrderFlowUserService.insertOrderFlowUser(textDesignPerson, split.getOrderId(), split.getId(), JykFlowConstants.assignTextDesignPerson,
                            procInsId);
            vars.put("textDesignPerson", textDesignPerson);
        }
        if (StringUtils.isNotBlank(designer)) {
            // 插入订单流程信息表
            erpOrderFlowUserService.insertOrderFlowUser(designer, split.getOrderId(), split.getId(), JykFlowConstants.designer, procInsId);

            vars.put("designer", designer);
        }
        if (isFinished) {

            String assignTextDesignPerson = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignTextDesignPerson)
                            .getUser().getId();
            String designerName = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.designer).getUser().getId();

            String consultantInterface = this.erpOrderFlowUserService.findListByFlowId(procInsId, JykFlowConstants.assignConsultantInterface)
                            .getUser().getId();

            vars.put("consultantInterface", consultantInterface);
            vars.put("textDesignPerson", assignTextDesignPerson);
            vars.put("designer", designerName);
            vars.put("isAll", "1");
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "指派文案策划、设计师", vars);
            // 指派文案策划
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YYYYMMDDHHMMSS);
            String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(split.getId());
            ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
            erpHisSplitServiceApi.creativity(split.getId(), sdf.format(new Date(System.currentTimeMillis())), storeInfo.getId(),
                            storeInfo.getShortName());
        }
        resObject.put("result", true);

        logger.info("指派文案策划、设计师 -> end [work_assign_planning_designer_3.2],taskId[{}],procInsId[{}],textDesignPerson[{}],designer[{}],isFinished[{}]",
                        taskId, procInsId, textDesignPerson, designer, isFinished);


        return resObject;
    }


    /**
     * 
     * 推广页面预览确认
     * 
     * @param taskId
     * @param procInsId
     * @param confirm
     * @return
     * @date 2018年5月9日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject promotionPlanPreviewConfirmation3p2(String taskId, String procInsId, String splitId, String confirm) {
        JSONObject resObject = new JSONObject();
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
        saveFlowForm(taskId, splitId, procInsId, "confirm", confirm, PROMOTION_PLAN_PREVIEW_CONFIRMATION_3_2, ErpFlowForm.NORMAL);

        if (Boolean.valueOf(confirm)) {
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "推广页面预览确认", null);
        }
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }


    /**
     * 推广广告计划提审确认成功
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param friendconfirm
     * @param weiboconfirm
     * @param momoconfirm
     * @return
     * @date 2018年5月10日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject promotionPlanReview3p2(String taskId, String procInsId, String splitId, String friendconfirm, String weiboconfirm,
                    String momoconfirm, String launchinfo) {
        JSONObject resObject = new JSONObject();
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
        // 确认推广计划提审成功 - 朋友圈
        saveFlowForm(taskId, splitId, procInsId, FRIENDCONFIRM, friendconfirm, PROMOTION_PLAN_REVIEW_3_2, ErpFlowForm.NORMAL);
        // 确认推广计划提审成功 - 微博
        saveFlowForm(taskId, splitId, procInsId, WEIBOCONFIRM, weiboconfirm, PROMOTION_PLAN_REVIEW_3_2, ErpFlowForm.NORMAL);
        // 确认推广计划提审成功 - 陌陌
        saveFlowForm(taskId, splitId, procInsId, MOMOCONFIRM, momoconfirm, PROMOTION_PLAN_REVIEW_3_2, ErpFlowForm.NORMAL);
        // 保存投放信息
        saveFlowFormAndInputDetail(taskId, procInsId, splitId, "投放信息", "launchinfo", launchinfo, PROMOTION_PLAN_REVIEW_3_2);
        // 较验流程是否流转
        friendconfirm = setConfirmInfo(friendconfirm);
        weiboconfirm = setConfirmInfo(weiboconfirm);
        momoconfirm = setConfirmInfo(momoconfirm);
        if (validateFlowFinish(splitId, friendconfirm, weiboconfirm, momoconfirm)) {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "推广计划 提审确认成功", null);
        }

        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }

    private String setConfirmInfo(String friendconfirm) {
        if (Boolean.valueOf(friendconfirm)) {
            friendconfirm = Constant.BLANK;
        }
        return friendconfirm;
    }


    /**
     * 
     * 保存流程表单数据和分单资料录入业务数据 <erp_flow_form,erp_order_input_detail> 普通文本 form_attr_type = TEXT
     * 
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param attrname
     * @param attrval
     * @param taskRef
     * @date 2018年5月10日
     * @author zjq
     */
    private void saveFlowFormAndInputDetail(String taskId, String procInsId, String splitId, String cnattrname, String attrname, String attrval,
                    String taskRef) {

        ErpFlowForm erpFlowForm = new ErpFlowForm();

        erpFlowForm.setBusId(splitId);
        erpFlowForm.setTaskId(taskId);
        erpFlowForm.setProcInsId(procInsId);
        erpFlowForm.setTaskDef(taskRef);
        erpFlowForm.setFormAttrName(attrname);
        erpFlowForm.setFormAttrType(ErpFlowForm.TEXT);
        erpFlowForm.setFormTextValue(base64Decode(attrval));

        erpFlowFormService.saveErpFlowForm(erpFlowForm);

        ErpOrderInputDetail erpOrderInputDetail = erpOrderInputDetailService.findListByInputKey(splitId, cnattrname);

        if (null == erpOrderInputDetail) {
            erpOrderInputDetail = new ErpOrderInputDetail();
        }
        erpOrderInputDetail.setSplitId(splitId);
        erpOrderInputDetail.setInputTaskName(cnattrname);
        erpOrderInputDetail.setInputDetail(base64Decode(attrval));
        erpOrderInputDetail.setInputTaskKey(attrname);


        erpOrderInputDetailService.save(erpOrderInputDetail);

    }

    /**
     * 
     * 最终确认推广计划
     * 
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param orderJsonObject
     * @return
     * @date 2018年5月10日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject finallyConfirmLaunchTime3p2(String taskId, String procInsId, String splitId, JSONObject orderJsonObject) {

        JSONObject resObject = new JSONObject();
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);

        if (StringUtils.isNoneBlank(orderJsonObject.getString("promotedate"))) {
            ErpOrderSplitInfo split = this.erpOrderSplitInfoService.getByProsIncId(procInsId);
            split.setPromotionTime(DateUtils.parseDate(orderJsonObject.getString("promotedate")));
            this.erpOrderSplitInfoService.save(split);
        }
        saveFlowForm(taskId, splitId, procInsId, "finallyConfirmJson", orderJsonObject.toJSONString(), "finally_confirm_launch_time_3.2",
                        ErpFlowForm.NORMAL);

        if (validatefinallyConfirmFlowFinish(orderJsonObject)) {
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultantInterface, taskId, procInsId, "最终确认推广计划", null);
        }

        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }

    private boolean validatefinallyConfirmFlowFinish(JSONObject orderJsonObject) {
        boolean isFinish = true;
        Set<String> set = orderJsonObject.keySet();
        for (String tmp : set) {
            // 朋友圈
            if (tmp.startsWith("weixin") && "false"
                                .equalsIgnoreCase(orderJsonObject.getString(tmp))) {
                    isFinish = false;
                    break;
                }
            // 微博
            if (tmp.startsWith("weibo") && "false"
                                .equalsIgnoreCase(orderJsonObject.getString(tmp))) {
                    isFinish = false;
                    break;
                }
            // 陌陌
            if (tmp.startsWith("momo") && "false"
                                .equalsIgnoreCase(orderJsonObject.getString(tmp))) {
                    isFinish = false;
                    break;
                }
        }

        return isFinish;
    }

    @Transactional(readOnly = false)
    public JSONObject reviewOfficialDocumentsV3p2(String taskId, String procInsId, String splitId, String checkvalue, String reason) {

        JSONObject resObject = new JSONObject();
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
        if (Constant.NO.equalsIgnoreCase(checkvalue)) {
            saveFlowForm(taskId, splitId, procInsId, "officalFailReason", reason, "review_official_documents_3.2", ErpFlowForm.TEXT);
        }
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("reviewResult", checkvalue);
        this.workFlowService.completeFlow(JykFlowConstants.assignTextDesignPerson, taskId, procInsId, "完成审核文案", vars);
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;

    }

    /**
     * 推广上线朋友圈
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param outerImgFriends
     * @param innerImgFriends
     * @param promoteDate
     * @return
     * @date 2018年5月10日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject promoteOnlineFriends3p2(String taskId, String procInsId, String splitId, String[] outerImgFriends, String[] innerImgFriends,
                    String promoteDate) {

        JSONObject resObject = new JSONObject();
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);

        JykDeliveryEffectInfo deliveryEffectInfo = queryDeliveryEffectInfoByProcessId(procInsId, splitId);

        saveFlowForm(taskId, splitId, procInsId, "friendPromoteDate", promoteDate, "promote_online_friends_3.2", ErpFlowForm.NORMAL);

        // 朋友圈外层入口截图
        saveFlowPreviewImg(taskId, procInsId, splitId, outerImgFriends, OUTER_IMG_FRIENDS, deliveryEffectInfo, FlowConstant.CHANNEL_OUTERIMGFRIENDS,
                        outerImgFriends_str);
        // 朋友圈落地页截图
        saveFlowPreviewImg(taskId, procInsId, splitId, innerImgFriends, INNER_IMG_FRIENDS, deliveryEffectInfo, FlowConstant.CHANNEL_INNERIMGFRIENDS,
                        innerImgFriends_str);

        jykDeliveryEffectInfoService.save(deliveryEffectInfo);

        savePromoteDate(splitId, promoteDate, Constant.CHANNEL_1);
        // 流程流转
        if (!isEmptyArrays(outerImgFriends) && !isEmptyArrays(innerImgFriends) && StringUtils.isNoneBlank(promoteDate)) {
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "完成推广上线—朋友圈", null);
        }

        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }



    /**
     * 推广上线微博
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param outerImgWeibo
     * @param innerImgWeibo
     * @param promoteDate
     * @return
     * @date 2018年5月10日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject promoteOnlineMicroblog3p2(String taskId, String procInsId, String splitId, String[] outerImgWeibo, String[] innerImgWeibo,
                    String promoteDate) {

        JSONObject resObject = new JSONObject();
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);

        JykDeliveryEffectInfo deliveryEffectInfo = queryDeliveryEffectInfoByProcessId(procInsId, splitId);

        saveFlowForm(taskId, splitId, procInsId, "weiboPromoteDate", promoteDate, "promote_online_microblog_3.2", ErpFlowForm.NORMAL);

        // 微博外层入口截图
        saveFlowPreviewImg(taskId, procInsId, splitId, outerImgWeibo, OUTER_IMG_WEIBO, deliveryEffectInfo, FlowConstant.CHANNEL_OUTERIMGWEIBO,
                        outerImgWeibo_str);
        // 微博落地页截图
        saveFlowPreviewImg(taskId, procInsId, splitId, innerImgWeibo, INNER_IMG_WEIBO, deliveryEffectInfo, FlowConstant.CHANNEL_INNERIMGWEIBO,
                        innerImgWeibo_str);

        jykDeliveryEffectInfoService.save(deliveryEffectInfo);

        savePromoteDate(splitId, promoteDate, Constant.CHANNEL_2);
        // 流程流转
        if (!isEmptyArrays(outerImgWeibo) && !isEmptyArrays(innerImgWeibo) && StringUtils.isNoneBlank(promoteDate)) {
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "完成推广上线—微博", null);
        }

        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }

    /**
     * 推广上线陌陌
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param outerImgMomo
     * @param innerImgMomo
     * @param promoteDate
     * @return
     * @date 2018年5月10日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject promoteOnlineMomo3p2(String taskId, String procInsId, String splitId, String[] outerImgMomo, String[] innerImgMomo,
                    String promoteDate) {

        JSONObject resObject = new JSONObject();
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);

        JykDeliveryEffectInfo deliveryEffectInfo = queryDeliveryEffectInfoByProcessId(procInsId, splitId);

        saveFlowForm(taskId, splitId, procInsId, "momoPromoteDate", promoteDate, "promote_online_momo_3.2", ErpFlowForm.NORMAL);

        // 陌陌外层入口截图
        saveFlowPreviewImg(taskId, procInsId, splitId, outerImgMomo, OUTER_IMG_MOMO, deliveryEffectInfo, FlowConstant.CHANNEL_OUTERIMGMOMO,
                        outerImgMomo_str);
        // 陌陌落地页截图
        saveFlowPreviewImg(taskId, procInsId, splitId, innerImgMomo, INNER_IMG_MOMO, deliveryEffectInfo, FlowConstant.CHANNEL_INNERIMGMOMO,
                        innerImgMomo_str);

        jykDeliveryEffectInfoService.save(deliveryEffectInfo);

        savePromoteDate(splitId, promoteDate, Constant.CHANNEL_3);
        // 流程流转
        if (!isEmptyArrays(outerImgMomo) && !isEmptyArrays(innerImgMomo) && StringUtils.isNoneBlank(promoteDate)) {
            this.workFlowService.completeFlow(JykFlowConstants.assignConsultant, taskId, procInsId, "完成推广上线—陌陌", null);
        }
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }

    /**
     * 保存推广时间
     *
     * @param splitId
     * @param promoteDate
     * @param channel
     * @date 2018年5月10日
     * @author zjq
     */
    private void savePromoteDate(String splitId, String promoteDate, String channel) {
        if (StringUtils.isNoneBlank(promoteDate)) {
            JykOrderPromotionChannel jykOrderPromotionChannel = jykOrderPromotionChannelService.getChannelSelected(splitId, channel);
            if (null != jykOrderPromotionChannel) {
                try {
                    jykOrderPromotionChannel.setPromoteStartDate(DateUtils.parseDate(promoteDate, "yyyy-MM-dd"));
                } catch (ParseException e) {
                    logger.error(e.getMessage(), e);
                }
                jykOrderPromotionChannel.setPromoteStatus(Constants.RUNNING);
                jykOrderPromotionChannelService.update(jykOrderPromotionChannel);

                logger.info("更新推广开始时间==>,splitId[{}],promoteDate[{}]channel[{}]", splitId, promoteDate, channel);

            }
        }
    }

    /**
     * 查询预览信息
     *
     * @param procInsId
     * @param splitId
     * @return
     * @date 2018年5月11日
     * @author zjq
     */
    private JykDeliveryEffectInfo queryDeliveryEffectInfoByProcessId(String procInsId, String splitId) {
        JykDeliveryEffectInfo deliveryEffectInfo = jykDeliveryEffectInfoService.getByProcInsId(procInsId);

        if (null == deliveryEffectInfo) {
            ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoService.getByProsIncId(procInsId);
            deliveryEffectInfo = new JykDeliveryEffectInfo();
            deliveryEffectInfo.setProcInsId(procInsId);
            deliveryEffectInfo.setSplitId(splitId);
            deliveryEffectInfo.setState(FlowConstant.STRING_1);
            deliveryEffectInfo.setOrderId(erpOrderSplitInfo.getOrderId());
        }
        return deliveryEffectInfo;
    }

    private boolean isEmptyArrays(String[] pickIdFriends) {

        if (null == pickIdFriends) {
            return true;
        }

        if (pickIdFriends.length == 0) {
            return true;
        }

        return false;
    }

    /**
     * 推广提案内
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param isPass
     * @param reason
     * @return
     * @date 2018年5月14日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public JSONObject promotionProposalAuditV3p2(String taskId, String procInsId, String splitId, String isPass, String reason) {
        JSONObject resObject = new JSONObject();
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_FALSE);
        Map<String, Object> vars = Maps.newHashMap();
        saveFlowForm(taskId, splitId, procInsId, "promotionCheckValue", isPass, "promotion_proposal_audit_3.2", ErpFlowForm.NORMAL);
        saveFlowForm(taskId, splitId, procInsId, "promotionFailReason", reason, "promotion_proposal_audit_3.2", ErpFlowForm.NORMAL);
        // 流程流转
        vars.put("reviewResult", isPass);
        if (Constant.YES.equalsIgnoreCase(isPass)) {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "完成推广提案提审", vars);
        }
        if (Constant.NO.equalsIgnoreCase(isPass)) {
            this.workFlowService.completeFlow(JykFlowConstants.Planning_Expert, taskId, procInsId, "完成推广提案提审", vars);
        }
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }

    @Transactional(readOnly = false)
    public BaseResult managementDiagnosisMarketingPlanningV3p2(String taskId, String procInsId, String splitId, String proposalAudit) {
        logger.info("确定完成任务 =》 经营诊断&营销策划，参数：splitId = {}, taskId = {}, procInsId = {}", splitId, taskId, procInsId);

        String resultMsg = "确定完成任务 =》 经营诊断&营销策划，结果：{}";
        // 校验表单
        BaseResult baseResult = formValidateService.validateForm(splitId);
        if (!BaseResult.isSuccess(baseResult)) {
            logger.info(resultMsg, baseResult);
            return baseResult;
        }
        // 完成任务
        JSONObject jsonObject = managementDiagnosisMarketingPlanning(taskId, procInsId, true, splitId, proposalAudit);
        if (!jsonObject.getBoolean("result")) {
            return baseResult.error("-3", jsonObject.getString("message"));
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
    @Transactional(readOnly = false)
    public JSONObject managementDiagnosisMarketingPlanning(String taskId, String procInsId, boolean isFinished, String splitId,
                    String proposalAudit) {
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

        // 选择套餐
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

        String storeId = jykOrderChoiceStoreService.getStoreIdBySplitId(jykFlow.getSplitId());
        // 设置流程变量
        Map<String, Object> vars = Maps.newHashMap();
        ErpStoreInfo storeInfo = erpStoreInfoService.get(storeId);
        // 判断是否上传过推广素材
        ErpStorePromotePhotoMaterial promotePhotoMaterial = erpStorePromotePhotoMaterialService.findlistWhereStoreId("0", storeId);
        if (promotePhotoMaterial != null) {
            vars.put("UploadPictureMaterial", promotePhotoMaterial.getStoreInfoId());
        }
        if (isFinished) {

            if (StringUtils.isNotEmpty(proposalAudit)) {
                erpOrderFlowUserService.insertOrderFlowUser(proposalAudit, erpOrderSplitInfo.getOrderId(), splitId, JykFlowConstants.proposalAudit,
                                procInsId);

                vars.put("proposalAudit", proposalAudit);
                this.workFlowService.completeFlow2(new String[] {JykFlowConstants.proposalAudit}, taskId, procInsId, "完成经营诊断&营销策划", vars);

            } else {
                this.workFlowService.completeFlow2(new String[] {JykFlowConstants.proposalAudit}, taskId, procInsId, "完成修改推广提案", vars);
            }
            // 根据经营诊断选择套餐信息，动态更新分单商品及数量
            erpOrderSplitInfoService.updateSplitGoodInfoByDiagnosis(splitId);

            // 经营诊断完成
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YYYYMMDDHHMMSS);
            erpHisSplitServiceApi.diagnosis(jykFlow.getSplitId(), sdf.format(new Date(System.currentTimeMillis())), storeInfo.getId(),
                            storeInfo.getShortName());

            // 获取推广通道
            String channelList = "";
            List<Integer> channels = jykOrderPromotionChannelService.getChannels(splitId);
            for (Integer integer : channels) {
                channelList += (String.valueOf(integer) + Constant.COMMA);
            }
            if (channelList.endsWith(Constant.COMMA)) {
                channelList = channelList.substring(0, channelList.lastIndexOf(Constant.COMMA));
            }

            // 确认投放通道后
            erpHisSplitServiceApi.deliveryChannel(jykFlow.getSplitId(), channelList, storeInfo.getId(), storeInfo.getShortName());

            // 判断接下来的流程是否往下流转
            // 获取当前流程下的任务
            ProcessInstance processInstance = actTaskService.getProcIns(procInsId);
            List<Task> tasks = taskService.createTaskQuery().includeProcessVariables().processInstanceId(processInstance.getId()).list();
            for (Task task : tasks) {
                // 判断是否完善朋友圈开户资料
                if ("perfect_friends_promote_info_zhixiao".equals(task.getTaskDefinitionKey()) || "perfect_friends_promote_info_service"
                                .equals(task.getTaskDefinitionKey())) {
                    ErpStoreAdvertiserFriends advertiserFriends = erpStoreAdvertiserFriendsService.getByStoreId(storeId);
                    if (advertiserFriends != null && 1 == advertiserFriends.getAuditStatus().intValue()) {
                        jykFlowAccountSignalService.perfectFriendsPromote(storeId);
                    }
                    continue;
                }

                // 判断是否完善微博开户资料
                if ("perfect_microblog_promote_info_zhixiao".equals(task.getTaskDefinitionKey()) || "perfect_microblog_promote_info_service"
                                .equals(task.getTaskDefinitionKey())) {
                    ErpStoreAdvertiserWeibo advertiserWeibo = erpStoreAdvertiserWeiboService.getByStoreId(storeId);
                    if (advertiserWeibo != null && 1 == advertiserWeibo.getAuditStatus().intValue()) {
                        jykFlowAccountSignalService.perfectMicroblogPromote(storeId);
                    }
                    continue;
                }

                // 判断是否完善陌陌开户资料
                if ("perfect_momo_promote_info_zhixiao".equals(task.getTaskDefinitionKey()) || "perfect_momo_promote_info_service"
                                .equals(task.getTaskDefinitionKey())) {
                    ErpStoreAdvertiserMomo advertiserMomo = erpStoreAdvertiserMomoService.getByStoreId(storeId);
                    if (advertiserMomo != null && 1 == advertiserMomo.getAuditStatus().intValue()) {
                        jykFlowAccountSignalService.perfectMomoPromote(storeId);
                    }
                    continue;
                }

            }
            resObject.put("result", true);
        }


        return resObject;
    }

    public JSONArray userListByRole(String roleName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Role commissioner = null;
        if (StringUtils.isEmpty(roleName)) {
            commissioner = systemService.getRoleByEnname(Constant.PROPOSAL_AUDIT);
        } else {
            commissioner = systemService.getRoleByEnname(roleName);
        }

        JSONArray jsonArray = new JSONArray();
        if (null != commissioner) {
            List<User> userListCommissioner = systemService.findUser(new User(new Role(commissioner.getId())));
            for (User user : userListCommissioner) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", BeanUtils.getProperty(user, "name"));
                jsonObject.put("id", BeanUtils.getProperty(user, "id"));
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }



    private void saveMomoLink(String taskId, String splitId, String momoLink, String procInsId) {

        if (StringUtils.isNotBlank(momoLink)) {

            // 保存数据到订单新资料表
            ErpOrderInputDetail erpOrderInputDetail = erpOrderInputDetailService.getBySplitId(splitId, "陌陌落地页链接");
            if (erpOrderInputDetail == null) {
                erpOrderInputDetail = new ErpOrderInputDetail();
            }
            erpOrderInputDetail.setInputDetail(momoLink);
            erpOrderInputDetail.setInputTaskName("陌陌落地页链接");
            erpOrderInputDetail.setSplitId(splitId);
            erpOrderInputDetailService.save(erpOrderInputDetail);
            this.workFlowService.setVariable(taskId, "momoDetail", momoLink);


            // 保存数据到流程表单
            ErpFlowForm erpFlowForm = new ErpFlowForm();

            erpFlowForm.setBusId(splitId);
            erpFlowForm.setTaskId(taskId);
            erpFlowForm.setProcInsId(procInsId);
            erpFlowForm.setTaskDef("output_design_draft_3.2");
            erpFlowForm.setFormAttrName("momoLink");
            erpFlowForm.setFormAttrValue(momoLink);
            erpFlowFormService.saveErpFlowForm(erpFlowForm);


        }
    }


}
