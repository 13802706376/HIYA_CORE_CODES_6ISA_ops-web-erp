package com.yunnex.ops.erp.modules.store.basic.api;

import java.util.ArrayList;
import java.util.List;

import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
/**
 * 提供前台接口封装
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpZhangbeiApi {
	private String zhangbeiId;
	private String shopName;
	private String shopId;

	/**
	 * 是否同步异常，Y:是N:否
	 */

	private String isAbnormal;
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	private List<ErpStoreInfo> storelist=new ArrayList<ErpStoreInfo>();
	public String getZhangbeiId() {
		return zhangbeiId;
	}
	public void setZhangbeiId(String zhangbeiId) {
		this.zhangbeiId = zhangbeiId;
	}
	public List<ErpStoreInfo> getStorelist() {
		return storelist;
	}
	public void setStorelist(List<ErpStoreInfo> storelist) {
		this.storelist = storelist;
	}
    public String getIsAbnormal() {
        return isAbnormal;
    }
    public void setIsAbnormal(String isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

}
