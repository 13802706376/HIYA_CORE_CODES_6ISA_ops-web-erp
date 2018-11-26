package com.yunnex.junit;

import java.util.Map;

import org.activiti.engine.task.Task;
import org.junit.Test;

import com.google.common.collect.Maps;

public class VerificationWorkflowServiceTest extends BaseActivitiTest
{
         //业务key
         String businessKey = "1003";
         
         //流程定义key
         String processDefineKey = "verification_flow";
         
         //处理人AR
         String arAssignee = "wade";
         
         //处理人CRO
         String croAssignee = "james";
         
         @Test    
	     public void testVerificationProcess( ) 
	     {  
        	 startProcessInstance();
        	 //completeTaskAr();
        	 //backTaskCro();
        	 //completeTaskAr();
        	 //completeTaskCro();
	     }  
         //启动流程
	     public void startProcessInstance( ) 
	     {  
        	 Map<String, Object> variables = Maps.newHashMap();
        	 variables.put("assignee", arAssignee);
        	 runtimeService.startProcessInstanceByKey(processDefineKey, businessKey,variables);
	     }  

         //AR完成任务
	     public void completeTaskAr( ) 
	     {  
        	 Task task = taskService.createTaskQuery().active().taskAssignee(arAssignee).list().get(0);
        	 Map<String, Object> variables = Maps.newHashMap();
        	 variables.put("assignee", croAssignee);
        	 taskService.complete(task.getId(), variables);
	     }  
         
         //CRO回退任务
	     public void backTaskCro( ) 
	     {  
        	 Task task = taskService.createTaskQuery().active().taskAssignee(croAssignee).list().get(0);
        	 Map<String, Object> variables = Maps.newHashMap();
        	 variables.put("assignee", arAssignee);
        	 variables.put("appoveResult", "N");
        	 taskService.complete(task.getId(), variables);
	     }  
         //CRO完成任务
	     public void completeTaskCro( ) 
	     {  
        	 Task task = taskService.createTaskQuery().active().processInstanceBusinessKey(businessKey).taskAssignee(croAssignee).list().get(0);
        	 Map<String, Object> variables = Maps.newHashMap();
        	 variables.put("appoveResult", "Y");
        	 taskService.complete(task.getId(), variables);
	     } 
}