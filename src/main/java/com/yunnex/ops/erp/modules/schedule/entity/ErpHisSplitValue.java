package com.yunnex.ops.erp.modules.schedule.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 生产进度小程序子表Entity
 * @author pengchenghe
 * @version 2018-01-19
 */
public class ErpHisSplitValue extends DataEntity<ErpHisSplitValue> {
	
	private static final long serialVersionUID = 1L;
	private String hisSplitId;		// 父表主键
	private String hisContent;		// 生产进度历史字段名称
	private String hisValue;		// 生产进度历史值
	
	public ErpHisSplitValue() {
		super();
	}

	public ErpHisSplitValue(String id){
		super(id);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return super.toString();
	}
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Length(min=0, max=255, message="父表主键长度必须介于 0 和 255 之间")
	public String getHisSplitId() {
		return hisSplitId;
	}

	public void setHisSplitId(String hisSplitId) {
		this.hisSplitId = hisSplitId;
	}
	
	@Length(min=0, max=255, message="生产进度历史字段名称长度必须介于 0 和 255 之间")
	public String getHisContent() {
		return hisContent;
	}

	public void setHisContent(String hisContent) {
		this.hisContent = hisContent;
	}
	
	@Length(min=0, max=255, message="生产进度历史值长度必须介于 0 和 255 之间")
	public String getHisValue() {
		return hisValue;
	}

	public void setHisValue(String hisValue) {
		this.hisValue = hisValue;
	}
	
}