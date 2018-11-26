package com.yunnex.ops.erp.modules.diagnosis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisDiscountTypeRecommend;

/**
 * 优惠形式推荐表DAO接口
 * @author yunnex
 * @version 2018-03-29
 */
@MyBatisDao
public interface DiagnosisDiscountTypeRecommendDao extends CrudDao<DiagnosisDiscountTypeRecommend> {
    /**
     * 获取优惠形式推荐推荐列表
     * 
     * @return
     */
    List<DiagnosisDiscountTypeRecommend> findRecommendList(@Param("splitId") String splitId);

    /**
     * 获取按优惠形式id汇总后的数据
     * 
     * @param industryId
     * @param diagnosisActivitingIdList
     * @return
     */
    List<DiagnosisDiscountTypeRecommend> findRecommendSummaryList(@Param("industryId") String industryId,
                    @Param("list") List<String> diagnosisActivitingIdList);

    void deleteBySplitId(String splitId);

    void saveBatch(@Param("list") List<DiagnosisDiscountTypeRecommend> list);
}