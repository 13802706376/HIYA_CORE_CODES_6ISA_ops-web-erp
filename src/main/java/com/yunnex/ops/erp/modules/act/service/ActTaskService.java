/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.yunnex.ops.erp.modules.act.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.BaseService;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.act.dao.ActDao;
import com.yunnex.ops.erp.modules.act.entity.Act;
import com.yunnex.ops.erp.modules.act.service.cmd.CreateAndTakeTransitionCmd;
import com.yunnex.ops.erp.modules.act.service.cmd.JumpTaskCmd;
import com.yunnex.ops.erp.modules.act.service.creator.ChainedActivitiesCreator;
import com.yunnex.ops.erp.modules.act.service.creator.MultiInstanceActivityCreator;
import com.yunnex.ops.erp.modules.act.service.creator.RuntimeActivityDefinitionEntityIntepreter;
import com.yunnex.ops.erp.modules.act.service.creator.SimpleRuntimeActivityDefinitionEntity;
import com.yunnex.ops.erp.modules.act.utils.ActUtils;
import com.yunnex.ops.erp.modules.act.utils.ProcessDefCache;
import com.yunnex.ops.erp.modules.act.utils.ProcessDefUtils;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;

/**
 * 流程定义相关Service
 * 
 * @author ThinkGem
 * @version 2013-11-03
 */
@Service
public class ActTaskService extends BaseService {

    @Autowired
    private ActDao actDao;

    @Autowired
    private ProcessEngineFactoryBean processEngineFactory;

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private ManagementService managementService;

    /**
     * 获取待办列表
     * 
     * @param procDefKey 流程定义标识
     * @return
     */
    public List<Act> todoList(Act act) {
        String userId = UserUtils.getUser().getId();

        List<Act> result = new ArrayList<Act>();
        // =============== 已经签收的任务 ===============
        TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId).active().includeProcessVariables().orderByTaskCreateTime()
                        .asc();
        
        // 设置查询条件
        if (StringUtils.isNotBlank(act.getProcDefKey())) {
            todoTaskQuery.processDefinitionKey(act.getProcDefKey());
        }
        if (act.getBeginDate() != null) {
            todoTaskQuery.taskCreatedAfter(act.getBeginDate());
        }
        if (act.getEndDate() != null) {
            todoTaskQuery.taskCreatedBefore(act.getEndDate());
        }
        // 查询列表
        List<Task> todoList = todoTaskQuery.list();
        for (Task task : todoList) {
            Act e = new Act();
            e.setTask(task);
            e.setVars(task.getProcessVariables());
            e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
            // 传入订单编号
            e.setBusinessId(task.getProcessInstanceId());
            e.setStatus("todo");
            // 从历史信息表中，获取任务的开始时间
            HistoricTaskInstanceEntity historicTask = (HistoricTaskInstanceEntity) historyService.createHistoricTaskInstanceQuery()
                            .taskId(task.getId()).singleResult();
            e.setTaskStarterDate(historicTask.getStartTime());

            result.add(e);
        }


        return result;
    }

    /**
     * 获取已办任务
     * 
     * @param page
     * @param procDefKey 流程定义标识
     * @return
     */
    public Page<Act> historicList(Page<Act> page, Act act) {
        String userId = UserUtils.getUser().getLoginName();

        HistoricTaskInstanceQuery histTaskQuery = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).finished()
                        .includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();

        // 设置查询条件
        if (StringUtils.isNotBlank(act.getProcDefKey())) {
            histTaskQuery.processDefinitionKey(act.getProcDefKey());
        }
        if (act.getBeginDate() != null) {
            histTaskQuery.taskCompletedAfter(act.getBeginDate());
        }
        if (act.getEndDate() != null) {
            histTaskQuery.taskCompletedBefore(act.getEndDate());
        }

        // 查询总数
        page.setCount(histTaskQuery.count());

        // 查询列表
        List<HistoricTaskInstance> histList = histTaskQuery.listPage(page.getFirstResult(), page.getMaxResults());
        // 处理分页问题
        List<Act> actList = Lists.newArrayList();
        for (HistoricTaskInstance histTask : histList) {
            Act e = new Act();
            e.setHistTask(histTask);
            e.setVars(histTask.getProcessVariables());
            e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
            e.setStatus("finish");
            actList.add(e);
        }
        page.setList(actList);
        return page;
    }

    /**
     * 获取流转历史列表
     * 
     * @param procInsId 流程实例
     * @param startAct 开始活动节点名称
     * @param endAct 结束活动节点名称
     */
    public List<Act> histoicFlowList(String procInsId, String startAct, String endAct) {//NOSONAR
        List<Act> actList = Lists.newArrayList();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(procInsId)
                        .orderByHistoricActivityInstanceStartTime().asc().orderByHistoricActivityInstanceEndTime().asc().list();

        boolean start = false;
        Map<String, Integer> actMap = Maps.newHashMap();

        for (int i = 0; i < list.size(); i++) {

            HistoricActivityInstance histIns = list.get(i);

            // 过滤开始节点前的节点
            if (StringUtils.isNotBlank(startAct) && startAct.equals(histIns.getActivityId())) {
                start = true;
            }
            if (StringUtils.isNotBlank(startAct) && !start) {
                continue;
            }

            // 只显示开始节点和结束节点，并且执行人不为空的任务
            if (StringUtils.isNotBlank(histIns.getAssignee()) || "startEvent".equals(histIns.getActivityType()) || "endEvent".equals(histIns
                            .getActivityType())) {

                // 给节点增加一个序号
                Integer actNum = actMap.get(histIns.getActivityId());
                if (actNum == null) {
                    actMap.put(histIns.getActivityId(), actMap.size());
                }

                Act e = new Act();
                e.setHistIns(histIns);
                // 获取流程发起人名称
                if ("startEvent".equals(histIns.getActivityType())) {
                    List<HistoricProcessInstance> il = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInsId)
                                    .orderByProcessInstanceStartTime().asc().list();
                    if (il != null && !il.isEmpty()) {
                        if (StringUtils.isNotBlank(il.get(0).getStartUserId())) {
                            User user = UserUtils.getByLoginName(il.get(0).getStartUserId());
                            if (user != null) {
                                e.setAssignee(histIns.getAssignee());
                                e.setAssigneeName(user.getName());
                            }
                        }
                    }
                }
                // 获取任务执行人名称
                if (StringUtils.isNotEmpty(histIns.getAssignee())) {
                    User user = UserUtils.getByLoginName(histIns.getAssignee());
                    if (user != null) {
                        e.setAssignee(histIns.getAssignee());
                        e.setAssigneeName(user.getName());
                    }
                }
                // 获取意见评论内容
                if (StringUtils.isNotBlank(histIns.getTaskId())) {
                    List<Comment> commentList = taskService.getTaskComments(histIns.getTaskId());
                    if (commentList != null && !commentList.isEmpty()) {
                        e.setComment(commentList.get(0).getFullMessage());
                    }
                }
                actList.add(e);
            }

            // 过滤结束节点后的节点
            if (StringUtils.isNotBlank(endAct) && endAct.equals(histIns.getActivityId())) {
                boolean bl = false;
                Integer actNum = actMap.get(histIns.getActivityId());
                // 该活动节点，后续节点是否在结束节点之前，在后续节点中是否存在
                for (int j = i + 1; j < list.size(); j++) {
                    HistoricActivityInstance hi = list.get(j);
                    Integer actNumA = actMap.get(hi.getActivityId());
                    if ((actNumA != null && actNumA < actNum) || StringUtils.equals(hi.getActivityId(), histIns.getActivityId())) {
                        bl = true;
                    }
                }
                if (!bl) {
                    break;
                }
            }
        }
        return actList;
    }

    /**
     * 获取流程列表
     * 
     * @param category 流程分类
     */
    public Page<Object[]> processList(Page<Object[]> page, String category) {
        /*
         * 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
         */
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().latestVersion().active()
                        .orderByProcessDefinitionKey().asc();

        if (StringUtils.isNotEmpty(category)) {
            processDefinitionQuery.processDefinitionCategory(category);
        }

        page.setCount(processDefinitionQuery.count());

        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(page.getFirstResult(), page.getMaxResults());
        for (ProcessDefinition processDefinition : processDefinitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            page.getList().add(new Object[] {processDefinition, deployment});
        }
        return page;
    }

    /**
     * 获取流程表单（首先获取任务节点表单KEY，如果没有则取流程开始节点表单KEY）
     * 
     * @return
     */
    public String getFormKey(String procDefId, String taskDefKey) {
        String formKey = "";
        if (StringUtils.isNotBlank(procDefId)) {
            if (StringUtils.isNotBlank(taskDefKey)) {
                try {
                    formKey = formService.getTaskFormKey(procDefId, taskDefKey);
                } catch (Exception e) {//NOSONAR
                    formKey = "";
                    logger.info(e.getMessage(), e);
                }
            }
            if (StringUtils.isBlank(formKey)) {
                formKey = formService.getStartFormKey(procDefId);
            }
            if (StringUtils.isBlank(formKey)) {
                formKey = "/404";
            }
        }
        logger.debug("getFormKey: {}", formKey);
        return formKey;
    }

    /**
     * 获取流程实例对象
     * 
     * @param procInsId
     * @return
     */
    @Transactional(readOnly = false)
    public ProcessInstance getProcIns(String procInsId) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult();
    }

    /**
     * 启动流程
     * 
     * @param procDefKey 流程定义KEY
     * @param businessTable 业务表表名
     * @param businessId 业务表编号
     * @return 流程实例ID
     */
    @Transactional(readOnly = false)
    public String startProcess(String procDefKey, String businessTable, String businessId) {
        return startProcess(procDefKey, businessTable, businessId, "");
    }

    /**
     * 启动流程
     * 
     * @param procDefKey 流程定义KEY
     * @param businessTable 业务表表名
     * @param businessId 业务表编号
     * @param title 流程标题，显示在待办任务标题
     * @return 流程实例ID
     */
    @Transactional(readOnly = false)
    public String startProcess(String procDefKey, String businessTable, String businessId, String title) {
        Map<String, Object> vars = Maps.newHashMap();
        return startProcess(procDefKey, businessTable, businessId, title, vars);
    }

    /**
     * 启动流程
     * 
     * @param procDefKey 流程定义KEY
     * @param businessTable 业务表表名
     * @param businessId 业务表编号
     * @param title 流程标题，显示在待办任务标题
     * @param vars 流程变量
     * @return 流程实例ID
     */
    @Transactional(readOnly = false)
    public String startProcess(String procDefKey, String businessTable, String businessId, String title, Map<String, Object> vars) {
        String userId = UserUtils.getUser().getLoginName();// ObjectUtils.toString(UserUtils.getUser().getId())

        // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
        identityService.setAuthenticatedUserId(userId);

        // 设置流程变量
        if (vars == null) {
            vars = Maps.newHashMap();
        }

        // 设置流程标题
        if (StringUtils.isNotBlank(title)) {
            vars.put("title", title);
        }

        // 启动流程
        ProcessInstance procIns = runtimeService.startProcessInstanceByKey(procDefKey, businessId, vars);

        // 更新业务表流程实例ID
        Act act = new Act();
        act.setBusinessTable(businessTable);// 业务表名
        act.setBusinessId(businessId); // 业务表ID
        act.setProcInsId(procIns.getId());
        actDao.updateProcInsIdByBusinessId(act);
        return act.getProcInsId();
    }

    /**
     * 获取任务
     * 
     * @param taskId 任务ID
     */
    public Task getTask(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    /**
     * 删除任务
     * 
     * @param taskId 任务ID
     * @param deleteReason 删除原因
     */
    @Transactional(readOnly = false)
    public void deleteTask(String taskId, String deleteReason) {
        taskService.deleteTask(taskId, deleteReason);
    }

    /**
     * 签收任务
     * 
     * @param taskId 任务ID
     * @param userId 签收用户ID（用户登录名）
     */
    @Transactional(readOnly = false)
    public void claim(String taskId, String userId) {
        taskService.claim(taskId, userId);
    }

    /**
     * 提交任务, 并保存意见
     * 
     * @param taskId 任务ID
     * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
     * @param comment 任务提交意见的内容
     * @param vars 任务变量
     */
    @Transactional(readOnly = false)
    public void complete(String taskId, String procInsId, String comment, Map<String, Object> vars) {
        complete(taskId, procInsId, comment, "", vars);
    }

    /**
     * 提交任务, 并保存意见
     * 
     * @param taskId 任务ID
     * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
     * @param comment 任务提交意见的内容
     * @param title 流程标题，显示在待办任务标题
     * @param vars 任务变量
     */
    @Transactional(readOnly = false)
    public void complete(String taskId, String procInsId, String comment, String title, Map<String, Object> vars) {
        // 添加意见
        if (StringUtils.isNotBlank(procInsId) && StringUtils.isNotBlank(comment)) {
            taskService.addComment(taskId, procInsId, comment);
        }

        // 设置流程变量
        if (vars == null) {
            vars = Maps.newHashMap();
        }

        // 设置流程标题
        if (StringUtils.isNotBlank(title)) {
            vars.put("title", title);
        }

        // 提交任务
        taskService.complete(taskId, vars);
    }

    /**
     * 完成第一个任务
     * 
     * @param procInsId
     */
    @Transactional(readOnly = false)
    public void completeFirstTask(String procInsId) {
        completeFirstTask(procInsId, null, null, null);
    }

    /**
     * 完成第一个任务
     * 
     * @param procInsId
     * @param comment
     * @param title
     * @param vars
     */
    @Transactional(readOnly = false)
    public void completeFirstTask(String procInsId, String comment, String title, Map<String, Object> vars) {
        String userId = UserUtils.getUser().getLoginName();
        Task task = taskService.createTaskQuery().taskAssignee(userId).processInstanceId(procInsId).active().singleResult();
        if (task != null) {
            complete(task.getId(), procInsId, comment, title, vars);
        }
    }


    /**
     * 添加任务意见
     */
    public void addTaskComment(String taskId, String procInsId, String comment) {
        taskService.addComment(taskId, procInsId, comment);
    }


    /**
     * 任务后退一步
     */
    public void taskBack(String procInsId, Map<String, Object> variables) {
        taskBack(getCurrentTask(procInsId), variables);
    }

    /**
     * 任务后退至指定活动
     */
    public void taskBack(TaskEntity currentTaskEntity, Map<String, Object> variables) {
        ActivityImpl activity = (ActivityImpl) ProcessDefUtils
                        .getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(), currentTaskEntity.getTaskDefinitionKey())
                        .getIncomingTransitions().get(0).getSource();
        jumpTask(currentTaskEntity, activity, variables);
    }

    /**
     * 任务前进一步
     */
    public void taskForward(String procInsId, Map<String, Object> variables) {
        taskForward(getCurrentTask(procInsId), variables);
    }

    /**
     * 任务前进至指定活动
     */
    public void taskForward(TaskEntity currentTaskEntity, Map<String, Object> variables) {
        ActivityImpl activity = (ActivityImpl) ProcessDefUtils
                        .getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(), currentTaskEntity.getTaskDefinitionKey())
                        .getOutgoingTransitions().get(0).getDestination();

        jumpTask(currentTaskEntity, activity, variables);
    }

    /**
     * 跳转（包括回退和向前）至指定活动节点
     */
    public void jumpTask(String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables) {
        jumpTask(getCurrentTask(procInsId), targetTaskDefinitionKey, variables);
    }
    
    /**
     * 跳转（包括回退和向前）至指定活动节点
     */
    public void changeRoleUser(String procInsId,String userId,String roleName) {
    	actDao.changeRoleUser(procInsId,userId,roleName);
    	actDao.changeRoleUser1(procInsId,userId,roleName);
    	actDao.changeRoleUser2(procInsId,userId,roleName);
    }

    /**
     * 跳转（包括回退和向前）至指定活动节点
     */
    @Transactional(readOnly = false)
    public void jumpTask(String procInsId, String currentTaskId, String targetTaskDefinitionKey, Map<String, Object> variables) {
        jumpTask(getTaskEntity(currentTaskId), targetTaskDefinitionKey, variables);
    }

    /**
     * 跳转（包括回退和向前）至指定活动节点
     * 
     * @param currentTaskEntity 当前任务节点
     * @param targetTaskDefinitionKey 目标任务节点（在模型定义里面的节点名称）
     * @throws Exception
     */
    @Transactional(readOnly = false)
    public void jumpTask(TaskEntity currentTaskEntity, String targetTaskDefinitionKey, Map<String, Object> variables) {
        ActivityImpl activity = ProcessDefUtils.getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(), targetTaskDefinitionKey);
        jumpTask(currentTaskEntity, activity, variables);
    }

    /**
     * 跳转（包括回退和向前）至指定活动节点
     * 
     * @param currentTaskEntity 当前任务节点
     * @param targetActivity 目标任务节点（在模型定义里面的节点名称）
     * @throws Exception
     */
    @Transactional(readOnly = false)
    private void jumpTask(TaskEntity currentTaskEntity, ActivityImpl targetActivity, Map<String, Object> variables) {
        CommandExecutor commandExecutor = ((RuntimeServiceImpl) runtimeService).getCommandExecutor();
        commandExecutor.execute(new JumpTaskCmd(currentTaskEntity, targetActivity, variables));
    }

    /**
     * 后加签
     */
    @SuppressWarnings("unchecked")
    public ActivityImpl[] insertTasksAfter(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables,
                    String... assignees) {//NOSONAR
        List<String> assigneeList = new ArrayList<String>();
        assigneeList.add(Authentication.getAuthenticatedUserId());
        assigneeList.addAll(CollectionUtils.arrayToList(assignees));
        String[] newAssignees = assigneeList.toArray(new String[0]);
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(procDefId);
        ActivityImpl prototypeActivity = ProcessDefUtils.getActivity(processEngine, processDefinition.getId(), targetTaskDefinitionKey);
        return cloneAndMakeChain(processDefinition, procInsId, targetTaskDefinitionKey, prototypeActivity.getOutgoingTransitions().get(0)
                        .getDestination().getId(), variables, newAssignees);
    }

    /**
     * 前加签
     */
    public ActivityImpl[] insertTasksBefore(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables,
                    String... assignees) {//NOSONAR
        ProcessDefinitionEntity procDef = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(procDefId);
        return cloneAndMakeChain(procDef, procInsId, targetTaskDefinitionKey, targetTaskDefinitionKey, variables, assignees);
    }

    /**
     * 分裂某节点为多实例节点
     */
    public ActivityImpl splitTask(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables,
                    String... assignee) {//NOSONAR
        return splitTask(procDefId, procInsId, targetTaskDefinitionKey, variables, true, assignee);
    }

    /**
     * 分裂某节点为多实例节点
     */
    @SuppressWarnings("unchecked")
    public ActivityImpl splitTask(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables,
                    boolean isSequential, String... assignees) {//NOSONAR
        SimpleRuntimeActivityDefinitionEntity info = new SimpleRuntimeActivityDefinitionEntity();
        info.setProcessDefinitionId(procDefId);
        info.setProcessInstanceId(procInsId);

        RuntimeActivityDefinitionEntityIntepreter radei = new RuntimeActivityDefinitionEntityIntepreter(info);

        radei.setPrototypeActivityId(targetTaskDefinitionKey);
        radei.setAssignees(CollectionUtils.arrayToList(assignees));
        radei.setSequential(isSequential);

        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(procDefId);
        ActivityImpl clone = new MultiInstanceActivityCreator().createActivities(processEngine, processDefinition, info)[0];

        TaskEntity currentTaskEntity = this.getCurrentTask(procInsId);

        CommandExecutor commandExecutor = ((RuntimeServiceImpl) runtimeService).getCommandExecutor();
        commandExecutor.execute(new CreateAndTakeTransitionCmd(currentTaskEntity, clone, variables));

        return clone;
    }

    private TaskEntity getCurrentTask(String procInsId) {
        return (TaskEntity) taskService.createTaskQuery().processInstanceId(procInsId).active().singleResult();
    }

    private TaskEntity getTaskEntity(String taskId) {
        return (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    @SuppressWarnings("unchecked")
    private ActivityImpl[] cloneAndMakeChain(ProcessDefinitionEntity procDef, String procInsId, String prototypeActivityId, String nextActivityId,
                    Map<String, Object> variables, String... assignees) {//NOSONAR
        SimpleRuntimeActivityDefinitionEntity info = new SimpleRuntimeActivityDefinitionEntity();
        info.setProcessDefinitionId(procDef.getId());
        info.setProcessInstanceId(procInsId);

        RuntimeActivityDefinitionEntityIntepreter radei = new RuntimeActivityDefinitionEntityIntepreter(info);
        radei.setPrototypeActivityId(prototypeActivityId);
        radei.setAssignees(CollectionUtils.arrayToList(assignees));
        radei.setNextActivityId(nextActivityId);

        ActivityImpl[] activities = new ChainedActivitiesCreator().createActivities(processEngine, procDef, info);

        jumpTask(procInsId, activities[0].getId(), variables);

        return activities;
    }


    /**
     * 读取带跟踪的图片
     * 
     * @param executionId 环节ID
     * @return 封装了各种节点信息
     */
    public InputStream tracePhoto(String processDefinitionId, String executionId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        List<String> activeActivityIds = Lists.newArrayList();
        if (runtimeService.createExecutionQuery().executionId(executionId).count() > 0) {
            activeActivityIds = runtimeService.getActiveActivityIds(executionId);
        }


        // 使用spring注入引擎请使用下面的这行代码
        Context.setProcessEngineConfiguration(processEngineFactory.getProcessEngineConfiguration());
        return processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator().generateDiagram(bpmnModel, "png", activeActivityIds);
    }

    /**
     * 流程跟踪图信息
     * 
     * @param processInstanceId 流程实例ID
     * @return 封装了各种节点信息
     */
    public List<Map<String, Object>> traceProcess(String processInstanceId) throws Exception {
        Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();// 执行实例
        Object property = PropertyUtils.getProperty(execution, "activityId");
        String activityId = "";
        if (property != null) {
            activityId = property.toString();
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                        .getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
        List<ActivityImpl> activitiList = processDefinition.getActivities();// 获得当前任务的所有节点

        List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
        for (ActivityImpl activity : activitiList) {

            boolean currentActiviti = false;
            String id = activity.getId();

            // 当前节点
            if (id.equals(activityId)) {
                currentActiviti = true;
            }

            Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, processInstance, currentActiviti);

            activityInfos.add(activityImageInfo);
        }

        return activityInfos;
    }


    /**
     * 封装输出信息，包括：当前节点的X、Y坐标、变量信息、任务类型、任务描述
     * 
     * @param activity
     * @param processInstance
     * @param currentActiviti
     * @return
     */
    private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, ProcessInstance processInstance, boolean currentActiviti) {
        Map<String, Object> vars = new HashMap<String, Object>();
        Map<String, Object> activityInfo = new HashMap<String, Object>();
        activityInfo.put("currentActiviti", currentActiviti);
        setPosition(activity, activityInfo);
        setWidthAndHeight(activity, activityInfo);

        Map<String, Object> properties = activity.getProperties();
        vars.put("节点名称", properties.get("name"));
        vars.put("任务类型", ActUtils.parseToZhType(properties.get("type").toString()));

        ActivityBehavior activityBehavior = activity.getActivityBehavior();
        logger.debug("activityBehavior={}", activityBehavior);
        if (activityBehavior instanceof UserTaskActivityBehavior) {

            Task currentTask = null;

            // 当前节点的task
            if (currentActiviti) {
                currentTask = getCurrentTaskInfo(processInstance);
            }

            // 当前任务的分配角色
            UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
            TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
            Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
            if (!candidateGroupIdExpressions.isEmpty()) {

                // 任务的处理角色
                setTaskGroup(vars, candidateGroupIdExpressions);

                // 当前处理人
                if (currentTask != null) {
                    setCurrentTaskAssignee(vars, currentTask);
                }
            }
        }

        vars.put("节点说明", properties.get("documentation"));

        String description = activity.getProcessDefinition().getDescription();
        vars.put("描述", description);

        logger.debug("trace variables: {}", vars);
        activityInfo.put("vars", vars);
        return activityInfo;
    }

    /**
     * 设置任务组
     * 
     * @param vars
     * @param candidateGroupIdExpressions
     */
    private void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
        StringBuilder sb = new StringBuilder();
        for (Expression expression : candidateGroupIdExpressions) {
            String expressionText = expression.getExpressionText();
            String roleName = identityService.createGroupQuery().groupId(expressionText).singleResult().getName();
            sb.append(roleName);
        }
        vars.put("任务所属角色", sb.toString());
    }

    /**
     * 设置当前处理人信息
     * 
     * @param vars
     * @param currentTask
     */
    private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
        String assignee = currentTask.getAssignee();
        if (assignee != null) {
            org.activiti.engine.identity.User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
            String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
            vars.put("当前处理人", userInfo);
        }
    }

    /**
     * 获取当前节点信息
     * 
     * @param processInstance
     * @return
     */
    public Task getCurrentTaskInfo(ProcessInstance processInstance) {
        Task currentTask = null;
        try {
            String activitiId = (String) PropertyUtils.getProperty(processInstance, "activityId");
            logger.debug("current activity id: {}", activitiId);
            currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(activitiId).singleResult();
            logger.debug("current task for processInstance: {}", ToStringBuilder.reflectionToString(currentTask));

        } catch (Exception e) {
            logger.error("can not get property activityId from processInstance: {}", processInstance, e);
        }
        return currentTask;
    }

    /**
     * 设置宽度、高度属性
     * 
     * @param activity
     * @param activityInfo
     */
    private static void setWidthAndHeight(ActivityImpl activity, Map<String, Object> activityInfo) {
        activityInfo.put("width", activity.getWidth());
        activityInfo.put("height", activity.getHeight());
    }

    /**
     * 设置坐标位置
     * 
     * @param activity
     * @param activityInfo
     */
    private static void setPosition(ActivityImpl activity, Map<String, Object> activityInfo) {
        activityInfo.put("x", activity.getX());
        activityInfo.put("y", activity.getY());
    }

    public ProcessEngine getProcessEngine() {
        return processEngine;
    }



    /**
     * 获取多用户任务
     * 
     * @param procDefKey 流程定义标识
     * @return
     */
    public List<Act> todoTeamList(List<String> userIds) {
        List<Act> result = new ArrayList<Act>();
        for (String userId : userIds) {
            TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId).active().includeProcessVariables().orderByTaskCreateTime()
                            .desc();
            // 查询列表
            List<Task> todoList = todoTaskQuery.list();
            for (Task task : todoList) {
                Act e = new Act();
                e.setTask(task);
                e.setVars(task.getProcessVariables());
                e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
                // 传入订单编号
                e.setBusinessId(task.getProcessInstanceId());
                e.setStatus("todo");
                // 从历史信息表中，获取任务的开始时间
                HistoricTaskInstanceEntity historicTask = (HistoricTaskInstanceEntity) historyService.createHistoricTaskInstanceQuery()
                                .taskId(task.getId()).singleResult();
                e.setTaskStarterDate(historicTask.getStartTime());
                result.add(e);
            }
        }
        return result;
    }
    
    /**
     * 获取多用户任务(使用查询语句，不再使用单个遍历查询)
     *
     * @param userIds
     * @return
     * @date 2017年12月25日
     * @author Administrator
     */
    public Map<String, Act> todoTeamList2(List<String> userIds) {
    	Map<String, Act> map = new HashMap<String, Act>();
        StringBuffer sql = new StringBuffer("SELECT t.* FROM ");
        sql.append(managementService.getTableName(Task.class));
        sql.append(" t where 1=1 and (");
        for(int i=0;i<userIds.size();i++){
            if(i != userIds.size()-1){
                sql.append("t.assignee_ = '" + userIds.get(i) + "' or ");
            }else{
                sql.append("t.assignee_ = '" + userIds.get(i) + "'");
            }
        }
        sql.append(") order by t.create_time_ desc");
        
        NativeTaskQuery todoTaskQuery = 
                taskService.createNativeTaskQuery().sql(sql.toString());
        
        
        List<Task> todoList = todoTaskQuery.list();
        for (Task task : todoList) {
            Act e = new Act();
            e.setTask(task);
            e.setVars(task.getProcessVariables());
            e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
            // 传入订单编号
            e.setBusinessId(task.getProcessInstanceId());
            e.setStatus("todo");
            // 从历史信息表中，获取任务的开始时间
            HistoricTaskInstanceEntity historicTask = 
                    (HistoricTaskInstanceEntity) historyService.createHistoricTaskInstanceQuery()
                            .taskId(task.getId()).singleResult();
            e.setTaskStarterDate(historicTask.getStartTime());
            map.put(task.getId(), e);
        }
        return map;
    }
    
    // 获取最后一次提交的某个任务(有可能多次提交同一个任务)
    public HistoricTaskInstance getLastHistoryTask(String procInsId, String taskDefKey) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                      .processInstanceId(procInsId).taskDefinitionKey(taskDefKey).finished()
                      .orderByTaskCreateTime().desc().listPage(0, 1);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    /**
     * 获取流程正在进行的任务名称
     *
     * @param processId
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    public List<String> findTaskNameByProcInsId(String procInsId) {
        return actDao.findTaskNameByProcInsId(procInsId);
    }
    
    /**
     * 获取流程正在进行的任务名称
     *
     * @param processId
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    public List<String> findTidByProcInsId(String procInsId) {
        return actDao.findTidByProcInsId(procInsId);
    }

    /**
     * 流程id中任务节点是否已完成
     *
     * @param procInsId
     * @param taskDefKey
     * @return
     * @date 2018年7月9日
     * @author linqunzhi
     */
    public boolean taskIsFinish(String procInsId, String taskDefKey) {
        logger.info("taskIsFinish start | procInsId={}|taskDefKey={}", procInsId, taskDefKey);
        int count = actDao.getFinishTaskCount(procInsId, taskDefKey);
        boolean result = count > 0;
        logger.info("taskIsFinish end | result={}", result);
        return result;
    }

    public boolean tasksIsRun(String procInsId, List<String> taskDefKeys) {
        return actDao.getRunTaskCount(procInsId, taskDefKeys) > 0;
    }
}
