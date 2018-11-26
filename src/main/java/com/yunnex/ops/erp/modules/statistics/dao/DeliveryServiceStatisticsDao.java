package com.yunnex.ops.erp.modules.statistics.dao;

import java.util.List;
import java.util.Map;

import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceStatisticsRequestDto;
import com.yunnex.ops.erp.modules.statistics.entity.DeliveryServiceStatistics;
import com.yunnex.ops.erp.modules.team.entity.ErpUserTotal;

/**
 * 生产服务报表dao
 * 
 * @author linqunzhi
 * @date 2018年5月28日
 */
@MyBatisDao
public interface DeliveryServiceStatisticsDao {

    /**
     * 获取生产服务订单信息列表
     *
     * @param dto
     * @return
     * @date 2018年5月28日
     * @author linqunzhi
     */
    List<DeliveryServiceStatistics> findStatistics(DeliveryServiceStatisticsRequestDto dto);
    
    List<Map> findFlow(ErpUserTotal erpUserTotal);
}
