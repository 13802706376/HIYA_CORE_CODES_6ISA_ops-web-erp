package com.yunnex.ops.erp.modules.act.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.act.dao.ActDefExtDao;
import com.yunnex.ops.erp.modules.act.entity.ActDefExt;

/**
 * 流程节点扩展Service
 * @author 1
 * @version 2017-11-30
 */
@Service
public class ActDefExtService extends CrudService<ActDefExtDao, ActDefExt> {

    @Override
    public ActDefExt get(String id) {
        return super.get(id);
    }

    @Override
    public List<ActDefExt> findList(ActDefExt actDefExt) {
        return super.findList(actDefExt);
    }

    @Override
    public Page<ActDefExt> findPage(Page<ActDefExt> page, ActDefExt actDefExt) {
        return super.findPage(page, actDefExt);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ActDefExt actDefExt) {
        super.save(actDefExt);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ActDefExt actDefExt) {
        super.delete(actDefExt);
    }

    public ActDefExt getByActId(String actId) {
        return dao.getByActId(actId);
    }

    public ActDefExt findByProcDefKeyAndActId(String procDefKey, String actId) {
        return dao.findByProcDefKeyAndActId(procDefKey, actId);
    }

}
