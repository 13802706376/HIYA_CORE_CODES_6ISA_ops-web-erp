package com.yunnex.ops.erp.modules.diagnosis.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 行业属性Entity
 * 
 * @author yunnex
 * @version 2018-04-03
 */
public class DiagnosisIndustryAttribute extends DataEntity<DiagnosisIndustryAttribute> {

    private static final long serialVersionUID = 1L;
    private String name; // 行业名称
    private String industryAttribute; // 推荐的行业属性
    private String pid; // 父类行业
    private Integer level; // 当前行业分类级别,默认从1开始递增
    private Integer order; // 排序

    public DiagnosisIndustryAttribute() {
        super();
    }

    public DiagnosisIndustryAttribute(String id) {
        super(id);
    }

    @Length(min = 0, max = 255, message = "行业名称长度必须介于 0 和 255 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 255, message = "推荐的行业属性长度必须介于 0 和 255 之间")
    public String getIndustryAttribute() {
        return industryAttribute;
    }

    public void setIndustryAttribute(String industryAttribute) {
        this.industryAttribute = industryAttribute;
    }

    @Length(min = 0, max = 64, message = "父类行业长度必须介于 0 和 64 之间")
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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
