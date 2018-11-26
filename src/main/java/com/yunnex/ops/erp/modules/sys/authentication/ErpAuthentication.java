package com.yunnex.ops.erp.modules.sys.authentication;

import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.ldap.LdapDao;
import com.yunnex.ops.erp.modules.sys.security.UsernamePasswordToken;
import com.yunnex.ops.erp.modules.sys.service.SystemService;

/**
 * 针对ERP认证，比如管理员
 * @author czj
 *
 */
public class ErpAuthentication implements IAuthentication
{
    @Override
    public boolean doAuthentication(UsernamePasswordToken token, User user, LdapDao ldapDao)
    {
        return SystemService.validatePassword(token.getLdappass(), user.getPassword());
    }
}
