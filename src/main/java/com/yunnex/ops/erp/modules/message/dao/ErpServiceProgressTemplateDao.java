package com.yunnex.ops.erp.modules.message.dao;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceProgressTemplate;

/**
 * 服务进度模板表DAO接口
 * @author yunnex
 * @version 2018-07-04
 */
@MyBatisDao
public interface ErpServiceProgressTemplateDao extends CrudDao<ErpServiceProgressTemplate> {

    /**
     * 根据条件获取唯一数据
     *
     * @param serviceType
     * @param type
     * @param status
     * @param processVersion
     * @return
     * @date 2018年7月19日
     * @author linqunzhi
     */
    ErpServiceProgressTemplate getOnly(@Param("serviceType") String serviceType, @Param("type") String type, @Param("status") String status,
                    @Param("processVersion") int processVersion);
	
}