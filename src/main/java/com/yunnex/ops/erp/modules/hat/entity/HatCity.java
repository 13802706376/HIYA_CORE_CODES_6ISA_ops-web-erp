/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.hat.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 市Entity
 * @author yunnex
 * @version 2018-01-05
 */
public class HatCity extends DataEntity<HatCity> {
    
    private static final long serialVersionUID = 1L;
    private String cityId;        // city_id
    private String city;        // city
    private String father;        // 所属省编码
    
    public HatCity() {
        super();
    }

    public HatCity(String id){
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

    @Length(min=0, max=6, message="city_id长度必须介于 0 和 6 之间")
    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
    
    @Length(min=0, max=50, message="city长度必须介于 0 和 50 之间")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    @Length(min=0, max=6, message="所属省编码长度必须介于 0 和 6 之间")
    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

}