package com.yunnex.ops.erp.modules.shop.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.shop.dao.ErpShopActualLinkmanDao;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopActualLinkman;

/**
 * 商户实际联系人信息Service
 * @author yunnex
 * @version 2017-12-09
 */
@Service
public class ErpShopActualLinkmanService extends CrudService<ErpShopActualLinkmanDao, ErpShopActualLinkman> {
	@Autowired
	private ErpShopActualLinkmanDao linkDao;

    @Override
	@Transactional(readOnly = false)
	public void save(ErpShopActualLinkman erpShopActualLinkman) {
		super.save(erpShopActualLinkman);
	}

    @Override
	@Transactional(readOnly = false)
    public void delete(ErpShopActualLinkman erpShopActualLinkman) {
		super.delete(erpShopActualLinkman);
	}

	public List<ErpShopActualLinkman> findShopLinmanByShopId(String del,String id){
		return linkDao.findShopLinmanByShopId(del, id);
	}
	
    @Transactional(readOnly = false)
    public BaseResult deleteEntity(ErpShopActualLinkman erpShopActualLinkman) {
        if (StringUtils.isBlank(erpShopActualLinkman.getId())) {
            return new IllegalArgumentErrorResult();
        }

        int value = linkDao.delete(erpShopActualLinkman);
        return value > 0 ? new BaseResult() : new BaseResult().error("-1", "删除失败");
    }

    @Transactional(readOnly = false)
    public BaseResult saveOrUpdateEntity(ErpShopActualLinkman erpShopActualLinkman) {
        // 新增
        if (StringUtils.isBlank(erpShopActualLinkman.getId())) {
            erpShopActualLinkman.preInsert();
            int insert = linkDao.insert(erpShopActualLinkman);
            return insert > 0 ? new BaseResult() : new BaseResult().error("-1", "新增失败");
        }
        // 更新
        else {
            erpShopActualLinkman.preUpdate();
            int update = linkDao.update(erpShopActualLinkman);
            return update > 0 ? new BaseResult() : new BaseResult().error("-1", "更新失败");
        }
    }
}