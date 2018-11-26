package com.yunnex.ops.erp.modules.workflow.flow.handler;

import static com.yunnex.ops.erp.modules.workflow.flow.handler.ProcessHandlerUtil.notExpired;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
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
 * 售后上门培训付费流程 启动
 * 
 * @author Ejon
 * @date 2018年7月4日
 */
public class AfterSaleVisitProcessHandler implements ProcessHandler {


    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService = SpringContextHolder.getBean(ErpOrderGoodServiceInfoService.class);
    private SysConstantsService sysConstantsService = SpringContextHolder.getBean(SysConstantsService.class);
    private WorkFlow3p25Service workFlow3p25Service = SpringContextHolder.getBean(WorkFlow3p25Service.class);

    @Override
    public void start(List<SplitGoodForm> request, ErpOrderOriginalInfo erpOrderOriginalInfo, Map<String, Object> response,
                    ProcessHandlerChain chain) {

        logger.info("售后上门培训付费服务流程 ,[{}]", request);


        // 售后上门培训付费服务流程服务项
        ErpOrderGoodServiceInfo kclGoodService = erpOrderGoodServiceInfoService.getOrderGoodServiceByOrderIdSingle(erpOrderOriginalInfo.getId(),
                        sysConstantsService.getConstantValByKey(WorkFlowConstants.DELIVERY_ITEM_CODE_VC));
        Map<String, Object> vars = Maps.newHashMap();

        // 订单里的服务项目是售后上门培训（付费版）；
        if (Optional.ofNullable(kclGoodService).isPresent() && notExpired(kclGoodService.getExpirationTime())) {
        	
            vars.put(FlowConstant.SERVICETYPE, DeliveryFlowConstant.SERVICE_TYPE_VC);
            vars.put(FlowConstant.SERVICE_SOURCE_ID, kclGoodService.getId());
            workFlow3p25Service.startDeliveryFlow(request, erpOrderOriginalInfo, vars, kclGoodService);

        }
        logger.info("售后上门培训付费服务流程 结束,[{}]", vars);
        chain.start(request, erpOrderOriginalInfo, response, chain);
    }

}
