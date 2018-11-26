/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.hat.web;

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
import com.yunnex.ops.erp.modules.hat.entity.HatProvince;
import com.yunnex.ops.erp.modules.hat.service.HatProvinceService;

/**
 * 省Controller
 * @author yunnex
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/hat/hatProvince")
public class HatProvinceController extends BaseController {

    /**
     * 省操作Service
     */
    @Autowired
    private HatProvinceService hatProvinceService;
    
    @ModelAttribute
    public HatProvince get(@RequestParam(required=false) String id) {
        HatProvince entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = hatProvinceService.get(id);
        }
        if (entity == null){
            entity = new HatProvince();
        }
        return entity;
    }
    
    @RequiresPermissions("hat:hatProvince:view")
    @RequestMapping(value = {"list", ""})
    public String list(HatProvince hatProvince, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<HatProvince> page = hatProvinceService.findPage(new Page<HatProvince>(request, response), hatProvince); 
        model.addAttribute("page", page);
        return "modules/hat/hatProvinceList";
    }

    @RequiresPermissions("hat:hatProvince:view")
    @RequestMapping(value = "form")
    public String form(HatProvince hatProvince, Model model) {
        model.addAttribute("hatProvince", hatProvince);
        return "modules/hat/hatProvinceForm";
    }

    @RequiresPermissions("hat:hatProvince:edit")
    @RequestMapping(value = "save")
    public String save(HatProvince hatProvince, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, hatProvince)){
            return form(hatProvince, model);
        }
        hatProvinceService.save(hatProvince);
        addMessage(redirectAttributes, "保存省成功");
        return "redirect:"+Global.getAdminPath()+"/hat/hatProvince/?repage";
    }
    
    @RequiresPermissions("hat:hatProvince:edit")
    @RequestMapping(value = "delete")
    public String delete(HatProvince hatProvince, RedirectAttributes redirectAttributes) {
        hatProvinceService.delete(hatProvince);
        addMessage(redirectAttributes, "删除省成功");
        return "redirect:"+Global.getAdminPath()+"/hat/hatProvince/?repage";
    }
}