package com.yunnex.ops.erp.modules.workflow.effect.entity;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 聚引客投放效果对象
 * 
 * @author SunQ
 * @date 2018年1月25日
 */
public class JykDeliveryEffectInfo extends DataEntity<JykDeliveryEffectInfo> {

    private static final long serialVersionUID = -4826897970038601541L;

    /**
     * 原始订单信息
     */
    private String orderId;
    
    /**
     * 分单序号
     */
    private String splitId;
    
    /**
     * 流程序号
     */
    private String procInsId;
    
    /**
     * 状态(0:默认,表示不使用 1:当前正在使用 2:已发布到小程序(确认预览投放) 3:确认投放)
     */
    private String state;
    
    /**
     * 外层入口图URL(朋友圈)
     */
    private String outerImgUrlFriends;
    
    /**
     * 外层入口图名称(朋友圈)
     */
    private String outerImgNameFriends;
    
    /**
     * 内层落地页图URL(朋友圈)
     */
    private String innerImgUrlFriends;
    
    /**
     * 内层落地页图名称(朋友圈)
     */
    private String innerImgNameFriends;
    
    /**
     * 外层入口图URL(微博)
     */
    private String outerImgUrlWeibo;
    
    /**
     * 外层入口图名称(微博)
     */
    private String outerImgNameWeibo;
    
    /**
     * 内层落地页图URL(微博)
     */
    private String innerImgUrlWeibo;
    
    /**
     * 内层落地页图名称(微博)
     */
    private String innerImgNameWeibo;
    
    /**
     * 外层入口图URL(陌陌)
     */
    private String outerImgUrlMomo;
    
    /**
     * 外层入口图名称(陌陌)
     */
    private String outerImgNameMomo;
    
    /**
     * 内层落地页图URL(陌陌)
     */
    private String innerImgUrlMomo;
    
    /**
     * 内层落地页图名称(陌陌)
     */
    private String innerImgNameMomo;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 排序字段
     */
    private Long sort;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOuterImgUrlFriends() {
        return outerImgUrlFriends;
    }

    public void setOuterImgUrlFriends(String outerImgUrlFriends) {
        this.outerImgUrlFriends = outerImgUrlFriends;
    }

    public String getOuterImgNameFriends() {
        return outerImgNameFriends;
    }

    public void setOuterImgNameFriends(String outerImgNameFriends) {
        this.outerImgNameFriends = outerImgNameFriends;
    }

    public String getInnerImgUrlFriends() {
        return innerImgUrlFriends;
    }

    public void setInnerImgUrlFriends(String innerImgUrlFriends) {
        this.innerImgUrlFriends = innerImgUrlFriends;
    }

    public String getInnerImgNameFriends() {
        return innerImgNameFriends;
    }

    public void setInnerImgNameFriends(String innerImgNameFriends) {
        this.innerImgNameFriends = innerImgNameFriends;
    }

    public String getOuterImgUrlWeibo() {
        return outerImgUrlWeibo;
    }

    public void setOuterImgUrlWeibo(String outerImgUrlWeibo) {
        this.outerImgUrlWeibo = outerImgUrlWeibo;
    }

    public String getOuterImgNameWeibo() {
        return outerImgNameWeibo;
    }

    public void setOuterImgNameWeibo(String outerImgNameWeibo) {
        this.outerImgNameWeibo = outerImgNameWeibo;
    }

    public String getInnerImgUrlWeibo() {
        return innerImgUrlWeibo;
    }

    public void setInnerImgUrlWeibo(String innerImgUrlWeibo) {
        this.innerImgUrlWeibo = innerImgUrlWeibo;
    }

    public String getInnerImgNameWeibo() {
        return innerImgNameWeibo;
    }

    public void setInnerImgNameWeibo(String innerImgNameWeibo) {
        this.innerImgNameWeibo = innerImgNameWeibo;
    }

    public String getOuterImgUrlMomo() {
        return outerImgUrlMomo;
    }

    public void setOuterImgUrlMomo(String outerImgUrlMomo) {
        this.outerImgUrlMomo = outerImgUrlMomo;
    }

    public String getOuterImgNameMomo() {
        return outerImgNameMomo;
    }

    public void setOuterImgNameMomo(String outerImgNameMomo) {
        this.outerImgNameMomo = outerImgNameMomo;
    }

    public String getInnerImgUrlMomo() {
        return innerImgUrlMomo;
    }

    public void setInnerImgUrlMomo(String innerImgUrlMomo) {
        this.innerImgUrlMomo = innerImgUrlMomo;
    }

    public String getInnerImgNameMomo() {
        return innerImgNameMomo;
    }

    public void setInnerImgNameMomo(String innerImgNameMomo) {
        this.innerImgNameMomo = innerImgNameMomo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }
}