package com.yunnex.ops.erp.modules.order.constant;

/**
 * 分单常量
 * 
 * @author linqunzhi
 * @date 2018年4月11日
 */
public interface OrderSplitConstants {

    /** 进入待生产库的原因 资质原因 */
    String PENDING_REASON_QUALIFICATION = "Q";

    /** 进入待生产库的原因 延期原因 */
    String PENDING_REASON_DELAY = "D";

    /** 暂停原因 存在资质问题 */
    String SUSPEND_REASON_QUALIFICATION = "qualification_problem";

    /** 暂停原因 商户主动要求延迟上线 */
    String SUSPEND_REASON_DELAY = "ask_for_delay_launch";

    /** 暂停原因 退款中 */
    String SUSPEND_REASON_REFUNDING = "refunding";

    /** 处理中状态 */
    int STATUS_BEGIN = 0;
    // 已完成状态
    int STATUS_FINISH = 1;
    // 已取消状态
    int STATUS_CANCEL = 2;

    /**
     * 无效数据错误编号
     */
    String INVALID_DATA_ERROR = "-1";
    /**
     * 暂停原因字典
     */
    String SUSPEND_REASON = "suspend_reason";

    String MODIFYING = "修改";

    /** 流程版本号302 */
    int PROCESS_VERSION_302 = 302;

}
