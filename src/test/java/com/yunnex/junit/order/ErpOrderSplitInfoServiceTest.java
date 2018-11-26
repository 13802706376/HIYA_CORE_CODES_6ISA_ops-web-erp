package com.yunnex.junit.order;

import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderGoodServiceInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
public class ErpOrderSplitInfoServiceTest extends BaseTest {

    @Autowired
    private ErpOrderSplitInfoService erpOrderSplitInfoService;
    @Autowired
    private ErpOrderGoodServiceInfoService erpOrderGoodServiceInfoService;

    /**
     * 测试暂停流程
     *
     * @date 2018年4月13日
     */
    @Test
    public void testSuspend() {
        ErpOrderSplitInfo erpOrderSplitInfo = new ErpOrderSplitInfo();
        erpOrderSplitInfo.setId("1e65044d004143c8906a339aec462ef3");
        erpOrderSplitInfo.setNextContactTime(new Date());
        erpOrderSplitInfo.setSuspendFlag("Y");
        erpOrderSplitInfo.setSuspendReason("qualification_problem");// 字典
        erpOrderSplitInfo.setSuspendReasonContent("xxx");
        erpOrderSplitInfoService.suspend(erpOrderSplitInfo);
    }

    @Test
    public void testQuerySum() {
        Map<String, Object> querySum = erpOrderGoodServiceInfoService.querySum("00027317e88744e0bc80800b33a337ce");
        System.out.println(querySum);
    }

}
