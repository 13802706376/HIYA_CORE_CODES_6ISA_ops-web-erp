package com.yunnex.ops.erp.modules.sys.constant;

/**
 * 系统常量
 */
public interface SysConstant {

    /** 以下常量在数据字典中的类型名 */
    String ERP_AGENT_TYPE = "erp_agent_type";
    /** 类型：ERP。用户，角色，团队，工号等 */
    String TYPE_ERP = "erp";
    /** 类型：服务商。用户，角色，团队，工号等 */
    String TYPE_AGENT = "agent";

    /** 公司类型在数据字典的类型名 */
    String COMPANY_TYPE = "company_type";
    /** 总部 */
    String COMPANY_TYPE_HEAD = "head";
    /** 分公司 */
    String COMPANY_TYPE_BRANCH = "branch";
    /** 代理服务商 */
    String COMPANY_TYPE_AGENT = "agent";

    // 系统管理员
    String ADMIN_CN = "系统管理员";
    // 系统管理员ID
    String ADMIN_ID = "1";

    // 管理员排序高
    Integer SORT_ADMIN = 1000000;
    // 用户排序低
    Integer SORT_USER = 0;


    // 名称长度（用户姓名、登录名）
    String NAME_LENGTH = "{2,32}";


    interface SystemEnvironment
    {
         String  USER_ADMINISTRATOR = "administrator";
         String  PROFLIE_PRODUCTION   = "production";
         String PROFLIE_DEVELOPMENT = "development";
         String PROFLIE_TEST = "test";
         String PROFLIE_DEFAULT = "production";
    }
    
}
