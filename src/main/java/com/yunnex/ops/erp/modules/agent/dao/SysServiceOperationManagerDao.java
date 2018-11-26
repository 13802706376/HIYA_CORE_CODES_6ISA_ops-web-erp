package com.yunnex.ops.erp.modules.agent.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.agent.entity.SysServiceOperationManager;

/**
 * 服务商运营经关系对应表DAO接口
 * @author hanhan
 * @version 2018-06-01
 */
@MyBatisDao
public interface SysServiceOperationManagerDao extends CrudDao<SysServiceOperationManager> {

    SysServiceOperationManager findByServiceNo(String serviceNo);
	
}
