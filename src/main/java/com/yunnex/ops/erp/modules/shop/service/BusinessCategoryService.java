package com.yunnex.ops.erp.modules.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.shop.dao.BusinessCategoryDao;
import com.yunnex.ops.erp.modules.shop.entity.BusinessCategory;

/**
 * 经营类目Service
 * @author 11
 * @version 2017-12-20
 */
@Service
public class BusinessCategoryService extends CrudService<BusinessCategoryDao, BusinessCategory> {
    
	@Autowired
	private BusinessCategoryDao businessCategoryDao;
	
    public List<BusinessCategory> findAllList(){
		return businessCategoryDao.findAllList();
	}
	
	public BusinessCategory whereCategoryId(String cid){
		return businessCategoryDao.whereCategoryId(cid);
	}
}