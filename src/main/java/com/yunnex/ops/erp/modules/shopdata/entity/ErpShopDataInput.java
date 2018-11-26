package com.yunnex.ops.erp.modules.shopdata.entity;

import com.yunnex.ops.erp.common.persistence.ActEntity;

/**
 * 商户资料录入实体类
 * 
 * @author SunQ
 * @date 2017年12月7日
 */
public class ErpShopDataInput extends ActEntity<ErpShopDataInput> {

    private static final long serialVersionUID = 2125552562699561895L;

    //订单ID
    private String orderId;
    //商户ID
    private String shopId;
    //订单编号
    private String orderNumber;
    //商户名称
    private String shopName;
    //订单来源
    private String source;
    //订单类别 1直销 2渠道
    private Integer orderType;
    //商户地址
    private String address;
    //订单负责人
    private String planningExpert;
    //备注
    private String remark;
    
    private String userId;
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPlanningExpert() {
        return planningExpert;
    }

    public void setPlanningExpert(String planningExpert) {
        this.planningExpert = planningExpert == null ? null : planningExpert.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}