package com.yunnex.ops.erp.modules.diagnosis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisCardCouponsDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisCardCoupons;

/**
 * 卡券内容Service
 * @author yunnex
 * @version 2018-03-29
 */
@Service
public class DiagnosisCardCouponsService extends CrudService<DiagnosisCardCouponsDao, DiagnosisCardCoupons> {
    @Autowired
    private DiagnosisCardCouponsDao diagnosisCardCouponsDao;

    @Override
    @Transactional(readOnly = false)
    public void save(DiagnosisCardCoupons diagnosisCardCoupons) {
        super.save(diagnosisCardCoupons);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(DiagnosisCardCoupons diagnosisCardCoupons) {
        super.delete(diagnosisCardCoupons);
    }

    @Transactional(readOnly = false)
    public void batchDeleteByIds(List<String> removedCouponIds) {
        diagnosisCardCouponsDao.batchDeleteByIds(removedCouponIds);
    }

    public List<DiagnosisCardCoupons> findBySplitId(String splitId) {
        return diagnosisCardCouponsDao.findBySplitId(splitId);
    }

}
