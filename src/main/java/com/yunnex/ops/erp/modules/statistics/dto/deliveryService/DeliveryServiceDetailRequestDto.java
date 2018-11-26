package com.yunnex.ops.erp.modules.statistics.dto.deliveryService;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.RequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.OrderSecurityRequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.PageResponseDto;

/**
 * 生产服务报表 requestDto
 * 
 * @author linqunzhi
 * @date 2018年5月28日
 */
public class DeliveryServiceDetailRequestDto extends RequestDto<DeliveryServiceDetailRequestDto> {

    private static final long serialVersionUID = 8889717356532795096L;

    /** 开始时间 */
    private String startDateStr;

    /** 结束时间 */
    private String endDateStr;
    
    /** 用户 */
    private String userId;
    
    /** 用户 */
    private List<String> userIds;
    
    /** 结束时间 */
    private String endTime;

    /** 商户名称 */
    private String shopName;
    
    /** 服务类别*/
    private String serviceType;
    
    /** 团队类别 */
    private String teamType;
    
    private String procInsId;

    /** 团队id */
    private String teamId;
    private List<String> teamIds;
    /** 团队id */
    private String teamName;
    
    /** 团队类别 */
    private String agentType;//agent/branch
    
    /** 运营顾问*/
    private String operationAdviser;
    
    /** 物料顾问 */
    private String materialAdviser;
    
    /** 开户顾问*/
    private String accountAdviser;

    
	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}

	public List<String> getTeamIds() {
		return teamIds;
	}

	public void setTeamIds(List<String> teamIds) {
		this.teamIds = teamIds;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getTeamType() {
		return teamType;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public String getOperationAdviser() {
		return operationAdviser;
	}

	public void setOperationAdviser(String operationAdviser) {
		this.operationAdviser = operationAdviser;
	}

	public String getMaterialAdviser() {
		return materialAdviser;
	}

	public void setMaterialAdviser(String materialAdviser) {
		this.materialAdviser = materialAdviser;
	}

	public String getAccountAdviser() {
		return accountAdviser;
	}

	public void setAccountAdviser(String accountAdviser) {
		this.accountAdviser = accountAdviser;
	}

}
