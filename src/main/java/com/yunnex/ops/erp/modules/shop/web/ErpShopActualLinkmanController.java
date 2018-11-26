package com.yunnex.ops.erp.modules.shop.web;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopActualLinkman;
import com.yunnex.ops.erp.modules.shop.service.ErpShopActualLinkmanService;

/**
 * 商户实际联系人信息Controller
 * @author yunnex
 * @version 2017-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/shop/erpShopActualLinkman")
public class ErpShopActualLinkmanController extends BaseController {

    @Autowired
    private ErpShopActualLinkmanService erpShopActualLinkmanService;

    @ModelAttribute
    public ErpShopActualLinkman get(@RequestParam(required = false) String id) {
        ErpShopActualLinkman entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpShopActualLinkmanService.get(id);
        }
        if (entity == null) {
            entity = new ErpShopActualLinkman();
        }
        return entity;
    }

    @RequiresPermissions("shop:erpShopActualLinkman:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpShopActualLinkman erpShopActualLinkman, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ErpShopActualLinkman> page = erpShopActualLinkmanService.findPage(new Page<ErpShopActualLinkman>(request, response),
                        erpShopActualLinkman);
        model.addAttribute("page", page);
        return "modules/shop/erpShopActualLinkmanList";
    }

    @RequiresPermissions("shop:erpShopActualLinkman:view")
    @RequestMapping(value = "form")
    public String form(ErpShopActualLinkman erpShopActualLinkman, Model model) {
        model.addAttribute("erpShopActualLinkman", erpShopActualLinkman);
        return "modules/shop/erpShopActualLinkmanForm";
    }

    @RequiresPermissions("shop:erpShopActualLinkman:edit")
    @RequestMapping(value = "save")
    public String save(ErpShopActualLinkman erpShopActualLinkman, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpShopActualLinkman)) {
            return form(erpShopActualLinkman, model);
        }
        erpShopActualLinkmanService.save(erpShopActualLinkman);
        addMessage(redirectAttributes, "保存商户实际联系人信息成功");
        return "redirect:" + Global.getAdminPath() + "/shop/erpShopActualLinkman/?repage";
    }

    /**
     * 删除
     *
     * @param erpShopActualLinkman
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("shop:erpShopActualLinkman:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpShopActualLinkman erpShopActualLinkman, RedirectAttributes redirectAttributes) {
        erpShopActualLinkmanService.delete(erpShopActualLinkman);
        addMessage(redirectAttributes, "删除商户实际联系人信息成功");
        return "redirect:" + Global.getAdminPath() + "/shop/erpShopActualLinkman/?repage";
    }

    @RequestMapping(value = "findActualLinkManByShopInfoId", method = RequestMethod.GET)
    @ResponseBody
    public List<ErpShopActualLinkman> findActualLinkManByShopInfoId(String shopInfoId) {
        return erpShopActualLinkmanService.findShopLinmanByShopId(Global.NO, shopInfoId);
    }

    @RequestMapping(value = "saveOrUpdateEntity", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult saveOrUpdateEntity(@RequestBody ErpShopActualLinkman erpShopActualLinkman) {
        return erpShopActualLinkmanService.saveOrUpdateEntity(erpShopActualLinkman);
    }

    @RequestMapping(value = "deleteEntity", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult deleteEntity(@RequestBody ErpShopActualLinkman erpShopActualLinkman) {
        return erpShopActualLinkmanService.deleteEntity(erpShopActualLinkman);
    }
}