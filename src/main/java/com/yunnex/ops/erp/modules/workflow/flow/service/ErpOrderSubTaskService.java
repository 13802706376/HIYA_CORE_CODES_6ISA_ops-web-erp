package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.workflow.flow.dao.ErpOrderSubTaskDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderSubTask;

/**
 * 订单子任务信息表
 * 
 * @author yunnex
 * @date 2017年11月2日
 */
@Service
public class ErpOrderSubTaskService extends CrudService<ErpOrderSubTaskDao, ErpOrderSubTask> {

    @Autowired
    ErpOrderSubTaskDao dao;

    public ErpOrderSubTask get(String id) {
        return super.get(id);
    }

    public List<ErpOrderSubTask> getSubTaskList(String taskId) {
        return dao.getSubTaskList(taskId);
    }

    public ErpOrderSubTask getSubTask(String taskId, String subTaskValue)

    {
        return dao.getSubTask(taskId, subTaskValue);
    }

    public List<ErpOrderSubTask> findList(ErpOrderSubTask erpOrderSubTask) {
        return super.findList(erpOrderSubTask);
    }

    public Page<ErpOrderSubTask> findPage(Page<ErpOrderSubTask> page, ErpOrderSubTask erpOrderSubTask) {
        return super.findPage(page, erpOrderSubTask);
    }

    @Transactional(readOnly = false)
    public void save(ErpOrderSubTask erpOrderSubTask) {
        super.save(erpOrderSubTask);
    }

    @Transactional(readOnly = false)
    public void delete(ErpOrderSubTask erpOrderSubTask) {
        super.delete(erpOrderSubTask);
    }

    @Transactional(readOnly = false)
    public ErpOrderSubTask updateState(String taskId, String subTaskValue) {
        ErpOrderSubTask subTask = dao.getSubTask(taskId, subTaskValue);
        if (subTask!=null && !subTask.getState().equals("1")) {
            subTask.setState("1");
            this.save(subTask);
        }

        return null;
    }
    
    @Transactional(readOnly = false)
    public void updateTaskState(String taskId, String state) {
        dao.updateTaskState(taskId, state);
    }
    
    // 修改指定的任务ID为目标ID
    @Transactional(readOnly = false)
    public boolean updateTaskId(String targetId, String taskId) {
        return dao.updateTaskId(targetId, taskId);
    }

    @Transactional(readOnly = false)
    public void updateTaskRemark(String taskId, String subTaskId, String remark) {
        dao.updateTaskRemark(taskId, subTaskId, remark);
    }
}
