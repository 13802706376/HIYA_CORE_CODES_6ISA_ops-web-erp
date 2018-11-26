package com.yunnex.ops.erp.modules.team.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamUser;

/**
 * 成员DAO接口
 * @author huanghaidong
 * @version 2017-10-26
 */
@MyBatisDao
public interface ErpTeamUserDao extends CrudDao<ErpTeamUser> {

    void deleteByTeamId(String teamId);

    List<ErpTeamUser> findListByTeamId(@Param("teamId") String teamId);

    List<ErpTeamUser> findwhereuser(@Param("del") String del, @Param("leaderf") String leaderf, @Param("userid") String userid);

    List<ErpTeamUser> findwhereteam(@Param("del") String del, @Param("tid") String tid);
    
    List<ErpTeam> findAdminTeams(@Param("userId") String userId, @Param("teamId") String teamId);
    
    List<String> findTeamUserIds(@Param("adminTeamIds") List<String> adminTeamIds);

    /**
     * 根据团队id 获取团队用户id集合
     *
     * @param teamId
     * @param leaderFlag
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    List<String> findUserIdsByTeamId(@Param("teamId") String teamId, @Param("leaderFlag") Integer leaderFlag);

    /**
     * 根据用户id 获取所在团队用户id集合
     *
     * @param userId
     * @param leaderFlag
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    List<String> findUserIdsByUserId(@Param("userId") String userId, @Param("leaderFlag") Integer leaderFlag);

    ErpTeamUser findByTeamIdAndUserId(@Param("teamId") String teamId, @Param("userId") String userId);

    /**
     * 修改服务商用户与团队的关联关系的可用状态
     * 
     * @param delFlag
     * @param updateDate
     * @param agentId
     */
    void changeAgentTeamUserState(@Param("delFlag") String delFlag, @Param("updateDate") Date updateDate, @Param("agentId") Integer agentId);

    /**
     * 修改用户与团队关联的删除标志
     * 
     * @param user
     */
    void changUserTeamDelFlag(User user);

}
