package com.yunnex.ops.erp.modules.shop.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.shop.pay.entity.ErpShopPayAlipa;
import com.yunnex.ops.erp.modules.shop.pay.dao.ErpShopPayAlipaDao;

/**
 * 支付宝口碑Service
 * @author hanhan
 * @version 2018-05-26
 */
@Service
public class ErpShopPayAlipaService extends CrudService<ErpShopPayAlipaDao, ErpShopPayAlipa> {
    @Autowired
    private ErpShopPayAlipaDao erpShopPayAlipaDao;
	public ErpShopPayAlipa get(String id) {
		return super.get(id);
	}
	
	public List<ErpShopPayAlipa> findList(ErpShopPayAlipa erpShopPayAlipa) {
		return super.findList(erpShopPayAlipa);
	}
	
	public Page<ErpShopPayAlipa> findPage(Page<ErpShopPayAlipa> page, ErpShopPayAlipa erpShopPayAlipa) {
		return super.findPage(page, erpShopPayAlipa);
	}
	
	@Transactional(readOnly = false)
	public void save(ErpShopPayAlipa erpShopPayAlipa) {
		super.save(erpShopPayAlipa);
	}
	
	@Transactional(readOnly = false)
	public void delete(ErpShopPayAlipa erpShopPayAlipa) {
		super.delete(erpShopPayAlipa);
	}
	@Transactional(readOnly = false)
    public ErpShopPayAlipa getShopAilpaInfoByShopInfoId(String shopInfoId) {
	  return  erpShopPayAlipaDao.getShopAilpaInfoByShopInfoId(shopInfoId);
    }
	
}