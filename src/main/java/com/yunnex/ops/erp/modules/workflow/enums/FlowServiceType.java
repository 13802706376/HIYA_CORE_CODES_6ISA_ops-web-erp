package com.yunnex.ops.erp.modules.workflow.enums;

/**
 * 流程服务类型 enum
 * 
 * @author linqunzhi
 * @date 2018年7月10日
 */
public enum FlowServiceType {

    SPLIT_JU_YIN_KE("SplitJuYinKe", "引流推广服务"), 
    DELIVERY_FMPS("FMPS", "智能客流运营全套落地服务"),
    DELIVERY_FMPS_BASIC("FMPS_BASIC","首次上门服务基础版"), 
    DELIVERY_JYK("JYK", "聚引客交付服务"), 
    DELIVERY_INTO_PIECES("INTO_PIECES", "掌贝平台交付服务"), ZHI_HUI_CAN_TING("ZhiHuiCanTing", "智慧餐厅安装交付服务");


    private String type;

    private String name;

    private FlowServiceType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * 根据服务类型 获取对象
     *
     * @param type
     * @return
     * @date 2018年7月11日
     * @author linqunzhi
     */
    public static FlowServiceType getByType(String type) {
        for (FlowServiceType em : FlowServiceType.values()) {
            if (em.getType().equals(type)) {
                return em;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
