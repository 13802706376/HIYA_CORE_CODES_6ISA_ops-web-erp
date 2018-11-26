package com.yunnex.ops.erp.modules.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderCouponOutput;

/**
 * 卡券输出DAO接口
 * @author yunnex
 * @version 2018-05-08
 */
@MyBatisDao
public interface ErpOrderCouponOutputDao extends CrudDao<ErpOrderCouponOutput> {

    List<ErpOrderCouponOutput> findListBySplitId(String splitId);

    void batchDelete(@Param("ids") List<String> ids);
	
}