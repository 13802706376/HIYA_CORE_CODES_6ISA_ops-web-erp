package com.yunnex.ops.erp.modules.store.basic.api;

import java.util.ArrayList;
import java.util.List;

public class ErpStoreInfoApi {
	private String id;
	private String storeName;// 门店简称
	private String contentName; // 联系人
	private String contentPhone; // 联系人电话
	private String address;// 门店地址
	private List<String> pay = new ArrayList<String>(); // 支付方式
	private List<String> advertiser = new ArrayList<String>(); // 推广方式
	private String isMain; // 是否主体
	private String storeMaterial = "未完成"; // 商户资料
	private Integer picNum;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getContentPhone() {
		return contentPhone;
	}

	public void setContentPhone(String contentPhone) {
		this.contentPhone = contentPhone;
	}

	public List<String> getPay() {
		return pay;
	}

	public void setPay(List<String> pay) {
		this.pay = pay;
	}

	public List<String> getAdvertiser() {
		return advertiser;
	}

	public void setAdvertiser(List<String> advertiser) {
		this.advertiser = advertiser;
	}

	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public String getStoreMaterial() {
		return storeMaterial;
	}

	public void setStoreMaterial(String storeMaterial) {
		this.storeMaterial = storeMaterial;
	}

	public Integer getPicNum() {
		return picNum;
	}

	public void setPicNum(Integer picNum) {
		this.picNum = picNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
