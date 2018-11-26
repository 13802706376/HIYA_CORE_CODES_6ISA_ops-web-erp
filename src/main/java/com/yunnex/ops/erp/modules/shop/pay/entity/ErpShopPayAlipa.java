package com.yunnex.ops.erp.modules.shop.pay.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 支付宝口碑Entity
 * @author hanhan
 * @version 2018-05-26
 */
public class ErpShopPayAlipa extends DataEntity<ErpShopPayAlipa> {
	
	private static final long serialVersionUID = 1L;
	private String shopInfoId;		// 商户id
	private String accountNo;		// 支付宝账号
	private String accountPassword;		// 支付宝密码
	public ErpShopPayAlipa() {
		super();
	}

	public ErpShopPayAlipa(String id){
		super(id);
	}

	@Length(min=1, max=64, message="商户id长度必须介于 1 和 64 之间")
	public String getShopInfoId() {
		return shopInfoId;
	}

	public void setShopInfoId(String shopInfoId) {
		this.shopInfoId = shopInfoId;
	}
	
	@Length(min=1, max=50, message="支付宝账号长度必须介于 1 和 50 之间")
	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	@Length(min=1, max=50, message="支付宝密码长度必须介于 1 和 50 之间")
	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	
}