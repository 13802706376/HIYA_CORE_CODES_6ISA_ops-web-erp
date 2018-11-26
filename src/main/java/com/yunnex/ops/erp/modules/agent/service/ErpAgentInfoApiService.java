package com.yunnex.ops.erp.modules.agent.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.HttpUtil;
import com.yunnex.ops.erp.modules.agent.constant.AgentConstant;
import com.yunnex.ops.erp.modules.agent.entity.ErpAgentInfo;
import com.yunnex.ops.erp.modules.sys.constant.RoleConstant;
import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.sys.dao.RoleDao;
import com.yunnex.ops.erp.modules.sys.dao.UserDao;
import com.yunnex.ops.erp.modules.sys.dto.UserRoleResponseDto;
import com.yunnex.ops.erp.modules.sys.entity.Office;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.ldap.AgentUserLdapService;
import com.yunnex.ops.erp.modules.sys.service.AgentUserService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.constant.TeamConstant;
import com.yunnex.ops.erp.modules.team.dao.ErpTeamDao;
import com.yunnex.ops.erp.modules.team.dao.ErpTeamUserDao;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamUser;

/**
 * 服务商同步
 */
@Service
public class ErpAgentInfoApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpAgentInfoApiService.class);

    private static final Long SLEEP_TIME = 2000L;
    // OEM服务商
    private static final Integer OEM_AGENT = 0;
    // OEM停用
    private static final String OEM_DISABLE = "0";
    // OEM启用
    private static final String OEM_ENABLE = "1";
    // 服务商管理员用户的序号
    private static final String ADMIN_ORDER_NO = "001";
    // 日志
    private static final String MSG = "服务商同步: ";
    private static final String USER_REMARKS = "OEM同步";

    @Value("${api_agent_url}")
    private String apiAgentUrl;

    @Autowired
    protected DataSourceTransactionManager txManager;
    @Autowired
    private ErpAgentInfoService agentInfoService;
    @Autowired
    private AgentUserService agentUserService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ErpTeamDao teamDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ErpTeamUserDao teamUserDao;
    @Autowired
    private AgentUserLdapService agentUserLdapService;

    /**
     * 同步服务商
     *
     * @return
     */
    public Boolean syncAll() {
        new Thread(() -> syncAgents()).start(); // NOSONAR
        return Boolean.TRUE;
    }

    /**
     * 从OEM获取所有服务商，并保存相应数据。
     */
    public void syncAgents() {
        JSONArray agents = getAgentsFromOem();
        if (agents == null) {
            return;
        }
        agents.forEach(agent -> saveOne((JSONObject) agent));
    }

    /**
     * 保存单个服务商及相应数据
     *
     * @param agent
     */
    public void saveOne(JSONObject agent) {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) { // NOSONAR
            LOGGER.info("睡眠被打断！", e);
            return;
        }
        LOGGER.info("{}保存单个服务商信息入参：{}", MSG, agent);
        // 异步需要手动控制事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
        TransactionStatus status = txManager.getTransaction(def); // 获得事务状态
        ErpAgentInfo agentInfo = null;
        try {
            // 解析服务商信息
            agentInfo = parseAgent(agent);
            if (agentInfo == null) {
            	LOGGER.info("服务商信息为空：{}, 保存失败。", agent);
            	txManager.rollback(status);
            	return;
            }
            // 保存服务商信息
            saveAgent(agentInfo);
            // 保存用户信息
            User agentUser = saveAgentUser(agentInfo);
            // 给用户分配管理员和运营经理角色运营顾问
            saveAgentUserRole(agentUser, RoleConstant.AGENT_ADMIN);
            grantOpsManager(agentUser);
            grantOpsAdviser(agentUser);
            // 保存服务商团队
            ErpTeam agentTeam = saveAgentTeam(agentInfo);
            // 设置服务商用户为团队管理员
            saveAgentUserTeam(agentTeam, agentUser);
            // 根据服务商的可用状态改变其下用户及相关数据的可用状态
            changState(agentInfo);
            // 创建或移动服务商
            agentUserLdapService.createOrMoveAgent(agentInfo);
            // 在LDAP上创建/修改服务商对应的用户
            agentUserLdapService.saveAgentUser(agentUser);
            txManager.commit(status);
        } catch (RuntimeException e) {
            LOGGER.error(MSG + "保存服务商出错！agentInfo = {}", agentInfo, e);
            // 手动回滚事务，让后面的代码可以继续执行
            txManager.rollback(status);
        }
    }
    
    private void grantOpsAdviser(User user) {
        List<UserRoleResponseDto> agentRoles = agentInfoService.findAgentRoles(user.getAgentId(), RoleConstant.OPS_ADVISER_AGENT);
        if (CollectionUtils.isEmpty(agentRoles)) {
            saveAgentUserRole(user, RoleConstant.OPS_ADVISER_AGENT);
        }
    }

    /**
     * 根据服务商的可用状态改变其下用户的可用状态.<br/>
     * 1.已停用的服务商，对应的管理员及管理员下的创建的用户不能登录系统；<br/>
     * 2.重新启用后，对应的管理员及管理员下的创建的用户能重现登录系统；<br/>
     * 3.（逻辑）删除不可用用户与团队和角色的关联关系？？
     * 
     * @param agentInfo
     */
    private void changState(ErpAgentInfo agentInfo) {
        checkAgentNotNull(agentInfo);
        Integer agentId = agentInfo.getAgentId();

        String state = agentInfo.getState();
        String loginFlag = Global.YES; // 0：不可登录，1：可登录
        if (Constant.STATE_DISENABLE.equals(state)) {
            loginFlag = Global.NO;
        }

        Date now = new Date();
        // 改变服务商下所有用户的登录状态
        userDao.changeAgentUserState(loginFlag, now, agentId);
        // 改变某个服务商下的所有用户与所有角色的关联关系的可用状态？
        // 改变服务商用户与团队的关联关系的可用状态？
    }

    /**
     * 设置用户为团队管理员
     * 
     * @param team
     * @param user
     */
    private void saveAgentUserTeam(ErpTeam team, User user) {
        if (user == null || team == null) {
            return;
        }
        String teamId = team.getId();
        String userId = user.getId();
        ErpTeamUser teamUser = teamUserDao.findByTeamIdAndUserId(teamId, userId);
        if (teamUser == null) {
            teamUser = new ErpTeamUser();
            teamUser.setUserId(userId);
            teamUser.setTeamId(teamId);
            // 设置为团队管理员
            teamUser.setLeaderFlag(Integer.valueOf(Global.YES));
            teamUser.setIsNewRecord(false);
            teamUser.preInsert();
            teamUserDao.insert(teamUser);
        } else {
            teamUser.preUpdate();
            teamUser.setLeaderFlag(Integer.valueOf(Global.YES));
            teamUserDao.update(teamUser);
        }
    }

    /**
     * 保存服务商团队
     *
     * @param agentInfo
     */
    private ErpTeam saveAgentTeam(ErpAgentInfo agentInfo) {
        checkAgentNotNull(agentInfo);
        Integer agentId = agentInfo.getAgentId();

        ErpTeam agentTeam = teamDao.findAgentTeam(agentId);
        if (agentTeam == null) {
            agentTeam = new ErpTeam();
            agentTeam.setTeamName(agentInfo.getName());
            agentTeam.setCompanyType(SysConstant.COMPANY_TYPE_AGENT);
            agentTeam.setAgentTeamFlag(Constant.YES);
            agentTeam.setSort(TeamConstant.SORT_AGENT);
            agentTeam.setAgentId(agentId);
            agentTeam.setIsNewRecord(false);
            agentTeam.preInsert();
            teamDao.insert(agentTeam);
        } else {
            // 只更新团队名为服务商名
            agentTeam.setTeamName(agentInfo.getName());
            agentTeam.preUpdate();
            teamDao.update(agentTeam);
        }
        return agentTeam;
    }

    /**
     * 分配运营经理角色，如果服务商下已经有一个运营经理，则不分配
     */
    private void grantOpsManager(User user) {
        List<UserRoleResponseDto> agentRoles = agentInfoService.findAgentRoles(user.getAgentId(), RoleConstant.AGENT_OPERATION_MANAGER);
        if (CollectionUtils.isEmpty(agentRoles)) {
            saveAgentUserRole(user, RoleConstant.AGENT_OPERATION_MANAGER);
        }
    }

    /**
     * 给用户分配角色
     *
     * @param user
     */
    private void saveAgentUserRole(User user, String roleEnName) {
        if (user == null) {
            return;
        }
        Role role = new Role();
        role.setEnname(roleEnName);
        Role roleDB = roleDao.getByEnname(role);
        if (roleDB == null) {
            String msg = MSG + "角色不存在！角色英文名：" + roleEnName;
            LOGGER.error(msg);
            throw new ServiceException(msg);
        }
        user.setRole(roleDB);
        Integer count = userDao.countUserRole(user.getId(), user.getRole().getId());
        // 用户不拥有角色时建立关联
        if (count == null || count == Constant.ZERO) {
            userDao.saveUserRole(user);
        }
    }

    /**
     * 保存服务商对应的用户信息
     *
     * @param agentInfo
     * @return
     */
    private User saveAgentUser(ErpAgentInfo agentInfo) {
        checkAgentNotNull(agentInfo);
        Integer agentId = agentInfo.getAgentId();
        User agentUser = agentUserService.findAgentUser(agentId);
        if (agentUser == null) {
            agentUser = new User();
            // 公司和部门字段不允许为空
            agentUser.setCompany(new Office(Constant.BLANK));
            agentUser.setOffice(new Office(Constant.BLANK));
            // 用户姓名只在创建用户时设置，不更新，因为ERP可以编辑
            agentUser.setName(agentInfo.getName());
            // 员工ID = 服务商编号 + 序号
            agentUser.setEmployeeId(agentId + ADMIN_ORDER_NO);
            agentUser.setRemarks(USER_REMARKS);
            // 排序
            agentUser.setSort(SysConstant.SORT_ADMIN);
            agentUser.setType(SysConstant.TYPE_AGENT);
            agentUser.setAgentUserFlag(Constant.YES);
        }
        agentUser.setAgentName(agentInfo.getName());
        agentUser.setLoginName(agentInfo.getLoginAccount());
        agentUser.setPassword(agentInfo.getPassword());
        agentUser.setSalt(agentInfo.getSalt());
        agentUser.setAgentId(agentId);
        agentUserService.save(agentUser);
        UserUtils.clearCache(agentUser);
        return agentUser;
    }

    /**
     * 保存服务商信息
     *
     * @param agentInfo
     */
    private ErpAgentInfo saveAgent(ErpAgentInfo agentInfo) {
        checkAgentNotNull(agentInfo);
        Integer agentId = agentInfo.getAgentId();
        ErpAgentInfo agentInfoDB = agentInfoService.findByAgentId(agentId);
        // 数据库中已有，表示要执行更新。由于目前ERP不能编辑服务商信息，所以所有字段值以OEM为准。
        if (agentInfoDB != null) {
            agentInfo.setId(agentInfoDB.getId()); // 设置agentInfo的id，告诉save方法执行更新操作
            agentInfo.setOldName(agentInfoDB.getName()); // 旧服务商名称
        }
        agentInfoService.save(agentInfo);
        return agentInfo;
    }

    /**
     * 检查服务商是否存在
     *
     * @param agentInfo
     */
    private static void checkAgentNotNull(ErpAgentInfo agentInfo) {
        if (agentInfo == null || agentInfo.getAgentId() == null) {
            throw new ServiceException("服务商不存在！");
        }
    }

    /**
     * 解析服务商json成java对象
     * 
     * @param jsonObject
     * @return
     */
    private ErpAgentInfo parseAgent(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        // 服务商编号，对应OEM的服务商主键
        Object id = jsonObject.get("id");
        String agentName = jsonObject.getString("companyName");
        Integer agentId = com.yunnex.ops.erp.common.utils.StringUtils.toInteger(id);
        if (agentId == null) {
            String msg = "服务商编号不能为空！";
            LOGGER.error("{}{}服务商名称：{}", MSG, msg, agentName);
            throw new ServiceException(msg);
        }
        Object agentProperty = jsonObject.get("agentProperty");
        // 只同步渠道（非直销）类型的服务商
        if (agentProperty != null && Integer.valueOf(agentProperty.toString()) != OEM_AGENT) {
            LOGGER.info("{}非渠道类型的服务商不同步！agentId={}", MSG, agentId);
            return null;
        }
        ErpAgentInfo agentInfo = new ErpAgentInfo();
        agentInfo.setAgentId(agentId);
        agentInfo.setName(agentName);
        agentInfo.setContactName(jsonObject.getString("contacts"));
        agentInfo.setContactPhone(jsonObject.getString("contactsPhone"));
        agentInfo.setLoginAccount(jsonObject.getString("account"));
        agentInfo.setPassword(jsonObject.getString("pwd"));
        agentInfo.setSalt(jsonObject.getString("salt"));
        // OEM服务商启用状态, 0：停用，1：启用
        String state = jsonObject.getString("useState");
        if (OEM_ENABLE.equals(state)) {
            agentInfo.setState(Constant.STATE_ENABLE);
            // 可用排序在前
            agentInfo.setSort(AgentConstant.SORT_ENABLE);
        } else if (OEM_DISABLE.equals(state)) {
            agentInfo.setState(Constant.STATE_DISENABLE);
            // 停用排序在后
            agentInfo.setSort(AgentConstant.SORT_DISABLE);
        }
        return agentInfo;
    }

    /**
     * 从OEM获取服务商信息，一次性获取全部
     *
     * @return
     */
    private JSONArray getAgentsFromOem() {
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MSG + "请求路径：{}", apiAgentUrl);
            }
            String result = HttpUtil.sendHttpGetReqToServer(apiAgentUrl);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MSG + "响应结果：{}", result);
            }
            if (StringUtils.isBlank(result)) {
                return null;
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!jsonObject.getBoolean(CommonConstants.RETURN_CODE_SUCCESS)) {
                return null;
            }
            JSONArray array = jsonObject.getJSONArray(CommonConstants.RESULT);
            if (array == null || array.isEmpty()) {
                return null;
            }
            return array;
        } catch (RuntimeException e) {
            LOGGER.error(MSG + "调用OEM接口出错！", e);
        }
        return null;
    }

}
