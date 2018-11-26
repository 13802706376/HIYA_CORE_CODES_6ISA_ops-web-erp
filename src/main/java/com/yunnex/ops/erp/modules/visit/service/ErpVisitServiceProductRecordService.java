package com.yunnex.ops.erp.modules.visit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.visit.entity.ErpVisitServiceProductRecord;
import com.yunnex.ops.erp.modules.visit.dao.ErpVisitServiceProductRecordDao;

/**
 * 硬件产品交付Service
 * @author hanhan
 * @version 2018-07-09
 */
@Service
public class ErpVisitServiceProductRecordService extends CrudService<ErpVisitServiceProductRecordDao, ErpVisitServiceProductRecord> {
    @Autowired
    private ErpVisitServiceProductRecordDao erpVisitServiceProductRecordDao;
    
    @Override
	public ErpVisitServiceProductRecord get(String id) {
		return super.get(id);
	}

    @Override
	public List<ErpVisitServiceProductRecord> findList(ErpVisitServiceProductRecord erpVisitServiceProductRecord) {
		return super.findList(erpVisitServiceProductRecord);
	}

    @Override
	public Page<ErpVisitServiceProductRecord> findPage(Page<ErpVisitServiceProductRecord> page, ErpVisitServiceProductRecord erpVisitServiceProductRecord) {
		return super.findPage(page, erpVisitServiceProductRecord);
	}

    @Override
	@Transactional(readOnly = false)
	public void save(ErpVisitServiceProductRecord erpVisitServiceProductRecord) {
		super.save(erpVisitServiceProductRecord);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(ErpVisitServiceProductRecord erpVisitServiceProductRecord) {
		super.delete(erpVisitServiceProductRecord);
	}
    
    @Transactional(readOnly = false)
    public  List<ErpVisitServiceProductRecord> getProductInfoByVisitServiceId(String visitServiceInfoId) {
       return erpVisitServiceProductRecordDao.getProductInfoByVisitServiceId(visitServiceInfoId);
    }
    
    @Transactional(readOnly = false)
    public  void deleteVisitProductRecordByVisitId(String visitServiceInfoId) {
        erpVisitServiceProductRecordDao.deleteVisitProductRecordByVisitId(visitServiceInfoId);
    }
    
    
}