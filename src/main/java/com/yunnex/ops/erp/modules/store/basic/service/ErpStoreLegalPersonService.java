package com.yunnex.ops.erp.modules.store.basic.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.store.basic.dao.ErpStoreLegalPersonDao;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLegalPerson;

/**
 * 法人信息Service
 * @author yunnex
 * @version 2017-12-09
 */
@Service
public class ErpStoreLegalPersonService extends CrudService<ErpStoreLegalPersonDao, ErpStoreLegalPerson> {

    @Override
    @Transactional(readOnly = false)
    public void save(ErpStoreLegalPerson erpStoreLegalPerson) {
        super.save(erpStoreLegalPerson);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpStoreLegalPerson erpStoreLegalPerson) {
        super.delete(erpStoreLegalPerson);
    }

}