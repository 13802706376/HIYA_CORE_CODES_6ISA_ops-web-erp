package com.yunnex.ops.erp.modules.good.category.web;

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
import com.yunnex.ops.erp.modules.good.category.entity.ErpGoodCategory;
import com.yunnex.ops.erp.modules.good.category.service.ErpGoodCategoryService;

/**
 * 商品分类管理Controller
 * @author Frank
 * @version 2017-10-21
 */
@Controller
@RequestMapping(value = "${adminPath}/good/category")
public class ErpGoodCategoryController extends BaseController {

	@Autowired
	private ErpGoodCategoryService erpGoodCategoryService;
	
	@ModelAttribute
	public ErpGoodCategory get(@RequestParam(required=false) String id) {
		ErpGoodCategory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = erpGoodCategoryService.get(id);
		}
		if (entity == null){
			entity = new ErpGoodCategory();
		}
		return entity;
	}
	
	@RequiresPermissions("good.category:erpGoodCategory:view")
	@RequestMapping(value = {"list", ""})
	public String list(ErpGoodCategory erpGoodCategory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ErpGoodCategory> page = erpGoodCategoryService.findPage(new Page<ErpGoodCategory>(request, response), erpGoodCategory); 
		model.addAttribute("page", page);
        return "modules/good/category/erpGoodCategoryList";
	}

	@RequiresPermissions("good.category:erpGoodCategory:view")
	@RequestMapping(value = "form")
	public String form(ErpGoodCategory erpGoodCategory, Model model) {
		model.addAttribute("erpGoodCategory", erpGoodCategory);
        return "modules/good/category/erpGoodCategoryForm";
	}

	@RequiresPermissions("good.category:erpGoodCategory:edit")
	@RequestMapping(value = "save")
	public String save(ErpGoodCategory erpGoodCategory, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, erpGoodCategory)){
			return form(erpGoodCategory, model);
		}
		erpGoodCategoryService.save(erpGoodCategory);
		addMessage(redirectAttributes, "保存商品分类成功");
        return "redirect:" + Global.getAdminPath() + "/good/category/?repage";
	}
	
	@RequiresPermissions("good.category:erpGoodCategory:edit")
	@RequestMapping(value = "delete")
	public String delete(ErpGoodCategory erpGoodCategory, RedirectAttributes redirectAttributes) {
		erpGoodCategoryService.delete(erpGoodCategory);
		addMessage(redirectAttributes, "删除商品分类成功");
        return "redirect:" + Global.getAdminPath() + "/good/category/?repage";
	}

}