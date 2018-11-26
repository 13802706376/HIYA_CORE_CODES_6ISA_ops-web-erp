package com.yunnex.ops.erp.modules.order.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 订单寄送信息Entity
 * 
 * @author yunnex
 * @version 2018-07-13
 */
public class ErpOrderSendInfo extends DataEntity<ErpOrderSendInfo> {

    private static final long serialVersionUID = 1L;
    private String orderNumber; // 贝虎订单号
    private Long ysOrderId; // 易商订单ID，来自易商平台
    private String linkMan; // 联系人
    private String linkPhone; // 联系电话
    private String receiveAddress; // 收货地址

    public ErpOrderSendInfo() {
        super();
    }

    public ErpOrderSendInfo(String id) {
        super(id);
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getYsOrderId() {
        return ysOrderId;
    }

    public void setYsOrderId(Long ysOrderId) {
        this.ysOrderId = ysOrderId;
    }

    @Length(min = 1, max = 30, message = "联系人长度必须介于 1 和 30 之间")
    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    @Length(min = 1, max = 20, message = "联系电话长度必须介于 1 和 20 之间")
    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    @Length(min = 1, max = 150, message = "收货地址长度必须介于 1 和 150 之间")
    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
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
