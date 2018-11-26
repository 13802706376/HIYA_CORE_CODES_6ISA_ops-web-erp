package com.yunnex.junit.good;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodInfo;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodService;
import com.yunnex.ops.erp.modules.good.entity.ErpGoodServiceItem;
import com.yunnex.ops.erp.modules.good.service.ErpGoodInfoService;
import com.yunnex.ops.erp.modules.good.service.ErpGoodServiceItemService;
import com.yunnex.ops.erp.modules.good.service.ErpGoodServiceService;


public class ErpGoodServiceTest extends BaseTest {

    @Autowired
    private ErpGoodInfoService erpGoodInfoService;
    @Autowired
    private ErpGoodServiceService erpGoodServiceService;
    @Autowired
    private ErpGoodServiceItemService erpGoodServiceItemService;
    @Test
    public void testEdit() {
        erpGoodInfoService.edit("2");
    }

    @Test
    public void testSaveOrUpdateGoodService() {
        ErpGoodInfo entity = new ErpGoodInfo();
        entity.setId("2");

        List<ErpGoodService> singleService = new ArrayList<>();

        // 修改原来的服务项目
        ErpGoodService e = new ErpGoodService();
        e.setGoodId("2");
        e.setId("17727ee6-60c1-11e8-a04a-fa163eaf8427");
        e.setServiceItemId("2");
        e.setServiceItemName("首次营销策划服务");
        e.setIsDeadline("Y");
        e.setServiceTerm(7);
        e.setTimes(3);
        e.setIsPackage("N");
        singleService.add(e);

        e = new ErpGoodService();
        e.setGoodId("2");
        e.setId("eb20eafa-62ed-11e8-a04a-fa163eaf8427");
        e.setServiceItemId("3");
        e.setServiceItemName("定期营销优化上门服务");
        e.setIsDeadline("Y");
        e.setServiceTerm(6);
        e.setTimes(1);
        e.setIsPackage("N");
        singleService.add(e);

        // 增加新的
        e = new ErpGoodService();
        e.setGoodId("2");
        e.setServiceItemId("4");
        e.setServiceItemName("定期营销优化远程服务");
        e.setIsDeadline("Y");
        e.setServiceTerm(7);
        e.setTimes(2);
        e.setIsPackage("N");
        singleService.add(e);

        List<ErpGoodService> packageService = new ArrayList<>();

        // 修改原来的服务项目
        e = new ErpGoodService();
        e.setGoodId("2");
        e.setId("99cc3ced-60c0-11e8-a04a-fa163eaf8427");
        e.setServiceItemId("1");
        e.setServiceItemName("聚引客服务");
        e.setIsDeadline("Y");
        e.setServiceTerm(7);
        e.setTimes(3);
        e.setIsPackage("Y");
        packageService.add(e);

        // 新增加了一个
        e = new ErpGoodService();
        e.setGoodId("2");
        e.setServiceItemId("3");
        e.setServiceItemName("定期营销优化上门服务");
        e.setIsDeadline("Y");
        e.setServiceTerm(7);
        e.setTimes(1);
        e.setIsPackage("Y");
        packageService.add(e);

        entity.setSingleServiceList(singleService);
        entity.setPackageServiceList(packageService);

        erpGoodInfoService.saveGoodService(entity);
    }

    @Test
    public void testFindListofGoodServiceItem() {
        ErpGoodServiceItem entity = new ErpGoodServiceItem();
        List<ErpGoodServiceItem> findList = erpGoodServiceItemService.findList(entity);
        System.out.println(findList);

    }

    @Test
    public void testAddGoodServiceItem() {
        ErpGoodServiceItem entity = new ErpGoodServiceItem();
        entity.setName("aa");
        entity.setReadonly(Constant.NO);
        erpGoodServiceItemService.add(entity);
    }

    @Test
    public void testFindGoodService() {
        List<ErpGoodService> findServiceList = erpGoodServiceService.findServiceList(null, Constant.YES);
        System.out.println(findServiceList);
    }

    @Test
    public void testDeleteGoodService() {
        erpGoodServiceService.deleteByGoodId("46");
    }



}
