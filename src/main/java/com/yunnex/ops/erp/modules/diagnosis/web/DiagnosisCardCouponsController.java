package com.yunnex.ops.erp.modules.diagnosis.web;

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
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisCardCoupons;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisCardCouponsService;

/**
 * 卡券内容Controller
 * @author yunnex
 * @version 2018-03-29
 */
@Controller
@RequestMapping(value = "${adminPath}/diagnosis/diagnosisCardCoupons")
public class DiagnosisCardCouponsController extends BaseController {

    @Autowired
    private DiagnosisCardCouponsService diagnosisCardCouponsService;

    /**
     * @param id
     * @return
     */
    @ModelAttribute
    public DiagnosisCardCoupons get(@RequestParam(required = false) String id) {
        DiagnosisCardCoupons entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = diagnosisCardCouponsService.get(id);
        }
        if (entity == null) {
            entity = new DiagnosisCardCoupons();
        }
        return entity;
    }

    @RequiresPermissions("diagnosis:diagnosisCardCoupons:view")
    @RequestMapping(value = {"list", ""})
    public String list(DiagnosisCardCoupons diagnosisCardCoupons, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DiagnosisCardCoupons> page = diagnosisCardCouponsService.findPage(new Page<DiagnosisCardCoupons>(request, response),
                        diagnosisCardCoupons);
        model.addAttribute("page", page);
        return "modules/diagnosis/diagnosisCardCouponsList";
    }

    @RequiresPermissions("diagnosis:diagnosisCardCoupons:view")
    @RequestMapping(value = "form")
    public String form(DiagnosisCardCoupons diagnosisCardCoupons, Model model) {
        model.addAttribute("diagnosisCardCoupons", diagnosisCardCoupons);
        return "modules/diagnosis/diagnosisCardCouponsForm";
    }

    @RequiresPermissions("diagnosis:diagnosisCardCoupons:edit")
    @RequestMapping(value = "save")
    public String save(DiagnosisCardCoupons diagnosisCardCoupons, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, diagnosisCardCoupons)) {
            return form(diagnosisCardCoupons, model);
        }
        diagnosisCardCouponsService.save(diagnosisCardCoupons);
        addMessage(redirectAttributes, "保存卡券内容成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisCardCoupons/?repage";
    }

    @RequiresPermissions("diagnosis:diagnosisCardCoupons:edit")
    @RequestMapping(value = "delete")
    public String delete(DiagnosisCardCoupons diagnosisCardCoupons, RedirectAttributes redirectAttributes) {
        diagnosisCardCouponsService.delete(diagnosisCardCoupons);
        addMessage(redirectAttributes, "删除卡券内容成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisCardCoupons/?repage";
    }

}
