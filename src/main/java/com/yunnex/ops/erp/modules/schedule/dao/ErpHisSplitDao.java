package com.yunnex.ops.erp.modules.schedule.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.schedule.entity.ErpHisSplit;

/**
 * 生产进度小程序父表DAO接口
 * @author pengchenghe
 * @version 2018-01-19
 */
@MyBatisDao
public interface ErpHisSplitDao extends CrudDao<ErpHisSplit> {
	
}