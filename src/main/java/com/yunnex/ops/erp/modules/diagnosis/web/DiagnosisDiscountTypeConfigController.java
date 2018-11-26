package com.yunnex.ops.erp.modules.diagnosis.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.yunnex.ops.erp.modules.diagnosis.constant.DiagnosisConstant;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisDiscountTypeConfig;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisDiscountTypeConfigService;
import com.yunnex.ops.erp.modules.sys.entity.Dict;
import com.yunnex.ops.erp.modules.sys.utils.DictUtils;

/**
 * 优惠形式配置表Controller
 * @author yunnex
 * @version 2018-03-29
 */
@Controller
@RequestMapping(value = "${adminPath}/diagnosis/diagnosisDiscountTypeConfig")
public class DiagnosisDiscountTypeConfigController extends BaseController {

    @Autowired
    private DiagnosisDiscountTypeConfigService diagnosisDiscountTypeConfigService;

    @ModelAttribute
    public DiagnosisDiscountTypeConfig get(@RequestParam(required = false) String id) {
        DiagnosisDiscountTypeConfig entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = diagnosisDiscountTypeConfigService.get(id);
        }
        if (entity == null) {
            entity = new DiagnosisDiscountTypeConfig();
        }
        return entity;
    }

    // @RequiresPermissions("diagnosis:diagnosisDiscountTypeConfig:view")
    @RequestMapping(value = {"list", ""})
    public String list(DiagnosisDiscountTypeConfig diagnosisDiscountTypeConfig, HttpServletRequest request, HttpServletResponse response,
                    Model model) {
        Page<DiagnosisDiscountTypeConfig> page = diagnosisDiscountTypeConfigService.findPage(new Page<DiagnosisDiscountTypeConfig>(request, response),
                        diagnosisDiscountTypeConfig);
        model.addAttribute("page", page);
        return "modules/diagnosis/diagnosisDiscountTypeConfigList";
    }

    // @RequiresPermissions("diagnosis:diagnosisDiscountTypeConfig:view")
    @RequestMapping(value = "form")
    public String form(DiagnosisDiscountTypeConfig diagnosisDiscountTypeConfig, Model model) {
        List<Dict> activityRequirements = DictUtils.getDictList(DiagnosisConstant.ACTIVITY_REQUIREMENT_DICT_TYPE);
        List<Dict> industryAttributes = DictUtils.getDictList(DiagnosisConstant.INDUSTRY_ATTRIBUTE_DICT_TYPE);
        List<Dict> discountTypes = DictUtils.getDictList(DiagnosisConstant.DISCOUNT_TYPE_DICT_TYPE);

        model.addAttribute("diagnosisDiscountTypeConfig", diagnosisDiscountTypeConfig);
        model.addAttribute("activityRequirements", activityRequirements);
        model.addAttribute("industryAttributes", industryAttributes);
        model.addAttribute("discountTypes", discountTypes);
        return "modules/diagnosis/diagnosisDiscountTypeConfigForm";
    }

    // @RequiresPermissions("diagnosis:diagnosisDiscountTypeConfig:edit")
    @RequestMapping(value = "save")
    public String save(DiagnosisDiscountTypeConfig diagnosisDiscountTypeConfig, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, diagnosisDiscountTypeConfig)) {
            return form(diagnosisDiscountTypeConfig, model);
        }
        diagnosisDiscountTypeConfigService.save(diagnosisDiscountTypeConfig);
        addMessage(redirectAttributes, "保存优惠形式配置表成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisDiscountTypeConfig/?repage";
    }

    // @RequiresPermissions("diagnosis:diagnosisDiscountTypeConfig:edit")
    @RequestMapping(value = "delete")
    public String delete(DiagnosisDiscountTypeConfig diagnosisDiscountTypeConfig, RedirectAttributes redirectAttributes) {
        diagnosisDiscountTypeConfigService.delete(diagnosisDiscountTypeConfig);
        addMessage(redirectAttributes, "删除优惠形式配置表成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisDiscountTypeConfig/?repage";
    }

}