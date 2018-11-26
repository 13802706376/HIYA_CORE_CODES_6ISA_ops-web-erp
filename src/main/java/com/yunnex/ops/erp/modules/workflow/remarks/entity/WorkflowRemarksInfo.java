package com.yunnex.ops.erp.modules.workflow.remarks.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 流程备注Entity
 * 
 * @author sunq
 * @version 2018-03-29
 */
public class WorkflowRemarksInfo extends DataEntity<WorkflowRemarksInfo> {

    private static final long serialVersionUID = 1L;
    private String procInsId; // 流程ID
    private String flowType; // 流程类型(J:聚引客流程D:商户资料录入流程P:支付进件流程)
    private String remarkText; // 备注内容
    private Long sort; // 排序字段
    private String remarkItemId;// 备注项Id

    /**
     * 创建人员ID(只匹配查询，不存入数据库)
     */
    private String createUserId;
    /**
     * 创建人员名称(只匹配查询，不存入数据库)
     */
    private String createUserName;
    private String remarkItemName;// 备注项名称
    private String remarkItemType;// 备注项类型



    public WorkflowRemarksInfo() {
        super();
    }

    public WorkflowRemarksInfo(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "流程ID长度必须介于 1 和 64 之间")
    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    @Length(min = 1, max = 1, message = "流程类型(J:聚引客流程D:商户资料录入流程P:支付进件流程)长度必须介于 1 和 1 之间")
    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    @Length(min = 1, max = 100, message = "备注内容长度必须介于 1 和 100 之间")
    public String getRemarkText() {
        return remarkText;
    }

    public void setRemarkText(String remarkText) {
        this.remarkText = remarkText;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getRemarkItemId() {
        return remarkItemId;
    }

    public void setRemarkItemId(String remarkItemId) {
        this.remarkItemId = remarkItemId;
    }

    public String getRemarkItemName() {
        return remarkItemName;
    }

    public void setRemarkItemName(String remarkItemName) {
        this.remarkItemName = remarkItemName;
    }

    public String getRemarkItemType() {
        return remarkItemType;
    }

    public void setRemarkItemType(String remarkItemType) {
        this.remarkItemType = remarkItemType;
    }

}
