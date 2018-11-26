package com.yunnex.ops.erp.modules.order.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.modules.promotion.entity.ErpOrderCouponReceiveRecord;

/**
 * 卡券输出Entity
 * 
 * @author yunnex
 * @version 2018-05-08
 */
public class ErpOrderCouponOutput extends DataEntity<ErpOrderCouponOutput> {

    private static final long serialVersionUID = 1L;
    private String splitId; // 分单ID
    private String couponName; // 卡券名称
    private String couponLinkCategory; // 卡券链接类型(微信链接weixin_link,手机号链接cellphone_number_link)
    private String couponLink; // 卡券链接
    private String createrName; // 创建者名字
    private String couponLinkCategoryName;
    private Integer writeOffNum;// 卡券核销数量
    private List<ErpOrderCouponReceiveRecord> erpOrderCouponReceiveRecords;// 卡券领取记录集合

    public ErpOrderCouponOutput() {
        super();
    }

    public ErpOrderCouponOutput(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "分单ID长度必须介于 1 和 64 之间")
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    @Length(min = 1, max = 128, message = "卡券名称长度必须介于 1 和 128 之间")
    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    @Length(min = 1, max = 64, message = "卡券链接类型(微信链接weixin_link,手机号链接cellphone_number_link)长度必须介于 1 和 64 之间")
    public String getCouponLinkCategory() {
        return couponLinkCategory;
    }

    public void setCouponLinkCategory(String couponLinkCategory) {
        this.couponLinkCategory = couponLinkCategory;
    }

    @Length(min = 1, max = 128, message = "卡券链接长度必须介于 1 和 128 之间")
    public String getCouponLink() {
        return couponLink;
    }

    public void setCouponLink(String couponLink) {
        this.couponLink = couponLink;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getCouponLinkCategoryName() {
        return couponLinkCategoryName;
    }

    public void setCouponLinkCategoryName(String couponLinkCategoryName) {
        this.couponLinkCategoryName = couponLinkCategoryName;
    }

    public List<ErpOrderCouponReceiveRecord> getErpOrderCouponReceiveRecords() {
        return erpOrderCouponReceiveRecords;
    }

    public void setErpOrderCouponReceiveRecords(List<ErpOrderCouponReceiveRecord> erpOrderCouponReceiveRecords) {
        erpOrderCouponReceiveRecords = erpOrderCouponReceiveRecords;
    }

    public Integer getWriteOffNum() {
        return writeOffNum;
    }

    public void setWriteOffNum(Integer writeOffNum) {
        this.writeOffNum = writeOffNum;
    }
}
