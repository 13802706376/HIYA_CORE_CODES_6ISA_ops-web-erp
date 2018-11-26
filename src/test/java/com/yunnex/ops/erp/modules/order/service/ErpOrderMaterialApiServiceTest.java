package com.yunnex.ops.erp.modules.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.api.dto.request.MaterialContentRequestDto;
import com.yunnex.ops.erp.api.dto.request.OrderMaterialRequestDto;

import yunnex.common.core.dto.ApiResult;
import yunnex.common.mybatis.PageResult;
import yunnex.operation.biz.facade.MaterialOrderFacade;
import yunnex.operation.biz.request.query.MaterialOrderReq;
import yunnex.operation.biz.response.MaterialOrderResp;

public class ErpOrderMaterialApiServiceTest extends BaseTest {

    @Autowired
    private ErpOrderMaterialApiService orderMaterialApiService;

    @Autowired
    private MaterialOrderFacade materialOrderFacade;

    /**
     * 订单类别为首次
     */
    @Test
    public void testFirstCategory() {
        OrderMaterialRequestDto requestDto = new OrderMaterialRequestDto();
        requestDto.setYsOrderId(100004L);
        requestDto.setYsOrderBuyTime(new Date());
        requestDto.setOrderLinkMan("李群");
        requestDto.setOrderLinkPhone("13988822222");
        requestDto.setOrderReceiveAddress("深圳福田");
        requestDto.setYsOrderBuyTime(new Date());
        requestDto.setYsOrderRealPrice(30005L);
        requestDto.setZhangbeiId("18665061934");
        requestDto.setOrderNumber("5577653542");
        requestDto.setMaterialPackageUrl("/home/test/data/apache/operation/739/2018/08/14/2d34c22d81f847e79bb21cc868f0a551/");

        List<MaterialContentRequestDto> materialContents = new ArrayList<>();
        MaterialContentRequestDto materialContent1 = new MaterialContentRequestDto();
        materialContent1.setFrontName("会员");
        materialContent1.setReverseName("储值");
        materialContent1.setFrontImage("海报-储值-594mmx841mm/海报-储值-TP背胶-594mmx841mm-1-正面.jpg");
        materialContent1.setReverseImage("易拉宝-会员-800mmx1800mm/易拉宝-会员-800mmx1800mm-2-正面.jpg");
        materialContent1.setReverseImage("海报-储值-594mmx841mm/海报-储值-TP背胶-594mmx841mm-1-正面.jpg");
        materialContent1.setResourceUrl("http://res.test-a.saofu.cn/saofu/operation/739/2018/08/14/2d34c22d81f847e79bb21cc868f0a551/物料素材/");
        materialContent1.setMaterialAmount(30);
        materialContent1.setScenarioType(2);
        materialContent1.setMaterialTypeName("桌台台卡-会员");
        materialContent1.setMaterialQuality("日光灯");
        materialContent1.setSize("30mm*300mm");
        materialContents.add(materialContent1);
        MaterialContentRequestDto materialContent2 = new MaterialContentRequestDto();
        materialContent2.setFrontName("");
        materialContent2.setReverseName("储值");
        materialContent2.setResourceUrl("http://res.test-a.saofu.cn/saofu/operation/739/2018/08/14/2d34c22d81f847e79bb21cc868f0a551/物料素材/");
        materialContent2.setFrontImage("收银台-储值-105mmx148mm/收银台-储值-亚克力-105mmx148mm-3-正面.jpg");
        materialContent2.setMaterialAmount(40);
        materialContent2.setScenarioType(3);
        materialContent2.setMaterialTypeName("桌台台卡-储值");
        materialContent2.setMaterialQuality("few老朋友");
        materialContent2.setSize("220mm*400mm");
        materialContents.add(materialContent2);

        requestDto.setMaterialContents(materialContents);

        orderMaterialApiService.saveOne(requestDto);
    }

    /**
     * 订单类别为更新
     */
    @Test
    public void testUpdateCategory() {
        OrderMaterialRequestDto requestDto = new OrderMaterialRequestDto();
        requestDto.setYsOrderId(200023L);
        requestDto.setYsOrderBuyTime(new Date());
        requestDto.setYsOrderRealPrice(300000L);
        requestDto.setOrderLinkMan("李舜生");
        requestDto.setOrderLinkPhone("13655589994");
        requestDto.setOrderReceiveAddress("深圳南山");
        requestDto.setZhangbeiId("13300130034");

        List<MaterialContentRequestDto> materialContents = new ArrayList<>();
        MaterialContentRequestDto materialContent1 = new MaterialContentRequestDto();
        materialContent1.setFrontImage("/upload/erp_store/20180723/28034de3ff9a48f6a48fd08f17cbaf42.jpg");
        materialContent1.setReverseImage("/upload/erp_store/20180723/28034de3ff9a48f6a48fd08f17cbaf42.jpg");
        materialContent1.setMaterialAmount(20);
        materialContent1.setScenarioType(2);
        materialContent1.setMaterialTypeName("桌台台卡-储值");
        materialContent1.setMaterialQuality("零用钱");
        materialContent1.setSize("110mm*240mm");
        materialContents.add(materialContent1);
        MaterialContentRequestDto materialContent2 = new MaterialContentRequestDto();
        materialContent2.setFrontImage("/upload/erp_store/20180723/28034de3ff9a48f6a48fd08f17cbaf42.jpg");
        materialContent2.setReverseImage("/upload/erp_store/20180723/28034de3ff9a48f6a48fd08f17cbaf42.jpg");
        materialContent2.setMaterialAmount(50);
        materialContent2.setScenarioType(4);
        materialContent2.setMaterialTypeName("桌台台卡-会员");
        materialContent2.setMaterialQuality("隔热");
        materialContent2.setSize("133mm*256mm");
        materialContents.add(materialContent2);

        requestDto.setMaterialContents(materialContents);

        orderMaterialApiService.saveOne(requestDto);
    }

    @Test
    public void t3() {
        orderMaterialApiService.syncOrderMaterials();
    }

    @Test
    public void t4() {
        MaterialOrderReq materialOrderReq = new MaterialOrderReq();
        materialOrderReq.setPageNum(1);
        materialOrderReq.setPageSize(10);
        // materialOrderReq.setOrderId(1L);
        ApiResult<PageResult<MaterialOrderResp>> result = materialOrderFacade.pullUnSynchronization(materialOrderReq);
        System.out.println(result);
    }
}
