package com.yunnex.ops.erp.modules.visit.entity;


import java.util.List;

import com.yunnex.ops.erp.common.persistence.RequestDto;

public class ErpServiceExp extends RequestDto<ErpServiceExp> {
	private static final long serialVersionUID = 1L;
    private String teamName; // 团队名称
    private String teamId; // 团队名称
    private List<String> teamIds; // 团队名称
    private String teamType; // 团队类别
    /** 开始时间 */
    private String startDateStr;
    /** 结束时间 */
    private String endDateStr;
    
    private String expType;
    
    /** 商户名称 */
    private String shopName;
    
    /** 服务类别*/
    private String serviceType;
    
    /** 用户 */
    private String userId;
    private List<String> userIds;
    
    
	public List<String> getTeamIds() {
		return teamIds;
	}
	public void setTeamIds(List<String> teamIds) {
		this.teamIds = teamIds;
	}
	public List<String> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getExpType() {
		return expType;
	}
	public void setExpType(String expType) {
		this.expType = expType;
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
	public String getTeamType() {
		return teamType;
	}
	public void setTeamType(String teamType) {
		this.teamType = teamType;
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
}
