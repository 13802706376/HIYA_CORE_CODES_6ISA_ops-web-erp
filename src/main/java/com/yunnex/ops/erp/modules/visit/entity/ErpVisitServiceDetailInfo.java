package com.yunnex.ops.erp.modules.visit.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 上门服务Entity
 * 
 * @author R/Q
 * @version 2018-05-26
 */
public class ErpVisitServiceDetailInfo extends DataEntity<ErpVisitServiceDetailInfo> {

    private static final long serialVersionUID = 1L;
    private String shopInfoId; // 商户ID，erp_shop_info.id
    private String shopInfoName;// 商户名称
    private String serviceAddress; // 上门服务目的地
    private String serviceTypeCode; // 服务类型code，erp_visit_service_item.service_type_code
    private String serviceTypeTxt;// 服务类型中文释义
    private String serviceGoalCode; // 上门服务目的code，erp_visit_service_item.service_goal_code
    private String serviceGoalTxt;// 上门服务目的中文释义
    private String serviceGoal; // 上门服务目的（手填）
    private String serviceUser; // 上门服务人员,sys_user.id
    private String serviceUserName;// 上门服务人员名称
    private String servicePreparationInfo; // 服务人员准备清单
    private String shopAttendees; // 商户参与人员
    private String shopPreparationInfo; // 商户准备清单
    private String trainRecorder; // 培训记录员
    private String serviceReason; // 服务原因
    private String shopNeedsPicture; // 商户需求截图，英文半角分号分隔
    private Date appointedStartTime; // 预约开始时间
    private Date appointedEndTime; // 预约结束时间
    private Date serviceStartTime; // 服务开始时间
    private Date serviceEndTime; // 服务结束时间
    /** 开始时间 */
    private String startDateStr;
    /** 结束时间 */
    private String endDateStr;
    private String auditStatus; // 审核状态 0=已预约，1=待审核，2=已审核（待上门），3=审核不通过，4=已上门，5=已取消
    private String auditStatusTxt;// 审核状态中文释义
    private String auditUser;// 审核人,sys_user.id
    private String auditUserName;// 审核人名称
    private String cancelReason; // 取消原因
    private String modifySuggest; // 修改建议（驳回原因）
    private String receivingReport; // 验收单
    private String procInsId; // 流程实例ID
    private String remark; // 备注
    private String teamId;// 上门服务人员所属团队ID
    private String teamTxt;// 上门服务人员所属团队
    private String score;// 服务评分
    private String hardwareDeliverFlag;//是否需要硬件交付
    private String remindFlag; // 上门提醒标识（Y 已提醒,N 未提醒）
    
    private String cloumnType; // 上门提醒标识（Y 已提醒,N 未提醒）
    // 子数据集合
    private List<ErpVisitServiceItemRecord> itemRecords;// 服务项数据集合
    //上门服务硬件产品
    private List<ErpVisitServiceProductRecord> listProductObj;
    
    
    public String getCloumnType() {
		return cloumnType;
	}

	public void setCloumnType(String cloumnType) {
		this.cloumnType = cloumnType;
	}

	public String getShopInfoId() {
        return shopInfoId;
    }

    public void setShopInfoId(String shopInfoId) {
        this.shopInfoId = shopInfoId;
    }

    @Length(min = 0, max = 100, message = "上门服务目的地长度必须介于 0 和 100 之间")
    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getServiceTypeCode() {
        return serviceTypeCode;
    }

    public void setServiceTypeCode(String serviceTypeCode) {
        this.serviceTypeCode = serviceTypeCode;
    }

    public String getServiceGoalCode() {
        return serviceGoalCode;
    }

    public void setServiceGoalCode(String serviceGoalCode) {
        this.serviceGoalCode = serviceGoalCode;
    }

    @Length(min = 0, max = 100, message = "上门服务目的（手填）长度必须介于 0 和 100 之间")
    public String getServiceGoal() {
        return serviceGoal;
    }

    public void setServiceGoal(String serviceGoal) {
        this.serviceGoal = serviceGoal;
    }

    @Length(min = 0, max = 64, message = "上门服务人员长度必须介于 0 和 64 之间")
    public String getServiceUser() {
        return serviceUser;
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

    public void setServiceUser(String serviceUser) {
        this.serviceUser = serviceUser;
    }

    @Length(min = 0, max = 100, message = "服务人员准备清单长度必须介于 0 和 100 之间")
    public String getServicePreparationInfo() {
        return servicePreparationInfo;
    }

    public void setServicePreparationInfo(String servicePreparationInfo) {
        this.servicePreparationInfo = servicePreparationInfo;
    }

    @Length(min = 0, max = 100, message = "商户参与人员长度必须介于 0 和 100 之间")
    public String getShopAttendees() {
        return shopAttendees;
    }

    public void setShopAttendees(String shopAttendees) {
        this.shopAttendees = shopAttendees;
    }

    @Length(min = 0, max = 100, message = "商户准备清单长度必须介于 0 和 100 之间")
    public String getShopPreparationInfo() {
        return shopPreparationInfo;
    }

    public void setShopPreparationInfo(String shopPreparationInfo) {
        this.shopPreparationInfo = shopPreparationInfo;
    }

    @Length(min = 0, max = 64, message = "培训记录员长度必须介于 0 和 64 之间")
    public String getTrainRecorder() {
        return trainRecorder;
    }

    public void setTrainRecorder(String trainRecorder) {
        this.trainRecorder = trainRecorder;
    }

    @Length(min = 0, max = 100, message = "服务原因长度必须介于 0 和 100 之间")
    public String getServiceReason() {
        return serviceReason;
    }

    public void setServiceReason(String serviceReason) {
        this.serviceReason = serviceReason;
    }

    @Length(min = 0, max = 300, message = "商户需求截图，英文半角分号分隔长度必须介于 0 和 300 之间")
    public String getShopNeedsPicture() {
        return shopNeedsPicture;
    }

    public void setShopNeedsPicture(String shopNeedsPicture) {
        this.shopNeedsPicture = shopNeedsPicture;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "预约开始时间不能为空")
    public Date getAppointedStartTime() {
        return appointedStartTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public void setAppointedStartTime(Date appointedStartTime) {
        this.appointedStartTime = appointedStartTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getAppointedEndTime() {
        return appointedEndTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public void setAppointedEndTime(Date appointedEndTime) {
        this.appointedEndTime = appointedEndTime;
    }


	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	@Length(min = 0, max = 1, message = "审核状态 1=待审核，2=已审核（待上门），3=审核不通过，4=已上门，5=已取消长度必须介于 0 和 1 之间")
    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    @Length(min = 0, max = 100, message = "取消原因长度必须介于 0 和 100 之间")
    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    @Length(min = 0, max = 100, message = "修改建议（驳回原因）长度必须介于 0 和 100 之间")
    public String getModifySuggest() {
        return modifySuggest;
    }

    public void setModifySuggest(String modifySuggest) {
        this.modifySuggest = modifySuggest;
    }

    @Length(min = 0, max = 300, message = "验收单长度必须介于 0 和 300 之间")
    public String getReceivingReport() {
        return receivingReport;
    }

    public void setReceivingReport(String receivingReport) {
        this.receivingReport = receivingReport;
    }

    @Length(min = 0, max = 64, message = "流程实例ID长度必须介于 0 和 64 之间")
    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    @Length(min = 0, max = 256, message = "备注长度必须介于 0 和 256 之间")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getServiceGoalTxt() {
        return serviceGoalTxt;
    }

    public void setServiceGoalTxt(String serviceGoalTxt) {
        this.serviceGoalTxt = serviceGoalTxt;
    }

    public String getServiceUserName() {
        return serviceUserName;
    }

    public void setServiceUserName(String serviceUserName) {
        this.serviceUserName = serviceUserName;
    }

    public String getServiceTypeTxt() {
        return serviceTypeTxt;
    }

    public void setServiceTypeTxt(String serviceTypeTxt) {
        this.serviceTypeTxt = serviceTypeTxt;
    }

    public String getShopInfoName() {
        return shopInfoName;
    }

    public void setShopInfoName(String shopInfoName) {
        this.shopInfoName = shopInfoName;
    }

    public String getAuditStatusTxt() {
        return auditStatusTxt;
    }

    public void setAuditStatusTxt(String auditStatusTxt) {
        this.auditStatusTxt = auditStatusTxt;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public List<ErpVisitServiceItemRecord> getItemRecords() {
        return itemRecords;
    }

    public void setItemRecords(List<ErpVisitServiceItemRecord> itemRecords) {
        this.itemRecords = itemRecords;
    }

    public String getTeamTxt() {
        return teamTxt;
    }

    public void setTeamTxt(String teamTxt) {
        this.teamTxt = teamTxt;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getHardwareDeliverFlag() {
        return hardwareDeliverFlag;
    }

    public void setHardwareDeliverFlag(String hardwareDeliverFlag) {
        this.hardwareDeliverFlag = hardwareDeliverFlag;
    }

    public List<ErpVisitServiceProductRecord> getListProductObj() {
        return listProductObj;
    }

    public void setListProductObj(List<ErpVisitServiceProductRecord> listProductObj) {
        this.listProductObj = listProductObj;
    }

    public String getRemindFlag() {
        return remindFlag;
    }

    public void setRemindFlag(String remindFlag) {
        this.remindFlag = remindFlag;
    }
    
}
