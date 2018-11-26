package com.yunnex.ops.erp.modules.visit.constants;

/**
 * 上门服务项目类型 常量
 * 
 * @author linqunzhi
 * @date 2018年8月28日
 */
public interface VisitServiceItemConstants {

    /**
     * 上门目的
     * 
     * @author linqunzhi
     * @date 2018年8月28日
     */
    interface Goal {
        /** 智能客流运营全套落地服务 code */
        String FMPS_CODE = "1";
        /** 智能客流运营全套落地服务 上门 code */
        String FMPS_VISIT_CODE = "2";
        /** 聚引客交付服务 code */
        String JYK_CODE = "3";
        /** 首次上门服务基础版 code */
        String FMPS_BASIC_CODE = "7";
        /** 智慧餐厅 code */
        String ZHCT_CODE = "9";
    }

}
