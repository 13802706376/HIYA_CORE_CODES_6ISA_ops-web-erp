package com.yunnex.ops.erp.modules.order.dto;

import java.util.Date;
import java.util.List;

import com.yunnex.ops.erp.modules.material.dto.MaterialContentDto;

import yunnex.common.core.dto.BaseDto;

/**
 * 订单物料同步DTO
 */
public class OrderMaterialDto extends BaseDto {

    private static final long serialVersionUID = 1L;

    private String orderNumber; // 贝虎订单号
    private Long ysOrderId; // 易商订单ID
    private String zhangbeiId; // 掌贝账号
    private String orderCategory; // 订单类别，首次/更新
    private Date orderBuyTime; // 订单

    private String orderLinkMan; // 订单联系人
    private String orderLinkPhone; // 订单联系电话
    private String orderReceiveAddress; // 订单收货地址

    /**
     * 物料内容
     */
    private List<MaterialContentDto> materialContents;

    // 商户名称
    private String shopName;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getYsOrderId() {
        return ysOrderId;
    }

    public void setYsOrderId(Long ysOrderId) {
        this.ysOrderId = ysOrderId;
    }

    public String getZhangbeiId() {
        return zhangbeiId;
    }

    public void setZhangbeiId(String zhangbeiId) {
        this.zhangbeiId = zhangbeiId;
    }

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public Date getOrderBuyTime() {
        return orderBuyTime;
    }

    public void setOrderBuyTime(Date orderBuyTime) {
        this.orderBuyTime = orderBuyTime;
    }

    public String getOrderLinkMan() {
        return orderLinkMan;
    }

    public void setOrderLinkMan(String orderLinkMan) {
        this.orderLinkMan = orderLinkMan;
    }

    public String getOrderLinkPhone() {
        return orderLinkPhone;
    }

    public void setOrderLinkPhone(String orderLinkPhone) {
        this.orderLinkPhone = orderLinkPhone;
    }

    public String getOrderReceiveAddress() {
        return orderReceiveAddress;
    }

    public void setOrderReceiveAddress(String orderReceiveAddress) {
        this.orderReceiveAddress = orderReceiveAddress;
    }

    public List<MaterialContentDto> getMaterialContents() {
        return materialContents;
    }

    public void setMaterialContents(List<MaterialContentDto> materialContents) {
        this.materialContents = materialContents;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
