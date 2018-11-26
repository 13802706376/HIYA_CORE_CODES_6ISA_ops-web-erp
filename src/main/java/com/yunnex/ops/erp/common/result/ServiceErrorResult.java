package com.yunnex.ops.erp.common.result;

/**
 * 服务层通用错误结果
 */
public class ServiceErrorResult extends BaseResult {

    private static final long serialVersionUID = 1L;

    public ServiceErrorResult() {
        super();

        this.code = "100010";
        this.message = "系统异常";
    }

    public ServiceErrorResult(String message) {
        super();

        this.code = "100010";
        this.message = message;
    }

}
