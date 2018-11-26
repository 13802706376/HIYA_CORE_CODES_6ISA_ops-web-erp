package com.yunnex.ops.erp.modules.store.advertiser.dao;

import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.store.advertiser.dto.ShopWeiboResponseDto;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;

/**
 * 微博广告主开通资料DAO接口
 * 
 * @author yunnex
 * @version 2017-12-09
 */
@MyBatisDao
public interface ErpStoreAdvertiserWeiboDao extends CrudDao<ErpStoreAdvertiserWeibo> {
    
    /**
     * 获取门店的微博开户信息
     *
     * @param storeId
     * @return
     * @date 2018年1月11日
     * @author SunQ
     */
    ErpStoreAdvertiserWeibo getByStoreId(String storeId);

    /**
     * 更新微博广告主开通资料表
     *
     * @param id
     * @param openOrtrans 新开户 /转户
     * @return
     * @date 2018年4月10日
     * @author zjq
     */
    int updateOpenOrTrans(@Param("id") String id, @Param("openOrTrans") String openOrtrans);

    /**
     * 查找商户下的所有微博账号（通过微博账号和微博Uid去重，见{@link ShopWeiboResponseDto}）
     * 
     * @param zhangbeiId
     * @return
     */
    Set<ShopWeiboResponseDto> findByZhangbeiId(String zhangbeiId);
    
    ErpStoreAdvertiserWeibo getByProcInsId(String procInsId);

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
    void syncAuditStatusByAccount(ErpStoreAdvertiserWeibo paramObj);
}
