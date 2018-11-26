package com.yunnex.ops.erp.modules.store.advertiser.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 陌陌广告主开通资料Entity
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpStoreAdvertiserMomo extends DataEntity<ErpStoreAdvertiserMomo> {

    private static final long serialVersionUID = 1L;
    private String accountNo; // 陌陌号
    private String brandName; // 品牌名称
    private String icp; // ICP
    private String followZhangbeiScreenshot; // 关注掌贝陌陌号的截图
    private Integer auditStatus = 0; // 审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0
    private String auditContent;
    private String deliveryPic;// 陌陌投放图片
    private String copywritingPlan;// 陌陌文案
    private Date expectedDeliveryTime;// 预计投放时间
    private String deliveryUrl; // 投放链接



    public ErpStoreAdvertiserMomo() {
        super();
    }

    public ErpStoreAdvertiserMomo(String id) {
        super(id);
    }

    public Date getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(Date expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }

    public String getDeliveryUrl() {
        return deliveryUrl;
    }

    public void setDeliveryUrl(String deliveryUrl) {
        this.deliveryUrl = deliveryUrl;
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

    @Length(min = 0, max = 64, message = "陌陌号长度必须介于 0 和 64 之间")
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    @Length(min = 0, max = 64, message = "品牌名称长度必须介于 0 和 64 之间")
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Length(min = 0, max = 64, message = "ICP长度必须介于 0 和 64 之间")
    public String getIcp() {
        return icp;
    }

    public void setIcp(String icp) {
        this.icp = icp;
    }

    @Length(min = 0, max = 255, message = "关注掌贝陌陌号的截图长度必须介于 0 和 255 之间")
    public String getFollowZhangbeiScreenshot() {
        return followZhangbeiScreenshot;
    }

    public void setFollowZhangbeiScreenshot(String followZhangbeiScreenshot) {
        this.followZhangbeiScreenshot = followZhangbeiScreenshot;
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
