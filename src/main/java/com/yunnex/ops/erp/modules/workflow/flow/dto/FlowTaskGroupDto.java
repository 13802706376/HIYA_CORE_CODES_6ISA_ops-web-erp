package com.yunnex.ops.erp.modules.workflow.flow.dto;

import java.util.List;

/**
 * 流程任务组信息
 * 
 * @author Ejon
 * @date 2018年5月24日
 */
public class FlowTaskGroupDto extends CompositeFlowInfoTask {

    private String name;
    private String number;
    private String procInsKey;
    private String hurryFlag;
    private String sName;

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getHurryFlag() {
        return hurryFlag;
    }

    public void setHurryFlag(String hurryFlag) {
        this.hurryFlag = hurryFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProcInsKey() {
        return procInsKey;
    }

    public void setProcInsKey(String procInsKey) {
        this.procInsKey = procInsKey;
    }

    public List<FlowInfoTask> getEquipment() {
        return this.equipment;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        result = prime * result + ((procInsKey == null) ? 0 : procInsKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FlowTaskGroupDto other = (FlowTaskGroupDto) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (number == null) {
            if (other.number != null)
                return false;
        } else if (!number.equals(other.number))
            return false;
        if (procInsKey == null) {
            if (other.procInsKey != null)
                return false;
        } else if (!procInsKey.equals(other.procInsKey))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FlowTaskGroupDto [name=" + name + ", number=" + number + ", procInsKey=" + procInsKey + "]";
    }

}
