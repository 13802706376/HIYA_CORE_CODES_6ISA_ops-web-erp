package com.yunnex.ops.erp.modules.diagnosis.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 第一层重点宣传图片Entity
 * @author yunnex
 * @version 2018-04-08
 */
public class DiagnosisFirstAdImage extends DataEntity<DiagnosisFirstAdImage> {

    private static final long serialVersionUID = 1L;
    private String splitId; // 分单ID
    private String type; // 类型，1：微信朋友圈，2：新浪微博，3：陌陌
    private String imgUrl; // 图片路径

    private String typeName; // 类型名称

    public DiagnosisFirstAdImage() {
        super();
    }

    public DiagnosisFirstAdImage(String id) {
        super(id);
    }

    @Length(min = 0, max = 64, message = "分单ID长度必须介于 0 和 64 之间")
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    @Length(min = 0, max = 10, message = "类型，1：微信朋友圈，2：新浪微博，3：陌陌，second_adv：第二层广告图长度必须介于 0 和 10 之间")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Length(min = 0, max = 512, message = "图片路径长度必须介于 0 和 512 之间")
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}