package com.yunnex.ops.erp.modules.store.basic.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.modules.hat.entity.HatArea;
import com.yunnex.ops.erp.modules.hat.entity.HatCity;
import com.yunnex.ops.erp.modules.hat.entity.HatProvince;
import com.yunnex.ops.erp.modules.shop.entity.BusinessCategory;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;

/**
 * 门店基本信息Entity
 * 
 * @author yunnex
 * @version 2017-12-09
 */
public class ErpStoreInfo extends DataEntity<ErpStoreInfo> {

    private static final long serialVersionUID = 1L;
    private Integer isMain = 0; // 是否是掌贝进件主体,0:否,1:是,默认0
    private String shortName; // 门店简称
    private String address; // 门店经营地址
    private String province; // 省编码
    private String city; // 门店所在城市编码
    private String area; // 区编码
    private String provinceName; // 省
    private String cityName; // 门店所在城市
    private String areaName; // 区
    private String telephone; // 门店电话
    private String companyUrl; // 公司网址
    private String productName; // 产品名称
    private String productConcreteInfo; // 投放产品具体信息
    private Integer businessType = 0; // 商户类型，1：个体工商商户，2：企业商户，默认0
    private Integer isOldShopStore = 0; // 是否是根据老商户生成的门店，0：否，1：是
    private Integer auditStatus = 0; // 掌贝进件状态(1:等待审核2:审核通过3:审核未通过4:已下架)
    private String auditContent; // 审核意见
    private String shopInfoId; // 商户ID
    private String legalPersonId; // 法人ID
    private String credentialsId; // 营业资质ID
    private String weixinPayId; // 微信支付ID
    private String unionpayId; // 银联支付ID
    private String advertiserFriendsId; // 朋友圈广告主ID
    private String advertiserWeiboId; // 微博广告主ID
    private String advertiserMomoId; // 陌陌广告主ID
    private String icpMessage; // icp备案信息
    private String notOpenUnionpayFlag; // 商户暂不开通银联标示
	private String shopId;
    private String shopName;
    private String shopAbbreviation; // 商户简称
    private String machineToolNumber;
    private String contentName; // 联系人
    private String contentPhone; // 联系人电话
    private Integer environmentPhotoCount = 0;// 门店环境图数量
    private Integer productPhotoCount = 0;// 产品图数量
    private Integer menuPhotoCount = 0;
    private ErpStorePayWeixin wxPay;
    private ErpStorePayUnionpay unionPay;
    private ErpStoreAdvertiserFriends friend;
    private ErpStoreAdvertiserMomo momo;
    private ErpStoreAdvertiserWeibo weibo;
    private ErpStorePromotePhotoMaterial photoMaterial;
    private ErpStoreLegalPerson person;
    private ErpStoreCredentials credentials;
    private ErpStoreLinkman stroeLinkMan;
    private List<BusinessScope> scope = new ArrayList<BusinessScope>();// 经营范围
    private String payOpenName;// 支付进件名称，临时字段
    private List<BusinessCategory> category = new ArrayList<BusinessCategory>();// 经营类目临时list，显示作用


    // 门店是否被推广0.否 1.是
    private String storeExtension;
    // 朋友圈是否被推广0.否 1.是
    private String friendExtension;
    // 陌陌是否被推广0.否 1.是
    private String momoExtension;
    // 微博 是否被推广0.否 1.是
    private String weiboExtension;

    /**
     * 是否OEM同步(Y:是N:否)
     */
    private String syncOem;

    // 商户经营类目临时字段
    private Integer businesscategory;
    private String businesscategoryname;

    private List<ErpStorePayWeixin> wxPays;
    private List<ErpStorePayUnionpay> unionPays;

    // 前端判断锁定输入框微信银联状态
    private Integer wxStatus = 0;
    private Integer unionStatus = 0;
    private String wxStatusName;
    private String unionStatusName;

    private List<HatProvince> provinces;
    private List<HatCity> cities;
    private List<HatArea> areas;

    // OEM传值用参数
    private String idCard;// 身份证号
    private String businessLicenseCode;// 营业执照号

    public ErpStoreInfo() {
        super();
    }

    /**
     * 业务定义：深度克隆
     * 
     * @date 2018年4月27日
     * @author R/Q
     */
    public ErpStoreInfo copy() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        return (ErpStoreInfo) ois.readObject();
    }

	public String getMachineToolNumber() {
		return machineToolNumber;
	}

	public void setMachineToolNumber(String machineToolNumber) {
		this.machineToolNumber = machineToolNumber;
	}

	public ErpStoreInfo(String id) {
        super(id);
    }

    public Integer getWxStatus() {
        return wxStatus;
    }

    public void setWxStatus(Integer wxStatus) {
        this.wxStatus = wxStatus;
    }

    public Integer getUnionStatus() {
        return unionStatus;
    }

    public void setUnionStatus(Integer unionStatus) {
        this.unionStatus = unionStatus;
    }

    public String getWxStatusName() {
        return wxStatusName;
    }

    public void setWxStatusName(String wxStatusName) {
        this.wxStatusName = wxStatusName;
    }

    public String getUnionStatusName() {
        return unionStatusName;
    }

    public void setUnionStatusName(String unionStatusName) {
        this.unionStatusName = unionStatusName;
    }

    public Integer getMenuPhotoCount() {
        return menuPhotoCount;
    }

    public void setMenuPhotoCount(Integer menuPhotoCount) {
        this.menuPhotoCount = menuPhotoCount;
    }

    public String getStoreExtension() {
        return storeExtension;
    }

    public void setStoreExtension(String storeExtension) {
        this.storeExtension = storeExtension;
    }

    public String getFriendExtension() {
        return friendExtension;
    }

    public void setFriendExtension(String friendExtension) {
        this.friendExtension = friendExtension;
    }

    public String getMomoExtension() {
        return momoExtension;
    }

    public void setMomoExtension(String momoExtension) {
        this.momoExtension = momoExtension;
    }

    public String getWeiboExtension() {
        return weiboExtension;
    }

    public void setWeiboExtension(String weiboExtension) {
        this.weiboExtension = weiboExtension;
    }

    public Integer getBusinesscategory() {
        return businesscategory;
    }

    public void setBusinesscategory(Integer businesscategory) {
        this.businesscategory = businesscategory;
    }

    public String getBusinesscategoryname() {
        return businesscategoryname;
    }

    public void setBusinesscategoryname(String businesscategoryname) {
        this.businesscategoryname = businesscategoryname;
    }

    public List<BusinessCategory> getCategory() {
        return category;
    }

    public void setCategory(List<BusinessCategory> category) {
        this.category = category;
    }

    public String getPayOpenName() {
        return payOpenName;
    }

    public void setPayOpenName(String payOpenName) {
        this.payOpenName = payOpenName;
    }

    public List<BusinessScope> getScope() {
        return scope;
    }

    public void setScope(List<BusinessScope> scope) {
        this.scope = scope;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getIsOldShopStore() {
        return isOldShopStore;
    }

    public void setIsOldShopStore(Integer isOldShopStore) {
        this.isOldShopStore = isOldShopStore;
    }

    public String getAuditContent() {
        return auditContent;
    }

    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }

    public String getIcpMessage() {
        return icpMessage;
    }

    public void setIcpMessage(String icpMessage) {
        this.icpMessage = icpMessage;
    }

    public ErpStoreLinkman getStroeLinkMan() {
        return stroeLinkMan;
    }

    public void setStroeLinkMan(ErpStoreLinkman stroeLinkMan) {
        this.stroeLinkMan = stroeLinkMan;
    }

    public ErpStoreCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(ErpStoreCredentials credentials) {
        this.credentials = credentials;
    }

    public ErpStoreLegalPerson getPerson() {
        return person;
    }

    public void setPerson(ErpStoreLegalPerson person) {
        this.person = person;
    }

    public ErpStorePayWeixin getWxPay() {
        return wxPay;
    }

    public void setWxPay(ErpStorePayWeixin wxPay) {
        this.wxPay = wxPay;
    }

    public ErpStorePayUnionpay getUnionPay() {
        return unionPay;
    }

    public void setUnionPay(ErpStorePayUnionpay unionPay) {
        this.unionPay = unionPay;
    }

    public ErpStoreAdvertiserFriends getFriend() {
        return friend;
    }

    public void setFriend(ErpStoreAdvertiserFriends friend) {
        this.friend = friend;
    }

    public ErpStoreAdvertiserMomo getMomo() {
        return momo;
    }

    public void setMomo(ErpStoreAdvertiserMomo momo) {
        this.momo = momo;
    }

    public ErpStoreAdvertiserWeibo getWeibo() {
        return weibo;
    }

    public void setWeibo(ErpStoreAdvertiserWeibo weibo) {
        this.weibo = weibo;
    }

    public ErpStorePromotePhotoMaterial getPhotoMaterial() {
        return photoMaterial;
    }

    public void setPhotoMaterial(ErpStorePromotePhotoMaterial photoMaterial) {
        this.photoMaterial = photoMaterial;
    }

    public Integer getEnvironmentPhotoCount() {
        return environmentPhotoCount;
    }

    public void setEnvironmentPhotoCount(Integer environmentPhotoCount) {
        this.environmentPhotoCount = environmentPhotoCount;
    }

    public Integer getProductPhotoCount() {
        return productPhotoCount;
    }

    public void setProductPhotoCount(Integer productPhotoCount) {
        this.productPhotoCount = productPhotoCount;
    }

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

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentPhone() {
        return contentPhone;
    }

    public void setContentPhone(String contentPhone) {
        this.contentPhone = contentPhone;
    }

    @NotNull(message = "是否是掌贝进件主体,0:否,1:是,默认0不能为空")
    public Integer getIsMain() {
        return isMain;
    }

    public void setIsMain(Integer isMain) {
        this.isMain = isMain;
    }

    @Length(min = 1, max = 64, message = "门店简称长度必须介于 1 和 64 之间")
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Length(min = 1, max = 64, message = "门店经营地址长度必须介于 1 和 64 之间")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Length(min = 1, max = 64, message = "门店所在城市长度必须介于 1 和 64 之间")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Length(min = 1, max = 20, message = "门店电话长度必须介于 1 和 20 之间")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Length(min = 0, max = 64, message = "公司网址长度必须介于 0 和 64 之间")
    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    @Length(min = 0, max = 128, message = "产品名称长度必须介于 0 和 128 之间")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Length(min = 0, max = 64, message = "投放产品具体信息长度必须介于 0 和 64 之间")
    public String getProductConcreteInfo() {
        return productConcreteInfo;
    }

    public void setProductConcreteInfo(String productConcreteInfo) {
        this.productConcreteInfo = productConcreteInfo;
    }



    @NotNull(message = "商户类型，1：个体工商商户，2：企业商户，默认1不能为空")
    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    @NotNull(message = "审核状态，0：未提交，1：待审核，2：正在审核，3：拒绝，4：通过，默认0不能为空")
    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    @Length(min = 0, max = 64, message = "商户ID长度必须介于 0 和 64 之间")
    public String getShopInfoId() {
        return shopInfoId;
    }

    public void setShopInfoId(String shopInfoId) {
        this.shopInfoId = shopInfoId;
    }

    @Length(min = 0, max = 64, message = "法人ID长度必须介于 0 和 64 之间")
    public String getLegalPersonId() {
        return legalPersonId;
    }

    public void setLegalPersonId(String legalPersonId) {
        this.legalPersonId = legalPersonId;
    }

    @Length(min = 0, max = 64, message = "营业资质ID长度必须介于 0 和 64 之间")
    public String getCredentialsId() {
        return credentialsId;
    }

    public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
    }

    @Length(min = 0, max = 64, message = "微信支付ID长度必须介于 0 和 64 之间")
    public String getWeixinPayId() {
        return weixinPayId;
    }

    public void setWeixinPayId(String weixinPayId) {
        this.weixinPayId = weixinPayId;
    }

    @Length(min = 0, max = 64, message = "银联支付ID长度必须介于 0 和 64 之间")
    public String getUnionpayId() {
        return unionpayId;
    }

    public void setUnionpayId(String unionpayId) {
        this.unionpayId = unionpayId;
    }

    @Length(min = 0, max = 64, message = "朋友圈广告主ID长度必须介于 0 和 64 之间")
    public String getAdvertiserFriendsId() {
        return advertiserFriendsId;
    }

    public void setAdvertiserFriendsId(String advertiserFriendsId) {
        this.advertiserFriendsId = advertiserFriendsId;
    }

    @Length(min = 0, max = 64, message = "微博广告主ID长度必须介于 0 和 64 之间")
    public String getAdvertiserWeiboId() {
        return advertiserWeiboId;
    }

    public void setAdvertiserWeiboId(String advertiserWeiboId) {
        this.advertiserWeiboId = advertiserWeiboId;
    }

    @Length(min = 0, max = 64, message = "陌陌广告主ID长度必须介于 0 和 64 之间")
    public String getAdvertiserMomoId() {
        return advertiserMomoId;
    }

    public void setAdvertiserMomoId(String advertiserMomoId) {
        this.advertiserMomoId = advertiserMomoId;
    }

    public List<ErpStorePayWeixin> getWxPays() {
        return wxPays;
    }

    public void setWxPays(List<ErpStorePayWeixin> wxPays) {
        this.wxPays = wxPays;
    }

    public List<ErpStorePayUnionpay> getUnionPays() {
        return unionPays;
    }

    public void setUnionPays(List<ErpStorePayUnionpay> unionPays) {
        this.unionPays = unionPays;
    }

    public List<HatProvince> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<HatProvince> provinces) {
        this.provinces = provinces;
    }

    public List<HatCity> getCities() {
        return cities;
    }

    public void setCities(List<HatCity> cities) {
        this.cities = cities;
    }

    public List<HatArea> getAreas() {
        return areas;
    }

    public void setAreas(List<HatArea> areas) {
        this.areas = areas;
    }

    public String getSyncOem() {
        return syncOem;
    }

    public void setSyncOem(String syncOem) {
        this.syncOem = syncOem;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBusinessLicenseCode() {
        return businessLicenseCode;
    }

    public void setBusinessLicenseCode(String businessLicenseCode) {
        this.businessLicenseCode = businessLicenseCode;
    }

    public String getShopAbbreviation() {
        return shopAbbreviation;
    }

    public void setShopAbbreviation(String shopAbbreviation) {
        this.shopAbbreviation = shopAbbreviation;
    }
    
    public String getNotOpenUnionpayFlag() {
		return notOpenUnionpayFlag;
	}

	public void setNotOpenUnionpayFlag(String notOpenUnionpayFlag) {
		this.notOpenUnionpayFlag = notOpenUnionpayFlag;
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
