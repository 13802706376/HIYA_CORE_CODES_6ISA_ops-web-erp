package com.yunnex.ops.erp.modules.workflow.flow.dao;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderOperateValue;

/**
 * 订单操作内容数据层接口
 * 
 * @author SunQ
 * @date 2018年2月7日
 */
@MyBatisDao
public interface ErpOrderOperateValueDao extends CrudDao<ErpOrderOperateValue> {

    ErpOrderOperateValue getOnlyOne(@Param("procInsId") String procInsId, @Param("keyName") String keyName, @Param("subTaskId") String subTaskId);
}