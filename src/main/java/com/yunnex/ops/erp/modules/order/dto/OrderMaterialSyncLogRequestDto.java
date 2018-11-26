package com.yunnex.ops.erp.modules.order.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.RequestDto;

/**
 * 订单物料同步日志查询DTO
 */
public class OrderMaterialSyncLogRequestDto extends RequestDto {

    private static final long serialVersionUID = 1L;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date syncDateStart; // 同步时间（开始）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date syncDateEnd; // 同步时间（结束）
    private Boolean exceptionOnly; // 是否只查询异常信息

    public Date getSyncDateStart() {
        return syncDateStart;
    }

    public void setSyncDateStart(Date syncDateStart) {
        this.syncDateStart = syncDateStart;
    }

    public Date getSyncDateEnd() {
        return syncDateEnd;
    }

    public void setSyncDateEnd(Date syncDateEnd) {
        this.syncDateEnd = syncDateEnd;
    }

    public Boolean getExceptionOnly() {
        return exceptionOnly;
    }

    public void setExceptionOnly(Boolean exceptionOnly) {
        this.exceptionOnly = exceptionOnly;
    }

}
