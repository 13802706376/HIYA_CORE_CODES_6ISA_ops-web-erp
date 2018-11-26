package com.yunnex.ops.erp.modules.workflow.flow.constant;

public interface FlowFormConstant {
  
    
    /**智慧餐厅菜单配置任务操作备注 勾选框*/
    public static final String ZHCT_MENU_CONFIG_MEMO = "zhctMenuConfigMemo";
    /**联系商户  任务操作备注 勾选框*/
    public static final String CONTACT_SHOP_MEMO = "contactShopMemo";
    /**建立商户服务群  任务操作备注  勾选框*/
    public static final String SHOP_SERVICEGROUP_MEMO ="shopServiceGroupMemo";
    /**任务操作  勾选框*/
    public static final String  FLOW_OPERATOR= "flowOperator";
    /**确认商户是否有微信公众号 */
    public static final String  IS_SHOP_WEIXIN_ID= "isShopweixinID";
    /***确认商户是否 有口碑门店*/
    public static final String  IS_SHOP_ALIPAY_ID= "isShopAliPayID";
    /***确认商户是否 有打印机*/
    public static final String  IS_SHOP_PRINTER_ID= "isShopPrinterID";
    /***店内交换机接口是否够连接打印机*/
    public static final String  IS_SHOP_CONNECT_PRINTER_ID= "isConnectPrinter";
    /***商户店内布线及桌台场景*/
    public static final String  SHOP_DESC= "shopDesc";
    /***确认商户是否 有收银机*/
    public static final String  IS_SHOP_CASH_REGISTER_ID= "isShopCashRegisterID";
    /**安装智慧餐厅客户端的电脑是否可以联网*/
    public static final String  IS_CONNECT_INTERNET_ID= "isConnectInterID";
    /**完成转发小程序 勾选框*/
    public static final String  COMPLETE_TO_APPLET ="completeToApplet";
    /**掌贝后台创建门店 勾选框*/
    public static final String COMPLETE_CREATE_STORE="completeCreateStore";
    /**公众号 公开信息 截图 文件id*/
    public static final String PUBLIC_FILE_INFO_ID="publicFileInfoId";
    /**公众号  开通操作 任务操作备忘  勾选框*/
    public static final String  WEI_XIN_ACCOUNT_OPEN_MEMO ="weixinAccountOpenMemo";
    /**完成微信支付验证 账户 勾选框  */
    public static final String  WEI_XIN_PAY_CHECK= "weixinPayCheck";
    /**完成微信支付商户号配置 勾选框*/
    public static final String  WEI_XIN_PAY_MERNO_DEPLOY= "weixinPayMerNoDeploy";
    /**支付宝口碑申请 任务操作 备忘*/
    public static final String  ALIPA_MEMO= "ailipaMemo";
    /**基于商户原因 是否开通支付宝口碑*/
    public static final String  OPEN_ALIPA_FLAG= "openAlipaflag";
    /**口碑后台 详解截图*/
    public static final String  ALIPA_ACCOUNT_SCREENSHOT="alipaAccountScreenshot";
    /**掌贝后台 绑定口碑门店 截图*/
    public static final String  ZHANGBEI_BOUND_STORE_SCREENSHOT="zhangbeiBoundStoreScreenshot";
    /**不开通支付宝口碑聊天截图*/
    public static final String   NOT_OPEN_ALIPA_CHAT_SCREENSHOT= "notOpenAlipaChatScreenshot";
    /**完成在掌贝后台机具号配置  勾选框*/
    public static final String MACHINE_NO_CONFIGURATION = "machineNoConfiguration";
    /**基于商户原因  开通 不开通 银联*/
    public static final String UNIONPAY_OPEN_STATE= "unionpayOpenState";
    /**不开通银联支付 聊天截图*/
    public static final String  NOT_OPEN_UNIONPAY_SCREENSHOT="notOpenUnionpayScreenshot";
    /**完成银联支付和配置 勾选框*/
    public static final String UNIONPAY_TRAIN_TEST= "unionpayTrainTest";
    /**确认物料部署指导视频已经发给商户*/
    public static final String COMPLETE_MATERIAL_DEPLOY_VIDEOSHOP="completeMaterialDeployVideoShop";
    
}
