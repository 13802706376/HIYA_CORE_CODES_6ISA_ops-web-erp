package com.yunnex.ops.erp.common.activiti.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.act.entity.ActDefExt;
import com.yunnex.ops.erp.modules.act.service.ActDefExtService;

@Service
public class ListenerHandlerContext {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 业务定义：流程监听AOP
     * 
     * @param task 节点数据对象
     * @param execution 节点执行对象
     * @param mustBeanId 必须执行的执行器beanId
     * 
     * @see 通过节点扩展表（ACT_RE_ACTDEF_EXT）的callback_service字段获取beanId来获得对应的执行器对象，然后通过params来获取对应参数，
     *      执行自定义逻辑
     * @see callback_service字段json格式：[{"beanId":"xxxxxHandler","params":{"xxxx":"xxxxx"}]
     */
    @Transactional(readOnly = false)
    public void listenerAdapter(DelegateTask task, DelegateExecution execution, String mustBeanId) {
        try {
            DelegateExecution exObj = execution != null ? execution : task.getExecution();
            if (Optional.ofNullable(exObj).isPresent()) {
                String procDefKey = exObj.getEngineServices().getRepositoryService().getProcessDefinition(exObj.getProcessDefinitionId()).getKey();// 获取流程KEY
                String actId = exObj.getCurrentActivityId();// 获取流程节点ID
                if (ExecutionListener.EVENTNAME_TAKE.equalsIgnoreCase(exObj.getEventName())) {// 线条监控获取线条ID
                    ExecutionEntity ee = (ExecutionEntity) exObj;
                    actId = ee.getTransition().getId();
                }
                ActDefExtService actDefExtService = SpringContextHolder.getBean(ActDefExtService.class);
                ActDefExt actDefExt = actDefExtService.findByProcDefKeyAndActId(procDefKey, actId);
                logger.info("ListenerHandlerContext params:  BusinessKey={},ActDefExt={}", exObj.getProcessBusinessKey(), actDefExt);// NOSONAR
                JSONArray jsonArray = null;
                try {
                    jsonArray = JSON.parseArray(StringEscapeUtils.unescapeHtml4(actDefExt.getCallbackService()));// 获取配置的监听参数
                } catch (Exception e) {} // NOSONAR

                String actType = "";
                if (null != actDefExt && StringUtils.isNotEmpty(actDefExt.getActType())) {
                    try {
                        JSONObject jsonJ = JSONObject.parseObject(actDefExt.getActType());
                        String variableNames = jsonJ.getString("variableName");
                        if (StringUtils.isNotEmpty(variableNames)) {
                            StringBuffer stb = null;
                            String[] variables = variableNames.split(";");
                            for (String variable : variables) {
                                if (null == stb) {
                                    stb = new StringBuffer();
                                    stb.append(exObj.getVariable(variable) + "");
                                } else {
                                    stb.append("+" + exObj.getVariable(variable));
                                }
                            }
                            String actMappings = jsonJ.getString("actMapping");
                            if (StringUtils.isNotEmpty(actMappings)) {
                                JSONObject jsonact = JSONObject.parseObject(actMappings);
                                actType = jsonact.getString(stb + "");
                            }
                        }
                    } catch (Exception e) {} // NOSONAR
                }
                Map<String, Object> paramsactType = new HashMap<String, Object>();
                paramsactType.put("actType", actType);
                if (Optional.ofNullable(jsonArray).isPresent()) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String beanId = jsonObj.getString(CommonConstants.Listener.HANDLER_BEAN_ID_KEY);
                        String event = jsonObj.getString(CommonConstants.Listener.HANDLER_EVENT_KEY);
                        String workEvent = execution != null ? execution.getEventName() : task.getEventName();
                        if (SpringContextHolder.getApplicationContext().containsBean(beanId) && StringUtils.equals(workEvent, event)) {
                            Map<String, Object> params = jsonObj.getJSONObject(CommonConstants.Listener.HANDLER_PARAMS_KEY);
                            IListenerHandler handler = SpringContextHolder.getBean(beanId);
                            handler.doListenerBusiness(task, exObj, params);
                        }
                    }
                }
                // 每个节点都进行判断是否有必须执行的执行器
                if (null != mustBeanId && SpringContextHolder.getApplicationContext().containsBean(mustBeanId)) {
                    IListenerHandler handler = SpringContextHolder.getBean(mustBeanId);
                    handler.doListenerBusiness(task, exObj, paramsactType);
                }
            }
        } catch (Exception e) {// NOSONAR
            logger.error("ListenerHandlerContext Error: {}", e);// NOSONAR
        }
    }

    public static ListenerHandlerContext getSigleInstance() {
        return SigleInstance.INSTANCE.instance;
    }

    public enum SigleInstance {
        INSTANCE;
        public ListenerHandlerContext instance;

        SigleInstance() {
            instance = new ListenerHandlerContext();
        }

        public ListenerHandlerContext getInstance() {
            return instance;
        }
    }
}
