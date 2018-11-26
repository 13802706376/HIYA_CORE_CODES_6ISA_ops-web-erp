package com.yunnex.ops.erp.modules.agent.dto;

import java.io.Serializable;

/**
 * 服务商列表Dto
 */
public class AgentInfoResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; // 服务商ID
    private Integer agentId; // 服务商编号
    private String name; // 服务商名称
    private String contactName; // 业务联系人
    private String contactPhone; // 联系人电话
    private String loginAccount; // 登录账号
    // 状态，enable: 可用，disable: 停用
    private String state;
    private Integer memberCount; // 员工数量

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }
}
