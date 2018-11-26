package com.yunnex.ops.erp.modules.diagnosis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisSecondAdImageDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisSecondAdImage;

/**
 * 第二层重点宣传图片Service
 * @author yunnex
 * @version 2018-04-08
 */
@Service
public class DiagnosisSecondAdImageService extends CrudService<DiagnosisSecondAdImageDao, DiagnosisSecondAdImage> {
    @Autowired
    private DiagnosisSecondAdImageDao diagnosisSecondAdImageDao;

    public DiagnosisSecondAdImage get(String id) {
        return super.get(id);
    }

    public List<DiagnosisSecondAdImage> findList(DiagnosisSecondAdImage diagnosisSecondAdImage) {
        return super.findList(diagnosisSecondAdImage);
    }

    public Page<DiagnosisSecondAdImage> findPage(Page<DiagnosisSecondAdImage> page, DiagnosisSecondAdImage diagnosisSecondAdImage) {
        return super.findPage(page, diagnosisSecondAdImage);
    }

    @Transactional(readOnly = false)
    public void save(DiagnosisSecondAdImage diagnosisSecondAdImage) {
        super.save(diagnosisSecondAdImage);
    }

    @Transactional(readOnly = false)
    public void delete(DiagnosisSecondAdImage diagnosisSecondAdImage) {
        super.delete(diagnosisSecondAdImage);
    }

    public List<DiagnosisSecondAdImage> findBySplitId(String splitId) {
        return diagnosisSecondAdImageDao.findBySplitId(splitId);
    }

    public List<DiagnosisSecondAdImage> findListByIds(List<String> removedSecondAdImgIds) {
        return diagnosisSecondAdImageDao.findListByIds(removedSecondAdImgIds);
    }

    @Transactional(readOnly = false)
    public int deleteBatchByIds(List<String> removedSecondAdImgIds) {
        return diagnosisSecondAdImageDao.deleteBatchByIds(removedSecondAdImgIds);
    }

}