package com.yunnex.ops.erp.modules.workflow.flow.handler;
import static com.yunnex.ops.erp.modules.workflow.flow.handler.ProcessHandlerUtil.notExpired;
import static com.yunnex.ops.erp.modules.workflow.flow.handler.ProcessHandlerUtil.positiveInteger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.order.constant.OrderConstants;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderGoodServiceInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;
import com.yunnex.ops.erp.modules.order.service.ErpOrderGoodServiceInfoService;
import com.yunnex.ops.erp.modules.sys.service.SysConstantsService;
import com.yunnex.ops.erp.modules.workflow.flow.common.WorkFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlow3p25Service;

/**
 * 智慧餐厅首次服务 流程启动
 * 
 * @author Ejon
 * @date 2018年7月4日
 */
public class WisdomShopProcessHandler implements ProcessHandler {


    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService = SpringContextHolder.getBean(ErpOrderGoodServiceInfoService.class);
    private SysConstantsService sysConstantsService = SpringContextHolder.getBean(SysConstantsService.class);
    private WorkFlow3p25Service workFlow3p25Service = SpringContextHolder.getBean(WorkFlow3p25Service.class);

    @Override
    public void start(List<SplitGoodForm> request, ErpOrderOriginalInfo erpOrderOriginalInfo, Map<String, Object> response,
                    ProcessHandlerChain chain) {

        logger.info("智慧餐厅首次服务   流程 ,[{}]", request);

        // 客常来商品
        long kclCount = request.stream().filter(good -> OrderConstants.GOOD_TYPE_ID_KCL == Optional.ofNullable(good.getGoodTypeId()).orElse(0L))
                        .count();
        // 智慧餐厅首次服务项
        ErpOrderGoodServiceInfo wisdomShopGoodService = erpOrderGoodServiceInfoService.getOrderGoodServiceByOrderIdSingle(erpOrderOriginalInfo.getId(),
                        sysConstantsService.getConstantValByKey(WorkFlowConstants.DELIVERY_ITEM_CODE_ZHCT));

        Map<String, Object> vars = Maps.newHashMap();

        // 订单里有商品的业务类型是客常来，对应的服务项目有智慧餐厅落地服务；
        if (positiveInteger(kclCount) && Optional.ofNullable(wisdomShopGoodService)
                        .isPresent() && notExpired(wisdomShopGoodService.getExpirationTime())) {
            vars.put(FlowConstant.SERVICETYPE, DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT);
            workFlow3p25Service.startDeliveryFlow(request, erpOrderOriginalInfo, vars, wisdomShopGoodService);

        }

        logger.info("智慧餐厅首次服务    结束,[{}]", vars);
        chain.start(request, erpOrderOriginalInfo, response, chain);
    }



}
