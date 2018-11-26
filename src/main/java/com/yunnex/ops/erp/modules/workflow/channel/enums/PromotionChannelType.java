package com.yunnex.ops.erp.modules.workflow.channel.enums;

/**
 * 推广通道 enum
 * 
 * @author linqunzhi
 * @date 2018年7月18日
 */
public enum PromotionChannelType {

    FRIENDS("1", "朋友圈"), WEIBO("2", "微博"), MOMO("3", "陌陌");

    private String code;

    private String name;

    private PromotionChannelType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取em
     *
     * @param code
     * @return
     * @date 2018年7月18日
     * @author linqunzhi
     */
    public static PromotionChannelType getByCode(String code) {
        for (PromotionChannelType em : PromotionChannelType.values()) {
            if (em.getCode() == code) {
                return em;
            }
        }
        return null;
    }

    /**
     * 根据code获取name
     *
     * @param code
     * @return
     * @date 2018年7月18日
     * @author linqunzhi
     */
    public static String getNameByCode(String code) {
        PromotionChannelType em = getByCode(code);
        return em == null ? null : em.getName();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
