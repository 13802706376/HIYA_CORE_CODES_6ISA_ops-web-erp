package com.yunnex.junit.material;

import java.text.ParseException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.modules.material.dto.MaterialCreationRequestDto;
import com.yunnex.ops.erp.modules.material.dto.MaterialCreationResponseDto;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.material.service.ErpOrderMaterialCreationService;


public class MaterialCreationServiceTest extends BaseTest {

    @Autowired
    private ErpOrderMaterialCreationService erpOrderMaterialCreationService;

    @Test
    public void testList() throws ParseException {
        MaterialCreationRequestDto requestDto = new MaterialCreationRequestDto();
        requestDto.setPageNo(1);
        requestDto.setPageSize(10);
        // requestDto.setOrderNumber("13512345");
        // requestDto.setStatus("waiting_order");
        // requestDto.setShopName("运营ERPtest2te");
        // requestDto.setOperationAdviserId("141938022432424424");
        // requestDto.setProviderName("test");
        // requestDto.setPlaceOrderStartTime(DateUtils.parseDate("2018-05-28 15:44:36", "yyyy-MM-dd
        // HH:mm:ss"));// 2018-05-28
        // requestDto.setPlaceOrderEndTime(DateUtils.parseDate("2018-05-28 15:44:50", "yyyy-MM-dd
        // HH:mm:ss")); // 2018-05-28
        // requestDto.setDeliverStartTime(DateUtils.parseDate("2018-05-29", "yyyy-MM-dd")); //
        // 15:44:36
                                                                                                             // 15:47:18
        // requestDto.setPlaceOrderEndTime(new Date());//2018-05-28 15:47:18 2018-05-28 15:44:36
        MaterialCreationRequestDto dto = new MaterialCreationRequestDto();
        MaterialCreationResponseDto findPage = erpOrderMaterialCreationService.findPage(dto);
        System.out.println(findPage);
    }

    @Test
    public void testConfirmOrder() {
        ErpOrderMaterialCreation entity = new ErpOrderMaterialCreation();
        entity.setProviderName("物料供应商A");
        entity.setCost(10L);
        entity.setPlaceOrderTime(new Date());
        entity.setId("1061354f-624b-11e8-a04a-fa163eaf8427");
        erpOrderMaterialCreationService.confirmOrder(entity);
        ErpOrderMaterialCreation erpOrderMaterialCreation = erpOrderMaterialCreationService.get("f44d2595-6224-11e8-a04a-fa163eaf8427");
        Assert.assertEquals("placed_order", erpOrderMaterialCreation.getStatus());
    }

    @Test
    public void testConfirmSendOff() {
        ErpOrderMaterialCreation entity = new ErpOrderMaterialCreation();
        entity.setId("1061354f-624b-11e8-a04a-fa163eaf8427");
        entity.setLogisticsNumber("155553456789");
        erpOrderMaterialCreationService.confirmSendOff(entity);
        ErpOrderMaterialCreation erpOrderMaterialCreation = erpOrderMaterialCreationService.get("f44d2595-6224-11e8-a04a-fa163eaf8427");
        Assert.assertEquals("155553456789", erpOrderMaterialCreation.getLogisticsNumber());
    }

    @Test
    public void testConfirmArrived() {
        ErpOrderMaterialCreation entity = new ErpOrderMaterialCreation();
        entity.setId("1061354f-624b-11e8-a04a-fa163eaf8427");
        Date now = new Date();
        entity.setDeliverTime(now);
        erpOrderMaterialCreationService.confirmArrived(entity);
        ErpOrderMaterialCreation erpOrderMaterialCreation = erpOrderMaterialCreationService.get("1061354f-624b-11e8-a04a-fa163eaf8427");
    }

    @Test
    public void testSave() {
        ErpOrderMaterialCreation entity = new ErpOrderMaterialCreation();
        entity.setShopName("哈哈");
        erpOrderMaterialCreationService.save(entity);
    }

}
