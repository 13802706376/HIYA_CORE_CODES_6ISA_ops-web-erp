package com.yunnex.ops.erp.modules.team.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.team.dao.ErpTeamUserDao;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamUser;

/**
 * 成员Service
 * 
 * @author huanghaidong
 * @version 2017-10-26
 */
@Service
public class ErpTeamUserService extends CrudService<ErpTeamUserDao, ErpTeamUser> {

    @Autowired
    private ErpTeamUserDao erpTeamUserDao;

    public ErpTeamUser get(String id) {
        return super.get(id);
    }

    public List<ErpTeamUser> findList(ErpTeamUser erpTeamUser) {
        return super.findList(erpTeamUser);
    }

    public Page<ErpTeamUser> findPage(Page<ErpTeamUser> page, ErpTeamUser erpTeamUser) {
        return super.findPage(page, erpTeamUser);
    }

    @Transactional(readOnly = false)
    public void save(ErpTeamUser erpTeamUser) {
        super.save(erpTeamUser);
    }

    @Transactional(readOnly = false)
    public void delete(ErpTeamUser erpTeamUser) {
        super.delete(erpTeamUser);
    }

    public void deleteByTeamId(String teamId) {
        erpTeamUserDao.deleteByTeamId(teamId);
    }

    public List<ErpTeamUser> findListByTeamId(String teamId) {
        return erpTeamUserDao.findListByTeamId(teamId);
    }

    public List<ErpTeamUser> findwhereuser(String del, String leaderf, String userid) {
        return erpTeamUserDao.findwhereuser(del, leaderf, userid);
    }

    public List<ErpTeamUser> findwhereteam(String del, String tid) {
        return erpTeamUserDao.findwhereteam(del, tid);
    }

    public List<ErpTeam> findAdminTeams(String userId, String teamId) {
        return erpTeamUserDao.findAdminTeams(userId, teamId);
    }

    public List<String> findTeamUserIds(List<String> adminTeamIds) {
        return erpTeamUserDao.findTeamUserIds(adminTeamIds);
    }

    public void changUserTeamDelFlag(User user) {
        erpTeamUserDao.changUserTeamDelFlag(user);
    }

    public ErpTeamUser findByTeamIdAndUserId(String teamId, String userId) {
        return erpTeamUserDao.findByTeamIdAndUserId(teamId, userId);
    }

    /**
     * 根据团队id 获取团队用户id集合
     *
     * @param teamId
     * @param leaderFlag
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    public List<String> findUserIdsByTeamId(String teamId, Integer leaderFlag) {
        logger.info("findUserIdsByTeamId start | teamId={}|leaderFlag={}", teamId, leaderFlag);
        if (StringUtils.isBlank(teamId)) {
            logger.error("teamId 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        List<String> result = erpTeamUserDao.findUserIdsByTeamId(teamId, leaderFlag);
        logger.info("findUserIdsByTeamId end | result.size={}", result == null ? 0 : result.size());
        return result;
    }

    /**
     * 根据用户id 获取所在团队用户id集合
     *
     * @param userId
     * @param leaderFlag
     * @return
     * @date 2018年5月9日
     * @author linqunzhi
     */
    public List<String> findUserIdsByUserId(String userId, Integer leaderFlag) {
        logger.info("findUserIdsByUserId start | userId={}|leaderFlag={}", userId, leaderFlag);
        if (StringUtils.isBlank(userId)) {
            logger.error("userId 不能为空");
            throw new ServiceException(CommonConstants.FailMsg.PARAM);
        }
        List<String> result = erpTeamUserDao.findUserIdsByUserId(userId, leaderFlag);
        logger.info("findUserIdsByUserId end | result.size={}", result == null ? 0 : result.size());
        return result;
    }

}
