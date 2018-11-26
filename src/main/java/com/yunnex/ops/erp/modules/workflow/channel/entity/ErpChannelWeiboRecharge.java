package com.yunnex.ops.erp.modules.workflow.channel.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 微博通道充值Entity
 * 
 * @author yunnex
 * @version 2018-05-08
 */
public class ErpChannelWeiboRecharge extends DataEntity<ErpChannelWeiboRecharge> {

    private static final long serialVersionUID = 1L;
    private String splitId; // 分单ID
    private String shopInfoId; // 商户ID
    private String weiboAccountNo; // 微博账号
    private String weiboUid; // 微博UID
    private Double applyAmount; // 申请充值金额
    private Double actualAmount; // 实际充值金额
    private Date applyDate; // 充值申请日期
    private Date finishDate; // 充值完成日期
    private String status; // 充值状态。提交：Commit，申请：Applying，成功：Success，取消：Cancel
    private String source; // 来源，流程：Flow, 管理界面：Manage

    public ErpChannelWeiboRecharge() {
        super();
    }

    public ErpChannelWeiboRecharge(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "分单ID长度必须介于 1 和 64 之间")
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    @Length(min = 1, max = 64, message = "商户ID长度必须介于 1 和 64 之间")
    public String getShopInfoId() {
        return shopInfoId;
    }

    public void setShopInfoId(String shopInfoId) {
        this.shopInfoId = shopInfoId;
    }

    @Length(min = 1, max = 64, message = "微博账号长度必须介于 1 和 64 之间")
    public String getWeiboAccountNo() {
        return weiboAccountNo;
    }

    public void setWeiboAccountNo(String weiboAccountNo) {
        this.weiboAccountNo = weiboAccountNo;
    }

    @Length(min = 1, max = 64, message = "微博UID长度必须介于 1 和 64 之间")
    public String getWeiboUid() {
        return weiboUid;
    }

    public void setWeiboUid(String weiboUid) {
        this.weiboUid = weiboUid;
    }

    @NotNull(message = "申请充值金额不能为空")
    public Double getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Double applyAmount) {
        this.applyAmount = applyAmount;
    }

    @NotNull(message = "实际充值金额不能为空")
    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "充值申请日期不能为空")
    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "充值完成日期不能为空")
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    @Length(min = 1, max = 10, message = "充值状态。申请：Applying，成功：Success，取消：Cancel长度必须介于 1 和 10 之间")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Length(min = 1, max = 10, message = "来源，流程：Flow, 管理界面：Manage长度必须介于 1 和 10 之间")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
