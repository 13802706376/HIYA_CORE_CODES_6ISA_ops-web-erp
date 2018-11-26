package com.yunnex.ops.erp.modules.promotion.dto;

import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisForm;

import yunnex.common.core.dto.BaseDto;

public class DiagnosisResponseDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DiagnosisForm diagnosisForm; // 大表单对象
    private Boolean modifyFlag;// 是否有修改权限

    public DiagnosisForm getDiagnosisForm() {
        return diagnosisForm;
    }

    public void setDiagnosisForm(DiagnosisForm diagnosisForm) {
        this.diagnosisForm = diagnosisForm;
    }

    public Boolean getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(Boolean modifyFlag) {
        this.modifyFlag = modifyFlag;
    }
}
