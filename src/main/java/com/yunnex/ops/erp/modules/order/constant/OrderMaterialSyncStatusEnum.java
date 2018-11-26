package com.yunnex.ops.erp.modules.order.constant;

import com.yunnex.ops.erp.common.constant.Constant;

/**
 * 订单物料同步状态常量
 */
public enum OrderMaterialSyncStatusEnum {

    NORMAL("Normal", "正常"), SHOP_NOT_EXIST("ShopNotExist", "商户不存在"), ORDER_MATERIAL_CREATION_FLOW_NOT_EXIST("OrderMaterialCreationFlowNotExist",
                    "订单没有启动物料制作服务相关流程"), SYNC_MATERIAL_STATUS_FAILED("SyncMaterialStatusFailed",
                                    "物料制作状态同步失败"), SHOP_AGENT_NOT_EXIST("ShopAgentNotExist",
                                                    "商户对应的服务商不存在"), ORDER_NOT_EXIST("OrderNotExist", "订单不存在");

    private String name;
    private String displayName;

    OrderMaterialSyncStatusEnum(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public static String getByName(String name) {
        for (OrderMaterialSyncStatusEnum statusEnum : values()) {
            if (statusEnum.getName().equals(name)) {
                return statusEnum.getDisplayName();
            }
        }
        return Constant.BLANK;
    }
}
