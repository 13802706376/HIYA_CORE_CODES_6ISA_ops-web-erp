package com.yunnex.ops.erp.modules.store.basic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.store.basic.dao.ErpStoreLinkmanDao;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLinkman;

/**
 * 门店联系人信息Service
 * 
 * @author yunnex
 * @version 2017-12-09
 */
@Service
public class ErpStoreLinkmanService extends CrudService<ErpStoreLinkmanDao, ErpStoreLinkman> {
    @Autowired
    private ErpStoreLinkmanDao linkmanDao;

    @Override
    @Transactional(readOnly = false)
    public void save(ErpStoreLinkman erpStoreLinkman) {
        super.save(erpStoreLinkman);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpStoreLinkman erpStoreLinkman) {
        super.delete(erpStoreLinkman);
    }


    public ErpStoreLinkman findWhereStoreId(String del, String id) {
        return linkmanDao.findWhereStoreId(del, id);
    }

}
