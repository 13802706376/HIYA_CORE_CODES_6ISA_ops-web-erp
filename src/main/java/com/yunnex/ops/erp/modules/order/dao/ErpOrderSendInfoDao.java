package com.yunnex.ops.erp.modules.order.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSendInfo;

/**
 * 订单寄送信息DAO接口
 * @author yunnex
 * @version 2018-07-13
 */
@MyBatisDao
public interface ErpOrderSendInfoDao extends CrudDao<ErpOrderSendInfo> {

    ErpOrderSendInfo findByOrderNumber(String orderNumber);

    ErpOrderSendInfo findByYsOrderId(Long ysOrderId);

}
