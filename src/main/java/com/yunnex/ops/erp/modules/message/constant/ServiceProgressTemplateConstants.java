package com.yunnex.ops.erp.modules.message.constant;

/**
 * 服务进度模板 常量
 * 
 * @author linqunzhi
 * @date 2018年7月9日
 */
public interface ServiceProgressTemplateConstants {

    /** 未开始 */
    String STATUS_NO_BEGIN = "NoBegin";
    /** 开始 */
    String STATUS_BEGIN = "Begin";
    /** 结束 */
    String STATUS_END = "End";
    /** 任务key触发类型 and */
    String TASK_KEY_TYPE_AND = "And";
    /** 任务key触发类型 or */
    String TASK_KEY_TYPE_OR = "Or";

    /**
     * 服务进度类型
     * 
     * @author linqunzhi
     * @date 2018年7月23日
     */
    interface Type {
        /** 流程启动 */
        String SERVICE_START = "ServiceStart";
        /** 上门服务 */
        String VISIT_SERVICE = "VisitService";

        /** 智能客流运营全套落地服务 */
        String FLOOR_SERVICE = "FloorService";

        /** 菜单配置服务 */
        String MENU_CONFIG = "MenuConfig";
    }

}
