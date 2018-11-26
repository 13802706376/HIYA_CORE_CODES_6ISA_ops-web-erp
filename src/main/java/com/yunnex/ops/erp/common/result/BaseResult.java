package com.yunnex.ops.erp.common.result;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * 共用的基本返回结果
 * 
 * @author zhangjl
 * @date 2018年1月19日
 */
public class BaseResult implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String CODE_ERROR_ARG = "100002";

    public BaseResult() {}

    public BaseResult(Object attach) {
        this.attach = attach;
    }
    /**
     * 消息编码
     */
    protected String code = "0"; // 默认成功

    /**
     * 消息内容
     */
    protected String message;

    /**
     * 消息附加对象
     */
    protected Object attach;

    /**
     * 判断是否成功
     *
     * @param result 结果
     * @return
     * @date 2018年1月31日
     * @author zhangjl
     */
    public static final boolean isSuccess(BaseResult result) {
        return null != result && "0".equals(result.code);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseResult> T error(String code, String message) {
        this.code = code;
        this.message = message;
        return (T) this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getAttach() {
        return attach;
    }

    public BaseResult setAttach(Object attach) {
        this.attach = attach;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
