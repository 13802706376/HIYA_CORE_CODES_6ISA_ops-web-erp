package com.yunnex.ops.erp.modules.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.dto.OrderMaterialSyncLogRequestDto;
import com.yunnex.ops.erp.modules.order.dto.OrderMaterialSyncLogResponseDto;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderMaterialSyncLog;

/**
 * 订单物料同步日志DAO接口
 *
 * @author yunnex
 * @version 2018-07-02
 */
@MyBatisDao
public interface ErpOrderMaterialSyncLogDao extends CrudDao<ErpOrderMaterialSyncLog> {

    List<OrderMaterialSyncLogResponseDto> findByPage(OrderMaterialSyncLogRequestDto requestDto);

    ErpOrderMaterialSyncLog findByYsOrderId(Long ysOrderId);

    /**
     * 按条件修改异常状态
     * @param log
     * @param exStatus
     * @return
     */
    int updateStatus(@Param("log") ErpOrderMaterialSyncLog log, @Param("exStatus") String exStatus);
}
