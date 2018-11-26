package com.yunnex.ops.erp.modules.workflow.flow.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.JykFlow;
import com.yunnex.ops.erp.modules.workflow.flow.from.FlowForm;
import com.yunnex.ops.erp.modules.workflow.flow.from.WorkFlowQueryForm;

/**
 * 开户流程信息表DAO接口
 * @author Frank
 * @version 2017-10-27
 */
@MyBatisDao
public interface JykFlowDao extends CrudDao<JykFlow> {
    
    JykFlow getByProcInstId(String procInsId);

    void updateFlowByProcIncId(JykFlow jykFlow);
    
    List<Map<String, Object>> findTaskAssignee(String procInsId);
    
    List<Map<String, Object>> findProcessByProcId(String procInsId);
    
    List<Map<String, Object>> getContentList(@Param("taskId") String taskId,@Param("procInsId") String procInsId);
    
    List<Map<String, Object>> getContentListDetail(@Param("procInsId") String procInsId,@Param("name") String name);
    
    List<FlowForm> findTaskList(WorkFlowQueryForm queryForm);
    
}