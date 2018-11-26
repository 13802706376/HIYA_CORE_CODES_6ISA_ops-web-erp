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
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisDiscountTypeRecommend;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisDiscountTypeRecommendService;

/**
 * 优惠形式推荐表Controller
 * @author yunnex
 * @version 2018-03-29
 */
@Controller
@RequestMapping(value = "${adminPath}/diagnosis/diagnosisDiscountTypeRecommend")
public class DiagnosisDiscountTypeRecommendController extends BaseController {

    @Autowired
    private DiagnosisDiscountTypeRecommendService diagnosisDiscountTypeRecommendService;

    /**
     * sonar
     * 
     * @param id
     * @return
     */
    @ModelAttribute
    public DiagnosisDiscountTypeRecommend get(@RequestParam(required = false) String id) {
        DiagnosisDiscountTypeRecommend entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = diagnosisDiscountTypeRecommendService.get(id);
        }
        if (entity == null) {
            entity = new DiagnosisDiscountTypeRecommend();
        }
        return entity;
    }

    @RequiresPermissions("diagnosis:diagnosisDiscountTypeRecommend:view")
    @RequestMapping(value = {"list", ""})
    public String list(DiagnosisDiscountTypeRecommend diagnosisDiscountTypeRecommend, HttpServletRequest request, HttpServletResponse response,
                    Model model) {
        Page<DiagnosisDiscountTypeRecommend> page = diagnosisDiscountTypeRecommendService
                        .findPage(new Page<DiagnosisDiscountTypeRecommend>(request, response), diagnosisDiscountTypeRecommend);
        model.addAttribute("page", page);
        return "modules/diagnosis/diagnosisDiscountTypeRecommendList";
    }

    @RequiresPermissions("diagnosis:diagnosisDiscountTypeRecommend:view")
    @RequestMapping(value = "form")
    public String form(DiagnosisDiscountTypeRecommend diagnosisDiscountTypeRecommend, Model model) {
        model.addAttribute("diagnosisDiscountTypeRecommend", diagnosisDiscountTypeRecommend);
        return "modules/diagnosis/diagnosisDiscountTypeRecommendForm";
    }

    @RequiresPermissions("diagnosis:diagnosisDiscountTypeRecommend:edit")
    @RequestMapping(value = "save")
    public String save(DiagnosisDiscountTypeRecommend diagnosisDiscountTypeRecommend, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, diagnosisDiscountTypeRecommend)) {
            return form(diagnosisDiscountTypeRecommend, model);
        }
        diagnosisDiscountTypeRecommendService.save(diagnosisDiscountTypeRecommend);
        addMessage(redirectAttributes, "保存优惠形式推荐表成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisDiscountTypeRecommend/?repage";
    }

    @RequiresPermissions("diagnosis:diagnosisDiscountTypeRecommend:edit")
    @RequestMapping(value = "delete")
    public String delete(DiagnosisDiscountTypeRecommend diagnosisDiscountTypeRecommend, RedirectAttributes redirectAttributes) {
        diagnosisDiscountTypeRecommendService.delete(diagnosisDiscountTypeRecommend);
        addMessage(redirectAttributes, "删除优惠形式推荐表成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisDiscountTypeRecommend/?repage";
    }

}
