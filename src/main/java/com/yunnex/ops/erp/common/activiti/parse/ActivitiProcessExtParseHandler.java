package com.yunnex.ops.erp.common.activiti.parse;

import java.util.List;

import org.activiti.bpmn.model.EventListener;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ProcessParseHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunnex.ops.erp.common.activiti.handler.ListenerHandlerContext;
import com.yunnex.ops.erp.common.constant.CommonConstants;

public class ActivitiProcessExtParseHandler extends ProcessParseHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected void createEventListeners(BpmnParse bpmnParse, List<EventListener> eventListeners, ProcessDefinitionEntity currentProcessDefinition) {
        super.createEventListeners(bpmnParse, eventListeners, currentProcessDefinition);
        currentProcessDefinition.addExecutionListener(ExecutionListener.EVENTNAME_END, execution -> {
            logger.info("全流程-结束-监听，节点ID={}", execution.getCurrentActivityId());// NOSONAR
            ListenerHandlerContext.getSigleInstance().listenerAdapter(null, execution, CommonConstants.Listener.HANDLER_BEAN_PROCESSE_END);
        });
    }
}
