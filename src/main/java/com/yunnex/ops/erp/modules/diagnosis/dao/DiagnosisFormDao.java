package com.yunnex.ops.erp.modules.diagnosis.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisForm;

/**
 * 经营诊断营销策划表单DAO接口
 * @author yunnex
 * @version 2018-03-29
 */
@MyBatisDao
public interface DiagnosisFormDao extends CrudDao<DiagnosisForm> {

    DiagnosisForm findBySplitId(String splitId);

    DiagnosisForm findBySplitIdWithCreaterName(String splitId);

    DiagnosisForm getDiagnosisFormBySplitId(String splitId);

}