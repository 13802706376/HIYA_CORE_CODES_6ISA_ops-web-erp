package com.yunnex.ops.erp.modules.statistics.dto;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.RequestDto;

/**
 * 分单报表 requestDto
 * 
 * @author linqunzhi
 * @date 2018年5月7日
 */
public class SplitStatisticsRequestDto extends RequestDto<SplitStatisticsRequestDto> {

    private static final long serialVersionUID = -3147873086084697868L;

    /** 是否需要对查询出来的列进行过滤 Y：是 N：否 */
    private String queryColumnsFlag;

    /** 交付周期 {\"min\":-2,\"max\":-2} */
    private List<SplitDeliveryCycleDto> deliveryCycleList;

    /** 结束时间 */
    private String endDateStr;

    /** 推广通道 */
    private List<String> extensionChannelList;

    /** 商品 */
    private List<String> goodIdList;

    /** 是否为标记完成订单 */
    private String manualFinishFlag;

    /** 订单号 */
    private String orderNum;

    /** 订单类别 */
    private List<String> orderTypeList;

    /** 策划专家 */
    private List<String> planningExpertIdList;

    /** 分单流程版本号 */
    private List<String> processVersionList;

    /** 项目异常原因 */
    private List<String> projectAnomalyList;

    /** 商户名称 */
    private String shopName;

    /** 工单性质 */
    private List<String> splitNatureList;

    /** 当前订单状态 */
    private List<String> splitStatusList;

    /** 分单类型 */
    private String splitType;

    /** 开始时间 */
    private String startDateStr;

    /** 执行中的任务是否存在超时 */
    private String taskTimeoutFlag;

    /** 团队id */
    private String teamId;

    /** 成员id集合 */
    private List<String> userIdList;

    /** 是否有超时风险 */
    private String timeoutFlag;



    public SplitStatisticsRequestDto() {

    }

    public SplitStatisticsRequestDto(String startDateStr, String endDateStr, List<String> userIdList) {
        this.startDateStr = startDateStr;
        this.endDateStr = endDateStr;
        this.userIdList = userIdList;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public List<String> getExtensionChannelList() {
        return extensionChannelList;
    }

    public void setExtensionChannelList(List<String> extensionChannelList) {
        this.extensionChannelList = extensionChannelList;
    }

    public List<String> getGoodIdList() {
        return goodIdList;
    }

    public void setGoodIdList(List<String> goodIdList) {
        this.goodIdList = goodIdList;
    }

    public String getManualFinishFlag() {
        return manualFinishFlag;
    }

    public void setManualFinishFlag(String manualFinishFlag) {
        this.manualFinishFlag = manualFinishFlag;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public List<String> getOrderTypeList() {
        return orderTypeList;
    }

    public void setOrderTypeList(List<String> orderTypeList) {
        this.orderTypeList = orderTypeList;
    }

    public List<String> getPlanningExpertIdList() {
        return planningExpertIdList;
    }

    public void setPlanningExpertIdList(List<String> planningExpertIdList) {
        this.planningExpertIdList = planningExpertIdList;
    }

    public List<String> getProcessVersionList() {
        return processVersionList;
    }

    public void setProcessVersionList(List<String> processVersionList) {
        this.processVersionList = processVersionList;
    }

    public List<String> getProjectAnomalyList() {
        return projectAnomalyList;
    }

    public void setProjectAnomalyList(List<String> projectAnomalyList) {
        this.projectAnomalyList = projectAnomalyList;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<String> getSplitNatureList() {
        return splitNatureList;
    }

    public void setSplitNatureList(List<String> splitNatureList) {
        this.splitNatureList = splitNatureList;
    }

    public List<String> getSplitStatusList() {
        return splitStatusList;
    }

    public void setSplitStatusList(List<String> splitStatusList) {
        this.splitStatusList = splitStatusList;
    }

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getTaskTimeoutFlag() {
        return taskTimeoutFlag;
    }

    public void setTaskTimeoutFlag(String taskTimeoutFlag) {
        this.taskTimeoutFlag = taskTimeoutFlag;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTimeoutFlag() {
        return timeoutFlag;
    }

    public void setTimeoutFlag(String timeoutFlag) {
        this.timeoutFlag = timeoutFlag;
    }

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public String getQueryColumnsFlag() {
        return queryColumnsFlag;
    }

    public void setQueryColumnsFlag(String queryColumnsFlag) {
        this.queryColumnsFlag = queryColumnsFlag;
    }

    public List<SplitDeliveryCycleDto> getDeliveryCycleList() {
        return deliveryCycleList;
    }

    public void setDeliveryCycleList(List<SplitDeliveryCycleDto> deliveryCycleList) {
        this.deliveryCycleList = deliveryCycleList;
    }
}
