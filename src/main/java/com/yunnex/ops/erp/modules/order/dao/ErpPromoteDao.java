package com.yunnex.ops.erp.modules.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.entity.PromoteOrderSplit;

@MyBatisDao
public interface ErpPromoteDao extends CrudDao<PromoteOrderSplit> {
	
	List<PromoteOrderSplit> findPromoteOrder(@Param("userId") String userId,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("order_type") String order_type);
	List<PromoteOrderSplit> findPromoteSplit(@Param("userId") String userId,@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("order_type") String order_type);
}
