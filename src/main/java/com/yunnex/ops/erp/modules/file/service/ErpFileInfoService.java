package com.yunnex.ops.erp.modules.file.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.file.dao.ErpFileInfoDao;
import com.yunnex.ops.erp.modules.file.entity.ErpFileInfo;

/**
 * 文件信息Service
 * @author yunnex
 * @version 2017-12-16
 */
@Service
public class ErpFileInfoService extends CrudService<ErpFileInfoDao, ErpFileInfo> {
    @Autowired
    private ErpFileInfoDao erpFileInfo;

    @Override
    @Transactional(readOnly = false)
    public void save(ErpFileInfo erpFileInfo) {
        super.save(erpFileInfo);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpFileInfo erpFileInfo) {
        super.delete(erpFileInfo);
    }

    public ErpFileInfo findByPath(String path) {
        return erpFileInfo.findByPath(path);
    }

}