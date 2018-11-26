package com.yunnex.ops.erp.common.result;

/**
 * 参数不合法的错误返回结果
 * 
 * @date 2018年1月19日
 */
public class IllegalArgumentErrorResult extends BaseResult {
    private static final long serialVersionUID = 1L;

    public IllegalArgumentErrorResult() {
        super();

        this.code = CODE_ERROR_ARG;
        this.message = "参数不合法";
    }

    public IllegalArgumentErrorResult(String message) {
        super();

        this.code = CODE_ERROR_ARG;
        this.message = message;
    }
}
