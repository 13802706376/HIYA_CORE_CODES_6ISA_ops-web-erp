package com.yunnex.ops.erp.modules.diagnosis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisFirstAdImage;

/**
 * 第一层重点宣传图片DAO接口
 * @author yunnex
 * @version 2018-04-08
 */
@MyBatisDao
public interface DiagnosisFirstAdImageDao extends CrudDao<DiagnosisFirstAdImage> {

    List<DiagnosisFirstAdImage> findBySplitId(String splitId);

    List<DiagnosisFirstAdImage> findListByIds(@Param("ids") List<String> ids);

    int deleteBatchByIds(@Param("ids") List<String> ids);

    List<DiagnosisFirstAdImage> findBySplitIdAndType(@Param("splitId") String splitId, @Param("type") String promotionChannel);

}