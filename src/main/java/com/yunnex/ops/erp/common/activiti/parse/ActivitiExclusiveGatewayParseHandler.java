package com.yunnex.ops.erp.common.activiti.parse;

import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ExclusiveGatewayParseHandler;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunnex.ops.erp.common.activiti.handler.ListenerHandlerContext;

public class ActivitiExclusiveGatewayParseHandler extends ExclusiveGatewayParseHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void executeParse(BpmnParse bpmnParse, ExclusiveGateway gateway) {
        ActivityImpl activity = createActivityOnCurrentScope(bpmnParse, gateway, BpmnXMLConstants.ELEMENT_GATEWAY_EXCLUSIVE);

        activity.setAsync(gateway.isAsynchronous());
        activity.setExclusive(!gateway.isNotExclusive());
        activity.addExecutionListener(ExecutionListener.EVENTNAME_START, execution -> {
            logger.info("互斥网关-开始-监听，节点ID={}", execution.getCurrentActivityId());// NOSONAR
            ListenerHandlerContext.getSigleInstance().listenerAdapter(null, execution, null);
        });

        activity.setActivityBehavior(bpmnParse.getActivityBehaviorFactory().createExclusiveGatewayActivityBehavior(gateway));
    }

}
