package com.yunnex.ops.erp.modules.sys.dao;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.sys.entity.SysConstants;

/**
 * 系统常量DAO接口
 * 
 * @author linqunzhi
 * @version 2018-04-16
 */
@MyBatisDao
public interface SysConstantsDao extends CrudDao<SysConstants> {

    /**
     * 根据key获取常量信息
     *
     * @param key
     * @return
     * @date 2018年4月16日
     * @author linqunzhi
     */
    SysConstants getByKey(@Param("key") String key);

}
