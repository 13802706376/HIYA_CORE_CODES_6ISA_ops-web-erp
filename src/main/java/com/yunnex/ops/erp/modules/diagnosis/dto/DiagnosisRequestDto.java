package com.yunnex.ops.erp.modules.diagnosis.dto;

import com.yunnex.ops.erp.modules.diagnosis.entity.DiagnosisForm;

import yunnex.common.core.dto.BaseDto;

public class DiagnosisRequestDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DiagnosisForm diagnosisForm;
    private String diagnosisMaterialType;

    public DiagnosisForm getDiagnosisForm() {
        return diagnosisForm;
    }

    public void setDiagnosisForm(DiagnosisForm diagnosisForm) {
        this.diagnosisForm = diagnosisForm;
    }

    public String getDiagnosisMaterialType() {
        return diagnosisMaterialType;
    }

    public void setDiagnosisMaterialType(String diagnosisMaterialType) {
        this.diagnosisMaterialType = diagnosisMaterialType;
    }
}
