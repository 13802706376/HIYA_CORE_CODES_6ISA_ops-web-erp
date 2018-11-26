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
import com.yunnex.ops.erp.modules.workflow.flow.asyevent.AsyUpgradeCombo;
import com.yunnex.ops.erp.modules.workflow.flow.common.WorkFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlow3p25Service;

/**
 * 首次营销策划流程启动
 * 
 * @author Ejon
 * @date 2018年7月4日
 */
public class DeliveryFlowfirstPlanProcessHandler implements ProcessHandler {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService = SpringContextHolder.getBean(ErpOrderGoodServiceInfoService.class);
    private SysConstantsService sysConstantsService = SpringContextHolder.getBean(SysConstantsService.class);
    private WorkFlow3p25Service workFlow3p25Service = SpringContextHolder.getBean(WorkFlow3p25Service.class);
    private AsyUpgradeCombo asyUpgradeCombo = SpringContextHolder.getBean(AsyUpgradeCombo.class);
    @Override
    public void start(List<SplitGoodForm> request, ErpOrderOriginalInfo erpOrderOriginalInfo, Map<String, Object> response,
                    ProcessHandlerChain chain) {

        logger.info("首次营销策划服务流程 ,[{}]", request);

        // 掌贝平台交付服务
        ErpOrderGoodServiceInfo zbGoodService = erpOrderGoodServiceInfoService.getOrderGoodServiceByOrderIdSingle(erpOrderOriginalInfo.getId(),
                        sysConstantsService.getConstantValByKey(WorkFlowConstants.DELIVERY_ITEM_CODE_ZB));
        // 首次营销策划服务项
        ErpOrderGoodServiceInfo kclGoodService = erpOrderGoodServiceInfoService.getOrderGoodServiceByOrderIdSingle(erpOrderOriginalInfo.getId(),
                        sysConstantsService.getConstantValByKey(WorkFlowConstants.DELIVERY_ITEM_CODE_KCL));
        // 智慧餐厅
        ErpOrderGoodServiceInfo zhctGoodService = erpOrderGoodServiceInfoService.getOrderGoodServiceByOrderIdSingle(erpOrderOriginalInfo.getId(),
                        sysConstantsService.getConstantValByKey(WorkFlowConstants.DELIVERY_ITEM_CODE_ZHCT));

        
        Map<String, Object> vars = Maps.newHashMap();

        // 订单里的商品包含服务项目“掌贝平台交付服务”和“首次营销策划服务”；
        if (Optional.ofNullable(zbGoodService).isPresent() && notExpired(zbGoodService.getExpirationTime()) && Optional.ofNullable(kclGoodService)
                        .isPresent() && notExpired(kclGoodService.getExpirationTime())) {
        	//SERVICE_TYPE_KE_CHANG_LAI=首次营销FMPS
            vars.put(FlowConstant.SERVICETYPE, DeliveryFlowConstant.SERVICE_TYPE_KE_CHANG_LAI);
            
            vars.put(FlowConstant.SERVICE_SOURCE_ID, zbGoodService.getId()+Constant.COMMA+kclGoodService.getId());
            vars.put(DeliveryFlowConstant.ZHCT_FLAG, Constant.NO );
            
            if(Optional.ofNullable(zhctGoodService).isPresent() && notExpired(zhctGoodService.getExpirationTime())){
                vars.put(DeliveryFlowConstant.ZHCT_FLAG, Constant.YES );  
                
                //区分是“交付服务流程”里面的还是“智慧餐厅安装交付（老商户）流程”  智慧餐厅 交付 服务 流程SERVICE_TYPE_KE_ZHCT = "ZHCT";
                vars.put(DeliveryFlowConstant.ZHCT_ACT_TYPE, DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT); 
                
                //如果包含智慧餐厅需要把智慧餐厅的服务来源id保存到变量中
                String serviceSourceIdStr = (String) vars.get(FlowConstant.SERVICE_SOURCE_ID);
                vars.put(FlowConstant.SERVICE_SOURCE_ID, serviceSourceIdStr+Constant.COMMA+zhctGoodService.getId());
                
            }
            
            String zhctFlag = (String) vars.get(DeliveryFlowConstant.ZHCT_FLAG);
            if(Constant.YES.equals(zhctFlag)) {
            	 workFlow3p25Service.startDeliveryFlow(request, erpOrderOriginalInfo, vars, kclGoodService, zbGoodService,zhctGoodService);
            }else {
            	workFlow3p25Service.startDeliveryFlow(request, erpOrderOriginalInfo, vars, kclGoodService, zbGoodService);
            }
            
            response.put(FlowConstant.HAS_START_FLOW, Constant.YES);
            //异步判断要不要套餐升级
            String procInsId= vars.get(FlowConstant.PROCINSID)+Constant.BLANK;
            asyUpgradeCombo.upCombo(erpOrderOriginalInfo,procInsId,zhctFlag);
        }

        logger.info("首次营销策划服务流程 结束,[{}]", vars);
        chain.start(request, erpOrderOriginalInfo, response, chain);
    }

}
