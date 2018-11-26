/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.hat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.hat.dao.HatAreaDao;
import com.yunnex.ops.erp.modules.hat.entity.HatArea;

/**
 * 区Service
 * @author yunnex
 * @version 2018-01-05
 */
@Service
public class HatAreaService extends CrudService<HatAreaDao, HatArea> {
    
    @Override
    @Transactional(readOnly = false)
    public void save(HatArea entity) {
        super.save(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(HatArea entity) {
        super.delete(entity);
    }
}