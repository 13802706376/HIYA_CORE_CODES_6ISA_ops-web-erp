package com.yunnex.ops.erp.modules.promotion.dto;

import java.util.List;
import java.util.Map;

import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;
import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderInputDetail;

import yunnex.common.core.dto.BaseDto;

public class PromotionMaterialsResponseDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<ErpOrderFile> erpOrderFiles; //任务相关文件
    private List<ErpOrderInputDetail> erpOrderInputDetails;// 任务相关资料
    private Map<String, String> diagnosisPromotionMaterialType; // 文案-diagnosis_copywriting，设计-diagnosis_design，商户-diagnosis_merchant，投放顾问-diagnosis_consultant
    private String splitId;// 分单ID
    private Map<String, String> diagnosisPromotionMaterialInfo;// 经营诊断推广资料的创建人创建时间
    private Map<String, String> couponInfoPromotionMaterialInfo;// 卡券信息推广资料的创建人创建时间

    public List<ErpOrderFile> getErpOrderFiles() {
        return erpOrderFiles;
    }

    public void setErpOrderFiles(List<ErpOrderFile> erpOrderFiles) {
        this.erpOrderFiles = erpOrderFiles;
    }

    public List<ErpOrderInputDetail> getErpOrderInputDetails() {
        return erpOrderInputDetails;
    }

    public void setErpOrderInputDetails(List<ErpOrderInputDetail> erpOrderInputDetails) {
        this.erpOrderInputDetails = erpOrderInputDetails;
    }

    public Map<String, String> getDiagnosisPromotionMaterialType() {
        return diagnosisPromotionMaterialType;
    }

    public void setDiagnosisPromotionMaterialType(Map<String, String> diagnosisPromotionMaterialType) {
        this.diagnosisPromotionMaterialType = diagnosisPromotionMaterialType;
    }

    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    public Map<String, String> getDiagnosisPromotionMaterialInfo() {
        return diagnosisPromotionMaterialInfo;
    }

    public void setDiagnosisPromotionMaterialInfo(Map<String, String> diagnosisPromotionMaterialInfo) {
        this.diagnosisPromotionMaterialInfo = diagnosisPromotionMaterialInfo;
    }

    public Map<String, String> getCouponInfoPromotionMaterialInfo() {
        return couponInfoPromotionMaterialInfo;
    }

    public void setCouponInfoPromotionMaterialInfo(Map<String, String> couponInfoPromotionMaterialInfo) {
        this.couponInfoPromotionMaterialInfo = couponInfoPromotionMaterialInfo;
    }

}
