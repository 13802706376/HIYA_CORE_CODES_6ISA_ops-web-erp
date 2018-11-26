package com.yunnex.ops.erp.modules.good.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodInfo;

/**
 * 商品信息管理DAO接口
 * @author Frank
 * @version 2017-10-21
 */
@MyBatisDao
public interface ErpGoodInfoDao extends CrudDao<ErpGoodInfo> {

    public int updateCategoryId(ErpGoodInfo erpGoodInfo);

    public void batchDelete(@Param("ids") List<String> ids);

    public ErpGoodInfo getDetail(@Param("id") String id);

    public int updateDetail(ErpGoodInfo erpGoodInfo);
    
    List<ErpGoodInfo> findwherecategory();

    ErpGoodInfo findGoodAndCategoryById(String id);

}
