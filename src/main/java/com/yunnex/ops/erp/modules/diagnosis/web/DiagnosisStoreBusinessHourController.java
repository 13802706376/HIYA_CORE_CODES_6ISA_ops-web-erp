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
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisStoreBusinessHour;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisStoreBusinessHourService;

/**
 * 经营诊断的门店的营业时间Controller
 * @author yunnex
 * @version 2018-03-29
 */
@Controller
@RequestMapping(value = "${adminPath}/diagnosis/diagnosisStoreBusinessHour")
public class DiagnosisStoreBusinessHourController extends BaseController {

    @Autowired
    private DiagnosisStoreBusinessHourService diagnosisStoreBusinessHourService;

    /**
     * sonar check
     * 
     * @param id
     * @return
     */
    @ModelAttribute
    public DiagnosisStoreBusinessHour get(@RequestParam(required = false) String id) {
        DiagnosisStoreBusinessHour entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = diagnosisStoreBusinessHourService.get(id);
        }
        if (entity == null) {
            entity = new DiagnosisStoreBusinessHour();
        }
        return entity;
    }

    @RequiresPermissions("diagnosis:diagnosisStoreBusinessHour:view")
    @RequestMapping(value = {"list", ""})
    public String list(DiagnosisStoreBusinessHour diagnosisStoreBusinessHour, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DiagnosisStoreBusinessHour> page = diagnosisStoreBusinessHourService.findPage(new Page<DiagnosisStoreBusinessHour>(request, response),
                        diagnosisStoreBusinessHour);
        model.addAttribute("page", page);
        return "modules/diagnosis/diagnosisStoreBusinessHourList";
    }

    @RequiresPermissions("diagnosis:diagnosisStoreBusinessHour:view")
    @RequestMapping(value = "form")
    public String form(DiagnosisStoreBusinessHour diagnosisStoreBusinessHour, Model model) {
        model.addAttribute("diagnosisStoreBusinessHour", diagnosisStoreBusinessHour);
        return "modules/diagnosis/diagnosisStoreBusinessHourForm";
    }

    @RequiresPermissions("diagnosis:diagnosisStoreBusinessHour:edit")
    @RequestMapping(value = "save")
    public String save(DiagnosisStoreBusinessHour diagnosisStoreBusinessHour, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, diagnosisStoreBusinessHour)) {
            return form(diagnosisStoreBusinessHour, model);
        }
        diagnosisStoreBusinessHourService.save(diagnosisStoreBusinessHour);
        addMessage(redirectAttributes, "保存经营诊断的门店的营业时间成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisStoreBusinessHour/?repage";
    }

    @RequiresPermissions("diagnosis:diagnosisStoreBusinessHour:edit")
    @RequestMapping(value = "delete")
    public String delete(DiagnosisStoreBusinessHour diagnosisStoreBusinessHour, RedirectAttributes redirectAttributes) {
        diagnosisStoreBusinessHourService.delete(diagnosisStoreBusinessHour);
        addMessage(redirectAttributes, "删除经营诊断的门店的营业时间成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisStoreBusinessHour/?repage";
    }

}
