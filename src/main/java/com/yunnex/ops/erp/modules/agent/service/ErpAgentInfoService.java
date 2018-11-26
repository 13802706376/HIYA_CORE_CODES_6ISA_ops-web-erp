package com.yunnex.ops.erp.modules.agent.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.persistence.Pager;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.agent.dao.ErpAgentInfoDao;
import com.yunnex.ops.erp.modules.agent.dto.AgentInfoRequestDto;
import com.yunnex.ops.erp.modules.agent.dto.AgentInfoResponseDto;
import com.yunnex.ops.erp.modules.agent.entity.ErpAgentInfo;
import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.sys.dao.UserDao;
import com.yunnex.ops.erp.modules.sys.dto.UserRoleResponseDto;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;

/**
 * 服务商信息Service
 * 
 * @author yunnex
 * @version 2018-05-28
 */
@Service
public class ErpAgentInfoService extends CrudService<ErpAgentInfoDao, ErpAgentInfo> {

    @Autowired
    private UserDao userDao;

    @Override
    public ErpAgentInfo get(String id) {
        return super.get(id);
    }

    @Override
    public List<ErpAgentInfo> findList(ErpAgentInfo erpAgentInfo) {
        return super.findList(erpAgentInfo);
    }

    @Override
    public Page<ErpAgentInfo> findPage(Page<ErpAgentInfo> page, ErpAgentInfo erpAgentInfo) {
        return super.findPage(page, erpAgentInfo);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(ErpAgentInfo erpAgentInfo) {
        super.save(erpAgentInfo);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(ErpAgentInfo erpAgentInfo) {
        super.delete(erpAgentInfo);
    }

    /**
     * 根据服务商编号查找
     *
     * @param agentId
     * @return
     */
    public ErpAgentInfo findByAgentId(Integer agentId) {
        return dao.findByAgentId(agentId);
    }

    /**
     * 服务商列表
     * 
     * @param requestDto
     * @return
     */
    public Pager<AgentInfoResponseDto> findByPage(AgentInfoRequestDto requestDto) {
        logger.info("服务商列表入参：{}", JSON.toJSON(requestDto));

        // 如果是服务商用户，只能查询自己所属的服务商信息
        User user = UserUtils.getUser();
        if (user != null && SysConstant.TYPE_AGENT.equals(user.getType())) {
            requestDto.setAgentId(user.getAgentId());
        }

        List<AgentInfoResponseDto> data = dao.findByPage(requestDto);
        // 计算服务商员工数量
        if (CollectionUtils.isNotEmpty(data)) {
            data.forEach(dto -> {
                Integer total = userDao.countAgentUsers(dto.getAgentId());
                dto.setMemberCount(total);
            });
        }
        requestDto.setPage(false); // 计算总数时不分页，不排序
        Long count = dao.count(requestDto);
        return new Pager(data, count);
    }


    /**
     * 查找服务商下指定角色的所有用户
     *
     * @param agentId
     * @param roleEnName
     * @return
     */
    public List<UserRoleResponseDto> findAgentRoles(Integer agentId, String roleEnName) {
        return dao.findAgentRoles(agentId, roleEnName);
    }
}
