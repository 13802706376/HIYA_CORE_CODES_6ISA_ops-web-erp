package com.yunnex.ops.erp.common.activiti.parse;


import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.ReceiveTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ReceiveTaskParseHandler;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunnex.ops.erp.common.activiti.handler.ListenerHandlerContext;
import com.yunnex.ops.erp.common.constant.CommonConstants;

public class ActivitiReceiveTaskExtParseHandler extends ReceiveTaskParseHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected void executeParse(BpmnParse bpmnParse, ReceiveTask receiveTask) {
        ActivityImpl activity = createActivityOnCurrentScope(bpmnParse, receiveTask, BpmnXMLConstants.ELEMENT_TASK_RECEIVE);
        activity.setActivityBehavior(bpmnParse.getActivityBehaviorFactory().createReceiveTaskActivityBehavior(receiveTask));

        activity.setAsync(receiveTask.isAsynchronous());
        activity.setExclusive(!receiveTask.isNotExclusive());

        activity.addExecutionListener(ExecutionListener.EVENTNAME_START, execution -> {
            logger.info("消息节点-开始-监听，节点ID={}", execution.getCurrentActivityId());// NOSONAR
            ListenerHandlerContext.getSigleInstance().listenerAdapter(null, execution, CommonConstants.Listener.HANDLER_BEAN_RECEIVE_TASK_START);
        });
        activity.addExecutionListener(ExecutionListener.EVENTNAME_END, execution -> {
            logger.info("消息节点-结束-监听，节点ID={}", execution.getCurrentActivityId());// NOSONAR
            ListenerHandlerContext.getSigleInstance().listenerAdapter(null, execution, CommonConstants.Listener.HANDLER_BEAN_RECEIVE_TASK_END);
        });
    }
}
