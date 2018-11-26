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
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisStoreInfo;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisStoreInfoService;

/**
 * 经营诊断的门店信息Controller
 * @author yunnex
 * @version 2018-03-29
 */
@Controller
@RequestMapping(value = "${adminPath}/diagnosis/diagnosisStoreInfo")
public class DiagnosisStoreInfoController extends BaseController {

    @Autowired
    private DiagnosisStoreInfoService diagnosisStoreInfoService;

    /**
     * sonar check
     * 
     * @param id
     * @return
     */
    @ModelAttribute
    public DiagnosisStoreInfo get(@RequestParam(required = false) String id) {
        DiagnosisStoreInfo entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = diagnosisStoreInfoService.get(id);
        }
        if (entity == null) {
            entity = new DiagnosisStoreInfo();
        }
        return entity;
    }

    @RequiresPermissions("diagnosis:diagnosisStoreInfo:view")
    @RequestMapping(value = {"list", ""})
    public String list(DiagnosisStoreInfo diagnosisStoreInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DiagnosisStoreInfo> page = diagnosisStoreInfoService.findPage(new Page<DiagnosisStoreInfo>(request, response), diagnosisStoreInfo);
        model.addAttribute("page", page);
        return "modules/diagnosis/diagnosisStoreInfoList";
    }

    @RequiresPermissions("diagnosis:diagnosisStoreInfo:view")
    @RequestMapping(value = "form")
    public String form(DiagnosisStoreInfo diagnosisStoreInfo, Model model) {
        model.addAttribute("diagnosisStoreInfo", diagnosisStoreInfo);
        return "modules/diagnosis/diagnosisStoreInfoForm";
    }

    @RequiresPermissions("diagnosis:diagnosisStoreInfo:edit")
    @RequestMapping(value = "save")
    public String save(DiagnosisStoreInfo diagnosisStoreInfo, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, diagnosisStoreInfo)) {
            return form(diagnosisStoreInfo, model);
        }
        diagnosisStoreInfoService.save(diagnosisStoreInfo);
        addMessage(redirectAttributes, "保存经营诊断的门店信息成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisStoreInfo/?repage";
    }

    @RequiresPermissions("diagnosis:diagnosisStoreInfo:edit")
    @RequestMapping(value = "delete")
    public String delete(DiagnosisStoreInfo diagnosisStoreInfo, RedirectAttributes redirectAttributes) {
        diagnosisStoreInfoService.delete(diagnosisStoreInfo);
        addMessage(redirectAttributes, "删除经营诊断的门店信息成功");
        return "redirect:" + Global.getAdminPath() + "/diagnosis/diagnosisStoreInfo/?repage";
    }

}
