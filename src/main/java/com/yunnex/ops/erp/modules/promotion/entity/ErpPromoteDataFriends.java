package com.yunnex.ops.erp.modules.promotion.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.common.utils.excel.annotation.ExcelField;

/**
 * 朋友圈推广数据Entity
 * 
 * @author yunnex
 * @version 2018-05-09
 */
@ExcelField(title = " 朋友圈推广数据", isCellIndex = true)
public class ErpPromoteDataFriends extends DataEntity<ErpPromoteDataFriends> {

    private static final long serialVersionUID = -1168753481909026389L;
    private String splitOrderId; // 分单ID，外键
    @ExcelField(title = "时间", cellIndex = 2)
    private Date dataTime; // 推广数据产生时间
    @ExcelField(title = "花费（元）", cellIndex = 6)
    private Double expenditure; // 花费（元）
    @ExcelField(title = "详情查看数量", cellIndex = 7)
    private Integer detailsQueryNum; // 详情查看数量
    @ExcelField(title = "详情查看成本", cellIndex = 8)
    private Double detailsQueryCost; // 详情查看成本
    @ExcelField(title = "详情查看率", cellIndex = 9)
    private Double detailsQueryPercent; // 详情查看率
    @ExcelField(title = "曝光量", cellIndex = 10)
    private Integer exposureNum; // 曝光量
    @ExcelField(title = "原生推广页查看量", cellIndex = 11)
    private Integer promotePageQueryNum; // 原生推广页查看量
    @ExcelField(title = "原生推广页查看成本", cellIndex = 12)
    private Double promotePageQueryCost; // 原生推广页查看成本
    @ExcelField(title = "原生推广页查看率", cellIndex = 13)
    private Double promotePageQueryPercent; // 原生推广页查看率
    @ExcelField(title = "原生推广页转发量", cellIndex = 17)
    private Integer promotePagePropagateNum; // 原生推广页转发量
    @ExcelField(title = "门店查看量", cellIndex = 14)
    private Integer storeQueryNum; // 门店查看量
    @ExcelField(title = "点赞评论数量", cellIndex = 15)
    private Integer upvoteCommentNum; // 点赞评论数量
    @ExcelField(title = "关注量", cellIndex = 16)
    private Integer attentionNum; // 关注量
    @ExcelField(title = "销售线索量", cellIndex = 18)
    private Integer sellClewNum; // 销售线索量
    private String remark; // 备注

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

    public Double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    public Integer getDetailsQueryNum() {
        return detailsQueryNum;
    }

    public void setDetailsQueryNum(Integer detailsQueryNum) {
        this.detailsQueryNum = detailsQueryNum;
    }

    public Double getDetailsQueryCost() {
        return detailsQueryCost;
    }

    public void setDetailsQueryCost(Double detailsQueryCost) {
        this.detailsQueryCost = detailsQueryCost;
    }

    public Double getDetailsQueryPercent() {
        return detailsQueryPercent;
    }

    public void setDetailsQueryPercent(Double detailsQueryPercent) {
        this.detailsQueryPercent = detailsQueryPercent;
    }

    public Integer getExposureNum() {
        return exposureNum;
    }

    public void setExposureNum(Integer exposureNum) {
        this.exposureNum = exposureNum;
    }

    public Integer getPromotePageQueryNum() {
        return promotePageQueryNum;
    }

    public void setPromotePageQueryNum(Integer promotePageQueryNum) {
        this.promotePageQueryNum = promotePageQueryNum;
    }

    public Double getPromotePageQueryCost() {
        return promotePageQueryCost;
    }

    public void setPromotePageQueryCost(Double promotePageQueryCost) {
        this.promotePageQueryCost = promotePageQueryCost;
    }

    public Double getPromotePageQueryPercent() {
        return promotePageQueryPercent;
    }

    public void setPromotePageQueryPercent(Double promotePageQueryPercent) {
        this.promotePageQueryPercent = promotePageQueryPercent;
    }

    public Integer getPromotePagePropagateNum() {
        return promotePagePropagateNum;
    }

    public void setPromotePagePropagateNum(Integer promotePagePropagateNum) {
        this.promotePagePropagateNum = promotePagePropagateNum;
    }

    public Integer getStoreQueryNum() {
        return storeQueryNum;
    }

    public void setStoreQueryNum(Integer storeQueryNum) {
        this.storeQueryNum = storeQueryNum;
    }

    public Integer getUpvoteCommentNum() {
        return upvoteCommentNum;
    }

    public void setUpvoteCommentNum(Integer upvoteCommentNum) {
        this.upvoteCommentNum = upvoteCommentNum;
    }

    public Integer getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(Integer attentionNum) {
        this.attentionNum = attentionNum;
    }

    public Integer getSellClewNum() {
        return sellClewNum;
    }

    public void setSellClewNum(Integer sellClewNum) {
        this.sellClewNum = sellClewNum;
    }

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
