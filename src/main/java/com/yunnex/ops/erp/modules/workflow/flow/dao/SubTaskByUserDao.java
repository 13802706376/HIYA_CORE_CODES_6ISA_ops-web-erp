package com.yunnex.ops.erp.modules.workflow.flow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.SubTaskByUser;

@MyBatisDao
public interface SubTaskByUserDao extends CrudDao<SubTaskByUser> {
	List<SubTaskByUser> getSubTaskByUser(@Param("taskId") String taskId);
}
