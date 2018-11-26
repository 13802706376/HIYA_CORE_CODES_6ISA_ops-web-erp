package com.yunnex.ops.erp.modules.good.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.modules.good.service.ErpGoodInfoApiService;

@Controller
@RequestMapping(value = "api/good/erpGoodInfo")
public class ErpGoodInfoApiController {

    @Autowired
    private ErpGoodInfoApiService erpGoodInfoApiService;

    @RequestMapping("push")
    @ResponseBody
    public JSONObject push(@RequestBody String jsonStr) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 1);
        if (StringUtils.isNotEmpty(jsonStr)) {
            boolean result = erpGoodInfoApiService.push(jsonStr);
            if (result) {
                jsonObject.put("code", 0);
            }
        }
        return jsonObject;
    }
}
