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
import com.yunnex.ops.erp.modules.schedule.entity.ErpHisSplitValue;
import com.yunnex.ops.erp.modules.schedule.service.ErpHisSplitValueService;

/**
 * 生产进度小程序子表Controller
 * @author pengchenghe
 * @version 2018-01-19
 * 
 * 对应父表值
 */
@Controller
@RequestMapping(value = "${adminPath}/schedule/erpHisSplitValue")
public class ErpHisSplitValueController extends BaseController {

	@Autowired
	private ErpHisSplitValueService erpHisSplitValueService;
	
	@ModelAttribute
	public ErpHisSplitValue get(@RequestParam(required=false) String id) {
		ErpHisSplitValue entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpHisSplitValueService.get(id);
		}
		if (entity == null){
			entity = new ErpHisSplitValue();
		}
		return entity;
	}
	
	@RequiresPermissions("schedule:erpHisSplitValue:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpHisSplitValue erpHisSplitValue, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpHisSplitValue> page = erpHisSplitValueService.findPage(new Page<ErpHisSplitValue>(request, response), erpHisSplitValue); 
		model.addAttribute("page", page);
		return "modules/schedule/erpHisSplitValueList";
	}

	@RequiresPermissions("schedule:erpHisSplitValue:view")
	@RequestMapping(value = "form")
	public String form(ErpHisSplitValue erpHisSplitValue, Model model) {
		model.addAttribute("erpHisSplitValue", erpHisSplitValue);
		return "modules/schedule/erpHisSplitValueForm";
	}

	@RequiresPermissions("schedule:erpHisSplitValue:edit")
	@RequestMapping(value = "save")
	public String save(ErpHisSplitValue erpHisSplitValue, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpHisSplitValue)){
			return form(erpHisSplitValue, model);
		}
		erpHisSplitValueService.save(erpHisSplitValue);
		addMessage(redirectAttributes, "保存生产进度小程序子表成功");
		return "redirect:"+Global.getAdminPath()+"/schedule/erpHisSplitValue/?repage";
	}
	
	@RequiresPermissions("schedule:erpHisSplitValue:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpHisSplitValue erpHisSplitValue, RedirectAttributes redirectAttributes) {
		erpHisSplitValueService.delete(erpHisSplitValue);
		addMessage(redirectAttributes, "删除生产进度小程序子表成功");
		return "redirect:"+Global.getAdminPath()+"/schedule/erpHisSplitValue/?repage";
	}

}