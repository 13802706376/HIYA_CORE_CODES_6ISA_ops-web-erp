package com.yunnex.ops.erp.modules.store.advertiser.web;

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
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserMomoService;

/**
 * 陌陌广告主开通资料Controller
 * @author yunnex
 * @version 2017-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/store/advertiser/erpStoreAdvertiserMomo")
public class ErpStoreAdvertiserMomoController extends BaseController {

    @Autowired
    private ErpStoreAdvertiserMomoService erpStoreAdvertiserMomoService;

    @ModelAttribute
    public ErpStoreAdvertiserMomo get(@RequestParam(required = false) String id) {
        ErpStoreAdvertiserMomo entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpStoreAdvertiserMomoService.get(id);
        }
        if (entity == null) {
            entity = new ErpStoreAdvertiserMomo();
        }
        return entity;
    }

    @RequiresPermissions("store:advertiser:erpStoreAdvertiserMomo:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpStoreAdvertiserMomo erpStoreAdvertiserMomo, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ErpStoreAdvertiserMomo> page = erpStoreAdvertiserMomoService.findPage(new Page<ErpStoreAdvertiserMomo>(request, response),
                        erpStoreAdvertiserMomo);
        model.addAttribute("page", page);
        return "modules/store/advertiser/erpStoreAdvertiserMomoList";
    }

    @RequiresPermissions("store:advertiser:erpStoreAdvertiserMomo:view")
    @RequestMapping(value = "form")
    public String form(ErpStoreAdvertiserMomo erpStoreAdvertiserMomo, Model model) {
        model.addAttribute("erpStoreAdvertiserMomo", erpStoreAdvertiserMomo);
        return "modules/store/advertiser/erpStoreAdvertiserMomoForm";
    }

    @RequiresPermissions("store:advertiser:erpStoreAdvertiserMomo:edit")
    @RequestMapping(value = "save")
    public String save(ErpStoreAdvertiserMomo erpStoreAdvertiserMomo, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpStoreAdvertiserMomo)) {
            return form(erpStoreAdvertiserMomo, model);
        }
        erpStoreAdvertiserMomoService.save(erpStoreAdvertiserMomo);
        addMessage(redirectAttributes, "保存陌陌广告主开通资料成功");
        return "redirect:" + Global.getAdminPath() + "/store/advertiser/erpStoreAdvertiserMomo/?repage";
    }

    /**
     * 删除
     *
     * @param erpStoreAdvertiserMomo
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("store:advertiser:erpStoreAdvertiserMomo:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpStoreAdvertiserMomo erpStoreAdvertiserMomo, RedirectAttributes redirectAttributes) {
        erpStoreAdvertiserMomoService.delete(erpStoreAdvertiserMomo);
        addMessage(redirectAttributes, "删除陌陌广告主开通资料成功");
        return "redirect:" + Global.getAdminPath() + "/store/advertiser/erpStoreAdvertiserMomo/?repage";
    }

}