package com.yunnex.ops.erp.modules.material.dto;

import java.util.List;

import com.yunnex.ops.erp.common.persistence.Pager;
import com.yunnex.ops.erp.modules.material.entity.ErpOrderMaterialCreation;
import com.yunnex.ops.erp.modules.sys.entity.Dict;
import com.yunnex.ops.erp.modules.sys.entity.User;

import yunnex.common.core.dto.BaseDto;

public class MaterialCreationResponseDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Pager<ErpOrderMaterialCreation> pager;
    private List<Dict> materialCreationStatusList;
    private List<User> operationAdviserList;

    public Pager<ErpOrderMaterialCreation> getPager() {
        return pager;
    }

    public void setPager(Pager<ErpOrderMaterialCreation> pager) {
        this.pager = pager;
    }

    public List<Dict> getMaterialCreationStatusList() {
        return materialCreationStatusList;
    }

    public void setMaterialCreationStatusList(List<Dict> materialCreationStatusList) {
        this.materialCreationStatusList = materialCreationStatusList;
    }

    public List<User> getOperationAdviserList() {
        return operationAdviserList;
    }

    public void setOperationAdviserList(List<User> operationAdviserList) {
        this.operationAdviserList = operationAdviserList;
    }


}
