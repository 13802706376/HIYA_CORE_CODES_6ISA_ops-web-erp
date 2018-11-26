package com.yunnex.ops.erp.modules.message.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceLink;
import com.yunnex.ops.erp.modules.message.dao.ErpServiceLinkDao;

/**
 * 交互入口配置表Service
 * @author yunnex
 * @version 2018-07-04
 */
@Service
@Transactional(readOnly = true)
public class ErpServiceLinkService extends CrudService<ErpServiceLinkDao, ErpServiceLink> {

    @Override
	public ErpServiceLink get(String id) {
		return super.get(id);
	}

    @Override
	public List<ErpServiceLink> findList(ErpServiceLink erpServiceLink) {
		return super.findList(erpServiceLink);
	}

    @Override
	public Page<ErpServiceLink> findPage(Page<ErpServiceLink> page, ErpServiceLink erpServiceLink) {
		return super.findPage(page, erpServiceLink);
	}

    @Override
	@Transactional(readOnly = false)
	public void save(ErpServiceLink erpServiceLink) {
		super.save(erpServiceLink);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(ErpServiceLink erpServiceLink) {
		super.delete(erpServiceLink);
	}
	
}