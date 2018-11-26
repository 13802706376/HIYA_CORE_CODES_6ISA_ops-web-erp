package com.yunnex.ops.erp.modules.workflow.flow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpFlowForm;

/**
 * 流程表单数据DAO接口
 * @author xiaoyunfei
 * @version 2018-05-07
 */
@MyBatisDao
public interface ErpFlowFormHisDao extends CrudDao<ErpFlowForm> {
    void batchInsertFlowFormData(@Param("list") List<ErpFlowForm> list);
}