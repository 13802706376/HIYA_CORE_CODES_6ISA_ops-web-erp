package com.yunnex.ops.erp.modules.team.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.agent.entity.SysServiceOperationManager;
import com.yunnex.ops.erp.modules.agent.service.SysServiceOperationManagerService;
import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.sys.entity.User;
import com.yunnex.ops.erp.modules.sys.service.SystemService;
import com.yunnex.ops.erp.modules.team.entity.ErpTeam;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamUser;
import com.yunnex.ops.erp.modules.team.service.ErpTeamService;
import com.yunnex.ops.erp.modules.team.service.ErpTeamUserService;

/**
 * 团队Controller
 * 
 * @author huanghaidong
 * @version 2017-10-26
 */
@Controller
@RequestMapping(value = "${adminPath}/team/erpTeam")
public class ErpTeamController extends BaseController {

    @Autowired
    private ErpTeamService erpTeamService;

    @Autowired
    private ErpTeamUserService erpTeamUserService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private SysServiceOperationManagerService serviceOperationManagerService;

    @ModelAttribute
    public ErpTeam get(@RequestParam(required = false) String id) {
        ErpTeam entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpTeamService.get(id);
        }
        if (entity == null) {
            entity = new ErpTeam();
        }
        return entity;
    }

    @RequiresPermissions("team:erpTeam:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpTeam erpTeam, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ErpTeam> page = erpTeamService.findPage(new Page<ErpTeam>(request, response), erpTeam);
        model.addAttribute("page", page);
        return "modules/team/erpTeamList";
    }

    @RequiresPermissions("team:erpTeam:view")
    @RequestMapping(value = "form")
    public String form(ErpTeam erpTeam, Model model) {
        List<ErpTeamUser> erpTeamUsers = erpTeamUserService.findListByTeamId(erpTeam.getId());
        StringBuilder leaderNameBuilder = new StringBuilder("");
        StringBuilder memberNameBuilder = new StringBuilder("");
        StringBuilder leaderIdsBuilder = new StringBuilder("");
        StringBuilder memberIdsBuilder = new StringBuilder("");
        if (CollectionUtils.isNotEmpty(erpTeamUsers)) {
            for (ErpTeamUser erpTeamUser : erpTeamUsers) {
                if (erpTeamUser.getLeaderFlag() == ErpTeamUser.LEADER_FLAG_NO) {
                    memberNameBuilder.append(",").append(erpTeamUser.getUserName());
                    memberIdsBuilder.append(",").append(erpTeamUser.getUserId());
                } else if (erpTeamUser.getLeaderFlag() == ErpTeamUser.LEADER_FLAG_YES) {
                    leaderNameBuilder.append(",").append(erpTeamUser.getUserName());
                    leaderIdsBuilder.append(",").append(erpTeamUser.getUserId());
                }
            }
        }
        List<SysServiceOperationManager> branches = serviceOperationManagerService.findList(new SysServiceOperationManager());
        model.addAttribute("erpTeam", erpTeam)
                        .addAttribute("leaderName", leaderNameBuilder.length() == 0 ? leaderNameBuilder.toString() : leaderNameBuilder.substring(1))
                        .addAttribute("memberName", memberNameBuilder.length() == 0 ? memberNameBuilder.toString() : memberNameBuilder.substring(1))
                        .addAttribute("leaderIds", leaderIdsBuilder.length() == 0 ? leaderIdsBuilder.toString() : leaderIdsBuilder.substring(1))
                        .addAttribute("memberIds", memberIdsBuilder.length() == 0 ? memberIdsBuilder.toString() : memberIdsBuilder.substring(1))
                        .addAttribute("branches", branches);
        return "modules/team/erpTeamForm";
    }

    @RequiresPermissions("team:erpTeam:edit")
    @RequestMapping(value = "save")
    public String save(ErpTeam erpTeam, Model model, RedirectAttributes redirectAttributes,
                    @RequestParam(required = true, value = "teamLeaderIds") String teamLeaderIds,
                    @RequestParam(required = true, value = "teamMemberIds") String teamMemberIds) {
        if (!beanValidator(model, erpTeam)) {
            return form(erpTeam, model);
        }
        erpTeamService.saveWithMemberAndLeader(erpTeam, teamLeaderIds, teamMemberIds);
        addMessage(redirectAttributes, "保存团队成功");
        return "redirect:" + Global.getAdminPath() + "/team/erpTeam/?repage";
    }

    @RequiresPermissions("team:erpTeam:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpTeam erpTeam, RedirectAttributes redirectAttributes) {
        erpTeamService.delete(erpTeam);
        addMessage(redirectAttributes, "删除团队成功");
        return "redirect:" + Global.getAdminPath() + "/team/erpTeam/?repage";
    }

    @RequiresUser
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(HttpServletResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        List<Map<String, Object>> mapList = Lists.newArrayList();
        User user = new User();
        user.setType(SysConstant.TYPE_ERP);
        List<User> list = systemService.findUser(user);
        for (int i = 0; i < list.size(); i++) {
            User e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("name", e.getName());
            mapList.add(map);
        }
        return mapList;
    }

}
