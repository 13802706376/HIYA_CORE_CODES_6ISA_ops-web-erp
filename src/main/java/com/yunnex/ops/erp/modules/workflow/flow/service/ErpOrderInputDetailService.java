package com.yunnex.ops.erp.modules.workflow.flow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.workflow.flow.dao.ErpOrderInputDetailDao;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderInputDetail;

/**
 * 订单输入项任务
 * @author yunnex
 * @date 2017年11月2日
 */
@Service
public class ErpOrderInputDetailService extends CrudService<ErpOrderInputDetailDao, ErpOrderInputDetail> {

    @Autowired
    private ErpOrderInputDetailDao dao;
    
    public ErpOrderInputDetail get(String id) {
        return super.get(id);
    }
    
    public List<ErpOrderInputDetail> findList(ErpOrderInputDetail erpOrderInputDetail) {
        return super.findList(erpOrderInputDetail);
    }
    
    public Page<ErpOrderInputDetail> findPage(Page<ErpOrderInputDetail> page, ErpOrderInputDetail erpOrderInputDetail) {
        return super.findPage(page, erpOrderInputDetail);
    }
    
    @Transactional(readOnly = false)
    public void save(ErpOrderInputDetail erpOrderInputDetail) {
        super.save(erpOrderInputDetail);
    }
    
    @Transactional(readOnly = false)
    public void delete(ErpOrderInputDetail erpOrderInputDetail) {
        super.delete(erpOrderInputDetail);
    }
    
    public List<ErpOrderInputDetail> findListBySplitId(String splitId) {
        return  dao.findListBySplitId(splitId);
    }
    public ErpOrderInputDetail  getBySplitId(String splitId,String inputTaskName) {
        return  dao.getBySplitId(splitId, inputTaskName);
    }

    public ErpOrderInputDetail findListByInputKey(String splitId, String inputTaskKey) {
        return dao.findListByInputKey(splitId, inputTaskKey);
    }

}