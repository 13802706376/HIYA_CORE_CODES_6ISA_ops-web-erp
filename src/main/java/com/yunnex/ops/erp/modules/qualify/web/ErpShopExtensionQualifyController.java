package com.yunnex.ops.erp.modules.qualify.web;

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
import com.yunnex.ops.erp.modules.qualify.entity.ErpShopExtensionQualify;
import com.yunnex.ops.erp.modules.qualify.service.ErpShopExtensionQualifyService;

/**
 * 商户推广资质Controller
 * @author huanghaidong
 * @version 2017-10-24
 */
@Controller
@RequestMapping(value = "${adminPath}/qualify/erpShopExtensionQualify")
public class ErpShopExtensionQualifyController extends BaseController {

	@Autowired
	private ErpShopExtensionQualifyService erpShopExtensionQualifyService;
	
	@ModelAttribute
	public ErpShopExtensionQualify get(@RequestParam(required=false) String id) {
		ErpShopExtensionQualify entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpShopExtensionQualifyService.get(id);
		}
		if (entity == null){
			entity = new ErpShopExtensionQualify();
		}
		return entity;
	}
	
	@RequiresPermissions("qualify:erpShopExtensionQualify:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpShopExtensionQualify erpShopExtensionQualify, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpShopExtensionQualify> page = erpShopExtensionQualifyService.findPage(new Page<ErpShopExtensionQualify>(request, response), erpShopExtensionQualify); 
		model.addAttribute("page", page);
		return "modules/qualify/erpShopExtensionQualifyList";
	}

	@RequiresPermissions("qualify:erpShopExtensionQualify:view")
	@RequestMapping(value = "form")
	public String form(ErpShopExtensionQualify erpShopExtensionQualify, Model model) {
		model.addAttribute("erpShopExtensionQualify", erpShopExtensionQualify);
		return "modules/qualify/erpShopExtensionQualifyForm";
	}

	@RequiresPermissions("qualify:erpShopExtensionQualify:edit")
	@RequestMapping(value = "save")
	public String save(ErpShopExtensionQualify erpShopExtensionQualify, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpShopExtensionQualify)){
			return form(erpShopExtensionQualify, model);
		}
		erpShopExtensionQualifyService.save(erpShopExtensionQualify);
		addMessage(redirectAttributes, "保存商户推广资质成功");
		return "redirect:"+Global.getAdminPath()+"/qualify/erpShopExtensionQualify/?repage";
	}
	
	@RequiresPermissions("qualify:erpShopExtensionQualify:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpShopExtensionQualify erpShopExtensionQualify, RedirectAttributes redirectAttributes) {
		erpShopExtensionQualifyService.delete(erpShopExtensionQualify);
		addMessage(redirectAttributes, "删除商户推广资质成功");
		return "redirect:"+Global.getAdminPath()+"/qualify/erpShopExtensionQualify/?repage";
	}

}