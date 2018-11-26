package com.yunnex.ops.erp.modules.material.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialContent;

/**
 * 订单物料内容DAO接口
 * @author yunnex
 * @version 2018-07-13
 */
@MyBatisDao
public interface ErpOrderMaterialContentDao extends CrudDao<ErpOrderMaterialContent> {

    List<ErpOrderMaterialContent> findByOrderNumber(@Param("orderNumber") String orderNumber, @Param("delFlag") String delFlag);

    List<ErpOrderMaterialContent> findByYsOrderId(Long ysOrderId);

    /**
     * 物理删除指定订单下的所有物料内容
     * 
     * @param orderNumber
     * @return
     */
    int deleteByOrderNumber(String orderNumber);

    int deleteByYsOrderId(Long ysOrderId);

    /**
     * 批量保存
     * 
     * @param list
     * @return
     */
    int saveBatch(@Param("list") List<ErpOrderMaterialContent> list);
}
