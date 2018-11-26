package com.yunnex.ops.erp.modules.sys.authentication;

import com.yunnex.ops.erp.common.utils.SpringContextHolder;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.ldap.LdapDao;
import com.yunnex.ops.erp.modules.sys.security.UsernamePasswordToken;
import static com.yunnex.ops.erp.modules.sys.constant.SysConstant.SystemEnvironment.PROFLIE_DEFAULT;

/**
 * 静态代理模式
 */
public class AuthenticationProxy implements IAuthentication
{
    private String activeProflie = PROFLIE_DEFAULT;

    public AuthenticationProxy()
    {
        String activeProfiles[] = SpringContextHolder.getApplicationContext().getEnvironment().getActiveProfiles();
        activeProflie = activeProfiles.length > 0 ? activeProfiles[0] : PROFLIE_DEFAULT;
    }

    /**
     * 统一认证机制 
     */
    @Override
    public boolean doAuthentication(UsernamePasswordToken token, User user, LdapDao ldapDao)
    {
        return AuthenticationFactory.getAuthentication(activeProflie, user.getLoginName()).doAuthentication(token, user, ldapDao);
    }

    /**
     * 单例模式
     * 枚举序列化后可以保持单例；默认是线程安全 。
     * @return
     */
    public static AuthenticationProxy getSigleInstance()
    {
        return SigleInstance.INSTANCE.instance;
    }

    public enum SigleInstance
    {
        INSTANCE;
        private AuthenticationProxy instance;

        SigleInstance()
        {
            instance = new AuthenticationProxy();
        }

        public AuthenticationProxy getInstance()
        {
            return instance;
        }
    }
}
