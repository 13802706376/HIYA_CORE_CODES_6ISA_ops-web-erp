package com.yunnex.ops.erp.modules.workflow.flow.web;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.TaskService;
import org.apache.commons.lang.StringUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.workflow.flow.constant.DeliveryFlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.from.WorkFlowQueryForm;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlow3p25Service;

/**
 * 
 * @author Ejon
 * @date 2018年5月24日
 */
@Controller
@RequestMapping(value = "${adminPath}/workflow/3p25")
public class WorkFlow3p25Controller extends BaseController {

    @Autowired
    private WorkFlow3p25Service workFlow3p25Service;
    @Autowired
    private TaskService taskService;


    /**
     * 任务列表
     *
     * @param actType tab页类型
     * @param response
     * @param model
     * @return
     * @throws ParseException
     * @date 2018年5月24日
     * @author zjq
     */
    @RequestMapping(value = {"tasklist", ""})
    public String tasklist(WorkFlowQueryForm workFlowQueryForm,
                    HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("list", workFlow3p25Service.queryTaskListPage(workFlowQueryForm));
        model.addAttribute("act_type", workFlowQueryForm.getActType());
        model.addAttribute("tasks", workFlowQueryForm.getTaskKey());
        return "modules/workflow/tasklist3p25";
    }

    /**
     * 运营服务任务数量统计
     *
     * @return
     * @date 2018年5月29日
     * @author zjq
     */
    @ResponseBody
    @RequestMapping(value = {"operatingStatistics", ""})
    public String operatingStatistics() {
        return workFlow3p25Service.operatingStatistics();
    }


    /**
     * 任务详情
     *
     * @param taskId
     * @param procInsId
     * @param taskDefKey
     * @param actType
     * @param model
     * @return
     * @date 2018年5月30日
     * @author zjq
     */
    @RequestMapping(value = {"taskDetail", ""})
    public String tasklist(@RequestParam(required = true) String taskId, @RequestParam(required = true) String procInsId,
                    @RequestParam(required = true) String taskDefKey, @RequestParam(required = true) String actType,
                    @RequestParam(required = true) String processDefineKey, Model model) {
        model.addAttribute("taskId", taskId);
        model.addAttribute("procInsId", procInsId);
        model.addAttribute("taskDefKey", taskDefKey);
        model.addAttribute("actType", actType);
        model.addAttribute(DeliveryFlowConstant.SERVICE_TYPE, taskService.getVariable(taskId, DeliveryFlowConstant.SERVICE_TYPE)+"");
        model.addAttribute(DeliveryFlowConstant.VISIT_TYPE, taskService.getVariable(taskId, DeliveryFlowConstant.VISIT_TYPE)+"");
        model.addAttribute("processDefineKey", processDefineKey);
        model.addAttribute(DeliveryFlowConstant.SERVICE_TYPE, taskService.getVariable(taskId, DeliveryFlowConstant.SERVICE_TYPE)+StringUtils.EMPTY);
        model.addAttribute(DeliveryFlowConstant.VISIT_TYPE, taskService.getVariable(taskId, DeliveryFlowConstant.VISIT_TYPE)+StringUtils.EMPTY);
        model.addAttribute("flowInfo", workFlow3p25Service.getProcGeneralInfo(taskId, procInsId, processDefineKey));
        return "modules/workflow/taskDetail3p25";
    }


}