package com.yunnex.ops.erp.modules.material.dto;

import java.io.Serializable;

import com.yunnex.ops.erp.common.utils.excel.annotation.MapperCell;

public class MaterialCreationExcelResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @MapperCell(order = 1, cellName = "订单号")// NOSONAR
    private String orderNumber; // 订单号
    @MapperCell(order = 2, cellName = "运营顾问")// NOSONAR
    private String operationAdviserName;// 运营顾问名字
    @MapperCell(order = 3, cellName = "商户")// NOSONAR
    private String shopName; // 商户名称
    @MapperCell(order = 4, cellName = "设计稿")// NOSONAR
    private String layoutName; // 设计稿名称
    @MapperCell(order = 5, cellName = "物料制作供应商")// NOSONAR
    private String providerName; // 供应商名称
    @MapperCell(order = 6, cellName = "成本")// NOSONAR
    private Long cost; // 成本(单位：分)
    @MapperCell(order = 7, cellName = "物流单号")// NOSONAR
    private String logisticsNumber; // 物流单号
    @MapperCell(order = 8, cellName = "下单时间")// NOSONAR
    private String placeOrderTime; // 下单时间
    @MapperCell(order = 9, cellName = "物料到店时间")// NOSONAR
    private String deliverTime; // 物料到店时间
    @MapperCell(order = 10, cellName = "状态")// NOSONAR
    private String statusName; // 状态名称

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOperationAdviserName() {
        return operationAdviserName;
    }

    public void setOperationAdviserName(String operationAdviserName) {
        this.operationAdviserName = operationAdviserName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPlaceOrderTime() {
        return placeOrderTime;
    }

    public void setPlaceOrderTime(String placeOrderTime) {
        this.placeOrderTime = placeOrderTime;
    }

    public String getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(String deliverTime) {
        this.deliverTime = deliverTime;
    }
}