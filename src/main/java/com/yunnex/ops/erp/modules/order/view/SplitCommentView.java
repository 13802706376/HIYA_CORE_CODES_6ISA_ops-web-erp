package com.yunnex.ops.erp.modules.order.view;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.BaseView;

/**
 * 分单评价问答view
 * 
 * @author linqunzhi
 * @date 2018年4月10日
 */
public class SplitCommentView extends BaseView {

    private static final long serialVersionUID = 7403015879620531282L;

    /** 订单ID */
    private String splitId;
    
    /** 创建时间字符串 */
    private String createDateStr;

    /** 服务满意度评分 */
    private Double serviceScore;

    /** 效果推广评分 */
    private Double promotionScore;

    /** 评价问题集合 */
    private List<SplitCommentQuestionView> questionList;

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public Double getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Double serviceScore) {
        this.serviceScore = serviceScore;
    }

    public Double getPromotionScore() {
        return promotionScore;
    }

    public void setPromotionScore(Double promotionScore) {
        this.promotionScore = promotionScore;
    }

    public List<SplitCommentQuestionView> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<SplitCommentQuestionView> questionList) {
        this.questionList = questionList;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

}
