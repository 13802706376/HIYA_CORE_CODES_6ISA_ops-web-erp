package com.yunnex.ops.erp.modules.statistics.constant;

/**
 * 分单统计常量
 * 
 * @author linqunzhi
 * @date 2018年5月14日
 */
public interface SplitStatisticsConstants {

    /** 生产中 */
    String SPLIT_STATUS_PRODUCTION_BEGIN = "productionBegin";
    /** 投放中 */
    String SPLIT_STATUS_CIRCULATION_BEGIN = "circulationBegin";
    /** 已完成 */
    String SPLIT_STATUS_FINISH = "finish";
    /** 存在资质问题 */
    String SPLIT_STATUS_QUALIFICATION_PROBLEM = "qualificationProblem";
    /** 商户主动延迟上线 */
    String SPLIT_STATUS_DELAY_LAUNCH = "delayLaunch";
    /** 退款中 */
    String SPLIT_STATUS_DELAY_REFUNDING = "refunding";

    /** 急单 */
    String SPLIT_NATURE_URGENCY = "urgency";
    /** 复购单 */
    String SPLIT_NATURE_REPEAT = "repeat";
    /** 自建单 */
    String SPLIT_NATURE_SELF = "self";

    /** 接入订单 */
    String SPLIT_TYPE_NEW = "new";
    /** 上线订单 */
    String SPLIT_TYPE_ONLINE = "online";
    /** 已完成订单 */
    String SPLIT_TYPE_FINISH = "finish";

    /** 默认开始时间 */
    String DEFAULT_START_DATE_STR = "2017-07-25 00:00:00";

}
