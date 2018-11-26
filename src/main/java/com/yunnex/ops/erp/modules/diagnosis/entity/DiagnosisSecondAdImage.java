package com.yunnex.ops.erp.modules.diagnosis.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 第二层重点宣传图片Entity
 * @author yunnex
 * @version 2018-04-08
 */
public class DiagnosisSecondAdImage extends DataEntity<DiagnosisSecondAdImage> {

    private static final long serialVersionUID = 1L;
    private String splitId; // 分单ID
    private String imgUrl; // 图片路径

    public DiagnosisSecondAdImage() {
        super();
    }

    public DiagnosisSecondAdImage(String id) {
        super(id);
    }

    @Length(min = 0, max = 64, message = "分单ID长度必须介于 0 和 64 之间")
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    @Length(min = 0, max = 512, message = "图片路径长度必须介于 0 和 512 之间")
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}