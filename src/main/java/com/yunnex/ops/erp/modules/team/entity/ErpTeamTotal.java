package com.yunnex.ops.erp.modules.team.entity;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.RequestDto;
import com.yunnex.ops.erp.modules.statistics.dto.OrderSecurityRequestDto;

/**
 * 团队Entity
 * @author wangwei
 * @version 2017-10-26
 */
public class ErpTeamTotal extends RequestDto<ErpTeamTotal> {

	private static final long serialVersionUID = 1L;
	private String userId; // 服务商编号
    private Integer agentId; // 服务商编号
    private String teamName; // 团队名称
    private String teamId; // 团队id
    private List<String> teamIds; // 团队id
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

	/** 订单类别 （1：直销 2：服务商） */
    private List<Integer> orderTypeList;
    
    private int curCount;
    
	
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
