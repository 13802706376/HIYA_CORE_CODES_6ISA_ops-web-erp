package com.yunnex.ops.erp.common.result;

/**
 * 资源不存在的错误返回结果
 * 
 * @date 2018年1月19日
 */
public class ResourceUnexistErrorResult extends BaseResult {
    private static final long serialVersionUID = 1L;

    public ResourceUnexistErrorResult() {
        super();

        this.code = "100006";
        this.message = "资源不存在";
    }

    public ResourceUnexistErrorResult(String msg) {
        this();
        this.message = msg;
    }
}
