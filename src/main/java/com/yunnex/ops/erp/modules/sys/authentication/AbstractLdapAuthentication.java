package com.yunnex.ops.erp.modules.sys.authentication;

import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.ldap.LdapDao;
import com.yunnex.ops.erp.modules.sys.security.UsernamePasswordToken;
import com.yunnex.ops.erp.modules.sys.service.AgentUserService;
/**
 * 模板方法模式
 * @author zjq
 *
 */
public abstract class AbstractLdapAuthentication implements IAuthentication
{
    @Override
    public boolean doAuthentication(UsernamePasswordToken token, User user, LdapDao ldapDao)
    {
        boolean flag = false;
        // ERP LDAP验证
        if (SysConstant.TYPE_ERP.equals(user.getType()))
        {
            flag = ldapDao.erpUserAuth(token.getUsername(), token.getLdappass());
        } 
        // 服务商用户LDAP验证
        else if (SysConstant.TYPE_AGENT.equals(user.getType()))
        {
            String pwd = AgentUserService.encryptPassword(token.getLdappass(), user.getSalt());
             flag = ldapDao.agentUserAuth(token.getUsername(), pwd);
        }
        return flag;
    }
}
