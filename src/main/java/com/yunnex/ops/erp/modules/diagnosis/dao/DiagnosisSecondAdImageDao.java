package com.yunnex.ops.erp.modules.diagnosis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisSecondAdImage;

/**
 * 第二层重点宣传图片DAO接口
 * @author yunnex
 * @version 2018-04-08
 */
@MyBatisDao
public interface DiagnosisSecondAdImageDao extends CrudDao<DiagnosisSecondAdImage> {

    List<DiagnosisSecondAdImage> findBySplitId(String splitId);

    List<DiagnosisSecondAdImage> findListByIds(@Param("ids") List<String> ids);

    int deleteBatchByIds(@Param("ids") List<String> ids);

}