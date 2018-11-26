package com.yunnex.ops.erp.modules.workflow.flow.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 流程表单数据Entity
 * @author xiaoyunfei
 * @version 2018-05-07
 */
public class ErpFlowForm extends DataEntity<ErpFlowForm> {

    public final static String NORMAL = "NORMAL";
    public final static String TEXT = "TEXT";
    public final static String FILE = "FILE";
	
	private static final long serialVersionUID = 1L;
	private String taskId;		// 任务id
	private String busId;		// 业务主表id
	private String procInsId;		// 流程id
	private String taskDef;		// 任务key
    private String formAttrType = "NORMAL"; // 属性类型 NORMAL:普通类型,TEXT:富文本类型
	private String formAttrName;		// 表单属性名
    private String formAttrValue = ""; // 表单属性值
    private String formTextValue = ""; // 文本输入项内容
    private String formAttrDesc = ""; // 描述
    private String remark = ""; // 备注
	
	public ErpFlowForm() {
		super();
	}
	
	

	public ErpFlowForm(String taskId, String busId, String procInsId, String taskDef, String formAttrType,
			String formAttrName, String formAttrValue, String formTextValue) {
		super();
		this.taskId = taskId;
		this.busId = busId;
		this.procInsId = procInsId;
		this.taskDef = taskDef;
		this.formAttrType = formAttrType;
		this.formAttrName = formAttrName;
		this.formAttrValue = formAttrValue;
		this.formTextValue = formTextValue;
	}



	public ErpFlowForm(String id){
		super(id);
	}

	@Length(min=1, max=64, message="任务id长度必须介于 1 和 64 之间")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	@Length(min=1, max=64, message="业务主表id长度必须介于 1 和 64 之间")
	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}
	
	@Length(min=1, max=64, message="流程id长度必须介于 1 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@Length(min=1, max=64, message="任务key长度必须介于 1 和 64 之间")
	public String getTaskDef() {
		return taskDef;
	}

	public void setTaskDef(String taskDef) {
		this.taskDef = taskDef;
	}
	
	@Length(min=1, max=64, message="属性类型 NORMAL:普通类型,TEXT:富文本类型长度必须介于 1 和 64 之间")
	public String getFormAttrType() {
		return formAttrType;
	}

	public void setFormAttrType(String formAttrType) {
		this.formAttrType = formAttrType;
	}
	
	@Length(min=1, max=64, message="表单属性名长度必须介于 1 和 64 之间")
	public String getFormAttrName() {
		return formAttrName;
	}

	public void setFormAttrName(String formAttrName) {
		this.formAttrName = formAttrName;
	}
	
	@Length(min=1, max=500, message="表单属性值长度必须介于 1 和 500 之间")
	public String getFormAttrValue() {
		return formAttrValue;
	}

	public void setFormAttrValue(String formAttrValue) {
		this.formAttrValue = formAttrValue;
	}
	
	public String getFormTextValue() {
		return formTextValue;
	}

	public void setFormTextValue(String formTextValue) {
		this.formTextValue = formTextValue;
	}
	
	@Length(min=0, max=200, message="描述长度必须介于 0 和 200 之间")
	public String getFormAttrDesc() {
		return formAttrDesc;
	}

	public void setFormAttrDesc(String formAttrDesc) {
		this.formAttrDesc = formAttrDesc;
	}
	
	@Length(min=0, max=256, message="备注长度必须介于 0 和 256 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}