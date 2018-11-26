package com.yunnex.junit.ldap;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.common.security.Digests;
import com.yunnex.ops.erp.modules.sys.ldap.AgentUser;
import com.yunnex.ops.erp.modules.sys.ldap.AgentUserLdapDao;

public class AgentUserLdapDaoTest extends BaseTest {

    @Autowired
    private AgentUserLdapDao agentUserLdapDao;

    @Test
    public void findByDn() {
        AgentUser agentUser = new AgentUser();
        agentUser.setAgentName("云多科技");
        agentUser.setFullName("abc");
        AgentUser result = agentUserLdapDao.findByDn(agentUser);
        System.out.println(result);
    }

    @Test
    public void create() {
        AgentUser agentUser = new AgentUser();
        agentUser.setAgentName("掌贝科技");
        agentUser.setFullName("13655556666");
        agentUser.setLastName("13655556666");
        agentUser.setPassword(Hex.encodeHexString(Digests.sha1("123456".getBytes())));
        agentUserLdapDao.create(agentUser);
    }

    @Test
    public void update() {
        AgentUser agentUser = new AgentUser();
        agentUser.setAgentName("发福");
        agentUser.setFullName("abccc");
        AgentUser result = agentUserLdapDao.findByDn(agentUser);
        System.out.println(result);
        // result.setDescription("ERP创建");
        // result.setPassword(Hex.encodeHexString(Digests.sha1("123456".getBytes())));
        AgentUser user = new AgentUser();
        user.setDn(result.getDn());
        user.setAgentName("发福");
        user.setFullName("abccc");
        user.setLastName("abccc");
        agentUserLdapDao.update(user);
    }

    @Test
    public void delete() {
        AgentUser agentUser = new AgentUser();
        agentUser.setAgentName("云多科技");
        agentUser.setFullName("abc");
        agentUserLdapDao.delete(agentUser);
    }

    @Test
    public void createOu() {
        agentUserLdapDao.createOu("掌贝科技");
    }

    @Test
    public void auth() {
        boolean auth = agentUserLdapDao.agentUserAuth("13655556666", Hex.encodeHexString(Digests.sha1("123456".getBytes())));
        System.out.println(auth);
    }

}
