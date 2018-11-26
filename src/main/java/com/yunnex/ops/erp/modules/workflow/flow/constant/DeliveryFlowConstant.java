package com.yunnex.ops.erp.modules.workflow.flow.constant;

public interface DeliveryFlowConstant {

    // 流程节点变量
    /** 查看订单信息,指派订单处理人员 **/
    public static final String ASSIGN_ORDER_HANDLERS = "assign_order_handlers";
    
    /** 电话联系商户,确认服务内容 **/
    public static final String TELEPHONE_CONFIRM_SERVICE = "telephone_confirm_service";
    /** 电话联系商户,确认服务内容(智慧餐厅+客常来) **/
    public static final String TELEPHONE_CONFIRM_SERVICE_ZHCT = "telephone_confirm_service_zhct";
    
  
    
    /** 智慧餐厅菜单配置 **/
    public static final String ZHCT_MENU_CONFIGURATION = "zhct_menu_configuration";
    
    public static final String ZHCT_MENU_CONFIGURATION_NAME = "智慧餐厅菜单配置";
    
    /** 进件资料收集 **/
    public static final String INTO_MATERIAL_COLLECTION = "into_material_collection";
    
    /** 掌贝门店创建 **/
    public static final String ZHANGBEI_STORE_CREATE = "zhangbei_store_create";
    
    /** 支付宝口碑申请 **/
    public static final String ALIPAY_PUBLIC_PRAISE_APPLY = "alipay_public_praise_apply";
    
    /** 公众号开通 **/
    public static final String PUBLIC_NUMBER_OPEN = "public_number_open";
    
    /** 微信账号开通 **/
    public static final String WECHAT_ACCOUNT_OPEN = "wechat_account_open";
    
    /** 微信支付商户号配置 **/
    public static final String WECHAT_SHOP_CONFIGURATION = "wechat_shop_configuration";
    
    /** 银联支付开通 **/
    public static final String UNIONPAY_ACCOUNT_OPEN = "unionpay_account_open";
    
    /** 银联支付培训测试（远程） **/
    public static final String UNIONPAY_ACCOUNT_TRAIN = "unionpay_account_train";
    /** 掌贝账号开通(提示) **/
    public static final String ZHANGBEI_ACCOUNT_OPEN = "zhangbei_account_open";
    
    /** 上门服务完成（首次营销策划服务）任务 完成节点 */
    String VISIT_SERVICE_COMPLETE_FIRST = "visit_service_complete_public";
    String VISIT_SERVICE_COMPLETE_FIRST_M = "visit_service_complete_public_FMPS_FMPS_M";
    String VISIT_SERVICE_COMPLETE_ZHCT ="visit_service_complete_zhct";
    /** 上门服务完成（物料服务）任务 完成节点 */
    String VISIT_SERVICE_COMPLETE_MATERIAL = "visit_service_complete_material";
    /** 上门服务完成（JYK）任务 完成节点 */
    String VISIT_SERVICE_COMPLETE_JYK = "visit_service_complete_jyk";
    /** 制作物料内容提交 完成节点 */
    String MATERIAL_MAKE_SUBMIT = "material_make_submit";
    /** 商户资料收集 任务 完成节点 */
    String SHOP_INFO_COLLECTION = "shop_info_collection";
    String SHOP_INFO_COLLECTION_NAME="商户信息收集(为上门策划&交付做准备)";
    
    /** 电话预约商户(聚引客) 完成节点 */
    String VISIT_SERVICE_SUBSCRIBE_JYK = "visit_service_subscribe_jyk";
    /** 物料制作跟踪(物料更新服务) */
    String MATERIAL_MAKE_FOLLOW_UPDATE="material_make_follow_update";
    /** 物料制作跟踪 */
    String MATERIAL_MAKE_FOLLOW="material_make_follow";
    /** 物料制作跟踪新版 */
    String MATERIAL_MAKE_FOLLOW_FIRST = "material_make_follow_first";

    
    
    /** 电话预约商户 */
    String   VISIT_SERVICE_SUBSCRIBE_PUBLIC = "visit_service_subscribe_public";
    /** 上门服务预约申请 */
    String  VISIT_SERVICE_APPLY_PUBLIC = "visit_service_apply_public";
    /** 审核上门服务预约申请 */
    String  VISIT_SERVICE_REVIEW_PUBLIC = "visit_service_review_public";
    /** 上门服务预约申请不通过&修改 */
    String  VISIT_SERVICE_MODIFY_PUBLIC = "visit_service_modify_public";
    /** 上门服务提醒 */
    String  VISIT_SERVICE_REMIND_PUBLIC = "visit_service_remind_public";
    /** 上门服务完成 */
    String  VISIT_SERVICE_COMPLETE_PUBLIC= "visit_service_complete_public";
    
    
    
    
    
    
    /** 开通服务结束监听 */
    String OPEN_SERVICE_END_LISTENER = "open_service_end_listener";
    /** 上门服务提醒 */
    String VISIT_SERVICE_REMIND_JYK = "visit_service_remind_jyk";
    /** 上门服务提醒 */
    String VISIT_SERVICE_REMIND_FIRST = "visit_service_remind_first";
    /** 上门服务提醒 */
    String VISIT_SERVICE_REMIND_MATERIAL = "visit_service_remind_material";

    String VISIT_SERVICE_REMIND = "visit_service_remind";

    String  VISIT_SERVICE_REMIND_FIRST_3V3 = "visit_service_remind_first_3.3";
    

    // 流程变量
    /** 有无 掌贝账号 */
    public static final String ZHANG_BEI_FLAG = "zhangbeiFlag";
    /** 有无口碑门店 */
    public static final String  PUBLIC_PRAISE_FLAG="publicPraiseFlag";
    /** 有无 微信公众号公众号 */
    public static final String PUBLIC_NUMBER_FLAG= "publicNumberFlag";
    /** 有无 微信账号 */
    public static final String WECHAT_ACCOUNT_FLAG="wechatAccountFlag";
    /** 有无 银联账号 */
    public static final String UNIONPAY_ACCOUNT_FLAG="unionPayAccountFlag";
    /** 是否 开通银联支付 */
    public static final String UNIONPAY_OPEN_FLAG="unionpayOpenFlag";
    /** 电话预约商户(智慧餐厅安装交付服务) */
	public static final String VISIT_SERVICE_SUBSCRIBE_ZHCT = "visit_service_subscribe_zhct";


    /** 服务类型 （聚引客 、 客常来） */
    String SERVICE_TYPE = "serviceType";
    String VISIT_TYPE = "visitType";
    String FIRST_SERVICE_M_SIGN="mSign";
    String VISIT_TYPE_FMPS_I = "FMPS_I";// 流程启动初始化流程变量 区分首次营销策划中的 首次上门和 物料上门
    String SERVICE_M_SIGN = "Y";// 有值说明已经走到物料了
    String ZHCT_FLAG = "zhctFlag";// 是否包含智慧餐厅
    String ZHCT_ACT_TYPE = "zhctActType";// 区分智慧餐厅走那条类型的流程
    // 流程变量值
    /** 首次营销 */
    String SERVICE_TYPE_KE_CHANG_LAI = "FMPS";
    /** 聚引客交付服务 */
    String SERVICE_TYPE_JU_YIN_KE = "JYK";
    /** 掌贝平台交付服务 */
    String SERVICE_TYPE_JU_YIN_DATA = "INTO_PIECES";
    /** 首次上门服务（基础版） */
    String SERVICE_TYPE_KE_CHANG_LAI_BASIC = "FMPS_BASIC";
    /** 智慧餐厅 交付 服务 流程 */
    String SERVICE_TYPE_KE_ZHCT = "ZHCT";
    /** 智慧餐厅 （老商户） 流程 */
    String SERVICE_TYPE_KE_ZHCT_OLD = "ZHCT_OLD";
    /** 物料跟新 */
    String SERVICE_TYPE_MU = "MU";
    /** 上门收费 */
    String SERVICE_TYPE_VC = "VC";

    /** 物料实施 */
    String VISIT_TYPE_FMPS_M = "FMPS_M";
    /** 重启标识 */
    String RESET_FLAG = "resetFlag";
    
    String  UP_FLOW_FLAG= "upFlowFlag";

}
