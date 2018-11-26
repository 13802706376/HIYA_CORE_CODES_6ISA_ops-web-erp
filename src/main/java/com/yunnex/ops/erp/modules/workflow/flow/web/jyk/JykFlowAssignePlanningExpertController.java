package com.yunnex.ops.erp.modules.workflow.flow.web.jyk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;

/**
 * 指派策划专家
 * 
 * @author zjq
 * @date 2018年4月17日
 */
@Controller
@RequestMapping(value = "${adminPath}/jyk/flow/planningexpert")
public class JykFlowAssignePlanningExpertController {

    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;

    /**
     * 指派策划专家
     *
     * @param taskId
     * @param PlanningExpert
     * @return
     * @date 2018年4月17日
     * @author zjq
     */
    @RequestMapping(value = "assigne_planning_expert")
    @ResponseBody
    public JSONObject assignePlanningExpert(@RequestParam(required = true) String splitId, @RequestParam(required = true) String taskId,
                    @RequestParam(required = true) String planningExpert) {
        return erpOrderSplitInfoService.assignePlanningExpert(taskId, planningExpert, splitId);
    }

}
