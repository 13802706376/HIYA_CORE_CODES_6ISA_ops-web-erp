package com.yunnex.ops.erp.modules.sys.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.yunnex.ops.erp.common.utils.RegexUtils;
import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.sys.entity.Role;

/**
 * 服务商用户保存Dto
 */
public class AgentUserSaveDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId; // 用户ID
    private String jobId; // 工号ID
    private String employeeId; // 员工ID
    @NotNull(message = "服务商编号不能为空！")
    private Integer agentId; // 服务商ID
    @NotBlank(message = "姓名不能为空！")
    @Pattern(regexp = RegexUtils.REGEX_CN_EN_DIGIT + SysConstant.NAME_LENGTH, message = "姓名只能包含中文、英文和数字并且长度必须在 2-32 个字符之间！")
    private String userName; // 姓名（用户/员工）
    @NotBlank(message = "手机号不能为空！")
    @Pattern(regexp = RegexUtils.REGEX_MOBILE, message = "手机号格式不正确！") // 以1开头的数字，共11位
    private String mobile; // 手机号
    @NotBlank(message = "邮箱不能为空！")
    @Email(regexp = RegexUtils.REGEX_EMAIL, message = "邮箱格式不正确！")
    private String email; // 邮箱
    @NotBlank(message = "登录账号不能为空！")
    @Pattern(regexp = RegexUtils.REGEX_EN_DIGIT + SysConstant.NAME_LENGTH, message = "登录账号只能为字母和数字并且长度必须在 2-32 个字符之间！")
    private String loginName; // 登录账号
    @NotBlank(message = "工号不能为空！")
    private String jobNumber; // 工号
    @NotBlank(message = "工号头像不能为空！")
    private String jobIconImg; // 工号头像
    @NotBlank(message = "工号评分不能为空！")
    @Min(value = 3, message = "工号评分最少 {value} 分")
    private String jobScore; // 工号评分
    @NotNull(message = "请选择角色！")
    @Size(min = 1, message = "至少选择 {min} 个角色")
    private List<Role> roles; // 角色

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
