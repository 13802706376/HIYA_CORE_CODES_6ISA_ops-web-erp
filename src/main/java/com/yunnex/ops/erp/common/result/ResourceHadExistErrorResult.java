package com.yunnex.ops.erp.common.result;

/**
 * 资源已存在的错误返回结果
 * 
 * @date 2018年1月19日
 */
public class ResourceHadExistErrorResult extends BaseResult {
    private static final long serialVersionUID = 1L;

    public ResourceHadExistErrorResult() {
        super();

        this.code = "100005";
        this.message = "资源已存在";
    }
}
