package com.yunnex.ops.erp.modules.shop.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;
import com.yunnex.ops.erp.common.persistence.DataEntity;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;

/**
 * 商户管理Entity
 * 
 * @author huanghaidong
 * @version 2017-10-24
 */
public class ErpShopInfo extends DataEntity<ErpShopInfo> {

    private static final long serialVersionUID = 1L;
    private String number; // 商户编号
    private String name; // 商户名称
    private String abbreviation; // 商户简称
    private String industryType; // 行业类型
    private String address; // 商户地址
    private String cityLevel; // 城市级别
    private String contactEmail; // 联系邮箱
    private String contactName; // 商户联系人
    private String contactPhone; // 联系电话
    private String serviceProvider; // 服务商
    private String serviceProviderPhone; // 服务商联系方式
    private String zhangbeiId; // 掌贝id
    private String password; // 掌贝密码
    private Integer storeCount = 0; // 门店总数
    private String remark; // 备注
    private Long sort; // 排序字段
    private String intoPieces;
    private String fwrole; // 展示用，服务角色
    private String fwname; // 展示用服务人员名
    private String fwtype; // 展示用服务项目
    private Integer storenum;// 门店数量
    private Integer pendingServiceNum;// 运营服务待处理
    private Integer pendingJykNum;// 待处理聚引客数量
    private String loginName; // 登录名
    private Integer agentId; // 服务商编号
    private String operationAdviserId; // 运营顾问id
    private String operationAdviserName; // 运营顾问名字
    // 当前状态，可用：Y, 停用：N, 默认Y，当进件订单状态为取消时为停用
    private String currentStatus;

    // 门店访问权限人临时字段
    private String roleshopId;
    private String roleuserId;
    private String roleuserName;

    // 经营类目和编码
    private Integer businessCategory;
    private String businessCategoryName;


    // 掌贝进件状态(1:等待审核2:审核通过3:审核未通过4:已下架)
    private Integer zhangbeiState;
    // 微信支付进件状态(1:新增待审核2:通过3:拒绝4:已下架5:更新待审核6:待审核7:正在审核)
    private Integer wechatpayState;
    // 银联支付进件状态(1:新增待审核2:通过3:拒绝4:已下架5:更新待审核6:待审核7:正在审核)
    private Integer unionpayState;

    private String machineToolNumber;
    // 掌贝进件备注
    private String zhangbeiRemark;
    // 微信支付进件备注
    private String wechatpayRemark;
    // 银联支付进件备注
    private String unionpayRemark;

    // 商户来源(0:默认值, 1:ERP添加)
    private String source;
    // 关联订单ID
    private String orderId;

    // 商户同步是否异常(Y:是N:否)
    private String isAbnormal;

    @JSONField(serialize = false)
    private ErpStoreInfo erpStoreInfo;
    // 商户对应的主店地址
    private String mainStoreAddress;
    // 支付宝开通状态（open：开通，notOpen:不开通）
    private String alipaState;

    private String keyWord;// 搜索字段

    public String getAlipaState() {
        return alipaState;
    }

    public void setAlipaState(String alipaState) {
        this.alipaState = alipaState;
    }

    public String getMachineToolNumber() {
        return machineToolNumber;
    }

    public void setMachineToolNumber(String machineToolNumber) {
        this.machineToolNumber = machineToolNumber;
    }

    public Integer getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(Integer businessCategory) {
        this.businessCategory = businessCategory;
    }

    public String getBusinessCategoryName() {
        return businessCategoryName;
    }

    public void setBusinessCategoryName(String businessCategoryName) {
        this.businessCategoryName = businessCategoryName;
    }

    public String getRoleshopId() {
        return roleshopId;
    }

    public void setRoleshopId(String roleshopId) {
        this.roleshopId = roleshopId;
    }

    public String getRoleuserId() {
        return roleuserId;
    }

    public void setRoleuserId(String roleuserId) {
        this.roleuserId = roleuserId;
    }

    public String getRoleuserName() {
        return roleuserName;
    }

    public void setRoleuserName(String roleuserName) {
        this.roleuserName = roleuserName;
    }

    public Integer getStorenum() {
        return storenum;
    }

    public void setStorenum(Integer storenum) {
        this.storenum = storenum;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getFwrole() {
        return fwrole;
    }

    public void setFwrole(String fwrole) {
        this.fwrole = fwrole;
    }

    public String getFwname() {
        return fwname;
    }

    public void setFwname(String fwname) {
        this.fwname = fwname;
    }

    public String getFwtype() {
        return fwtype;
    }

    public void setFwtype(String fwtype) {
        this.fwtype = fwtype;
    }

    public String getIntoPieces() {
        return intoPieces;
    }

    public void setIntoPieces(String intoPieces) {
        this.intoPieces = intoPieces;
    }

    public ErpShopInfo() {
        super();
    }

    public ErpShopInfo(String id) {
        super(id);
    }

    @Length(min = 1, max = 50, message = "商户编号长度必须介于 1 和 50 之间")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Length(min = 1, max = 50, message = "商户名称长度必须介于 1 和 50 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 1, max = 30, message = "商户简称长度必须介于 1 和 30 之间")
    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Length(min = 1, max = 50, message = "行业类型长度必须介于 1 和 50 之间")
    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    @Length(min = 1, max = 50, message = "商户地址长度必须介于 1 和 50 之间")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Length(min = 1, max = 50, message = "联系邮箱长度必须介于 1 和 50 之间")
    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Length(min = 1, max = 50, message = "商户联系人长度必须介于 1 和 50 之间")
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Length(min = 1, max = 20, message = "联系电话长度必须介于 1 和 20 之间")
    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @Length(min = 1, max = 30, message = "服务商长度必须介于 1 和 30 之间")
    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Length(min = 1, max = 20, message = "服务商联系方式长度必须介于 1 和 20 之间")
    public String getServiceProviderPhone() {
        return serviceProviderPhone;
    }

    public void setServiceProviderPhone(String serviceProviderPhone) {
        this.serviceProviderPhone = serviceProviderPhone;
    }

    @Length(min = 1, max = 20, message = "掌贝id长度必须介于 1 和 20 之间")
    public String getZhangbeiId() {
        return zhangbeiId;
    }

    public void setZhangbeiId(String zhangbeiId) {
        this.zhangbeiId = zhangbeiId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(Integer storeCount) {
        this.storeCount = storeCount;
    }

    @Length(min = 1, max = 256, message = "备注长度必须介于 1 和 256 之间")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @NotNull(message = "排序字段不能为空")
    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Integer getZhangbeiState() {
        return zhangbeiState;
    }

    public void setZhangbeiState(Integer zhangbeiState) {
        this.zhangbeiState = zhangbeiState;
    }

    public Integer getWechatpayState() {
        return wechatpayState;
    }

    public void setWechatpayState(Integer wechatpayState) {
        this.wechatpayState = wechatpayState;
    }

    public Integer getUnionpayState() {
        return unionpayState;
    }

    public void setUnionpayState(Integer unionpayState) {
        this.unionpayState = unionpayState;
    }

    public String getZhangbeiRemark() {
        return zhangbeiRemark;
    }

    public void setZhangbeiRemark(String zhangbeiRemark) {
        this.zhangbeiRemark = zhangbeiRemark;
    }

    public String getWechatpayRemark() {
        return wechatpayRemark;
    }

    public void setWechatpayRemark(String wechatpayRemark) {
        this.wechatpayRemark = wechatpayRemark;
    }

    public String getUnionpayRemark() {
        return unionpayRemark;
    }

    public void setUnionpayRemark(String unionpayRemark) {
        this.unionpayRemark = unionpayRemark;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ErpStoreInfo getErpStoreInfo() {
        return erpStoreInfo;
    }

    public void setErpStoreInfo(ErpStoreInfo erpStoreInfo) {
        this.erpStoreInfo = erpStoreInfo;
    }

    public String getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(String cityLevel) {
        this.cityLevel = cityLevel;
    }

    public String getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(String isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public String getMainStoreAddress() {
        return mainStoreAddress;
    }

    public void setMainStoreAddress(String mainStoreAddress) {
        this.mainStoreAddress = mainStoreAddress;
    }

    public String getOperationAdviserId() {
        return operationAdviserId;
    }

    public void setOperationAdviserId(String operationAdviserId) {
        this.operationAdviserId = operationAdviserId;
    }

    public String getOperationAdviserName() {
        return operationAdviserName;
    }

    public void setOperationAdviserName(String operationAdviserName) {
        this.operationAdviserName = operationAdviserName;
    }

    public Integer getPendingServiceNum() {
        return pendingServiceNum;
    }

    public void setPendingServiceNum(Integer pendingServiceNum) {
        this.pendingServiceNum = pendingServiceNum;
    }

    public Integer getPendingJykNum() {
        return pendingJykNum;
    }

    public void setPendingJykNum(Integer pendingJykNum) {
        this.pendingJykNum = pendingJykNum;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}
