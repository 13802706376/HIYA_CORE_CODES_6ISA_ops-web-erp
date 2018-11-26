package com.yunnex.ops.erp.modules.workflow.store.excel;

import com.yunnex.ops.erp.common.utils.excel.annotation.MapperCell;

public class AdvMomo {

    @MapperCell(cellName = "商户陌陌号", order = 1)
    private String accountNo; // 商户陌陌号
    @MapperCell(cellName = "品牌名称", order = 2)
    private String brandName; // 品牌名称
    @MapperCell(cellName = "预计投放时间", order = 3)
    private String expectedDeliveryTime;// 预计投放时间
    @MapperCell(cellName = "投放落地页链接", order = 4)
    private String deliveryUrl; // 投放落地页链接

    @MapperCell(cellName = "企业名称", order = 5)
    private String registerName; // 企业名称
    @MapperCell(cellName = "营业执照注册号", order = 6)
    private String registerNo; // 营业执照注册号

    @MapperCell(cellName = "联系人姓名", order = 7)
    private String name; // 联系人姓名
    @MapperCell(cellName = "联系人手机号", order = 8)
    private String phone; // 联系人手机号

    @MapperCell(cellName = "产品名称", order = 9)
    private String productName; // 产品名称
    @MapperCell(cellName = "投放产品具体信息", order = 10)
    private String productConcreteInfo; // 投放产品具体信息

    @FileName("工信部ICP备案信息查询截图")
    private String icpMessage; // 工信部ICP备案信息查询截图
    @FileName("营业执照照片")
    private String businessLicence; // 营业执照照片
    @FileName("行业资质照片")
    private String specialCertificate; // 行业资质照片
    @FileName("关注掌贝陌陌号的截图")
    private String followZhangbeiScreenshot; // 关注掌贝陌陌号的截图
    @FileName("陌陌投放图片")
    private String deliveryPic;// 陌陌投放图片
    @FileName("陌陌文案素材")
    private String copywritingPlan;// 陌陌文案素材

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(String expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }

    public String getDeliveryUrl() {
        return deliveryUrl;
    }

    public void setDeliveryUrl(String deliveryUrl) {
        this.deliveryUrl = deliveryUrl;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductConcreteInfo() {
        return productConcreteInfo;
    }

    public void setProductConcreteInfo(String productConcreteInfo) {
        this.productConcreteInfo = productConcreteInfo;
    }

    public String getIcpMessage() {
        return icpMessage;
    }

    public void setIcpMessage(String icpMessage) {
        this.icpMessage = icpMessage;
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

    public String getFollowZhangbeiScreenshot() {
        return followZhangbeiScreenshot;
    }

    public void setFollowZhangbeiScreenshot(String followZhangbeiScreenshot) {
        this.followZhangbeiScreenshot = followZhangbeiScreenshot;
    }

    public String getDeliveryPic() {
        return deliveryPic;
    }

    public void setDeliveryPic(String deliveryPic) {
        this.deliveryPic = deliveryPic;
    }

    public String getCopywritingPlan() {
        return copywritingPlan;
    }

    public void setCopywritingPlan(String copywritingPlan) {
        this.copywritingPlan = copywritingPlan;
    }

    @Override
    public String toString() {
        return "AdvMomo [accountNo=" + accountNo + ", brandName=" + brandName + ", expectedDeliveryTime=" + expectedDeliveryTime + ", deliveryUrl=" + deliveryUrl + ", registerName=" + registerName + ", registerNo=" + registerNo + ", name=" + name + ", phone=" + phone + ", productName=" + productName + ", productConcreteInfo=" + productConcreteInfo + ", icpMessage=" + icpMessage + ", businessLicence=" + businessLicence + ", specialCertificate=" + specialCertificate + ", followZhangbeiScreenshot=" + followZhangbeiScreenshot + ", deliveryPic=" + deliveryPic + ", copywritingPlan=" + copywritingPlan + "]";
    }

}
