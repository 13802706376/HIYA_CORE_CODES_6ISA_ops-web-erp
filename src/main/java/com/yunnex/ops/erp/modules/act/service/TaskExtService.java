package com.yunnex.ops.erp.modules.act.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.act.dao.TaskExtDao;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;

/**
 * act_ru_task_ext生成Service
 * @author act_ru_task_ext生成
 * @version 2018-01-13
 */
@Service
public class TaskExtService extends CrudService<TaskExtDao, TaskExt> {

	@Override
	public TaskExt get(String id) {
		return super.get(id);
	}
	
	@Override
	public List<TaskExt> findList(TaskExt taskExt) {
		return super.findList(taskExt);
	}
	
	@Override
	public Page<TaskExt> findPage(Page<TaskExt> page, TaskExt taskExt) {
		return super.findPage(page, taskExt);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(TaskExt taskExt) {
		super.save(taskExt);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(TaskExt taskExt) {
		super.delete(taskExt);
	}

    @Transactional(readOnly = false)
    public void updateTaskState(TaskExt taskExt) {
        dao.updateTaskState(taskExt);
    }
    
    @Transactional
    public void updateTaskExtInfoByTaskId(TaskExt taskExt, String taskId) {
        logger.info("-----------TaskExtService.updateTaskExtInfoByTaskId() start-----------");
        logger.info("taskExt={},id={}", taskExt, taskId);
        if (StringUtils.isBlank(taskId)) {
            logger.error("参数taskId={},非法参数！", taskId);
            return;
        }

        taskExt.setTaskId(taskId);
        dao.updateTaskExtInfoByTaskId(taskExt);

        logger.info("-----------TaskExtService.updateTaskExtInfoByTaskId() end-----------");
    }

    public Integer findOverTime(List<String> templist){
    	return dao.findOverTime(templist);
    }

    public Integer findOverTimeByOrder(List<String> userIds) {
        return dao.findOverTimeByOrder(userIds);
    }

    public List<TaskExt> findByTaskIds(List<String> taskIdList) {
        return dao.findByTaskIds(taskIdList);
    }

}