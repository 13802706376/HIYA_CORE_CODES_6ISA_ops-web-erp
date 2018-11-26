package com.yunnex.ops.erp.modules.message.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceLink;

/**
 * 交互入口配置表DAO接口
 * @author yunnex
 * @version 2018-07-04
 */
@MyBatisDao
public interface ErpServiceLinkDao extends CrudDao<ErpServiceLink> {
	
}