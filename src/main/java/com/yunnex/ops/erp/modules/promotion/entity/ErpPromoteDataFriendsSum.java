package com.yunnex.ops.erp.modules.promotion.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.common.persistence.Page;

/**
 * 朋友圈推广数据合计Entity
 * 
 * @author yunnex
 * @version 2018-05-09
 */
public class ErpPromoteDataFriendsSum extends DataEntity<ErpPromoteDataFriendsSum> {


    private static final long serialVersionUID = 1258746725849683433L;

    private String splitOrderId; // 分单ID
    private String promotionState;// 推广状态
    private String promotionStateTxt;// 推广状态文字
    private Date promotionBeginDate;// 推广开始时间
    private Date promotionEndDate;// 推广结束时间
    private Integer exposureNumSum; // 曝光量
    private Double expenditureSum; // 花费（元）
    private Double singleExposureCostSum; // 单次曝光成本
    private Integer detailsQueryNumSum; // 详情查看数量
    private Double detailsQuerySumPercent; // 详情查看率
    private Integer interactionNumSum;// 互动量
    private List<ErpPromoteDataFriends> friendsDataList;// 朋友圈推广数据集合
    private Page<ErpPromoteDataFriends> friendsDataPage;// 朋友圈推广数据-分页数据



    public String getSplitOrderId() {
        return splitOrderId;
    }

    public void setSplitOrderId(String splitOrderId) {
        this.splitOrderId = splitOrderId;
    }

    public String getPromotionState() {
        return promotionState;
    }

    public void setPromotionState(String promotionState) {
        this.promotionState = promotionState;
    }

    public String getPromotionStateTxt() {
        return promotionStateTxt;
    }

    public void setPromotionStateTxt(String promotionStateTxt) {
        this.promotionStateTxt = promotionStateTxt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getPromotionBeginDate() {
        return promotionBeginDate;
    }

    public void setPromotionBeginDate(Date promotionBeginDate) {
        this.promotionBeginDate = promotionBeginDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getPromotionEndDate() {
        return promotionEndDate;
    }

    public void setPromotionEndDate(Date promotionEndDate) {
        this.promotionEndDate = promotionEndDate;
    }

    public Integer getExposureNumSum() {
        return exposureNumSum;
    }

    public void setExposureNumSum(Integer exposureNumSum) {
        this.exposureNumSum = exposureNumSum;
    }

    public Double getExpenditureSum() {
        return expenditureSum;
    }

    public void setExpenditureSum(Double expenditureSum) {
        this.expenditureSum = expenditureSum;
    }



    public Double getSingleExposureCostSum() {
        return singleExposureCostSum;
    }

    public void setSingleExposureCostSum(Double singleExposureCostSum) {
        this.singleExposureCostSum = singleExposureCostSum;
    }

    public Integer getDetailsQueryNumSum() {
        return detailsQueryNumSum;
    }

    public void setDetailsQueryNumSum(Integer detailsQueryNumSum) {
        this.detailsQueryNumSum = detailsQueryNumSum;
    }

    public Double getDetailsQuerySumPercent() {
        return detailsQuerySumPercent;
    }

    public void setDetailsQuerySumPercent(Double detailsQuerySumPercent) {
        this.detailsQuerySumPercent = detailsQuerySumPercent;
    }

    public Integer getInteractionNumSum() {
        return interactionNumSum;
    }

    public void setInteractionNumSum(Integer interactionNumSum) {
        this.interactionNumSum = interactionNumSum;
    }

    public List<ErpPromoteDataFriends> getFriendsDataList() {
        return friendsDataList;
    }

    public void setFriendsDataList(List<ErpPromoteDataFriends> friendsDataList) {
        this.friendsDataList = friendsDataList;
    }

    public Page<ErpPromoteDataFriends> getFriendsDataPage() {
        return friendsDataPage;
    }

    public void setFriendsDataPage(Page<ErpPromoteDataFriends> friendsDataPage) {
        this.friendsDataPage = friendsDataPage;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
