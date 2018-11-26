package com.yunnex.ops.erp.modules.sys.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.sys.entity.SysOfficeUser;

/**
 * 人员部门关联关系DAO接口
 * @author 林群植
 * @version 2018-10-18
 */
@MyBatisDao
public interface SysOfficeUserDao extends CrudDao<SysOfficeUser> {
	
}