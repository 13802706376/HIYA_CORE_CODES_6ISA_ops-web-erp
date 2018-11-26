package com.yunnex.ops.erp.modules.diagnosis.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.modules.store.basic.dto.PublicAccountAndWeiboDto;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.sys.entity.Dict;

/**
 * 经营诊断的门店信息Entity.
 * 多个分单可能绑定同一个门店。同一个门店所有信息相同。
 * @author yunnex
 * @version 2018-03-29
 */
public class DiagnosisStoreInfo extends DataEntity<DiagnosisStoreInfo> {

    private static final long serialVersionUID = 1L;
    private String storeInfoId; // 门店ID
    private String cityLevel; // 城市级别（数据字典）
    private String consumptionArea; // 消费圈位置（数据字典）
    private String consumptionTypes; // 消费圈类型（数据字典；例：,1,2,）
    private String trafficGuide; // 交通指引
    private Integer personAvgPriceMin; // 人均客单价最小值
    private Integer personAvgPriceMax; // 人均客单价最大值
    private Integer tableAvgPersonNumMin; // 桌均人数最少值
    private Integer tableAvgPersonNumMax; // 桌均人数最大值
    private String genderDistribution; // 性别分布（数据字典）
    private String occupationDistributions; // 职业分布（数据字典；例：,1,2,）
    private Integer ageDistributionMin; // 年龄分布最少值
    private Integer ageDistributionMax; // 年龄分布最大值
    private String dianpingStoreName; // 大众点评店名
    private String dianpingLink; // 大众点评点评链接
    private String dianpingRanking; // 大众点评排名

    /* 数据封装字段 */
    private String erpStoreName; // 门店名称
    private String erpStoreAddress; // 门店详细地址
    private String erpStorePhone; // 门店联系电话
    private Integer tableAvgPriceMin; // 桌均客单价最小值
    private Integer tableAvgPriceMax; // 桌均客单价最大值
    private String normalBusinessHourStr; // 正常营业时间段
    private String peakBusinessHourStr; // 高峰营业时间段
    private ErpStoreInfo erpStoreInfo; // ERP门店信息，门店信息主表
    private PublicAccountAndWeiboDto publicAccountAndWeiboDto; // 公众号和微博信息
    private List<DiagnosisStoreBusinessHour> businessHours; // 营业时间
    private List<DiagnosisStoreBusinessHour> normalBusinessHours; // 正常营业时间
    private List<DiagnosisStoreBusinessHour> peakBusinessHours; // 高峰营业时间
    private List<Dict> cityLevels; // 城市级别
    private List<Dict> consumptionAreaList; // 消费圈位置
    private List<Dict> consumptionTypesList; // 消费圈类型
    private List<Dict> genderDistributionList; // 性别分布（数据字典）
    private List<Dict> occupationDistributionsList; // 职业分布（数据字典）

    public DiagnosisStoreInfo() {
        super();
    }

    public DiagnosisStoreInfo(String id) {
        super(id);
    }

    public Integer getTableAvgPriceMin() {
        tableAvgPriceMin = 0;
        if (personAvgPriceMin != null && tableAvgPersonNumMin != null) {
            tableAvgPriceMin = personAvgPriceMin * tableAvgPersonNumMin;
        }
        return tableAvgPriceMin;
    }

    public Integer getTableAvgPriceMax() {
        tableAvgPriceMax = 0;
        if (personAvgPriceMax != null && tableAvgPersonNumMax != null) {
            tableAvgPriceMax = personAvgPriceMax * tableAvgPersonNumMax;
        }
        return tableAvgPriceMax;
    }

    public void setTableAvgPriceMin(Integer tableAvgPriceMin) {
        this.tableAvgPriceMin = tableAvgPriceMin;
    }

    public void setTableAvgPriceMax(Integer tableAvgPriceMax) {
        this.tableAvgPriceMax = tableAvgPriceMax;
    }

    public String getErpStoreName() {
        return erpStoreName;
    }

    public void setErpStoreName(String erpStoreName) {
        this.erpStoreName = erpStoreName;
    }

    public String getErpStoreAddress() {
        return erpStoreAddress;
    }

    public void setErpStoreAddress(String erpStoreAddress) {
        this.erpStoreAddress = erpStoreAddress;
    }

    public String getErpStorePhone() {
        return erpStorePhone;
    }

    public void setErpStorePhone(String erpStorePhone) {
        this.erpStorePhone = erpStorePhone;
    }

    @Length(min = 1, max = 64, message = "门店ID长度必须介于 1 和 64 之间")
    public String getStoreInfoId() {
        return storeInfoId;
    }

    public void setStoreInfoId(String storeInfoId) {
        this.storeInfoId = storeInfoId;
    }

    @Length(min = 1, max = 20, message = "城市级别（数据字典）长度必须介于 1 和 20 之间")
    public String getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(String cityLevel) {
        this.cityLevel = cityLevel;
    }

    @Length(min = 1, max = 100, message = "消费圈位置（数据字典）长度必须介于 1 和 100 之间")
    public String getConsumptionArea() {
        return consumptionArea;
    }

    public void setConsumptionArea(String consumptionArea) {
        this.consumptionArea = consumptionArea;
    }

    @Length(min = 1, max = 100, message = "消费圈类型（数据字典；例：,1,2,）长度必须介于 1 和 100 之间")
    public String getConsumptionTypes() {
        return consumptionTypes;
    }

    public void setConsumptionTypes(String consumptionTypes) {
        this.consumptionTypes = consumptionTypes;
    }

    @Length(min = 1, max = 500, message = "交通指引长度必须介于 1 和 500 之间")
    public String getTrafficGuide() {
        return trafficGuide;
    }

    public void setTrafficGuide(String trafficGuide) {
        this.trafficGuide = trafficGuide;
    }

    @NotNull(message = "人均客单价最小值不能为空")
    public Integer getPersonAvgPriceMin() {
        return personAvgPriceMin;
    }

    public void setPersonAvgPriceMin(Integer personAvgPriceMin) {
        this.personAvgPriceMin = personAvgPriceMin;
    }

    @NotNull(message = "人均客单价最大值不能为空")
    public Integer getPersonAvgPriceMax() {
        return personAvgPriceMax;
    }

    public void setPersonAvgPriceMax(Integer personAvgPriceMax) {
        this.personAvgPriceMax = personAvgPriceMax;
    }

    @NotNull(message = "桌均人数最少值不能为空")
    public Integer getTableAvgPersonNumMin() {
        return tableAvgPersonNumMin;
    }

    public void setTableAvgPersonNumMin(Integer tableAvgPersonNumMin) {
        this.tableAvgPersonNumMin = tableAvgPersonNumMin;
    }

    @NotNull(message = "桌均人数最大值不能为空")
    public Integer getTableAvgPersonNumMax() {
        return tableAvgPersonNumMax;
    }

    public void setTableAvgPersonNumMax(Integer tableAvgPersonNumMax) {
        this.tableAvgPersonNumMax = tableAvgPersonNumMax;
    }

    @Length(min = 1, max = 100, message = "性别分布（数据字典）长度必须介于 1 和 100 之间")
    public String getGenderDistribution() {
        return genderDistribution;
    }

    public void setGenderDistribution(String genderDistribution) {
        this.genderDistribution = genderDistribution;
    }

    @Length(min = 1, max = 100, message = "职业分布（数据字典；例：,1,2,）长度必须介于 1 和 100 之间")
    public String getOccupationDistributions() {
        return occupationDistributions;
    }

    public void setOccupationDistributions(String occupationDistributions) {
        this.occupationDistributions = occupationDistributions;
    }

    @NotNull(message = "年龄分布最少值不能为空")
    public Integer getAgeDistributionMin() {
        return ageDistributionMin;
    }

    public void setAgeDistributionMin(Integer ageDistributionMin) {
        this.ageDistributionMin = ageDistributionMin;
    }

    @NotNull(message = "年龄分布最大值不能为空")
    public Integer getAgeDistributionMax() {
        return ageDistributionMax;
    }

    public void setAgeDistributionMax(Integer ageDistributionMax) {
        this.ageDistributionMax = ageDistributionMax;
    }

    @Length(min = 1, max = 255, message = "大众点评店名长度必须介于 1 和 255 之间")
    public String getDianpingStoreName() {
        return dianpingStoreName;
    }

    public void setDianpingStoreName(String dianpingStoreName) {
        this.dianpingStoreName = dianpingStoreName;
    }

    @Length(min = 1, max = 500, message = "大众点评点评链接长度必须介于 1 和 500 之间")
    public String getDianpingLink() {
        return dianpingLink;
    }

    public void setDianpingLink(String dianpingLink) {
        this.dianpingLink = dianpingLink;
    }

    @Length(min = 1, max = 500, message = "大众点评排名长度必须介于 1 和 500 之间")
    public String getDianpingRanking() {
        return dianpingRanking;
    }

    public void setDianpingRanking(String dianpingRanking) {
        this.dianpingRanking = dianpingRanking;
    }

    public ErpStoreInfo getErpStoreInfo() {
        return erpStoreInfo;
    }

    public void setErpStoreInfo(ErpStoreInfo erpStoreInfo) {
        this.erpStoreInfo = erpStoreInfo;
    }

    public PublicAccountAndWeiboDto getPublicAccountAndWeiboDto() {
        return publicAccountAndWeiboDto;
    }

    public void setPublicAccountAndWeiboDto(PublicAccountAndWeiboDto publicAccountAndWeiboDto) {
        this.publicAccountAndWeiboDto = publicAccountAndWeiboDto;
    }

    public String getNormalBusinessHourStr() {
        return normalBusinessHourStr;
    }

    public void setNormalBusinessHourStr(String normalBusinessHourStr) {
        this.normalBusinessHourStr = normalBusinessHourStr;
    }

    public String getPeakBusinessHourStr() {
        return peakBusinessHourStr;
    }

    public void setPeakBusinessHourStr(String peakBusinessHourStr) {
        this.peakBusinessHourStr = peakBusinessHourStr;
    }

    public List<DiagnosisStoreBusinessHour> getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(List<DiagnosisStoreBusinessHour> businessHours) {
        this.businessHours = businessHours;
    }

    public List<DiagnosisStoreBusinessHour> getNormalBusinessHours() {
        return normalBusinessHours;
    }

    public void setNormalBusinessHours(List<DiagnosisStoreBusinessHour> normalBusinessHours) {
        this.normalBusinessHours = normalBusinessHours;
    }

    public List<DiagnosisStoreBusinessHour> getPeakBusinessHours() {
        return peakBusinessHours;
    }

    public void setPeakBusinessHours(List<DiagnosisStoreBusinessHour> peakBusinessHours) {
        this.peakBusinessHours = peakBusinessHours;
    }

    public List<Dict> getCityLevels() {
        return cityLevels;
    }

    public void setCityLevels(List<Dict> cityLevels) {
        this.cityLevels = cityLevels;
    }

    public List<Dict> getConsumptionAreaList() {
        return consumptionAreaList;
    }

    public void setConsumptionAreaList(List<Dict> consumptionAreaList) {
        this.consumptionAreaList = consumptionAreaList;
    }

    public List<Dict> getConsumptionTypesList() {
        return consumptionTypesList;
    }

    public void setConsumptionTypesList(List<Dict> consumptionTypesList) {
        this.consumptionTypesList = consumptionTypesList;
    }

    public List<Dict> getGenderDistributionList() {
        return genderDistributionList;
    }

    public void setGenderDistributionList(List<Dict> genderDistributionList) {
        this.genderDistributionList = genderDistributionList;
    }

    public List<Dict> getOccupationDistributionsList() {
        return occupationDistributionsList;
    }

    public void setOccupationDistributionsList(List<Dict> occupationDistributionsList) {
        this.occupationDistributionsList = occupationDistributionsList;
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
