package com.yunnex.ops.erp.modules.shop.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.HttpGetUploadUtil;
import com.yunnex.ops.erp.common.utils.HttpUtil;
import com.yunnex.ops.erp.common.utils.UploadUtil;
import com.yunnex.ops.erp.modules.hat.dao.HatAreaDao;
import com.yunnex.ops.erp.modules.hat.dao.HatCityDao;
import com.yunnex.ops.erp.modules.hat.dao.HatProvinceDao;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.shop.constant.ShopConstant;
import com.yunnex.ops.erp.modules.shop.entity.BusinessCategory;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreCredentials;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLegalPerson;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLinkman;
import com.yunnex.ops.erp.modules.store.basic.service.BusinessScopeService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreCredentialsService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreInfoService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreLegalPersonService;
import com.yunnex.ops.erp.modules.store.basic.service.ErpStoreLinkmanService;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStoreBank;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayUnionpay;
import com.yunnex.ops.erp.modules.store.pay.entity.ErpStorePayWeixin;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStoreBankService;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayUnionpayService;
import com.yunnex.ops.erp.modules.store.pay.service.ErpStorePayWeixinService;
import com.yunnex.ops.erp.modules.sys.utils.OpenBankEnum;
import com.yunnex.ops.erp.modules.workflow.flow.service.SdiFlowSignalService;

/**
 * 与OEM商户同步Service
 * 
 * @author SunQ
 * @date 2018年4月8日
 */
@Service
public class ErpShopInfoSyncApiService {

    private final int PAGE_SIZE = 100;// NOSONAR

    private static final int STATE_NUMBER_2 = 2;

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();// NOSONAR

    @Autowired
    protected ErpShopInfoService erpShopInfoService;
    @Autowired
    protected ErpStoreLinkmanService erpStoreLinkmanService;
    @Autowired
    protected ErpStoreLegalPersonService erpStoreLegalPersonService;
    @Autowired
    protected ErpStoreCredentialsService erpStoreCredentialsService;
    @Autowired
    protected ErpStoreInfoService erpStoreInfoService;
    @Autowired
    protected ErpStoreBankService erpStoreBankService;
    @Autowired
    protected ErpStorePayWeixinService erpStorePayWeixinService;
    @Autowired
    protected ErpStorePayUnionpayService erpStorePayUnionpayService;
    @Autowired
    protected ErpOrderOriginalInfoService erpOrderOriginalInfoService;
    @Autowired
    protected SdiFlowSignalService sdiFlowSignalService;
    @Autowired
    protected HatProvinceDao hatProvinceDao;
    @Autowired
    protected HatCityDao hatCityDao;
    @Autowired
    protected HatAreaDao hatAreaDao;
    @Autowired
    private BusinessCategoryService businessCategoryService;
    @Autowired
    protected DataSourceTransactionManager txManager;
    @Autowired
    private BusinessScopeService businessScopeService;

    @Value("${api_agent_shop_store_info_url}")
    private String API_AGENT_SHOP_STORE_INFO_URL; // NOSONAR

    private static final String UPLOAD = "/upload/";

    public static final String RES_OEM_DOMAIN = Global.getResOemDomain();
    public static final String FILES_BASE_DIR = Global.getUserfilesBaseDir();
    private static final String RES_DOMAIN = Global.getResDomain();

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpShopInfoApiService.class);

    @Transactional(readOnly = false)
    public boolean syncAbnormalAll() {// NOSONAR
        singleThreadExecutor.submit(new Runnable() {// NOSONAR
            @Override
            public void run() {
                int page = 1;
                String requestResult = "商户门店数据同步到第 {} 页时, {}";
                while (true) {
                    try {
                        JSONObject response = getShopStoreInfoFromOem(page++, PAGE_SIZE, null, null, null);
                        if (response == null) {
                            LOGGER.info(requestResult, page, "远程服务器没有响应");
                            break;
                        }
                        if (!"0000".equals(response.getString("code"))) {
                            LOGGER.info(requestResult, page, "请求出错: " + response.getString("message"));
                            break;
                        }
                        JSONArray shopStoreInfoArray = response.getJSONArray("attach");
                        if (CollectionUtils.isEmpty(shopStoreInfoArray)) {
                            LOGGER.info(requestResult, page, "同步完毕!");
                            break;
                        }
                        // 有数据，开始同步
                        LOGGER.info("有数据，开始同步");

                        // 处理数据
                        loopData(shopStoreInfoArray);
                    } catch (RuntimeException e) {
                        LOGGER.error(e.getMessage(), e);
                        break;
                    }
                }
            }
        });
        return true;
    }

    /**
     * 从OEM获取返回的数据
     *
     * @param pageNo
     * @param pageSize
     * @param startId
     * @param endId
     * @return
     * @date 2018年4月8日
     * @author SunQ
     */
    private JSONObject getShopStoreInfoFromOem(int pageNo, int pageSize, Integer startId, Integer endId, String zhangbeiId) {
        try {
            JSONObject reqObject = new JSONObject();
            reqObject.put("currentPage", pageNo);
            reqObject.put("pageSize", pageSize);
            if (startId != null)
                reqObject.put("startId", startId);
            if (endId != null)
                reqObject.put("endId", endId);
            if (StringUtils.isNotBlank(zhangbeiId)) {
                reqObject.put("userName", zhangbeiId);
            }
            LOGGER.info("同步商户门店数据请求参数：{}", reqObject);
            String resStr = HttpUtil.sendHttpPostReqToServerByReqbody(API_AGENT_SHOP_STORE_INFO_URL, reqObject.toJSONString(), "application/json");
            LOGGER.info("OEM返回数据：{}", resStr);
            if (StringUtils.isNotEmpty(resStr)) {
                return JSONObject.parseObject(resStr);
            }
        } catch (RuntimeException e) {
            LOGGER.error("同步商户门店请求出错！{}", e);
        }
        return null;
    }

    // 页内数据循环(多商户)
    private void loopData(JSONArray shopStoreInfoArray) {
        JSONObject shopStoreInfo = null;
        for (int i = 0; i < shopStoreInfoArray.size(); i++) {
            try {
                // 获得页内第 i 条数据
                shopStoreInfo = shopStoreInfoArray.getJSONObject(i);
                if (shopStoreInfo == null) {
                    continue;
                }

                String zhangbeiId = getJSONObject(shopStoreInfo, "shopAccounts").getString("username");
                ErpShopInfo erpShopInfoDB = erpShopInfoService.findListByZhangbeiId(zhangbeiId);
                // 商户存在并且OEM进件门店数量不为0，不需要同步
                if (erpShopInfoDB != null) {
                    Integer storeCon = erpStoreInfoService.countStoreForOEM(erpShopInfoDB.getId(), Global.NO);
                    if (storeCon > 0) {
                        LOGGER.info("OEM门店数大于 0, 不同步OEM门店及支付信息");
                        continue;
                    }
                }

                // 格式化商户信息
                ErpShopInfo erpShopInfo = parseData(shopStoreInfo);
                if (erpShopInfo != null) {
                    saveData(erpShopInfo, "batch");// 同步数据
                    // 门店冲突标识、无冲突覆盖操作
                    if (erpShopInfo != null && StringUtils.isNotBlank(erpShopInfo.getId())) {
                        // 获取掌贝进件主体对象
                        List<ErpStoreInfo> zhangbeiStoreInfos = erpStoreInfoService.findwhereshopidListForOEM(erpShopInfo.getId(), Global.NO,
                                        Global.YES);
                        if (!CollectionUtils.isEmpty(zhangbeiStoreInfos)) {
                            ErpStoreInfo erpStoreInfoOEM = zhangbeiStoreInfos.get(0);
                            ErpStoreInfo erpStoreInfoERP = erpStoreInfoService.findismain(erpShopInfo.getId(), Global.NO);

                            // 流程流转
                            flowSignal(erpShopInfo);
                            // 只处理审核通过
                            if (STATE_NUMBER_2 == erpStoreInfoOEM.getAuditStatus()) {
                                if (null != erpStoreInfoERP) {
                                    if (!erpStoreInfoOEM.getShortName().equals(erpStoreInfoERP.getShortName())) {
                                        erpShopInfo.setIsAbnormal(ShopConstant.whether.YES);// 如果门店简称冲突则设置冲突标识
                                        erpShopInfoService.save(erpShopInfo);
                                    } else {
                                        mainStoreUpdate(erpStoreInfoOEM, erpStoreInfoERP);// 如果门店简称一直则执行覆盖操作
                                        LOGGER.info("批量同步->OEM数据覆盖ERP主门店数据，ERP门店ID={}", erpStoreInfoERP.getId());
                                    }
                                } else {// 执行生成商户主门店操作
                                    copyOEMStoreInfoToERP(erpStoreInfoOEM);
                                    erpShopInfo.setStoreCount(erpShopInfo.getStoreCount() + 1);
                                    erpShopInfoService.save(erpShopInfo);
                                    LOGGER.info("批量同步->OEM数据生成ERP主门店数据，ERP商户ID={}", erpShopInfo.getId());
                                }
                            } else {
                                LOGGER.info("批量同步->OEM门店数据审核状态错误，不予操作，OEM门店ID={}", erpStoreInfoOEM.getId());
                            }
                        } else {
                            LOGGER.info("批量同步->商户无OEM门店信息，不予操作，ERP商户ID={}", erpShopInfo.getId());
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                LOGGER.info("商户门店页内数据循环到第 {} 条时出错：{}, 继续循环下一条数据!", (i + 1), shopStoreInfo);
            }
        }
    }

    // 页内数据循环(单商户)
    private Map<String, Object> loopData2(JSONArray shopStoreInfoArray) {
        Map<String, Object> remap = Maps.newHashMap();
        JSONObject shopStoreInfo = null;
        try {
            shopStoreInfo = shopStoreInfoArray.getJSONObject(0);
            if (shopStoreInfo == null) {
                remap.put("success", "false");
                remap.put("message", "获取数据为空");
            }

            // 格式化商户信息
            ErpShopInfo erpShopInfo = parseData(shopStoreInfo);
            if (erpShopInfo != null) {
                saveData(erpShopInfo, "single");
                remap.put("success", "true");
                remap.put("erpShopInfo", erpShopInfo);
            }
        } catch (RuntimeException e) {
            remap.put("success", "false");
            remap.put("message", "商户门店页内数据获取出错");
            LOGGER.error(e.getMessage(), e);
            LOGGER.info("商户门店页内数据获取出错：{}", shopStoreInfo);
        }
        return remap;
    }

    // 解析json数据到对象
    private ErpShopInfo parseData(JSONObject shopStoreInfo) {
        try {
            // 解析得到商户信息
            ErpShopInfo erpShopInfo = parseErpShopInfo(shopStoreInfo);

            // 解析得到门店信息
            ErpStoreInfo erpStoreInfo = parseErpStoreInfo(shopStoreInfo);

            erpShopInfo.setErpStoreInfo(erpStoreInfo);
            return erpShopInfo;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.info("商户门店json数据解析出错：{}", shopStoreInfo);
        }
        return null;
    }

    /**
     * 以商户为单位生成门店信息。 由于线程不受spring容器管理，所以手动控制事务
     */
    private void saveData(ErpShopInfo erpShopInfo, String type) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
        TransactionStatus status = txManager.getTransaction(def); // 获得事务状态
        try {
            ErpShopInfo erpShopInfoDB = erpShopInfoService.findListByZhangbeiId(erpShopInfo.getZhangbeiId());
            if (erpShopInfoDB == null) {
                LOGGER.info("新增商户数据: {}", erpShopInfo);
                erpShopInfoService.save(erpShopInfo);
            } else {
                erpShopInfo.setId(erpShopInfoDB.getId());
                erpShopInfo.setStoreCount(erpShopInfoDB.getStoreCount());
            }

            ErpStoreInfo erpStoreInfo = erpShopInfo.getErpStoreInfo();

            // 以最多的支付资料数量为准, 生成对应数量的门店
            List<ErpStorePayWeixin> erpStorePayWeixins = erpStoreInfo.getWxPays();
            List<ErpStorePayUnionpay> erpStorePayUnionpays = erpStoreInfo.getUnionPays();
            LOGGER.info("支付资料 <=> 微信支付信息: {}", erpStorePayWeixins);
            LOGGER.info("支付资料 <=> 银联支付信息: {}", erpStorePayUnionpays);

            if ("single".equals(type)) {
                // 删除之前同步的数据,使用新同步的数据替换
                erpStoreInfoService.deleteStoreInfo(erpShopInfo.getId());
            }
            Map<String, ErpStorePayWeixin> weChatPayMap = Maps.newHashMap();// 微信MAP
            Map<String, ErpStorePayUnionpay> unionPayMap = Maps.newHashMap();// 银联MAP
            List<ErpStoreInfo> storeList = Lists.newArrayList();


            if (!CollectionUtils.isEmpty(erpStorePayWeixins)) {// 使用法人身份证号+营业执照号+银行卡号组成标识并去重
                for (int i = 0; i < erpStorePayWeixins.size(); i++) {
                    weChatPayMap.put(
                                    erpStorePayWeixins.get(i).getLegalPerson().getIdCardNo() + erpStorePayWeixins.get(i).getCredentials()
                                                    .getRegisterNo() + erpStorePayWeixins.get(i).getBank().getCreditCardNo(),
                                    erpStorePayWeixins.get(i));
                }
            }
            if (!CollectionUtils.isEmpty(erpStorePayUnionpays)) {// 使用法人身份证号+营业执照号+银行卡号组成标识并去重
                for (int i = 0; i < erpStorePayUnionpays.size(); i++) {
                    unionPayMap.put(erpStorePayUnionpays.get(i).getLegalPerson().getIdCardNo() + erpStorePayUnionpays.get(i).getCredentials()
                                    .getRegisterNo() + erpStorePayUnionpays.get(i).getBank().getCreditCardNo(), erpStorePayUnionpays.get(i));
                }
            }
            boolean mainFlag = true;// 主店计数标识
            if (!CollectionUtils.isEmpty(weChatPayMap)) {// 微信处理
                for (String key : weChatPayMap.keySet()) {// NOSONAR
                    ErpStorePayWeixin weChatObj = weChatPayMap.get(key);
                    ErpStoreInfo newObj = erpStoreInfo.copy();
                    if (!StringUtils.equals(newObj.getIdCard(), weChatObj.getLegalPerson().getIdCardNo()) || !StringUtils
                                    .equals(newObj.getBusinessLicenseCode(), weChatObj.getCredentials().getRegisterNo())) {// 判断法人身份证号或者营业执照号与门店信息不同，则创建新门店信息，并且设置身份证号和营业执照号信息
                        newObj.getPerson().setIdCardNo(weChatObj.getLegalPerson().getIdCardNo());
                        newObj.getCredentials().setRegisterNo(weChatObj.getCredentials().getRegisterNo());
                    } else {
                        newObj.setIsMain(mainFlag ? 1 : 0);// 如果相同，则第一条数据则为主店数据
                        mainFlag = false;
                    }
                    newObj.setWxPay(weChatObj);
                    storeList.add(newObj);
                }
            }
            if (!CollectionUtils.isEmpty(unionPayMap)) {// 银联处理
                if (!CollectionUtils.isEmpty(storeList)) {// 微信门店数据是否为空，如果为空就直接新增银联数据，否则还需和微信数据对比
                    for (String key : unionPayMap.keySet()) {// NOSONAR
                        ErpStorePayUnionpay unionPayObj = unionPayMap.get(key);
                        ErpStoreInfo newObj = null;
                        for (ErpStoreInfo esiObj : storeList) {
                            if (StringUtils.equals(esiObj.getIdCard(), unionPayObj.getLegalPerson().getIdCardNo()) && StringUtils.equals(
                                            esiObj.getBusinessLicenseCode(),
                                            unionPayObj.getCredentials().getRegisterNo()) && esiObj.getIsMain() == 1 && esiObj
                                                            .getUnionPay() == null) {
                                // 如果是主店，并且银联信息尚未设置，就设置主店对应银联支付信息
                                esiObj.setUnionPay(unionPayObj);
                            } else {
                                newObj = erpStoreInfo.copy();
                                if (!StringUtils.equals(newObj.getIdCard(), unionPayObj.getLegalPerson().getIdCardNo()) || !StringUtils
                                                .equals(newObj.getBusinessLicenseCode(), unionPayObj.getCredentials().getRegisterNo())) {// 判断法人身份证号或者营业执照号与门店信息不同，则创建新门店信息，并且设置身份证号和营业执照号信息
                                    newObj.getPerson().setIdCardNo(unionPayObj.getLegalPerson().getIdCardNo());
                                    newObj.getCredentials().setRegisterNo(unionPayObj.getCredentials().getRegisterNo());
                                } else {
                                    newObj.setIsMain(mainFlag ? 1 : 0);// 如果相同，则第一条数据则为主店数据
                                    mainFlag = false;
                                }
                            }
                            break;
                        }
                        if (newObj != null) {
                            newObj.setUnionPay(unionPayObj);
                            storeList.add(newObj);
                        }
                    }
                } else {
                    for (String key : unionPayMap.keySet()) {// NOSONAR
                        ErpStorePayUnionpay unionPayObj = unionPayMap.get(key);
                        ErpStoreInfo newObj = erpStoreInfo.copy();
                        if (!StringUtils.equals(newObj.getIdCard(), unionPayObj.getLegalPerson().getIdCardNo()) || !StringUtils
                                        .equals(newObj.getBusinessLicenseCode(), unionPayObj.getCredentials().getRegisterNo())) {// 判断法人身份证号或者营业执照号与门店信息不同，则创建新门店信息，并且设置身份证号和营业执照号信息
                            newObj.getPerson().setIdCardNo(unionPayObj.getLegalPerson().getIdCardNo());
                            newObj.getCredentials().setRegisterNo(unionPayObj.getCredentials().getRegisterNo());
                        } else {
                            newObj.setIsMain(mainFlag ? 1 : 0);// 如果相同，则第一条数据则为主店数据
                            mainFlag = false;
                        }
                        newObj.setUnionPay(unionPayObj);
                        storeList.add(newObj);
                    }
                }
            }
            if (mainFlag) {// 主店与所有支付信息都不匹配的情况下，塞入主店
                erpStoreInfo.setIsMain(1);
                storeList.add(erpStoreInfo);
            }
            LOGGER.info("支付资料 <=> 商户信息 : {}，对应门店数据：{}", erpShopInfo.getNumber(), storeList);
            if (!CollectionUtils.isEmpty(storeList)) {// 持久化进数据库
                for (int i = 0; i < storeList.size(); i++) {
                    if (storeList.get(i).getIsMain() == 1) {// 主店判断
                        if (storeList.get(i).getWxPay() != null) {// 修改商户经营类目操作
                            ErpShopInfo shopObj = new ErpShopInfo();
                            shopObj.setId(erpShopInfo.getId());
                            BusinessCategory businessCategory = businessCategoryService
                                            .whereCategoryId(String.valueOf(storeList.get(i).getWxPay().getBusinesscategory()));
                            if (businessCategory != null) {
                                shopObj.setBusinessCategory(Integer.valueOf(businessCategory.getCategoryId().toString()));// 设置经营类目
                                shopObj.setBusinessCategoryName(businessCategory.getCategoryName());// 设置经营类目
                            }
                            erpShopInfoService.save(shopObj);
                        }
                    } else {
                        storeList.get(i).setShortName(storeList.get(i).getShortName() + String.valueOf(i));
                    }
                    erpShopInfo.setErpStoreInfo(storeList.get(i));
                    saveOne(erpShopInfo);
                }
            }
            txManager.commit(status);
        } catch (Exception e) {
            LOGGER.info("商户门店生成失败，即将回滚事务！失败原因：{}，失败对象为：{}", e, erpShopInfo);
            LOGGER.error(e.getMessage(), e);
            // 手动回滚事务，让后面的代码可以继续执行
            txManager.rollback(status);
        }

    }

    // 保存数据
    private void saveOne(ErpShopInfo erpShopInfo) {
        LOGGER.info("支付资料 <=> 保存的OEM门店信息: {}", erpShopInfo);
        // 得到门店基本信息
        ErpStoreInfo erpStoreInfo = erpShopInfo.getErpStoreInfo();
        ErpStoreLinkman erpStoreLinkman = erpStoreInfo.getStroeLinkMan();
        ErpStoreLegalPerson erpStoreLegalPerson = erpStoreInfo.getPerson();
        ErpStoreCredentials erpStoreCredentials = erpStoreInfo.getCredentials();
        ErpStorePayWeixin erpStorePayWeixin = erpStoreInfo.getWxPay();
        ErpStorePayUnionpay erpStorePayUnionpay = erpStoreInfo.getUnionPay();

        // 置空ID以执行保存操作
        initId(erpShopInfo);
        // 数据先后顺序覆盖操作
        if (erpStorePayWeixin != null) {
            erpStoreInfo.setShortName(erpStorePayWeixin.getShortName());
            erpStoreInfo.setTelephone(erpStorePayWeixin.getTelephone());
            erpStoreInfo.setCompanyUrl(erpStorePayWeixin.getCompanyUrl());
            erpStoreInfo.setBusinessType(erpStorePayWeixin.getBusinessType());
            erpStoreInfo.getStroeLinkMan().setAddress(erpStoreLinkman.getAddress());
            erpStoreLinkman = erpStorePayWeixin.getLinkman();
            erpStoreLegalPerson = erpStorePayWeixin.getLegalPerson();
            erpStorePayWeixin.getCredentials().setOrganizationCodeCertificateNo(erpStoreCredentials.getOrganizationCodeCertificateNo());
            erpStorePayWeixin.getCredentials().setOrganizationCodeCertificate(erpStoreCredentials.getOrganizationCodeCertificate());
            erpStoreCredentials = erpStorePayWeixin.getCredentials();
            if (erpStorePayUnionpay != null) {
                erpStoreLegalPerson.setIdCardInHandPhoto(erpStorePayUnionpay.getLegalPerson().getIdCardInHandPhoto());
                erpStoreCredentials.setRegisterCity(erpStorePayUnionpay.getCredentials().getRegisterCity());
            }
        } else if (erpStorePayUnionpay != null) {
            erpStoreInfo.setAddress(erpStorePayUnionpay.getAddress());
            erpStoreInfo.setBusinessType(erpStorePayUnionpay.getBusinessType());
            erpStoreLinkman.setEmail(erpStorePayUnionpay.getLinkman().getEmail());
            erpStoreLegalPerson = erpStorePayUnionpay.getLegalPerson();
            erpStorePayUnionpay.getCredentials().setBusinessScope(erpStoreCredentials.getBusinessScope());
            erpStorePayUnionpay.getCredentials().setOrganizationCodeCertificateNo(erpStoreCredentials.getOrganizationCodeCertificateNo());
            erpStorePayUnionpay.getCredentials().setOrganizationCodeCertificate(erpStoreCredentials.getOrganizationCodeCertificate());
            erpStoreCredentials = erpStorePayUnionpay.getCredentials();
        }
        // 保存微信进件数据
        if (erpStorePayWeixin != null) {
            ErpStoreBank bank = erpStorePayWeixin.getBank();
            bank.setId(null);
            erpStoreBankService.save(bank);
            erpStorePayWeixin.setBankId(bank.getId());
            erpStorePayWeixin.setId(null);
            erpStorePayWeixin.setSyncOem(ShopConstant.whether.YES);
            erpStorePayWeixinService.save(erpStorePayWeixin);
            erpStoreInfo.setWeixinPayId(erpStorePayWeixin.getId());
        }
        // 保存银联数据
        if (erpStorePayUnionpay != null) {
            ErpStoreBank bank = erpStorePayUnionpay.getBank();
            bank.setId(null);
            erpStoreBankService.save(bank);
            erpStorePayUnionpay.setBankId(bank.getId());
            erpStorePayUnionpay.setId(null);
            erpStorePayUnionpay.setSyncOem(ShopConstant.whether.YES);
            erpStorePayUnionpayService.save(erpStorePayUnionpay);
            erpStoreInfo.setUnionpayId(erpStorePayUnionpay.getId());
        }
        // 保存法人信息
        erpStoreLegalPersonService.save(erpStoreLegalPerson);
        erpStoreInfo.setLegalPersonId(erpStoreLegalPerson.getId());
        // 保存营业资质信息
        erpStoreCredentialsService.save(erpStoreCredentials);
        erpStoreInfo.setCredentialsId(erpStoreCredentials.getId());

        erpStoreInfo.setShopInfoId(erpShopInfo.getId()); // 设置门店的商户ID
        erpStoreInfo.setSyncOem(ShopConstant.whether.YES);
        erpStoreInfoService.save(erpStoreInfo);

        erpStoreLinkman.setStoreInfoId(erpStoreInfo.getId());
        erpStoreLinkmanService.save(erpStoreLinkman);

        // 判断掌贝进件主体门店是否冲突
        // 是否主店
        if ((Global.YES.toString()).equals(erpStoreInfo.getIsMain().toString())) {
            ErpStoreInfo mainStore = erpStoreInfoService.findismain(erpShopInfo.getId(), Global.NO);
            if (null != mainStore && !mainStore.getShortName().equals(erpStoreInfo.getShortName())) {
                erpShopInfo.setIsAbnormal(ShopConstant.whether.YES);
            }
        }

        // 更新门店的支付信息ID, 前面保存之后会生成ID，于是这里执行更新
        erpStoreInfoService.save(erpStoreInfo);
        // 如果门店审核通过，更新到商户的掌贝审核状态中
        if (erpStoreInfo.getAuditStatus() == 2) {
            erpShopInfo.setZhangbeiState(erpStoreInfo.getAuditStatus());
        }
        erpShopInfoService.updateStoreInfo(erpShopInfo.getZhangbeiState(), erpShopInfo.getStoreCount(), erpShopInfo.getIsAbnormal(),
                        erpShopInfo.getZhangbeiId());
    }

    /*---------------------------------------数据操作工具方法 start------------------------------------*/
    // 返回不为 null 的 JSONObject
    private static JSONObject getJSONObject(JSONObject jsonObject, String key) {
        JSONObject result = new JSONObject();
        if (jsonObject == null) {
            return result;
        }
        JSONObject jb = jsonObject.getJSONObject(key);
        if (jb != null) {
            return jb;
        }
        return result;
    }

    // 数字解析出错时返回0
    private static Integer getInteger(JSONObject jsonObject, String key) {
        if (jsonObject == null) {
            return 0;
        }
        Integer ret = 0;
        try {
            ret = jsonObject.getInteger(key);
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return ret == null ? 0 : ret;
    }

    // 下载图片
    private static String downloadFile(String urls) throws Exception {
        if (StringUtils.isBlank(urls)) {
            return "";
        }
        String result = "";
        String[] paths = urls.split(",");
        for (int i = 0; i < paths.length; i++) {// 地推进件的文件路径信息，同步后，修改了路径及文件名，会导致与OEM交互时，OEM不认ERP的路径信息
            boolean flag = HttpGetUploadUtil.saveImageToDisk(RES_OEM_DOMAIN + paths[i], FILES_BASE_DIR + UPLOAD + getDir(paths[i]),
                            getFilename(paths[i]));
            if (flag) {
                result += UPLOAD + paths[i] + ";";
                String filaPath = UPLOAD + getDir(paths[i]) + "/" + getFilename(paths[i]);
                try (InputStream inputStream = HttpGetUploadUtil.getInputStream(RES_OEM_DOMAIN + filaPath)) {
                    if (inputStream == null) {// 每次同步奖图片存储至OEM，判断图片是否存在，存在则不保存
                        JSONObject jsonObject = UploadUtil.toOEM(RES_DOMAIN + filaPath, UPLOAD + getDir(paths[i]), getFilename(paths[i]));
                        if (jsonObject == null || !"0000".equals(jsonObject.getString("code"))) {
                            LOGGER.error("保存图片至OEM失败，OEM返回 : {}", jsonObject);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        if (paths.length == 1 && StringUtils.isNotBlank(result)) {
            result = result.substring(0, result.lastIndexOf(';'));
        }
        return result;
    }

    private static String getDir(String filePath) {
        if (filePath.contains("/"))
            return filePath.substring(0, filePath.lastIndexOf('/'));
        return "";
    }

    private static String getFilename(String filePath) {
        if (filePath.contains("/"))
            return filePath.substring(filePath.lastIndexOf('/') + 1, filePath.length());
        return "";
    }

    private static ErpShopInfo parseErpShopInfo(JSONObject erpShopStoreInfo) {
        ErpShopInfo erpShopInfo = new ErpShopInfo();
        erpShopInfo.setNumber(getJSONObject(erpShopStoreInfo, "shop").getString("serial"));
        erpShopInfo.setName(getJSONObject(erpShopStoreInfo, "shop").getString("fullName"));
        erpShopInfo.setAbbreviation(getJSONObject(erpShopStoreInfo, "shop").getString("name"));
        erpShopInfo.setContactEmail(getJSONObject(erpShopStoreInfo, "shop").getString("email"));
        erpShopInfo.setLoginName(getJSONObject(erpShopStoreInfo, "shop").getString("suffixShop"));
        erpShopInfo.setIndustryType(getJSONObject(erpShopStoreInfo, "businessScope").getString("text"));
        if (null != erpShopStoreInfo) {
            erpShopInfo.setAddress(erpShopStoreInfo.getString("headAddress"));
            erpShopInfo.setContactName(erpShopStoreInfo.getString("contactName"));
            erpShopInfo.setContactPhone(erpShopStoreInfo.getString("contactPhone"));
        }
        erpShopInfo.setServiceProvider(getJSONObject(erpShopStoreInfo, "agent").getString("companyName"));
        erpShopInfo.setServiceProviderPhone(getJSONObject(erpShopStoreInfo, "agent").getString("companyPhone"));
        erpShopInfo.setAgentId(getJSONObject(erpShopStoreInfo, "agent").getInteger("id"));
        erpShopInfo.setZhangbeiId(getJSONObject(erpShopStoreInfo, "shopAccounts").getString("username"));
        erpShopInfo.setPassword(getJSONObject(erpShopStoreInfo, "shopAccounts").getString("password"));
        return erpShopInfo;
    }

    /**
     * 业务定义：解析JSON获取商户信息对象
     * 
     * @date 2018年4月27日
     * @author R/Q
     * @throws Exception
     */
    private ErpStoreInfo parseErpStoreInfo(JSONObject shopStoreInfo) throws Exception {
        ErpStoreInfo erpStoreInfo = new ErpStoreInfo();
        String province = shopStoreInfo.getString("province");
        String city = shopStoreInfo.getString("city");
        String area = shopStoreInfo.getString("area");
        erpStoreInfo.setProvince(province);
        erpStoreInfo.setCity(city);
        erpStoreInfo.setArea(area);
        erpStoreInfo.setProvinceName(getProvinceName(province));
        erpStoreInfo.setCityName(getCityName(city));
        erpStoreInfo.setAreaName(getAreaName(area));
        if (StringUtils.isNotBlank(getJSONObject(shopStoreInfo, "shop").getString("name"))) {// 门店名称
            erpStoreInfo.setShortName(getJSONObject(shopStoreInfo, "shop").getString("name"));
        } else {
            erpStoreInfo.setShortName(getJSONObject(shopStoreInfo, "shop").getString("fullName"));
        }
        erpStoreInfo.setTelephone(getJSONObject(shopStoreInfo, "shop").getString("phoneNumber"));// 手机号码
        erpStoreInfo.setAddress(shopStoreInfo.getString("headAddress"));// 地址
        String businessType = shopStoreInfo.getString("shopType");
        if (StringUtils.equals(businessType, "5")) {
            erpStoreInfo.setBusinessType(1);// 商户类型
        } else if (StringUtils.equals(businessType, "8")) {
            erpStoreInfo.setBusinessType(2);// 商户类型
        } else {
            erpStoreInfo.setBusinessType(Integer.valueOf(shopStoreInfo.getString("shopType")));// 商户类型
        }
        erpStoreInfo.setAuditStatus(getInteger(shopStoreInfo, "checkState"));// 审批状态
        JSONArray shopRefuseInfos = shopStoreInfo.getJSONArray("shopRefuseInfos");// 驳回原因
        if (shopRefuseInfos != null && !shopRefuseInfos.isEmpty()) {
            StringBuffer sbStr = new StringBuffer();
            for (int i = 0; i < shopRefuseInfos.size(); i++) {
                sbStr.append(shopRefuseInfos.getJSONObject(i).getString("text"));
                sbStr.append(";");
            }
            erpStoreInfo.setAuditContent(sbStr.toString());
        }
        erpStoreInfo.setIdCard(shopStoreInfo.getString("idCard"));// 身份证号
        erpStoreInfo.setBusinessLicenseCode(shopStoreInfo.getString("businessLicenseCode"));// 营业执照号

        erpStoreInfo.setStroeLinkMan(parseErpStoreLinkman(shopStoreInfo));// 联系人信息
        erpStoreInfo.setPerson(parseErpStoreLegalPerson(shopStoreInfo));// 法人信息
        erpStoreInfo.setCredentials(parseErpStoreCredentials(shopStoreInfo));// 营业资质信息
        erpStoreInfo.setWxPays(parseErpStorePayWeixins(shopStoreInfo));// 微信进件信息
        erpStoreInfo.setUnionPays(parseErpStorePayUnionpays(shopStoreInfo));// 银联进件信息
        return erpStoreInfo;
    }

    private String getProvinceName(String code) {
        if (StringUtils.isBlank(code))
            return "";
        return hatProvinceDao.getByCode(code);
    }

    private String getCityName(String code) {
        if (StringUtils.isBlank(code))
            return "";
        return hatCityDao.getByCode(code);
    }

    private String getAreaName(String code) {
        if (StringUtils.isBlank(code))
            return "";
        return hatAreaDao.getByCode(code);
    }

    private static ErpStoreLinkman parseErpStoreLinkman(JSONObject shopStoreInfo) {
        ErpStoreLinkman erpStoreLinkman = new ErpStoreLinkman();
        erpStoreLinkman.setName(shopStoreInfo.getString("contactName"));
        erpStoreLinkman.setPhone(shopStoreInfo.getString("contactPhone"));
        erpStoreLinkman.setAddress(shopStoreInfo.getString("address"));
        if (shopStoreInfo.getJSONObject("shop") != null) {
            erpStoreLinkman.setEmail(shopStoreInfo.getJSONObject("shop").getString("email"));
        }
        return erpStoreLinkman;
    }

    private static ErpStoreLegalPerson parseErpStoreLegalPerson(JSONObject shopStoreInfo) throws Exception {
        ErpStoreLegalPerson erpStoreLegalPerson = new ErpStoreLegalPerson();
        erpStoreLegalPerson.setName(shopStoreInfo.getString("legalName"));
        erpStoreLegalPerson.setIdCardNo(shopStoreInfo.getString("idCard"));
        erpStoreLegalPerson.setIdCardStartDate(shopStoreInfo.getDate("idCardStartDate"));
        erpStoreLegalPerson.setIdCardEndDate(shopStoreInfo.getDate("idCardEndDate"));
        erpStoreLegalPerson.setIsLongterm(erpStoreLegalPerson.getIdCardEndDate() != null ? ShopConstant.whether.NO : ShopConstant.whether.YES);
        String idCardUrl = shopStoreInfo.getString("idCardUrl");
        if (StringUtils.isNotBlank(idCardUrl)) {
            String[] idCardUrls = idCardUrl.split(",");
            if (idCardUrls.length >= 1) {// 法人身份证正面照
                erpStoreLegalPerson.setIdCardFrontPhoto(downloadFile(idCardUrls[0]));
            }
            if (idCardUrls.length >= 2) {// 法人身份证反面照
                erpStoreLegalPerson.setIdCardReversePhoto(downloadFile(idCardUrls[1]));
            }
        }
        return erpStoreLegalPerson;
    }

    private ErpStoreCredentials parseErpStoreCredentials(JSONObject shopStoreInfo) throws Exception {
        ErpStoreCredentials erpStoreCredentials = new ErpStoreCredentials();
        if (null != shopStoreInfo)// sonar问题
        {
            erpStoreCredentials.setRegisterName(getJSONObject(shopStoreInfo, "shop").getString("fullName"));
            erpStoreCredentials.setRegisterNo(shopStoreInfo.getString("businessLicenseCode"));
            erpStoreCredentials.setOrganizationCodeCertificateNo(shopStoreInfo.getString("organizationCode"));
            erpStoreCredentials.setOrganizationCodeCertificate(downloadFile(shopStoreInfo.getString("organizationUrl")));
            erpStoreCredentials.setBusinessLicence(downloadFile(shopStoreInfo.getString("businessLicenseUrl")));
            erpStoreCredentials.setBusinessScope(shopStoreInfo.getJSONObject("businessScope").getInteger("id"));
        }
        return erpStoreCredentials;
    }

    private List<ErpStorePayWeixin> parseErpStorePayWeixins(JSONObject shopStoreInfo) throws Exception {
        List<ErpStorePayWeixin> list = new ArrayList<ErpStorePayWeixin>();
        JSONArray jsonArray = shopStoreInfo.getJSONArray("shopWxpayDtos");
        if (jsonArray == null || jsonArray.isEmpty())
            return list;
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(parseErpStorePayWeixin(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    private List<ErpStorePayUnionpay> parseErpStorePayUnionpays(JSONObject shopStoreInfo) throws Exception {
        List<ErpStorePayUnionpay> list = new ArrayList<ErpStorePayUnionpay>();
        JSONArray jsonArray = shopStoreInfo.getJSONArray("shopUnionPayDtos");
        if (jsonArray == null || jsonArray.isEmpty())
            return list;
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(parseErpStorePayUnionpay(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    private ErpStorePayWeixin parseErpStorePayWeixin(JSONObject weixinPay) throws Exception {
        ErpStorePayWeixin erpStorePayWeixin = new ErpStorePayWeixin();
        // 解析银行信息
        ErpStoreBank erpStoreBank = new ErpStoreBank();
        Integer accountType = getInteger(weixinPay, "accountType");// 账户类型
        erpStoreBank.setAccountType(accountType == 0 ? 1 : accountType);
        erpStoreBank.setPayWay(0);
        Integer openBankId = getInteger(weixinPay, "openBank");// 开户银行/银行名称
        if (openBankId != 0) {
            erpStoreBank.setBankId(openBankId);
            erpStoreBank.setBankName(OpenBankEnum.get(openBankId));
        }
        erpStoreBank.setBranchBankName(weixinPay.getString("detailAddress"));// 开户支行
        erpStoreBank.setOpenAccountName(weixinPay.getString("openName"));// 开户名称
        erpStoreBank.setCreditCardNo(weixinPay.getString("bankAccount"));// 银行卡号
        erpStoreBank.setBankNo(weixinPay.getString("bankUnionNumber"));
        String province = weixinPay.getString("province");
        String city = weixinPay.getString("city");
        String area = weixinPay.getString("area");
        erpStoreBank.setProvince(province);
        erpStoreBank.setCity(city);
        erpStoreBank.setArea(area);
        erpStoreBank.setProvinceName(getProvinceName(province));
        erpStoreBank.setCityName(getCityName(city));
        erpStoreBank.setAreaName(getAreaName(area));
        // erpStoreBank.setOpenAccountLicence(downloadFile(weixinPay.getString("businessUrl")));//开户许可证或银联印鉴证
        erpStoreBank.setAuthorizeProxy(downloadFile(weixinPay.getString("settlementAuthorizationUrl")));
        erpStorePayWeixin.setBank(erpStoreBank);
        erpStorePayWeixin.setPublicAccountAppid(weixinPay.getString("appId"));
        erpStorePayWeixin.setAuditStatus(weixinPay.getInteger("verifyStatus"));
        JSONArray weixinRefuseInfos = weixinPay.getJSONArray("weixinRefuseInfos");// 驳回原因
        if (weixinRefuseInfos != null && !weixinRefuseInfos.isEmpty()) {
            StringBuffer sbStr = new StringBuffer();
            for (int i = 0; i < weixinRefuseInfos.size(); i++) {
                sbStr.append(weixinRefuseInfos.getJSONObject(i).getString("text"));
                sbStr.append(";");
            }
            erpStorePayWeixin.setAuditContent(sbStr.toString());
        }
        erpStorePayWeixin.setNumber(weixinPay.getString("id"));
        // 门店信息获取
        erpStorePayWeixin.setShortName(StringUtils.isNotBlank(weixinPay.getString("shopShortName")) ? weixinPay.getString("shopShortName") : weixinPay
                        .getString("shopName"));// 门店简称
        erpStorePayWeixin.setTelephone(weixinPay.getString("servicePhone"));// 门店电话
        erpStorePayWeixin.setCompanyUrl(weixinPay.getString("companyWebsite"));// 公司网址
        String businesscategory = weixinPay.getString("businessCategory");// 经营类目
        if (StringUtils.isNotBlank(businesscategory)) {// OEM方是一个乱序的类目集合字段，逗号分隔，erp取最大的经营类目编号
            String[] businesscategoryArray = businesscategory.split(",");
            Arrays.sort(businesscategoryArray);
            erpStorePayWeixin.setBusinesscategory(Integer.valueOf(businesscategoryArray[businesscategoryArray.length - 1]));
        }
        String businessType = weixinPay.getString("applyType");
        if (StringUtils.equals(businessType, "5")) {
            erpStorePayWeixin.setBusinessType(1);// 商户类型
        } else if (StringUtils.equals(businessType, "8")) {
            erpStorePayWeixin.setBusinessType(2);// 商户类型
        } else {
            erpStorePayWeixin.setBusinessType(Integer.valueOf(weixinPay.getString("applyType")));// 商户类型
        }
        erpStorePayWeixin.setMultiAccountApplicationForm(downloadFile(weixinPay.getString("accountApplyFormUrl")));
        // 解析联系人信息
        ErpStoreLinkman linkman = new ErpStoreLinkman();
        linkman.setName(weixinPay.getString("principal"));
        linkman.setPhone(weixinPay.getString("principalPhone"));
        linkman.setEmail(weixinPay.getString("vipEmail"));
        linkman.setAddress(weixinPay.getString("address"));
        erpStorePayWeixin.setLinkman(linkman);
        // 解析法人信息
        ErpStoreLegalPerson legalPerson = new ErpStoreLegalPerson();
        legalPerson.setName(weixinPay.getString("cerficateHolderName"));
        legalPerson.setIdCardNo(weixinPay.getString("cerficateCode"));
        legalPerson.setIdCardStartDate(weixinPay.getDate("cerficateStartDate"));
        legalPerson.setIdCardEndDate(weixinPay.getDate("cerficateEndDate"));
        legalPerson.setIsLongterm(legalPerson.getIdCardEndDate() != null ? "N" : "Y");
        String idCardUrl = weixinPay.getString("idCardUrl");
        if (StringUtils.isNotBlank(idCardUrl)) {
            String[] idCardUrls = idCardUrl.split(",");
            if (idCardUrls.length >= 1)
                legalPerson.setIdCardFrontPhoto(downloadFile(idCardUrls[0]));
            if (idCardUrls.length >= 2)
                legalPerson.setIdCardReversePhoto(downloadFile(idCardUrls[1]));
        }
        erpStorePayWeixin.setLegalPerson(legalPerson);
        // 解析营业资质信息
        ErpStoreCredentials credentials = new ErpStoreCredentials();
        credentials.setRegisterName(weixinPay.getString("shopName"));
        credentials.setRegisterNo(weixinPay.getString("businessCode"));
        credentials.setRegisterAddress(weixinPay.getString("address"));
        credentials.setBusinessScope(businessScopeService.queryIdByText(weixinPay.getString("businessScope")));// OEM方微信进件信息中经营范围只有文本，没有ID，反推
        credentials.setStartDate(weixinPay.getDate("businessCodeStartDate"));
        credentials.setEndDate(weixinPay.getDate("businessCodeEndDate"));
        credentials.setIsLongTime(credentials.getEndDate() != null ? 0 : 1);
        credentials.setBusinessLicence(downloadFile(weixinPay.getString("businessUrl")));
        if (StringUtils.isNotBlank(weixinPay.getString("businessSupply"))) {
            String[] specialCertificates = weixinPay.getString("businessSupply").split(";");
            StringBuffer sdf = new StringBuffer();
            for (String specialCertificate : specialCertificates) {
                sdf.append(downloadFile(specialCertificate));
                sdf.append(";");
            }
            credentials.setSpecialCertificate(sdf.toString());
        }
        erpStorePayWeixin.setCredentials(credentials);
        return erpStorePayWeixin;
    }

    private ErpStorePayUnionpay parseErpStorePayUnionpay(JSONObject unionPay) throws Exception {
        ErpStorePayUnionpay erpStorePayUnionpay = new ErpStorePayUnionpay();
        // 解析银行信息
        ErpStoreBank erpStoreBank = new ErpStoreBank();
        erpStoreBank.setPayWay(1);
        Integer accountType = getInteger(unionPay, "accountType");// 账户类型
        erpStoreBank.setAccountType(accountType == 0 ? 1 : accountType);
        erpStoreBank.setCreditCardNo(unionPay.getString("bankAccount"));// 银行卡号
        erpStoreBank.setOpenAccountName(unionPay.getString("openName"));// 开户名称
        Integer openBankId = getInteger(unionPay, "openBank");// 开户银行
        if (openBankId != 0) {
            erpStoreBank.setBankId(openBankId);
            erpStoreBank.setBankName(OpenBankEnum.get(openBankId));
        }
        erpStoreBank.setBranchBankName(unionPay.getString("bankDetailAddress"));// 开户支行
        erpStoreBank.setBankNo(unionPay.getString("bankUnionNumber"));// 银行联行号
        erpStoreBank.setZhangbeiBindCount(getInteger(unionPay, "deviceNum"));// 绑定掌贝设备数量
        String province = unionPay.getString("bankProvince");// 开户银行所在省份
        String city = unionPay.getString("bankCity");// 开户银行所在市区
        String area = unionPay.getString("bankArea");// 开户银行所在区县
        erpStoreBank.setProvince(province);
        erpStoreBank.setCity(city);
        erpStoreBank.setArea(area);
        erpStoreBank.setProvinceName(getProvinceName(province));
        erpStoreBank.setCityName(getCityName(city));
        erpStoreBank.setAreaName(getAreaName(area));
        erpStoreBank.setCreditCardFrontPhoto(downloadFile(unionPay.getString("bankCardFaceUrl")));// 法人银行卡正面照
        erpStoreBank.setCreditCardReversePhoto(downloadFile(unionPay.getString("bankCardBackUrl")));// 法人银行卡反面照
        erpStoreBank.setOpenAccountLicence(downloadFile(unionPay.getString("licenseUnionUrl")));// 开户许可证或银联印鉴卡
        erpStoreBank.setAuthorizeProxy(downloadFile(unionPay.getString("settlementAuthorizationUrl")));// 结算授权书
        erpStorePayUnionpay.setBank(erpStoreBank);
        // 解析银联信息
        erpStorePayUnionpay.setNumber(unionPay.getString("id"));
        erpStorePayUnionpay.setAuditStatus(unionPay.getInteger("verifyStatus"));
        erpStorePayUnionpay.setMachineToolNumber(unionPay.getString("unionMachineNo"));
        JSONArray unionRefuseInfos = unionPay.getJSONArray("unionRefuseInfos");// 驳回原因
        if (unionRefuseInfos != null && !unionRefuseInfos.isEmpty()) {
            StringBuffer sbStr = new StringBuffer();
            for (int i = 0; i < unionRefuseInfos.size(); i++) {
                sbStr.append(unionRefuseInfos.getJSONObject(i).getString("text"));
                sbStr.append(";");
            }
            erpStorePayUnionpay.setAuditContent(sbStr.toString());
        }
        erpStorePayUnionpay.setStorePhotoDoorHead(downloadFile(unionPay.getString("shopOutsideUrl")));// 门头照
        erpStorePayUnionpay.setStorePhotoCashierDesk(downloadFile(unionPay.getString("shopCashierUrl")));// 收银台照
        erpStorePayUnionpay.setStorePhotoEnvironment(downloadFile(unionPay.getString("shopInsideUrl")));// 店内环境照
        if (StringUtils.isNotBlank(unionPay.getString("remarksUrl"))) {// 银行支付三联单+补充资料
            String[] specialCertificates = unionPay.getString("remarksUrl").split(";");
            StringBuffer sdf = new StringBuffer();
            for (String specialCertificate : specialCertificates) {
                sdf.append(downloadFile(specialCertificate));
                sdf.append(";");
            }
            erpStorePayUnionpay.setAdditionalPhoto(sdf.toString());
        }
        erpStorePayUnionpay.setMultiAccountApplicationForm(downloadFile(unionPay.getString("accountApplyFormUrl")));
        // 解析联系人信息
        ErpStoreLinkman linkman = new ErpStoreLinkman();
        linkman.setName(unionPay.getString("contactName"));
        linkman.setPhone(unionPay.getString("contactPhone"));
        linkman.setEmail(unionPay.getString("contactEmail"));
        erpStorePayUnionpay.setLinkman(linkman);
        // 解析法人信息
        ErpStoreLegalPerson legalPerson = new ErpStoreLegalPerson();
        legalPerson.setName(unionPay.getString("legalName"));
        legalPerson.setIdCardNo(unionPay.getString("idCard"));
        legalPerson.setIdCardStartDate(unionPay.getDate("idCardStartDate"));
        legalPerson.setIdCardEndDate(unionPay.getDate("idCardEndDate"));
        legalPerson.setIsLongterm(legalPerson.getIdCardEndDate() != null ? "0" : "1");
        legalPerson.setIdCardFrontPhoto(downloadFile(unionPay.getString("idCardFaceUrl")));
        legalPerson.setIdCardReversePhoto(downloadFile(unionPay.getString("idCardBackUrl")));
        legalPerson.setIdCardInHandPhoto(downloadFile(unionPay.getString("idCardHandUrl")));
        erpStorePayUnionpay.setLegalPerson(legalPerson);
        // 解析营业资质信息
        ErpStoreCredentials credentials = new ErpStoreCredentials();
        credentials.setRegisterName(unionPay.getString("businessLicenseFullname"));
        credentials.setRegisterNo(unionPay.getString("businessLicenseCode"));
        credentials.setRegisterCity(unionPay.getString("shopCity"));
        credentials.setRegisterAddress(unionPay.getString("businessLicenseAddress"));
        credentials.setStartDate(unionPay.getDate("businessCodeStartDate"));
        credentials.setEndDate(unionPay.getDate("businessCodeEndDate"));
        credentials.setIsLongTime(credentials.getEndDate() != null ? 0 : 1);
        credentials.setBusinessLicence(downloadFile(unionPay.getString("businessUrl")));
        erpStorePayUnionpay.setCredentials(credentials);
        return erpStorePayUnionpay;
    }

    private static void initId(ErpShopInfo erpShopInfo) {
        ErpStoreInfo erpStoreInfo = erpShopInfo.getErpStoreInfo();
        erpStoreInfo.setId(null);
        erpStoreInfo.setLegalPersonId(null);
        erpStoreInfo.setCredentialsId(null);
        erpStoreInfo.setWeixinPayId(null);
        erpStoreInfo.setUnionpayId(null);

        erpStoreInfo.getPerson().setId(null);
        erpStoreInfo.getCredentials().setId(null);
        erpStoreInfo.getStroeLinkMan().setId(null);
    }
    /*---------------------------------------数据操作工具方法 end------------------------------------*/

    /**
     * 通过掌贝ID同步商户门店信息
     *
     * @param zhangbeiId
     * @return
     * @date 2018年4月2日
     * @author SunQ
     */
    @Transactional(readOnly = false)
    public BaseResult syncAbnormal(String zhangbeiId) {
        BaseResult res = new BaseResult();
        Map<String, Object> remap = Maps.newHashMap();
        try {
            String requestResult = "商户门店数据同步操作, {}";
            JSONObject response = getShopStoreInfoFromOem(1, PAGE_SIZE, null, null, zhangbeiId);
            if (null != response) // 修改sonar问题
            {
                if (!"0000".equals(response.getString("code"))) {
                    LOGGER.info(requestResult, "请求出错: " + response.getString("message"));
                }
                JSONArray shopStoreInfoArray = response.getJSONArray("attach");
                if (!CollectionUtils.isEmpty(shopStoreInfoArray)) {
                    // 有数据，开始同步
                    LOGGER.info("有数据，开始处理");
                    // 处理数据
                    Map<String, Object> map = loopData2(shopStoreInfoArray);
                    if (!map.isEmpty() && "true".equals(map.get("success").toString())) {
                        ErpShopInfo erpShopInfo = (ErpShopInfo) map.get("erpShopInfo");
                        // 获取掌贝进件主体对象
                        List<ErpStoreInfo> zhangbeiStoreInfos = erpStoreInfoService.findwhereshopidListForOEM(erpShopInfo.getId(), Global.NO,
                                        Global.YES);
                        if (!CollectionUtils.isEmpty(zhangbeiStoreInfos)) {
                            ErpStoreInfo erpStoreInfoOEM = zhangbeiStoreInfos.get(0);
                            ErpStoreInfo erpStoreInfoERP = erpStoreInfoService.findismain(erpShopInfo.getId(), Global.NO);

                            // 流程流转
                            flowSignal(erpShopInfo);
                            // 只处理审核通过
                            if (STATE_NUMBER_2 == erpStoreInfoOEM.getAuditStatus()) {
                                if (null != erpStoreInfoERP) {
                                    if (!erpStoreInfoOEM.getShortName().equals(erpStoreInfoERP.getShortName())) {
                                        // 主店冲突
                                        remap.put("status", "conflict");
                                        remap.put("message", "主店冲突");
                                        erpShopInfo.setIsAbnormal(ShopConstant.whether.YES);
                                        erpShopInfoService.save(erpShopInfo);
                                    } else {
                                        mainStoreUpdate(erpStoreInfoOEM, erpStoreInfoERP);
                                        remap.put("status", "success");
                                        remap.put("message", "同步成功");
                                    }
                                } else {
                                    copyOEMStoreInfoToERP(erpStoreInfoOEM);
                                    erpShopInfo.setStoreCount(erpShopInfo.getStoreCount() + 1);
                                    erpShopInfoService.save(erpShopInfo);
                                    remap.put("status", "success");
                                    remap.put("message", "同步成功");
                                }
                            } else {
                                remap.put("status", "fail");
                                remap.put("message", "门店不是审核通过状态");
                            }
                        } else {
                            remap.put("status", "fail");
                            remap.put("message", "没有获取到掌贝进件主体");
                        }
                    }
                } else {
                    LOGGER.error("同步失败，从OEM没有获取到商户门店数据！");
                    remap.put("status", "fail");
                    remap.put("message", "同步失败，从OEM没有获取到商户门店数据");
                }
            } else {
                LOGGER.error("同步失败，远程服务器没有响应！response=null");
                remap.put("status", "fail");
                remap.put("message", "同步失败，远程服务器没有响应，response=null");
            }
        } catch (Exception e) {
            LOGGER.error("商户{}同步失败", zhangbeiId, e);
            remap.put("status", "fail");
            remap.put("message", "同步失败");
        }

        if (MapUtils.isEmpty(remap)) {
            remap.put("status", "fail");
            remap.put("message", "同步失败");
        }

        res.setAttach(remap);
        return res;
    }

    /**
     * 流程触发向下执行
     *
     * @return
     * @date 2018年4月8日
     * @author SunQ
     */
    private boolean flowSignal(ErpShopInfo erpShopInfo) {
        // 更新掌贝进件状态
        List<ErpStoreInfo> zhangbeiStoreInfos = erpStoreInfoService.findwhereshopidListForOEM(erpShopInfo.getId(), Global.NO, Global.YES);
        if (!CollectionUtils.isEmpty(zhangbeiStoreInfos)) {
            ErpStoreInfo erpStoreInfo = zhangbeiStoreInfos.get(0);
            if (STATE_NUMBER_2 == erpStoreInfo.getAuditStatus()) {
                sdiFlowSignalService.applyZhangbeiIntopiece(erpShopInfo.getZhangbeiId());
            }
        }

        // 更新微信支付进件状态
        List<ErpStoreInfo> wechatStoreInfos = erpStoreInfoService.findwxpayauditForOEM(erpShopInfo.getId(), Global.NO);
        if (!CollectionUtils.isEmpty(wechatStoreInfos)) {
            for (ErpStoreInfo wechatStoreInfo : wechatStoreInfos) {
                ErpStorePayWeixin weixin = wechatStoreInfo.getWxPay();
                if (null != weixin && STATE_NUMBER_2 == weixin.getAuditStatus()) {
                    sdiFlowSignalService.wechatPayIntopiece(erpShopInfo.getZhangbeiId(), wechatStoreInfo.getCredentials().getRegisterNo(),
                                    weixin.getBank().getCreditCardNo());// 跳过微信支付流程节点
                    sdiFlowSignalService.applyWechatPayIntopiece(erpShopInfo.getZhangbeiId());// 跳过商户资料录入流程中微信支付进件节点
                }

                if (null != weixin) {
                    List<ErpStorePayWeixin> wxpaylist = erpStorePayWeixinService.findwxpayaudit(erpShopInfo.getId(),
                                    wechatStoreInfo.getCredentials().getRegisterNo(), weixin.getBank().getCreditCardNo());
                    if (!CollectionUtils.isEmpty(wxpaylist)) {
                        ErpStorePayWeixin wxpay = null;
                        for (int i = 0; i < wxpaylist.size(); i++) {
                            wxpay = erpStorePayWeixinService.get(wxpaylist.get(i).getId());
                            wxpay.setAuditStatus(weixin.getAuditStatus());
                            if (StringUtils.isNotBlank(weixin.getAuditContent())) {
                                wxpay.setAuditContent(weixin.getAuditContent());
                            }
                            if (StringUtils.isNotBlank(wxpay.getId())) {
                                erpStorePayWeixinService.save(wxpay);
                            }
                        }
                    }
                }
            }
        }

        // 更新银联支付进件状态
        List<ErpStoreInfo> unionStoreInfos = erpStoreInfoService.findunionauditForOEM(erpShopInfo.getId(), Global.NO);
        if (!CollectionUtils.isEmpty(unionStoreInfos)) {
            for (ErpStoreInfo unionStoreInfo : unionStoreInfos) {
                ErpStorePayUnionpay union = unionStoreInfo.getUnionPay();
                if (null != union && STATE_NUMBER_2 == union.getAuditStatus()) {
                    sdiFlowSignalService.wechatPayIntopiece(erpShopInfo.getZhangbeiId(), unionStoreInfo.getCredentials().getRegisterNo(),
                                    union.getBank().getCreditCardNo());// 跳过银联支付流程节点
                    sdiFlowSignalService.applyUnionPayIntopiece(erpShopInfo.getZhangbeiId());// 跳过商户资料录入流程中银联支付进件节点
                }

                if (null != union) {
                    List<ErpStorePayUnionpay> unionlist = erpStorePayUnionpayService.findunionaudit(erpShopInfo.getId(),
                                    unionStoreInfo.getCredentials().getRegisterNo(), union.getBank().getCreditCardNo());
                    if (!CollectionUtils.isEmpty(unionlist)) {
                        ErpStorePayUnionpay unpay = null;
                        for (int i = 0; i < unionlist.size(); i++) {
                            unpay = erpStorePayUnionpayService.get(unionlist.get(i).getId());
                            unpay.setAuditStatus(union.getAuditStatus());
                            if (StringUtils.isNotBlank(union.getAuditContent())) {
                                unpay.setAuditContent(union.getAuditContent());
                            }
                            if (StringUtils.isNotBlank(unpay.getId())) {
                                erpStorePayUnionpayService.save(unpay);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 赋值OEM门店对象作为主门店并保存至EPR
     *
     * @param erpStoreInfoOEM
     * @date 2018年4月8日
     * @author SunQ
     * @throws Exception
     */
    private void copyOEMStoreInfoToERP(ErpStoreInfo erpStoreInfoOEM) throws Exception {
        ErpStoreInfo storeInfoCopy = erpStoreInfoOEM.copy();

        ErpStoreLinkman erpStoreLinkman = erpStoreLinkmanService.findWhereStoreId(Global.NO, storeInfoCopy.getId());
        ErpStoreLegalPerson erpStoreLegalPerson = erpStoreLegalPersonService.get(storeInfoCopy.getLegalPersonId());
        ErpStoreCredentials erpStoreCredentials = erpStoreCredentialsService.get(storeInfoCopy.getCredentialsId());
        // 联系人
        if (erpStoreLinkman != null) {
            erpStoreLinkman.setId(null);
            erpStoreLinkman.setStoreInfoId(null);
        }
        // 法人
        if (erpStoreLegalPerson != null) {
            erpStoreLegalPerson.setId(null);
            erpStoreLegalPersonService.save(erpStoreLegalPerson);
            storeInfoCopy.setLegalPersonId(erpStoreLegalPerson.getId());
        }
        // 营业资质
        if (erpStoreCredentials != null) {
            erpStoreCredentials.setId(null);
            erpStoreCredentialsService.save(erpStoreCredentials);
            storeInfoCopy.setCredentialsId(erpStoreCredentials.getId());
        }

        ErpStorePayWeixin erpStorePayWeixin = erpStorePayWeixinService.get(storeInfoCopy.getWeixinPayId());
        if (null != erpStorePayWeixin) {
            erpStorePayWeixin.setId(null);
            ErpStoreBank erpStoreBankWx = erpStoreBankService.get(erpStorePayWeixin.getBankId());
            if (null != erpStoreBankWx) {
                erpStoreBankWx.setId(null);
                erpStoreBankService.save(erpStoreBankWx);
                erpStorePayWeixin.setBankId(erpStoreBankWx.getId());
            }
            erpStorePayWeixin.setSyncOem(ShopConstant.whether.NO);
            erpStorePayWeixinService.save(erpStorePayWeixin);
            storeInfoCopy.setWeixinPayId(erpStorePayWeixin.getId());
        }

        ErpStorePayUnionpay erpStorePayUnionpay = erpStorePayUnionpayService.get(storeInfoCopy.getWeixinPayId());
        if (null != erpStorePayUnionpay) {
            erpStorePayUnionpay.setId(null);
            ErpStoreBank erpStoreBankUnion = erpStoreBankService.get(erpStorePayUnionpay.getBankId());
            if (null != erpStoreBankUnion) {
                erpStoreBankUnion.setId(null);
                erpStoreBankService.save(erpStoreBankUnion);
                erpStorePayUnionpay.setBankId(erpStoreBankUnion.getId());
            }
            erpStorePayUnionpay.setSyncOem(ShopConstant.whether.NO);
            erpStorePayUnionpayService.save(erpStorePayUnionpay);
            storeInfoCopy.setUnionpayId(erpStorePayUnionpay.getId());
        }

        storeInfoCopy.setId(null);
        storeInfoCopy.setIsMain(1);
        storeInfoCopy.setSyncOem(ShopConstant.whether.NO);
        erpStoreInfoService.save(storeInfoCopy);
        if (null != erpStoreLinkman) {
            erpStoreLinkman.setStoreInfoId(storeInfoCopy.getId());
            erpStoreLinkmanService.save(erpStoreLinkman);
        }
    }

    /**
     * 覆盖主店信息
     *
     * @param erpStoreInfoOEM
     * @param erpStoreInfoERP
     * @return
     * @date 2018年4月8日
     * @author SunQ
     */
    private boolean mainStoreUpdate(ErpStoreInfo erpStoreInfoOEM, ErpStoreInfo erpStoreInfoERP) {

        try {
            ErpStoreLinkman erpStoreLinkmanOEM = erpStoreLinkmanService.findWhereStoreId(Global.NO, erpStoreInfoOEM.getId());
            ErpStoreLegalPerson erpStoreLegalPersonOEM = erpStoreLegalPersonService.get(erpStoreInfoOEM.getLegalPersonId());
            ErpStoreCredentials erpStoreCredentialsOEM = erpStoreCredentialsService.get(erpStoreInfoOEM.getCredentialsId());
            ErpStorePayWeixin erpStorePayWeixinOEM = erpStorePayWeixinService.get(erpStoreInfoOEM.getWeixinPayId());
            ErpStoreBank erpStoreBankWxOEM = null;
            if (null != erpStorePayWeixinOEM) {
                erpStoreBankWxOEM = erpStoreBankService.get(erpStorePayWeixinOEM.getBankId());
            }
            ErpStorePayUnionpay erpStorePayUnionpayOEM = erpStorePayUnionpayService.get(erpStoreInfoOEM.getWeixinPayId());
            ErpStoreBank erpStoreBankUnionOEM = null;
            if (null != erpStorePayUnionpayOEM) {
                erpStoreBankUnionOEM = erpStoreBankService.get(erpStorePayUnionpayOEM.getBankId());
            }

            ErpStoreLinkman erpStoreLinkmanERP = erpStoreLinkmanService.findWhereStoreId(Global.NO, erpStoreInfoERP.getId());
            ErpStoreLegalPerson erpStoreLegalPersonERP = erpStoreLegalPersonService.get(erpStoreInfoERP.getLegalPersonId());
            ErpStoreCredentials erpStoreCredentialsERP = erpStoreCredentialsService.get(erpStoreInfoERP.getCredentialsId());
            ErpStorePayWeixin erpStorePayWeixinERP = erpStorePayWeixinService.get(erpStoreInfoERP.getWeixinPayId());
            ErpStoreBank erpStoreBankWxERP = null;
            if (null != erpStorePayWeixinERP) {
                erpStoreBankWxERP = erpStoreBankService.get(erpStorePayWeixinERP.getBankId());
            }
            ErpStorePayUnionpay erpStorePayUnionpayERP = erpStorePayUnionpayService.get(erpStoreInfoERP.getWeixinPayId());
            ErpStoreBank erpStoreBankUnionERP = null;
            if (null != erpStorePayUnionpayERP) {
                erpStoreBankUnionERP = erpStoreBankService.get(erpStorePayUnionpayERP.getBankId());
            }
            if (erpStoreInfoERP != null) {// 门店基本信息
                erpStoreInfoOEM.setId(erpStoreInfoERP.getId());
                erpStoreInfoOEM.setSyncOem(ShopConstant.whether.NO);// 覆盖主店，同步标识设置为N
                // 联系人信息
                if (erpStoreLinkmanOEM != null) {
                    erpStoreLinkmanOEM.setId(erpStoreLinkmanERP != null ? erpStoreLinkmanERP.getId() : StringUtils.EMPTY);
                    erpStoreLinkmanOEM.setStoreInfoId(erpStoreInfoERP.getId());
                    erpStoreLinkmanService.save(erpStoreLinkmanOEM);
                }
                // 法人信息
                if (erpStoreLegalPersonOEM != null) {
                    erpStoreLegalPersonOEM.setId(erpStoreLegalPersonERP != null ? erpStoreLegalPersonERP.getId() : StringUtils.EMPTY);
                    erpStoreLegalPersonService.save(erpStoreLegalPersonOEM);
                    erpStoreInfoOEM.setLegalPersonId(erpStoreLegalPersonOEM.getId());// 设置外键
                }
                // 营业资质
                if (erpStoreCredentialsOEM != null) {
                    erpStoreCredentialsOEM.setId(erpStoreCredentialsERP != null ? erpStoreCredentialsERP.getId() : StringUtils.EMPTY);
                    erpStoreCredentialsService.save(erpStoreCredentialsOEM);
                    erpStoreInfoOEM.setCredentialsId(erpStoreCredentialsOEM.getId());// 设置外键
                }
                // 微信支付关联的银行信息
                if (erpStoreBankWxOEM != null) {
                    erpStoreBankWxOEM.setId(erpStoreBankWxERP != null ? erpStoreBankWxERP.getId() : StringUtils.EMPTY);
                    erpStoreBankService.save(erpStoreBankWxOEM);
                }
                // 微信支付信息
                if (erpStorePayWeixinOEM != null) {
                    erpStorePayWeixinOEM.setId(erpStorePayWeixinERP != null ? erpStorePayWeixinERP.getId() : StringUtils.EMPTY);
                    erpStorePayWeixinOEM.setSyncOem(ShopConstant.whether.NO);// 覆盖主店，同步标识设置为N
                    erpStorePayWeixinOEM.setBankId(erpStoreBankWxOEM != null ? erpStoreBankWxOEM.getId() : StringUtils.EMPTY);
                    erpStorePayWeixinService.save(erpStorePayWeixinOEM);
                    erpStoreInfoOEM.setWeixinPayId(erpStorePayWeixinOEM.getId());// 设置外键
                }
                // 银联支付对应银行信息
                if (erpStoreBankUnionOEM != null) {
                    erpStoreBankUnionOEM.setId(erpStoreBankUnionERP != null ? erpStoreBankUnionERP.getId() : StringUtils.EMPTY);
                    erpStoreBankService.save(erpStoreBankUnionOEM);
                }
                // 银联信息
                if (erpStorePayUnionpayOEM != null) {
                    erpStorePayUnionpayOEM.setId(erpStorePayUnionpayERP != null ? erpStorePayUnionpayERP.getId() : StringUtils.EMPTY);
                    erpStorePayUnionpayOEM.setBankId(erpStoreBankUnionOEM != null ? erpStoreBankUnionOEM.getId() : StringUtils.EMPTY);
                    erpStorePayUnionpayOEM.setSyncOem(ShopConstant.whether.NO);// 覆盖主店，同步标识设置为N
                    erpStorePayUnionpayService.save(erpStorePayUnionpayOEM);
                    erpStoreInfoOEM.setUnionpayId(erpStorePayUnionpayERP.getId());// 设置外键
                }
                erpStoreInfoService.save(erpStoreInfoOEM);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 覆盖主店信息(直接覆盖)
     *
     * @param zhangbeiId
     * @return
     * @date 2018年4月8日
     * @author SunQ
     */
    @Transactional(readOnly = false)
    public boolean mainStoreUpdate(String zhangbeiId) {

        try {
            ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(zhangbeiId);
            // 获取掌贝进件主体对象
            List<ErpStoreInfo> zhangbeiStoreInfos = erpStoreInfoService.findwhereshopidListForOEM(erpShopInfo.getId(), Global.NO, Global.YES);
            if (!CollectionUtils.isEmpty(zhangbeiStoreInfos)) {
                ErpStoreInfo erpStoreInfoOEM = zhangbeiStoreInfos.get(0);
                ErpStoreInfo erpStoreInfoERP = erpStoreInfoService.findismain(erpShopInfo.getId(), Global.NO);
                mainStoreUpdate(erpStoreInfoOEM, erpStoreInfoERP);
                erpShopInfo.setIsAbnormal(ShopConstant.whether.NO);
                erpShopInfoService.save(erpShopInfo);
                // 流程流转
                flowSignal(erpShopInfo);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 替换主店(保留两者)
     *
     * @param zhangbeiId
     * @return
     * @date 2018年4月8日
     * @author SunQ
     */
    @Transactional(readOnly = false)
    public boolean mainStoreReplace(String zhangbeiId) {

        try {
            ErpShopInfo erpShopInfo = erpShopInfoService.findListByZhangbeiId(zhangbeiId);
            // 获取掌贝进件主体对象
            List<ErpStoreInfo> zhangbeiStoreInfos = erpStoreInfoService.findwhereshopidListForOEM(erpShopInfo.getId(), Global.NO, Global.YES);
            if (!CollectionUtils.isEmpty(zhangbeiStoreInfos)) {
                ErpStoreInfo erpStoreInfoOEM = zhangbeiStoreInfos.get(0);
                ErpStoreInfo erpStoreInfoERP = erpStoreInfoService.findismain(erpShopInfo.getId(), Global.NO);
                copyOEMStoreInfoToERP(erpStoreInfoOEM);
                erpStoreInfoERP.setIsMain(0);
                erpStoreInfoService.save(erpStoreInfoERP);
                erpShopInfo.setIsAbnormal(ShopConstant.whether.NO);
                erpShopInfo.setStoreCount(erpShopInfo.getStoreCount() + 1);
                erpShopInfoService.save(erpShopInfo);
                // 流程流转
                flowSignal(erpShopInfo);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }
}
