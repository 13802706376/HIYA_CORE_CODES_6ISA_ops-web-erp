package com.yunnex.ops.erp.modules.visit.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 硬件产品交付Entity
 * @author hanhan
 * @version 2018-07-09
 */
public class ErpVisitServiceProductRecord extends DataEntity<ErpVisitServiceProductRecord> {
	
	private static final long serialVersionUID = 1L;
	private String visitServiceInfoId;		// 交付记录来源ID,erp_visit_service_info[id]
	private String shopInfoId;		// 商户ID
	private String prodName;		// 产品名称
	private String prodType;		// 产品型号
	private String prodNum;		// 产品选择数量
	private String remark;		// 备注
	
	public ErpVisitServiceProductRecord() {
		super();
	}

	public ErpVisitServiceProductRecord(String id){
		super(id);
	}

	@Length(min=1, max=64, message="交付记录来源ID,erp_visit_service_info[id]长度必须介于 1 和 64 之间")
	public String getVisitServiceInfoId() {
		return visitServiceInfoId;
	}

	public void setVisitServiceInfoId(String visitServiceInfoId) {
		this.visitServiceInfoId = visitServiceInfoId;
	}
	
	@Length(min=1, max=64, message="商户ID长度必须介于 1 和 64 之间")
	public String getShopInfoId() {
		return shopInfoId;
	}

	public void setShopInfoId(String shopInfoId) {
		this.shopInfoId = shopInfoId;
	}
	
	@Length(min=1, max=64, message="产品名称长度必须介于 1 和 64 之间")
	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	
	@Length(min=1, max=32, message="产品型号长度必须介于 1 和 32 之间")
	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	
	@Length(min=1, max=32, message="产品选择数量长度必须介于 1 和 32 之间")
	public String getProdNum() {
		return prodNum;
	}

	public void setProdNum(String prodNum) {
		this.prodNum = prodNum;
	}
	
	@Length(min=0, max=256, message="备注长度必须介于 0 和 256 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}