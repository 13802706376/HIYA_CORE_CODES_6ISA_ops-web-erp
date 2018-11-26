package com.yunnex.ops.erp.modules.order.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunnex.ops.erp.common.persistence.DataEntity;

/**
 * 订单Entity
 * 
 * @author huanghaidong
 * @version 2017-10-24
 */
public class ErpOrderOriginalInfo extends DataEntity<ErpOrderOriginalInfo> {

    private static final long serialVersionUID = 1L;
    private String orderNumber; // 订单号
    private String orderVersion; // 订单版本号
    private Date buyDate; // 购买时间
    private Date payDate; // 订单支付时间
    private Date createAt; // 订单创建时间
    private String shopId; // 商户id
    private String shopName; // 商户名称
    private String shopAbbreviation; // 商户简称
    private String shopNumber; // 商户编号
    private String shopExtensionId; // 预留扩展id
    private Long prePrice; // 预计价格（单位：分）
    private Long realPrice; // 实际价格（单位：分）
    private String contactName; // 联系人
    private String source; // 订单来源
    private String contactNumber; // 联系电话
    private Integer pendingNum; // 待处理的商品数量
    private Integer processNum; // 处理中的商品数量
    private Integer finishNum; // 完成的商品数量
    private Integer orderType; // 订单类别
    private Integer goodType; // 服务类型
    private String remark; // 备注
    private Long sort; // 排序字段
    private Date beginBuyDate; // 开始 购买时间
    private Date endBuyDate; // 结束 购买时间
    private String promotePhone; // 推广联系方式
    private String promoteContact;// 推广联系人
    private String salePerson;// 销售人
    private String agentName;// 服务商名称
    private Integer agentId;// 服务商编号
    private Integer cancel; // 是否作废/撤消订单，0：否（默认），1：是
    /**
     * 商户行业类型
     */
    private String industryType;
    /**
     * 订单来源(0：OEM推送(默认) 1：erp添加 2: 易商)
     */
    private Integer orderSource;
    /**
     * 是否是新商户,ERP添加时判断标记(N:默认值,否 Y:是)
     */
    private String isNewShop;

    private Integer orderStatus; // 订单状态 未签约，已签约，已支付，进件中，已进件
    private Integer[] orderStatusValues; // 封装订单状态查询条件
    private Integer[] goodTypeValues; // 服务类型查询条件
    private List<ErpOrderOriginalGood> erpOrderOriginalGoods;// 订单对应购买的商品集合

    /**
     * 查询接收字段(接收订单的拆单数量)
     */
    private Integer splitCount;

    /**
     * 对时间类型进行转换,用于前台回显(购买时间)
     */
    private String tempBuyDate;

    /**
     * 实际支付金额,用于修改时回显
     */
    private String tempRealPrice;

    /**
     * 查询接收字段(接收订单启动商户资料录入的数量)
     */
    private Integer sdiCount;

    /* 运营服务待处理数量 */
    private BigDecimal pendingServiceNum;

    private String auditStatus; // 订单审核状态，0=待初审，1=待复审，2=审核通过，3=审核不通过
    private String auditStatusTxt;// 审核状态中文释义
    private String procInsId; // 流程实例ID
    private Date findBuyStartDate;// 购买开始时间-查询字段
    private Date findBuyEndDate;// 购买结束时间-查询字段
    private String createByName;// 订单创建人-回显字段

    /**
     * 订单类别，First: 首次，Update: 更新
     */
    private String orderCategory;
    /**
     * 易商订单ID，来自易商平台
     */
    private Long ysOrderId;
    /**
     * 易商订单购买时间
     */
    private Date ysOrderBuyTime;
    /**
     * 易商订单实际支付金额，单位分
     */
    private Long ysOrderRealPrice;
    /**
     * 是否是进件审核订单，Y：是，N：否
     */
    private String isAuditOrder;
    /**
     * 升级订单号
     */
    private String  upOrderNumber;
    
    

    public String getUpOrderNumber() {
        return upOrderNumber;
    }

    public void setUpOrderNumber(String upOrderNumber) {
        this.upOrderNumber = upOrderNumber;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ErpOrderOriginalInfo() {
        super();
    }

    public ErpOrderOriginalInfo(String id) {
        super(id);
    }

    @Length(min = 1, max = 50, message = "订单号长度必须介于 1 和 50 之间")
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "购买时间不能为空")
    public Date getBuyDate() {
        return buyDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "订单支付时间不能为空")
    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "订单创建时间不能为空")
    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @NotNull(message = "商户id不能为空")
    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Length(min = 1, max = 50, message = "商户名称长度必须介于 1 和 50 之间")
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Length(min = 1, max = 50, message = "商户简称长度必须介于 1 和 50 之间")
    public String getShopAbbreviation() {
        return shopAbbreviation;
    }

    public void setShopAbbreviation(String shopAbbreviation) {
        this.shopAbbreviation = shopAbbreviation;
    }

    @Length(min = 1, max = 50, message = "商户编号长度必须介于 1 和 50 之间")
    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    @Length(min = 1, max = 50, message = "预留扩展id长度必须介于 1 和 50 之间")
    public String getShopExtensionId() {
        return shopExtensionId;
    }

    public void setShopExtensionId(String shopExtensionId) {
        this.shopExtensionId = shopExtensionId;
    }

    @NotNull(message = "预计价格（单位：分）不能为空")
    public Long getPrePrice() {
        return prePrice;
    }

    public void setPrePrice(Long prePrice) {
        this.prePrice = prePrice;
    }

    @NotNull(message = "实际价格（单位：分）不能为空")
    public Long getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(Long realPrice) {
        this.realPrice = realPrice;
    }

    @Length(min = 1, max = 20, message = "联系人长度必须介于 1 和 20 之间")
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Length(min = 1, max = 20, message = "订单来源长度必须介于 1 和 20 之间")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Length(min = 1, max = 20, message = "联系电话长度必须介于 1 和 20 之间")
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @NotNull(message = "待处理的商品数量不能为空")
    public Integer getPendingNum() {
        return pendingNum;
    }

    public void setPendingNum(Integer pendingNum) {
        this.pendingNum = pendingNum;
    }

    @NotNull(message = "处理中的商品数量不能为空")
    public Integer getProcessNum() {
        return processNum;
    }

    public void setProcessNum(Integer processNum) {
        this.processNum = processNum;
    }

    @NotNull(message = "完成的商品数量不能为空")
    public Integer getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(Integer finishNum) {
        this.finishNum = finishNum;
    }

    @NotNull(message = "订单类别不能为空")
    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getGoodType() {
        return goodType;
    }

    public void setGoodType(Integer goodType) {
        this.goodType = goodType;
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

    public Date getBeginBuyDate() {
        return beginBuyDate;
    }

    public void setBeginBuyDate(Date beginBuyDate) {
        this.beginBuyDate = beginBuyDate;
    }

    public Date getEndBuyDate() {
        return endBuyDate;
    }

    public void setEndBuyDate(Date endBuyDate) {
        this.endBuyDate = endBuyDate;
    }


    public String getPromotePhone() {
        return promotePhone;
    }

    public void setPromotePhone(String promotePhone) {
        this.promotePhone = promotePhone;
    }

    public String getPromoteContact() {
        return promoteContact;
    }

    public void setPromoteContact(String promoteContact) {
        this.promoteContact = promoteContact;
    }

    public String getSalePerson() {
        return salePerson;
    }

    public void setSalePerson(String salePerson) {
        this.salePerson = salePerson;
    }

    public Integer getCancel() {
        return cancel;
    }

    public void setCancel(Integer cancel) {
        this.cancel = cancel;
    }

    public Integer getSplitCount() {
        return splitCount;
    }

    public void setSplitCount(Integer splitCount) {
        this.splitCount = splitCount;
    }

    @Length(min = 1, max = 20, message = "商户类别长度必须介于 1 和 20 之间")
    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public Integer getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(Integer orderSource) {
        this.orderSource = orderSource;
    }

    public String getTempBuyDate() {
        return tempBuyDate;
    }

    public void setTempBuyDate(String tempBuyDate) {
        this.tempBuyDate = tempBuyDate;
    }

    public String getTempRealPrice() {
        return tempRealPrice;
    }

    public void setTempRealPrice(String tempRealPrice) {
        this.tempRealPrice = tempRealPrice;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getIsNewShop() {
        return isNewShop;
    }

    public void setIsNewShop(String isNewShop) {
        this.isNewShop = isNewShop;
    }

    public Integer getSdiCount() {
        return sdiCount;
    }

    public void setSdiCount(Integer sdiCount) {
        this.sdiCount = sdiCount;
    }

    public String getOrderVersion() {
        return orderVersion;
    }

    public void setOrderVersion(String orderVersion) {
        this.orderVersion = orderVersion;
    }


    public BigDecimal getPendingServiceNum() {
        return pendingServiceNum;
    }

    public void setPendingServiceNum(BigDecimal pendingServiceNum) {
        this.pendingServiceNum = pendingServiceNum;
    }

    public Integer[] getOrderStatusValues() {
        return orderStatusValues;
    }

    public void setOrderStatusValues(Integer[] orderStatusValues) {
        this.orderStatusValues = orderStatusValues;
    }

    public Integer[] getGoodTypeValues() {
        return goodTypeValues;
    }

    public void setGoodTypeValues(Integer[] goodTypeValues) {
        this.goodTypeValues = goodTypeValues;
    }

    public List<ErpOrderOriginalGood> getErpOrderOriginalGoods() {
        return erpOrderOriginalGoods;
    }

    public void setErpOrderOriginalGoods(List<ErpOrderOriginalGood> erpOrderOriginalGoods) {
        this.erpOrderOriginalGoods = erpOrderOriginalGoods;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatusTxt() {
        return auditStatusTxt;
    }

    public void setAuditStatusTxt(String auditStatusTxt) {
        this.auditStatusTxt = auditStatusTxt;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getFindBuyStartDate() {
        return findBuyStartDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public void setFindBuyStartDate(Date findBuyStartDate) {
        this.findBuyStartDate = findBuyStartDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getFindBuyEndDate() {
        return findBuyEndDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public void setFindBuyEndDate(Date findBuyEndDate) {
        this.findBuyEndDate = findBuyEndDate;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public Long getYsOrderId() {
        return ysOrderId;
    }

    public void setYsOrderId(Long ysOrderId) {
        this.ysOrderId = ysOrderId;
    }

    public Date getYsOrderBuyTime() {
        return ysOrderBuyTime;
    }

    public void setYsOrderBuyTime(Date ysOrderBuyTime) {
        this.ysOrderBuyTime = ysOrderBuyTime;
    }

    public Long getYsOrderRealPrice() {
        return ysOrderRealPrice;
    }

    public void setYsOrderRealPrice(Long ysOrderRealPrice) {
        this.ysOrderRealPrice = ysOrderRealPrice;
    }

    public String getIsAuditOrder() {
        return isAuditOrder;
    }

    public void setIsAuditOrder(String isAuditOrder) {
        this.isAuditOrder = isAuditOrder;
    }
}
