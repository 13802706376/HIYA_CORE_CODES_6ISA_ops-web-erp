package com.yunnex.ops.erp.modules.diagnosis.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 经营诊断的优惠内容Entity
 * @author yunnex
 * @version 2018-03-29
 */
public class DiagnosisDiscount extends DataEntity<DiagnosisDiscount> {

    private static final long serialVersionUID = 1L;
    private String splitId; // 分单序号
    private String title; // 活动标题
    private String content; // 活动内容

    public DiagnosisDiscount() {
        super();
    }

    public DiagnosisDiscount(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "分单序号长度必须介于 1 和 64 之间")
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    @Length(min = 1, max = 255, message = "活动标题长度必须介于 1 和 255 之间")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Length(min = 1, max = 1024, message = "活动内容长度必须介于 1 和 1024 之间")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
