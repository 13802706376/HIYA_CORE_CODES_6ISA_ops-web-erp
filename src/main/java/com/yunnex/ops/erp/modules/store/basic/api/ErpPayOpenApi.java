package com.yunnex.ops.erp.modules.store.basic.api;

import java.util.ArrayList;
import java.util.List;

import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
/**
 * 提供前台接口封装
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpPayOpenApi {
	private String shopid;
	private String shopname;
	private List<ErpStoreInfo> store=new ArrayList<ErpStoreInfo>();
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getShopname() {
		return shopname;
	}
	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	public List<ErpStoreInfo> getStore() {
		return store;
	}
	public void setStore(List<ErpStoreInfo> store) {
		this.store = store;
	}
	
}
