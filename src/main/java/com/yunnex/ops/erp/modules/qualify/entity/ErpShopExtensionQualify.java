package com.yunnex.ops.erp.modules.qualify.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 商户推广资质Entity
 * @author huanghaidong
 * @version 2017-10-24
 */
public class ErpShopExtensionQualify extends DataEntity<ErpShopExtensionQualify> {
	
	private static final long serialVersionUID = 1L;
    private String shopId; // 商户id
	private String extensionValue;		// 推广资质的值
	private String remark;		// 备注
	private Long sort;		// 排序字段
	
	public ErpShopExtensionQualify() {
		super();
	}

	public ErpShopExtensionQualify(String id){
		super(id);
	}

	@NotNull(message="商户id不能为空")
    public String getShopId() {
		return shopId;
	}

    public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	
	@Length(min=1, max=1, message="推广资质的值长度必须介于 1 和 1 之间")
	public String getExtensionValue() {
		return extensionValue;
	}

	public void setExtensionValue(String extensionValue) {
		this.extensionValue = extensionValue;
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