package com.yunnex.ops.erp.modules.store.advertiser.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.store.advertiser.dao.ErpStoreAdvertiserFriendsDao;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;

/**
 * 朋友圈广告主开通资料Service
 * 
 * @author yunnex
 * @version 2017-12-09
 */
@Service
public class ErpStoreAdvertiserFriendsService extends CrudService<ErpStoreAdvertiserFriendsDao, ErpStoreAdvertiserFriends> {

    @Autowired
    private ErpStoreAdvertiserFriendsDao erpStoreAdvertiserFriendsDao;

    @Override
	@Transactional(readOnly = false)
	public void save(ErpStoreAdvertiserFriends erpStoreAdvertiserFriends) {
		super.save(erpStoreAdvertiserFriends);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(ErpStoreAdvertiserFriends erpStoreAdvertiserFriends) {
		super.delete(erpStoreAdvertiserFriends);
	}
	
	public ErpStoreAdvertiserFriends getByStoreId(String storeId) {
	    return erpStoreAdvertiserFriendsDao.getByStoreId(storeId);
	}
	
	
	
	public boolean isPromoteAccountFinish(String storeId) {
		
	    return true;
	}

    public void update(ErpStoreAdvertiserFriends entity) {
        erpStoreAdvertiserFriendsDao.update(entity);
    }
    public ErpStoreAdvertiserFriends getByProcInsId(String procInsId) {
        
        return erpStoreAdvertiserFriendsDao.getByProcInsId(procInsId);
    }
    
    /**
     * 业务定义：查询同商户下其他门店同账号开户状态
     * 
     * @date 2018年5月18日
     * @author R/Q
     */
    public Integer queryRecordDataAuditStatus(ErpStoreInfo paramObj) {
        return erpStoreAdvertiserFriendsDao.queryRecordDataAuditStatus(paramObj);
    }

    /**
     * 业务定义：同步同商户下同账号的广告主审核状态
     * 
     * @date 2018年5月22日
     * @author R/Q
     */
    public void syncAuditStatusByAccount(ErpStoreAdvertiserFriends paramObj) {
        erpStoreAdvertiserFriendsDao.syncAuditStatusByAccount(paramObj);
    }

}