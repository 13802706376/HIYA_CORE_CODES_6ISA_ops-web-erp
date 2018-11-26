package com.yunnex.ops.erp.modules.store.advertiser.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;

/**
 * 陌陌广告主开通资料DAO接口
 * @author yunnex
 * @version 2017-12-09
 */
@MyBatisDao
public interface ErpStoreAdvertiserMomoDao extends CrudDao<ErpStoreAdvertiserMomo> {
    
    /**
     * 获取门店的陌陌开户信息
     *
     * @param storeId
     * @return
     * @date 2018年1月11日
     * @author SunQ
     */
    ErpStoreAdvertiserMomo getByStoreId(String storeId);
}