package com.yunnex.ops.erp.modules.shop.pay.web;

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
import com.yunnex.ops.erp.modules.shop.pay.entity.ErpShopPayAlipa;
import com.yunnex.ops.erp.modules.shop.pay.service.ErpShopPayAlipaService;

/**
 * 支付宝口碑Controller
 * @author hanhan
 * @version 2018-05-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pay/erpShopPayAlipa")
public class ErpShopPayAlipaController extends BaseController {

	@Autowired
	private ErpShopPayAlipaService erpShopPayAlipaService;
	
	@ModelAttribute
	public ErpShopPayAlipa get(@RequestParam(required=false) String id) {
		ErpShopPayAlipa entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpShopPayAlipaService.get(id);
		}
		if (entity == null){
			entity = new ErpShopPayAlipa();
		}
		return entity;
	}
	
	@RequiresPermissions("pay:erpShopPayAlipa:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpShopPayAlipa erpShopPayAlipa, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpShopPayAlipa> page = erpShopPayAlipaService.findPage(new Page<ErpShopPayAlipa>(request, response), erpShopPayAlipa); 
		model.addAttribute("page", page);
		return "shop/pay/erpShopPayAlipaList";
	}

	@RequiresPermissions("pay:erpShopPayAlipa:view")
	@RequestMapping(value = "form")
	public String form(ErpShopPayAlipa erpShopPayAlipa, Model model) {
		model.addAttribute("erpShopPayAlipa", erpShopPayAlipa);
		return "shop/pay/erpShopPayAlipaForm";
	}

	@RequiresPermissions("pay:erpShopPayAlipa:edit")
	@RequestMapping(value = "save")
	public String save(ErpShopPayAlipa erpShopPayAlipa, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpShopPayAlipa)){
			return form(erpShopPayAlipa, model);
		}
		erpShopPayAlipaService.save(erpShopPayAlipa);
		addMessage(redirectAttributes, "保存支付宝口碑成功");
		return "redirect:"+Global.getAdminPath()+"/pay/erpShopPayAlipa/?repage";
	}
	
	@RequiresPermissions("pay:erpShopPayAlipa:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpShopPayAlipa erpShopPayAlipa, RedirectAttributes redirectAttributes) {
		erpShopPayAlipaService.delete(erpShopPayAlipa);
		addMessage(redirectAttributes, "删除支付宝口碑成功");
		return "redirect:"+Global.getAdminPath()+"/pay/erpShopPayAlipa/?repage";
	}

}