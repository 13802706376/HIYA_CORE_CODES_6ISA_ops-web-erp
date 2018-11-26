package com.yunnex.ops.erp.modules.workflow.flow.entity;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 商户资料录入流程信息表
 * 
 * @author SunQ
 * @date 2017年12月9日
 */
public class SdiFlow extends DataEntity<SdiFlow> {

    private static final long serialVersionUID = -3201459530386501414L;
    
    //原始订单信息
    private String orderId;
    //商户资料录入表ID
    private String sdiId;
    //流程编号
    private String procInsId;
    //运营经理
    private String operationManager;
    //运营顾问
    private String operationAdviser;
    //备注
    private String remark;
    //排序字段
    private Long sort;
    
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getSdiId() {
        return sdiId;
    }
    public void setSdiId(String sdiId) {
        this.sdiId = sdiId;
    }
    public String getProcInsId() {
        return procInsId;
    }
    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }
    public String getOperationManager() {
        return operationManager;
    }
    public void setOperationManager(String operationManager) {
        this.operationManager = operationManager;
    }
    public String getOperationAdviser() {
        return operationAdviser;
    }
    public void setOperationAdviser(String operationAdviser) {
        this.operationAdviser = operationAdviser;
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