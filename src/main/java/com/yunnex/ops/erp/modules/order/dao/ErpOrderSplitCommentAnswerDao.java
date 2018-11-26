package com.yunnex.ops.erp.modules.order.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitCommentAnswer;

/**
 * 分单评价回答表DAO接口
 * @author linqunzhi
 * @version 2018-04-04
 */
@MyBatisDao
public interface ErpOrderSplitCommentAnswerDao extends CrudDao<ErpOrderSplitCommentAnswer> {
	
}