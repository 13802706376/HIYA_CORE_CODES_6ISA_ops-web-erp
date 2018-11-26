package com.yunnex.ops.erp.modules.order.dto;

import java.io.Serializable;

/**
 * 流程和服务关联DTO
 * 
 * yunnex
 * 
 * @date 2018年7月18日
 */
public class FlowServiceItemLinkDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String procInsId; // 流程ID
    private String serviceSourceId;// 服务来源ID(关联表erp_order_good_service_info的主键)
    private String delFlag; // 删除标记

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getServiceSourceId() {
        return serviceSourceId;
    }

    public void setServiceSourceId(String serviceSourceId) {
        this.serviceSourceId = serviceSourceId;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
