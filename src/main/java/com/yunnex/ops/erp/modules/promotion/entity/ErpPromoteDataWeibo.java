package com.yunnex.ops.erp.modules.promotion.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.common.utils.excel.annotation.ExcelField;

/**
 * 推广数据-微博Entity
 * 
 * @author yunnex
 * @version 2018-05-10
 */
@ExcelField(title = " 微博推广数据", isCellIndex = true)
public class ErpPromoteDataWeibo extends DataEntity<ErpPromoteDataWeibo> {

    private static final long serialVersionUID = -6378915996329011870L;
    private String splitOrderId; // 分单ID，外键
    @ExcelField(title = "时间", cellIndex = 0)
    private Date dataTime; // 推广数据产生时间
    @ExcelField(title = "广告计划", cellIndex = 1)
    private String advertisingPlan; // 广告计划
    @ExcelField(title = "曝光量", cellIndex = 2)
    private Integer exposureNum; // 曝光量
    @ExcelField(title = "千次曝光成本", cellIndex = 3)
    private Double thousandsExposureCost; // 千次曝光成本
    @ExcelField(title = "花费", cellIndex = 4)
    private Double expenditure; // 花费（元）
    @ExcelField(title = "转发量", cellIndex = 5)
    private Integer propagateNum; // 转发量
    @ExcelField(title = "点赞量", cellIndex = 6)
    private Integer upvoteNum; // 点赞量
    @ExcelField(title = "评论量", cellIndex = 7)
    private Integer commentNum; // 评论量
    @ExcelField(title = " 导流数", cellIndex = 8)
    private Integer flowNum; // 导流数
    @ExcelField(title = "导流率", cellIndex = 9)
    private Double flowPercent; // 导流率
    @ExcelField(title = "单次导流成本", cellIndex = 10)
    private Double singleFlowCost; // 单次导流成本
    @ExcelField(title = "加关注数", cellIndex = 11)
    private Integer addAttentionNum; // 加关注数
    @ExcelField(title = "加关注率", cellIndex = 12)
    private Double addAttentionPercent; // 加关注率
    @ExcelField(title = "加关注成本", cellIndex = 13)
    private Double addAttentionCost; // 加关注成本
    @ExcelField(title = "小card图文点击数", cellIndex = 14)
    private Integer cardClickNum; // 小card图文点击数
    @ExcelField(title = "互动数", cellIndex = 15)
    private Integer interactionNum; // 互动数
    @ExcelField(title = "互动率", cellIndex = 16)
    private Double interactionPercent; // 互动率
    @ExcelField(title = "单次互动成本", cellIndex = 17)
    private Double singleInteractionCost; // 单次互动成本
    private String remark; // 备注

    @Length(min = 1, max = 64, message = "分单ID，外键长度必须介于 1 和 64 之间")
    public String getSplitOrderId() {
        return splitOrderId;
    }

    public void setSplitOrderId(String splitOrderId) {
        this.splitOrderId = splitOrderId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getDataTime() {
        return dataTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    @Length(min = 0, max = 50, message = "广告计划长度必须介于 0 和 50 之间")
    public String getAdvertisingPlan() {
        return advertisingPlan;
    }

    public void setAdvertisingPlan(String advertisingPlan) {
        this.advertisingPlan = advertisingPlan;
    }

    public Integer getExposureNum() {
        return exposureNum;
    }

    public void setExposureNum(Integer exposureNum) {
        this.exposureNum = exposureNum;
    }

    public Double getThousandsExposureCost() {
        return thousandsExposureCost;
    }

    public void setThousandsExposureCost(Double thousandsExposureCost) {
        this.thousandsExposureCost = thousandsExposureCost;
    }

    public Double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    public Integer getPropagateNum() {
        return propagateNum;
    }

    public void setPropagateNum(Integer propagateNum) {
        this.propagateNum = propagateNum;
    }

    public Integer getUpvoteNum() {
        return upvoteNum;
    }

    public void setUpvoteNum(Integer upvoteNum) {
        this.upvoteNum = upvoteNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getFlowNum() {
        return flowNum;
    }

    public void setFlowNum(Integer flowNum) {
        this.flowNum = flowNum;
    }

    public Double getFlowPercent() {
        return flowPercent;
    }

    public void setFlowPercent(Double flowPercent) {
        this.flowPercent = flowPercent;
    }

    public Double getSingleFlowCost() {
        return singleFlowCost;
    }

    public void setSingleFlowCost(Double singleFlowCost) {
        this.singleFlowCost = singleFlowCost;
    }

    public Integer getAddAttentionNum() {
        return addAttentionNum;
    }

    public void setAddAttentionNum(Integer addAttentionNum) {
        this.addAttentionNum = addAttentionNum;
    }

    public Double getAddAttentionPercent() {
        return addAttentionPercent;
    }

    public void setAddAttentionPercent(Double addAttentionPercent) {
        this.addAttentionPercent = addAttentionPercent;
    }

    public Double getAddAttentionCost() {
        return addAttentionCost;
    }

    public void setAddAttentionCost(Double addAttentionCost) {
        this.addAttentionCost = addAttentionCost;
    }

    public Integer getCardClickNum() {
        return cardClickNum;
    }

    public void setCardClickNum(Integer cardClickNum) {
        this.cardClickNum = cardClickNum;
    }

    public Integer getInteractionNum() {
        return interactionNum;
    }

    public void setInteractionNum(Integer interactionNum) {
        this.interactionNum = interactionNum;
    }

    public Double getInteractionPercent() {
        return interactionPercent;
    }

    public void setInteractionPercent(Double interactionPercent) {
        this.interactionPercent = interactionPercent;
    }

    public Double getSingleInteractionCost() {
        return singleInteractionCost;
    }

    public void setSingleInteractionCost(Double singleInteractionCost) {
        this.singleInteractionCost = singleInteractionCost;
    }

    @Length(min = 0, max = 256, message = "备注长度必须介于 0 和 256 之间")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
