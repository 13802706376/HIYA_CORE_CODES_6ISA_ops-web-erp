package com.yunnex.ops.erp.modules.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopActualLinkman;

/**
 * 商户实际联系人信息DAO接口
 * @author yunnex
 * @version 2017-12-09
 */
@MyBatisDao
public interface ErpShopActualLinkmanDao extends CrudDao<ErpShopActualLinkman> {
	List<ErpShopActualLinkman> findShopLinmanByShopId(@Param("del")String del,@Param("id")String id);
}