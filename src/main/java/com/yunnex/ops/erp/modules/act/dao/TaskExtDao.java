package com.yunnex.ops.erp.modules.act.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;

/**
 * act_ru_task_ext生成DAO接口
 * @author act_ru_task_ext生成
 * @version 2018-01-13
 */
@MyBatisDao
public interface TaskExtDao extends CrudDao<TaskExt> {

    void updateTaskState(TaskExt taskExt);

    void updateTaskExtInfoByTaskId(TaskExt taskExt);
    
    void deleteTaskExtsByProcInsId(@Param("procInsId") String procInsId);
    
    Integer findOverTime(@Param("templist") List<String> templist);
    
    Integer findOverTimeByOrder(@Param("userIds") List<String> userIds);

    List<TaskExt> findByTaskIds(@Param("ids") List<String> ids);

}