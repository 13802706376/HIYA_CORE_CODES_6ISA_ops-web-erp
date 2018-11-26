package com.yunnex.ops.erp.modules.order.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 分单评价回答表Entity
 * @author linqunzhi
 * @version 2018-04-04
 */
public class ErpOrderSplitCommentAnswer extends DataEntity<ErpOrderSplitCommentAnswer> {
	
	private static final long serialVersionUID = 1L;
	private String questionId;		// 问题id
	private String content;		// 回答内容
	private Integer rank;	// 显示顺序
	private String checkFlag;		// 选择状态（N：未勾选；Y：勾选）
	
	public ErpOrderSplitCommentAnswer() {
		super();
	}

	public ErpOrderSplitCommentAnswer(String id){
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
	
	@Length(min=0, max=1, message="选择状态（N：未勾选；Y：勾选）长度必须介于 0 和 1 之间")
	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	
}