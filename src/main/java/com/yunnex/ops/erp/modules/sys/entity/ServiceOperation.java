package com.yunnex.ops.erp.modules.sys.entity;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 服务商对应运营经理Entity
 * 
 * @author SunQ
 * @date 2017年12月15日
 */
public class ServiceOperation extends DataEntity<ServiceOperation> {

    private static final long serialVersionUID = 8580963217984113174L;

    // 服务商编号
    private String serviceNo;
    // 分公司名称
    private String companyName;
    // 服务区域
    private String serverAddress;
    // 角色
    private String roleName;
    // 默认运营经理人员
    private String defaultManager;
    // 默认运营经理人员ID
    private String defaultManagerId;
    // 备选运营经理人员子任务处理人
    private String alternativeManager;
    // 备选运营经理人员子任务处理人ID
    private String alternativeManagerId;
    
    public String getServiceNo() {
        return serviceNo;
    }
    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getServerAddress() {
        return serverAddress;
    }
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public String getDefaultManager() {
        return defaultManager;
    }
    public void setDefaultManager(String defaultManager) {
        this.defaultManager = defaultManager;
    }
    public String getDefaultManagerId() {
        return defaultManagerId;
    }
    public void setDefaultManagerId(String defaultManagerId) {
        this.defaultManagerId = defaultManagerId;
    }
    public String getAlternativeManager() {
        return alternativeManager;
    }
    public void setAlternativeManager(String alternativeManager) {
        this.alternativeManager = alternativeManager;
    }
    public String getAlternativeManagerId() {
        return alternativeManagerId;
    }
    public void setAlternativeManagerId(String alternativeManagerId) {
        this.alternativeManagerId = alternativeManagerId;
    }
}