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
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisDiscount;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisDiscountService;

/**
 * 经营诊断的优惠内容Controller
 * @author yunnex
 * @version 2018-03-29
 */
@Controller
@RequestMapping(value = "${adminPath}/diagnosis/diagnosisDiscount")
public class DiagnosisDiscountController extends BaseController {

    @Autowired
    private DiagnosisDiscountService diagnosisDiscountService;

    /**
     * get
     * 
     * @param id
     * @return
     */
    @ModelAttribute
    public DiagnosisDiscount get(@RequestParam(required = false) String id) {
        DiagnosisDiscount entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = diagnosisDiscountService.get(id);
        }
        if (entity == null) {
            entity = new DiagnosisDiscount();
        }
        return entity;
    }

    @RequiresPermissions("diagnosis:diagnosisDiscount:view")
    @RequestMapping(value = {"list", ""})
    public String list(DiagnosisDiscount diagnosisDiscount, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DiagnosisDiscount> page = diagnosisDiscountService.findPage(new Page<DiagnosisDiscount>(request, response), diagnosisDiscount);
        model.addAttribute("page", page);
        return "modules/diagnosis/diagnosisDiscountList";
    }

    @RequiresPermissions("diagnosis:diagnosisDiscount:view")
    @RequestMapping(value = "form")
    public String form(DiagnosisDiscount diagnosisDiscount, Model model) {
        model.addAttribute("diagnosisDiscount", diagnosisDiscount);
        return "modules/diagnosis/diagnosisDiscountForm";
    }

    @RequiresPermissions("diagnosis:diagnosisDiscount:edit")
    @RequestMapping(value = "save")
    public String save(DiagnosisDiscount diagnosisDiscount, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, diagnosisDiscount)) {
            return form(diagnosisDiscount, model);
        }
        diagnosisDiscountService.save(diagnosisDiscount);
        addMessage(redirectAttributes, "保存经营诊断的优惠内容成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisDiscount/?repage";
    }

    @RequiresPermissions("diagnosis:diagnosisDiscount:edit")
    @RequestMapping(value = "delete")
    public String delete(DiagnosisDiscount diagnosisDiscount, RedirectAttributes redirectAttributes) {
        diagnosisDiscountService.delete(diagnosisDiscount);
        addMessage(redirectAttributes, "删除经营诊断的优惠内容成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisDiscount/?repage";
    }

}
