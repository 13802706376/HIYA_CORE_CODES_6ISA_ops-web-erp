package com.yunnex.ops.erp.modules.workflow.flow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderSubTask;

/**
 * 工作流子任务处理情况表DAO接口
 * @author Frank
 * @version 2017-11-02
 */
@MyBatisDao
public interface ErpOrderSubTaskDao extends CrudDao<ErpOrderSubTask> {

    ErpOrderSubTask getSubTask(@Param("taskId")String taskId,@Param("subTaskValue") String subTaskValue);

    List<ErpOrderSubTask> getSubTaskList(@Param("taskId") String taskId);
    
    boolean updateTaskId(@Param("targetId") String targetId, @Param("taskId") String taskId);

    void updateTaskState(@Param("taskId") String taskId, @Param("state") String state);
    
    void updateTaskRemark(@Param("taskId") String taskId, @Param("subTaskId") String subTaskId, @Param("remark") String remark);
}