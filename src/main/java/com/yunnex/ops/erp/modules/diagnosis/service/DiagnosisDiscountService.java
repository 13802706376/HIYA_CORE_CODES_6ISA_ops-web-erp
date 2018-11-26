package com.yunnex.ops.erp.modules.diagnosis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisDiscountDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisDiscount;

/**
 * 经营诊断的优惠内容Service
 * @author yunnex
 * @version 2018-03-29
 */
@Service
public class DiagnosisDiscountService extends CrudService<DiagnosisDiscountDao, DiagnosisDiscount> {

    @Override
    @Transactional(readOnly = false)
    public void save(DiagnosisDiscount diagnosisDiscount) {
        super.save(diagnosisDiscount);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(DiagnosisDiscount diagnosisDiscount) {
        super.delete(diagnosisDiscount);
    }

}
