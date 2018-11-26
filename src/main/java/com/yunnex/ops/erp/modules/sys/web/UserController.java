/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.yunnex.ops.erp.modules.sys.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.beanvalidator.BeanValidators;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.persistence.Pager;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.ServiceErrorResult;
import com.yunnex.ops.erp.common.result.SystemErrorResult;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.DateUtils;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.utils.excel.ExportExcel;
import com.yunnex.ops.erp.common.utils.excel.ImportExcel;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.sys.dto.AgentUserRequestDto;
import com.yunnex.ops.erp.modules.sys.dto.AgentUserResponseDto;
import com.yunnex.ops.erp.modules.sys.dto.AgentUserSaveDto;
import com.yunnex.ops.erp.modules.sys.dto.UserRoleResponseDto;
import com.yunnex.ops.erp.modules.sys.entity.Office;
import com.yunnex.ops.erp.modules.sys.entity.Role;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.ldap.AgentUserLdapService;
import com.yunnex.ops.erp.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunnex.ops.erp.modules.sys.service.AgentUserService;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.sys.service.UserApiService;
import com.yunnex.ops.erp.modules.sys.service.UserService;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.service.ErpTeamService;

/**
 * 用户Controller
 * 
 * @author ThinkGem
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private UserApiService userApiService;
    @Autowired
    private ErpTeamService erpTeamService;
    @Autowired
    private UserService userService;

    @Autowired
    private AgentUserService agentUserService;

    @Autowired
    private AgentUserLdapService agentUserLdapService;

    @ModelAttribute
    public User get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return systemService.getUser(id);
        } else {
            return new User();
        }
    }

    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = {"index"})
    public String index(User user, Model model) {
        return "modules/sys/userIndex";
    }

    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = {"list", ""})
    public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model,
                    @RequestParam(required = false, defaultValue = SysConstant.TYPE_ERP) String type) {
        user.setType(type);
        Page<User> page = systemService.findUser(new Page<User>(request, response), user);
        model.addAttribute("page", page);
        return "modules/sys/userList";
    }

    @ResponseBody
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = {"listData"})
    public Page<User> listData(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<User> page = systemService.findUser(new Page<User>(request, response), user);
        return page;
    }

    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "form")
    public String form(User user, Model model) {
        if (user.getCompany() == null || user.getCompany().getId() == null) {
            user.setCompany(UserUtils.getUser().getCompany());
        }
        if (user.getOffice() == null || user.getOffice().getId() == null) {
            user.setOffice(UserUtils.getUser().getOffice());
        }
        model.addAttribute("user", user);
        model.addAttribute("allRoles", systemService.findAllRole());
        return "modules/sys/userForm";
    }

    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "save")
    public String save(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/user/list?repage";
        }
        // 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
        user.setCompany(new Office(request.getParameter("company.id")));
        user.setOffice(new Office(request.getParameter("office.id")));
        // 如果新密码为空，则不更换密码
        if (StringUtils.isNotBlank(user.getNewPassword())) {
            user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
        }
        if (!beanValidator(model, user)) {
            return form(user, model);
        }
        if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))) {
            addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
            return form(user, model);
        }
        // 角色数据有效性验证，过滤不在授权内的角色
        List<Role> roleList = Lists.newArrayList();
        List<String> roleIdList = user.getRoleIdList();
        for (Role r : systemService.findAllRole()) {
            if (roleIdList.contains(r.getId())) {
                roleList.add(r);
            }
        }
        user.setRoleList(roleList);
        // 保存用户信息
        systemService.saveUser(user);
        // 清除当前用户缓存
        if (user.getLoginName().equals(UserUtils.getUser().getLoginName())) {
            UserUtils.clearCache();
            // UserUtils.getCacheMap().clear();
        }
        addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "delete")
    public String delete(User user, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/user/list?repage";
        }
        if (UserUtils.getUser().getId().equals(user.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
        } else if (User.isAdmin(user.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
        } else {
            systemService.deleteUser(user);
            addMessage(redirectAttributes, "删除用户成功");
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 导出用户数据
     * 
     * @param user
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user);
            new ExportExcel("用户数据", User.class).setDataList(page.getList()).write(response, fileName).dispose();
            return null;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "导出用户失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 导入用户数据
     * 
     * @param file
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/user/list?repage";
        }
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<User> list = ei.getDataList(User.class);
            for (User user : list) {
                try {
                    if ("true".equals(checkLoginName("", user.getLoginName()))) {
                        // 初始化密码：
                        user.setPassword(SystemService.entryptPassword("888888"));
                        BeanValidators.validateWithException(validator, user);
                        systemService.saveUser(user);
                        successNum++;
                    } else {
                        failureMsg.append("<br/>登录名 " + user.getLoginName() + " 已存在; ");
                        failureNum++;
                    }
                } catch (ConstraintViolationException ex) {
                    failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：");
                    List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (String message : messageList) {
                        failureMsg.append(message + "; ");
                        failureNum++;
                    }
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条用户，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条用户" + failureMsg);
        } catch (InvalidFormatException | InstantiationException | IllegalAccessException | IOException e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "导入用户失败！失败信息：" + e.getMessage());
        }

        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 下载导入用户数据模板
     * 
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "用户数据导入模板.xlsx";
            List<User> list = Lists.newArrayList();
            list.add(UserUtils.getUser());
            new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/sys/user/list?repage";
    }

    /**
     * 验证登录名是否有效
     * 
     * @param oldLoginName
     * @param loginName
     * @return
     */
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "checkLoginName")
    public String checkLoginName(String oldLoginName, String loginName) {
        if (loginName != null && loginName.equals(oldLoginName)) {
            return "true";
        } else if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 用户信息显示及保存
     * 
     * @param user
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "info")
    public String info(User user, HttpServletResponse response, Model model) {
        User currentUser = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getName())) {
            if (Global.isDemoMode()) {
                model.addAttribute("message", "演示模式，不允许操作！");
                return "modules/sys/userInfo";
            }
            currentUser.setEmail(user.getEmail());
            currentUser.setPhone(user.getPhone());
            currentUser.setMobile(user.getMobile());
            currentUser.setRemarks(user.getRemarks());
            currentUser.setPhoto(user.getPhoto());
            systemService.updateUserInfo(currentUser);
            model.addAttribute("message", "保存用户信息成功");
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("Global", new Global());
        return "modules/sys/userInfo";
    }

    /**
     * 返回用户信息
     * 
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "infoData")
    public User infoData() {
        return UserUtils.getUser();
    }

    /**
     * 修改个人用户密码
     * 
     * @param oldPassword
     * @param newPassword
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "modifyPwd")
    public String modifyPwd(@RequestParam(value = "updatePasswordFlag", defaultValue = "0") String updatePasswordFlag, String oldPassword,
                    String newPassword, Model model) {
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
            if (Global.isDemoMode()) {
                model.addAttribute("message", "演示模式，不允许操作！");
                return "modules/sys/userModifyPwd";
            }
            if (SystemService.validatePassword(oldPassword, user.getPassword())) {
                // 修改密码及状态：
                updatePasswordFlag = "1";
                systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword, updatePasswordFlag);
                model.addAttribute("message", "修改密码成功");
                // 转向首页
                return "modules/sys/sysIndex";

            } else {
                model.addAttribute("message", "修改密码失败，旧密码错误");
            }
        }
        model.addAttribute("user", user);

        return "modules/sys/userModifyPwd";
    }

    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String officeId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<User> list = systemService.findUserByOfficeId(officeId);
        for (int i = 0; i < list.size(); i++) {
            User e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", "u_" + e.getId());
            map.put("pId", officeId);
            map.put("name", StringUtils.replace(e.getName(), " ", ""));
            mapList.add(map);
        }
        return mapList;
    }

    @RequiresPermissions("sys:user:edit")
    @ResponseBody
    @RequestMapping(value = "sync")
    public JSONObject sync() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", userApiService.sync());
        return jsonObject;
    }

    /**
     * 根据团队id获取用户信息列表
     *
     * @param teamId
     * @param leaderFlag
     * @return
     * @date 2018年5月16日
     * @author linqunzhi
     */
    @ResponseBody
    @RequestMapping(value = "findByTeamId")
    public BaseResult findByTeamId(String teamId, Integer leaderFlag) {
        List<User> result = userService.findByTeamId(teamId, leaderFlag);
        return new BaseResult(result);
    }

    /**
     * 根据多个团队id 获取团队人员信息
     *
     * @param teamIdList
     * @param leaderFlag
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    @ResponseBody
    @RequestMapping(value = "findByTeamIds")
    public BaseResult findByTeamIds(@RequestParam("teamIdList") List<String> teamIdList, Integer leaderFlag) {
        Principal principal = UserUtils.getPrincipal();
        List<User> result = userService.findByTeamIds(principal.getId(), teamIdList, leaderFlag);
        return new BaseResult(result);
    }
    
    /**
     * 根据多个团队id 获取团队人员信息
     *
     * @param teamIdList
     * @param leaderFlag
     * @return
     * @date 2018年5月31日
     * @author linqunzhi
     */
    @ResponseBody
    @RequestMapping(value = "findByTeamIds1")
    public BaseResult findByTeamIds1(@RequestParam("teamIdList") List<String> teamIdList, Integer leaderFlag) {
        Principal principal = UserUtils.getPrincipal();
        if(teamIdList.size()==0){
        	List<ErpTeam> list = erpTeamService.findByUserId1(principal.getId(),UserUtils.getUser().getAgentId());
            if (CollectionUtils.isNotEmpty(list)) {
                for (ErpTeam team : list) {
                	teamIdList.add(team.getId());
                }
            }
        }
        List<User> result = userService.findByTeamIds1(principal.getId(), teamIdList, leaderFlag);
        return new BaseResult(result);
    }

    /**
     * 获取服务商用户角色信息
     *
     * @param userId
     * @return
     */
    @RequestMapping("findAgentUserRoleInfo")
    public @ResponseBody AgentUserResponseDto findAgentUserRoleInfo(String userId) {
        return agentUserService.findAgentUserRoleInfo(userId);
    }

    /**
     * 业务定义：获取可供用户选择的头像列表
     * 
     * @date 2018年10月12日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping("getAvatarList")
    public Object getAvatarList() {
        return agentUserService.getAvatarList();
    }

    /**
     * 根据商户主键id获取该商户所在团队下面的运营顾问成员
     *
     * @param userId
     * @param key 根据key模糊查询运营顾问名字，如果为null则查询所有
     * @return
     * @throws yunnex.common.exception.ServiceException
     */
    @RequestMapping("getOperationAdviserByShopInfoId")
    @ResponseBody
    public List<User> getOperationAdviserByShopInfoId(String shopInfoId) {
        return userService.getOperationAdviserByShopInfoId(shopInfoId);
    }

    /**
     * 服务商用户/员工列表管理页
     *
     * @param from 标识从哪个入口进入，如果从服务商列表进入，则非服务商用户可根据权限确定能否新增员工，因为能确定服务商编号
     * @param model
     * @return
     */
    @RequiresPermissions("sys:user:agent:list")
    @RequestMapping("agent/list")
    public String agentUserList(@RequestParam(required = false, defaultValue = "menu") String from, Model model) {
        model.addAttribute("from", from);
        return "modules/agent/agentUserList";
    }

    /**
     * 分页查询服务商用户列表
     *
     * @param requestDto
     * @return
     */
    @RequiresPermissions("sys:user:agent:list")
    @RequestMapping("findAgentUsersByPage")
    public @ResponseBody Pager<AgentUserResponseDto> findAgentUsersByPage(AgentUserRequestDto requestDto) {
        return agentUserService.findAgentUsersByPage(requestDto);
    }

    /**
     * 重置密码
     *
     * @param loginName
     */
    @RequiresPermissions("sys:user:agent:resetPwd")
    @RequestMapping(value = "resetPwd", method = RequestMethod.POST)
    public @ResponseBody BaseResult resetPwd(String loginName) {
        return agentUserService.resetPwd(loginName);
    }

    /**
     * 删除服务商用户
     *
     * @param userId
     * @return
     */
    @RequiresPermissions("sys:user:agent:delete")
    @RequestMapping(value = "deleteAgentUser", method = RequestMethod.POST)
    public @ResponseBody BaseResult deleteAgentUser(String userId) {
        return agentUserService.deleteAgentUser(userId);
    }

    /**
     * 修改服务商管理员用户的密码
     * 
     * @param originalPwd
     * @param newPwd
     * @param confirmPwd
     * @return
     */
    @RequestMapping(value = "modifyAgentUserPwd", method = RequestMethod.POST)
    public @ResponseBody BaseResult modifyAgentUserPwd(String originalPwd, String newPwd, String confirmPwd) {
        BaseResult result = null;
        try {
            result = agentUserService.modifyAgentUserPwd(originalPwd, newPwd, confirmPwd);
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
            return new SystemErrorResult(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("其他错误！", e);
            return new SystemErrorResult();
        }
        return result;
    }

    /**
     * 检查ERP LDAP和服务商用户LDAP上是否已存在指定登录账号
     * 
     * @param loginName
     * @return true: 已存在，false: 不存在
     */
    @RequestMapping("checkNameExists")
    public @ResponseBody Boolean checkNameExists(String loginName) {
        return userService.checkNameExists(loginName);
    }

    /**
     * 查找服务商下的运营经理
     *
     * @return
     */
    @RequestMapping("getAgentOpsManager")
    public @ResponseBody UserRoleResponseDto getAgentOpsManager(Integer agentId) {
        return agentUserService.getAgentOpsManager(agentId);
    }

    /**
     * 创建服务商用户/员工
     * 
     * @param saveDto
     * @return
     */
    @RequiresPermissions("sys:user:agent:create")
    @RequestMapping(value = "agent/create", method = RequestMethod.POST)
    public @ResponseBody BaseResult create(@RequestBody AgentUserSaveDto saveDto) {
        return save(saveDto);
    }

    /**
     * 修改服务商用户/员工
     *
     * @param saveDto
     * @return
     */
    @RequiresPermissions("sys:user:agent:update")
    @RequestMapping(value = "agent/update", method = RequestMethod.POST)
    public @ResponseBody BaseResult update(@RequestBody AgentUserSaveDto saveDto) {
        return save(saveDto);
    }

    /**
     * 保存服务商用户信息
     * 
     * @param saveDto
     * @return
     */
    public BaseResult save(AgentUserSaveDto saveDto) {
        BaseResult baseResult;
        String msg = "保存服务商用户出错！{}";
        try {
            baseResult = agentUserService.save(saveDto);
        } catch (ServiceException e) {
            String message = e.getMessage();
            logger.error(msg, message, e);
            return new ServiceErrorResult(message);
        } catch (RuntimeException e) {
            logger.error(msg, "其他错误！", e);
            return new SystemErrorResult();
        }
        return baseResult;
    }

    /**
     * 根据数据库数据在LDAP上创建所有服务商及其下所有用户
     * 
     * @return
     */
    @RequestMapping(value = "agent/saveLdapAgentsAndUsers")
    public @ResponseBody BaseResult saveLdapAgentsAndUsers() {
        agentUserLdapService.saveLdapAgentsAndUsers();
        return new BaseResult();
    }

}
