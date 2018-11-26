package com.yunnex.ops.erp.modules.workflow.zhct.dao;


import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.zhct.entity.ErpZhctProductRecord;

/**
 * 智慧餐厅产品信息DAO接口
 * @author yunnex
 * @version 2018-08-28
 */
@MyBatisDao
public interface ErpZhctProductRecordDao extends CrudDao<ErpZhctProductRecord> {
	
	ErpZhctProductRecord findByProcInsIdAndType(@Param("procInsId")String procInsId,@Param("prodType")String prodType);
}