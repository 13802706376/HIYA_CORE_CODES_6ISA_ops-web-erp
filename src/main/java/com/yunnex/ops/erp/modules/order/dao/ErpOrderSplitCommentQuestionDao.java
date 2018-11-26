package com.yunnex.ops.erp.modules.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitCommentQuestion;

/**
 * 分单评价问题DAO接口
 * @author yunnex
 * @version 2018-04-04
 */
@MyBatisDao
public interface ErpOrderSplitCommentQuestionDao extends CrudDao<ErpOrderSplitCommentQuestion> {
    
   List<ErpOrderSplitCommentQuestion> getCommentAnswerByComIdAndType (@Param("commentId")String commentId,@Param("type")String type);
	
}