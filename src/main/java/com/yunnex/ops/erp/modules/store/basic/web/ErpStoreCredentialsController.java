package com.yunnex.ops.erp.modules.store.basic.web;

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
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreCredentials;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreCredentialsService;

/**
 * 商户营业资质信息Controller
 * @author yunnex
 * @version 2017-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/store/basic/erpStoreCredentials")
public class ErpStoreCredentialsController extends BaseController {

    @Autowired
    private ErpStoreCredentialsService erpStoreCredentialsService;

    @ModelAttribute
    public ErpStoreCredentials get(@RequestParam(required = false) String id) {
        ErpStoreCredentials entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpStoreCredentialsService.get(id);
        }
        if (entity == null) {
            entity = new ErpStoreCredentials();
        }
        return entity;
    }

    @RequiresPermissions("store:basic:erpStoreCredentials:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpStoreCredentials erpStoreCredentials, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ErpStoreCredentials> page = erpStoreCredentialsService.findPage(new Page<ErpStoreCredentials>(request, response), erpStoreCredentials);
        model.addAttribute("page", page);
        return "modules/store/basic/erpStoreCredentialsList";
    }

    @RequiresPermissions("store:basic:erpStoreCredentials:view")
    @RequestMapping(value = "form")
    public String form(ErpStoreCredentials erpStoreCredentials, Model model) {
        model.addAttribute("erpStoreCredentials", erpStoreCredentials);
        return "modules/store/basic/erpStoreCredentialsForm";
    }

    @RequiresPermissions("store:basic:erpStoreCredentials:edit")
    @RequestMapping(value = "save")
    public String save(ErpStoreCredentials erpStoreCredentials, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpStoreCredentials)) {
            return form(erpStoreCredentials, model);
        }
        erpStoreCredentialsService.save(erpStoreCredentials);
        addMessage(redirectAttributes, "保存商户营业资质信息成功");
        return "redirect:" + Global.getAdminPath() + "/store/basic/erpStoreCredentials/?repage";
    }

    /**
     * 删除
     *
     * @param erpStoreCredentials
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("store:basic:erpStoreCredentials:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpStoreCredentials erpStoreCredentials, RedirectAttributes redirectAttributes) {
        erpStoreCredentialsService.delete(erpStoreCredentials);
        addMessage(redirectAttributes, "删除商户营业资质信息成功");
        return "redirect:" + Global.getAdminPath() + "/store/basic/erpStoreCredentials/?repage";
    }

}