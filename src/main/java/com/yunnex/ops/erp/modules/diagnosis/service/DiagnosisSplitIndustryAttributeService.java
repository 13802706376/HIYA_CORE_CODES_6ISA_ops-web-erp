package com.yunnex.ops.erp.modules.diagnosis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisSplitIndustryAttributeDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisSplitIndustryAttribute;

/**
 * 分单行业属性关联表Service
 * @author yunnex
 * @version 2018-03-29
 */
@Service
public class DiagnosisSplitIndustryAttributeService extends CrudService<DiagnosisSplitIndustryAttributeDao, DiagnosisSplitIndustryAttribute> {

    @Override
    @Transactional(readOnly = false)
    public void save(DiagnosisSplitIndustryAttribute diagnosisSplitIndustryAttribute) {
        super.save(diagnosisSplitIndustryAttribute);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(DiagnosisSplitIndustryAttribute diagnosisSplitIndustryAttribute) {
        super.delete(diagnosisSplitIndustryAttribute);
    }

}
