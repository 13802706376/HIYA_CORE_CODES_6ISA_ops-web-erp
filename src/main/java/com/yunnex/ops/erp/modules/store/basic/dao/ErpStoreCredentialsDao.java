package com.yunnex.ops.erp.modules.store.basic.dao;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreCredentials;

/**
 * 商户营业资质信息DAO接口
 * @author yunnex
 * @version 2017-12-09
 */
@MyBatisDao
public interface ErpStoreCredentialsDao extends CrudDao<ErpStoreCredentials> {

}