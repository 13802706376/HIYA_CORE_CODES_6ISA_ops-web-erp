package com.yunnex.ops.erp.modules.order.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.order.dto.OrderMaterialSyncLogRequestDto;
import com.yunnex.ops.erp.modules.order.dto.OrderMaterialSyncLogResponseDto;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderMaterialSyncLog;
import com.yunnex.ops.erp.modules.order.service.ErpOrderMaterialApiService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderMaterialSyncLogService;

/**
 * 订单物料同步日志Controller
 * @author yunnex
 * @version 2018-07-02
 */
@Controller
@RequestMapping(value = "${adminPath}/order/erpOrderMaterialSyncLog")
public class ErpOrderMaterialSyncLogController extends BaseController {

	@Autowired
	private ErpOrderMaterialSyncLogService erpOrderMaterialSyncLogService;
    @Autowired
    private ErpOrderMaterialApiService orderMaterialApiService;
	
	@ModelAttribute
	public ErpOrderMaterialSyncLog get(@RequestParam(required=false) String id) {
		ErpOrderMaterialSyncLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpOrderMaterialSyncLogService.get(id);
		}
		if (entity == null){
			entity = new ErpOrderMaterialSyncLog();
		}
		return entity;
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(ErpOrderMaterialSyncLog erpOrderMaterialSyncLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpOrderMaterialSyncLog> page = erpOrderMaterialSyncLogService.findPage(new Page<ErpOrderMaterialSyncLog>(request, response), erpOrderMaterialSyncLog); 
		model.addAttribute("page", page);
		return "modules/order/erpOrderMaterialSyncLogList";
	}

	@RequiresPermissions("order:erpOrderMaterialSyncLog:view")
	@RequestMapping(value = "form")
	public String form(ErpOrderMaterialSyncLog erpOrderMaterialSyncLog, Model model) {
		model.addAttribute("erpOrderMaterialSyncLog", erpOrderMaterialSyncLog);
		return "modules/order/erpOrderMaterialSyncLogForm";
	}

	@RequiresPermissions("order:erpOrderMaterialSyncLog:edit")
	@RequestMapping(value = "save")
	public String save(ErpOrderMaterialSyncLog erpOrderMaterialSyncLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpOrderMaterialSyncLog)){
			return form(erpOrderMaterialSyncLog, model);
		}
		erpOrderMaterialSyncLogService.save(erpOrderMaterialSyncLog);
		addMessage(redirectAttributes, "保存订单物料同步日志成功");
		return "redirect:"+Global.getAdminPath()+"/order/erpOrderMaterialSyncLog/?repage";
	}
	
	@RequiresPermissions("order:erpOrderMaterialSyncLog:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpOrderMaterialSyncLog erpOrderMaterialSyncLog, RedirectAttributes redirectAttributes) {
		erpOrderMaterialSyncLogService.delete(erpOrderMaterialSyncLog);
		addMessage(redirectAttributes, "删除订单物料同步日志成功");
		return "redirect:"+Global.getAdminPath()+"/order/erpOrderMaterialSyncLog/?repage";
	}

    @RequestMapping(value = {"page"}, method = RequestMethod.POST)
    public @ResponseBody Page<OrderMaterialSyncLogResponseDto> listPage(@RequestBody OrderMaterialSyncLogRequestDto requestDto) {
        return erpOrderMaterialSyncLogService.findByPage(requestDto);
    }

}
