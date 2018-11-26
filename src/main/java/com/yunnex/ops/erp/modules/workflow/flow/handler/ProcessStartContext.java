package com.yunnex.ops.erp.modules.workflow.flow.handler;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;

/**
 * 流程启动上下文
 * 
 * @author Ejon
 * @date 2018年7月4日
 */
public class ProcessStartContext {



    /**
     * 贝虎订单同步，启动流程
     *
     * @param erpOrderOriginalInfo
     * @param splitGoodLists
     * @date 2018年7月4日
     * @author zjq
     */
    public static void startByBeihuOrder(ErpOrderOriginalInfo erpOrderOriginalInfo, List<SplitGoodForm> splitGoodLists) {
    	Map<String, Object> vars = Maps.newHashMap();
        vars.put(ProcessHandler.FIRST, true);
        
        ProcessHandlerChain handlerChain = new ProcessHandlerChain();
        // 聚引客生产
        handlerChain.addHandler(JykProduceProcessHandler::new)
                        // 首次营销策划
                        .addHandler(DeliveryFlowfirstPlanProcessHandler::new)
                        // 首次上门服务（基础版）
                        .addHandler(DeliveryFlowfirstPlanBasicProcessHandler::new)
                        // 聚引客交付
                        .addHandler(JykDeliveryProcessHandler::new)
                        // 掌贝平台交付服务(进件流程)
                        .addHandler(DataInputProcessHandler::new)
                        // 售后上门培训付费
                        .addHandler(AfterSaleVisitProcessHandler::new)
                        //智慧餐厅老商户
                        .addHandler(ZhctOldShopProcessHandler::new)
                        .start(splitGoodLists, erpOrderOriginalInfo, vars, handlerChain);
    }

    /**
     * ERP新增订单，启动流程
     *
     * @param erpOrderOriginalInfo
     * @param splitGoodLists
     * @date 2018年7月4日
     * @author zjq
     */
    public static void startByErpOrder(ErpOrderOriginalInfo erpOrderOriginalInfo, List<SplitGoodForm> splitGoodLists) {
    	 Map<String, Object> vars = Maps.newHashMap();
         vars.put(ProcessHandler.FIRST, true);
        
         ProcessHandlerChain handlerChain = new ProcessHandlerChain();
                        // 聚引客生产
         handlerChain.addHandler(JykProduceProcessHandler::new)
                        // 聚引客交付
                        .addHandler(JykDeliveryProcessHandler::new)
                        // 售后上门培训付费
                        .addHandler(AfterSaleVisitProcessHandler::new)
                        .start(splitGoodLists, erpOrderOriginalInfo, vars, handlerChain);
    }

    /**
     * 
     * 订单详情页面聚引客拆单,启动流程(折单)
     * 
     * @param erpOrderOriginalInfo
     * @param splitGoodLists
     * @date 2018年7月12日
     * @author zjq
     */
    public static ErpOrderSplitInfo startByOrderSplit(List<SplitGoodForm> splitGoodLists) {
	    Map<String, Object> vars = Maps.newHashMap();
        vars.put(ProcessHandler.FIRST, false);
        
        ProcessHandlerChain handlerChain = new ProcessHandlerChain();
        // 聚引客生产
        handlerChain.addHandler(JykProduceProcessHandler::new).start(splitGoodLists, null, vars, handlerChain);

        return (ErpOrderSplitInfo)vars.get("splitInfo");
    }


    /**
     * ERP新增客常来订单，启动审核流程
     *
     * @param erpOrderOriginalInfo
     * @param splitGoodLists
     * @param triggerFlag [订单同步触发 true / ERP新增 false]
     * @date 2018年7月4日
     * @author zjq
     */
    public static void startOrderReview(ErpOrderOriginalInfo erpOrderOriginalInfo) {

        ProcessHandlerChain handlerChain = new ProcessHandlerChain();
        // 订单审核流程
        handlerChain.addHandler(KclOrderReviewProcessHandler::new).start(null, erpOrderOriginalInfo, null, handlerChain);
    }

    /**
     * 商户管理后台订单同步，启动流程
     *
     * @param erpOrderOriginalInfo
     * @date 2018年7月10日
     * @author zjq
     */
    public static void startByMerchantsOrder(ErpOrderOriginalInfo erpOrderOriginalInfo) {

        ProcessHandlerChain handlerChain = new ProcessHandlerChain();
        // 物料更新流程
        handlerChain.addHandler(MaterialUpdateProcessHandler::new).start(null, erpOrderOriginalInfo, null, handlerChain);
    }

}
