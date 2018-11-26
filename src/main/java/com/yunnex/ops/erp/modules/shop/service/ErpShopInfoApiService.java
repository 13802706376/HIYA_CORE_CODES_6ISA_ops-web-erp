package com.yunnex.ops.erp.modules.shop.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.ArrayUtils;
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
import com.yunnex.ops.erp.common.config.Global;
import com.yunnex.ops.erp.common.result.BaseResult;
import com.yunnex.ops.erp.common.utils.HttpGetUploadUtil;
import com.yunnex.ops.erp.common.utils.HttpUtil;
import com.yunnex.ops.erp.common.utils.ResponeUtil;
import com.yunnex.ops.erp.modules.hat.dao.HatAreaDao;
import com.yunnex.ops.erp.modules.hat.dao.HatCityDao;
import com.yunnex.ops.erp.modules.hat.dao.HatProvinceDao;
import com.yunnex.ops.erp.modules.order.service.ErpOrderOriginalInfoService;
import com.yunnex.ops.erp.modules.shop.entity.ErpShopInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreCredentials;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreInfo;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLegalPerson;
import com.yunnex.ops.erp.modules.store.basic.entity.ErpStoreLinkman;
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

@Service
public class ErpShopInfoApiService {

    private final int PAGE_SIZE = 30;// NOSONAR

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
    protected HatProvinceDao hatProvinceDao;
    @Autowired
    protected HatCityDao hatCityDao;
    @Autowired
    protected HatAreaDao hatAreaDao;
    @Autowired
    protected DataSourceTransactionManager txManager;

    @Value("${api_agent_info_url}")
    private String API_AGENT_INFO_URL;// NOSONAR

    @Value("${api_shop_password_url}")
    private String API_SHOP_PASSWORD_URL; // NOSONAR

    @Value("${api_agent_shop_store_info_url}")
    private String API_AGENT_SHOP_STORE_INFO_URL; // NOSONAR
    private static final String UPLOAD = "/upload/";

    public static final String RES_OEM_DOMAIN = Global.getResOemDomain();
    public static final String FILES_BASE_DIR = Global.getUserfilesBaseDir();

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpShopInfoApiService.class);

    @Transactional(readOnly = false)
    public boolean syncAll() {// NOSONAR
        singleThreadExecutor.submit(() -> {
            int page = 1;
            while (true) { // NOSONAR
                try {
                    JSONObject data = getDataFromOem(null, page++, PAGE_SIZE);// 分页获取OEM数据
                    if (ResponeUtil.isResponeValid(data)) {
                        JSONArray shops = data.getJSONArray("shops");
                        if (null != shops && !shops.isEmpty()) {
                            for (int i = 0; i < shops.size(); i++) {
                                syncOne(shops.getJSONObject(i));
                            }
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                } catch (RuntimeException e) {
                    LOGGER.error(e.getMessage(), e);
                    break;
                }
            }
        });
        return true;
    }

    /**
     * 同步单个商户，如果存在，更新，否则新增
     * 
     * @param shopJson
     */
    @Transactional
    public void syncOne(JSONObject shopJson) {
        ErpShopInfo erpShopInfo = convertFromJsonObject(shopJson);
        if (StringUtils.isEmpty(erpShopInfo.getZhangbeiId())) {// 判断数据中是否有掌贝ID，如果没有就跳过
            return;
        }
        if (erpShopInfoService.countShopByZhangbeiId(erpShopInfo.getZhangbeiId()) > 0) {// 查询erp对应掌贝ID的商户信息，如果有就修改，没有就新增
            erpShopInfo.setUpdateDate(new Date());
            erpShopInfoService.updateByZhangbeiId(erpShopInfo);
        } else {
            erpShopInfoService.save(erpShopInfo);
        }
        /* add by SunQ 由ERP提交的商户进件后,需要将商户的信息同步至订单中 */
        erpOrderOriginalInfoService.updateShopInfoByShopId(erpShopInfo.getZhangbeiId(), erpShopInfo.getName(), erpShopInfo.getAbbreviation(),
                        erpShopInfo.getNumber());
    }

    /**
     * 根据掌贝id查询是否是新商户，现在本地数据库查一遍，如果没有再去OEM拉取一遍插入数据库
     *
     * @param zhangBeiId
     * @return
     * @date 2017年10月25日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public boolean isNew(String zhangBeiId) {
        if (erpShopInfoService.countShopByZhangbeiId(zhangBeiId) > 0) {
            return false;
        }
        JSONObject data = getDataFromOem(new String[] {zhangBeiId}, 1, PAGE_SIZE);
        // 判断返回的商户信息是否存在
        if (data.containsKey("shops")) {
            JSONArray shops = data.getJSONArray("shops");
            if (null != shops && !shops.isEmpty()) {
                ErpShopInfo shopInfoFromOem = convertFromJsonObject(shops.getJSONObject(0));
                erpShopInfoService.save(shopInfoFromOem);
                /* add by SunQ 由ERP提交的商户进件后,需要将进件的信息同步至订单中 */
                erpOrderOriginalInfoService.updateShopInfoByShopId(shopInfoFromOem.getZhangbeiId(), shopInfoFromOem.getName(),
                                shopInfoFromOem.getAbbreviation(), shopInfoFromOem.getNumber());
                return false;
            }
        }

        return true;
    }

    /**
     * 根据掌贝id查询是否是新商户，现在本地数据库查一遍，如果没有再去OEM拉取一遍插入数据库
     *
     * @param zhangBeiId
     * @return
     * @date 2017年10月25日
     * @author zjq
     */
    @Transactional(readOnly = false)
    public boolean isShopInputPieces(String zhangBeiId) {
        if (erpShopInfoService.countShopByZhangbeiId(zhangBeiId) > 0) {
            return true;
        }
        JSONObject data = getDataFromOem(new String[] {zhangBeiId}, 1, PAGE_SIZE);
        JSONArray shops = data.getJSONArray("shops");
        if (null != shops && !shops.isEmpty()) {
            ErpShopInfo shopInfoFromOem = convertFromJsonObject(shops.getJSONObject(0));
            erpShopInfoService.save(shopInfoFromOem);
            return true;
        }
        return false;
    }

    public JSONObject getDataFromOem(String[] zhangBeiIds, Integer page, Integer pageSize) {
        JSONObject reqObject = new JSONObject();
        if (ArrayUtils.isEmpty(zhangBeiIds)) {
            if (null == page || null == pageSize) {
                return new JSONObject();
            }
            reqObject.put("page", page);
            reqObject.put("pageSize", pageSize);

        } else {
            reqObject.put("page", page);
            reqObject.put("pageSize", pageSize);
            reqObject.put("phoneNumbers", Arrays.asList(zhangBeiIds));
        }

        return getDataFromOem(reqObject);
    }

    public JSONObject getDataFromOem(JSONObject reqObject) {
        String resStr = HttpUtil.sendHttpPostReqToServerByReqbody(API_AGENT_INFO_URL, reqObject.toJSONString(), "application/json");
        if (StringUtils.isNotEmpty(resStr)) {
            return JSONObject.parseObject(resStr);
        }
        return new JSONObject();
    }

    private static ErpShopInfo convertFromJsonObject(JSONObject shop) {
        ErpShopInfo erpShopInfo = new ErpShopInfo();
        erpShopInfo.setNumber(shop.getString("nubmer"));
        erpShopInfo.setName(shop.getString("name"));
        erpShopInfo.setAbbreviation(shop.getString("abbreivation"));
        erpShopInfo.setIndustryType(shop.getString("industryType"));
        erpShopInfo.setAddress(shop.getString("address"));
        erpShopInfo.setContactEmail(shop.getString("contactEmail"));
        erpShopInfo.setContactName(shop.getString("contactName"));
        erpShopInfo.setContactPhone(shop.getString("contactPhone"));
        String agentId = shop.getString("agentId");
        int agentIdInt = StringUtils.isBlank(agentId) ? -1 : Integer.parseInt(agentId);
        erpShopInfo.setAgentId(agentIdInt);
        erpShopInfo.setServiceProvider(shop.getString("serviceProvider"));
        erpShopInfo.setServiceProviderPhone(shop.getString("serviceProviderPhone"));
        erpShopInfo.setZhangbeiId(shop.getString("zhangbeiId"));
        erpShopInfo.setPassword(shop.getString("password"));
        erpShopInfo.setLoginName(shop.getString("suffixShop"));
        return erpShopInfo;
    }

    /**
     * 更新本地数据库的商户密码
     *
     * @param shopId
     * @param passWord
     * @return
     * @date 2017年12月12日
     * @author SunQ
     */
    @Transactional(readOnly = false)
    public Map<String, String> syncShopPassword(String shopId, String passWord) {

        Map<String, String> reMap = new HashMap<String, String>();
        ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiID(shopId);
        // 判断商户信息是否存在
        if (shopInfo != null) {
            if (erpShopInfoService.updateShopPassword(shopId, passWord)) {
                reMap.put("state", "success");
                reMap.put("message", "密码同步成功");
            } else {
                reMap.put("state", "fail");
                reMap.put("message", "密码同步失败");
            }
        } else {
            reMap.put("state", "fail");
            reMap.put("message", "商户信息不存在");
        }
        return reMap;
    }

    @Transactional(readOnly = false)
    public Map<String, String> syncShopLoginName(String zhangbeiId, String loginName) {
        Map<String, String> reMap = new HashMap<String, String>();
        ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiID(zhangbeiId);
        // 判断商户信息是否存在
        if (shopInfo != null) {
            if (erpShopInfoService.updateShopLoginName(zhangbeiId, loginName)) {
                reMap.put("state", "success");
                reMap.put("message", "密码同步成功");
            } else {
                reMap.put("state", "fail");
                reMap.put("message", "密码同步失败");
            }
        } else {
            reMap.put("state", "fail");
            reMap.put("message", "ERP不存在此商户信息，掌贝ID：" + zhangbeiId);
        }
        return reMap;
    }

    /**
     * 更新本地数据库商户进件状态
     *
     * @param erpShopInfo
     * @return
     * @date 2017年12月12日
     * @author SunQ
     */
    @Transactional(readOnly = false)
    public Map<String, String> syncShopState(ErpShopInfo erpShopInfo) {

        Map<String, String> reMap = new HashMap<String, String>();
        ErpShopInfo shopInfo = erpShopInfoService.getByZhangbeiID(erpShopInfo.getZhangbeiId());
        // 判断商户信息是否存在
        if (shopInfo != null) {
            if (erpShopInfo.getZhangbeiState() == null) {
                erpShopInfo.setZhangbeiState(shopInfo.getZhangbeiState());
            }
            if (StringUtils.isBlank(erpShopInfo.getPassword())) {
                erpShopInfo.setPassword(shopInfo.getPassword());
            }
            if (erpShopInfo.getWechatpayState() == null) {
                erpShopInfo.setWechatpayState(shopInfo.getWechatpayState());
            }
            if (erpShopInfo.getUnionpayState() == null) {
                erpShopInfo.setUnionpayState(shopInfo.getUnionpayState());
            }

            if (erpShopInfoService.updateShopState(erpShopInfo)) {
                reMap.put("state", "success");
                reMap.put("message", "状态同步成功");
            } else {
                reMap.put("state", "fail");
                reMap.put("message", "状态同步失败");
            }
        } else {
            reMap.put("state", "fail");
            reMap.put("message", "商户信息不存在");
        }
        return reMap;
    }

    /**
     * 同步商户密码至商户后台
     *
     * @param zhangBeiId
     * @param passWord
     * @return
     * @date 2017年12月18日
     * @author SunQ
     */
    public JSONObject syncShopPasswordApi(String zhangBeiId, String passWord) {

        JSONObject reqObject = new JSONObject();

        if (StringUtils.isNotBlank(zhangBeiId) && StringUtils.isNotBlank(passWord)) {
            reqObject.put("shopId", zhangBeiId);
            reqObject.put("passWord", passWord);

            String resStr = HttpUtil.sendHttpPostReqToServerByReqbody(API_SHOP_PASSWORD_URL, reqObject.toJSONString(), "application/json");
            LOGGER.info("syncShopPassword>resStr = {}", resStr);
            if (StringUtils.isNotEmpty(resStr)) {
                return JSONObject.parseObject(resStr);
            }
        }
        return new JSONObject();
    }



    /**
     * 从OEM同步没有门店的商户和支付资料并生成对应的门店
     */
    public boolean syncShopStores() {
        singleThreadExecutor.submit(new Runnable() {
            @Override
            public void run() {
                fetchData();
            }
        });
        return true;
    }

    /**
     * 拉取远程数据
     */
    public void fetchData() {
        int page = 0;
        String requestResult = "商户门店数据同步到第 {} 页时, {}";
        // 翻页循环
        while (true) { // NOSONAR
            try {
                ++page;
                JSONObject response = getShopStoreInfoFromOem(page, PAGE_SIZE, null, null);
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
                // 页内数据循环
                loopData(shopStoreInfoArray);
            } catch (RuntimeException e) {
                LOGGER.error(e.getMessage(), e);
                break;
            }
        }
    }

    // 页内数据循环
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
                // 如果已有的商户门店数大于0，不再同步门店信息
                if (erpShopInfoDB != null && erpShopInfoDB.getStoreCount() > 0) {
                    LOGGER.info("门店数大于 0, 不同步门店及支付信息, 因为已经同步过或在小程序录入过");
                    continue;
                }

                ErpShopInfo erpShopInfo = parseData(shopStoreInfo);
                if (erpShopInfo != null) {
                    saveData(erpShopInfo);
                }
            } catch (RuntimeException e) {
                LOGGER.error(e.getMessage(), e);
                LOGGER.info("商户门店页内数据循环到第 {} 条时出错：{}, 继续循环下一条数据!", (i + 1), shopStoreInfo);
            }
        }
    }

    // 解析json数据到对象
    private ErpShopInfo parseData(JSONObject shopStoreInfo) {
        try {
            // 解析得到商户信息
            ErpShopInfo erpShopInfo = parseErpShopInfo(shopStoreInfo);

            // 解析得到门店基本信息
            ErpStoreInfo erpStoreInfo = parseErpStoreInfo(shopStoreInfo);
            ErpStoreLinkman erpStoreLinkman = parseErpStoreLinkman(shopStoreInfo);
            ErpStoreLegalPerson erpStoreLegalPerson = parseErpStoreLegalPerson(shopStoreInfo);
            ErpStoreCredentials erpStoreCredentials = parseErpStoreCredentials(shopStoreInfo);
            List<ErpStorePayWeixin> erpStorePayWeixins = parseErpStorePayWeixins(shopStoreInfo);
            List<ErpStorePayUnionpay> erpStorePayUnionpays = parseErpStorePayUnionpays(shopStoreInfo);

            // 封装数据
            erpStoreInfo.setStroeLinkMan(erpStoreLinkman);
            erpStoreInfo.setPerson(erpStoreLegalPerson);
            erpStoreInfo.setCredentials(erpStoreCredentials);
            erpStoreInfo.setWxPays(erpStorePayWeixins);
            erpStoreInfo.setUnionPays(erpStorePayUnionpays);

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
    private void saveData(ErpShopInfo erpShopInfo) {
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
            }

            ErpStoreInfo erpStoreInfo = erpShopInfo.getErpStoreInfo();

            // 以最多的支付资料数量为准, 生成对应数量的门店
            List<ErpStorePayWeixin> erpStorePayWeixins = erpStoreInfo.getWxPays();
            List<ErpStorePayUnionpay> erpStorePayUnionpays = erpStoreInfo.getUnionPays();

            int erpStorePayWeixinCount = !CollectionUtils.isEmpty(erpStorePayWeixins) ? erpStorePayWeixins.size() : 0;
            int erpStorePayUnionpayCount = !CollectionUtils.isEmpty(erpStorePayUnionpays) ? erpStorePayUnionpays.size() : 0;
            int storeCount = erpStorePayWeixinCount >= erpStorePayUnionpayCount ? erpStorePayWeixinCount : erpStorePayUnionpayCount;
            storeCount = storeCount == 0 ? 1 : storeCount;
            LOGGER.info("支付资料 <=> 门店 数量: {}", storeCount);

            for (int i = 0; i < storeCount; i++) {
                // 第一个作为总店
                if (i == 0) {
                    erpStoreInfo.setIsMain(1);
                } else {
                    erpStoreInfo.setIsMain(0);
                }
                if (i < erpStorePayWeixins.size()) {
                    erpStoreInfo.setWxPay(getWxPay(erpStorePayWeixins, i));
                } else {
                    erpStoreInfo.setWxPay(null);
                }
                if (i < erpStorePayUnionpays.size()) {
                    erpStoreInfo.setUnionPay(getUnionPay(erpStorePayUnionpays, i));
                } else {
                    erpStoreInfo.setUnionPay(null);
                }
                saveOne(erpShopInfo);
            }

            txManager.commit(status);
        } catch (RuntimeException e) {
            LOGGER.info("商户门店生成失败，即将回滚事务！失败原因：{}，失败对象为：{}", e, erpShopInfo);
            LOGGER.error(e.getMessage(), e);
            // 手动回滚事务，让后面的代码可以继续执行
            txManager.rollback(status);
        }
    }

    // 获取支付资料, 如果是第 1 个门店, 获取审核通过的
    private static ErpStorePayWeixin getWxPay(List<ErpStorePayWeixin> erpStorePayWeixins, int index) {
        if (index == 0) {
            for (int i = 0; i < erpStorePayWeixins.size(); i++) {
                if (erpStorePayWeixins.get(i).getAuditStatus() == STATE_NUMBER_2) {
                    // 把审核通过的放到第 1 个位置, 避免再次拿到
                    Collections.swap(erpStorePayWeixins, i, 0);
                    return erpStorePayWeixins.get(i);
                }
            }
        }
        return erpStorePayWeixins.get(index);
    }

    // 获取支付资料, 如果是第 1 个门店, 获取审核通过的
    private static ErpStorePayUnionpay getUnionPay(List<ErpStorePayUnionpay> erpStorePayUnionpays, int index) {
        if (index == 0) {
            for (int i = 0; i < erpStorePayUnionpays.size(); i++) {
                if (erpStorePayUnionpays.get(i).getAuditStatus() == STATE_NUMBER_2) {
                    // 把审核通过的放到第 1 个位置, 避免再次拿到
                    Collections.swap(erpStorePayUnionpays, i, 0);
                    return erpStorePayUnionpays.get(i);
                }
            }
        }
        return erpStorePayUnionpays.get(index);
    }

    // 保存数据
    private void saveOne(ErpShopInfo erpShopInfo) {
        // 得到门店基本信息
        ErpStoreInfo erpStoreInfo = erpShopInfo.getErpStoreInfo();
        ErpStoreLinkman erpStoreLinkman = erpStoreInfo.getStroeLinkMan();
        ErpStoreLegalPerson erpStoreLegalPerson = erpStoreInfo.getPerson();
        ErpStoreCredentials erpStoreCredentials = erpStoreInfo.getCredentials();

        // 置空ID以执行保存操作
        initId(erpShopInfo);
        // 补充门店信息
        String storeName = StringUtils.isNotBlank(erpShopInfo.getName()) ? erpShopInfo.getName() : erpShopInfo.getAbbreviation();
        storeName = StringUtils.isNotBlank(storeName) ? storeName : erpShopInfo.getContactPhone();
        if (erpStoreInfo.getIsMain() == 0) {
            storeName += " - 分店" + erpShopInfo.getStoreCount();
        }
        erpStoreInfo.setShortName(storeName);
        erpStoreInfo.setShopInfoId(erpShopInfo.getId()); // 设置门店的商户ID
        erpStoreInfo.setIsOldShopStore(1);

        erpStoreLegalPersonService.save(erpStoreLegalPerson);
        erpStoreCredentialsService.save(erpStoreCredentials);
        erpStoreInfo.setLegalPersonId(erpStoreLegalPerson.getId());
        erpStoreInfo.setCredentialsId(erpStoreCredentials.getId());
        erpStoreInfoService.save(erpStoreInfo);
        erpStoreLinkman.setStoreInfoId(erpStoreInfo.getId());
        erpStoreLinkmanService.save(erpStoreLinkman);

        ErpStorePayWeixin erpStorePayWeixin = erpStoreInfo.getWxPay();
        ErpStorePayUnionpay erpStorePayUnionpay = erpStoreInfo.getUnionPay();

        if (erpStorePayWeixin != null) {
            ErpStoreBank bank = erpStorePayWeixin.getBank();
            bank.setId(null);
            erpStoreBankService.save(bank);
            erpStorePayWeixin.setBankId(bank.getId());
            erpStorePayWeixin.setId(null);
            erpStorePayWeixinService.save(erpStorePayWeixin);
            erpStoreInfo.setWeixinPayId(erpStorePayWeixin.getId());
        }

        if (erpStorePayUnionpay != null) {
            ErpStoreBank bank = erpStorePayUnionpay.getBank();
            bank.setId(null);
            erpStoreBankService.save(bank);
            erpStorePayUnionpay.setBankId(bank.getId());
            erpStorePayUnionpay.setId(null);
            erpStorePayUnionpayService.save(erpStorePayUnionpay);
            erpStoreInfo.setUnionpayId(erpStorePayUnionpay.getId());
        }

        // 更新门店的支付信息ID, 前面保存之后会生成ID，于是这里执行更新
        erpStoreInfoService.save(erpStoreInfo);
        // 如果门店审核通过，更新到商户的掌贝审核状态中
        if (erpStoreInfo.getAuditStatus() == 2) {
            erpShopInfo.setZhangbeiState(erpStoreInfo.getAuditStatus());
        }
        // 更新商户的门店数量
        erpShopInfo.setStoreCount(erpShopInfo.getStoreCount() + 1);
        erpShopInfoService.updateStoreInfo(erpShopInfo.getZhangbeiState(), erpShopInfo.getStoreCount(), null, erpShopInfo.getZhangbeiId());
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
        Integer accountType = getInteger(weixinPay, "accountType");
        erpStoreBank.setAccountType(accountType == 0 ? 1 : accountType);
        erpStoreBank.setPayWay(0);
        Integer openBankId = getInteger(weixinPay, "openBank");
        if (openBankId != 0) {
            erpStoreBank.setBankId(openBankId);
            erpStoreBank.setBankName(OpenBankEnum.get(openBankId));
        }
        erpStoreBank.setBranchBankName(weixinPay.getString("detailAddress"));
        erpStoreBank.setOpenAccountName(weixinPay.getString("openName"));
        erpStoreBank.setCreditCardNo(weixinPay.getString("bankAccount"));
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
        erpStoreBank.setOpenAccountLicence(downloadFile(weixinPay.getString("licenseUnionUrl")));
        erpStoreBank.setAuthorizeProxy(downloadFile(weixinPay.getString("settlementAuthorizationUrl")));

        String idCardUrl = weixinPay.getString("idCardUrl");
        if (StringUtils.isNotBlank(idCardUrl)) {
            String[] idCardUrls = idCardUrl.split(",");
            if (idCardUrls.length >= 1)
                erpStoreBank.setCreditCardFrontPhoto(downloadFile(idCardUrls[0]));
            if (idCardUrls.length >= 2)
                erpStoreBank.setCreditCardReversePhoto(downloadFile(idCardUrls[1]));
        }

        erpStorePayWeixin.setBank(erpStoreBank);
        erpStorePayWeixin.setPublicAccountAppid(weixinPay.getString("investgamesId"));
        erpStorePayWeixin.setAuditStatus(weixinPay.getInteger("verifyStatus"));
        erpStorePayWeixin.setNumber(weixinPay.getString("id"));
        return erpStorePayWeixin;
    }

    private ErpStorePayUnionpay parseErpStorePayUnionpay(JSONObject unionPay) throws Exception {
        ErpStorePayUnionpay erpStorePayUnionpay = new ErpStorePayUnionpay();

        // 解析银行信息
        ErpStoreBank erpStoreBank = new ErpStoreBank();
        erpStoreBank.setPayWay(1);
        Integer accountType = getInteger(unionPay, "accountType");
        erpStoreBank.setAccountType(accountType == 0 ? 1 : accountType);
        Integer openBankId = getInteger(unionPay, "openBank");
        if (openBankId != 0) {
            erpStoreBank.setBankId(openBankId);
            erpStoreBank.setBankName(OpenBankEnum.get(openBankId));
        }
        erpStoreBank.setBranchBankName(unionPay.getString("detailAddress"));
        erpStoreBank.setOpenAccountName(unionPay.getString("openName"));
        erpStoreBank.setCreditCardNo(unionPay.getString("bankAccount"));
        erpStoreBank.setBankNo(unionPay.getString("bankUnionNumber"));
        erpStoreBank.setZhangbeiBindCount(getInteger(unionPay, "deviceNum"));
        String province = unionPay.getString("province");
        String city = unionPay.getString("city");
        String area = unionPay.getString("area");
        erpStoreBank.setProvince(province);
        erpStoreBank.setCity(city);
        erpStoreBank.setArea(area);
        erpStoreBank.setProvinceName(getProvinceName(province));
        erpStoreBank.setCityName(getCityName(city));
        erpStoreBank.setAreaName(getAreaName(area));
        erpStoreBank.setCreditCardFrontPhoto(downloadFile(unionPay.getString("idCardFaceUrl")));
        erpStoreBank.setCreditCardReversePhoto(downloadFile(unionPay.getString("idCardBackUrl")));
        erpStoreBank.setOpenAccountLicence(downloadFile(unionPay.getString("licenseUnionUrl")));
        erpStoreBank.setAuthorizeProxy(downloadFile(unionPay.getString("settlementAuthorizationUrl")));

        erpStorePayUnionpay.setBank(erpStoreBank);
        erpStorePayUnionpay.setAuditStatus(unionPay.getInteger("verifyStatus"));
        erpStorePayUnionpay.setStorePhotoDoorHead(downloadFile(unionPay.getString("shopOutsideUrl")));
        erpStorePayUnionpay.setStorePhotoCashierDesk(downloadFile(unionPay.getString("shopCashierUrl")));
        erpStorePayUnionpay.setStorePhotoEnvironment(downloadFile(unionPay.getString("shopInsideUrl")));
        erpStorePayUnionpay.setAdditionalPhoto(downloadFile(unionPay.getString("remarksUrl")));
        erpStorePayUnionpay.setNumber(unionPay.getString("id"));
        return erpStorePayUnionpay;
    }

    private ErpStoreCredentials parseErpStoreCredentials(JSONObject shopStoreInfo) throws Exception {
        ErpStoreCredentials erpStoreCredentials = new ErpStoreCredentials();
        erpStoreCredentials.setRegisterNo(shopStoreInfo.getString("businessLicenseCode"));
        erpStoreCredentials.setOrganizationCodeCertificateNo(shopStoreInfo.getString("organizationCode"));
        erpStoreCredentials.setOrganizationCodeCertificate(downloadFile(shopStoreInfo.getString("organizationUrl")));
        return erpStoreCredentials;
    }

    // 下载图片
    private static String downloadFile(String urls) throws Exception {
        if (StringUtils.isBlank(urls)) {
            return "";
        }
        String result = "";
        String[] paths = urls.split(",");
        for (int i = 0; i < paths.length; i++) {
            boolean flag = HttpGetUploadUtil.saveImageToDisk(RES_OEM_DOMAIN + paths[i], FILES_BASE_DIR + UPLOAD + getDir(paths[i]),
                            getFilename(paths[i]));
            if (flag) {
                result += UPLOAD + paths[i] + ";";
            }
        }
        if (paths.length == 1 && StringUtils.isNotBlank(result)) {
            result = result.substring(0, result.lastIndexOf(';'));
        }
        return result;
    }

    private static ErpStoreLegalPerson parseErpStoreLegalPerson(JSONObject shopStoreInfo) throws Exception {
        ErpStoreLegalPerson erpStoreLegalPerson = new ErpStoreLegalPerson();
        erpStoreLegalPerson.setName(shopStoreInfo.getString("legalName"));
        erpStoreLegalPerson.setIdCardNo(shopStoreInfo.getString("idCard"));
        erpStoreLegalPerson.setIdCardStartDate(shopStoreInfo.getDate("idCardStartDate"));
        erpStoreLegalPerson.setIdCardEndDate(shopStoreInfo.getDate("idCardEndDate"));
        erpStoreLegalPerson.setIsLongterm("N");
        String idCardUrl = shopStoreInfo.getString("idCardUrl");
        if (StringUtils.isNotBlank(idCardUrl)) {
            String[] idCardUrls = idCardUrl.split(",");
            if (idCardUrls.length >= 1) {
                erpStoreLegalPerson.setIdCardFrontPhoto(downloadFile(idCardUrls[0]));
            }
            if (idCardUrls.length >= 2) {
                erpStoreLegalPerson.setIdCardFrontPhoto(downloadFile(idCardUrls[1]));
            }
        }
        return erpStoreLegalPerson;
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

    private static ErpStoreLinkman parseErpStoreLinkman(JSONObject shopStoreInfo) {
        ErpStoreLinkman erpStoreLinkman = new ErpStoreLinkman();
        erpStoreLinkman.setName(shopStoreInfo.getString("contactName"));
        erpStoreLinkman.setPhone(shopStoreInfo.getString("contactPhone"));
        return erpStoreLinkman;
    }

    private ErpStoreInfo parseErpStoreInfo(JSONObject shopStoreInfo) {
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
        erpStoreInfo.setAddress(shopStoreInfo.getString("address"));
        int shopType = getInteger(shopStoreInfo, "shopType");
        erpStoreInfo.setBusinessType(shopType == 0 ? 1 : shopType);
        erpStoreInfo.setAuditStatus(getInteger(shopStoreInfo, "checkState"));
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
        erpShopInfo.setZhangbeiId(getJSONObject(erpShopStoreInfo, "shopAccounts").getString("username"));
        erpShopInfo.setPassword(getJSONObject(erpShopStoreInfo, "shopAccounts").getString("password"));
        return erpShopInfo;
    }

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

    private JSONObject getShopStoreInfoFromOem(int pageNo, int pageSize, Integer startId, Integer endId) {
        try {
            JSONObject reqObject = new JSONObject();
            reqObject.put("currentPage", pageNo);
            reqObject.put("pageSize", pageSize);
            if (startId != null)
                reqObject.put("startId", startId);
            if (endId != null)
                reqObject.put("endId", endId);
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

    public BaseResult getShopServiceInfoByNumber(String number) {
        return erpShopInfoService.getShopServiceInfoByNumber(number);
    }
}
