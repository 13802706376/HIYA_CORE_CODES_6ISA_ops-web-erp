package com.yunnex.ops.erp.modules.store.basic.api;

import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;

/**
 * 提供前端接口
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpFriendsApi {
	private String shopid;
	private String shopname;
	private String storeid;
	private ErpStoreAdvertiserFriends frinds;
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
	public String getStoreid() {
		return storeid;
	}
	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}
	public ErpStoreAdvertiserFriends getFrinds() {
		return frinds;
	}
	public void setFrinds(ErpStoreAdvertiserFriends frinds) {
		this.frinds = frinds;
	}
	

}
