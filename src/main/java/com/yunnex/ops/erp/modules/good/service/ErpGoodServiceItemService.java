package com.yunnex.ops.erp.modules.good.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.good.dao.ErpGoodServiceItemDao;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodServiceItem;

/**
 * 商品服务项目Service
 * @author yunnex
 * @version 2018-05-29
 */
@Service
public class ErpGoodServiceItemService extends CrudService<ErpGoodServiceItemDao, ErpGoodServiceItem> {

	public ErpGoodServiceItem get(String id) {
		return super.get(id);
	}
	
	public List<ErpGoodServiceItem> findList(ErpGoodServiceItem erpGoodServiceItem) {
		return super.findList(erpGoodServiceItem);
	}
	
	public Page<ErpGoodServiceItem> findPage(Page<ErpGoodServiceItem> page, ErpGoodServiceItem erpGoodServiceItem) {
		return super.findPage(page, erpGoodServiceItem);
	}
	
	@Transactional(readOnly = false)
	public void save(ErpGoodServiceItem erpGoodServiceItem) {
		super.save(erpGoodServiceItem);
	}
	
	@Transactional(readOnly = false)
	public void delete(ErpGoodServiceItem erpGoodServiceItem) {
		super.delete(erpGoodServiceItem);
	}

    @Transactional
    public BaseResult add(ErpGoodServiceItem entity) {
        if (entity == null || StringUtils.isBlank(entity.getName()) || StringUtils.isBlank(entity.getReadonly())) {
            return new IllegalArgumentErrorResult();
        }
        super.save(entity);
        return new BaseResult();
    }
	
}