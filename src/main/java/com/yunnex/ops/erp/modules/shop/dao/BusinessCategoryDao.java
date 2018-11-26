package com.yunnex.ops.erp.modules.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.shop.entity.BusinessCategory;

/**
 * 经营类目DAO接口
 * @author 11
 * @version 2017-12-20
 */
@MyBatisDao
public interface BusinessCategoryDao extends CrudDao<BusinessCategory> {
	List<BusinessCategory> findAllList();
	
	BusinessCategory whereCategoryId(@Param("cid")String cid);
	
}