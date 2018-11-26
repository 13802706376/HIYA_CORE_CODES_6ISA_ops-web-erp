package com.yunnex.ops.erp.modules.statistics.enums;

/**
 * 生产服务报表 查询条件
 * 
 * @author linqunzhi
 * @date 2018年5月29日
 */
public enum DeliveryServiceStatisticsQuery {

    ORDER_TYPE("orderType", "订单类型");


    /** 列 code */
    private String code;

    /** 列名 */
    private String name;

    private DeliveryServiceStatisticsQuery(String code, String name) {
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
