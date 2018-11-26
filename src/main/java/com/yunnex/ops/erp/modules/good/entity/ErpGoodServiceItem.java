package com.yunnex.ops.erp.modules.good.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 商品服务项目Entity
 * @author yunnex
 * @version 2018-05-29
 */
public class ErpGoodServiceItem extends DataEntity<ErpGoodServiceItem> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 商品服务项目名称
    private String readonly;
	
	public ErpGoodServiceItem() {
		super();
	}

	public ErpGoodServiceItem(String id){
		super(id);
	}

    @Length(min = 1, max = 64, message = "商品服务项目名称长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getReadonly() {
        return readonly;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }
	
}