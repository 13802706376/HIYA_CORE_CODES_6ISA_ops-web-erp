package com.yunnex.ops.erp.modules.shopdata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.shopdata.dao.ErpShopDataInputDao;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpShopDataInput;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;

/**
 * 商户资料录入Service
 * 
 * @author SunQ
 * @date 2017年12月7日
 */
@Service
public class ErpShopDataInputService extends CrudService<ErpShopDataInputDao, ErpShopDataInput> {

    @Autowired
    private ErpShopDataInputDao erpShopDataInputDao;

    @Transactional(readOnly = false)
    @Override
    public void save(ErpShopDataInput entity) {
        super.save(entity);
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(ErpShopDataInput entity) {
        super.delete(entity);
    }

    @Transactional(readOnly = false)
    public boolean insert(ErpShopDataInput entity) {
        return erpShopDataInputDao.insert(entity) > 0;
    }
    
    public List<ErpShopDataInput> findListByParams(ErpShopDataInput entity) {
        entity.setUserId(UserUtils.getUser().getId());
        return erpShopDataInputDao.findListByParams(entity);
    }
    
    public ErpShopDataInput getByProsIncId(String procInsId) {
        return erpShopDataInputDao.getByProsIncId(procInsId);
    }
    
    public List<String> findFollowByParams(String shopName, String orderNumber) {
        ErpShopDataInput erpShopDataInput = new ErpShopDataInput();
        erpShopDataInput.setShopName(shopName);
        erpShopDataInput.setOrderNumber(orderNumber);
        erpShopDataInput.setUserId(UserUtils.getUser().getId());
        return erpShopDataInputDao.findFollowByParams(erpShopDataInput);
    } 
    
    public int countByShopId(String shopId) {
        return erpShopDataInputDao.countByShopId(shopId);
    }
    
    public ErpShopDataInput getByShopId(String shopId) {
        return erpShopDataInputDao.getByShopId(shopId);
    }
    
    public List<String> findTaskId(String zhangbeiId) {
        return erpShopDataInputDao.findTaskId(zhangbeiId);
    }
}