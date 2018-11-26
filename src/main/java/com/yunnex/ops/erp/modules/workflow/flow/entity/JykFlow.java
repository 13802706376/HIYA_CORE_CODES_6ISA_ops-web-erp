package com.yunnex.ops.erp.modules.workflow.flow.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.ActEntity;
import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.modules.oa.entity.TestAudit;

/**
 * 开户流程信息表Entity
 * @author Frank
 * @version 2017-10-27
 */
public class JykFlow  extends DataEntity<JykFlow>  {
	
	private static final long serialVersionUID = 1L;
	private String orderId;		// 原始订单信息
	private String splitId;		// 分单序号
	private String planningExpertInterface;		// 策划专家接口人
	private String planningExpert;		// 策划专家
	private String operationManager;		// 运营经理
	private String operationAdviser;		// 运营顾问
	private Long adviserShopDockingState;		// 商户对接状态(运营顾问)
	private Long planningShopDockingState;		// 商户对接状态(策划专家)
	private Date promotionTime;		// 推广时间
	private Date nextContactTime;		// 下次联络时间
	private String reCast;		// 是否复投
	private Long createShopBranch;		// 是否创建商户新门店
	private String remark;		// 备注
	private Long sort;		// 排序字段
	private String procInsId; //流程编号
	
	
	public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public JykFlow() {
		super();
	}

	public JykFlow(String id){
		super(id);
	}

	@Length(min=1, max=64, message="原始订单信息长度必须介于 1 和 64 之间")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@Length(min=1, max=64, message="分单序号长度必须介于 1 和 64 之间")
	public String getSplitId() {
		return splitId;
	}

	public void setSplitId(String splitId) {
		this.splitId = splitId;
	}
	
	
	@Length(min=1, max=64, message="策划专家接口人长度必须介于 1 和 64 之间")
	public String getPlanningExpertInterface() {
		return planningExpertInterface;
	}

	public void setPlanningExpertInterface(String planningExpertInterface) {
		this.planningExpertInterface = planningExpertInterface;
	}
	
	@Length(min=0, max=64, message="策划专家长度必须介于 0 和 64 之间")
	public String getPlanningExpert() {
		return planningExpert;
	}

	public void setPlanningExpert(String planningExpert) {
		this.planningExpert = planningExpert;
	}
	
	@Length(min=0, max=64, message="运营经理长度必须介于 0 和 64 之间")
	public String getOperationManager() {
		return operationManager;
	}

	public void setOperationManager(String operationManager) {
		this.operationManager = operationManager;
	}
	
	@Length(min=0, max=64, message="运营顾问长度必须介于 0 和 64 之间")
	public String getOperationAdviser() {
		return operationAdviser;
	}

	public void setOperationAdviser(String operationAdviser) {
		this.operationAdviser = operationAdviser;
	}
	
	public Long getAdviserShopDockingState() {
		return adviserShopDockingState;
	}

	public void setAdviserShopDockingState(Long adviserShopDockingState) {
		this.adviserShopDockingState = adviserShopDockingState;
	}
	
	public Long getPlanningShopDockingState() {
		return planningShopDockingState;
	}

	public void setPlanningShopDockingState(Long planningShopDockingState) {
		this.planningShopDockingState = planningShopDockingState;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPromotionTime() {
		return promotionTime;
	}

	public void setPromotionTime(Date promotionTime) {
		this.promotionTime = promotionTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getNextContactTime() {
		return nextContactTime;
	}

	public void setNextContactTime(Date nextContactTime) {
		this.nextContactTime = nextContactTime;
	}
	
	@Length(min=0, max=1, message="是否复投长度必须介于 0 和 1 之间")
	public String getReCast() {
		return reCast;
	}

	public void setReCast(String reCast) {
		this.reCast = reCast;
	}
	
	public Long getCreateShopBranch() {
		return createShopBranch;
	}

	public void setCreateShopBranch(Long createShopBranch) {
		this.createShopBranch = createShopBranch;
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
	
}