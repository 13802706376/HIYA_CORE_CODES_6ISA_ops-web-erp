package com.yunnex.ops.erp.modules.order.dto;

import java.util.List;

import com.yunnex.ops.erp.modules.order.entity.ErpOrderCouponOutput;

import yunnex.common.core.dto.BaseDto;

public class CouponOutputRequestDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> removedCouponIds; // 要删除的卡券输出ID
    private List<ErpOrderCouponOutput> orderCouponOutputs;// 要新增和修改的卡券输出信息
    private String promotionMaterialId;// 推广资料ID
    private String splitId;

    public List<String> getRemovedCouponIds() {
        return removedCouponIds;
    }

    public void setRemovedCouponIds(List<String> removedCouponIds) {
        this.removedCouponIds = removedCouponIds;
    }

    public List<ErpOrderCouponOutput> getOrderCouponOutputs() {
        return orderCouponOutputs;
    }

    public void setOrderCouponOutputs(List<ErpOrderCouponOutput> orderCouponOutputs) {
        this.orderCouponOutputs = orderCouponOutputs;
    }

    public String getPromotionMaterialId() {
        return promotionMaterialId;
    }

    public void setPromotionMaterialId(String promotionMaterialId) {
        this.promotionMaterialId = promotionMaterialId;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }


}
