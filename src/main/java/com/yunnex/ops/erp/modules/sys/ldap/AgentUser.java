package com.yunnex.ops.erp.modules.sys.ldap;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;

/**
 * LDAP服务商用户
 */
@Entry(objectClasses = {"top", "person", "organizationalPerson", "inetOrgPerson"})
public final class AgentUser {

    @Id
    private Name dn; // DN
    @Attribute(name = "cn")
    private String fullName; // 姓名
    @Attribute(name = "sn")
    private String lastName; // 姓
    @Attribute(name = "displayName")
    private String displayName; // 显示名
    @Attribute(name = "userPassword")
    private String password; // 密码
    @Attribute(name = "description")
    private String description; // 描述
    @Attribute(name = "mobile")
    private String mobile; // 手机号
    @Attribute(name = "mail")
    private String mail; // 邮箱
    @Transient
    private String agentName; // 服务商名称

    public Name getDn() {
        return dn;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Override
    public String toString() {
        return "AgentUser [dn=" + dn + ", fullName=" + fullName + ", lastName=" + lastName + ", displayName=" + displayName + ", password=" + password + ", description=" + description + ", mobile=" + mobile + ", mail=" + mail + ", agentName=" + agentName + "]";
    }
}
