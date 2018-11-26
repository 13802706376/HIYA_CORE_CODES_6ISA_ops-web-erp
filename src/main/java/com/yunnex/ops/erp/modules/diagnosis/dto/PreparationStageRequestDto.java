package com.yunnex.ops.erp.modules.diagnosis.dto;

import java.util.List;

import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;

import yunnex.common.core.dto.BaseDto;

/**
 * 电话前准备阶段DTO
 * 
 * yunnex
 * @date 2018年4月3日
 */
public class PreparationStageRequestDto extends BaseDto {


    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String cityLevel;// 城市级别
    private List<ErpOrderSplitGood> erpOrderSplitGoods; // 订单拆单商品信息
    private String packageAdditional; // 套餐其他信息补充
    private String serviceKnow;// 商户产品／服务信息了解（大众点评）
    private String orderId;// 订单ID
    private String splitId;// 拆单ID

    public String getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(String cityLevel) {
        this.cityLevel = cityLevel;
    }

    public List<ErpOrderSplitGood> getErpOrderSplitGoods() {
        return erpOrderSplitGoods;
    }

    public void setErpOrderSplitGoods(List<ErpOrderSplitGood> erpOrderSplitGoods) {
        this.erpOrderSplitGoods = erpOrderSplitGoods;
    }

    public String getPackageAdditional() {
        return packageAdditional;
    }

    public void setPackageAdditional(String packageAdditional) {
        this.packageAdditional = packageAdditional;
    }

    public String getServiceKnow() {
        return serviceKnow;
    }

    public void setServiceKnow(String serviceKnow) {
        this.serviceKnow = serviceKnow;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }
}
