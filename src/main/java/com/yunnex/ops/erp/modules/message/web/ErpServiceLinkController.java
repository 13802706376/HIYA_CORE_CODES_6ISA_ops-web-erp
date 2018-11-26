package com.yunnex.ops.erp.modules.message.web;

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
import com.yunnex.ops.erp.modules.message.entity.ErpServiceLink;
import com.yunnex.ops.erp.modules.message.service.ErpServiceLinkService;

/**
 * 交互入口配置表Controller
 * @author yunnex
 * @version 2018-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/message/erpServiceLink")
public class ErpServiceLinkController extends BaseController {

	@Autowired
	private ErpServiceLinkService erpServiceLinkService;
	
	@ModelAttribute
	public ErpServiceLink get(@RequestParam(required=false) String id) {
		ErpServiceLink entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpServiceLinkService.get(id);
		}
		if (entity == null){
			entity = new ErpServiceLink();
		}
		return entity;
	}
	
	@RequiresPermissions("message:erpServiceLink:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpServiceLink erpServiceLink, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpServiceLink> page = erpServiceLinkService.findPage(new Page<ErpServiceLink>(request, response), erpServiceLink); 
		model.addAttribute("page", page);
		return "modules/message/erpServiceLinkList";
	}

	@RequiresPermissions("message:erpServiceLink:view")
	@RequestMapping(value = "form")
	public String form(ErpServiceLink erpServiceLink, Model model) {
		model.addAttribute("erpServiceLink", erpServiceLink);
		return "modules/message/erpServiceLinkForm";
	}

	@RequiresPermissions("message:erpServiceLink:edit")
	@RequestMapping(value = "save")
	public String save(ErpServiceLink erpServiceLink, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpServiceLink)){
			return form(erpServiceLink, model);
		}
		erpServiceLinkService.save(erpServiceLink);
		addMessage(redirectAttributes, "保存交互入口配置表成功");
		return "redirect:"+Global.getAdminPath()+"/message/erpServiceLink/?repage";
	}
	
	@RequiresPermissions("message:erpServiceLink:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpServiceLink erpServiceLink, RedirectAttributes redirectAttributes) {
		erpServiceLinkService.delete(erpServiceLink);
		addMessage(redirectAttributes, "删除交互入口配置表成功");
		return "redirect:"+Global.getAdminPath()+"/message/erpServiceLink/?repage";
	}

}