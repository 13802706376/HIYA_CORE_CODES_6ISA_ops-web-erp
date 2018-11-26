package com.yunnex.ops.erp.modules.order.entity;

import com.yunnex.ops.erp.common.persistence.DataEntity;

public class PromoteOrderSplit extends DataEntity<PromoteOrderSplit> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderNumber;
	private String shopName;
	private String buyDate;
	private String contactNumber;
	private String orderType;
	private String companyName;
	private String agentName; 
	private String goodNames;
	private String promoteStatus;
	private String splitId;
	private String promotionChannel;
	private String goodName;
	private String promoteStartDate;
	private String promoteEndDate;
	private double expenditurePy;
	private double expenditureWb;
	private double expenditureMm;
	private int rowCount; 
	
	public String getPromoteStatus() {
		return promoteStatus;
	}
	public void setPromoteStatus(String promoteStatus) {
		this.promoteStatus = promoteStatus;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public void setExpenditurePy(double expenditurePy) {
		this.expenditurePy = expenditurePy;
	}
	public void setExpenditureWb(double expenditureWb) {
		this.expenditureWb = expenditureWb;
	}
	public void setExpenditureMm(double expenditureMm) {
		this.expenditureMm = expenditureMm;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getGoodNames() {
		return goodNames;
	}
	public void setGoodNames(String goodNames) {
		this.goodNames = goodNames;
	}
	public String getSplitId() {
		return splitId;
	}
	public void setSplitId(String splitId) {
		this.splitId = splitId;
	}
	public String getPromotionChannel() {
		return promotionChannel;
	}
	public void setPromotionChannel(String promotionChannel) {
		this.promotionChannel = promotionChannel;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getPromoteStartDate() {
		return promoteStartDate;
	}
	public void setPromoteStartDate(String promoteStartDate) {
		this.promoteStartDate = promoteStartDate;
	}
	public String getPromoteEndDate() {
		return promoteEndDate;
	}
	public void setPromoteEndDate(String promoteEndDate) {
		this.promoteEndDate = promoteEndDate;
	}
	public Double getExpenditurePy() {
		return expenditurePy;
	}
	public void setExpenditurePy(Double expenditurePy) {
		this.expenditurePy = expenditurePy;
	}
	public Double getExpenditureWb() {
		return expenditureWb;
	}
	public void setExpenditureWb(Double expenditureWb) {
		this.expenditureWb = expenditureWb;
	}
	public Double getExpenditureMm() {
		return expenditureMm;
	}
	public void setExpenditureMm(Double expenditureMm) {
		this.expenditureMm = expenditureMm;
	}
	
	@Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
