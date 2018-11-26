package com.yunnex.ops.erp.modules.promotion.dto;

import java.util.List;

import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;

import yunnex.common.core.dto.BaseDto;

public class ErpOrderFileRequestDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<ErpOrderFile> addedErpOrderFiles;// 新增的文件
    private List<String> removedErpOrderFileIds;// 删除的文件
    private List<String> tempFileUrls;// 删除临时的文件
    private String splitId;
    private String promotionMaterialId;


    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public String getPromotionMaterialId() {
        return promotionMaterialId;
    }

    public void setPromotionMaterialId(String promotionMaterialId) {
        this.promotionMaterialId = promotionMaterialId;
    }

    public List<ErpOrderFile> getAddedErpOrderFiles() {
        return addedErpOrderFiles;
    }

    public void setAddedErpOrderFiles(List<ErpOrderFile> addedErpOrderFiles) {
        this.addedErpOrderFiles = addedErpOrderFiles;
    }

    public List<String> getRemovedErpOrderFileIds() {
        return removedErpOrderFileIds;
    }

    public void setRemovedErpOrderFileIds(List<String> removedErpOrderFileIds) {
        this.removedErpOrderFileIds = removedErpOrderFileIds;
    }

    public List<String> getTempFileUrls() {
        return tempFileUrls;
    }

    public void setTempFileUrls(List<String> tempFileUrls) {
        this.tempFileUrls = tempFileUrls;
    }


}
