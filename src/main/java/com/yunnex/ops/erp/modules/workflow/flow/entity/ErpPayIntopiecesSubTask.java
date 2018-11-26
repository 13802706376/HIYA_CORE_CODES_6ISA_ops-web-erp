package com.yunnex.ops.erp.modules.workflow.flow.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 支付进件流程子任务处理信息
 * 
 * @author SunQ
 * @date 2017年12月9日
 */
public class ErpPayIntopiecesSubTask extends DataEntity<ErpPayIntopiecesSubTask> {
    
    private static final long serialVersionUID = 1L;
    // 支付进件流程表ID
    private String piId;
    // 任务编号
    private String taskId;
    // 子任务编号
    private String subTaskId;
    // 状态0:已完成,1:正在处理 2:未开始
    private String state;
    // 子任务处理人
    private String subTaskPerson;
    // 备注
    private String remark;
    // 排序字段
    private Long sort;
    // 子任务名称
    private String subTaskDetail;
    
    public ErpPayIntopiecesSubTask() {
        super();
    }

    public ErpPayIntopiecesSubTask(String id){
        super(id);
    }
    
    public String getSubTaskDetail() {
        return subTaskDetail;
    }

    public void setSubTaskDetail(String subTaskDetail) {
        this.subTaskDetail = subTaskDetail;
    }

    @Length(min=1, max=64, message="支付进件流程表ID长度必须介于 1 和 64 之间")
    public String getPiId() {
        return piId;
    }

    public void setPiId(String piId) {
        this.piId = piId;
    }

    @Length(min=1, max=64, message="任务编号长度必须介于 1 和 64 之间")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
    public String getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(String subTaskId) {
        this.subTaskId = subTaskId;
    }
    
    @Length(min=0, max=64, message="状态0:已完成,1:正在处理 2:未开始长度必须介于 0 和 64 之间")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public String getSubTaskPerson() {
        return subTaskPerson;
    }

    public void setSubTaskPerson(String subTaskPerson) {
        this.subTaskPerson = subTaskPerson;
    }
    
    @Length(min=0, max=256, message="备注长度必须介于 0 和 256 之间")
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