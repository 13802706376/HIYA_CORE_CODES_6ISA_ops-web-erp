package com.yunnex.ops.erp.modules.store.pay.web;

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
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayWeixinService;

/**
 * 微信支付开通资料Controller
 * @author yunnex
 * @version 2017-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/store/pay/erpStorePayWeixin")
public class ErpStorePayWeixinController extends BaseController {

    @Autowired
    private ErpStorePayWeixinService erpStorePayWeixinService;

    @ModelAttribute
    public ErpStorePayWeixin get(@RequestParam(required = false) String id) {
        ErpStorePayWeixin entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpStorePayWeixinService.get(id);
        }
        if (entity == null) {
            entity = new ErpStorePayWeixin();
        }
        return entity;
    }

    @RequiresPermissions("store:pay:erpStorePayWeixin:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpStorePayWeixin erpStorePayWeixin, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ErpStorePayWeixin> page = erpStorePayWeixinService.findPage(new Page<ErpStorePayWeixin>(request, response), erpStorePayWeixin);
        model.addAttribute("page", page);
        return "modules/store/pay/erpStorePayWeixinList";
    }

    @RequiresPermissions("store:pay:erpStorePayWeixin:view")
    @RequestMapping(value = "form")
    public String form(ErpStorePayWeixin erpStorePayWeixin, Model model) {
        model.addAttribute("erpStorePayWeixin", erpStorePayWeixin);
        return "modules/store/pay/erpStorePayWeixinForm";
    }

    @RequiresPermissions("store:pay:erpStorePayWeixin:edit")
    @RequestMapping(value = "save")
    public String save(ErpStorePayWeixin erpStorePayWeixin, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpStorePayWeixin)) {
            return form(erpStorePayWeixin, model);
        }
        erpStorePayWeixinService.save(erpStorePayWeixin);
        addMessage(redirectAttributes, "保存微信支付开通资料成功");
        return "redirect:" + Global.getAdminPath() + "/store/pay/erpStorePayWeixin/?repage";
    }

    /**
     * 删除
     *
     * @param erpStorePayWeixin
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("store:pay:erpStorePayWeixin:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpStorePayWeixin erpStorePayWeixin, RedirectAttributes redirectAttributes) {
        erpStorePayWeixinService.delete(erpStorePayWeixin);
        addMessage(redirectAttributes, "删除微信支付开通资料成功");
        return "redirect:" + Global.getAdminPath() + "/store/pay/erpStorePayWeixin/?repage";
    }

}