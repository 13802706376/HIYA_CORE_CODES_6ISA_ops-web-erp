package com.yunnex.ops.erp.modules.sys.dao;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.sys.entity.JobNumberInfo;

/**
 * 工号管理DAO接口
 * 
 * @author SunQ
 * @date 2018年1月24日
 */
@MyBatisDao
public interface JobNumberInfoDao extends CrudDao<JobNumberInfo> {

    /**
     * 通过人员ID获取工号对象
     *
     * @param userId
     * @return
     * @date 2018年1月30日
     * @author Administrator
     */
    JobNumberInfo getByUserId(@Param("userId") String userId);
    
    /**
     * 获取人员存在的数量
     *
     * @param userId
     * @return
     * @date 2018年1月30日
     * @author SunQ
     */
    int count(@Param("userId") String userId);

    void deleteByUserId(String userId);
}
