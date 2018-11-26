package com.yunnex.ops.erp.modules.store.basic.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.constant.CommonConstants;
import com.yunnex.ops.erp.common.constant.Constant;
import com.yunnex.ops.erp.common.service.ServiceException;
import com.yunnex.ops.erp.common.utils.AESUtil;
import com.yunnex.ops.erp.common.utils.StringUtils;
import com.yunnex.ops.erp.modules.shop.constant.ShopConstant;
import com.yunnex.ops.erp.modules.shop.entity.BusinessCategory;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopActualLinkman;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.shop.service.BusinessCategoryService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopActualLinkmanService;
import com.yunnex.ops.erp.modules.shop.service.ErpShopInfoService;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserFriends;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserMomo;
import com.yunnex.ops.erp.modules.store.advertiser.entity.ErpStoreAdvertiserWeibo;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserFriendsService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserMomoService;
import com.yunnex.ops.erp.modules.store.advertiser.service.ErpStoreAdvertiserWeiboService;
import com.yunnex.ops.erp.modules.store.basic.api.ErpFriendsApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpMomoApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpMomoCheckApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpPhotoApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpShopRoleApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpShopStoreInfoApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpStoreInfoApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpStoreSynthesizeApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpStoreWaiterApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpUnionPayApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpWeiboApi;
import com.yunnex.ops.erp.modules.store.basic.api.ErpWxPayApi;
import com.yunnex.ops.erp.modules.store.basic.entity.BankEnum;
import com.yunnex.ops.erp.modules.store.basic.entity.BusinessScope;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreCredentials;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLegalPerson;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLinkman;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStorePromotePhotoMaterial;
import com.yunnex.ops.erp.modules.store.constant.StoreConstant;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStoreBank;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStoreBankService;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayUnionpayService;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayWeixinService;
import com.yunnex.ops.erp.modules.sys.constant.SysConstant;
import com.yunnex.ops.erp.modules.sys.utils.OpenBankEnum;
import com.yunnex.ops.erp.modules.workflow.flow.service.JykFlowAccountSignalService;

/**
 * 门店信息操作Service
 * <p>
 * notice: 移自{@link com.yunnex.ops.erp.modules.store.basic.web.ErpStoreInfoController}
 * </p>
 */
@Service
public class ErpStoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpStoreService.class);

    private static final String WXPAY = "微信支付";
    private static final String UNIONPAY = "银联支付";
    private static final String FRIENDS_PROMOTION = "朋友圈推广";
    private static final String WEIBO_PROMOTION = "微博推广";
    private static final String MOMO_PROMOTION = "陌陌推广";
    private static final String FINISH = "完成";
    private static final String UN_FINISH = "未完成";
    private static final String CHANGE_PWD_MSG = "商户已进件审核，密码可能修改";
    private static final String ID = "id";
    private static final String RESULT = "result";
    private static final String MESSAGE = "message";
    private static final String ADD_SUCCESS = "新增成功";
    private static final String UPDATE_SUCCESS = "修改成功";
    private static final String DELETE_SUCCESS = "删除成功";
    private static final String DELETE_FAILED = "删除失败";
    private static final String HAS_MAIN_STORE_ERROR = "新增失败，该商户已有主体总店";
    private static final String SET_MAIN_STORE_SUCCESS = "设置主体成功";
    private static final String STORE_NOT_EXISTS = "门店不存在！";

    private static final Integer SIX = 6;

    @Autowired
    private ErpStoreInfoService erpStoreInfoService;
    @Autowired
    private ErpStoreLinkmanService storeLinkmanService;
    @Autowired
    private ErpStoreLegalPersonService legalPersonService;
    @Autowired
    private ErpStoreCredentialsService credentialsService;
    @Autowired
    private ErpShopInfoService shopInfoService;
    @Autowired
    private ErpShopActualLinkmanService shopActualLinkmanService;
    @Autowired
    private BusinessScopeService businessScopeService;
    @Autowired
    private BusinessCategoryService businessCategoryService;
    @Autowired
    private ErpStorePayWeixinService wxpayService;
    @Autowired
    private ErpStorePayUnionpayService uninpayService;
    @Autowired
    private ErpStoreBankService bankService;
    @Autowired
    private ErpStoreAdvertiserFriendsService friendsService;
    @Autowired
    private ErpStoreAdvertiserWeiboService weiboService;
    @Autowired
    private ErpStoreAdvertiserMomoService momoService;
    @Autowired
    private ErpStorePromotePhotoMaterialService promotePhotoMaterialService;
    @Autowired
    private JykFlowAccountSignalService jykFlowAccountSignalService;


    /**
     * 门店综合信息新增联系人
     */
    @Transactional
    public JSONObject addLinkman(String shopid, String linkname, String linkphone, String position) {
        JSONObject resObject = new JSONObject();
        ErpShopActualLinkman linkman = new ErpShopActualLinkman();
        linkman.setName(linkname);
        linkman.setPhoneNo(linkphone);
        linkman.setPosition(position);
        linkman.setShopInfoId(shopid);
        shopActualLinkmanService.save(linkman);
        resObject.put(RESULT, true);
        resObject.put(MESSAGE, ADD_SUCCESS);
        resObject.put(ID, linkman.getId());
        return resObject;
    }

    /**
     * 门店综合信息修改联系人
     *
     */
    @Transactional
    public JSONObject updateLinkman(String shopid, String linkid, String linkname, String linkphone, String position) {
        JSONObject resObject = new JSONObject();
        ErpShopActualLinkman linkman = shopActualLinkmanService.get(linkid);
        linkman.setName(linkname);
        linkman.setPhoneNo(linkphone);
        linkman.setPosition(position);
        linkman.setShopInfoId(shopid);
        shopActualLinkmanService.save(linkman);
        resObject.put(RESULT, true);
        resObject.put(MESSAGE, UPDATE_SUCCESS);
        return resObject;
    }

    /**
     * 门店综合信息删除联系人
     */
    @Transactional
    public JSONObject deleteLinkman(String linkid) {
        JSONObject resObject = new JSONObject();
        ErpShopActualLinkman linkman = shopActualLinkmanService.get(linkid);
        if (null != linkman) {
            shopActualLinkmanService.delete(linkman);
            resObject.put(RESULT, true);
            resObject.put(MESSAGE, DELETE_SUCCESS);
            return resObject;
        }
        resObject.put(RESULT, false);
        resObject.put(MESSAGE, DELETE_FAILED);
        return resObject;
    }

    /**
     * 商户有权限的访问人list
     *
     * @throws ParseException
     *
     */
    public List<ErpShopRoleApi> shopRoleList(String shopId) {
        ErpShopInfo e = shopInfoService.get(shopId);
        List<ErpShopInfo> list = shopInfoService.findshoprole(e.getZhangbeiId());
        List<ErpShopInfo> listtwo = shopInfoService.findshoproleTwo(e.getZhangbeiId());
        ErpShopRoleApi role;
        List<ErpShopRoleApi> rolelist = new ArrayList();
        if (CollectionUtils.isEmpty(list)) {
            role = new ErpShopRoleApi();
            role.setUserId(SysConstant.ADMIN_ID);
            role.setUserName(SysConstant.ADMIN_CN);
            rolelist.add(role);
        } else {
            for (int i = 0; i < list.size(); i++) {
                role = new ErpShopRoleApi();
                role.setUserId(list.get(i).getRoleuserId());
                role.setUserName(list.get(i).getRoleuserName());
                rolelist.add(role);
            }
            role = new ErpShopRoleApi();
            role.setShopId(list.get(Constant.ZERO).getRoleshopId());
            role.setUserId(SysConstant.ADMIN_ID);
            role.setUserName(SysConstant.ADMIN_CN);
            rolelist.add(role);
        }

        if (!CollectionUtils.isEmpty(listtwo)) {
            for (int j = 0; j < listtwo.size(); j++) {
                if (null != listtwo.get(j)) {
                    role = new ErpShopRoleApi();
                    role.setUserId(listtwo.get(j).getRoleuserId());
                    role.setUserName(listtwo.get(j).getRoleuserName());
                    rolelist.add(role);
                }
            }
        }
        return rolelist;
    }

    public ErpShopStoreInfoApi getList(String id) {
        List<ErpStoreInfo> list = erpStoreInfoService.findAllListWhereShopId(Global.NO, id);
        ErpShopStoreInfoApi shopstoreapi = new ErpShopStoreInfoApi();
        ErpStoreInfoApi storeapi;
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                storeapi = new ErpStoreInfoApi();
                if (null != list.get(i).getWeixinPayId() && !(Constant.BLANK.equals(list.get(i).getWeixinPayId()))) {
                    storeapi.getPay().add(WXPAY);
                }
                if (null != list.get(i).getUnionpayId() && !(Constant.BLANK.equals(list.get(i).getUnionpayId()))) {
                    storeapi.getPay().add(UNIONPAY);
                }
                if (null != list.get(i).getAdvertiserFriendsId() && !(Constant.BLANK.equals(list.get(i).getAdvertiserFriendsId()))) {
                    storeapi.getAdvertiser().add(FRIENDS_PROMOTION);
                }
                if (null != list.get(i).getAdvertiserWeiboId() && !(Constant.BLANK.equals(list.get(i).getAdvertiserWeiboId()))) {
                    storeapi.getAdvertiser().add(WEIBO_PROMOTION);
                }
                if (null != list.get(i).getAdvertiserMomoId() && !(Constant.BLANK.equals(list.get(i).getAdvertiserMomoId()))) {
                    storeapi.getAdvertiser().add(MOMO_PROMOTION);
                }
                storeapi.setId(list.get(i).getId());
                storeapi.setStoreName(list.get(i).getShortName());
                if (list.get(i).getStroeLinkMan() != null) {// 门店联系人取门店表数据
                    storeapi.setContentName(list.get(i).getStroeLinkMan().getName());
                    storeapi.setContentPhone(list.get(i).getStroeLinkMan().getPhone());
                }
                storeapi.setPicNum(list.get(i).getEnvironmentPhotoCount() + list.get(i).getProductPhotoCount() + list.get(i).getMenuPhotoCount());
                storeapi.setIsMain(list.get(i).getIsMain().toString());
                if (null != list.get(i).getWeixinPayId() || null != list.get(i).getUnionpayId()) {
                    storeapi.setStoreMaterial(FINISH);
                }
                if (null == list.get(i).getAdvertiserFriendsId() && null == list.get(i).getAdvertiserWeiboId() && null == list.get(i)
                                .getAdvertiserMomoId()) {
                    storeapi.setStoreMaterial(UN_FINISH);
                }
                storeapi.setAddress(list.get(i).getAddress());
                shopstoreapi.setShopId(list.get(i).getShopId());
                shopstoreapi.setShopName(list.get(i).getShopName());
                shopstoreapi.getStorelist().add(storeapi);
            }
        }
        return shopstoreapi;
    }

    /**
     * 门店综合信息
     *
     */
    public ErpStoreSynthesizeApi getSynthesizeList(String storeId, String shopId, String isMain) {
        if (Constant.ZERO.toString().equals(isMain)) {
            ErpStoreInfo erpStoreInfo = erpStoreInfoService.findOnetoManyAll(Global.NO, storeId);
            if (erpStoreInfo == null) {
                return null;
            }
            ErpShopInfo shop = shopInfoService.get(shopId);
            ErpStoreSynthesizeApi Synthesize = new ErpStoreSynthesizeApi();
            Synthesize.setShopId(shopId);
            Synthesize.setShopName(shop.getName());
            Synthesize.setShopType(erpStoreInfo.getBusinessType());
            Synthesize.setIndustryType(shop.getIndustryType());
            Synthesize.setStoreName(erpStoreInfo.getShortName());
            Synthesize.setStoreAddress(erpStoreInfo.getAddress());
            if (null != erpStoreInfo.getWxPay()) {
                Synthesize.setWeixinSchedule(erpStoreInfo.getWxPay().getAuditStatus());
            } else {
                Synthesize.setWeixinSchedule(Constant.ZERO);
            }
            if (null != erpStoreInfo.getUnionPay()) {
                Synthesize.setUnionpaySchedule(erpStoreInfo.getUnionPay().getAuditStatus());
            } else {
                Synthesize.setUnionpaySchedule(Constant.ZERO);
            }
            if (null != erpStoreInfo.getFriend()) {
                Synthesize.setFriendsSchedule(erpStoreInfo.getFriend().getAuditStatus());
            } else {
                Synthesize.setFriendsSchedule(Constant.ZERO);
            }
            if (null != erpStoreInfo.getMomo()) {
                Synthesize.setMomoSchedule(erpStoreInfo.getMomo().getAuditStatus());
            } else {

                Synthesize.setMomoSchedule(Constant.ZERO);
            }
            if (null != erpStoreInfo.getWeibo()) {

                Synthesize.setWeiboSchedule(erpStoreInfo.getWeibo().getAuditStatus());
            } else {

                Synthesize.setWeiboSchedule(Constant.ZERO);
            }
            if (null != erpStoreInfo.getPhotoMaterial()) {
                Synthesize.setPhotoSchedul(erpStoreInfo.getPhotoMaterial().getAuditStatus());
            } else {
                Synthesize.setPhotoSchedul(Constant.ZERO);
            }
            return Synthesize;
        } else {
            ErpStoreInfo erpStoreInfo = erpStoreInfoService.findOnetoManyAll(Global.NO, storeId);
            ErpStoreInfo e = erpStoreInfoService.findismain(shopId, Global.NO);
            ErpShopInfo shop = shopInfoService.get(shopId);
            ErpStoreSynthesizeApi Synthesize = new ErpStoreSynthesizeApi();
            Synthesize.setShopId(shopId);
            Synthesize.setShopName(shop.getName());
            Synthesize.setShopType(erpStoreInfo.getBusinessType());
            Synthesize.setIndustryType(shop.getIndustryType());
            Synthesize.setStoreName(erpStoreInfo.getShortName());
            Synthesize.setStoreAddress(erpStoreInfo.getAddress());
            Synthesize.setShopNumber(shop.getNumber());
            Synthesize.setXcxAccount(shop.getZhangbeiId());
            if (e.getAuditStatus() == Constant.ZERO) {
                Synthesize.setXcxPass(shop.getZhangbeiId().substring(shop.getZhangbeiId().length() - SIX));
            } else {
                Synthesize.setXcxPass(CHANGE_PWD_MSG);
            }
            Synthesize.setServiceProvider(shop.getServiceProvider());
            Synthesize.setServicePhone(shop.getServiceProviderPhone());
            Synthesize.setStorecount(shop.getStoreCount());
            if (null != erpStoreInfo.getWxPay()) {
                Synthesize.setWeixinSchedule(erpStoreInfo.getWxPay().getAuditStatus());
            } else {
                Synthesize.setWeixinSchedule(Constant.ZERO);
            }
            if (null != erpStoreInfo.getUnionPay()) {
                Synthesize.setUnionpaySchedule(erpStoreInfo.getUnionPay().getAuditStatus());
            } else {
                Synthesize.setUnionpaySchedule(Constant.ZERO);
            }
            if (null != erpStoreInfo.getFriend()) {
                Synthesize.setFriendsSchedule(erpStoreInfo.getFriend().getAuditStatus());
            } else {
                Synthesize.setFriendsSchedule(Constant.ZERO);
            }
            if (null != erpStoreInfo.getMomo()) {
                Synthesize.setMomoSchedule(erpStoreInfo.getMomo().getAuditStatus());
            } else {

                Synthesize.setMomoSchedule(Constant.ZERO);
            }
            if (null != erpStoreInfo.getWeibo()) {

                Synthesize.setWeiboSchedule(erpStoreInfo.getWeibo().getAuditStatus());
            } else {

                Synthesize.setWeiboSchedule(Constant.ZERO);
            }
            if (null != erpStoreInfo.getPhotoMaterial()) {
                Synthesize.setPhotoSchedul(erpStoreInfo.getPhotoMaterial().getAuditStatus());
            } else {
                Synthesize.setPhotoSchedul(Constant.ZERO);
            }
            List<ErpShopActualLinkman> linkman = shopActualLinkmanService.findShopLinmanByShopId(Global.NO, shop.getId());
            Synthesize.getLinkMan().addAll(linkman);
            List<ErpShopInfo> fw = shopInfoService.findshopwaiter(shop.getZhangbeiId());
            if (null != fw && !fw.isEmpty()) {
                ErpStoreWaiterApi waiter;
                for (int i = 0; i < fw.size(); i++) {
                    waiter = new ErpStoreWaiterApi();
                    waiter.setFwRole(fw.get(i).getFwrole());
                    waiter.setFwName(fw.get(i).getFwname());
                    waiter.setFwType(fw.get(i).getFwtype());
                    Synthesize.getStoreWaiter().add(waiter);
                }
            }
            return Synthesize;
        }
    }

    /**
     * 门店基本信息list
     *
     */
    public ErpStoreInfo basicInformationList(String shopId, String storeId) {
        ErpShopInfo shop = shopInfoService.get(shopId);
        ErpStoreInfo store = erpStoreInfoService.findBasicInformation(Global.NO, storeId);
        ErpStoreLinkman linkman = storeLinkmanService.findWhereStoreId(Global.NO, store.getId());
        List<BusinessScope> scopelist = businessScopeService.findAllList();
        List<BusinessCategory> category = businessCategoryService.findAllList();
        store.setStroeLinkMan(linkman);
        store.setShopId(shopId);
        store.setShopName(shop.getName());
        store.setBusinesscategory(shop.getBusinessCategory());
        store.setBusinesscategoryname(shop.getBusinessCategoryName());
        store.getScope().addAll(scopelist);
        store.getCategory().addAll(category);
        return store;
    }

    /**
     * 检查是否有高级修改权限
     * 
     * @param needCheck
     */
    private static void checkStoreAdvancedEditPerm(boolean needCheck) {
        if (needCheck) {
            SecurityUtils.getSubject().checkPermission(StoreConstant.PERMISSION_STORE_ADVANCED_EDIT);
        }
    }

    /**
     * 保存门店基本信息
     *
     * @param jsonObject
     * @return
     */
    @Transactional
    public JSONObject saveBasicStoreInfo(String jsonObject) {
        JSONObject resObject = new JSONObject();
        String newJson = StringEscapeUtils.unescapeHtml4(jsonObject);
        ErpStoreInfo e = JSON.parseObject(newJson, new TypeReference<ErpStoreInfo>() {});
        ErpStoreInfo s = erpStoreInfoService.get(e.getId());
        if (s == null) {
            resObject.put(RESULT, false);
            resObject.put(MESSAGE, STORE_NOT_EXISTS);
            return resObject;
        }
        // 如果已经审核通过，检查是否有高级修改权限
        checkStoreAdvancedEditPerm(StoreConstant.STORE_AUDIT_STATUS_PASS == s.getAuditStatus());
        if (s.getIsMain() != Constant.ONE && e.getIsMain() == Constant.ONE) {
            Integer count = erpStoreInfoService.countIsMain(Global.YES, e.getShopId(), Global.NO);
            if (count > Constant.ZERO) {
                resObject.put(RESULT, false);
                resObject.put(MESSAGE, HAS_MAIN_STORE_ERROR);
                return resObject;
            }
        }
        ErpStoreInfo store = erpStoreInfoService.get(e.getId());
        store.setShortName(e.getShortName());
        store.setAddress(e.getAddress());
        store.setProvince(e.getProvince());
        store.setCity(e.getCity());
        store.setArea(e.getArea());
        store.setProvinceName(e.getProvinceName());
        store.setCityName(e.getCityName());
        store.setAreaName(e.getAreaName());
        store.setTelephone(e.getTelephone());
        store.setCompanyUrl(e.getCompanyUrl());
        store.setProductName(e.getProductName());
        store.setProductConcreteInfo(e.getProductConcreteInfo());
        store.setBusinessType(e.getBusinessType());
        store.setIcpMessage(e.getIcpMessage());
        store.setShopInfoId(e.getShopId());
        store.setIsMain(e.getIsMain());
        // 门店联系人
        ErpStoreLinkman linkman;
        if (null != e.getStroeLinkMan().getId() && !Constant.BLANK.equals(e.getStroeLinkMan().getId())) {
            linkman = storeLinkmanService.get(e.getStroeLinkMan().getId());
        } else {
            linkman = new ErpStoreLinkman();
        }
        linkman.setName(e.getStroeLinkMan().getName());
        linkman.setAddress(e.getStroeLinkMan().getAddress());
        linkman.setPhone(e.getStroeLinkMan().getPhone());
        linkman.setEmail(e.getStroeLinkMan().getEmail());
        linkman.setStoreInfoId(store.getId());
        // 门店法人
        ErpStoreLegalPerson person;
        if (null != e.getPerson().getId() && !Constant.BLANK.equals(e.getPerson().getId())) {
            person = legalPersonService.get(e.getPerson().getId());
        } else {
            person = new ErpStoreLegalPerson();
        }
        person.setName(e.getPerson().getName());
        person.setIdCardNo(e.getPerson().getIdCardNo());
        person.setIdCardStartDate(e.getPerson().getIdCardStartDate());
        person.setIdCardEndDate(e.getPerson().getIdCardEndDate());
        person.setIdCardFrontPhoto(e.getPerson().getIdCardFrontPhoto());
        person.setIdCardReversePhoto(e.getPerson().getIdCardReversePhoto());
        person.setIdCardInHandPhoto(e.getPerson().getIdCardInHandPhoto());
        person.setIsLongterm(e.getPerson().getIsLongterm());
        // 营业资质信息
        ErpStoreCredentials credentials;
        if (null != e.getCredentials().getId() && !Constant.BLANK.equals(e.getCredentials().getId())) {
            credentials = credentialsService.get(e.getCredentials().getId());
        } else {
            credentials = new ErpStoreCredentials();
        }
        credentials.setRegisterName(e.getCredentials().getRegisterName());
        credentials.setRegisterNo(e.getCredentials().getRegisterNo());
        credentials.setRegisterCity(e.getCredentials().getRegisterCity());
        credentials.setRegisterAddress(e.getCredentials().getRegisterAddress());
        credentials.setBusinessScope(e.getCredentials().getBusinessScope());
        credentials.setStartDate(e.getCredentials().getStartDate());
        credentials.setEndDate(e.getCredentials().getEndDate());
        credentials.setIsLongTime(e.getCredentials().getIsLongTime());
        credentials.setOrganizationCodeCertificate(e.getCredentials().getOrganizationCodeCertificate());
        credentials.setOrganizationCodeCertificateNo(e.getCredentials().getOrganizationCodeCertificateNo());
        credentials.setBusinessLicence(e.getCredentials().getBusinessLicence());
        credentials.setSpecialCertificate(e.getCredentials().getSpecialCertificate());
        storeLinkmanService.save(linkman);
        legalPersonService.save(person);
        credentialsService.save(credentials);
        store.setLegalPersonId(person.getId());
        store.setCredentialsId(credentials.getId());
        erpStoreInfoService.save(store);
        ErpShopInfo shop = shopInfoService.get(e.getShopId());
        shop.setBusinessCategory(e.getBusinesscategory());
        shop.setBusinessCategoryName(e.getBusinesscategoryname());
        shopInfoService.save(shop);

        resObject.put(RESULT, true);
        resObject.put(MESSAGE, ADD_SUCCESS);
        return resObject;
    }

    /**
     * 微信支付开通list
     *
     * @throws ParseException
     *
     */
    public ErpWxPayApi wxpayList(String shopId, String storeId) {
        ErpShopInfo shop = shopInfoService.get(shopId);
        ErpStoreInfo store = erpStoreInfoService.findBasicInformation(Global.NO, storeId);
        ErpStorePayWeixin wxpay = null;
        String wxpayId = store.getWeixinPayId();
        if (StringUtils.isNotBlank(wxpayId)) {
            wxpay = wxpayService.getPayAndBank(wxpayId);
            if (wxpay == null) {
                LOGGER.error("微信支付信息不存在！wxpayId={}", wxpayId);
                throw new ServiceException(CommonConstants.FailMsg.PARAM);
            }
            // 公众号解密
            String publicAccountPassword = wxpay.getPublicAccountPassword();
            if (StringUtils.isNotBlank(publicAccountPassword)) {
                publicAccountPassword = AESUtil.decrypt(publicAccountPassword);
                wxpay.setPublicAccountPassword(publicAccountPassword);
            }
            // 邮箱密码解密
            String emailPassword = wxpay.getEmailPassword();
            if (StringUtils.isNotBlank(emailPassword)) {
                emailPassword = AESUtil.decrypt(emailPassword);
                wxpay.setEmailPassword(emailPassword);
            }
        }
        ErpWxPayApi wxpayApi = new ErpWxPayApi();
        wxpayApi.setShopid(shop.getId());
        wxpayApi.setShopname(shop.getName());
        wxpayApi.setStoreid(store.getId());
        wxpayApi.setCredentialsId(store.getCredentialsId());
        wxpayApi.setWeixinPay(wxpay);
        wxpayApi.setBankenum(bankEnum());
        ErpStoreLinkman linkman = storeLinkmanService.findWhereStoreId(Global.NO, store.getId());
        store.setStroeLinkMan(linkman);
        List<BusinessScope> scopelist = businessScopeService.findAllList();
        List<BusinessCategory> category = businessCategoryService.findAllList();
        store.getScope().addAll(scopelist);
        store.getCategory().addAll(category);
        store.setBusinesscategory(shop.getBusinessCategory());
        store.setBusinesscategoryname(shop.getBusinessCategoryName());
        wxpayApi.setStoreInfo(store);
        return wxpayApi;
    }

    /**
     * 微信支付保存
     */
    @Transactional
    public JSONObject addWxpay(String jsonObject) {
        JSONObject resObject = new JSONObject();
        String newJson = StringEscapeUtils.unescapeHtml4(jsonObject);
        ErpWxPayApi e = JSON.parseObject(newJson, new TypeReference<ErpWxPayApi>() {});
        // 如果已经审核通过，检查是否有高级修改权限
        Optional.ofNullable(e).ifPresent(wx -> {
            ErpStorePayWeixin weixinPay = wx.getWeixinPay();
            if (weixinPay != null && StringUtils.isNotBlank(weixinPay.getId())) {
                ErpStorePayWeixin erpStorePayWeixin = wxpayService.get(weixinPay.getId());
                if (erpStorePayWeixin != null) {
                    checkStoreAdvancedEditPerm(StoreConstant.PAY_AUDIT_STATUS_PASS == erpStorePayWeixin.getAuditStatus());
                }
            }
        });
        erpStoreInfoService.addwxpay(e);
        resObject.put(RESULT, true);
        resObject.put(MESSAGE, ADD_SUCCESS);
        return resObject;
    }


    /**
     * 银行枚举封装list
     */
    public List<BankEnum> bankEnum() {
        List<BankEnum> list = new ArrayList();
        for (OpenBankEnum type : OpenBankEnum.values()) {
            BankEnum b = new BankEnum();
            b.setCode(type.getId());
            b.setDesc(type.getText());
            list.add(b);
        }
        return list;
    }

    /**
     * 新增门店基本信息list
     *
     */
    @Transactional
    public ErpStoreInfo newStoreList(String shopId, String storeName) {
        Integer count = erpStoreInfoService.findCountWhereShopId(Global.NO, shopId);
        ErpStoreInfo e = new ErpStoreInfo();
        if (count == Constant.ZERO) {
            e.setIsMain(Constant.ONE);
        } else {
            e.setIsMain(Constant.ZERO);
        }
        e.setShortName(storeName);
        e.setShopInfoId(shopId);
        e.setSyncOem(ShopConstant.whether.NO);
        erpStoreInfoService.save(e);
        ErpShopInfo s = shopInfoService.get(shopId);
        s.setStoreCount(s.getStoreCount() + Constant.ONE);
        shopInfoService.save(s);

        ErpShopInfo shop = shopInfoService.get(shopId);
        String newstorename = StringEscapeUtils.unescapeHtml4(storeName);
        ErpStoreInfo store = erpStoreInfoService.getIsmainStore(Global.NO, shop.getId(), Global.YES);
        List<BusinessCategory> category = businessCategoryService.findAllList();
        List<BusinessScope> scopelist = businessScopeService.findAllList();
        if (null != store) {
            ErpStoreLinkman linkman = storeLinkmanService.findWhereStoreId(Global.NO, store.getId());
            store.setId(e.getId());
            store.setStroeLinkMan(linkman);
            store.setShopId(shopId);
            store.setShortName(newstorename);
            store.setShopName(shop.getName());
            store.setBusinesscategory(shop.getBusinessCategory());
            store.setBusinesscategoryname(shop.getBusinessCategoryName());
            store.getScope().addAll(scopelist);
            store.getCategory().addAll(category);
            store.setIsMain(e.getIsMain());
        } else {
            store = new ErpStoreInfo();
            store.setId(e.getId());
            store.getScope().addAll(scopelist);
            store.getCategory().addAll(category);
            store.setIsMain(e.getIsMain());
        }
        store.setSyncOem(ShopConstant.whether.NO);
        return store;
    }

    /**
     * 新增门店基本信息保存
     *
     */
    @Transactional
    public JSONObject addNewStore(String jsonObject) {
        JSONObject resObject = new JSONObject();
        String newJson = StringEscapeUtils.unescapeHtml4(jsonObject);
        LOGGER.info("store save info ,json=", newJson);
        ErpStoreInfo e = JSON.parseObject(newJson, new TypeReference<ErpStoreInfo>() {});
        Integer storeCount = erpStoreInfoService.findCountWhereShopId(Global.NO, e.getShopId());
        if (storeCount > Constant.ONE && e.getIsMain() == Constant.ONE) {
            Integer count = erpStoreInfoService.countIsMain(Global.YES, e.getShopId(), Global.NO);
            if (count > Constant.ZERO) {
                resObject.put(RESULT, false);
                resObject.put(MESSAGE, HAS_MAIN_STORE_ERROR);
                return resObject;
            }
        }
        ErpStoreInfo store = erpStoreInfoService.get(e.getId());
        if (null == store) {
            store = new ErpStoreInfo();
        }
        store.setShortName(e.getShortName());
        store.setAddress(e.getAddress());
        store.setProvince(e.getProvince());
        store.setCity(e.getCity());
        store.setArea(e.getArea());
        store.setProvinceName(e.getProvinceName());
        store.setCityName(e.getCityName());
        store.setAreaName(e.getAreaName());
        store.setTelephone(e.getTelephone());
        store.setCompanyUrl(e.getCompanyUrl());
        store.setProductName(e.getProductName());
        store.setProductConcreteInfo(e.getProductConcreteInfo());
        store.setBusinessType(e.getBusinessType());
        store.setIcpMessage(e.getIcpMessage());
        store.setShopInfoId(e.getShopId());
        store.setSyncOem(ShopConstant.whether.NO);
        Integer count = erpStoreInfoService.findCountWhereShopId(Global.NO, e.getShopId());
        if (count == Constant.ONE) {
            store.setIsMain(Constant.ONE);
        } else {
            store.setIsMain(e.getIsMain());
        }
        // 门店联系人
        ErpStoreLinkman linkman = new ErpStoreLinkman();
        linkman.setName(e.getStroeLinkMan().getName());
        linkman.setAddress(e.getStroeLinkMan().getAddress());
        linkman.setPhone(e.getStroeLinkMan().getPhone());
        linkman.setEmail(e.getStroeLinkMan().getEmail());

        // 门店法人
        ErpStoreLegalPerson person = new ErpStoreLegalPerson();
        person.setName(e.getPerson().getName());
        person.setIdCardNo(e.getPerson().getIdCardNo());
        person.setIdCardStartDate(e.getPerson().getIdCardStartDate());
        person.setIdCardEndDate(e.getPerson().getIdCardEndDate());
        person.setIdCardFrontPhoto(e.getPerson().getIdCardFrontPhoto());
        person.setIdCardReversePhoto(e.getPerson().getIdCardReversePhoto());
        person.setIdCardInHandPhoto(e.getPerson().getIdCardInHandPhoto());
        person.setIsLongterm(e.getPerson().getIsLongterm());
        // 营业资质信息
        ErpStoreCredentials credentials = new ErpStoreCredentials();
        credentials.setRegisterName(e.getCredentials().getRegisterName());
        credentials.setRegisterNo(e.getCredentials().getRegisterNo());
        credentials.setRegisterCity(e.getCredentials().getRegisterCity());
        credentials.setRegisterAddress(e.getCredentials().getRegisterAddress());
        credentials.setBusinessScope(e.getCredentials().getBusinessScope());
        credentials.setStartDate(e.getCredentials().getStartDate());
        credentials.setEndDate(e.getCredentials().getEndDate());
        credentials.setIsLongTime(e.getCredentials().getIsLongTime());
        credentials.setOrganizationCodeCertificate(e.getCredentials().getOrganizationCodeCertificate());
        credentials.setOrganizationCodeCertificateNo(e.getCredentials().getOrganizationCodeCertificateNo());
        credentials.setBusinessLicence(e.getCredentials().getBusinessLicence());
        credentials.setSpecialCertificate(e.getCredentials().getSpecialCertificate());
        legalPersonService.save(person);
        credentialsService.save(credentials);
        store.setLegalPersonId(person.getId());
        store.setCredentialsId(credentials.getId());
        erpStoreInfoService.save(store);
        linkman.setStoreInfoId(store.getId());
        storeLinkmanService.save(linkman);
        // 门店数加1
        ErpShopInfo shop = shopInfoService.get(e.getShopId());
        shop.setBusinessCategory(e.getBusinesscategory());
        shop.setBusinessCategoryName(e.getBusinesscategoryname());
        shopInfoService.save(shop);

        resObject.put(RESULT, true);
        resObject.put(MESSAGE, ADD_SUCCESS);
        return resObject;
    }

    /**
     * 银联支付开通list
     */
    public ErpUnionPayApi unionpayList(String shopId, String storeId) {
        ErpShopInfo shop = shopInfoService.get(shopId);
        ErpStoreInfo store = erpStoreInfoService.findBasicInformation(Global.NO, storeId);
        ErpStorePayUnionpay unionpay = null;
        if (null != store.getUnionpayId() && !Constant.BLANK.equals(store.getUnionpayId())) {
            unionpay = uninpayService.getPayAndBank(store.getUnionpayId());
        }
        ErpUnionPayApi unionApi = new ErpUnionPayApi();
        unionApi.setShopid(shop.getId());
        unionApi.setShopname(shop.getName());
        unionApi.setStoreid(store.getId());
        unionApi.setCredentialsId(store.getCredentialsId());
        unionApi.setUnionPay(unionpay);
        unionApi.setBankenum(bankEnum());
        ErpStoreLinkman linkman = storeLinkmanService.findWhereStoreId(Global.NO, store.getId());
        store.setStroeLinkMan(linkman);
        unionApi.setStoreInfo(store);
        return unionApi;
    }

    /**
     * 银联支付保存
     *
     */
    @Transactional
    public JSONObject addUnionpayPay(String jsonObject) {
        JSONObject resObject = new JSONObject();
        String newJson = StringEscapeUtils.unescapeHtml4(jsonObject);
        ErpUnionPayApi e = JSON.parseObject(newJson, new TypeReference<ErpUnionPayApi>() {});
        ErpStoreInfo store = erpStoreInfoService.get(e.getStoreid());
        ErpStorePayUnionpay union;
        if (null != e.getUnionPay()) {
            if (null != e.getUnionPay().getId() && !Constant.BLANK.equals(e.getUnionPay().getId())) {
                union = uninpayService.get(e.getUnionPay().getId());
            } else {
                union = new ErpStorePayUnionpay();
            }
        } else {
            union = new ErpStorePayUnionpay();
        }
        // 如果已经审核通过，检查是否有高级修改权限
        checkStoreAdvancedEditPerm(StoreConstant.PAY_AUDIT_STATUS_PASS == union.getAuditStatus());
        union.setAuditContent(e.getUnionPay().getAuditContent());
        union.setLianDan(e.getUnionPay().getLianDan());
        union.setStorePhotoCashierDesk(e.getUnionPay().getStorePhotoCashierDesk());
        union.setStorePhotoDoorHead(e.getUnionPay().getStorePhotoDoorHead());
        union.setStorePhotoEnvironment(e.getUnionPay().getStorePhotoEnvironment());
        union.setAdditionalPhoto(e.getUnionPay().getAdditionalPhoto());
        union.setMultiAccountApplicationForm(e.getUnionPay().getMultiAccountApplicationForm());
        union.setSyncOem(ShopConstant.whether.NO);
        ErpStoreBank bank;
        if (null != e.getUnionPay().getBank()) {
            if (null != e.getUnionPay().getBank().getId() && !Constant.BLANK.equals(e.getUnionPay().getBank().getId())) {
                bank = bankService.get(e.getUnionPay().getBank().getId());
            } else {
                bank = new ErpStoreBank();
            }
        } else {
            bank = new ErpStoreBank();
        }
        bank.setBankId(e.getUnionPay().getBank().getBankId());
        bank.setBankName(e.getUnionPay().getBank().getBankName());
        bank.setAccountType(e.getUnionPay().getBank().getAccountType());
        bank.setOpenAccountLicence(e.getUnionPay().getBank().getOpenAccountLicence());
        bank.setCreditCardFrontPhoto(e.getUnionPay().getBank().getCreditCardFrontPhoto());
        bank.setCreditCardReversePhoto(e.getUnionPay().getBank().getCreditCardReversePhoto());
        bank.setAuthorizeProxy(e.getUnionPay().getBank().getAuthorizeProxy());
        bank.setOpenAccountName(e.getUnionPay().getBank().getOpenAccountName());
        bank.setBranchBankName(e.getUnionPay().getBank().getBranchBankName());
        bank.setCreditCardNo(e.getUnionPay().getBank().getCreditCardNo());
        bank.setBankNo(e.getUnionPay().getBank().getBankNo());
        bank.setZhangbeiBindCount(e.getUnionPay().getBank().getZhangbeiBindCount());
        bank.setPayWay(Constant.ZERO);
        bank.setProvince(e.getUnionPay().getBank().getProvince());
        bank.setProvinceName(e.getUnionPay().getBank().getProvinceName());
        bank.setCity(e.getUnionPay().getBank().getCity());
        bank.setCityName(e.getUnionPay().getBank().getCityName());
        bank.setArea(e.getUnionPay().getBank().getArea());
        bank.setAreaName(e.getUnionPay().getBank().getAreaName());
        bankService.save(bank);
        union.setBankId(bank.getId());
        uninpayService.save(union);
        store.setUnionpayId(union.getId());
        erpStoreInfoService.save(store);

        resObject.put(RESULT, true);
        resObject.put(MESSAGE, ADD_SUCCESS);
        return resObject;
    }

    /**
     * 朋友圈推广开通list
     */
    public ErpFriendsApi friendsList(String shopId, String storeId) {
        ErpShopInfo shop = shopInfoService.get(shopId);
        ErpStoreInfo store = erpStoreInfoService.get(storeId);
        ErpStoreAdvertiserFriends friends = null;
        if (null != store.getAdvertiserFriendsId() && !Constant.BLANK.equals(store.getAdvertiserFriendsId())) {
            friends = friendsService.get(store.getAdvertiserFriendsId());
            if (friends != null) {
                // 解密 公众号登录密码
                String accountPassword = friends.getAccountPassword();
                if (StringUtils.isNotBlank(accountPassword)) {
                    friends.setAccountPassword(AESUtil.decrypt(accountPassword));
                }
            }
        }
        ErpFriendsApi friendsApi = new ErpFriendsApi();
        friendsApi.setShopid(shop.getId());
        friendsApi.setShopname(shop.getName());
        friendsApi.setStoreid(store.getId());
        friendsApi.setFrinds(friends);
        return friendsApi;
    }

    /**
     * 朋友圈推广保存
     *
     */
    @Transactional
    public JSONObject addFriends(String jsonObject) {
        JSONObject resObject = new JSONObject();
        String newJson = StringEscapeUtils.unescapeHtml4(jsonObject);
        ErpFriendsApi e = JSON.parseObject(newJson, new TypeReference<ErpFriendsApi>() {});
        ErpStoreInfo store = erpStoreInfoService.get(e.getStoreid());
        ErpStoreAdvertiserFriends friends;
        if (null != e.getFrinds()) {
            if (null != e.getFrinds().getId() && !Constant.BLANK.equals(e.getFrinds().getId())) {
                friends = friendsService.get(e.getFrinds().getId());
            } else {
                friends = new ErpStoreAdvertiserFriends();
            }
        } else {
            friends = new ErpStoreAdvertiserFriends();
        }
        // 如果已经审核通过，检查是否有高级修改权限
        checkStoreAdvancedEditPerm(StoreConstant.ADVERTISER_AUDIT_STATUS_PASS == friends.getAuditStatus());
        friends.setProvideAccountInfo(e.getFrinds().getProvideAccountInfo());
        friends.setAccountNo(e.getFrinds().getAccountNo());
        friends.setAccountPassword(AESUtil.encrypt(e.getFrinds().getAccountPassword()));
        friends.setAccountOriginalId(e.getFrinds().getAccountOriginalId());
        friends.setAdvertiserScreenshot(e.getFrinds().getAdvertiserScreenshot());
        friends.setStoreScreenshot(e.getFrinds().getStoreScreenshot());

        friendsService.save(friends);
        store.setAdvertiserFriendsId(friends.getId());
        erpStoreInfoService.save(store);

        resObject.put(RESULT, true);
        resObject.put(MESSAGE, ADD_SUCCESS);
        return resObject;
    }

    /**
     * 微博推广开通list
     */
    public ErpWeiboApi weiboList(String shopId, String storeId) {
        ErpShopInfo shop = shopInfoService.get(shopId);
        ErpStoreInfo store = erpStoreInfoService.get(storeId);
        ErpStoreAdvertiserWeibo weibo = null;
        if (null != store.getAdvertiserWeiboId() && !Constant.BLANK.equals(store.getAdvertiserWeiboId())) {
            weibo = weiboService.get(store.getAdvertiserWeiboId());
            if (weibo != null) {
                // 解密 微博登录密码
                String accountPassword = weibo.getAccountPassword();
                if (StringUtils.isNotBlank(accountPassword)) {
                    weibo.setAccountPassword(AESUtil.decrypt(accountPassword));
                }
            }
        }
        ErpWeiboApi weiboApi = new ErpWeiboApi();
        weiboApi.setShopid(shop.getId());
        weiboApi.setShopname(shop.getName());
        weiboApi.setStoreid(store.getId());
        weiboApi.setWeibo(weibo);
        return weiboApi;
    }

    /**
     * 微博推广保存
     */
    @Transactional
    public JSONObject addWeibo(String jsonObject) {
        JSONObject resObject = new JSONObject();
        String newJson = StringEscapeUtils.unescapeHtml4(jsonObject);
        ErpWeiboApi e = JSON.parseObject(newJson, new TypeReference<ErpWeiboApi>() {});
        ErpStoreInfo store = erpStoreInfoService.get(e.getStoreid());
        ErpStoreAdvertiserWeibo weibo;
        if (null != e.getWeibo()) {
            if (null != e.getWeibo().getId() && !Constant.BLANK.equals(e.getWeibo().getId())) {
                weibo = weiboService.get(e.getWeibo().getId());
            } else {
                weibo = new ErpStoreAdvertiserWeibo();
            }
        } else {
            weibo = new ErpStoreAdvertiserWeibo();
        }
        // 如果已经审核通过，检查是否有高级修改权限
        checkStoreAdvancedEditPerm(StoreConstant.ADVERTISER_AUDIT_STATUS_PASS == weibo.getAuditStatus());
        weibo.setAccountType(e.getWeibo().getAccountType());
        weibo.setAccountNo(e.getWeibo().getAccountNo());
        weibo.setAccountPassword(AESUtil.encrypt(e.getWeibo().getAccountPassword()));
        weibo.setUid(e.getWeibo().getUid());
        weibo.setNickName(e.getWeibo().getNickName());
        weibo.setRelationProveLetter(e.getWeibo().getRelationProveLetter());
        weibo.setAdvAuthLetter(e.getWeibo().getAdvAuthLetter());
        weibo.setPromotePromiseLetter(e.getWeibo().getPromotePromiseLetter());
        weibo.setOpenOrTrans(e.getWeibo().getOpenOrTrans());

        weiboService.save(weibo);
        store.setAdvertiserWeiboId(weibo.getId());
        erpStoreInfoService.save(store);

        resObject.put(RESULT, true);
        resObject.put(MESSAGE, ADD_SUCCESS);
        return resObject;
    }

    /**
     * 陌陌推广开通list
     */
    public ErpMomoApi momoList(String shopId, String storeId) {
        ErpShopInfo shop = shopInfoService.get(shopId);
        ErpStoreInfo store = erpStoreInfoService.get(storeId);
        ErpStoreAdvertiserMomo momo = null;
        if (null != store.getAdvertiserMomoId() && !Constant.BLANK.equals(store.getAdvertiserMomoId())) {
            momo = momoService.get(store.getAdvertiserMomoId());
        }
        ErpMomoApi momoApi = new ErpMomoApi();
        momoApi.setShopid(shop.getId());
        momoApi.setShopname(shop.getName());
        momoApi.setStoreid(store.getId());
        momoApi.setMomo(momo);
        return momoApi;
    }

    /**
     * 陌陌审核list
     */
    public List<ErpMomoCheckApi> momoCheckList(String shopId) {
        List<ErpStoreInfo> list = erpStoreInfoService.findmomoaudit(shopId);
        ErpMomoCheckApi momo;
        ErpStoreAdvertiserMomo momoDB;
        List<ErpMomoCheckApi> apilist = new ArrayList();
        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                momo = new ErpMomoCheckApi();
                momo.setStoreId(list.get(i).getId());
                momo.setStoreName(list.get(i).getShortName());
                momoDB = list.get(i).getMomo();
                if (momoDB != null) {
                    momo.setMomoNum(momoDB.getAccountNo());
                    momo.setAuditState(momoDB.getAuditStatus());
                    momo.setAuditContent(momoDB.getAuditContent());
                }
                apilist.add(momo);
            }
            return apilist;
        }
        return apilist;
    }

    /**
     * 陌陌推广保存
     */
    @Transactional
    public JSONObject addMomo(String jsonObject) {
        JSONObject resObject = new JSONObject();
        String newJson = StringEscapeUtils.unescapeHtml4(jsonObject);
        ErpMomoApi e = JSON.parseObject(newJson, new TypeReference<ErpMomoApi>() {});
        ErpStoreInfo store = erpStoreInfoService.get(e.getStoreid());
        ErpStoreAdvertiserMomo momo;
        if (null != e.getMomo()) {
            if (null != e.getMomo().getId() && !Constant.BLANK.equals(e.getMomo().getId())) {
                momo = momoService.get(e.getMomo().getId());
            } else {
                momo = new ErpStoreAdvertiserMomo();
            }
        } else {
            momo = new ErpStoreAdvertiserMomo();
        }
        // 如果已经审核通过，检查是否有高级修改权限
        checkStoreAdvancedEditPerm(StoreConstant.ADVERTISER_AUDIT_STATUS_PASS == momo.getAuditStatus());
        momo.setAccountNo(e.getMomo().getAccountNo());
        momo.setBrandName(e.getMomo().getBrandName());
        momo.setIcp(e.getMomo().getIcp());
        momo.setFollowZhangbeiScreenshot(e.getMomo().getFollowZhangbeiScreenshot());
        momoService.save(momo);
        store.setAdvertiserMomoId(momo.getId());
        erpStoreInfoService.save(store);

        resObject.put(RESULT, true);
        resObject.put(MESSAGE, ADD_SUCCESS);
        return resObject;
    }

    /**
     * 推广图片素材list
     */
    public ErpPhotoApi photoList(String shopId, String storeId) {
        ErpShopInfo shop = shopInfoService.get(shopId);
        ErpStoreInfo store = erpStoreInfoService.get(storeId);
        ErpStorePromotePhotoMaterial photo = promotePhotoMaterialService.findlistWhereStoreId(Global.NO, storeId);
        if (null == photo) {
            photo = new ErpStorePromotePhotoMaterial();
        }

        ErpPhotoApi photoApi = new ErpPhotoApi();
        photoApi.setShopid(shop.getId());
        photoApi.setShopname(shop.getName());
        photoApi.setStoreid(store.getId());
        photoApi.setPhoto(photo);
        return photoApi;
    }

    /**
     * 推广图片素材保存
     */
    @Transactional
    public JSONObject addPhoto(String jsonObject) {
        JSONObject resObject = new JSONObject();
        String newJson = StringEscapeUtils.unescapeHtml4(jsonObject);
        ErpPhotoApi e = JSON.parseObject(newJson, new TypeReference<ErpPhotoApi>() {});
        // 根据门店id 获取推广图片素材
        ErpStorePromotePhotoMaterial photo = promotePhotoMaterialService.findlistWhereStoreId(Global.NO, e.getStoreid());
        if (photo == null) {
            photo = new ErpStorePromotePhotoMaterial();
        }
        photo.setEnvironmentPhoto(e.getPhoto().getEnvironmentPhoto());
        photo.setProductPhoto(e.getPhoto().getProductPhoto());
        photo.setEnvironmentPhotoCount(e.getPhoto().getEnvironmentPhotoCount());
        photo.setProductPhotoCount(e.getPhoto().getProductPhotoCount());
        photo.setMenuPhoto(e.getPhoto().getMenuPhoto());
        photo.setMenuPhotoCount(e.getPhoto().getMenuPhotoCount());
        photo.setStoreInfoId(e.getStoreid());
        jykFlowAccountSignalService.uploadPPromotionalPictures(e.getStoreid());
        promotePhotoMaterialService.save(photo);
        resObject.put(RESULT, true);
        resObject.put(MESSAGE, ADD_SUCCESS);
        return resObject;
    }

    /**
     * 掌贝进件设置主店
     */
    @Transactional
    public JSONObject setMainStore(String shopId, String storeId) {
        JSONObject resObject = new JSONObject();
        erpStoreInfoService.updateWhereShopId(shopId);
        erpStoreInfoService.updateWhereStoreId(storeId);
        resObject.put(RESULT, true);
        resObject.put(MESSAGE, SET_MAIN_STORE_SUCCESS);
        return resObject;
    }

}
