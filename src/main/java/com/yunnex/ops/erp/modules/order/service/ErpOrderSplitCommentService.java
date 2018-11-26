package com.yunnex.ops.erp.modules.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSplitCommentDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitComment;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitCommentAnswer;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitCommentQuestion;
import com.yunnex.ops.erp.modules.order.view.SplitCommentAnswerView;
import com.yunnex.ops.erp.modules.order.view.SplitCommentQuestionView;
import com.yunnex.ops.erp.modules.order.view.SplitCommentView;

/**
 * 聚引客分单评论Service
 * 
 * @author yunnex
 * @version 2018-01-30
 */
@Service
public class ErpOrderSplitCommentService extends CrudService<ErpOrderSplitCommentDao, ErpOrderSplitComment> {

    @Autowired
    private ErpOrderSplitCommentQuestionService erpOrderSplitCommentQuestionService;

    @Autowired
    private ErpOrderSplitCommentAnswerService erpOrderSplitCommentAnswerService;


    @Override
    public ErpOrderSplitComment get(String id) {
        return super.get(id);
    }

    @Override
    public List<ErpOrderSplitComment> findList(ErpOrderSplitComment erpOrderSplitComment) {
        return super.findList(erpOrderSplitComment);
    }

    @Override
    public Page<ErpOrderSplitComment> findPage(Page<ErpOrderSplitComment> page, ErpOrderSplitComment erpOrderSplitComment) {
        return super.findPage(page, erpOrderSplitComment);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ErpOrderSplitComment erpOrderSplitComment) {
        super.save(erpOrderSplitComment);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpOrderSplitComment erpOrderSplitComment) {
        super.delete(erpOrderSplitComment);
    }

    /**
     * 根据分单id获取所有评价信息
     *
     * @param splitId
     * @return
     * @date 2018年4月10日
     * @author linqunzhi
     */
    public List<SplitCommentView> findSplitCommentViewList(String splitId) {
        logger.info("findSplitCommentViewList start | splitId={}", splitId);
        if (StringUtils.isBlank(splitId)) {
            logger.info("splitId 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        List<SplitCommentView> result = null;
        ErpOrderSplitComment comment = new ErpOrderSplitComment();
        comment.setSplitId(splitId);
        // 获取该分单所有评论
        List<ErpOrderSplitComment> commentlist = this.findList(comment);
        if (commentlist != null && commentlist.size() > 0) {
            result = new ArrayList<>();
            SplitCommentView commentView = null;
            SplitCommentQuestionView questionView = null;
            SplitCommentAnswerView answerView = null;
            for (ErpOrderSplitComment splitComment : commentlist) {
                // 初始化评论视图
                commentView = new SplitCommentView();
                commentView.setPromotionScore(splitComment.getPromotionScore());
                commentView.setSplitId(splitId);
                commentView.setServiceScore(splitComment.getServiceScore());
                commentView.setCreateDateStr(DateUtils.formatDate(splitComment.getCreateDate(), DateUtils.YYYY_MM_DD_HH_MM));
                // 添加评论视图
                result.add(commentView);
                // 评论id
                String commentId = splitComment.getId();
                ErpOrderSplitCommentQuestion question = new ErpOrderSplitCommentQuestion();
                question.setCommentId(commentId);
                // 获取评价中所有问题列表
                List<ErpOrderSplitCommentQuestion> questionList = erpOrderSplitCommentQuestionService.findList(question);
                if (questionList != null && questionList.size() > 0) {
                    commentView.setQuestionList(new ArrayList<SplitCommentQuestionView>());
                    for (ErpOrderSplitCommentQuestion splitQuestion : questionList) {
                        // 初始化问题视图
                        questionView = new SplitCommentQuestionView();
                        questionView.setCommentId(commentId);
                        questionView.setContent(splitQuestion.getContent());
                        questionView.setType(splitQuestion.getType());
                        // 评论视图中 添加 问题视图
                        commentView.getQuestionList().add(questionView);
                        // 问题id
                        String questionId = splitQuestion.getId();
                        ErpOrderSplitCommentAnswer answer = new ErpOrderSplitCommentAnswer();
                        answer.setQuestionId(questionId);
                        // 获取问题中所有回答列表
                        List<ErpOrderSplitCommentAnswer> answerList = erpOrderSplitCommentAnswerService.findList(answer);
                        if (answerList != null && answerList.size() > 0) {
                            questionView.setAnswerList(new ArrayList<SplitCommentAnswerView>());
                            for (ErpOrderSplitCommentAnswer splitAnswer : answerList) {
                                // 初始化回答视图
                                answerView = new SplitCommentAnswerView();
                                answerView.setCheckFlag(splitAnswer.getCheckFlag());
                                answerView.setContent(splitAnswer.getContent());
                                answerView.setQuestionId(questionId);
                                // 问题视图中 添加回答视图
                                questionView.getAnswerList().add(answerView);
                            }
                        }

                    }
                }
            }
        }
        logger.info("findSplitCommentViewList end | result.size={}", result == null ? 0 : result.size());
        return result;
    }

}
