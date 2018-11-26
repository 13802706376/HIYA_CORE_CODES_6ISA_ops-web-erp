package com.yunnex.ops.erp.modules.order.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 分单评价回答配置Entity
 * @author yunnex
 * @version 2018-04-04
 */
public class ErpOrderSplitCommentAConf extends DataEntity<ErpOrderSplitCommentAConf> {
	
	private static final long serialVersionUID = 1L;
	private String questionId;		// 问题id
	private String content;		// 回答内容
	private Integer rank;		// 显示顺序
	
	public ErpOrderSplitCommentAConf() {
		super();
	}

	public ErpOrderSplitCommentAConf(String id){
		super(id);
	}

	@Length(min=1, max=64, message="问题id长度必须介于 1 和 64 之间")
	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	
	@Length(min=0, max=255, message="回答内容长度必须介于 0 和 255 之间")
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
	
}