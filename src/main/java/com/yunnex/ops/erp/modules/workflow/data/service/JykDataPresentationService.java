package com.yunnex.ops.erp.modules.workflow.data.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.workflow.data.dao.JykDataPresentationDao;
import com.yunnex.ops.erp.modules.workflow.data.entity.JykDataPresentation;

/**
 * 数据报告查询Service
 * 
 * @author SunQ
 * @date 2018年1月23日
 */
@Service
public class JykDataPresentationService extends CrudService<JykDataPresentationDao, JykDataPresentation> {

    /**
     * 数据报告查询Dao
     */
    @Autowired
    private JykDataPresentationDao jykDataPresentationDao;

    @Transactional(readOnly = false)
    @Override
    public void save(JykDataPresentation entity) {
        super.save(entity);
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(JykDataPresentation entity) {
        super.delete(entity);
    }
    
    public List<JykDataPresentation> findListBySplitId(String splitId) {
        return jykDataPresentationDao.findListBySplitId(splitId);
    }
    
    public List<JykDataPresentation> findListBySplitIdAndState(String splitId, String state) {
        return jykDataPresentationDao.findListBySplitIdAndState(splitId, state);
    }
    
    public JykDataPresentation getOnlyOne(String splitId, String state, String dataType) {
        return jykDataPresentationDao.getOnlyOne(splitId, state, dataType);
    }
    
    @Transactional(readOnly = false)
    public void deleteBefore(String splitId, String dataType) {
        jykDataPresentationDao.deleteBefore(splitId, dataType);
    }
    
    @Transactional(readOnly = false)
    public void updateState(@Param("id") String id, @Param("state") String state) {
        jykDataPresentationDao.updateState(id, state);
    }
    
    public JykDataPresentation getByProcInsId(@Param("procInsId") String procInsId, @Param("dataType") String dataType) {
        return jykDataPresentationDao.getByProcInsId(procInsId, dataType);
    }
}