package com.yunnex.ops.erp.modules.workflow.flow.entity;

import java.util.Date;

/**
 * 任务列表Entity
 * 
 * @author Frank
 * @version 2017-10-27
 */
public class SubTaskHistory {
    /** 订单编号 */
    private String taskName;
    /** 商户名称 */
    private String taskPerson;
    /** 完成时间 */
    private Date taskTime;
    /** 是否完成 */
    private String isFinished;
    /**开始时间*/
    private Date startTaskTime;
    
    

    public Date getStartTaskTime() {
        return startTaskTime;
    }

    public void setStartTaskTime(Date startTaskTime) {
        this.startTaskTime = startTaskTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskPerson() {
        return taskPerson;
    }

    public void setTaskPerson(String taskPerson) {
        this.taskPerson = taskPerson;
    }

    public Date getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Date taskTime) {
        this.taskTime = taskTime;
    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }




}
