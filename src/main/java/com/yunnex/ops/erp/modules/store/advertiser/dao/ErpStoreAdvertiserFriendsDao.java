package com.yunnex.ops.erp.modules.store.advertiser.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;

/**
 * 朋友圈广告主开通资料DAO接口
 * 
 * @author yunnex
 * @version 2017-12-09
 */
@MyBatisDao
public interface ErpStoreAdvertiserFriendsDao extends CrudDao<ErpStoreAdvertiserFriends> {
    
    /**
     * 通过门店获取朋友圈开户信息
     *
     * @param storeId
     * @return
     * @date 2018年1月11日
     * @author SunQ
     */
    ErpStoreAdvertiserFriends getByStoreId(String storeId);
    
    ErpStoreAdvertiserFriends getByProcInsId(String procInsId);

    /**
     * 业务定义：查询同商户下其他门店同账号开户状态
     * 
     * @date 2018年5月18日
     * @author R/Q
     */
    Integer queryRecordDataAuditStatus(ErpStoreInfo paramObj);

    /**
     * 业务定义：同步同商户下同账号的广告主审核状态
     * 
     * @date 2018年5月22日
     * @author R/Q
     */
    void syncAuditStatusByAccount(ErpStoreAdvertiserFriends paramObj);
    
    
}