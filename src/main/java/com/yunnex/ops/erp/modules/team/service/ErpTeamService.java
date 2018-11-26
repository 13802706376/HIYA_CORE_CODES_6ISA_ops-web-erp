package com.yunnex.ops.erp.modules.team.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.team.constant.TeamConstant;
import com.yunnex.ops.erp.modules.team.dao.ErpTeamDao;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamUser;

/**
 * 团队Service
 * @author huanghaidong
 * @version 2017-10-26
 */
@Service
public class ErpTeamService extends CrudService<ErpTeamDao, ErpTeam> {

    @Autowired
    private ErpTeamUserService erpTeamUserService;
    @Autowired
    private ErpTeamDao erpTeamDao;


    public ErpTeam get(String id) {
        return super.get(id);
    }

    public List<ErpTeam> findList(ErpTeam erpTeam) {
        return super.findList(erpTeam);
    }

    public Page<ErpTeam> findPage(Page<ErpTeam> page, ErpTeam erpTeam) {
        return super.findPage(page, erpTeam);
    }

    @Transactional(readOnly = false)
    public void save(ErpTeam erpTeam) {
        super.save(erpTeam);
    }

    @Transactional(readOnly = false)
    public void delete(ErpTeam erpTeam) {
        super.delete(erpTeam);
    }

    @Transactional(readOnly = false)
    public void saveWithMemberAndLeader(ErpTeam erpTeam, String teamLeaderIds, String teamMemberIds) {
        // 目前只能管理总部和分公司的团队
        if (erpTeam.getAgentId() > Constant.ZERO) {
            erpTeam.setSort(TeamConstant.SORT_BRANCH);
            erpTeam.setCompanyType(SysConstant.COMPANY_TYPE_BRANCH);
        } else {
            erpTeam.setSort(TeamConstant.SORT_HEAD);
            erpTeam.setCompanyType(SysConstant.COMPANY_TYPE_HEAD);
        }
        super.save(erpTeam);
        updateTeamMemberAndLeader(erpTeam.getId(), teamLeaderIds, teamMemberIds);
    }

    @Transactional(readOnly = false)
    public void updateTeamMemberAndLeader(String teamId, String teamLeaderIds, String teamMemberIds) {
        // 删除原有团队数据
        erpTeamUserService.deleteByTeamId(teamId);
        // leader Set
        Set<String> leaderSet = new HashSet<>();
        if (StringUtils.isNotEmpty(teamLeaderIds)) {
            String[] leadersArray = teamLeaderIds.split(",");
            for (int i = 0; i < leadersArray.length; i++) {
                leaderSet.add(leadersArray[i]);
            }
        }
        // 组员 Set
        Set<String> memberSet = new HashSet<>();
        if (StringUtils.isNotEmpty(teamMemberIds)) {
            String[] membersArray = teamMemberIds.split(",");
            for (int i = 0; i < membersArray.length; i++) {
                memberSet.add(membersArray[i]);
            }
        }
        // leader 设置
        Iterator<String> leaderIt = leaderSet.iterator();
        ErpTeamUser erpTeamUser = null;
        while (leaderIt.hasNext()) {
            String userId = leaderIt.next();
            erpTeamUser = new ErpTeamUser();
            erpTeamUser.setTeamId(teamId);
            erpTeamUser.setUserId(userId);
            erpTeamUser.setLeaderFlag(ErpTeamUser.LEADER_FLAG_YES);
            erpTeamUserService.save(erpTeamUser);
        }
        // 组员设置
        Iterator<String> memberIt = memberSet.iterator();
        while (memberIt.hasNext()) {
            String userId = memberIt.next();
            erpTeamUser = new ErpTeamUser();
            erpTeamUser.setTeamId(teamId);
            erpTeamUser.setUserId(userId);
            erpTeamUser.setLeaderFlag(ErpTeamUser.LEADER_FLAG_NO);
            erpTeamUserService.save(erpTeamUser);
        }
    }

    public int findteam(String del, String teamid) {
        return erpTeamDao.findteam(del, teamid);
    }

    /**
     * 根据userId 获取所在团队列表
     *
     * @param userId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    public List<ErpTeam> findByUserId(String userId) {
        logger.info("findListByUserId start | userId={}", userId);
        List<ErpTeam> result = erpTeamDao.findByUserId(userId);
        logger.info("findListByUserId end | result.size={}", result != null ? result.size() : 0);
        return result;
    }
    
    /**
     * 根据userId 获取所在团队列表
     *
     * @param userId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    public List<ErpTeam> findByUserId1(String userId,int agentId) {
        logger.info("findListByUserId start | userId={}", userId);
        List<ErpTeam> result = erpTeamDao.findByUserId1(userId,agentId);
        logger.info("findListByUserId end | result.size={}", result != null ? result.size() : 0);
        return result;
    }
    
    /**
     * 根据userId 获取所在团队列表
     *
     * @param userId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    public List<Map<String, String>> findByService() {
    	List<Map<String, String>> result = erpTeamDao.findByService();
        logger.info("findListByUserId end | result.size={}", result != null ? result.size() : 0);
        return result;
    }
    
    /**
     * 根据userId 获取所在团队列表
     *
     * @param userId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    public List<Map<String, String>> findTeamByService(String principalId) {
    	List<Map<String, String>> result = erpTeamDao.findTeamByService(principalId);
        logger.info("findListByUserId end | result.size={}", result != null ? result.size() : 0);
        return result;
    }
    /**
     * 根据userId 获取所在团队列表
     *
     * @param userId
     * @return
     * @date 2018年5月29日
     * @author linqunzhi
     */
    public List<Map<String, String>> findUserByService(String principalId) {
    	List<Map<String, String>> result = erpTeamDao.findUserByService(principalId);
        logger.info("findListByUserId end | result.size={}", result != null ? result.size() : 0);
        return result;
    }

    public List<String> findTeamUsersByUserId(String userId) {
        return dao.findTeamUsersByUserId(userId);
    }
}
