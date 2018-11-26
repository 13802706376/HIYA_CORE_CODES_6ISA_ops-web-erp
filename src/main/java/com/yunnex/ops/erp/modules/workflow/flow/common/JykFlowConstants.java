package com.yunnex.ops.erp.modules.workflow.flow.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 聚引客工作流常量定义信息表
 * 
 * @author yunnex
 * @date 2017年10月31日
 */
public class JykFlowConstants {
    
    /** 策划专家接口人(流程启动处理用户) */
    public static final String PLANNING_EXPERT_INTERFACE_MAN = "PlanningExpertInterfaceMan";
    /** 策划专家 */
    public static final String Planning_Expert = "PlanningExpert";
    /** 运营经理 */
    public static final String OPERATION_MANAGER="operationManager";
    /** 运营顾问 */
    public static final String OPERATION_ADVISER="OperationAdviser";
    /** 文案设计接口人 */
    public static final String assignTextDesignInterfacePerson="assignTextDesignInterfacePerson";
    /** 指派投放顾问接口人 */
    public static final String assignConsultantInterface="assignConsultantInterface";
    
    /** 文案设计 */
    public static final String assignTextDesignPerson="assignTextDesignPerson";
    /** 设计师 */
    public static final String designer="designer";
    /** 设计师接口人 */
    public static final String designerInterface = "designerInterface";
    /** 投放顾问 */
    public static final String assignConsultant="assignConsultant";
    /** 业管-朋友圈开户 */
    public static final String pipeIndustryFriends="pipeIndustryFriends";
    /** 业管-微博开户 */
    public static final String pipeIndustryWeibo="pipeIndustryWeibo";
    /** 业管-陌陌开户 */
    public static final String pipeIndustryMomo="pipeIndustryMomo";
    /** 订单管理专员 **/
    public static final String orderCommissioner = "OrderCommissioner";
    /** 微博充值审核员 */
    public static final String weiboRechargeCommissioner="weiboRechargeCommissioner";
    /** 内审专员 */
    public static final String proposalAudit = "proposalAudit";
    /** 设计师接口人 */
    public static final String designerInterfacePerson = "designerInterfacePerson";

    /** 开户顾问 */
    public static final String ACCOUNT_ADVISER="accountAdviser";
    /** 物料顾问 */
    public static final String MATERIAL_ADVISER="materialAdviser";
    /** 聚引客流程 */
    public static final String JYK_FLOW="jyk_flow";
    

    public static final String TASK_USER = "TaskUser";

    public static final String promotionTimeDetermination ="promotion_time_determination";
    
    public static final String MICROBLOG_PROMOTE_INFO_REVIEW_ZHIXIAO_LATEST = "microblog_promote_info_review_zhixiao_latest";
    
    public static final  String  MICROBLO_RECHARGE_FINISH= "microblog_recharge_finish_3.2";

    /** 投放日期距离现在时间超于20个工作日的订单任务 */
    public static final int PLANNING_DATE_DISTINCT=20;
    

    /** 当前对象实例 */
    private static JykFlowConstants jykFlowConstants = new JykFlowConstants();

    /** 获取当前对象实例 */
    public static JykFlowConstants getInstance() {
        return jykFlowConstants;
    }

    /** 指派策划专家 task def **/
    public static final String ASSIGNE_PLANNING_EXPERT = "assigne_planning_expert";
    /** 推广确认 服务商 3.1 task def **/
    public static final String CONTACT_SHOP_SERVICE_LATEST = "contact_shop_service_latest";
    /** 推广确认 直销 3.1 task def **/
    public static final String CONTACT_SHOP_ZHIXIAO_LATEST = "contact_shop_zhixiao_latest";
    /** 推广确认 服务商 3.0 task def **/
    public static final String CONTACT_SHOP_SERVICE = "contact_shop_service";
    /** 推广确认 直销 3.0 task def **/
    public static final String CONTACT_SHOP_ZHIXIAO = "contact_shop_zhixiao";

    /** 推广确认 3.5 task def **/
    public static final String CONTACT_SHOP_3P5 = "contact_shop_3.5";

    public static final String CHOOSE_FRIEND_FLAG = "chooseFriendFlag";
    public static final String CHOOSE_MICROBLOG_FLAG = "chooseMicroblogFlag";
    public static final String CHOOSE_MOMO_FLAG = "chooseMomoFlag";
    public static final String STORE_FRIENDS_HISFLAG = "storeFriendsHisFlag";
    public static final String STORE_MICROBLOG_HISFLAG = "storeMicroblogHisFlag";
    public static final String STORE_MOMO_HISFLAG = "storeMomoHisFlag";
    public static final String SHOP_FRIENDS_HISFLAG = "shopFriendsHisFlag";
    public static final String SHOP_MICROBLOG_HISFLAG = "shopMicroblogHisFlag";
    public static final String SHOP_MOMO_HISFLAG = "shopMomoHisFlag";


    public static Map<String, String> flowMap = new HashMap<>();

    static {
        // workflow_remarks_info 流程类型对应的缩写
        flowMap.put("jyk_flow", "J");
        flowMap.put("sdi_flow", "D");
        flowMap.put("payInto_flow", "P");
        flowMap.put("promote_info_flow", "K");
        flowMap.put("delivery_service_flow", "E");
        flowMap.put("visit_service_flow", "V");
    }


}
