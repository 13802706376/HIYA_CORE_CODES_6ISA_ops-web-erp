package com.yunnex.ops.erp.modules.store.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;

/**
 * 微信支付开通资料DAO接口
 * @author yunnex
 * @version 2017-12-09
 */
@MyBatisDao
public interface ErpStorePayWeixinDao extends CrudDao<ErpStorePayWeixin> {
    ErpStorePayWeixin getPayAndBank(@Param("id") String id);

    List<ErpStorePayWeixin> findwxpayaudit(@Param("shopid") String shopid, @Param("registerno") String registerno, @Param("bankno") String bankno);

    ErpStorePayWeixin getBySplitId(String splitId);
}
