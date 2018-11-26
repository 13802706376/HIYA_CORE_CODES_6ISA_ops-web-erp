package com.yunnex.ops.erp.modules.workflow.flow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpPayIntopiecesSubTask;

/**
 * 支付进件工作流子任务处理情况表DAO接口
 * 
 * @author SunQ
 * @date 2017年12月9日
 */
@MyBatisDao
public interface ErpPayIntopiecesSubTaskDao extends CrudDao<ErpPayIntopiecesSubTask> {

    ErpPayIntopiecesSubTask getSubTask(@Param("taskId")String taskId, @Param("subTaskValue") String subTaskValue);

    List<ErpPayIntopiecesSubTask> getSubTaskList(@Param("taskId") String taskId);

    boolean updateTaskId(@Param("targetId") String targetId, @Param("taskId") String taskId);
    
    void updateTaskState(@Param("taskId") String taskId, @Param("state") String state);
}