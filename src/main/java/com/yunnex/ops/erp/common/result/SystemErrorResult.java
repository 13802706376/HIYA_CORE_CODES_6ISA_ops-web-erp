package com.yunnex.ops.erp.common.result;

/**
 * 系统异常返回结果
 * 
 * @author zhangjl
 * @date 2018年1月19日
 */
public class SystemErrorResult extends BaseResult {
    private static final long serialVersionUID = 1L;

    public SystemErrorResult() {
        super();

        this.code = "100001";
        this.message = "系统异常";
    }

    public SystemErrorResult(String msg) {
        this.message = msg;
    }

}
