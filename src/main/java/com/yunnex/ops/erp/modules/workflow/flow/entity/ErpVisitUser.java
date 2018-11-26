package com.yunnex.ops.erp.modules.workflow.flow.entity;

import com.yunnex.ops.erp.common.persistence.DataEntity;

public class ErpVisitUser extends DataEntity<ErpVisitUser>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;		// 用户id
	private String roleName; 	// 处理人角色
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
	
}
