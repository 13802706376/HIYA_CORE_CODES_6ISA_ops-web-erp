package com.yunnex.ops.erp.modules.store.basic.api;

public class ErpFriendCheckApi {
    // 门店ID
    private String storeId;
    // 门店名称
    private String storeName;
    // APPID
    private String appId;
    // 审核状态
    private Integer auditState;
    // 备注
    private String auditContent;
    public String getStoreId() {
        return storeId;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public Integer getAuditState() {
        return auditState;
    }
    public void setAuditState(Integer auditState) {
        this.auditState = auditState;
    }
    public String getAuditContent() {
        return auditContent;
    }
    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }
    
    
    

}
