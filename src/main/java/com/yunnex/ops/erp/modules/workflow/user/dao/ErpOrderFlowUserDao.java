package com.yunnex.ops.erp.modules.workflow.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.statistics.entity.ErpTeamFollowOrder;
import com.yunnex.ops.erp.modules.workflow.user.entity.ErpOrderFlowUser;

/**
 * 工作流人员关系DAO接口
 * 
 * @author Frank
 * @version 2017-10-27
 */
@MyBatisDao
public interface ErpOrderFlowUserDao extends CrudDao<ErpOrderFlowUser> {

    ErpOrderFlowUser findListByFlowId(@Param("procInsId") String procInsId,@Param("flowUserId") String flowUserId);
    
    void changeRoleUser(@Param("procInsId") String procInsId,@Param("userId") String userId,@Param("roleName") String roleName);
    
    ErpOrderFlowUser findBySplitIdAndFlowUser(@Param("splitId") String splitId, @Param("flowUserId") String flowUserId);

    List<ErpOrderFlowUser> findListByFlowIdAndUserId(@Param("procInsId")String procInsId,@Param("userId") String userId);
    
    ErpOrderFlowUser findByProcInsIdAndRoleName(@Param("procInsId") String procInsId, @Param("roleName") String roleName);

    List<ErpOrderFlowUser> findtest();
    
    List<ErpOrderFlowUser> findstatistics(@Param("userId") String userId, @Param("orderNum") String orderNum,
    		@Param("shopName") String shopName, @Param("orderType") String orderType, @Param("starDate") String starDate, @Param("endDate") String endDate);
    
    List<ErpOrderFlowUser> findstatistics2(@Param("userId") String userId, @Param("orderNum") String orderNum,
    		@Param("shopName") String shopName, @Param("orderType") String orderType, @Param("starDate") String starDate, @Param("endDate") String endDate);
    
    List<ErpOrderFlowUser> findstatisticswhereteamId(@Param("teamId") String teamId, @Param("orderNum") String orderNum,
    		@Param("shopName") String shopName, @Param("orderType") String orderType, @Param("starDate") String starDate, @Param("endDate") String endDate);
	
    List<ErpOrderFlowUser> findstatisticswhereteamId2(@Param("teamId") String teamId, @Param("orderNum") String orderNum,
    		@Param("shopName") String shopName, @Param("orderType") String orderType, @Param("starDate") String starDate, @Param("endDate") String endDate);
	
    List<ErpOrderFlowUser> findstatisticswhereuserId(@Param("userId") String userId, @Param("orderNum") String orderNum,
    		@Param("shopName") String shopName, @Param("orderType") String orderType, @Param("starDate") String starDate, @Param("endDate") String endDate);
 
    List<ErpOrderFlowUser> findoverTimestatistics(@Param("userId") String userId, @Param("orderNum") String orderNum,
    		@Param("shopName") String shopName, @Param("orderType") String orderType, @Param("starDate") String starDate, @Param("endDate") String endDate);
    
    List<ErpOrderFlowUser> findoverTimewhereteamId(@Param("teamId") String teamId, @Param("orderNum") String orderNum,
    		@Param("shopName") String shopName, @Param("orderType") String orderType, @Param("starDate") String starDate, @Param("endDate") String endDate);
    
    List<ErpOrderFlowUser> findstatisticsReport(@Param("userId") String userId, @Param("starDate") String starDate, @Param("endDate") String endDate);
    
    List<ErpOrderFlowUser> findstatisticsReportOnline(@Param("userId") String userId, @Param("starDate") String starDate, @Param("endDate") String endDate);
    
    List<ErpOrderFlowUser> findstatisticsReportNoCancel(@Param("userId") String userId);
    
    List<ErpTeamFollowOrder> findstatisticsFollowOrder(@Param("userId") String userId);
    List<ErpTeamFollowOrder> findstatisticsFollowOrderWhereTeamId(@Param("teamId") String teamId);
    
    /**
     * 获取订单周/月统计
     *
     * @param userIds
     * @param startDate
     * @param endDate
     * @return
     * @date 2018年3月19日
     * @author SunQ
     */
    Map<String, Object> statisticsWeekAndMonth(@Param("userIds") List<String> userIds, @Param("startDate") String startDate, @Param("endDate") String endDate);


    List<Map<String, String>> findByProcInsId(@Param("procInsId") String procInsId);

    /**
     * 业务定义：复制流程处理人
     * 
     * @date 2018年8月31日
     * @author R/Q
     */
    void copyFlowUsers(@Param("oldProcInsId") String oldProcInsId, @Param("newProcInsId") String newProcInsId);

    /**
     * 业务定义：依据流程实例ID删除对应流程处理人
     * 
     * @date 2018年9月6日
     * @author R/Q
     */
    void deleteByProcInsId(@Param("procInsId") String procInsId);

    List<ErpOrderFlowUser> findByOrderId(String orderId);

    /**
     * 业务定义：根据订单ID获取对应交付服务最新指定的流程节点处理人
     * 
     * @date 2018年10月18日
     * @author R/Q
     */
    List<ErpOrderFlowUser> getDeliveryFlowUserByOrderId(@Param("orderId") String orderId);
}
