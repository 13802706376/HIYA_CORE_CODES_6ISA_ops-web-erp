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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderCouponOutput;
import com.yunnex.ops.erp.modules.order.service.ErpOrderCouponOutputService;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;

/**
 * 卡券输出Controller
 * @author yunnex
 * @version 2018-05-08
 */
@Controller
@RequestMapping(value = "${adminPath}/order/erpOrderCouponOutput")
public class ErpOrderCouponOutputController extends BaseController {

	@Autowired
	private ErpOrderCouponOutputService erpOrderCouponOutputService;
	
	@ModelAttribute
	public ErpOrderCouponOutput get(@RequestParam(required=false) String id) {
		ErpOrderCouponOutput entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpOrderCouponOutputService.get(id);
		}
		if (entity == null){
			entity = new ErpOrderCouponOutput();
		}
		return entity;
	}
	
	@RequiresPermissions("order:erpOrderCouponOutput:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpOrderCouponOutput erpOrderCouponOutput, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpOrderCouponOutput> page = erpOrderCouponOutputService.findPage(new Page<ErpOrderCouponOutput>(request, response), erpOrderCouponOutput); 
		model.addAttribute("page", page);
		return "modules/order/erpOrderCouponOutputList";
	}

	@RequiresPermissions("order:erpOrderCouponOutput:view")
	@RequestMapping(value = "form")
	public String form(ErpOrderCouponOutput erpOrderCouponOutput, Model model) {
		model.addAttribute("erpOrderCouponOutput", erpOrderCouponOutput);
		return "modules/order/erpOrderCouponOutputForm";
	}

	@RequiresPermissions("order:erpOrderCouponOutput:edit")
	@RequestMapping(value = "save")
	public String save(ErpOrderCouponOutput erpOrderCouponOutput, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpOrderCouponOutput)){
			return form(erpOrderCouponOutput, model);
		}
		erpOrderCouponOutputService.save(erpOrderCouponOutput);
		addMessage(redirectAttributes, "保存卡券输出成功");
		return "redirect:"+Global.getAdminPath()+"/order/erpOrderCouponOutput/?repage";
	}
	
	@RequiresPermissions("order:erpOrderCouponOutput:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpOrderCouponOutput erpOrderCouponOutput, RedirectAttributes redirectAttributes) {
		erpOrderCouponOutputService.delete(erpOrderCouponOutput);
		addMessage(redirectAttributes, "删除卡券输出成功");
		return "redirect:"+Global.getAdminPath()+"/order/erpOrderCouponOutput/?repage";
	}

    @RequestMapping(value = "cardDelete")
    @ResponseBody
    public JSONObject cardDelete(ErpOrderCouponOutput erpOrderCouponOutput, RedirectAttributes redirectAttributes) {
        JSONObject resObject = new JSONObject();
        erpOrderCouponOutputService.delete(erpOrderCouponOutput);
        resObject.put(FlowConstant.RESULT, FlowConstant.RESULT_TRUE);
        return resObject;
    }
}