package com.yunnex.ops.erp.modules.workflow.flow.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.SplitGoodForm;

/**
 * 
 * @author Ejon
 * @date 2018年7月4日
 */
public class ProcessHandlerChain implements ProcessHandler {

    public ProcessHandlerChain() {}

    List<ProcessHandler> filters = new ArrayList<ProcessHandler>();
    int index = 0;

    public ProcessHandlerChain addHandler(Supplier<ProcessHandler> handler) {
        filters.add(handler.get());
        return this;
    }

    public void start(List<SplitGoodForm> splitGoodForms, ErpOrderOriginalInfo erpOrderOriginalInfo, Map<String, Object> vars,
                    ProcessHandlerChain chain) {

        if (index == filters.size()) {
            return;
        }
        ProcessHandler f = filters.get(index);
        index++;
        f.start(splitGoodForms, erpOrderOriginalInfo, vars, chain);
    }
}
