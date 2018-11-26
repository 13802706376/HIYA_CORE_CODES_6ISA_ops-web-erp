package com.yunnex.ops.erp.modules.sys.entity;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 工号管理Entity
 * 
 * @author SunQ
 * @date 2018年1月24日
 */
public class JobNumberInfo extends DataEntity<JobNumberInfo> {

    private static final long serialVersionUID = 7053725562147806861L;

    /**
     * 工号
     */
    private String jobNumber;
    
    /**
     * 角色ID
     */
    private String roleId;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 人员ID
     */
    private String userId;
    
    /**
     * 人员名称
     */
    private String userName;
    
    /**
     * 电话
     */
    private String telephone;
    
    /**
     * 评分
     */
    private String score;
    
    /**
     * 头像
     */
    private String iconImg;

    private String type; // 类型，erp: ERP用户工号，agent: 服务商用户工号

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
