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
import com.yunnex.ops.erp.modules.hat.entity.HatCity;
import com.yunnex.ops.erp.modules.hat.service.HatCityService;

/**
 * 市Controller
 * @author yunnex
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/hat/hatCity")
public class HatCityController extends BaseController {

    /**
     * 市操作Service
     */
    @Autowired
    private HatCityService hatCityService;
    
    @ModelAttribute
    public HatCity get(@RequestParam(required=false) String id) {
        HatCity entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = hatCityService.get(id);
        }
        if (entity == null){
            entity = new HatCity();
        }
        return entity;
    }
    
    @RequiresPermissions("hat:hatCity:view")
    @RequestMapping(value = {"list", ""})
    public String list(HatCity hatCity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<HatCity> page = hatCityService.findPage(new Page<HatCity>(request, response), hatCity); 
        model.addAttribute("page", page);
        return "modules/hat/hatCityList";
    }

    @RequiresPermissions("hat:hatCity:view")
    @RequestMapping(value = "form")
    public String form(HatCity hatCity, Model model) {
        model.addAttribute("hatCity", hatCity);
        return "modules/hat/hatCityForm";
    }

    @RequiresPermissions("hat:hatCity:edit")
    @RequestMapping(value = "save")
    public String save(HatCity hatCity, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, hatCity)){
            return form(hatCity, model);
        }
        hatCityService.save(hatCity);
        addMessage(redirectAttributes, "保存市成功");
        return "redirect:"+Global.getAdminPath()+"/hat/hatCity/?repage";
    }
    
    @RequiresPermissions("hat:hatCity:edit")
    @RequestMapping(value = "delete")
    public String delete(HatCity hatCity, RedirectAttributes redirectAttributes) {
        hatCityService.delete(hatCity);
        addMessage(redirectAttributes, "删除市成功");
        return "redirect:"+Global.getAdminPath()+"/hat/hatCity/?repage";
    }

}