package com.yunnex.ops.erp.modules.sys.authentication;
import static com.yunnex.ops.erp.modules.sys.constant.SysConstant.SystemEnvironment.PROFLIE_DEVELOPMENT;
import static com.yunnex.ops.erp.modules.sys.constant.SysConstant.SystemEnvironment.PROFLIE_PRODUCTION;
import static com.yunnex.ops.erp.modules.sys.constant.SysConstant.SystemEnvironment.PROFLIE_TEST;
import static com.yunnex.ops.erp.modules.sys.constant.SysConstant.SystemEnvironment.USER_ADMINISTRATOR;

/**
 *  工厂方法模式 
 */
public class AuthenticationFactory 
{
    /**
     * 初始化目标 实现类
     * @param activeProflie
     * @param userName
     */
    public static IAuthentication getAuthentication(String activeProflie,String userName)
    {
        IAuthentication lvs = null;
         switch (userName)
         {
            case USER_ADMINISTRATOR:
                lvs = new ErpAuthentication();
                break;
            default:
                    switch (activeProflie)
                    {
                        case PROFLIE_DEVELOPMENT:
                            lvs = new LdapDevAuthentication();
                            break;
            
                        case PROFLIE_TEST:
                        lvs = new LdapTestAuthentication();
                            break;
            
                        case PROFLIE_PRODUCTION:
                            lvs = new LdapProAuthentication();
                            break;
                            
                        default:
                            lvs = new LdapProAuthentication();
                            break;
                    }
                    break;
           }
         return lvs;
    }
}
