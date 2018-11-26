package com.yunnex.ops.erp.modules.workflow.flow.strategy;

import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowInfoTask;

public interface IProcOperate {
    
    void operate(FlowInfoTask task);
    
}
