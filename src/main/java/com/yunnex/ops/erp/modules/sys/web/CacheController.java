/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.yunnex.ops.erp.modules.sys.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunnex.ops.erp.common.utils.CacheUtils;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.JedisUtils;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import redis.clients.jedis.Jedis;
 import static com.yunnex.ops.erp.common.constant.CommonConstants.RedisCacheParams.*;

/**
 * 缓存监控
 * 1 监控session的动态
 * 2 监控缓存的动态
 * 3 清除缓存 
 * 4 清除session
 * 
 * @author ThinkGem
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/cache")
public class CacheController extends BaseController 
{

    @Autowired
    private UserService userService;
    ObjectMapper mapper = new ObjectMapper();
 
    /**
     * 清除用户缓存 
     * @param loginName
     * @return
     */
    @RequestMapping(value = "clearCacheByUser")
    @ResponseBody
    public String clearCacheByUser(String loginName) 
    {
        User user = userService.getByLoginName( loginName);
        if(null==user)
        {
            return  "User ["+loginName+"] is not exists.";
        }
        UserUtils.clearCache(user);
        return "Clear User ["+loginName+"] Cache Success.";
    }
    
  /**
   * 清除缓存 根据名称和key
   * @param cacheName
   * @param cacheKey
   * @return
   */
    @RequestMapping(value = "clearCacheByKey")
    @ResponseBody
    public String clearCacheByKey(String cacheName,String cacheKey) 
    {
        CacheUtils.remove(cacheName, cacheKey);
        return "Clear  ["+cacheName+":"+cacheKey+"]  Cache Success.";
    }
    
    /**
     * 查看redis的session  
     * @param cacheName
     * @param cacheKey
     * @return
     */
    @RequestMapping(value = "getRedisSessions")
    @ResponseBody
    public String getRedisSessions() 
    {
        Map<String,Map<String,String>>  resultMap = new  HashMap<String,Map<String,String>>();
        Jedis jedis = JedisUtils.getResource();
        Set<String> sessionSet = jedis.hkeys(REDIS_SESSION_PREFIX);
        for(String sid:sessionSet)
        {
            String vals =  jedis.hget(REDIS_SESSION_PREFIX,sid);
            String valArr[] = vals.split("\\|");
            Map<String,String> valMap = new HashMap<String,String> ();
            valMap.put("用户编号", valArr[0]);
            valMap.put("超时时间", (Long.parseLong(valArr[1])/1000)+"秒");
            valMap.put("最后访问时间", DateUtils.formatDateTime(new Date(Long.parseLong(valArr[2]))));
            resultMap.put(sid, valMap);
        }
        return resultMap.toString();
    }
    
    /**
     * 查看redis的session  
     * @param cacheName
     * @param cacheKey
     * @return
     */
    @RequestMapping(value = "clearSessionById")
    @ResponseBody
    public String clearSessionById(String sessionId) 
    {
        Jedis jedis = JedisUtils.getResource();
        jedis.hdel(REDIS_SESSION_PREFIX, sessionId);
        return "Clear  Session By ["+sessionId+"]  Success.";
    }
    
    /**
     * 查看redis的 缓存信息 
     * @param cacheName
     * @param cacheKey
     * @return
     * @throws JsonProcessingException 
     * @throws CacheException 
     */
    @RequestMapping(value = "getRedisByCacheName")
    @ResponseBody
    public String getRedisByCacheName(String cacheName) throws CacheException, JsonProcessingException 
    {
        cacheName = StringUtils.isEmpty(cacheName)?REDIS_CACHE_DEFAULT:cacheName;
        Map<String,String>  resultMap = new  HashMap<String,String>();
        Cache <String,Object>   userCache =  CacheUtils.getCache(cacheName);
        Set<String> set = userCache.keys();
        Jedis jedis = JedisUtils.getResource();
        for(String key:set)
        {
            Object value = (Object)JedisUtils.toObject(jedis.hget(JedisUtils.getBytesKey(REDIS_CACHE_PREFIX+cacheName), JedisUtils.getBytesKey(key)));
            resultMap.put(key, mapper.writeValueAsString(value));
        }
        return resultMap.toString();
    }
}
