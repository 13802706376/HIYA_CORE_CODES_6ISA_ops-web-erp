package com.yunnex.ops.erp.modules.store.pay.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.store.pay.dao.ErpStoreBankDao;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStoreBank;

/**
 * 银行信息Service
 * 
 * @author yunnex
 * @version 2017-12-15
 */
@Service
public class ErpStoreBankService extends CrudService<ErpStoreBankDao, ErpStoreBank> {

    @Override
    @Transactional(readOnly = false)
    public void save(ErpStoreBank erpStoreBank) {
        super.save(erpStoreBank);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpStoreBank erpStoreBank) {
        super.delete(erpStoreBank);
    }

    public List<ErpStoreBank> findwhereshopid(String shopId) {
        return dao.findwhereshopid(shopId);
    }

}
