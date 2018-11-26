package com.yunnex.ops.erp.modules.workflow.flow.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单信息
 */
public class OrderInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderTypeName; // 订单类型，直销或服务商
    private String orderNo; // 订单号，包括分单号
    private Integer hurryFlag; // 加急标识
    private String shopName; // 商户名称
    private String serviceTypeInfo; // 购买的服务类型
    private String orderRemarks; // 备注
    private String shopContactPerson; // 商户联系人
    private String shopContactPhone; // 商户联系方式
    private String agentName; // 服务商名称
    private String agentContactPerson; // 服务商联系人
    private String agentContactPhone; // 服务商联系方式
    private String operationAdviser; // 运营顾问
    private Date buyDate; // 购买时间

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getHurryFlag() {
        return hurryFlag;
    }

    public void setHurryFlag(Integer hurryFlag) {
        this.hurryFlag = hurryFlag;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getServiceTypeInfo() {
        return serviceTypeInfo;
    }

    public void setServiceTypeInfo(String serviceTypeInfo) {
        this.serviceTypeInfo = serviceTypeInfo;
    }

    public String getOrderRemarks() {
        return orderRemarks;
    }

    public void setOrderRemarks(String orderRemarks) {
        this.orderRemarks = orderRemarks;
    }

    public String getShopContactPerson() {
        return shopContactPerson;
    }

    public void setShopContactPerson(String shopContactPerson) {
        this.shopContactPerson = shopContactPerson;
    }

    public String getShopContactPhone() {
        return shopContactPhone;
    }

    public void setShopContactPhone(String shopContactPhone) {
        this.shopContactPhone = shopContactPhone;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentContactPerson() {
        return agentContactPerson;
    }

    public void setAgentContactPerson(String agentContactPerson) {
        this.agentContactPerson = agentContactPerson;
    }

    public String getAgentContactPhone() {
        return agentContactPhone;
    }

    public void setAgentContactPhone(String agentContactPhone) {
        this.agentContactPhone = agentContactPhone;
    }

    public String getOperationAdviser() {
        return operationAdviser;
    }

    public void setOperationAdviser(String operationAdviser) {
        this.operationAdviser = operationAdviser;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }
}
