package com.yunnex.ops.erp.modules.statistics.entity;

public class ErpTeamFollowOrder {
	private String userId;
	private String userName;
	private String userRole;
	private Integer followOrder;//跟进订单
	private Integer taskOrder;//有任务正在处理订单
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public Integer getFollowOrder() {
		return followOrder;
	}
	public void setFollowOrder(Integer followOrder) {
		this.followOrder = followOrder;
	}
	public Integer getTaskOrder() {
		return taskOrder;
	}
	public void setTaskOrder(Integer taskOrder) {
		this.taskOrder = taskOrder;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	

}
