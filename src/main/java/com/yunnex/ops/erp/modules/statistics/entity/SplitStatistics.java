package com.yunnex.ops.erp.modules.statistics.entity;

import java.util.Date;

/**
 * 分单明细数据
 * 
 * @author linqunzhi
 * @date 2018年5月7日
 */
public class SplitStatistics {

    /** 分单id */
    private String id;

    /** 分单序号 */
    private String splitId;

    /** 订单号 */
    private String orderNumber;

    /** 购买时间 */
    private Date buyDate;

    /** 订单类别 */
    private Integer orderType;

    /** 商户名称 */
    private String shopName;

    /** 创建时间 */
    private Date createDate;

    /** 备注 */
    private String remarks;

    /** 服务商名称 */
    private String agentName;

    /** 策划专家名称 */
    private String planningExpertName;

    /** 运营顾问名称 */
    private String operationAdviserName;

    /** 分单状态 */
    private String splitStatus;

    /** 交付周期 */
    private Integer deliveryCycle;

    /** 推广通道 */
    private String extensionChannelNames;

    /** 朋友圈上线日期 */
    private Date friendOnlineDate;

    /** 陌陌上线日期 */
    private Date momoOnlineDate;

    /** 微博上线日期 */
    private Date weiboOnlineDate;

    /** 完成效果报告日期 */
    private Date presentationDate;

    /** 工单性质 */
    private String splitNatures;

    /** 是否有超时风险 */
    private String timeoutFlag;

    /** 执行中的任务是否存在超时 */
    private String taskTimeoutFlag;

    /** 订单上线时间 */
    private Date onlineDate;

    /** 手动标记完成时间 */
    private Date manualDate;

    /** 项目异常原因 */
    private String pendingReason;

    /** 评论数 */
    private Integer commentCount;

    /** 是否为标记完成订单 */
    private String manualFinishFlag;

    /** 流程id */
    private String processId;

    /** 流程版本号 */
    private Integer processVersion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
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

    public String getSplitStatus() {
        return splitStatus;
    }

    public void setSplitStatus(String splitStatus) {
        this.splitStatus = splitStatus;
    }

    public Integer getDeliveryCycle() {
        return deliveryCycle;
    }

    public void setDeliveryCycle(Integer deliveryCycle) {
        this.deliveryCycle = deliveryCycle;
    }

    public String getExtensionChannelNames() {
        return extensionChannelNames;
    }

    public void setExtensionChannelNames(String extensionChannelNames) {
        this.extensionChannelNames = extensionChannelNames;
    }

    public Date getFriendOnlineDate() {
        return friendOnlineDate;
    }

    public void setFriendOnlineDate(Date friendOnlineDate) {
        this.friendOnlineDate = friendOnlineDate;
    }

    public Date getMomoOnlineDate() {
        return momoOnlineDate;
    }

    public void setMomoOnlineDate(Date momoOnlineDate) {
        this.momoOnlineDate = momoOnlineDate;
    }

    public Date getWeiboOnlineDate() {
        return weiboOnlineDate;
    }

    public void setWeiboOnlineDate(Date weiboOnlineDate) {
        this.weiboOnlineDate = weiboOnlineDate;
    }

    public Date getPresentationDate() {
        return presentationDate;
    }

    public void setPresentationDate(Date presentationDate) {
        this.presentationDate = presentationDate;
    }

    public String getSplitNatures() {
        return splitNatures;
    }

    public void setSplitNatures(String splitNatures) {
        this.splitNatures = splitNatures;
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

    public Date getOnlineDate() {
        return onlineDate;
    }

    public void setOnlineDate(Date onlineDate) {
        this.onlineDate = onlineDate;
    }

    public Date getManualDate() {
        return manualDate;
    }

    public void setManualDate(Date manualDate) {
        this.manualDate = manualDate;
    }

    public String getPendingReason() {
        return pendingReason;
    }

    public void setPendingReason(String pendingReason) {
        this.pendingReason = pendingReason;
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

    public Integer getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(Integer processVersion) {
        this.processVersion = processVersion;
    }
}
