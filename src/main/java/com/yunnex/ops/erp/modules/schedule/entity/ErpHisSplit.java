package com.yunnex.ops.erp.modules.schedule.entity;


import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 生产进度小程序父表Entity
 * @author pengchenghe
 * @version 2018-01-19
 */
public class ErpHisSplit extends DataEntity<ErpHisSplit> {
	
	private static final long serialVersionUID = 1L;
	private String erpStoreInfoId;		// 门店id
	private String shopInfoId;		// 商户id
	private String orderNum;		// 订单号
	private String orderId;		// 订单id
	private String splitNum;		// 分单号
	private String splitId;		// 分单id
	private String processTitle;		// 流程标题
	private String processType; //类型
	
	
	public ErpHisSplit() {
		super();
	}

	public ErpHisSplit(String id){
		super(id);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	

	@Length(min=0, max=255, message="门店id长度必须介于 0 和 255 之间")
	public void setErpStoreInfoId(String erpStoreInfoId) {
		this.erpStoreInfoId = erpStoreInfoId;
	}

	public void setShopInfoId(String shopInfoId) {
		this.shopInfoId = shopInfoId;
	}
	
	@Length(min=0, max=255, message="商户id长度必须介于 0 和 255 之间")
	public String getShopInfoId() {
		return shopInfoId;
	}

	public String getErpStoreInfoId() {
		return erpStoreInfoId;
	}
	

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	
	@Length(min=0, max=255, message="订单号长度必须介于 0 和 255 之间")
	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	@Length(min=0, max=255, message="订单id长度必须介于 0 和 255 之间")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@Length(min=0, max=255, message="分单号长度必须介于 0 和 255 之间")
	public String getSplitNum() {
		return splitNum;
	}

	public void setSplitNum(String splitNum) {
		this.splitNum = splitNum;
	}
	
	@Length(min=0, max=255, message="分单id长度必须介于 0 和 255 之间")
	public String getSplitId() {
		return splitId;
	}

	public void setSplitId(String splitId) {
		this.splitId = splitId;
	}
	
	@Length(min=0, max=255, message="流程标题长度必须介于 0 和 255 之间")
	public String getProcessTitle() {
		return processTitle;
	}

	public void setProcessTitle(String processTitle) {
		this.processTitle = processTitle;
	}
	
}