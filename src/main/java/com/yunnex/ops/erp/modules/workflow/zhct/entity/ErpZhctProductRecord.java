package com.yunnex.ops.erp.modules.workflow.zhct.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 智慧餐厅产品信息Entity
 * @author yunnex
 * @version 2018-08-28
 */
public class ErpZhctProductRecord extends DataEntity<ErpZhctProductRecord> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程id
	private String shopInfoId;		// 商户ID
	private String prodType;		// 产品类型（printer 打印机, cashRegister 收银机）
	private String prodInfoJson;		// 产品信息 json字符串
	private String prodNum;		// 产品总数量
	
	
	public ErpZhctProductRecord() {
		super();
	}

	public ErpZhctProductRecord(String id){
		super(id);
	}

	@Length(min=1, max=64, message="流程id长度必须介于 1 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@Length(min=1, max=64, message="商户ID长度必须介于 1 和 64 之间")
	public String getShopInfoId() {
		return shopInfoId;
	}

	public void setShopInfoId(String shopInfoId) {
		this.shopInfoId = shopInfoId;
	}
	
	@Length(min=1, max=32, message="产品类型（printer 打印机, cashRegister 收银机）长度必须介于 1 和 32 之间")
	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	
	@Length(min=1, max=64, message="产品信息 json字符串长度必须介于 1 和 64 之间")
	public String getProdInfoJson() {
		return prodInfoJson;
	}

	public void setProdInfo(String prodInfoJson) {
		this.prodInfoJson = prodInfoJson;
	}
	
	@Length(min=1, max=32, message="产品总数量长度必须介于 1 和 32 之间")
	public String getProdNum() {
		return prodNum;
	}

	public void setProdNum(String prodNum) {
		this.prodNum = prodNum;
	}
	
}