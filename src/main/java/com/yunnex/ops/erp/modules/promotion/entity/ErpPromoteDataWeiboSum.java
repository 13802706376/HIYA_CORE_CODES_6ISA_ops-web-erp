package com.yunnex.ops.erp.modules.promotion.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.common.persistence.Page;

/**
 * 微博推广数据合计Entity
 * 
 * @author yunnex
 * @version 2018-05-09
 */
public class ErpPromoteDataWeiboSum extends DataEntity<ErpPromoteDataWeiboSum> {


    private static final long serialVersionUID = 376661810806396350L;
    private String splitOrderId; // 分单ID
    private String promotionState;// 推广状态
    private String promotionStateTxt;// 推广状态文字
    private Date promotionBeginDate;// 推广开始时间
    private Date promotionEndDate;// 推广结束时间
    private Integer exposureNumSum; // 曝光量
    private Double expenditureSum; // 花费（元）
    private Double singleExposureCostSum; // 单次曝光成本
    private Integer flowNumSum;// 导流量
    private Double flowSumPercent; // 导流率
    private Integer interactionNumSum; // 互动量
    private List<ErpPromoteDataWeibo> weiboDataList;// 微博推广数据集合
    private Page<ErpPromoteDataWeibo> weiboDataPage;// 微博推广数据-分页数据


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


    public Integer getFlowNumSum() {
        return flowNumSum;
    }


    public void setFlowNumSum(Integer flowNumSum) {
        this.flowNumSum = flowNumSum;
    }


    public Double getFlowSumPercent() {
        return flowSumPercent;
    }


    public void setFlowSumPercent(Double flowSumPercent) {
        this.flowSumPercent = flowSumPercent;
    }


    public Integer getInteractionNumSum() {
        return interactionNumSum;
    }


    public void setInteractionNumSum(Integer interactionNumSum) {
        this.interactionNumSum = interactionNumSum;
    }


    public List<ErpPromoteDataWeibo> getWeiboDataList() {
        return weiboDataList;
    }


    public void setWeiboDataList(List<ErpPromoteDataWeibo> weiboDataList) {
        this.weiboDataList = weiboDataList;
    }


    public Page<ErpPromoteDataWeibo> getWeiboDataPage() {
        return weiboDataPage;
    }


    public void setWeiboDataPage(Page<ErpPromoteDataWeibo> weiboDataPage) {
        this.weiboDataPage = weiboDataPage;
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
