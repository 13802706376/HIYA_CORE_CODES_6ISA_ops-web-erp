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
 * 银联支付开通资料Entity
 * 
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpStorePayUnionpay extends DataEntity<ErpStorePayUnionpay> {

    private static final long serialVersionUID = 1L;
    private Integer auditStatus = 0; // 审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0
    private String auditContent; // 审核意见
    private String bankId; // 银行ID
    private String lianDan; // 银联支付三联单
    private String storePhotoDoorHead; // 门头照
    private String storePhotoCashierDesk; // 收银台照
    private String storePhotoEnvironment; // 店内环境照
    private String additionalPhoto; // 补充资料
    // 机具号
    private String machineToolNumber;
    private String multiAccountApplicationForm;// 多账号申请表
    private ErpStoreBank bank;
    private List<BankEnum> bankenum = new ArrayList<BankEnum>();// 银行枚举list
    private String number;


    /**
     * 是否OEM同步(Y:是N:否)
     */
    private String syncOem;

    // OEM传值用参数-后续可优化成对象传值
    private String address; // 门店经营地址
    private Integer businessType = 0; // 商户类型，1：个体工商商户，2：企业商户，默认0
    private ErpStoreLinkman linkman;
    private ErpStoreLegalPerson legalPerson;// 门店法人对象
    private ErpStoreCredentials credentials;// 营业资质对象

    public ErpStorePayUnionpay() {
        super();
    }

    public ErpStorePayUnionpay(String id) {
        super(id);
    }

    public String getMachineToolNumber() {
        return machineToolNumber;
    }

    public void setMachineToolNumber(String machineToolNumber) {
        this.machineToolNumber = machineToolNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<BankEnum> getBankenum() {
        return bankenum;
    }

    public void setBankenum(List<BankEnum> bankenum) {
        this.bankenum = bankenum;
    }

    public ErpStoreBank getBank() {
        return bank;
    }

    public void setBank(ErpStoreBank bank) {
        this.bank = bank;
    }

    public String getAuditContent() {
        return auditContent;
    }

    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }

    @NotNull(message = "审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0不能为空")
    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    @Length(min = 0, max = 64, message = "支付银行ID长度必须介于 0 和 64 之间")
    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }


    @Length(min = 0, max = 512, message = "银联支付三联单长度必须介于 0 和 512 之间")
    public String getLianDan() {
        return lianDan;
    }

    public void setLianDan(String lianDan) {
        this.lianDan = lianDan;
    }

    @Length(min = 0, max = 255, message = "门头照长度必须介于 0 和 255 之间")
    public String getStorePhotoDoorHead() {
        return storePhotoDoorHead;
    }

    public void setStorePhotoDoorHead(String storePhotoDoorHead) {
        this.storePhotoDoorHead = storePhotoDoorHead;
    }

    @Length(min = 0, max = 255, message = "收银台照长度必须介于 0 和 255 之间")
    public String getStorePhotoCashierDesk() {
        return storePhotoCashierDesk;
    }

    public void setStorePhotoCashierDesk(String storePhotoCashierDesk) {
        this.storePhotoCashierDesk = storePhotoCashierDesk;
    }

    @Length(min = 0, max = 255, message = "店内环境照长度必须介于 0 和 255 之间")
    public String getStorePhotoEnvironment() {
        return storePhotoEnvironment;
    }

    public void setStorePhotoEnvironment(String storePhotoEnvironment) {
        this.storePhotoEnvironment = storePhotoEnvironment;
    }

    @Length(min = 0, max = 255, message = "补充资料长度必须介于 0 和 255 之间")
    public String getAdditionalPhoto() {
        return additionalPhoto;
    }

    public void setAdditionalPhoto(String additionalPhoto) {
        this.additionalPhoto = additionalPhoto;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
