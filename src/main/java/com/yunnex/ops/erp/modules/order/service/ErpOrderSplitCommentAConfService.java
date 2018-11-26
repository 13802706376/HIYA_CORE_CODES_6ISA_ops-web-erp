package com.yunnex.ops.erp.modules.order.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSplitCommentAConfDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitCommentAConf;

/**
 * 分单评价回答配置Service
 * 
 * @author yunnex
 * @version 2018-04-04
 */
@Service
public class ErpOrderSplitCommentAConfService extends CrudService<ErpOrderSplitCommentAConfDao, ErpOrderSplitCommentAConf> {

    public ErpOrderSplitCommentAConf get(String id) {
        return super.get(id);
    }

    public List<ErpOrderSplitCommentAConf> findList(ErpOrderSplitCommentAConf erpOrderSplitCommentAConf) {
        return super.findList(erpOrderSplitCommentAConf);
    }

    public Page<ErpOrderSplitCommentAConf> findPage(Page<ErpOrderSplitCommentAConf> page, ErpOrderSplitCommentAConf erpOrderSplitCommentAConf) {
        return super.findPage(page, erpOrderSplitCommentAConf);
    }

    @Transactional(readOnly = false)
    public void save(ErpOrderSplitCommentAConf erpOrderSplitCommentAConf) {
        super.save(erpOrderSplitCommentAConf);
    }

    @Transactional(readOnly = false)
    public void delete(ErpOrderSplitCommentAConf erpOrderSplitCommentAConf) {
        super.delete(erpOrderSplitCommentAConf);
    }

    /**
     * 根据问题配置id获取回答配置信息集合
     *
     * @param questionId
     * @return
     * @date 2018年4月4日
     * @author linqunzhi
     */
    public List<ErpOrderSplitCommentAConf> findListByQuestionId(String questionId) {
        logger.info("findListByQuestionId start | questionId={}", questionId);
        if (StringUtils.isBlank(questionId)) {
            logger.info("questionId 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        List<ErpOrderSplitCommentAConf> result = dao.findListByQuestionId(questionId);
        logger.info("findListByQuestionId end | result.size={}", result == null ? 0 : result.size());
        return result;
    }

}
