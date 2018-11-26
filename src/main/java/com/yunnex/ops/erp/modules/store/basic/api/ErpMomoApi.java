package com.yunnex.ops.erp.modules.store.basic.api;

import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;
/**
 * 提供前台接口封装
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpMomoApi {
	private String shopid;
	private String shopname;
	private String storeid;
	private ErpStoreAdvertiserMomo momo;
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
	public ErpStoreAdvertiserMomo getMomo() {
		return momo;
	}
	public void setMomo(ErpStoreAdvertiserMomo momo) {
		this.momo = momo;
	}

}
