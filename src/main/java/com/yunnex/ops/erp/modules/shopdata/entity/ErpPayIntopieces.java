package com.yunnex.ops.erp.modules.shopdata.entity;

import com.yunnex.ops.erp.common.persistence.ActEntity;

/**
 * 商户支付进件实体
 * 
 * @author SunQ
 * @date 2017年12月20日
 */
public class ErpPayIntopieces extends ActEntity<ErpPayIntopieces> {

    private static final long serialVersionUID = 1126265247208768368L;

    //门店ID
    private String storeId;
    //商户ID
    private String shopId;
    //支付进件类型(0:微信支付1:银联支付)
    private String intopiecesType;
    //支付进件名称
    private String intopiecesName;
    //门店简称
    private String shortName;
    //负责人
    private String chargePerson;
    //备注
    private String remark;
    
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
    public String getStoreId() {
        return storeId;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    public String getShopId() {
        return shopId;
    }
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    public String getIntopiecesType() {
        return intopiecesType;
    }
    public void setIntopiecesType(String intopiecesType) {
        this.intopiecesType = intopiecesType;
    }
    public String getIntopiecesName() {
        return intopiecesName;
    }
    public void setIntopiecesName(String intopiecesName) {
        this.intopiecesName = intopiecesName;
    }
    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public String getChargePerson() {
        return chargePerson;
    }
    public void setChargePerson(String chargePerson) {
        this.chargePerson = chargePerson;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}