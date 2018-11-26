package com.yunnex.ops.erp.modules.store.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStoreBank;

/**
 * 银行信息DAO接口
 * @author yunnex
 * @version 2017-12-15
 */
@MyBatisDao
public interface ErpStoreBankDao extends CrudDao<ErpStoreBank> {
	List<ErpStoreBank> findwhereshopid(@Param("shopId")String shopId);
	
}