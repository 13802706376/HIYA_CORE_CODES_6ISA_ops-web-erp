package com.yunnex.junit.diagnosis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunnex.junit.BaseTest;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.AESUtil;
import com.yunnex.ops.erp.modules.diagnosis.constant.DiagnosisConstant;
import com.yunnex.ops.erp.modules.diagnosis.dto.CouponsPlanRequestDto;
import com.yunnex.ops.erp.modules.diagnosis.dto.PreparationStageRequestDto;
import com.yunnex.ops.erp.modules.diagnosis.dto.PropagandaKeyRequestDto;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisCardCoupons;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisDiscount;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisDiscountTypeRecommend;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisFirstAdImage;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisForm;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisSecondAdImage;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisSplitIndustryAttribute;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisStoreBusinessHour;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisStoreInfo;
import com.yunnex.ops.erp.modules.diagnosis.service.DiagnosisFormService;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;

import net.sf.json.util.JSONUtils;

public class DiagnosisFormServiceTest extends BaseTest {

    @Autowired
    private DiagnosisFormService diagnosisFormService;

    // 保存电话过程中数据
    @Test
    public void saveByPhone() {
        String splitId = "ddb8666db96643aca1f3eee761d8719c";
        String storeId = "573cad3d6c464b0d802dd90639221d3c";

        DiagnosisForm form = new DiagnosisForm();
        // 表单
        form.setSplitId(splitId);
        form.setContactPerson("Jame");
        form.setContactPhone("13789984444");
        form.setPackageSelection("ed7ac6e2e37a43ae8cc0a0454ef0630b");
        form.setMajorProduct("fds博采众长不打自招few");
        form.setPromoteProduct("fe6w56更怕竭尽所能ae2");
        form.setBrandLightspot("few老朋友王不屑一顾g3");
        form.setOriginalityCulture("fefe43睡的也算隔墙有耳f2");
        form.setActivityRequirements("19361ccbf24b4be2b1391e275d502155,6120e74df57f47fd960dcaec439dc7c1");
        form.setActivityGoal("brand_exposure,attract_customer_to_register");

        // 优惠内容
        List<DiagnosisDiscount> diagnosisDiscounts = new ArrayList<>();
        DiagnosisDiscount diagnosisDiscount1 = new DiagnosisDiscount();
        diagnosisDiscount1.setSplitId(splitId);
        diagnosisDiscount1.setTitle("few天气秀");
        diagnosisDiscount1.setContent("fgnr进水互换性加油站");
        diagnosisDiscounts.add(diagnosisDiscount1);
        DiagnosisDiscount diagnosisDiscount2 = new DiagnosisDiscount();
        diagnosisDiscount2.setSplitId(splitId);
        diagnosisDiscount2.setTitle("孤独睡眠");
        diagnosisDiscount2.setContent("旧调重弹申请尽管副教授");
        diagnosisDiscounts.add(diagnosisDiscount2);

        form.setDiagnosisDiscounts(diagnosisDiscounts);

        // 门店信息
        List<DiagnosisStoreInfo> diagnosisStoreInfos = new ArrayList<>();

        DiagnosisStoreInfo diagnosisStoreInfo1 = new DiagnosisStoreInfo();
        diagnosisStoreInfo1.setStoreInfoId(storeId);
        diagnosisStoreInfo1.setCityLevel("2");
        diagnosisStoreInfo1.setTrafficGuide("地铁1F圾号线fewFewff2a");
        diagnosisStoreInfo1.setConsumptionArea("60be96eff77611e7bbd1fa163eaf8427");
        diagnosisStoreInfo1.setConsumptionTypes("76c46439f77611e7bbd1fa163eaf8427,76c8b92af77611e7bbd1fa163eaf8427");
        diagnosisStoreInfo1.setPersonAvgPriceMax(3);
        diagnosisStoreInfo1.setPersonAvgPriceMin(1);
        diagnosisStoreInfo1.setTableAvgPersonNumMax(5);
        diagnosisStoreInfo1.setTableAvgPersonNumMin(4);
        diagnosisStoreInfo1.setAgeDistributionMax(33);
        diagnosisStoreInfo1.setAgeDistributionMin(30);
        diagnosisStoreInfo1.setGenderDistribution("8582f6d0f77611e7bbd1fa163eaf8427,8586e702f77611e7bbd1fa163eaf8427");
        diagnosisStoreInfo1.setOccupationDistributions("9516f760f77611e7bbd1fa163eaf8427,951c3004f77611e7bbd1fa163eaf8427");

        ErpStoreInfo erpStoreInfo1 = new ErpStoreInfo();
        erpStoreInfo1.setId(storeId);
        erpStoreInfo1.setProvince("120000");
        erpStoreInfo1.setProvinceName("天津市");
        erpStoreInfo1.setCity("120100");
        erpStoreInfo1.setCityName("市辖区");
        erpStoreInfo1.setArea("120102");
        erpStoreInfo1.setAreaName("河东区");
        erpStoreInfo1.setAddress("建设路122号");
        erpStoreInfo1.setTelephone("13455556666");

        diagnosisStoreInfo1.setErpStoreInfo(erpStoreInfo1);

        // 营业时间
        List<DiagnosisStoreBusinessHour> normalBusinessHours = new ArrayList<>();
        List<DiagnosisStoreBusinessHour> peakBusinessHours = new ArrayList<>();
        DiagnosisStoreBusinessHour normalBusinessHour = new DiagnosisStoreBusinessHour();
        normalBusinessHour.setBusinessType(DiagnosisConstant.BUSINESS_TYPE_NORMAL);
        normalBusinessHour.setStartTimeStr("3:30");
        normalBusinessHour.setEndTimeStr("5:00");
        normalBusinessHour.setWorkdays("2,5,6");
        normalBusinessHours.add(normalBusinessHour);

        diagnosisStoreInfo1.setNormalBusinessHours(normalBusinessHours);
        diagnosisStoreInfo1.setPeakBusinessHours(peakBusinessHours);

        diagnosisStoreInfos.add(diagnosisStoreInfo1);

        form.setDiagnosisStoreInfos(diagnosisStoreInfos);

        BaseResult baseResult = diagnosisFormService.saveByPhone(form);
        System.out.println(baseResult);
    }

    // 保存电话后阶段数据
    @Test
    public void saveAfterPhone() {
        String splitId = "ddb8666db96643aca1f3eee761d8719c";
        String storeId = "573cad3d6c464b0d802dd90639221d3c";

        DiagnosisForm form = new DiagnosisForm();
        // 表单
        form.setSplitId(splitId);
        form.setDiagnosisContentAdditional("ffee电光火石慢聊敢去博采众家之长32314");
        form.setReferenceMaterial("fega日光灯发出来导致黑豹淡淡1236");

        // 行业属性
        List<DiagnosisSplitIndustryAttribute> splitIndustryAttributes = new ArrayList<>();
        splitIndustryAttributes.add(new DiagnosisSplitIndustryAttribute(splitId, "2"));
        splitIndustryAttributes.add(new DiagnosisSplitIndustryAttribute(splitId, "25"));
        form.setSplitIndustryAttributes(splitIndustryAttributes);

        // 门店大众点评信息
        List<DiagnosisStoreInfo> storeInfos = new ArrayList<>();
        DiagnosisStoreInfo storeInfo1 = new DiagnosisStoreInfo();
        storeInfo1.setStoreInfoId(storeId);
        storeInfo1.setDianpingLink("https://www.taobao.com");
        storeInfo1.setDianpingRanking("韩服fe2");
        storeInfo1.setDianpingStoreName("gr34电灯泡臂力");
        storeInfos.add(storeInfo1);
        form.setDiagnosisStoreInfos(storeInfos);

        BaseResult baseResult = diagnosisFormService.saveAfterPhone(form);
        System.out.println(baseResult);
    }

    // 保存优惠形式和内容数据
    @Test
    public void saveDiscountTypeContent() {
        String splitId = "ddb8666db96643aca1f3eee761d8719c";
        DiagnosisForm form = new DiagnosisForm();
        form.setSplitId(splitId);
        form.setMainPush("博采众家之长敢吃2e");
        form.setBackupFirst("few进行淡淡3");
        form.setBackupSecond("老朋友少数人臂力f2");
        form.setPushArea("悄悄电光火石卫生所f23a");
        form.setPromotionTime(new Date());

        // 优惠形式
        List<DiagnosisDiscountTypeRecommend> recommends = new ArrayList<>();
        DiagnosisDiscountTypeRecommend recommend1 = new DiagnosisDiscountTypeRecommend();
        recommend1.setChecked("Y");
        recommend1.setDiscountTypeId("282c1a3ad9f34640a0774c9975e7a3c3");
        recommend1.setRecommendScore(4);
        DiagnosisDiscountTypeRecommend recommend2 = new DiagnosisDiscountTypeRecommend();
        recommend2.setChecked("N");
        recommend2.setDiscountTypeId("8df566d5c8a641b095a60ede8385a434");
        recommend2.setRecommendScore(6);
        DiagnosisDiscountTypeRecommend recommend3 = new DiagnosisDiscountTypeRecommend();
        recommend3.setChecked("N");
        recommend3.setDiscountTypeId("e3c95c793bbf41a0ba854dbbf29719ec");
        recommend3.setRecommendScore(8);
        recommends.add(recommend1);
        recommends.add(recommend2);
        recommends.add(recommend3);
        form.setDiscountTypeRecommends(recommends);

        BaseResult baseResult = diagnosisFormService.saveDiscountTypeContent(form);
        System.out.println(baseResult);
    }

    /**
     * OK
     *
     * @date 2018年4月15日
     */
    @Test
    public void savePreparationStageDataTest() {
        PreparationStageRequestDto dto = new PreparationStageRequestDto();
        dto.setSplitId("5d544a8cc7a04130a605e83689161d76");

        // 1.更新商户信息表的城市级别
        dto.setCityLevel("2");

        // 2.更新大表单(save,update)
        dto.setPackageAdditional("测试Darren");
        dto.setServiceKnow("know");

        List<ErpOrderSplitGood> erpOrderSplitGoods = new ArrayList<>();

        // 3.修改拆单商品
        ErpOrderSplitGood good1 = new ErpOrderSplitGood();
        good1.setId("04820e71087c4945aebf9f6dbef6a4c7");
        good1.setBuyExposure(new BigDecimal(10));
        good1.setDonateExposure(new BigDecimal(0.1));

        ErpOrderSplitGood good2 = new ErpOrderSplitGood();
        good2.setId("33fe2f33cbde427f83972496bb5da947");
        good2.setBuyExposure(new BigDecimal(100000000));
        good2.setDonateExposure(new BigDecimal(111.2));

        ErpOrderSplitGood good3 = new ErpOrderSplitGood();
        good3.setId("68a80139fb1d457c8ceb47bec0616d26");
        good3.setBuyExposure(new BigDecimal(10));
        good3.setDonateExposure(new BigDecimal(0.9));

        erpOrderSplitGoods.add(good1);
        erpOrderSplitGoods.add(good2);
        erpOrderSplitGoods.add(good3);
        dto.setErpOrderSplitGoods(erpOrderSplitGoods);

        diagnosisFormService.savePreparationStageData(dto);
    }

    /**
     * OK
     *
     * @date 2018年4月15日
     */
    @Test
    public void saveCouponsPlanDataTest() {
        CouponsPlanRequestDto dto = new CouponsPlanRequestDto();
        dto.setSplitId("fd9dfdeb6a224f6e99ce932dc18ac25a");

        // 1.测试大表单（save,update）
        dto.setShopUsername("testDarren");
        dto.setShopPassword(AESUtil.encrypt("12345689"));

        List<DiagnosisCardCoupons> diagnosisCardCoupons = new ArrayList<>();
        // 2.测试更新卡券
        DiagnosisCardCoupons e = new DiagnosisCardCoupons();
        e.setId("6d9462e78a5e460e84b8043ef12c0bd0");
        e.setSplitId("fd9dfdeb6a224f6e99ce932dc18ac25a");
        e.setShopName("名称Darren");
        e.setCouponType("gift_vouchers");
        e.setUseThreshold("满二百元可用Darren");
        e.setReduceAmount("60元");
        e.setCouponName("60元代金券");
        e.setInventory(60000);
        e.setLimitNum("一人二次6");
        e.setEffectiveTime("二个月6");
        e.setAvailableHours("卡券使用时间段6");
        e.setDescription("水电费第三方佳都科技6");
        e.setTerms("第三方第三方6");
        e.setPhoneNumber("13333333316");
        e.setFitStore("水电费水电费6");
        diagnosisCardCoupons.add(e);

        // 3.测试保存新卡券
        DiagnosisCardCoupons e2 = new DiagnosisCardCoupons();
        e2.setSplitId("fd9dfdeb6a224f6e99ce932dc18ac25a");
        e2.setShopName("哈哈6");
        e2.setCouponType("discount_vouchers");
        e2.setUseThreshold("满一亿元可用6");
        e2.setReduceAmount("2000000000000元6");
        e2.setCouponName("2000000000000元代金券6");
        e2.setInventory(1000001336);
        e2.setLimitNum("一人N次6");
        e2.setEffectiveTime("一辈子6");
        e2.setAvailableHours("卡券使用时间段6");
        e2.setDescription("你妈妈妈妈妈妈吗6");
        e2.setTerms("我去去去去去去去6");
        e2.setPhoneNumber("13333333316");
        e2.setFitStore("哈哈哈哈哈6");
        diagnosisCardCoupons.add(e2);
        dto.setDiagnosisCardCoupons(diagnosisCardCoupons);

        // 4.测试删除卡券
        List<String> removedCardCouponsIds = new ArrayList<>();
        removedCardCouponsIds.add("0e66971d6d6742329f02e27a317565e2");
        dto.setRemovedCouponIds(removedCardCouponsIds);

        diagnosisFormService.saveCouponsPlanData(dto);
    }

    @Test
    public void savePropagandaKeyData() {
        PropagandaKeyRequestDto dto = new PropagandaKeyRequestDto();
        String splitId = "fd9dfdeb6a224f6e99ce932dc18ac25a";

        // 更新大表单(save,update)
        String firstPropagandaContent = "myTest6";
        String secondPropagandaContent = "myTest6";
        dto.setFirstPropagandaContent(firstPropagandaContent);
        dto.setSecondPropagandaContent(secondPropagandaContent);

        List<DiagnosisFirstAdImage> diagnosisFirstAdImages = new ArrayList<>();
        DiagnosisFirstAdImage firstAdImage = new DiagnosisFirstAdImage();
        firstAdImage.setSplitId("fd9dfdeb6a224f6e99ce932dc18ac25a");
        firstAdImage.setImgUrl("/test.jpg");
        diagnosisFirstAdImages.add(firstAdImage);

        List<DiagnosisSecondAdImage> diagnosisSecondAdImages = new ArrayList<>();
        DiagnosisSecondAdImage SecondAdImage = new DiagnosisSecondAdImage();
        diagnosisSecondAdImages.add(SecondAdImage);

        // 删除第一层图片
        List<String> removedFirstAdImgIds = new ArrayList<>();
        removedFirstAdImgIds.add("66e0822675a644d7b824a2634f250c38");
        removedFirstAdImgIds.add("fd9dfdeb6a224f6e99ce932dc182a223");

        // 删除第二层图片
        List<String> removedSecondAdImgIds = new ArrayList<>();
        removedSecondAdImgIds.add("5d699a2b198a4da6989306754b930fac");
        removedSecondAdImgIds.add("6367b2ef6296459a8da1392b9c1756be");

        dto.setDiagnosisFirstAdImages(diagnosisFirstAdImages);
        dto.setDiagnosisSecondAdImages(diagnosisSecondAdImages);

        dto.setRemovedFirstAdImgIds(removedFirstAdImgIds);
        dto.setRemovedSecondAdImgIds(removedSecondAdImgIds);
        dto.setSplitId(splitId);
        diagnosisFormService.savePropagandaKeyData(dto);
    }

    @Test
    public void findBySplitIdWithCreaterName() {
        String splitId = "0244df13885f4ecdabd2cc310e38fb8d";
        DiagnosisForm findBySplitIdWithCreaterName = diagnosisFormService.findBySplitId(splitId);
        System.out.println(findBySplitIdWithCreaterName);
    }

    @Test
    public void getPromotionData() {
        String splitId = "0244df13885f4ecdabd2cc310e38fb8d";
        DiagnosisForm diagnosisDataBySplitId = diagnosisFormService.getDiagnosisDataBySplitId(splitId);
        System.out.println(JSONUtils.valueToString(diagnosisDataBySplitId));
    }

    @Test
    public void getDiagnosisDataForModifying() {
        String splitId = "0244df13885f4ecdabd2cc310e38fb8d";
        BaseResult result = diagnosisFormService.getDiagnosisDataForModifying(splitId);
        System.out.println(JSONUtils.valueToString(result));
    }



}
