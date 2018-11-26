package com.yunnex.ops.erp.modules.material.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yunnex.ops.erp.common.persistence.Pager;

public class MaterialCreationRequestDto extends Pager<MaterialCreationRequestDto> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String orderNumber;// 订单号
    private String operationAdviserId; // 运营顾问ID
    private String shopName; // 商户名称
    private String providerName; // 供应商名称
    private String status;
    private Date placeOrderStartTime; // 下单开始时间
    private Date placeOrderEndTime; // 下单结束时间
    private Date deliverStartTime;// 物料到店开始时间
    private Date deliverEndTime;// 物料到店结束开始时间
    private List<String> teamUserIds; // 团队用户ID

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    public String getOperationAdviserId() {
        return operationAdviserId;
    }
    public void setOperationAdviserId(String operationAdviserId) {
        this.operationAdviserId = operationAdviserId;
    }
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getProviderName() {
        return providerName;
    }
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getPlaceOrderStartTime() {
        return placeOrderStartTime;
    }
    public void setPlaceOrderStartTime(Date placeOrderStartTime) {
        this.placeOrderStartTime = placeOrderStartTime;
    }
    public Date getPlaceOrderEndTime() {
        return placeOrderEndTime;
    }
    public void setPlaceOrderEndTime(Date placeOrderEndTime) {
        this.placeOrderEndTime = placeOrderEndTime;
    }
    public Date getDeliverStartTime() {
        return deliverStartTime;
    }
    public void setDeliverStartTime(Date deliverStartTime) {
        this.deliverStartTime = deliverStartTime;
    }
    public Date getDeliverEndTime() {
        return deliverEndTime;
    }
    public void setDeliverEndTime(Date deliverEndTime) {
        this.deliverEndTime = deliverEndTime;
    }

    public List<String> getTeamUserIds() {
        return teamUserIds;
    }

    public void setTeamUserIds(List<String> teamUserIds) {
        this.teamUserIds = teamUserIds;
    }
}
