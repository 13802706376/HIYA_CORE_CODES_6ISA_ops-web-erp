package com.yunnex.ops.erp.common.activiti.parse;

import java.util.Map;

import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.EventSubProcess;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.bpmn.data.IOSpecification;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.SubProcessParseHandler;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunnex.ops.erp.common.activiti.handler.ListenerHandlerContext;

public class ActivitiSubProcessExtParseHandler extends SubProcessParseHandler {
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    protected void executeParse(BpmnParse bpmnParse, SubProcess subProcess) {
        
        ActivityImpl activity = createActivityOnScope(bpmnParse, subProcess, BpmnXMLConstants.ELEMENT_SUBPROCESS, bpmnParse.getCurrentScope());
        
        activity.setAsync(subProcess.isAsynchronous());
        activity.setExclusive(!subProcess.isNotExclusive());

        boolean triggeredByEvent = false;
        if (subProcess instanceof EventSubProcess) {
          triggeredByEvent = true;
        }
        activity.setProperty("triggeredByEvent", triggeredByEvent);
        
        // event subprocesses are not scopes
        activity.setScope(!triggeredByEvent);
        activity.setActivityBehavior(bpmnParse.getActivityBehaviorFactory().createSubprocActivityBehavior(subProcess));
        
        
        bpmnParse.setCurrentScope(activity);
        bpmnParse.setCurrentSubProcess(subProcess);
        
        bpmnParse.processFlowElements(subProcess.getFlowElements());
        processArtifacts(bpmnParse, subProcess.getArtifacts(), activity);
        
        // no data objects for event subprocesses
        if (!(subProcess instanceof EventSubProcess)) {
          // parse out any data objects from the template in order to set up the necessary process variables
          Map<String, Object> variables = processDataObjects(bpmnParse, subProcess.getDataObjects(), activity);
          activity.setVariables(variables);
        }

        bpmnParse.removeCurrentScope();
        bpmnParse.removeCurrentSubProcess();
        
        if (subProcess.getIoSpecification() != null) {
          IOSpecification ioSpecification = createIOSpecification(bpmnParse, subProcess.getIoSpecification());
          activity.setIoSpecification(ioSpecification);
        }

        activity.addExecutionListener(ExecutionListener.EVENTNAME_END, execution -> {
            logger.info("子流程-结束-监听，节点ID={}", execution.getCurrentActivityId());// NOSONAR
            ListenerHandlerContext.getSigleInstance().listenerAdapter(null, execution, null);
        });

      }
}
