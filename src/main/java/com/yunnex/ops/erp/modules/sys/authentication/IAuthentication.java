package com.yunnex.ops.erp.modules.sys.authentication;

import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.ldap.LdapDao;
import com.yunnex.ops.erp.modules.sys.security.UsernamePasswordToken;

public interface IAuthentication
{
    boolean doAuthentication(UsernamePasswordToken token,User user,LdapDao ldapDao);
}
