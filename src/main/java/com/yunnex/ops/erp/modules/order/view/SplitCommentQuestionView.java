package com.yunnex.ops.erp.modules.order.view;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.BaseView;

/**
 * 分单评价问题view
 * 
 * @author linqunzhi
 * @date 2018年4月10日
 */
public class SplitCommentQuestionView extends BaseView {

    private static final long serialVersionUID = 8768837316314461761L;

    /** 评论id */
    private String commentId;

    /** 问题内容 */
    private String content;

    /** 题目类型（SelectMultiple 多选题，SelectSingle 单选题 ,Text 文本题） */
    private String type;

    /** 回答列表 */
    List<SplitCommentAnswerView> answerList;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SplitCommentAnswerView> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<SplitCommentAnswerView> answerList) {
        this.answerList = answerList;
    }

}
