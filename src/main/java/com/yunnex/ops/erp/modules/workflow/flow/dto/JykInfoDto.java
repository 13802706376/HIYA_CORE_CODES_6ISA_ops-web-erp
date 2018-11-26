package com.yunnex.ops.erp.modules.workflow.flow.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;

/**
 * 聚引客信息
 */
public class JykInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ErpOrderSplitGood> splitGoods; // 选择推广的套餐
    private Date promotionTime; // 推广时间
    private String promotionChannels; // 推广通道
    private String planningExpert; // 策划专家
    private List<AdvAuditStatusDto> advAuditStatus; // 推广门店的广告主审核状态

    public List<ErpOrderSplitGood> getSplitGoods() {
        return splitGoods;
    }

    public void setSplitGoods(List<ErpOrderSplitGood> splitGoods) {
        this.splitGoods = splitGoods;
    }

    public Date getPromotionTime() {
        return promotionTime;
    }

    public void setPromotionTime(Date promotionTime) {
        this.promotionTime = promotionTime;
    }

    public String getPromotionChannels() {
        return promotionChannels;
    }

    public void setPromotionChannels(String promotionChannels) {
        this.promotionChannels = promotionChannels;
    }

    public String getPlanningExpert() {
        return planningExpert;
    }

    public void setPlanningExpert(String planningExpert) {
        this.planningExpert = planningExpert;
    }

    public List<AdvAuditStatusDto> getAdvAuditStatus() {
        return advAuditStatus;
    }

    public void setAdvAuditStatus(List<AdvAuditStatusDto> advAuditStatus) {
        this.advAuditStatus = advAuditStatus;
    }
}
