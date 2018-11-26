package com.yunnex.ops.erp.common.activiti.parse;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunnex.ops.erp.common.activiti.handler.ListenerHandlerContext;
import com.yunnex.ops.erp.common.constant.CommonConstants;

public class ActivitiUserTaskExtParseHandler extends UserTaskParseHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public TaskDefinition parseTaskDefinition(BpmnParse bpmnParse, UserTask userTask, String taskDefinitionKey,
                    ProcessDefinitionEntity processDefinition) {
        TaskDefinition taskDefinition = super.parseTaskDefinition(bpmnParse, userTask, taskDefinitionKey, processDefinition);
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE, task -> {
            logger.info("用户节点-创建-监听，节点ID={}", task.getTaskDefinitionKey());// NOSONAR
            ListenerHandlerContext.getSigleInstance().listenerAdapter(task, null, CommonConstants.Listener.HANDLER_BEAN_USER_TASK_CREATE);
        });
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_COMPLETE, task -> {
            logger.info("用户节点-完成-监听，节点ID={}", task.getTaskDefinitionKey());// NOSONAR
            ListenerHandlerContext.getSigleInstance().listenerAdapter(task, null, CommonConstants.Listener.HANDLER_BEAN_USER_TASK_COMPLETE);
        });
        return taskDefinition;
    }
}
