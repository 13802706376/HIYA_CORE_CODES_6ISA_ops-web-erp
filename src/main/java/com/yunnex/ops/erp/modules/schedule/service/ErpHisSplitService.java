package com.yunnex.ops.erp.modules.schedule.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.schedule.entity.ErpHisSplit;
import com.yunnex.ops.erp.modules.schedule.dao.ErpHisSplitDao;

/**
 * 生产进度小程序父表Service
 * @author pengchenghe
 * @version 2018-01-19
 */
@Service
public class ErpHisSplitService extends CrudService<ErpHisSplitDao, ErpHisSplit> {

	@Override
	@Transactional(readOnly = false)
	public void save(ErpHisSplit erpHisSplit) {
		super.save(erpHisSplit);
	}
	@Override
	@Transactional(readOnly = false)
	public void delete(ErpHisSplit erpHisSplit) {
		super.delete(erpHisSplit);
	}
	
}