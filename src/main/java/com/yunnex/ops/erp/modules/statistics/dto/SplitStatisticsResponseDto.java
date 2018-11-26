package com.yunnex.ops.erp.modules.statistics.dto;

import com.yunnex.ops.erp.common.persistence.ResponseDto;
import com.yunnex.ops.erp.common.utils.excel.annotation.MapperCell;

/**
 * 分单报表 responseDto
 * 
 * @author linqunzhi
 * @date 2018年5月7日
 */
public class SplitStatisticsResponseDto extends ResponseDto<SplitStatisticsResponseDto> {

    private static final long serialVersionUID = -3075091638872532404L;

    /** 订单号 */
    @MapperCell(cellName = "订单号", order = 1)
    private String orderNum;

    /** 购买时间 */
    @MapperCell(cellName = "购买时间", order = 2)
    private String buyDate;

    /** 订单类别名称 */
    @MapperCell(cellName = "订单类别", order = 3)
    private String orderTypeName;

    /** 商户名称 */
    @MapperCell(cellName = "商户名称", order = 4)
    private String shopName;

    /** 已购服务 */
    @MapperCell(cellName = "已购服务处理数量", order = 5)
    private String serviceAndNums;

    /** 接入时间 */
    @MapperCell(cellName = "接入时间", order = 6)
    private String createDate;

    /** 备注 */
    @MapperCell(cellName = "订单备注", order = 7)
    private String remarks;

    /** 服务商 */
    @MapperCell(cellName = "服务商名称", order = 8)
    private String agentName;

    /** 策划专家 */
    @MapperCell(cellName = "策划专家", order = 9)
    private String planningExpertName;

    /** 运营顾问 */
    @MapperCell(cellName = "运营顾问", order = 10)
    private String operationAdviserName;

    /** 订单状态 */
    @MapperCell(cellName = "订单状态提醒", order = 11)
    private String splitStatusName;

    /** 交付周期 */
    @MapperCell(cellName = "交付周期", order = 12)
    private String deliveryCycle;

    /** 推广通道 */
    @MapperCell(cellName = "推广通道", order = 13)
    private String extensionChannel;

    /** 朋友圈上线时间 */
    @MapperCell(cellName = "朋友圈上线日期", order = 14)
    private String friendOnlineDate;

    /** 陌陌上线时间 */
    @MapperCell(cellName = "陌陌上线日期", order = 15)
    private String momoOnlineDate;

    /** 微博上线时间 */
    @MapperCell(cellName = "微博上线日期", order = 16)
    private String weiboOnlineDate;

    /** 完成效果报告时间 */
    @MapperCell(cellName = "完成效果报告日期", order = 17)
    private String presentationDate;

    /** 工单性质 */
    @MapperCell(cellName = "工单性质", order = 18)
    private String splitNatureNames;

    /** 是否有超时风险 */
    @MapperCell(cellName = "超时风险", order = 19)
    private String timeoutFlag;

    /** 执行中的任务是否存在超时 */
    @MapperCell(cellName = "超时状态", order = 20)
    private String taskTimeoutFlag;

    /** 订单上线时间 */
    @MapperCell(cellName = "上线时间", order = 21)
    private String onlineDate;

    /** 手动标记完成时间 */
    @MapperCell(cellName = "手动标记完成时间", order = 22)
    private String manualDate;

    /** 项目异常原因 */
    @MapperCell(cellName = "项目异常原因", order = 23)
    private String projectAnomaly;

    /** 分单id */
    private String id;

    /** 评论数 */
    private Integer commentCount;

    /** 是否为标记完成订单 */
    private String manualFinishFlag;

    /** 流程id */
    private String processId;

    /** 当前任务 */
    private String taskNames;

    /** 流程版本名称 */
    private String processVersionName;

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

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getServiceAndNums() {
        return serviceAndNums;
    }

    public void setServiceAndNums(String serviceAndNums) {
        this.serviceAndNums = serviceAndNums;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getPlanningExpertName() {
        return planningExpertName;
    }

    public void setPlanningExpertName(String planningExpertName) {
        this.planningExpertName = planningExpertName;
    }

    public String getOperationAdviserName() {
        return operationAdviserName;
    }

    public void setOperationAdviserName(String operationAdviserName) {
        this.operationAdviserName = operationAdviserName;
    }

    public String getSplitStatusName() {
        return splitStatusName;
    }

    public void setSplitStatusName(String splitStatusName) {
        this.splitStatusName = splitStatusName;
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

    public String getFriendOnlineDate() {
        return friendOnlineDate;
    }

    public void setFriendOnlineDate(String friendOnlineDate) {
        this.friendOnlineDate = friendOnlineDate;
    }

    public String getMomoOnlineDate() {
        return momoOnlineDate;
    }

    public void setMomoOnlineDate(String momoOnlineDate) {
        this.momoOnlineDate = momoOnlineDate;
    }

    public String getWeiboOnlineDate() {
        return weiboOnlineDate;
    }

    public void setWeiboOnlineDate(String weiboOnlineDate) {
        this.weiboOnlineDate = weiboOnlineDate;
    }

    public String getPresentationDate() {
        return presentationDate;
    }

    public void setPresentationDate(String presentationDate) {
        this.presentationDate = presentationDate;
    }

    public String getSplitNatureNames() {
        return splitNatureNames;
    }

    public void setSplitNatureNames(String splitNatureNames) {
        this.splitNatureNames = splitNatureNames;
    }

    public String getTimeoutFlag() {
        return timeoutFlag;
    }

    public void setTimeoutFlag(String timeoutFlag) {
        this.timeoutFlag = timeoutFlag;
    }

    public String getTaskTimeoutFlag() {
        return taskTimeoutFlag;
    }

    public void setTaskTimeoutFlag(String taskTimeoutFlag) {
        this.taskTimeoutFlag = taskTimeoutFlag;
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

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getManualFinishFlag() {
        return manualFinishFlag;
    }

    public void setManualFinishFlag(String manualFinishFlag) {
        this.manualFinishFlag = manualFinishFlag;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getTaskNames() {
        return taskNames;
    }

    public void setTaskNames(String taskNames) {
        this.taskNames = taskNames;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessVersionName() {
        return processVersionName;
    }

    public void setProcessVersionName(String processVersionName) {
        this.processVersionName = processVersionName;
    }

}
