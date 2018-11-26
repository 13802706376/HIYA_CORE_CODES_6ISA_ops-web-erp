package com.yunnex.ops.erp.modules.diagnosis.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisDiscountTypeConfig;

/**
 * 优惠形式配置表DAO接口
 * @author yunnex
 * @version 2018-03-29
 */
@MyBatisDao
public interface DiagnosisDiscountTypeConfigDao extends CrudDao<DiagnosisDiscountTypeConfig> {

}