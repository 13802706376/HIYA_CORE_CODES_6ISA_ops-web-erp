package com.yunnex.ops.erp.modules.order.entity;

import java.util.Date;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 订单商品服务Entity
 * @author yunnex
 * @version 2018-06-02
 */
public class ErpOrderGoodServiceInfo extends DataEntity<ErpOrderGoodServiceInfo> {
	
	private static final long serialVersionUID = 1L;
	private String orderId;		// 订单编号
	private String goodName;		// 商品名称
    private String serviceItemName; // 服务项目名称
    private String serviceItemId; // 服务项目ID
    private Integer serviceNum; // 服务数量
    private Integer pendingNum; // 待处理数量
    private Integer processNum; // 处理中数量
    private Integer finishNum; // 已完成数量
    private Integer serviceTerm; // 服务期限
	private Date expirationTime;		// 过期时间
	private String remark;		// 备注

    /*扩展属性*/
    private Boolean isValid; // 是否有效

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }


    public Integer getServiceNum() {
        return serviceNum;
    }

    public void setServiceNum(Integer serviceNum) {
        this.serviceNum = serviceNum;
    }

    public Integer getPendingNum() {
        return pendingNum;
    }

    public void setPendingNum(Integer pendingNum) {
        this.pendingNum = pendingNum;
    }

    public Integer getProcessNum() {
        return processNum;
    }

    public void setProcessNum(Integer processNum) {
        this.processNum = processNum;
    }

    public Integer getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(Integer finishNum) {
        this.finishNum = finishNum;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getServiceTerm() {
        return serviceTerm;
    }

    public void setServiceTerm(Integer serviceTerm) {
        this.serviceTerm = serviceTerm;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public String getServiceItemName() {
        return serviceItemName;
    }

    public void setServiceItemName(String serviceItemName) {
        this.serviceItemName = serviceItemName;
    }

    public String getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(String serviceItemId) {
        this.serviceItemId = serviceItemId;
    }
	
	
	
	
	
}