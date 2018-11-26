package com.yunnex.ops.erp.modules.store.advertiser.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.store.advertiser.dao.ErpStoreAdvertiserWeiboDao;
import com.yunnex.ops.erp.modules.store.advertiser.dto.ShopWeiboResponseDto;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;

/**
 * 微博广告主开通资料Service
 * 
 * @author yunnex
 * @version 2017-12-09
 */
@Service
public class ErpStoreAdvertiserWeiboService extends CrudService<ErpStoreAdvertiserWeiboDao, ErpStoreAdvertiserWeibo> {

    @Autowired
    private ErpStoreAdvertiserWeiboDao erpStoreAdvertiserWeiboDao;

    @Override
    @Transactional(readOnly = false)
    public void save(ErpStoreAdvertiserWeibo erpStoreAdvertiserWeibo) {
        super.save(erpStoreAdvertiserWeibo);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpStoreAdvertiserWeibo erpStoreAdvertiserWeibo) {
        super.delete(erpStoreAdvertiserWeibo);
    }

    public ErpStoreAdvertiserWeibo getByStoreId(String storeId) {
        return erpStoreAdvertiserWeiboDao.getByStoreId(storeId);
    }

    public void updateOpenOrTrans(String weiboId, String openOrTrans) {
        erpStoreAdvertiserWeiboDao.updateOpenOrTrans(weiboId, openOrTrans);
    }

    public Set<ShopWeiboResponseDto> findByZhangbeiId(String zhangbeiId) {
        return dao.findByZhangbeiId(zhangbeiId);
    }

    public void update(ErpStoreAdvertiserWeibo entity) {
        dao.update(entity);
    }
    
    public ErpStoreAdvertiserWeibo getByProcInsId(String procInsId) {
        return erpStoreAdvertiserWeiboDao.getByProcInsId(procInsId);
    }

    /**
     * 业务定义：查询同商户下其他门店同账号开户状态
     * 
     * @date 2018年5月18日
     * @author R/Q
     */
    public Integer queryRecordDataAuditStatus(ErpStoreInfo paramObj) {
        return erpStoreAdvertiserWeiboDao.queryRecordDataAuditStatus(paramObj);
    }

    /**
     * 业务定义：同步同商户下同账号的广告主审核状态
     * 
     * @date 2018年5月22日
     * @author R/Q
     */
    public void syncAuditStatusByAccount(ErpStoreAdvertiserWeibo paramObj) {
        erpStoreAdvertiserWeiboDao.syncAuditStatusByAccount(paramObj);
    }
}
