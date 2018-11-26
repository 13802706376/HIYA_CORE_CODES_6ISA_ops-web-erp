package com.yunnex.ops.erp.modules.store.basic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.store.basic.api.ErpFriendCheckApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpWeiboCheckApi;
import com.yunnex.ops.erp.modules.store.basic.dto.PublicAccountAndWeiboDto;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.workflow.flow.dto.PayAuditStatusDto;

/**
 * 门店基本信息DAO接口
 * 
 * @author yunnex
 * @version 2017-12-09
 */
@MyBatisDao
public interface ErpStoreInfoDao extends CrudDao<ErpStoreInfo> {
    List<ErpStoreInfo> findAllListWhereShopId(@Param("del") String del, @Param("id") String id);

    int findCountWhereShopId(@Param("del") String del, @Param("id") String id);

    ErpStoreInfo findOnetoManyAll(@Param("del") String del, @Param("id") String id);

    ErpStoreInfo findShopInfoByStoreId(@Param("del") String del, @Param("id") String id);

    ErpStoreInfo findBasicInformation(@Param("del") String del, @Param("id") String id);

    ErpStoreInfo getIsmainStore(@Param("del") String del, @Param("id") String id, @Param("ismain") String ismain);

    Integer countIsMain(@Param("ismain") String ismain, @Param("id") String id, @Param("del") String del);

    List<ErpStoreInfo> findwhereshopidList(@Param("shopid") String shopid, @Param("del") String del);

    ErpStoreInfo findismain(@Param("shopid") String shopid, @Param("del") String del);

    ErpStoreInfo findzhangbeiaudit(@Param("id") String id, @Param("del") String del);

    Integer updateWhereShopId(@Param("shopid") String shopid);

    Integer updateWhereStoreId(@Param("id") String id);

    List<ErpStoreInfo> findwxpayaudit(@Param("shopid") String shopid, @Param("del") String del);

    /**
     * 某个商户的所有门店的所有微信进件审核状态
     * 
     * @param shopId
     * @return
     */
    List<PayAuditStatusDto> findWxpayAuditStatus(String shopId);

    /**
     * 某个商户的所有门店的所有银联进件审核状态
     * 
     * @param shopId
     * @return
     */
    List<PayAuditStatusDto> findUnionpayAuditStatus(String shopId);
    
    ErpStoreInfo wxpayaudit(@Param("id") String id,@Param("del") String del);
    
    ErpStoreInfo unionaudit(@Param("id") String id,@Param("del") String del);
    
    List<ErpStoreInfo> findunionaudit(@Param("shopid") String shopid,@Param("del") String del);
    
    List<ErpStoreInfo> syncwxpayaudit(@Param("shopid") String shopid);
    
    List<String> findTaskIdByStoreId(@Param("storeId") String storeId);

    /**
     * 获取门店的支付资质
     *
     * @param id
     * @return
     * @date 2017年12月21日
     * @author SunQ
     */
    String getPayQualifyById(@Param("id") String id);


    Integer findrolecount(@Param("userId") String userId, @Param("enname") String enname);

    ErpStoreInfo getStorePayInfo(String id);

    List<ErpFriendCheckApi> findfriendaudit(@Param("shopId") String shopId);

    ErpStoreInfo friendaudit(@Param("storeId") String storeId);

    List<ErpStoreInfo> findmomoaudit(@Param("shopId") String shopId);

    ErpStoreInfo momoaudit(@Param("storeId") String storeId);

    List<ErpWeiboCheckApi> findweiboaudit(@Param("shopId") String shopId);

    ErpStoreInfo weiboaudit(@Param("storeId") String storeId);

    List<ErpStoreInfo> getPromotionStores(String splitId);

    /*
     * ======================add by SunQ 2018-4-3 14:27:51 对于从OEM同步回来的门店信息,单独使用查询方法，防止与之前的业务发生冲突
     * start======================
     */
    /**
     * 获取从OEM同步回来的门店信息
     *
     * @param shopid
     * @param del
     * @param isMain
     * @return
     * @date 2018年4月3日
     * @author SunQ
     */
    List<ErpStoreInfo> findwhereshopidListForOEM(@Param("shopid") String shopid, @Param("del") String del, @Param("isMain") String isMain);
    
    /**
     * 微信支付显示list
     *
     * @param shopid
     * @param del
     * @return
     * @date 2018年4月3日
     * @author SunQ
     */
    List<ErpStoreInfo> findwxpayauditForOEM(@Param("shopid") String shopid, @Param("del") String del);
    
    /**
     * 银联支付显示list
     *
     * @param shopid
     * @param del
     * @return
     * @date 2018年4月3日
     * @author SunQ
     */
    List<ErpStoreInfo> findunionauditForOEM(@Param("shopid") String shopid,@Param("del") String del);
    
    /**
     * 获取从OEM同步的门店数量
     *
     * @param shopid
     * @param del
     * @return
     * @date 2018年4月3日
     * @author SunQ
     */
    Integer countStoreForOEM(@Param("shopid") String shopid,@Param("del") String del);
    /*
     * ======================add by SunQ 2018-4-3 14:27:51 对于从OEM同步回来的门店信息,单独使用查询方法，防止与之前的业务发生冲突
     * end======================
     */

    /**
     * 更新门店联系信息
     */
    void updateContactInfo(ErpStoreInfo storeInfo);

    /**
     * 公众号和微博信息
     */
    List<PublicAccountAndWeiboDto> findPublicAccountAndWeibo(@Param("ids") List<String> storeInfoIds);
    
    ErpStoreInfo getWeiboPromotionInfobyProcInsId(@Param("procInsId") String procInsId);
    
    ErpStoreInfo getFriendsPromotionInfobyProcInsId(@Param("procInsId") String procInsId);
    
    /**
     * 更新不开通银联标示
     * 
     * @param storeId
     * @param notOpenUnionpayFlag
     */
	void updateNotOpenUnionpayFlag(@Param("storeId")String storeId, @Param("notOpenUnionpayFlag")String notOpenUnionpayFlag);

    /**
     * 业务定义：充值门店推广状态
     * 
     * @date 2018年9月27日
     * @author R/Q
     */
    void resetExtension(@Param("storeId") String storeId);

}
