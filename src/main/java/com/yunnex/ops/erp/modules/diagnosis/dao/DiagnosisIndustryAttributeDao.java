package com.yunnex.ops.erp.modules.diagnosis.dao;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisIndustryAttribute;

/**
 * 行业属性DAO接口
 * @author yunnex
 * @version 2018-04-03
 */
@MyBatisDao
public interface DiagnosisIndustryAttributeDao extends CrudDao<DiagnosisIndustryAttribute> {

    List<DiagnosisIndustryAttribute> findBySplitId(String splitId);

    List<DiagnosisIndustryAttribute> findByPid(String pid);

}