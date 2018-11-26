package com.yunnex.ops.erp.modules.qualify.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.qualify.entity.ErpShopPayQualify;

/**
 * 商户支付资质DAO接口
 * @author huanghaidong
 * @version 2017-10-24
 */
@MyBatisDao
public interface ErpShopPayQualifyDao extends CrudDao<ErpShopPayQualify> {

    List<String> findPayQualifyList(@Param("shopId") String shopId);
    
    /**
     * 获取商户提交进件资质的数量
     * @param shopId
     * @param payValue
     * @return
     */
    int countByShopIdAndPaytype(@Param("shopId") String shopId, @Param("payValue") String payValue);
}