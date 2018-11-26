package com.yunnex.ops.erp.modules.workflow.flow.constant;

public interface FlowConstant {
    /**
     * 暂停原因字典
     */
    String SUSPEND_REASON = "suspend_reason";

    /**
     * 流程异常原因：订单取消
     */
    String EXCEPTION_REASON_CANCEL_ORDER = "订单已取消";

    String MESSAGE = "message";
    String RESULT = "result";
    boolean RESULT_TRUE = true;
    boolean RESULT_FALSE = false;
    
    String STRING_0 = "0";
    String STRING_1 = "1";
    String STRING_2 = "2";
    
    /**
     * 智慧餐厅上门目的
     */
    String ZHCT_GOAL  ="9";

    /**文本类型：普通类型*/
    String FLOW_FORM_DATA_ATTR_TYPE_NORMAL = "NORMAL";
    /**文本类型：富文本类型*/
    String FLOW_FORM_DATA_ATTR_TYPE_TEXT = "TEXT";
    
    String PROCINSID="procInsId";
    
    // 预览图片标识
    String CHANNEL_OUTERIMGFRIENDS = "outerImgFriends"; // 朋友圈外层入口截图
    String CHANNEL_INNERIMGFRIENDS = "innerImgFriends"; // 朋友圈落地页截图
    String CHANNEL_OUTERIMGWEIBO = "outerImgWeibo"; // 微博外层入口截图
    String CHANNEL_INNERIMGWEIBO = "innerImgWeibo"; // 微博落地页截图
    String CHANNEL_OUTERIMGMOMO = "outerImgMomo"; // 陌陌外层入口截图
    String CHANNEL_INNERIMGMOMO = "innerImgMomo"; // 陌陌落地页截图

    // 推广上线监测
   String PROMOTE_ONLINE_MONITOR_MOMO = "promote_online_monitor_momo";
   String PROMOTE_ONLINE_MONITOR_MICROBLOG = "promote_online_monitor_microblog";
   String PROMOTE_ONLINE_MONITOR_FRIENDS = "promote_online_monitor_friends";
    // 推广上线
     String PROMOTE_ONLINE_MOMO = "promote_online_momo";
     String PROMOTE_ONLINE_MICROBLOG = "promote_online_microblog";
     String PROMOTE_ONLINE_FRIENDS = "promote_online_friends";

    // 推广提案提审
     String PROMOTION_PROPOSAL_AUDIT = "promotion_proposal_audit_";
    // 修改推广提案
    String MODIFY_PROMOTION_PROPOSAL = "modify_promotion_proposal";
    // 指派策划专家
    String ASSIGNE_PLANNING_EXPERT = "assigne_planning_expert";
    // 经营诊断
   String MANAGEMENT_DIAGNOSIS_MARKETING_PLANNING = "management_diagnosis_marketing_planning_";
    // 推广方案预览确认
     String PROMOTION_PLAN_PREVIEW_CONFIRMATION = "promotion_plan_preview_confirmation_";
    // 输出推广页面预览给策划专家
   String WORK_PROMOTION_SCHEME_PREVIEW = "work_promotion_scheme_preview_";
    // 输出设计稿
    String OUTPUT_DESIGN_DRAFT = "output_design_draft_";
    // 输出文案
    String OUTPUT_OFFICIAL_DOCUMENTS = "output_official_documents_";
    // 修改设计稿
    String MODIFY_DESIGN_DRAFT = "modify_design_draft_";
    // 修改文案
     String MODIFY_OFFICIAL_DOCUMENTS = "modify_official_documents_";
    // 审核设计稿
    String REVIEW_DESIGN_DRAFT = "review_design_draft_";
    // 审核文案
     String REVIEW_OFFICIAL_DOCUMENTS = "review_official_documents_";

    /** 首次审核订单 */
   String ORDER_REVIEW_FIRST = "order_review_first";
    /** 二次订单审核 */
    String ORDER_REVIEW_SECOND = "order_review_second";
    /** 修改/删除订单 */
    String MODIFY_ORDER_INFO = "modify_order_info";

    /**等待推广状态同步*/
    String WAITING_PROMOTION_STATE_SYNC = "waiting_promotion_state_sync_3.2";

    /**微博充值审核结果*/
   String  MICROBLOG_RECHARGE_REVIEW_RESULT = "microblogRechargeReviewResult";
    /**是否确认微博推广充值 */
    String CHOOSE_MICROBLOG_RECHARGE_FLAG =  "chooseMicroblogRechargeFlag";
    
    /**确认投放信息*/
   String PUTINFO_CHECKBOX = "putInfoCheckBox";
    /**确认最终推广上线时间*/
    String PROMOTION_TIME_CHECKBOX = "promotionTimeCheckBox";
    /** 确认广告主开户成功通知商户*/
    String NOTIFY_SHOP_CHECKBOX = "notifyShopCheckBox";
    /** 确认 首日投放数据简报给通知商户*/
    String FIRST_DAY_PROMOTE_DATA_INFORM_SHOP="firstDayPromoteDataInformShop";
    /** 推广状态同步*/
    String INFORM_SHOP_UP_LINE= "InformShopUpLine";
    /** 讲效果报告上传至OEM后台，并修改推广状态*/
     String  EFFECT_REPORT_UPLOAD_OEM= "effectReportuploadOEM";
    /**效果报告通知商户查看 */
   String  EFFECT_REPORT_INFORM_SHOP_CHECK= "effectReportInformShopCheck";
    /**是否完成卡券信息输入 */
     String  IS_CARD_INFO_INPUT="isCardInfoInput";
    /**确认是否进行微博充值*/
    String IS_SURE_RECHARGE = "isSureRecharge";
    
    
    /**微博充值审核 */
    String MICROBLO_RECHARGE_CHECK = "microblogRechargeCheck";
   
    String MICROBLO_RECHARGE_CHECK_FAIL_REASON =  "microblogRechargeCheckFailReason";
    
    /**朋友圈开户审核 */
    String FRIENDS_PROMOTION_FLOW_CHECK_V1 = "friendsPromotionFlowCheckV1";
    String FRIENDS_PROMOTION_FLOW_CHECK_FAIL_REASON_V1 =  "friendsPromotionFlowCheckFailReasonV1";
    
    /**微博开户审核 */
    String MICROBLOG_PROMOTION_FLOW_CHECK_V1 = "microblogPromotionFlowCheckV1";
    String MICROBLOG_PROMOTION_FLOW_CHECK_FAIL_REASON_V1 =  "microblogPromotionFlowCheckFailReasonV1";
    
    /** 开通服务 **/
     String ACCOUNT_PAY_OPEN = "account_pay_open";
    /** 营销策划 **/
    String MARKETING_PLANNING = "marketing_planning";
    /** 服务启动 **/
    String SERVICE_STARTUP = "service_startup";
    /** 物料服务 **/
    String MATERIAL_SERVICE = "material_service";
    /** 服务商类型 **/
     String AGENTTYPE = "agentType";

    String SERVICETYPE = "serviceType";

     String FINISH = "finish";
     String RUNNING = "running";
    
    String  SERVICE_SOURCE_ID="service_source_id";
    String  HAS_START_FLOW="has_start_flow";
    
    // ================================流程角色
    /** 订单初审员 **/
     String FIRST_ORDER_AUDITOR_FLOWROLE = "firstOrderAuditor";
    /** 订单创建人 **/
   String ORDER_CREATOR_FLOWROLE = "orderCreator";
    /** 二次审核员 **/
    String SECOND_ORDER_AUDITOR_FLOWROLE = "secondOrderAuditor";
    // ================================系统角色
    /** 订单初审员 **/
    String FIRST_ORDER_AUDITOR_SYSROLE = "first_order_auditor";
    /** 二次审核员 **/
     String SECOND_ORDER_AUDITOR_SYSROLE = "second_order_auditor";
    /** 订单创建人 **/
    String ORDER_CREATOR_SYSROLE = "order_creator";

    /**
     * 服务流程类型
     * 
     * @author linqunzhi
     * @date 2018年7月10日
     */
    interface ServiceType {

        /** 引流推广服务 */
        String SPLIT_JU_YIN_KE = "SplitJuYinKe";

    }
    /**
     * question类型
     */
    interface QuestionType {
        /** 多选题 */
        String SELECT_MULTIPLE = "SelectMultiple";
        /** 多选表格 */
        String SELECT_MULTIPLE_TABLE = "SelectMultipleTable";

    }
}
