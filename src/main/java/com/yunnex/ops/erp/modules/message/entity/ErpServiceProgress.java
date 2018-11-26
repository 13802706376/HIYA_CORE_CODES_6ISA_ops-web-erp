package com.yunnex.ops.erp.modules.message.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 服务进度表Entity
 * @author yunnex
 * @version 2018-07-04
 */
public class ErpServiceProgress extends DataEntity<ErpServiceProgress> {
	
	private static final long serialVersionUID = 1L;
	private String templateId;		// 模板id
	private String procInsId;		// 流程id
	private Date startTime;		// 启动时间
    private String zhangbeiId;// 掌贝id
	
	public ErpServiceProgress() {
		super();
	}

	public ErpServiceProgress(String id){
		super(id);
	}

	@Length(min=1, max=64, message="模板id长度必须介于 1 和 64 之间")
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	@Length(min=1, max=64, message="流程id长度必须介于 1 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="启动时间不能为空")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

    public String getZhangbeiId() {
        return zhangbeiId;
    }

    public void setZhangbeiId(String zhangbeiId) {
        this.zhangbeiId = zhangbeiId;
    }


	
}