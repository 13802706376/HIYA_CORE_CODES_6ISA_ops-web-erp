package com.yunnex.ops.erp.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 系统常量Entity
 * @author linqunzhi
 * @version 2018-04-16
 */
public class SysConstants extends DataEntity<SysConstants> {
	
	private static final long serialVersionUID = 1L;
	private String key;		// 常量名
	private String value;		// 常量值
	private String description;		// 描述
	
	public SysConstants() {
		super();
	}

	public SysConstants(String id){
		super(id);
	}

	@Length(min=1, max=100, message="常量名长度必须介于 1 和 100 之间")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	@Length(min=1, max=2000, message="常量值长度必须介于 1 和 2000 之间")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Length(min=1, max=200, message="描述长度必须介于 1 和 200 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}