package com.yunnex.ops.erp.modules.diagnosis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisSplitIndustryAttribute;

/**
 * 分单行业属性关联表DAO接口
 * @author yunnex
 * @version 2018-03-29
 */
@MyBatisDao
public interface DiagnosisSplitIndustryAttributeDao extends CrudDao<DiagnosisSplitIndustryAttribute> {

    void deleteBySplitId(String splitId);

    void saveBatch(@Param("list") List<DiagnosisSplitIndustryAttribute> list);

    List<DiagnosisSplitIndustryAttribute> findBySplitId(String splitId);

}