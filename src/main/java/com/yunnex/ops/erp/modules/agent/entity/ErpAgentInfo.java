package com.yunnex.ops.erp.modules.agent.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 服务商信息Entity
 * @author yunnex
 * @version 2018-05-28
 */
public class ErpAgentInfo extends DataEntity<ErpAgentInfo> {

    private static final long serialVersionUID = 1L;

    private Integer agentId; // 服务商编号
    private String name; // 服务商名称
    private String contactName; // 业务联系人
    private String contactPhone; // 联系人电话
    private String loginAccount; // 登录账号
    private String password; // 登录密码
    private String salt; // 盐
    // 状态，enable: 可用，disable: 停用
    private String state;
    private Integer sort; // 排序，可用在前：10，停用在后：0

    private String oldName; // 旧服务商名称

    public ErpAgentInfo() {
        super();
    }

    public ErpAgentInfo(String id) {
        super(id);
    }

    @NotNull(message = "服务商编号不能为空")
    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    @Length(min = 1, max = 50, message = "服务商名称长度必须介于 1 和 50 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 1, max = 64, message = "业务联系人长度必须介于 1 和 64 之间")
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Length(min = 1, max = 64, message = "联系人电话长度必须介于 1 和 64 之间")
    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @Length(min = 1, max = 64, message = "登录账号长度必须介于 1 和 64 之间")
    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    @Length(min = 1, max = 64, message = "登录密码长度必须介于 1 和 64 之间")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    @Override
    public String toString() {
        return "ErpAgentInfo [agentId=" + agentId + ", name=" + name + ", contactName=" + contactName + ", contactPhone=" + contactPhone + ", loginAccount=" + loginAccount + ", password=" + password + ", salt=" + salt + ", state=" + state + ", sort=" + sort + "]";
    }
}
