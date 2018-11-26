/**

 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.hat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.hat.dao.HatProvinceDao;
import com.yunnex.ops.erp.modules.hat.entity.HatProvince;

/**
 * 省Service
 * @author yunnex
 * @version 2018-01-05
 */
@Service
public class HatProvinceService extends CrudService<HatProvinceDao, HatProvince> {

    @Transactional(readOnly = false)
    @Override
    public void save(HatProvince entity) {
        super.save(entity);
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(HatProvince entity) {
        super.delete(entity);
    }
}