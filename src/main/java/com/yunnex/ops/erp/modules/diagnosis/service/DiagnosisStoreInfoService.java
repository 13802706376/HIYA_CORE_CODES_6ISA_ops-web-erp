package com.yunnex.ops.erp.modules.diagnosis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisStoreInfoDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisStoreInfo;

/**
 * 经营诊断的门店信息Service
 * @author yunnex
 * @version 2018-03-29
 */
@Service
public class DiagnosisStoreInfoService extends CrudService<DiagnosisStoreInfoDao, DiagnosisStoreInfo> {
    @Autowired
    private DiagnosisStoreInfoDao diagnosisStoreInfoDao;

    @Override
    @Transactional(readOnly = false)
    public void save(DiagnosisStoreInfo diagnosisStoreInfo) {
        super.save(diagnosisStoreInfo);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(DiagnosisStoreInfo diagnosisStoreInfo) {
        super.delete(diagnosisStoreInfo);
    }

    public List<DiagnosisStoreInfo> getPromotionStores(String splitId) {
        return diagnosisStoreInfoDao.getPromotionStores(splitId);
    }

}
