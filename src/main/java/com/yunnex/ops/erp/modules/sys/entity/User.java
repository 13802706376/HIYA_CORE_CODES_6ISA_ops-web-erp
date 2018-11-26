/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.sys.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.common.supcan.annotation.treelist.cols.SupCol;
import com.yunnex.ops.erp.common.utils.Collections3;
import com.yunnex.ops.erp.common.utils.excel.annotation.ExcelField;
import com.yunnex.ops.erp.common.utils.excel.fieldtype.RoleListType;
import com.yunnex.ops.erp.modules.agent.entity.ErpAgentInfo;
import com.yunnex.ops.erp.modules.sys.dto.UserRoleDto;

/**
 * 用户Entity
 * @author ThinkGem
 * @version 2013-12-05
 */
public class User extends DataEntity<User> {

    private static final long serialVersionUID = 1L;
    private Office company;    // 归属公司
    private Office office;    // 归属部门
    private String loginName;// 登录名
    private String password;// 密码
    private String no;        // 工号
    private String name;    // 姓名
    private String email;    // 邮箱
    private String phone;    // 电话
    private String mobile;    // 手机
    private String userType;// 用户类型
    private String loginIp;    // 最后登陆IP
    private Date loginDate;    // 最后登陆日期
    private String loginFlag;    // 是否允许登陆
    private String photo;    // 头像
    private String salt; // 盐
    private String employeeId; // 员工ID
    private Integer sort; // 排序

    private String oldLoginName;// 原登录名
    private String newPassword;    // 新密码
    
    private String oldLoginIp;    // 上次登陆IP
    private Date oldLoginDate;    // 上次登陆日期

    /*
     * <if test="type != null and type != ''">type = #{type},</if> <if
     * test="agentUserFlag != null and agentUserFlag != ''">agent_user_flag = #{agentUserFlag},</if> <if
     * test="agentId != null and agentId != ''">agent_info_id = #{agentId},</if>
     */
    // 类型，erp: ERP用户，agent: 服务商用户
    private String type;
    private String agentUserFlag; // 是否是服务商对应的用户，Y：是，N：否
    private Integer agentId; // 所属服务商编号

    private Role role;    // 根据角色查询用户条件
    
    private Integer updatePasswordFlag;// 根据id查询密码是否修改，0未修改；

    private List<UserRoleDto> userRoles;
    
    /**
     * 花名
     */
    private String nickName;
    /**
     * 评分
     */
    private String score;
    /**
     * 头像图片
     */
    private String iconImg;
    /**
     * 服务商名称
     */
    private String agentName;
    /**
     * 服务商信息
     */
    private ErpAgentInfo agentInfo;

    public Integer getUpdatePasswordFlag() {
        return updatePasswordFlag;
    }

    public void setUpdatePasswordFlag(Integer updatePasswordFlag) {
        this.updatePasswordFlag = updatePasswordFlag;
    }

    public List<UserRoleDto> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRoleDto> userRoles) {
        this.userRoles = userRoles;
    }

    private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表

    public User() {
        super();
        this.loginFlag = Global.YES;
    }
    
    public User(String id){
        super(id);
    }

    public User(String id, String loginName){
        super(id);
        this.loginName = loginName;
    }

    public User(Role role){
        super();
        this.role = role;
    }
    
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    @SupCol(isUnique="true", isHide="true")
    @ExcelField(title="ID", type=1, align=2, sort=1)
    @Override
    public String getId() {
        return id;
    }

    @JsonIgnore
    @NotNull(message="归属公司不能为空")
    @ExcelField(title="归属公司", align=2, sort=20)
    public Office getCompany() {
        return company;
    }

    public void setCompany(Office company) {
        this.company = company;
    }
    
    @JsonIgnore
    @NotNull(message="归属部门不能为空")
    @ExcelField(title="归属部门", align=2, sort=25)
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    @Length(min=1, max=100, message="登录名长度必须介于 1 和 100 之间")
    @ExcelField(title="登录名", align=2, sort=30)
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @JsonIgnore
    @Length(min=1, max=100, message="密码长度必须介于 1 和 100 之间")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
    @ExcelField(title="姓名", align=2, sort=40)
    public String getName() {
        return name;
    }
    
    @Length(min=1, max=100, message="工号长度必须介于 1 和 100 之间")
    @ExcelField(title="工号", align=2, sort=45)
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Email(message="邮箱格式不正确")
    @Length(min=0, max=200, message="邮箱长度必须介于 1 和 200 之间")
    @ExcelField(title="邮箱", align=1, sort=50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Length(min=0, max=200, message="电话长度必须介于 1 和 200 之间")
    @ExcelField(title="电话", align=2, sort=60)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Length(min=0, max=200, message="手机长度必须介于 1 和 200 之间")
    @ExcelField(title="手机", align=2, sort=70)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @ExcelField(title="备注", align=1, sort=900)
    @Override
    public String getRemarks() {
        return remarks;
    }
    
    @Length(min=0, max=100, message="用户类型长度必须介于 1 和 100 之间")
    @ExcelField(title="用户类型", align=2, sort=80, dictType="sys_user_type")
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @ExcelField(title="创建时间", type=0, align=1, sort=90)
    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @ExcelField(title="最后登录IP", type=1, align=1, sort=100)
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title="最后登录日期", type=1, align=1, sort=110)
    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getOldLoginName() {
        return oldLoginName;
    }

    public void setOldLoginName(String oldLoginName) {
        this.oldLoginName = oldLoginName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldLoginIp() {
        if (oldLoginIp == null){
            return loginIp;
        }
        return oldLoginIp;
    }

    public void setOldLoginIp(String oldLoginIp) {
        this.oldLoginIp = oldLoginIp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getOldLoginDate() {
        if (oldLoginDate == null){
            return loginDate;
        }
        return oldLoginDate;
    }

    public void setOldLoginDate(Date oldLoginDate) {
        this.oldLoginDate = oldLoginDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAgentUserFlag() {
        return agentUserFlag;
    }

    public void setAgentUserFlag(String agentUserFlag) {
        this.agentUserFlag = agentUserFlag;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @JsonIgnore
    @ExcelField(title="拥有角色", align=1, sort=800, fieldType=RoleListType.class)
    public List<Role> getRoleList() {
        return roleList;
    }
    
    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @JsonIgnore
    public List<String> getRoleIdList() {
        List<String> roleIdList = Lists.newArrayList();
        for (Role role : roleList) {
            roleIdList.add(role.getId());
        }
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        roleList = Lists.newArrayList();
        for (String roleId : roleIdList) {
            Role role = new Role();
            role.setId(roleId);
            roleList.add(role);
        }
    }
    
    /**
     * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
     */
    public String getRoleNames() {
        return Collections3.extractToString(roleList, "name", ",");
    }
    
    public boolean isAdmin(){
        return isAdmin(this.id);
    }
    
    public static boolean isAdmin(String id){
        return id != null && "1".equals(id);
    }
    
    @Override
    public String toString() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getIconImg() {
        return iconImg;
    }

    public void setIconImg(String iconImg) {
        this.iconImg = iconImg;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public ErpAgentInfo getAgentInfo() {
        return agentInfo;
    }

    public void setAgentInfo(ErpAgentInfo agentInfo) {
        this.agentInfo = agentInfo;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
