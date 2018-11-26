package com.yunnex.ops.erp.modules.promotion.dao;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderCouponOutput;
import com.yunnex.ops.erp.modules.promotion.entity.ErpOrderCouponReceiveRecord;

/**
 * 卡券领取记录DAO接口
 * 
 * @author yunnex
 * @version 2018-05-22
 */
@MyBatisDao
public interface ErpOrderCouponReceiveRecordDao extends CrudDao<ErpOrderCouponReceiveRecord> {

    /**
     * 业务定义：查询分单对应卡券信息
     * 
     * @date 2018年5月22日
     * @author R/Q
     */
    List<ErpOrderCouponOutput> queryCouponData(String splitOrderId);
	
}