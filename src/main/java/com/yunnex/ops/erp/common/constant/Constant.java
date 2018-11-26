package com.yunnex.ops.erp.common.constant;

public interface Constant {

    String YES = "Y";
    String NO = "N";

    // "+"
    String PLUS = "+";
    // "-"
    String DASH = "-";
    // 下划线
    String UNDERLINE = "_";
    // =
    String EQUAL = "=";
    // 句号，点号 "."
    String POINT = ".";
    // 半角逗号","
    String COMMA = ",";
    // 全角逗号"，"
    String COMMA_FULL = "，";
    // 一个空格
    String SPACE = " ";
    // 空字符串
    String BLANK = "";
    // 顿号
    String DUN_HAO = "、";
    // 竖线"|"
    String VERTICAL_LINE = "|";
    // 斜杠
    String SPRIT = "/";
    // 百分号
    String PERCENT = "%";
    // 问号
    String QUESTION = "?";

    // 空
    Integer ZERO = 0;
    // 1
    Integer ONE = 1;
    // -1
    Integer NEGATIVE_ONE = -1;

    String SEMICOLON = ";";
    String ASTERISK = "*";

    String CHARSET_GBK = "GBK";
    String CHARSET_UTF8 = "UTF-8";

    // 原始订单默认版本号
    String ORDER_VERSION = "3.0";
    String ORDER_VERSION_2 = "2.0";
    // 默认分单序号
    Integer DEFUAL_SPLIT_NUMBER = 0;
    
    String INCUMBENCY = "0";//在职
    String DIMISSION = "1";//离职

    // 可用
    String STATE_ENABLE = "enable";
    // 不可用
    String STATE_DISENABLE = "disable";

    /**
     * <系统角色>订单管理专员 系统自动分单的默认处理人
     */
    String ORDER_MANAGER_COMMISSIONER = "order_manager_commissioner";
    /**
     * <系统角色>策划专家
     */
    public static final String PLANNING_PERSON = "planning_person";

    /**
     * <系统角色>提案内审
     */
    public static final String PROPOSAL_AUDIT = "proposal_internal_auditor";
    /**
     * <系统角色>设计师接口人
     */
    public static final String DESIGNER_INTERFACE_PERSON = "designer_interface_person";
    // 聚引客字典 code juyinke
    String JYK_ORDER_GOOD_TYPECODE = "juyinke";
    String KCL_ORDER_GOOD_TYPECODE = "kehcanglai";

    String ORDER_VERSION_DICT_CODE = "order_version";

    // 推广通道 1：朋友圈 2：微博 3：陌陌
    /**
     * 推广通道 1：朋友圈
     */
    String CHANNEL_1 = "1";
    /**
     * 推广通道 1：微博
     */
    String CHANNEL_2 = "2";
    /**
     * 推广通道 1：陌陌
     */
    String CHANNEL_3 = "3";

    /**
     * 字符常量
     */
    String STRCONSTANT = "1";

}
