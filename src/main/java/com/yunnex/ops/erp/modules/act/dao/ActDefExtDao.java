package com.yunnex.ops.erp.modules.act.dao;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.act.entity.ActDefExt;

/**
 * 流程节点扩展DAO接口
 * @author 1
 * @version 2017-11-30
 */
@MyBatisDao
public interface ActDefExtDao extends CrudDao<ActDefExt> {

    ActDefExt getByActId(String actId);
    
    ActDefExt findByProcDefKeyAndActId(@Param("procDefKey") String procDefKey, @Param("actId") String actId);
}