package com.yunnex.ops.erp.modules.sys.web;

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
import com.yunnex.ops.erp.modules.sys.entity.SysOfficeUser;
import com.yunnex.ops.erp.modules.sys.service.SysOfficeUserService;

/**
 * 人员部门关联关系Controller
 * @author 林群植
 * @version 2018-10-18
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysOfficeUser")
public class SysOfficeUserController extends BaseController {

	@Autowired
	private SysOfficeUserService sysOfficeUserService;
	
	@ModelAttribute
	public SysOfficeUser get(@RequestParam(required=false) String id) {
		SysOfficeUser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysOfficeUserService.get(id);
		}
		if (entity == null){
			entity = new SysOfficeUser();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:sysOfficeUser:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysOfficeUser sysOfficeUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysOfficeUser> page = sysOfficeUserService.findPage(new Page<SysOfficeUser>(request, response), sysOfficeUser); 
		model.addAttribute("page", page);
		return "modules/sys/sysOfficeUserList";
	}

	@RequiresPermissions("sys:sysOfficeUser:view")
	@RequestMapping(value = "form")
	public String form(SysOfficeUser sysOfficeUser, Model model) {
		model.addAttribute("sysOfficeUser", sysOfficeUser);
		return "modules/sys/sysOfficeUserForm";
	}

	@RequiresPermissions("sys:sysOfficeUser:edit")
	@RequestMapping(value = "save")
	public String save(SysOfficeUser sysOfficeUser, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysOfficeUser)){
			return form(sysOfficeUser, model);
		}
		sysOfficeUserService.save(sysOfficeUser);
		addMessage(redirectAttributes, "保存人员部门关联关系成功");
		return "redirect:"+Global.getAdminPath()+"/sys/sysOfficeUser/?repage";
	}
	
	@RequiresPermissions("sys:sysOfficeUser:edit")
	@RequestMapping(value = "delete")
	public String delete(SysOfficeUser sysOfficeUser, RedirectAttributes redirectAttributes) {
		sysOfficeUserService.delete(sysOfficeUser);
		addMessage(redirectAttributes, "删除人员部门关联关系成功");
		return "redirect:"+Global.getAdminPath()+"/sys/sysOfficeUser/?repage";
	}

}