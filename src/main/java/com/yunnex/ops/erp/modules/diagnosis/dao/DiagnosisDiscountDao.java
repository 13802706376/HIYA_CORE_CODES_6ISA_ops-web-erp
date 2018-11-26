package com.yunnex.ops.erp.modules.diagnosis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisDiscount;

/**
 * 经营诊断的优惠内容DAO接口
 * @author yunnex
 * @version 2018-03-29
 */
@MyBatisDao
public interface DiagnosisDiscountDao extends CrudDao<DiagnosisDiscount> {

    List<DiagnosisDiscount> findBySplitId(String splitId);

    void deleteBySplitId(String splitId);

    void saveBatch(@Param("list") List<DiagnosisDiscount> list);

}