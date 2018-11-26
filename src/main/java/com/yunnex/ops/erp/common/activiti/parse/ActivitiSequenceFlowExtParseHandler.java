package com.yunnex.ops.erp.common.activiti.parse;


import java.util.List;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.SequenceFlowParseHandler;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunnex.ops.erp.common.activiti.handler.ListenerHandlerContext;

public class ActivitiSequenceFlowExtParseHandler extends SequenceFlowParseHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected void createExecutionListenersOnTransition(BpmnParse bpmnParse, List<ActivitiListener> activitiListenerList, TransitionImpl transition) {
        super.createExecutionListenersOnTransition(bpmnParse, activitiListenerList, transition);
        transition.addExecutionListener(execution -> {
            if (ExecutionListener.EVENTNAME_TAKE.equalsIgnoreCase(execution.getEventName())) {// 线条监控获取线条ID
                ExecutionEntity ee = (ExecutionEntity) execution;
                logger.info("线条-执行-监听，节点ID={}", ee.getTransition().getId());// NOSONAR
            }
            ListenerHandlerContext.getSigleInstance().listenerAdapter(null, execution, null);
        });
    }
}
