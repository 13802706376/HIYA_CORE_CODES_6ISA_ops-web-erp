package com.yunnex.ops.erp.modules.workflow.channel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.utils.excel.annotation.MapperCell;
import com.yunnex.ops.erp.modules.workflow.channel.constant.Constants;

import java.io.Serializable;
import java.util.Date;

public class WeiboRechargeResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // ID
    @MapperCell(order = 1, cellName = "订单号", groups = {Constants.STATUS_APPLYING, Constants.STATUS_SUCCESS})
    private String splitOrderNo; // 订单号
    private String splitId; // 分单ID
    private String orderId; // 订单ID
    private String shopInfoId; // 商户ID
    @MapperCell(order = 3, cellName = "商户名称", groups = {Constants.STATUS_APPLYING, Constants.STATUS_SUCCESS})
    private String shopName; // 商户名称
    @MapperCell(order = 4, cellName = "微博账号", groups = {Constants.STATUS_APPLYING, Constants.STATUS_SUCCESS})
    private String weiboAccountNo; // 微博账号
    @MapperCell(order = 5, cellName = "微博UID", groups = {Constants.STATUS_APPLYING, Constants.STATUS_SUCCESS})
    private String weiboUid; // 微博UID
    @MapperCell(order = 6, cellName = "申请充值金额", groups = {Constants.STATUS_APPLYING, Constants.STATUS_SUCCESS})
    private Double applyAmount; // 申请充值金额
    @MapperCell(order = 7, cellName = "实际充值金额", groups = {Constants.STATUS_SUCCESS})
    private Double actualAmount; // 实际充值金额
    @MapperCell(order = 2, cellName = "充值申请日期", groups = {Constants.STATUS_APPLYING, Constants.STATUS_SUCCESS})
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date applyDate; // 充值申请日期
    @MapperCell(order = 8, cellName = "充值成功日期", groups = {Constants.STATUS_SUCCESS})
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date finishDate; // 充值成功日期
    private String status; // 充值状态。申请：Applying，成功：Success，取消：Cancel

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSplitOrderNo() {
        return splitOrderNo;
    }

    public void setSplitOrderNo(String splitOrderNo) {
        this.splitOrderNo = splitOrderNo;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopInfoId() {
        return shopInfoId;
    }

    public void setShopInfoId(String shopInfoId) {
        this.shopInfoId = shopInfoId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getWeiboAccountNo() {
        return weiboAccountNo;
    }

    public void setWeiboAccountNo(String weiboAccountNo) {
        this.weiboAccountNo = weiboAccountNo;
    }

    public String getWeiboUid() {
        return weiboUid;
    }

    public void setWeiboUid(String weiboUid) {
        this.weiboUid = weiboUid;
    }

    public Double getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Double applyAmount) {
        this.applyAmount = applyAmount;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
