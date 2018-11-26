package com.yunnex.junit.ldap;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.modules.agent.entity.ErpAgentInfo;
import com.yunnex.ops.erp.modules.sys.ldap.AgentUserLdapService;

public class AgentUserLdapServiceTest extends BaseTest {

    @Autowired
    private AgentUserLdapService agentUserLdapService;

    @Test
    public void createOrMoveAgent() {
        ErpAgentInfo agentInfo = new ErpAgentInfo();
        agentInfo.setAgentId(-200);
        agentInfo.setName("服务商重命名new");
        agentInfo.setOldName("服务商重命名hello");
        agentUserLdapService.createOrMoveAgent(agentInfo);
    }

    @Test
    public void t1() {
        agentUserLdapService.saveLdapAgentsAndUsers();
    }

}
