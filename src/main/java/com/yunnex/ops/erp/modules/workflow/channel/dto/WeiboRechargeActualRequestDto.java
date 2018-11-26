package com.yunnex.ops.erp.modules.workflow.channel.dto;

import java.util.Date;

/**
 * 微博实际充值金额
 */
public class WeiboRechargeActualRequestDto {

    private String id; // 微博充值记录ID
    private Double actualAmount; // 实际充值金额
    private Date finishDate; // 充值完成日期

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
}
