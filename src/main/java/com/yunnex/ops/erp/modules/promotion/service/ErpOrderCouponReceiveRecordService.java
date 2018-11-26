package com.yunnex.ops.erp.modules.promotion.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderCouponOutput;
import com.yunnex.ops.erp.modules.promotion.dao.ErpOrderCouponReceiveRecordDao;
import com.yunnex.ops.erp.modules.promotion.entity.ErpOrderCouponReceiveRecord;

/**
 * 卡券领取记录Service
 * 
 * @author yunnex
 * @version 2018-05-22
 */
@Service
public class ErpOrderCouponReceiveRecordService extends CrudService<ErpOrderCouponReceiveRecordDao, ErpOrderCouponReceiveRecord> {

    @Override
	public ErpOrderCouponReceiveRecord get(String id) {
		return super.get(id);
	}

    /**
     * 业务定义：查询分单对应卡券信息
     * 
     * @date 2018年5月22日
     * @author R/Q
     */
    public List<ErpOrderCouponOutput> queryCouponData(String splitOrderId) {
        return super.dao.queryCouponData(splitOrderId);
    }

    @Override
	@Transactional(readOnly = false)
	public void save(ErpOrderCouponReceiveRecord erpOrderCouponReceiveRecord) {
		super.save(erpOrderCouponReceiveRecord);
	}
	
    @Override
	@Transactional(readOnly = false)
	public void delete(ErpOrderCouponReceiveRecord erpOrderCouponReceiveRecord) {
		super.delete(erpOrderCouponReceiveRecord);
	}
}