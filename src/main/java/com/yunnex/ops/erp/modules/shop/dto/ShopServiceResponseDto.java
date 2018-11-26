package com.yunnex.ops.erp.modules.shop.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ShopServiceResponseDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String procInsId; // 流程实例id
    private String orderNumber;// 订单编号
    private String goodName;// 商品名称
    private String serviceItemName;// 服务项目名称
    private String operationAdviserName;// 运营顾问名字
    private Date startTime;// 服务开始时间
    private Date endTime;// 服务结束时间
    private String pendingServiceNum;// 待处理服务数量
    private Integer serviceTerm;// 服务期限
    private Date expirationTime;// 到期时间
    private String availability;// 有效性
    private Integer serviceTimesSum; // 服务总次数
    private Integer pendingServiceTimesSum; // 待处理服务次数

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getServiceItemName() {
        return serviceItemName;
    }

    public void setServiceItemName(String serviceItemName) {
        this.serviceItemName = serviceItemName;
    }

    public String getOperationAdviserName() {
        return operationAdviserName;
    }

    public void setOperationAdviserName(String operationAdviserName) {
        this.operationAdviserName = operationAdviserName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPendingServiceNum() {
        return pendingServiceNum;
    }

    public void setPendingServiceNum(String pendingServiceNum) {
        this.pendingServiceNum = pendingServiceNum;
    }

    public Integer getServiceTerm() {
        return serviceTerm;
    }

    public void setServiceTerm(Integer serviceTerm) {
        this.serviceTerm = serviceTerm;
    }


    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Integer getServiceTimesSum() {
        return serviceTimesSum;
    }

    public void setServiceTimesSum(Integer serviceTimesSum) {
        this.serviceTimesSum = serviceTimesSum;
    }

    public Integer getPendingServiceTimesSum() {
        return pendingServiceTimesSum;
    }

    public void setPendingServiceTimesSum(Integer pendingServiceTimesSum) {
        this.pendingServiceTimesSum = pendingServiceTimesSum;
    }


}
