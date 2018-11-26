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
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStoreBank;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStoreBankService;

/**
 * 银行信息Controller
 * @author yunnex
 * @version 2017-12-15
 */
@Controller
@RequestMapping(value = "${adminPath}/store/pay/erpStoreBank")
public class ErpStoreBankController extends BaseController {

    @Autowired
    private ErpStoreBankService erpStoreBankService;

    @ModelAttribute
    public ErpStoreBank get(@RequestParam(required = false) String id) {
        ErpStoreBank entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpStoreBankService.get(id);
        }
        if (entity == null) {
            entity = new ErpStoreBank();
        }
        return entity;
    }

    @RequiresPermissions("store:pay:erpStoreBank:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpStoreBank erpStoreBank, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ErpStoreBank> page = erpStoreBankService.findPage(new Page<ErpStoreBank>(request, response), erpStoreBank);
        model.addAttribute("page", page);
        return "modules/store/pay/erpStoreBankList";
    }

    @RequiresPermissions("store:pay:erpStoreBank:view")
    @RequestMapping(value = "form")
    public String form(ErpStoreBank erpStoreBank, Model model) {
        model.addAttribute("erpStoreBank", erpStoreBank);
        return "modules/store/pay/erpStoreBankForm";
    }

    @RequiresPermissions("store:pay:erpStoreBank:edit")
    @RequestMapping(value = "save")
    public String save(ErpStoreBank erpStoreBank, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpStoreBank)) {
            return form(erpStoreBank, model);
        }
        erpStoreBankService.save(erpStoreBank);
        addMessage(redirectAttributes, "保存银行信息成功");
        return "redirect:" + Global.getAdminPath() + "/store/pay/erpStoreBank/?repage";
    }

    /**
     * 删除
     *
     * @param erpStoreBank
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("store:pay:erpStoreBank:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpStoreBank erpStoreBank, RedirectAttributes redirectAttributes) {
        erpStoreBankService.delete(erpStoreBank);
        addMessage(redirectAttributes, "删除银行信息成功");
        return "redirect:" + Global.getAdminPath() + "/store/pay/erpStoreBank/?repage";
    }

}