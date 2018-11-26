package com.yunnex.ops.erp.modules.order.constant;

/**
 * 订单常量
 * 
 * @author linqunzhi
 * @date 2018年5月9日
 */
public interface OrderConstants {

    // 直销类型
    int TYPE_DIRECT = 1;// NOSONAR  

    // 服务商类型
    int TYPE_SERVICE = 2;// NOSONAR  
    // 订单结束
    int ORDER_END=2;// NOSONAR  
    // 订单取消
    int ORDER_CANCEL=1;// NOSONAR  
    
    int GOOD_TYPE_ID_JYK = 5; // 聚引客

    int GOOD_TYPE_ID_KCL = 6; // 客常来

    // 订单类别：直销
    Integer ORDER_TYPE_DIRECT = 1;
    // 订单类别：渠道
    Integer ORDER_TYPE_SERVICE = 2;


    String AGENT = "agent";
    /** 所有权限 */
    String SECURITY_ALL = "order:erpDeliveryServiceStatistics:all";
    /** 分公司权限 */
    String SECURITY_BRANCH_COMPANY = "order:erpDeliveryServiceStatistics:branchCompany";
    /** 服务商权限 */
    String SECURITY_SERVICE_COMPANY = "order:erpDeliveryServiceStatistics:serviceCompany";
    /** 所在公司权限 */
    String SECURITY_COMPANY = "order:erpDeliveryServiceStatistics:company";
    
    /** 所有权限 */
    String TEAM_ALL = "order:erpDeliveryServiceStatistics:all";
    /** 分公司权限 */
    String TEAM_BRANCH_COMPANY = "order:erpDeliveryServiceStatistics:branchCompany";
    /** 服务商权限 */
    String TEAM_SERVICE_COMPANY = "order:erpDeliveryServiceStatistics:serviceCompany";
    /** 所在公司权限 */
    String TEAM_COMPANY = "order:erpDeliveryServiceStatistics:company";
    
    /** 作废状态(1：作废) */
    Integer CANCEL_YES = 1;
    /** 作废状态(0：正常) */

    Integer CANCEL_NO = 0;
    /** 订单来源(0：OEM) */
    Integer ORDER_SOURCE_OEM = 0;
    /** 订单来源(1：ERP) */
    Integer ORDER_SOURCE_ERP = 1;
    /** 订单来源"易商"(2: YS) */
    Integer ORDER_SOURCE_YS = 2;

    /** 订单状态：-1取消，0未签约，1已签约，2支付中，3已支付，4进件中，5已进件 */
    Integer ORDER_STATUS_CANCEL = -1;
    /** 订单状态：0未签约，1已签约，2支付中，3已支付，4进件中，5已进件 */
    Integer ORDER_STATUS_3 = 3;
    /** 订单状态：0未签约，1已签约，2支付中，3已支付，4进件中，5已进件 */
    Integer ORDER_STATUS_4 = 4;
    /** 订单状态：0未签约，1已签约，2支付中，3已支付，4进件中，5已进件 */
    Integer ORDER_STATUS_5 = 5;
    /** 订单审核状态：待初审 */
    String ORDER_AUDIT_STATUS_0 = "0";
    /** 订单审核状态：待复审 */
    String ORDER_AUDIT_STATUS_1 = "1";
    /** 订单审核状态：通过 */
    String ORDER_AUDIT_STATUS_2 = "2";
    /** 订单审核状态：驳回 */
    String ORDER_AUDIT_STATUS_3 = "3";


    /** 订单支付状态 3已支付 */
    int PAY_STATUS_3 = 3;
    /** 订单支付状态 4进件中 */
    int PAY_STATUS_4 = 4;
    /** 订单支付状态 5已进件 */
    int PAY_STATUS_5 = 5;

    // 订单类别：首次
    String ORDER_CATEGORY_FIRST = "First";
    // 订单类别：更新
    String ORDER_CATEGORY_UPDATE = "Update";

}
