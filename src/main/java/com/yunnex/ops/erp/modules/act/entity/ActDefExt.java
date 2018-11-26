package com.yunnex.ops.erp.modules.act.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 流程节点扩展Entity
 * @author 1
 * @version 2017-11-30
 */
public class ActDefExt extends DataEntity<ActDefExt> {

    private static final long serialVersionUID = 1L;
    private String processDefineKey; // 流程定义KEY
    private String actId; // 节点ID
    private String roleId; // 角色ID
    private String roleName; // 角色名称
    private String assignee; // 处理人
    private String formTemplate; // 表单模板
    private String emailTemplate; // 邮件配置
    private String callbackService; // 回调服务
    private String remark; // 备注
    private String createUser; // 创建人
    private String updateUser; // 修改人

    private String taskDateHours; // 正常工时
    private String urgentTaskDateHours; // 加急工时
    private String taskUserRole; // 指派的角色
    private String taskUserRole2; // 指派的角色2

    private String isKeyPoint = Constant.NO; // 是否是关键任务点，N：否，Y：是
    private Integer normalTaskHours = 0; // 正常既定消耗工时
    private Integer urgentTaskHours = 0; // 紧急既定消耗工时
    // 从待生产库激活后正常既定消耗工时
    private Integer activationNormalTaskHours = 0;
    // 从待生产库激活后紧急既定消耗工时
    private Integer activationUrgentTaskHours = 0;
    
    private String actType;

    public ActDefExt() {
        super();
    }

    public ActDefExt(String id) {
        super(id);
    }

    @Length(min = 1, max = 164, message = "流程定义KEY长度必须介于 1 和 164 之间")
    public String getProcessDefineKey() {
        return processDefineKey;
    }

    public void setProcessDefineKey(String processDefineKey) {
        this.processDefineKey = processDefineKey;
    }

    @Length(min = 1, max = 164, message = "节点ID长度必须介于 1 和 164 之间")
    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    @Length(min = 1, max = 164, message = "角色ID长度必须介于 1 和 164 之间")
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Length(min = 1, max = 300, message = "角色名称长度必须介于 1 和 300 之间")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Length(min = 1, max = 200, message = "处理人长度必须介于 1 和 200 之间")
    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getFormTemplate() {
        return formTemplate;
    }

    public void setFormTemplate(String formTemplate) {
        this.formTemplate = formTemplate;
    }

    @Length(min = 0, max = 2000, message = "邮件配置长度必须介于 0 和 2000 之间")
    public String getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(String emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    @Length(min = 0, max = 2000, message = "回调服务长度必须介于 0 和 2000 之间")
    public String getCallbackService() {
        return callbackService;
    }

    public void setCallbackService(String callbackService) {
        this.callbackService = callbackService;
    }

    @Length(min = 0, max = 2000, message = "备注长度必须介于 0 和 2000 之间")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Length(min = 0, max = 64, message = "创建人长度必须介于 0 和 64 之间")
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Length(min = 0, max = 64, message = "修改人长度必须介于 0 和 64 之间")
    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getTaskDateHours() {
        return taskDateHours;
    }

    public void setTaskDateHours(String taskDateHours) {
        this.taskDateHours = taskDateHours;
    }

    public String getUrgentTaskDateHours() {
        return urgentTaskDateHours;
    }

    public void setUrgentTaskDateHours(String urgentTaskDateHours) {
        this.urgentTaskDateHours = urgentTaskDateHours;
    }

    public String getTaskUserRole() {
        return taskUserRole;
    }

    public void setTaskUserRole(String taskUserRole) {
        this.taskUserRole = taskUserRole;
    }

    public String getTaskUserRole2() {
        return taskUserRole2;
    }

    public void setTaskUserRole2(String taskUserRole2) {
        this.taskUserRole2 = taskUserRole2;
    }

    public String getIsKeyPoint() {
        return isKeyPoint;
    }

    public void setIsKeyPoint(String isKeyPoint) {
        this.isKeyPoint = isKeyPoint;
    }

    public Integer getNormalTaskHours() {
        return normalTaskHours;
    }

    public void setNormalTaskHours(Integer normalTaskHours) {
        this.normalTaskHours = normalTaskHours;
    }

    public Integer getUrgentTaskHours() {
        return urgentTaskHours;
    }

    public void setUrgentTaskHours(Integer urgentTaskHours) {
        this.urgentTaskHours = urgentTaskHours;
    }

    public Integer getActivationNormalTaskHours() {
        return activationNormalTaskHours;
    }

    public void setActivationNormalTaskHours(Integer activationNormalTaskHours) {
        this.activationNormalTaskHours = activationNormalTaskHours;
    }

    public Integer getActivationUrgentTaskHours() {
        return activationUrgentTaskHours;
    }

    public void setActivationUrgentTaskHours(Integer activationUrgentTaskHours) {
        this.activationUrgentTaskHours = activationUrgentTaskHours;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }
    
}
