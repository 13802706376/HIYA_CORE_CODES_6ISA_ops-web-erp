package com.yunnex.ops.erp.modules.qualify.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 商户支付资质Entity
 * @author huanghaidong
 * @version 2017-10-24
 */
public class ErpShopPayQualify extends DataEntity<ErpShopPayQualify> {
	
	private static final long serialVersionUID = 1L;
    private String shopId; // 商户id
	private String payValue;		// 支付渠道value
	private String remark;		// 备注
	private Long sort;		// 排序字段
	
	public ErpShopPayQualify() {
		super();
	}


    public ErpShopPayQualify(String shopId, String payValue) {
        this.shopId = shopId;
        this.payValue = payValue;
    }

	public ErpShopPayQualify(String id){
		super(id);
	}

    public String getShopId() {
		return shopId;
	}

    public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	
	@Length(min=0, max=1, message="支付渠道value长度必须介于 0 和 1 之间")
	public String getPayValue() {
		return payValue;
	}

	public void setPayValue(String payValue) {
		this.payValue = payValue;
	}
	
	@Length(min=1, max=256, message="备注长度必须介于 1 和 256 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@NotNull(message="排序字段不能为空")
	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}
	
}