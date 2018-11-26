/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yunnex.ops.erp.modules.hat.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 省Entity
 * @author yunnex
 * @version 2018-01-05
 */
public class HatProvince extends DataEntity<HatProvince> {
    
    private static final long serialVersionUID = 1L;
    private String provinceId;        // province_id
    private String province;        // province
    
    public HatProvince() {
        super();
    }

    public HatProvince(String id){
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

    @Length(min=0, max=6, message="province_id长度必须介于 0 和 6 之间")
    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }
    
    @Length(min=0, max=40, message="province长度必须介于 0 和 40 之间")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

}