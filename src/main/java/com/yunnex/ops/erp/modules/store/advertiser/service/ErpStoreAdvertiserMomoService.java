package com.yunnex.ops.erp.modules.store.advertiser.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.store.advertiser.dao.ErpStoreAdvertiserMomoDao;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;

/**
 * 陌陌广告主开通资料Service
 * @author yunnex
 * @version 2017-12-09
 */
@Service
public class ErpStoreAdvertiserMomoService extends CrudService<ErpStoreAdvertiserMomoDao, ErpStoreAdvertiserMomo> {

    @Autowired
    private ErpStoreAdvertiserMomoDao erpStoreAdvertiserMomoDao;

    @Override
    @Transactional(readOnly = false)
    public void save(ErpStoreAdvertiserMomo erpStoreAdvertiserMomo) {
        super.save(erpStoreAdvertiserMomo);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpStoreAdvertiserMomo erpStoreAdvertiserMomo) {
        super.delete(erpStoreAdvertiserMomo);
    }
    
    public ErpStoreAdvertiserMomo getByStoreId(String storeId) {
        return erpStoreAdvertiserMomoDao.getByStoreId(storeId);
    }
}