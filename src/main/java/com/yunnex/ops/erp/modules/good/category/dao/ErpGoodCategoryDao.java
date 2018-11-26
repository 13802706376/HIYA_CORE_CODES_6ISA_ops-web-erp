package com.yunnex.ops.erp.modules.good.category.dao;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.good.category.entity.ErpGoodCategory;

/**
 * 商品分类管理DAO接口
 * @author Frank
 * @version 2017-10-21
 */
@MyBatisDao
public interface ErpGoodCategoryDao extends CrudDao<ErpGoodCategory> {

    ErpGoodCategory getByGoodId(@Param("goodId") Long goodId);

    ErpGoodCategory findByCode(@Param("code") String code);

}