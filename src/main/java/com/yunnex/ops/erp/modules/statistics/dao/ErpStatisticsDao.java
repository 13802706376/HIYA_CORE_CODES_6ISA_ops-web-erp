package com.yunnex.ops.erp.modules.statistics.dao;


import java.util.List;

import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.statistics.dto.SplitStatisticsRequestDto;
import com.yunnex.ops.erp.modules.statistics.entity.SplitStatistics;


/**
 * erp 统计 dao
 * 
 * @author linqunzhi
 * @date 2018年5月9日
 */
@MyBatisDao
public interface ErpStatisticsDao {

    /**
     * 获取分单明细分页列表
     *
     * @param dto
     * @return
     * @date 2018年5月8日
     * @author linqunzhi
     */
    public List<SplitStatistics> findSplitStatistics(SplitStatisticsRequestDto dto);

    /**
     * 根据查询条件获取总条数
     *
     * @param dto
     * @return
     * @date 2018年5月8日
     * @author linqunzhi
     */
    public int countBySplitStatisticsRequestDto(SplitStatisticsRequestDto dto);

    /**
     * 上线订单平均周期（除存在过资质问题和商户曾经主动要求延迟上线的订单,保留3位小数）
     *
     * @param dto
     * @return
     * @date 2018年5月15日
     * @author linqunzhi
     */
    public double getOnlineAvgCycle(SplitStatisticsRequestDto dto);

}
