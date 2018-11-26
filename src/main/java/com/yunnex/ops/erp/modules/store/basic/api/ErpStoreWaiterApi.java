package com.yunnex.ops.erp.modules.store.basic.api;
/**
 * 提供前台接口封装
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpStoreWaiterApi {
	private String fwRole;
	private String fwName;
	private String fwType;
	public String getFwRole() {
		return fwRole;
	}
	public void setFwRole(String fwRole) {
		this.fwRole = fwRole;
	}
	public String getFwName() {
		return fwName;
	}
	public void setFwName(String fwName) {
		this.fwName = fwName;
	}
	public String getFwType() {
		return fwType;
	}
	public void setFwType(String fwType) {
		this.fwType = fwType;
	}

}
