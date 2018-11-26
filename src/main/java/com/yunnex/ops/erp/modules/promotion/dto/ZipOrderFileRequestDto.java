package com.yunnex.ops.erp.modules.promotion.dto;

import java.util.List;

import com.yunnex.ops.erp.modules.workflow.flow.entity.ErpOrderFile;

import yunnex.common.core.dto.BaseDto;

public class ZipOrderFileRequestDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<ErpOrderFile> erpOrderFiles;// 要打包的文件
    private String shopName;// 商户名称
    private String promotionMaterialName;// 推广资料名称

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<ErpOrderFile> getErpOrderFiles() {
        return erpOrderFiles;
    }

    public void setErpOrderFiles(List<ErpOrderFile> erpOrderFiles) {
        this.erpOrderFiles = erpOrderFiles;
    }

    public String getPromotionMaterialName() {
        return promotionMaterialName;
    }

    public void setPromotionMaterialName(String promotionMaterialName) {
        this.promotionMaterialName = promotionMaterialName;
    }

}
