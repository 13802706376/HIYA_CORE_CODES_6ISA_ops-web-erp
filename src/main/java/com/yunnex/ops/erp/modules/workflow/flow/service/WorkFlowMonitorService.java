package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.yunnex.ops.erp.modules.act.dao.ActDao;

/**
 * 流程监控/管理/干预 统一的 API服务 
 * 
 * @author czj
 * @date 2017年11月23日
 */
@Service
public class WorkFlowMonitorService
{
    private static Logger logger = LoggerFactory.getLogger(WorkFlowMonitorService.class);

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected RuntimeService runtimeService;

    @Autowired
    protected TaskService taskService;
    
    @Autowired
    protected ActDao actDao;

    /**
     * 部署流程
     *
     * @param filePath
     * @date 2017年11月24日
     * @author czj
     */
    public void deploymentProcess(String filePath)
    {
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource(filePath);
        builder.deploy();
        logger.info("Deploy {} success.", filePath);
    }

    /**
     * 启动流程
     *
     * @param processDefinitionKey
     * @param businessKey
     * @param variables
     * @return
     * @date 2017年11月24日
     * @author czj
     */
    public ProcessInstance startProcess(String processDefinitionKey, String businessKey, Map<String, Object> variables)
    {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        return processInstance;
    }

    
    /**
     * 查询流程当前的所有 任务节点 
     * 节点编号:task.getTaskDefinitionKey(); 
     * 节点名称: task.getName(); 
     *  如果处于并发任务 或者 子流程任务，返回有多个。
     * @param processInstanceId
     * @return
     * @date 2017年11月24日
     * @author czj
     */
    public List<Task> getCurrentTasks(String processInstanceId)
    {
        List<Task> tasks = taskService.createTaskQuery().active().processInstanceId(processInstanceId).list();
        return tasks;
    }
    
    /**
     * 查询所有的节点 (只包含用户节点 和 结束的节点)
     *
     * @param processInstanceId
     * @return
     * @date 2017年11月24日
     * @author czj
     */
    public List<ActivityImpl> getProcessNodes(String processInstanceId)
    {
         List<ActivityImpl> nodes = new ArrayList<ActivityImpl>();
         List<ActivityImpl> list = getProcessActivities(processInstanceId);
         for(ActivityImpl activityImpl: list)
         {
             String activityBehavior = activityImpl.getActivityBehavior().toString();
             if(-1 != activityBehavior.indexOf("UserTaskActivityBehavior") || -1 != activityBehavior.indexOf("NoneEndEventActivityBehavior"))
             {
                 nodes.add(activityImpl);
             }
         }
         return nodes;
    }
    
    /**
     * 查询所有的活动节点 (包含所有类型节点，路由,子流程等)
     *
     * @param processInstanceId
     * @return
     * @date 2017年11月24日
     * @author czj
     */
    public List<ActivityImpl> getProcessActivities(String processInstanceId)
    {
         List<Task> tasks = getCurrentTasks(processInstanceId);
         Task task = tasks.get(0);
         ReadOnlyProcessDefinition deployedProcessDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(task.getProcessDefinitionId());
         @SuppressWarnings("unchecked")
         List<ActivityImpl> activities = (List<ActivityImpl>) deployedProcessDefinition.getActivities();
         return activities;
    }
    
    /**
     * 查询流程当前的所有 子流程 任务的节点 ，排除其他节点 
     * 节点编号:task.getTaskDefinitionKey(); 
     * 节点名称: task.getName(); 
     *
     * @param processInstanceId
     * @return
     * @date 2017年11月24日
     * @author czj
     */
    public List<Task> getCurrentSubProcessTasks(String processInstanceId)
    {
        List<Task> subProcessTasks = new ArrayList<Task>();
        List<Task> tasks = taskService.createTaskQuery().active().processInstanceId(processInstanceId).list();
        for(Task task:tasks)
        {
            Execution exe =  runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
            if(!StringUtils.isEmpty(exe.getParentId()) &&   !processInstanceId.equals(exe.getParentId()))
            {
                subProcessTasks.add(task);
            }
        }
        return subProcessTasks;
    }
    
    /**
     * 提交流程，按照参数流转 
     *
     * @param taskId
     * @param variables
     * @param activityId
     * @throws Exception
     * @date 2017年11月24日
     * @author czj
     */
    private void commitProcess(String taskId, Map<String, Object> variables, String activityId) throws Exception
    {
         Map<String, Object> variablesNew = null==variables?new HashMap<String, Object>():variables;
    
        if (StringUtils.isEmpty(activityId))
        {
            taskService.complete(taskId, variablesNew);
        } 
        else
        {
            turnTransition(taskId, activityId, variablesNew);
        }
    }

    /**
     * 清空指定活动节点流向
     *
     * @param activityImpl
     * @return
     * @date 2017年11月24日
     * @author czj
     */
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl)
    {
        // 存储当前节点所有流向临时变量
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
        // 获取当前节点所有流向，存储到临时变量，然后清空
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        for (PvmTransition pvmTransition : pvmTransitionList)
        {
            oriPvmTransitionList.add(pvmTransition);
        }
        pvmTransitionList.clear();
        return oriPvmTransitionList;
    }

    /**
     * 还原指定活动节点流向
     *
     * @param activityImpl
     * @param oriPvmTransitionList
     * @date 2017年11月24日
     * @author czj
     */
    private void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList)
    {
        // 清空现有流向
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        pvmTransitionList.clear();
        // 还原以前流向
        for (PvmTransition pvmTransition : oriPvmTransitionList)
        {
            pvmTransitionList.add(pvmTransition);
        }
    }

    /**
     * 流程转向操作
     *
     * @param taskId
     * @param activityId
     * @param variables
     * @throws Exception
     * @date 2017年11月24日
     * @author czj
     */
    private void turnTransition(String taskId, String activityId, Map<String, Object> variables) throws Exception
    {
        // 当前节点
        ActivityImpl currActivity = findActivitiImpl(taskId, "");
        // 清空当前流向
        List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);
        // 创建新流向
        TransitionImpl newTransition = currActivity.createOutgoingTransition();
        // 目标节点
        ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
        // 设置新流向的目标节点
        newTransition.setDestination(pointActivity);
        // 执行转向任务
        taskService.complete(taskId, variables);
        
        // 删除目标节点新流入
        pointActivity.getIncomingTransitions().remove(newTransition);
        // 还原以前流向
        restoreTransition(currActivity, oriPvmTransitionList);
    }

    /**
     *  根据任务ID和节点ID获取活动节点
     *
     * @param taskId
     * @param activityId
     * @return
     * @throws Exception
     * @date 2017年11月24日
     * @author czj
     */
    private ActivityImpl findActivitiImpl(String taskId, String activityId) throws Exception
    {
                // 取得流程定义
                ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);
                // 获取当前活动节点ID
                String activityIdNew = StringUtils.isEmpty(activityId)?findTaskById(taskId).getTaskDefinitionKey():activityId;
                
                // 根据流程定义，获取该流程实例的结束节点，需要杀掉子流程，因为子流程pvmTransitionList也是空。
                if ("END".equalsIgnoreCase(activityIdNew.toUpperCase()))
                {
                    for (ActivityImpl activityImpl : processDefinition.getActivities())
                    {
                        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
                        String activityBehavior = activityImpl.getActivityBehavior().toString();
                        
                        //以下是 activityBehavior 的所有取值 
                        /* org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior@2f04993d
                         org.activiti.engine.impl.bpmn.behavior.InclusiveGatewayActivityBehavior@9d3d54e
                         org.activiti.engine.impl.bpmn.behavior.ParallelGatewayActivityBehavior@5b0902b4
                         org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior@433ef204
                         org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior@6ddc67d0
                         org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior@573aeab2*/
                        
                        if (pvmTransitionList.isEmpty() && -1 != activityBehavior.indexOf("NoneEndEventActivityBehavior"))
                        {
                             return activityImpl;
                        }
                    }
                }
        
                // 根据节点ID，获取对应的活动节点
                ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityIdNew);
                return activityImpl;
    }

    /**
     * 根据任务ID获取流程定义
     *
     * @param taskId
     * @return
     * @throws Exception
     * @date 2017年11月24日
     * @author czj
     */
    public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) throws Exception
    {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(findTaskById(taskId).getProcessDefinitionId());
        if (processDefinition == null)
        {
            throw new Exception("流程定义未找到!");
        }
        return processDefinition;
    }

    /**
     * 根据任务ID获得任务实例
     *
     * @param taskId
     * @return
     * @throws Exception
     * @date 2017年11月24日
     * @author czj
     */
    public TaskEntity findTaskById(String taskId) throws Exception
    {
        TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
        {
            throw new Exception("任务实例未找到!");
        }
        return task;
    }

    /**
     *  中止流程(管理员干预 )
     *
     * @param processInstanceId
     * @throws Exception
     * @date 2017年11月24日
     * @author czj
     */
    public void endProcess(String processInstanceId) throws Exception
    {
        /**
         * ### Error updating database.  Cause: com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`erp`.`ACT_RU_VARIABLE`, CONSTRAINT `ACT_FK_VAR_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`))
            ### The error may involve org.activiti.engine.impl.persistence.entity.ExecutionEntity.deleteExecution-Inline
            ### The error occurred while setting parameters
            ### SQL: delete from ACT_RU_EXECUTION where ID_ = ? and REV_ = ?
            ### Cause: com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`erp`.`ACT_RU_VARIABLE`, CONSTRAINT `ACT_FK_VAR_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `ACT_RU_EXECUTION` (`ID_`))
                at org.apache.ibatis.exceptions.ExceptionFactory.wrapException(ExceptionFactory.java:26)
                at org.apache.ibatis.session.defaults.DefaultSqlSession.update(DefaultSqlSession.java:154)
                交付服务 作废报错，先删除变量表。
         */
       // actDao.deleteVaribleByInstId(processInstanceId);
        //jump(processInstanceId,null, "end");
        runtimeService.deleteProcessInstance(processInstanceId, "强行流程结束");
    }
    
    
    
     /**
      * 自由跳转 节点 
      *
      * @param processInstanceId
      * @param targetActivityId
      * @throws Exception
      * @date 2017年11月24日
      * @author czji
      */
    public void jump(String processInstanceId, Map<String, Object> variables, String targetActivityId) throws Exception {
        List<Task> tasks = getCurrentTasks(processInstanceId);
        for (int p = 0; p < tasks.size(); p++) {
            Task taskObj = tasks.get(p);
            if (0 == p) {
                ActivityImpl targetActivity = findActivitiImpl(taskObj.getId(), targetActivityId);
                commitProcess(taskObj.getId(), variables, targetActivity.getId());
            } else {
                actDao.deleteTask(taskObj.getId());
            }
        }
    }
}
