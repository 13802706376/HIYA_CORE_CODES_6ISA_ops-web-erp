package com.yunnex.ops.erp.modules.schedule.web;

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
import com.yunnex.ops.erp.modules.schedule.entity.ErpHisSplit;
import com.yunnex.ops.erp.modules.schedule.service.ErpHisSplitService;

/**
 * 生产进度小程序父表Controller
 * @author pengchenghe
 * @version 2018-01-19
 * 
 * update 2018-01-19
 */
@Controller
@RequestMapping(value = "${adminPath}/schedule/erpHisSplit")
public class ErpHisSplitController extends BaseController {

	@Autowired
	private ErpHisSplitService erpHisSplitService;
	
	@ModelAttribute
	public ErpHisSplit get(@RequestParam(required=false) String id) {
		ErpHisSplit entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpHisSplitService.get(id);
		}
		if (entity == null){
			entity = new ErpHisSplit();
		}
		return entity;
	}
	
	@RequiresPermissions("schedule:erpHisSplit:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpHisSplit erpHisSplit, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpHisSplit> page = erpHisSplitService.findPage(new Page<ErpHisSplit>(request, response), erpHisSplit); 
		model.addAttribute("page", page);
		return "modules/schedule/erpHisSplitList";
	}

	@RequiresPermissions("schedule:erpHisSplit:view")
	@RequestMapping(value = "form")
	public String form(ErpHisSplit erpHisSplit, Model model) {
		model.addAttribute("erpHisSplit", erpHisSplit);
		return "modules/schedule/erpHisSplitForm";
	}

	@RequiresPermissions("schedule:erpHisSplit:edit")
	@RequestMapping(value = "save")
	public String save(ErpHisSplit erpHisSplit, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpHisSplit)){
			return form(erpHisSplit, model);
		}
		erpHisSplitService.save(erpHisSplit);
		addMessage(redirectAttributes, "保存生产进度小程序父表成功");
		return "redirect:"+Global.getAdminPath()+"/schedule/erpHisSplit/?repage";
	}
	
	@RequiresPermissions("schedule:erpHisSplit:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpHisSplit erpHisSplit, RedirectAttributes redirectAttributes) {
		erpHisSplitService.delete(erpHisSplit);
		addMessage(redirectAttributes, "删除生产进度小程序父表成功");
		return "redirect:"+Global.getAdminPath()+"/schedule/erpHisSplit/?repage";
	}

}