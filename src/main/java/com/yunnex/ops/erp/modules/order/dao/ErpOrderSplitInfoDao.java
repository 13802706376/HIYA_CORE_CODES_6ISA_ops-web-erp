package com.yunnex.ops.erp.modules.order.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;

/**
 * 分单DAO接口
 * @author huanghaidong
 * @version 2017-10-24
 */
@MyBatisDao
public interface ErpOrderSplitInfoDao extends CrudDao<ErpOrderSplitInfo> {

    int countByOrderId(@Param("orderId") String orderId);

    List<ErpOrderSplitInfo> findListByOrderId(@Param("orderId") String orderId);

	List<ErpOrderSplitInfo> findListByOrderInfo(@Param("orderId") String orderId, @Param("goodType") Integer goodType);
	
    int updateNum(@Param("id") String id, @Param("num") Integer num);
	
    int updateHurryFlag(@Param("id") String id, @Param("hurryFlag") Integer hurryFlag);
    
    List<ErpOrderSplitInfo> findListByParams(ErpOrderSplitInfo entity);
    
    List<String> findFollowOrderByParams(ErpOrderSplitInfo erpOrderSplitInfo);
            
    ErpOrderSplitInfo getByProcInstId(String procInsId);

	List<ErpOrderSplitInfo> getBystate(@Param("status") Integer status,@Param("userId") String userId);
	
	List<ErpOrderSplitInfo> findcomplete(@Param("status") Integer status,@Param("del") String del,@Param("orderNumber") String orderNumber,@Param("splitId")String splitId,@Param("shopId")String shopId,@Param("hurryFlag") Integer hurryFlag);

	List<ErpOrderSplitInfo> findListByOrderInfoAndUser(@Param("orderId") String orderId, @Param("goodType") Integer goodType);

    List<ErpOrderSplitInfo> getByOrderId(@Param("orderId") String orderId);

    List<ErpOrderSplitInfo> findByOrderId(String orderId);
    
    Integer WhereShopIdCount(@Param("shopid") String shopid, @Param("date") String date,@Param("del") String del);
    
    /**
     * 使用语句匹配订单和任务的关系，提高任务列表的查询效率
     * @param entity
     * @param userIds
     * @return
     */
    List<ErpOrderSplitInfo> findListOrderInfoAndTask(@Param("erpOrderSplitInfo") ErpOrderSplitInfo entity, @Param("userIds") List<String> userIds);
    
    /**
     * 获取到商户相关的分单任务
     *
     * @param shopId
     * @return
     * @date 2018年1月10日
     * @author SunQ
     */
    List<String> findProcIdListByShopId(@Param("shopId") String shopId);
    
    ErpOrderSplitInfo getOrderSplitInfo(String splitId);

    Date getPromotionEndTime(String splitId);
    
    boolean publishToWxapp(String splitId);
    
    /*V3.2
     * List<ErpOrderSplitInfo> promotionalMaterials(@Param("orderNumber") String orderNumber, @Param("shopName") String shopName,
                    @Param("status") String status, @Param("planningExpert") String planningExpert);*/
    List<ErpOrderSplitInfo> promotionalMaterials(@Param("orderNumber") String orderNumber, @Param("shopName") String shopName);
    Map<String, String> getDiagnosisTaskInfo(@Param("splitId")String splitId);
    //正在进行的订单
    Integer findWhereUnderway(@Param("userIds") List<String> userIds);
    //资质问题进行中订单
    Integer findWhereQualifications(@Param("userIds") List<String> userIds);
    //有过资质问题进行中订单
    Integer findAllQualifications(@Param("userIds") List<String> userIds);
    //主动延迟的订单
    Integer findWhereActiveDelay(@Param("userIds") List<String> userIds);
    //有过主动延迟的订单
    Integer findAllActiveDelay(@Param("userIds") List<String> userIds);
    //超时风险订单
    Integer findWhereRiskCount(@Param("userIds") List<String> userIds);
    //订单总数
    Integer findstatisticsReportNoCancel(@Param("userIds") List<String> userIds);
    //订单总数
    Integer findstatisticsReportCompete(@Param("userIds") List<String> userIds);
    // 正常跟进订单数
    Integer findNormalOrders(@Param("userIds") List<String> userIds);
    
    List<ErpOrderSplitInfo> findWherefollowOrder(@Param("assignne") String assignne);
    
    List<ErpOrderSplitInfo> findWheretaskOrder(@Param("assignne") String assignne);

    Integer updatePlanningExpert(@Param("planningExpert") String planningExpert, @Param("splitId") String splitId);

    void updatePromotionTime(@Param("promotionTime") Date promotionTime, @Param("id") String id);
   
    Integer deleteSplitInfoById(@Param("id") String id);

    List<ErpOrderSplitInfo> findByOrderNumber(String orderNumber);

    void updateExceptionReason(@Param("exception") String exception, @Param("status") Integer status, @Param("ids") List ids);
}
