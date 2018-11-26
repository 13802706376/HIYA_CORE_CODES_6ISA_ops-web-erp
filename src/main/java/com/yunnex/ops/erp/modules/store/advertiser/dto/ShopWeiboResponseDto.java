package com.yunnex.ops.erp.modules.store.advertiser.dto;

import java.io.Serializable;
import java.util.Objects;

public class ShopWeiboResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String shopId;
    private String shopName;
    private String weiboAccountNo;
    private String weiboUid;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getWeiboAccountNo() {
        return weiboAccountNo;
    }

    public void setWeiboAccountNo(String weiboAccountNo) {
        this.weiboAccountNo = weiboAccountNo;
    }

    public String getWeiboUid() {
        return weiboUid;
    }

    public void setWeiboUid(String weiboUid) {
        this.weiboUid = weiboUid;
    }

    /**
     * 通过weiboAccountNo和weiboUid来确定唯一性
     * 
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ShopWeiboResponseDto that = (ShopWeiboResponseDto) o;
        return Objects.equals(weiboAccountNo, that.weiboAccountNo) && Objects.equals(weiboUid, that.weiboUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weiboAccountNo, weiboUid);
    }
}
