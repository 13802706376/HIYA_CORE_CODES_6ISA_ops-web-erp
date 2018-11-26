package com.yunnex.ops.erp.modules.workflow.flow.web.jyk;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlow3P2Service;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowDiagnosis3p2Service;


/**
 * 聚引客  直销开户相关  Controller
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Controller
@RequestMapping(value = "${adminPath}/jyk/flow/3p2")
public class JykFlowController3p2 extends BaseController {
    @Autowired
    private JykFlow3P2Service jykFlow3P2Service;
    @Autowired
    private JykFlowDiagnosis3p2Service jykFlowDiagnosis3p2Service;
    /**
     * 投放信息审阅
     *
     * @param taskId
     * @param consultantInterface
     * @return
     * @date 2018年5月7日
     * @author hanhan
     */
    @RequestMapping(value = "put_info_review_3.2")
    @ResponseBody
    public JSONObject putInfoReview(String taskId, String procInsId, String splitId, String putInfoCheckBox,String promotionTimeCheckBox ,boolean isFinished) {
        return  jykFlow3P2Service.putInfoReview(taskId, procInsId,splitId,putInfoCheckBox,promotionTimeCheckBox ,isFinished);
    }

    /**
     * 广告主开户成功通知商户
     *
     * @param taskId
     * @param putInfoCheckBox 投放信息勾选框 是否勾选
     * @param promotionTimeCheckBox 确认推广时间勾选框是否勾选
     * @return
     * @date 2018年5月7日
     * @author hanhan
     */
    
    @RequestMapping(value = "advertiser_success_notify_shop_3.2")
    @ResponseBody
    public JSONObject advertiserSuccessNotifyShop(String taskId, String procInsId,String splitId, String notifyShopCheckBox ) {
        return  jykFlow3P2Service.advertiserSuccessNotifyShop(taskId, procInsId,splitId,notifyShopCheckBox );
    }
    
    /**
     * 首日投放数据同步
     *
     * @param taskId
     * @param procInsId
     * @param firstDayPromoteDataInformShop
     * @return
     * @date 2018年5月9日
     * @author hanhan
     */
    @RequestMapping(value = "work_data_synchronization_first_day_3.2")
    @ResponseBody
    public JSONObject workDataSynFirstDay(String taskId, String procInsId,String splitId, String firstDayPromoteDataInformShop, HttpServletRequest request) {
        String jsonStr=  request.getParameter("jsonStr");
        return jykFlow3P2Service.workDataSynFirstDay(taskId, procInsId,splitId, firstDayPromoteDataInformShop,jsonStr);
    };
    
    /**
     * 推广状态同步
     *
     * @param taskId
     * @param procInsId
     * @param InformShopUpLine
     * @return
     * @date 2018年5月9日
     * @author hanhan
     */
    @RequestMapping(value = "promotion_state_sync_3.2")
    @ResponseBody
    public JSONObject promotionStateSync(String taskId, String procInsId,String splitId, String InformShopUpLine) {
        return jykFlow3P2Service.promotionStateSync(taskId, procInsId,splitId, InformShopUpLine);
    };
    
    /**
     * 效果报告输出给商户
     *
     * @param taskId
     * @param procInsId
     * @param effectReportuploadOEM
     * @param effectReportInformShopCheck
     * @return
     * @date 2018年5月9日
     * @author hanhan
     */
    @RequestMapping(value = "effect_report_exported_shop_3.2")
    @ResponseBody
    public JSONObject effectReportExportedShop(String taskId, String procInsId, String splitId,HttpServletRequest request,String effectReportuploadOEM, String effectReportInformShopCheck,boolean isFinished) {
        String jsonStr=  request.getParameter("jsonStr");
        return jykFlow3P2Service.effectReportExportedShop(taskId, procInsId,splitId, effectReportuploadOEM,effectReportInformShopCheck,jsonStr,isFinished);
    };
    
    /**
     * 输出卡券
     *
     * @param taskId
     * @param procInsId
     * @param jsonStr 
     * @return
     * @date 2018年5月10日
     * @author hanhan
     */
    @RequestMapping(value = "output_card_coupon_3.2")
    @ResponseBody
    public JSONObject outputCardCoupon(String taskId, String procInsId, String splitId, HttpServletRequest request,String isCardInfo, boolean isFinished) {
       String jsonStr=  request.getParameter("jsonStr");
        return jykFlow3P2Service.outputCardCoupon(taskId, procInsId,splitId, jsonStr, isCardInfo, isFinished);
    };
    
    /**
     * 推广提案内审
     *
     * @param taskId
     * @param procInsId
     * @param isPass 是否通过 Y：通过N：不通过
     * @param reason
     * @return
     * @date 2018年5月7日
     * @author zjq
     */
    @RequestMapping(value = "promotion_proposal_audit_3.2")
    @ResponseBody
    public JSONObject promotionProposalAuditV3p2(String taskId, String procInsId, String splitId, String isPass, String reason) {

        return jykFlowDiagnosis3p2Service.promotionProposalAuditV3p2(taskId, procInsId, splitId, isPass, reason);
    };
    

    
    /**
     * 修改推广提案提交流程
     *
     * @param taskId
     * @param procInsId
     * @return
     * @date 2018年5月7日
     * @author zjq
     */
    @RequestMapping(value = "modify_promotion_proposal_3.2")
    @ResponseBody
    public BaseResult modifyPromotionProposalV3p2(String taskId, String procInsId, String splitId) {
        return jykFlowDiagnosis3p2Service.managementDiagnosisMarketingPlanningV3p2(taskId, procInsId, splitId, null);
    };


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
    @RequestMapping(value = "promotion_proposal_confirmation_3.2")
    @ResponseBody
    public JSONObject promotionProposalConfirmationV3p2(String taskId, String procInsId, String splitId, String proposalConfirm) {

        return jykFlowDiagnosis3p2Service.promotionProposalConfirmationV3p2(taskId, procInsId, splitId, proposalConfirm);
    };

    /**
     * 输出文案
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @return
     * @date 2018年5月7日
     * @author zjq
     */
    @RequestMapping(value = "output_official_documents_3.2")
    @ResponseBody
    public JSONObject outputOfficialDocumentsV3p2(String taskId, String procInsId, String splitId, String editorWeibo, String editorMomo,
                    String editorFriend, String checkvalue) {

        return jykFlowDiagnosis3p2Service.outputOfficialDocumentsV3p2(taskId, procInsId, splitId, editorWeibo, editorMomo, editorFriend, checkvalue);
    };

    /**
     * 审核文案
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param checkvalue
     * @return
     * @date 2018年5月10日
     * @author zjq
     */
    @RequestMapping(value = "review_official_documents_3.2")
    @ResponseBody
    public JSONObject reviewOfficialDocumentsV3p2(String taskId, String procInsId, String splitId, String checkvalue, String reason) {

        return jykFlowDiagnosis3p2Service.reviewOfficialDocumentsV3p2(taskId, procInsId, splitId, checkvalue, reason);
    };


    /**
     * 修改文案
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @return
     * @date 2018年5月7日
     * @author zjq
     */
    @RequestMapping(value = "modify_official_documents_3.2")
    @ResponseBody
    public JSONObject modifyOfficialDocumentsV3p2(String taskId, String procInsId, String splitId, String editorWeibo, String editorMomo,
                    String editorFriend, String checkvalue) {

        return jykFlowDiagnosis3p2Service.modifyOfficialDocumentsV3p2(taskId, procInsId, splitId, editorWeibo, editorMomo, editorFriend, checkvalue);
    };

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
    @RequestMapping(value = "output_design_draft_3.2")
    @ResponseBody
    public JSONObject outputDesignDraftV3p2(String taskId, String procInsId, String splitId, String[] pickIdFriends, String[] pickIdWeibo,
                    String[] pickIdMomo, String momoLink) {
        return jykFlowDiagnosis3p2Service.outputDesignDraftV3p2(taskId, procInsId, splitId, pickIdFriends, pickIdWeibo, pickIdMomo, momoLink);
    };


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
    @RequestMapping(value = "review_design_draft_3.2")
    @ResponseBody
    public JSONObject reviewDesignDrafV3p2(String taskId, String procInsId, String splitId, String checkvalue, String reason) {

        return jykFlowDiagnosis3p2Service.reviewDesignDrafV3p2(taskId, procInsId, splitId, checkvalue, reason);
    };


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
    @RequestMapping(value = "modify_design_draft_3.2")
    @ResponseBody
    public JSONObject modifyDesignDraftV3p2(String taskId, String procInsId, String splitId, String[] pickIdFriends, String[] pickIdWeibo,
                    String[] pickIdMomo, String momoLink) {

        return jykFlowDiagnosis3p2Service.modifyDesignDraftV3p2(taskId, procInsId, splitId, pickIdFriends, pickIdWeibo, pickIdMomo, momoLink);
    };


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
    @RequestMapping(value = "assigned_operation_adviser_3.2")
    @ResponseBody
    public JSONObject assignedOperationAdviser3p2(String taskId, String procInsId, String assignConsultant) {

        return jykFlowDiagnosis3p2Service.assignedOperationAdviser3p2(taskId, procInsId, assignConsultant);
    };


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
    @RequestMapping(value = "work_promotion_scheme_preview_3.2")
    @ResponseBody
    public JSONObject workPromotionSchemePreview3p2(String taskId, String procInsId, String splitId, String[] outerImgFriends,
                    String[] innerImgFriends,
                    String[] outerImgWeibo, String[] innerImgWeibo, String[] outerImgMomo, String[] innerImgMomo, String qrcode, String checkvalue) {

        return jykFlowDiagnosis3p2Service.workPromotionSchemePreview3p2(taskId, procInsId, splitId, outerImgFriends, innerImgFriends, outerImgWeibo,
                        innerImgWeibo, outerImgMomo, innerImgMomo, qrcode, checkvalue);
    };


    /**
     * 
     * 指派文案策划、设计师
     * 
     * @param taskId
     * @param procInsId
     * @param textDesignPerson
     * @param designer
     * @param isFinished
     * @return
     * @date 2018年5月9日
     * @author zjq
     */
    @RequestMapping(value = "work_assign_planning_designer_3.2")
    @ResponseBody
    public JSONObject workAssignPlanningDesigner3p2(String taskId, String procInsId, String textDesignPerson, String designer, boolean isFinished) {

        return jykFlowDiagnosis3p2Service.workAssignPlanningDesigner3p2(taskId, procInsId, textDesignPerson, designer, isFinished);
    };


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
    @RequestMapping(value = "promotion_plan_preview_confirmation_3.2")
    @ResponseBody
    public JSONObject promotionPlanPreviewConfirmation3p2(String taskId, String procInsId, String splitId, String confirm) {

        return jykFlowDiagnosis3p2Service.promotionPlanPreviewConfirmation3p2(taskId, procInsId, splitId, confirm);
    };


    /**
     * 
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
    @RequestMapping(value = "promotion_plan_review_3.2")
    @ResponseBody
    public JSONObject promotionPlanReview3p2(String taskId, String procInsId, String splitId, String friendConfirm, String weiboConfirm,
                    String momoConfirm, String launchinfo) {

        return jykFlowDiagnosis3p2Service.promotionPlanReview3p2(taskId, procInsId, splitId, friendConfirm, weiboConfirm, momoConfirm, launchinfo);
    };

    /**
     * 最终确认推广计划
     *
     * @param taskId
     * @param procInsId
     * @param splitId
     * @param confirminfo
     * @return
     * @date 2018年5月10日
     * @author zjq
     */
    @RequestMapping(value = "finally_confirm_launch_time_3.2")
    @ResponseBody
    public JSONObject finallyConfirmLaunchTime3p2(String taskId, String procInsId, String splitId, String confirminfo) {
        String newJson = StringEscapeUtils.unescapeHtml4(confirminfo);
        JSONObject orderJsonObject = JSONObject.parseObject(newJson);
        return jykFlowDiagnosis3p2Service.finallyConfirmLaunchTime3p2(taskId, procInsId, splitId, orderJsonObject);
    };


    /**
     * 推广上线 朋友圈
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
    @RequestMapping(value = "promote_online_friends_3.2")
    @ResponseBody
    public JSONObject promoteOnlineFriends3p2(String taskId, String procInsId, String splitId, String[] outerImgFriends, String[] innerImgFriends,
                    String promoteDate) {

        return jykFlowDiagnosis3p2Service.promoteOnlineFriends3p2(taskId, procInsId, splitId, outerImgFriends, innerImgFriends, promoteDate);
    };

    /**
     * 推广上线 微博
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
    @RequestMapping(value = "promote_online_microblog_3.2")
    @ResponseBody
    public JSONObject promoteOnlineMicroblog3p2(String taskId, String procInsId, String splitId, String[] outerImgWeibo, String[] innerImgWeibo,
                    String promoteDate) {

        return jykFlowDiagnosis3p2Service.promoteOnlineMicroblog3p2(taskId, procInsId, splitId, outerImgWeibo, innerImgWeibo, promoteDate);
    };

    /**
     * 推广上线 陌陌
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
    @RequestMapping(value = "promote_online_momo_3.2")
    @ResponseBody
    public JSONObject promoteOnlineMomo3p2(String taskId, String procInsId, String splitId, String[] outerImgMomo, String[] innerImgMomo,
                    String promoteDate) {

        return jykFlowDiagnosis3p2Service.promoteOnlineMomo3p2(taskId, procInsId, splitId, outerImgMomo, innerImgMomo, promoteDate);
    };


    @RequestMapping(value = "management_diagnosis_marketing_planning_3.2")
    public @ResponseBody BaseResult managementDiagnosisMarketingPlanningV3p2(String splitId, String taskId, String procInsId,String proposalAudit) {
        return jykFlowDiagnosis3p2Service.managementDiagnosisMarketingPlanningV3p2(taskId, procInsId, splitId,proposalAudit);
    }

    /**
     * 获取用户列表
     *
     * @param roleName
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @date 2018年5月15日
     * @author zjq
     */
    @RequestMapping(value = "userListByRole")
    @ResponseBody
    public JSONArray userListByRole(String roleName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        
        return jykFlowDiagnosis3p2Service.userListByRole(roleName);
    };

}