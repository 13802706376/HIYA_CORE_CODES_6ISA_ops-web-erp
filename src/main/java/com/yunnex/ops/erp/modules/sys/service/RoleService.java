package com.yunnex.ops.erp.modules.sys.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.sys.dao.RoleDao;
import com.yunnex.ops.erp.modules.sys.entity.Role;

/**
 * 角色业务管理
 * 
 * @author linqunzhi
 * @date 2018年5月15日
 */
@Service
public class RoleService extends CrudService<RoleDao, Role> {

    @Autowired
    private SystemService systemService;

    /**
     * 根据用户id获取用户角色
     *
     * @param userId
     * @return
     * @date 2018年5月15日
     * @author linqunzhi
     */
    public List<Role> findByUserId(String userId) {
        logger.info("findByUserId start | userId={}", userId);
        List<Role> result = dao.findByUserId(userId);
        logger.info("findByUserId end | result.size={}", result == null ? 0 : result.size());
        return result;

    }

    /**
     * 按类型查找
     *
     * @param type
     * @return
     */
    public List<Role> findByType(String type) {
        return dao.findByType(type);
    }


    /**
     * 获取指定类型的角色(用户权限范围内的角色)
     *
     * @param type
     * @return
     */
    public List<Role> getAllRoles(String type) {
        List<Role> roles = systemService.findAllRole();
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }

        if (StringUtils.isBlank(type)) {
            throw new ServiceException("角色类型不能为空！");
        }

        return roles.stream().map(role -> role.clone()).filter(role -> type.equals(role.getType())).collect(Collectors.toList());
    }

    @Transactional
    public void updateRoleOfficeByOfficeId(String newOfficeId, String oldOfficeId) {
        dao.updateRoleOfficeByOfficeId(newOfficeId, oldOfficeId);
    }

    @Transactional
    public void updateOfficeIdByOfficeId(String newOfficeId, Date updateDate, String oldOfficeId) {
        dao.updateOfficeIdByOfficeId(newOfficeId, updateDate, oldOfficeId);
    }

}
