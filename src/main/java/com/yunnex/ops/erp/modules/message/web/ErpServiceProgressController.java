package com.yunnex.ops.erp.modules.message.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.persistence.Page;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.message.entity.ErpServiceProgress;
import com.yunnex.ops.erp.modules.message.service.ErpServiceProgressService;

/**
 * 服务进度表Controller
 * 
 * @author yunnex
 * @version 2018-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/message/erpServiceSchedule")
public class ErpServiceProgressController extends BaseController {

    @Autowired
    private ErpServiceProgressService erpServiceScheduleService;

    @ModelAttribute
    public ErpServiceProgress get(@RequestParam(required = false) String id) {
        ErpServiceProgress entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = erpServiceScheduleService.get(id);
        }
        if (entity == null) {
            entity = new ErpServiceProgress();
        }
        return entity;
    }

    @RequiresPermissions("message:erpServiceSchedule:view")
    @RequestMapping(value = {"list", ""})
    public String list(ErpServiceProgress erpServiceSchedule, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ErpServiceProgress> page = erpServiceScheduleService.findPage(new Page<ErpServiceProgress>(request, response), erpServiceSchedule);
        model.addAttribute("page", page);
        return "modules/message/erpServiceScheduleList";
    }

    @RequiresPermissions("message:erpServiceSchedule:view")
    @RequestMapping(value = "form")
    public String form(ErpServiceProgress erpServiceSchedule, Model model) {
        model.addAttribute("erpServiceSchedule", erpServiceSchedule);
        return "modules/message/erpServiceScheduleForm";
    }

    @RequiresPermissions("message:erpServiceSchedule:edit")
    @RequestMapping(value = "save")
    public String save(ErpServiceProgress erpServiceSchedule, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, erpServiceSchedule)) {
            return form(erpServiceSchedule, model);
        }
        erpServiceScheduleService.save(erpServiceSchedule);
        addMessage(redirectAttributes, "保存服务进度表成功");
        return "redirect:" + Global.getAdminPath() + "/message/erpServiceSchedule/?repage";
    }

    @RequiresPermissions("message:erpServiceSchedule:edit")
    @RequestMapping(value = "delete")
    public String delete(ErpServiceProgress erpServiceSchedule, RedirectAttributes redirectAttributes) {
        erpServiceScheduleService.delete(erpServiceSchedule);
        addMessage(redirectAttributes, "删除服务进度表成功");
        return "redirect:" + Global.getAdminPath() + "/message/erpServiceSchedule/?repage";
    }

    /**
     * 业务定义：保存流程进度
     * 
     * @date 2018年7月23日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "saveProgress")
    public Object saveProgress(String paramJson) {
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            erpServiceScheduleService.saveProgress(paramJson);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (Exception e) {// NOSONAR
            logger.error("保存服务进度数据出错，paramJson={}，error={}", paramJson, e);// NOSONAR
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, CommonConstants.SYSTEM_ERROR_MESSAGE);
        }
        return returnMap;
    }

}
