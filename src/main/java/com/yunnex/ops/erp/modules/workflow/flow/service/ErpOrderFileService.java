package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.workflow.flow.dao.ErpOrderFileDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;

/**
 * 订单文件服务
 * 
 * @author Frank
 * @version 2017-11-02
 */
@Service
public class ErpOrderFileService extends CrudService<ErpOrderFileDao, ErpOrderFile> {

    @Autowired
    private ErpOrderFileDao erpOrderFileDao;

    public ErpOrderFile get(String id) {
        return super.get(id);
    }

    public List<ErpOrderFile> findList(ErpOrderFile erpOrderFile) {
        return super.findList(erpOrderFile);
    }

    public Page<ErpOrderFile> findPage(Page<ErpOrderFile> page, ErpOrderFile erpOrderFile) {
        return super.findPage(page, erpOrderFile);
    }

    @Transactional(readOnly = false)
    public void save(ErpOrderFile erpOrderFile) {
        super.save(erpOrderFile);
    }

    @Transactional(readOnly = false)
    public void delete(ErpOrderFile erpOrderFile) {
        super.delete(erpOrderFile);
    }

    public List<ErpOrderFile> findListByProcInsId(String procInsId) {
        return erpOrderFileDao.findListByProcInsId(procInsId);
    } 
    @Transactional(readOnly = false)
    public void deleteByProcInsId(String fileTitle, String procInsId) {
        erpOrderFileDao.deleteByProcInsId(fileTitle, procInsId);
    }

    public List<ErpOrderFile> findListSubTask(ErpOrderFile erpOrderFile) {
        return erpOrderFileDao.findListSubTask(erpOrderFile);
    }
    
    public List<ErpOrderFile> findByProcInsIdAndTileName(String fileTitle, String procInsId) {
        return erpOrderFileDao.findByProcInsIdAndTileName(fileTitle, procInsId);
    }
    
    public List<ErpOrderFile> findByProcInsIdAndTileNameAll(String fileTitle, String procInsId) {
        return erpOrderFileDao.findByProcInsIdAndTileNameAll(fileTitle, procInsId);
    }

    public List<ErpOrderFile> findListByIds(List<String> removedErpOrderFileIds) {
        return erpOrderFileDao.findListByIds(removedErpOrderFileIds);
    }

    public int batchDelete(List<String> removedErpOrderFileIds) {
        return erpOrderFileDao.batchDelete(removedErpOrderFileIds);
    }
}
