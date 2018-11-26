package com.yunnex.ops.erp.modules.workflow.flow.entity;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 子任务操作内容对象
 * 
 * @author SunQ
 * @date 2018年1月25日
 */
public class ErpOrderOperateValue extends DataEntity<ErpOrderOperateValue> {

    private static final long serialVersionUID = -6562919872810126391L;

    /**
     * 订单ID
     */
    private String orderId;
    
    /**
     * 分单序号
     */
    private String splitId;
    
    /**
     * 流程编号
     */
    private String procInsId;
    
    /**
     * 子任务编号
     */
    private String subTaskId;
    
    /**
     * 模板名称
     */
    private String keyName;
    
    /**
     * 操作内容
     */
    private String value;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 排序字段
     */
    private Long sort;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(String subTaskId) {
        this.subTaskId = subTaskId;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }
}