package com.yunnex.ops.erp.modules.good.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodService;

/**
 * 商品服务DAO接口
 * @author yunnex
 * @version 2018-05-29
 */
@MyBatisDao
public interface ErpGoodServiceDao extends CrudDao<ErpGoodService> {

    List<ErpGoodService> findServiceList(@Param("goodId") String goodId, @Param("isPackage") String isPackage);

    List<ErpGoodService> findServiceListByBatch(@Param("goodIds") List<String> goodId, @Param("isPackage") String isPackage);

    void deleteByGoodId(@Param("goodId") String goodId);
}