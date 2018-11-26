package com.yunnex.ops.erp.modules.statistics.enums;

/**
 * 生产服务报表 列
 * 
 * @author linqunzhi
 * @date 2018年5月28日
 */
public enum DeliveryServiceStatisticsColumn {

    AGENT_NAME("agentName", "所属服务商"), OPEN_DELAY_FLAG("openDelayFlag", "开户顾问是否延迟"), MATERIEL_DELAY_FLAG("materielDelayFlag",
                    "物料顾问是否延迟"), OPERATION_DELAY_FLAG("operationDelayFlag", "运营顾问是否延迟"), OPEN_DELAY_DURATION("openDelayDuration",
                                    "开户顾问延迟时长"), MATERIEL_DELAY_DURATION("materielDelayDuration",
                                                    "物料顾问延迟时长"), OPERATION_DELAY_DURATION("operationDelayDuration", "运营顾问延迟时长");

    /** 列 code */
    private String code;

    /** 列名 */
    private String name;

    private DeliveryServiceStatisticsColumn(String code, String name) {
        this.code = code;
        this.name = name;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
