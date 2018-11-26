package com.yunnex.ops.erp.modules.agent.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunnex.ops.erp.common.persistence.CrudDao;
import com.yunnex.ops.erp.common.persistence.annotation.MyBatisDao;
import com.yunnex.ops.erp.modules.agent.dto.AgentInfoRequestDto;
import com.yunnex.ops.erp.modules.agent.dto.AgentInfoResponseDto;
import com.yunnex.ops.erp.modules.agent.entity.ErpAgentInfo;
import com.yunnex.ops.erp.modules.sys.dto.UserRoleResponseDto;

/**
 * 服务商信息DAO接口
 * @author yunnex
 * @version 2018-05-28
 */
@MyBatisDao
public interface ErpAgentInfoDao extends CrudDao<ErpAgentInfo> {

    /**
     * 根据服务商编号查找
     * 
     * @param agentId
     * @return
     */
    ErpAgentInfo findByAgentId(Integer agentId);

    /**
     * 服务商列表
     * 
     * @param requestDto
     * @return
     */
    List<AgentInfoResponseDto> findByPage(AgentInfoRequestDto requestDto);

    /**
     * 服务商总数
     * 
     * @param requestDto
     * @return
     */
    Long count(AgentInfoRequestDto requestDto);

    /**
     * 查找服务商下指定角色的所有用户
     * 
     * @param agentId
     * @param roleEnName
     * @return
     */
    List<UserRoleResponseDto> findAgentRoles(@Param("agentId") Integer agentId, @Param("roleEnName") String roleEnName);
}
