package com.yunnex.ops.erp.modules.workflow.flow.dto;

import java.util.Date;
import java.util.Iterator;

public abstract class FlowInfoTask {

    private String taskName;

    private String taskId;

    private String procInsId;

    private String processDefineKey;

    private String orderId;

    private String orderNumber;

    private String shopName;

    private Integer taskHour;

    private Integer urgentTaskHour;

    private Date taskStartDate;

    private Date taskEndDate;

    private Integer taskConsumTime;

    private String taskRef;

    private Integer total;

    private String flowMark;

    public String getFlowMark() {
        return flowMark;
    }

    public void setFlowMark(String flowMark) {
        this.flowMark = flowMark;
    }

    public String getTaskRef() {
        return taskRef;
    }

    public void setTaskRef(String taskRef) {
        this.taskRef = taskRef;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTaskConsumTime() {
        return taskConsumTime;
    }

    public void setTaskConsumTime(Integer taskConsumTime) {
        this.taskConsumTime = taskConsumTime;
    }

    public Date getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(Date taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public Date getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Date taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Integer getTaskHour() {
        return taskHour;
    }

    public void setTaskHour(Integer taskHour) {
        this.taskHour = taskHour;
    }

    public Integer getUrgentTaskHour() {
        return urgentTaskHour;
    }

    public void setUrgentTaskHour(Integer urgentTaskHour) {
        this.urgentTaskHour = urgentTaskHour;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getProcessDefineKey() {
        return processDefineKey;
    }

    public void setProcessDefineKey(String processDefineKey) {
        this.processDefineKey = processDefineKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public boolean add(FlowInfoTask equipment) {  
        return false;
    }
    public boolean remove(FlowInfoTask equipment) {  
        return false;
    }

    @SuppressWarnings("rawtypes")
    public Iterator iter() {  
        return null;
    }

    @Override
    public String toString() {
        return "FlowInfoTask [taskName=" + taskName + ", processDefineKey=" + processDefineKey + ", orderNumber=" + orderNumber + "]";
    }



}
