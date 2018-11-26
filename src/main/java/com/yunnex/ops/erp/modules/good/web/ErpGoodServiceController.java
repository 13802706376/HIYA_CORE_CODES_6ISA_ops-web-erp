package com.yunnex.ops.erp.modules.good.web;

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
import com.yunnex.ops.erp.modules.good.entity.ErpGoodService;
import com.yunnex.ops.erp.modules.good.service.ErpGoodServiceService;

/**
 * 商品服务Controller
 * @author yunnex
 * @version 2018-05-29
 */
@Controller
@RequestMapping(value = "${adminPath}/good/erpGoodService")
public class ErpGoodServiceController extends BaseController {

	@Autowired
	private ErpGoodServiceService erpGoodServiceService;
	
	@ModelAttribute
	public ErpGoodService get(@RequestParam(required=false) String id) {
		ErpGoodService entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpGoodServiceService.get(id);
		}
		if (entity == null){
			entity = new ErpGoodService();
		}
		return entity;
	}
	
	@RequiresPermissions("good:erpGoodService:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpGoodService erpGoodService, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpGoodService> page = erpGoodServiceService.findPage(new Page<ErpGoodService>(request, response), erpGoodService); 
		model.addAttribute("page", page);
		return "modules/good/erpGoodServiceList";
	}

	@RequiresPermissions("good:erpGoodService:view")
	@RequestMapping(value = "form")
	public String form(ErpGoodService erpGoodService, Model model) {
		model.addAttribute("erpGoodService", erpGoodService);
		return "modules/good/erpGoodServiceForm";
	}

	@RequiresPermissions("good:erpGoodService:edit")
	@RequestMapping(value = "save")
	public String save(ErpGoodService erpGoodService, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpGoodService)){
			return form(erpGoodService, model);
		}
		erpGoodServiceService.save(erpGoodService);
		addMessage(redirectAttributes, "保存商品服务成功");
		return "redirect:"+Global.getAdminPath()+"/good/erpGoodService/?repage";
	}
	
	@RequiresPermissions("good:erpGoodService:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpGoodService erpGoodService, RedirectAttributes redirectAttributes) {
		erpGoodServiceService.delete(erpGoodService);
		addMessage(redirectAttributes, "删除商品服务成功");
		return "redirect:"+Global.getAdminPath()+"/good/erpGoodService/?repage";
	}
}