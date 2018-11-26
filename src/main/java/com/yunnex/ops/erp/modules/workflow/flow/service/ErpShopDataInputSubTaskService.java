package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.workflow.flow.dao.ErpShopDataInputSubTaskDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpShopDataInputSubTask;

/**
 * 商户资料录入子任务信息表
 * 
 * @author SunQ
 * @date 2017年12月9日
 */
@Service
public class ErpShopDataInputSubTaskService extends CrudService<ErpShopDataInputSubTaskDao, ErpShopDataInputSubTask> {

    @Autowired
    private ErpShopDataInputSubTaskDao erpShopDataInputSubTaskDao;

    public List<ErpShopDataInputSubTask> getSubTaskList(String taskId) {
        return erpShopDataInputSubTaskDao.getSubTaskList(taskId);
    }

    public ErpShopDataInputSubTask getSubTask(String taskId, String subTaskValue) {
        return erpShopDataInputSubTaskDao.getSubTask(taskId, subTaskValue);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ErpShopDataInputSubTask erpShopDataInputSubTask) {
        super.save(erpShopDataInputSubTask);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpShopDataInputSubTask erpShopDataInputSubTask) {
        super.delete(erpShopDataInputSubTask);
    }

    @Transactional(readOnly = false)
    public ErpShopDataInputSubTask updateState(String taskId, String subTaskValue) {
        ErpShopDataInputSubTask subTask = erpShopDataInputSubTaskDao.getSubTask(taskId, subTaskValue);
        if (subTask!=null && !"1".equals(subTask.getState())) {
            subTask.setState("1");
            this.save(subTask);
        }
        return null;
    }
    
    @Transactional(readOnly = false)
    public void updateTaskState(String taskId, String state) {
        erpShopDataInputSubTaskDao.updateTaskState(taskId, state);
    }
    
    // 修改指定的任务ID为目标ID
    @Transactional(readOnly = false)
    public boolean updateTaskId(String targetId, String taskId) {
        return erpShopDataInputSubTaskDao.updateTaskId(targetId, taskId);
    }
    
    @Transactional(readOnly = false)
    public void updateTaskRemark(String taskId, String subTaskId, String remark){
        erpShopDataInputSubTaskDao.updateTaskRemark(taskId, subTaskId, remark);
    }
}
