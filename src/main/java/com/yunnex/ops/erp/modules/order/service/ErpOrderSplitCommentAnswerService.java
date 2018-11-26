package com.yunnex.ops.erp.modules.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitCommentAnswer;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSplitCommentAnswerDao;

/**
 * 分单评价回答表Service
 * @author linqunzhi
 * @version 2018-04-04
 */
@Service
public class ErpOrderSplitCommentAnswerService extends CrudService<ErpOrderSplitCommentAnswerDao, ErpOrderSplitCommentAnswer> {

	public ErpOrderSplitCommentAnswer get(String id) {
		return super.get(id);
	}
	
	public List<ErpOrderSplitCommentAnswer> findList(ErpOrderSplitCommentAnswer erpOrderSplitCommentAnswer) {
		return super.findList(erpOrderSplitCommentAnswer);
	}
	
	public Page<ErpOrderSplitCommentAnswer> findPage(Page<ErpOrderSplitCommentAnswer> page, ErpOrderSplitCommentAnswer erpOrderSplitCommentAnswer) {
		return super.findPage(page, erpOrderSplitCommentAnswer);
	}
	
	@Transactional(readOnly = false)
	public void save(ErpOrderSplitCommentAnswer erpOrderSplitCommentAnswer) {
		super.save(erpOrderSplitCommentAnswer);
	}
	
	@Transactional(readOnly = false)
	public void delete(ErpOrderSplitCommentAnswer erpOrderSplitCommentAnswer) {
		super.delete(erpOrderSplitCommentAnswer);
	}
	
}