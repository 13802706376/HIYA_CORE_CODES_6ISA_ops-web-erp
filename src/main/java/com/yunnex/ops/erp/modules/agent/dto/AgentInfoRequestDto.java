package com.yunnex.ops.erp.modules.agent.dto;

import java.io.Serializable;

import com.yunnex.ops.erp.common.persistence.Pager;

/**
 * 服务商列表请求参数Dto
 */
public class AgentInfoRequestDto extends Pager<AgentInfoRequestDto> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer agentId; // 服务商编号
    private String name; // 服务商名称
    private String contactPhone; // 联系人电话
    private String loginAccount; // 登录账号

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
}
