/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.sys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.TreeService;
import com.yunnex.ops.erp.modules.sys.dao.OfficeDao;
import com.yunnex.ops.erp.modules.sys.entity.LdapOfficeDto;
import com.yunnex.ops.erp.modules.sys.entity.Office;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
public class OfficeService extends TreeService<OfficeDao, Office> {

    @Autowired
    private OfficeDao officeDao;

    public List<Office> findAll() {
        return UserUtils.getOfficeList();
    }

    public List<Office> findList(Boolean isAll) {
        if (isAll != null && isAll) {
            return UserUtils.getOfficeAllList();
        } else {
            return UserUtils.getOfficeList();
        }
    }

    public List<Office> findList(Office office) {
        if (office != null) {
            office.setParentIds(office.getParentIds() + "%");
            return dao.findByParentIdsLike(office);
        }
        return new ArrayList<Office>();
    }

    @Transactional(readOnly = false)
    public void save(Office office) {
        super.save(office);
        UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
    }

    @Transactional(readOnly = false)
    public void delete(Office office) {
        super.delete(office);
        UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
    }

    @Transactional(readOnly = false)
    public void updateName(String id, String name, Date updateDate) {
        officeDao.updateName(id, name, updateDate);
    }

    public int countByParentId(String parentId) {
        Integer result = officeDao.countByParentId(parentId);
        return null == result ? 0 : result.intValue();
    }

    public Office findByLdapDn(String ldapDn) {
        return dao.findByLdapDn(ldapDn);
    }

    /**
     * 更新LDAP信息
     *
     * @param dto
     */
    @Transactional
    public void updateLdapInfo(LdapOfficeDto dto) {
        dao.updateLdapInfo(dto);
    }

    /**
     * 根据ID查找
     *
     * @param id
     * @return
     */
    public Office findById(String id) {
        return dao.findById(id);
    }

    public List<Office> findByParentIds(String parentIds) {
        return dao.findByParentIds(parentIds);
    }

    public List<Office> findAllList(Office office) {
        return dao.findAllList(office);
    }
}
