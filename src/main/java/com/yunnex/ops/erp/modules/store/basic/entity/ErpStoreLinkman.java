package com.yunnex.ops.erp.modules.store.basic.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 门店联系人信息Entity
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpStoreLinkman extends DataEntity<ErpStoreLinkman> {

    private static final long serialVersionUID = 1L;
    private String name; // 姓名
    private String phone; // 手机号
    private String email; // 邮箱
    private String address; // 地址
    private String storeInfoId; // 门店ID
    
    public ErpStoreLinkman() {
        super();
    }

    public ErpStoreLinkman(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "姓名长度必须介于 1 和 64 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 1, max = 20, message = "手机号长度必须介于 1 和 20 之间")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Length(min = 1, max = 64, message = "邮箱长度必须介于 1 和 64 之间")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Length(min = 1, max = 64, message = "地址长度必须介于 1 和 64 之间")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Length(min = 0, max = 64, message = "门店ID长度必须介于 0 和 64 之间")
    public String getStoreInfoId() {
        return storeInfoId;
    }

    public void setStoreInfoId(String storeInfoId) {
        this.storeInfoId = storeInfoId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
