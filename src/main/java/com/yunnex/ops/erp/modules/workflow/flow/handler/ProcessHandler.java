package com.yunnex.ops.erp.modules.workflow.flow.handler;

import java.util.List;
import java.util.Map;

import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;

@FunctionalInterface
public interface ProcessHandler {

    String FIRST = "isFist";
    String PLANNINGEXPERT = "planningExpert";

    void start(List<SplitGoodForm> request, ErpOrderOriginalInfo erpOrderOriginalInfo, Map<String, Object> response, ProcessHandlerChain chain);

   /* public static ProcessHandler create(Supplier<ProcessHandler> supplier) {
        return supplier.get();
    }*/
}
