package com.yunnex.ops.erp.modules.workflow.flow.common;


/**
 * 聚引客工作流常量定义信息表
 * 
 * @author yunnex
 * @date 2017年10月31日
 */
public class WorkFlowConstants {
    
    /**正常工作时间*/
    public static final String TASK_DATE_HOURS = "taskDateHours";
    /**加急工作时间*/
    public static final String URGENT_TASK_DATEHOURS = "urgentTaskDateHours";
    /**分配用户组*/
    public static final String TASK_USER_ROLE = "taskUserRole";
    /**分配用户组*/
    public static final String TASK_USER_ROLE2 = "taskUserRole2";
    /**正常*/
    public static final String NORMAL = "1";
    /**既将到期*/
    public static final String ONLY_WILL_OVER_TIME = "2";
    /**超时*/
    public static final String OVER_TIME = "3";
    
    /** 当前对象实例 */
    private static WorkFlowConstants jykFlowConstants = new WorkFlowConstants();
    /** 获取当前对象实例 */
    public static WorkFlowConstants getInstance() {
        return jykFlowConstants;
    }


    /** 首次营销策划 */
    public static final String DELIVERY_ITEM_CODE_KCL = "delivery_item_code_kcl"; // 首次营销策划
    /** 上门服务基础版 */
    public static final String DELIVERY_ITEM_CODE_KCL_BASIC = "delivery_item_code_kcl_basic"; // 上门服务基础版
    /** 聚引客交付 */
    public static final String DELIVERY_ITEM_CODE_JYK = "delivery_item_code_jyk"; // 聚引客交付
    /** 售后上门培训付费服务流程 */
    public static final String DELIVERY_ITEM_CODE_VC = "delivery_item_code_vc"; // 售后上门培训付费服务流程
    /** 智慧餐厅 */
    public static final String DELIVERY_ITEM_CODE_ZHCT = "delivery_item_code_zhct"; // 智慧餐厅
    /** 物料更新服务 */
    public static final String DELIVERY_ITEM_CODE_MU = "delivery_item_code_mu"; // 物料更新服务
    /** 掌贝平台交付服务 */
    public static final String DELIVERY_ITEM_CODE_ZB = "delivery_item_code_zb"; // 掌贝平台交付服务

}
