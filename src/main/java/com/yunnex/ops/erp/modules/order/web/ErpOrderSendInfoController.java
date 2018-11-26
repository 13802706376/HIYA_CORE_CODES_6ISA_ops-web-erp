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
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSendInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSendInfoService;

/**
 * 订单寄送信息Controller
 * @author yunnex
 * @version 2018-07-13
 */
@Controller
@RequestMapping(value = "${adminPath}/order/erpOrderSendInfo")
public class ErpOrderSendInfoController extends BaseController {

	@Autowired
	private ErpOrderSendInfoService erpOrderSendInfoService;
	
	@ModelAttribute
	public ErpOrderSendInfo get(@RequestParam(required=false) String id) {
		ErpOrderSendInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpOrderSendInfoService.get(id);
		}
		if (entity == null){
			entity = new ErpOrderSendInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("order:erpOrderSendInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpOrderSendInfo erpOrderSendInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpOrderSendInfo> page = erpOrderSendInfoService.findPage(new Page<ErpOrderSendInfo>(request, response), erpOrderSendInfo); 
		model.addAttribute("page", page);
		return "modules/order/erpOrderSendInfoList";
	}

	@RequiresPermissions("order:erpOrderSendInfo:view")
	@RequestMapping(value = "form")
	public String form(ErpOrderSendInfo erpOrderSendInfo, Model model) {
		model.addAttribute("erpOrderSendInfo", erpOrderSendInfo);
		return "modules/order/erpOrderSendInfoForm";
	}

	@RequiresPermissions("order:erpOrderSendInfo:edit")
	@RequestMapping(value = "save")
	public String save(ErpOrderSendInfo erpOrderSendInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpOrderSendInfo)){
			return form(erpOrderSendInfo, model);
		}
		erpOrderSendInfoService.save(erpOrderSendInfo);
		addMessage(redirectAttributes, "保存订单寄送信息成功");
		return "redirect:"+Global.getAdminPath()+"/order/erpOrderSendInfo/?repage";
	}
	
	@RequiresPermissions("order:erpOrderSendInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpOrderSendInfo erpOrderSendInfo, RedirectAttributes redirectAttributes) {
		erpOrderSendInfoService.delete(erpOrderSendInfo);
		addMessage(redirectAttributes, "删除订单寄送信息成功");
		return "redirect:"+Global.getAdminPath()+"/order/erpOrderSendInfo/?repage";
	}

}