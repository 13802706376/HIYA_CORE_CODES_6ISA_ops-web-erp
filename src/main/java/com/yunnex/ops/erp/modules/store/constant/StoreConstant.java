package com.yunnex.ops.erp.modules.store.constant;

import com.yunnex.ops.erp.common.constant.Constant;

/**
 * 门店常量类
 */
public interface StoreConstant {


    /**
     * 进件审核状态
     */
    public enum ZhangBeiAuditStatus {
        UN_COMMIT(0, "未提交"), TO_BE_AUDIT(1, "待审核"), PASS(2, "审核通过"), UN_PASS(3, "审核未通过"), OFF_SHELVES(4, "已下架");

        private Integer status;
        private String name;

        ZhangBeiAuditStatus() {}

        ZhangBeiAuditStatus(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static String getByStatus(Integer status) {
            if (status == null) {
                return Constant.BLANK;
            }
            for (ZhangBeiAuditStatus auditStatus : values()) {
                if (auditStatus.getStatus() == status) {
                    return auditStatus.getName();
                }
            }
            return Constant.BLANK;
        }
    }


    /**
     * 门店审核状态
     */
    public enum StoreAuditStatus {
        UN_COMMIT(0, "未提交"), TO_BE_AUDIT(1, "等待审核"), PASS(2, "已通过"), UN_PASS(3, "审核未通过"), OFF_SHELVES(4, "已下架");

        private Integer status;
        private String name;

        StoreAuditStatus() {}

        StoreAuditStatus(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static String getByStatus(Integer status) {
            if (status == null) {
                return Constant.BLANK;
            }
            for (StoreAuditStatus auditStatus : values()) {
                if (auditStatus.getStatus() == status) {
                    return auditStatus.getName();
                }
            }
            return Constant.BLANK;
        }
    }

    /**
     * 支付审核状态
     */
    public enum PayAuditStatus {
        UN_COMMIT(0, "未提交"), NEW_TO_BE_AUDIT(1, "新增待审核"), PASS(2, "已通过"), REJECT(3, "拒绝"), OFF_SHELVES(4, "已下架"), UPDATE_TO_BE_AUDIT(5,
                        "更新待审核"), TO_BE_AUDIT(6, "待审核"), AUDITING(7, "正在审核");

        private Integer status;
        private String name;

        PayAuditStatus() {}

        PayAuditStatus(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static String getByStatus(Integer status) {
            if (status == null) {
                return Constant.BLANK;
            }
            for (PayAuditStatus auditStatus : values()) {
                if (auditStatus.getStatus() == status) {
                    return auditStatus.getName();
                }
            }
            return Constant.BLANK;
        }
    }

    /**
     * 广告主审核状态
     */
    public enum AdvertiserAuditStatus {
        UN_COMMIT(0, "未提交"), TO_BE_AUDIT(1, "待审核"), AUDITING(2, "正在审核"), REJECT(3, "拒绝"), PASS(4, "已通过");

        private Integer status;
        private String name;

        AdvertiserAuditStatus() {}

        AdvertiserAuditStatus(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static String getByStatus(Integer status) {
            if (status == null) {
                return Constant.BLANK;
            }
            for (AdvertiserAuditStatus auditStatus : values()) {
                if (auditStatus.getStatus() == status) {
                    return auditStatus.getName();
                }
            }
            return Constant.BLANK;
        }
    }

    // 门店通过审核状态
    Integer STORE_AUDIT_STATUS_PASS = 2;
    // 支付资料通过审核状态
    Integer PAY_AUDIT_STATUS_PASS = 2;
    // 广告主资料通过审核状态
    Integer ADVERTISER_AUDIT_STATUS_PASS = 4;

    /**
     * OEM错误码对应字典type
     */
    String OEM_ERROR_CODE_DICT_TYPE = "oem_error_code";

    // 门店资料提交审核权限
    String PERMISSION_COMMIT_AUDIT = "shop:auditManage:edit";
    // 门店资料修改权限
    String PERMISSION_STORE_EDIT = "shop:storeData:edit";
    // 门店资料高级修改权限
    String PERMISSION_STORE_ADVANCED_EDIT = "shop:storeData:advancedEdit";

}
