package com.yunnex.ops.erp.modules.statistics.entity;

import java.util.ArrayList;
import java.util.List;

import com.yunnex.ops.erp.common.utils.excel.annotation.MapperCell;

public class ErpStatistics {
    @MapperCell(cellName = "订单号", order = 1)
    private String orderNum;// 订单号
    @MapperCell(cellName = "购买时间", order = 2)
    private String buyDate;// 购买时间
    @MapperCell(cellName = "订单类别", order = 3)
    private String orderCategory;// 订单类别
    @MapperCell(cellName = "商户名称", order = 4)
    private String shopName;// 商户名称
    @MapperCell(cellName = "已购服务处理数量", order = 5)
    private String serviceAndNum;// 已购服务处理数量
    @MapperCell(cellName = "接入时间", order = 6)
    private String accessDate;// 接入时间
    @MapperCell(cellName = "订单备注", order = 7)
    private String remarks;// 订单备注
    @MapperCell(cellName = "服务商名称", order = 8)
    private String agent;// 服务商名称
    @MapperCell(cellName = "策划专家", order = 9)
    private String planningExpert;// 策划专家
    @MapperCell(cellName = "运营顾问", order = 10)
    private String operationAdviser;// 运营顾问
    @MapperCell(cellName = "订单状态提醒", order = 11)
    private String orderState;// 订单状态提醒
    @MapperCell(cellName = "交付周期", order = 12)
    private String deliveryCycle;// 交付周期
    @MapperCell(cellName = "推广通道", order = 13)
    private String extensionChannel;// 推广通道
    @MapperCell(cellName = "朋友圈上线日期", order = 14)
    private String friendDate;// 朋友圈上线日期
    @MapperCell(cellName = "陌陌上线日期", order = 15)
    private String momoDate;// 陌陌上线日期
    @MapperCell(cellName = "微博上线日期", order = 16)
    private String weiboDate;// 微博上线日期
    @MapperCell(cellName = "完成效果报告日期", order = 17)
    private String createPresentation;// 完成效果报告日期
    @MapperCell(cellName = "工单性质", order = 18)
    private String nature;// 工单性质
    @MapperCell(cellName = "超时风险", order = 19)
    private String timeoutFlag;// 超时风险
    @MapperCell(cellName = "超时状态", order = 20)
    private String taskStatus;//有超时任务的订单或者即将超时的订单或者正常的订单
    @MapperCell(cellName = "上线时间", order = 21)
    private String onlineDate;
    @MapperCell(cellName = "手动标记完成时间", order = 22)
    private String manualDate;
    @MapperCell(cellName = "项目异常原因", order = 23)
    private String projectAnomaly;
    
    private String pid;// 任务流程id
    private String sid;
    private Integer commentCount; // 评论数

    private List<String> currentTasks = new ArrayList<String>();// 当前任务

    public Integer getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getServiceAndNum() {
        return serviceAndNum;
    }

    public void setServiceAndNum(String serviceAndNum) {
        this.serviceAndNum = serviceAndNum;
    }

    public String getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(String accessDate) {
        this.accessDate = accessDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getPlanningExpert() {
        return planningExpert;
    }

    public void setPlanningExpert(String planningExpert) {
        this.planningExpert = planningExpert;
    }

    public String getOperationAdviser() {
        return operationAdviser;
    }

    public void setOperationAdviser(String operationAdviser) {
        this.operationAdviser = operationAdviser;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getDeliveryCycle() {
        return deliveryCycle;
    }

    public void setDeliveryCycle(String deliveryCycle) {
        this.deliveryCycle = deliveryCycle;
    }

    public String getExtensionChannel() {
        return extensionChannel;
    }

    public void setExtensionChannel(String extensionChannel) {
        this.extensionChannel = extensionChannel;
    }

    public String getFriendDate() {
        return friendDate;
    }

    public void setFriendDate(String friendDate) {
        this.friendDate = friendDate;
    }

    public String getMomoDate() {
        return momoDate;
    }

    public void setMomoDate(String momoDate) {
        this.momoDate = momoDate;
    }

    public String getWeiboDate() {
        return weiboDate;
    }

    public void setWeiboDate(String weiboDate) {
        this.weiboDate = weiboDate;
    }

    public String getCreatePresentation() {
        return createPresentation;
    }

    public void setCreatePresentation(String createPresentation) {
        this.createPresentation = createPresentation;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }
    public List<String> getCurrentTasks() {
        return currentTasks;
    }
    public void setCurrentTasks(List<String> currentTasks) {
        this.currentTasks = currentTasks;
    }
	public String getTimeoutFlag() {
		return timeoutFlag;
	}
	public void setTimeoutFlag(String timeoutFlag) {
		this.timeoutFlag = timeoutFlag;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getOnlineDate() {
		return onlineDate;
	}
	public void setOnlineDate(String onlineDate) {
		this.onlineDate = onlineDate;
	}
	public String getManualDate() {
		return manualDate;
	}
	public void setManualDate(String manualDate) {
		this.manualDate = manualDate;
	}
	public String getProjectAnomaly() {
		return projectAnomaly;
	}
	public void setProjectAnomaly(String projectAnomaly) {
		this.projectAnomaly = projectAnomaly;
	}
	


}
