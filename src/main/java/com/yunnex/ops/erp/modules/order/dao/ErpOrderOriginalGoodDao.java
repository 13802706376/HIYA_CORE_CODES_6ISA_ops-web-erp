package com.yunnex.ops.erp.modules.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalGood;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;

/**
 * 订单商品DAO接口
 * 
 * @author huanghaidong
 * @version 2017-10-24
 */
@MyBatisDao
public interface ErpOrderOriginalGoodDao extends CrudDao<ErpOrderOriginalGood> {

    List<ErpOrderOriginalGood> findListByOrderId(@Param("orderId") String orderId);
    
    List<ErpOrderOriginalGood> findListByOrderInfo(@Param("orderId") String orderId, @Param("goodType") Integer goodType);
    List<ErpOrderOriginalGood> getListOriginalGood(@Param("orderId") String orderId);
    
    int decreasePendingNum(@Param("id") String id, @Param("num") Integer num);
    
    int decreaseProcessNum(@Param("id") String id, @Param("num") Integer num);
    
    Integer findJykPendingGoodNumByOrderId(@Param("orderId") String orderId);

    /**
     * 通过订单ID删除订单关联的商品信息
     *
     * @param id
     * @return
     * @date 2017年11月28日
     * @author SunQ
     */
    int deleteByOrderId(@Param("orderId") String id);

    /**
     * 获取订单的聚引客和客常来服务类型
     * 
     * @param orderId
     * @return
     */
    List<ErpOrderOriginalGood> findJykAndKclDistinct(String orderId);

    /**
     * 业务定义：批量插入订单商品信息
     * 
     * @date 2018年7月3日
     * @author R/Q
     */
    void batchInsert(@Param("erpOrderOriginalGoods") List<ErpOrderOriginalGood> erpOrderOriginalGoods, @Param("orderId") String orderId);
    
    /**
     * 业务定义：根据订单ID查询商品信息
     * 
     * @date 2018年7月5日
     * @author R/Q
     */
    List<SplitGoodForm> queryGoodFormList(@Param("orderId") String orderId);

    /**
     * 业务定义：重置商品处理数量
     * 
     * @date 2018年8月31日
     * @author R/Q
     */
    void resetPendingNum(@Param("splitGoodLists") List<SplitGoodForm> splitGoodLists);

}
