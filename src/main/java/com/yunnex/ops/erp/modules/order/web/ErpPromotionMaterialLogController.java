package com.yunnex.ops.erp.modules.order.web;

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
import com.yunnex.ops.erp.modules.order.entity.ErpPromotionMaterialLog;
import com.yunnex.ops.erp.modules.order.service.ErpPromotionMaterialLogService;

/**
 * 推广资料操作日志Controller
 * @author yunnex
 * @version 2018-05-09
 */
@Controller
@RequestMapping(value = "${adminPath}/order/erpPromotionMaterialLog")
public class ErpPromotionMaterialLogController extends BaseController {

	@Autowired
	private ErpPromotionMaterialLogService erpPromotionMaterialLogService;
	
	@ModelAttribute
	public ErpPromotionMaterialLog get(@RequestParam(required=false) String id) {
		ErpPromotionMaterialLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpPromotionMaterialLogService.get(id);
		}
		if (entity == null){
			entity = new ErpPromotionMaterialLog();
		}
		return entity;
	}
	
	@RequiresPermissions("order:erpPromotionMaterialLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpPromotionMaterialLog erpPromotionMaterialLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpPromotionMaterialLog> page = erpPromotionMaterialLogService.findPage(new Page<ErpPromotionMaterialLog>(request, response), erpPromotionMaterialLog); 
		model.addAttribute("page", page);
		return "modules/order/erpPromotionMaterialLogList";
	}

	@RequiresPermissions("order:erpPromotionMaterialLog:view")
	@RequestMapping(value = "form")
	public String form(ErpPromotionMaterialLog erpPromotionMaterialLog, Model model) {
		model.addAttribute("erpPromotionMaterialLog", erpPromotionMaterialLog);
		return "modules/order/erpPromotionMaterialLogForm";
	}

	@RequiresPermissions("order:erpPromotionMaterialLog:edit")
	@RequestMapping(value = "save")
	public String save(ErpPromotionMaterialLog erpPromotionMaterialLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpPromotionMaterialLog)){
			return form(erpPromotionMaterialLog, model);
		}
		erpPromotionMaterialLogService.save(erpPromotionMaterialLog);
		addMessage(redirectAttributes, "保存推广资料操作日志成功");
		return "redirect:"+Global.getAdminPath()+"/order/erpPromotionMaterialLog/?repage";
	}
	
	@RequiresPermissions("order:erpPromotionMaterialLog:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpPromotionMaterialLog erpPromotionMaterialLog, RedirectAttributes redirectAttributes) {
		erpPromotionMaterialLogService.delete(erpPromotionMaterialLog);
		addMessage(redirectAttributes, "删除推广资料操作日志成功");
		return "redirect:"+Global.getAdminPath()+"/order/erpPromotionMaterialLog/?repage";
	}



}