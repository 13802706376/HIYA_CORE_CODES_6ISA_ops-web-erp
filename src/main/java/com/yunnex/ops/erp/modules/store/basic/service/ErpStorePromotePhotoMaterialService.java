package com.yunnex.ops.erp.modules.store.basic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.store.basic.dao.ErpStorePromotePhotoMaterialDao;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;

/**
 * 推广图片素材Service
 * 
 * @author yunnex
 * @version 2017-12-09
 */
@Service
public class ErpStorePromotePhotoMaterialService extends CrudService<ErpStorePromotePhotoMaterialDao, ErpStorePromotePhotoMaterial> {
    @Autowired
    private ErpStorePromotePhotoMaterialDao promotephotodao;

    @Override
    @Transactional(readOnly = false)
    public void save(ErpStorePromotePhotoMaterial erpStorePromotePhotoMaterial) {
        super.save(erpStorePromotePhotoMaterial);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpStorePromotePhotoMaterial erpStorePromotePhotoMaterial) {
        super.delete(erpStorePromotePhotoMaterial);
    }

    public int countWhereStoreId(String del, String id) {
        return promotephotodao.countWhereStoreId(del, id);
    }

    public ErpStorePromotePhotoMaterial findlistWhereStoreId(String del, String id) {
        return promotephotodao.findlistWhereStoreId(del, id);
    }

}
