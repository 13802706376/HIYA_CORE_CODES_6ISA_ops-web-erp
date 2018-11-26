package com.yunnex.ops.erp.common.activiti.handler;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务任务节点开始事件触发执行器
 * 
 * @date 2018年7月25日
 */
@Service("serviceTaskStartListenerHandler")
public class ServiceTaskStartListenerHandler implements IListenerHandler {

    @Override
    @Transactional(readOnly = false)
    public String doListenerBusiness(DelegateTask task, DelegateExecution execution, Map<String, Object> params) {
        return null;
    }

}
