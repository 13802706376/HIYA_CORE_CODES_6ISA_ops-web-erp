package com.yunnex.ops.erp.modules.order.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitComment;

/**
 * 聚引客分单评论DAO接口
 * @author yunnex
 * @version 2018-01-30
 */
@MyBatisDao
public interface ErpOrderSplitCommentDao extends CrudDao<ErpOrderSplitComment> {

}
