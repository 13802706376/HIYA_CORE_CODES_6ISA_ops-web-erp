package com.yunnex.ops.erp.modules.store.basic.api;
/**
 * 提供前台接口封装
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpWeiboCheckApi {
	private String storeId;
	private String storeName;
	private String weiboNum;
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
	public String getWeiboNum() {
		return weiboNum;
	}
	public void setWeiboNum(String weiboNum) {
		this.weiboNum = weiboNum;
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
