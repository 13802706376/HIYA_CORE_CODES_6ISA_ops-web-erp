package com.yunnex.ops.erp.modules.diagnosis.dto;

import java.util.List;

import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisForm;
import com.yunnex.ops.erp.modules.sys.entity.Dict;

import yunnex.common.core.dto.BaseDto;


public class DiagnosisFormResponseDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DiagnosisForm diagnosisForm;
    private List<Dict> consumptionAreaList; // 消费圈位置字典信息
    private List<Dict> consumptionTypeList;// 消费圈类型字典信息
    private List<Dict> workdayList;// 工作日
    private List<Dict> occupationDistributionList;// 职业分布
    private List<Dict> genderDistributionList;// 性别分布
    private List<Dict> activityRequirementList;// 推广需求的数据字典类型
    private List<Dict> activityGoalList;// 活动目的的数据字典类型
    private List<String> packageInfos; // 拼接好的套餐信息
    private String resDomain; // 访问资源的域名

    public DiagnosisForm getDiagnosisForm() {
        return diagnosisForm;
    }

    public void setDiagnosisForm(DiagnosisForm diagnosisForm) {
        this.diagnosisForm = diagnosisForm;
    }

    public List<Dict> getConsumptionAreaList() {
        return consumptionAreaList;
    }

    public void setConsumptionAreaList(List<Dict> consumptionAreaList) {
        this.consumptionAreaList = consumptionAreaList;
    }

    public List<Dict> getConsumptionTypeList() {
        return consumptionTypeList;
    }

    public void setConsumptionTypeList(List<Dict> consumptionTypeList) {
        this.consumptionTypeList = consumptionTypeList;
    }

    public List<Dict> getWorkdayList() {
        return workdayList;
    }

    public void setWorkdayList(List<Dict> workdayList) {
        this.workdayList = workdayList;
    }

    public List<Dict> getOccupationDistributionList() {
        return occupationDistributionList;
    }

    public void setOccupationDistributionList(List<Dict> occupationDistributionList) {
        this.occupationDistributionList = occupationDistributionList;
    }

    public List<Dict> getGenderDistributionList() {
        return genderDistributionList;
    }

    public void setGenderDistributionList(List<Dict> genderDistributionList) {
        this.genderDistributionList = genderDistributionList;
    }

    public List<String> getPackageInfos() {
        return packageInfos;
    }

    public void setPackageInfos(List<String> packageInfos) {
        this.packageInfos = packageInfos;
    }

    public List<Dict> getActivityRequirementList() {
        return activityRequirementList;
    }

    public void setActivityRequirementList(List<Dict> activityRequirementList) {
        this.activityRequirementList = activityRequirementList;
    }

    public List<Dict> getActivityGoalList() {
        return activityGoalList;
    }

    public void setActivityGoalList(List<Dict> activityGoalList) {
        this.activityGoalList = activityGoalList;
    }

    public String getResDomain() {
        return resDomain;
    }

    public void setResDomain(String resDomain) {
        this.resDomain = resDomain;
    }
}
