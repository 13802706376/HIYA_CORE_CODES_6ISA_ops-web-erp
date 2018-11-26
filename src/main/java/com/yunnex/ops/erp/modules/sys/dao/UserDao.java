/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.sys.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.sys.dto.AgentUserRequestDto;
import com.yunnex.ops.erp.modules.sys.dto.AgentUserResponseDto;
import com.yunnex.ops.erp.modules.sys.dto.UserRoleResponseDto;
import com.yunnex.ops.erp.modules.sys.entity.User;

/**
 * 用户DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {
    
    /**
     * 根据登录名称查询用户
     * @param loginName
     * @return
     */
    public User getByLoginName(User user);
    
    public User getUserByTaskId(String taskId);
    

    /**
     * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
     * @param user
     * @return
     */
    public List<User> findUserByOfficeId(User user);
    
    /**
     * 查询全部用户数目
     * @return
     */
    public long findAllCount(User user);
    
    /**
     * 更新用户密码和状态标志
     * 
     * @param user
     * @return
     */
    public int updatePasswordById(User user);
    
    /**
     * 更新登录信息，如：登录IP、登录时间
     * @param user
     * @return
     */
    public int updateLoginInfo(User user);

    /**
     * 删除用户角色关联数据
     * @param user
     * @return
     */
    public int deleteUserRole(User user);
    
    /**
     * 插入用户角色关联数据
     * @param user
     * @return
     */
    public int insertUserRole(User user);
    
    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public int updateUserInfo(User user);

    public int getUpdatePasswordFlag(String userid);


    public List<String> findAllNormalUserIdList();

    public List<User> getUserByRoleName(String roleName);

    public List<User> getUserByRoleNameIn(@Param("name") String name, @Param("array") String... roleNames);

    public List<User> getOpsAdviserByRight(@Param("userId") String userId);

    /**
     * 根据商户表的主键获取该商户所在团队下面具有运营顾问权限的成员信息
     *
     * @param shopInfoId
     * @return
     * @date 2018年7月13日
     */
    public List<User> getOperationAdviserByShopInfoId(@Param("shopInfoId") String shopInfoId);

    /**
     * 根据团队id获取用户信息列表
     *
     * @param teamId
     * @param leaderFlag
     * @return
     * @date 2018年5月16日
     * @author linqunzhi
     */
    public List<User> findByTeamId(@Param("teamId") String teamId, @Param("leaderFlag") Integer leaderFlag);

    /**
     * 查找服务商唯一对应的管理员用户
     * 
     * @param agentId
     * @return
     */
    User findAgentUser(Integer agentId);

    /**
     * 查询用户是否拥有某个角色
     * 
     * @param userId
     * @param roleId
     * @return
     */
    Integer countUserRole(@Param("userId") String userId, @Param("roleId") String roleId);

    /**
     * 关联用户和角色
     * 
     * @param user
     */
    void saveUserRole(@Param("user") User user);

    /**
     * 改变服务商下所有用户的可用状态
     * 
     * @param updateDate
     * @param agentId
     */
    void changeAgentUserState(@Param("loginFlag") String loginFlag, @Param("updateDate") Date updateDate,
                    @Param("agentId") Integer agentId);

    /**
     * 改变某个服务商下的所有用户与所有角色的关联关系的可用状态
     * 
     * @param agentId
     */
    void changeAgentUserRoleState(@Param("delFlag") String delFlag, @Param("agentId") Integer agentId);

    /**
     * 计算服务商用户数量
     * 
     * @param agentId
     * @return
     */
    Integer countAgentUsers(Integer agentId);

    /**
     * 计算服务商的所有用户数量，包括删除的
     * 
     * @param agentId
     * @return
     */
    Integer countAllAgentUsers(Integer agentId);

    /**
     * 获取服务商用户角色信息
     * 
     * @param userId
     * @return
     */
    AgentUserResponseDto findAgentUserRoleInfo(String userId);

    /**
     * 服务商用户列表
     */
    List<AgentUserResponseDto> findAgentUsersByPage(AgentUserRequestDto requestDto);

    /**
     * 服务商用户列表总数
     * 
     * @param requestDto
     * @return
     */
    Long countAgentUsersPage(AgentUserRequestDto requestDto);

    /**
     * 查找用户的所有角色
     * 
     * @param userId
     * @return
     */
    List<UserRoleResponseDto> findUserRoles(String userId);
    
    public List<User> getUserByRoleNameAndAgentId(@Param("roleName") String roleName, @Param("agentId") Integer agentId);

    /**
     * 查找服务商对应团队下指定角色的用户
     * 
     * @return
     */
    List<User> findRoleUsersByAgent(@Param("agentId") Integer agentId, @Param("roleEnName") String roleEnName);

    /**
     * 根据登录名查找用户信息
     * 
     * @param loginName
     * @return
     */
    User findByLoginName(String loginName);

    /**
     * 修改密码
     * 
     * @param user
     */
    void updatePwdByLoginName(User user);


    /**
     * 根据多个团队id 获取团队人员信息
     *
     * @param teamIdList
     * @param leaderFlag
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    public List<User> findByTeamIds(@Param("teamIdList") List<String> teamIdList, @Param("leaderFlag") Integer leaderFlag);

    
    /**
     * 根据多个团队id 获取团队人员信息
     *
     * @param teamIdList
     * @param leaderFlag
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    public List<User> findByTeamIds1(@Param("teamIdList") List<String> teamIdList);
    /**
     * 删除指定用户的指定角色
     * 
     * @param userId
     * @param roleId
     */
    void delUserRoleByUidAndRid(@Param("userId") String userId, @Param("roleId") String roleId);

    /**
     * 查询用户是否是指定角色
     * 
     * @param userId
     * @param roleEnName
     * @return
     */
    Integer isUserRole(@Param("userId") String userId, @Param("roleEnName") String roleEnName);

    void updateOfficeIdByOfficeId(@Param("newOfficeId") String newOfficeId, @Param("updateDate") Date date, @Param("oldOfficeId") String oldOfficeId);

    /**
     * 查找服务商下的所有用户
     * 
     * @param agentId
     * @return
     */
    List<User> findAllByAgentId(Integer agentId);
    
    User findManagerFlow(String procInsId);
}

