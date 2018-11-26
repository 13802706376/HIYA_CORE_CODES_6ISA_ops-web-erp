package com.yunnex.ops.erp.modules.material.dto;

import java.io.Serializable;

/**
 * 订单物料内容DTO
 */
public class MaterialContentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ysOrderId; // 物料订单ID
    private String materialTypeName; // 物料种类显示名称
    private String materialQuality; // 材质
    private String frontName; // 正面名称
    private String reverseName; // 反面名称
    private String size; // 尺寸
    private Integer materialAmount; // 物料数量
    private String frontImage; // 正面图片
    private String reverseImage; // 反面图片
    private String frontImageName; // 正面图片名称
    private String reverseImageName; // 反面图片名称

    public Long getYsOrderId() {
        return ysOrderId;
    }

    public void setYsOrderId(Long ysOrderId) {
        this.ysOrderId = ysOrderId;
    }

    public String getMaterialQuality() {
        return materialQuality;
    }

    public void setMaterialQuality(String materialQuality) {
        this.materialQuality = materialQuality;
    }

    public String getMaterialTypeName() {
        return materialTypeName;
    }

    public void setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getMaterialAmount() {
        return materialAmount;
    }

    public void setMaterialAmount(Integer materialAmount) {
        this.materialAmount = materialAmount;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public String getReverseImage() {
        return reverseImage;
    }

    public void setReverseImage(String reverseImage) {
        this.reverseImage = reverseImage;
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

    public String getFrontImageName() {
        return frontImageName;
    }

    public void setFrontImageName(String frontImageName) {
        this.frontImageName = frontImageName;
    }

    public String getReverseImageName() {
        return reverseImageName;
    }

    public void setReverseImageName(String reverseImageName) {
        this.reverseImageName = reverseImageName;
    }
}
