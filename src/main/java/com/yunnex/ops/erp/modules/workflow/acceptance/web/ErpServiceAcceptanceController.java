package com.yunnex.ops.erp.modules.workflow.acceptance.web;

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
import com.yunnex.ops.erp.modules.workflow.acceptance.entity.ErpServiceAcceptance;
import com.yunnex.ops.erp.modules.workflow.acceptance.service.ErpServiceAcceptanceService;

/**
 * 服务验收评价Controller
 * @author yunnex
 * @version 2018-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/workflow.acceptance/erpServiceAcceptance")
public class ErpServiceAcceptanceController extends BaseController {

	@Autowired
	private ErpServiceAcceptanceService erpServiceAcceptanceService;
	
	@ModelAttribute
	public ErpServiceAcceptance get(@RequestParam(required=false) String id) {
		ErpServiceAcceptance entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpServiceAcceptanceService.get(id);
		}
		if (entity == null){
			entity = new ErpServiceAcceptance();
		}
		return entity;
	}
	
	@RequiresPermissions("workflow.acceptance:erpServiceAcceptance:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpServiceAcceptance erpServiceAcceptance, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpServiceAcceptance> page = erpServiceAcceptanceService.findPage(new Page<ErpServiceAcceptance>(request, response), erpServiceAcceptance); 
		model.addAttribute("page", page);
		return "modules/workflow.acceptance/erpServiceAcceptanceList";
	}

	@RequiresPermissions("workflow.acceptance:erpServiceAcceptance:view")
	@RequestMapping(value = "form")
	public String form(ErpServiceAcceptance erpServiceAcceptance, Model model) {
		model.addAttribute("erpServiceAcceptance", erpServiceAcceptance);
		return "modules/workflow.acceptance/erpServiceAcceptanceForm";
	}

	@RequiresPermissions("workflow.acceptance:erpServiceAcceptance:edit")
	@RequestMapping(value = "save")
	public String save(ErpServiceAcceptance erpServiceAcceptance, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpServiceAcceptance)){
			return form(erpServiceAcceptance, model);
		}
		erpServiceAcceptanceService.save(erpServiceAcceptance);
		addMessage(redirectAttributes, "保存服务验收评价成功");
		return "redirect:"+Global.getAdminPath()+"/workflow.acceptance/erpServiceAcceptance/?repage";
	}
	
	@RequiresPermissions("workflow.acceptance:erpServiceAcceptance:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpServiceAcceptance erpServiceAcceptance, RedirectAttributes redirectAttributes) {
		erpServiceAcceptanceService.delete(erpServiceAcceptance);
		addMessage(redirectAttributes, "删除服务验收评价成功");
		return "redirect:"+Global.getAdminPath()+"/workflow.acceptance/erpServiceAcceptance/?repage";
	}

}