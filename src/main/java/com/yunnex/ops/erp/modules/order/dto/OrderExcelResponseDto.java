package com.yunnex.ops.erp.modules.order.dto;

import java.io.Serializable;

import com.yunnex.ops.erp.common.utils.excel.annotation.MapperCell;

public class OrderExcelResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // 订单id
    @MapperCell(order = 1, cellName = "订单号") // NOSONAR
    private String orderNumber; // 订单号
    @MapperCell(order = 2, cellName = "商户名称")// NOSONAR
    private String shopName; // 商户名称
    @MapperCell(order = 3, cellName = "订单类别")// NOSONAR
    private String orderTypeStr; // 订单类别
    @MapperCell(order = 4, cellName = "购买时间")// NOSONAR
    private String buyDate; // 购买时间
    @MapperCell(order = 5, cellName = " 聚引客服务待处理")// NOSONAR
    private Integer pendingNum; // 聚引客待处理服务数量
    @MapperCell(order = 6, cellName = "商户运营服务待处理")// NOSONAR
    private Integer shopOperationPendingServiceNum; // 商户运营服务待处理
    @MapperCell(order = 7, cellName = "修改时间")// NOSONAR
    private String updateDate;
    @MapperCell(order = 8, cellName = "状态")// NOSONAR
    private String orderStatusStr;// NOSONAR // 订单状态 未签约，已签约，已支付，进件中，已进件

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOrderTypeStr() {
        return orderTypeStr;
    }

    public void setOrderTypeStr(String orderTypeStr) {
        this.orderTypeStr = orderTypeStr;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getOrderStatusStr() {
        return orderStatusStr;
    }

    public void setOrderStatusStr(String orderStatusStr) {
        this.orderStatusStr = orderStatusStr;
    }

    public Integer getShopOperationPendingServiceNum() {
        return shopOperationPendingServiceNum;
    }

    public void setShopOperationPendingServiceNum(Integer shopOperationPendingServiceNum) {
        this.shopOperationPendingServiceNum = shopOperationPendingServiceNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPendingNum() {
        return pendingNum;
    }

    public void setPendingNum(Integer pendingNum) {
        this.pendingNum = pendingNum;
    }

}