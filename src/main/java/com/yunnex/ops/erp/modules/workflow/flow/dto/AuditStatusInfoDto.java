package com.yunnex.ops.erp.modules.workflow.flow.dto;

import java.util.List;

import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;

/**
 * 掌贝进件和支付进件审核状态信息
 */
public class AuditStatusInfoDto {

    private String mainStoreStatus; // 掌贝进件审核状态
    private List<PayAuditStatusDto> wxPayAuditStatus; // 微信进件审核状态
    private List<PayAuditStatusDto> unionPayAuditStatus; // 银联进件审核状态
    private String aliPayStatus; // 口碑进件审核状态

    public String getAliPayStatus() {
        return aliPayStatus;
    }

    public void setAliPayStatus(String aliPayStatus) {
        this.aliPayStatus = aliPayStatus;
    }

    public String getMainStoreStatus() {
        return mainStoreStatus;
    }

    public void setMainStoreStatus(String mainStoreStatus) {
        this.mainStoreStatus = mainStoreStatus;
    }

    public List<PayAuditStatusDto> getWxPayAuditStatus() {
        return wxPayAuditStatus;
    }

    public void setWxPayAuditStatus(List<PayAuditStatusDto> wxPayAuditStatus) {
        this.wxPayAuditStatus = wxPayAuditStatus;
    }

    public List<PayAuditStatusDto> getUnionPayAuditStatus() {
        return unionPayAuditStatus;
    }

    public void setUnionPayAuditStatus(List<PayAuditStatusDto> unionPayAuditStatus) {
        this.unionPayAuditStatus = unionPayAuditStatus;
    }
}
