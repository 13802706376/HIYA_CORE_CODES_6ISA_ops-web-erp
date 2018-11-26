package com.yunnex.ops.erp.modules.workflow.flow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderInputDetail;

/**
 * 订单输入信息表DAO接口
 * 
 * @author Frank
 * @version 2017-11-02
 */
@MyBatisDao
public interface ErpOrderInputDetailDao extends CrudDao<ErpOrderInputDetail> {

    List<ErpOrderInputDetail> findListBySplitId(@Param("splitId")String splitId);

    ErpOrderInputDetail getBySplitId(@Param("splitId")String splitId,@Param("inputTaskName")String inputTaskName);

    ErpOrderInputDetail findListByInputKey(@Param("splitId") String splitId, @Param("inputTaskKey") String inputTaskKey);
}
