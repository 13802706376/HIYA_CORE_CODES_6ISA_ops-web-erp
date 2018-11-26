package com.yunnex.ops.erp.modules.store.basic.api;

import java.util.ArrayList;
import java.util.List;

import com.yunnex.ops.erp.modules.store.basic.entity.BankEnum;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
/**
 * 提供前台接口封装
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpUnionPayApi {
	private String shopid;
	private String shopname;
	private String storeid;
	private String credentialsId;
	private ErpStorePayUnionpay unionPay;
	private ErpStoreInfo storeInfo;
	private List<BankEnum> bankenum=new ArrayList<BankEnum>();//银行枚举list
	public List<BankEnum> getBankenum() {
		return bankenum;
	}
	public void setBankenum(List<BankEnum> bankenum) {
		this.bankenum = bankenum;
	}
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
	public String getCredentialsId() {
		return credentialsId;
	}
	public void setCredentialsId(String credentialsId) {
		this.credentialsId = credentialsId;
	}
	public ErpStorePayUnionpay getUnionPay() {
		return unionPay;
	}
	public void setUnionPay(ErpStorePayUnionpay unionPay) {
		this.unionPay = unionPay;
	}
    public ErpStoreInfo getStoreInfo() {
        return storeInfo;
    }
    public void setStoreInfo(ErpStoreInfo storeInfo) {
        this.storeInfo = storeInfo;
    }
}
