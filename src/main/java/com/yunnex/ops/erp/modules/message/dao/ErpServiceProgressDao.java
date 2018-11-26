package com.yunnex.ops.erp.modules.message.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceProgress;
import com.yunnex.ops.erp.modules.message.extraModel.ServiceProgressExtra;

/**
 * 服务进度表DAO接口
 * @author yunnex
 * @version 2018-07-04
 */
@MyBatisDao
public interface ErpServiceProgressDao extends CrudDao<ErpServiceProgress> {

    /**
     * 根据流程id 和 服务进度类型 获取服务进度
     *
     * @param procInsId
     * @param serviceType
     * @param type
     * @return
     * @date 2018年7月9日
     * @author linqunzhi
     */
    ErpServiceProgress getByProcInsIdAndType(@Param("procInsId") String procInsId, @Param("serviceType") String serviceType,
                    @Param("type") String type);
	
    /**
     * 获取 服务进度 扩展类列表
     *
     * @param extra
     * @return
     * @date 2018年8月24日
     * @author linqunzhi
     */
    List<ServiceProgressExtra> findExtra(ServiceProgressExtra extra);


}