package com.yunnex.ops.erp.modules.store.basic.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 经营范围Entity
 * @author a
 * @version 2017-12-19
 */
public class BusinessScope extends DataEntity<BusinessScope> {
	
	private static final long serialVersionUID = 1L;
	private String text;		// 业务名
	
	public BusinessScope() {
		super();
	}

	public BusinessScope(String id){
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


	@Length(min=0, max=45, message="业务名长度必须介于 0 和 45 之间")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}