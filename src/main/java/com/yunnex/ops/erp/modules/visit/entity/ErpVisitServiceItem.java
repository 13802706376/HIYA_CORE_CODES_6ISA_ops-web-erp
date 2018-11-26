package com.yunnex.ops.erp.modules.visit.entity;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 服务类型Entity
 * 
 * @author zjq
 * @date 2018年5月29日
 */
public class ErpVisitServiceItem extends DataEntity<ErpVisitServiceInfo> {

    private static final long serialVersionUID = -5220652160552166774L;

    private String serviceTypeCode;// 服务类型code
    private String serviceType;// 服务类型
    private String serviceGoalCode;// 服务目的code
    private String serviceGoal;// 服务目的
    private String serviceItemCode;// 服务项code
    private String serviceItem;// 服务项
    private Integer serviceTimeLength;// 服务时长（分钟）
    private String serviceTimeLengthTxt;// 服务时长（显示字段）
    private String serviceItemAttendees;// 服务项参与人
    private String serviceFlag;// 服务标识，0=基本介绍类，1=支付开通类，2=营销策划类，3=培训类，4=物料类，5=售后服务类
    private String defaultFlag;// 默认标识，Y=是，N=否（需要商户确认是否需要该服务）
    private String remark;// 备注

	public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceGoalCode() {
        return serviceGoalCode;
    }

    public void setServiceGoalCode(String serviceGoalCode) {
        this.serviceGoalCode = serviceGoalCode;
    }

    public String getServiceGoal() {
        return serviceGoal;
    }

    public void setServiceGoal(String serviceGoal) {
        this.serviceGoal = serviceGoal;
    }

    public String getServiceItemCode() {
        return serviceItemCode;
    }

    public void setServiceItemCode(String serviceItemCode) {
        this.serviceItemCode = serviceItemCode;
    }

    public String getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(String serviceItem) {
        this.serviceItem = serviceItem;
    }

    public Integer getServiceTimeLength() {
        return serviceTimeLength;
    }

    public void setServiceTimeLength(Integer serviceTimeLength) {
        this.serviceTimeLength = serviceTimeLength;
    }

    public String getServiceTimeLengthTxt() {
        return serviceTimeLengthTxt;
    }

    public void setServiceTimeLengthTxt(String serviceTimeLengthTxt) {
        this.serviceTimeLengthTxt = serviceTimeLengthTxt;
    }

    public String getServiceItemAttendees() {
        return serviceItemAttendees;
    }

    public void setServiceItemAttendees(String serviceItemAttendees) {
        this.serviceItemAttendees = serviceItemAttendees;
    }

    public String getServiceFlag() {
        return serviceFlag;
    }

    public void setServiceFlag(String serviceFlag) {
        this.serviceFlag = serviceFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
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
