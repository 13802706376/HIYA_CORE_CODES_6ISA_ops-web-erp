package com.yunnex.ops.erp.modules.order.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 分单评价问题配置Entity
 * @author yunnex
 * @version 2018-04-04
 */
public class ErpOrderSplitCommentQConf extends DataEntity<ErpOrderSplitCommentQConf> {
	
	private static final long serialVersionUID = 1L;
	private String content;		// 问题内容
	private Integer rank;		// 显示顺序
	private String type;		// 题目类型（SelectMultiple 多选题，SelectSingle 单选题, Text 文本题）
	
	/** 问题是否必答 */
	private String mustFlag;
	
	public ErpOrderSplitCommentQConf() {
		super();
	}

	public ErpOrderSplitCommentQConf(String id){
		super(id);
	}

	@Length(min=0, max=255, message="问题内容长度必须介于 0 和 255 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@NotNull(message="显示顺序不能为空")
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
	
	@Length(min=1, max=50, message="题目类型（SelectMultiple 多选题，SelectSingle 单选题, Text 文本题）长度必须介于 1 和 50 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public String getMustFlag() {
        return mustFlag;
    }

    public void setMustFlag(String mustFlag) {
        this.mustFlag = mustFlag;
    }
	
}