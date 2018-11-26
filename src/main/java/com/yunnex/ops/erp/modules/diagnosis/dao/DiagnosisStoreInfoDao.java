package com.yunnex.ops.erp.modules.diagnosis.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisStoreInfo;

import java.util.List;

/**
 * 经营诊断的门店信息DAO接口
 * @author yunnex
 * @version 2018-03-29
 */
@MyBatisDao
public interface DiagnosisStoreInfoDao extends CrudDao<DiagnosisStoreInfo> {

    /**
     * 获取指定分单下推广的门店
     */
    List<DiagnosisStoreInfo> getPromotionStores(String splitId);

    /**
     * 获取指定的门店
     */
    DiagnosisStoreInfo findPromotionStore(String storeInfoId);

}