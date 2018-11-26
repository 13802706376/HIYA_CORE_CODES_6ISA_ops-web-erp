/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.hat.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 区Entity
 * @author yunnex
 * @version 2018-01-05
 */
public class HatArea extends DataEntity<HatArea> {
    
    private static final long serialVersionUID = 1L;
    private String areaId;        // area_id
    private String area;        // area
    private String father;        // father
    
    public HatArea() {
        super();
    }
    
    public HatArea(String id){
        super(id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Length(min=0, max=50, message="area_id长度必须介于 0 和 50 之间")
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
    
    @Length(min=0, max=60, message="area长度必须介于 0 和 60 之间")
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
    
    @Length(min=0, max=6, message="father长度必须介于 0 和 6 之间")
    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

}