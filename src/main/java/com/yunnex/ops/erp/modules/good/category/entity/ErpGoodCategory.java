package com.yunnex.ops.erp.modules.good.category.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 商品分类管理Entity
 * @author Frank
 * @version 2017-10-21
 */
public class ErpGoodCategory extends DataEntity<ErpGoodCategory> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 商品类型名称
	private String code;		// 商品类型编码
	private String readonly;		// 可编辑标记
	private String remark;		// 备注
	private Long sort;		// 排序字段

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public ErpGoodCategory() {
		super();
	}

	public ErpGoodCategory(String id){
		super(id);
	}

	@Length(min=0, max=64, message="商品类型名称长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=0, max=1, message="可编辑标记长度必须介于 0 和 1 之间")
	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}
	
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
	
}