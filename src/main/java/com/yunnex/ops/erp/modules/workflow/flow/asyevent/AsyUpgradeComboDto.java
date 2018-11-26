package com.yunnex.ops.erp.modules.workflow.flow.asyevent;

import yunnex.common.core.dto.BaseDto;

public class AsyUpgradeComboDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;// 推广资料ID
    private String procInsId;
    private String oldProcInsId;
    private String name;
    private String taskDefKey;
    private String procDefId ;
    private String type ;
    private String actId;
    private String excutionId;
    
    public String getExcutionId() {
        return excutionId;
    }
    public void setExcutionId(String excutionId) {
        this.excutionId = excutionId;
    }
    public String getActId() {
        return actId;
    }
    public void setActId(String actId) {
        this.actId = actId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getProcDefId() {
        return procDefId;
    }
    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getProcInsId() {
        return procInsId;
    }
    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }
    public String getOldProcInsId() {
        return oldProcInsId;
    }
    public void setOldProcInsId(String oldProcInsId) {
        this.oldProcInsId = oldProcInsId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTaskDefKey() {
        return taskDefKey;
    }
    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }
    



}
