package com.yunnex.ops.erp.modules.workflow.acceptance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.workflow.acceptance.dao.ErpServiceAcceptanceDao;
import com.yunnex.ops.erp.modules.workflow.acceptance.entity.ErpServiceAcceptance;

/**
 * 服务验收评价Service
 * @author yunnex
 * @version 2018-07-04
 */
@Service
public class ErpServiceAcceptanceService extends CrudService<ErpServiceAcceptanceDao, ErpServiceAcceptance> {
	@Autowired 
    private ErpServiceAcceptanceDao erpServiceAcceptanceDao;
    @Override
	public ErpServiceAcceptance get(String id) {
		return super.get(id);
	}

    @Override
	public List<ErpServiceAcceptance> findList(ErpServiceAcceptance erpServiceAcceptance) {
		return super.findList(erpServiceAcceptance);
	}

    @Override
	public Page<ErpServiceAcceptance> findPage(Page<ErpServiceAcceptance> page, ErpServiceAcceptance erpServiceAcceptance) {
		return super.findPage(page, erpServiceAcceptance);
	}

    @Override
	@Transactional(readOnly = false)
	public void save(ErpServiceAcceptance erpServiceAcceptance) {
		super.save(erpServiceAcceptance);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(ErpServiceAcceptance erpServiceAcceptance) {
		super.delete(erpServiceAcceptance);
	}

    /**
     * 根据上门信息id 获取 验收及评分 信息
     *
     * @param visitInfoId
     * @return
     * @date 2018年7月23日
     * @author linqunzhi
     */
    public ErpServiceAcceptance getByVisitId(String visitInfoId) {
        ErpServiceAcceptance acceptance = dao.getByVisitId(visitInfoId);
        return acceptance;
    }

    @Transactional(readOnly = false)
    public ErpServiceAcceptance getAcceptanceInfoByVisitId(String visitInfoId) {
        return erpServiceAcceptanceDao.getAcceptanceInfoByVisitId(visitInfoId);
    }

    @Transactional
    public void deleteByProcInsId(String procInsId) {
        logger.info("deleteByProcInsId start | procInsId={}", procInsId);
        dao.deleteByProcInsId(procInsId);
        logger.info("deleteByProcInsId end");
    }
}