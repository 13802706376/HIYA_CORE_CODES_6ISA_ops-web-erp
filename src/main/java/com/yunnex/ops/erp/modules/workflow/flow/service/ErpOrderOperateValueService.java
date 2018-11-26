package com.yunnex.ops.erp.modules.workflow.flow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.workflow.flow.dao.ErpOrderOperateValueDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderOperateValue;

/**
 * 聚引客流程订单操作内容Service
 * 
 * @author SunQ
 * @date 2018年2月7日
 */
@Service
public class ErpOrderOperateValueService extends CrudService<ErpOrderOperateValueDao, ErpOrderOperateValue> {

    /**
     * 订单操作内容Dao
     */
    @Autowired
    private ErpOrderOperateValueDao erpOrderOperateValueDao;

    @Transactional(readOnly = false)
    @Override
    public void save(ErpOrderOperateValue entity) {
        super.save(entity);
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(ErpOrderOperateValue entity) {
        super.delete(entity);
    }
    
    public ErpOrderOperateValue getOnlyOne(String procInsId, String keyName, String subTaskId) {
        return erpOrderOperateValueDao.getOnlyOne(procInsId, keyName, subTaskId);
    }
}