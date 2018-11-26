package com.yunnex.ops.erp.modules.diagnosis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisStoreBusinessHour;

/**
 * 经营诊断的门店的营业时间DAO接口
 * @author yunnex
 * @version 2018-03-29
 */
@MyBatisDao
public interface DiagnosisStoreBusinessHourDao extends CrudDao<DiagnosisStoreBusinessHour> {

    List<DiagnosisStoreBusinessHour> findByStoreInfoId(String storeInfoId);

    void deleteByStoreInfoId(String storeInfoId);

    void saveBatch(@Param("list") List<DiagnosisStoreBusinessHour> list);

}