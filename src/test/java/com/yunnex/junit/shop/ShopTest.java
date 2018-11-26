package com.yunnex.junit.shop;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopActualLinkman;
import com.yunnex.ops.erp.modules.shop.service.ErpShopActualLinkmanService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;

import yunnex.common.exception.ServiceException;

public class ShopTest extends BaseTest {
    @Autowired
    private ErpShopInfoService shopInfoService;
    @Autowired
    private ErpShopActualLinkmanService erpShopActualLinkmanService;
    @Test
    public void test1() {
        BaseResult result = shopInfoService.getServiceSumByZhangbeiId("operationService", "15220210395");
        System.out.println(result);
    }

    @Test
    public void test2() {
        BaseResult result = shopInfoService.getServiceSumByZhangbeiId("jykService", "13188881131");
        System.out.println(result);
    }

    @Test
    public void test3() {
        /* BaseResult result = shopInfoService.getJykGoodInfoByZhangbeiId("pending", "15996999999");
        System.out.println(result);*/

        /* BaseResult result = shopInfoService.getJykGoodInfoByZhangbeiId("process", "13802506348");
        System.out.println(result);*/

        BaseResult result = shopInfoService.getJykGoodInfoByZhangbeiId("finish", "13802506348");
        System.out.println(result);
    }

    @Test
    public void test4() {
        List<ErpShopActualLinkman> findShopLinmanByShopId = erpShopActualLinkmanService.findShopLinmanByShopId("0",
                        "e36eda004f5e46abb8b5b25a5bffe6aa");
        System.out.println(findShopLinmanByShopId);
    }

    @Test
    public void test5() throws ServiceException {
        /*        ErpShopActualLinkman erpShopActualLinkman = new ErpShopActualLinkman();
        erpShopActualLinkman.setId("01e8630e814f4c7290aae59c9a75870b");
        erpShopActualLinkman.setName("313131111");
        erpShopActualLinkman.setPhoneNo("1310001000111");
        erpShopActualLinkman.setPosition("老板11");
        erpShopActualLinkman.setShopInfoId("e36eda004f5e46abb8b5b25a5bffe6aa");
        erpShopActualLinkman.setWechatID("13100010001");
        erpShopActualLinkmanService.save(erpShopActualLinkman);
        */
        ErpShopActualLinkman erpShopActualLinkman = new ErpShopActualLinkman();
        erpShopActualLinkman.setId("ec342092ece142e180d05d285c0fdc0a");
        erpShopActualLinkman.setName("1868822222222");
        erpShopActualLinkman.setPhoneNo("1868875262722222");
        erpShopActualLinkman.setPosition("darre11");
        erpShopActualLinkman.setShopInfoId("e36eda004f5e46abb8b5b25a5bffe6aa");
        erpShopActualLinkman.setWechatID("131000100011122222222");
        BaseResult saveOrUpdateEntity = erpShopActualLinkmanService.saveOrUpdateEntity(erpShopActualLinkman);
        System.out.println(saveOrUpdateEntity);
        /*ErpShopActualLinkman erpShopActualLinkman = new ErpShopActualLinkman();
        erpShopActualLinkman.setId("asdf");
        BaseResult deleteEntity = erpShopActualLinkmanService.deleteEntity(erpShopActualLinkman);
        System.out.println(deleteEntity);*/
    }


}
