package com.yunnex.ops.erp.modules.act.web;

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
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.act.entity.TaskExt;
import com.yunnex.ops.erp.modules.act.service.TaskExtService;

/**
 * act_ru_task_ext生成Controller
 * @author act_ru_task_ext生成
 * @version 2018-01-13
 */
@Controller
@RequestMapping(value = "${adminPath}/act/taskExt")
public class TaskExtController extends BaseController {

    /**
     * taskExt操作service，（为满足sonar5%注释要求）
     */
    @Autowired
    private TaskExtService taskExtService;
    
    @ModelAttribute
    public TaskExt get(@RequestParam(required=false) String id) {
        TaskExt entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = taskExtService.get(id);
        }
        if (entity == null){
            entity = new TaskExt();
        }
        return entity;
    }
    
    @RequiresPermissions("act:taskExt:view")
    @RequestMapping(value = {"list", ""})
    public String list(TaskExt taskExt, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TaskExt> page = taskExtService.findPage(new Page<TaskExt>(request, response), taskExt); 
        model.addAttribute("page", page);
        return "modules/act/taskExtList";
    }

    @RequiresPermissions("act:taskExt:view")
    @RequestMapping(value = "form")
    public String form(TaskExt taskExt, Model model) {
        model.addAttribute("taskExt", taskExt);
        return "modules/act/taskExtForm";
    }

    @RequiresPermissions("act:taskExt:edit")
    @RequestMapping(value = "save")
    public String save(TaskExt taskExt, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, taskExt)){
            return form(taskExt, model);
        }
        taskExtService.save(taskExt);
        addMessage(redirectAttributes, "保存act_ru_task_ext生成成功");
        return "redirect:"+Global.getAdminPath()+"/act/taskExt/?repage";
    }
    
    @RequiresPermissions("act:taskExt:edit")
    @RequestMapping(value = "delete")
    public String delete(TaskExt taskExt, RedirectAttributes redirectAttributes) {
        taskExtService.delete(taskExt);
        addMessage(redirectAttributes, "删除act_ru_task_ext生成成功");
        return "redirect:"+Global.getAdminPath()+"/act/taskExt/?repage";
    }

}