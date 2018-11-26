package com.yunnex.ops.erp.modules.statistics.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供前台接口api
 * @author yunnex
 * @version 2017-12-09
 * 封装
 */

public class ErpStatisticsApi {
	private Integer newCount;
	private Integer onlineCount;
	private Integer createCount;
	private List<ErpStatistics>  statistics=new ArrayList<ErpStatistics>();

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
	public Integer getCreateCount() {
		return createCount;
	}
	public void setCreateCount(Integer createCount) {
		this.createCount = createCount;
	}
	public List<ErpStatistics> getStatistics() {
		return statistics;
	}
	public void setStatistics(List<ErpStatistics> statistics) {
		this.statistics = statistics;
	}
	
	

}
