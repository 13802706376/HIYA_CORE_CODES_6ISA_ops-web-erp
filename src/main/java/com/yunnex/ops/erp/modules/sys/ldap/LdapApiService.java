package com.yunnex.ops.erp.modules.sys.ldap;

import java.util.Hashtable;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LdapApiService {
    @Value("${ldap_url}")
    private String LDAP_URL; // NOSONAR

    @Value("${ldap_admin_name}")
    private String LDAP_ADMIN_NAME; // NOSONAR

    @Value("${ldap_admin_password}")
    private String LDAP_ADMIN_PASSWORD; // NOSONAR

    private static LdapContext ctx = null;


    /**
     * 初始化ldap
     */
    @PostConstruct
    public void initLdap() {
        Hashtable hashEnv = new Hashtable();
        hashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别
        hashEnv.put(Context.SECURITY_PRINCIPAL, LDAP_ADMIN_NAME); // AD User
        hashEnv.put(Context.SECURITY_CREDENTIALS, LDAP_ADMIN_PASSWORD); // AD Password
        hashEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
        hashEnv.put(Context.PROVIDER_URL, LDAP_URL);
        try {
            ctx = new InitialLdapContext(hashEnv, null);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public LdapContext getLdapContext() {
        // 该对象是 服务器启动的时候初始化，长时间可能断开连接，每次获取ldap上下文的时候初始化
        initLdap();
        return ctx;
    }
}
