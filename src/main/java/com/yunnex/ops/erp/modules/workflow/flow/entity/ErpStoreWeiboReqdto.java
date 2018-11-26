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
public class ErpStoreWeiboReqdto {
    private String accountNo;       // 微博登录账号
    private String uid;     // 微博UID
    private Integer auditStatus=0;      // 审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0
    private String storeName;//推广门店
    private String taskDisplay;//推广套餐
    public String getAccountNo() {
        return accountNo;
    }
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public Integer getAuditStatus() {
        return auditStatus;
    }
    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }
    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public String getTaskDisplay() {
        return taskDisplay;
    }
    public void setTaskDisplay(String taskDisplay) {
        this.taskDisplay = taskDisplay;
    }
    
    
    
    
}
