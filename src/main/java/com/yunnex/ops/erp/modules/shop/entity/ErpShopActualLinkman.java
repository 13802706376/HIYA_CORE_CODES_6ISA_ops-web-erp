package com.yunnex.ops.erp.modules.shop.entity;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 商户实际联系人信息Entity
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpShopActualLinkman extends DataEntity<ErpShopActualLinkman> {

    private static final long serialVersionUID = 1L;
    private String name; // 姓名
    private String phoneNo; // 手机号
    private String position; // 职位
    private String shopInfoId; // 商户ID
    private String wechatID;//微信号
    public ErpShopActualLinkman() {
        super();
    }

    public ErpShopActualLinkman(String id) {
        super(id);
    }

    @Length(min = 0, max = 30, message = "姓名长度必须介于 0 和 30 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 20, message = "手机号长度必须介于 0 和 20 之间")
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Length(min = 0, max = 64, message = "职位长度必须介于 0 和 64 之间")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Length(min = 0, max = 64, message = "商户ID长度必须介于 0 和 64 之间")
    public String getShopInfoId() {
        return shopInfoId;
    }

    public void setShopInfoId(String shopInfoId) {
        this.shopInfoId = shopInfoId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String getWechatID() {
        return wechatID;
    }

    public void setWechatID(String wechatID) {
        this.wechatID = wechatID;
    }

}