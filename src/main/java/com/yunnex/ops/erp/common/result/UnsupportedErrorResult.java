package com.yunnex.ops.erp.common.result;

/**
 * 不支持的操作的错误返回结果
 * 
 * @date 2018年1月19日
 */
public class UnsupportedErrorResult extends BaseResult {
    private static final long serialVersionUID = 1L;

    public UnsupportedErrorResult() {
        super();

        this.code = "100007";
        this.message = "不支持的操作";
    }

    public UnsupportedErrorResult(String msg) {
        this();
        this.message = msg;
    }
}
