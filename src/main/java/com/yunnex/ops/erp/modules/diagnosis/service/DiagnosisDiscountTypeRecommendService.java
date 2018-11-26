package com.yunnex.ops.erp.modules.diagnosis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisDiscountTypeRecommendDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisDiscountTypeRecommend;

/**
 * 优惠形式推荐表Service
 * @author yunnex
 * @version 2018-03-29
 */
@Service
public class DiagnosisDiscountTypeRecommendService extends CrudService<DiagnosisDiscountTypeRecommendDao, DiagnosisDiscountTypeRecommend> {

    @Override
    @Transactional(readOnly = false)
    public void save(DiagnosisDiscountTypeRecommend diagnosisDiscountTypeRecommend) {
        super.save(diagnosisDiscountTypeRecommend);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(DiagnosisDiscountTypeRecommend diagnosisDiscountTypeRecommend) {
        super.delete(diagnosisDiscountTypeRecommend);
    }

}
