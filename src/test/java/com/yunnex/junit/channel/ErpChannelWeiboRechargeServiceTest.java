package com.yunnex.junit.channel;

import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.modules.workflow.channel.constant.Constants;
import com.yunnex.ops.erp.modules.workflow.channel.dto.WeiboRechargeActualRequestDto;
import com.yunnex.ops.erp.modules.workflow.channel.entity.ErpChannelWeiboRecharge;
import com.yunnex.ops.erp.modules.workflow.channel.service.ErpChannelWeiboRechargeService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class ErpChannelWeiboRechargeServiceTest extends BaseTest {

    @Autowired
    private ErpChannelWeiboRechargeService weiboRechargeService;

    /**
     * 新增充值
     */
    @Test
    public void testCreate() {
        ErpChannelWeiboRecharge weiboRecharge = new ErpChannelWeiboRecharge();
        weiboRecharge.setSplitId("0244df13885f4ecdabd2cc310e38fb8d");
        weiboRecharge.setShopInfoId("0605acd6d23943168cb3bfa014588ccd");
        weiboRecharge.setWeiboAccountNo("43215432");
        weiboRecharge.setWeiboUid("232qfd23q1");
        weiboRecharge.setApplyAmount(3432.32);
        weiboRecharge.setApplyDate(new Date());

        BaseResult result = weiboRechargeService.create(weiboRecharge, Constants.SOURCE_MANAGE, Constants.STATUS_APPLYING);
        System.out.println(result);
        Assert.assertTrue(BaseResult.isSuccess(result));
    }

    /**
     * 修改实际充值金额
     */
    @Test
    public void testChangeActualRecharge() {
        WeiboRechargeActualRequestDto weiboRecharge = new WeiboRechargeActualRequestDto();
        weiboRecharge.setId("53e4c6456f8c4d4197590ffea790310a");
        weiboRecharge.setActualAmount(686.36);
        weiboRecharge.setFinishDate(new Date());
        BaseResult result = weiboRechargeService.changeActualRecharge(weiboRecharge);
        Assert.assertTrue(BaseResult.isSuccess(result));
    }

    /**
     * 修改充值状态
     */
    @Test
    public void testChange() {
        String id = "53e4c6456f8c4d4197590ffea790310a", status = Constants.STATUS_APPLYING;
        BaseResult result = weiboRechargeService.changeRechargeStatus(id, status);
        Assert.assertTrue(BaseResult.isSuccess(result));
    }

}
