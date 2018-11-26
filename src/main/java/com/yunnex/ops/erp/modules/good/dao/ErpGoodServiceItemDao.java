package com.yunnex.ops.erp.modules.good.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodServiceItem;

/**
 * 商品服务项目DAO接口
 * @author yunnex
 * @version 2018-05-29
 */
@MyBatisDao
public interface ErpGoodServiceItemDao extends CrudDao<ErpGoodServiceItem> {

}