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
import com.yunnex.ops.erp.modules.hat.entity.HatArea;
import com.yunnex.ops.erp.modules.hat.service.HatAreaService;

/**
 * 区Controller
 * @author yunnex
 * @version 2018-01-05
 */
@Controller
@RequestMapping(value = "${adminPath}/hat/hatArea")
public class HatAreaController extends BaseController {

    /**
     * 区操作Service
     */
    @Autowired
    private HatAreaService hatAreaService;
    
    @ModelAttribute
    public HatArea get(@RequestParam(required=false) String id) {
        HatArea entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = hatAreaService.get(id);
        }
        if (entity == null){
            entity = new HatArea();
        }
        return entity;
    }
    
    @RequiresPermissions("hat:hatArea:view")
    @RequestMapping(value = {"list", ""})
    public String list(HatArea hatArea, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<HatArea> page = hatAreaService.findPage(new Page<HatArea>(request, response), hatArea); 
        model.addAttribute("page", page);
        return "modules/hat/hatAreaList";
    }

    @RequiresPermissions("hat:hatArea:view")
    @RequestMapping(value = "form")
    public String form(HatArea hatArea, Model model) {
        model.addAttribute("hatArea", hatArea);
        return "modules/hat/hatAreaForm";
    }

    @RequiresPermissions("hat:hatArea:edit")
    @RequestMapping(value = "save")
    public String save(HatArea hatArea, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, hatArea)){
            return form(hatArea, model);
        }
        hatAreaService.save(hatArea);
        addMessage(redirectAttributes, "保存区成功");
        return "redirect:"+Global.getAdminPath()+"/hat/hatArea/?repage";
    }
    
    @RequiresPermissions("hat:hatArea:edit")
    @RequestMapping(value = "delete")
    public String delete(HatArea hatArea, RedirectAttributes redirectAttributes) {
        hatAreaService.delete(hatArea);
        addMessage(redirectAttributes, "删除区成功");
        return "redirect:"+Global.getAdminPath()+"/hat/hatArea/?repage";
    }
}