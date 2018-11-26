package com.yunnex.ops.erp.common.persistence.interceptor;

import java.util.Date;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.poi.ss.formula.functions.T;
import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import static com.yunnex.ops.erp.common.constant.CommonConstants.BaseCrudParams.*;

/**
 * 1 拦截mybatis修改方法 自动填充 updateBy，updateDate，delFlag等基础参数  
 * 2 支持mybatis默认的update和自定义修改 
 * 3 支持参数是实体类和map，或者注解参数  
 * 4 支持insert 方法 ,如果已经封装好参数 不影响 ,createBy，createDate，delFlag等
 * 5 可以取代 preUpdate 和 preInsert方法  
 * 6 过滤了 删除逻辑 
 * @author caozhijun
 * @version 2018-6-8
 */
@Intercepts({ @Signature(type = Executor.class, method =SQL_TYPE_UPDATE, args = { MappedStatement.class, Object.class }) })
public class CrudInterceptor extends BaseInterceptor
{
    private static final long serialVersionUID = 1575L;

    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String sql = boundSql.getSql() ;
        //过滤删除逻辑 
        if (!sql.toLowerCase().startsWith(SQL_TYPE_DELATE)) 
        {  
            Object parameterObject = boundSql.getParameterObject();
            //map参数 
            if(parameterObject instanceof   Map)
            {
                @SuppressWarnings("unchecked")
                Map<String,Object> parameterObjectMap = (Map<String,Object>)parameterObject;
                swapMapParams(parameterObjectMap);
            }
            //实体类  
            else if(parameterObject instanceof   DataEntity)
            {
                @SuppressWarnings("unchecked")
                DataEntity<T> de = (DataEntity<T>)parameterObject;
                swapEntityParams(de);
            }
        }  
        return invocation.proceed();
    }
    
  /**
   * 封装map基础参数 
   * @param parameterObjectMap
   */
    private void swapMapParams(Map<String,Object> parameterObjectMap)
    {
        String userId = UserUtils.getUser().getId();
        userId = null==userId?USER_ID_DELATE:userId;
        Date now = new Date();
        if(!parameterObjectMap.containsKey(UPDATE_BY))
        {
            parameterObjectMap.put(UPDATE_BY, userId);
        }
        if(!parameterObjectMap.containsKey(UPDATE_DATE))
        {
            parameterObjectMap.put(UPDATE_DATE, now);
        }
        if(!parameterObjectMap.containsKey(CREATE_BY))
        {
            parameterObjectMap.put(CREATE_BY, userId);
        }
        if(!parameterObjectMap.containsKey(CREATE_DATE))
        {
            parameterObjectMap.put(CREATE_DATE, now);
        }
        if(!parameterObjectMap.containsKey(DEL_FALG))
        {
            parameterObjectMap.put(DEL_FALG, DEL_FALG_DEFAULT);
        }
    }

    /**
     * 封装实体类基础参数 
     * @param DataEntity<T> 
     */
      private void swapEntityParams( DataEntity<T> de)
      {
          User currentUser = UserUtils.getUser();
          currentUser.setId(StringUtils.isEmpty(currentUser.getId())?USER_ID_DELATE:currentUser.getId());
          Date now = new Date();
          if(null == de.getCreateBy())
          {
              de.setCreateBy(currentUser);
          }
          if(null == de.getCreateDate())
          {
              de.setCreateDate(now);
          }
          if(null == de.getUpdateBy())
          {
              de.setUpdateBy(currentUser);
          }
          if(null == de.getUpdateDate())
          {
              de.setUpdateDate(now);
          }
          if(null == de.getDelFlag())
          {
              de.setDelFlag(DEL_FALG_DEFAULT);
          }
      }
      
    @Override
    public Object plugin(Object target)
    {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties)
    {
        super.initProperties(properties);
    }
}
