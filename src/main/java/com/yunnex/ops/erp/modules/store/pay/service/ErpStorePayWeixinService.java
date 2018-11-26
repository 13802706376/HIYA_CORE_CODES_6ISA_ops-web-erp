package com.yunnex.ops.erp.modules.store.pay.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.store.pay.dao.ErpStorePayWeixinDao;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;

/**
 * 微信支付开通资料Service
 * @author yunnex
 * @version 2017-12-09
 */
@Service
public class ErpStorePayWeixinService extends CrudService<ErpStorePayWeixinDao, ErpStorePayWeixin> {

    @Override
    @Transactional(readOnly = false)
    public void save(ErpStorePayWeixin erpStorePayWeixin) {
        super.save(erpStorePayWeixin);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpStorePayWeixin erpStorePayWeixin) {
        super.delete(erpStorePayWeixin);
    }

    public ErpStorePayWeixin getPayAndBank(String id) {
        return dao.getPayAndBank(id);
    }

    public List<ErpStorePayWeixin> findwxpayaudit(String shopid, String registerno, String bankno) {
        return dao.findwxpayaudit(shopid, registerno, bankno);
    }

    public ErpStorePayWeixin getBySplitId(String splitId) {
        return dao.getBySplitId(splitId);
    }

}