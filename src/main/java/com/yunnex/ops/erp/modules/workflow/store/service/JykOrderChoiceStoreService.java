package com.yunnex.ops.erp.modules.workflow.store.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.store.constant.StoreConstant;
import com.yunnex.ops.erp.modules.workflow.flow.dto.AdvAuditStatusDto;
import com.yunnex.ops.erp.modules.workflow.store.dao.JykOrderChoiceStoreDao;
import com.yunnex.ops.erp.modules.workflow.store.entity.JykOrderChoiceStore;

/**
 * 聚引客订单推广门店管理Service
 * 
 * @author SunQ
 * @date 2018年1月8日
 */
@Service
public class JykOrderChoiceStoreService extends CrudService<JykOrderChoiceStoreDao, JykOrderChoiceStore> {

    @Autowired
    private JykOrderChoiceStoreDao jykOrderChoiceStoreDao;

    @Transactional(readOnly = false)
    @Override
    public void save(JykOrderChoiceStore entity) {
        super.save(entity);
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(JykOrderChoiceStore entity) {
        super.delete(entity);
    }

    public JykOrderChoiceStore getBySplitId(String splitId) {
        return jykOrderChoiceStoreDao.getBySplitId(splitId);
    }

    public String getStoreIdBySplitId(String splitId) {
        return jykOrderChoiceStoreDao.getStoreIdBySplitId(splitId);
    }

    public String getStoreIdByProcInsId(String procInsId) {
        return jykOrderChoiceStoreDao.getStoreIdByProcInsId(procInsId);
    }

    @Transactional(readOnly = false)
    public void deleteByByProcInsId(String procInsId) {
        jykOrderChoiceStoreDao.deleteByByProcInsId(procInsId);
    }

    public List<String> getAllStoreIdBySplitId(String splitId) {
        return jykOrderChoiceStoreDao.getAllStoreIdBySplitId(splitId);
    }

    /**
     * 获取推广门店的广告主审核状态
     * 
     * @param splitId
     * @return
     */
    public List<AdvAuditStatusDto> getAdvAuditStatus(String splitId) {
        if (splitId == null) {
            return Lists.newArrayList();
        }
        List<AdvAuditStatusDto> advAuditStatus = dao.getAdvAuditStatus(splitId);
        if (CollectionUtils.isEmpty(advAuditStatus)) {
            return Lists.newArrayList();
        }
        // 设置状态码对应的显示名称
        for (AdvAuditStatusDto auditStatus : advAuditStatus) {
            // SQL中有可能查出NULL记录
            if (null == auditStatus) {
                continue;
            }
            auditStatus.setFriendsAuditName(StoreConstant.AdvertiserAuditStatus.getByStatus(auditStatus.getFriendsAuditStatus()));
            auditStatus.setMomoAuditName(StoreConstant.AdvertiserAuditStatus.getByStatus(auditStatus.getMomoAuditStatus()));
            auditStatus.setWeiboAuditName(StoreConstant.AdvertiserAuditStatus.getByStatus(auditStatus.getWeiboAuditStatus()));
        }

        return advAuditStatus;
    }
}
