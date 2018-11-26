/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.sys.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.User;

/**
 * 角色DAO接口
 * @author ThinkGem
 * @version 2013-12-05
 */
@MyBatisDao
public interface RoleDao extends CrudDao<Role> {

    public Role getByName(Role role);

    public Role getByEnname(Role role);

    /**
     * 维护角色与菜单权限关系
     * 
     * @param role
     * @return
     */
    public int deleteRoleMenu(Role role);

    public int insertRoleMenu(Role role);

    /**
     * 维护角色与公司部门关系
     * 
     * @param role
     * @return
     */
    public int deleteRoleOffice(Role role);

    public int insertRoleOffice(Role role);

    /**
     * 根据用户id获取用户角色
     *
     * @param userId
     * @return
     * @date 2018年5月15日
     * @author linqunzhi
     */
    public List<Role> findByUserId(String userId);

    /**
     * 按类型查找
     * 
     * @param type
     * @return
     */
    public List<Role> findByType(String type);

    /**
     * 修改用户与角色关联的删除标志
     * 
     * @param user
     */
    void changeUserRoleDelFlag(User user);

    void updateOfficeIdByOfficeId(@Param("newOfficeId") String newOfficeId, @Param("updateDate") Date updateDate,
                    @Param("oldOfficeId") String oldOfficeId);

    void updateRoleOfficeByOfficeId(@Param("newOfficeId") String newOfficeId, @Param("oldOfficeId") String oldOfficeId);
}
