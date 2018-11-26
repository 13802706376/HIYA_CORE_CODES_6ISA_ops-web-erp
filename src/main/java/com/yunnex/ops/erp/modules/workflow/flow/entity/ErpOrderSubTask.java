package com.yunnex.ops.erp.modules.workflow.flow.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 订单子任务处理信息
 * @author Frank
 * @version 2017-11-02
 */
public class ErpOrderSubTask extends DataEntity<ErpOrderSubTask> {
	
	private static final long serialVersionUID = 1L;
	private String splitId;		// 分单序号
	private String taskId;		// 任务编号
	private String subTaskId;		// 子任务编号
	private String state;		// 状态0:已完成,1:正在处理 2:未开始
	private String subTaskPerson;		// 子任务处理人
	private String remark;		// 备注
	private Long sort;		// 排序字段
	private String subTaskDetail;
	
    /**
     * 接收子任务中对应的文件列表
     */
    private List<ErpOrderFile> orderFiles = new ArrayList<ErpOrderFile>();

	public String getSubTaskDetail() {
        return subTaskDetail;
    }

    public void setSubTaskDetail(String subTaskDetail) {
        this.subTaskDetail = subTaskDetail;
    }

    public ErpOrderSubTask() {
		super();
	}

	public ErpOrderSubTask(String id){
		super(id);
	}

	
	@Length(min=1, max=64, message="分单序号长度必须介于 1 和 64 之间")
	public String getSplitId() {
		return splitId;
	}

	public void setSplitId(String splitId) {
		this.splitId = splitId;
	}
	
	@Length(min=1, max=64, message="任务编号长度必须介于 1 和 64 之间")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getSubTaskId() {
		return subTaskId;
	}

	public void setSubTaskId(String subTaskId) {
		this.subTaskId = subTaskId;
	}
	
	@Length(min=0, max=64, message="状态0:已完成,1:正在处理 2:未开始长度必须介于 0 和 64 之间")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getSubTaskPerson() {
		return subTaskPerson;
	}

	public void setSubTaskPerson(String subTaskPerson) {
		this.subTaskPerson = subTaskPerson;
	}
	
	@Length(min=0, max=256, message="备注长度必须介于 0 和 256 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

    public List<ErpOrderFile> getOrderFiles() {
        return orderFiles;
    }

    public void setOrderFiles(List<ErpOrderFile> orderFiles) {
        this.orderFiles = orderFiles;
    }

}