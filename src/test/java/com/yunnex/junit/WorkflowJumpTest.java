package com.yunnex.junit;

import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Maps;

public class WorkflowJumpTest extends BaseActivitiTest
{
    // 业务key
    String businessKey = "10006";

    // 流程定义key
    String processDefineKey = "leave_process";

    String assingee = "wade";
    String assingeeKey = "assingee";

    @Test
    public void testJump() throws Exception
    {
        workFlowMonitorService.deploymentProcess("act/designs/oa/leave/leave.bpmn");
        Map<String, Object> variables = Maps.newHashMap();
        variables.put(assingeeKey, assingee);
        workFlowMonitorService.startProcess(processDefineKey, businessKey, variables);

        // A1--终止 OK
        workFlowMonitorService.endProcess("36db492e001946aea5fa83ee36136817");

        // A1--A6 OK
        workFlowMonitorService.jump("dd2fd5821b6744c38dc328bbd732053d", null, "A6");

        // A1--A5(并发任务) OK
        workFlowMonitorService.jump("5ca2fd8c4b1f4c0ea2ce8d1357611ab4", null, "A5");

        // A4|A5--终止 OK
        workFlowMonitorService.endProcess("5ca2fd8c4b1f4c0ea2ce8d1357611ab4");

        // 获取所有节点 OK
        List<ActivityImpl> list = workFlowMonitorService.getProcessNodes("2ef52cd066e540fe9665dc707df7c2cb");
        for (ActivityImpl activityImpl : list)
        {
            System.out.println(activityImpl.getId());
            System.out.println(activityImpl.getProperties().get("name"));
        }

        // 获取所有任务
        List<Task> tasks = workFlowMonitorService.getCurrentTasks("5ca2fd8c4b1f4c0ea2ce8d1357611ab4");

        // 获取子流程任务
        tasks = workFlowMonitorService.getCurrentSubProcessTasks("33eed5dfdb3344d3864f9ab29de1daa8");
        completeTask();
    }

    @Ignore
    public void completeTask()
    {
        Task task = taskService.createTaskQuery().active().processInstanceId("36db492e001946aea5fa83ee36136817")
                .taskAssignee(assingee).list().get(0);
        Map<String, Object> variables = Maps.newHashMap();
        variables.put(assingeeKey, assingee);
        variables.put("way", "A3");
        taskService.complete(task.getId(), variables);
    }

    @Ignore
    public void testHI()
    {
        List<HistoricActivityInstance> list = processEngine.getHistoryService() // 历史任务Service
                .createHistoricActivityInstanceQuery() // 创建历史活动实例查询
                .processInstanceId("110a076907b84d2db4bab8f6759cbbae") // 指定流程实例id
                .finished() // 查询已经完成的任务
                .list();
        for (HistoricActivityInstance hai : list)
        {
            System.out.println("任务ID:" + hai.getId());
            System.out.println("流程实例ID:" + hai.getProcessInstanceId());
            System.out.println("活动名称：" + hai.getActivityName());
            System.out.println("办理人：" + hai.getAssignee());
            System.out.println("开始时间：" + hai.getStartTime());
            System.out.println("结束时间：" + hai.getEndTime());
            System.out.println("===========================");
        }
    }

    @Ignore
    public void findHistoryProcessVariables()
    {
        String processInstanceId = "110a076907b84d2db4bab8f6759cbbae";
        List<HistoricVariableInstance> list = processEngine.getHistoryService()//
                .createHistoricVariableInstanceQuery()// 创建一个历史的流程变量查询对象
                .processInstanceId(processInstanceId)//
                .list();
        if (list != null && list.size() > 0)
        {
            for (HistoricVariableInstance hvi : list)
            {
                System.out.println(hvi.getId() + "   " + hvi.getProcessInstanceId() + "   " + hvi.getVariableName()
                        + "   " + hvi.getVariableTypeName() + "    " + hvi.getValue());
                System.out.println("###############################################");
            }
        }
    }

    @Test
    public void t2()
    {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId("04c1883e217149ab8fb4ae4075d4d5da").taskDefinitionKey("work_management_diagnosis")
                .orderByTaskCreateTime().desc().listPage(0, 1);
        System.out.println(list);
    }

    @Test
    public void t3()
    {
        List<Task> list = taskService.createTaskQuery().includeProcessVariables()
                .processInstanceId("840ca687d9974609908442c62259a46a").list();
        for (Task task : list)
        {
            System.out.println(task);
        }
    }
    
    @Test
    public void a(){
        try {
            workFlowMonitorService.endProcess("9dd310e88d3147b8987620d3fe520430");
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    
    

}