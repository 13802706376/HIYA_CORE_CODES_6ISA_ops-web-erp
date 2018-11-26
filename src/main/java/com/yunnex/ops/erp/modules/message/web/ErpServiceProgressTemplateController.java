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
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceProgressTemplate;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressTemplateService;

/**
 * 服务进度模板表Controller
 * @author yunnex
 * @version 2018-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/message/erpServiceScheduleTemplate")
public class ErpServiceProgressTemplateController extends BaseController {

	@Autowired
	private ErpServiceProgressTemplateService erpServiceScheduleTemplateService;
	
	@ModelAttribute
	public ErpServiceProgressTemplate get(@RequestParam(required=false) String id) {
		ErpServiceProgressTemplate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpServiceScheduleTemplateService.get(id);
		}
		if (entity == null){
			entity = new ErpServiceProgressTemplate();
		}
		return entity;
	}
	
	@RequiresPermissions("message:erpServiceScheduleTemplate:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpServiceProgressTemplate erpServiceScheduleTemplate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpServiceProgressTemplate> page = erpServiceScheduleTemplateService.findPage(new Page<ErpServiceProgressTemplate>(request, response), erpServiceScheduleTemplate); 
		model.addAttribute("page", page);
		return "modules/message/erpServiceScheduleTemplateList";
	}

	@RequiresPermissions("message:erpServiceScheduleTemplate:view")
	@RequestMapping(value = "form")
	public String form(ErpServiceProgressTemplate erpServiceScheduleTemplate, Model model) {
		model.addAttribute("erpServiceScheduleTemplate", erpServiceScheduleTemplate);
		return "modules/message/erpServiceScheduleTemplateForm";
	}

	@RequiresPermissions("message:erpServiceScheduleTemplate:edit")
	@RequestMapping(value = "save")
	public String save(ErpServiceProgressTemplate erpServiceScheduleTemplate, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpServiceScheduleTemplate)){
			return form(erpServiceScheduleTemplate, model);
		}
		erpServiceScheduleTemplateService.save(erpServiceScheduleTemplate);
		addMessage(redirectAttributes, "保存服务进度模板表成功");
		return "redirect:"+Global.getAdminPath()+"/message/erpServiceScheduleTemplate/?repage";
	}
	
	@RequiresPermissions("message:erpServiceScheduleTemplate:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpServiceProgressTemplate erpServiceScheduleTemplate, RedirectAttributes redirectAttributes) {
		erpServiceScheduleTemplateService.delete(erpServiceScheduleTemplate);
		addMessage(redirectAttributes, "删除服务进度模板表成功");
		return "redirect:"+Global.getAdminPath()+"/message/erpServiceScheduleTemplate/?repage";
	}

}