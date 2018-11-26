package com.yunnex.ops.erp.modules.shop.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class JykServiceResponseDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String procInsId; // 流程实例id
    private String orderNumber;// 订单编号
    private String orderSplitNumber;// 订单编号+分单号
    private String goodName;// 商品名称
    private String version;// 套餐版本
    private Integer pendingNum;// 待处理商品数量
    private Integer splitNum;// 商品分单数量
    private String operationAdviserName;// 运营顾问名字
    private String planningExpertName;// 策划专家名字
    private Date startTime; // 流程开始时间
    private Date endTime;// 流程结束时间

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOrderSplitNumber() {
        return orderSplitNumber;
    }

    public void setOrderSplitNumber(String orderSplitNumber) {
        this.orderSplitNumber = orderSplitNumber;
    }

    public Integer getSplitNum() {
        return splitNum;
    }

    public void setSplitNum(Integer splitNum) {
        this.splitNum = splitNum;
    }

    public void setPendingNum(Integer pendingNum) {
        this.pendingNum = pendingNum;
    }

    public Integer getPendingNum() {
        return pendingNum;
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

    public String getPlanningExpertName() {
        return planningExpertName;
    }

    public void setPlanningExpertName(String planningExpertName) {
        this.planningExpertName = planningExpertName;
    }

}
