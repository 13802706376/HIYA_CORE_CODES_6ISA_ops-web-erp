package com.yunnex.ops.erp.common.result;

/**
 * 系统内部访问超时的错误返回结果
 * 
 * @date 2018年1月19日
 */
public class InnerTimeoutErrorResult extends BaseResult {
    private static final long serialVersionUID = 1L;

    public InnerTimeoutErrorResult() {
        super();

        this.code = "100003";
        this.message = "系统内部访问超时";
    }
}
