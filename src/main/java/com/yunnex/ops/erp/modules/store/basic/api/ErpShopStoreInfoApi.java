package com.yunnex.ops.erp.modules.store.basic.api;

import java.util.ArrayList;
import java.util.List;
/**
 * 提供前台接口封装
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpShopStoreInfoApi {
	private String shopId;
	private String shopName;
	private List<ErpStoreInfoApi> storelist=new ArrayList<ErpStoreInfoApi>();
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public List<ErpStoreInfoApi> getStorelist() {
		return storelist;
	}
	public void setStorelist(List<ErpStoreInfoApi> storelist) {
		this.storelist = storelist;
	}
	
	
	

}
