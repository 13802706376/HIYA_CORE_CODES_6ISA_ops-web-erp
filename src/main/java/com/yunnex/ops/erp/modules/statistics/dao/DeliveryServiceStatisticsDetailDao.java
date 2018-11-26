package com.yunnex.ops.erp.modules.statistics.dao;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsDetailDto;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsRequestDto;
import com.yunnex.ops.erp.modules.statistics.entity.DeliveryServiceStatistics;

/**
 * 生产服务报表dao
 * 
 * @author linqunzhi
 * @date 2018年5月28日
 */
@MyBatisDao
public interface DeliveryServiceStatisticsDetailDao {

    /**
     * 获取生产服务订单信息列表
     *
     * @param dto
     * @return
     * @date 2018年5月28日
     * @author linqunzhi
     */
    List<DeliveryServiceStatistics> findStatistics(DeliveryServiceStatisticsDetailDto dto);
    
    /**
     * 获取生产服务订单信息列表
     *
     * @param dto
     * @return
     * @date 2018年5月28日
     * @author linqunzhi
     */
    List<DeliveryServiceStatistics> findStatisticsByUser(DeliveryServiceStatisticsDetailDto dto);
    
    List<DeliveryServiceStatistics> findStatisticsByUser2(DeliveryServiceStatisticsDetailDto dto);
    
    /**
     * 获取生产服务订单信息列表
     *
     * @param dto
     * @return
     * @date 2018年5月28日
     * @author linqunzhi
     */
    List<DeliveryServiceStatistics> findStatisticsComplete(DeliveryServiceStatisticsDetailDto dto);
}
