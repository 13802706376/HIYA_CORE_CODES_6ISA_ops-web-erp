package com.yunnex.ops.erp.modules.sys.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.sys.entity.ServiceOperation;

/**
 * 服务商编码与运营经理对应关系DAO
 * 
 * @author SunQ
 * @date 2017年12月15日
 */
@MyBatisDao
public interface ServiceOperationDao extends CrudDao<ServiceOperation> {

    /**
     * 通过服务商编号获取对应关系
     *
     * @param serviceNo
     * @return
     * @date 2017年12月15日
     * @author SunQ
     */
    ServiceOperation getByServiceNo(String serviceNo);
}