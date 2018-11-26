package com.yunnex.ops.erp.modules.workflow.store.excel;

import com.yunnex.ops.erp.common.utils.excel.annotation.MapperCell;

public class AdvWeibo {

    @MapperCell(cellName = "微博登录账号", order = 1)
    private String accountNo; // 微博登录账号
    @MapperCell(cellName = "微博登录密码", order = 2)
    private String accountPassword; // 微博登录密码
    @MapperCell(cellName = "微博昵称", order = 3)
    private String nickName; // 微博昵称
    @MapperCell(cellName = "微博UID", order = 4)
    private String uid; // 微博UID

    @MapperCell(cellName = "企业名称", order = 5)
    private String registerName; // 企业名称
    @MapperCell(cellName = "营业执照注册号", order = 6)
    private String registerNo; // 营业执照注册号
    @MapperCell(cellName = "营业执照有效期", order = 7)
    private String validDate; // 营业执照有效期
    @MapperCell(cellName = "经营范围", order = 8)
    private String businessScope; // 经营范围

    @MapperCell(cellName = "联系人姓名", order = 9)
    private String name; // 联系人姓名
    @MapperCell(cellName = "联系人手机号", order = 10)
    private String phone; // 联系人手机号
    @MapperCell(cellName = "联系人邮箱", order = 11)
    private String email; // 联系人邮箱
    @MapperCell(cellName = "联系人通讯地址", order = 12)
    private String address; // 联系人通讯地址
    
    @FileName("微博账号关系证明函")
    private String relationProveLetter; // 微博账号关系证明函
    @FileName("微博广告授权函")
    private String advAuthLetter; // 微博广告授权函
    @FileName("微博推广承诺函")
    private String promotePromiseLetter; // 微博推广承诺函
    @FileName("营业执照扫描件")
    private String businessLicence; // 营业执照扫描件
    @FileName("行业资质照片")
    private String specialCertificate; // 行业资质照片

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRelationProveLetter() {
        return relationProveLetter;
    }

    public void setRelationProveLetter(String relationProveLetter) {
        this.relationProveLetter = relationProveLetter;
    }

    public String getAdvAuthLetter() {
        return advAuthLetter;
    }

    public void setAdvAuthLetter(String advAuthLetter) {
        this.advAuthLetter = advAuthLetter;
    }

    public String getPromotePromiseLetter() {
        return promotePromiseLetter;
    }

    public void setPromotePromiseLetter(String promotePromiseLetter) {
        this.promotePromiseLetter = promotePromiseLetter;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }

    public String getSpecialCertificate() {
        return specialCertificate;
    }

    public void setSpecialCertificate(String specialCertificate) {
        this.specialCertificate = specialCertificate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "AdvWeibo [accountNo=" + accountNo + ", accountPassword=" + accountPassword + ", nickName=" + nickName + ", uid=" + uid + ", registerName=" + registerName + ", registerNo=" + registerNo + ", validDate=" + validDate + ", businessScope=" + businessScope + ", name=" + name + ", phone=" + phone + ", email=" + email + ", address=" + address + ", relationProveLetter=" + relationProveLetter + ", advAuthLetter=" + advAuthLetter + ", promotePromiseLetter=" + promotePromiseLetter + ", businessLicence=" + businessLicence + ", specialCertificate=" + specialCertificate + "]";
    }

}
