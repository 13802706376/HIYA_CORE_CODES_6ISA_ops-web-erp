package com.yunnex.ops.erp.modules.order.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitCommentQConf;

/**
 * 分单评价问题配置DAO接口
 * @author yunnex
 * @version 2018-04-04
 */
@MyBatisDao
public interface ErpOrderSplitCommentQConfDao extends CrudDao<ErpOrderSplitCommentQConf> {
	
}