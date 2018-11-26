package com.yunnex.ops.erp.modules.statistics.constant;

public interface DeliveryServiceStatisticsConstants {

    /** 开户顾问是否延迟 提示内容 */
    String OPEN_DELAY_FLAG_CONTENT = "开户顾问延迟完成服务是指服务启动后，%s个工作日内未能完成“银联支付培训&测试（远程）”任务";
    /** 物料顾问是否延迟 提示内容 */
    String MATERIEL_DELAY_FLAG_CONTENT = "物料顾问延迟完成服务是指“物料制作内容提交“任务完成后，%s个工作日内未能完成”物料制作跟踪“任务";
    /** 运营顾问是否延迟 提示内容 */
    String OPERATION_DELAYFLAG_CONTENT = "运营顾问延迟完成服务是指服务启动后，%s个工作日内未能完成“上门服务完成（首次营销策划服务）”任务";
    /** 售后上门培训（收费） 提示内容 */
    String VC_DELAYFLAG_CONTENT = "运营顾问延迟完成服务是指服务启动后，%s个工作日内完成“售后上门培训（收费）”任务";
    /** 物料更新服务 提示内容 */
    String MUBU_DELAYFLAG_CONTENT = "运营顾问延迟完成服务是指服务启动后，%s个工作日内未完成”物料更新服务——物料部署“任务";
    /** 物料更新服务 提示内容 */
    String MU_DELAYFLAG_CONTENT = "物料顾问延迟完成服务是指服务启动后，%s个工作日内未确认“物料已到店”（“物料已到店”为物料跟进状态）";
    /**
     * 时间维度类型
     * 
     * @author linqunzhi
     * @date 2018年5月31日
     */
    interface DateType {
        String BUY_CODE = "buy";
        String BUY_NAME = "购买时间";
        String START_CODE = "start";
        String START_NAME = "启动时间";
        String SHOULD_FLOW_END_CODE = "shouldFlowEnd";
        String SHOULD_FLOW_END_NAME = "应完成交付时间";
    }

    /**
     * 服务类型
     * 
     * @author linqunzhi
     * @date 2018年5月31日
     */
    interface ServiceType {
        String JU_YING_KE_CODE = "{\"includeList\":[5],\"notIncludeList\":[6,11]}";
        String JU_YING_KE_NAME = "聚引客";
        String KE_CHANG_LAI_CODE = "{\"includeList\":[6],\"notIncludeList\":[5,11]}";
        String KE_CHANG_LAI_NAME = "客常来";
        String ZHI_HUI_CODE = "{\"includeList\":[11],\"notIncludeList\":[5,6]}";
        String ZHI_HUI_NAME = "智慧餐厅";
    }
    
    /**
     * 服务类型
     * 
     * @author linqunzhi
     * @date 2018年5月31日
     */
    interface ServiceName {
        String JU_YING_KE_CODE = "{\"includeList\":[5],\"notIncludeList\":[6]}";
        String JU_YING_KE_NAME = "只包含“聚引客”，不包含“客常来”";
        String KE_CHANG_LAI_CODE = "{\"includeList\":[6],\"notIncludeList\":[5]}";
        String KE_CHANG_LAI_NAME = "只包含“客常来”，不包含“聚引客”";
        String ALL_CODE = "{\"includeList\":[5,6]}";
        String ALL_NAME = "同时包含”聚引客“和”客常来“";
    }
    
    /**
     * 服务类型
     * 
     * @author linqunzhi
     * @date 2018年5月31日
     */
    interface ServiceCode {
        String FMPS_CODE = "FMPS";
        String FMPS = "智慧客流运营全套落地服务";
        String JYK_CODE = "JYK";
        String JYK = "聚引客安装交付服务";
        String SRIDS_CODE = "ZHCT";
        String SRIDS = "智慧餐厅安装交付服务";
        String INTOPIECES_CODE = "INTO_PIECES";
        String INTOPIECES = "掌贝平台交付服务";
        String FMPS_BASIC_CODE = "FMPS_BASIC";
        String FMPS_BASIC = "首次上门服务基础版";
    }

}

