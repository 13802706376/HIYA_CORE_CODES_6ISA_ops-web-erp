package com.yunnex.ops.erp.modules.order.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 订单物料同步日志响应DTO
 */
public class OrderMaterialSyncLogResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String ysOrderId; // 易商订单ID
    private String orderNumber; // 贝虎订单号，对应订单表的订单号
    private String zhangbeiId; // 掌贝id
    private String orderCategory; // 订单类别。First：首次，Update：更新
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date syncDate; // 同步时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date recoverDate; // 恢复正常时间
    /**
     * 同步情况。Normal:正常; OrderNotExist:订单不存在; ShopNotExist:商户不存在;
     * OrderMaterialCreationFlowNotExist:订单没有启动物料制作服务相关流程; SyncMaterialStatusFailed:物料制作状态同步失败;
     * ShopAgentNotExist:商户对应的服务商不存在
     */
    private String syncStatus;
    // 同步情况中文名称。见 sync_status 字段说明
    private String syncStatusName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getYsOrderId() {
        return ysOrderId;
    }

    public void setYsOrderId(String ysOrderId) {
        this.ysOrderId = ysOrderId;
    }

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public Date getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(Date syncDate) {
        this.syncDate = syncDate;
    }

    public Date getRecoverDate() {
        return recoverDate;
    }

    public void setRecoverDate(Date recoverDate) {
        this.recoverDate = recoverDate;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getSyncStatusName() {
        return syncStatusName;
    }

    public void setSyncStatusName(String syncStatusName) {
        this.syncStatusName = syncStatusName;
    }
}
