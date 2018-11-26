package com.yunnex.ops.erp.modules.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitCommentQConf;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSplitCommentQConfDao;

/**
 * 分单评价问题配置Service
 * @author yunnex
 * @version 2018-04-04
 */
@Service
public class ErpOrderSplitCommentQConfService extends CrudService<ErpOrderSplitCommentQConfDao, ErpOrderSplitCommentQConf> {

	public ErpOrderSplitCommentQConf get(String id) {
		return super.get(id);
	}
	
	public List<ErpOrderSplitCommentQConf> findList(ErpOrderSplitCommentQConf erpOrderSplitCommentQConf) {
		return super.findList(erpOrderSplitCommentQConf);
	}
	
	public Page<ErpOrderSplitCommentQConf> findPage(Page<ErpOrderSplitCommentQConf> page, ErpOrderSplitCommentQConf erpOrderSplitCommentQConf) {
		return super.findPage(page, erpOrderSplitCommentQConf);
	}
	
	@Transactional(readOnly = false)
	public void save(ErpOrderSplitCommentQConf erpOrderSplitCommentQConf) {
		super.save(erpOrderSplitCommentQConf);
	}
	
	@Transactional(readOnly = false)
	public void delete(ErpOrderSplitCommentQConf erpOrderSplitCommentQConf) {
		super.delete(erpOrderSplitCommentQConf);
	}
	
}