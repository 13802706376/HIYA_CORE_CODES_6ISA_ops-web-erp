package com.yunnex.ops.erp.common.activiti.handler;

import java.util.Map;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;

/**
 * 监听器 业务处理接口
 * @author zjq
 *
 */
public interface IListenerHandler
{
     String doListenerBusiness(DelegateTask task,DelegateExecution execution,Map<String,Object> params);
}