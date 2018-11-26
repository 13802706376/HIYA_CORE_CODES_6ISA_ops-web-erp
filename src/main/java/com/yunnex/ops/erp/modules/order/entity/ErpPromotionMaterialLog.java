package com.yunnex.ops.erp.modules.order.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;

/**
 * 推广资料操作日志Entity
 * @author yunnex
 * @version 2018-05-09
 */
public class ErpPromotionMaterialLog extends DataEntity<ErpPromotionMaterialLog> {
	
	private static final long serialVersionUID = 1L;
	private String splitId;		// 分单ID
	private String promotionMaterialsId;		// 推广资料ID,用于标识某一个推广资料
	private String operator;		// 操作人
	private Date operateTime;		// 操作时间
	private String operateType;		// 操作类型
    private String operatorName; // 操作人姓名
	
	public ErpPromotionMaterialLog() {
		super();
	}

	public ErpPromotionMaterialLog(String id){
		super(id);
	}

    public ErpPromotionMaterialLog(String splitId, String promotionMaterialsId, String operateType) {
        this.splitId = splitId;
        this.promotionMaterialsId = promotionMaterialsId;
        this.operateType = operateType;
        this.operator = UserUtils.getUser().getId();
        this.operateTime = new Date();
        this.remarks = "";
    }

	@Length(min=1, max=64, message="分单ID长度必须介于 1 和 64 之间")
	public String getSplitId() {
		return splitId;
	}

	public void setSplitId(String splitId) {
		this.splitId = splitId;
	}
	
	@Length(min=1, max=128, message="推广资料ID,用于标识某一个推广资料长度必须介于 1 和 128 之间")
	public String getPromotionMaterialsId() {
		return promotionMaterialsId;
	}

	public void setPromotionMaterialsId(String promotionMaterialsId) {
		this.promotionMaterialsId = promotionMaterialsId;
	}
	
	@Length(min=1, max=64, message="操作人长度必须介于 1 和 64 之间")
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="操作时间不能为空")
	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	
	@Length(min=1, max=64, message="操作类型长度必须介于 1 和 64 之间")
	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
	
}