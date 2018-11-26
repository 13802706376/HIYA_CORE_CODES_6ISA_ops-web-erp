package com.yunnex.ops.erp.modules.store.pay.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.store.pay.dao.ErpStorePayUnionpayDao;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;

/**
 * 银联支付开通资料Service
 * @author yunnex
 * @version 2017-12-09
 */
@Service
public class ErpStorePayUnionpayService extends CrudService<ErpStorePayUnionpayDao, ErpStorePayUnionpay> {

    @Override
    @Transactional(readOnly = false)
    public void save(ErpStorePayUnionpay erpStorePayUnionpay) {
        super.save(erpStorePayUnionpay);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpStorePayUnionpay erpStorePayUnionpay) {
        super.delete(erpStorePayUnionpay);
    }

    public ErpStorePayUnionpay getPayAndBank(String id) {
        return dao.getPayAndBank(id);
    }

    public List<ErpStorePayUnionpay> findunionaudit(String shopid, String registerno, String bankno) {
        return dao.findunionaudit(shopid, registerno, bankno);
    }

}
