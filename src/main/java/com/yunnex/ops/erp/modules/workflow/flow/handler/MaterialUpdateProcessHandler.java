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
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;
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
 * 物料更新服务流程启动
 * 
 * @author Ejon
 * @date 2018年7月4日
 */
public class MaterialUpdateProcessHandler implements ProcessHandler {


    protected Logger logger = LoggerFactory.getLogger(getClass());
    private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService = SpringContextHolder.getBean(ErpOrderGoodServiceInfoService.class);
    private SysConstantsService sysConstantsService = SpringContextHolder.getBean(SysConstantsService.class);
    private WorkFlow3p25Service workFlow3p25Service = SpringContextHolder.getBean(WorkFlow3p25Service.class);
    private ErpOrderMaterialCreationService erpOrderMaterialCreationService = SpringContextHolder.getBean(ErpOrderMaterialCreationService.class);
    

    @Override
    public void start(List<SplitGoodForm> request, ErpOrderOriginalInfo erpOrderOriginalInfo, Map<String, Object> response,
                    ProcessHandlerChain chain) {

        logger.info("物料更新服务服务流程 ,[{}]", request);

        // 物料升级套餐 服务项
        ErpOrderGoodServiceInfo muGoodService = erpOrderGoodServiceInfoService.getOrderGoodServiceByOrderIdSingle(erpOrderOriginalInfo.getId(),
                        sysConstantsService.getConstantValByKey(WorkFlowConstants.DELIVERY_ITEM_CODE_MU));
        Map<String, Object> vars = Maps.newHashMap();
        vars.put(FlowConstant.SERVICE_SOURCE_ID, muGoodService.getId());
        if (Optional.ofNullable(muGoodService).isPresent() && notExpired(muGoodService.getExpirationTime())) {
            vars.put(FlowConstant.SERVICETYPE, DeliveryFlowConstant.SERVICE_TYPE_MU);
            workFlow3p25Service.startDeliveryFlow(request, erpOrderOriginalInfo, vars, muGoodService);
            //插入物料表流程id
            String procInsId=vars.get(FlowConstant.PROCINSID)+StringUtils.EMPTY;
            ErpOrderMaterialCreation erpOrderMaterialCreation = erpOrderMaterialCreationService.findByYsOrderId(erpOrderOriginalInfo.getYsOrderId());
            if (erpOrderMaterialCreation == null) {
                erpOrderMaterialCreation = new ErpOrderMaterialCreation();
                erpOrderMaterialCreation.setProcInsId(procInsId);
                erpOrderMaterialCreation.setYsOrderId(erpOrderOriginalInfo.getYsOrderId());
            }
            erpOrderMaterialCreationService.save(erpOrderMaterialCreation);
        }

        logger.info("物料更新服务流程 结束,[{}]", request);
        chain.start(request, erpOrderOriginalInfo, response, chain);
    }

}
