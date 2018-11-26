package com.yunnex.ops.erp.modules.workflow.flow.entity;

import com.yunnex.ops.erp.common.persistence.DataEntity;

public class SubTaskByUser extends DataEntity<SubTaskByUser> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8428210104021248148L;
	
	private String taskId;
	private String delFlag;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
}
