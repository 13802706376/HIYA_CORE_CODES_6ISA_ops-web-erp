package com.yunnex.ops.erp.modules.shop.pay.dao;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.shop.pay.entity.ErpShopPayAlipa;

/**
 * 支付宝口碑DAO接口
 * @author hanhan
 * @version 2018-05-26
 */
@MyBatisDao
public interface ErpShopPayAlipaDao extends CrudDao<ErpShopPayAlipa> {
    
    ErpShopPayAlipa  getShopAilpaInfoByShopInfoId(@Param("shopInfoId") String shopInfoId);
    
	
}