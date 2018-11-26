package com.yunnex.ops.erp.modules.diagnosis.dto;

import java.util.List;

import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisCardCoupons;

import yunnex.common.core.dto.BaseDto;

/**
 * 卡券策划接收参数DTO
 * 
 * yunnex
 * @date 2018年4月13日
 */
public class CouponsPlanRequestDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<DiagnosisCardCoupons> diagnosisCardCoupons; // 卡券信息
    private String shopUsername; // 掌贝账号
    private String shopPassword;// 掌贝密码
    private String splitId;// 分单ID
    private List<String> removedCouponIds; // 要删除的卡券ID

    public List<DiagnosisCardCoupons> getDiagnosisCardCoupons() {
        return diagnosisCardCoupons;
    }

    public void setDiagnosisCardCoupons(List<DiagnosisCardCoupons> diagnosisCardCoupons) {
        this.diagnosisCardCoupons = diagnosisCardCoupons;
    }

    public String getShopUsername() {
        return shopUsername;
    }

    public void setShopUsername(String shopUsername) {
        this.shopUsername = shopUsername;
    }

    public String getShopPassword() {
        return shopPassword;
    }

    public void setShopPassword(String shopPassword) {
        this.shopPassword = shopPassword;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public List<String> getRemovedCouponIds() {
        return removedCouponIds;
    }

    public void setRemovedCouponIds(List<String> removedCouponIds) {
        this.removedCouponIds = removedCouponIds;
    }


}
