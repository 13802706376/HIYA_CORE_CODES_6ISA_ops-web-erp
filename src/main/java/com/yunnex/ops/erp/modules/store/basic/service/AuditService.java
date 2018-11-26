package com.yunnex.ops.erp.modules.store.basic.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.persistence.BaseEntity;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.HttpUtil;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.order.constant.OrderConstants;
import com.yunnex.ops.erp.modules.order.constant.OrderSplitConstants;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderOriginalInfo;
import com.yunnex.ops.erp.modules.order.entity.ErpOrderSplitInfo;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.order.service.ErpOrderSplitInfoService;
import com.yunnex.ops.erp.modules.qualify.entity.ErpShopPayQualify;
import com.yunnex.ops.erp.modules.qualify.service.ErpShopPayQualifyService;
import com.yunnex.ops.erp.modules.shop.constant.ShopConstant;
import com.yunnex.ops.erp.modules.shop.entity.BusinessCategory;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.BusinessCategoryService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.shopdata.dao.ErpShopDataInputDao;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpPayIntopieces;
import com.yunnex.ops.erp.modules.shopdata.entity.ErpShopDataInput;
import com.yunnex.ops.erp.modules.shopdata.service.ErpPayIntopiecesService;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserFriendsService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserMomoService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLinkman;
import com.yunnex.ops.erp.modules.store.constant.StoreConstant;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayUnionpayService;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayWeixinService;
import com.yunnex.ops.erp.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.yunnex.ops.erp.modules.sys.utils.DictUtils;
import com.yunnex.ops.erp.modules.sys.utils.UserUtils;
import com.yunnex.ops.erp.modules.workflow.delivery.constant.ErpDeliveryServiceConstants;
import com.yunnex.ops.erp.modules.workflow.delivery.dao.ErpDeliveryServiceDao;
import com.yunnex.ops.erp.modules.workflow.delivery.entity.ErpDeliveryService;
import com.yunnex.ops.erp.modules.workflow.delivery.service.ErpDeliveryServiceService;
import com.yunnex.ops.erp.modules.workflow.flow.constant.FlowConstant;
import com.yunnex.ops.erp.modules.workflow.flow.service.FriendsPromotionFlow3P2Service;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowAccountSignalService;
import com.yunnex.ops.erp.modules.workflow.flow.service.MicroblogPromotionFlow3P2Service;
import com.yunnex.ops.erp.modules.workflow.flow.service.SdiFlowSignalService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowMonitorService;
import com.yunnex.ops.erp.modules.workflow.flow.service.WorkFlowService;

@Service
public class AuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    private static final String BEFORE_AUDIT_MSG = "商户提交进件前检查";
    private static final String DATA = "data";
    private static final String RESULTS = "results";
    private static final String ORDER_STATUS = "order_status";
    private static final String ERR_CODE = "errcode";
    private static final Integer SUCCESS_CODE = 200;

    @Value("${api_storeaudit_url}")
    private String API_STOREAUDIT_URL; // NOSONAR
    @Value("${api_order_bind_url}")
    private String API_ORDER_BIND_URL; // NOSONAR
    @Value("${api_wxpayaudit_url}")
    private String API_WXPAYAUDIT_URL; // NOSONAR
    @Value("${api_unionaudit_url}")
    private String API_UNIONAUDIT_URL; // NOSONAR

    @Value("${api_order_info_url}")
    private String apiOrderInfoUrl; // NOSONAR

    @Autowired
    private ErpShopDataInputDao shopDataInputDao;
    @Autowired
    private ErpShopInfoService shopService;
    @Autowired
    private ErpOrderOriginalInfoService orderService;
    @Autowired
    private ErpStoreLinkmanService storeLinkmanService;
    @Autowired
    private SdiFlowSignalService sdiService;
    @Autowired
    private ErpStoreInfoService storeInfoService;
    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpStoreAdvertiserFriendsService friendsService;
    @Autowired
    private JykFlowAccountSignalService jykService;
    @Autowired
    private ErpStoreAdvertiserWeiboService weiboService;
    @Autowired
    private FriendsPromotionFlow3P2Service friendsPromotionFlow3P2Service;
    @Autowired
    private MicroblogPromotionFlow3P2Service microblogPromotionFlow3P2Service;
    @Autowired
    private BusinessCategoryService categoryService;
    @Autowired
    private ErpStorePayWeixinService wxpayService;
    @Autowired
    private ErpPayIntopiecesService intoService;
    @Autowired
    private WorkFlowService flowService;
    @Autowired
    private ErpShopPayQualifyService erpShopPayQualifyService;
    @Autowired
    private ErpStorePayUnionpayService uninpayService;
    @Autowired
    private ErpDeliveryServiceDao erpDeliveryServiceDao;
    @Autowired
    private ErpStoreAdvertiserMomoService momoService;
    @Autowired
    private ErpDeliveryServiceService deliveryServiceService;
    @Autowired
    private ErpOrderSplitInfoService orderSplitInfoService;
    @Autowired
    private WorkFlowMonitorService workFlowMonitorService;

    /**
     * 掌贝进件提交审核
     */
    @Transactional
    public JSONObject zhangbeiaudit(String shopId) {
        LOGGER.info("掌贝进件提交审核, shopId = {}", shopId);
        JSONObject resObject = new JSONObject();
        if (StringUtils.isBlank(shopId)) {
            resObject.put("result", false);
            resObject.put("message", "shopId不能为空！");
            return resObject;
        }
        final ErpShopInfo shop = shopService.get(shopId);
        if (shop == null) {
            resObject.put("result", false);
            resObject.put("message", "商户不存在！");
            return resObject;
        }
        // 检查商户能否提交进件
        JSONObject checkResult = checkAuditOrder(shop.getId());
        if (checkResult == null || !checkResult.getBoolean(CommonConstants.RESULT)) {
            return checkResult;
        }
        final String zhangbeiId = shop.getZhangbeiId();
        if (StringUtils.isBlank(zhangbeiId)) {
            resObject.put("result", false);
            resObject.put("message", "zhangbeiId不能为空！");
            return resObject;
        }
        List<ErpOrderOriginalInfo> order = orderService.getAgentId(zhangbeiId, Global.NO);
        if (null == order || order.isEmpty()) {
            resObject.put("result", false);
            resObject.put("message", "该商户没有订单信息！");
            return resObject;
        }
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("agentId", order.get(0).getAgentId());
        m.put("shopId", shop.getNumber());

        ErpStoreInfo e = storeInfoService.findismain(shopId, Global.NO);
        ErpStoreInfo store = storeInfoService.findzhangbeiaudit(e.getId(), Global.NO);
        if (store != null) {
            m.put("address", store.getAddress());
            m.put("shortName", store.getShortName());
            m.put("shopType", store.getBusinessType());
            m.put("headPhone", store.getTelephone());
            m.put("province", store.getProvince());
            m.put("city", store.getCity());
            m.put("area", store.getArea());
            m.put("headAddress", store.getAddress());
        } else {
            resObject.put("result", false);
            resObject.put("message", "该商户还未提交掌贝主体信息");
            return resObject;
        }
        ErpStoreLinkman linkman = storeLinkmanService.findWhereStoreId(Global.NO, store.getId());
        if (linkman != null) {
            m.put("contactName", linkman.getName());
            m.put("contactPhone", linkman.getPhone());
            m.put("vipEmail", linkman.getEmail());
        } else {
            resObject.put("result", false);
            resObject.put("message", "该商户还未提交联系人信息");
            return resObject;
        }
        if (null != store.getCredentials()) {
            m.put("fullName", store.getCredentials().getRegisterName());
            m.put("businessScope", store.getCredentials().getBusinessScope());
            m.put("businessLicenseCode", store.getCredentials().getRegisterNo());
            m.put("businessLicenseUrl", store.getCredentials().getBusinessLicence());
            m.put("organizationCode", store.getCredentials().getOrganizationCodeCertificateNo());
            m.put("organizationUrl", store.getCredentials().getOrganizationCodeCertificate());
        } else {
            resObject.put("result", false);
            resObject.put("message", "该商户还未提交营业资质");
            return resObject;
        }
        if (null != store.getPerson()) {
            m.put("legalName", store.getPerson().getName());
            m.put("idCard", store.getPerson().getIdCardNo());
            m.put("idCardStartDate", store.getPerson().getIdCardStartDate());
            m.put("idCardEndDate", store.getPerson().getIdCardEndDate());
            m.put("idCardFrontUrl", store.getPerson().getIdCardFrontPhoto());
            m.put("idCardBackUrl", store.getPerson().getIdCardReversePhoto());
        } else {
            resObject.put("result", false);
            resObject.put("message", "该商户还未提交法人信息");
            return resObject;
        }
        m.put("phoneNumber", zhangbeiId);
        String json = JSON.toJSONString(m);

        String str = HttpUtil.sendHttpPostReqToServerByReqbody(API_STOREAUDIT_URL, json, HttpUtil.CONTENT_TYPE_AJ);
        JSONObject object = JSONObject.parseObject(str);

        if (null == object) {
            resObject.put("result", false);
            resObject.put("message", "OEM无数据返回！");
            return resObject;
        }
        String code = object.getString("code");
        if (StringUtils.isBlank(code)) {
            resObject.put("result", false);
            resObject.put("message", "OEM无响应码返回！");
            return resObject;
        }
        // 错误码映射
        String errorMsg = DictUtils.getDictValue(code, StoreConstant.OEM_ERROR_CODE_DICT_TYPE, StringUtils.EMPTY);
        if (StringUtils.isNotBlank(errorMsg)) {
            resObject.put("result", false);
            resObject.put("message", code + "：" + errorMsg);
            return resObject;
        }
        // 必填项校验失败
        if ("100002".equals(code)) {
            resObject.put("result", false);
            resObject.put("message", object.getString("message"));
            return resObject;
        }
        // 其他原因失败
        if ("failure".equals(code)) {
            resObject.put("result", false);
            resObject.put("message", object.getString("reason"));
            return resObject;
        }
        // 成功时获取商户编码
        JSONObject resultObj = object.getJSONObject("result");
        if (resultObj == null || resultObj.get("shopId") == null) {
            resObject.put("result", false);
            resObject.put("message", "OEM无商户编码返回！");
            return resObject;
        }

        shop.setNumber(resultObj.get("shopId").toString());

        shop.setZhangbeiState(1);
        shopService.save(shop);
        store.setAuditStatus(1);
        storeInfoService.save(store);
        sdiService.applyZhangbeiIntopiece(shop.getZhangbeiId());

        // 绑定订单到商户（OEM）
        // 异步处理，跟审核没有原子关系
        final String shopNumber = shop.getNumber();
        new Thread() {
            @Override
            public void run() {
                bindDituiOrder(zhangbeiId, shopNumber);
            }
        }.start();

        resObject.put("result", true);
        resObject.put("message", "提交审核成功");
        return resObject;
    }

    /**
     * 绑定订单到商户（OEM）
     *
     * @param zhangbeiId
     * @param shopNumber
     */
    public void bindDituiOrder(String zhangbeiId, String shopNumber) {
        String msg = "绑定订单到商户（OEM）：";
        LOGGER.info("{}zhangbeiId = {}, orderNumber = {}", msg, zhangbeiId, shopNumber);

        ErpShopDataInput shopDataInput = shopDataInputDao.getByShopId(zhangbeiId);
        ErpDeliveryService shopDataInp=erpDeliveryServiceDao.getDeliveryInfoByShopIdAsc(zhangbeiId);

        String orderNumber="";
        if(shopDataInput!=null){
        	orderNumber=shopDataInput.getOrderNumber();
            LOGGER.info("order 1.0---->！zhangbeiId = {}, orderNumber = {}", zhangbeiId, orderNumber);
        }else{
        	orderNumber=shopDataInp.getOrderNumber();
            LOGGER.info("order 2.0---->！zhangbeiId = {}, orderNumber = {}", zhangbeiId, orderNumber);
        }
        
        if (StringUtils.isBlank(orderNumber)) {
            LOGGER.error("{}该商户还没有订单号！zhangbeiId = {}, shopNumber = {}", msg, zhangbeiId, shopNumber);
            return;
        }

        if (StringUtils.isBlank(shopNumber)) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("该商户还没有序列号！zhangbeiId = {}, shopNumber = {}", msg, zhangbeiId, shopNumber);
            }
            return;
        }

        String url = String.format(API_ORDER_BIND_URL, orderNumber, shopNumber);
        String str = HttpUtil.sendHttpPostReqToServerByReqbody(url, null, null);
        JSONObject object = JSONObject.parseObject(str);
        if (object == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("{}OEM无响应！请求参数：shopNumber = {}, orderNumber = {}", msg, shopNumber, orderNumber);
            }
            return;
        }
        if (object.getBoolean("success")) {
            LOGGER.info("{}请求成功！请求参数：shopNumber = {}, orderNumber = {}", msg, shopNumber, orderNumber);// NOSONAR
        } else {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("{}OEM响应 - 不成功！请求参数：shopNumber = {}, orderNumber = {}, 响应结果：{}", msg, shopNumber, orderNumber,
                                object.getString("reason"));
            }
        }
    }

    /**
     * 业务定义：微信进件提交审核
     */
    @Transactional(readOnly = false)
    public JSONObject wxpayaudit(String shopid, String storeids, String intopiecesName) {// NOSONAR
        LOGGER.info("微信进件提交审核, shopId = {}, storeids = {},intopiecesName={}", shopid, storeids, intopiecesName);
        JSONObject resObject = new JSONObject();
        ErpShopInfo shop = shopService.get(shopid);
        if (shop == null) {
            resObject.put("result", false);
            resObject.put("message", "商户不存在！");
            return resObject;
        }
        // 检查商户能否提交进件
        JSONObject checkResult = checkAuditOrder(shop.getId());
        if (checkResult == null || !checkResult.getBoolean(CommonConstants.RESULT)) {
            return checkResult;
        }
        String[] o = storeids.split(",");
        ErpStoreInfo store = erpStoreInfoService.wxpayaudit(o[0], Global.NO);
        ErpStoreLinkman linkman = storeLinkmanService.findWhereStoreId(Global.NO, store.getId());
        if (null == linkman) {
            resObject.put("result", false);
            resObject.put("message", "门店联系人信息未填写");
            return resObject;
        }
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("infoId", store.getWxPay().getNumber());
        m.put("shopId", shop.getNumber());
        m.put("principal", linkman.getName());
        m.put("principalPhone", linkman.getPhone());
        m.put("vipEmail", linkman.getEmail());
        if (store.getBusinessType() == 1) {
            m.put("applyType", 5);
        } else {
            m.put("applyType", 8);
        }
        m.put("shopShortName", store.getShortName());
        if (null == shop.getBusinessCategory()) {
            resObject.put("result", false);
            resObject.put("message", "商户经营类目未填写");
            return resObject;
        }
        BusinessCategory category = categoryService.whereCategoryId(shop.getBusinessCategory().toString());
        if (null == category) {
            resObject.put("result", false);
            resObject.put("message", "商户经营类目不正确，请修改");
            return resObject;
        }
        m.put("businessCategory", category.getFather() + "," + shop.getBusinessCategory().toString());
        m.put("serviceFrontPhone", store.getTelephone());
        m.put("shopName", store.getCredentials().getRegisterName());
        if (null != store.getCredentials()) {
            m.put("address", store.getCredentials().getRegisterAddress());
            m.put("businessCode", store.getCredentials().getRegisterNo());
            m.put("businessScope", store.getCredentials().getBusinessScope());
            m.put("businessCodeStartDate", store.getCredentials().getStartDate());
            m.put("businessCodeEndDate", store.getCredentials().getEndDate());
            m.put("businessUrl", store.getCredentials().getBusinessLicence());
            m.put("organizationCode", store.getCredentials().getOrganizationCodeCertificateNo());
            m.put("organizationStartDate", store.getCredentials().getStartDate());
            if (store.getCredentials().getIsLongTime() == 0) {
                m.put("organizationEndDate", store.getCredentials().getEndDate());
            }
            m.put("organizationUrl", store.getCredentials().getOrganizationCodeCertificate());
            m.put("remarksUrl", store.getCredentials().getSpecialCertificate());
        } else {
            resObject.put("result", false);
            resObject.put("message", "商户还未提交营业资质");
            return resObject;
        }
        m.put("certificateHolderType", 2);// 证件持有1业务经办人；2法人
        m.put("cerficateType", 1);// 1身份证；2护照
        if (null != store.getPerson()) {
            m.put("cerficateHolderName", store.getPerson().getName());
            m.put("cerficateStartDate", store.getPerson().getIdCardStartDate());
            m.put("cerficateEndDate", store.getPerson().getIdCardEndDate());
            m.put("cerficateCode", store.getPerson().getIdCardNo());
            m.put("idCardFrontUrl", store.getPerson().getIdCardFrontPhoto());
            m.put("idCardBackUrl", store.getPerson().getIdCardReversePhoto());
        } else {
            resObject.put("result", false);
            resObject.put("message", "商户还未提交法人信息");
            return resObject;
        }
        m.put("accountType", store.getWxPay().getBank().getAccountType());
        m.put("openName", store.getWxPay().getBank().getOpenAccountName());
        m.put("openBank", store.getWxPay().getBank().getBankId().toString());
        m.put("province", store.getWxPay().getBank().getProvince());
        m.put("city", store.getWxPay().getBank().getCity());
        m.put("detailAddress", store.getWxPay().getBank().getBranchBankName());
        m.put("bankAccount", store.getWxPay().getBank().getCreditCardNo());
        m.put("appId", store.getWxPay().getPublicAccountAppid());
        m.put("accountApplyFormUrl", store.getWxPay().getMultiAccountApplicationForm());
        String json = JSON.toJSONString(m);
        String str = HttpUtil.sendHttpPostReqToServerByReqbody(API_WXPAYAUDIT_URL, json, HttpUtil.CONTENT_TYPE_AJ);
        JSONObject object = JSONObject.parseObject(str);
        if (null == object) {
            resObject.put("result", false);
            resObject.put("message", "OEM无数据返回！");
            return resObject;
        }
        String code = object.getString("code");
        if (StringUtils.isBlank(code)) {
            resObject.put("result", false);
            resObject.put("message", "OEM无响应码返回！");
            return resObject;
        }
        // 错误码映射
        String errorMsg = DictUtils.getDictValue(code, StoreConstant.OEM_ERROR_CODE_DICT_TYPE, StringUtils.EMPTY);
        if (StringUtils.isNotBlank(errorMsg)) {
            resObject.put("result", false);
            resObject.put("message", code + "：" + errorMsg);
            return resObject;
        }
        // 必填项校验失败
        if ("100002".equals(code)) {
            resObject.put("result", false);
            resObject.put("message", object.getString("message"));
            return resObject;
        }
        // 其他原因失败
        if ("failure".equals(code)) {
            resObject.put("result", false);
            resObject.put("message", object.getString("reason"));
            return resObject;
        }
        // 成功时获取微信进件编码
        JSONObject resultObj = object.getJSONObject("result");
        if (resultObj == null || resultObj.get("infoId") == null) {
            resObject.put("result", false);
            resObject.put("message", "OEM无微信进件编码返回！");
            return resObject;
        }
        String intoId = "";
        Principal principal = UserUtils.getPrincipal();
        for (int i = 0; i < o.length; i++) {
            ErpStoreInfo s = erpStoreInfoService.get(o[i]);
            ErpStorePayWeixin wxpay = wxpayService.get(s.getWeixinPayId());
            wxpay.setAuditStatus(6);
            JSONObject results = object.getJSONObject("result");
            wxpay.setNumber(results.get("infoId").toString());
            wxpayService.save(wxpay);
            shop.setWechatpayState(6);
            shopService.save(shop);
            if (wxpay.getAuditStatus() != 0) {
                resObject.put("result", true);
                resObject.put("message", "提交审核成功");
                return resObject;
            }
            ErpPayIntopieces into = new ErpPayIntopieces();
            into.setStoreId(o[i]);
            into.setShopId(shopid);
            into.setIntopiecesType("0");
            into.setIntopiecesName(intopiecesName);
            into.setShortName(s.getShortName());
            into.setChargePerson(principal.getId());
            into.setRemark("");
            intoService.save(into);
            intoId = into.getId();
        }
        flowService.startPayIntopiecesWorkFlow(principal.getId(), intoId, "0");
        sdiService.applyWechatPayIntopiece(shop.getZhangbeiId());
        if (erpShopPayQualifyService.countByShopIdAndPaytype(shop.getId(), "2") == 0) {
            ErpShopPayQualify payQualify = new ErpShopPayQualify();
            payQualify.setShopId(shop.getId());
            payQualify.setPayValue("2");
            this.erpShopPayQualifyService.save(payQualify);
        }
        resObject.put("result", true);
        resObject.put("message", "提交审核成功");
        return resObject;
    }

    /**
     * 银联支付进件提交审核
     */
    @Transactional(readOnly = false)
    public JSONObject unionaudit(String shopid, String storeids, String intopiecesName) {// NOSONAR
        LOGGER.info("银联支付进件提交审核, shopId = {}, storeids = {},intopiecesName={}", shopid, storeids, intopiecesName);
        JSONObject resObject = new JSONObject();
        ErpShopInfo shop = shopService.get(shopid);
        if (shop == null) {
            resObject.put("result", false);
            resObject.put("message", "商户不存在！");
            return resObject;
        }
        // 检查商户能否提交进件
        JSONObject checkResult = checkAuditOrder(shop.getId());
        if (checkResult == null || !checkResult.getBoolean(CommonConstants.RESULT)) {
            return checkResult;
        }
        String[] o = storeids.split(",");
        ErpStoreInfo store = erpStoreInfoService.unionaudit(o[0], Global.NO);
        ErpStoreLinkman linkman = storeLinkmanService.findWhereStoreId(Global.NO, store.getId());
        if (null == linkman) {
            resObject.put("result", false);
            resObject.put("message", "门店联系人信息未填写");
            return resObject;
        }
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("shopId", shop.getNumber());
        m.put("infoId", store.getUnionPay().getNumber());
        m.put("contactName", linkman.getName());
        m.put("contactPhone", linkman.getPhone());
        m.put("contactEmail", linkman.getEmail());
        m.put("shopProvince", store.getProvince());
        m.put("shopCity", store.getCity());
        m.put("shopArea", store.getArea());
        if (store.getBusinessType() == 1) {
            m.put("shopType", 5);
        } else {
            m.put("shopType", 8);
        }
        m.put("shopShortName", store.getShortName());
        if (null == shop.getBusinessCategory()) {
            resObject.put("result", false);
            resObject.put("message", "商户经营类目未填写");
            return resObject;
        }
        BusinessCategory category = categoryService.whereCategoryId(shop.getBusinessCategory().toString());
        if (null == category) {
            resObject.put("result", false);
            resObject.put("message", "商户经营类目不正确，请修改");
            return resObject;
        }
        m.put("businessCategory", category.getFather() + "," + shop.getBusinessCategory().toString());
        m.put("serviceFrontPhone", store.getTelephone());
        m.put("shopName", store.getCredentials().getRegisterName());
        if (null != store.getCredentials()) {
            m.put("businessLicenseFullname", store.getCredentials().getRegisterName());
            m.put("businessLicenseAddress", store.getCredentials().getRegisterAddress());
            m.put("businessLicenseCode", store.getCredentials().getRegisterNo());
            m.put("businessScope", store.getCredentials().getBusinessScope());
            m.put("businessCodeStartDate", store.getCredentials().getStartDate());
            m.put("businessCodeEndDate", store.getCredentials().getEndDate());
            m.put("businessUrl", store.getCredentials().getBusinessLicence());
            m.put("organizationCode", store.getCredentials().getOrganizationCodeCertificateNo());
            m.put("organizationStartDate", store.getCredentials().getStartDate());
            if (store.getCredentials().getIsLongTime() == 0) {
                m.put("organizationEndDate", store.getCredentials().getEndDate());
            }
            m.put("organizationUrl", store.getCredentials().getOrganizationCodeCertificate());
        } else {
            resObject.put("result", false);
            resObject.put("message", "商户还未提交营业资质");
            return resObject;
        }
        m.put("certificateHolderType", 2);// 证件持有1业务经办人；2法人
        m.put("cerficateType", 1);// 1身份证；2护照
        if (null != store.getPerson()) {
            m.put("legalName", store.getPerson().getName());
            m.put("idCardStartDate", store.getPerson().getIdCardStartDate());
            m.put("idCardEndDate", store.getPerson().getIdCardEndDate());
            m.put("idCard", store.getPerson().getIdCardNo());
            m.put("idCardFaceUrl", store.getPerson().getIdCardFrontPhoto());
            m.put("idCardBackUrl", store.getPerson().getIdCardReversePhoto());
            m.put("idCardHandUrl", store.getPerson().getIdCardInHandPhoto());
        } else {
            resObject.put("result", false);
            resObject.put("message", "商户还未提交法人信息");
            return resObject;
        }
        m.put("accountType", store.getUnionPay().getBank().getAccountType());
        m.put("openName", store.getUnionPay().getBank().getOpenAccountName());
        m.put("bankUnionNumber", store.getUnionPay().getBank().getBankNo());
        m.put("bankCardFaceUrl", store.getUnionPay().getBank().getCreditCardFrontPhoto());
        m.put("bankCardBackUrl", store.getUnionPay().getBank().getCreditCardReversePhoto());
        m.put("openBank", store.getUnionPay().getBank().getBankId().toString());
        m.put("bankProvince", store.getUnionPay().getBank().getProvince());
        m.put("bankCity", store.getUnionPay().getBank().getCity());
        m.put("bankArea", store.getUnionPay().getBank().getArea());
        m.put("bankDetailAddress", store.getUnionPay().getBank().getBranchBankName());
        m.put("bankAccount", store.getUnionPay().getBank().getCreditCardNo());
        m.put("deviceNum", store.getUnionPay().getBank().getZhangbeiBindCount());
        m.put("licenseUnionUrl", store.getUnionPay().getBank().getOpenAccountLicence());
        m.put("settlementAuthorizationUrl", store.getUnionPay().getBank().getAuthorizeProxy());
        m.put("shopOutsideUrl", store.getUnionPay().getStorePhotoDoorHead());
        m.put("shopInsideUrl", store.getUnionPay().getStorePhotoCashierDesk());
        m.put("shopCashierUrl", store.getUnionPay().getStorePhotoEnvironment());
        m.put("accountApplyFormUrl", store.getUnionPay().getMultiAccountApplicationForm());
        m.put("remarksUrl", store.getUnionPay().getLianDan() + ";" + store.getUnionPay().getAdditionalPhoto());
        String json = JSON.toJSONString(m);
        String str = HttpUtil.sendHttpPostReqToServerByReqbody(API_UNIONAUDIT_URL, json, HttpUtil.CONTENT_TYPE_AJ);
        JSONObject object = JSONObject.parseObject(str);
        if (null == object) {
            resObject.put("result", false);
            resObject.put("message", "OEM无数据返回！");
            return resObject;
        }
        String code = object.getString("code");
        if (StringUtils.isBlank(code)) {
            resObject.put("result", false);
            resObject.put("message", "OEM无响应码返回！");
            return resObject;
        }
        // 错误码映射
        String errorMsg = DictUtils.getDictValue(code, StoreConstant.OEM_ERROR_CODE_DICT_TYPE, StringUtils.EMPTY);
        if (StringUtils.isNotBlank(errorMsg)) {
            resObject.put("result", false);
            resObject.put("message", code + "：" + errorMsg);
            return resObject;
        }
        // 必填项校验失败
        if ("100002".equals(code)) {
            resObject.put("result", false);
            resObject.put("message", object.getString("message"));
            return resObject;
        }
        // 其他原因失败
        if ("failure".equals(code)) {
            resObject.put("result", false);
            resObject.put("message", object.getString("reason"));
            return resObject;
        }
        // 成功时获取银联支付进件编码
        JSONObject resultObj = object.getJSONObject("result");
        if (resultObj == null || resultObj.get("infoId") == null) {
            resObject.put("result", false);
            resObject.put("message", "OEM无银联支付进件编码返回！");
            return resObject;
        }
        Principal principal = UserUtils.getPrincipal();
        String intoId = "";
        for (int i = 0; i < o.length; i++) {
            ErpStoreInfo s = erpStoreInfoService.get(o[i]);
            ErpStorePayUnionpay union = uninpayService.get(s.getUnionpayId());
            union.setAuditStatus(6);
            JSONObject results = object.getJSONObject("result");
            union.setNumber(results.get("infoId").toString());
            uninpayService.save(union);
            shop.setUnionpayState(6);;
            shopService.save(shop);
            if (union.getAuditStatus() != 0) {
                resObject.put("result", true);
                resObject.put("message", "提交审核成功");
                return resObject;
            }
            ErpPayIntopieces into = new ErpPayIntopieces();
            into.setStoreId(o[i]);
            into.setShopId(shopid);
            into.setIntopiecesType("1");
            into.setIntopiecesName(intopiecesName);
            into.setShortName(s.getShortName());
            into.setChargePerson(principal.getId());
            into.setRemark("");
            intoService.save(into);
            intoId = into.getId();
        }
        flowService.startPayIntopiecesWorkFlow(principal.getId(), intoId, "1");
        sdiService.applyUnionPayIntopiece(shop.getZhangbeiId());
        if (erpShopPayQualifyService.countByShopIdAndPaytype(shop.getId(), "3") == 0) {
            ErpShopPayQualify payQualify = new ErpShopPayQualify();
            payQualify.setShopId(shop.getId());
            payQualify.setPayValue("3");
            this.erpShopPayQualifyService.save(payQualify);
        }
        resObject.put("result", true);
        resObject.put("message", "提交审核成功");
        return resObject;
    }

    /**
     * 
     * 业务定义：朋友圈广告主提审
     * 
     * @date 2018年5月18日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public JSONObject friendSubmitAudit(String storeId) {
        JSONObject resObject = new JSONObject();
        ErpStoreInfo store = erpStoreInfoService.friendaudit(storeId);
        if (null == store) {
            resObject.put("result", false);
            resObject.put("message", "门店信息异常");
            return resObject;
        }
        // 检查商户能否提交进件
        JSONObject checkResult = checkAuditOrder(store.getShopInfoId());
        if (checkResult == null || !checkResult.getBoolean(CommonConstants.RESULT)) {
            return checkResult;
        }
        if (StringUtils.isBlank(store.getAdvertiserFriendsId())) {
            resObject.put("result", false);
            resObject.put("message", "门店还未填写朋友圈推广资料");
            return resObject;
        }
        if (StringUtils.isBlank(store.getFriend().getAccountOriginalId())) {
            resObject.put("result", false);
            resObject.put("message", "公众号原始ID未填写");
            return resObject;
        }
        if (StringUtils.isBlank(store.getFriend().getAccountNo())) {
            resObject.put("result", false);
            resObject.put("message", "公众号登录账户未填写");
            return resObject;
        }
        if (StringUtils.isBlank(store.getFriend().getAccountPassword())) {
            resObject.put("result", false);
            resObject.put("message", "公众号登录密码未填写");
            return resObject;
        }
        if (StringUtils.isBlank(store.getFriend().getAdvertiserScreenshot())) {
            resObject.put("result", false);
            resObject.put("message", "广告主开通截图未上传");
            return resObject;
        }
        if (StringUtils.isBlank(store.getFriend().getStoreScreenshot())) {
            resObject.put("result", false);
            resObject.put("message", "门店开通截图未上传");
            return resObject;
        }
        Integer recordStatus = friendsService.queryRecordDataAuditStatus(store);
        ErpStoreInfo e = erpStoreInfoService.get(storeId);
        ErpStoreAdvertiserFriends fd = friendsService.get(e.getAdvertiserFriendsId());
        jykService.perfectFriendsPromote(storeId);// 完成聚引客流程节点
        if (recordStatus != null) {// 如果同商户有同推广账号，则同步审核状态
            fd.setAuditStatus(recordStatus);
            friendsService.save(fd);
            resObject.put("result", true);
            resObject.put("message", "已同步审核状态");
        } else {
            fd.setAuditStatus(1);
            friendsService.save(fd);
            if (!StringUtils.equals(e.getFriendExtension(), Global.YES)) {// 如果该门店没有微信广告主推广历史，启动广告主提审流程。用于避开聚引客流程中的广告主提审节点
                friendsPromotionFlow3P2Service.startMicroblogPromotionFlow(e.getId(), fd.getId());// 启动朋友圈广告主推广开户流程
            }
            resObject.put("result", true);
            resObject.put("message", "提交审核成功");
        }
        return resObject;
    }

    /**
     * 
     * 业务定义：微博广告主提审
     * 
     * @date 2018年5月18日
     * @author R/Q
     */
    @Transactional(readOnly = false)
    public JSONObject weiboSubmitAudit(String storeId) {// NOSONAR
        JSONObject resObject = new JSONObject();
        ErpStoreInfo store = erpStoreInfoService.weiboaudit(storeId);
        if (null == store) {
            resObject.put("result", false);
            resObject.put("message", "门店信息异常");
            return resObject;
        }
        // 检查商户能否提交进件
        JSONObject checkResult = checkAuditOrder(store.getShopInfoId());
        if (checkResult == null || !checkResult.getBoolean(CommonConstants.RESULT)) {
            return checkResult;
        }
        if (StringUtils.isBlank(store.getAdvertiserWeiboId())) {
            resObject.put("result", false);
            resObject.put("message", "门店还未填写微博推广资料");
            return resObject;
        }
        if (StringUtils.isBlank(store.getWeibo().getAccountNo())) {
            resObject.put("result", false);
            resObject.put("message", "微博登录账号未填写");
            return resObject;
        }
        if (StringUtils.isBlank(store.getWeibo().getAccountPassword())) {
            resObject.put("result", false);
            resObject.put("message", "微博登录密码未填写");
            return resObject;
        }
        if (StringUtils.isBlank(store.getWeibo().getNickName())) {
            resObject.put("result", false);
            resObject.put("message", "微博昵称未填写");
            return resObject;
        }
        if (StringUtils.isBlank(store.getWeibo().getUid())) {
            resObject.put("result", false);
            resObject.put("message", "微博UID未填写");
            return resObject;
        }
        if (StringUtils.isBlank(store.getCredentialsId())) {
            resObject.put("result", false);
            resObject.put("message", "门店未填写营业资质");
            return resObject;
        }
        if (StringUtils.isBlank(store.getCredentials().getRegisterName())) {
            resObject.put("result", false);
            resObject.put("message", "营业执照注册名称未填写");
            return resObject;
        }
        if (StringUtils.isBlank(store.getCredentials().getRegisterNo())) {
            resObject.put("result", false);
            resObject.put("message", "营业执照注册号未填写");
            return resObject;
        }

        if (null == store.getCredentials().getStartDate()) {
            resObject.put("result", false);
            resObject.put("message", "营业执照有效开始日期未填写");
            return resObject;
        }
        if (null == store.getCredentials().getBusinessScope()) {
            resObject.put("result", false);
            resObject.put("message", "经营范围未填写");
            return resObject;
        }
        ErpStoreLinkman linkman = storeLinkmanService.findWhereStoreId(Global.NO, store.getId());
        if (null == linkman) {
            resObject.put("result", false);
            resObject.put("message", "门店联系人信息未填写");
            return resObject;
        }
        if (StringUtils.isBlank(linkman.getName())) {
            resObject.put("result", false);
            resObject.put("message", "联系人姓名未填写");
            return resObject;
        }
        if (StringUtils.isBlank(linkman.getPhone())) {
            resObject.put("result", false);
            resObject.put("message", "联系人手机号未填写");
            return resObject;
        }
        if (StringUtils.isBlank(linkman.getEmail())) {
            resObject.put("result", false);
            resObject.put("message", "联系人邮箱未填写");
            return resObject;
        }
        if (StringUtils.isBlank(linkman.getAddress())) {
            resObject.put("result", false);
            resObject.put("message", "联系人通讯地址未填写");
            return resObject;
        }
        Integer recordStatus = weiboService.queryRecordDataAuditStatus(store);
        ErpStoreInfo e = erpStoreInfoService.get(storeId);
        ErpStoreAdvertiserWeibo bo = weiboService.get(e.getAdvertiserWeiboId());
        jykService.perfectMicroblogPromote(storeId);// 完成聚引客流程节点
        if (recordStatus != null) {// 如果同商户有同推广账号，则同步审核状态
            bo.setAuditStatus(recordStatus);
            weiboService.save(bo);
            resObject.put("result", true);
            resObject.put("message", "已同步审核状态");
        } else {
            bo.setAuditStatus(1);
            weiboService.save(bo);
            if (!StringUtils.equals(e.getWeiboExtension(), Global.YES)) {// 如果该门店没有微博广告主推广历史，启动广告主提审流程。用于避开聚引客流程中的广告主提审节点
                microblogPromotionFlow3P2Service.startMicroblogPromotionFlow(e.getId(), bo.getId());
            }
            resObject.put("result", true);
            resObject.put("message", "提交审核成功");
        }
        return resObject;
    }

    /**
     * 陌陌提交审核
     */
    @Transactional
    public JSONObject momoCheck(String storeIds) {
        JSONObject resObject = new JSONObject();
        String[] storeId = storeIds.split(Constant.COMMA);
        ErpStoreInfo store = erpStoreInfoService.momoaudit(storeId[0]);
        if (null == store) {
            resObject.put("result", false);
            resObject.put("message", "门店信息异常");
            return resObject;
        }
        // 检查商户能否提交进件
        JSONObject checkResult = checkAuditOrder(store.getShopInfoId());
        if (checkResult == null || !checkResult.getBoolean(CommonConstants.RESULT)) {
            return checkResult;
        }
        if (StringUtils.isBlank(store.getShortName())) {
            resObject.put("result", false);
            resObject.put("message", "门店简称未填写");
            return resObject;
        }
        if (StringUtils.isBlank(store.getAdvertiserMomoId())) {
            resObject.put("result", false);
            resObject.put("message", "门店还未填写陌陌推广资料");
            return resObject;
        }
        if (StringUtils.isBlank(store.getProductName())) {
            resObject.put("result", false);
            resObject.put("message", "门店还未填写产品名称");
            return resObject;
        }
        if (StringUtils.isBlank(store.getProductConcreteInfo())) {
            resObject.put("result", false);
            resObject.put("message", "门店还未填写投放产品具体信息");
            return resObject;
        }
        if (StringUtils.isBlank(store.getMomo().getBrandName())) {
            resObject.put("result", false);
            resObject.put("message", "品牌名称未填写");
            return resObject;
        }
        ErpStoreLinkman linkman = storeLinkmanService.findWhereStoreId(Global.NO, store.getId());
        if (null == linkman) {
            resObject.put("result", false);
            resObject.put("message", "门店联系人信息未填写");
            return resObject;
        }
        if (StringUtils.isBlank(linkman.getName())) {
            resObject.put("result", false);
            resObject.put("message", "门店联系人姓名未填写");
            return resObject;
        }
        if (StringUtils.isBlank(linkman.getPhone())) {
            resObject.put("result", false);
            resObject.put("message", "门店联系人电话未填写");
            return resObject;
        }

        if (StringUtils.isBlank(store.getIcpMessage())) {
            resObject.put("result", false);
            resObject.put("message", "ICP备案截图未上传");
            return resObject;
        }
        if (StringUtils.isBlank(store.getMomo().getFollowZhangbeiScreenshot())) {
            resObject.put("result", false);
            resObject.put("message", "关注掌贝陌陌号的截图未上传");
            return resObject;
        }

        ErpStoreInfo e;
        ErpStoreAdvertiserMomo mo;
        for (int i = 0; i < storeId.length; i++) {
            e = erpStoreInfoService.get(storeId[i]);
            mo = momoService.get(e.getAdvertiserMomoId());
            mo.setAuditStatus(Constant.ONE);
            momoService.save(mo);
            jykService.perfectMomoPromote(storeId[i]);
        }
        resObject.put("result", true);
        resObject.put("message", "提交审核成功");
        return resObject;
    }

    /**
     * 若商户对应的进件订单存在，先从贝虎系统获取最新的进件订单状态，当进件订单的状态为“已取消”（地推订单的状态为”已取消“时，
     * 对应接口的order_status=-1）时，则弹窗提示“【商户名称】已取消订单，无法完成进件”，同时，将商户的”当前状态“改为”停用“，
     * 并结束进件订单关联的所有流程，订单状态为”取消““，记录对应流程的服务异常原因为”订单已取消“；
     * 
     * @param shopInfoId
     * @return
     */
    @Transactional
    public JSONObject checkAuditOrder(String shopInfoId) {
        String msg = CommonConstants.SYSTEM_ERROR_MESSAGE;
        JSONObject result = new JSONObject();
        result.put(CommonConstants.RESULT, false);
        result.put(CommonConstants.MESSSAGE, msg);

        LOGGER.info("{}商户的进件订单状态, shopInfoId={}", BEFORE_AUDIT_MSG, shopInfoId);
        ErpShopInfo erpShopInfo = shopService.get(shopInfoId);
        if (erpShopInfo == null || BaseEntity.DEL_FLAG_DELETE.equals(erpShopInfo.getDelFlag())) {
            msg = "商户不存在或已被删除！";
            result.put(CommonConstants.MESSSAGE, msg);
            LOGGER.error("{}, {}shopInfoId={}", BEFORE_AUDIT_MSG, msg, shopInfoId);
            return result;
        }

        String zhangbeiId = erpShopInfo.getZhangbeiId();
        if (!erpShopInfo.getCurrentStatus().equals(ShopConstant.whether.YES)) {
            msg = erpShopInfo.getName() + " 已取消订单，无法完成进件";
            result.put(CommonConstants.MESSSAGE, msg);
            LOGGER.error("{}, {}！商户的进件订单状态为停用！掌贝ID：{}", BEFORE_AUDIT_MSG, msg, zhangbeiId);
            return result;
        }

        // 贝蚁v2.1之前的商户没有进件订单. wrote at: 2018-9-4
        ErpOrderOriginalInfo auditOrder = orderService.findAuditOrder(zhangbeiId);
        if (auditOrder == null) {
            msg = "商户没有进件订单!可以提交进件!";
            result.put(CommonConstants.RESULT, true);
            result.put(CommonConstants.MESSSAGE, msg);
            LOGGER.info("{}, {}zhangbeiId={}", BEFORE_AUDIT_MSG, msg, zhangbeiId);
            return result;
        }

        String orderNumber = auditOrder.getOrderNumber();
        // 从贝虎获取订单信息
        String resStr = getOrderInfo(orderNumber);

        LOGGER.info("{}, 贝虎响应的数据：{}", BEFORE_AUDIT_MSG, resStr);
        if (StringUtils.isBlank(resStr)) {
            msg = "从贝虎获取订单信息出错！贝虎无响应数据！";
            result.put(CommonConstants.MESSSAGE, msg);
            LOGGER.error("{}, {}", BEFORE_AUDIT_MSG, msg);
            return result;
        }

        JSONObject resResult = JSONObject.parseObject(resStr);
        if (SUCCESS_CODE.compareTo(resResult.getInteger(ERR_CODE)) != Constant.ZERO) {
            msg = "从贝虎获取订单信息失败！";
            result.put(CommonConstants.MESSSAGE, msg);
            LOGGER.error("{}, {}{}", BEFORE_AUDIT_MSG, msg, resResult);
            return result;
        }

        Integer orderStatus;
        JSONArray jsonArray = resResult.getJSONObject(DATA).getJSONArray(RESULTS);
        if (jsonArray == null || jsonArray.isEmpty()) {
            orderStatus = auditOrder.getOrderStatus();
            LOGGER.info("{}，{}orderNumber={},orderStatus={}", BEFORE_AUDIT_MSG, "贝虎系统不存在当前商户的进件订单！将以当前订单状态为准！", orderNumber, orderStatus);
        } else {
            orderStatus = jsonArray.getJSONObject(Constant.ZERO).getInteger(ORDER_STATUS);
            LOGGER.info("{}，使用贝虎返回的进件订单状态！orderNumber={},orderStatus={}", BEFORE_AUDIT_MSG, orderNumber, orderStatus);
        }

        if (OrderConstants.ORDER_STATUS_CANCEL.compareTo(orderStatus) != Constant.ZERO) {
            msg = "进件订单没有取消，可以进件！";
            result.put(CommonConstants.RESULT, true);
            result.put(CommonConstants.MESSSAGE, msg);
            LOGGER.info("{}, {}orderStatus={}", BEFORE_AUDIT_MSG, msg, orderStatus);
            return result;
        }

        // 结束流程
        List<ErpDeliveryService> deliveryServices = deliveryServiceService.findByOrederId(auditOrder.getId());
        List<ErpOrderSplitInfo> orderSplitInfos = orderSplitInfoService.findByOrderId(auditOrder.getId());
        Set<String> procInstIds = new HashSet<>(); // 去重流程ID
        if (CollectionUtils.isNotEmpty(deliveryServices)) {
            List<String> deliveryIds = new ArrayList<>();
            deliveryServices.forEach(service -> {
                // 处理中的
                if (service.getFlowEndTime() == null) {
                    procInstIds.add(service.getProcInsId());
                    deliveryIds.add(service.getId());
                }
            });
            // 更新流程异常原因为“订单已取消”
            if (CollectionUtils.isNotEmpty(procInstIds)) {
                deliveryServiceService.updateExceptionLogo(FlowConstant.EXCEPTION_REASON_CANCEL_ORDER, new Date(),
                                ErpDeliveryServiceConstants.STATUS_CANCEL, deliveryIds);
            }
        }
        if (CollectionUtils.isNotEmpty(orderSplitInfos)) {
            List<String> splitIds = new ArrayList<>();
            orderSplitInfos.forEach(split -> {
                // 处理中的
                if (OrderSplitConstants.STATUS_BEGIN == split.getStatus()) {
                    procInstIds.add(split.getProcInsId());
                    splitIds.add(split.getId());
                }
            });
            // 更新流程异常原因为“订单已取消”
            if (CollectionUtils.isNotEmpty(splitIds)) {
                orderSplitInfoService.updateExceptionReason(FlowConstant.EXCEPTION_REASON_CANCEL_ORDER, OrderSplitConstants.STATUS_CANCEL, splitIds);
            }
        }

        Optional.ofNullable(procInstIds).ifPresent(ids -> ids.forEach(id -> {
            try {
                workFlowMonitorService.endProcess(id);
            } catch (Exception e) { // NOSONAR
                String message = "流程结束失败！";
                LOGGER.error("{}，{}procInstId={}", BEFORE_AUDIT_MSG, message, id);
                throw new ServiceException(message);
            }
        }));

        // 修改状态
        auditOrder.setOrderStatus(orderStatus);
        auditOrder.setIsAuditOrder(Constant.NO);
        orderService.save(auditOrder);
        erpShopInfo.setCurrentStatus(ShopConstant.whether.NO);
        erpShopInfo.setSort(ShopConstant.Sort.DISENABLED);
        shopService.save(erpShopInfo);

        msg = erpShopInfo.getName() + " 已取消订单，无法完成进件";
        result.put(CommonConstants.MESSSAGE, msg);
        LOGGER.error("{}, {}, orderNumber={}", BEFORE_AUDIT_MSG, msg, orderNumber);
        return result;
    }

    /**
     * 从贝虎获取最新订单信息
     * 
     * @param orderNumber
     * @return
     */
    private String getOrderInfo(String orderNumber) {
        String beiHuOrderUrl = Constant.BLANK;
        // 从贝虎获取订单信息的地址，去掉路径中的请求参数
        if (apiOrderInfoUrl.indexOf(Constant.QUESTION) != Constant.NEGATIVE_ONE) {
            beiHuOrderUrl = apiOrderInfoUrl.substring(Constant.ZERO, apiOrderInfoUrl.indexOf(Constant.QUESTION));
        }
        beiHuOrderUrl += "?order_numbers=[%22" + orderNumber + "%22]";

        LOGGER.info("{}, 从贝虎获取订单最新状态，beiHuOrderUrl={}", BEFORE_AUDIT_MSG, beiHuOrderUrl);
        return HttpUtil.sendHttpGetReqToServer(beiHuOrderUrl);
    }

}
