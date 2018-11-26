package com.yunnex.ops.erp.modules.message.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceMessage;

/**
 * 服务通知表DAO接口
 * @author yunnex
 * @version 2018-07-04
 */
@MyBatisDao
public interface ErpServiceMessageDao extends CrudDao<ErpServiceMessage> {

    /**
     * 根据流程id 和 节点类型 获取 服务通知数据
     *
     * @param procInsId
     * @param nodeType
     * @return
     * @date 2018年8月6日
     * @author linqunzhi
     */
    List<ErpServiceMessage> findByProcInsIdAndNodeType(@Param("procInsId") String procInsId, @Param("nodeType") String nodeType);
	
}