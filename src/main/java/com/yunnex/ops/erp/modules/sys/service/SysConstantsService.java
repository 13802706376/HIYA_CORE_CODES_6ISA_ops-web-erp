package com.yunnex.ops.erp.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.sys.dao.SysConstantsDao;
import com.yunnex.ops.erp.modules.sys.entity.SysConstants;

/**
 * 系统常量Service
 * 
 * @author linqunzhi
 * @version 2018-04-16
 */
@Service
public class SysConstantsService extends CrudService<SysConstantsDao, SysConstants> {

    @Override
    public SysConstants get(String id) {
        return super.get(id);
    }

    @Override
    public List<SysConstants> findList(SysConstants sysConstants) {
        return super.findList(sysConstants);
    }

    @Override
    public Page<SysConstants> findPage(Page<SysConstants> page, SysConstants sysConstants) {
        return super.findPage(page, sysConstants);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(SysConstants sysConstants) {
        super.save(sysConstants);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(SysConstants sysConstants) {
        super.delete(sysConstants);
    }

    /**
     * 根据key获取常量信息
     *
     * @param key
     * @return
     * @date 2018年4月16日
     * @author linqunzhi
     */
    public SysConstants getByKey(String key) {
        logger.info("getByKey start | key={}", key);
        if (StringUtils.isBlank(key)) {
            logger.error("key 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        SysConstants result = dao.getByKey(key);
        logger.info("getByKey end");
        return result;
    }


    public String getConstantValByKey(String key) {
        SysConstants result = this.getByKey(key);
        if (null == result)
            return StringUtils.EMPTY;
        return result.getValue();
    }

}
