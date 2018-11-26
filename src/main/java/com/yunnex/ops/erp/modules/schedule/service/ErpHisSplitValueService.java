package com.yunnex.ops.erp.modules.schedule.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.schedule.entity.ErpHisSplitValue;
import com.yunnex.ops.erp.modules.schedule.dao.ErpHisSplitValueDao;

/**
 * 生产进度小程序子表Service
 * @author pengchenghe
 * @version 2018-01-19
 */
@Service
public class ErpHisSplitValueService extends CrudService<ErpHisSplitValueDao, ErpHisSplitValue> {

	@Override
	@Transactional(readOnly = false)
	public void save(ErpHisSplitValue erpHisSplitValue) {
		super.save(erpHisSplitValue);
	}
	@Override
	@Transactional(readOnly = false)
	public void delete(ErpHisSplitValue erpHisSplitValue) {
		super.delete(erpHisSplitValue);
	}
	
}