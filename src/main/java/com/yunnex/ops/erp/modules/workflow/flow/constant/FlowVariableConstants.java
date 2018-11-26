package com.yunnex.ops.erp.modules.workflow.flow.constant;

/**
 * 流程变量常量类
 * 
 * @author linqunzhi
 * @date 2018年8月17日
 */
public interface FlowVariableConstants {

    /** 交付服务变量 */
    interface Delivery {

        /** 是否需要上门标识 Y：是,N:否 */
        String VISIT_SERVICE_FLAG = "visitServiceFlag";
    }
    
    /** 流程中包含的服务集合 */
    String GOOD_SERVIE_TYPE_LIST = "goodServieTypeList";

}
