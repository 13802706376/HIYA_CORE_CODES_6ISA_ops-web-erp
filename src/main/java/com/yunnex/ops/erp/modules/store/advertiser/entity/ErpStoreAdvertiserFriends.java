package com.yunnex.ops.erp.modules.store.advertiser.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.ActEntity;

/**
 * 朋友圈广告主开通资料Entity
 * 
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpStoreAdvertiserFriends extends ActEntity<ErpStoreAdvertiserFriends> {

    private static final long serialVersionUID = 1L;
    private Integer provideAccountInfo = 0; // 提供公众号账号、密码,0:否,1:是,默认0
    private String accountNo; // 公众号登录账号
    private String accountPassword; // 公众号登录密码
    private String accountOriginalId; // 公众号原始ID
    private String advertiserScreenshot; // 广告主开通截图
    private String storeScreenshot; // 门店开通截图
    private Integer auditStatus = 0; // 审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0
    private String auditContent;
    private String procInsId; // 流程id
    private String shopInfoId;// 对应商户ID
    // 封装字段
    private String specialCertificate; // 特殊资质

    public ErpStoreAdvertiserFriends() {
        super();
    }

    public ErpStoreAdvertiserFriends(String id) {
        super(id);
    }

    public String getAuditContent() {
        return auditContent;
    }

    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    @NotNull(message = "提供公众号账号、密码,0:否,1:是,默认0不能为空")
    public Integer getProvideAccountInfo() {
        return provideAccountInfo;
    }

    public void setProvideAccountInfo(Integer provideAccountInfo) {
        this.provideAccountInfo = provideAccountInfo;
    }

    @Length(min = 0, max = 64, message = "公众号登录账号长度必须介于 0 和 64 之间")
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    @Length(min = 0, max = 64, message = "公众号登录密码长度必须介于 0 和 64 之间")
    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    @Length(min = 0, max = 64, message = "公众号原始ID长度必须介于 0 和 64 之间")
    public String getAccountOriginalId() {
        return accountOriginalId;
    }

    public void setAccountOriginalId(String accountOriginalId) {
        this.accountOriginalId = accountOriginalId;
    }

    @Length(min = 0, max = 255, message = "广告主开通截图长度必须介于 0 和 255 之间")
    public String getAdvertiserScreenshot() {
        return advertiserScreenshot;
    }

    public void setAdvertiserScreenshot(String advertiserScreenshot) {
        this.advertiserScreenshot = advertiserScreenshot;
    }

    @Length(min = 0, max = 255, message = "门店开通截图长度必须介于 0 和 255 之间")
    public String getStoreScreenshot() {
        return storeScreenshot;
    }

    public void setStoreScreenshot(String storeScreenshot) {
        this.storeScreenshot = storeScreenshot;
    }

    public String getSpecialCertificate() {
        return specialCertificate;
    }

    public void setSpecialCertificate(String specialCertificate) {
        this.specialCertificate = specialCertificate;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getShopInfoId() {
        return shopInfoId;
    }

    public void setShopInfoId(String shopInfoId) {
        this.shopInfoId = shopInfoId;
    }



}
