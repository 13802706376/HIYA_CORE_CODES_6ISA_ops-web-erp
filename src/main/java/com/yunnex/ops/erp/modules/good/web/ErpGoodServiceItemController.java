package com.yunnex.ops.erp.modules.good.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodServiceItem;
import com.yunnex.ops.erp.modules.good.service.ErpGoodServiceItemService;

/**
 * 商品服务项目Controller
 * @author yunnex
 * @version 2018-05-29
 */
@Controller
@RequestMapping(value = "${adminPath}/good/erpGoodServiceItem")
public class ErpGoodServiceItemController extends BaseController {

	@Autowired
	private ErpGoodServiceItemService erpGoodServiceItemService;
	
	@ModelAttribute
	public ErpGoodServiceItem get(@RequestParam(required=false) String id) {
		ErpGoodServiceItem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpGoodServiceItemService.get(id);
		}
		if (entity == null){
			entity = new ErpGoodServiceItem();
		}
		return entity;
	}
	
    @RequestMapping(value = "findList")
    @ResponseBody
    public JSONObject findList() {
        ErpGoodServiceItem entity = new ErpGoodServiceItem();
        List<ErpGoodServiceItem> list = erpGoodServiceItemService.findList(entity);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serviceItemList", list);
        return jsonObject;
	}

	@RequiresPermissions("good:erpGoodServiceItem:view")
	@RequestMapping(value = "form")
	public String form(ErpGoodServiceItem erpGoodServiceItem, Model model) {
		model.addAttribute("erpGoodServiceItem", erpGoodServiceItem);
		return "modules/good/erpGoodServiceItemForm";
	}

	@RequiresPermissions("good:erpGoodServiceItem:edit")
	@RequestMapping(value = "save")
	public String save(ErpGoodServiceItem erpGoodServiceItem, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpGoodServiceItem)){
			return form(erpGoodServiceItem, model);
		}
		erpGoodServiceItemService.save(erpGoodServiceItem);
		addMessage(redirectAttributes, "保存商品服务项目成功");
		return "redirect:"+Global.getAdminPath()+"/good/erpGoodServiceItem/?repage";
	}
	
    @RequestMapping(value = "add")
    @ResponseBody
    public BaseResult add(@RequestBody ErpGoodServiceItem entity) {
        return erpGoodServiceItemService.add(entity);
    }

    @RequiresPermissions("good:erpGoodServiceItem:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpGoodServiceItem erpGoodServiceItem, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ErpGoodServiceItem> page = erpGoodServiceItemService.findPage(new Page<ErpGoodServiceItem>(request, response), erpGoodServiceItem);
        model.addAttribute("page", page);
        return "modules/good/erpGoodServiceItemList";
    }

	@RequiresPermissions("good:erpGoodServiceItem:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpGoodServiceItem erpGoodServiceItem, RedirectAttributes redirectAttributes) {
		erpGoodServiceItemService.delete(erpGoodServiceItem);
		addMessage(redirectAttributes, "删除商品服务项目成功");
		return "redirect:"+Global.getAdminPath()+"/good/erpGoodServiceItem/?repage";
	}

}