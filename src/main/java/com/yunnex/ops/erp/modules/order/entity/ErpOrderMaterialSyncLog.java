package com.yunnex.ops.erp.modules.order.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 订单物料同步日志Entity
 * 
 * @author yunnex
 * @version 2018-07-02
 */
public class ErpOrderMaterialSyncLog extends DataEntity<ErpOrderMaterialSyncLog> {

    private static final long serialVersionUID = 1L;
    private String orderNumber; // 贝虎订单号
    private Long ysOrderId; // 易商订单ID
    private String zhangbeiId; // 掌贝id
    private String orderCategory; // 订单类别。First：首次，Update：更新
    private Date syncDate; // 同步时间
    private Date recoverDate; // 恢复正常时间
    /**
     * 同步情况。Normal:正常; OrderNotExist:订单不存在; ShopNotExist:商户不存在;
     * OrderMaterialCreationFlowNotExist:订单没有启动物料制作服务相关流程; SyncMaterialStatusFailed:物料制作状态同步失败;
     * ShopAgentNotExist:商户对应的服务商不存在
     */
    private String syncStatus;
    // 同步情况中文名称。见 sync_status 字段说明
    private String syncStatusName;

    public ErpOrderMaterialSyncLog() {
        super();
    }

    public ErpOrderMaterialSyncLog(String id) {
        super(id);
    }

    public Long getYsOrderId() {
        return ysOrderId;
    }

    public void setYsOrderId(Long ysOrderId) {
        this.ysOrderId = ysOrderId;
    }

    @Length(min = 1, max = 50, message = "贝虎订单号长度必须介于 1 和 50 之间")
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Length(min = 0, max = 20, message = "掌贝id长度必须介于 0 和 20 之间")
    public String getZhangbeiId() {
        return zhangbeiId;
    }

    public void setZhangbeiId(String zhangbeiId) {
        this.zhangbeiId = zhangbeiId;
    }

    @Length(min = 0, max = 20, message = "订单类别。First：首次，Update：更新长度必须介于 0 和 20 之间")
    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "同步时间不能为空")
    public Date getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(Date syncDate) {
        this.syncDate = syncDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "恢复正常时间不能为空")
    public Date getRecoverDate() {
        return recoverDate;
    }

    public void setRecoverDate(Date recoverDate) {
        this.recoverDate = recoverDate;
    }

    @Length(min = 0, max = 50, message = "同步情况。Normal: 正常；NoOrderNo: 没有订单号；NoZhangbeiId: 没有掌贝账号；NoFirstMarketingPlanService: 没有首次营销策划流程；SyncMaterialStatusFailed: 物料制作状态同步失败长度必须介于 0 和 30 之间")
    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    @Length(min = 0, max = 30, message = "同步情况中文名称。见 sync_status 字段说明长度必须介于 0 和 30 之间")
    public String getSyncStatusName() {
        return syncStatusName;
    }

    public void setSyncStatusName(String syncStatusName) {
        this.syncStatusName = syncStatusName;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "ErpOrderMaterialSyncLog{" + "orderNumber='" + orderNumber + '\'' + ", ysOrderId=" + ysOrderId + ", zhangbeiId='" + zhangbeiId + '\'' + ", orderCategory='" + orderCategory + '\'' + ", syncDate=" + syncDate + ", recoverDate=" + recoverDate + ", syncStatus='" + syncStatus + '\'' + ", syncStatusName='" + syncStatusName + '\'' + '}';
    }
}
