package com.yunnex.ops.erp.modules.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopMallForm;

/**
 * 商户店铺信息收集DAO接口
 * @author hanhan
 * @version 2018-05-26
 */
@MyBatisDao
public interface ErpShopMallFormDao extends CrudDao<ErpShopMallForm> {
     
    List<ErpShopMallForm> getShopMallFormListByShopInfoId(@Param("shopInfoId") String shopInfoId);
}