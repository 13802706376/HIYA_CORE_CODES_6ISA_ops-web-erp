package com.yunnex.ops.erp.common.result;

/**
 * 未知错误/通用错误返回结果
 * 
 * @author zhangjl
 * @date 2018年1月19日
 */
public class UnkownErrorResult extends BaseResult {
    private static final long serialVersionUID = 1L;

    public UnkownErrorResult() {
        super();

        this.code = "-1";
        this.message = "系统异常";
    }
}
