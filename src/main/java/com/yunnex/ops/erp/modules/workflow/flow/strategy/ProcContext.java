package com.yunnex.ops.erp.modules.workflow.flow.strategy;

import java.util.List;

import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.workflow.flow.dto.FlowTaskDto;

public class ProcContext {


    public void operateTaskDto(List<FlowTaskDto> dtos) {
        for (FlowTaskDto flowTaskDto : dtos) {
            operate(flowTaskDto);
        }
    }

    public void operate(FlowTaskDto flowTaskDto) {

        IProcOperate iProcTypeOperate = SpringContextHolder.getBean(JykProcessOperate.class);

        // jyk流程
        if (flowTaskDto.getProcessDefineKey().startsWith(ActUtils.JYK_OPEN_ACCOUNT_FLOW[0])) {
            iProcTypeOperate = SpringContextHolder.getBean(JykProcessOperate.class);
        }
        // 交付服务流程
        else if (flowTaskDto.getProcessDefineKey().startsWith(ActUtils.DELIVERY_SERVICE_FLOW[0])) {
            iProcTypeOperate = SpringContextHolder.getBean(DeliveryProcessOperate.class);
        }
        // 商户资料录入
        else if (flowTaskDto.getProcessDefineKey().startsWith(ActUtils.SHOP_DATA_INPUT_FLOW[0])) {
            iProcTypeOperate = SpringContextHolder.getBean(JykProcessOperate.class);
        }

        iProcTypeOperate.operate(flowTaskDto);
    }

}
