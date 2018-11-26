package com.yunnex.ops.erp.modules.store.basic.dao;


import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;

/**
 * 推广图片素材DAO接口
 * @author yunnex
 * @version 2017-12-09
 */
@MyBatisDao
public interface ErpStorePromotePhotoMaterialDao extends CrudDao<ErpStorePromotePhotoMaterial> {
	int countWhereStoreId(@Param("del") String del,@Param("id") String id);
	
	ErpStorePromotePhotoMaterial findlistWhereStoreId(@Param("del") String del,@Param("id") String id);
	
}