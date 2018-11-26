package com.yunnex.ops.erp.modules.message.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.message.dao.ErpServiceProgressTemplateDao;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceProgressTemplate;

/**
 * 服务进度模板表Service
 * 
 * @author yunnex
 * @version 2018-07-04
 */
@Service
@Transactional(readOnly = true)
public class ErpServiceProgressTemplateService extends CrudService<ErpServiceProgressTemplateDao, ErpServiceProgressTemplate> {

    @Override
	public ErpServiceProgressTemplate get(String id) {
		return super.get(id);
	}

    @Override
	public List<ErpServiceProgressTemplate> findList(ErpServiceProgressTemplate erpServiceScheduleTemplate) {
		return super.findList(erpServiceScheduleTemplate);
	}

    @Override
	public Page<ErpServiceProgressTemplate> findPage(Page<ErpServiceProgressTemplate> page, ErpServiceProgressTemplate erpServiceScheduleTemplate) {
		return super.findPage(page, erpServiceScheduleTemplate);
	}

    @Override
	@Transactional(readOnly = false)
	public void save(ErpServiceProgressTemplate erpServiceScheduleTemplate) {
		super.save(erpServiceScheduleTemplate);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(ErpServiceProgressTemplate erpServiceScheduleTemplate) {
		super.delete(erpServiceScheduleTemplate);
	}

    /**
     * 获取唯一数据
     *
     * @param serviceType
     * @param type
     * @param status
     * @param processVersion
     * @return
     * @date 2018年7月19日
     * @author linqunzhi
     */
    public ErpServiceProgressTemplate getOnly(String serviceType, String type, String status, int processVersion) {
        logger.info("getOne start | serviceType={}|type={}|status={}|processVersion={}");
        ErpServiceProgressTemplate result = dao.getOnly(serviceType, type, status, processVersion);
        logger.info("getOne end");
        return result;
    }

    /**
     * 业务定义：根据ID集合批量查询模板数据
     * 
     * @date 2018年7月24日
     * @author R/Q
     */
    public List<ErpServiceProgressTemplate> queryTemplateByIds(List<String> ids) {
        ErpServiceProgressTemplate paramObj = new ErpServiceProgressTemplate();
        paramObj.setIds(Optional.ofNullable(ids).orElse(new ArrayList<String>()));
        return super.findList(paramObj);
    }
	
}