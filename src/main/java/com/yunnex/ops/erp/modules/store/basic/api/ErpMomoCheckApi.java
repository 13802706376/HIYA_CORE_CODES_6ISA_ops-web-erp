package com.yunnex.ops.erp.modules.store.basic.api;
/**
 * 提供前台接口封装
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpMomoCheckApi {
	private String storeId;
	private String storeName;
	private String momoNum;
	private Integer auditState;
	private String auditContent;
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getMomoNum() {
		return momoNum;
	}
	public void setMomoNum(String momoNum) {
		this.momoNum = momoNum;
	}
	public Integer getAuditState() {
		return auditState;
	}
	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}
	public String getAuditContent() {
		return auditContent;
	}
	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}
	
	

}
