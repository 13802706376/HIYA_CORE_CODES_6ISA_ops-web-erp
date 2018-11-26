package com.yunnex.ops.erp.modules.shop.web;

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
import com.yunnex.ops.erp.modules.shop.entity.BusinessCategory;
import com.yunnex.ops.erp.modules.shop.service.BusinessCategoryService;

/**
 * 经营类目Controller
 * @author 11
 * @version 2017-12-20
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/businessCategory")
public class BusinessCategoryController extends BaseController {

    /**
     * 经营类目Service
     */
    @Autowired
    private BusinessCategoryService businessCategoryService;
    
    @ModelAttribute
    public BusinessCategory get(@RequestParam(required=false) String id) {
        BusinessCategory entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = businessCategoryService.get(id);
        }
        if (entity == null){
            entity = new BusinessCategory();
        }
        return entity;
    }
    
    @RequiresPermissions("shop:businessCategory:view")
    @RequestMapping(value = {"list", ""})
    public String list(BusinessCategory businessCategory, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BusinessCategory> page = businessCategoryService.findPage(new Page<BusinessCategory>(request, response), businessCategory); 
        model.addAttribute("page", page);
        return "modules/shop/businessCategoryList";
    }

    @RequiresPermissions("shop:businessCategory:view")
    @RequestMapping(value = "form")
    public String form(BusinessCategory businessCategory, Model model) {
        model.addAttribute("businessCategory", businessCategory);
        return "modules/shop/businessCategoryForm";
    }

    @RequiresPermissions("shop:businessCategory:edit")
    @RequestMapping(value = "save")
    public String save(BusinessCategory businessCategory, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, businessCategory)){
            return form(businessCategory, model);
        }
        businessCategoryService.save(businessCategory);
        addMessage(redirectAttributes, "保存经营类目成功");
        return "redirect:"+Global.getAdminPath()+"/shop/businessCategory/?repage";
    }
    
    @RequiresPermissions("shop:businessCategory:edit")
    @RequestMapping(value = "delete")
    public String delete(BusinessCategory businessCategory, RedirectAttributes redirectAttributes) {
        businessCategoryService.delete(businessCategory);
        addMessage(redirectAttributes, "删除经营类目成功");
        return "redirect:"+Global.getAdminPath()+"/shop/businessCategory/?repage";
    }
}