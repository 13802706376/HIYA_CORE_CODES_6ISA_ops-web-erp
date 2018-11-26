package com.yunnex.ops.erp.modules.diagnosis.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.utils.RegexUtils;
import com.yunnex.ops.erp.modules.diagnosis.constant.DiagnosisConstant;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisFirstAdImageDao;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisIndustryAttributeDao;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisSecondAdImageDao;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisCardCoupons;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisFirstAdImage;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisForm;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisIndustryAttribute;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisSecondAdImage;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisStoreBusinessHour;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisStoreInfo;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSplitInfoDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.shop.dao.ErpShopInfoDao;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.sys.entity.Dict;
import com.yunnex.ops.erp.modules.workflow.channel.dao.JykOrderPromotionChannelDao;
import com.yunnex.ops.erp.modules.workflow.channel.entity.JykOrderPromotionChannel;

@Service
public class DiagnosisFormValidateService {

    private static final Logger LOG = LoggerFactory.getLogger(DiagnosisFormValidateService.class);

    /**
     * 数据无效的错误码
     */
    public static final String DATA_INVALID_ERRORCODE = "-2";
    private static final int ONE = 1;
    private static final int THREE = 3;

    @Autowired
    private DiagnosisFormService diagnosisFormService;
    @Autowired
    private DiagnosisCardCouponsService diagnosisCardCouponsService;
    @Autowired
    private DiagnosisIndustryAttributeDao diagnosisIndustryAttributeDao;
    @Autowired
    private ErpOrderSplitInfoDao erpOrderSplitInfoDao;
    @Autowired
    private ErpShopInfoDao erpShopInfoDao;
    @Autowired
    private DiagnosisFirstAdImageDao diagnosisFirstAdImageDao;
    @Autowired
    private DiagnosisSecondAdImageDao diagnosisSecondAdImageDao;
    @Autowired
    private JykOrderPromotionChannelDao jykOrderPromotionChannelDao;
    
    public static final int NUMBER_10 = 10;
    public static final int NUMBER_20 = 20;
    
    
    /**
     * 经营诊断表单校验
     *
     * @return
     * @date 2018年4月9日
     */
    public BaseResult validateForm(String splitId) {
        LOG.info("经营诊断表单校验入参：split = {}", splitId);
        if (StringUtils.isBlank(splitId)) {
            return new IllegalArgumentErrorResult();
        }

        BaseResult result = null;

        // 电话前准备阶段
        result = validatePreparationStage(splitId);
        if (!BaseResult.isSuccess(result)) {
            return result;
        }

        // 电话过程中阶段
        result = validateByPhone(splitId);
        if (!BaseResult.isSuccess(result)) {
            return result;
        }

        // 校验行业属性
        result = validateIndustryAttribute(splitId);
        if (!BaseResult.isSuccess(result)) {
            return result;
        }

        // 校验卡券内容
        result = validateCouponsPlan(splitId);
        if (!BaseResult.isSuccess(result)) {
            return result;
        }

        // 校验宣传重点
        result = validatePropagandaKey(splitId);
        if (!BaseResult.isSuccess(result)) {
            return result;
        }

        return result;
    }

    private BaseResult validateByPhone(String splitId) {
        DiagnosisForm form = diagnosisFormService.findByPhone(splitId);

        BaseResult result = new BaseResult();

        if (form == null || form.getId() == null) {
            return result.error(DATA_INVALID_ERRORCODE, "表单未填写！");
        }

        if (StringUtils.isBlank(form.getContactPerson())) {
            return result.error(DATA_INVALID_ERRORCODE, "本次推广联系人未填写！");
        }

        String contactPhone = form.getContactPhone();
        if (StringUtils.isBlank(contactPhone)) {
            return result.error(DATA_INVALID_ERRORCODE, "本次推广联系人电话未填写！");
        } else if (RegexUtils.checkIsMobile(contactPhone) && !RegexUtils.checkMobile(contactPhone)) {
            return result.error(DATA_INVALID_ERRORCODE, "本次推广联系人手机号格式不正确！");
        } else if (!RegexUtils.checkPhone(contactPhone)) {
            return result.error(DATA_INVALID_ERRORCODE, "本次推广联系人固话格式不正确！");
        }

        // 属于第4步的字段
        if (StringUtils.isBlank(form.getMainPush())) {
            return result.error(DATA_INVALID_ERRORCODE, "主推未填写！");
        }

        // 校验门店信息
        result = validateStoreInfo(form);
        if (!BaseResult.isSuccess(result)) {
            return result;
        }

        // 校验活动主题/推广需求
        result = validateActivityRequirements(form.getActivityRequirementsList());
        if (!BaseResult.isSuccess(result)) {
            return result;
        }

        // 校验活动目的
        result = validateActivityGoals(form.getActivityGoalList());
        if (!BaseResult.isSuccess(result)) {
            return result;
        }

        return result;
    }

    /**
     * 校验门店信息
     *
     * @param form
     * @return
     * @date 2018年4月9日
     */
    private BaseResult validateStoreInfo(DiagnosisForm form) {
        BaseResult result = new BaseResult();

        if (form == null || form.getPromotionStoreNum() == 0) {
            return result;
        }

        List<DiagnosisStoreInfo> diagnosisStoreInfos = form.getDiagnosisStoreInfos();
        if (CollectionUtils.isEmpty(diagnosisStoreInfos)) {
            return result.error(DATA_INVALID_ERRORCODE, "门店资料未填写！");
        }

        for (DiagnosisStoreInfo storeInfo : diagnosisStoreInfos) {
            if (StringUtils.isEmpty(storeInfo.getConsumptionArea())) {
                return result.error(DATA_INVALID_ERRORCODE, "消费圈位置未选择！");
            }
            if (StringUtils.isEmpty(storeInfo.getConsumptionTypes())) {
                return result.error(DATA_INVALID_ERRORCODE, "消费圈类型未选择！");
            }

            String shopCityLevelStr = form.getShopCityLevel();
            String cityLevelStr = storeInfo.getCityLevel();
            if (StringUtils.isNotBlank(shopCityLevelStr) && StringUtils.isNotBlank(cityLevelStr)) {
                Integer shopCityLevel = Integer.valueOf(shopCityLevelStr);
                Integer cityLevel = Integer.valueOf(cityLevelStr);
                if (cityLevel > shopCityLevel) {
                    return result.error(DATA_INVALID_ERRORCODE, "门店的城市级别不能高于商户的城市级别！");
                }
            }

            result = validateBusinessHours(storeInfo.getNormalBusinessHours(), "营业时间");
            if (!BaseResult.isSuccess(result)) {
                return result;
            }

            if (storeInfo.getPersonAvgPriceMin() == null || storeInfo.getPersonAvgPriceMax() == null) {
                return result.error(DATA_INVALID_ERRORCODE, "人均客单价未填写！");
            } else if (storeInfo.getPersonAvgPriceMin() < 0 || storeInfo.getPersonAvgPriceMax() < 0) {
                return result.error(DATA_INVALID_ERRORCODE, "人均客单价不能小于0！");
            } else if (storeInfo.getPersonAvgPriceMin() > storeInfo.getPersonAvgPriceMax()) {
                return result.error(DATA_INVALID_ERRORCODE, "人均客单价结束值要大于或等于开始值！");
            }
            if (StringUtils.isEmpty(storeInfo.getGenderDistribution())) {
                return result.error(DATA_INVALID_ERRORCODE, "性别分布未选择！");
            }
            if (null == storeInfo.getAgeDistributionMin() || null == storeInfo.getAgeDistributionMax()) {
                return result.error(DATA_INVALID_ERRORCODE, "年龄分布未填写！");
            }
        }

        return result;
    }

    private static BaseResult validateBusinessHours(List<DiagnosisStoreBusinessHour> businessHours, String msg) {
        BaseResult result = new BaseResult();

        if (CollectionUtils.isEmpty(businessHours)) {
            return result.error(DATA_INVALID_ERRORCODE, msg + "未填写！");
        }

        for (DiagnosisStoreBusinessHour businessHour : businessHours) {
            if (StringUtils.isBlank(businessHour.getWorkdays())) {
                return result.error(DATA_INVALID_ERRORCODE, msg + " - 工作日未填写！");
            }
            if (StringUtils.isBlank(businessHour.getStartTimeStr())) {
                return result.error(DATA_INVALID_ERRORCODE, msg + " - 开始时间未填写！");
            }
            if (StringUtils.isBlank(businessHour.getEndTimeStr())) {
                return result.error(DATA_INVALID_ERRORCODE, msg + " - 结束时间未填写！");
            }
        }

        return result;
    }

    /**
     * 校验活动主题/推广需求
     *
     * @param data 电话诊断数据
     */
    private BaseResult validateActivityRequirements(List<Dict> activityRequirementsList) {
        BaseResult result = new BaseResult();

        if (CollectionUtils.isEmpty(activityRequirementsList)) {
            LOG.error("活动主题/推广需求数据异常！");
            return result.error(DATA_INVALID_ERRORCODE, "活动主题/推广需求数据异常！");
        }

        // 选择1-3个
        int checkedNum = 0;
        for (Dict d : activityRequirementsList) {
            if (d.getChecked()) {
                checkedNum++;
            }
        }
        if (checkedNum < ONE || checkedNum > THREE) {
            return result.error(DATA_INVALID_ERRORCODE, "活动主题/推广需求请选择1-3个！");
        }

        return result;
    }

    /**
     * 校验活动目的
     *
     * @param activityRequirementsList 电话诊断数据
     * @return
     */
    private BaseResult validateActivityGoals(List<Dict> activityRequirementsList) {
        BaseResult result = new BaseResult();

        if (CollectionUtils.isEmpty(activityRequirementsList)) {
            LOG.error("活动目的数据异常！");
            return result.error(DATA_INVALID_ERRORCODE, "活动目的数据异常！");
        }

        int checkedNum = 0;
        for (Dict d : activityRequirementsList) {
            if (d.getChecked()) {
                checkedNum++;
            }
        }
        if (checkedNum < 1) {
            return result.error(DATA_INVALID_ERRORCODE, "活动目的未选择！");
        }

        return result;
    }

    /**
     * 校验电话后诊断阶段数据
     *
     * @param splitId
     * @date 2018年4月9日
     */
    private BaseResult validateIndustryAttribute(String splitId) {
        BaseResult result = new BaseResult();

        List<DiagnosisIndustryAttribute> industryAttributes = diagnosisIndustryAttributeDao.findBySplitId(splitId);
        if (CollectionUtils.isEmpty(industryAttributes)) {
            return result.error(DATA_INVALID_ERRORCODE, "行业属性未选择！");
        }

        return result;

    }

    /**
     * 校验经营诊断1:电话前准备阶段
     *
     * @param splitId
     * @return
     * @date 2018年4月9日
     */
    private BaseResult validatePreparationStage(String splitId) {
        if (StringUtils.isBlank(splitId)) {
            return new IllegalArgumentErrorResult();
        }

        DiagnosisForm diagnosisForm = diagnosisFormService.findBySplitId(splitId);

        BaseResult result = new BaseResult();
        if (StringUtils.isBlank(diagnosisForm.getId())) {
            return result.error(DATA_INVALID_ERRORCODE, "表单未填写！");
        }

        // 校验商户产品／服务信息了解（大众点评）
        String serviceKnow = diagnosisForm.getServiceKnow();
        if (StringUtils.isBlank(serviceKnow)) {
            return result.error(DATA_INVALID_ERRORCODE, "商户产品／服务信息了解未填写！");
        }
        ErpOrderSplitInfo orderSplitInfo = erpOrderSplitInfoDao.getOrderSplitInfo(splitId);
        List<ErpOrderSplitGood> erpOrderSplitGoods = orderSplitInfo.getErpOrderSplitGoods();

        for (ErpOrderSplitGood erpOrderSplitGood : erpOrderSplitGoods) {
            BigDecimal buyExposure = erpOrderSplitGood.getBuyExposure();
            if (buyExposure == null) {
                return result.error(DATA_INVALID_ERRORCODE, "购买曝光量未填写！");
            }
            if (buyExposure.intValue() < 0) {
                return result.error(DATA_INVALID_ERRORCODE, "购买曝光量不能小于0！");
            }
        }

        // 校验商户城市级别信息
        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
        if (erpOrderSplitInfo == null) {
            return result.error(DATA_INVALID_ERRORCODE, "拆单信息数据有误！");
        }

        if (erpOrderSplitInfo != null) {
            ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());
            if (erpShopInfo == null) {
                return result.error(DATA_INVALID_ERRORCODE, "商户信息不存在！");
            }

            String cityLevel = erpShopInfo.getCityLevel();
            if (StringUtils.isBlank(cityLevel)) {
                return result.error(DATA_INVALID_ERRORCODE, "商户所在城市级别未填写！");
            }
        }

        return result;
    }

    private BaseResult validateCouponsPlan(String splitId) {
        BaseResult result = new BaseResult();
        if (StringUtils.isBlank(splitId)) {
            return new IllegalArgumentErrorResult();
        }

        DiagnosisForm diagnosisForm = diagnosisFormService.findBySplitId(splitId);

        // 判断选择的活动目的
        boolean isCouponsAttractCustomer = false;
        String activityGoal = diagnosisForm.getActivityGoal();
        if (StringUtils.isNotBlank(activityGoal)) {
            String[] activityGoals = activityGoal.split(",");
            for (String value : activityGoals) {
                if (DiagnosisConstant.COUPONS_ATTRACT_CUSTOMER.equals(value)) {
                    isCouponsAttractCustomer = true;
                    break;
                }
            }
        }
        if (isCouponsAttractCustomer) {
            // 获取卡券内容信息
            List<DiagnosisCardCoupons> diagnosisCardCoupons = diagnosisCardCouponsService.findBySplitId(splitId);
            if (CollectionUtils.isNotEmpty(diagnosisCardCoupons)) {
                for (DiagnosisCardCoupons cardCoupons : diagnosisCardCoupons) {
                    if (cardCoupons == null) {
                        return result.error(DATA_INVALID_ERRORCODE, "卡券内容未填写");
                    }
                    if (StringUtils.isBlank(cardCoupons.getShopName())) {
                        return result.error(DATA_INVALID_ERRORCODE, "商户名称未填写");
                    }

                    String couponType = cardCoupons.getCouponType();
                    if (couponType == null) {
                        return result.error(DATA_INVALID_ERRORCODE, "优惠券类型未填写");
                    }

                    // 选择的是礼品券 需要校验礼品券名称和每次最多可叠加几张
                    if (DiagnosisConstant.GIFT_VOUCHERS.equals(couponType)) {
                        if (StringUtils.isBlank(cardCoupons.getGiftCouponName())) {
                            return result.error(DATA_INVALID_ERRORCODE, "礼品券名称未填写");
                        }

                        if (StringUtils.isBlank(cardCoupons.getSuperpositionNum())) {
                            return result.error(DATA_INVALID_ERRORCODE, "每次最多可叠加几张未填写");
                        }
                    }

                    // 选择的是满减券需要校验使用门槛，减免金额，优惠券名称
                    if (DiagnosisConstant.FULL_MINUS_VOUCHERS.equals(couponType)) {
                        if (StringUtils.isBlank(cardCoupons.getCouponName())) {
                            return result.error(DATA_INVALID_ERRORCODE, "优惠券名称未填写");
                        }
                        if (cardCoupons.getUseThreshold() == null) {
                            return result.error(DATA_INVALID_ERRORCODE, "使用门槛未填写");
                        }
                        if (cardCoupons.getReduceAmount() == null) {
                            return result.error(DATA_INVALID_ERRORCODE, "减免金额未填写");
                        }
                    }

                    // 选择的是折扣券需要校验折扣券名称,折扣比例
                    if (DiagnosisConstant.DISCOUNT_VOUCHERS.equals(couponType)) {
                        if (StringUtils.isBlank(cardCoupons.getDiscountCouponName())) {
                            return result.error(DATA_INVALID_ERRORCODE, "折扣券名称未填写");
                        }
                        if (cardCoupons.getDiscountScale() == null) {
                            return result.error(DATA_INVALID_ERRORCODE, "折扣比例未填写");
                        }
                    }

                    if (cardCoupons.getInventory() == null) {
                        return result.error(DATA_INVALID_ERRORCODE, "总库存未填写");
                    }
                    if (StringUtils.isBlank(cardCoupons.getLimitNum())) {
                        return result.error(DATA_INVALID_ERRORCODE, "每人限领未填写");
                    }
                    if (StringUtils.isBlank(cardCoupons.getEffectiveTime())) {
                        return result.error(DATA_INVALID_ERRORCODE, "有效时间未填写");
                    }
                    if (StringUtils.isBlank(cardCoupons.getAvailableHours())) {
                        return result.error(DATA_INVALID_ERRORCODE, "可用时段未填写");
                    }
                    if (StringUtils.isBlank(cardCoupons.getDescription())) {
                        return result.error(DATA_INVALID_ERRORCODE, "详细描述未填写");
                    }
                    if (StringUtils.isBlank(cardCoupons.getTerms())) {
                        return result.error(DATA_INVALID_ERRORCODE, "使用须知未填写");
                    }
                    if (StringUtils.isBlank(cardCoupons.getPhoneNumber())) {
                        return result.error(DATA_INVALID_ERRORCODE, "客服电话未填写");
                    }
                    if (StringUtils.isBlank(cardCoupons.getFitStore())) {
                        return result.error(DATA_INVALID_ERRORCODE, "适合门店未填写");
                    }
                }
            }

            // 校验掌贝商户后台账号和密码
            String shopUsername = diagnosisForm.getShopUsername();
            if (StringUtils.isBlank(shopUsername)) {
                return result.error(DATA_INVALID_ERRORCODE, "掌贝商户后台账号未填写");
            }
            String shopPassword = diagnosisForm.getShopPassword();
            if (StringUtils.isBlank(shopPassword)) {
                return result.error(DATA_INVALID_ERRORCODE, "掌贝商户后台密码未填写");
            }
        }
        return result;
    }

    private BaseResult validatePropagandaKey(String splitId) {
        BaseResult result = new BaseResult();
        if (StringUtils.isBlank(splitId)) {
            return new IllegalArgumentErrorResult();
        }


        DiagnosisForm diagnosisForm = diagnosisFormService.findBySplitId(splitId);
        if (diagnosisForm == null) {
            return result.error(DATA_INVALID_ERRORCODE, "表单未填写！");
        }
        // 校验重点宣传文案
        String firstPropagandaContent = diagnosisForm.getFirstPropagandaContent();
        if (firstPropagandaContent == null) {
            return result.error(DATA_INVALID_ERRORCODE, "第一层重点宣传文案未填写");
        }

        String secondPropagandaContent = diagnosisForm.getSecondPropagandaContent();
        if (secondPropagandaContent == null) {
            return result.error(DATA_INVALID_ERRORCODE, "第二层重点宣传文案未填写");
        }

        // 获取推广通道
        List<JykOrderPromotionChannel> jykOrderPromotionChannels = jykOrderPromotionChannelDao.findListBySplitId(splitId);
        if (CollectionUtils.isNotEmpty(jykOrderPromotionChannels)) {
            for (JykOrderPromotionChannel jykOrderPromotionChannel : jykOrderPromotionChannels) {
                String promotionChannel = jykOrderPromotionChannel.getPromotionChannel();
                // 获取第一层重点宣传图片信息
                List<DiagnosisFirstAdImage> diagnosisFirstAdImages = diagnosisFirstAdImageDao.findBySplitIdAndType(splitId, promotionChannel);
                // 微信朋友圈(微信只能上传1张)
                if ("1".equals(promotionChannel)) {
                    if (diagnosisFirstAdImages == null) {
                        return result.error(DATA_INVALID_ERRORCODE, "第一层微信朋友圈广告图未上传");
                    }
                    if (diagnosisFirstAdImages.size() > 1) {
                        return result.error(DATA_INVALID_ERRORCODE, "第一层微信朋友圈广告图最多只能上传一张");
                    }
                }
                // 新浪微博(微博和陌陌第一层最多上传10张图片)
                if ("2".equals(promotionChannel)) {
                    if (diagnosisFirstAdImages == null) {
                        return result.error(DATA_INVALID_ERRORCODE, "第一层新浪微博广告图未上传");
                    }
                    if (diagnosisFirstAdImages.size() > NUMBER_10) {
                        return result.error(DATA_INVALID_ERRORCODE, "第一层新浪微博广告图最多只能上传十张");
                    }
                }
                // 陌陌
                if ("3".equals(promotionChannel)) {
                    if (diagnosisFirstAdImages == null) {
                        return result.error(DATA_INVALID_ERRORCODE, "第一层陌陌广告图未上传");
                    }
                    if (diagnosisFirstAdImages.size() > NUMBER_10) {
                        return result.error(DATA_INVALID_ERRORCODE, "第一层陌陌广告图最多只能上传十张");
                    }
                }
            }
        }

        // 获取第二层重点宣传图片信息
        List<DiagnosisSecondAdImage> diagnosisSecondAdImages = diagnosisSecondAdImageDao.findBySplitId(splitId);
        if (diagnosisSecondAdImages == null) {
            return result.error(DATA_INVALID_ERRORCODE, "第二层广告图未上传");
        }
        if (diagnosisSecondAdImages.size() > NUMBER_20) {
            return result.error(DATA_INVALID_ERRORCODE, "第二层广告图最多只能上传二十张");
        }
        return result;
    }

}
