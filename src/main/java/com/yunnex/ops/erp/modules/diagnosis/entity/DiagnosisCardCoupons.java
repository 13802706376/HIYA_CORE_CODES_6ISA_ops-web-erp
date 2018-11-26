package com.yunnex.ops.erp.modules.diagnosis.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 卡券内容Entity
 * @author yunnex
 * @version 2018-03-29
 */
public class DiagnosisCardCoupons extends DataEntity<DiagnosisCardCoupons> {

    private static final long serialVersionUID = 1L;
    private String splitId; // 分单ID
    private String shopName; // 商户名称（名称+区域店名）
    private String couponType; // 优惠券类型，来源数据字典
    private Double originalPrice; // 商品券原价
    private Double paymentAmount; // 商品券支付金额
    private String couponName; // 优惠券名称
    private String useThreshold; // 使用门槛
    private String reduceAmount; // 减免金额
    private String giftCouponName; // 礼品券名称
    private String superpositionNum; // 每次最多可叠加几张
    private String discountCouponName; // 折扣券名称
    private String discountScale; // 折扣比例
    private Integer inventory; // 总库存
    private String limitNum; // 每人限领
    private String effectiveTime; // 有效时间,如一个月
    private String availableHours; // 可用时段
    private String description; // 详细描述
    private String terms; // 使用须知
    private String phoneNumber; // 客服电话
    private String fitStore; // 适合门店

    public DiagnosisCardCoupons() {
        super();
    }

    public DiagnosisCardCoupons(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "分单ID长度必须介于 1 和 64 之间")
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    @Length(min = 1, max = 255, message = "商户名称（名称+区域店名）长度必须介于 1 和 255 之间")
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Length(min = 1, max = 10, message = "优惠券类型，来源数据字典长度必须介于 1 和 10 之间")
    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    @NotNull(message = "商品券原价不能为空")
    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    @NotNull(message = "商品券支付金额不能为空")
    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @Length(min = 1, max = 100, message = "优惠券名称长度必须介于 1 和 100 之间")
    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    @Length(min = 1, max = 100, message = "使用门槛长度必须介于 1 和 100 之间")
    public String getUseThreshold() {
        return useThreshold;
    }

    public void setUseThreshold(String useThreshold) {
        this.useThreshold = useThreshold;
    }

    @Length(min = 1, max = 100, message = "减免金额长度必须介于 1 和 100 之间")
    public String getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(String reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    @Length(min = 1, max = 100, message = "礼品券名称长度必须介于 1 和 100 之间")
    public String getGiftCouponName() {
        return giftCouponName;
    }

    public void setGiftCouponName(String giftCouponName) {
        this.giftCouponName = giftCouponName;
    }

    @Length(min = 1, max = 100, message = "每次最多可叠加几张长度必须介于 1 和 100 之间")
    public String getSuperpositionNum() {
        return superpositionNum;
    }

    public void setSuperpositionNum(String superpositionNum) {
        this.superpositionNum = superpositionNum;
    }

    @Length(min = 1, max = 100, message = "折扣券名称长度必须介于 1 和 100 之间")
    public String getDiscountCouponName() {
        return discountCouponName;
    }

    public void setDiscountCouponName(String discountCouponName) {
        this.discountCouponName = discountCouponName;
    }

    @Length(min = 1, max = 100, message = "折扣比例长度必须介于 1 和 100 之间")
    public String getDiscountScale() {
        return discountScale;
    }

    public void setDiscountScale(String discountScale) {
        this.discountScale = discountScale;
    }

    @NotNull(message = "总库存不能为空")
    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    @Length(min = 0, max = 50, message = "每人限领长度必须介于 0 和 50 之间")
    public String getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(String limitNum) {
        this.limitNum = limitNum;
    }

    @Length(min = 1, max = 100, message = "有效时间,如一个月长度必须介于 1 和 100 之间")
    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    @Length(min = 1, max = 100, message = "可用时段长度必须介于 1 和 100 之间")
    public String getAvailableHours() {
        return availableHours;
    }

    public void setAvailableHours(String availableHours) {
        this.availableHours = availableHours;
    }

    @Length(min = 1, max = 255, message = "详细描述长度必须介于 1 和 255 之间")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Length(min = 1, max = 512, message = "使用须知长度必须介于 1 和 512 之间")
    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    @Length(min = 1, max = 20, message = "客服电话长度必须介于 1 和 20 之间")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Length(min = 1, max = 100, message = "适合门店长度必须介于 1 和 100 之间")
    public String getFitStore() {
        return fitStore;
    }

    public void setFitStore(String fitStore) {
        this.fitStore = fitStore;
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
