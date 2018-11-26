/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.hat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.hat.dao.HatCityDao;
import com.yunnex.ops.erp.modules.hat.entity.HatCity;

/**
 * 市Service
 * @author yunnex
 * @version 2018-01-05
 */
@Service
public class HatCityService extends CrudService<HatCityDao, HatCity> {

    @Transactional(readOnly = false)
    @Override
    public void save(HatCity entity) {
        super.save(entity);
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(HatCity entity) {
        super.delete(entity);
    }
}