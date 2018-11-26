package com.yunnex.ops.erp.modules.material.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 物料制作Entity
 *
 * @author yunnex
 * @version 2018-05-25
 */
public class ErpOrderMaterialCreation extends DataEntity<ErpOrderMaterialCreation> {

    private static final long serialVersionUID = 1L;
    private String orderNumber; // 订单编号
    private Long ysOrderId; // 易商订单ID，来自易商平台
    private String procInsId; // 流程实例ID
    private String providerName; // 供应商名称
    private Long cost; // 成本(单位：分)
    private String logisticsNumber; // 物流单号
    private Date placeOrderTime; // 下单时间
    private Date deliverTime; // 物料到店时间
    private String layoutName; // 设计稿名称
    private String layoutUrl; // 设计稿url
    private String status; // 状态:waiting_layout待设计稿,waiting_order待下单制作,placed_order已下单制作,in_transit物料运送中,arrived物料已到店
    private String statusName; // 状态名称
    private String shopName; // 商户名称
    private String zhangbeiId; // 掌贝id
    private String operationAdviserId; // 运营顾问ID
    private String operationAdviserName;// 运营顾问名字
    private String materialPath; // 物料包存放路径

    /*封装字段*/
    private String shopInfoId;// 商户信息表主键

    public ErpOrderMaterialCreation() {
        super();
    }

    public ErpOrderMaterialCreation(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "流程实例ID长度必须介于 1 和 64 之间")
    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    @Length(min = 1, max = 50, message = "供应商名称长度必须介于 1 和 50 之间")
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @NotNull(message = "成本(单位：分)不能为空")
    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Long getYsOrderId() {
        return ysOrderId;
    }

    public void setYsOrderId(Long ysOrderId) {
        this.ysOrderId = ysOrderId;
    }

    @Length(min = 1, max = 64, message = "物流单号长度必须介于 1 和 64 之间")
    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "下单时间不能为空")
    public Date getPlaceOrderTime() {
        return placeOrderTime;
    }

    public void setPlaceOrderTime(Date placeOrderTime) {
        this.placeOrderTime = placeOrderTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "物料到店时间不能为空")
    public Date getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    @Length(min = 1, max = 50, message = "设计稿名称长度必须介于 1 和 50 之间")
    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    @Length(min = 1, max = 256, message = "设计稿url长度必须介于 1 和 256 之间")
    public String getLayoutUrl() {
        return layoutUrl;
    }

    public void setLayoutUrl(String layoutUrl) {
        this.layoutUrl = layoutUrl;
    }

    @Length(min = 1, max = 50, message = "状态:waiting_layout待设计稿,waiting_order待下单制作,placed_order已下单制作,in_transit物料运送中,arrived物料已到店长度必须介于 1 和 50 之间")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOperationAdviserId() {
        return operationAdviserId;
    }

    public void setOperationAdviserId(String operationAdviserId) {
        this.operationAdviserId = operationAdviserId;
    }

    public String getOperationAdviserName() {
        return operationAdviserName;
    }

    public void setOperationAdviserName(String operationAdviserName) {
        this.operationAdviserName = operationAdviserName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getZhangbeiId() {
        return zhangbeiId;
    }

    public void setZhangbeiId(String zhangbeiId) {
        this.zhangbeiId = zhangbeiId;
    }

    public String getShopInfoId() {
        return shopInfoId;
    }

    public void setShopInfoId(String shopInfoId) {
        this.shopInfoId = shopInfoId;
    }

    public String getMaterialPath() {
        return materialPath;
    }

    public void setMaterialPath(String materialPath) {
        this.materialPath = materialPath;
    }
}
