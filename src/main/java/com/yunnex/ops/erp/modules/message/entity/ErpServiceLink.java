package com.yunnex.ops.erp.modules.message.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 交互入口配置表Entity
 * @author yunnex
 * @version 2018-07-04
 */
public class ErpServiceLink extends DataEntity<ErpServiceLink> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String param;		// 参数
	private String name;		// 名称
	
	public ErpServiceLink() {
		super();
	}

	public ErpServiceLink(String id){
		super(id);
	}

	@Length(min=1, max=50, message="类型长度必须介于 1 和 50 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=1, max=200, message="参数长度必须介于 1 和 200 之间")
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	
	@Length(min=1, max=50, message="名称长度必须介于 1 和 50 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}