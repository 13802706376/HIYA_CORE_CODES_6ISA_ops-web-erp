package com.yunnex.ops.erp.modules.diagnosis.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitGood;
import com.yunnex.ops.erp.modules.sys.entity.Dict;

/**
 * 经营诊断营销策划表单Entity
 * 
 * @author yunnex
 * @version 2018-03-29
 */
public class DiagnosisForm extends DataEntity<DiagnosisForm> {

    private static final long serialVersionUID = 1L;
    private String splitId; // 分单ID
    private String packageAdditional; // 套餐其他信息补充
    private String serviceKnow; // 商户产品／服务信息了解（大众点评）
    private String contactPerson; // 本次推广联系人
    private String contactPhone; // 本次推广联系人电话

    private String majorProduct; // 主打产品/服务特色
    private String promoteProduct; // 本次推广的产品/服务特色
    private String activityRequirements; // 活动主题/推广需求，多个以半角逗号隔开
    private String activityGoal; // 活动目的，多个以半角逗号隔开
    private String brandLightspot; // 品牌文化亮点
    private String originalityCulture; // 创意文化
    private String diagnosisContentAdditional; // 电话后补充的诊断内容
    private String referenceMaterial; // 参考推文或材料
    private String mainPush; // 主推
    private String backupFirst; // 备选1
    private String backupSecond; // 备选2
    private String pushArea; // 投放地域
    private String shopUsername; // 掌贝后台账号
    private String shopPassword; // 掌贝后台密码
    private String firstPropagandaContent; // 第一层重点宣传文案
    private String secondPropagandaContent; // 第二层重点宣传文案

    /* 封装数据属性 */
    private String packageSelection; // 本次推广套餐选择，多个以半角逗号隔开
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date promotionTime; // 推广时间/投放开始时间
    private String industryAttribute; // 行业属性
    private String promotionChannel; // 推广通道
    private Integer promotionStoreNum; // 推广门店数量
    private String shopCityLevel; // 商户的城市级别
    private String activityRequirementStr; // 活动主题/推广需求，显示名称
    private ErpOrderOriginalInfo erpOrderOriginalInfo; // 订单信息
    private List<DiagnosisStoreInfo> diagnosisStoreInfos; // 经营诊断门店信息
    private List<Dict> activityRequirementsList; // 活动主题/推广需求
    private List<Dict> activityGoalList; // 活动目的
    private List<DiagnosisDiscount> diagnosisDiscounts; // 以往及进行中的优惠内容
    private List<ErpOrderSplitGood> erpOrderSplitGoods; // 本次推广套餐
    private List<DiagnosisCardCoupons> diagnosisCardCoupons; // 卡券信息
    private List<DiagnosisIndustryAttribute> industryAttributes; // 行业属性
    private List<DiagnosisSplitIndustryAttribute> splitIndustryAttributes; // 分单行业属性关联
    private List<DiagnosisDiscountTypeRecommend> discountTypeRecommends; // 推荐的优惠形式
    private List<DiagnosisFirstAdImage> firstAdImages; // 第一层广告图
    private List<DiagnosisSecondAdImage> secondAdImages; // 第二层广告图
    private String createrName; // 创建者名字

    public DiagnosisForm() {
        super();
    }

    public DiagnosisForm(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "分单ID长度必须介于 1 和 64 之间")
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    @Length(min = 0, max = 512, message = "套餐其他信息补充长度必须介于 0 和 512 之间")
    public String getPackageAdditional() {
        return packageAdditional;
    }

    public void setPackageAdditional(String packageAdditional) {
        this.packageAdditional = packageAdditional;
    }

    @Length(min = 0, max = 64, message = "商户产品／服务信息了解（大众点评）长度必须介于 0 和 64 之间")
    public String getServiceKnow() {
        return serviceKnow;
    }

    public void setServiceKnow(String serviceKnow) {
        this.serviceKnow = serviceKnow;
    }

    @Length(min = 0, max = 30, message = "本次推广联系人长度必须介于 0 和 30 之间")
    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    @Length(min = 0, max = 20, message = "本次推广联系人电话长度必须介于 0 和 20 之间")
    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @Length(min = 0, max = 300, message = "本次推广套餐选择，多个以半角逗号隔开长度必须介于 0 和 300 之间")
    public String getPackageSelection() {
        return packageSelection;
    }

    public void setPackageSelection(String packageSelection) {
        this.packageSelection = packageSelection;
    }

    @Length(min = 0, max = 1000, message = "主打产品/服务特色长度必须介于 0 和 1000 之间")
    public String getMajorProduct() {
        return majorProduct;
    }

    public void setMajorProduct(String majorProduct) {
        this.majorProduct = majorProduct;
    }

    @Length(min = 0, max = 1000, message = "本次推广的产品/服务特色长度必须介于 0 和 1000 之间")
    public String getPromoteProduct() {
        return promoteProduct;
    }

    public void setPromoteProduct(String promoteProduct) {
        this.promoteProduct = promoteProduct;
    }

    @Length(min = 1, max = 100, message = "活动主题/推广需求，多个以半角逗号隔开长度必须介于 1 和 100 之间")
    public String getActivityRequirements() {
        return activityRequirements;
    }

    public void setActivityRequirements(String activityRequirements) {
        this.activityRequirements = activityRequirements;
    }

    @Length(min = 1, max = 255, message = "活动目的，多个以半角逗号隔开长度必须介于 1 和 255 之间")
    public String getActivityGoal() {
        return activityGoal;
    }

    public void setActivityGoal(String activityGoal) {
        this.activityGoal = activityGoal;
    }

    @Length(min = 1, max = 1000, message = "品牌文化亮点长度必须介于 1 和 1000 之间")
    public String getBrandLightspot() {
        return brandLightspot;
    }

    public void setBrandLightspot(String brandLightspot) {
        this.brandLightspot = brandLightspot;
    }

    @Length(min = 1, max = 1000, message = "创意文化长度必须介于 1 和 1000 之间")
    public String getOriginalityCulture() {
        return originalityCulture;
    }

    public void setOriginalityCulture(String originalityCulture) {
        this.originalityCulture = originalityCulture;
    }

    @Length(min = 1, max = 1000, message = "电话后补充的诊断内容长度必须介于 1 和 1000 之间")
    public String getDiagnosisContentAdditional() {
        return diagnosisContentAdditional;
    }

    public void setDiagnosisContentAdditional(String diagnosisContentAdditional) {
        this.diagnosisContentAdditional = diagnosisContentAdditional;
    }

    @Length(min = 1, max = 1000, message = "参考推文或材料长度必须介于 1 和 1000 之间")
    public String getReferenceMaterial() {
        return referenceMaterial;
    }

    public void setReferenceMaterial(String referenceMaterial) {
        this.referenceMaterial = referenceMaterial;
    }

    @Length(min = 1, max = 255, message = "主推长度必须介于 1 和 255 之间")
    public String getMainPush() {
        return mainPush;
    }

    public void setMainPush(String mainPush) {
        this.mainPush = mainPush;
    }

    @Length(min = 1, max = 255, message = "备选1长度必须介于 1 和 255 之间")
    public String getBackupFirst() {
        return backupFirst;
    }

    public void setBackupFirst(String backupFirst) {
        this.backupFirst = backupFirst;
    }

    @Length(min = 1, max = 255, message = "备选2长度必须介于 1 和 255 之间")
    public String getBackupSecond() {
        return backupSecond;
    }

    public void setBackupSecond(String backupSecond) {
        this.backupSecond = backupSecond;
    }

    @Length(min = 1, max = 255, message = "投放地域长度必须介于 1 和 255 之间")
    public String getPushArea() {
        return pushArea;
    }

    public void setPushArea(String pushArea) {
        this.pushArea = pushArea;
    }

    @Length(min = 1, max = 45, message = "掌贝后台账号长度必须介于 1 和 45 之间")
    public String getShopUsername() {
        return shopUsername;
    }

    public void setShopUsername(String shopUsername) {
        this.shopUsername = shopUsername;
    }

    @Length(min = 1, max = 255, message = "掌贝后台密码长度必须介于 1 和 255 之间")
    public String getShopPassword() {
        return shopPassword;
    }

    public void setShopPassword(String shopPassword) {
        this.shopPassword = shopPassword;
    }

    @Length(min = 1, max = 1000, message = "第一层重点宣传文案长度必须介于 1 和 1000 之间")
    public String getFirstPropagandaContent() {
        return firstPropagandaContent;
    }

    public void setFirstPropagandaContent(String firstPropagandaContent) {
        this.firstPropagandaContent = firstPropagandaContent;
    }

    @Length(min = 1, max = 1000, message = "第二层重点宣传文案长度必须介于 1 和 1000 之间")
    public String getSecondPropagandaContent() {
        return secondPropagandaContent;
    }

    public void setSecondPropagandaContent(String secondPropagandaContent) {
        this.secondPropagandaContent = secondPropagandaContent;
    }

    public String getIndustryAttribute() {
        return industryAttribute;
    }

    public void setIndustryAttribute(String industryAttribute) {
        this.industryAttribute = industryAttribute;
    }

    public List<DiagnosisStoreInfo> getDiagnosisStoreInfos() {
        return diagnosisStoreInfos;
    }

    public void setDiagnosisStoreInfos(List<DiagnosisStoreInfo> diagnosisStoreInfos) {
        this.diagnosisStoreInfos = diagnosisStoreInfos;
    }

    public String getPromotionChannel() {
        return promotionChannel;
    }

    public void setPromotionChannel(String promotionChannel) {
        this.promotionChannel = promotionChannel;
    }

    public Date getPromotionTime() {
        return promotionTime;
    }

    public void setPromotionTime(Date promotionTime) {
        this.promotionTime = promotionTime;
    }

    public String getShopCityLevel() {
        return shopCityLevel;
    }

    public void setShopCityLevel(String shopCityLevel) {
        this.shopCityLevel = shopCityLevel;
    }

    public String getActivityRequirementStr() {
        return activityRequirementStr;
    }

    public void setActivityRequirementStr(String activityRequirementStr) {
        this.activityRequirementStr = activityRequirementStr;
    }

    public ErpOrderOriginalInfo getErpOrderOriginalInfo() {
        return erpOrderOriginalInfo;
    }

    public void setErpOrderOriginalInfo(ErpOrderOriginalInfo erpOrderOriginalInfo) {
        this.erpOrderOriginalInfo = erpOrderOriginalInfo;
    }

    public List<Dict> getActivityRequirementsList() {
        return activityRequirementsList;
    }

    public void setActivityRequirementsList(List<Dict> activityRequirementsList) {
        this.activityRequirementsList = activityRequirementsList;
    }

    public List<Dict> getActivityGoalList() {
        return activityGoalList;
    }

    public void setActivityGoalList(List<Dict> activityGoalList) {
        this.activityGoalList = activityGoalList;
    }

    public List<DiagnosisDiscount> getDiagnosisDiscounts() {
        return diagnosisDiscounts;
    }

    public void setDiagnosisDiscounts(List<DiagnosisDiscount> diagnosisDiscounts) {
        this.diagnosisDiscounts = diagnosisDiscounts;
    }

    public List<ErpOrderSplitGood> getErpOrderSplitGoods() {
        return erpOrderSplitGoods;
    }

    public void setErpOrderSplitGoods(List<ErpOrderSplitGood> erpOrderSplitGoods) {
        this.erpOrderSplitGoods = erpOrderSplitGoods;
    }

    public Integer getPromotionStoreNum() {
        return promotionStoreNum;
    }

    public void setPromotionStoreNum(Integer promotionStoreNum) {
        this.promotionStoreNum = promotionStoreNum;
    }

    public List<DiagnosisCardCoupons> getDiagnosisCardCoupons() {
        return diagnosisCardCoupons;
    }

    public void setDiagnosisCardCoupons(List<DiagnosisCardCoupons> diagnosisCardCoupons) {
        this.diagnosisCardCoupons = diagnosisCardCoupons;
    }

    public List<DiagnosisIndustryAttribute> getIndustryAttributes() {
        return industryAttributes;
    }

    public void setIndustryAttributes(List<DiagnosisIndustryAttribute> industryAttributes) {
        this.industryAttributes = industryAttributes;
    }


    public List<DiagnosisSplitIndustryAttribute> getSplitIndustryAttributes() {
        return splitIndustryAttributes;
    }

    public void setSplitIndustryAttributes(List<DiagnosisSplitIndustryAttribute> splitIndustryAttributes) {
        this.splitIndustryAttributes = splitIndustryAttributes;
    }

    public List<DiagnosisDiscountTypeRecommend> getDiscountTypeRecommends() {
        return discountTypeRecommends;
    }

    public void setDiscountTypeRecommends(List<DiagnosisDiscountTypeRecommend> discountTypeRecommends) {
        this.discountTypeRecommends = discountTypeRecommends;
    }

    public List<DiagnosisFirstAdImage> getFirstAdImages() {
        return firstAdImages;
    }

    public void setFirstAdImages(List<DiagnosisFirstAdImage> firstAdImages) {
        this.firstAdImages = firstAdImages;
    }

    public List<DiagnosisSecondAdImage> getSecondAdImages() {
        return secondAdImages;
    }

    public void setSecondAdImages(List<DiagnosisSecondAdImage> secondAdImages) {
        this.secondAdImages = secondAdImages;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
