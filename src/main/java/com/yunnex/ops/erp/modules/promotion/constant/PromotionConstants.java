package com.yunnex.ops.erp.modules.promotion.constant;

public interface PromotionConstants {

    /**
     * 经营诊断标识
     */
    String DIAGNOSIS = "diagnosis";
    /**
     * 卡券信息推广资料名称标识 卡券信息-coupon_info
     */
    String COUPON_INFO = "coupon_info";

    /*
     * 推广资料名称标识（任务相关文件）
     */
    String ERP_ORDER_FILE = "erp_order_file";

    /*
     * 推广资料名称标识（任务相关资料）
     */
    String ERP_ORDER_INPUT_DETAIL = "erp_order_input_detail";

    String COLON = ":";

    /**
     * 卡券链接类型-数据字典
     */
    String COUPON_LINK_CATEGORY = "coupon_link_category";

    /**
     * 日志操作类型 新增、修改、审核、下载
     */
    String MODIFYING = "修改";

    /**
     * 浏览全部订单的推广资料权限
     */
    String VIEW_ALL_PROMOTION_MATERIALS = "order:erpOrderSplitInfo:viewAllPromotionMaterials";

    /**
     * 经营诊断推广资料名称标识 文案-diagnosis_copywriting
     */
    String DIAGNOSIS_COPYWRITING = "diagnosis_copywriting";
    /**
     * 经营诊断推广资料名称标识 设计-diagnosis_design
     */
    String DIAGNOSIS_DESIGN = "diagnosis_design";
    /**
     * 经营诊断推广资料名称标识 商户-diagnosis_merchant
     */
    String DIAGNOSIS_MERCHANT = "diagnosis_merchant";
    /**
     * 经营诊断推广资料名称标识 投放顾问-diagnosis_consultant
     */
    String DIAGNOSIS_CONSULTANT = "diagnosis_consultant";
    /**
     * 上传类型：朋友圈
     */
    String IMPORT_TYPE_FRIENDS = "friends";
    /**
     * 上传类型：陌陌
     */
    String IMPORT_TYPE_MOMO = "momo";
    /**
     * 上传类型：微博
     */
    String IMPORT_TYPE_WEIBO = "weibo";
}
