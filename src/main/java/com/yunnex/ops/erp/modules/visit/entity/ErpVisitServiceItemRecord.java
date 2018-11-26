package com.yunnex.ops.erp.modules.visit.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 商户服务对应服务项Entity
 * 
 * @author zjq
 * @date 2018年5月28日
 */
public class ErpVisitServiceItemRecord extends DataEntity<ErpVisitServiceItemRecord> {

    private static final long serialVersionUID = 1268663070434498565L;

    private String serviceInfoId;// 上门服务主键，erp_visit_service_info.id
    private String serviceItemCode;// 服务项code，【其它】时为-1,erp_visit_service_item.service_item_code
    private String serviceItemTxt;// 服务项code对应中文释义
    private String serviceItem;// 服务项（手填）
    private Date serviceStartTime; // 服务开始时间
    private Date serviceEndTime; // 服务结束时间
    private String completeFlag = "N";// 达成标识，Y=完成，N=未完成
    private String remark;// 备注
    private String serviceItemAttendees;// 服务项参与人

    public String getServiceInfoId() {
        return serviceInfoId;
    }

    public void setServiceInfoId(String serviceInfoId) {
        this.serviceInfoId = serviceInfoId;
    }

    public String getServiceItemCode() {
        return serviceItemCode;
    }

    public void setServiceItemCode(String serviceItemCode) {
        this.serviceItemCode = serviceItemCode;
    }

    public String getServiceItemTxt() {
        return serviceItemTxt;
    }

    public void setServiceItemTxt(String serviceItemTxt) {
        this.serviceItemTxt = serviceItemTxt;
    }

    public String getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(String serviceItem) {
        this.serviceItem = serviceItem;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompleteFlag() {
        return completeFlag;
    }

    public void setCompleteFlag(String completeFlag) {
        this.completeFlag = completeFlag;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getServiceStartTime() {
        return serviceStartTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public void setServiceStartTime(Date serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getServiceEndTime() {
        return serviceEndTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public void setServiceEndTime(Date serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public String getServiceItemAttendees() {
        return serviceItemAttendees;
    }

    public void setServiceItemAttendees(String serviceItemAttendees) {
        this.serviceItemAttendees = serviceItemAttendees;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
