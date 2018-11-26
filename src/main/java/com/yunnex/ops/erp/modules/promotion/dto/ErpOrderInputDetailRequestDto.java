package com.yunnex.ops.erp.modules.promotion.dto;

import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderInputDetail;

import yunnex.common.core.dto.BaseDto;

public class ErpOrderInputDetailRequestDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ErpOrderInputDetail erpOrderInputDetail;
    private String splitId;
    private String promotionMaterialId;

    public ErpOrderInputDetail getErpOrderInputDetail() {
        return erpOrderInputDetail;
    }

    public void setErpOrderInputDetail(ErpOrderInputDetail erpOrderInputDetail) {
        this.erpOrderInputDetail = erpOrderInputDetail;
    }

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
}
