package com.yunnex.ops.erp.modules.store.basic.dao;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLinkman;

/**
 * 门店联系人信息DAO接口
 * @author yunnex
 * @version 2017-12-09
 */
@MyBatisDao
public interface ErpStoreLinkmanDao extends CrudDao<ErpStoreLinkman> {
    ErpStoreLinkman findWhereStoreId(@Param("del") String del, @Param("id") String id);

    ErpStoreLinkman getBySplitId(String splitId);
}