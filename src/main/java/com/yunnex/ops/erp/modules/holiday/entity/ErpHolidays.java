package com.yunnex.ops.erp.modules.holiday.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 节假日配置Entity
 * @author pch
 * @version 2017-11-02
 */

public class ErpHolidays extends DataEntity<ErpHolidays> {
    
    private static final long serialVersionUID = 1L;
    private String remark;        // 备注
    private Long sort;        // 排序字段
    private Date holidayDate;        // 节假日
    
    public ErpHolidays() {
        super();
    }

    public ErpHolidays(String id){
        super(id);
    }
    
    

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Length(min=0, max=256, message="备注长度必须介于 0 和 256 之间")
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
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }
}