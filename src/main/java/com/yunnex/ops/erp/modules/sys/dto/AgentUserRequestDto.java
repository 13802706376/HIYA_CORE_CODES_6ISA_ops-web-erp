package com.yunnex.ops.erp.modules.sys.dto;

import java.io.Serializable;

import com.yunnex.ops.erp.common.persistence.Pager;

/**
 * 服务商用户列表请求参数
 */
public class AgentUserRequestDto extends Pager<AgentUserRequestDto> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer agentId; // 服务商编号
    private String q; // 查找内容

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
