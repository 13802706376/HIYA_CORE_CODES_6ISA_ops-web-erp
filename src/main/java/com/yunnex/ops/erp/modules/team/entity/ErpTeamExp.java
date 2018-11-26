package com.yunnex.ops.erp.modules.team.entity;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.RequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.OrderSecurityRequestDto;

/**
 * 团队Entity
 * @author wangwei
 * @version 2017-10-26
 */
public class ErpTeamExp extends RequestDto<ErpTeamExp> {

	private static final long serialVersionUID = 1L;
    private String teamId; // 团队id
    
    private String userId; // 人员id
    private List<String> teamIds; // 团队id
    private List<String> userIds; // 人员id
    private List<String> serviceUsers; // 人员id
    private String percentage; // 百分比
    /** 开始时间 */
    private String startDateStr;
    /** 结束时间 */
    private String endDateStr;
    
    private String expType;
    private String flowUserId;//角色
    /** 订单权限配置过滤 */
    
    /** 服务商编号 */
    private List<Integer> agentIdList;

	public List<String> getServiceUsers() {
		return serviceUsers;
	}
	public void setServiceUsers(List<String> serviceUsers) {
		this.serviceUsers = serviceUsers;
	}
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
	/** 订单类别 （1：直销 2：服务商） */
    private List<Integer> orderTypeList;
    
    
    public String getFlowUserId() {
		return flowUserId;
	}
	public void setFlowUserId(String flowUserId) {
		this.flowUserId = flowUserId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getExpType() {
		return expType;
	}
	public void setExpType(String expType) {
		this.expType = expType;
	}
	private int curCount;
    
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	
	public int getCurCount() {
		return curCount;
	}
	public void setCurCount(int curCount) {
		this.curCount = curCount;
	}
	public List<Integer> getAgentIdList() {
		return agentIdList;
	}
	public void setAgentIdList(List<Integer> agentIdList) {
		this.agentIdList = agentIdList;
	}
	public List<Integer> getOrderTypeList() {
		return orderTypeList;
	}
	public void setOrderTypeList(List<Integer> orderTypeList) {
		this.orderTypeList = orderTypeList;
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
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
}
