package com.yunnex.ops.erp.modules.workflow.user.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.modules.sys.entity.User;

/**
 * 工作流人员关系Entity
 * @author Frank
 * @version 2017-10-27
 */
public class ErpOrderFlowUser extends DataEntity<ErpOrderFlowUser> {

    private static final long serialVersionUID = 1L;
    private String orderId; // 原始订单信息
    private String splitId; // 分单序号
    private String flowId; // 流程编号
    private User user; // 人员编号
    private String flowUserId; // 用户标识
    private String remark; // 备注


    // 统计字段
    private String orderNum;
    private String planningExpert;
    private String operationAdviser;
    private String payDate;
    private String orderType;
    private String shopName;
    private String agentName;
    private String splitIds;
    private String goodName;
    private String num;
    private String createDates;
    private String promotionChannel;
    private String momoDate;
    private String friendsDate;
    private String weiboDate;
    private String createPresentation;
    private String hurryFlag;
    private String orderSource;
    private String osStatuss;
    private String pid;
    private double onlineUseTime;
    /** 分单ID */
    private String sid;
    private String shopid;
    private Integer commentCount; // 分单评论数

    // 是否待生产库
    private String pendingProduced;
    // 待生产库原因(Q:资质问题D:延迟上线)
    private String pendingReason;
    //超时风险
    private String timeoutFlag;
    //超时状态
    private String taskStatus;
    //上线时间
    private Date onlineDate;
    //手动标记完成时间
    private Date manualDate;
    

    
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


	public String getTaskStatus() {
		return taskStatus;
	}


	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}


	public String getTimeoutFlag() {
		return timeoutFlag;
	}


	public void setTimeoutFlag(String timeoutFlag) {
		this.timeoutFlag = timeoutFlag;
	}


	public double getOnlineUseTime() {
		return onlineUseTime;
	}


	public void setOnlineUseTime(double onlineUseTime) {
		this.onlineUseTime = onlineUseTime;
	}


	public String getPendingReason() {
        return pendingReason;
    }


    public void setPendingReason(String pendingReason) {
        this.pendingReason = pendingReason;
    }


    public ErpOrderFlowUser() {
        super();
    }


    public ErpOrderFlowUser(String id) {
        super(id);
    }

    public String getShopid() {
        return shopid;
    }


    public void setShopid(String shopid) {
        this.shopid = shopid;
    }


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


    public String getOsStatuss() {
        return osStatuss;
    }


    public void setOsStatuss(String osStatuss) {
        this.osStatuss = osStatuss;
    }


    public String getHurryFlag() {
        return hurryFlag;
    }


    public void setHurryFlag(String hurryFlag) {
        this.hurryFlag = hurryFlag;
    }


    public String getOrderSource() {
        return orderSource;
    }


    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }


    public String getOrderNum() {
        return orderNum;
    }


    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getCreateDates() {
        return createDates;
    }


    public void setCreateDates(String createDates) {
        this.createDates = createDates;
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


    public String getPayDate() {
        return payDate;
    }


    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }


    public String getOrderType() {
        return orderType;
    }


    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }


    public String getShopName() {
        return shopName;
    }


    public void setShopName(String shopName) {
        this.shopName = shopName;
    }


    public String getAgentName() {
        return agentName;
    }


    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }


    public String getSplitIds() {
        return splitIds;
    }


    public void setSplitIds(String splitIds) {
        this.splitIds = splitIds;
    }


    public String getGoodName() {
        return goodName;
    }


    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }


    public String getNum() {
        return num;
    }


    public void setNum(String num) {
        this.num = num;
    }





    public String getPromotionChannel() {
        return promotionChannel;
    }


    public void setPromotionChannel(String promotionChannel) {
        this.promotionChannel = promotionChannel;
    }


    public String getMomoDate() {
        return momoDate;
    }


    public void setMomoDate(String momoDate) {
        this.momoDate = momoDate;
    }


    public String getFriendsDate() {
        return friendsDate;
    }


    public void setFriendsDate(String friendsDate) {
        this.friendsDate = friendsDate;
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


    public String getFlowUserId() {
        return flowUserId;
    }


    public void setFlowUserId(String flowUserId) {
        this.flowUserId = flowUserId;
    }


    @Length(min = 1, max = 64, message = "原始订单信息长度必须介于 1 和 64 之间")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Length(min = 1, max = 64, message = "分单序号长度必须介于 1 和 64 之间")
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    @Length(min = 1, max = 64, message = "流程编号长度必须介于 1 和 64 之间")
    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    @NotNull(message = "人员编号不能为空")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Length(min = 0, max = 256, message = "备注长度必须介于 0 和 256 之间")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPendingProduced() {
        return pendingProduced;
    }

    public void setPendingProduced(String pendingProduced) {
        this.pendingProduced = pendingProduced;
    }
    


	@Override
    public int hashCode() {
        return super.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}