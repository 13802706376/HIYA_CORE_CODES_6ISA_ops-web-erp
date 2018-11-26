package com.yunnex.ops.erp.modules.shop.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 商户店铺信息收集Entity
 * @author hanhan
 * @version 2018-05-26
 */
public class ErpShopMallForm extends DataEntity<ErpShopMallForm> {
	
	private static final long serialVersionUID = 1L;
	private String shopInfoId;		// 商户id
	private String formAttrName;		// 表单属性名
	private String formAttrValue;		// 表单属性值
	private String remark;		// 备注
	
	public ErpShopMallForm() {
		super();
	}

	public ErpShopMallForm(String id){
		super(id);
	}

	@Length(min=1, max=64, message="商户id长度必须介于 1 和 64 之间")
	public String getShopInfoId() {
		return shopInfoId;
	}

	public void setShopInfoId(String shopInfoId) {
		this.shopInfoId = shopInfoId;
	}
	
	@Length(min=1, max=100, message="表单属性名长度必须介于 1 和 100 之间")
	public String getFormAttrName() {
		return formAttrName;
	}

	public void setFormAttrName(String formAttrName) {
		this.formAttrName = formAttrName;
	}
	
	@Length(min=1, max=100, message="表单属性值长度必须介于 1 和 100 之间")
	public String getFormAttrValue() {
		return formAttrValue;
	}

	public void setFormAttrValue(String formAttrValue) {
		this.formAttrValue = formAttrValue;
	}
	
	@Length(min=0, max=256, message="备注长度必须介于 0 和 256 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}