package com.yunnex.ops.erp.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.sys.dao.SysOfficeUserDao;
import com.yunnex.ops.erp.modules.sys.entity.SysOfficeUser;

/**
 * 人员部门关联关系Service
 * 
 * @author 林群植
 * @version 2018-10-18
 */
@Service
public class SysOfficeUserService extends CrudService<SysOfficeUserDao, SysOfficeUser> {

    @Override
    @Transactional(readOnly = false)
    public void save(SysOfficeUser sysOfficeUser) {
        super.save(sysOfficeUser);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(SysOfficeUser sysOfficeUser) {
        super.delete(sysOfficeUser);
    }

}
