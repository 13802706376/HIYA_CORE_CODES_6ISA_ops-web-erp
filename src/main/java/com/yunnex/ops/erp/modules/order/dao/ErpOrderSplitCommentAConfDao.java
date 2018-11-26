package com.yunnex.ops.erp.modules.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitCommentAConf;

/**
 * 分单评价回答配置DAO接口
 * 
 * @author yunnex
 * @version 2018-04-04
 */
@MyBatisDao
public interface ErpOrderSplitCommentAConfDao extends CrudDao<ErpOrderSplitCommentAConf> {

    /**
     * 根据问题配置id获取回答配置信息集合
     *
     * @param questionId
     * @return
     * @date 2018年4月4日
     * @author linqunzhi
     */
    List<ErpOrderSplitCommentAConf> findListByQuestionId(@Param("questionId") String questionId);

}
