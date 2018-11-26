package com.yunnex.ops.erp.modules.agent.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.junit.BaseTest;

public class ErpAgentInfoApiServiceTest extends BaseTest {

    @Autowired
    private ErpAgentInfoApiService agentInfoApiService;

    @Test
    public void saveOne() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 9999999);
        jsonObject.put("companyName", "新服务商测试new");
        jsonObject.put("agentProperty", 0);
        jsonObject.put("contacts", "联系人");
        jsonObject.put("contactsPhone", "13655552222");
        jsonObject.put("account", "newagent123");
        jsonObject.put("pwd", "123456");
        jsonObject.put("salt", "123456");
        jsonObject.put("useState", "1");
        agentInfoApiService.saveOne(jsonObject);
        System.out.println("success");
    }
}
