package com.yunnex.ops.erp.modules.shopdata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.shopdata.dao.ErpPayIntopiecesDao;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpPayIntopieces;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;

/**
 * 商户支付进件Service
 * 
 * @author SunQ
 * @date 2018年2月7日
 */
@Service
public class ErpPayIntopiecesService extends CrudService<ErpPayIntopiecesDao, ErpPayIntopieces> {
    
    /**
     * 商户支付进件Dao
     */
    @Autowired
    private ErpPayIntopiecesDao erpPayIntopiecesDao;

    @Transactional(readOnly = false)
    @Override
    public void save(ErpPayIntopieces entity) {
        super.save(entity);
    }
    
    public ErpPayIntopieces getByProsIncId(String procInsId) {
        return erpPayIntopiecesDao.getByProsIncId(procInsId);
    }
    
    public List<ErpPayIntopieces> findListByParams(String orderNumber, String shopName) {
        return erpPayIntopiecesDao.findListByParams(UserUtils.getUser().getId(), orderNumber, shopName);
    }
    
    public List<String> findTaskId(String shopId, String intopiecesType) {
        return erpPayIntopiecesDao.findTaskId(shopId, intopiecesType);
    }
}