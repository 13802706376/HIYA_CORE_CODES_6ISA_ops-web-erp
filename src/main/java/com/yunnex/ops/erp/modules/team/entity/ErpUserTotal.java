package com.yunnex.ops.erp.modules.team.entity;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.RequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.OrderSecurityRequestDto;
import com.yunnex.ops.erp.modules.sys.entity.User;

/**
 * 团队Entity
 * @author wangwei
 * @version 2017-10-26
 */
public class ErpUserTotal extends RequestDto<ErpUserTotal> {

	private static final long serialVersionUID = 1L;
    private Integer agentId; // 服务商编号
    private String userId; // 服务商编号
    private List<String> userIds; // 服务商编号
    private String userName; // 服务商编号
    private String teamName; // 团队名称
    private String teamId; // 团队id
    private List<String> teamIds; // 团队id
    private String flowUserId;
    private String flowName; 
    private String flowRole; 
    private int newCount; // 新订单个数
    private int shouldflowCount; // 应完成订单数
    private int flowEndCount; // 实际完成订单数
    private int noCompleteCount; // 延期未完成订单数
    private int completeCount; // 延期已完成订单数
    private int completeExCount; // 延期已完成没异常订单数
    private String percentage; // 百分比
    /** 开始时间 */
    private String startDateStr;
    /** 结束时间 */
    private String endDateStr;
    
    private String serviceType;
    
    /** 订单权限配置过滤 */
    
    /** 服务商编号 */
    private List<Integer> agentIdList;

	
	public String getFlowRole() {
		return flowRole;
	}
	public void setFlowRole(String flowRole) {
		this.flowRole = flowRole;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
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
	/** 订单类别 （1：直销 2：服务商） */
    private List<Integer> orderTypeList;
    
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
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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
	public Integer getAgentId() {
		return agentId;
	}
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public int getNewCount() {
		return newCount;
	}
	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}
	public int getShouldflowCount() {
		return shouldflowCount;
	}
	public void setShouldflowCount(int shouldflowCount) {
		this.shouldflowCount = shouldflowCount;
	}
	public int getFlowEndCount() {
		return flowEndCount;
	}
	public void setFlowEndCount(int flowEndCount) {
		this.flowEndCount = flowEndCount;
	}
	public int getNoCompleteCount() {
		return noCompleteCount;
	}
	public void setNoCompleteCount(int noCompleteCount) {
		this.noCompleteCount = noCompleteCount;
	}
	public int getCompleteCount() {
		return completeCount;
	}
	public void setCompleteCount(int completeCount) {
		this.completeCount = completeCount;
	}
	public int getCompleteExCount() {
		return completeExCount;
	}
	public void setCompleteExCount(int completeExCount) {
		this.completeExCount = completeExCount;
	}
    	

}
