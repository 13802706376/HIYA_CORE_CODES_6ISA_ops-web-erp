package com.yunnex.ops.erp.modules.diagnosis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisCardCoupons;

/**
 * 卡券内容DAO接口
 * @author yunnex
 * @version 2018-03-29
 */
@MyBatisDao
public interface DiagnosisCardCouponsDao extends CrudDao<DiagnosisCardCoupons> {

    List<DiagnosisCardCoupons> findBySplitId(String splitId);

    void batchDeleteByIds(@Param("ids") List<String> ids);

}