package com.yunnex.ops.erp.modules.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.entity.ErpPromotionMaterialLog;

/**
 * 推广资料操作日志DAO接口
 * @author yunnex
 * @version 2018-05-09
 */
@MyBatisDao
public interface ErpPromotionMaterialLogDao extends CrudDao<ErpPromotionMaterialLog> {

    List<ErpPromotionMaterialLog> getPromotionMaterialLogs(@Param("splitId") String splitId,
                    @Param("promotionMaterialsId") String promotionMaterialsId);
	
}