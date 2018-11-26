package com.yunnex.ops.erp.modules.diagnosis.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 优惠形式配置表Entity
 * @author yunnex
 * @version 2018-03-29
 */
public class DiagnosisDiscountTypeConfig extends DataEntity<DiagnosisDiscountTypeConfig> {

    private static final long serialVersionUID = 1L;
    private String industryAttributeId; // 行业属性id
    private String activityRequirementId; // 推广需求id
    private String discountTypeId; // 优惠形式id
    private Integer score; // 分值

    private String industryAttributeName; // 行业属性名称
    private String activityRequirementName; // 推广需求名称
    private String discountTypeName; // 优惠形式名称

    public DiagnosisDiscountTypeConfig() {
        super();
    }

    public DiagnosisDiscountTypeConfig(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "行业属性id长度必须介于 1 和 64 之间")
    public String getIndustryAttributeId() {
        return industryAttributeId;
    }

    public void setIndustryAttributeId(String industryAttributeId) {
        this.industryAttributeId = industryAttributeId;
    }

    @Length(min = 1, max = 64, message = "推广需求id长度必须介于 1 和 64 之间")
    public String getActivityRequirementId() {
        return activityRequirementId;
    }

    public void setActivityRequirementId(String activityRequirementId) {
        this.activityRequirementId = activityRequirementId;
    }

    @Length(min = 1, max = 64, message = "优惠形式id长度必须介于 1 和 64 之间")
    public String getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(String discountTypeId) {
        this.discountTypeId = discountTypeId;
    }

    @NotNull(message = "分值不能为空")
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getIndustryAttributeName() {
        return industryAttributeName;
    }

    public void setIndustryAttributeName(String industryAttributeName) {
        this.industryAttributeName = industryAttributeName;
    }

    public String getActivityRequirementName() {
        return activityRequirementName;
    }

    public void setActivityRequirementName(String activityRequirementName) {
        this.activityRequirementName = activityRequirementName;
    }

    public String getDiscountTypeName() {
        return discountTypeName;
    }

    public void setDiscountTypeName(String discountTypeName) {
        this.discountTypeName = discountTypeName;
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
