package com.yunnex.ops.erp.modules.store.basic.api;

import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
/**
 * 提供前台接口封装
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpWeiboApi {
	private String shopid;
	private String shopname;
	private String storeid;
	private ErpStoreAdvertiserWeibo weibo;
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
	public ErpStoreAdvertiserWeibo getWeibo() {
		return weibo;
	}
	public void setWeibo(ErpStoreAdvertiserWeibo weibo) {
		this.weibo = weibo;
	}
	
}
