package com.yunnex.ops.erp.modules.promotion.entity;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 卡券领取记录Entity
 * 
 * @author yunnex
 * @version 2018-05-22
 */
public class ErpOrderCouponReceiveRecord extends DataEntity<ErpOrderCouponReceiveRecord> {
	
    private static final long serialVersionUID = 1L;
    private String splitOrderId; // 分单ID，erp_order_split_info.id
    private String couponOutputId; // 卡券输出ID，erp_order_coupon_output.id
    private Date receiveDate; // 领取时间
    private Integer receiveNum; // 领取数量
    private String remark; // 备注
	
    @NotBlank
	public String getSplitOrderId() {
		return splitOrderId;
	}

	public void setSplitOrderId(String splitOrderId) {
		this.splitOrderId = splitOrderId;
	}
	
    @NotBlank
	public String getCouponOutputId() {
		return couponOutputId;
	}

	public void setCouponOutputId(String couponOutputId) {
		this.couponOutputId = couponOutputId;
	}
	
    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	
    @NotBlank
	public Integer getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(Integer receiveNum) {
		this.receiveNum = receiveNum;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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