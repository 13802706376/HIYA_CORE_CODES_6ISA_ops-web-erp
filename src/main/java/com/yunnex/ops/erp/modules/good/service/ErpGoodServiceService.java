package com.yunnex.ops.erp.modules.good.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.good.dao.ErpGoodServiceDao;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodService;

/**
 * 商品服务Service
 * @author yunnex
 * @version 2018-05-29
 */
@Service
public class ErpGoodServiceService extends CrudService<ErpGoodServiceDao, ErpGoodService> {
    @Autowired
    private ErpGoodServiceDao erpGoodServiceDao;

    public ErpGoodService get(String id) {
		return super.get(id);
	}
	
	public List<ErpGoodService> findList(ErpGoodService erpGoodService) {
		return super.findList(erpGoodService);
	}
	
	public Page<ErpGoodService> findPage(Page<ErpGoodService> page, ErpGoodService erpGoodService) {
		return super.findPage(page, erpGoodService);
	}
	
	@Transactional(readOnly = false)
	public void save(ErpGoodService erpGoodService) {
		super.save(erpGoodService);
	}
	
	@Transactional(readOnly = false)
	public void delete(ErpGoodService erpGoodService) {
		super.delete(erpGoodService);
	}

    /**
     * 根据goodId和isPackage查询商品对应的服务项目
     *
     * @param goodId
     * @param isPackage
     * @return
     * @date 2018年6月1日
     */
    public List<ErpGoodService> findServiceList(String goodId, String isPackage) {
        return erpGoodServiceDao.findServiceList(goodId, isPackage);
    }


    public List<ErpGoodService> findServiceListByBatch(List<String> goodIds, String isPackage) {
        return erpGoodServiceDao.findServiceListByBatch(goodIds, isPackage);
    }

    public void deleteByGoodId(String id) {
        erpGoodServiceDao.deleteByGoodId(id);
    }
}