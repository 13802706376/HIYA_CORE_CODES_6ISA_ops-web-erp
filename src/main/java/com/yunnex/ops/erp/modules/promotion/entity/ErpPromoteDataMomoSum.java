package com.yunnex.ops.erp.modules.promotion.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.common.persistence.Page;

/**
 * 推广数据合计-陌陌Entity
 * 
 * @author yunnex
 * @version 2018-05-10
 */
public class ErpPromoteDataMomoSum extends DataEntity<ErpPromoteDataMomoSum> {

    private static final long serialVersionUID = 892958602587861904L;
    private String splitOrderId; // 分单ID，外键
    private String promotionState;// 推广状态
    private String promotionStateTxt;// 推广状态文字
    private Date promotionBeginDate;// 推广开始时间
    private Date promotionEndDate;// 推广结束时间
    private Integer showNumSum; // 展示量(次)
    private Double expenditureSum; // 花费（元）
    private Double singleShowCostSum; // 单次曝光成本
    private Integer clickNumSum; // 点击量(次)
    private Double clickSumPercent; // 点击率(%)
    private List<ErpPromoteDataMomo> momoDataList;// 陌陌推广数据集合
    private Page<ErpPromoteDataMomo> momoDataPage;// 陌陌推广数据-分页数据

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

    public Integer getShowNumSum() {
        return showNumSum;
    }

    public void setShowNumSum(Integer showNumSum) {
        this.showNumSum = showNumSum;
    }

    public Double getExpenditureSum() {
        return expenditureSum;
    }

    public void setExpenditureSum(Double expenditureSum) {
        this.expenditureSum = expenditureSum;
    }

    public Double getSingleShowCostSum() {
        return singleShowCostSum;
    }

    public void setSingleShowCostSum(Double singleShowCostSum) {
        this.singleShowCostSum = singleShowCostSum;
    }

    public Integer getClickNumSum() {
        return clickNumSum;
    }

    public void setClickNumSum(Integer clickNumSum) {
        this.clickNumSum = clickNumSum;
    }

    public Double getClickSumPercent() {
        return clickSumPercent;
    }

    public void setClickSumPercent(Double clickSumPercent) {
        this.clickSumPercent = clickSumPercent;
    }

    public List<ErpPromoteDataMomo> getMomoDataList() {
        return momoDataList;
    }

    public void setMomoDataList(List<ErpPromoteDataMomo> momoDataList) {
        this.momoDataList = momoDataList;
    }

    public Page<ErpPromoteDataMomo> getMomoDataPage() {
        return momoDataPage;
    }

    public void setMomoDataPage(Page<ErpPromoteDataMomo> momoDataPage) {
        this.momoDataPage = momoDataPage;
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
