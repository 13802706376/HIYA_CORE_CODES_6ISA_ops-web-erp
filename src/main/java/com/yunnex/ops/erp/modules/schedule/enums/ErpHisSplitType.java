package com.yunnex.ops.erp.modules.schedule.enums;

import com.yunnex.ops.erp.common.utils.StringUtils;

/**
 * 生产进度小程序 类型与名称 enum
 * 
 * @author linqunzhi
 * @date 2018年4月26日
 */
public enum ErpHisSplitType {

    ORDER_EFFECTIVE("1", "订单生效"), DESIGNATE_SERVICE("2", "专属服务人员指派中"), EXTENSION_STORE("3", "确认推广门店"), STORE_QUALIFICATION("4",
                    "确认投放门店资质齐全"), DIAGNOSIS_FINISH("5", "经营诊断完成"), MARKETING_PLANNING("6", "确认推广提案"), CREATIVITY_OUTPUT("7",
                                    "推广预览输出中"), CREATIVITY_AFFIRMANCE("8", "推广预览页面确认中"), CREATIVITY_FINISH("9", "推广预览页面确认"), DELIVERY_CHANNEL("10",
                                                    "推广通道确定"), OPEN_ACCOUNT("11", "完成投放平台开户"), PROMOTION_CREATIVITY("12",
                                                                    "推广方案通过投放平台审核"), PROMOTION_ONLINE("13", "推广正式上线"), PROMOTION_SYNC("14",
                                                                                    "首日推广数据同步"), PROMOTION_SYNC_TWO("15",
                                                                                                    "数据报告同步"), EFFECT_OUTPUT("16", "效果报告输出");
    /** 类型 */
    private String type;

    /** 名称 */
    private String name;

    private ErpHisSplitType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * 根据类型获取enum对象
     *
     * @param type
     * @return
     * @date 2018年4月26日
     * @author linqunzhi
     */
    public static ErpHisSplitType getByType(String type) {
        if (StringUtils.isNotBlank(type)) {
            for (ErpHisSplitType obj : ErpHisSplitType.values()) {
                if (obj.getType().equals(type)) {
                    return obj;
                }
            }
        }
        return null;
    }

    /**
     * 根据类型获取名称
     *
     * @param type
     * @return
     * @date 2018年4月26日
     * @author linqunzhi
     */
    public static String getNameByType(String type) {
        ErpHisSplitType obj = getByType(type);
        return obj == null ? null : obj.getName();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }



}
