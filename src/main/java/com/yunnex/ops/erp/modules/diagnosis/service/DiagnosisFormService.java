package com.yunnex.ops.erp.modules.diagnosis.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.result.IllegalArgumentErrorResult;
import com.yunnex.ops.erp.common.service.CrudService;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.AESUtil;
import com.yunnex.ops.erp.common.utils.FileUtils;
import com.yunnex.ops.erp.modules.diagnosis.constant.DiagnosisConstant;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisDiscountDao;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisDiscountTypeRecommendDao;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisFormDao;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisIndustryAttributeDao;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisSplitIndustryAttributeDao;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisStoreBusinessHourDao;
import com.yunnex.ops.erp.modules.diagnosis.dao.DiagnosisStoreInfoDao;
import com.yunnex.ops.erp.modules.diagnosis.dto.CouponsPlanRequestDto;
import com.yunnex.ops.erp.modules.diagnosis.dto.DiagnosisFormResponseDto;
import com.yunnex.ops.erp.modules.diagnosis.dto.DiagnosisRequestDto;
import com.yunnex.ops.erp.modules.diagnosis.dto.PreparationStageRequestDto;
import com.yunnex.ops.erp.modules.diagnosis.dto.PropagandaKeyRequestDto;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisCardCoupons;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisDiscount;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisDiscountTypeRecommend;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisFirstAdImage;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisForm;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisIndustryAttribute;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisSecondAdImage;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisSplitIndustryAttribute;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisStoreBusinessHour;
import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisStoreInfo;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSplitGoodDao;
import com.yunnex.ops.erp.modules.order.dao.ErpOrderSplitInfoDao;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpPromotionMaterialLog;
import com.yunnex.ops.erp.modules.order.service.ErpPromotionMaterialLogService;
import com.yunnex.ops.erp.modules.shop.dao.ErpShopInfoDao;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserFriendsService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import com.yunnex.ops.erp.modules.store.basic.dto.PublicAccountAndWeiboDto;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.sys.entity.Dict;
import com.yunnex.ops.erp.modules.sys.service.DictService;
import com.yunnex.ops.erp.modules.sys.utils.DictUtils;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.channel.dao.JykOrderPromotionChannelDao;
import com.yunnex.ops.erp.modules.workflow.channel.entity.JykOrderPromotionChannel;

/**
 * 经营诊断营销策划表单Service
 * 
 * @author yunnex
 * @version 2018-03-29
 */
@Service
public class DiagnosisFormService extends CrudService<DiagnosisFormDao, DiagnosisForm> {

    // 优惠形式推荐个数
    public static final int DISCOUNT_TYPE_RECOMMEND_NUM = 5;
    public static final String TO_CN = "至";
    public static final String TEN_THOUSAND = "万";
    public static final String BUY_EXPOSURE = "（购买曝光量）";
    public static final String DONATE_EXPOSURE = "（赠送曝光量）";

    // 资源域名
    @Value("${domain.erp.res}") // NOSONAR
    private String resDomain;

    // 图片保存目录
    @Value("${userfiles.basedir}") // NOSONAR
    private String basedir;

    @Autowired
    private DiagnosisFormDao diagnosisFormDao;
    @Autowired
    private DiagnosisStoreInfoDao diagnosisStoreInfoDao;
    @Autowired
    private DiagnosisIndustryAttributeDao diagnosisIndustryAttributeDao;
    @Autowired
    private JykOrderPromotionChannelDao jykOrderPromotionChannelDao;
    @Autowired
    private ErpOrderSplitInfoDao erpOrderSplitInfoDao;
    @Autowired
    private DiagnosisDiscountTypeRecommendDao diagnosisDiscountTypeRecommendDao;
    @Autowired
    private DiagnosisStoreBusinessHourDao diagnosisStoreBusinessHourDao;
    @Autowired
    private DiagnosisDiscountDao diagnosisDiscountDao;
    @Autowired
    private ErpOrderSplitGoodDao erpOrderSplitGoodDao;
    @Autowired
    private ErpShopInfoDao erpShopInfoDao;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private DiagnosisStoreInfoService diagnosisStoreInfoService;
    @Autowired
    private DiagnosisCardCouponsService diagnosisCardCouponsService;
    @Autowired
    private DiagnosisSplitIndustryAttributeDao splitIndustryAttributeDao;
    @Autowired
    private DiagnosisFirstAdImageService diagnosisFirstAdImageService;
    @Autowired
    private DiagnosisSecondAdImageService diagnosisSecondAdImageService;
    @Autowired
    private DictService dictService;
    @Autowired
    private ErpStoreAdvertiserFriendsService erpStoreAdvertiserFriendsService;
    @Autowired
    private ErpStoreAdvertiserWeiboService erpStoreAdvertiserWeiboService;
    @Autowired
    private ErpPromotionMaterialLogService erpPromotionMaterialLogService;

    @Override
    @Transactional(readOnly = false)
    public void save(DiagnosisForm diagnosisForm) {
        super.save(diagnosisForm);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(DiagnosisForm diagnosisForm) {
        super.delete(diagnosisForm);
    }

    /**
     * 获取电话诊断阶段数据
     *
     * @param splitId
     * @return
     * @date 2018年3月30日
     */
    public DiagnosisForm findByPhone(String splitId) {
        logger.info("获取电话诊断阶段数据入参：splitId = {}", splitId);
        if (StringUtils.isBlank(splitId)) {
            return null;
        }

        // 根据分单ID获取表单
        DiagnosisForm diagnosisForm = findBySplitId(splitId);

        // 本次推广套餐选择
        fillPackageInfos(diagnosisForm);

        // 以往及进行中的优惠内容
        List<DiagnosisDiscount> diagnosisDiscounts = diagnosisDiscountDao.findBySplitId(splitId);
        diagnosisForm.setDiagnosisDiscounts(diagnosisDiscounts);

        // 获取商户的城市级别
        ErpShopInfo erpShopInfo = erpShopInfoDao.findBySplitId(splitId);
        if (erpShopInfo != null) {
            diagnosisForm.setShopCityLevel(erpShopInfo.getCityLevel());
        }

        // 封装门店信息
        fillPromotionStoreInfo(diagnosisForm);

        // 活动主题/推广需求
        fillActivityRequirements(diagnosisForm);

        // 活动目的
        fillActivityGoal(diagnosisForm);

        logger.info("获取电话诊断阶段数据结果：{}", JSON.toJSON(diagnosisForm));
        return diagnosisForm;
    }

    /**
     * 活动目的
     *
     * @param diagnosisForm
     * @date 2018年3月30日
     */
    private void fillActivityGoal(DiagnosisForm diagnosisForm) {
        if (diagnosisForm == null) {
            return;
        }

        List<Dict> dictList = DictUtils.getDictList(DiagnosisConstant.ACTIVITY_GOAL_DICT_TYPE);
        // 异常值
        if (CollectionUtils.isEmpty(dictList)) {
            logger.error("数据字典 - 活动目的没有选项");
            diagnosisForm.setActivityGoalList(new ArrayList<Dict>());
            return;
        }

        List<String> goals = new ArrayList<>();
        String activityGoal = diagnosisForm.getActivityGoal();
        if (StringUtils.isNotBlank(activityGoal)) {
            goals = Arrays.asList(activityGoal.split(Constant.COMMA));
        }

        // 不能修改全局缓存中的数据, 因为其他线程也在使用同一个对象
        List<Dict> goalList = new ArrayList<>();
        for (Dict dict : dictList) {
            Dict clone = dict.clone();
            clone.setChecked(goals.contains(dict.getValue()));
            goalList.add(clone);
        }

        diagnosisForm.setActivityGoalList(goalList);
    }

    /**
     * 活动主题/推广需求
     *
     * @param diagnosisForm
     * @date 2018年3月30日
     */
    private void fillActivityRequirements(DiagnosisForm diagnosisForm) {
        if (diagnosisForm == null) {
            return;
        }

        List<Dict> dictList = DictUtils.getDictList(DiagnosisConstant.ACTIVITY_REQUIREMENT_DICT_TYPE);
        // 异常值
        if (CollectionUtils.isEmpty(dictList)) {
            logger.error("数据字典 - 推广需求没有选项");
            diagnosisForm.setActivityRequirementsList(new ArrayList<Dict>());
            return;
        }

        List<String> requirements = new ArrayList<>();
        String activityRequirements = diagnosisForm.getActivityRequirements();
        if (StringUtils.isNotBlank(activityRequirements)) {
            requirements = Arrays.asList(activityRequirements.split(Constant.COMMA));
        }

        List<Dict> requirementList = new ArrayList<>();
        for (Dict dict : dictList) {
            Dict clone = dict.clone();
            clone.setChecked(requirements.contains(dict.getId()));
            requirementList.add(clone);
        }

        diagnosisForm.setActivityRequirementsList(requirementList);
    }

    /**
     * 封装门店信息
     *
     * @param diagnosisForm
     * @date 2018年3月30日
     */
    public void fillPromotionStoreInfo(DiagnosisForm diagnosisForm) {
        checkFormSplitId(diagnosisForm);

        List<DiagnosisStoreInfo> promotionStores = diagnosisStoreInfoDao.getPromotionStores(diagnosisForm.getSplitId());

        if (CollectionUtils.isEmpty(promotionStores)) {
            diagnosisForm.setPromotionStoreNum(0);
            return;
        }

        for (DiagnosisStoreInfo diagnosisStoreInfo : promotionStores) {
            // 封装城市级别
            fillCityLevel(diagnosisStoreInfo);

            // 封装消费圈位置信息
            fillStoreConsumptionArea(diagnosisStoreInfo);

            // 封装消费圈类型信息
            fillStoreConsumptionTypes(diagnosisStoreInfo);

            // 封装门店营业时间
            fillStoreBusinessHour(diagnosisStoreInfo);

            // 封装职业分布信息
            fillStoreOccupationDistributions(diagnosisStoreInfo);

            // 封装性别分布信息
            fillStoreGenderDistribution(diagnosisStoreInfo);
        }

        diagnosisForm.setDiagnosisStoreInfos(promotionStores);
        diagnosisForm.setPromotionStoreNum(promotionStores.size());
    }

    /**
     * 封装门店信息
     *
     * @param diagnosisForm
     * @date 2018年3月30日
     */
    public void fillPromotionStoreInfoForModifying(DiagnosisForm diagnosisForm) {
        checkFormSplitId(diagnosisForm);

        List<DiagnosisStoreInfo> promotionStores = diagnosisStoreInfoDao.getPromotionStores(diagnosisForm.getSplitId());

        if (CollectionUtils.isEmpty(promotionStores)) {
            diagnosisForm.setPromotionStoreNum(0);
            return;
        }

        // 封装公众号和微博
        encapsulatePublicAccountAndWeiboInfo(promotionStores);

        for (DiagnosisStoreInfo diagnosisStoreInfo : promotionStores) {
            // 封装门店营业时间
            fillStoreBusinessHourForModifying(diagnosisStoreInfo);
        }

        diagnosisForm.setDiagnosisStoreInfos(promotionStores);
        diagnosisForm.setPromotionStoreNum(promotionStores.size());
    }

    /**
     * 填充门店的职业分布信息
     *
     * @param diagnosisStoreInfo 门店信息
     * @date 2018年1月16日
     * @author zhangjl
     */
    private void fillStoreOccupationDistributions(DiagnosisStoreInfo diagnosisStoreInfo) {
        if (diagnosisStoreInfo == null) {
            return;
        }

        List<Dict> list = DictUtils.getDictList(DiagnosisConstant.OCCUPATION_DISTRIBUTION_DICT_TYPE);
        // 异常值
        if (CollectionUtils.isEmpty(list)) {
            logger.error("数据字典 - 职业分布没有选项");
            diagnosisStoreInfo.setOccupationDistributionsList(new ArrayList<Dict>());
            return;
        }

        List<String> occupations = new ArrayList<>();
        String occupationDistributions = diagnosisStoreInfo.getOccupationDistributions();
        if (StringUtils.isNotBlank(occupationDistributions)) {
            occupations = Arrays.asList(occupationDistributions.split(Constant.COMMA));
        }

        List<Dict> occupationList = new ArrayList<>();
        for (Dict d : list) {
            Dict clone = d.clone();
            clone.setChecked(occupations.contains(d.getId()));
            occupationList.add(clone);
        }
        diagnosisStoreInfo.setOccupationDistributionsList(occupationList);
    }

    /**
     * 填充门店的性别分布信息
     *
     * @param diagnosisStoreInfo 门店信息
     * @date 2018年1月16日
     * @author zhangjl
     */
    private void fillStoreGenderDistribution(DiagnosisStoreInfo diagnosisStoreInfo) {
        if (diagnosisStoreInfo == null) {
            return;
        }

        List<Dict> list = DictUtils.getDictList(DiagnosisConstant.GENDER_DISTRIBUTION_DICT_TYPE);
        // 异常值
        if (CollectionUtils.isEmpty(list)) {
            logger.error("数据字典 - 性别分布没有选项");
            diagnosisStoreInfo.setGenderDistributionList(new ArrayList<Dict>());
            return;
        }

        List<Dict> genderList = new ArrayList<>();
        for (Dict d : list) {
            Dict clone = d.clone();
            clone.setChecked(null != diagnosisStoreInfo.getGenderDistribution() && diagnosisStoreInfo.getGenderDistribution().equals(d.getId()));
            genderList.add(clone);
        }
        diagnosisStoreInfo.setGenderDistributionList(genderList);
    }


    /**
     * 封装门店城市级别信息
     *
     * @param diagnosisStoreInfo
     * @date 2018年3月30日
     */
    private void fillCityLevel(DiagnosisStoreInfo diagnosisStoreInfo) {
        if (diagnosisStoreInfo == null) {
            return;
        }

        List<Dict> dictList = DictUtils.getDictList(DiagnosisConstant.CITY_LEVEL_DICT_TYPE);
        if (CollectionUtils.isEmpty(dictList)) {
            logger.error("数据字典 - 城市级别没有选项");
            diagnosisStoreInfo.setCityLevels(new ArrayList<Dict>());
            return;
        }

        List<Dict> cityLevelList = new ArrayList<>();
        for (Dict dict : dictList) {
            Dict clone = dict.clone();
            clone.setChecked(diagnosisStoreInfo.getCityLevel() != null && diagnosisStoreInfo.getCityLevel().equals(dict.getValue()));
            cityLevelList.add(clone);
        }

        diagnosisStoreInfo.setCityLevels(cityLevelList);
    }

    /**
     * 封装消费圈类型
     *
     * @param diagnosisStoreInfo
     * @date 2018年3月30日
     */
    private void fillStoreConsumptionTypes(DiagnosisStoreInfo diagnosisStoreInfo) {
        if (diagnosisStoreInfo == null) {
            return;
        }

        List<Dict> dictList = DictUtils.getDictList(DiagnosisConstant.CONSUMPTION_TYPE_DICT_TYPE);
        if (CollectionUtils.isEmpty(dictList)) {
            logger.error("数据字典 - 消费圈类型没有选项");
            diagnosisStoreInfo.setConsumptionTypesList(new ArrayList<Dict>());
            return;
        }

        List<String> strTypeList = new ArrayList<>();
        if (StringUtils.isNotBlank(diagnosisStoreInfo.getConsumptionTypes())) {
            String[] types = diagnosisStoreInfo.getConsumptionTypes().split(Constant.COMMA);
            strTypeList = Arrays.asList(types);
        }

        // 不能修改全局缓存中的数据
        List<Dict> typeList = new ArrayList<>();
        for (Dict dict : dictList) {
            Dict clone = dict.clone();
            clone.setChecked(strTypeList.contains(dict.getId()));
            typeList.add(clone);
        }

        diagnosisStoreInfo.setConsumptionTypesList(typeList);
    }


    /**
     * 填充门店的消费圈位置信息
     *
     * @date 2018年1月16日
     * @author zhangjl
     */
    private void fillStoreConsumptionArea(DiagnosisStoreInfo diagnosisStoreInfo) {
        if (diagnosisStoreInfo == null) {
            return;
        }

        List<Dict> list = DictUtils.getDictList(DiagnosisConstant.CONSUMPTION_AREA_DICT_TYPE);
        // 异常值
        if (CollectionUtils.isEmpty(list)) {
            logger.error("数据字典 - 消费圈位置没有选项");
            diagnosisStoreInfo.setConsumptionAreaList(new ArrayList<Dict>());
            return;
        }

        List<Dict> consumptionList = new ArrayList<>();
        for (Dict d : list) {
            Dict clone = d.clone();
            clone.setChecked(null != diagnosisStoreInfo.getConsumptionArea() && diagnosisStoreInfo.getConsumptionArea().equals(d.getId()));
            consumptionList.add(clone);
        }
        diagnosisStoreInfo.setConsumptionAreaList(consumptionList);
    }

    private List<Dict> getWorkDays() {
        List<Dict> list = DictUtils.getDictList(DiagnosisConstant.WORKDAY_DICT_TYPE);
        // 异常值
        if (CollectionUtils.isEmpty(list)) {
            logger.error("数据字典 - 工作日没有选项");
            return new ArrayList<Dict>();
        }

        List<Dict> workdayList = new ArrayList<>();
        for (Dict dict : list) {
            Dict clone = dict.clone();
            clone.setChecked(false);
            workdayList.add(clone);
        }

        return workdayList;
    }

    /**
     * 封装门店营业时间
     *
     * @date 2018年3月30日
     */
    public void fillStoreBusinessHour(DiagnosisStoreInfo diagnosisStoreInfo) {
        if (diagnosisStoreInfo == null) {
            return;
        }

        List<DiagnosisStoreBusinessHour> businessHours = diagnosisStoreBusinessHourDao.findByStoreInfoId(diagnosisStoreInfo.getStoreInfoId());

        List<DiagnosisStoreBusinessHour> normalBusinessHours = new ArrayList<>();
        List<DiagnosisStoreBusinessHour> peakBusinessHours = new ArrayList<>();
        diagnosisStoreInfo.setNormalBusinessHours(normalBusinessHours);
        diagnosisStoreInfo.setPeakBusinessHours(peakBusinessHours);

        List<Dict> workdayDict = getWorkDays();
        // 没有填写营业时间时，返回初始数据
        // 初始化周，没有选择任何工作日时，显示没有勾选的周
        DiagnosisStoreBusinessHour normalBusinessHour = new DiagnosisStoreBusinessHour();
        normalBusinessHour.setWorkdayList(workdayDict);
        normalBusinessHour.setBusinessType(DiagnosisConstant.BUSINESS_TYPE_NORMAL);
        DiagnosisStoreBusinessHour peakBusinessHour = normalBusinessHour.clone();
        peakBusinessHour.setBusinessType(DiagnosisConstant.BUSINESS_TYPE_PEAK);

        normalBusinessHours.add(normalBusinessHour);
        peakBusinessHours.add(peakBusinessHour);

        if (CollectionUtils.isEmpty(businessHours)) {
            return;
        }

        for (DiagnosisStoreBusinessHour businessHour : businessHours) {
            businessHour.setStartTimeStr(DiagnosisStoreBusinessHour.format2HHmm(businessHour.getStartTime()));
            businessHour.setEndTimeStr(DiagnosisStoreBusinessHour.format2HHmm(businessHour.getEndTime()));
            String workdays = businessHour.getWorkdays();
            List<Dict> workdayList = new ArrayList<>();
            businessHour.setWorkdayList(workdayList);
            for (Dict dict : workdayDict) {
                Dict clone = dict.clone();
                clone.setChecked(StringUtils.isNoneBlank(workdays) && workdays.contains(clone.getValue()));
                workdayList.add(clone);
            }

            if (DiagnosisConstant.BUSINESS_TYPE_NORMAL.equals(businessHour.getBusinessType())) {
                normalBusinessHours.add(businessHour);
            } else if (DiagnosisConstant.BUSINESS_TYPE_PEAK.equals(businessHour.getBusinessType())) {
                peakBusinessHours.add(businessHour);
            }
        }

        // 如果用户有填写过营业时间,去掉初始数据
        if (normalBusinessHours.size() > 1) {
            normalBusinessHours.remove(0);
        }
        if (peakBusinessHours.size() > 1) {
            peakBusinessHours.remove(0);
        }
    }

    public void fillStoreBusinessHourForModifying(DiagnosisStoreInfo diagnosisStoreInfo) {
        if (diagnosisStoreInfo == null) {
            return;
        }

        List<DiagnosisStoreBusinessHour> businessHours = diagnosisStoreBusinessHourDao.findByStoreInfoId(diagnosisStoreInfo.getStoreInfoId());

        if (CollectionUtils.isEmpty(businessHours)) {
            return;
        }

        List<DiagnosisStoreBusinessHour> normalBusinessHours = new ArrayList<>();
        List<DiagnosisStoreBusinessHour> peakBusinessHours = new ArrayList<>();
        for (DiagnosisStoreBusinessHour businessHour : businessHours) {
            businessHour.setStartTimeStr(DiagnosisStoreBusinessHour.format2HHmm(businessHour.getStartTime()));
            businessHour.setEndTimeStr(DiagnosisStoreBusinessHour.format2HHmm(businessHour.getEndTime()));
            if (DiagnosisConstant.BUSINESS_TYPE_NORMAL.equals(businessHour.getBusinessType())) {
                normalBusinessHours.add(businessHour);
            } else if (DiagnosisConstant.BUSINESS_TYPE_PEAK.equals(businessHour.getBusinessType())) {
                peakBusinessHours.add(businessHour);
            }
        }
        diagnosisStoreInfo.setNormalBusinessHours(normalBusinessHours);
        diagnosisStoreInfo.setPeakBusinessHours(peakBusinessHours);
    }

    /**
     * 保存电话诊断阶段数据
     *
     * @param form
     * @return
     * @date 2018年4月2日
     */
    @Transactional
    public BaseResult saveByPhone(DiagnosisForm form) {
        logger.info("保存电话诊断阶段数据入参: DiagnosisForm = {}", JSON.toJSON(form));

        // 保存表单数据
        saveDiagnosisFormByPhone(form);

        // 保存选择的推广套餐
        savePackageInfo(form);

        // 删除然后保存优惠内容
        saveDiagnosisDiscount(form);

        // 保存门店信息
        saveDiagnosisStoreInfos(form);

        return new BaseResult();
    }

    /**
     * 保存电话诊断阶段表单数据
     *
     * @param form
     * @date 2018年4月3日
     */
    public void saveDiagnosisFormByPhone(DiagnosisForm form) {
        checkFormSplitId(form);

        // 修改之前的推广需求
        String activityRequirementsBefore = null;
        String splitId = form.getSplitId();
        DiagnosisForm diagnosisFormDB = diagnosisFormDao.findBySplitId(splitId);
        if (diagnosisFormDB == null) {
            diagnosisFormDB = new DiagnosisForm();
        }
        activityRequirementsBefore = diagnosisFormDB.getActivityRequirements();

        diagnosisFormDB.setSplitId(splitId);
        diagnosisFormDB.setContactPerson(form.getContactPerson());
        diagnosisFormDB.setContactPhone(form.getContactPhone());
        diagnosisFormDB.setMajorProduct(form.getMajorProduct());
        diagnosisFormDB.setPromoteProduct(form.getPromoteProduct());
        diagnosisFormDB.setBrandLightspot(form.getBrandLightspot());
        diagnosisFormDB.setOriginalityCulture(form.getOriginalityCulture());
        diagnosisFormDB.setActivityRequirements(form.getActivityRequirements());
        diagnosisFormDB.setActivityGoal(form.getActivityGoal());

        this.save(diagnosisFormDB);

        // 删除优惠形式推荐（在此处理破坏了职责单一原则）
        deleteDiscountTypeRecommend(activityRequirementsBefore, form.getActivityRequirements(), splitId);
    }

    /**
     * 如果推广需求修改了，删除优惠形式推荐，因为推荐是根据推广需求和行业属性计算出来的
     * 
     * @param activityRequirementsBefore 修改之前的推广需求
     * @param activityRequirementsAfter 修改之后的推广需求
     */
    private void deleteDiscountTypeRecommend(String activityRequirementsBefore, String activityRequirementsAfter, String splitId) {
        String[] beforeArr = stringToSortArray(activityRequirementsBefore);
        String[] afterArr = stringToSortArray(activityRequirementsAfter);

        if (!Arrays.equals(beforeArr, afterArr)) {
            diagnosisDiscountTypeRecommendDao.deleteBySplitId(splitId);
        }
    }

    /**
     * 转换成数组，排序字符串
     * 
     * @param str
     * @return
     */
    private String[] stringToSortArray(String str) {
        if (StringUtils.isEmpty(str)) {
            return new String[] {};
        }

        String[] strArr = str.split(Constant.COMMA);
        Arrays.sort(strArr);

        return strArr;
    }

    /**
     * 对必要的分单ID进行检查
     *
     * @param form
     */
    public void checkFormSplitId(DiagnosisForm form) {
        if (form == null || StringUtils.isBlank(form.getSplitId())) {
            throw new IllegalArgumentException("表单没有填写或者没有指定分单ID！");
        }
    }

    /**
     * 保存选择的推广套餐
     * 
     * @param form
     */
    public void savePackageInfo(DiagnosisForm form) {
        checkFormSplitId(form);
        String packageSelection = form.getPackageSelection();

        String splitId = form.getSplitId();
        // 先全部更新为不推广
        ErpOrderSplitGood splitGood = new ErpOrderSplitGood();
        splitGood.setOriginalSplitId(splitId);
        splitGood.setIsPromote(Constant.NO);
        splitGood.preUpdate();
        erpOrderSplitGoodDao.updatePromotionBySplitId(splitGood);

        // 再将用户选择的更新为推广
        if (StringUtils.isNotBlank(packageSelection)) {
            List<String> ids = Arrays.asList(packageSelection.split(Constant.COMMA));
            erpOrderSplitGoodDao.updatePromotionByIds(ids);
        }
    }

    /**
     * 删除然后保存优惠内容
     *
     * @param form
     */
    public void saveDiagnosisDiscount(DiagnosisForm form) {
        checkFormSplitId(form);

        diagnosisDiscountDao.deleteBySplitId(form.getSplitId());
        logger.info("修改经营诊断的优惠内容, 先物理删除, 后批量保存. splitId = {}", form.getSplitId());
        List<DiagnosisDiscount> diagnosisDiscounts = form.getDiagnosisDiscounts();
        if (CollectionUtils.isNotEmpty(diagnosisDiscounts)) {
            for (DiagnosisDiscount diagnosisDiscount : diagnosisDiscounts) {
                // 生成UUID
                diagnosisDiscount.setIsNewRecord(false);
                diagnosisDiscount.preInsert();
            }
            diagnosisDiscountDao.saveBatch(diagnosisDiscounts);
        }
    }

    /**
     * 保存门店信息
     *
     * @param form
     */
    public void saveDiagnosisStoreInfos(DiagnosisForm form) {
        checkFormSplitId(form);

        List<DiagnosisStoreInfo> diagnosisStoreInfos = form.getDiagnosisStoreInfos();
        if (CollectionUtils.isEmpty(diagnosisStoreInfos)) {
            return;
        }

        for (DiagnosisStoreInfo diagnosisStoreInfo : diagnosisStoreInfos) {
            // 保存ERP门店信息
            saveErpStoreInfo(diagnosisStoreInfo);

            // 保存经营诊断门店信息
            saveDiagnosisStoreInfo(diagnosisStoreInfo);

            // 保存门店营业时间
            saveDiagnosisStoreBusinessHour(diagnosisStoreInfo);
        }
    }

    /**
     * 更新门店信息
     *
     * @param form
     */
    public void updateDiagnosisStoreInfos(DiagnosisForm form) {
        List<DiagnosisStoreInfo> diagnosisStoreInfos = form.getDiagnosisStoreInfos();
        if (CollectionUtils.isEmpty(diagnosisStoreInfos)) {
            return;
        }

        for (DiagnosisStoreInfo diagnosisStoreInfo : diagnosisStoreInfos) {
            // 保存ERP门店信息
            saveErpStoreInfo(diagnosisStoreInfo);

            // 保存经营诊断门店信息
            updateDiagnosisStoreInfo(diagnosisStoreInfo);

            // 保存门店营业时间
            saveDiagnosisStoreBusinessHour(diagnosisStoreInfo);

            // 更新公众号和微博
            updatePublicAccountAndWeibo(diagnosisStoreInfo);
        }
    }

    /**
     * 保存门店营业时间
     *
     * @param diagnosisStoreInfo
     */
    public void saveDiagnosisStoreBusinessHour(DiagnosisStoreInfo diagnosisStoreInfo) {
        checkStoreInfoId(diagnosisStoreInfo);

        // 先删除营业时间信息
        String storeInfoId = diagnosisStoreInfo.getStoreInfoId();
        diagnosisStoreBusinessHourDao.deleteByStoreInfoId(storeInfoId);
        logger.info("修改经营诊断的门店营业时间, 先物理删除, 后批量保存. storeInfoId = {}", storeInfoId);

        List<DiagnosisStoreBusinessHour> result = new ArrayList<>();
        List<DiagnosisStoreBusinessHour> normalBusinessHours = diagnosisStoreInfo.getNormalBusinessHours();
        List<DiagnosisStoreBusinessHour> peakBusinessHours = diagnosisStoreInfo.getPeakBusinessHours();

        processBusinessHour(normalBusinessHours, result, storeInfoId);
        processBusinessHour(peakBusinessHours, result, storeInfoId);

        if (CollectionUtils.isNotEmpty(result)) {
            diagnosisStoreBusinessHourDao.saveBatch(result);
        }
    }

    public void processBusinessHour(List<DiagnosisStoreBusinessHour> businessHours, List<DiagnosisStoreBusinessHour> result, String storeInfoId) {
        if (CollectionUtils.isEmpty(businessHours) || null == result) {
            return;
        }
        for (DiagnosisStoreBusinessHour businessHour : businessHours) {
            businessHour.setIsNewRecord(false);
            businessHour.preInsert();
            businessHour.setStoreInfoId(storeInfoId);
            businessHour.setStartTime(DiagnosisStoreBusinessHour.format2Int(businessHour.getStartTimeStr()));
            businessHour.setEndTime(DiagnosisStoreBusinessHour.format2Int(businessHour.getEndTimeStr()));
            result.add(businessHour);
        }
    }

    /**
     * 对必要的门店ID进行检查
     *
     * @param diagnosisStoreInfo
     */
    public void checkStoreInfoId(DiagnosisStoreInfo diagnosisStoreInfo) {
        if (diagnosisStoreInfo == null || StringUtils.isBlank(diagnosisStoreInfo.getStoreInfoId())) {
            throw new IllegalArgumentException("DiagnosisStoreInfo的ERP门店ID不能为空！");
        }
    }

    /**
     * 保存Diagnosis门店信息
     *
     * @param diagnosisStoreInfo
     */
    public void saveDiagnosisStoreInfo(DiagnosisStoreInfo diagnosisStoreInfo) {
        checkStoreInfoId(diagnosisStoreInfo);

        String storeInfoId = diagnosisStoreInfo.getStoreInfoId();
        // 根据门店ID查找经营诊断的门店信息
        DiagnosisStoreInfo diagnosisStoreInfoDB = diagnosisStoreInfoDao.findPromotionStore(storeInfoId);
        if (diagnosisStoreInfoDB == null) {
            diagnosisStoreInfoDB = new DiagnosisStoreInfo();
        }
        diagnosisStoreInfoDB.setStoreInfoId(storeInfoId);
        diagnosisStoreInfoDB.setCityLevel(diagnosisStoreInfo.getCityLevel());
        diagnosisStoreInfoDB.setTrafficGuide(diagnosisStoreInfo.getTrafficGuide());
        diagnosisStoreInfoDB.setConsumptionArea(diagnosisStoreInfo.getConsumptionArea());
        diagnosisStoreInfoDB.setConsumptionTypes(diagnosisStoreInfo.getConsumptionTypes());
        diagnosisStoreInfoDB.setPersonAvgPriceMax(diagnosisStoreInfo.getPersonAvgPriceMax());
        diagnosisStoreInfoDB.setPersonAvgPriceMin(diagnosisStoreInfo.getPersonAvgPriceMin());
        diagnosisStoreInfoDB.setTableAvgPersonNumMax(diagnosisStoreInfo.getTableAvgPersonNumMax());
        diagnosisStoreInfoDB.setTableAvgPersonNumMin(diagnosisStoreInfo.getTableAvgPersonNumMin());
        diagnosisStoreInfoDB.setAgeDistributionMax(diagnosisStoreInfo.getAgeDistributionMax());
        diagnosisStoreInfoDB.setAgeDistributionMin(diagnosisStoreInfo.getAgeDistributionMin());
        diagnosisStoreInfoDB.setGenderDistribution(diagnosisStoreInfo.getGenderDistribution());
        diagnosisStoreInfoDB.setOccupationDistributions(diagnosisStoreInfo.getOccupationDistributions());
        diagnosisStoreInfoService.save(diagnosisStoreInfoDB);
    }

    public void updateDiagnosisStoreInfo(DiagnosisStoreInfo diagnosisStoreInfo) {
        String storeInfoId = diagnosisStoreInfo.getStoreInfoId();
        // 根据门店ID查找经营诊断的门店信息
        DiagnosisStoreInfo diagnosisStoreInfoDB = diagnosisStoreInfoDao.findPromotionStore(storeInfoId);
        diagnosisStoreInfoDB.setStoreInfoId(storeInfoId);
        diagnosisStoreInfoDB.setCityLevel(diagnosisStoreInfo.getCityLevel());
        diagnosisStoreInfoDB.setTrafficGuide(diagnosisStoreInfo.getTrafficGuide());
        diagnosisStoreInfoDB.setConsumptionArea(diagnosisStoreInfo.getConsumptionArea());
        diagnosisStoreInfoDB.setConsumptionTypes(diagnosisStoreInfo.getConsumptionTypes());
        diagnosisStoreInfoDB.setPersonAvgPriceMax(diagnosisStoreInfo.getPersonAvgPriceMax());
        diagnosisStoreInfoDB.setPersonAvgPriceMin(diagnosisStoreInfo.getPersonAvgPriceMin());
        diagnosisStoreInfoDB.setTableAvgPersonNumMax(diagnosisStoreInfo.getTableAvgPersonNumMax());
        diagnosisStoreInfoDB.setTableAvgPersonNumMin(diagnosisStoreInfo.getTableAvgPersonNumMin());
        diagnosisStoreInfoDB.setAgeDistributionMax(diagnosisStoreInfo.getAgeDistributionMax());
        diagnosisStoreInfoDB.setAgeDistributionMin(diagnosisStoreInfo.getAgeDistributionMin());
        diagnosisStoreInfoDB.setGenderDistribution(diagnosisStoreInfo.getGenderDistribution());
        diagnosisStoreInfoDB.setOccupationDistributions(diagnosisStoreInfo.getOccupationDistributions());
        diagnosisStoreInfoDB.setDianpingLink(diagnosisStoreInfo.getDianpingLink());
        diagnosisStoreInfoDB.setDianpingRanking(diagnosisStoreInfo.getDianpingRanking());
        diagnosisStoreInfoDB.setDianpingStoreName(diagnosisStoreInfo.getDianpingStoreName());
        diagnosisStoreInfoService.save(diagnosisStoreInfoDB);
    }

    /**
     * 保存ERP门店信息
     *
     * @param diagnosisStoreInfo
     */
    public void saveErpStoreInfo(DiagnosisStoreInfo diagnosisStoreInfo) {
        checkStoreInfoId(diagnosisStoreInfo);

        ErpStoreInfo erpStoreInfo = diagnosisStoreInfo.getErpStoreInfo();
        if (erpStoreInfo != null) {
            ErpStoreInfo storeInfo = new ErpStoreInfo();
            storeInfo.setId(erpStoreInfo.getId());
            storeInfo.setProvince(erpStoreInfo.getProvince());
            storeInfo.setProvinceName(erpStoreInfo.getProvinceName());
            storeInfo.setCity(erpStoreInfo.getCity());
            storeInfo.setCityName(erpStoreInfo.getCityName());
            storeInfo.setArea(erpStoreInfo.getArea());
            storeInfo.setAreaName(erpStoreInfo.getAreaName());
            storeInfo.setAddress(erpStoreInfo.getAddress());
            storeInfo.setTelephone(erpStoreInfo.getTelephone());
            erpStoreInfoService.updateContactInfo(storeInfo);
        }
    }

    /**
     * 根据分单获取经营诊断表单
     *
     * @param splitId
     * @return
     * @date 2018年4月3日
     */
    public DiagnosisForm findBySplitId(String splitId) {
        DiagnosisForm diagnosisForm = diagnosisFormDao.findBySplitId(splitId);
        if (diagnosisForm == null) {
            diagnosisForm = new DiagnosisForm();
            diagnosisForm.setSplitId(splitId);
        }
        return diagnosisForm;
    }

    public DiagnosisForm findBySplitIdWithCreaterName(String splitId) {
        return diagnosisFormDao.findBySplitIdWithCreaterName(splitId);
    }

    public DiagnosisForm getDiagnosisFormBySplitId(String splitId) {
        return diagnosisFormDao.getDiagnosisFormBySplitId(splitId);
    }

    /**
     * 获取电话后补充阶段数据
     *
     * @return
     * @date 2018年4月3日
     */
    public DiagnosisForm findAfterPhone(String splitId) {
        logger.info("获取电话诊断阶段数据入参：splitId = {}", splitId);
        if (StringUtils.isBlank(splitId)) {
            return null;
        }

        // 根据分单ID获取表单
        DiagnosisForm diagnosisForm = findBySplitId(splitId);

        // 获取推广的门店及大众点评信息
        List<DiagnosisStoreInfo> promotionStores = diagnosisStoreInfoDao.getPromotionStores(diagnosisForm.getSplitId());
        diagnosisForm.setDiagnosisStoreInfos(promotionStores);

        // 行业属性
        List<DiagnosisIndustryAttribute> industryAttributes = diagnosisIndustryAttributeDao.findBySplitId(splitId);
        diagnosisForm.setIndustryAttributes(industryAttributes);

        logger.info("获取电话诊断阶段数据结果：{}", diagnosisForm);
        return diagnosisForm;
    }

    /**
     * 保存电话后补充阶段数据
     *
     * @return
     * @date 2018年4月3日
     */
    @Transactional(readOnly = false)
    public BaseResult saveAfterPhone(DiagnosisForm form) {
        logger.info("保存电话后补充阶段数据入参: DiagnosisForm = {}", JSON.toJSON(form));

        // 保存表单数据
        saveDiagnosisFormAfterPhone(form);

        // 删除优惠形式表单（应在保存行业属性之前执行）
        deleteDiscountTypeRecommend(form);

        // 保存行业属性
        saveIndustryAttributes(form);

        // 保存门店的大众点评信息
        saveStoreDianpingInfos(form);

        return new BaseResult();
    }

    /**
     * 保存门店的大众点评信息
     *
     * @param form
     * @date 2018年4月3日
     */
    @Transactional(readOnly = false)
    public void saveStoreDianpingInfos(DiagnosisForm form) {
        if (form == null) {
            throw new ServiceException("表单没有填写！");
        }

        List<DiagnosisStoreInfo> diagnosisStoreInfos = form.getDiagnosisStoreInfos();
        if (CollectionUtils.isEmpty(diagnosisStoreInfos)) {
            return;
        }

        for (DiagnosisStoreInfo diagnosisStoreInfo : diagnosisStoreInfos) {
            checkStoreInfoId(diagnosisStoreInfo);
            String storeInfoId = diagnosisStoreInfo.getStoreInfoId();
            DiagnosisStoreInfo storeInfoDB = diagnosisStoreInfoDao.findPromotionStore(storeInfoId);
            if (storeInfoDB == null) {
                storeInfoDB = new DiagnosisStoreInfo();
            }
            storeInfoDB.setStoreInfoId(storeInfoId);
            storeInfoDB.setDianpingLink(diagnosisStoreInfo.getDianpingLink());
            storeInfoDB.setDianpingRanking(diagnosisStoreInfo.getDianpingRanking());
            storeInfoDB.setDianpingStoreName(diagnosisStoreInfo.getDianpingStoreName());
            diagnosisStoreInfoService.save(storeInfoDB);
        }
    }

    /**
     * 比较行业属性修改前后是否相同，如不相同，删除优惠形式推荐
     * 
     * @param form 经营诊断表单
     */
    @Transactional(readOnly = false)
    public void deleteDiscountTypeRecommend(DiagnosisForm form) {
        checkFormSplitId(form);

        String splitId = form.getSplitId();
        List<DiagnosisSplitIndustryAttribute> splitIndustryAttributesBefore = splitIndustryAttributeDao.findBySplitId(splitId);
        List<DiagnosisSplitIndustryAttribute> splitIndustryAttributesAfter = form.getSplitIndustryAttributes();
        if (CollectionUtils.isEmpty(splitIndustryAttributesBefore)) {
            splitIndustryAttributesBefore = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(splitIndustryAttributesAfter)) {
            splitIndustryAttributesAfter = new ArrayList<>();
        }

        // 排序
        Comparator<DiagnosisSplitIndustryAttribute> comparator = (DiagnosisSplitIndustryAttribute o1, DiagnosisSplitIndustryAttribute o2) -> o1
                        .getIndustryAttributeId().compareTo(o2.getIndustryAttributeId());

        Collections.sort(splitIndustryAttributesBefore, comparator);
        Collections.sort(splitIndustryAttributesAfter, comparator);

        String beforeStr = Constant.BLANK, afterStr = Constant.BLANK;
        for (DiagnosisSplitIndustryAttribute attribute : splitIndustryAttributesBefore) {
            beforeStr += attribute.getIndustryAttributeId();
        }
        for (DiagnosisSplitIndustryAttribute attribute : splitIndustryAttributesAfter) {
            afterStr += attribute.getIndustryAttributeId();
        }

        // 如果不相同，删除优惠形式推荐
        if (!beforeStr.equals(afterStr)) {
            diagnosisDiscountTypeRecommendDao.deleteBySplitId(splitId);
        }
    }

    /**
     * 保存行业属性
     *
     * @param form
     * @date 2018年4月3日
     */
    @Transactional(readOnly = false)
    public void saveIndustryAttributes(DiagnosisForm form) {
        checkFormSplitId(form);

        String splitId = form.getSplitId();
        // 先删除
        splitIndustryAttributeDao.deleteBySplitId(splitId);

        List<DiagnosisSplitIndustryAttribute> splitIndustryAttributes = form.getSplitIndustryAttributes();
        if (CollectionUtils.isNotEmpty(splitIndustryAttributes)) {
            // 批量保存
            splitIndustryAttributeDao.saveBatch(splitIndustryAttributes);
        }
    }

    /**
     * 保存电话后补充表单
     *
     * @param form
     * @date 2018年4月3日
     */
    @Transactional(readOnly = false)
    public void saveDiagnosisFormAfterPhone(DiagnosisForm form) {
        checkFormSplitId(form);

        String splitId = form.getSplitId();
        DiagnosisForm diagnosisFormDB = diagnosisFormDao.findBySplitId(splitId);
        if (diagnosisFormDB == null) {
            diagnosisFormDB = new DiagnosisForm();
        }

        diagnosisFormDB.setSplitId(splitId);
        diagnosisFormDB.setDiagnosisContentAdditional(form.getDiagnosisContentAdditional());
        diagnosisFormDB.setReferenceMaterial(form.getReferenceMaterial());
        this.save(diagnosisFormDB);
    }

    /**
     * 获取优惠形式和内容
     *
     * @return
     * @date 2018年3月29日
     */
    public DiagnosisForm findDiscountTypeContent(String splitId) {
        logger.info("获取优惠形式和内容入参：splitId = {}", splitId);
        if (StringUtils.isBlank(splitId)) {
            return null;
        }

        // 获取表单数据
        DiagnosisForm diagnosisForm = findBySplitId(splitId);

        // 获取推广的门店信息
        List<DiagnosisStoreInfo> promotionStores = diagnosisStoreInfoDao.getPromotionStores(splitId);
        diagnosisForm.setDiagnosisStoreInfos(promotionStores);

        // 获取行业属性
        fillIndustryAttribute(diagnosisForm);

        // 获取推广需求
        fillActivityRequirementStr(diagnosisForm);

        // 获取推荐的优惠形式
        fillDiscountTypeRecommend(diagnosisForm);

        // 获取投放通道
        fillPromotionChannel(diagnosisForm);

        // 获取投放开始时间
        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(splitId);
        if (erpOrderSplitInfo != null) {
            diagnosisForm.setPromotionTime(erpOrderSplitInfo.getPromotionTime());
        }

        logger.info("获取优惠形式和内容结果：{}", diagnosisForm);
        return diagnosisForm;
    }

    /**
     * 获取推荐的优惠形式
     *
     * @param diagnosisForm
     * @date 2018年4月4日
     */
    private void fillDiscountTypeRecommend(DiagnosisForm diagnosisForm) {
        checkFormSplitId(diagnosisForm);

        // 如果已选择优惠形式，直接返回
        List<DiagnosisDiscountTypeRecommend> discountTypeRecommendList = diagnosisDiscountTypeRecommendDao
                        .findRecommendList(diagnosisForm.getSplitId());

        if (CollectionUtils.isNotEmpty(discountTypeRecommendList)) {
            diagnosisForm.setDiscountTypeRecommends(discountTypeRecommendList);
            return;
        }

        // 根据所选择的行业属性获取推荐的行业属性
        List<DiagnosisIndustryAttribute> industryAttributes = diagnosisForm.getIndustryAttributes();
        if (CollectionUtils.isEmpty(industryAttributes)) {
            return;
        }

        DiagnosisIndustryAttribute industryAttribute = Collections.max(industryAttributes,
                        (DiagnosisIndustryAttribute o1, DiagnosisIndustryAttribute o2) -> Integer.compare(o1.getLevel(), o2.getLevel()));

        // 如果所选行业没有推荐的行业属性，退出
        String industryAttributeId = industryAttribute.getIndustryAttribute();
        if (StringUtils.isBlank(industryAttributeId)) {
            return;
        }

        // 获取推广需求
        String activityRequirements = diagnosisForm.getActivityRequirements();
        activityRequirements = StringUtils.isNotBlank(activityRequirements) ? activityRequirements : "";
        List<String> activityRequirementsIdList = Arrays.asList(activityRequirements.split(Constant.COMMA));
        if (CollectionUtils.isEmpty(activityRequirementsIdList)) {
            return;
        }

        // 根据推荐的行业属性和推广需求从配置表获取推荐的优惠形式
        // 推荐优惠形式展示5个，如果第5个同分，可展示第6.7.8...个
        discountTypeRecommendList = diagnosisDiscountTypeRecommendDao.findRecommendSummaryList(industryAttributeId, activityRequirementsIdList);
        if (CollectionUtils.isNotEmpty(discountTypeRecommendList) && discountTypeRecommendList.size() > DISCOUNT_TYPE_RECOMMEND_NUM) {
            List<DiagnosisDiscountTypeRecommend> delRecommendList = new ArrayList<>();
            int fifthScore = discountTypeRecommendList.get(DISCOUNT_TYPE_RECOMMEND_NUM - 1).getRecommendScore().intValue();
            for (int i = DISCOUNT_TYPE_RECOMMEND_NUM; i < discountTypeRecommendList.size(); i++) {
                if (discountTypeRecommendList.get(i).getRecommendScore().intValue() != fifthScore) {
                    delRecommendList.add(discountTypeRecommendList.get(i));
                }
            }
            discountTypeRecommendList.removeAll(delRecommendList);
        }
        diagnosisForm.setDiscountTypeRecommends(discountTypeRecommendList);
    }

    /**
     * 获取推广通道
     *
     * @param diagnosisForm
     * @date 2018年4月4日
     */
    private void fillPromotionChannel(DiagnosisForm diagnosisForm) {
        checkFormSplitId(diagnosisForm);

        List<JykOrderPromotionChannel> channels = jykOrderPromotionChannelDao.findListBySplitId(diagnosisForm.getSplitId());
        if (CollectionUtils.isEmpty(channels)) {
            return;
        }

        List<Dict> dicts = DictUtils.getDictList(DiagnosisConstant.EXTENSION_PASSAGEWAY_QULIFY);
        if (CollectionUtils.isEmpty(dicts)) {
            dicts = new ArrayList<>();
        }

        String channelStr = "";
        for (JykOrderPromotionChannel channel : channels) {
            for (Dict dict : dicts) {
                if (dict.getValue().equals(channel.getPromotionChannel())) {
                    channelStr += (dict.getLabel() + Constant.COMMA_FULL);
                    break;
                }
            }
        }
        if (channelStr.endsWith(Constant.COMMA_FULL)) {
            channelStr = channelStr.substring(0, channelStr.lastIndexOf(Constant.COMMA_FULL));
        }
        diagnosisForm.setPromotionChannel(channelStr);
    }

    /**
     * 获取营销活动需求
     *
     * @param diagnosisForm
     * @date 2018年4月4日
     */
    private void fillActivityRequirementStr(DiagnosisForm diagnosisForm) {
        if (diagnosisForm == null || StringUtils.isBlank(diagnosisForm.getActivityRequirements())) {
            return;
        }

        String[] requirementArray = diagnosisForm.getActivityRequirements().split(Constant.COMMA);
        List<String> requirementList = Arrays.asList(requirementArray);
        if (CollectionUtils.isEmpty(requirementList)) {
            return;
        }

        List<Dict> dicts = DictUtils.getDictList(DiagnosisConstant.ACTIVITY_REQUIREMENT_DICT_TYPE);
        if (CollectionUtils.isEmpty(dicts)) {
            return;
        }

        String require = "";
        for (String requirement : requirementList) {
            for (Dict dict : dicts) {
                if (requirement.equals(dict.getId())) {
                    require += (dict.getLabel() + Constant.COMMA);
                    break;
                }
            }
        }

        if (require.endsWith(Constant.COMMA)) {
            require = require.substring(0, require.lastIndexOf(Constant.COMMA));
        }
        diagnosisForm.setActivityRequirementStr(require);
    }

    /**
     * 获取行业属性
     *
     * @param diagnosisForm
     * @date 2018年4月4日
     */
    private void fillIndustryAttribute(DiagnosisForm diagnosisForm) {
        checkFormSplitId(diagnosisForm);

        List<DiagnosisIndustryAttribute> industryAttributes = diagnosisIndustryAttributeDao.findBySplitId(diagnosisForm.getSplitId());
        diagnosisForm.setIndustryAttributes(industryAttributes);
        if (CollectionUtils.isNotEmpty(industryAttributes)) {
            String industry = "";
            for (DiagnosisIndustryAttribute industryAttribute : industryAttributes) {
                industry += (industryAttribute.getName() + Constant.DASH);
            }
            if (industry.endsWith(Constant.DASH)) {
                industry = industry.substring(0, industry.lastIndexOf(Constant.DASH));
            }
            diagnosisForm.setIndustryAttribute(industry);
        }
    }

    /**
     * 获取行业属性
     *
     * @param diagnosisForm
     * @date 2018年4月4日
     */
    private void fillIndustryAttributeForModifying(DiagnosisForm diagnosisForm) {
        List<DiagnosisIndustryAttribute> industryAttributes = diagnosisIndustryAttributeDao.findBySplitId(diagnosisForm.getSplitId());
        diagnosisForm.setIndustryAttributes(industryAttributes);
    }

    /**
     * 保存优惠形式和内容数据
     */
    @Transactional(readOnly = false)
    public BaseResult saveDiscountTypeContent(DiagnosisForm form) {
        logger.info("保存优惠形式和内容数据入参: DiagnosisForm = {}", JSON.toJSON(form));

        // 保存表单数据
        saveDiagnosisFormDiscountTypeContent(form);

        // 保存推荐的优惠形式
        saveDiscountTypeRecommends(form);

        // 保存推广时间/投放开始时间
        savePromotionTime(form);

        return new BaseResult();
    }

    /**
     * 保存推广时间/投放开始时间
     *
     * @param form
     * @date 2018年4月8日
     */
    private void savePromotionTime(DiagnosisForm form) {
        checkFormSplitId(form);

        if (form.getPromotionTime() != null) {
            erpOrderSplitInfoDao.updatePromotionTime(form.getPromotionTime(), form.getSplitId());
        }
    }

    /**
     * 保存推荐的优惠形式表
     *
     * @param form
     * @date 2018年4月8日
     */
    private void saveDiscountTypeRecommends(DiagnosisForm form) {
        checkFormSplitId(form);

        String splitId = form.getSplitId();
        // 先删除
        diagnosisDiscountTypeRecommendDao.deleteBySplitId(splitId);
        logger.info("修改经营诊断推荐的优惠形式表, 先物理删除, 后批量保存. splitId = {}", splitId);

        List<DiagnosisDiscountTypeRecommend> discountTypeRecommends = form.getDiscountTypeRecommends();
        if (CollectionUtils.isEmpty(discountTypeRecommends)) {
            return;
        }

        // 后添加
        for (DiagnosisDiscountTypeRecommend recommend : discountTypeRecommends) {
            recommend.setIsNewRecord(false);
            recommend.preInsert();
            recommend.setSplitId(splitId);
        }

        diagnosisDiscountTypeRecommendDao.saveBatch(discountTypeRecommends);
    }

    /**
     * 保存优惠形式和内容数据表单数据
     *
     * @param form
     */
    public void saveDiagnosisFormDiscountTypeContent(DiagnosisForm form) {
        checkFormSplitId(form);

        String splitId = form.getSplitId();
        DiagnosisForm diagnosisFormDB = diagnosisFormDao.findBySplitId(splitId);
        if (diagnosisFormDB == null) {
            diagnosisFormDB = new DiagnosisForm();
        }
        diagnosisFormDB.setSplitId(splitId);
        diagnosisFormDB.setMainPush(form.getMainPush());
        diagnosisFormDB.setBackupFirst(form.getBackupFirst());
        diagnosisFormDB.setBackupSecond(form.getBackupSecond());
        diagnosisFormDB.setPushArea(form.getPushArea());

        this.save(diagnosisFormDB);
    }

    /**
     * 获取诊断准备阶段数据
     * 
     * @param splitId
     * @return
     */
    public BaseResult getPreparationStageData(String splitId) {
        logger.info("-----------获取经营诊断电话前准备阶段详细数据 Start-----------------");
        logger.info("入参情况:splitId={}", splitId);

        if (StringUtils.isBlank(splitId)) {
            logger.error("无效参数={}", splitId);
            return new IllegalArgumentErrorResult();
        }

        // 返回结果
        Map<String, Object> res = new HashMap<>();
        res.put("splitId", splitId);

        ErpOrderSplitInfo orderSplitInfo = erpOrderSplitInfoDao.getOrderSplitInfo(splitId);
        ErpOrderOriginalInfo orderInfo = null;
        if (orderSplitInfo != null) {
            // 获取商户城市级别信息
            ErpShopInfo erpShopInfo = orderSplitInfo.getErpShopInfo();
            if (erpShopInfo != null) {
                res.put("cityLevel", erpShopInfo.getCityLevel());
            }

            // 获取订单套餐信息
            List<ErpOrderSplitGood> orderSplitGoods = orderSplitInfo.getErpOrderSplitGoods();
            res.put("erpOrderSplitGoods", orderSplitGoods);
            res.put("orderId", orderSplitInfo.getOrderId());
            orderInfo = orderSplitInfo.getOrderInfo();
        }

        fillShopOrderInfo(res, orderInfo);

        // 获取“商户所在的城市级别”字典数据
        List<Dict> cityLevelList = DictUtils.getDictList("city_level");
        res.put("cityLevelList", cityLevelList);

        // 获取“商户信息了解”字典数据
        List<Dict> shopInfoKnowList = DictUtils.getDictList("shop_info_know");
        res.put("shopInfoKnowList", shopInfoKnowList);

        // 获取其他信息
        DiagnosisForm diagnosisForm = diagnosisFormDao.findBySplitId(splitId);
        if (diagnosisForm != null) {
            res.put("activityGoal", diagnosisForm.getActivityGoal());
            res.put("packageAdditional", diagnosisForm.getPackageAdditional());
            res.put("serviceKnow", diagnosisForm.getServiceKnow());
        }

        BaseResult result = new BaseResult();
        result.setAttach(res);
        logger.info("出参情况result={}", result);
        logger.info("-----------获取经营诊断电话前准备阶段详细数据 End-----------------");
        return result;
    }

    /**
     * 填充商户及订单信息
     *
     * @param shopInfo
     * @param orderInfo
     * @date 2018年4月10日
     */
    private void fillShopOrderInfo(Map<String, Object> shopInfo, ErpOrderOriginalInfo orderInfo) {
        if (shopInfo != null && orderInfo != null) {
            // 获取商户信息
            shopInfo.put("shopName", orderInfo.getShopName());
            shopInfo.put("shopAbbreviation", (orderInfo.getShopAbbreviation()));
            shopInfo.put("agentName", (orderInfo.getAgentName()));
            shopInfo.put("contactName", (orderInfo.getContactName()));
            shopInfo.put("contactNumber", (orderInfo.getContactNumber()));
            shopInfo.put("promotePhone", (orderInfo.getPromotePhone()));
            shopInfo.put("promoteContact", (orderInfo.getPromoteContact()));

            // 获取订单版本号信息
            shopInfo.put("orderVersion", orderInfo.getOrderVersion());
        }
    }

    /**
     * 保存经营诊断1-电话前准备阶段数据
     *
     * @return
     * @date 2018年4月3日
     */
    @Transactional(readOnly = false)
    public BaseResult savePreparationStageData(PreparationStageRequestDto dto) {
        logger.info("-----------保存经营诊断电话前准备阶段详细数据 Start-----------------");
        logger.info("入参情况:PreparationStageDto={}", dto);

        if (StringUtils.isBlank(dto.getSplitId())) {
            logger.error("无效参数 splitId={}", dto.getSplitId());
            return new IllegalArgumentErrorResult();
        }

        // 保存城市级别信息
        saveCityLevel(dto);

        // 保存购买曝光量和赠送曝光量信息
        saveExposure(dto);

        // 保存其他信息
        saveDiagnosisForm(dto);

        BaseResult result = new BaseResult();
        result.setMessage("保存经营诊断电话前准备阶段详细数据成功！");
        logger.info("出参情况:result={}", result);
        logger.info("-----------保存经营诊断电话前准备阶段详细数据 End-----------------");
        return result;
    }

    /**
     * 获取活动策划2（卡券策划）数据
     * 
     * @param splitId
     *
     * @return
     * @date 2018年4月3日
     */
    public BaseResult getCouponsPlanData(String splitId) {
        logger.info("-----------获取获取活动策划2（卡券策划） Start-----------------");
        logger.info("入参情况:splitId={}", splitId);

        if (StringUtils.isBlank(splitId)) {
            logger.error("无效参数 splitId={}", splitId);
            return new IllegalArgumentErrorResult();
        }

        Map<String, Object> resultMap = new HashMap<>();

        DiagnosisForm diagnosisForm = diagnosisFormDao.getDiagnosisFormBySplitId(splitId);

        if (null != diagnosisForm) {
            resultMap.put("shopUsername", diagnosisForm.getShopUsername());
            resultMap.put("shopPassword", AESUtil.decrypt(diagnosisForm.getShopPassword()));

            // 获取活动目的
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

            // 判断活动目标是不是选择couponsAttractCustomer
            resultMap.put("isCouponsAttractCustomer", isCouponsAttractCustomer);
            if (isCouponsAttractCustomer) {
                // 获取“优惠券类型”字典数据
                List<Dict> couponTypeList = DictUtils.getDictList("coupon_type");

                // 去掉商品券
                Iterator<Dict> it = couponTypeList.iterator();
                while (it.hasNext()) {
                    if (DiagnosisConstant.GOODS_VOUCHERS.equals(it.next().getValue())) {
                        it.remove();
                    }
                }
                resultMap.put("couponTypeList", couponTypeList);

                // 获取卡券内容信息
                List<DiagnosisCardCoupons> diagnosisCardCoupons = diagnosisForm.getDiagnosisCardCoupons();
                if (diagnosisCardCoupons != null) {
                    resultMap.put("diagnosisCardCoupons", diagnosisCardCoupons);
                }
            }
        }

        BaseResult result = new BaseResult();
        result.setAttach(resultMap);
        logger.info("出参情况:result={}", result);
        logger.info("-----------获取活动策划2（卡券策划）数据 End-----------------");
        return result;
    }

    /**
     * 
     * 保存活动策划2（卡券策划）数据
     * 
     * @param couponsPlanDto
     * @return
     * @date 2018年4月3日
     */
    @Transactional(readOnly = false)
    public BaseResult saveCouponsPlanData(CouponsPlanRequestDto couponsPlanDto) {
        logger.info("-----------保存活动策划2（卡券策划）数据 Start-----------------");
        if (couponsPlanDto == null || StringUtils.isBlank(couponsPlanDto.getSplitId())) {
            logger.error("无效参数 couponsPlanDto={}", couponsPlanDto);
            return new IllegalArgumentErrorResult();
        }
        logger.info("入参情况:shopUsername={},shopPassword={},splitId={},removedCouponIds={},diagnosisCardCoupons{}", couponsPlanDto.getShopUsername(), // NOSONAR
                        couponsPlanDto.getShopPassword(), couponsPlanDto.getSplitId(), couponsPlanDto.getRemovedCouponIds(),
                        couponsPlanDto.getDiagnosisCardCoupons());

        // 保存卡券信息
        List<DiagnosisCardCoupons> diagnosisCardCoupons = couponsPlanDto.getDiagnosisCardCoupons();
        saveOrUpdateDiagnosisCoupon(diagnosisCardCoupons);

        // 删除卡券信息
        List<String> removedCouponIds = couponsPlanDto.getRemovedCouponIds();
        if (CollectionUtils.isNotEmpty(removedCouponIds)) {
            diagnosisCardCouponsService.batchDeleteByIds(removedCouponIds);
        }

        // 保存掌贝商户后台账号和密码
        DiagnosisForm diagnosisFormDB = diagnosisFormDao.findBySplitId(couponsPlanDto.getSplitId());
        if (diagnosisFormDB == null) {
            diagnosisFormDB = new DiagnosisForm();
        }

        diagnosisFormDB.setSplitId(couponsPlanDto.getSplitId());
        diagnosisFormDB.setShopUsername(couponsPlanDto.getShopUsername());
        diagnosisFormDB.setShopPassword(AESUtil.encrypt(couponsPlanDto.getShopPassword()));
        this.save(diagnosisFormDB);

        BaseResult result = new BaseResult();
        result.setMessage("保存活动策划2（卡券策划）数据成功！");
        logger.info("出参情况:result={}", result);
        logger.info("-----------保存活动策划2（卡券策划）数据 End-----------------");
        return result;
    }

    @Transactional(readOnly = false)
    private void saveOrUpdateDiagnosisCoupon(List<DiagnosisCardCoupons> diagnosisCardCoupons) {
        if (CollectionUtils.isNotEmpty(diagnosisCardCoupons)) {
            for (DiagnosisCardCoupons diagnosisCardCouponsDB : diagnosisCardCoupons) {
                diagnosisCardCouponsService.save(diagnosisCardCouponsDB);
            }
        }
    }

    /**
     * 获取活动策划3（宣传重点）数据
     *
     * @param splitId
     * @return
     * @date 2018年4月4日
     */
    public BaseResult getPropagandaKeyData(String splitId) {
        logger.info("-----------获取活动策划3（宣传重点）数据 Start-----------------");
        logger.info("入参情况:splitId={}", splitId);

        if (StringUtils.isBlank(splitId)) {
            logger.error("无效参数 splitId=" + splitId);
            return new IllegalArgumentErrorResult();
        }

        Map<String, Object> resultMap = new HashMap<>();

        // 获取诊断表单信息
        DiagnosisForm diagnosisForm = diagnosisFormDao.findBySplitId(splitId);
        if (diagnosisForm != null) {
            resultMap.put("mainPush", diagnosisForm.getMainPush());
            resultMap.put("majorProduct", diagnosisForm.getMajorProduct());
            resultMap.put("promoteProduct", diagnosisForm.getPromoteProduct());
            resultMap.put("brandLightspot", diagnosisForm.getBrandLightspot());
            resultMap.put("originalityCulture", diagnosisForm.getOriginalityCulture());
            resultMap.put("firstPropagandaContent", diagnosisForm.getFirstPropagandaContent());
            resultMap.put("secondPropagandaContent", diagnosisForm.getSecondPropagandaContent());

            // 获取活动主题/推广需求
            String activityRequirements = "";
            String activityRequirementIds = diagnosisForm.getActivityRequirements();
            if (StringUtils.isNotBlank(activityRequirementIds)) {
                String[] activityRequirementIdArr = activityRequirementIds.split(",");
                for (String activityRequirementId : activityRequirementIdArr) {
                    Dict dict = dictService.get(activityRequirementId);
                    if (dict != null) {
                        activityRequirements += "/" + dict.getLabel();
                    }
                }
                activityRequirements = activityRequirements.substring(1);
            }
            resultMap.put("activityRequirements", activityRequirements);
        }

        // 获取推广通道
        List<String> promotionChannels = new ArrayList<>();
        List<JykOrderPromotionChannel> jykOrderPromotionChannels = jykOrderPromotionChannelDao.findListBySplitId(splitId);
        if (CollectionUtils.isNotEmpty(jykOrderPromotionChannels)) {
            // 推广通道
            for (JykOrderPromotionChannel jykOrderPromotionChannel : jykOrderPromotionChannels) {
                String promotionChannel = jykOrderPromotionChannel.getPromotionChannel();
                promotionChannels.add(promotionChannel);
            }
            resultMap.put("promotionChannels", promotionChannels);

            // 获取第一层重点宣传图片信息
            List<DiagnosisFirstAdImage> diagnosisFirstAdImages = diagnosisFirstAdImageService.findBySplitId(splitId);
            if (diagnosisFirstAdImages != null) {
                resultMap.put("diagnosisFirstAdImages", diagnosisFirstAdImages);
            }
        }

        // 获取第二层重点宣传图片信息
        List<DiagnosisSecondAdImage> diagnosisSecondAdImages = diagnosisSecondAdImageService.findBySplitId(splitId);
        if (diagnosisSecondAdImages != null) {
            resultMap.put("diagnosisSecondAdImages", diagnosisSecondAdImages);
        }

        // 访问图片的域名
        String domainName = Global.getResDomain() + "/";
        resultMap.put("domainName", domainName);

        // 获取诊断门店信息
        List<String> trafficGuides = new ArrayList<>();
        List<String> dianpingRankings = new ArrayList<>();
        List<DiagnosisStoreInfo> diagnosisStoreInfo = diagnosisStoreInfoService.getPromotionStores(splitId);
        if (CollectionUtils.isNotEmpty(diagnosisStoreInfo)) {
            for (DiagnosisStoreInfo storeInfo : diagnosisStoreInfo) {
                if (StringUtils.isNotBlank(storeInfo.getTrafficGuide())) {
                    trafficGuides.add(storeInfo.getTrafficGuide());
                }

                if (StringUtils.isNotBlank(storeInfo.getDianpingRanking())) {
                    dianpingRankings.add(storeInfo.getDianpingRanking());
                }
            }
        }
        resultMap.put("trafficGuides", trafficGuides);
        resultMap.put("dianpingRankings", dianpingRankings);

        BaseResult result = new BaseResult();
        result.setAttach(resultMap);
        logger.info("出参情况:result={}", result);
        logger.info("-----------获取活动策划3（宣传重点）数据 End-----------------");
        return result;
    }

    private void saveDiagnosisForm(PreparationStageRequestDto dto) {
        DiagnosisForm diagnosisFormDB = diagnosisFormDao.findBySplitId(dto.getSplitId());

        if (null == diagnosisFormDB) {
            diagnosisFormDB = new DiagnosisForm();
        }

        diagnosisFormDB.setSplitId(dto.getSplitId());
        diagnosisFormDB.setPackageAdditional(dto.getPackageAdditional());
        diagnosisFormDB.setServiceKnow(dto.getServiceKnow());
        this.save(diagnosisFormDB);
    }

    private void saveExposure(PreparationStageRequestDto dto) {
        List<ErpOrderSplitGood> erpOrderSplitGoods = dto.getErpOrderSplitGoods();
        for (ErpOrderSplitGood erpOrderSplitGood : erpOrderSplitGoods) {
            if (StringUtils.isBlank(erpOrderSplitGood.getId())) {
                throw new RuntimeException("参数不合法，缺少拆单商品ID，无法更新。");
            }

            erpOrderSplitGood.preUpdate();
            erpOrderSplitGoodDao.updateExposureById(erpOrderSplitGood);
        }
    }

    private void saveCityLevel(PreparationStageRequestDto dto) {
        ErpOrderSplitInfo erpOrderSplitInfo = erpOrderSplitInfoDao.get(dto.getSplitId());
        if (erpOrderSplitInfo != null) {
            ErpShopInfo erpShopInfo = erpShopInfoDao.getByZhangbeiID(erpOrderSplitInfo.getShopId());
            if (erpShopInfo == null) {
                throw new RuntimeException("商户信息数据有异常！");
            }

            erpShopInfo.setCityLevel(dto.getCityLevel());
            erpShopInfo.preUpdate();
            erpShopInfoDao.update(erpShopInfo);
        }
    }

    /**
     * 保存活动策划3（宣传重点）
     *
     * @param dto
     * @return
     * @date 2018年4月8日
     */
    @Transactional(readOnly = false)
    public BaseResult savePropagandaKeyData(PropagandaKeyRequestDto dto) {
        logger.info("-----------保存活动策划3（宣传重点）数据 Start-----------------");
        logger.info("入参情况:propagandaKeyDto={}", dto);

        if (dto == null || StringUtils.isBlank(dto.getSplitId())) {
            logger.error("无效参数 propagandaKeyDto=" + dto);
            return new IllegalArgumentErrorResult();
        }

        List<String> removedImgUrls = dto.getRemovedImgUrls();
        removeServerImgs(removedImgUrls);

        // 保存第一层重点宣传图片信息
        List<DiagnosisFirstAdImage> diagnosisFirstAdImages = dto.getDiagnosisFirstAdImages();
        saveDiagnosisFirstAdImages(diagnosisFirstAdImages);

        // 删除第一层重点宣传图片信息
        List<String> removedFirstAdImgIds = dto.getRemovedFirstAdImgIds();
        removeFirstAdImgs(removedFirstAdImgIds);

        // 保存第二层重点宣传图片信息
        List<DiagnosisSecondAdImage> diagnosisSecondAdImages = dto.getDiagnosisSecondAdImages();
        saveDiagnosisSecondAdImage(diagnosisSecondAdImages);

        // 删除第二层重点宣传图片信息
        List<String> removedSecondAdImgIds = dto.getRemovedSecondAdImgIds();
        removeSecondAdImages(removedSecondAdImgIds);

        // 保存文案内容
        DiagnosisForm diagnosisFormDB = diagnosisFormDao.findBySplitId(dto.getSplitId());
        if (diagnosisFormDB == null) {
            diagnosisFormDB = new DiagnosisForm();
        }
        diagnosisFormDB.setSplitId(dto.getSplitId());
        diagnosisFormDB.setFirstPropagandaContent(dto.getFirstPropagandaContent());
        diagnosisFormDB.setSecondPropagandaContent(dto.getSecondPropagandaContent());
        this.save(diagnosisFormDB);

        BaseResult result = new BaseResult();
        result.setMessage("保存活动策划3（宣传重点）数据成功！");
        logger.info("出参情况:result={}", result);
        logger.info("-----------保存活动策划3（宣传重点）数据 End-----------------");
        return result;
    }

    private void removeSecondAdImages(List<String> removedSecondAdImgIds) {
        if (CollectionUtils.isNotEmpty(removedSecondAdImgIds)) {
            List<DiagnosisSecondAdImage> removedSecondAdImgList = diagnosisSecondAdImageService.findListByIds(removedSecondAdImgIds);
            // 删除数据库记录
            int value = diagnosisSecondAdImageService.deleteBatchByIds(removedSecondAdImgIds);
            if (value > 0 && CollectionUtils.isNotEmpty(removedSecondAdImgList)) {
                // 删除文件
                for (DiagnosisSecondAdImage removedSecondAdImg : removedSecondAdImgList) {
                    if (StringUtils.isNotEmpty(removedSecondAdImg.getImgUrl())) {
                        FileUtils.deleteFile(basedir + "/" + removedSecondAdImg.getImgUrl());
                    }
                }
            }
        }
    }

    private void saveDiagnosisSecondAdImage(List<DiagnosisSecondAdImage> diagnosisSecondAdImages) {
        if (CollectionUtils.isNotEmpty(diagnosisSecondAdImages)) {
            for (DiagnosisSecondAdImage diagnosisSecondAdImageDB : diagnosisSecondAdImages) {
                diagnosisSecondAdImageService.save(diagnosisSecondAdImageDB);
            }
        }
    }

    private void removeFirstAdImgs(List<String> removedFirstAdImgIds) {
        if (CollectionUtils.isNotEmpty(removedFirstAdImgIds)) {
            List<DiagnosisFirstAdImage> removedFirstAdImgList = diagnosisFirstAdImageService.findListByIds(removedFirstAdImgIds);
            // 删除数据库记录
            int value = diagnosisFirstAdImageService.deleteBatchByIds(removedFirstAdImgIds);
            if (value > 0 && CollectionUtils.isNotEmpty(removedFirstAdImgList)) {
                // 删除文件
                for (DiagnosisFirstAdImage removedFirstAdImg : removedFirstAdImgList) {
                    if (StringUtils.isNotEmpty(removedFirstAdImg.getImgUrl())) {
                        FileUtils.deleteFile(basedir + "/" + removedFirstAdImg.getImgUrl());
                    }
                }
            }
        }
    }

    private void saveDiagnosisFirstAdImages(List<DiagnosisFirstAdImage> diagnosisFirstAdImages) {
        if (CollectionUtils.isNotEmpty(diagnosisFirstAdImages)) {
            for (DiagnosisFirstAdImage diagnosisFirstAdImageDB : diagnosisFirstAdImages) {
                diagnosisFirstAdImageService.save(diagnosisFirstAdImageDB);
            }
        }
    }

    private void removeServerImgs(List<String> removedImgUrls) {
        // 删除未入库的图片
        if (CollectionUtils.isNotEmpty(removedImgUrls)) {
            for (String url : removedImgUrls) {
                if (StringUtils.isNotEmpty(url)) {
                    FileUtils.deleteFile(basedir + "/" + url);
                }
            }
        }
    }

    /**
     * 获取推广资料
     *
     * @param splitId
     * @date 2018年4月10日
     */
    public DiagnosisForm getDiagnosisDataBySplitId(String splitId) {

        logger.info("获取推广资料入参：splitId = {}", splitId);
        if (StringUtils.isBlank(splitId)) {
            return null;
        }

        DiagnosisForm diagnosisForm = findBySplitId(splitId);
        processDiagnosisForm(diagnosisForm);

        // 商户分单信息
        fillShopOrderInfo(diagnosisForm); // NOSONAR

        // 套餐
        fillPackageInfos(diagnosisForm);

        // 投放通道
        fillPromotionChannel(diagnosisForm);

        // 行业属性
        fillIndustryAttribute(diagnosisForm);
        diagnosisForm.setIndustryAttributes(null);

        // 门店信息
        fillPromotionStoreInfo(diagnosisForm);
        // 处理门店信息返回的格式
        processStoreInfo(diagnosisForm);

        // 推广需求
        fillActivityRequirements(diagnosisForm);
        processActivityRequirements(diagnosisForm);

        // 处理活动目的返回格式
        fillActivityGoal(diagnosisForm);
        processActivityGoal(diagnosisForm);

        // 宣传重点广告图
        fillAdImages(diagnosisForm);

        // 以往及进行中的优惠内容
        List<DiagnosisDiscount> diagnosisDiscounts = diagnosisDiscountDao.findBySplitId(splitId);
        diagnosisForm.setDiagnosisDiscounts(diagnosisDiscounts);

        // 卡券内容
        List<DiagnosisCardCoupons> cardCoupons = diagnosisCardCouponsService.findBySplitId(splitId);
        diagnosisForm.setDiagnosisCardCoupons(cardCoupons);

        return diagnosisForm;
    }

    /**
     * 获取修改经营诊断推广资料需要的回显数据
     *
     * @param splitId
     * @return
     * @date 2018年5月10日
     */
    public BaseResult getDiagnosisDataForModifying(String splitId) {
        logger.info("获取修改经营诊断推广资料需要的回显数据：splitId = {}", splitId);
        if (StringUtils.isBlank(splitId)) {
            return new IllegalArgumentErrorResult();
        }
        DiagnosisFormResponseDto dto = new DiagnosisFormResponseDto();
        dto.setResDomain(resDomain);

        // 封装字典信息
        encapsulateDictInfo(dto);

        DiagnosisForm diagnosisForm = findBySplitId(splitId);
        processPassword(diagnosisForm);

        // 商户分单信息
        fillShopOrderInfo(diagnosisForm); // NOSONAR

        // 套餐
        encapsulatePackageInfos(dto, diagnosisForm);

        // 投放通道
        fillPromotionChannel(diagnosisForm);

        // 行业属性
        fillIndustryAttributeForModifying(diagnosisForm);

        // 门店信息
        fillPromotionStoreInfoForModifying(diagnosisForm);

        // 宣传重点广告图
        fillAdImages(diagnosisForm);

        // 以往及进行中的优惠内容
        List<DiagnosisDiscount> diagnosisDiscounts = diagnosisDiscountDao.findBySplitId(splitId);
        diagnosisForm.setDiagnosisDiscounts(diagnosisDiscounts);

        // 卡券内容
        List<DiagnosisCardCoupons> cardCoupons = diagnosisCardCouponsService.findBySplitId(splitId);
        diagnosisForm.setDiagnosisCardCoupons(cardCoupons);

        BaseResult result = new BaseResult();
        dto.setDiagnosisForm(diagnosisForm);
        result.setAttach(dto);
        return result;
    }

    private void encapsulateDictInfo(DiagnosisFormResponseDto dto) {
        List<Dict> list = DictUtils.getDictList(DiagnosisConstant.CONSUMPTION_AREA_DICT_TYPE);
        if (CollectionUtils.isEmpty(list)) {
            logger.error("没有获取到消费圈位置的数据字典类型");
            throw new ServiceException("没有获取到消费圈位置的数据字典类型");
        }
        dto.setConsumptionAreaList(list);

        list = DictUtils.getDictList(DiagnosisConstant.CONSUMPTION_TYPE_DICT_TYPE);
        if (CollectionUtils.isEmpty(list)) {
            logger.error("没有获取到消费圈类型的数据字典类型");
            throw new ServiceException("没有获取到消费圈类型的数据字典类型");
        }
        dto.setConsumptionTypeList(list);

        list = DictUtils.getDictList(DiagnosisConstant.WORKDAY_DICT_TYPE);
        if (CollectionUtils.isEmpty(list)) {
            logger.error("没有获取到工作日的数据字典类型");
            throw new ServiceException("没有获取到工作日的数据字典类型");
        }
        dto.setWorkdayList(list);

        list = DictUtils.getDictList(DiagnosisConstant.OCCUPATION_DISTRIBUTION_DICT_TYPE);
        if (CollectionUtils.isEmpty(list)) {
            logger.error("没有获取到职业分布的数据字典类型");
            throw new ServiceException("没有获取到职业分布的数据字典类型");
        }
        dto.setOccupationDistributionList(list);

        list = DictUtils.getDictList(DiagnosisConstant.GENDER_DISTRIBUTION_DICT_TYPE);
        if (CollectionUtils.isEmpty(list)) {
            logger.error("没有获取到性别分布的数据字典类型");
            throw new ServiceException("没有获取到性别分布的数据字典类型");
        }
        dto.setGenderDistributionList(list);

        list = DictUtils.getDictList(DiagnosisConstant.ACTIVITY_REQUIREMENT_DICT_TYPE);
        if (CollectionUtils.isEmpty(list)) {
            logger.error("没有获取到推广需求的数据字典类型");
            throw new ServiceException("没有获取到推广需求的数据字典类型");
        }
        dto.setActivityRequirementList(list);

        list = DictUtils.getDictList(DiagnosisConstant.ACTIVITY_GOAL_DICT_TYPE);
        if (CollectionUtils.isEmpty(list)) {
            logger.error("没有获取到活动目的的数据字典类型");
            throw new ServiceException("没有获取到活动目的的数据字典类型");
        }
        dto.setActivityGoalList(list);
    }

    /**
     * 处理表单字段显示方式
     *
     * @param diagnosisForm
     */
    private void processDiagnosisForm(DiagnosisForm diagnosisForm) {
        if (diagnosisForm == null) {
            return;
        }
        // 解密商户后台密码
        String shopPassword = diagnosisForm.getShopPassword();
        if (StringUtils.isNotBlank(shopPassword)) {
            diagnosisForm.setShopPassword(AESUtil.decrypt(shopPassword));
        }
    }

    /**
     * 处理表单字段显示方式
     *
     * @param diagnosisForm
     */
    private void processPassword(DiagnosisForm diagnosisForm) {
        if (diagnosisForm == null) {
            return;
        }
        // 解密商户后台密码
        String shopPassword = diagnosisForm.getShopPassword();
        if (StringUtils.isNotBlank(shopPassword)) {
            diagnosisForm.setShopPassword(AESUtil.decrypt(shopPassword));
        }
    }

    /**
     * 拼接套餐信息
     *
     * @param diagnosisForm
     * @date 2018年4月13日
     */
    private void fillPackageInfos(DiagnosisForm diagnosisForm) {
        checkFormSplitId(diagnosisForm);

        List<ErpOrderSplitGood> goods = erpOrderSplitGoodDao.findBySplitId(diagnosisForm.getSplitId());
        if (CollectionUtils.isEmpty(goods)) {
            return;
        }

        // 拼接并勾选用户的选择, 格式: 套餐版本-套餐类型*数量-购买曝光量-赠送曝光量
        StringBuilder sb = null;
        for (ErpOrderSplitGood erpOrderSplitGood : goods) {
            sb = new StringBuilder();
            String orderVersion = erpOrderSplitGood.getOrderVersion();
            String goodTypeName = erpOrderSplitGood.getGoodTypeName();
            Integer num = erpOrderSplitGood.getNum();
            BigDecimal buyExposure = erpOrderSplitGood.getBuyExposure();
            BigDecimal donateExposure = erpOrderSplitGood.getDonateExposure();
            String packageInfo = sb.append(StringUtils.isNotEmpty(orderVersion) ? orderVersion : Constant.SPACE).append(Constant.SPACE)
                            .append(Constant.DASH).append(Constant.SPACE).append(StringUtils.isNotEmpty(goodTypeName) ? goodTypeName : Constant.SPACE)
                            .append(Constant.SPACE).append(Constant.ASTERISK).append(Constant.SPACE).append(num != null ? num : 0)
                            .append(Constant.SPACE).append(Constant.DASH).append(Constant.SPACE).append(buyExposure != null ? buyExposure : 0)
                            .append(TEN_THOUSAND).append(BUY_EXPOSURE).append(Constant.DASH).append(Constant.SPACE)
                            .append(donateExposure != null ? donateExposure : 0).append(TEN_THOUSAND).append(DONATE_EXPOSURE).toString();
            erpOrderSplitGood.setPackageInfo(packageInfo);
            erpOrderSplitGood.setChecked(false);
            if (Constant.YES.equals(erpOrderSplitGood.getIsPromote())) {
                erpOrderSplitGood.setChecked(true);
            }
        }

        diagnosisForm.setErpOrderSplitGoods(goods);
    }


    private void encapsulatePackageInfos(DiagnosisFormResponseDto dto, DiagnosisForm diagnosisForm) {
        checkFormSplitId(diagnosisForm);

        List<ErpOrderSplitGood> goods = erpOrderSplitGoodDao.findBySplitId(diagnosisForm.getSplitId());
        if (CollectionUtils.isEmpty(goods)) {
            return;
        }

        // 拼接并勾选用户的选择, 格式: 套餐版本-套餐类型*数量-购买曝光量-赠送曝光量
        StringBuilder sb = null;
        List<String> packageInfos = new ArrayList<>();
        for (ErpOrderSplitGood erpOrderSplitGood : goods) {
            sb = new StringBuilder();
            String orderVersion = erpOrderSplitGood.getOrderVersion();
            String goodTypeName = erpOrderSplitGood.getGoodTypeName();
            Integer num = erpOrderSplitGood.getNum();
            BigDecimal buyExposure = erpOrderSplitGood.getBuyExposure();
            BigDecimal donateExposure = erpOrderSplitGood.getDonateExposure();
            String packageInfo = sb.append(StringUtils.isNotEmpty(orderVersion) ? orderVersion : Constant.SPACE).append(Constant.SPACE)
                            .append(Constant.DASH).append(Constant.SPACE).append(StringUtils.isNotEmpty(goodTypeName) ? goodTypeName : Constant.SPACE)
                            .append(Constant.SPACE).append(Constant.ASTERISK).append(Constant.SPACE).append(num != null ? num : 0)
                            .append(Constant.SPACE).append(Constant.DASH).append(Constant.SPACE).append(buyExposure != null ? buyExposure : 0)
                            .append(TEN_THOUSAND).append(BUY_EXPOSURE).append(Constant.DASH).append(Constant.SPACE)
                            .append(donateExposure != null ? donateExposure : 0).append(TEN_THOUSAND).append(DONATE_EXPOSURE).toString();
            packageInfos.add(packageInfo);
        }
        dto.setPackageInfos(packageInfos);
    }

    /**
     * 宣传重点广告图
     *
     * @param diagnosisForm
     * @date 2018年4月10日
     */
    private void fillAdImages(DiagnosisForm diagnosisForm) {
        checkFormSplitId(diagnosisForm);

        String splitId = diagnosisForm.getSplitId();
        List<Dict> dictList = DictUtils.getDictList(DiagnosisConstant.EXTENSION_PASSAGEWAY_QULIFY);

        // 获取第一层重点宣传图片信息
        List<DiagnosisFirstAdImage> diagnosisFirstAdImages = diagnosisFirstAdImageService.findBySplitId(splitId);
        if (CollectionUtils.isNotEmpty(diagnosisFirstAdImages)) {
            for (DiagnosisFirstAdImage diagnosisFirstAdImage : diagnosisFirstAdImages) {
                diagnosisFirstAdImage.setImgUrl(resDomain + File.separator + diagnosisFirstAdImage.getImgUrl());
                for (Dict dict : dictList) {
                    if (diagnosisFirstAdImage.getType().equals(dict.getValue())) {
                        diagnosisFirstAdImage.setTypeName(dict.getLabel());
                    }
                }
            }
            diagnosisForm.setFirstAdImages(diagnosisFirstAdImages);
        }

        // 获取第二层重点宣传图片信息
        List<DiagnosisSecondAdImage> diagnosisSecondAdImages = diagnosisSecondAdImageService.findBySplitId(splitId);
        if (CollectionUtils.isNotEmpty(diagnosisSecondAdImages)) {
            for (DiagnosisSecondAdImage diagnosisSecondAdImage : diagnosisSecondAdImages) {
                diagnosisSecondAdImage.setImgUrl(resDomain + File.separator + diagnosisSecondAdImage.getImgUrl());
            }
            diagnosisForm.setSecondAdImages(diagnosisSecondAdImages);
        }
    }

    /**
     * 处理活动目的返回格式
     *
     * @param diagnosisForm
     * @date 2018年4月10日
     */
    private void processActivityGoal(DiagnosisForm diagnosisForm) {
        if (diagnosisForm == null || CollectionUtils.isEmpty(diagnosisForm.getActivityGoalList())) {
            return;
        }
        String result = concatWithCommaFull(diagnosisForm.getActivityGoalList());
        diagnosisForm.setActivityGoal(result);
        diagnosisForm.setActivityGoalList(null); // 置空没用的数据,减少传输量
    }

    /**
     * 处理推广需求返回格式
     *
     * @param diagnosisForm
     * @date 2018年4月10日
     */
    private void processActivityRequirements(DiagnosisForm diagnosisForm) {
        if (diagnosisForm == null || CollectionUtils.isEmpty(diagnosisForm.getActivityRequirementsList())) {
            return;
        }

        String result = concatWithCommaFull(diagnosisForm.getActivityRequirementsList());
        diagnosisForm.setActivityRequirements(result);
        diagnosisForm.setActivityRequirementsList(null); // 置空没用的数据,减少传输量
    }

    // 以"，"连接
    private String concatWithCommaFull(List<Dict> dicts) {
        if (CollectionUtils.isEmpty(dicts)) {
            return Constant.BLANK;
        }

        String result = Constant.BLANK;
        for (int i = 0, size = dicts.size(); i < size; i++) {
            Dict dict = dicts.get(i);
            if (dict.getChecked()) {
                result += (dict.getLabel() + Constant.COMMA_FULL);
            }
        }
        if (result.endsWith(Constant.COMMA_FULL)) {
            result = result.substring(0, result.lastIndexOf(Constant.COMMA_FULL));
        }

        return result;
    }

    /**
     * 填充商户以及分单信息
     *
     * @param diagnosisForm
     * @date 2018年4月10日
     */
    private void fillShopOrderInfo(DiagnosisForm diagnosisForm) {
        checkFormSplitId(diagnosisForm);
        ErpOrderSplitInfo orderSplitInfo = erpOrderSplitInfoDao.getOrderSplitInfo(diagnosisForm.getSplitId());
        if (orderSplitInfo != null) {
            // 投放时间
            diagnosisForm.setPromotionTime(orderSplitInfo.getPromotionTime());
            if (orderSplitInfo.getOrderInfo() != null) {
                diagnosisForm.setErpOrderOriginalInfo(orderSplitInfo.getOrderInfo());
            }
        }
    }

    /**
     * 处理门店返回格式
     *
     * @param diagnosisForm
     * @date 2018年4月10日
     * @author zjq
     */
    private void processStoreInfo(DiagnosisForm diagnosisForm) {
        if (diagnosisForm == null || CollectionUtils.isEmpty(diagnosisForm.getDiagnosisStoreInfos())) {
            return;
        }

        // 门店ID
        List<String> storeInfoIds = new ArrayList<>();
        for (DiagnosisStoreInfo diagnosisStoreInfo : diagnosisForm.getDiagnosisStoreInfos()) {
            storeInfoIds.add(diagnosisStoreInfo.getStoreInfoId());
            ErpStoreInfo erpStoreInfo = diagnosisStoreInfo.getErpStoreInfo();
            if (erpStoreInfo != null) {
                // 推广门店地址
                StringBuilder build = new StringBuilder();
                build.append(erpStoreInfo.getProvinceName()).append(Constant.DASH).append(erpStoreInfo.getCityName()).append(Constant.DASH)
                                .append(erpStoreInfo.getAreaName()).append(Constant.SPACE).append(erpStoreInfo.getAddress());
                diagnosisStoreInfo.setErpStoreAddress(build.toString());
                diagnosisStoreInfo.setErpStorePhone(erpStoreInfo.getTelephone());
            }
            // 消费圈位置
            String consumptionArea = concatWithCommaFull(diagnosisStoreInfo.getConsumptionAreaList());
            diagnosisStoreInfo.setConsumptionArea(consumptionArea);
            diagnosisStoreInfo.setConsumptionAreaList(null); // 置空没用的数据,减少传输量
            // 消费圈类型
            String consumptionTypes = concatWithCommaFull(diagnosisStoreInfo.getConsumptionTypesList());
            diagnosisStoreInfo.setConsumptionTypes(consumptionTypes);
            diagnosisStoreInfo.setConsumptionTypesList(null);
            // 性别分布
            String genderDistribution = concatWithCommaFull(diagnosisStoreInfo.getGenderDistributionList());
            diagnosisStoreInfo.setGenderDistribution(genderDistribution);
            diagnosisStoreInfo.setGenderDistributionList(null);
            // 职业分布
            String occupationDistributions = concatWithCommaFull(diagnosisStoreInfo.getOccupationDistributionsList());
            diagnosisStoreInfo.setOccupationDistributions(occupationDistributions);
            diagnosisStoreInfo.setOccupationDistributionsList(null);
            // 营业时间段
            String normalBusinessHourStr = getBusinessTime(diagnosisStoreInfo.getNormalBusinessHours());
            diagnosisStoreInfo.setNormalBusinessHourStr(normalBusinessHourStr);
            diagnosisStoreInfo.setNormalBusinessHours(null);
            String peakBusinessHourStr = getBusinessTime(diagnosisStoreInfo.getPeakBusinessHours());
            diagnosisStoreInfo.setPeakBusinessHourStr(peakBusinessHourStr);
            diagnosisStoreInfo.setPeakBusinessHours(null);
            // 置空城市级别列表
            diagnosisStoreInfo.setCityLevels(null);
        }

        // 为门店填充公众号和微博信息
        fillPublicAccountAndWeiboInfo(storeInfoIds, diagnosisForm.getDiagnosisStoreInfos());
    }

    /**
     * 为门店填充公众号和微博信息
     */
    private void fillPublicAccountAndWeiboInfo(List<String> storeInfoIds, List<DiagnosisStoreInfo> diagnosisStoreInfos) {
        List<PublicAccountAndWeiboDto> publicAccountAndWeiboDtos = null;
        // 查出storeInfoIds中的所有门店
        if (CollectionUtils.isNotEmpty(storeInfoIds)) {
            publicAccountAndWeiboDtos = erpStoreInfoService.findPublicAccountAndWeibo(storeInfoIds);
        }
        if (CollectionUtils.isEmpty(publicAccountAndWeiboDtos) || CollectionUtils.isEmpty(diagnosisStoreInfos)) {
            return;
        }

        for (DiagnosisStoreInfo diagnosisStoreInfo : diagnosisStoreInfos) {
            for (PublicAccountAndWeiboDto publicAccountAndWeiboDto : publicAccountAndWeiboDtos) {
                String diagnosisStoreInfoId = diagnosisStoreInfo.getStoreInfoId();
                String erpStoreInfoId = publicAccountAndWeiboDto.getStoreInfoId();
                if (StringUtils.isNotBlank(diagnosisStoreInfoId) && StringUtils.isNotBlank(erpStoreInfoId) && diagnosisStoreInfoId
                                .equals(erpStoreInfoId)) {
                    String publicAccountPassword = publicAccountAndWeiboDto.getPublicAccountPassword();
                    if (StringUtils.isNotBlank(publicAccountPassword)) {
                        publicAccountAndWeiboDto.setPublicAccountPassword(AESUtil.decrypt(publicAccountPassword));
                    }
                    String weiboAccountPassword = publicAccountAndWeiboDto.getWeiboAccountPassword();
                    if (StringUtils.isNotBlank(weiboAccountPassword)) {
                        publicAccountAndWeiboDto.setWeiboAccountPassword(AESUtil.decrypt(weiboAccountPassword));
                    }
                    diagnosisStoreInfo.setPublicAccountAndWeiboDto(publicAccountAndWeiboDto);
                    break;
                }
            }
        }
    }


    private void encapsulatePublicAccountAndWeiboInfo(List<DiagnosisStoreInfo> diagnosisStoreInfos) {

        if (CollectionUtils.isEmpty(diagnosisStoreInfos)) {
            return;
        }
        List<String> storeInfoIds = new ArrayList<>();
        for (DiagnosisStoreInfo diagnosisStoreInfo : diagnosisStoreInfos) {
            storeInfoIds.add(diagnosisStoreInfo.getStoreInfoId());
        }
        List<PublicAccountAndWeiboDto> publicAccountAndWeiboDtos = null;
        // 查出storeInfoIds中的所有门店
        if (CollectionUtils.isNotEmpty(storeInfoIds)) {
            publicAccountAndWeiboDtos = erpStoreInfoService.findPublicAccountAndWeibo(storeInfoIds);
        }
        if (CollectionUtils.isEmpty(publicAccountAndWeiboDtos) || CollectionUtils.isEmpty(diagnosisStoreInfos)) {
            return;
        }

        for (DiagnosisStoreInfo diagnosisStoreInfo : diagnosisStoreInfos) {
            for (PublicAccountAndWeiboDto publicAccountAndWeiboDto : publicAccountAndWeiboDtos) {
                String diagnosisStoreInfoId = diagnosisStoreInfo.getStoreInfoId();
                String erpStoreInfoId = publicAccountAndWeiboDto.getStoreInfoId();
                if (StringUtils.isNotBlank(diagnosisStoreInfoId) && StringUtils.isNotBlank(erpStoreInfoId) && diagnosisStoreInfoId
                                .equals(erpStoreInfoId)) {
                    diagnosisStoreInfo.setPublicAccountAndWeiboDto(publicAccountAndWeiboDto);
                    break;
                }
            }
        }
    }

    /**
     * 获取营业时段和高峰时段。将营业时间转换成如下格式：周一、周三、周四、周日 2:30-7:30，周四至周六 9:30-17:30
     * 
     * @param businessHours
     * @return
     * @author dengyulong
     */
    private String getBusinessTime(List<DiagnosisStoreBusinessHour> businessHours) {
        if (CollectionUtils.isEmpty(businessHours)) {
            return "";
        }

        StringBuilder build = new StringBuilder();
        List<String> handedWorkDay = new ArrayList<>();
        for (DiagnosisStoreBusinessHour busHour : businessHours) { // NOSONAR
            String workDay = busHour.getWorkdays();
            if (StringUtils.isBlank(workDay)) {
                continue;
            }
            // 已经处理过
            if (handedWorkDay.contains(workDay)) {
                continue;
            }

            List<String> workDayList = Arrays.asList(workDay.split(Constant.COMMA));
            if (CollectionUtils.isEmpty(workDayList)) {
                continue;
            }

            Collections.sort(workDayList);
            boolean flag = isWorkDayContinuous(workDayList);
            // 连续
            if (flag) {
                build.append(getZhWorkDay(workDayList.get(0))).append(TO_CN).append(getZhWorkDay(workDayList.get(workDayList.size() - 1)));

            } else {
                for (int i = 0; i < workDayList.size(); i++) {
                    build.append(getZhWorkDay(workDayList.get(i)));
                    if (i != workDayList.size() - 1) {
                        build.append(Constant.DUN_HAO);
                    }
                }
            }

            for (DiagnosisStoreBusinessHour tbusHour : businessHours) {
                if (busHour.getWorkdays().equals(tbusHour.getWorkdays())) {
                    build.append(Constant.SPACE).append(tbusHour.getStartTimeStr()).append(Constant.DASH).append(tbusHour.getEndTimeStr());
                }
            }

            build.append(Constant.COMMA_FULL);

            handedWorkDay.add(workDay);
        }

        String businessHour = build.toString();
        if (businessHour.endsWith(Constant.DUN_HAO)) {
            businessHour = businessHour.substring(0, businessHour.lastIndexOf(Constant.DUN_HAO));
        }
        if (businessHour.endsWith(Constant.COMMA_FULL)) {
            businessHour = businessHour.substring(0, businessHour.lastIndexOf(Constant.COMMA_FULL));
        }
        return businessHour;
    }

    /**
     * 返回周几
     * 
     * @param day 1到7
     * @return
     */
    private static String getZhWorkDay(String day) {
        List<Dict> workdays = DictUtils.getDictList(DiagnosisConstant.WORKDAY_DICT_TYPE);
        if (CollectionUtils.isNotEmpty(workdays)) {
            for (Dict dict : workdays) {
                if (dict.getValue().equals(day)) {
                    return dict.getLabel();
                }
            }
        }
        return "";
    }

    /**
     * 判断workday是否连续
     * 
     * @return
     */
    private static boolean isWorkDayContinuous(List<String> workDayList) {
        boolean flag = false;
        for (int i = 0; i < workDayList.size() - 1; i++) {
            if ((Integer.parseInt(workDayList.get(i)) + 1) == Integer.parseInt(workDayList.get(i + 1))) {
                flag = true;
            } else {
                return false;
            }
        }
        return flag;
    }


    @Transactional
    public BaseResult updateDiagnosisData(DiagnosisRequestDto diagnosisRequestDto) {
        DiagnosisForm diagnosisForm = diagnosisRequestDto.getDiagnosisForm();
        if (diagnosisForm == null) {
            throw new ServiceException();
        }

        // 更新表diagnosis_form
        updateDiagnosisForm(diagnosisForm);

        // 更新表diagnosis_split_industry_attribute
        saveIndustryAttributes(diagnosisForm);

        // 更新表erp_order_split_info 投放开始时间（promotion_time）
        updateOrderSplitInfo(diagnosisForm);

        // 更新表erp_store_info,diagnosis_store_info,diagnosis_store_business_hour,erp_store_advertiser_friends,erp_store_advertiser_weibo
        updateDiagnosisStoreInfos(diagnosisForm);

        // 更新表diagnosis_discount
        saveDiagnosisDiscount(diagnosisForm);

        // 更新表diagnosis_first_ad_image,diagnosis_second_ad_image
        updateDiagnosisAdImage(diagnosisRequestDto);

        // 更新卡券信息
        updateDiagnosisCardCoupon(diagnosisForm);

        // 记录日志操作
        ErpPromotionMaterialLog log = new ErpPromotionMaterialLog();
        log.setPromotionMaterialsId(diagnosisRequestDto.getDiagnosisMaterialType());
        log.setOperateTime(new Date());
        log.setOperateType(DiagnosisConstant.MODIFYING);
        log.setOperator(UserUtils.getUser().getId());
        log.setSplitId(diagnosisForm.getSplitId());
        log.setRemarks("");
        erpPromotionMaterialLogService.save(log);

        return new BaseResult();
    }

    private void updateDiagnosisCardCoupon(DiagnosisForm diagnosisForm) {
        List<DiagnosisCardCoupons> diagnosisCardCoupons = diagnosisForm.getDiagnosisCardCoupons();
        saveOrUpdateDiagnosisCoupon(diagnosisCardCoupons);
    }

    private void updateDiagnosisAdImage(DiagnosisRequestDto diagnosisRequestDto) {
        List<DiagnosisFirstAdImage> diagnosisFirstAdImages = diagnosisRequestDto.getDiagnosisForm().getFirstAdImages();
        saveDiagnosisFirstAdImages(diagnosisFirstAdImages);

        List<DiagnosisSecondAdImage> diagnosisSecondAdImages = diagnosisRequestDto.getDiagnosisForm().getSecondAdImages();
        saveDiagnosisSecondAdImage(diagnosisSecondAdImages);
    }

    private void updatePublicAccountAndWeibo(DiagnosisStoreInfo diagnosisStoreInfo) {
        PublicAccountAndWeiboDto publicAccountAndWeiboDto = diagnosisStoreInfo.getPublicAccountAndWeiboDto();
        String publicAccountId = publicAccountAndWeiboDto.getPublicAccountId();
        ErpStoreAdvertiserFriends erpStoreAdvertiserFriendsDB = erpStoreAdvertiserFriendsService.get(publicAccountId);
        erpStoreAdvertiserFriendsDB.setAccountNo(publicAccountAndWeiboDto.getPublicAccountNo());
        erpStoreAdvertiserFriendsDB.setAccountPassword(AESUtil.encrypt(publicAccountAndWeiboDto.getPublicAccountPassword()));
        erpStoreAdvertiserFriendsDB.setAccountOriginalId(publicAccountAndWeiboDto.getPublicAccountOriginalId());
        erpStoreAdvertiserFriendsService.update(erpStoreAdvertiserFriendsDB);

        String weiboAccountId = publicAccountAndWeiboDto.getWeiboAccountId();
        ErpStoreAdvertiserWeibo erpStoreAdvertiserWeiboDB = erpStoreAdvertiserWeiboService.get(weiboAccountId);
        erpStoreAdvertiserWeiboDB.setAccountNo(publicAccountAndWeiboDto.getWeiboAccountNo());
        erpStoreAdvertiserWeiboDB.setAccountPassword(AESUtil.encrypt(publicAccountAndWeiboDto.getWeiboAccountPassword()));
        erpStoreAdvertiserWeiboService.update(erpStoreAdvertiserWeiboDB);
    }

    private void updateOrderSplitInfo(DiagnosisForm diagnosisForm) {
        ErpOrderSplitInfo erpOrderSplitInfoDB = erpOrderSplitInfoDao.get(diagnosisForm.getSplitId());
        if (!erpOrderSplitInfoDB.getPromotionTime().toString().equals(diagnosisForm.getPromotionTime().toString())) {
            erpOrderSplitInfoDB.setPromotionTime(diagnosisForm.getPromotionTime());
            erpOrderSplitInfoDB.preUpdate();
            erpOrderSplitInfoDao.update(erpOrderSplitInfoDB);
        }
    }

    private void updateDiagnosisForm(DiagnosisForm diagnosisForm) {
        DiagnosisForm diagnosisFormDB = get(diagnosisForm.getId());
        diagnosisFormDB.setShopUsername(diagnosisForm.getShopUsername());
        diagnosisFormDB.setShopPassword(AESUtil.encrypt(diagnosisForm.getShopPassword()));
        diagnosisFormDB.setPushArea(diagnosisForm.getPushArea());
        diagnosisFormDB.setMajorProduct(diagnosisForm.getMainPush());
        diagnosisFormDB.setPromoteProduct(diagnosisForm.getPromoteProduct());
        diagnosisFormDB.setBrandLightspot(diagnosisForm.getBrandLightspot());
        diagnosisFormDB.setOriginalityCulture(diagnosisForm.getOriginalityCulture());
        diagnosisFormDB.setActivityRequirements(diagnosisForm.getActivityRequirements());
        diagnosisFormDB.setMainPush(diagnosisForm.getMainPush());
        diagnosisFormDB.setReferenceMaterial(diagnosisForm.getReferenceMaterial());
        diagnosisFormDB.setActivityGoal(diagnosisForm.getActivityGoal());
        diagnosisFormDB.setFirstPropagandaContent(diagnosisForm.getFirstPropagandaContent());
        diagnosisFormDB.setSecondPropagandaContent(diagnosisForm.getSecondPropagandaContent());
        diagnosisFormDB.preUpdate();
        diagnosisFormDao.update(diagnosisFormDB);
    }

}
