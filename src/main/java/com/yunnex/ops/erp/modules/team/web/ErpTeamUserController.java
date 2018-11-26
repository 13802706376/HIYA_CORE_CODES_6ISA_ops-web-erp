package com.yunnex.ops.erp.modules.team.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.team.entity.ErpTeamUser;
import com.yunnex.ops.erp.modules.team.service.ErpTeamUserService;

/**
 * 成员Controller
 * 
 * @author huanghaidong
 * @version 2017-10-26
 */
@Controller
@RequestMapping(value = "${adminPath}/team/erpTeamUser")
public class ErpTeamUserController extends BaseController {

	@Autowired
	private ErpTeamUserService erpTeamUserService;
	
	@ModelAttribute
	public ErpTeamUser get(@RequestParam(required=false) String id) {
		ErpTeamUser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpTeamUserService.get(id);
		}
		if (entity == null){
			entity = new ErpTeamUser();
		}
		return entity;
	}
	
	@RequiresPermissions("team:erpTeamUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpTeamUser erpTeamUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpTeamUser> page = erpTeamUserService.findPage(new Page<ErpTeamUser>(request, response), erpTeamUser); 
		model.addAttribute("page", page);
		return "modules/team/erpTeamUserList";
	}

	@RequiresPermissions("team:erpTeamUser:view")
	@RequestMapping(value = "form")
	public String form(ErpTeamUser erpTeamUser, Model model) {
		model.addAttribute("erpTeamUser", erpTeamUser);
		return "modules/team/erpTeamUserForm";
	}

	@RequiresPermissions("team:erpTeamUser:edit")
	@RequestMapping(value = "save")
	public String save(ErpTeamUser erpTeamUser, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpTeamUser)){
			return form(erpTeamUser, model);
		}
		erpTeamUserService.save(erpTeamUser);
        addMessage(redirectAttributes, "保存成员成功");
		return "redirect:"+Global.getAdminPath()+"/team/erpTeamUser/?repage";
	}
	
	@RequiresPermissions("team:erpTeamUser:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpTeamUser erpTeamUser, RedirectAttributes redirectAttributes) {
		erpTeamUserService.delete(erpTeamUser);
        addMessage(redirectAttributes, "删除成员成功");
		return "redirect:"+Global.getAdminPath()+"/team/erpTeamUser/?repage";
	}

    /**
     * 业务定义：根据团队ID查询对应用户信息
     * 
     * @date 2018年7月5日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "findListByTeamId")
    public Object findListByTeamId(String teamId) {
        return erpTeamUserService.findListByTeamId(teamId);
    }

}