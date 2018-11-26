package com.yunnex.ops.erp.modules.sys.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.validation.Validator;

import org.activiti.engine.TaskService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.beanvalidator.BeanValidators;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.Pager;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.result.ResourceUnexistErrorResult;
import com.yunnex.ops.erp.common.result.SystemErrorResult;
import com.yunnex.ops.erp.common.result.UnsupportedErrorResult;
import com.yunnex.ops.erp.common.security.Digests;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.Encodes;
import com.yunnex.ops.erp.common.utils.HttpUtil;
import com.yunnex.ops.erp.modules.agent.entity.ErpAgentInfo;
import com.yunnex.ops.erp.modules.agent.service.ErpAgentInfoService;
import com.yunnex.ops.erp.modules.sys.constant.RoleConstant;
import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.sys.dao.RoleDao;
import com.yunnex.ops.erp.modules.sys.dao.UserDao;
import com.yunnex.ops.erp.modules.sys.dto.AgentUserRequestDto;
import com.yunnex.ops.erp.modules.sys.dto.AgentUserResponseDto;
import com.yunnex.ops.erp.modules.sys.dto.AgentUserSaveDto;
import com.yunnex.ops.erp.modules.sys.dto.UserRoleResponseDto;
import com.yunnex.ops.erp.modules.sys.entity.JobNumberInfo;
import com.yunnex.ops.erp.modules.sys.entity.Office;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.ldap.AgentUser;
import com.yunnex.ops.erp.modules.sys.ldap.AgentUserLdapDao;
import com.yunnex.ops.erp.modules.sys.ldap.AgentUserLdapService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.dao.ErpTeamDao;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamUser;
import com.yunnex.ops.erp.modules.team.service.ErpTeamUserService;

import yunnex.common.core.dto.ApiResult;

/**
 * 服务商用户Service
 */
@Service
public class AgentUserService extends CrudService<UserDao, User> {

    // 服务商密码加密次数
    private static final int HASH_ITERATION = 1024;
    // 盐的长度
    private static final int SALT_SIZE = 8;
    // 不足3位数左边用0补充
    private static final String FORMAT_THREE_ZERO_FILL = "%03d";
    private static final String USER_REMARKS_CREATE = "ERP创建";

    // 默认密码
    @Value("${default_user_password}")
    private String defaultPwd;
    @Value("${api_agent_password_modify_url}")
    private String API_AGENT_PASSWORD_MODIFY_URL;
    @Value("${userfiles.basedir}")
    private String filePath;

    @Autowired
    protected Validator validator;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ErpTeamDao erpTeamDao;
    @Autowired
    private AgentUserLdapDao agentUserLdapDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private ErpAgentInfoService agentInfoService;
    @Autowired
    private JobNumberInfoService jobNumberInfoService;
    @Autowired
    private ErpTeamUserService teamUserService;
    @Autowired
    private AgentUserLdapService agentUserLdapService;

    /**
     * 查找服务商唯一对应的管理员用户
     *
     * @param agentId
     * @return
     */
    public User findAgentUser(Integer agentId) {
        return dao.findAgentUser(agentId);
    }

    /**
     * 计算服务商用户数量
     *
     * @param agentId
     * @return
     */
    public Integer countAgentUsers(Integer agentId) {
        return dao.countAgentUsers(agentId);
    }

    /**
     * 服务商对应的用户信息
     *
     * @param agentId
     * @return
     */
    public AgentUserResponseDto findAgentUserInfo(Integer agentId) {
        User agentUser = userDao.findAgentUser(agentId);
        if (agentUser == null) {
            return null;
        }
        return findAgentUserRoleInfo(agentUser.getId());
    }

    /**
     * 获取服务商用户角色信息
     *
     * @param userId
     * @return
     */
    public AgentUserResponseDto findAgentUserRoleInfo(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }

        AgentUserResponseDto agentUserDto = dao.findAgentUserRoleInfo(userId);
        if (agentUserDto == null) {
            return null;
        }

        // 服务商角色
        List<Role> agentRoles = roleService.findByType(SysConstant.TYPE_AGENT);
        agentUserDto.setAgentRoles(agentRoles);

        // 选中用户的角色
        List<UserRoleResponseDto> userRoles = agentUserDto.getUserRoles();
        if (CollectionUtils.isNotEmpty(agentRoles) && CollectionUtils.isNotEmpty(userRoles)) {
            agentRoles.forEach(agentRole -> userRoles.forEach(userRole -> {
                if (agentRole.getId().equals(userRole.getRoleId())) {
                    agentRole.setChecked(Boolean.TRUE);
                    return;
                }
            }));
        }

        return agentUserDto;
    }

    /**
     * 分页查询服务商用户列表
     */
    public Pager<AgentUserResponseDto> findAgentUsersByPage(AgentUserRequestDto requestDto) {
        logger.info("服务商用户列表入参：{}", JSON.toJSON(requestDto));

        if (requestDto == null) {
            return null;
        }

        User loginUser = UserUtils.getUser();
        if (loginUser != null && SysConstant.TYPE_AGENT.equals(loginUser.getType())) {
            requestDto.setAgentId(loginUser.getAgentId());
        }

        List<AgentUserResponseDto> agentUsers = dao.findAgentUsersByPage(requestDto);
        // 查找用户的所有角色
        if (CollectionUtils.isNotEmpty(agentUsers)) {
            agentUsers.forEach(user -> {
                List<UserRoleResponseDto> userRoles = userDao.findUserRoles(user.getId());
                user.setUserRoles(userRoles);
            });
        }
        requestDto.setPage(false);
        Long count = dao.countAgentUsersPage(requestDto);
        return new Pager<>(agentUsers, count);
    }

    /**
     * 重置用户密码为“888888”
     *
     * @param loginName
     */
    @Transactional
    public BaseResult resetPwd(String loginName) {
        logger.info("重置服务商用户密码，loginName={}", loginName);
        if (StringUtils.isBlank(loginName)) {
            throw new ServiceException("登录账号不能为空！");
        }
        User user = dao.findByLoginName(loginName);
        if (user == null) {
            return new ResourceUnexistErrorResult("用户不存在或已被删除！");
        }
        if (Constant.YES.equals(user.getAgentUserFlag())) {
            return new UnsupportedErrorResult("服务商管理员不能重置密码！");
        }
        ErpAgentInfo agentInfo = user.getAgentInfo();
        if (agentInfo == null) {
            return new ResourceUnexistErrorResult("用户对应的服务商不存在或已被删除！");
        }

        // 修改数据库密码
        String salt = genSalt();
        String pwd = encryptPassword(defaultPwd, salt);
        user.setPassword(pwd);
        user.setSalt(salt);
        user.setUpdateBy(UserUtils.getUser());
        user.setUpdateDate(new Date());
        dao.updatePwdByLoginName(user);

        // 修改LDAP密码
        AgentUser agentUser = new AgentUser();
        agentUser.setAgentName(agentInfo.getName());
        agentUser.setFullName(loginName);
        AgentUser agentUserLdap = agentUserLdapDao.findByDn(agentUser);
        if (agentUserLdap == null) {
            throw new ServiceException("LDAP上不存在此服务商用户！");
        }
        agentUserLdap.setPassword(pwd);
        agentUserLdapDao.update(agentUserLdap);

        // 清空缓存
        UserUtils.clearCache(user);
        return new BaseResult();
    }

    /**
     * 删除服务商用户
     *
     * @param userId
     * @return
     */
    @Transactional
    public BaseResult deleteAgentUser(String userId) {
        logger.info("删除服务商用户，userId={}", userId);
        if (StringUtils.isBlank(userId)) {
            throw new ServiceException("用户ID不能为空！");
        }

        User userDB = userDao.get(userId);
        if (userDB == null) {
            return new ResourceUnexistErrorResult("用户不存在或已被删除!");
        }

        BaseResult result = new BaseResult();
        // 检查用户是否有正在处理的任务
        long count = taskService.createTaskQuery().taskAssignee(userId).count();
        if (count > Constant.ZERO) {
            result.error("20000", "当前用户有任务正在处理！");
            return result;
        }

        User user = new User();
        user.setId(userId);
        user.setDelFlag(Global.YES);
        user.setUpdateDate(new Date());

        // 逻辑删除用户
        userDao.delete(user);

        // 逻辑删除工号
        jobNumberInfoService.deleteByUserId(userId);

        // 逻辑删除用户与角色的关联关系
        roleDao.changeUserRoleDelFlag(user);

        // 逻辑删除用户与团队的关联关系
        teamUserService.changUserTeamDelFlag(user);

        // 清空缓存
        UserUtils.clearCache(user);
        return result;
    }

    /**
     * 查找服务商下的运营经理
     *
     * @param agentId
     * @return
     */
    public UserRoleResponseDto getAgentOpsManager(Integer agentId) {
        Integer aId = getAgentId(agentId);
        List<UserRoleResponseDto> agentRoles = agentInfoService.findAgentRoles(aId, RoleConstant.AGENT_OPERATION_MANAGER);
        if (CollectionUtils.isEmpty(agentRoles)) {
            return null;
        }
        return agentRoles.get(Constant.ZERO);
    }

    /**
     * 保存服务商用户信息
     *
     * @param saveDto
     * @return
     */
    @Transactional
    public BaseResult save(AgentUserSaveDto saveDto) {
        logger.info("保存服务商用户入参: saveDto={}", JSON.toJSON(saveDto));

        if (saveDto == null) {
            return new IllegalArgumentErrorResult("保存内容不能为空！");
        }

        // 确定服务商编号
        Integer agentId = getAgentId(saveDto.getAgentId());
        saveDto.setAgentId(agentId);

        // 字段合法性校验
        String validateResult = BeanValidators.validateWithMessage(validator, saveDto);
        if (validateResult != null) {
            return new IllegalArgumentErrorResult(validateResult);
        }

        // 保存用户信息
        User saveUser = saveUser(saveDto);

        // 保存工号信息
        saveUserJobNumberInfo(saveDto, saveUser);

        // 保存角色信息
        saveUserRole(saveDto, saveUser);

        // 保存团队信息
        saveUserTeam(saveDto, saveUser);

        // 保存用户信息到LDAP
        saveUserToLdap(saveUser);

        return new BaseResult();
    }

    /**
     * 获取服务商编号。如果前端指定了服务商编号，直接使用，否则获取当前登录服务商用户的服务商编号。
     *
     * @param agentId
     * @return
     */
    public Integer getAgentId(Integer agentId) {
        if (agentId != null) {
            return agentId;
        }
        User loginUser = UserUtils.getUser();
        checkLoginUser(loginUser);
        checkAgentUser(loginUser);
        return loginUser.getAgentId();
    }

    /**
     * 保存用户信息到LDAP
     *
     * @param saveUser
     */
    private void saveUserToLdap(User saveUser) {
        checkUser(saveUser);
        // 用户对应的服务商
        ErpAgentInfo agentInfo = agentInfoService.findByAgentId(saveUser.getAgentId());
        if (agentInfo == null) {
            throw new ServiceException("服务商不存在！");
        }
        User userInfo = new User();
        userInfo.setAgentName(agentInfo.getName());
        userInfo.setName(saveUser.getName());
        userInfo.setLoginName(saveUser.getLoginName());
        userInfo.setPassword(saveUser.getPassword());
        userInfo.setMobile(saveUser.getMobile());
        userInfo.setEmail(saveUser.getEmail());

        agentUserLdapService.saveAgentUser(userInfo);
    }

    /**
     * 保存团队信息。将用户划到服务商团队，如果有运营经理角色，指定为团队管理员。
     *
     * @param saveDto
     * @param saveUser
     */
    private void saveUserTeam(AgentUserSaveDto saveDto, User saveUser) {
        checkAgentUserSaveDto(saveDto);
        checkUser(saveUser);

        // 服务商团队
        ErpTeam agentTeam = erpTeamDao.findAgentTeam(saveDto.getAgentId());
        if (agentTeam == null) {
            String msg = "服务商团队不存在！";
            logger.error("{}agentId={}", msg, saveDto.getAgentId());
            throw new ServiceException(msg);
        }

        // 用户是否已经归属于服务商团队
        ErpTeamUser teamUser = teamUserService.findByTeamIdAndUserId(agentTeam.getId(), saveUser.getId());
        if (teamUser == null) {
            teamUser = new ErpTeamUser();
        }
        teamUser.setUserId(saveUser.getId());
        teamUser.setTeamId(agentTeam.getId());
        // 如果有运营经理角色，设置为团队管理员
        boolean hasOper = saveDto.getRoles().stream().map(Role::getEnname).filter(name -> RoleConstant.AGENT_OPERATION_MANAGER.equals(name))
                        .count() > Constant.ZERO; // 判断是否包含运营经理角色
        teamUser.setLeaderFlag(hasOper ? Integer.valueOf(Global.YES) : Integer.valueOf(Global.NO));
        teamUserService.save(teamUser);
    }

    /**
     * 保存用户角色信息。一个服务商只能有一个运营经理。
     *
     * @param saveDto
     * @param saveUser
     */
    private void saveUserRole(AgentUserSaveDto saveDto, User saveUser) {
        checkAgentUserSaveDto(saveDto);
        checkUser(saveUser);

        // 删除角色前查询是否是服务商管理员
        boolean isAdmin = isAgentAdmin(saveUser.getId());

        // 先删除原有角色
        userDao.deleteUserRole(new User(saveUser.getId()));

        saveDto.getRoles().forEach(role -> {
            // 原来不是管理员的用户，不能设置为管理员
            if (!isAdmin && RoleConstant.AGENT_ADMIN.equals(role.getEnname())) {
                String msg = "不能设置为管理员角色！";
                throw new ServiceException(msg);
            }
            // 如果设置为运营经理，先将原来的运营经理删除（关联关系）
            if (RoleConstant.AGENT_OPERATION_MANAGER.equals(role.getEnname())) {
                UserRoleResponseDto agentOpsManager = getAgentOpsManager(saveUser.getAgentId());
                if (agentOpsManager != null) {
                    userDao.delUserRoleByUidAndRid(agentOpsManager.getUserId(), agentOpsManager.getRoleId());
                }
            }
            // 给用户分配新角色
            User user = new User(saveUser.getId());
            user.setRole(new Role(role.getId()));
            userDao.saveUserRole(user);
        });
    }

    /**
     * 保存工号信息
     *
     * @param saveDto
     * @param saveUser
     */
    private void saveUserJobNumberInfo(AgentUserSaveDto saveDto, User saveUser) {
        checkAgentUserSaveDto(saveDto);
        checkUser(saveUser);

        JobNumberInfo jobNumberInfo = null;
        String jobId = saveDto.getJobId();
        if (StringUtils.isNotBlank(jobId)) {
            jobNumberInfo = jobNumberInfoService.get(jobId);
            if (jobNumberInfo == null) {
                String msg = "更新的工号信息不存在或已被删除！";
                logger.error("{}jobId={}", msg, jobId);
                throw new ServiceException(msg);
            }
        }
        if (jobNumberInfo == null) {
            jobNumberInfo = new JobNumberInfo();
            jobNumberInfo.setRoleId(Constant.BLANK);
            jobNumberInfo.setRoleName(Constant.BLANK);
            jobNumberInfo.setTelephone(Constant.BLANK);
            jobNumberInfo.setType(SysConstant.TYPE_AGENT);
        }

        jobNumberInfo.setUserId(saveUser.getId()); // 新增用户保存后生成ID
        jobNumberInfo.setUserName(saveDto.getUserName());
        jobNumberInfo.setJobNumber(saveDto.getJobNumber());
        jobNumberInfo.setIconImg(saveDto.getJobIconImg());
        jobNumberInfo.setScore(saveDto.getJobScore());

        jobNumberInfoService.save(jobNumberInfo);
    }

    /**
     * 保存服务商用户信息
     *
     * @param saveDto
     * @return
     */
    private User saveUser(AgentUserSaveDto saveDto) {
        checkAgentUserSaveDto(saveDto);

        /* 保存用户信息 */
        User user = null;
        // ID不为空表示修改
        String userId = saveDto.getUserId();
        if (StringUtils.isNotBlank(userId)) {
            user = super.get(userId);
            // 如果数据库中没有要更新的用户，则更新错误
            if (user == null) {
                String msg = "更新的用户不存在或已被删除！";
                logger.error("{}userId={}", msg, userId);
                throw new ServiceException(msg);
            }
            // 清空缓存
            UserUtils.clearCache(user);
        }

        // 不存在该用户表示新增
        if (user == null) {
            user = new User();
            // 生成员工ID
            user.setEmployeeId(genEmployeeId(saveDto.getAgentId()));
            // 服务商
            user.setAgentId(saveDto.getAgentId());
            // 生成默认密码
            String salt = genSalt();
            String pwd = encryptPassword(defaultPwd, salt);
            user.setSalt(salt);
            user.setPassword(pwd);
            user.setAgentUserFlag(Constant.NO);
            user.setType(SysConstant.TYPE_AGENT);
            user.setRemarks(USER_REMARKS_CREATE);
            // 公司和部门字段不允许为空
            user.setCompany(new Office(Constant.BLANK));
            user.setOffice(new Office(Constant.BLANK));
            ErpAgentInfo agentInfo = agentInfoService.findByAgentId(saveDto.getAgentId());
            // 服务商不可用时不能登录
            String loginFlag = agentInfo != null && Constant.STATE_ENABLE.equals(agentInfo.getState()) ? Global.YES : Global.NO;
            user.setLoginFlag(loginFlag);
        }

        String loginName = saveDto.getLoginName().trim();
        boolean isLoginNameSame = loginName.equalsIgnoreCase(user.getLoginName());

        // 服务商对应的管理员不能修改登录账号，否则可以
        if (!isLoginNameSame && Constant.YES.equals(user.getAgentUserFlag())) {
            throw new ServiceException("服务商对应的管理员用户不能修改登录账号！");
        }
        // 如果登录账号和原来不一样，检查LDAP上是否已存在新的登录账号
        if (!isLoginNameSame && userService.checkNameExists(loginName)) {
            throw new ServiceException("登录账号已存在！");
        }

        user.setLoginName(loginName);
        user.setName(saveDto.getUserName());
        user.setMobile(saveDto.getMobile());
        user.setEmail(saveDto.getEmail());
        super.save(user);

        return user;
    }

    /**
     * 判断用户是否是服务商管理员角色
     *
     * @param userId
     * @return
     */
    public boolean isAgentAdmin(String userId) {
        if (StringUtils.isBlank(userId)) {
            return false; // 如果userId为空，当作不是
        }
        return userDao.isUserRole(userId, RoleConstant.AGENT_ADMIN) > Constant.ZERO;
    }

    /**
     * 生成员工ID: 服务商编码+(全部员工数+1)，小于3位数前面补0
     *
     * @return
     */
    public String genEmployeeId(Integer agentId) {
        Integer agentUsersCount = userDao.countAllAgentUsers(agentId);
        String count = String.format(FORMAT_THREE_ZERO_FILL, ++agentUsersCount);
        return agentId.toString() + count;
    }

    /**
     * 检查用户是否是服务商用户
     *
     * @param user
     */
    private void checkAgentUser(User user) {
        if (user == null || !SysConstant.TYPE_AGENT.equals(user.getType())) {
            String msg = "当前用户不是服务商用户！";
            logger.error(msg);
            throw new ServiceException(msg);
        }
    }

    private static void checkAgentUserSaveDto(AgentUserSaveDto saveDto) {
        if (saveDto == null) {
            throw new ServiceException("保存的用户内容不能为空！");
        }
    }

    private static void checkLoginUser(User loginUser) {
        if (loginUser == null) {
            throw new ServiceException("用户未登录！");
        }
    }

    private static void checkUser(User user) {
        if (user == null) {
            throw new ServiceException("用户数据不能为空！");
        }
    }

    /**
     * 修改服务商管理员用户密码（外部系统(OEM)调用）
     *
     * @param params
     * @return
     */
    @Transactional
    public ApiResult<Void> modifyAgentAdminUserPwd(Map<String, String> params) {
        String msg = "修改服务商管理员用户密码！";
        logger.info("{}入参：{}", msg, params);

        ApiResult<Void> apiResult = ApiResult.build();
        String agentIdStr = params.get("agentId");
        Integer agentId = agentIdStr != null ? Integer.valueOf(agentIdStr) : null;
        if (agentId == null) {
            apiResult.error(BaseResult.CODE_ERROR_ARG, "agentId不能为空！");
            return apiResult;
        }
        String encryptPwd = params.get("encryptPwd");
        if (StringUtils.isBlank(encryptPwd)) {
            apiResult.error(BaseResult.CODE_ERROR_ARG, "密码不能为空！");
            return apiResult;
        }

        // 服务商对应的管理员用户
        User agentUser = userDao.findAgentUser(agentId);
        if (agentUser == null) {
            String err = "不存在该服务商对应的管理员用户！";
            apiResult.error(BaseResult.CODE_ERROR_ARG, err);
            logger.error("{}{}agentId={}", msg, err, agentId);
            return apiResult;
        }

        // 修改用户密码
        modifyPassword(agentUser, encryptPwd);

        return apiResult;
    }

    /**
     * 修改服务商用户密码（用户表，商户表，LDAP）
     *
     * @param user
     * @param encryptPwd
     */
    @Transactional
    public void modifyPassword(User user, String encryptPwd) {
        String msg = "修改服务商用户密码！";
        if (logger.isInfoEnabled()) {
            logger.info("{}userId={}, agentId={}", msg, user != null ? user.getId() : null, user != null ? user.getAgentId() : null);
        }
        checkUser(user);

        // 修改服务商管理员用户密码
        user.setPassword(encryptPwd);
        user.setUpdatePasswordFlag(Integer.valueOf(Global.YES));
        userService.save(user);

        // 如果是服务商对应的管理员用户，要同时修改ERP的服务商密码（不能在此调用修改OEM服务商密码接口，因为OEM修改密码时也会调用这个方法）
        ErpAgentInfo agentInfo = agentInfoService.findByAgentId(user.getAgentId());
        if (Constant.YES.equals(user.getAgentUserFlag())) {
            if (logger.isInfoEnabled()) {
                logger.info("{}服务商ID={}", msg, agentInfo != null ? agentInfo.getId() : null);
            }
            if (agentInfo != null) {
                agentInfo.setPassword(encryptPwd);
                agentInfoService.save(agentInfo);
            } else {
                throw new ServiceException("当前用户是服务商管理员，但对应的服务商不存在！");
            }
        }

        // 修改LDAP密码
        AgentUser agentUser = new AgentUser();
        agentUser.setFullName(user.getLoginName());
        agentUser.setAgentName(agentInfo != null ? agentInfo.getName() : null);
        AgentUser agentUserLdap = agentUserLdapDao.findByDn(agentUser);
        logger.info("{}LDAP服务商用户信息：{}", msg, agentUserLdap);
        if (agentUserLdap != null) {
            agentUserLdap.setPassword(encryptPwd);
            agentUserLdapDao.update(agentUserLdap);
        } else {
            throw new ServiceException("LDAP上无此用户！");
        }

        // 清空缓存
        UserUtils.clearCache(user);

        logger.info("{}结束！", msg);
    }

    /**
     * 修改当前登录的服务商用户密码（管理员和员工）
     *
     * @param originalPwd
     * @param newPwd
     * @param confirmPwd
     * @return
     */
    @Transactional
    public BaseResult modifyAgentUserPwd(String originalPwd, String newPwd, String confirmPwd) {
        User loginUser = UserUtils.getUser();
        User userDB = userDao.get(loginUser.getId());
        if (userDB == null) {
            return new ResourceUnexistErrorResult("用户不存在或已被删除！");
        }

        if (!SysConstant.TYPE_AGENT.equals(userDB.getType())) {
            return new UnsupportedErrorResult("非服务商用户!");
        }

        if (Stream.of(originalPwd, newPwd, confirmPwd).anyMatch(StringUtils::isBlank)) {
            return new IllegalArgumentErrorResult("请输入密码!");
        }

        if (!newPwd.equals(confirmPwd)) {
            return new IllegalArgumentErrorResult("两次输入密码不一致!");
        }

        // 密码长度和复杂度校验
        BaseResult result = checkPwd(newPwd);
        if (!BaseResult.isSuccess(result)) {
            return result;
        }

        String encryptPwd = encryptPassword(originalPwd, userDB.getSalt());
        if (!userDB.getPassword().equals(encryptPwd)) {
            return new IllegalArgumentErrorResult("原密码不正确!");
        }

        /* 校验通过后开始修改 */

        // 加密新密码
        String pwdNew = encryptPassword(newPwd, userDB.getSalt());

        // 如果是服务商对应的管理员用户，调用OEM修改密码接口
        if (Constant.YES.equals(userDB.getAgentUserFlag())) {
            result = modifyOemAgentPwd(userDB.getAgentId(), pwdNew);
            if (!BaseResult.isSuccess(result)) {
                return result;
            }
        }

        // 持久化新密码
        modifyPassword(userDB, pwdNew);

        return result;
    }

    /**
     * 修改OEM服务商密码
     *
     * @param agentId
     * @param passWord
     * @return
     */
    private BaseResult modifyOemAgentPwd(Integer agentId, String passWord) {
        BaseResult result = new BaseResult();

        try {
            String url = String.format(API_AGENT_PASSWORD_MODIFY_URL, agentId, passWord);
            String httpResult = HttpUtil.sendHttpPostReqToServerByReqbody(url, null, "application/json");
            if (httpResult == null) {
                throw new ServiceException();
            }
            JSONObject jsonObject = JSONObject.parseObject(httpResult);
            if (!"success".equals(jsonObject.getString("code"))) {
                result.error(BaseResult.CODE_ERROR_ARG, jsonObject.getString("reason"));
                return result;
            }
        } catch (ServiceException e) {
            String msg = "调用OEM接口修改服务商管理员密码出错！";
            logger.error(msg, e);
            return new SystemErrorResult(msg);
        }

        return result;
    }


    /* ---------------服务商用户密码校验与加密，跟OEM保持一致(start)-------------- */

    /**
     * 服务商用户密码加密
     *
     * @param plainPassword
     * @param salt
     * @return
     */
    public static String encryptPassword(String plainPassword, String salt) {
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), Encodes.decodeHex(salt), HASH_ITERATION);
        return Encodes.encodeHex(hashPassword);
    }

    private static String genSalt() {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        return Encodes.encodeHex(salt);
    }

    private BaseResult checkPwd(String newPwd) {
        BaseResult result = new BaseResult();
        // 新密码长度限制
        if (newPwd.length() < 8 || newPwd.length() > 16) {
            return result.error(BaseResult.CODE_ERROR_ARG, "请输入8-16位的密码！");
        }

        // 增加对密码复杂度的验证
        boolean pwdComplexityEnough = checkPwdComplexity(newPwd);
        if (!pwdComplexityEnough) {
            return result.error(BaseResult.CODE_ERROR_ARG, "密码不合规范,须包含大小写字母和数字3种");
        }
        return result;
    }

    /**
     * 检查密码的复杂度（是否满足：包含大小写字母和数字3种）
     */
    private static boolean checkPwdComplexity(String password) {
        boolean hasCapital = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;
        char c;

        for (int i = 0; i < password.length(); i++) {
            c = password.charAt(i);
            if (c >= '0' && c <= '9') {
                hasNumber = true;
            } else if (c >= 'a' && c <= 'z') {
                hasLowercase = true;
            } else if (c >= 'A' && c <= 'Z') {
                hasCapital = true;
            }
        }

        return (hasCapital && hasLowercase && hasNumber);
    }

    /* ---------------服务商用户密码校验与加密，跟OEM保持一致(end)-------------- */

    /**
     * 业务定义：获取可供用户选择的头像列表
     * 
     * @date 2018年10月12日
     * @author R/Q
     */
    public List<String> getAvatarList() {
        List<String> avatarList = new ArrayList<String>();
        if (StringUtils.isNotBlank(this.filePath)) {
            String directoryPath = "upload/avatar";
            File file = new File(this.filePath + directoryPath);
            if (file != null) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File imgFile : files) {
                        if (!imgFile.isDirectory()) {
                            if (imgFile.getName().startsWith("avatar")) {
                                avatarList.add(directoryPath + "/" + imgFile.getName());
                            }
                        }
                    }
                }
            }
        }
        return avatarList;
    }
}
