package com.yunnex.ops.erp.modules.diagnosis.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisIndustryAttributeDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisIndustryAttribute;

/**
 * 行业属性Service
 * @author yunnex
 * @version 2018-04-03
 */
@Service
public class DiagnosisIndustryAttributeService extends CrudService<DiagnosisIndustryAttributeDao, DiagnosisIndustryAttribute> {

    @Override
    @Transactional(readOnly = false)
    public void save(DiagnosisIndustryAttribute diagnosisIndustryAttribute) {
        super.save(diagnosisIndustryAttribute);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(DiagnosisIndustryAttribute diagnosisIndustryAttribute) {
        super.delete(diagnosisIndustryAttribute);
    }

    public List<DiagnosisIndustryAttribute> findByPid(String pid) {
        return dao.findByPid(pid);
    }

}
