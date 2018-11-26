package com.yunnex.ops.erp.modules.order.dto;

import java.util.List;

import com.yunnex.ops.erp.modules.order.entity.ErpOrderCouponOutput;
import com.yunnex.ops.erp.modules.sys.entity.Dict;

import yunnex.common.core.dto.BaseDto;

public class CouponOutputResponseDto extends BaseDto {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<ErpOrderCouponOutput> erpOrderCouponOutputs; // 卡券输出信息
    private List<Dict> couponLinkCategoryDics;// 卡券链接类型字典
    private Boolean modifyFlag;// 是否有修改权限

    public List<ErpOrderCouponOutput> getErpOrderCouponOutputs() {
        return erpOrderCouponOutputs;
    }

    public void setErpOrderCouponOutputs(List<ErpOrderCouponOutput> erpOrderCouponOutputs) {
        this.erpOrderCouponOutputs = erpOrderCouponOutputs;
    }

    public List<Dict> getCouponLinkCategoryDics() {
        return couponLinkCategoryDics;
    }

    public void setCouponLinkCategoryDics(List<Dict> couponLinkCategoryDics) {
        this.couponLinkCategoryDics = couponLinkCategoryDics;
    }

    public Boolean getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(Boolean modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

}
