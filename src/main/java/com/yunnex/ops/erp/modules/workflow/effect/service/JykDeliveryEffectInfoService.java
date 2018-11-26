package com.yunnex.ops.erp.modules.workflow.effect.service;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.workflow.effect.dao.JykDeliveryEffectInfoDao;
import com.yunnex.ops.erp.modules.workflow.effect.entity.JykDeliveryEffectInfo;

/**
 * 聚引客投放效果Service
 * 
 * @author SunQ
 * @date 2018年1月25日
 */
@Service
public class JykDeliveryEffectInfoService extends CrudService<JykDeliveryEffectInfoDao, JykDeliveryEffectInfo> {

    @Autowired
    private JykDeliveryEffectInfoDao jykDeliveryEffectInfoDao;

    @Transactional(readOnly = false)
    @Override
    public void save(JykDeliveryEffectInfo entity) {
        super.save(entity);
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(JykDeliveryEffectInfo entity) {
        super.delete(entity);
    }
    
    @Transactional(readOnly = false)
    public void deleteBefore(@Param("splitId") String splitId) {
        jykDeliveryEffectInfoDao.deleteBefore(splitId);
    }
    
    @Transactional(readOnly = false)
    public void updateState(@Param("id") String id, @Param("state") String state) {
        jykDeliveryEffectInfoDao.updateState(id, state);
    }
    
    public JykDeliveryEffectInfo getByProcInsId(String procInsId) {
        return jykDeliveryEffectInfoDao.getByProcInsId(procInsId);
    }
}