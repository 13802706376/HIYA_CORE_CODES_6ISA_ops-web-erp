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
import com.yunnex.ops.erp.modules.order.entity.ErpOrderGoodServiceInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderGoodServiceInfoService;

/**
 * 订单商品服务Controller
 * @author yunnex
 * @version 2018-06-02
 */
@Controller
@RequestMapping(value = "${adminPath}/order/erpOrderGoodServiceInfo")
public class ErpOrderGoodServiceInfoController extends BaseController {

	@Autowired
	private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService;
	
	@ModelAttribute
	public ErpOrderGoodServiceInfo get(@RequestParam(required=false) String id) {
		ErpOrderGoodServiceInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpOrderGoodServiceInfoService.get(id);
		}
		if (entity == null){
			entity = new ErpOrderGoodServiceInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("order:erpOrderGoodServiceInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpOrderGoodServiceInfo erpOrderGoodServiceInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpOrderGoodServiceInfo> page = erpOrderGoodServiceInfoService.findPage(new Page<ErpOrderGoodServiceInfo>(request, response), erpOrderGoodServiceInfo); 
		model.addAttribute("page", page);
		return "modules/order/erpOrderGoodServiceInfoList";
	}

	@RequiresPermissions("order:erpOrderGoodServiceInfo:view")
	@RequestMapping(value = "form")
	public String form(ErpOrderGoodServiceInfo erpOrderGoodServiceInfo, Model model) {
		model.addAttribute("erpOrderGoodServiceInfo", erpOrderGoodServiceInfo);
		return "modules/order/erpOrderGoodServiceInfoForm";
	}

	@RequiresPermissions("order:erpOrderGoodServiceInfo:edit")
	@RequestMapping(value = "save")
	public String save(ErpOrderGoodServiceInfo erpOrderGoodServiceInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpOrderGoodServiceInfo)){
			return form(erpOrderGoodServiceInfo, model);
		}
		erpOrderGoodServiceInfoService.save(erpOrderGoodServiceInfo);
		addMessage(redirectAttributes, "保存订单商品服务成功");
		return "redirect:"+Global.getAdminPath()+"/order/erpOrderGoodServiceInfo/?repage";
	}
	
	@RequiresPermissions("order:erpOrderGoodServiceInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpOrderGoodServiceInfo erpOrderGoodServiceInfo, RedirectAttributes redirectAttributes) {
		erpOrderGoodServiceInfoService.delete(erpOrderGoodServiceInfo);
		addMessage(redirectAttributes, "删除订单商品服务成功");
		return "redirect:"+Global.getAdminPath()+"/order/erpOrderGoodServiceInfo/?repage";
	}
}