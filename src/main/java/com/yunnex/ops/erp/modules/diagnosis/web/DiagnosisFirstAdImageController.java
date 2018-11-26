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
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisFirstAdImage;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisFirstAdImageService;

/**
 * 第一层重点宣传图片Controller
 * @author yunnex
 * @version 2018-04-08
 */
@Controller
@RequestMapping(value = "${adminPath}/diagnosis/diagnosisFirstAdImage")
public class DiagnosisFirstAdImageController extends BaseController {

    @Autowired
    private DiagnosisFirstAdImageService diagnosisFirstAdImageService;

    /**
     * sonar check
     * 
     * @param id
     * @return
     */
    @ModelAttribute
    public DiagnosisFirstAdImage get(@RequestParam(required = false) String id) {
        DiagnosisFirstAdImage entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = diagnosisFirstAdImageService.get(id);
        }
        if (entity == null) {
            entity = new DiagnosisFirstAdImage();
        }
        return entity;
    }

    @RequiresPermissions("diagnosis:diagnosisFirstAdImage:view")
    @RequestMapping(value = {"list", ""})
    public String list(DiagnosisFirstAdImage diagnosisFirstAdImage, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DiagnosisFirstAdImage> page = diagnosisFirstAdImageService.findPage(new Page<DiagnosisFirstAdImage>(request, response),
                        diagnosisFirstAdImage);
        model.addAttribute("page", page);
        return "modules/diagnosis/diagnosisFirstAdImageList";
    }

    @RequiresPermissions("diagnosis:diagnosisFirstAdImage:view")
    @RequestMapping(value = "form")
    public String form(DiagnosisFirstAdImage diagnosisFirstAdImage, Model model) {
        model.addAttribute("diagnosisFirstAdImage", diagnosisFirstAdImage);
        return "modules/diagnosis/diagnosisFirstAdImageForm";
    }

    @RequiresPermissions("diagnosis:diagnosisFirstAdImage:edit")
    @RequestMapping(value = "save")
    public String save(DiagnosisFirstAdImage diagnosisFirstAdImage, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, diagnosisFirstAdImage)) {
            return form(diagnosisFirstAdImage, model);
        }
        diagnosisFirstAdImageService.save(diagnosisFirstAdImage);
        addMessage(redirectAttributes, "保存第一层重点宣传图片成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisFirstAdImage/?repage";
    }

    @RequiresPermissions("diagnosis:diagnosisFirstAdImage:edit")
    @RequestMapping(value = "delete")
    public String delete(DiagnosisFirstAdImage diagnosisFirstAdImage, RedirectAttributes redirectAttributes) {
        diagnosisFirstAdImageService.delete(diagnosisFirstAdImage);
        addMessage(redirectAttributes, "删除第一层重点宣传图片成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisFirstAdImage/?repage";
    }

}
