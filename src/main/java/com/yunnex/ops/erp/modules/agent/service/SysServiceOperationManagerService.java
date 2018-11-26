package com.yunnex.ops.erp.modules.agent.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.agent.entity.SysServiceOperationManager;
import com.yunnex.ops.erp.modules.agent.dao.SysServiceOperationManagerDao;

/**
 * 服务商运营经关系对应表Service
 * 
 * @author hanhan
 * @version 2018-06-01
 */
@Service
public class SysServiceOperationManagerService
        extends CrudService<SysServiceOperationManagerDao, SysServiceOperationManager>
{

    public SysServiceOperationManager findByServiceNo(String serviceNo) {
        return dao.findByServiceNo(serviceNo);
    }

}
