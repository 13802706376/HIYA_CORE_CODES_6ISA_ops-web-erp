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
public interface ErpFlowFormDao extends CrudDao<ErpFlowForm> {

    public ErpFlowForm findByCondition(ErpFlowForm erpFlowForm);

    public List<ErpFlowForm> findByTaskId(String taskId);

    public int deleteByFormAttrName(@Param("taskId") String taskId, @Param("formAttrName") String formAttrName);

    public ErpFlowForm findByProcessIdAndAttrName(@Param("procInsId") String procInsId, @Param("attrName") String attrName);

    public List<ErpFlowForm> findByProcessIdAndTask(@Param("procInsId") String procInsId, @Param("taskDefinitionKey") String taskDefinitionKey);

    public int deleteByProcInsId(@Param("procInsId") String procInsId);
    
    public List<ErpFlowForm> findByProcInsId(@Param("procInsId") String procInsId);
    
    void batchInsert(@Param("list") List<ErpFlowForm> list);

	public List<String> findFormAttrNamesByProcInsId(@Param("procInsId")String procInsId);
	
	public void updateErpFlowForm(ErpFlowForm erpFlowForm);
}