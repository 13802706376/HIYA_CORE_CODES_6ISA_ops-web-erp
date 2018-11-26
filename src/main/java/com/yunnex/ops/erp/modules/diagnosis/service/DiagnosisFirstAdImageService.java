package com.yunnex.ops.erp.modules.diagnosis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisFirstAdImageDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisFirstAdImage;

/**
 * 第一层重点宣传图片Service
 * @author yunnex
 * @version 2018-04-08
 */
@Service
public class DiagnosisFirstAdImageService extends CrudService<DiagnosisFirstAdImageDao, DiagnosisFirstAdImage> {
    @Autowired
    private DiagnosisFirstAdImageDao diagnosisFirstAdImageDao;

    public DiagnosisFirstAdImage get(String id) {
        return super.get(id);
    }

    public List<DiagnosisFirstAdImage> findList(DiagnosisFirstAdImage diagnosisFirstAdImage) {
        return super.findList(diagnosisFirstAdImage);
    }

    public Page<DiagnosisFirstAdImage> findPage(Page<DiagnosisFirstAdImage> page, DiagnosisFirstAdImage diagnosisFirstAdImage) {
        return super.findPage(page, diagnosisFirstAdImage);
    }

    @Transactional(readOnly = false)
    public void save(DiagnosisFirstAdImage diagnosisFirstAdImage) {
        super.save(diagnosisFirstAdImage);
    }

    @Transactional(readOnly = false)
    public void delete(DiagnosisFirstAdImage diagnosisFirstAdImage) {
        super.delete(diagnosisFirstAdImage);
    }

    public List<DiagnosisFirstAdImage> findBySplitId(String splitId) {
        return diagnosisFirstAdImageDao.findBySplitId(splitId);
    }

    public List<DiagnosisFirstAdImage> findListByIds(List<String> removedFirstAdImgIds) {
        return diagnosisFirstAdImageDao.findListByIds(removedFirstAdImgIds);
    }

    @Transactional(readOnly = false)
    public int deleteBatchByIds(List<String> removedFirstAdImgIds) {
        return diagnosisFirstAdImageDao.deleteBatchByIds(removedFirstAdImgIds);
    }

    public List<DiagnosisFirstAdImage> findBySplitIdAndType(String splitId, String promotionChannel) {
        return diagnosisFirstAdImageDao.findBySplitIdAndType(splitId, promotionChannel);
    }

}