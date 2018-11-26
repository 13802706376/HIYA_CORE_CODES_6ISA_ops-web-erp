package com.yunnex.ops.erp.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.sys.dao.JobNumberInfoDao;
import com.yunnex.ops.erp.modules.sys.entity.JobNumberInfo;

/**
 * 工号管理Service
 * @author SunQ
 * @date 2018年2月7日
 */
@Service
public class JobNumberInfoService extends CrudService<JobNumberInfoDao, JobNumberInfo> {

    /**
     * 工号管理Dao
     */
    @Autowired
    private JobNumberInfoDao jobNumberDao;
    @Autowired
    private SystemService systemService;

    @Transactional(readOnly = false)
    @Override
    public void save(JobNumberInfo entity) {
        super.save(entity);
    }

    @Transactional(readOnly = false)
    @Override
    public void delete(JobNumberInfo entity) {
        super.delete(entity);
    }
    
    public JobNumberInfo getByUserId(String userId) {
        return jobNumberDao.getByUserId(userId);
    }
    
    public int count(String userId) {
        return jobNumberDao.count(userId);
    }

    @Transactional
    public void deleteByUserId(String userId) {
        dao.deleteByUserId(userId);
    }

    /**
     * 创建ERP工号。2018/06/06从Controller移到Service.
     * 
     * @param jobNumber
     * @return
     */
    @Transactional
    public BaseResult createErpJobNumber(JobNumberInfo jobNumber) {
        BaseResult result = new BaseResult();
        String[] ids = jobNumber.getUserId().split(",");
        String[] names = jobNumber.getUserName().split(",");
        for (int i = 0; i < ids.length; i++) {
            if (count(ids[i]) == 0) {
                JobNumberInfo info = new JobNumberInfo();
                info.setJobNumber(jobNumber.getJobNumber());
                info.setRoleId(jobNumber.getRoleId());
                info.setRoleName(jobNumber.getRoleName());
                info.setUserId(ids[i]);
                info.setUserName(names[i]);
                info.setTelephone(jobNumber.getTelephone());
                info.setScore(jobNumber.getScore());
                info.setIconImg(jobNumber.getIconImg());
                // ERP类型的工号
                info.setType(SysConstant.TYPE_ERP);
                save(info);
            }
        }
        return result;
    }
}
