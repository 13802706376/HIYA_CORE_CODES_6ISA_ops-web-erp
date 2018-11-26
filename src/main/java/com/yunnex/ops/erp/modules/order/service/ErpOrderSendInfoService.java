package com.yunnex.ops.erp.modules.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSendInfo;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSendInfoDao;

/**
 * 订单寄送信息Service
 * @author yunnex
 * @version 2018-07-13
 */
@Service
public class ErpOrderSendInfoService extends CrudService<ErpOrderSendInfoDao, ErpOrderSendInfo> {

    @Override
	@Transactional(readOnly = false)
	public void save(ErpOrderSendInfo erpOrderSendInfo) {
		super.save(erpOrderSendInfo);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(ErpOrderSendInfo erpOrderSendInfo) {
		super.delete(erpOrderSendInfo);
	}

    public ErpOrderSendInfo findByOrderNumber(String orderNumber) {
        return dao.findByOrderNumber(orderNumber);
    }

    public ErpOrderSendInfo findByYsOrderId(Long ysOrderId) {
        return dao.findByYsOrderId(ysOrderId);
    }
}
