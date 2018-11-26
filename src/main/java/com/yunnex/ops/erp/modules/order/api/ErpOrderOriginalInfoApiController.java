package com.yunnex.ops.erp.modules.order.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.web.BaseController;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoApiService;


@Controller
@RequestMapping(value = "api/order/erpOrderOriginalInfo")
public class ErpOrderOriginalInfoApiController extends BaseController {

    @Autowired
    private ErpOrderOriginalInfoApiService erpOrderOriginalInfoApiService;

    @RequestMapping("push")
    @ResponseBody
    public JSONObject push(@RequestBody String jsonStr) {
        JSONObject resObject = new JSONObject();
        logger.info("接收到地推的订单推送信息为:" + jsonStr);
        boolean result = erpOrderOriginalInfoApiService.saveSingleOrder(jsonStr);
        resObject.put("code", result ? 0 : 1);
        resObject.put("errmsg", result ? "" : "推送错误");
        logger.info("处理结果:" + result);
        return resObject;
    }

}
