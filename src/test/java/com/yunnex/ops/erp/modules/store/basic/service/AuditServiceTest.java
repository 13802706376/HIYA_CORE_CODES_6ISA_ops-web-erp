package com.yunnex.ops.erp.modules.store.basic.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowMonitorService;

public class AuditServiceTest extends BaseTest {

    @Autowired
    private AuditService auditService;
    @Autowired
    private WorkFlowMonitorService workFlowMonitorService;

    @Test
    public void beforeAudit() {
        JSONObject jsonObject = auditService.checkAuditOrder("14f6b8d5582e4f2886af2f7662d4f2a6");
        System.out.println(jsonObject);
    }

    @Test
    public void test() throws Exception {
        workFlowMonitorService.endProcess("48f9c100934a42d6bdda05bc0e60de17");
        System.out.println("success");
    }

}
