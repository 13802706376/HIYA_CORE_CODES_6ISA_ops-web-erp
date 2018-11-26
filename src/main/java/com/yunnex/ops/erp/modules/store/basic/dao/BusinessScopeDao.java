package com.yunnex.ops.erp.modules.store.basic.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.store.basic.entity.BusinessScope;

import java.util.List;

/**
 * 经营范围DAO接口
 * 
 * @author a
 * @version 2017-12-19
 */
@MyBatisDao
public interface BusinessScopeDao extends CrudDao<BusinessScope> {
	List<BusinessScope> findAllList();
 /**
     * 
     * 业务定义：根据经营范围文本查询对应ID
     * 
     * @date 2018年5月16日
     * @author R/Q
     */
    Integer queryIdByText(String text);

    BusinessScope findByText(String text);
}

