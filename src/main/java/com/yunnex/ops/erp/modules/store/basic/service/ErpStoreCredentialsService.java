package com.yunnex.ops.erp.modules.store.basic.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.store.basic.dao.ErpStoreCredentialsDao;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreCredentials;

/**
 * 商户营业资质信息Service
 * @author yunnex
 * @version 2017-12-09
 */
@Service
public class ErpStoreCredentialsService extends CrudService<ErpStoreCredentialsDao, ErpStoreCredentials> {

    @Override
    @Transactional(readOnly = false)
    public void save(ErpStoreCredentials erpStoreCredentials) {
        super.save(erpStoreCredentials);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpStoreCredentials erpStoreCredentials) {
        super.delete(erpStoreCredentials);
    }

}