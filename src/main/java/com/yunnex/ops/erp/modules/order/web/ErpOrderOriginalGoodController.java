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
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalGood;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalGoodService;

/**
 * 订单商品Controller
 * @author huanghaidong
 * @version 2017-10-24
 */
@Controller
@RequestMapping(value = "${adminPath}/order/erpOrderOriginalGood")
public class ErpOrderOriginalGoodController extends BaseController {

	@Autowired
	private ErpOrderOriginalGoodService erpOrderOriginalGoodService;
	
	@ModelAttribute
	public ErpOrderOriginalGood get(@RequestParam(required=false) String id) {
		ErpOrderOriginalGood entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpOrderOriginalGoodService.get(id);
		}
		if (entity == null){
			entity = new ErpOrderOriginalGood();
		}
		return entity;
	}
	
    @RequiresPermissions("order:erpOrderOriginalGood:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpOrderOriginalGood erpOrderOriginalGood, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpOrderOriginalGood> page = erpOrderOriginalGoodService.findPage(new Page<ErpOrderOriginalGood>(request, response), erpOrderOriginalGood); 
		model.addAttribute("page", page);
		return "modules/order/erpOrderOriginalGoodList";
	}

	@RequiresPermissions("order:erpOrderOriginalGood:view")
	@RequestMapping(value = "form")
	public String form(ErpOrderOriginalGood erpOrderOriginalGood, Model model) {
		model.addAttribute("erpOrderOriginalGood", erpOrderOriginalGood);
		return "modules/order/erpOrderOriginalGoodForm";
	}

	@RequiresPermissions("order:erpOrderOriginalGood:edit")
	@RequestMapping(value = "save")
	public String save(ErpOrderOriginalGood erpOrderOriginalGood, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpOrderOriginalGood)){
			return form(erpOrderOriginalGood, model);
		}
		erpOrderOriginalGoodService.save(erpOrderOriginalGood);
		addMessage(redirectAttributes, "保存订单商品成功");
		return "redirect:"+Global.getAdminPath()+"/order/erpOrderOriginalGood/?repage";
	}
	
	@RequiresPermissions("order:erpOrderOriginalGood:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpOrderOriginalGood erpOrderOriginalGood, RedirectAttributes redirectAttributes) {
		erpOrderOriginalGoodService.delete(erpOrderOriginalGood);
		addMessage(redirectAttributes, "删除订单商品成功");
		return "redirect:"+Global.getAdminPath()+"/order/erpOrderOriginalGood/?repage";
	}

}