package com.yunnex.ops.erp.modules.workflow.store.excel;

import com.yunnex.ops.erp.common.utils.excel.annotation.MapperCell;

public class AdvFriends {

    @MapperCell(cellName = "公众号原始ID", order = 1)
    private String accountOriginalId; // 公众号原始ID
    @FileName("广告主开通截图")
    private String advertiserScreenshot; // 广告主开通截图
    @FileName("门店开通截图")
    private String storeScreenshot; // 门店开通截图
    @FileName("行业资质照片")
    private String industryPhoto; // 行业资质照片

    public AdvFriends() {
        super();
    }

    public AdvFriends(String accountOriginalId, String advertiserScreenshot, String storeScreenshot, String industryPhoto) {
        super();
        this.accountOriginalId = accountOriginalId;
        this.advertiserScreenshot = advertiserScreenshot;
        this.storeScreenshot = storeScreenshot;
        this.industryPhoto = industryPhoto;
    }

    public String getAccountOriginalId() {
        return accountOriginalId;
    }

    public void setAccountOriginalId(String accountOriginalId) {
        this.accountOriginalId = accountOriginalId;
    }

    public String getAdvertiserScreenshot() {
        return advertiserScreenshot;
    }

    public void setAdvertiserScreenshot(String advertiserScreenshot) {
        this.advertiserScreenshot = advertiserScreenshot;
    }

    public String getStoreScreenshot() {
        return storeScreenshot;
    }

    public void setStoreScreenshot(String storeScreenshot) {
        this.storeScreenshot = storeScreenshot;
    }

    public String getIndustryPhoto() {
        return industryPhoto;
    }

    public void setIndustryPhoto(String industryPhoto) {
        this.industryPhoto = industryPhoto;
    }

    @Override
    public String toString() {
        return "AdvFriends [accountOriginalId=" + accountOriginalId + ", advertiserScreenshot=" + advertiserScreenshot + ", storeScreenshot=" + storeScreenshot + ", industryPhoto=" + industryPhoto + "]";
    }

}
