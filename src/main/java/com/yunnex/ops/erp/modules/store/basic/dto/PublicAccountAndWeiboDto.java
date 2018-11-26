package com.yunnex.ops.erp.modules.store.basic.dto;

import java.io.Serializable;

/**
 * 公众号和微博信息
 */
public class PublicAccountAndWeiboDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String storeInfoId; // 门店ID
    private String publicAccountNo; // 公众号登录账号
    private String publicAccountPassword; // 公众号登录密码
    private String publicAccountOriginalId; // 公众号原始ID
    private String publicAccountId;

    private String weiboAccountNo; // 微博登录账号
    private String weiboAccountPassword; // 微博登录密码
    private String weiboAccountId;

    public String getStoreInfoId() {
        return storeInfoId;
    }

    public void setStoreInfoId(String storeInfoId) {
        this.storeInfoId = storeInfoId;
    }

    public String getPublicAccountNo() {
        return publicAccountNo;
    }

    public void setPublicAccountNo(String publicAccountNo) {
        this.publicAccountNo = publicAccountNo;
    }

    public String getPublicAccountPassword() {
        return publicAccountPassword;
    }

    public void setPublicAccountPassword(String publicAccountPassword) {
        this.publicAccountPassword = publicAccountPassword;
    }

    public String getPublicAccountOriginalId() {
        return publicAccountOriginalId;
    }

    public void setPublicAccountOriginalId(String publicAccountOriginalId) {
        this.publicAccountOriginalId = publicAccountOriginalId;
    }

    public String getWeiboAccountNo() {
        return weiboAccountNo;
    }

    public void setWeiboAccountNo(String weiboAccountNo) {
        this.weiboAccountNo = weiboAccountNo;
    }

    public String getWeiboAccountPassword() {
        return weiboAccountPassword;
    }

    public void setWeiboAccountPassword(String weiboAccountPassword) {
        this.weiboAccountPassword = weiboAccountPassword;
    }

    public String getPublicAccountId() {
        return publicAccountId;
    }

    public void setPublicAccountId(String publicAccountId) {
        this.publicAccountId = publicAccountId;
    }

    public String getWeiboAccountId() {
        return weiboAccountId;
    }

    public void setWeiboAccountId(String weiboAccountId) {
        this.weiboAccountId = weiboAccountId;
    }

}
