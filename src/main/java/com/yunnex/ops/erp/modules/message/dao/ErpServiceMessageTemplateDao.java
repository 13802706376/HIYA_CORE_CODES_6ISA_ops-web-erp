package com.yunnex.ops.erp.modules.message.dao;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceMessageTemplate;

/**
 * 服务通知模板表DAO接口
 * @author yunnex
 * @version 2018-07-04
 */
@MyBatisDao
public interface ErpServiceMessageTemplateDao extends CrudDao<ErpServiceMessageTemplate> {

    /**
     * 根据条件获取唯一数据
     *
     * @param serviceType
     * @param nodeType
     * @param status
     * @return
     * @date 2018年7月19日
     * @author linqunzhi
     */
    ErpServiceMessageTemplate getOnly(@Param("serviceType") String serviceType, @Param("nodeType") String nodeType, @Param("status") String status);
	
}