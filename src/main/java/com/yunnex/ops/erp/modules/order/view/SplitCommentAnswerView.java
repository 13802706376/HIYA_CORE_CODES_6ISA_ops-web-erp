package com.yunnex.ops.erp.modules.order.view;

import com.yunnex.ops.erp.common.persistence.BaseView;

/**
 * 分单评价回答view
 * 
 * @author linqunzhi
 * @date 2018年4月10日
 */
public class SplitCommentAnswerView extends BaseView {

    private static final long serialVersionUID = -939680928647422838L;
    /** 问题id */
    private String questionId;

    /** 回答内容 */
    private String content;

    /** 选择状态（N：未勾选；Y：勾选） */
    private String checkFlag;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }
}
