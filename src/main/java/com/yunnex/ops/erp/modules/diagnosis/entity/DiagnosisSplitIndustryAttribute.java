package com.yunnex.ops.erp.modules.diagnosis.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 分单行业属性关联表Entity
 * @author yunnex
 * @version 2018-03-29
 */
public class DiagnosisSplitIndustryAttribute extends DataEntity<DiagnosisSplitIndustryAttribute> {

    private static final long serialVersionUID = 1L;
    private String splitId; // 分单ID
    private String industryAttributeId; // 行业属性ID

    public DiagnosisSplitIndustryAttribute() {
        super();
    }

    public DiagnosisSplitIndustryAttribute(String id) {
        super(id);
    }

    public DiagnosisSplitIndustryAttribute(String splitId, String industryAttributeId) {
        super();
        this.splitId = splitId;
        this.industryAttributeId = industryAttributeId;
    }

    @Length(min = 1, max = 64, message = "分单ID长度必须介于 1 和 64 之间")
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    @Length(min = 1, max = 64, message = "行业属性ID长度必须介于 1 和 64 之间")
    public String getIndustryAttributeId() {
        return industryAttributeId;
    }

    public void setIndustryAttributeId(String industryAttributeId) {
        this.industryAttributeId = industryAttributeId;
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
