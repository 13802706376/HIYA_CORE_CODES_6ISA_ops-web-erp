/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.hat.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.hat.entity.HatArea;

/**
 * 区DAO接口
 * @author yunnex
 * @version 2018-01-05
 */
@MyBatisDao
public interface HatAreaDao extends CrudDao<HatArea> {
    String getByCode(String code);
}