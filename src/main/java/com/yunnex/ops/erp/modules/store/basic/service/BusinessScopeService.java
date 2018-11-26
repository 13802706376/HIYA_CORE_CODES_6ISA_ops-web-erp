package com.yunnex.ops.erp.modules.store.basic.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.store.basic.dao.BusinessScopeDao;
import com.yunnex.ops.erp.modules.store.basic.entity.BusinessScope;

/**
 * 经营范围Service
 * 
 * @author a
 * @version 2017-12-19
 */
@Service
public class BusinessScopeService extends CrudService<BusinessScopeDao, BusinessScope> {
	@Autowired
	private BusinessScopeDao businessScopeDao;

	@Override
	@Transactional(readOnly = false)
	public void save(BusinessScope businessScope) {
		super.save(businessScope);
	}
	@Override
	@Transactional(readOnly = false)
	public void delete(BusinessScope businessScope) {
		super.delete(businessScope);
	}
	
	public List<BusinessScope> findAllList(){
		return businessScopeDao.findAllList();
	}


    /**
     * 
     * 业务定义：根据经营范围文本查询对应ID
     * 
     * @date 2018年5月16日
     * @author R/Q
     */
    public Integer queryIdByText(String text) {
        if (StringUtils.isBlank(text)) {
            return -1;
        }
        Integer scopeId = businessScopeDao.queryIdByText(text.trim());
        if (null == scopeId) {
            return -1;
        }
        return scopeId;
    }


public BusinessScope findByText(String text) {
        return businessScopeDao.findByText(text);
    }

}

