package com.yunnex.junit;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowMonitorService;

public class BaseActivitiTest extends BaseTest
{
	@Autowired
	protected ProcessEngine processEngine;

	@Autowired
	protected RepositoryService repositoryService;

	@Autowired
	protected RuntimeService runtimeService;

	@Autowired
	protected TaskService taskService;

	@Autowired
	protected HistoryService historyService;

	@Autowired
	protected FormService formService;
	
	@Autowired
	WorkFlowMonitorService workFlowMonitorService;

}