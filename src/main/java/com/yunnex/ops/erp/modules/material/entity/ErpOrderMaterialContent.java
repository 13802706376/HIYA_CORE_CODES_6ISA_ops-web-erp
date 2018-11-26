package com.yunnex.ops.erp.modules.material.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 订单物料内容Entity
 *
 * @author yunnex
 * @version 2018-07-13
 */
public class ErpOrderMaterialContent extends DataEntity<ErpOrderMaterialContent> {

    private static final long serialVersionUID = 1L;
    private String orderNumber; // 订单号
    private Long ysOrderId; // 易商订单ID，来自易商平台
    // 物料布置场景类型
    private Integer scenarioType;
    // 物料种类显示名称
    private String materialTypeName;
    private String materialQuality; // 材质
    private Integer materialAmount; // 物料数量
    private String size; // 尺寸大小
    private String frontName; // 正面名称
    private String reverseName; // 反面名称
    private String frontImage; // 正面图片
    private String reverseImage; // 反面图片

    public ErpOrderMaterialContent() {
        super();
    }

    public ErpOrderMaterialContent(String id) {
        super(id);
    }

    @Length(min = 1, max = 50, message = "订单号长度必须介于 1 和 50 之间")
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getYsOrderId() {
        return ysOrderId;
    }

    public void setYsOrderId(Long ysOrderId) {
        this.ysOrderId = ysOrderId;
    }

    @Length(min = 1, max = 30, message = "材质长度必须介于 1 和 30 之间")
    public String getMaterialQuality() {
        return materialQuality;
    }

    public void setMaterialQuality(String materialQuality) {
        this.materialQuality = materialQuality;
    }

    public String getFrontName() {
        return frontName;
    }

    public void setFrontName(String frontName) {
        this.frontName = frontName;
    }

    public String getReverseName() {
        return reverseName;
    }

    public void setReverseName(String reverseName) {
        this.reverseName = reverseName;
    }

    public Integer getScenarioType() {
        return scenarioType;
    }

    public void setScenarioType(Integer scenarioType) {
        this.scenarioType = scenarioType;
    }

    public String getMaterialTypeName() {
        return materialTypeName;
    }

    public void setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
    }

    @Length(min = 1, max = 30, message = "尺寸大小长度必须介于 1 和 30 之间")
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @NotNull(message = "物料数量不能为空")
    public Integer getMaterialAmount() {
        return materialAmount;
    }

    public void setMaterialAmount(Integer materialAmount) {
        this.materialAmount = materialAmount;
    }

    @Length(min = 1, max = 100, message = "正面长度必须介于 1 和 100 之间")
    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    @Length(min = 1, max = 100, message = "反面长度必须介于 1 和 100 之间")
    public String getReverseImage() {
        return reverseImage;
    }

    public void setReverseImage(String reverseImage) {
        this.reverseImage = reverseImage;
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
