package com.yunnex.ops.erp.modules.workflow.flow.entity;

public class SubTask {
   
    private int subTaskConsumTime;
    
    private String subTaskDetail;

    private String taskId;

    private String taskName;

    private String startDate;

    private String endDate;

    private String assigneeName;

    private String userId;

    private String busId;

    private String procInsId;

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getSubTaskConsumTime() {
        return subTaskConsumTime;
    }

    public void setSubTaskConsumTime(int subTaskConsumTime) {
        this.subTaskConsumTime = subTaskConsumTime;
    }

    public String getSubTaskDetail() {
        return subTaskDetail;
    }

    public void setSubTaskDetail(String subTaskDetail) {
        this.subTaskDetail = subTaskDetail;
    }

    public SubTask(int subTaskConsumTime, String subTaskDetail) {
        super();
        this.subTaskConsumTime = subTaskConsumTime;
        this.subTaskDetail = subTaskDetail;
    }

    public SubTask(String taskId, String taskName, String startDate, String endDate, int subTaskConsumTime, String assigneeName, String userId,
                    String subTaskDetail) {
        super();
        this.subTaskConsumTime = subTaskConsumTime;
        this.taskId = taskId;
        this.taskName = taskName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assigneeName = assigneeName;
        this.userId = userId;
        this.subTaskDetail = subTaskDetail;
    }

    public SubTask(String taskId, String taskName, String startDate, String endDate, int subTaskConsumTime, String assigneeName, String userId,
                   String subTaskDetail,String busId) {
        super();
        this.subTaskConsumTime = subTaskConsumTime;
        this.taskId = taskId;
        this.taskName = taskName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assigneeName = assigneeName;
        this.userId = userId;
        this.subTaskDetail = subTaskDetail;
        this.busId = busId;
   }

    public SubTask(String taskId, String taskName, String startDate, String endDate, int subTaskConsumTime, String assigneeName, String userId,
                    String subTaskDetail, String busId, String procInsId) {
        super();
        this.subTaskConsumTime = subTaskConsumTime;
        this.taskId = taskId;
        this.taskName = taskName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assigneeName = assigneeName;
        this.userId = userId;
        this.subTaskDetail = subTaskDetail;
        this.busId = busId;
        this.procInsId = procInsId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

}

