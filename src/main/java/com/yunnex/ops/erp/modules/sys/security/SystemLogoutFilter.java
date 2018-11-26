package com.yunnex.ops.erp.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.yunnex.ops.erp.common.utils.CookieUtils;
import com.yunnex.ops.erp.common.utils.JedisUtils;
import com.yunnex.ops.erp.common.web.Servlets;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import redis.clients.jedis.Jedis;
import static com.yunnex.ops.erp.common.constant.CommonConstants.RedisCacheParams.*;

/**
 * 退出登录 
 */
@Component
public class SystemLogoutFilter extends LogoutFilter 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemLogoutFilter.class);

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception
    {
        User loginUser = UserUtils.getUser();
        LOGGER.info("退出登录！loginName={}", loginUser.getLoginName());
        
        // 退出前清空当前登录用户的缓存
        UserUtils.clearCache(loginUser);
        
        //清除session 
       String sessionId =  CookieUtils.getCookie(Servlets.getRequest(), REDIS_SESSION_COOKIE_NAME);
       Jedis jedis = JedisUtils.getResource();
       jedis.hdel(REDIS_SESSION_PREFIX, sessionId);
       
        return super.preHandle(request, response);
    }
}
