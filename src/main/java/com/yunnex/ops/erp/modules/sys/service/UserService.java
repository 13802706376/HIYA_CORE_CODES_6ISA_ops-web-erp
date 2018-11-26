package com.yunnex.ops.erp.modules.sys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.modules.sys.dao.UserDao;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.ldap.LdapDao;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.service.ErpTeamService;


/**
 * 用户服务层
 */
@Service
public class UserService extends CrudService<UserDao, User> {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ErpTeamService erpTeamService;
    @Autowired
    private LdapDao ldapDao;
    
    public List<User> getUserByRoleName(String roleName) {
        return userDao.getUserByRoleName(roleName);
    }
    
    public List<User> getUserByRoleNameAndAgentId(String roleName,Integer agentId) {
        return userDao.getUserByRoleNameAndAgentId(roleName,agentId);
    }
    
    
    /**
     * 根据团队id 获取团队人员信息
     *
     * @return
     * @date 2018年5月16日
     * @author linqunzhi
     */
      public User getByLoginName(String loginName)
      {
           return userDao.getByLoginName(new User(null, loginName));
      }

    

    /**
     * 根据团队id 获取团队人员信息
     *
     * @param teamId
     * @param leaderFlag
     * @return
     * @date 2018年5月16日
     * @author linqunzhi
     */
    public List<User> findByTeamId(String teamId, Integer leaderFlag) {
        logger.info("findByTeamId start | teamId={}|leaderFlag={}", teamId, leaderFlag);
        List<User> result = userDao.findByTeamId(teamId, leaderFlag);
        logger.info("findByTeamId end | result.size={}", result == null ? 0 : result.size());
        return result;
    }

    /**
     * 根据多个团队id 获取团队人员信息
     *
     * @param principalId
     * @param teamId
     * @param leaderFlag
     * @return
     * @date 2018年5月16日
     * @author linqunzhi
     */
    public List<User> findByTeamIds(String principalId, List<String> teamIdList, Integer leaderFlag) {
        String teamIdListStr = JSON.toJSONString(teamIdList);
        logger.info("findByTeamIds start | principalId={},teamIdList={}|leaderFlag={}", teamIdListStr, leaderFlag);
        // 如果团队id集合 无数据 ，则根据用户获取团队集合
        if (CollectionUtils.isEmpty(teamIdList)) {
            List<ErpTeam> list = erpTeamService.findByUserId(principalId);
            if (CollectionUtils.isNotEmpty(list)) {
                teamIdList = new ArrayList<>();
                for (ErpTeam team : list) {
                    teamIdList.add(team.getId());
                }
            }
        }
        List<User> result = userDao.findByTeamIds(teamIdList, leaderFlag);
        logger.info("findByTeamIds end | result.size={}", result == null ? 0 : result.size());
        return result;
    }
    
    /**
     * 根据多个团队id 获取团队人员信息
     *
     * @param principalId
     * @param teamId
     * @param leaderFlag
     * @return
     * @date 2018年5月16日
     * @author linqunzhi
     */
    public List<User> findByTeamIds1(String principalId, List<String> teamIdList, Integer leaderFlag) {
        String teamIdListStr = JSON.toJSONString(teamIdList);
        logger.info("findByTeamIds start | principalId={},teamIdList={}|leaderFlag={}", teamIdListStr, leaderFlag);
        // 如果团队id集合 无数据 ，则根据用户获取团队集合
        if (CollectionUtils.isEmpty(teamIdList)) {
            List<ErpTeam> list = erpTeamService.findByUserId(principalId);
            if (CollectionUtils.isNotEmpty(list)) {
                teamIdList = new ArrayList<>();
                for (ErpTeam team : list) {
                    teamIdList.add(team.getId());
                }
            }
        }
        List<User> result = userDao.findByTeamIds1(teamIdList);
        logger.info("findByTeamIds end | result.size={}", result == null ? 0 : result.size());
        return result;
    }

    /**
     * 检查ERP LDAP和服务商用户LDAP上是否已存在指定登录账号
     * 
     * @param loginName
     * @return true: 已存在，false: 不存在
     */
    public Boolean checkNameExists(String loginName) {
        Boolean flag = ldapDao.erpCnExists(loginName);
        if (!flag) {
            flag = ldapDao.agentCnExists(loginName);
        }
        return flag;
    }

    @Transactional
    public void updateOfficeIdByOfficeId(String newOfficeId, Date date, String oldOfficeId) {
        dao.updateOfficeIdByOfficeId(newOfficeId, date, oldOfficeId);
    }

    public List<User> getUserByRoleNameIn(String... roleNames) {
        List<User> userList = dao.getUserByRoleNameIn(null, roleNames);
        // 去掉管理员
        if (CollectionUtils.isNotEmpty(userList)) {
            Iterator<User> iterator = userList.iterator();
            while (iterator.hasNext()) {
                User user = iterator.next();
                if ("administrator".equals(user.getLoginName())) {
                    iterator.remove();
                }
            }
        }
        return userList;
    }

    /**
     * 根据name模糊查询指定的角色 例如：roleNames= "ops_adviser", "ops_adviser_agent"则查询erp和服务商的运营顾问
     * 如果name=null则查询所有指定的运营顾问
     * 
     * @param name
     * @param roleNames
     * @return
     * @date 2018年7月5日
     */
    public List<User> getUserByCondition(String name, String... roleNames) {

        List<User> userList = dao.getUserByRoleNameIn(name, roleNames);
        // 去掉管理员
        if (CollectionUtils.isNotEmpty(userList)) {
            Iterator<User> iterator = userList.iterator();
            while (iterator.hasNext()) {
                User user = iterator.next();
                if ("administrator".equals(user.getLoginName())) {
                    iterator.remove();
                }
            }
        }
        return userList;
    }

    public List<User> getOperationAdviserByShopInfoId(String shopInfoId) throws ServiceException {
        if (StringUtils.isBlank(shopInfoId)) {
            throw new ServiceException();
        }

        List<User> userList = dao.getOperationAdviserByShopInfoId(shopInfoId);
        // 去掉管理员
        if (CollectionUtils.isNotEmpty(userList)) {
            Iterator<User> iterator = userList.iterator();
            while (iterator.hasNext()) {
                User user = iterator.next();
                if ("administrator".equals(user.getLoginName())) {
                    iterator.remove();
                }
            }
        }
        return userList;
    }

    public List<User> getOpsAdviserByRightOfLoginUser() {
        List<User> userList = dao.getOpsAdviserByRight(UserUtils.getUser().getId());
        // 去掉管理员
        if (CollectionUtils.isNotEmpty(userList)) {
            Iterator<User> iterator = userList.iterator();
            while (iterator.hasNext()) {
                User user = iterator.next();
                if ("administrator".equals(user.getLoginName())) {
                    iterator.remove();
                }
            }
        }
        return userList;
        /*Map<String, User> result = new HashMap<>();
        // 所有数据
        if (SecurityUtils.getSubject().isPermitted("shop:erpShopInfo:all")) {
            List<User> all = dao.getUserByRight(null, null);
            fillIntoMap(result, all);
        }
        // 所在公司数据
        if (SecurityUtils.getSubject().isPermitted("shop:erpShopInfo:company")) {
            List<User> company = dao.getUserByRight("company", UserUtils.getUser().getId());
            fillIntoMap(result, company);
        }
        // 所有分公司数据
        if (SecurityUtils.getSubject().isPermitted("shop:erpShopInfo:branchCompany")) {
            List<User> branch = dao.getUserByRight("branch", null);
            fillIntoMap(result, branch);
        }
        // 所有服务商数据
        if (SecurityUtils.getSubject().isPermitted("shop:erpShopInfo:serviceCompany")) {
            List<User> agent = dao.getUserByRight("agent", null);
            fillIntoMap(result, agent);
        }
        
        List<User> userList = new ArrayList<>(result.values());
        
        // 去掉管理员
        if (CollectionUtils.isNotEmpty(userList)) {
            Iterator<User> iterator = userList.iterator();
            while (iterator.hasNext()) {
                User user = iterator.next();
                if ("administrator".equals(user.getLoginName())) {
                    iterator.remove();
                }
            }
        }
        return userList;*/
    }

    private void fillIntoMap(Map<String, User> result, List<User> all) {
        if (CollectionUtils.isNotEmpty(all)) {
            for (User user : all) {
                if (user != null) {
                    result.put(user.getId(), user);
                }
            }
        }
    }

    public List<User> findAllByAgentId(Integer agentId) {
        return dao.findAllByAgentId(agentId);
    }

    public List<User> findRoleUsersByAgent(Integer agentId, String roleEnName) {
        return dao.findRoleUsersByAgent(agentId, roleEnName);
    }

    public List<User> findAllList(User user) {
        return dao.findAllList(user);
    }
}
