package com.yunnex.ops.erp.modules.visit.constants;

public interface ErpVisitServiceConstants {

    /**
     * 上门服务数据查看权限
     */
    String VISIT_SERVICE_DATA_QUERY = "visitService:data:view";

    /**
     * 上门服务数据查看权限：所有数据
     */
    String VISIT_SERVICE_DATA_QUERY_ALL = "visitService:dataQuery:all";

    /**
     * 上门服务数据查看权限：公司所属数据
     */
    String VISIT_SERVICE_DATA_QUERY_COMPANY = "visitService:dataQuery:company";

    /**
     * 上门服务数据查看权限：个人数据
     */
    String VISIT_SERVICE_DATA_QUERY_PERSONAL = "visitService:dataQuery:personal";

    /**
     * 上门服务数据编辑权限：新增
     */
    String VISIT_SERVICE_DATA_EDIT_CREATE = "visitService:dataEdit:create";

    /**
     * 上门服务数据编辑权限：修改
     */
    String VISIT_SERVICE_DATA_EDIT_UPDATE = "visitService:dataEdit:update";

    /**
     * 上门服务数据编辑权限：取消
     */
    String VISIT_SERVICE_DATA_EDIT_CANCEL = "visitService:dataEdit:cancel";

    /**
     * 上门服务数据状态：已预约
     */
    String AUDIT_STATUS_RESERVED = "0";

    /**
     * 上门服务数据状态：待审核
     */
    String AUDIT_STATUS_SUBMITTED = "1";

    /**
     * 上门服务数据状态：已审核（待上门）
     */
    String AUDIT_STATUS_AUDITED = "2";

    /**
     * 上门服务数据状态：审核不通过
     */
    String AUDIT_STATUS_DISMISSED = "3";

    /**
     * 上门服务数据状态：已上门
     */
    String AUDIT_STATUS_COMPLETED = "4";

    /**
     * 上门服务数据状态：已取消
     */
    String AUDIT_STATUS_CALLED = "5";

    /**
     * 上门服务数据状态：修改，用于判断流程节点
     */
    String AUDIT_STATUS_UPDATE = "6";

    /**
     * 服务类型：基础服务
     */
    String SERVICE_TYPE_BASIC = "1";

    /**
     * 服务类型：售后服务
     */
    String SERVICE_TYPE_AFTER_SALE = "2";

}
