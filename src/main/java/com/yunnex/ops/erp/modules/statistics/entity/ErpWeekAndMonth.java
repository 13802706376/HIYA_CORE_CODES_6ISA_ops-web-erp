package com.yunnex.ops.erp.modules.statistics.entity;

public class ErpWeekAndMonth {
	private Integer newCount;
	private Integer onlineCount;
	private Integer onlineCountOvertime;//上线订单中的超时订单数
	private String avgCycle;//上线订单平均周期
	private Integer dayOvertime;//超时的订单数累和
	private String starTime;
	private String endTime;
	
	public String getStarTime() {
		return starTime;
	}
	public void setStarTime(String starTime) {
		this.starTime = starTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getNewCount() {
		return newCount;
	}
	public void setNewCount(Integer newCount) {
		this.newCount = newCount;
	}
	public Integer getOnlineCount() {
		return onlineCount;
	}
	public void setOnlineCount(Integer onlineCount) {
		this.onlineCount = onlineCount;
	}
	public Integer getOnlineCountOvertime() {
		return onlineCountOvertime;
	}
	public void setOnlineCountOvertime(Integer onlineCountOvertime) {
		this.onlineCountOvertime = onlineCountOvertime;
	}
	public String getAvgCycle() {
		return avgCycle;
	}
	public void setAvgCycle(String avgCycle) {
		this.avgCycle = avgCycle;
	}
	public Integer getDayOvertime() {
		return dayOvertime;
	}
	public void setDayOvertime(Integer dayOvertime) {
		this.dayOvertime = dayOvertime;
	}
	
	
}
