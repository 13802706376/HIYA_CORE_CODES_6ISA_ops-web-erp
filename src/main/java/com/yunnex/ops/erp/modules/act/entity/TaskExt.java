package com.yunnex.ops.erp.modules.act.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * act_ru_task_ext生成Entity
 * 
 * @author act_ru_task_ext生成
 * @version 2018-01-13
 */
public class TaskExt extends DataEntity<TaskExt> {

    private static final long serialVersionUID = 1L;
    private String taskId; // 任务ID
    private String pendingProdFlag; // 是否是待生产库
    private String status; // 任务状态
    private String remark; // 备注

    public TaskExt() {
        super();
    }

    public TaskExt(String id) {
        super(id);
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
        return super.toString();
    }

    @Length(min = 1, max = 64, message = "任务ID长度必须介于 1 和 64 之间")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Length(min = 1, max = 1, message = "是否是待生产库长度必须介于 1 和 1 之间")
    public String getPendingProdFlag() {
        return pendingProdFlag;
    }

    public void setPendingProdFlag(String pendingProdFlag) {
        this.pendingProdFlag = pendingProdFlag;
    }

    @Length(min = 1, max = 1, message = "任务状态长度必须介于 1 和 1 之间")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Length(min = 0, max = 2000, message = "备注长度必须介于 0 和 2000 之间")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
