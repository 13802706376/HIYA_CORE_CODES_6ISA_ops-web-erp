package com.yunnex.ops.erp.modules.diagnosis.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 优惠形式推荐表Entity
 * 
 * @author yunnex
 * @version 2018-03-29
 */
public class DiagnosisDiscountTypeRecommend extends DataEntity<DiagnosisDiscountTypeRecommend> {

    private static final long serialVersionUID = 1L;
    private String splitId; // 分单ID
    private String discountTypeId; // 优惠形式id
    private Integer recommendScore; // 推荐分值
    private String checked; // 是否选中,Y=未选中，N=选中


    private String discountTypeName; // 优惠形式名称
    private String example; // 优惠形式示例

    public DiagnosisDiscountTypeRecommend() {
        super();
    }

    public DiagnosisDiscountTypeRecommend(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "分单ID长度必须介于 1 和 64 之间")
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    @Length(min = 1, max = 64, message = "优惠形式id长度必须介于 1 和 64 之间")
    public String getDiscountTypeId() {
        return discountTypeId;
    }

    public void setDiscountTypeId(String discountTypeId) {
        this.discountTypeId = discountTypeId;
    }

    @NotNull(message = "推荐分值不能为空")
    public Integer getRecommendScore() {
        return recommendScore;
    }

    public void setRecommendScore(Integer recommendScore) {
        this.recommendScore = recommendScore;
    }

    @Length(min = 1, max = 1, message = "是否选中,Y=未选中，N=选中长度必须介于 1 和 1 之间")
    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getDiscountTypeName() {
        return discountTypeName;
    }

    public void setDiscountTypeName(String discountTypeName) {
        this.discountTypeName = discountTypeName;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
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
