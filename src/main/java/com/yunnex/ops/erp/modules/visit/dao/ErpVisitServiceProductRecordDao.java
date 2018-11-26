package com.yunnex.ops.erp.modules.visit.dao;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceProductRecord;

/**
 * 硬件产品交付DAO接口
 * @author hanhan
 * @version 2018-07-09
 */
@MyBatisDao
public interface ErpVisitServiceProductRecordDao extends CrudDao<ErpVisitServiceProductRecord> {
  public List<ErpVisitServiceProductRecord> getProductInfoByVisitServiceId(String visitServiceInfoId);
  
  void  deleteVisitProductRecordByVisitId (String visitServiceInfoId);
}