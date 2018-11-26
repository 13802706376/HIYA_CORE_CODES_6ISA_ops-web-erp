package com.yunnex.ops.erp.modules.statistics.dto.deliveryService;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.RequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.OrderSecurityRequestDto;

/**
 * 生产服务报表 requestDto
 * 
 * @author linqunzhi
 * @date 2018年5月28日
 */
public class DeliveryServiceStatisticsRequestDto extends RequestDto<DeliveryServiceStatisticsRequestDto> {

    private static final long serialVersionUID = 8889717356532795096L;

    /** 时间类型 */
    private String dateType;

    /** 开始时间 */
    private String startDateStr;

    /** 结束时间 */
    private String endDateStr;

    /** 订单号 */
    private String orderNum;

    /** 商户名称 */
    private String shopName;

    /** 服务商编号 */
    private List<Integer> agentIdList;

    /** 团队id */
    private String teamId;

    /** 成员id集合 */
    private List<String> userIdList;

    /** 服务类型 */
    private List<ServiceTypeDto> serviceTypeList;

    /** 订单类别 （1：直销 2：服务商） */
    private List<Integer> orderTypeList;
    
    /** 是否已完成交付 （Y：是，N：否） */
    private String flowEndFlag;

    /** 是否延期交付 （Y：是，N：否） */
    private String delayFlag;
    
    /** 成员id集合 */
    private List<String> serviceTypes;
    
    /** 成员id集合 */
    private List<String> serviceCodes;
    
    /** 成员id集合 */
    private String serviceCode;
    
    /** 成员id集合 */
    private String isFlag;

    /** 订单权限配置过滤 */
    private OrderSecurityRequestDto orderSecurity;

    private String expType;
    
    
	public List<String> getServiceCodes() {
		return serviceCodes;
	}

	public void setServiceCodes(List<String> serviceCodes) {
		this.serviceCodes = serviceCodes;
	}

	public String getIsFlag() {
		return isFlag;
	}

	public void setIsFlag(String isFlag) {
		this.isFlag = isFlag;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getExpType() {
		return expType;
	}

	public void setExpType(String expType) {
		this.expType = expType;
	}

	public List<String> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<String> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

	public OrderSecurityRequestDto getOrderSecurity() {
        return orderSecurity;
    }

    public void setOrderSecurity(OrderSecurityRequestDto orderSecurity) {
        this.orderSecurity = orderSecurity;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public List<ServiceTypeDto> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<ServiceTypeDto> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }

    public List<Integer> getOrderTypeList() {
        return orderTypeList;
    }

    public void setOrderTypeList(List<Integer> orderTypeList) {
        this.orderTypeList = orderTypeList;
    }

    public String getFlowEndFlag() {
        return flowEndFlag;
    }

    public void setFlowEndFlag(String flowEndFlag) {
        this.flowEndFlag = flowEndFlag;
    }

    public String getDelayFlag() {
        return delayFlag;
    }

    public void setDelayFlag(String delayFlag) {
        this.delayFlag = delayFlag;
    }

    public List<Integer> getAgentIdList() {
        return agentIdList;
    }

    public void setAgentIdList(List<Integer> agentIdList) {
        this.agentIdList = agentIdList;
    }
}
