package com.yunnex.ops.erp.modules.visit.entity;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.RequestDto;

public class ErpServiceCount extends RequestDto<ErpServiceCount> {
	private static final long serialVersionUID = 1L;
    private String teamName; // 团队名称
    private String teamId; // 团队名称
    private int agentId; // 团队名称
    private String teamType; // 团队类别
    private String serviceType; // 团队类别
    private int zhangbeiCount;//掌贝个数
    private int firstVisitCount; // 首次
    private int afterSalesCount;//售后维护实施
    private int jykCount;//优化远程
    private int fmpsbasicCount;//优化上门
    private int afterVisitCount;//售后上门培训
    private int zhctCount;//售后维修培训
    private int updateMatraCount;//物料更新
    /** 开始时间 */
    private String startDateStr;
    /** 结束时间 */
    private String endDateStr;
    
    /** 服务商编号 */
    private List<Integer> agentIdList;
    private List<String> teamIds; // 团队名称
	/** 订单类别 （1：直销 2：服务商） */
    private List<Integer> orderTypeList;
    
    private String code;
    
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public List<String> getTeamIds() {
		return teamIds;
	}
	public void setTeamIds(List<String> teamIds) {
		this.teamIds = teamIds;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public int getAgentId() {
		return agentId;
	}
	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}
	public String getTeamType() {
		return teamType;
	}
	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}
	public int getFirstVisitCount() {
		return firstVisitCount;
	}
	public void setFirstVisitCount(int firstVisitCount) {
		this.firstVisitCount = firstVisitCount;
	}
	public int getZhangbeiCount() {
		return zhangbeiCount;
	}
	public void setZhangbeiCount(int zhangbeiCount) {
		this.zhangbeiCount = zhangbeiCount;
	}
	public int getAfterSalesCount() {
		return afterSalesCount;
	}
	public void setAfterSalesCount(int afterSalesCount) {
		this.afterSalesCount = afterSalesCount;
	}
	public int getAfterVisitCount() {
		return afterVisitCount;
	}
	public void setAfterVisitCount(int afterVisitCount) {
		this.afterVisitCount = afterVisitCount;
	}
	
	public int getJykCount() {
		return jykCount;
	}
	public void setJykCount(int jykCount) {
		this.jykCount = jykCount;
	}
	public int getFmpsbasicCount() {
		return fmpsbasicCount;
	}
	public void setFmpsbasicCount(int fmpsbasicCount) {
		this.fmpsbasicCount = fmpsbasicCount;
	}
	public int getZhctCount() {
		return zhctCount;
	}
	public void setZhctCount(int zhctCount) {
		this.zhctCount = zhctCount;
	}
	public int getUpdateMatraCount() {
		return updateMatraCount;
	}
	public void setUpdateMatraCount(int updateMatraCount) {
		this.updateMatraCount = updateMatraCount;
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
    
}
