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
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisSplitIndustryAttribute;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisSplitIndustryAttributeService;

/**
 * 分单行业属性关联表Controller
 * @author yunnex
 * @version 2018-03-29
 */
@Controller
@RequestMapping(value = "${adminPath}/diagnosis/diagnosisSplitIndustryAttribute")
public class DiagnosisSplitIndustryAttributeController extends BaseController {

    @Autowired
    private DiagnosisSplitIndustryAttributeService diagnosisSplitIndustryAttributeService;

    /**
     * sonar check
     * 
     * @param id
     * @return
     */
    @ModelAttribute
    public DiagnosisSplitIndustryAttribute get(@RequestParam(required = false) String id) {
        DiagnosisSplitIndustryAttribute entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = diagnosisSplitIndustryAttributeService.get(id);
        }
        if (entity == null) {
            entity = new DiagnosisSplitIndustryAttribute();
        }
        return entity;
    }

    @RequiresPermissions("diagnosis:diagnosisSplitIndustryAttribute:view")
    @RequestMapping(value = {"list", ""})
    public String list(DiagnosisSplitIndustryAttribute diagnosisSplitIndustryAttribute, HttpServletRequest request, HttpServletResponse response,
                    Model model) {
        Page<DiagnosisSplitIndustryAttribute> page = diagnosisSplitIndustryAttributeService
                        .findPage(new Page<DiagnosisSplitIndustryAttribute>(request, response), diagnosisSplitIndustryAttribute);
        model.addAttribute("page", page);
        return "modules/diagnosis/diagnosisSplitIndustryAttributeList";
    }

    @RequiresPermissions("diagnosis:diagnosisSplitIndustryAttribute:view")
    @RequestMapping(value = "form")
    public String form(DiagnosisSplitIndustryAttribute diagnosisSplitIndustryAttribute, Model model) {
        model.addAttribute("diagnosisSplitIndustryAttribute", diagnosisSplitIndustryAttribute);
        return "modules/diagnosis/diagnosisSplitIndustryAttributeForm";
    }

    @RequiresPermissions("diagnosis:diagnosisSplitIndustryAttribute:edit")
    @RequestMapping(value = "save")
    public String save(DiagnosisSplitIndustryAttribute diagnosisSplitIndustryAttribute, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, diagnosisSplitIndustryAttribute)) {
            return form(diagnosisSplitIndustryAttribute, model);
        }
        diagnosisSplitIndustryAttributeService.save(diagnosisSplitIndustryAttribute);
        addMessage(redirectAttributes, "保存分单行业属性关联表成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisSplitIndustryAttribute/?repage";
    }

    @RequiresPermissions("diagnosis:diagnosisSplitIndustryAttribute:edit")
    @RequestMapping(value = "delete")
    public String delete(DiagnosisSplitIndustryAttribute diagnosisSplitIndustryAttribute, RedirectAttributes redirectAttributes) {
        diagnosisSplitIndustryAttributeService.delete(diagnosisSplitIndustryAttribute);
        addMessage(redirectAttributes, "删除分单行业属性关联表成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisSplitIndustryAttribute/?repage";
    }

}
