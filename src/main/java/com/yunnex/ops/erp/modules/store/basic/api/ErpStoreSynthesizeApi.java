package com.yunnex.ops.erp.modules.store.basic.api;

import java.util.ArrayList;
import java.util.List;

import com.yunnex.ops.erp.modules.shop.entity.ErpShopActualLinkman;
/**
 * 提供前台接口封装
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpStoreSynthesizeApi {
	private String shopId;
	private String shopName;
	private Integer shopType;
	private String storeName;
	private String storeAddress;
	private String industryType;//行业类型
	private Integer weixinSchedule;//0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0
	private Integer unionpaySchedule;
	private Integer friendsSchedule;
	private Integer momoSchedule;
	private Integer weiboSchedule;
	private Integer photoSchedul;
	private Integer storecount;
	
	//主体专有字段
	private String shopNumber;
	private String xcxAccount;
	private String xcxPass;
	private String serviceProvider;
	private String servicePhone;
	private List<ErpShopActualLinkman> linkMan=new ArrayList<ErpShopActualLinkman>();
	private List<ErpStoreWaiterApi> StoreWaiter=new ArrayList<ErpStoreWaiterApi>();
	
	
	public Integer getStorecount() {
		return storecount;
	}
	public void setStorecount(Integer storecount) {
		this.storecount = storecount;
	}
	public List<ErpStoreWaiterApi> getStoreWaiter() {
		return StoreWaiter;
	}
	public void setStoreWaiter(List<ErpStoreWaiterApi> storeWaiter) {
		StoreWaiter = storeWaiter;
	}
	public String getShopNumber() {
		return shopNumber;
	}
	public void setShopNumber(String shopNumber) {
		this.shopNumber = shopNumber;
	}
	public String getXcxAccount() {
		return xcxAccount;
	}
	public void setXcxAccount(String xcxAccount) {
		this.xcxAccount = xcxAccount;
	}
	public String getXcxPass() {
		return xcxPass;
	}
	public void setXcxPass(String xcxPass) {
		this.xcxPass = xcxPass;
	}
	public String getServiceProvider() {
		return serviceProvider;
	}
	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
	public String getServicePhone() {
		return servicePhone;
	}
	public void setServicePhone(String servicePhone) {
		this.servicePhone = servicePhone;
	}
	public List<ErpShopActualLinkman> getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(List<ErpShopActualLinkman> linkMan) {
		this.linkMan = linkMan;
	}
	public Integer getPhotoSchedul() {
		return photoSchedul;
	}
	public void setPhotoSchedul(Integer photoSchedul) {
		this.photoSchedul = photoSchedul;
	}
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
	public Integer getShopType() {
		return shopType;
	}
	public void setShopType(Integer integer) {
		this.shopType = integer;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public String getIndustryType() {
		return industryType;
	}
	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}
	public Integer getWeixinSchedule() {
		return weixinSchedule;
	}
	public void setWeixinSchedule(Integer weixinSchedule) {
		this.weixinSchedule = weixinSchedule;
	}
	public Integer getUnionpaySchedule() {
		return unionpaySchedule;
	}
	public void setUnionpaySchedule(Integer unionpaySchedule) {
		this.unionpaySchedule = unionpaySchedule;
	}
	public Integer getFriendsSchedule() {
		return friendsSchedule;
	}
	public void setFriendsSchedule(Integer friendsSchedule) {
		this.friendsSchedule = friendsSchedule;
	}
	public Integer getMomoSchedule() {
		return momoSchedule;
	}
	public void setMomoSchedule(Integer momoSchedule) {
		this.momoSchedule = momoSchedule;
	}
	public Integer getWeiboSchedule() {
		return weiboSchedule;
	}
	public void setWeiboSchedule(Integer weiboSchedule) {
		this.weiboSchedule = weiboSchedule;
	}

	
	
	
}
