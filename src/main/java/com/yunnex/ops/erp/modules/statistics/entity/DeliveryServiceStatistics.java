package com.yunnex.ops.erp.modules.statistics.entity;

import java.util.Date;

/**
 * 生产服务明细数据
 * 
 * @author linqunzhi
 * @date 2018年5月28日
 */
public class DeliveryServiceStatistics {


    /** 订单号 */
    private String orderNumber;

    /** 购买时间 */
    private Date buyDate;

    /** 订单类型 */
    private Integer orderType;

    /** 商户简称 */
    private String shopName;

    /** 服务类型 */
    private String serviceTypeNames;

    /** 所属服务商 */
    private String agentName;

    /** 运营顾问 */
    private String operationAdviserName;

    /** 启动时间 */
    private Date startTime;

    /** 应完成交付时间 */
    private Date shouldFlowEndTime;

    /** 实际完成交付时间 */
    private Date flowEndTime;

    /** 是否已完成交付 */
    private String flowEndFlag;

    /** 是否延期 */
    private String delayFlag;

    /** 开户顾问是否延迟 */
    private String openDelayFlag;

    /** 物料顾问是否延迟 */
    private String materielDelayFlag;

    /** 运营顾问是否延迟 */
    private String operationDelayFlag;

    /** 开户延迟时长 */
    private Integer delayDuration;
    
    /** 开户延迟时长 */
    private Integer openDelayDuration;

    /** 物料顾问延迟时长 */
    private Integer materielDelayDuration;

    /** 运营顾问延迟时长 */
    private Integer operationDelayDuration;

    /** 订单id */
    private String orderId;

    /** 商户id */
    private String shopId;

    /** 流程id */
    private String procInsId;
    
    private String excptionLogo;
    
    private String serviceType;
    
    private String roles;
    
    /** 银联支付培训&测试（远程）任务完成时间 */
    private Date trainTestTime;

    /** 银联支付培训&测试（远程）任务应该完成时间 */
    private Date shouldTrainTestTime;

    /** 物料制作跟踪任务完成时间 */
    private Date materielTime;

    /** 物料制作跟踪任务应该完成时间 */
    private Date shouldMaterielTime;

    /** 上门服务完成（首次营销策划服务）任务完成时间 */
    private Date visitServiceTime;

    /** 上门服务完成（首次营销策划服务）任务应该完成时间 */
    private Date shouldVisitServiceTime;
    
    public Date getTrainTestTime() {
		return trainTestTime;
	}

	public void setTrainTestTime(Date trainTestTime) {
		this.trainTestTime = trainTestTime;
	}

	public Date getShouldTrainTestTime() {
		return shouldTrainTestTime;
	}

	public void setShouldTrainTestTime(Date shouldTrainTestTime) {
		this.shouldTrainTestTime = shouldTrainTestTime;
	}

	public Date getMaterielTime() {
		return materielTime;
	}

	public void setMaterielTime(Date materielTime) {
		this.materielTime = materielTime;
	}

	public Date getShouldMaterielTime() {
		return shouldMaterielTime;
	}

	public void setShouldMaterielTime(Date shouldMaterielTime) {
		this.shouldMaterielTime = shouldMaterielTime;
	}

	public Date getVisitServiceTime() {
		return visitServiceTime;
	}

	public void setVisitServiceTime(Date visitServiceTime) {
		this.visitServiceTime = visitServiceTime;
	}

	public Date getShouldVisitServiceTime() {
		return shouldVisitServiceTime;
	}

	public void setShouldVisitServiceTime(Date shouldVisitServiceTime) {
		this.shouldVisitServiceTime = shouldVisitServiceTime;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getExcptionLogo() {
		return excptionLogo;
	}

	public void setExcptionLogo(String excptionLogo) {
		this.excptionLogo = excptionLogo;
	}

	public Integer getDelayDuration() {
		return delayDuration;
	}

	public void setDelayDuration(Integer delayDuration) {
		this.delayDuration = delayDuration;
	}

	public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getServiceTypeNames() {
        return serviceTypeNames;
    }

    public void setServiceTypeNames(String serviceTypeNames) {
        this.serviceTypeNames = serviceTypeNames;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getOperationAdviserName() {
        return operationAdviserName;
    }

    public void setOperationAdviserName(String operationAdviserName) {
        this.operationAdviserName = operationAdviserName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getShouldFlowEndTime() {
        return shouldFlowEndTime;
    }

    public void setShouldFlowEndTime(Date shouldFlowEndTime) {
        this.shouldFlowEndTime = shouldFlowEndTime;
    }

    public Date getFlowEndTime() {
        return flowEndTime;
    }

    public void setFlowEndTime(Date flowEndTime) {
        this.flowEndTime = flowEndTime;
    }

    public String getDelayFlag() {
        return delayFlag;
    }

    public void setDelayFlag(String delayFlag) {
        this.delayFlag = delayFlag;
    }

    public String getOpenDelayFlag() {
        return openDelayFlag;
    }

    public void setOpenDelayFlag(String openDelayFlag) {
        this.openDelayFlag = openDelayFlag;
    }

    public String getMaterielDelayFlag() {
        return materielDelayFlag;
    }

    public void setMaterielDelayFlag(String materielDelayFlag) {
        this.materielDelayFlag = materielDelayFlag;
    }

    public String getOperationDelayFlag() {
        return operationDelayFlag;
    }

    public void setOperationDelayFlag(String operationDelayFlag) {
        this.operationDelayFlag = operationDelayFlag;
    }

    public Integer getOpenDelayDuration() {
        return openDelayDuration;
    }

    public void setOpenDelayDuration(Integer openDelayDuration) {
        this.openDelayDuration = openDelayDuration;
    }

    public Integer getMaterielDelayDuration() {
        return materielDelayDuration;
    }

    public void setMaterielDelayDuration(Integer materielDelayDuration) {
        this.materielDelayDuration = materielDelayDuration;
    }

    public Integer getOperationDelayDuration() {
        return operationDelayDuration;
    }

    public void setOperationDelayDuration(Integer operationDelayDuration) {
        this.operationDelayDuration = operationDelayDuration;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getFlowEndFlag() {
        return flowEndFlag;
    }

    public void setFlowEndFlag(String flowEndFlag) {
        this.flowEndFlag = flowEndFlag;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

}
