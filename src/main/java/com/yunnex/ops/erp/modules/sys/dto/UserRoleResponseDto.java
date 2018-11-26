package com.yunnex.ops.erp.modules.sys.dto;

public class UserRoleResponseDto extends UserRoleDto {

    private static final long serialVersionUID = 1L;

    private String roleName; // 角色中文名
    private String roleEnName; // 角色英文名
    private String userName; // 用户姓名

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleEnName() {
        return roleEnName;
    }

    public void setRoleEnName(String roleEnName) {
        this.roleEnName = roleEnName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
