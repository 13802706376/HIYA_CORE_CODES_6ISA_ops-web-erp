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
import com.yunnex.ops.erp.modules.qualify.entity.ErpShopPayQualify;
import com.yunnex.ops.erp.modules.qualify.service.ErpShopPayQualifyService;

/**
 * 商户支付资质Controller
 * @author huanghaidong
 * @version 2017-10-24
 */
@Controller
@RequestMapping(value = "${adminPath}/qualify/erpShopPayQualify")
public class ErpShopPayQualifyController extends BaseController {

	@Autowired
	private ErpShopPayQualifyService erpShopPayQualifyService;
	
	@ModelAttribute
	public ErpShopPayQualify get(@RequestParam(required=false) String id) {
		ErpShopPayQualify entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpShopPayQualifyService.get(id);
		}
		if (entity == null){
			entity = new ErpShopPayQualify();
		}
		return entity;
	}
	
	@RequiresPermissions("qualify:erpShopPayQualify:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpShopPayQualify erpShopPayQualify, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpShopPayQualify> page = erpShopPayQualifyService.findPage(new Page<ErpShopPayQualify>(request, response), erpShopPayQualify); 
		model.addAttribute("page", page);
		return "modules/qualify/erpShopPayQualifyList";
	}

	@RequiresPermissions("qualify:erpShopPayQualify:view")
	@RequestMapping(value = "form")
	public String form(ErpShopPayQualify erpShopPayQualify, Model model) {
		model.addAttribute("erpShopPayQualify", erpShopPayQualify);
		return "modules/qualify/erpShopPayQualifyForm";
	}

	@RequiresPermissions("qualify:erpShopPayQualify:edit")
	@RequestMapping(value = "save")
	public String save(ErpShopPayQualify erpShopPayQualify, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpShopPayQualify)){
			return form(erpShopPayQualify, model);
		}
		erpShopPayQualifyService.save(erpShopPayQualify);
		addMessage(redirectAttributes, "保存商户支付资质成功");
		return "redirect:"+Global.getAdminPath()+"/qualify/erpShopPayQualify/?repage";
	}
	
	@RequiresPermissions("qualify:erpShopPayQualify:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpShopPayQualify erpShopPayQualify, RedirectAttributes redirectAttributes) {
		erpShopPayQualifyService.delete(erpShopPayQualify);
		addMessage(redirectAttributes, "删除商户支付资质成功");
		return "redirect:"+Global.getAdminPath()+"/qualify/erpShopPayQualify/?repage";
	}

}