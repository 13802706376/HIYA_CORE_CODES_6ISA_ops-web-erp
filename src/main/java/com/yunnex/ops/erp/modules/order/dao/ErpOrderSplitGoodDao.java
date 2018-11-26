package com.yunnex.ops.erp.modules.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;

/**
 * 
 * @author zjq
 * @date 2018年3月30日
 */
@MyBatisDao
public interface ErpOrderSplitGoodDao extends CrudDao<ErpOrderSplitGood> {

    List<ErpOrderSplitGood> findBySplitId(String splitId);
    
    int updateNum(@Param("id") String id, @Param("num") Integer num);

    void updateExposureById(ErpOrderSplitGood erpOrderSplitGood);

    public List<ErpOrderSplitGood> getErpOrderSplitGoodBySplitId(@Param("originalSplitId") String originalSplitId);

    public boolean updatePromotionBySplitId(ErpOrderSplitGood erpOrderSplitGood);

    public boolean updatePromotionByIds(@Param("ids") List<String> ids);

    /**
     * 业务定义：依据分单ID删除对应商品信息
     * 
     * @date 2018年8月31日
     * @author R/Q
     */
    void deleteBySplitId(@Param("splitId") String splitId);
}
