package com.yunnex.ops.erp.modules.workflow.remarks.web;

import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;
import com.yunnex.ops.erp.modules.workflow.remarks.entity.WorkflowRemarksInfo;
import com.yunnex.ops.erp.modules.workflow.remarks.service.WorkflowRemarksInfoService;

/**
 * 流程备注Controller
 * 
 * @author sunq
 * @version 2018-03-29
 */
@Controller
@RequestMapping(value = "${adminPath}/remarks/workflowRemarksInfo")
public class WorkflowRemarksInfoController extends BaseController {

    @Autowired
    private WorkflowRemarksInfoService workflowRemarksInfoService;
    @Autowired
    private WorkFlowService workFlowService;

    /**
     * 返回流程下的所有备注列表
     *
     * @param procInsId
     * @return
     * @date 2018年3月29日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = "findRemarksInfos")
    public Object findRemarksInfos(String procInsId) {
        List<WorkflowRemarksInfo> list = workflowRemarksInfoService.findListByProcInsId(procInsId);
        return list;
    }

    /**
     * 返回流程下的所有备注列表
     *
     * @param procInsId
     * @return
     * @date 2018年3月29日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = "findRemarksInfosByTaskId")
    public Object findRemarksInfosByTaskId(String taskId) {
        Task task = workFlowService.getTaskById(taskId);
        List<WorkflowRemarksInfo> list = workflowRemarksInfoService.findListByProcInsId(task.getProcessInstanceId());
        return list;
    }

    /**
     * 保存备注
     *
     * @param flowType
     * @param remarkText
     * @return
     * @date 2018年3月29日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = "save")
    public Object save(String paramJson) {
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            workflowRemarksInfoService.batchSave(paramJson);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (Exception e) {
            logger.error("流程备注保存操作出错，错误信息={}", e);// NOSONAR
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
        }
        return returnMap;
    }

    /**
     * 逻辑删除备注信息
     *
     * @param id
     * @return
     * @date 2018年3月29日
     * @author SunQ
     */
    @ResponseBody
    @RequestMapping(value = "delete")
    public Object delete(WorkflowRemarksInfo workflowRemarksInfo) {
        Map<String, Object> returnMap = Maps.newHashMap();
        try {
            workflowRemarksInfoService.delete(workflowRemarksInfo);
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_SUCCESS);
        } catch (ServiceException e) {// NOSONAR
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, e.getMessage());
        } catch (Exception e) {// NOSONAR
            logger.error("流程备注删除操作出错，错误信息={}", e);// NOSONAR
            returnMap.put(CommonConstants.RETURN_CODE, CommonConstants.RETURN_CODE_FAIL);
            returnMap.put(CommonConstants.RETURN_MESSAGE, CommonConstants.SYSTEM_ERROR_MESSAGE);
        }
        return returnMap;
    }

    /**
     * 新增备注页面
     *
     * @return
     * @date 2018年3月29日
     * @author SunQ
     */
    @RequestMapping(value = "add")
    public String add(String workflowKey, String workflowNodeKey, Model model) {
        return "modules/workflow/remarks/workflowRemarksInfoAdd";
    }

    /**
     * 业务定义：查询流程节点对应备注项类型信息
     * 
     * @date 2018年7月9日
     * @author R/Q
     */
    @ResponseBody
    @RequestMapping(value = "queryRemarkItems")
    public Object queryRemarkItems(String workflowKey, String workflowNodeKey, Model model) {
        return workflowRemarksInfoService.queryRemarkItems(workflowKey, workflowNodeKey);
    }
}
