package com.yunnex.ops.erp.modules.message.constant;

public interface ServiceMessageTemplateConstants {

    /** 启动状态 */
    String STATUS_BEGIN = "Begin";
    /** 结束状态 */
    String STATUS_END = "End";

    /**
     * 通知进度类型
     * 
     * @author linqunzhi
     * @date 2018年7月25日
     */
    interface NodeType {

        /** 智能客流运营全套落地服务 */
        String FLOOR_SERVICE = "FloorService";
        /** 智能客流运营全套落地服务（上门） */
        String FLOOR_SERVICE_VISIT = "FloorServiceVisit";
        /** 智能客流运营全套落地服务（评价） */
        String FLOOR_SERVICE_COMMENT = "FloorServiceComment";
        /** 物料制作服务（已到店） */
        String MATERIAL_PRODUCTION_ARRIVE = "MaterialProductionArrive";
        /** 物料制作服务（延时） */
        String MATERIAL_PRODUCTION_DELAY = "MaterialProductionDelay";
        /** 物料部署服务 */
        String MATERIAL_DEPLOYMENT = "MaterialDeployment";
        /** 物料部署服务(上门) */
        String MATERIAL_DEPLOYMENT_VISIT = "MaterialDeploymentVisit";
        /** 物料部署服务(评价) */
        String MATERIAL_DEPLOYMENT_COMMENT = "MaterialDeploymentComment";
        /** 上门服务 */
        String VISIT_SERVICE = "VisitService";
        /** 上门服务（上门） */
        String VISIT_SERVICE_VISIT = "VisitServiceVisit";
        /** 上门服务（评价） */
        String VISIT_SERVICE_COMMENT = "VisitServiceComment";
        /** 资料收集服务 */
        String DATA_COLLECT = "DataCollect";
        /** 菜单配置服务 */
        String MENU_CONFIG = "MenuConfig";

    }

}
