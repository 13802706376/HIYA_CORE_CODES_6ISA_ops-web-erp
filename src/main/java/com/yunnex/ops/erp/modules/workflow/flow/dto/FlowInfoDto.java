package com.yunnex.ops.erp.modules.workflow.flow.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderInputDetail;
import com.yunnex.ops.erp.modules.workflow.remarks.entity.WorkflowRemarksInfo;

/**
 * 流程信息
 */
public class FlowInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String flowName; // 流程名
    private OrderInfoDto orderInfoDto; // 订单信息
    private JykInfoDto jykInfoDto; // 聚引客信息
    private List<WorkflowRemarksInfo> remarksInfo; // 备注
    private AuditStatusInfoDto auditStatusInfoDto; // 进件状态信息
    private List<ErpOrderFile> orderFiles; // 任务相关文件
    private List<ErpOrderInputDetail> orderInputDetails; // 任务相关资料
    private String splitId; // 分单ID
    private Map<String, String> erpOrderFlowUsers;
    private ErpOrderOriginalInfo erpOrderOriginalInfo;
    private Map<String, String> flowMap;

    private String flowMark;
    private Date startDate;
    private Date endDate;
    private String assignee;

    public String getFlowMark() {
        return flowMark;
    }

    public void setFlowMark(String flowMark) {
        this.flowMark = flowMark;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Map<String, String> getFlowMap() {
        return flowMap;
    }

    public void setFlowMap(Map<String, String> flowMap) {
        this.flowMap = flowMap;
    }

    public ErpOrderOriginalInfo getErpOrderOriginalInfo() {
        return erpOrderOriginalInfo;
    }

    public void setErpOrderOriginalInfo(ErpOrderOriginalInfo erpOrderOriginalInfo) {
        this.erpOrderOriginalInfo = erpOrderOriginalInfo;
    }

    public Map<String, String> getErpOrderFlowUsers() {
        return erpOrderFlowUsers;
    }

    public void setErpOrderFlowUsers(Map<String, String> erpOrderFlowUsers) {
        this.erpOrderFlowUsers = erpOrderFlowUsers;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public OrderInfoDto getOrderInfoDto() {
        return orderInfoDto;
    }

    public void setOrderInfoDto(OrderInfoDto orderInfoDto) {
        this.orderInfoDto = orderInfoDto;
    }

    public JykInfoDto getJykInfoDto() {
        return jykInfoDto;
    }

    public void setJykInfoDto(JykInfoDto jykInfoDto) {
        this.jykInfoDto = jykInfoDto;
    }

    public List<WorkflowRemarksInfo> getRemarksInfo() {
        return remarksInfo;
    }

    public void setRemarksInfo(List<WorkflowRemarksInfo> remarksInfo) {
        this.remarksInfo = remarksInfo;
    }

    public AuditStatusInfoDto getAuditStatusInfoDto() {
        return auditStatusInfoDto;
    }

    public void setAuditStatusInfoDto(AuditStatusInfoDto auditStatusInfoDto) {
        this.auditStatusInfoDto = auditStatusInfoDto;
    }

    public List<ErpOrderFile> getOrderFiles() {
        return orderFiles;
    }

    public void setOrderFiles(List<ErpOrderFile> orderFiles) {
        this.orderFiles = orderFiles;
    }

    public List<ErpOrderInputDetail> getOrderInputDetails() {
        return orderInputDetails;
    }

    public void setOrderInputDetails(List<ErpOrderInputDetail> orderInputDetails) {
        this.orderInputDetails = orderInputDetails;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }
}
