package com.yunnex.ops.erp.modules.store.basic.web;

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
import com.yunnex.ops.erp.modules.store.basic.entity.BusinessScope;
import com.yunnex.ops.erp.modules.store.basic.service.BusinessScopeService;
import com.yunnex.ops.erp.common.utils.StringUtils;

/**
 * 经营范围Controller
 * @author a
 * @version 2017-12-19
 */
@Controller
@RequestMapping(value = "${adminPath}/store/basic/businessScope")
public class BusinessScopeController extends BaseController {

	@Autowired
	private BusinessScopeService businessScopeService;
	
	@ModelAttribute
	public BusinessScope get(@RequestParam(required=false) String id) {
		BusinessScope entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = businessScopeService.get(id);
		}
		if (entity == null){
			entity = new BusinessScope();
		}
		return entity;
	}
	
	/**
	 * 显示list方法
	 * @author a
	 * @version 2017-12-19
	 */
	@RequiresPermissions("store:basic:businessScope:view")
	@RequestMapping(value = {"list", ""})
	public String list(BusinessScope businessScope, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BusinessScope> page = businessScopeService.findPage(new Page<BusinessScope>(request, response), businessScope); 
		model.addAttribute("page", page);
		return "modules/store/basic/businessScopeList";
	}

	@RequiresPermissions("store:basic:businessScope:view")
	@RequestMapping(value = "form")
	public String form(BusinessScope businessScope, Model model) {
		model.addAttribute("businessScope", businessScope);
		return "modules/store/basic/businessScopeForm";
	}

	@RequiresPermissions("store:basic:businessScope:edit")
	@RequestMapping(value = "save")
	public String save(BusinessScope businessScope, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, businessScope)){
			return form(businessScope, model);
		}
		businessScopeService.save(businessScope);
		addMessage(redirectAttributes, "保存经营范围成功");
		return "redirect:"+Global.getAdminPath()+"/store/basic/businessScope/?repage";
	}
	
	@RequiresPermissions("store:basic:businessScope:edit")
	@RequestMapping(value = "delete")
	public String delete(BusinessScope businessScope, RedirectAttributes redirectAttributes) {
		businessScopeService.delete(businessScope);
		addMessage(redirectAttributes, "删除经营范围成功");
		return "redirect:"+Global.getAdminPath()+"/store/basic/businessScope/?repage";
	}

}