package com.yunnex.ops.erp.modules.order.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.api.constants.CommonCode;
import com.yunnex.ops.erp.api.dto.request.OrderMaterialRequestDto;
import com.yunnex.ops.erp.api.facade.order.ErpOrderMaterialFacade;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderMaterialApiService;

import yunnex.common.core.dto.ApiResult;

/**
 * 对外订单物料接口实现
 */
@Service
public class ErpOrderMaterialFacadeImpl implements ErpOrderMaterialFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpOrderMaterialFacadeImpl.class);
    private static final String SYNC_MSG = "根据物料订单ID获取订单的物料制作状态";
    private static final String PUSH_MSG = "易商向本系统推送订单物料信息";

    @Autowired
    private ErpOrderMaterialCreationService orderMaterialCreationService;
    @Autowired
    private ErpOrderMaterialApiService orderMaterialApiService;

    /**
     * 向本系统推送订单物料信息
     * @param requestDto 订单物料内容
     * @return
     */
    @Override
    public ApiResult<Void> pushOrderMaterial(OrderMaterialRequestDto requestDto) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{}:{}", PUSH_MSG, JSON.toJSONString(requestDto));
        }
        orderMaterialApiService.saveOne(requestDto);
        return ApiResult.build();
    }

    /**
     * 获取本系统的订单物料状态
     * @param ysOrderId 物料订单ID
     * @return
     */
    @Override
    public ApiResult<String> syncOrderMaterialStatus(Long ysOrderId) {
        LOGGER.info("{}入参：ysOrderId={}", SYNC_MSG, ysOrderId);
        ApiResult<String> result = ApiResult.build();
        if (ysOrderId == null) {
            result.error(CommonCode.INVALID_PARAM.getCode(), "订单ID不能为空！");
            return result;
        }

        ErpOrderMaterialCreation orderMaterialCreation = orderMaterialCreationService.findByYsOrderId(ysOrderId);
        if (orderMaterialCreation == null) {
            CommonCode code = CommonCode.RESOURCE_NOT_EXISTS;
            result.error(code.getCode(), code.getMessage());
            return result;
        }
        String status = orderMaterialCreation.getStatus();
        result.setEntry(status);
        LOGGER.info("{}结果：status={}", SYNC_MSG, status);
        return result;
    }

}
