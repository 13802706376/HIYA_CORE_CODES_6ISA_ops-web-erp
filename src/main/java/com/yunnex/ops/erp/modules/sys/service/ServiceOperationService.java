package com.yunnex.ops.erp.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.sys.dao.ServiceOperationDao;
import com.yunnex.ops.erp.modules.sys.entity.ServiceOperation;

/**
 * 
 * 
 * @author SunQ
 * @date 2017年12月15日
 */
@Service
public class ServiceOperationService extends CrudService<ServiceOperationDao, ServiceOperation> {

    @Autowired
    private ServiceOperationDao serviceOperationDao;

    @Transactional(readOnly = false)
    @Override
    public void save(ServiceOperation entity) {
        super.save(entity);
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(ServiceOperation entity) {
        super.delete(entity);
    }
    
    public ServiceOperation getByServiceNo(String serviceNo) {
        return serviceOperationDao.getByServiceNo(serviceNo);
    }
}