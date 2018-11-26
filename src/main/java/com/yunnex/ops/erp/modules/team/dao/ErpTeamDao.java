package com.yunnex.ops.erp.modules.team.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;

/**
 * 团队DAO接口
 * @author huanghaidong
 * @version 2017-10-26
 */
@MyBatisDao
public interface ErpTeamDao extends CrudDao<ErpTeam> {
	int findteam(@Param("del") String del, @Param("teamid") String teamid);

    /**
     * 查找服务商对应的同名团队
     * 
     * @param agentId
     * @return
     */
    ErpTeam findAgentTeam(Integer agentId);

    /**
     * 根据userId 获取团队列表
     *
     * @param userId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    List<ErpTeam> findByUserId(@Param("userId") String userId);
    
    List<ErpTeam> findByUserId1(@Param("userId") String userId,@Param("agentId") int agentId);
    
    List<Map<String, String>>  findByService();
    
    List<Map<String, String>>  findTeamByService(@Param("userId") String userId);
    
    List<Map<String, String>>  findTeamByType(@Param("serviceType") String serviceType,@Param("userId") String userId);
    
    List<Map<String, String>>  findUserByService();
    
    List<Map<String, String>>  findUserByService(@Param("userId") String userId);

    /**
     * 查找指定用户所属团队下的所有用户
     * 
     * @param userId
     * @return
     */
    List<String> findTeamUsersByUserId(String userId);
}
