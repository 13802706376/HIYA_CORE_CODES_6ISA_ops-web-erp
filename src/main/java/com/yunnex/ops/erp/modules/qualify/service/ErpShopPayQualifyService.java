package com.yunnex.ops.erp.modules.qualify.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.qualify.dao.ErpShopPayQualifyDao;
import com.yunnex.ops.erp.modules.qualify.entity.ErpShopPayQualify;

/**
 * 商户支付资质Service
 * @author huanghaidong
 * @version 2017-10-24
 */
@Service
public class ErpShopPayQualifyService extends CrudService<ErpShopPayQualifyDao, ErpShopPayQualify> {

    @Autowired
    private ErpShopPayQualifyDao erpShopPayQualifyDao;

	public ErpShopPayQualify get(String id) {
		return super.get(id);
	}
	
	public List<ErpShopPayQualify> findList(ErpShopPayQualify erpShopPayQualify) {
		return super.findList(erpShopPayQualify);
	}
	
	public Page<ErpShopPayQualify> findPage(Page<ErpShopPayQualify> page, ErpShopPayQualify erpShopPayQualify) {
		return super.findPage(page, erpShopPayQualify);
	}
	
	@Transactional(readOnly = false)
	public void save(ErpShopPayQualify erpShopPayQualify) {
		super.save(erpShopPayQualify);
	}
	
	@Transactional(readOnly = false)
	public void delete(ErpShopPayQualify erpShopPayQualify) {
		super.delete(erpShopPayQualify);
	}

    public List<String> findPayQualifyList(String shopId) {
        return erpShopPayQualifyDao.findPayQualifyList(shopId);
    }
	
    public int countByShopIdAndPaytype(String shopId, String payValue) {
    	return erpShopPayQualifyDao.countByShopIdAndPaytype(shopId, payValue);
    }
}