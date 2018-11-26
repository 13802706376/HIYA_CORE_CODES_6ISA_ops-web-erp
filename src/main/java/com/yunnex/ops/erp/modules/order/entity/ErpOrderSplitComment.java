package com.yunnex.ops.erp.modules.order.entity;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 聚引客分单评论Entity
 * 
 * @author yunnex
 * @version 2018-01-30
 */
public class ErpOrderSplitComment extends DataEntity<ErpOrderSplitComment> {

    private static final long serialVersionUID = 7254461216067488996L;
    /** 订单ID */
    private String splitId;

    /** 商户id */
    private String shopId;

    /** 服务满意度评分 */
    private Double serviceScore;

    /** 效果推广评分 */
    private Double promotionScore;

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Double getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Double serviceScore) {
        this.serviceScore = serviceScore;
    }

    public Double getPromotionScore() {
        return promotionScore;
    }

    public void setPromotionScore(Double promotionScore) {
        this.promotionScore = promotionScore;
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
