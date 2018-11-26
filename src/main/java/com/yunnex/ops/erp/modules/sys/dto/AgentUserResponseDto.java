package com.yunnex.ops.erp.modules.sys.dto;

import java.io.Serializable;
import java.util.List;

import com.yunnex.ops.erp.modules.sys.entity.Role;

/**
 * 服务商用户信息
 */
public class AgentUserResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // 用户ID
    private String employeeId; // 员工ID
    private String userName; // 姓名（用户/员工）
    private String mobile; // 手机号
    private String email; // 邮箱
    private String loginName; // 登录账号
    private Integer agentId; // 服务商编号
    private String agentUserFlag; // 是否是服务商对应的用户，Y：是，N：否
    private String jobId; // 工号ID
    private String jobNumber; // 工号
    private String jobIconImg; // 工号头像
    private String jobScore; // 工号评分
    private Boolean isAgentAdmin; // 是否是服务商管理员
    private List<UserRoleResponseDto> userRoles; // 用户的角色
    private List<Role> agentRoles; // 服务商角色

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getAgentUserFlag() {
        return agentUserFlag;
    }

    public void setAgentUserFlag(String agentUserFlag) {
        this.agentUserFlag = agentUserFlag;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getJobIconImg() {
        return jobIconImg;
    }

    public void setJobIconImg(String jobIconImg) {
        this.jobIconImg = jobIconImg;
    }

    public String getJobScore() {
        return jobScore;
    }

    public void setJobScore(String jobScore) {
        this.jobScore = jobScore;
    }

    public Boolean getAgentAdmin() {
        return isAgentAdmin;
    }

    public void setAgentAdmin(Boolean agentAdmin) {
        isAgentAdmin = agentAdmin;
    }

    public List<UserRoleResponseDto> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRoleResponseDto> userRoles) {
        this.userRoles = userRoles;
    }

    public List<Role> getAgentRoles() {
        return agentRoles;
    }

    public void setAgentRoles(List<Role> agentRoles) {
        this.agentRoles = agentRoles;
    }
}
