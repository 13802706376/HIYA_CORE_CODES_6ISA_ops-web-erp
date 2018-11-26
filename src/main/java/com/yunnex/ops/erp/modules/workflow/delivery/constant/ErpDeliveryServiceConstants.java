package com.yunnex.ops.erp.modules.workflow.delivery.constant;

/**
 * 生产服务常量
 * 
 * @author linqunzhi
 * @date 2018年5月31日
 */
public interface ErpDeliveryServiceConstants {

    /** 交付流程中相关工作日（系统常量表中的key） */
    String DELIVERY_SERVICE_WORK_DAYS = "delivery_service_work_days";
    
    /** 交付流程中相关工作日（系统常量表中的key） */
    String DELIVERY_SERVICE_ZHCT_DAYS = "delivery_service_zhct_days";
    
    /** 物料（系统常量表中的key） */
    String DELIVERY_SERVICE_MATERIAL_DAYS = "delivery_service_material_days";
    /** 售后（系统常量表中的key） */
    String DELIVERY_SERVICE_VC_DAYS = "delivery_service_vc_days";

    /** 流程类型：首次营销服务(First marketing plan service) */
    String SERVICE_TYPE_FMPS = "FMPS";

    /** 流程类型：首次上门服务（基础版） */
    String SERVICE_TYPE_FMPS_BASIC = "FMPS_BASIC";

    // 处理状态：其他
    int STATUS_FINISH = 0;
    // 处理状态：已取消
    int STATUS_CANCEL = 2;
}
