package com.yunnex.ops.erp.modules.diagnosis.dto;

import java.util.List;

import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisFirstAdImage;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisSecondAdImage;

import yunnex.common.core.dto.BaseDto;

/**
 * 活动策划3（宣传重点）
 * 
 * yunnex
 * @date 2018年4月8日
 */
public class PropagandaKeyRequestDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String splitId; // 分单ID
    private String firstPropagandaContent; // 第一层重点宣传文案
    private String secondPropagandaContent;// 第二层重点宣传文案
    private List<DiagnosisFirstAdImage> diagnosisFirstAdImages; // 第一层重点宣传图片
    private List<DiagnosisSecondAdImage> diagnosisSecondAdImages;// 第二层重点宣传图片
    private List<String> removedFirstAdImgIds;
    private List<String> removedSecondAdImgIds;
    private List<String> removedImgUrls; // 删除未入库的图片


    public String getFirstPropagandaContent() {
        return firstPropagandaContent;
    }

    public void setFirstPropagandaContent(String firstPropagandaContent) {
        this.firstPropagandaContent = firstPropagandaContent;
    }

    public String getSecondPropagandaContent() {
        return secondPropagandaContent;
    }

    public void setSecondPropagandaContent(String secondPropagandaContent) {
        this.secondPropagandaContent = secondPropagandaContent;
    }

    public List<DiagnosisFirstAdImage> getDiagnosisFirstAdImages() {
        return diagnosisFirstAdImages;
    }

    public void setDiagnosisFirstAdImages(List<DiagnosisFirstAdImage> diagnosisFirstAdImages) {
        this.diagnosisFirstAdImages = diagnosisFirstAdImages;
    }

    public List<DiagnosisSecondAdImage> getDiagnosisSecondAdImages() {
        return diagnosisSecondAdImages;
    }

    public void setDiagnosisSecondAdImages(List<DiagnosisSecondAdImage> diagnosisSecondAdImages) {
        this.diagnosisSecondAdImages = diagnosisSecondAdImages;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public List<String> getRemovedFirstAdImgIds() {
        return removedFirstAdImgIds;
    }

    public void setRemovedFirstAdImgIds(List<String> removedFirstAdImgIds) {
        this.removedFirstAdImgIds = removedFirstAdImgIds;
    }

    public List<String> getRemovedSecondAdImgIds() {
        return removedSecondAdImgIds;
    }

    public void setRemovedSecondAdImgIds(List<String> removedSecondAdImgIds) {
        this.removedSecondAdImgIds = removedSecondAdImgIds;
    }

    public List<String> getRemovedImgUrls() {
        return removedImgUrls;
    }

    public void setRemovedImgUrls(List<String> removedImgUrls) {
        this.removedImgUrls = removedImgUrls;
    }
}
