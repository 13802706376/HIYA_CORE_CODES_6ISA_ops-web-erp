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
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStorePromotePhotoMaterialService;

/**
 * 推广图片素材Controller
 * @author yunnex
 * @version 2017-12-09
 */
@Controller
@RequestMapping(value = "${adminPath}/store/basic/erpStorePromotePhotoMaterial")
public class ErpStorePromotePhotoMaterialController extends BaseController {

    @Autowired
    private ErpStorePromotePhotoMaterialService erpStorePromotePhotoMaterialService;

    @ModelAttribute
    public ErpStorePromotePhotoMaterial get(@RequestParam(required = false) String id) {
        ErpStorePromotePhotoMaterial entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpStorePromotePhotoMaterialService.get(id);
        }
        if (entity == null) {
            entity = new ErpStorePromotePhotoMaterial();
        }
        return entity;
    }

    @RequiresPermissions("store:basic:erpStorePromotePhotoMaterial:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpStorePromotePhotoMaterial erpStorePromotePhotoMaterial, HttpServletRequest request, HttpServletResponse response,
                    Model model) {
        Page<ErpStorePromotePhotoMaterial> page = erpStorePromotePhotoMaterialService
                        .findPage(new Page<ErpStorePromotePhotoMaterial>(request, response), erpStorePromotePhotoMaterial);
        model.addAttribute("page", page);
        return "modules/store/basic/erpStorePromotePhotoMaterialList";
    }

    @RequiresPermissions("store:basic:erpStorePromotePhotoMaterial:view")
    @RequestMapping(value = "form")
    public String form(ErpStorePromotePhotoMaterial erpStorePromotePhotoMaterial, Model model) {
        model.addAttribute("erpStorePromotePhotoMaterial", erpStorePromotePhotoMaterial);
        return "modules/store/basic/erpStorePromotePhotoMaterialForm";
    }

    @RequiresPermissions("store:basic:erpStorePromotePhotoMaterial:edit")
    @RequestMapping(value = "save")
    public String save(ErpStorePromotePhotoMaterial erpStorePromotePhotoMaterial, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpStorePromotePhotoMaterial)) {
            return form(erpStorePromotePhotoMaterial, model);
        }
        erpStorePromotePhotoMaterialService.save(erpStorePromotePhotoMaterial);
        addMessage(redirectAttributes, "保存推广图片素材成功");
        return "redirect:" + Global.getAdminPath() + "/store/basic/erpStorePromotePhotoMaterial/?repage";
    }

    /**
     * 删除
     *
     * @param erpStorePromotePhotoMaterial
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("store:basic:erpStorePromotePhotoMaterial:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpStorePromotePhotoMaterial erpStorePromotePhotoMaterial, RedirectAttributes redirectAttributes) {
        erpStorePromotePhotoMaterialService.delete(erpStorePromotePhotoMaterial);
        addMessage(redirectAttributes, "删除推广图片素材成功");
        return "redirect:" + Global.getAdminPath() + "/store/basic/erpStorePromotePhotoMaterial/?repage";
    }

}