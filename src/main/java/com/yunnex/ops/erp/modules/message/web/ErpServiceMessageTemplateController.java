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
import com.yunnex.ops.erp.modules.message.entity.ErpServiceMessageTemplate;
import com.yunnex.ops.erp.modules.message.service.ErpServiceMessageTemplateService;

/**
 * 服务通知模板表Controller
 * @author yunnex
 * @version 2018-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/message/erpServiceMessageTemplate")
public class ErpServiceMessageTemplateController extends BaseController {

	@Autowired
	private ErpServiceMessageTemplateService erpServiceMessageTemplateService;
	
	@ModelAttribute
	public ErpServiceMessageTemplate get(@RequestParam(required=false) String id) {
		ErpServiceMessageTemplate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpServiceMessageTemplateService.get(id);
		}
		if (entity == null){
			entity = new ErpServiceMessageTemplate();
		}
		return entity;
	}
	
	@RequiresPermissions("message:erpServiceMessageTemplate:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpServiceMessageTemplate erpServiceMessageTemplate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpServiceMessageTemplate> page = erpServiceMessageTemplateService.findPage(new Page<ErpServiceMessageTemplate>(request, response), erpServiceMessageTemplate); 
		model.addAttribute("page", page);
		return "modules/message/erpServiceMessageTemplateList";
	}

	@RequiresPermissions("message:erpServiceMessageTemplate:view")
	@RequestMapping(value = "form")
	public String form(ErpServiceMessageTemplate erpServiceMessageTemplate, Model model) {
		model.addAttribute("erpServiceMessageTemplate", erpServiceMessageTemplate);
		return "modules/message/erpServiceMessageTemplateForm";
	}

	@RequiresPermissions("message:erpServiceMessageTemplate:edit")
	@RequestMapping(value = "save")
	public String save(ErpServiceMessageTemplate erpServiceMessageTemplate, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpServiceMessageTemplate)){
			return form(erpServiceMessageTemplate, model);
		}
		erpServiceMessageTemplateService.save(erpServiceMessageTemplate);
		addMessage(redirectAttributes, "保存服务通知模板表成功");
		return "redirect:"+Global.getAdminPath()+"/message/erpServiceMessageTemplate/?repage";
	}
	
	@RequiresPermissions("message:erpServiceMessageTemplate:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpServiceMessageTemplate erpServiceMessageTemplate, RedirectAttributes redirectAttributes) {
		erpServiceMessageTemplateService.delete(erpServiceMessageTemplate);
		addMessage(redirectAttributes, "删除服务通知模板表成功");
		return "redirect:"+Global.getAdminPath()+"/message/erpServiceMessageTemplate/?repage";
	}

}