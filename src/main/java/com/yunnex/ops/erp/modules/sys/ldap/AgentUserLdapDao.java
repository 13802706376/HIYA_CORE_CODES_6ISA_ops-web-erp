package com.yunnex.ops.erp.modules.sys.ldap;

import java.util.List;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;

import com.yunnex.ops.erp.common.constant.Constant;

@Repository
public class AgentUserLdapDao extends LdapDao {

    /**
     * 新增
     */
    public void create(AgentUser agentUser) {
        agentUser.setDn(buildDn(agentUser));
        agentLdapTemplate.create(agentUser);
        logger.info("创建服务商用户：{}", agentUser);
    }

    /**
     * 查找
     */
    public AgentUser findByDn(AgentUser agentUser) {
        LdapName dn = null;
        AgentUser result = null;
        try {
            dn = buildDn(agentUser);
            result = agentLdapTemplate.findByDn(dn, AgentUser.class);
        } catch (NameNotFoundException e) {
            logger.info("服务商用户不存在！dn: {}", dn, e);
        }
        return result;
    }

    /**
     * 更新
     */
    public void update(AgentUser agentUser) {
        agentLdapTemplate.update(agentUser);
        logger.info("更新服务商用户：{}", agentUser);
    }

    /**
     * 删除
     */
    public void delete(AgentUser agentUser) {
        agentUser.setDn(buildDn(agentUser));
        agentLdapTemplate.delete(agentUser);
        logger.info("删除服务商用户：{}", agentUser);
    }

    /**
     * 构建服务商用户DN: ou=User, ou=服务商, ou=服务商xxx, cn=用户xxx
     */
    private static LdapName buildDn(AgentUser agentUser) {
        return LdapNameBuilder.newInstance().add(LdapConstant.DN_OU, LdapConstant.USER).add(LdapConstant.DN_OU, LdapConstant.AGENT)
                        .add(LdapConstant.DN_OU, agentUser.getAgentName()).add(LdapConstant.ATTR_CN, agentUser.getFullName()).build();
    }


    /**
     * 构建服务商DN： "ou=" + ou + ",ou=服务商,ou=User"
     */
    public String buildDn(String ou) {
        if (StringUtils.isBlank(ou)) {
            throw new IllegalArgumentException("ou不能为空！");
        }
        StringBuilder builder = new StringBuilder();
        builder.append(LdapConstant.DN_OU).append(Constant.EQUAL).append(ou).append(Constant.COMMA).append(LdapConstant.DN_OU).append(Constant.EQUAL)
                        .append(LdapConstant.AGENT).append(Constant.COMMA).append(LdapConstant.DN_OU).append(Constant.EQUAL)
                        .append(LdapConstant.USER);
        return builder.toString();
    }

    /**
     * 查看指定的服务商组织单元OU是否存在
     *
     * @param ou
     * @return
     */
    public Object lookUpDn(String ou) {
        Object o = null;
        String dn = buildDn(ou);
        try {
            o = agentLdapTemplate.lookup(dn);
        } catch (RuntimeException e) {
            logger.info("找不到组织单元! {}", dn, e);
        }
        return o;
    }

    /**
     * 创建服务商组织单元
     *
     * @param ou
     */
    public void createOu(String ou) {
        Object o = lookUpDn(ou);
        if (o == null) {
            Attributes attributes = new BasicAttributes(Boolean.TRUE);
            Attribute objectClass = new BasicAttribute(LdapConstant.ATTR_OBJECT_CLASS);
            objectClass.add(LdapConstant.OBJECT_CLASS_OU);
            objectClass.add(LdapConstant.OBJECT_CLASS_TOP);
            attributes.put(objectClass);
            String dn = buildDn(ou);
            agentLdapTemplate.bind(dn, (Object) null, attributes);
            logger.info("创建组织单元ou：{}", dn);
        }
    }

    /**
     * 查找服务商下的所有用户
     * 
     * @param ou
     * @return
     */
    public List<AgentUser> searchAgentUsers(String ou) {

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        List<AgentUser> agentUsers = agentLdapTemplate.find(buildAgentOu(ou), null, searchControls, AgentUser.class);

        return agentUsers;
    }


    private static LdapName buildAgentOu(String ou) {
        return LdapNameBuilder.newInstance().add(LdapConstant.DN_OU, LdapConstant.USER).add(LdapConstant.DN_OU, LdapConstant.AGENT)
                        .add(LdapConstant.DN_OU, ou).build();
    }

    /**
     * 递归删除OU
     * 
     * @param ou
     */
    public void delete(String ou) {
        agentLdapTemplate.unbind(buildAgentOu(ou), true);
    }

}
