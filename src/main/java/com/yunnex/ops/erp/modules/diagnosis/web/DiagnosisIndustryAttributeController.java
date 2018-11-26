package com.yunnex.ops.erp.modules.diagnosis.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisIndustryAttribute;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisIndustryAttributeService;

/**
 * 行业属性Controller
 * @author yunnex
 * @version 2018-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/diagnosis/diagnosisIndustryAttribute")
public class DiagnosisIndustryAttributeController extends BaseController {

    @Autowired
    private DiagnosisIndustryAttributeService diagnosisIndustryAttributeService;

    @ModelAttribute
    public DiagnosisIndustryAttribute get(@RequestParam(required = false) String id) {
        DiagnosisIndustryAttribute entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = diagnosisIndustryAttributeService.get(id);
        }
        if (entity == null) {
            entity = new DiagnosisIndustryAttribute();
        }
        return entity;
    }

    @RequestMapping(value = {"list", ""})
    public String list(DiagnosisIndustryAttribute diagnosisIndustryAttribute, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DiagnosisIndustryAttribute> page = diagnosisIndustryAttributeService.findPage(new Page<DiagnosisIndustryAttribute>(request, response),
                        diagnosisIndustryAttribute);
        model.addAttribute("page", page);
        return "modules/diagnosis/diagnosisIndustryAttributeList";
    }

    @RequestMapping(value = "form")
    public String form(DiagnosisIndustryAttribute diagnosisIndustryAttribute, Model model) {
        model.addAttribute("diagnosisIndustryAttribute", diagnosisIndustryAttribute);
        return "modules/diagnosis/diagnosisIndustryAttributeForm";
    }

    @RequestMapping(value = "save")
    public String save(DiagnosisIndustryAttribute diagnosisIndustryAttribute, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, diagnosisIndustryAttribute)) {
            return form(diagnosisIndustryAttribute, model);
        }
        diagnosisIndustryAttributeService.save(diagnosisIndustryAttribute);
        addMessage(redirectAttributes, "保存行业属性成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisIndustryAttribute/?repage";
    }

    @RequiresPermissions("diagnosis:diagnosisIndustryAttribute:edit")
    @RequestMapping(value = "delete")
    public String delete(DiagnosisIndustryAttribute diagnosisIndustryAttribute, RedirectAttributes redirectAttributes) {
        diagnosisIndustryAttributeService.delete(diagnosisIndustryAttribute);
        addMessage(redirectAttributes, "删除行业属性成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisIndustryAttribute/?repage";
    }

    /**
     * 根据父ID查找
     *
     * @param pid
     * @return
     * @date 2018年4月3日
     */
    @RequestMapping(value = "pid/{pid}")
    public @ResponseBody List<DiagnosisIndustryAttribute> findByPid(@PathVariable String pid) {
        return diagnosisIndustryAttributeService.findByPid(pid);
    }

}