package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.workflow.flow.dao.SubTaskByUserDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SubTaskByUser;

@Service
public class SubTaskByUserService extends CrudService<SubTaskByUserDao, SubTaskByUser> {
	@Autowired
    private SubTaskByUserDao subTaskByUserDao;
	
	/**
     * 获取商户资料录入业务表信息
     *
     * @param procInstId
     * @return
     * @date 2017年12月9日
     * @author SunQ
     */
    public List<SubTaskByUser> getSubTaskByUser(String taskId) {
        return subTaskByUserDao.getSubTaskByUser(taskId);
    }
}
