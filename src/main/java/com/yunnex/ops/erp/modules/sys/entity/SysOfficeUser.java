package com.yunnex.ops.erp.modules.sys.entity;

import com.yunnex.ops.erp.modules.sys.entity.Office;
import javax.validation.constraints.NotNull;
import com.yunnex.ops.erp.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 人员部门关联关系Entity
 * @author 林群植
 * @version 2018-10-18
 */
public class SysOfficeUser extends DataEntity<SysOfficeUser> {
	
	private static final long serialVersionUID = 1L;
	private Office office;		// 部门id
	private User user;		// 人员id
	private String officeDn;		// 部门dn（部门唯一标识）
	private String userCn;		// 人员cn（人员登录名）
	
	public SysOfficeUser() {
		super();
	}

	public SysOfficeUser(String id){
		super(id);
	}

	@NotNull(message="部门id不能为空")
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@NotNull(message="人员id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=1, max=255, message="部门dn（部门唯一标识）长度必须介于 1 和 255 之间")
	public String getOfficeDn() {
		return officeDn;
	}

	public void setOfficeDn(String officeDn) {
		this.officeDn = officeDn;
	}
	
	@Length(min=1, max=255, message="人员cn（人员登录名）长度必须介于 1 和 255 之间")
	public String getUserCn() {
		return userCn;
	}

	public void setUserCn(String userCn) {
		this.userCn = userCn;
	}
	
}