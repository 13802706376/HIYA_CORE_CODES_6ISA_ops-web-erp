package com.yunnex.ops.erp.common.activiti.parse;

import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.DataAssociation;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.bpmn.behavior.WebServiceActivityBehavior;
import org.activiti.engine.impl.bpmn.data.AbstractDataAssociation;
import org.activiti.engine.impl.bpmn.data.IOSpecification;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ServiceTaskParseHandler;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunnex.ops.erp.common.activiti.handler.ListenerHandlerContext;
import com.yunnex.ops.erp.common.constant.CommonConstants;

public class ActivitiServiceTaskExtParseHandler extends ServiceTaskParseHandler
{
    private static Logger logger = LoggerFactory.getLogger(ActivitiServiceTaskExtParseHandler.class);

    protected void executeParse(BpmnParse bpmnParse, ServiceTask serviceTask)
    {

        ActivityImpl activity = createActivityOnCurrentScope(bpmnParse, serviceTask,
                BpmnXMLConstants.ELEMENT_TASK_SERVICE);
        activity.setAsync(serviceTask.isAsynchronous());
        activity.setFailedJobRetryTimeCycleValue(serviceTask.getFailedJobRetryTimeCycleValue());
        activity.setExclusive(!serviceTask.isNotExclusive());

        activity.addExecutionListener(ExecutionListener.EVENTNAME_START, execution -> {
            logger.info("服务节点-开始-监听，节点ID={}", execution.getCurrentActivityId());// NOSONAR
            ListenerHandlerContext.getSigleInstance().listenerAdapter(null, execution, CommonConstants.Listener.HANDLER_BEAN_SERVICE_TASK_START);
        });
        activity.addExecutionListener(ExecutionListener.EVENTNAME_END, execution -> {
            logger.info("服务节点-结束-监听，节点ID={}", execution.getCurrentActivityId());// NOSONAR
            ListenerHandlerContext.getSigleInstance().listenerAdapter(null, execution, CommonConstants.Listener.HANDLER_BEAN_SERVICE_TASK_END);
        });

        if (StringUtils.isNotEmpty(serviceTask.getType()))
        {

            if (serviceTask.getType().equalsIgnoreCase("mail"))
            {
                activity.setActivityBehavior(
                        bpmnParse.getActivityBehaviorFactory().createMailActivityBehavior(serviceTask));

            } else if (serviceTask.getType().equalsIgnoreCase("mule"))
            {
                activity.setActivityBehavior(bpmnParse.getActivityBehaviorFactory()
                        .createMuleActivityBehavior(serviceTask, bpmnParse.getBpmnModel()));

            } else if (serviceTask.getType().equalsIgnoreCase("camel"))
            {
                activity.setActivityBehavior(bpmnParse.getActivityBehaviorFactory()
                        .createCamelActivityBehavior(serviceTask, bpmnParse.getBpmnModel()));

            } else if (serviceTask.getType().equalsIgnoreCase("shell"))
            {
                activity.setActivityBehavior(
                        bpmnParse.getActivityBehaviorFactory().createShellActivityBehavior(serviceTask));

            } else
            {
                logger.warn("Invalid service task type: '" + serviceTask.getType() + "' " + " for service task "
                        + serviceTask.getId());
            }

            // activiti:class
        } else if (ImplementationType.IMPLEMENTATION_TYPE_CLASS.equalsIgnoreCase(serviceTask.getImplementationType()))
        {
            activity.setActivityBehavior(
                    bpmnParse.getActivityBehaviorFactory().createClassDelegateServiceTask(serviceTask));

            // activiti:delegateExpression
        } else if (ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION
                .equalsIgnoreCase(serviceTask.getImplementationType()))
        {
            activity.setActivityBehavior(bpmnParse.getActivityBehaviorFactory()
                    .createServiceTaskDelegateExpressionActivityBehavior(serviceTask));

            // activiti:expression
        } else if (ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION
                .equalsIgnoreCase(serviceTask.getImplementationType()))
        {
            activity.setActivityBehavior(
                    bpmnParse.getActivityBehaviorFactory().createServiceTaskExpressionActivityBehavior(serviceTask));

            // Webservice
        } else if (ImplementationType.IMPLEMENTATION_TYPE_WEBSERVICE.equalsIgnoreCase(
                serviceTask.getImplementationType()) && StringUtils.isNotEmpty(serviceTask.getOperationRef()))
        {

            if (!bpmnParse.getOperations().containsKey(serviceTask.getOperationRef()))
            {
                logger.warn(serviceTask.getOperationRef() + " does not exist for service task " + serviceTask.getId());
            } else
            {

                WebServiceActivityBehavior webServiceActivityBehavior = bpmnParse.getActivityBehaviorFactory()
                        .createWebServiceActivityBehavior(serviceTask);
                webServiceActivityBehavior.setOperation(bpmnParse.getOperations().get(serviceTask.getOperationRef()));

                if (serviceTask.getIoSpecification() != null)
                {
                    IOSpecification ioSpecification = createIOSpecification(bpmnParse,
                            serviceTask.getIoSpecification());
                    webServiceActivityBehavior.setIoSpecification(ioSpecification);
                }

                for (DataAssociation dataAssociationElement : serviceTask.getDataInputAssociations())
                {
                    AbstractDataAssociation dataAssociation = createDataInputAssociation(bpmnParse,
                            dataAssociationElement);
                    webServiceActivityBehavior.addDataInputAssociation(dataAssociation);
                }

                for (DataAssociation dataAssociationElement : serviceTask.getDataOutputAssociations())
                {
                    AbstractDataAssociation dataAssociation = createDataOutputAssociation(bpmnParse,
                            dataAssociationElement);
                    webServiceActivityBehavior.addDataOutputAssociation(dataAssociation);
                }

                activity.setActivityBehavior(webServiceActivityBehavior);
            }
        } else
        {
            logger.warn(
                    "One of the attributes 'class', 'delegateExpression', 'type', 'operation', or 'expression' is mandatory on serviceTask "
                            + serviceTask.getId());
        }

    }

}
