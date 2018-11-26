package com.yunnex.ops.erp.modules.workflow.flow.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 *订单文件信息
 * @author Frank
 * @version 2017-11-02
 */
public class ErpOrderFile extends DataEntity<ErpOrderFile> {
	
	private static final long serialVersionUID = 1L;
    private String procInsId; // 流程实例ID
	private String fileName;		// 文件名称
	private String filePath;		// 文件路径
	private String fileType;		// 文件类型
	private String fileTitle;		// 文件类型
	private String remark;		// 备注
	
    /**
     * 任务主键
     */
    private String taskDefKey;

    /**
     * 子任务编号
     */
    private String subTaskId;

	private String createName;
    private String createTime;
	
	
	public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public ErpOrderFile() {
		super();
	}

	public ErpOrderFile(String id){
		super(id);
	}

    @Length(min = 1, max = 64, message = "流程实例ID长度必须介于 1 和 64 之间")
    public String getProcInsId() {
        return procInsId;
	}

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
	}
	
	@Length(min=1, max=64, message="文件名称长度必须介于 1 和 64 之间")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Length(min=0, max=256, message="文件路径长度必须介于 0 和 256 之间")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@Length(min=0, max=64, message="文件类型长度必须介于 0 和 64 之间")
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	@Length(min=0, max=64, message="文件类型长度必须介于 0 和 64 之间")
	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}
	
	@Length(min=0, max=256, message="备注长度必须介于 0 和 256 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    @Length(min = 1, max = 256, message = "任务主键长度必须介于 1和 64 之间")
    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(String subTaskId) {
        this.subTaskId = subTaskId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
	
}