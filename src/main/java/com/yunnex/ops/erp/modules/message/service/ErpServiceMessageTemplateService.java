package com.yunnex.ops.erp.modules.message.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.message.dao.ErpServiceMessageTemplateDao;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceMessageTemplate;

/**
 * 服务通知模板表Service
 * 
 * @author yunnex
 * @version 2018-07-04
 */
@Service
public class ErpServiceMessageTemplateService extends CrudService<ErpServiceMessageTemplateDao, ErpServiceMessageTemplate> {

    @Override
	public ErpServiceMessageTemplate get(String id) {
		return super.get(id);
	}

    @Override
	public List<ErpServiceMessageTemplate> findList(ErpServiceMessageTemplate erpServiceMessageTemplate) {
		return super.findList(erpServiceMessageTemplate);
	}

    @Override
	public Page<ErpServiceMessageTemplate> findPage(Page<ErpServiceMessageTemplate> page, ErpServiceMessageTemplate erpServiceMessageTemplate) {
		return super.findPage(page, erpServiceMessageTemplate);
	}

    @Override
	@Transactional(readOnly = false)
	public void save(ErpServiceMessageTemplate erpServiceMessageTemplate) {
		super.save(erpServiceMessageTemplate);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(ErpServiceMessageTemplate erpServiceMessageTemplate) {
		super.delete(erpServiceMessageTemplate);
	}

    /**
     * 根据条件获取唯一数据
     *
     * @param serviceType
     * @param nodeType
     * @param status
     * @return
     * @date 2018年7月19日
     * @author linqunzhi
     */
    public ErpServiceMessageTemplate getOnly(String serviceType, String nodeType, String status) {
        logger.info("getOnly start | serviceType={}|nodeType={}|status={}", serviceType, nodeType, status);
        ErpServiceMessageTemplate result = dao.getOnly(serviceType, nodeType, status);
        logger.info("getOnly end");
        return result;
    }

    /**
     * 业务定义：根据ID集合批量查询模板数据
     * 
     * @date 2018年7月24日
     * @author R/Q
     */
    public List<ErpServiceMessageTemplate> queryTemplateByIds(List<String> ids) {
        ErpServiceMessageTemplate paramObj = new ErpServiceMessageTemplate();
        paramObj.setIds(Optional.ofNullable(ids).orElse(new ArrayList<String>()));
        return super.findList(paramObj);
    }
	
}