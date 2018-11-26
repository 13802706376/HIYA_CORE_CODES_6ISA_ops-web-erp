package com.yunnex.ops.erp.modules.workflow.flow.from;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.yunnex.ops.erp.modules.team.entity.ErpTeam;

/**
 * 任务查询列表
 * 
 * @author yunnex
 * @date 2017年10月31日
 */
public class WorkFlowQueryForm {

    /** 订单编号 */
    private String orderNumber;
    /** 任务状态 */
    private String taskStateList;
    /** 商户名称 */
    private String shopName;
    /** 是否加急 */
    private String hurryFlag;
    /** 是否暂停 */
    private String suspendFlag;
    /** 是否有超时风险 */
    private String timeoutFlag;
    /** 商品编号 */
    private String goodsType;
    /** 处理人ID*/
    private String teamUserId;
    
    private String taskRef;

    private String taskState;

    private String actType;

    private String assignee;
    // 字符串转换后的结果
    private List<String> taskStates;
    private List<String> goodTypes;
    private List<String> userIds;
    private List<ErpTeam> teams;
    private List<Map<String, String>> taskKey;
    private String teamId;
    private String pendingProdFlag;
    
    private Integer isPage = 1; // 是否分页，1：是，0：否
    private Integer pageNo = 1;
    private Integer pageSize = 15;
    private Integer total;      // 总数
    private Integer pageTotal = 0;
    private Integer startIndex;
    private String orderBy = "orderDate"; // 排序字段，默认订单创建时间

    
 // 字符串转换成查询条件
    public List<String> getTaskStates() {
        return StringUtils.isNotBlank(getTaskStateList()) ? Arrays.asList(getTaskStateList().split(",")) : null;
    }

    public List<String> getGoodTypes() {
        return StringUtils.isNotBlank(getGoodsType()) ? Arrays.asList(getGoodsType().split(",")) : null;
    }
    
    public Integer getHurryFlagInt() {
        return StringUtils.isNotBlank(getHurryFlag()) ? Integer.parseInt(getHurryFlag()) : null;
    }
    
    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getTaskRef() {
        return taskRef;
    }

    public void setTaskRef(String taskRef) {
        this.taskRef = taskRef;
    }

    public List<Map<String, String>> getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(List<Map<String, String>> taskKey) {
        this.taskKey = taskKey;
    }

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTaskStateList() {
        return taskStateList;
    }

    public void setTaskStateList(String taskStateList) {
        this.taskStateList = taskStateList;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getHurryFlag() {
        return hurryFlag;
    }

    public void setHurryFlag(String hurryFlag) {
        this.hurryFlag = hurryFlag;
    }

    public String getTimeoutFlag() {
        return timeoutFlag;
    }

    public void setTimeoutFlag(String timeoutFlag) {
        this.timeoutFlag = timeoutFlag;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }
    
    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public void setTaskStates(List<String> taskStates) {
        this.taskStates = taskStates;
    }

    public void setGoodTypes(List<String> goodTypes) {
        this.goodTypes = goodTypes;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<ErpTeam> getTeams() {
        return teams;
    }

    public void setTeams(List<ErpTeam> teams) {
        this.teams = teams;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getPendingProdFlag() {
        return pendingProdFlag;
    }

    public void setPendingProdFlag(String pendingProdFlag) {
        this.pendingProdFlag = pendingProdFlag;
    }

    public Integer getIsPage() {
        return isPage;
    }

    public void setIsPage(Integer isPage) {
        this.isPage = isPage;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getStartIndex() {
        if (pageNo == null || pageNo < 1) {
            pageNo = 1;
        }
        this.startIndex = (pageNo - 1) * getPageSize();
        return this.startIndex;
    }
    
    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1 || pageSize > 20) {
            this.pageSize = 10;
        } else {
            this.pageSize = pageSize;
        }
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageTotal() {
        this.pageTotal = total % pageSize == 0 ? (total / pageSize) : (total / pageSize + 1);
        return this.pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    @Override
    public String toString() {
        return "WorkFlowQueryForm [orderNumber=" + orderNumber + ", taskStateList=" + taskStateList + ", shopName=" + shopName + ", hurryFlag=" + hurryFlag + ", goodsType=" + goodsType + ",teamUserId=" + teamUserId + ", assignee=" + assignee + ", taskStates=" + taskStates + ", goodTypes=" + goodTypes + ", userIds=" + userIds + ", teams=" + teams + ", teamId=" + teamId + ", pendingProdFlag=" + pendingProdFlag + ", isPage=" + isPage + ", pageNo=" + pageNo + ", pageSize=" + pageSize + ", total=" + total + ", pageTotal=" + pageTotal + ", startIndex=" + startIndex + "]";
    }

	public String getTeamUserId() {
		return teamUserId;
	}

	public void setTeamUserId(String teamUserId) {
		this.teamUserId = teamUserId;
	}

    public String getSuspendFlag() {
        return suspendFlag;
    }

    public void setSuspendFlag(String suspendFlag) {
        this.suspendFlag = suspendFlag;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
