package com.yunnex.ops.erp.modules.workflow.flow.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 任务列表Entity
 * 
 * @author Frank
 * @version 2017-10-27
 */
public class FlowHistory {

    /** 订单编号 */
    private String orderNo;
    /** 商户名称 */
    private String shopName;
    /** 商品类型 */
    private String goodType;
    /** 订单类型 */
    private String orderType;
    /** 商品数量 */
    private String goodCount;
    /** 商品名称 */
    private String goodName;
    /** 购买时间 */
    private Date orderTime;
    /** 投放时间 */
    private Date deliveryTime;
    /** 加急状态 */
    private Integer hurryFlag;
    /** 联系方式 */
    private String contactWay;
    
    private String agentName;
    
    private String orderRemark;

    public String getAgentName()
    {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    private Integer num;// 分单数量
    
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    /** 子任务 */
    private List<TaskHistory> taskHistory = new ArrayList<TaskHistory>();
    
    private List<ErpOrderFile>  orderFileList;
    
    List<ErpOrderInputDetail> inputDetailList;
    
    

    public List<ErpOrderInputDetail> getInputDetailList() {
        return inputDetailList;
    }

    public void setInputDetailList(List<ErpOrderInputDetail> inputDetailList) {
        this.inputDetailList = inputDetailList;
    }

    public List<ErpOrderFile> getOrderFileList() {
        return orderFileList;
    }

    public void setOrderFileList(List<ErpOrderFile> orderFileList) {
        this.orderFileList = orderFileList;
    }

    public Integer getHurryFlag() {
        return hurryFlag;
    }

    public void setHurryFlag(Integer hurryFlag) {
        this.hurryFlag = hurryFlag;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

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

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(String goodCount) {
        this.goodCount = goodCount;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<TaskHistory> getTaskHistory() {
        return taskHistory;
    }

    public void setTaskHistory(List<TaskHistory> taskHistory) {
        this.taskHistory = taskHistory;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

}
