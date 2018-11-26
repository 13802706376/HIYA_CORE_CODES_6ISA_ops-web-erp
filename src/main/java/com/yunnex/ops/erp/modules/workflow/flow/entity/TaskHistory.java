package com.yunnex.ops.erp.modules.workflow.flow.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 任务列表Entity
 * 
 * @author Frank
 * @version 2017-10-27
 */
public class TaskHistory {
    /** 订单编号 */
    private String taskName;
    /** 商户名称 */
    private String taskPerson;
    /** 完成时间 */
    private Date taskTime;
    /** 订单编号 */
    private String taskId;
    /** 子任务 */
    private List<SubTaskHistory> subTaskHistroy = new ArrayList<SubTaskHistory>();
    /**是否完成*/
    private Integer isFinished;
    /**开始时间*/
    private Date startTaskTime;
    
    private Integer sort;
    

    public Date getStartTaskTime() {
        return startTaskTime;
    }

    public void setStartTaskTime(Date startTaskTime) {
        this.startTaskTime = startTaskTime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<SubTaskHistory> getSubTaskHistroy() {
        return subTaskHistroy;
    }

    public void setSubTaskHistroy(List<SubTaskHistory> subTaskHistroy) {
        this.subTaskHistroy = subTaskHistroy;
    }

    public Integer getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Integer isFinished) {
        this.isFinished = isFinished;
    }

    
    


}
