package com.yunnex.ops.erp.modules.store.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;

/**
 * 银联支付开通资料DAO接口
 * @author yunnex
 * @version 2017-12-09
 */
@MyBatisDao
public interface ErpStorePayUnionpayDao extends CrudDao<ErpStorePayUnionpay> {
	ErpStorePayUnionpay getPayAndBank(@Param("id")String id);
	
	//List<ErpStorePayUnionpay> findunionaudit(@Param("shopid")String shopid,@Param("auditStatus")Integer auditStatus,@Param("registerno")String registerno,@Param("bankno")String bankno);
	List<ErpStorePayUnionpay> findunionaudit(@Param("shopid")String shopid,@Param("registerno")String registerno,@Param("bankno")String bankno);
	
}