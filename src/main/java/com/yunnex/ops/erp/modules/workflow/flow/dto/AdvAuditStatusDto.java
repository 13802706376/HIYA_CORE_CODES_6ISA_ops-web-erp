package com.yunnex.ops.erp.modules.workflow.flow.dto;

import java.io.Serializable;

/**
 * 广告主审核状态DTO
 */
public class AdvAuditStatusDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String storeName;
    private Integer friendsAuditStatus;
    private Integer momoAuditStatus;
    private Integer weiboAuditStatus;

    private String friendsAuditName;
    private String momoAuditName;
    private String weiboAuditName;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getFriendsAuditStatus() {
        return friendsAuditStatus;
    }

    public void setFriendsAuditStatus(Integer friendsAuditStatus) {
        this.friendsAuditStatus = friendsAuditStatus;
    }

    public Integer getMomoAuditStatus() {
        return momoAuditStatus;
    }

    public void setMomoAuditStatus(Integer momoAuditStatus) {
        this.momoAuditStatus = momoAuditStatus;
    }

    public Integer getWeiboAuditStatus() {
        return weiboAuditStatus;
    }

    public void setWeiboAuditStatus(Integer weiboAuditStatus) {
        this.weiboAuditStatus = weiboAuditStatus;
    }

    public String getFriendsAuditName() {
        return friendsAuditName;
    }

    public void setFriendsAuditName(String friendsAuditName) {
        this.friendsAuditName = friendsAuditName;
    }

    public String getMomoAuditName() {
        return momoAuditName;
    }

    public void setMomoAuditName(String momoAuditName) {
        this.momoAuditName = momoAuditName;
    }

    public String getWeiboAuditName() {
        return weiboAuditName;
    }

    public void setWeiboAuditName(String weiboAuditName) {
        this.weiboAuditName = weiboAuditName;
    }
}
