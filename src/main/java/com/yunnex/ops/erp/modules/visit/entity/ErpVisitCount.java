package com.yunnex.ops.erp.modules.visit.entity;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.RequestDto;

public class ErpVisitCount extends RequestDto<ErpVisitCount> {
	private static final long serialVersionUID = 1L;
    private String teamName; // 团队名称
    private int agentId; // 团队名称
    private String userId; // 团队id
    private String teamId; // 团队id
    private List<String> teamIds; // 团队id
    private int firstBasicVisitCount; // 首次
    private int materialImplCount;//物料实施
    private int firstVisitCount; // 首次
    private int jykVisitCount;//聚引客实施
    private int trainingCount;//收费培训
    private int freeTrainingCount;//免费培训
    private int comHandCount;//投诉处理
    private int materialUpdateCount;//物料更新
    private int zhctServiceCount;//智慧餐厅服务
    /** 开始时间 */
    private String startDateStr;
    /** 结束时间 */
    private String endDateStr;
    
    /** 服务商编号 */
    private List<Integer> agentIdList;

	/** 订单类别 （1：直销 2：服务商） */
    private List<Integer> orderTypeList;
    
    private int code;//服务code
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<String> getTeamIds() {
		return teamIds;
	}
	public void setTeamIds(List<String> teamIds) {
		this.teamIds = teamIds;
	}
	public int getZhctServiceCount() {
		return zhctServiceCount;
	}
	public void setZhctServiceCount(int zhctServiceCount) {
		this.zhctServiceCount = zhctServiceCount;
	}
	public int getFirstBasicVisitCount() {
		return firstBasicVisitCount;
	}
	public void setFirstBasicVisitCount(int firstBasicVisitCount) {
		this.firstBasicVisitCount = firstBasicVisitCount;
	}
	public int getTrainingCount() {
		return trainingCount;
	}
	public void setTrainingCount(int trainingCount) {
		this.trainingCount = trainingCount;
	}
	public int getMaterialUpdateCount() {
		return materialUpdateCount;
	}
	public void setMaterialUpdateCount(int materialUpdateCount) {
		this.materialUpdateCount = materialUpdateCount;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
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
	
	public int getAgentId() {
		return agentId;
	}
	public void setAgentId(int agentId) {
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
	public int getFirstVisitCount() {
		return firstVisitCount;
	}
	public void setFirstVisitCount(int firstVisitCount) {
		this.firstVisitCount = firstVisitCount;
	}
	public int getMaterialImplCount() {
		return materialImplCount;
	}
	public void setMaterialImplCount(int materialImplCount) {
		this.materialImplCount = materialImplCount;
	}
	public int getJykVisitCount() {
		return jykVisitCount;
	}
	public void setJykVisitCount(int jykVisitCount) {
		this.jykVisitCount = jykVisitCount;
	}
	public int getFreeTrainingCount() {
		return freeTrainingCount;
	}
	public void setFreeTrainingCount(int freeTrainingCount) {
		this.freeTrainingCount = freeTrainingCount;
	}
	public int getComHandCount() {
		return comHandCount;
	}
	public void setComHandCount(int comHandCount) {
		this.comHandCount = comHandCount;
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
