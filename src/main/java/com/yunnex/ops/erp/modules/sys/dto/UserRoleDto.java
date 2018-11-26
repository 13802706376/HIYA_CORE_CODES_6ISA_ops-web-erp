package com.yunnex.ops.erp.modules.sys.dto;

import java.io.Serializable;

/**
 * 用户角色信息
 */
public class UserRoleDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String roleId;
    private String delFlag;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
