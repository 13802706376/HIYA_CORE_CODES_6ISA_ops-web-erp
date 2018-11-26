package com.yunnex.ops.erp.modules.order.entity;

/**
 * 定义实体
 * @author yunnex
 * @version 2017-12-09
 * 
 * update 2017-12-09
 * 
 * 
 */
public class ApiCompleteOrder {
    private String orderId;

    private String orderNumber;

    private Integer splitId;
    
    private Integer num;//分单数量

    private String shopId;

    private String shopName;

    private String goodName;

    private String buyTime;

    private String phone;

    private String serverType;// 服务类别

    private String promotionTime;// 完成时间

    private String cname;// 联系人

    private String procInsId;
    
    private String orderType;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getSplitId() {
		return splitId;
	}

	public void setSplitId(Integer splitId) {
		this.splitId = splitId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
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

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getPromotionTime() {
		return promotionTime;
	}

	public void setPromotionTime(String promotionTime) {
		this.promotionTime = promotionTime;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
    
    

   


}
