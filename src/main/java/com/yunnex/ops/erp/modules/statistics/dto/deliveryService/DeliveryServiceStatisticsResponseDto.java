package com.yunnex.ops.erp.modules.statistics.dto.deliveryService;

import com.yunnex.ops.erp.common.persistence.ResponseDto;

/**
 * 
 * @author linqunzhi
 * @date 2018年5月28日
 */
public class DeliveryServiceStatisticsResponseDto extends ResponseDto<DeliveryServiceStatisticsResponseDto> {

    private static final long serialVersionUID = -8119115830232361748L;

    /** 订单号 */
    private String orderNum;

    /** 购买时间 */
    private String buyDate;

    /** 订单类型 */
    private String orderType;

    /** 商户简称 */
    private String shopName;

    /** 服务类型 */
    private String serviceTypeNames;

    /** 所属服务商 */
    private String agentName;

    /** 运营顾问 */
    private String operationAdviserName;

    /** 启动时间 */
    private String startTime;

    /** 应完成交付时间 */
    private String shouldFlowEndTime;

    /** 是否延期 */
    private String delayFlag;

    /** 任务完成进度 */
    private String taskSchedule;

    /** 是否已完成交付 */
    private String flowEndFlag;

    /** 实际完成交付时间 */
    private String flowEndTime;

    /** 开户顾问是否延迟 */
    private String openDelayFlag;

    /** 物料顾问是否延迟 */
    private String materielDelayFlag;

    /** 运营顾问是否延迟 */
    private String operationDelayFlag;
    /** 延迟时长 */
   
    private String delayDuration;
    /** 开户延迟时长 */
    private String openDelayDuration;
    
    private String roles;

    /** 物料顾问延迟时长 */
    private String materielDelayDuration;

    /** 运营顾问延迟时长 */
    private String operationDelayDuration;

    /** 订单id */
    private String orderId;

    /** 商户id */
    private String shopId;

    /** 流程id */
    private String procInsId;
    
    /** 流程id */
    private String excptionLogo;
    
    /** 流程id */
    private String serviceType;
    
    private String trainTestTime;

    private String shouldTrainTestTime;

    private String materielTime;

    private String shouldMaterielTime;

    private String visitServiceTime;

    private String shouldVisitServiceTime;
    
    public String getTrainTestTime() {
		return trainTestTime;
	}

	public void setTrainTestTime(String trainTestTime) {
		this.trainTestTime = trainTestTime;
	}

	public String getShouldTrainTestTime() {
		return shouldTrainTestTime;
	}

	public void setShouldTrainTestTime(String shouldTrainTestTime) {
		this.shouldTrainTestTime = shouldTrainTestTime;
	}

	public String getMaterielTime() {
		return materielTime;
	}

	public void setMaterielTime(String materielTime) {
		this.materielTime = materielTime;
	}

	public String getShouldMaterielTime() {
		return shouldMaterielTime;
	}

	public void setShouldMaterielTime(String shouldMaterielTime) {
		this.shouldMaterielTime = shouldMaterielTime;
	}

	public String getVisitServiceTime() {
		return visitServiceTime;
	}

	public void setVisitServiceTime(String visitServiceTime) {
		this.visitServiceTime = visitServiceTime;
	}

	public String getShouldVisitServiceTime() {
		return shouldVisitServiceTime;
	}

	public void setShouldVisitServiceTime(String shouldVisitServiceTime) {
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

	public String getDelayDuration() {
		return delayDuration;
	}

	public void setDelayDuration(String delayDuration) {
		this.delayDuration = delayDuration;
	}

	public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getShouldFlowEndTime() {
        return shouldFlowEndTime;
    }

    public void setShouldFlowEndTime(String shouldFlowEndTime) {
        this.shouldFlowEndTime = shouldFlowEndTime;
    }

    public String getTaskSchedule() {
        return taskSchedule;
    }

    public void setTaskSchedule(String taskSchedule) {
        this.taskSchedule = taskSchedule;
    }

    public String getFlowEndFlag() {
        return flowEndFlag;
    }

    public void setFlowEndFlag(String flowEndFlag) {
        this.flowEndFlag = flowEndFlag;
    }

    public String getFlowEndTime() {
        return flowEndTime;
    }

    public void setFlowEndTime(String flowEndTime) {
        this.flowEndTime = flowEndTime;
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

    public String getOpenDelayDuration() {
        return openDelayDuration;
    }

    public void setOpenDelayDuration(String openDelayDuration) {
        this.openDelayDuration = openDelayDuration;
    }

    public String getMaterielDelayDuration() {
        return materielDelayDuration;
    }

    public void setMaterielDelayDuration(String materielDelayDuration) {
        this.materielDelayDuration = materielDelayDuration;
    }

    public String getOperationDelayDuration() {
        return operationDelayDuration;
    }

    public void setOperationDelayDuration(String operationDelayDuration) {
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

    public String getDelayFlag() {
        return delayFlag;
    }

    public void setDelayFlag(String delayFlag) {
        this.delayFlag = delayFlag;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

}
