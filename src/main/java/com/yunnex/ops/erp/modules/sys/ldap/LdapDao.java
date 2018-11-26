package com.yunnex.ops.erp.modules.sys.ldap;

import java.util.List;

import javax.naming.directory.Attributes;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.yunnex.ops.erp.common.constant.Constant;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository
public class LdapDao {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("agentLdapTemplate")
    protected LdapTemplate agentLdapTemplate;
    @Autowired
    @Qualifier("erpLdapTemplate")
    protected LdapTemplate erpLdapTemplate;


    /**
     * 检查erp账号是否已存在
     * 
     * @param cn
     * @return
     */
    public Boolean erpCnExists(String cn) {
        return isCnExists(erpLdapTemplate, cn);
    }

    /**
     * 检查服务商账号是否已存在
     * 
     * @param cn
     * @return
     */
    public Boolean agentCnExists(String cn) {
        return isCnExists(agentLdapTemplate, cn);
    }

    /**
     * 登录账号是否存在
     * 
     * @param ldapTemplate
     * @param cn
     * @return true: 存在，false: 不存在
     */
    public Boolean isCnExists(LdapTemplate ldapTemplate, String cn) {
        if (StringUtils.isBlank(cn)) {
            return true; // 为空则当作已存在
        }
        List<String> result = ldapTemplate.search(
                        query().attributes(LdapConstant.ATTR_CN).where(LdapConstant.ATTR_OBJECT_CLASS).is(LdapConstant.OBJECT_CLASS_PERSON)
                                        .and(LdapConstant.ATTR_CN).is(cn),
                        (Attributes attributes) -> attributes.get(LdapConstant.ATTR_CN).get().toString());
        return !CollectionUtils.isEmpty(result);
    }

    /**
     * ERP用户认证。只查运营中心下的用户。
     * 
     * @param username
     * @param password
     * @return
     */
    public boolean erpUserAuth(String username, String password) {
        return auth(erpLdapTemplate, LdapConstant.BASE_ERP_OPS, username, password);
    }

    /**
     * 服务商用户认证
     *
     * @param username
     * @param password
     * @return
     */
    public boolean agentUserAuth(String username, String password) {
        return auth(agentLdapTemplate, Constant.BLANK, username, password);
    }

    protected boolean auth(LdapTemplate ldapTemplate, String base, String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return false;
        }
        return ldapTemplate.authenticate(base, LdapConstant.ATTR_CN + Constant.EQUAL + username, password);
    }

}
