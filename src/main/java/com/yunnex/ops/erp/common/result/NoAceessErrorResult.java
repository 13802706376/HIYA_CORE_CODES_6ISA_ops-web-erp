package com.yunnex.ops.erp.common.result;

/**
 * 无权限的错误返回结果
 * 
 * @date 2018年1月19日
 */
public class NoAceessErrorResult extends BaseResult {
    private static final long serialVersionUID = 1L;

    public NoAceessErrorResult() {
        super();

        this.code = "100008";
        this.message = "无权限";
    }
}
