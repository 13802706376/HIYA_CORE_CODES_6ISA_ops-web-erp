package com.yunnex.ops.erp.modules.file.dao;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.file.entity.ErpFileInfo;

/**
 * 文件信息DAO接口
 * @author yunnex
 * @version 2017-12-16
 */
@MyBatisDao
public interface ErpFileInfoDao extends CrudDao<ErpFileInfo> {
    ErpFileInfo findByPath(@Param("path") String path);
}