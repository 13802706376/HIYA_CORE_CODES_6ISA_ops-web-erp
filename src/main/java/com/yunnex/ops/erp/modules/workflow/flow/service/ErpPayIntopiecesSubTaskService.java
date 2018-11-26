package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.workflow.flow.dao.ErpPayIntopiecesSubTaskDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpPayIntopiecesSubTask;

/**
 * 商户资料录入子任务信息表
 * 
 * @author SunQ
 * @date 2017年12月9日
 */
@Service
public class ErpPayIntopiecesSubTaskService extends CrudService<ErpPayIntopiecesSubTaskDao, ErpPayIntopiecesSubTask> {

    @Autowired
    private ErpPayIntopiecesSubTaskDao erpPayIntopiecesSubTaskDao;

    public List<ErpPayIntopiecesSubTask> getSubTaskList(String taskId) {
        return erpPayIntopiecesSubTaskDao.getSubTaskList(taskId);
    }

    public ErpPayIntopiecesSubTask getSubTask(String taskId, String subTaskValue) {
        return erpPayIntopiecesSubTaskDao.getSubTask(taskId, subTaskValue);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ErpPayIntopiecesSubTask erpPayIntopiecesSubTask) {
        super.save(erpPayIntopiecesSubTask);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpPayIntopiecesSubTask erpPayIntopiecesSubTask) {
        super.delete(erpPayIntopiecesSubTask);
    }

    @Transactional(readOnly = false)
    public ErpPayIntopiecesSubTask updateState(String taskId, String subTaskValue) {
        ErpPayIntopiecesSubTask subTask = erpPayIntopiecesSubTaskDao.getSubTask(taskId, subTaskValue);
        if (subTask!=null && !"1".equals(subTask.getState())) {
            subTask.setState("1");
            this.save(subTask);
        }
        return null;
    }
    
    @Transactional(readOnly = false)
    public void updateTaskState(String taskId, String state) {
        erpPayIntopiecesSubTaskDao.updateTaskState(taskId, state);
    }
    
    // 修改指定的任务ID为目标ID
    @Transactional(readOnly = false)
    public boolean updateTaskId(String targetId, String taskId) {
        return erpPayIntopiecesSubTaskDao.updateTaskId(targetId, taskId);
    }
}
