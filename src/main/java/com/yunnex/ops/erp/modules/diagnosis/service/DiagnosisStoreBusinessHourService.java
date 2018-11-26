package com.yunnex.ops.erp.modules.diagnosis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisStoreBusinessHourDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisStoreBusinessHour;

/**
 * 经营诊断的门店的营业时间Service
 * @author yunnex
 * @version 2018-03-29
 */
@Service
public class DiagnosisStoreBusinessHourService extends CrudService<DiagnosisStoreBusinessHourDao, DiagnosisStoreBusinessHour> {

    @Override
    @Transactional(readOnly = false)
    public void save(DiagnosisStoreBusinessHour diagnosisStoreBusinessHour) {
        super.save(diagnosisStoreBusinessHour);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(DiagnosisStoreBusinessHour diagnosisStoreBusinessHour) {
        super.delete(diagnosisStoreBusinessHour);
    }

}
