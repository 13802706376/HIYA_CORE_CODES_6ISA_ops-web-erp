package com.yunnex.ops.erp.modules.workflow.flow.handler;

import static com.yunnex.ops.erp.modules.workflow.flow.handler.ProcessHandlerUtil.notExpired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderGoodServiceInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;
import com.yunnex.ops.erp.modules.order.service.ErpOrderGoodServiceInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.sys.service.SysConstantsService;
import com.yunnex.ops.erp.modules.workflow.enums.FlowServiceType;
import com.yunnex.ops.erp.modules.workflow.flow.common.WorkFlowConstants;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowVariableConstants;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlow3p25Service;

/**
 * 智慧餐厅（老商户）流程 启动
 * 
 * @author Ejon
 * @date 2018年7月4日
 */
public class ZhctOldShopProcessHandler implements ProcessHandler {

	//掌贝进件开通状态  2代表开通
    public static final int ZHANGBEI_OPEN = 2;

	protected Logger logger = LoggerFactory.getLogger(getClass());

    private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService = SpringContextHolder.getBean(ErpOrderGoodServiceInfoService.class);
    private SysConstantsService sysConstantsService = SpringContextHolder.getBean(SysConstantsService.class);
    private WorkFlow3p25Service workFlow3p25Service = SpringContextHolder.getBean(WorkFlow3p25Service.class);
    private ErpShopInfoService erpShopInfoService = SpringContextHolder.getBean(ErpShopInfoService.class);

    @Override
    public void start(List<SplitGoodForm> request, ErpOrderOriginalInfo erpOrderOriginalInfo, Map<String, Object> response,
                    ProcessHandlerChain chain) {

        logger.info("智慧餐厅老商户服务流程 ,[{}]", request);


        // 智慧餐厅安装交付服务
        ErpOrderGoodServiceInfo zhctGoodService = erpOrderGoodServiceInfoService.getOrderGoodServiceByOrderIdSingle(erpOrderOriginalInfo.getId(),
                        sysConstantsService.getConstantValByKey(WorkFlowConstants.DELIVERY_ITEM_CODE_ZHCT));
        // 掌贝平台交付服务
        ErpOrderGoodServiceInfo zbGoodService = erpOrderGoodServiceInfoService.getOrderGoodServiceByOrderIdSingle(erpOrderOriginalInfo.getId(),
                        sysConstantsService.getConstantValByKey(WorkFlowConstants.DELIVERY_ITEM_CODE_ZB));
        
        //获取商户信息
        String zhangbeiId = erpOrderOriginalInfo.getShopId();
        ErpShopInfo shopInfo = erpShopInfoService.findByZhangbeiId(zhangbeiId);
              
        Map<String, Object> vars = Maps.newHashMap();

        /*
          *  智慧餐厅老商户流程启动条件
          *  1.服务项目不包含“掌贝平台交付服务”；
          *  2.服务项目包含“智慧餐厅安装交付服务”；
          *  3.老商户（掌贝进件已开通）
         */
        if (Optional.ofNullable(zhctGoodService).isPresent() && notExpired(zhctGoodService.getExpirationTime())
        		&&!Optional.ofNullable(zbGoodService).isPresent()
        		&&erpOrderOriginalInfo.getIsNewShop().equals("N")
        		&&shopInfo.getZhangbeiState().intValue() == ZHANGBEI_OPEN
        		){
        	
        	//zhctIsOldShop=ZHCT_OLD 区分智慧餐厅走那条类型的流程
        	vars.put(DeliveryFlowConstant.ZHCT_ACT_TYPE, DeliveryFlowConstant.SERVICE_TYPE_KE_ZHCT_OLD); 
        	
        	vars.put(FlowConstant.SERVICETYPE, "");
            vars.put(FlowConstant.SERVICE_SOURCE_ID, zhctGoodService.getId());
            
            // 流程中包含的服务集合
            List<String> goodServieTypeList = new ArrayList<>();
            goodServieTypeList.add(FlowServiceType.ZHI_HUI_CAN_TING.getType());
            vars.put(FlowVariableConstants.GOOD_SERVIE_TYPE_LIST, goodServieTypeList);
            workFlow3p25Service.startDeliveryFlow(request, erpOrderOriginalInfo, vars, zhctGoodService);
        }
        logger.info("智慧餐厅老商户服务流程 结束,[{}]", vars);
        chain.start(request, erpOrderOriginalInfo, response, chain);
    }

}
