package com.yunnex.ops.erp.modules.promotion.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.common.utils.excel.annotation.ExcelField;

/**
 * 推广数据-陌陌Entity
 * 
 * @author yunnex
 * @version 2018-05-10
 */
@ExcelField(title = "陌陌推广数据", isCellIndex = true)
public class ErpPromoteDataMomo extends DataEntity<ErpPromoteDataMomo> {

    private static final long serialVersionUID = 892958602587861904L;
    private String splitOrderId; // 分单ID，外键
    @ExcelField(title = "时间", cellIndex = 0)
    private Date dataTime; // 推广数据产生时间
    @ExcelField(title = "消耗", cellIndex = 1)
    private Double expenditure; // 消耗（元）
    @ExcelField(title = "展示量", cellIndex = 2)
    private Integer showNum; // 展示量(次)
    @ExcelField(title = "点击量", cellIndex = 3)
    private Integer clickNum; // 点击量(次)
    @ExcelField(title = "点击率", cellIndex = 4)
    private Double clickPercent; // 点击率(%)
    @ExcelField(title = "CPM", cellIndex = 5)
    private Double cpm; // CPM(元)
    @ExcelField(title = "平均点击单价", cellIndex = 6)
    private Double avgClickUnivalent; // 平均点击单价(元)
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

    public Double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    public Integer getShowNum() {
        return showNum;
    }

    public void setShowNum(Integer showNum) {
        this.showNum = showNum;
    }

    public Integer getClickNum() {
        return clickNum;
    }

    public void setClickNum(Integer clickNum) {
        this.clickNum = clickNum;
    }

    public Double getClickPercent() {
        return clickPercent;
    }

    public void setClickPercent(Double clickPercent) {
        this.clickPercent = clickPercent;
    }

    public Double getCpm() {
        return cpm;
    }

    public void setCpm(Double cpm) {
        this.cpm = cpm;
    }

    public Double getAvgClickUnivalent() {
        return avgClickUnivalent;
    }

    public void setAvgClickUnivalent(Double avgClickUnivalent) {
        this.avgClickUnivalent = avgClickUnivalent;
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
