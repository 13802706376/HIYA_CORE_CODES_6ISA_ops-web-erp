package com.yunnex.ops.erp.common.constant;

/**
 * 公共常量类
 * 
 * @author linqunzhi
 * @date 2018年4月2日
 */
public interface CommonConstants {

    /** 返回标识KEY */
    String RETURN_CODE = "returnCode";
    /** 返回标识消息 */
    String RETURN_MESSAGE = "returnMessage";
    /** 返回标识数据 */
    String RETURN_DATA = "returnData";
    /** 返回标识-成功 */
    String RETURN_CODE_SUCCESS = "success";
    /** 返回标识-失败 */
    String RETURN_CODE_FAIL = "fail";
    /** 结果 */
    String RESULT = "result";
    /** 消息 */
    String MESSSAGE = "message";
    /** 系统错误提示语 */
    String SYSTEM_ERROR_MESSAGE = "系统错误，请联系管理员。";

    /**
     * 错误信息
     * 
     * @author linqunzhi
     * @date 2018年4月2日
     */
    public interface FailMsg {

        String PARAM = "参数异常";

        String DATA = "数据异常";

        String SYSTEM = "系统异常";

        String EXPORT = "导出失败";
    }

    /**
     * 成功消息
     * 
     * @author linqunzhi
     * @date 2018年5月29日
     */
    public interface SuccessMsg {

        String EXPORT = "导出成功";

    }

    /**
     * 标志、符号
     * 
     * @author linqunzhi
     * @date 2018年4月2日
     */
    interface Sign {

        int NUMBER_YES = 1;

        int NUMBER_NO = 0;

        String YES = "Y";

        String NO = "N";

        String DASH = "-";

        String COMMA = ",";

        String COMMA_FULL = "，";

        String ASTERISK = "*";

        String POINT = ".";

        String FORWARD_SLASH = "/";

        String SEMICOLON = ";";

        String SPACE = " ";

        String EMPTY_STRING = "";

        /** 时间默认值 */
        String DATE_TIME_DEFAULT = "0000-00-00 00:00:00";

        String DUN_HAO = "、";

        String EQUALS = "=";

    }

    /**
     * 成功消息
     * 
     * @author czj
     * @date 2018年5月29日
     */
    public interface BaseCrudParams 
    {
        String UPDATE_BY = "updateBy";
        String UPDATE_DATE = "updateDate";
        String CREATE_BY = "createBy";
        String CREATE_DATE = "createDate";
        String DEL_FALG = "delFlag";
        String DEL_FALG_DEFAULT = "0";
        String SQL_TYPE_DELATE = "delete";
        String SQL_TYPE_UPDATE = "update";
        String USER_ID_DELATE = "1";
    }
    
    /**
     * 成功消息
     * 
     * @author czj
     * @date 2018年5月29日
     */
    public interface RedisCacheParams 
    {
        String REDIS_SESSION_PREFIX = "OPS_WEB_ERP_session_";
        String REDIS_CACHE_PREFIX = "OPS_WEB_ERP_cache_";
        String REDIS_CACHE_DEFAULT = "userCache";
        String REDIS_SESSION_COOKIE_NAME = "jeesite.session.id";
        
    }
    
    /**
     * 成功消息
     * 
     * @author czj
     * @date 2018年5月29日
     */
    public interface DubboRegisterParams 
    {
        String DUBBO_REGISTER_KEY = "dubbo.register";
        String DUBBO_REGISTER_VAL = "false";
        String DUBBO_ENV_VAR = "DUBBO_CONFIG";
        
        String ZKSTRING = "ZKSTRING";
        String ZKSTRING_BOOT = "zk.string.boot";
    }
    
    /**
     * 流程监听器用常量
     * 
     * @author R/Q
     * @date 2018年7月25日
     */
    public interface Listener {
        /** 执行器beanId获取KEY */
        String HANDLER_BEAN_ID_KEY = "beanId";
        /** 执行器参数获取KEY */
        String HANDLER_PARAMS_KEY = "params";
        /** 执行器执行状态获取KEY */
        String HANDLER_EVENT_KEY = "event";

        /** 人工任务节点结束-执行器beanId */
        String HANDLER_BEAN_USER_TASK_COMPLETE = "userTaskCompletetListenerHandler";
        /** 人工任务节点创建-执行器beanId */
        String HANDLER_BEAN_USER_TASK_CREATE = "userTaskCreateListenerHandler";

        /** 服务任务节点开始-执行器beanId */
        String HANDLER_BEAN_SERVICE_TASK_START = "serviceTaskStartListenerHandler";
        /** 服务任务节点结束-执行器beanId */
        String HANDLER_BEAN_SERVICE_TASK_END = "serviceTaskEndListenerHandler";

        /** 消息任务节点开始-执行器beanId */
        String HANDLER_BEAN_RECEIVE_TASK_START = "receiveTaskStartListenerHandler";
        /** 消息任务节点结束-执行器beanId */
        String HANDLER_BEAN_RECEIVE_TASK_END = "receiveTaskEndListenerHandler";

        /** 流程结束-执行器beanId */
        String HANDLER_BEAN_PROCESSE_END = "processeEndListenerHandler";
    }

}
