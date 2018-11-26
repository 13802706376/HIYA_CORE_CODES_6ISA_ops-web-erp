package com.yunnex.ops.erp.modules.diagnosis.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisFormService;

/**
 * 角色推广资料汇总控制器
 */
@Controller
public class DiagnosisSummaryController {

    @Autowired
    private DiagnosisFormService diagnosisFormService;

    /**
     * 查询营销方案策划
     * 
     * @return
     */
    @RequestMapping(value = {"${adminPath}/diagnosis/summary/data", "/api/diagnosis/summary/data"})
    @ResponseBody
    public BaseResult findData(String splitId) {
        return new BaseResult(diagnosisFormService.getDiagnosisDataBySplitId(splitId));
    }

}
