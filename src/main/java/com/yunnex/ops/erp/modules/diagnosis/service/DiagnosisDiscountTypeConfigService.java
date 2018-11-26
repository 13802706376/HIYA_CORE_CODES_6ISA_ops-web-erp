package com.yunnex.ops.erp.modules.diagnosis.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.diagnosis.constant.DiagnosisConstant;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisDiscountTypeConfigDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisDiscountTypeConfig;
import com.yunnex.ops.erp.modules.sys.entity.Dict;
import com.yunnex.ops.erp.modules.sys.utils.DictUtils;

/**
 * 优惠形式配置表Service
 * 
 * @author yunnex
 * @version 2018-03-29
 */
@Service
public class DiagnosisDiscountTypeConfigService extends CrudService<DiagnosisDiscountTypeConfigDao, DiagnosisDiscountTypeConfig> {

    /**
     * 
     * @param page 分页对象
     * @param diagnosisDiscountTypeConfig
     * @return
     */
    @Override
    public Page<DiagnosisDiscountTypeConfig> findPage(Page<DiagnosisDiscountTypeConfig> page,
                    DiagnosisDiscountTypeConfig diagnosisDiscountTypeConfig) {
        Page<DiagnosisDiscountTypeConfig> pageData = super.findPage(page, diagnosisDiscountTypeConfig);
        if (pageData == null) {
            return null;
        }

        List<DiagnosisDiscountTypeConfig> configs = pageData.getList();
        if (CollectionUtils.isNotEmpty(configs)) {
            List<Dict> activityRequirements = DictUtils.getDictList(DiagnosisConstant.ACTIVITY_REQUIREMENT_DICT_TYPE);
            List<Dict> industryAttributes = DictUtils.getDictList(DiagnosisConstant.INDUSTRY_ATTRIBUTE_DICT_TYPE);
            List<Dict> discountTypes = DictUtils.getDictList(DiagnosisConstant.DISCOUNT_TYPE_DICT_TYPE);

            for (DiagnosisDiscountTypeConfig config : configs) {
                config.setActivityRequirementName(getDictById(activityRequirements, config.getActivityRequirementId()).getLabel());
                config.setIndustryAttributeName(getDictById(industryAttributes, config.getIndustryAttributeId()).getLabel());
                config.setDiscountTypeName(getDictById(discountTypes, config.getDiscountTypeId()).getLabel());
            }
        }
        return pageData;
    }

    private static Dict getDictById(List<Dict> dicts, String id) {
        Dict dict = new Dict();
        if (CollectionUtils.isEmpty(dicts) || StringUtils.isBlank(id)) {
            return dict;
        }

        for (Dict d : dicts) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        return dict;
    }

    @Override
    @Transactional(readOnly = false)
    public void save(DiagnosisDiscountTypeConfig diagnosisDiscountTypeConfig) {
        super.save(diagnosisDiscountTypeConfig);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(DiagnosisDiscountTypeConfig diagnosisDiscountTypeConfig) {
        super.delete(diagnosisDiscountTypeConfig);
    }

}
