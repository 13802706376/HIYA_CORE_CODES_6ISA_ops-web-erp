package com.yunnex.ops.erp.modules.agent.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.agent.entity.SysServiceOperationManager;
import com.yunnex.ops.erp.modules.agent.service.SysServiceOperationManagerService;

/**
 * 服务商运营经关系对应表Controller
 * @author hanhan
 * @version 2018-06-01
 */
@Controller
@RequestMapping(value = "${adminPath}/agent/sysServiceOperationManager")
public class SysServiceOperationManagerController extends BaseController {

	@Autowired
	private SysServiceOperationManagerService sysServiceOperationManagerService;
	
	@ModelAttribute
	public SysServiceOperationManager get(@RequestParam(required=false) String id) {
		SysServiceOperationManager entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysServiceOperationManagerService.get(id);
		}
		if (entity == null){
			entity = new SysServiceOperationManager();
		}
		return entity;
	}
	
	@RequiresPermissions("agent:sysServiceOperationManager:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysServiceOperationManager sysServiceOperationManager, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysServiceOperationManager> page = sysServiceOperationManagerService.findPage(new Page<SysServiceOperationManager>(request, response), sysServiceOperationManager); 
		model.addAttribute("page", page);
		return "modules/agent/sysServiceOperationManagerList";
	}

	@RequiresPermissions("agent:sysServiceOperationManager:view")
	@RequestMapping(value = "form")
	public String form(SysServiceOperationManager sysServiceOperationManager, Model model) {
		model.addAttribute("sysServiceOperationManager", sysServiceOperationManager);
		return "modules/agent/sysServiceOperationManagerForm";
	}

	@RequiresPermissions("agent:sysServiceOperationManager:edit")
	@RequestMapping(value = "save")
	public String save(SysServiceOperationManager sysServiceOperationManager, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysServiceOperationManager)){
			return form(sysServiceOperationManager, model);
		}
		sysServiceOperationManagerService.save(sysServiceOperationManager);
		addMessage(redirectAttributes, "保存服务商运营经关系对应表成功");
		return "redirect:"+Global.getAdminPath()+"/agent/sysServiceOperationManager/?repage";
	}
	
	@RequiresPermissions("agent:sysServiceOperationManager:edit")
	@RequestMapping(value = "delete")
	public String delete(SysServiceOperationManager sysServiceOperationManager, RedirectAttributes redirectAttributes) {
		sysServiceOperationManagerService.delete(sysServiceOperationManager);
		addMessage(redirectAttributes, "删除服务商运营经关系对应表成功");
		return "redirect:"+Global.getAdminPath()+"/agent/sysServiceOperationManager/?repage";
	}

}