package com.yunnex.ops.erp.modules.workflow.channel.dto;

import java.io.Serializable;
import java.util.Date;

import com.yunnex.ops.erp.common.persistence.Pager;

public class WeiboRechargeRequestDto extends Pager<WeiboRechargeRequestDto> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String status; // 充值状态。申请：Applying，成功：Success，取消：Cancel
    private String orderNumber; // 订单号
    private String shopName; // 商户名称
    private String weiboAccountNo; // 微博账号
    private String weiboUid; // 微博UID
    private Date applyDateStart; // 申请开始日期
    private Date applyDateEnd; // 申请结束日期
    private Date finishDateStart; // 充值成功开始日期
    private Date finishDateEnd; // 充值成功结束日期

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getApplyDateStart() {
        return applyDateStart;
    }

    public void setApplyDateStart(Date applyDateStart) {
        this.applyDateStart = applyDateStart;
    }

    public Date getApplyDateEnd() {
        return applyDateEnd;
    }

    public void setApplyDateEnd(Date applyDateEnd) {
        this.applyDateEnd = applyDateEnd;
    }

    public Date getFinishDateStart() {
        return finishDateStart;
    }

    public void setFinishDateStart(Date finishDateStart) {
        this.finishDateStart = finishDateStart;
    }

    public Date getFinishDateEnd() {
        return finishDateEnd;
    }

    public void setFinishDateEnd(Date finishDateEnd) {
        this.finishDateEnd = finishDateEnd;
    }
}
