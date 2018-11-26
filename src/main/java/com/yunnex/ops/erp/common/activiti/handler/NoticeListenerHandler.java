package com.yunnex.ops.erp.common.activiti.handler;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 通知 实现类
 * 
 * @author zjq
 *
 */
@Service
public class NoticeListenerHandler implements  IListenerHandler
{
    @Override
    @Transactional(readOnly = false)
     public String doListenerBusiness(DelegateTask task,DelegateExecution execution,Map<String,Object>params)
     {
         
         
         return "";
     }
}