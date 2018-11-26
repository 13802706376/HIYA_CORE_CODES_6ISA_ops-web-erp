package com.yunnex.ops.erp.modules.message.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.message.service.ServiceMessageManagerService;

import yunnex.common.core.dto.ApiResult;

/**
 * 服务通知api
 * 
 * @author linqunzhi
 * @date 2018年7月27日
 */
@Controller
@RequestMapping(value = "api/message/erpServiceMessage")
public class ErpServiceMessageApiController extends BaseController {

    @Autowired
    private ServiceMessageManagerService messageManagerService;

    /**
     * 服务验收评价 通知
     *
     * @param acceptance
     * @return
     * @date 2018年7月27日
     * @author linqunzhi
     */
    @RequestMapping(value = "/acceptanceCommit")
    @ResponseBody
    public ApiResult<String> acceptanceCommit(String visitInfoId) {
        messageManagerService.acceptanceCommit(visitInfoId);
        return ApiResult.build();
    }


}
