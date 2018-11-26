package com.yunnex.ops.erp.modules.workflow.flow.dto.rep;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单信息
 */
public class OrderInfoFlowRepDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String orderId;//订单id
    private String orderNo; // 订单号
    private String shopName; // 商户名称
    private String shopAbbreviation; // 商户简称
    private Date buyDate; // 购买时间
    private String contactName; // 联系人
    private String contactPhone; // 联系电话
    private String salePhone;//销售电话
    private String saleName;//销售人
    private String serviceItems; // 服务项
    private String serviceType;//服务类型
    private String shopType;//商户类型
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getShopAbbreviation() {
        return shopAbbreviation;
    }
    public void setShopAbbreviation(String shopAbbreviation) {
        this.shopAbbreviation = shopAbbreviation;
    }
    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getContactPhone() {
        return contactPhone;
    }
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    public String getSalePhone() {
        return salePhone;
    }
    public void setSalePhone(String salePhone) {
        this.salePhone = salePhone;
    }
    public String getSaleName() {
        return saleName;
    }
    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }
    public String getServiceItems() {
        return serviceItems;
    }
    public void setServiceItems(String serviceItems) {
        this.serviceItems = serviceItems;
    }
    public String getServiceType() {
        return serviceType;
    }
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    public String getShopType() {
        return shopType;
    }
    public void setShopType(String shopType) {
        this.shopType = shopType;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getBuyDate() {
        return buyDate;
    }
    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
   
}
