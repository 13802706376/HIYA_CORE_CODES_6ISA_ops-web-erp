package com.yunnex.ops.erp.modules.store.pay.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.modules.store.basic.entity.BankEnum;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreCredentials;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLegalPerson;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLinkman;

/**
 * 微信支付开通资料Entity
 * 
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpStorePayWeixin extends DataEntity<ErpStorePayWeixin> {

    private static final long serialVersionUID = 1L;
    private String bankId; // 银行ID
    private Integer provideAccountInfo = 0; // 提供公众号账号、密码,0:否,1:是,默认0
    private String publicAccountNo; // 公众号登录账号
    private String publicAccountPassword; // 公众号登录密码
    private String publicAccountAppid; // 公众号APPID
    private String publicAccountName; // 公众号名称
    private Integer auditStatus = 0; // 审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0
    private String auditContent; // 审核意见
    private String multiAccountApplicationForm;// 多账号申请表
    private ErpStoreBank bank;
    private String number;

    /** 邮箱账号 */
    private String emailNo;

    /** 邮箱密码 */
    private String emailPassword;

    /** 运营人员身份证 */
    private String operatorIdcard;

    /** 运营人员邮箱 */
    private String operatorEmail;

    /** 运营人员手机号 */
    private String operatorMobile;

    /** 运营人员名称 */
    private String operatorName;

    /** 微信号 */
    private String weixinNo;


    /**
     * 是否OEM同步(Y:是N:否)
     */
    private String syncOem;

    // OEM传值用字段
    private String shortName;// 门店简称
    private String telephone;// 门店电话
    private String companyUrl; // 公司网址
    private Integer businesscategory;// 经营类目
    private Integer businessType = 0; // 商户类型，1：个体工商商户，2：企业商户，默认0
    private ErpStoreLinkman linkman;// 门店联系人对象
    private ErpStoreLegalPerson legalPerson;// 门店法人对象
    private ErpStoreCredentials credentials;// 营业资质对象

    public ErpStorePayWeixin() {
        super();
    }

    public ErpStorePayWeixin(String id) {
        super(id);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    private List<BankEnum> bankenum = new ArrayList<BankEnum>();// 银行枚举list


    public List<BankEnum> getBankenum() {
        return bankenum;
    }

    public void setBankenum(List<BankEnum> bankenum) {
        this.bankenum = bankenum;
    }

    public String getAuditContent() {
        return auditContent;
    }

    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }

    public ErpStoreBank getBank() {
        return bank;
    }

    public void setBank(ErpStoreBank bank) {
        this.bank = bank;
    }

    @Length(min = 0, max = 64, message = "支付银行ID长度必须介于 0 和 64 之间")
    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    @NotNull(message = "提供公众号账号、密码,0:否,1:是,默认0不能为空")
    public Integer getProvideAccountInfo() {
        return provideAccountInfo;
    }

    public void setProvideAccountInfo(Integer provideAccountInfo) {
        this.provideAccountInfo = provideAccountInfo;
    }

    @Length(min = 0, max = 64, message = "公众号登录账号长度必须介于 0 和 64 之间")
    public String getPublicAccountNo() {
        return publicAccountNo;
    }

    public void setPublicAccountNo(String publicAccountNo) {
        this.publicAccountNo = publicAccountNo;
    }

    @Length(min = 0, max = 64, message = "公众号登录密码长度必须介于 0 和 64 之间")
    public String getPublicAccountPassword() {
        return publicAccountPassword;
    }

    public void setPublicAccountPassword(String publicAccountPassword) {
        this.publicAccountPassword = publicAccountPassword;
    }

    @Length(min = 0, max = 64, message = "公众号APPID长度必须介于 0 和 64 之间")
    public String getPublicAccountAppid() {
        return publicAccountAppid;
    }

    public void setPublicAccountAppid(String publicAccountAppid) {
        this.publicAccountAppid = publicAccountAppid;
    }

    public String getPublicAccountName() {
        return publicAccountName;
    }

    public void setPublicAccountName(String publicAccountName) {
        this.publicAccountName = publicAccountName;
    }

    @NotNull(message = "审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0不能为空")
    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getSyncOem() {
        return syncOem;
    }

    public void setSyncOem(String syncOem) {
        this.syncOem = syncOem;
    }

    public ErpStoreLinkman getLinkman() {
        return linkman;
    }

    public void setLinkman(ErpStoreLinkman linkman) {
        this.linkman = linkman;
    }

    public Integer getBusinesscategory() {
        return businesscategory;
    }

    public void setBusinesscategory(Integer businesscategory) {
        this.businesscategory = businesscategory;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public ErpStoreLegalPerson getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(ErpStoreLegalPerson legalPerson) {
        this.legalPerson = legalPerson;
    }

    public ErpStoreCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(ErpStoreCredentials credentials) {
        this.credentials = credentials;
    }

    public String getEmailNo() {
        return emailNo;
    }

    public void setEmailNo(String emailNo) {
        this.emailNo = emailNo;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getOperatorIdcard() {
        return operatorIdcard;
    }

    public void setOperatorIdcard(String operatorIdcard) {
        this.operatorIdcard = operatorIdcard;
    }

    public String getOperatorEmail() {
        return operatorEmail;
    }

    public void setOperatorEmail(String operatorEmail) {
        this.operatorEmail = operatorEmail;
    }

    public String getOperatorMobile() {
        return operatorMobile;
    }

    public void setOperatorMobile(String operatorMobile) {
        this.operatorMobile = operatorMobile;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getWeixinNo() {
        return weixinNo;
    }

    public void setWeixinNo(String weixinNo) {
        this.weixinNo = weixinNo;
    }

    public String getMultiAccountApplicationForm() {
        return multiAccountApplicationForm;
    }

    public void setMultiAccountApplicationForm(String multiAccountApplicationForm) {
        this.multiAccountApplicationForm = multiAccountApplicationForm;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
