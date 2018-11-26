package com.yunnex.ops.erp.modules.statistics.dao;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.statistics.dto.deliveryService.DeliveryServiceDetailRequestDto;

@MyBatisDao
public interface DeliveryServiceDetailDao {
	/**
     * 获取生产服务订单信息列表
     *
     * @param dto
     * @return
     * @date 2018年5月28日
     * @author linqunzhi
     */
    List<DeliveryServiceDetailRequestDto> getPageServiceDetail(DeliveryServiceDetailRequestDto dto);
}
