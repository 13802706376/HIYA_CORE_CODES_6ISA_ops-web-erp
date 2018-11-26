package com.yunnex.ops.erp.modules.visit.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.visit.service.ErpVisitServiceInfoService;


@Controller
@RequestMapping(value = "api/visit/")
public class ErpVisitServiceApiController extends BaseController {

    @Autowired
    private ErpVisitServiceInfoService erpVisitServiceInfoService;

    /**
     * 小程序确认验收
     *
     * @param visitId
     * @return
     * @date 2018年7月9日
     * @author zjq
     */
    @RequestMapping("acceptance")
    @ResponseBody
    public JSONObject acceptance(String visitId) {
        return erpVisitServiceInfoService.acceptance(visitId);
    }

}
