package com.yunnex.ops.erp.modules.workflow.acceptance.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 服务验收评价Entity
 * @author yunnex
 * @version 2018-07-04
 */
public class ErpServiceAcceptance extends DataEntity<ErpServiceAcceptance> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程id
	private Double score;		// 评分
    private String visitInfoId; // 上门服务数据id
    private String serviceGoal; // 本次服务（上门目的）
	
	public ErpServiceAcceptance() {
		super();
	}

	public ErpServiceAcceptance(String id){
		super(id);
	}

	@Length(min=1, max=64, message="流程id长度必须介于 1 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@NotNull(message="评分不能为空")
	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

    public String getVisitInfoId() {
        return visitInfoId;
    }

    public void setVisitInfoId(String visitInfoId) {
        this.visitInfoId = visitInfoId;
    }

    public String getServiceGoal() {
        return serviceGoal;
    }

    public void setServiceGoal(String serviceGoal) {
        this.serviceGoal = serviceGoal;
    }
}