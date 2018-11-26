package com.yunnex.ops.erp.modules.workflow.flow.dto;

import java.io.Serializable;

/**
 * 支付资料审核状态
 */
public class PayAuditStatusDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String storeName; // 门店名称
    private Integer auditStatus; // 审核状态
    private String auditStatusName; // 审核状态名称
    private String isMain;
    private String notOpenUnionpayFlag;

    public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	public String getNotOpenUnionpayFlag() {
		return notOpenUnionpayFlag;
	}

	public void setNotOpenUnionpayFlag(String notOpenUnionpayFlag) {
		this.notOpenUnionpayFlag = notOpenUnionpayFlag;
	}

	public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatusName() {
        return auditStatusName;
    }

    public void setAuditStatusName(String auditStatusName) {
        this.auditStatusName = auditStatusName;
    }
}
