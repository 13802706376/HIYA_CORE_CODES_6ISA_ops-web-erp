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
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisSecondAdImage;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisSecondAdImageService;

/**
 * 第二层重点宣传图片Controller
 * @author yunnex
 * @version 2018-04-08
 */
@Controller
@RequestMapping(value = "${adminPath}/diagnosis/diagnosisSecondAdImage")
public class DiagnosisSecondAdImageController extends BaseController {

    @Autowired
    private DiagnosisSecondAdImageService diagnosisSecondAdImageService;

    /**
     * sonar check
     *
     * @param id
     * @return
     */
    @ModelAttribute
    public DiagnosisSecondAdImage get(@RequestParam(required = false) String id) {
        DiagnosisSecondAdImage entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = diagnosisSecondAdImageService.get(id);
        }
        if (entity == null) {
            entity = new DiagnosisSecondAdImage();
        }
        return entity;
    }

    @RequiresPermissions("diagnosis:diagnosisSecondAdImage:view")
    @RequestMapping(value = {"list", ""})
    public String list(DiagnosisSecondAdImage diagnosisSecondAdImage, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DiagnosisSecondAdImage> page = diagnosisSecondAdImageService.findPage(new Page<DiagnosisSecondAdImage>(request, response),
                        diagnosisSecondAdImage);
        model.addAttribute("page", page);
        return "modules/diagnosis/diagnosisSecondAdImageList";
    }

    @RequiresPermissions("diagnosis:diagnosisSecondAdImage:view")
    @RequestMapping(value = "form")
    public String form(DiagnosisSecondAdImage diagnosisSecondAdImage, Model model) {
        model.addAttribute("diagnosisSecondAdImage", diagnosisSecondAdImage);
        return "modules/diagnosis/diagnosisSecondAdImageForm";
    }

    @RequiresPermissions("diagnosis:diagnosisSecondAdImage:edit")
    @RequestMapping(value = "save")
    public String save(DiagnosisSecondAdImage diagnosisSecondAdImage, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, diagnosisSecondAdImage)) {
            return form(diagnosisSecondAdImage, model);
        }
        diagnosisSecondAdImageService.save(diagnosisSecondAdImage);
        addMessage(redirectAttributes, "保存第二层重点宣传图片成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisSecondAdImage/?repage";
    }

    @RequiresPermissions("diagnosis:diagnosisSecondAdImage:edit")
    @RequestMapping(value = "delete")
    public String delete(DiagnosisSecondAdImage diagnosisSecondAdImage, RedirectAttributes redirectAttributes) {
        diagnosisSecondAdImageService.delete(diagnosisSecondAdImage);
        addMessage(redirectAttributes, "删除第二层重点宣传图片成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisSecondAdImage/?repage";
    }

}
