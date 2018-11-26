package com.yunnex.ops.erp.modules.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderMaterialSyncLogDao;
import com.yunnex.ops.erp.modules.order.dto.OrderMaterialSyncLogRequestDto;
import com.yunnex.ops.erp.modules.order.dto.OrderMaterialSyncLogResponseDto;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderMaterialSyncLog;

/**
 * 订单物料同步日志Service
 * @author yunnex
 * @version 2018-07-02
 */
@Service
public class ErpOrderMaterialSyncLogService extends CrudService<ErpOrderMaterialSyncLogDao, ErpOrderMaterialSyncLog> {

    @Override
	@Transactional(readOnly = false)
	public void save(ErpOrderMaterialSyncLog erpOrderMaterialSyncLog) {
		super.save(erpOrderMaterialSyncLog);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(ErpOrderMaterialSyncLog erpOrderMaterialSyncLog) {
		super.delete(erpOrderMaterialSyncLog);
	}

    public Page<OrderMaterialSyncLogResponseDto> findByPage(OrderMaterialSyncLogRequestDto requestDto) {
        return requestDto.getPage().setList(dao.findByPage(requestDto));
    }

    public ErpOrderMaterialSyncLog findByYsOrderId(Long ysOrderId) {
        return dao.findByYsOrderId(ysOrderId);
    }

    @Transactional
    public int updateStatus(ErpOrderMaterialSyncLog log, String exStatus) {
        return dao.updateStatus(log, exStatus);
    }
}
